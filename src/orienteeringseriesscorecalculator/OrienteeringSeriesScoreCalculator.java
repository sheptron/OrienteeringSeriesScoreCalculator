/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import IofXml30.java.ObjectFactory;
import IofXml30.java.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author shep
 * 
 * All classes with name starting with X are for serializing/deserializing IOF standard XML files
 */
public class OrienteeringSeriesScoreCalculator {
    
    /*
    TODO add logging - give the user information including
    - what races were successfully processed
    - what Athletes were "merged" (ie entered under different names but deemed
        to be the same person)
    - what "assumptions" were made (eg determining the year of the series from 
        the dates in the results XML files)
    - etc
    */

    public static int currentYear = 2016;       // TODO get this from xml files
    public static final int FIRST_PLACE_SCORE = 125;  // Score for the (handicapped winner)
    /* TODO decide whether to leave ALLOWED CLASSES up to the person exporting 
    the results from OE (or equivalent). It's probably safest as we may not be
    able to determine whether a course was (eg) "Red" standard as course names
    may be "Course 1" rather than "Red 1". */
    private static final String[] ALLOWED_CLASSES = {"Orange"}; 
    
    /* 
    Modes of operation: Twilight Series, ACT League, Jim Sawkins and (single) Handicap
    Jim Sawkins and Handicap are essentially the same but differ 
        - the format of the files they produce
        - Handicap assigns points in the same way as Twilight and ACT League
    */
    public enum Mode {TWILIGHT, ACT_LEAGUE, JIM_SAWKINS, HANDICAP};
    // Human readable version for the dialog box
    private static final String[] MODES = {"Twilight Series","ACT League", "Jim Sawkins"};

    public static void main(String[] args) throws JAXBException {

        /*
         1. Get list of all results .xml files
         2. Loop through each file and
         a. Sort unique/new Athletes
         b. Get/check handicap (from YOB Factors for 2016)
         c. Get race time for each Athlete
         d. Calculate handicapped time for each Athlete
         e. Sort handicapped results and assign scores
         3. Calculate total scores for each Athlete
         4. Sort Athletes by total score
         5. Write output    
         */                 
        
        ArrayList<Athlete> overallResultList = new ArrayList<>();

        ArrayList<XEvent> eventsList = new ArrayList<>();
        
        // TODO : ask the user - Twilight Series or ACT League
        // Select mode... hard code for now
        Mode mode = Mode.HANDICAP;

        // Get file directory from user...
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        switch (mode){
            case JIM_SAWKINS: 
            case HANDICAP:
                fc.setMultiSelectionEnabled(false);
                fc.setDialogTitle("Select the XML results file...");
                break;
            default:
                fc.setMultiSelectionEnabled(true);
                fc.setDialogTitle("Select all the XML results files you want to process");
        }
        fc.setFileFilter(new FileNameExtensionFilter("OE XML Results Files","xml"));
        
        File folder;
        File[] listOfFiles;
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            switch (mode) {
            
                case JIM_SAWKINS:
                case HANDICAP:
                    // We'll only have one file in this case
                    listOfFiles = new File[1];
                    listOfFiles[0] = fc.getSelectedFile();
                    folder = fc.getSelectedFile().getParentFile();
                    break;
                default:
                    listOfFiles = fc.getSelectedFiles();            
                    folder = listOfFiles[0].getParentFile(); // Get the parent directory (assuming all selected files are in the same dir)
            }
        } else {
            InformationDialog.infoBox("No directory selected, press OK to exit.", "Warning");
            return;
        }

        // Use for debugging rather than prompting user for files 
        //File folder = new File("/home/shep/NetBeansProjects/OrienteeringSeriesScoreCalculator/src/orienteeringseriesscorecalculator/TestFiles");
        //File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());

