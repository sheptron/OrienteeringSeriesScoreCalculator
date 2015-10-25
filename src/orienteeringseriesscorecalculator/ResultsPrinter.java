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

    ResultsPrinter(ArrayList<Event> eventsList) {

        // Create object with header rows
        // Race results must be filled in later with ArrayList of Results
        StringBuilder html = new StringBuilder("<table>");

        html.append("<th> <td>Place</td> <td>Name</td> <td>Club</td> ");
        html.append("<td>Runs</td> <td>Sum</td>");
        
        // Sort events
        Collections.sort(eventsList, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return e1.raceNumber - e2.raceNumber;
            }
        });
        
        for (Event event : eventsList){
            String string = "<td>" + event.parseRaceName() + "</td> ";
            html.append(string);
        }
        
        html.append("</th>");
        
        this.htmlResults = html.toString();
    }
    
    public void writeResults(ArrayList<Athlete> resultList){
        //return "";
    }
    
    public String finaliseTable(){
        // Write "</table>" at the end of the string
        StringBuilder html = new StringBuilder(this.htmlResults);
        html.append("</table>");
        this.htmlResults = html.toString();
        return this.htmlResults;
    }

}
