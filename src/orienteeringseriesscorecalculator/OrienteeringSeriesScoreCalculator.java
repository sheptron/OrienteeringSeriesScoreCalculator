/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JFileChooser;
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
    public static void main(String[] args) throws JAXBException {
        // TODO Handle JAXB exceptions PER FILE

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
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        File folder;
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            folder = fc.getSelectedFile();            
        } else {
            InformationDialog.infoBox("No directory selected, press OK to exit.", "Warning");
            return;            
        }
        
        //File folder = new File("/home/shep/NetBeansProjects/OrienteeringSeriesScoreCalculator/src/orienteeringseriesscorecalculator/TestFiles");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());

                if (getFileExtension(listOfFiles[i]).equals("xml")) {

                    // We have an XML file so parse it in
                    String filename = listOfFiles[i].getAbsolutePath();

                    JAXBContext jaxbContext = JAXBContext.newInstance(ResultList.class);
                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    ResultList resultList = (ResultList) jaxbUnmarshaller.unmarshal(new File(filename));
                    
                    // Remove any not allowed classes
                    resultList.cleanClasses(ALLOWED_CLASSES);

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

                            /*Athlete.Sex sex = personResult.person.getAthleteSex();
                            int controlCard = personResult.result.controlCard;
                            int birthYear = personResult.person.getBirthYear();
                            String firstName = personResult.person.name.given;
                            String lastName = personResult.person.name.family;
                            int id = personResult.person.id;                            
                            String club =  personResult.organisation.getShortName();
                            if (club == null) club = "";
                            // TODO just use a PersonResult in the constructor for Athlete
                            Athlete athlete = new Athlete(birthYear, controlCard, sex, firstName, lastName, id, club);*/
                            Athlete athlete = new Athlete(personResult);

                            double currentHandicap = athlete.calculateHandicap(currentYear);

                            int timeInSeconds = personResult.result.timeInSeconds;

                            Result result = new Result(event, timeInSeconds, distanceInMetres, currentHandicap);

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
                    for (int k=0; k<raceResultList.size(); k++){
                        raceResultList.get(k).results.get(0).handicappedPlace = k+1;
                        raceResultList.get(k).results.get(0).score = Math.max(125-k, 0);
                    }                                      
                    
                    // Now merge with previous raceResultLists                    
                    for (Athlete raceAthlete : raceResultList) {
                        if (overallResultList.contains(raceAthlete)) {
                            
                            int k = overallResultList.indexOf(raceAthlete);
                            overallResultList.get(k).results.add(raceAthlete.results.get(0));
                        }
                        else overallResultList.add(raceAthlete);
                    }
                }
            }
        }
        
        // Decide How Many Races to Include
        int totalNumberOfRaces = eventsList.size();
        
        int numberOfRaces = (int) Math.floor((double)totalNumberOfRaces / 2.0) + 1;
        
        String[] options = new String[totalNumberOfRaces];
        for (int k=0; k<totalNumberOfRaces; k++) options[k] = String.valueOf(k+1);
        
        String userSelection = InformationDialog.selectionBox(options, numberOfRaces-1, "Select","How Many Races Count?");
        
        if(userSelection != null){
            // If the user didn't press cancel, then check what they selected
            numberOfRaces = Integer.parseInt(userSelection);
        }
        
        // We've been through all the xml files now add up each athletes scores
        for (Athlete athlete : overallResultList) athlete.totalScore(numberOfRaces);
        
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
