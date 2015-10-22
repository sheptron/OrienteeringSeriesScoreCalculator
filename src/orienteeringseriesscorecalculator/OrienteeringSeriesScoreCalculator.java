/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlbeam.annotation.XBDocURL;

/**
 *
 * @author shep
 */
public class OrienteeringSeriesScoreCalculator {

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
                    
                    int m = 0;

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
