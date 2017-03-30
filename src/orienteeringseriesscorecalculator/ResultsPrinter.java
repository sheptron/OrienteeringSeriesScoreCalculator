/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import IofXml30.java.Event;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import orienteeringseriesscorecalculator.OrienteeringSeriesScoreCalculator.Mode;

/**
 *
 * @author shep
 */
public class ResultsPrinter {
    
    public String htmlResults;
    private Event event;

    ResultsPrinter(Event event) {        
        
        // Create object with header rows
        // Race results must be filled in later with ArrayList of Results
    }
    
    public static String writeJimSawkinsResults(ArrayList<Athlete> resultList, ArrayList<Athlete> division2ResultList){
        
        StringBuilder html = new StringBuilder("<!DOCTYPE html>\n<html>\n<head>\n<style>\ntable, th, td {\nborder: 1px solid black;\nborder-collapse: separate;\nmargin:0 20px;\n}\n</style>\n</head>\n<body><div>\n<table style=\"float: left\">\n<caption><h2>Division 1 Results</h2></caption>");
        html.append("<tr><th>Place</th><th>Name</th><th>Handicapped Km Rate</th>");
        
        int place = 1;

        for (Athlete athlete : resultList) {
            html.append("<tr>");
            // Place
            html.append("<td>").append(place).append("</td>");
            // Name
            html.append("<td>").append(athlete.name).append("</td>");
            // Club
            //html.append("<td>").append(athlete.club).append("</td>");
            // Handicapped Speed
            html.append("<td>").append(athlete.results.get(0).handicappedKmRate).append("</td>");
             // Course
             
             // Length
             
             // Time
             
             // Handicap
             //html.append("<td>").append(athlete.currentHandicap).append("</td>");
              
             //html.append("<td>").append(athlete.yearOfBirth).append("</td>");
            
            
            html.append("</tr>");
            place += 1;
        }
        
        html.append("</table>\n<table style=\"float: left\">\n<caption><h2>Division 2 Results</h2></caption>");
        html.append("<tr><th>Place</th><th>Name</th><th>Handicapped Km Rate</th>");
        
        //
        place = 1;

        for (Athlete athlete : division2ResultList) {
            html.append("<tr>");
            // Place
            html.append("<td>").append(place).append("</td>");
            // Name
            html.append("<td>").append(athlete.name).append("</td>");
            // Club
            //html.append("<td>").append(athlete.club).append("</td>");
            // Handicapped Speed
            html.append("<td>").append(athlete.results.get(0).handicappedKmRate).append("</td>");
             // Course
             
             // Length
             
             // Time
             
             // Handicap
             //html.append("<td>").append(athlete.currentHandicap).append("</td>");
              
             //html.append("<td>").append(athlete.yearOfBirth).append("</td>");
            
            
            html.append("</tr>");
            place += 1;
        }
        html.append("</table>\n</div>\n</body>\n</html>");
        //
        
        //this.htmlResults = html.toString();
        return html.toString();
    }
    
    public String finaliseTable(){
        // Write "</table>" at the end of the string
        StringBuilder html = new StringBuilder(this.htmlResults);
        html.append("</table>\n</body>\n</html>");
        this.htmlResults = html.toString();
        return this.htmlResults;
    }

}
