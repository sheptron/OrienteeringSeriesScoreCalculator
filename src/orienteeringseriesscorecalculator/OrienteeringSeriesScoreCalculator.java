/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import java.io.File;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author shep
 */
public class OrienteeringSeriesScoreCalculator {

    public static int currentYear = 2016; // TODO how to get this from xml?
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JAXBException {
        // TODO code application logic here

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
        
        // TODO get filenames from current working dir
        File folder = new File("/home/shep/NetBeansProjects/OrienteeringSeriesScoreCalculator/src/orienteeringseriesscorecalculator/TestFiles");
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
                    
                    // TODO instead of creating a new ArrayList we should add/edit what we've got...
                    ArrayList<Athlete> raceResultList = new ArrayList<>();
                    
                    /*
                    Now go through resultList and build Athletes with Results
                    For each classResult we need course.length for each personResult inside
                    */
                    Event event = resultList.event;
                    
                    for (int jj=0; jj<resultList.classResult.length; jj++){
                       
                        int distanceInMetres = resultList.classResult[jj].course.length;     
                        
                        for (PersonResult personResult : resultList.classResult[jj].personResult){
                            
                            Athlete.Sex sex = personResult.person.getAthleteSex();
                            int controlCard = personResult.result.controlCard;
                            int birthYear = personResult.person.getBirthYear();
                            String firstName = personResult.person.name.given;
                            String lastName = personResult.person.name.family;
                            Athlete athlete = new Athlete(birthYear, controlCard, sex, firstName, lastName);
                            
                            double currentHandicap = athlete.calculateHandicap(currentYear);
                            
                            int timeInSeconds = personResult.result.timeInSeconds;
                            
                            Result result = new Result(event, timeInSeconds, distanceInMetres, currentHandicap);
                            
                            athlete.addResult(result);
                            
                            // Now add this athlete and result to array
                            raceResultList.add(athlete);
                        }                                            
                    }

                    // Now to sort by handicappedSpeed
                    int lmn = 0;
                    
                    // TODO how to get all eg "Race 1" results out to sort
                    
                    // And assign points

                }
            }
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
}
