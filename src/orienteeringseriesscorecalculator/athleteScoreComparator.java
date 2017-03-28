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
            
            int speed1 = athlete1.getResults().get(0).getHandicappedSpeed();
            int speed2 = athlete2.getResults().get(0).getHandicappedSpeed();
            
            if (speed1 < speed2) return -1;
            else if (speed1 > speed2) return 1;
            else return 0;
        }    
}
