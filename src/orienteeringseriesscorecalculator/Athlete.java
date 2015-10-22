/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import java.util.ArrayList;



/**
 *
 * @author shep
 */
public class Athlete {

    /**
     *
     */
    public enum Sex {Male, Female, YesPlease};
    
    public ArrayList<Result> results;
    public int birthDate;
    public int controlCard;
    public int id;          // id and/or controlCard could be used for checking duplicates
    public double handicap;
    public Sex sex;
    
    Athlete (int _birthDate, int _controlCard, Sex _sex) {
        birthDate = _birthDate;
        controlCard = _controlCard;
        handicap = 1.0;                  
        results = new ArrayList<Result>();
        sex = _sex;
    }
    
    public void addResult(Result result){
        results.add(result);
    }
     
    public int totalScore(int numberOfRaces){
        // We might not always want best 9 results, after 5 races we might want
        // to post the cumulative results with best 3 races counting.
        
        // Sorts results by score and return the sum of the highest numberOfRaces.
        
        return 0;
    }
  
}