/*
 * Simple Lookup Table for YoB/Sex Handicap Factors
 */
package orienteeringseriesscorecalculator;

import orienteeringseriesscorecalculator.Athlete.Sex;

/**
 *
 * @author shep
 */
public class HandicapFactors {
    
    private static final int AGE_AT_END_OF_YEAR[] = {92,91,90,89,88,87,86,85,84,83,82,81,80,79,78,77,76,75,74,73,72,71,70,69,68,67,66,65,64,63,62,61,60,59,58,57,56,55,54,53,52,51,50,49,48,47,46,45,44,43,42,41,40,39,38,37,36,35,34,33,32,31,30,29,28,27,26,25,24,23,22,21,20,19,18,17,16,15,14,13,12,11,10};
    
    private static final double MALE_HANDICAP[] = {0.364,0.376,0.388,0.4,0.412,0.424,0.436,0.448,0.46,0.472,0.484,0.496,0.508,0.52,0.532,0.544,0.556,0.568,0.58,0.592,0.604,0.616,0.628,0.64,0.652,0.664,0.676,0.688,0.7,0.71,0.72,0.73,0.74,0.75,0.76,0.77,0.78,0.79,0.8,0.81,0.82,0.83,0.84,0.85,0.86,0.87,0.88,0.89,0.9,0.91,0.92,0.93,0.94,0.95,0.96,0.97,0.98,0.99,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0.97,0.94,0.91,0.88,0.85,0.82,0.79,0.76,0.73,0.7,0.67};

    private static final double FEMALE_HANDICAP[] = {0.2922,0.3018,0.3114,0.321,0.3306,0.3392,0.3488,0.3584,0.368,0.3776,0.3872,0.3968,0.4064,0.416,0.4256,0.4352,0.4448,0.4544,0.464,0.4736,0.4832,0.4928,0.5024,0.512,0.5216,0.5312,0.5408,0.5504,0.56,0.568,0.576,0.584,0.592,0.6,0.608,0.616,0.624,0.632,0.64,0.648,0.656,0.664,0.672,0.68,0.688,0.696,0.704,0.712,0.72,0.728,0.736,0.744,0.752,0.76,0.768,0.776,0.784,0.792,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.8,0.776,0.752,0.728,0.704,0.68,0.656,0.632,0.608,0.584,0.56,0.536};    
    
    public static double getHandicap(Sex sex, int yearOfBirth, int currentYear) {
        
        if (yearOfBirth == 0) {
            yearOfBirth = currentYear - 21;
        }
        
        // Get index 
        int ageAtEndOfThisYear = currentYear - yearOfBirth;
        
        // handicaps are capped at 92 and 10 years old
        int minimumAge = AGE_AT_END_OF_YEAR[AGE_AT_END_OF_YEAR.length-1];
        int maximumAge = AGE_AT_END_OF_YEAR[0];
        if (ageAtEndOfThisYear < minimumAge) ageAtEndOfThisYear = minimumAge;
        if (ageAtEndOfThisYear > maximumAge) ageAtEndOfThisYear = maximumAge;
        
  
        
        int index = AGE_AT_END_OF_YEAR[0] - ageAtEndOfThisYear;
        
        switch (sex) {
            case Male:
                return MALE_HANDICAP[index];               
            case Female:
                return FEMALE_HANDICAP[index];
            default:
                return 1.0;
        }
    }
}
