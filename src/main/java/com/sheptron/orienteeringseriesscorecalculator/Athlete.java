/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sheptron.orienteeringseriesscorecalculator;

import java.util.ArrayList;

/**
 *
 * @author shep
 */
public class Athlete {

    /**
     *
     */
    public ArrayList<Result> results;
    public int birthDate;
    public int controlCard;
    public double handicap;
    
    Athlete (int _birthDate, int _controlCard) {
        birthDate = _birthDate;
        controlCard = _controlCard;
        handicap = 1.0;
        results = new ArrayList<Result>();
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