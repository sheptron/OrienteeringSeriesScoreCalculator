/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author shep
 */
public class ResultsPrinter {
    
    public String htmlResults;
    private ArrayList<XEvent> events;

    ResultsPrinter(ArrayList<XEvent> eventsList) {        
        
        // Create object with header rows
        // Race results must be filled in later with ArrayList of Results
        
        StringBuilder html = new StringBuilder("<!DOCTYPE html>\n<html>\n<head>\n<style>\ntable, th, td {\nborder: 1px solid black;\nborder-collapse: collapse;\n}\n</style>\n</head>\n<body><table>");

        html.append("<tr><th>Place</th><th>Name</th><th>Club</th>");
        html.append("<th>Runs</th><th>Sum</th>");
        
        // Sort events
        Collections.sort(eventsList, new Comparator<XEvent>() {
            @Override
            public int compare(XEvent e1, XEvent e2) {
                return e1.raceNumber - e2.raceNumber;
            }
        });
        
        for (XEvent event : eventsList){
            String string = "<th>" + event.parseRaceName() + "</th>";
            html.append(string);
        }
        
        html.append("</tr>");
        
        this.htmlResults = html.toString();
        
        this.events = eventsList;
    }
    
    public void writeResults(ArrayList<Athlete> resultList){
        
        StringBuilder html = new StringBuilder(this.htmlResults);
        
        int place = 1;
        
        for (Athlete athlete : resultList) {
            html.append("<tr>");
            // Place
            html.append("<td>").append(place).append("</td>");
            // Name
            html.append("<td>").append(athlete.name).append("</td>");
            // Club
            html.append("<td>").append(athlete.club).append("</td>");
            // Runs
            html.append("<td>").append(athlete.results.size()).append("</td>");
            // Sum
            html.append("<td>").append(athlete.totalScore).append("</td>");
            
            for (XEvent event : this.events) {
                
                String string = "<td>0</td> ";
                // Did athlete do this event?
                for (Result result : athlete.results){
                    
                    if (result.raceNumber == event.raceNumber){
                        
                        if (result.score == 125){
                            string = "<td><font color=\"blue\">" + result.score + "</font></td>";                            
                        }
                        else if (result.score == 124){
                            string = "<td><font color=\"red\">" + result.score + "</font></td>"; 
                        }
                        else if (result.score == 123){
                            string = "<td><font color=\"green\">" + result.score + "</font></td>"; 
                        }
                        else string = "<td>" + result.score + "</td> ";
                        break;
                    }                                       
                }    
                
                html.append(string);
            }
            html.append("</tr>");
            place += 1;
        }
        //return "";
        this.htmlResults = html.toString();
    }
    
    public String finaliseTable(){
        // Write "</table>" at the end of the string
        StringBuilder html = new StringBuilder(this.htmlResults);
        html.append("</table>\n</body>\n</html>");
        this.htmlResults = html.toString();
        return this.htmlResults;
    }

}
