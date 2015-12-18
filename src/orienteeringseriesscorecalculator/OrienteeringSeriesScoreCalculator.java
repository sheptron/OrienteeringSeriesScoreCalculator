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
 */
public class OrienteeringSeriesScoreCalculator {

    public static int currentYear = 2016;       // TODO how to get this from xml?
    public static final int FIRST_PLACE_SCORE = 125;  // Score for the (handicapped winner)
    private static final String[] ALLOWED_CLASSES = {"Orange"};

    /**
     * @param args the command line arguments
     */
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

        ArrayList<Event> eventsList = new ArrayList<>();

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

                        JAXBContext jaxbContext = JAXBContext.newInstance(ResultList.class);
                        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                        ResultList resultList = (ResultList) jaxbUnmarshaller.unmarshal(new File(filename));

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
                        Event event = resultList.event;

                        for (int jj = 0; jj < resultList.classResult.length; jj++) {

                            int distanceInMetres = resultList.classResult[jj].course.length;

                            for (PersonResult personResult : resultList.classResult[jj].personResult) {
                                
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

                        // Now to sort by handicappedSpeed                     
                        Collections.sort(raceResultList, new Comparator<Athlete>() {
                            @Override
                            public int compare(Athlete a1, Athlete a2) {
                                return a1.results.get(0).handicappedSpeed - a2.results.get(0).handicappedSpeed;
                            }
                        });

                        // And assign points                    
                        for (int k = 0; k < raceResultList.size(); k++) {
                            raceResultList.get(k).results.get(0).setHandicappedPlace(k+1);
                            raceResultList.get(k).results.get(0).calculateScore();
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

        // And sort (decreasing
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
