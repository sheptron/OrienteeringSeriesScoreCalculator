/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import java.util.Comparator;

/**
 *
 * @author shep
 */
public class athleteScoreComparator implements Comparator<Athlete> {
    
        @Override
        public int compare(Athlete athlete1, Athlete athlete2) {
            // TODO need to work out the index, not just 0
            return (athlete1.results.get(0).handicappedSpeed > athlete2.results.get(0).handicappedSpeed) ? 1 : 0;
        }    
}
