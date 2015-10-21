/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheptron.orienteeringseriesscorecalculator;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author shep
 */
public class OrienteeringSeriesScoreCalculator {

    public static void main() {
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

    }

    private ArrayList<Athlete> parseXml(String filename) {

        /*
        We want results from 
            <Name>Orange 1</Name> and <Name>Orange 2</Name>
        and we need course lengths from 
            <Length>****</Length>
        in metres. 
        
        For Athletes and results...
            <PersonResult>
                <Person sex="M"> or <Person sex="F">
                <Id>326</Id>
                <Name>
                    <Family>Fcuk</Family>
                    <Given>Cnut</Given>
                </Name>        
                <BirthDate>1977-01-01</BirthDate>
                <Time>1191</Time>
                <ControlCard>1006144</ControlCard>
        */
        ArrayList<Athlete> athletes = new ArrayList<>();

        try {
            File inputFile = new File(filename);
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            
            System.out.println("Root element :"
                    + doc.getDocumentElement().getNodeName());
            
            NodeList nList = doc.getElementsByTagName("student");
            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :"
                        + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("Student roll no : "
                            + eElement.getAttribute("rollno"));
                    System.out.println("First Name : "
                            + eElement
                            .getElementsByTagName("firstname")
                            .item(0)
                            .getTextContent());
                    System.out.println("Last Name : "
                            + eElement
                            .getElementsByTagName("lastname")
                            .item(0)
                            .getTextContent());
                    System.out.println("Nick Name : "
                            + eElement
                            .getElementsByTagName("nickname")
                            .item(0)
                            .getTextContent());
                    System.out.println("Marks : "
                            + eElement
                            .getElementsByTagName("marks")
                            .item(0)
                            .getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return athletes;
    }
}
