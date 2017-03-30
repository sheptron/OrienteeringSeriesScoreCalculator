/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import IofXml30.java.ObjectFactory;
import IofXml30.java.*;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

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

    public static final int FIRST_PLACE_SCORE = 125;  // Score for the (handicapped winner)
    public static final double PREENTRY_POINTS_FACTOR = 1.1; // Points are multiplied by this factor for preentry races
    
    /* TODO decide whether to leave ALLOWED CLASSES up to the person exporting 
    the results from OE (or equivalent). It's probably safest as we may not be
    able to determine whether a course was (eg) "Red" standard as course names
    may be "Course 1" rather than "Red 1". */
    private static final String[] ALLOWED_CLASSES = {"Orange"}; 
    
    public static final String CREATOR = "Would You Believe Not a Spreadsheet";
    
    public static final String[] OACT_CLUB_EVENTOR_IDS = {"4", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "629"};
    
    public static final String DEFAULT_ORGANISATION_ID_VALUE = "4"; // If Athlete has no organisation make them OACT
    
    /* 
    Modes of operation: Twilight Series, ACT League, Jim Sawkins and (single) Handicap
    Jim Sawkins and Handicap are essentially the same but differ 
        - the format of the files they produce
        - Handicap assigns points in the same way as Twilight and ACT League
    */
    public enum Mode {TWILIGHT, ACT_LEAGUE, JIM_SAWKINS, HANDICAP};
    // Human readable version for the dialog box
    private static final String[] MODES = {"Twilight Series","ACT League", "Jim Sawkins", "Handicap"};

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
        
        // Initialise Current Year (Used for Handicap Calcs)
        int currentYear = 2016;       // Get it from xml files (createTime)
        
        ArrayList<Athlete> overallResultList = new ArrayList<>();

        ArrayList<Event> eventsList = new ArrayList<>();
        
        // TODO : ask the user - Twilight Series or ACT League
        // Select mode... hard code for now
        Mode mode = Mode.HANDICAP;
        
        Object[] optionz = { "Twilight", "ACT League" }; // Twilight=0, ACT League = 1
        int modeSelection = JOptionPane.showOptionDialog(null, "Do you want to apply handicap to Twilight or ACT League results?", "Please Select",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                null, optionz, optionz[0]);
        mode = Mode.values()[modeSelection];

        // Get file directory from user...
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fc.setMultiSelectionEnabled(false);
        fc.setDialogTitle("Select the XML results file...");

        fc.setFileFilter(new FileNameExtensionFilter("OE XML Results Files", "xml"));

        File folder;
        File[] listOfFiles;
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            // We'll only have one file in this case
            listOfFiles = new File[1];
            listOfFiles[0] = fc.getSelectedFile();
            folder = fc.getSelectedFile().getParentFile();

        } else {
            InformationDialog.infoBox("No directory selected, press OK to exit.", "Warning");
            return;
        }

        // Use for debugging rather than prompting user for files 
        //File folder = new File("/home/shep/NetBeansProjects/OrienteeringSeriesScoreCalculator/src/orienteeringseriesscorecalculator/TestFiles");
        //File[] listOfFiles = folder.listFiles();

        // TODO we don't need a for loop any more - there's only one file
        // we can also remove any "merging" of results - like the Americans and slavery... we don't do that anymore 
        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());

                if (getFileExtension(listOfFiles[i]).equals("xml")) {

                    // We have an XML file so parse it in
                    String filename = listOfFiles[i].getAbsolutePath();

                    try {                        

                        // Check for a # in the filename and replace with a _
                        // # will cause a file not found exception in JAXB
                        filename = cleanFilenameString(filename);

                        JAXBContext jaxbContext = JAXBContext.newInstance(ResultList.class);
                        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                        ResultList resultList = (ResultList) jaxbUnmarshaller.unmarshal(new File(filename));

                        // Remove any not allowed classes
                        // TODO change cleanClasses to remove if oclass.Name includes "Team"
                        // TODO this should be done by the OE exporter
                        //resultList.cleanClasses(ALLOWED_CLASSES);

                        // TODO check if there actually was any courses
                        
                        XMLGregorianCalendar createTime = resultList.getCreateTime();
                        int createYear = createTime.getYear();                        
                        
                        // Create a list of athletes with a result for this race
                        ArrayList<Athlete> raceResultList = new ArrayList<>();
                        //resultList.event.setYear(createYear);

                        // Keep a record of this race
                        eventsList.add(resultList.getEvent());
                        
                        // TODO check the year to see if it makes sense
                        currentYear = createYear;

                        /*
                         Now go through resultList and build Athletes with Results
                         For each classResult we need course.length for each personResult inside
                         */
                        //XEvent event = resultList.event;
                        Event event = resultList.getEvent();

                        for (ClassResult classResult : resultList.getClassResult()) {
                            
                            int distanceInMetres = classResult.getCourse().get(0).getLength().intValue();
                            for (PersonResult personResult : classResult.getPersonResult()) {
                                
                                if (!personResult.getResult().get(0).getStatus().equals(ResultStatus.OK)){
                                    continue;
                                }
                                
                                // Only OACT members are eligible for ACT League                                 
                                if (mode == Mode.ACT_LEAGUE && !isEligibleClub(personResult.getOrganisation())){
                                        continue;
                                }                                    
                                
                                Athlete athlete = new Athlete(personResult);
                                
                                double currentHandicap = athlete.calculateHandicap(currentYear);   

                                int timeInSeconds = personResult.getResult().get(0).getTime().intValue();// .result.timeInSeconds;

                                Result result = new Result(event, timeInSeconds, distanceInMetres, currentHandicap);

                                if (!personResult.getResult().get(0).getStatus().equals(ResultStatus.OK)){
                                    result.setStatus(false);
                                }
                                                               
                                athlete.addResult(result);

                                // Now add this athlete and result to array
                                raceResultList.add(athlete);
                            }
                        }
                        
                        // Assign Scores: this is where things get different depending on Mode                        
                        
                        // First sort by handicappedSpeed 
                        Collections.sort(raceResultList, new athleteScoreComparator());

                        switch (mode) {                            
                                                        
                            case JIM_SAWKINS:
                                
                                // Assign handicapped places                    
                                for (int k = 0; k < raceResultList.size(); k++) {
                                    raceResultList.get(k).results.get(0).setHandicappedPlace(k + 1);
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
        int totalNumberOfRaces = 1; //eventsList.size();

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
                
                for (Athlete athlete : overallResultList) {
                    // Add athlete if eligible
                    
                    int handicappedPlace = athlete.getResults().get(0).getHandicappedPlace();
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
        
        switch (mode) {
            case JIM_SAWKINS:
                // TODO need to fix results printer to work with Event (not XEvent)
                
                //ResultsPrinter resultsPrinter = new ResultsPrinter(eventsList, mode);
                //String htmlResults = null;
                
                String htmlResults = ResultsPrinter.writeJimSawkinsResults(overallResultList, division2ResultList);
                //htmlResults = resultsPrinter.htmlResults;   // TODO fix up this, we need finaliseTable below but it won't work for JIM_SAWKINS

                // Build Filename
                String outFilename;
                outFilename = folder.toString() + "/" + currentYear + "_Jim_Sawkins_Handicap_Results.html";
                StringToFile.write(outFilename, htmlResults);
                InformationDialog.infoBox(outFilename, "Results Written To ...");
                break;
                
            default:
                
                // Produce an XML for Eventor
                
                ObjectFactory factory = new ObjectFactory();
               
                // Event Name
                
                // Event Date            
                //currentYear = eventsList.get(0).getYear();
                ///String event
                double fastestTime = overallResultList.get(0).getResults().get(0).getHandicappedKmRate();
                
                // Build Person Results
                ArrayList<PersonResult> personResults = new ArrayList<>();  
                int position = 1;
                for (Athlete athlete : overallResultList) {                    
                    
                    // Person 
                    PersonName personName = factory.createPersonName();
                    personName.setFamily(athlete.getSurname());
                    personName.setGiven(athlete.getFirstName());                    
                    Person person = factory.createPerson();
                    person.setName(personName);                    
                    person.setSex(athlete.getSex());                    
                    //Id personId = factory.createId();
                    //personId.setValue(String.valueOf(athlete.id));
                    // Only Set the ID if The Athlete has one so eventor won't cry 
                    if (!athlete.getId().getValue().equals("")) {
                        ArrayList<Id> personIds = new ArrayList<>();
                        //personIds.add(personId);
                        personIds.add(athlete.getId());
                        person.setID(personIds);
                    }
                    // Organisation
                    Organisation organisation = factory.createOrganisation();
                    Organisation athletesOrganisation = athlete.getOrganisation();
                    Id orgId = factory.createId();
                    orgId.setValue(athletesOrganisation.getId().getValue());
                    organisation.setId(orgId);
                    
                    // Organisation (club)
                    organisation.setName(athletesOrganisation.getName());
                    organisation.setShortName(athletesOrganisation.getShortName());
                    Country cuntry = factory.createCountry();
                    if (athletesOrganisation.getCountry() == null) {
                        cuntry.setCode("");
                        cuntry.setValue("");
                    } else {
                        cuntry.setCode(athletesOrganisation.getCountry().getCode()); // TODO XOrganisation needs an XCountry to get this to work properly!
                        cuntry.setValue(athletesOrganisation.getCountry().getValue());
                    }
                    organisation.setCountry(cuntry); 
                    // Don't add an empty organisation (eventor doesn't like it)
                    boolean isMember = !athletesOrganisation.getId().equals("");
                    
                    // Result
                    // Race Time (handicapped)
                    PersonRaceResult result = factory.createPersonRaceResult();
                    result.setTime(athlete.getResults().get(0).getHandicappedKmRate());
                    
                    // Status
                    if (athlete.getResults().get(0).getStatus()) {
                        result.setStatus(ResultStatus.OK);
                    }
                    else result.setStatus(ResultStatus.DISQUALIFIED);
                    
                    // TODO Time Behind
                    double timeBehind = athlete.getResults().get(0).getHandicappedKmRate() - fastestTime;
                    
                    result.setTimeBehind(timeBehind);
                    
                    // Position
                    result.setPosition(BigInteger.valueOf(position));
                                  
                    // XML needs an array of results (there will only be one result)
                    ArrayList<PersonRaceResult> results = new ArrayList<>();
                    results.add(result);
                    
                    // Build the XML PersonResult
                    PersonResult personResult = factory.createPersonResult();                    
                    personResult.setPerson(person);
                    if (isMember) personResult.setOrganisation(organisation);
                    personResult.setResult(results);
                
                    // Finally add this Person Result to our list
                    personResults.add(personResult);
                    
                    position += 1;
                // Build Class
                }
                
                // Set up the Class (course) Result (there will only be one - Handicap)
                ClassResult classResult = factory.createClassResult();
                classResult.setPersonResult(personResults);
                ArrayList<ClassResult> classResults = new ArrayList<>();
                classResults.add(classResult);
                
                Clazz clazz = factory.createClass();
                clazz.setName("Handicap");
                clazz.setShortName("Handicap");
                classResult.setClazz(clazz);                  
                        
                ResultList resultList = factory.createResultList();
                resultList.setClassResult(classResults);
                resultList.setIofVersion("3.0");
                resultList.setCreator(CREATOR);
                
                Event event = factory.createEvent();
                Id eventId = factory.createId();
                eventId.setValue("1");
                
                // Event Name
                event.setName(eventsList.get(0).getName());
                // TODO might need start time
                /*
                DateAndOptionalTime startTime = factory.createDateAndOptionalTime();
                XMLGregorianCalendar date = new XMLGregorianCalendar();
                date.setYear(2016);
                date.setMonth(10);
                date.setDay(13);
                startTime.setDate(date);
                event.setStartTime(startTime);*/
                
                resultList.setEvent(event);                                

                String filename = eventsList.get(0).getName() + "_Handicapped_Times_For_Eventor.xml";
                
                File file = new File(folder, filename);
                                
		JAXBContext jaxbContext = JAXBContext.newInstance("IofXml30.java");
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		jaxbMarshaller.marshal(resultList, file);
		
                String filePath = file.toString();
                String info = "Handicapped results written to \n" + filePath + "\n" +
                        "Please upload this XML file to the corresponding event on Eventor.";
                
                info = info + String.format(" The year of this race was detected as %d, let the developer know if this is wrong.", currentYear);
                InformationDialog.infoBox(info, "Handicapping Finished");
                
                
                break;
                
            /*default:
                resultsPrinter.writeResults(overallResultList);
                htmlResults = resultsPrinter.finaliseTable();*/
        }        
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }
    
    private static String cleanFilenameString(String filename) throws IOException {
        
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
        
        return filename;
    }
    
    private static boolean isEligibleClub(Organisation organisation){
        
        if (organisation == null) return false;
        
        if (organisation.getId()==null) return false;
        
        String thisIdValue = organisation.getId().getValue();
        
        for (String orgIdValue : OACT_CLUB_EVENTOR_IDS){
            
            if (thisIdValue.equals(orgIdValue)){
                return true;
            }            
        }        
        return false;
    }
}