                if (getFileExtension(listOfFiles[i]).equals("xml")) {

                    // We have an XML file so parse it in
                    String filename = listOfFiles[i].getAbsolutePath();

                    try {

                        // Check for a # in the filename and replace with a _
                        // # will cause a file not found exception in JAXB
                        if (filename.contains("#")) {
                            File oldName = new File(filename);
                            String newFilename = filename.replace("#", "_");
                            File newName = new File(newFilename);
                            if (oldName.renameTo(newName)) {
                                filename = newFilename;
                            } else {
                                throw new IOException("Unable to rename file (replace '#' with '_').");
                            }
                        }

                        JAXBContext jaxbContext = JAXBContext.newInstance(XResultList.class);
                        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                        XResultList resultList = (XResultList) jaxbUnmarshaller.unmarshal(new File(filename));

                        // Remove any not allowed classes
                        // TODO change cleanClasses to remove if oclass.Name includes "Team"
                        // TODO this should be done by the OE exporter
                        //resultList.cleanClasses(ALLOWED_CLASSES);

                        // TODO check if there actually was any courses
                        
                        // Create a list of athletes with a result for this race
                        ArrayList<Athlete> raceResultList = new ArrayList<>();

                        // Keep a record of this race
                        eventsList.add(resultList.event);

                        /*
                         Now go through resultList and build Athletes with Results
                         For each classResult we need course.length for each personResult inside
                         */
                        XEvent event = resultList.event;

                        for (int jj = 0; jj < resultList.classResult.length; jj++) {

                            int distanceInMetres = resultList.classResult[jj].course.length;

                            for (XPersonResult personResult : resultList.classResult[jj].personResult) {
                                
                                Athlete athlete = new Athlete(personResult);
                                
                                double currentHandicap = athlete.calculateHandicap(currentYear);

                                int timeInSeconds = personResult.result.timeInSeconds;

                                Result result = new Result(event, timeInSeconds, distanceInMetres, currentHandicap);

                                if(!personResult.result.status.equals("OK")){
                                    result.setStatus(false);
                                }
                                
                                athlete.addResult(result);

                                // Now add this athlete and result to array
                                raceResultList.add(athlete);
                            }
                        }
                        
                        // Assign Scores: this is where things get different depending on Mode
                        
                        // First sort by handicappedSpeed 
                        Collections.sort(raceResultList, new Comparator<Athlete>() {
                            @Override
                            public int compare(Athlete a1, Athlete a2) {
                                return a1.results.get(0).handicappedSpeed - a2.results.get(0).handicappedSpeed;
                            }
                        });

                        switch (mode) {                            
                                                        
                            case JIM_SAWKINS:
                                
                                // Assign handicapped places                    
                                for (int k = 0; k < raceResultList.size(); k++) {
                                    raceResultList.get(k).results.get(0).setHandicappedPlace(k + 1);
                                    //raceResultList.get(k).results.get(0).calculateScore();
                                }
                                
                                break;                                

                            default:                              

                                // Assign points                    
                                for (int k = 0; k < raceResultList.size(); k++) {
                                    raceResultList.get(k).results.get(0).setHandicappedPlace(k + 1);
                                    raceResultList.get(k).results.get(0).calculateScore();
                                }
                            
                            break;
                        }

                        // Now merge with previous raceResultLists                   
                        for (Athlete raceAthlete : raceResultList) {
                            if (overallResultList.contains(raceAthlete)) {

                                int k = overallResultList.indexOf(raceAthlete);
                                overallResultList.get(k).results.add(raceAthlete.results.get(0));
                            } else {
                                overallResultList.add(raceAthlete);
                            }
                        }
                    } catch (JAXBException e) {
                        // TODO Handle JAXB exceptions PER FILE
                        System.out.println("File " + listOfFiles[i].getName() + " failed to parse.");
                    } catch (IOException e) {
                        System.out.println("File " + listOfFiles[i].getName() + ": " + e.getMessage());
                    }
                }
            }
        }

        // TODO get out now if there are no races
        
        // Decide How Many Races to Include
        int totalNumberOfRaces = eventsList.size();

        int numberOfRaces = (int) Math.floor((double) totalNumberOfRaces / 2.0) + 1;

        String[] options = new String[totalNumberOfRaces];
        for (int k = 0; k < totalNumberOfRaces; k++) {
            options[k] = String.valueOf(k + 1);
        }

        if (numberOfRaces > 1) {
            String userSelection = InformationDialog.selectionBox(options, numberOfRaces - 1, "Select", "How Many Races Count?");
         
            if (userSelection != null) {
            // If the user didn't press cancel, then check what they selected
            numberOfRaces = Integer.parseInt(userSelection);
            }
        }
        
        ArrayList<Athlete> division2ResultList = new ArrayList<>(); // TODO - only needed for JIM_SAWKINS, can we declare it only if needed?
        
        switch (mode) {

            case JIM_SAWKINS:

                /*
                Results are already sorted but it needs to be divided up into Division 1 and 2
                Division 1 is everyone
                Division 2 is everyone except for M/W21-34 and the winner of Division 1
                */
                
                // Create (copy) the Division 2 Result List
                //ArrayList<Athlete> division2ResultList = new ArrayList<>();             
                
                for (Athlete athlete : overallResultList) {
                    // Add athlete if eligible
                    
                    int handicappedPlace = athlete.results.get(0).handicappedPlace;
                    int ageAtEndOfThisYear = currentYear - athlete.yearOfBirth;
                    if ((handicappedPlace > 1)
                            && ((ageAtEndOfThisYear < 21) || (ageAtEndOfThisYear > 34))) {
                        // Not the winner, and athlete is under 21, or over 34
                        division2ResultList.add(athlete);
                    }
                    
                }
                
                break;

            default:

                // We've been through all the xml files now add up each athletes scores
                for (Athlete athlete : overallResultList) {
                    athlete.totalScore(numberOfRaces);
                }

                // And sort (decreasing)
                Collections.sort(overallResultList, new Comparator<Athlete>() {
                    @Override
                    public int compare(Athlete a1, Athlete a2) {
                        return a2.totalScore - a1.totalScore;
                    }
                });
        }
        
        // Now publish
        ResultsPrinter resultsPrinter = new ResultsPrinter(eventsList, mode);
        String htmlResults = null;
        switch (mode) {
            case JIM_SAWKINS:
                resultsPrinter.writeJimSawkinsResults(overallResultList, division2ResultList);
                htmlResults = resultsPrinter.htmlResults;   // TODO fix up this, we need finaliseTable below but it won't work for JIM_SAWKINS
                break;
                
            case HANDICAP:
                
                // Produce an XML for Eventor
                
                ObjectFactory factory = new ObjectFactory();
                //ObjectFactory factory = new ObjectFactory();
                //JAXBContext context = JAXBContext.newInstance("IofXml30"); 
                
                // Event Name
                ///String event
                
                // Build Person Results
                ArrayList<PersonResult> personResults = new ArrayList<>();  
                int position = 1;
                for (Athlete athlete : overallResultList) {
                    
                    // Person 
                    PersonName personName = factory.createPersonName();
                    personName.setFamily(athlete.surname);
                    personName.setGiven(athlete.firstName);
                    
                    Person person = factory.createPerson();
                    person.setName(personName);
                    
                    person.setSex(athlete.getSex());
                    
                    
                    Id id = factory.createId();
                    id.setValue(String.valueOf(athlete.id));
                    ArrayList<Id> ids = new ArrayList<>();
                    ids.add(id);
                    person.setID(ids);
                    
                    // Organisation
                    Organisation organisation = factory.createOrganisation();
                    Id orgId = factory.createId();
                    orgId.setValue(athlete.organisation.getId());
                    organisation.setId(orgId);
                    
                    organisation.setName(athlete.organisation.getName());
                    organisation.setShortName(athlete.organisation.getShortName());
                    Country cuntry = factory.createCountry();
                    cuntry.setCode(athlete.organisation.getCountry()); // TODO XOrganisation needs an XCountry to get this to work properly!
                    cuntry.setValue(athlete.organisation.getCountry());
                    organisation.setCountry(cuntry);
                    boolean isMember = !athlete.organisation.getCode().equals("");
                    
                    
                    PersonRaceResult result = factory.createPersonRaceResult();
                    result.setTime(athlete.results.get(0).handicappedKmRate);
                    
                    // Status
                    if (athlete.results.get(0).status) {
                        result.setStatus(ResultStatus.OK);
                    }
                    else result.setStatus(ResultStatus.DISQUALIFIED);
                    
                    // Position
                    result.setPosition(BigInteger.valueOf(position));
                    
                    
                    // TODO we might need more info in here
                    ArrayList<PersonRaceResult> results = new ArrayList<>();
                    results.add(result);
                    
                    PersonResult personResult = factory.createPersonResult();
                    //personResult.setEntryId(id);
                    personResult.setPerson(person);
                    if (isMember) personResult.setOrganisation(organisation);
                    personResult.setResult(results);
                    
                    
                
                    // Finally add this person result to our list
                    personResults.add(personResult);
                    
                    position += 1;
                // Build Class
                }
                
                ClassResult classResult = factory.createClassResult();
                classResult.setPersonResult(personResults);
                ArrayList<ClassResult> classResults = new ArrayList<>();
                classResults.add(classResult);
                
                Clazz clazz = factory.createClass();
                //Id classId = factory.createId();
                //classId.setValue("1");
                //clazz.setId(classId);
                clazz.setName("Handicap");
                clazz.setShortName("Handicap");
                classResult.setClazz(clazz);
                        
                ResultList resultList = factory.createResultList();
                resultList.setClassResult(classResults);
                resultList.setIofVersion("3.0");
                resultList.setCreator("Not Andrew Blakers");
                
                Event event = factory.createEvent();
                Id eventId = factory.createId();
                eventId.setValue("1");
                
                // Event Name
                event.setName(eventsList.get(0).getName());
                // TODO might need start time
                
                resultList.setEvent(event);
                
                //JAXBElement<ResultList> element = factory. .createResultList();

                 //Marshaller marshaller = context.createMarshaller();
                 //marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
                 //marshaller.marshal(element,System.out);
                File file = new File("/home/shep/Desktop/testFile.xml");
		JAXBContext jaxbContext = JAXBContext.newInstance("IofXml30.java");
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(resultList, file);
		//jaxbMarshaller.marshal(resultList, System.out);
                
                
                break;
                
            default:
                resultsPrinter.writeResults(overallResultList);
                htmlResults = resultsPrinter.finaliseTable();
        }        
        
        if (mode != Mode.HANDICAP) {
        // Build Filename
        String outFilename;
        switch (mode) {
            case JIM_SAWKINS:
                outFilename = folder.toString() + "/" + currentYear + "_Jim_Sawkins_Handicap_Results.html";
                break;
                
            case TWILIGHT:
                outFilename = folder.toString() + "/" + currentYear + "_Twilight_Series_Results_after_Round_" + totalNumberOfRaces + "_Best_" + numberOfRaces + ".html";
                break;
                
            default:
                outFilename = folder.toString() + "/" + "null.txt";
        }
        StringToFile.write(outFilename, htmlResults);
        InformationDialog.infoBox(outFilename, "Results Written To ...");
        }
        
        
        
        /* Testing
        // Build csv
        String outstring = "Name,Sex,Year of Birth,Handicap,Race Distance (metres),Race Time (sec),Handicapped Speed (sec/km)\n";
        for (int k=0; k<overallResultList.size(); k++){
            outstring += overallResultList.get(k).firstName + " " + overallResultList.get(k).surname + ",";
            outstring += overallResultList.get(k).sex + "," + overallResultList.get(k).yearOfBirth + ",";
            outstring += overallResultList.get(k).results.get(0).handicap + ",";
            outstring += overallResultList.get(k).results.get(0).distanceInMetres + ",";
            outstring += overallResultList.get(k).results.get(0).timeInSeconds +",";
            outstring += overallResultList.get(k).results.get(0).handicappedSpeed*10 + "\n";
        }
        String filename = folder.toString() + "/" + "test.csv";
        StringToFile.write(filename, outstring);
        */
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }
}
