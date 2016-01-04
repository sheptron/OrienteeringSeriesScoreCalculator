/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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
    
    // Two modes of operation: Twilight Series and ACT League
    public enum Mode {TWILIGHT, ACT_LEAGUE};
    // Human readable version for the dialog box
    private static final String[] MODES = {"Twilight Series","ACT League"};

    public static void main(String[] args) {

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
        Mode mode = Mode.TWILIGHT;

        // Get file directory from user...
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new FileNameExtensionFilter("OE XML Results Files","xml"));
        fc.setDialogTitle("Select all the XML results files you want to process");
        File folder;
        File[] listOfFiles;
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            //folder = fc.getSelectedFile();
            listOfFiles = fc.getSelectedFiles();
            folder = listOfFiles[0].getParentFile(); // Get the parent directory (assuming all selected files are in the same dir)
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
                        // # will cause a filenot found exception in JAXB
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

                        switch (mode) {

                            case TWILIGHT:
                                // Now to sort by handicappedSpeed                     
                                Collections.sort(raceResultList, new Comparator<Athlete>() {
                                    @Override
                                    public int compare(Athlete a1, Athlete a2) {
                                        return a1.results.get(0).handicappedSpeed - a2.results.get(0).handicappedSpeed;
                                    }
                                });

                                // And assign points                    
                                for (int k = 0; k < raceResultList.size(); k++) {
                                    raceResultList.get(k).results.get(0).setHandicappedPlace(k + 1);
                                    raceResultList.get(k).results.get(0).calculateScore();
                                }
                            
                            break;
                                
                            case ACT_LEAGUE:
                                /*
                                The revised kilometre rates are then matched 
                                against the nominal kilometre rate for the 
                                Australian elite male champion (par km rate) and 
                                points are awarded on the basis of relativity. 
                                That is if the rates match, the competitor 
                                receives 125 points, if it is faster, the 
                                competitor receives pro rata more than 125 and 
                                slower (as is usually the case) the competitor 
                                receives pro rata of 125 points.
                                
                                Loading factors are based on whether a 
                                competitor is able to view the map before 
                                starting. For events where a competitor views 
                                the map the factor is 1.0 and for those event 
                                where a competitor does not view the map the 
                                factor is 1.1.
                                */
                                
                                // TODO : get the "nominal kilometre rate for the Australian elite male champion"
                                
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

        String userSelection = InformationDialog.selectionBox(options, numberOfRaces - 1, "Select", "How Many Races Count?");

        if (userSelection != null) {
            // If the user didn't press cancel, then check what they selected
            numberOfRaces = Integer.parseInt(userSelection);
        }

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

        // And publish
        ResultsPrinter resultsPrinter = new ResultsPrinter(eventsList);
        resultsPrinter.writeResults(overallResultList);
        String htmlResults = resultsPrinter.finaliseTable();

        // Build Filename
        String outFilename = folder.toString() + "/" + currentYear + "_Twilight_Series_Results_after_Round_" + totalNumberOfRaces + "_Best_" + numberOfRaces + ".html";

        StringToFile.write(outFilename, htmlResults);

        InformationDialog.infoBox(outFilename, "Results Written To ...");
        
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
