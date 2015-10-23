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
    public String name;
    public int yearOfBirth;
    public int controlCard;
    public int id;          // id and/or controlCard could be used for checking duplicates
    public Sex sex;
    public double currentHandicap;  // Current - just to be clear that the handicap in a Result may be different
    
    Athlete (int _yearOfBirth, int _controlCard, Sex _sex, String firstName, String lastName) {
        yearOfBirth = _yearOfBirth;
        controlCard = _controlCard;                 
        results = new ArrayList<Result>();
        sex = _sex;
        name = firstName + " " + lastName;
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
    
    public double calculateHandicap(int currentYear){
        /*
        There are 3 sections all relative to year 0 (y0)
        y0 to y0+21 : 0.012(y-y0)+0.436, m=0.0096, c=0.3488 (W)
        y0+22 to y0+51 : 0.01(y-y0+22)+0.7, m=0.008, c=0.56 (W)
        y0+52 to y0+65 : 1, 0.8 (W)
        y0+66 to y0+76 : -0.03(y-y0+66)+0.97, m=-0.024, c=0.776 (W)
        
        where y0 = currentYear-86 (in 2016 it was 1930)
        */
       
        double handicap;
        
        int y0 = currentYear - 86;
        
        if (yearOfBirth < y0){
            if (sex == Athlete.Sex.Female) handicap = 0.3488;
            else handicap = 0.436;           
        }
        else if (yearOfBirth >= y0 && yearOfBirth <= y0+21){
            if (sex == Athlete.Sex.Female) handicap = 0.0096*(yearOfBirth-y0)+0.3488;
            else handicap = 0.012*(yearOfBirth-y0)+0.436; 
        }
        else if (yearOfBirth >= y0+22 && yearOfBirth <= y0+51){
            if (sex == Athlete.Sex.Female) handicap = 0.008*(yearOfBirth-y0-22)+0.56;
            else handicap = 0.01*(yearOfBirth-y0-22)+0.7; 
        }
        else if (yearOfBirth >= y0+52 && yearOfBirth <= y0+65){
            if (sex == Athlete.Sex.Female) handicap = 0.8;
            else handicap = 1.0; 
        }
        else if (yearOfBirth >= y0+66 && yearOfBirth <= y0+76){
            if (sex == Athlete.Sex.Female) handicap = -0.024*(yearOfBirth-y0-66)+0.776;
            else handicap = -0.03*(yearOfBirth-y0-66)+0.97; 
        }
        else {
            if (sex == Athlete.Sex.Female) handicap = 0.536;
            else handicap = 0.67; 
        }
        
        // Round to 3 decimal places
        handicap = Math.round(handicap*1000) / 1000.0;
        
        currentHandicap = handicap;
        return handicap;
    }
  
}