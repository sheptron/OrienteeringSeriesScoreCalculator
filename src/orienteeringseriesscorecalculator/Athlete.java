/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import IofXml30.java.Country;
import IofXml30.java.Id;
import IofXml30.java.Organisation;
import IofXml30.java.PersonResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import static orienteeringseriesscorecalculator.OrienteeringSeriesScoreCalculator.DEFAULT_ORGANISATION_ID_VALUE;



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
    public String name = "";
    String firstName = "";
    String surname = "";
    public int yearOfBirth = 0;
    public int controlCard = 0;
    public Id id;                      // id and/or controlCard could be used for checking duplicates
    public Sex sex = Sex.YesPlease;
    public double currentHandicap = 1.0;    // Current - just to be clear that the handicap in a Result may be different
    public int totalScore = 0;
    public String className = "Handicap";
    
    public Organisation organisation;
  
    Athlete (int _yearOfBirth, int _controlCard, Sex _sex, String firstName, String lastName, Id _id) {
        yearOfBirth = _yearOfBirth;
        controlCard = _controlCard;                 
        results = new ArrayList<Result>();
        sex = _sex;
        name = firstName + " " + lastName;
        id = _id;        
    }
    
    /*Athlete (XPersonResult personResult) {
        yearOfBirth = personResult.person.getBirthYear();
        controlCard = personResult.result.controlCard;                 
        results = new ArrayList<Result>();
        sex = personResult.person.getAthleteSex();
        firstName = personResult.person.name.given;
        surname = personResult.person.name.family;
        name = firstName + " " + surname;
        id = personResult.person.id;         
        organisation = personResult.getOrganisation();
    }*/
    
    Athlete (PersonResult personResult) {
        yearOfBirth = personResult.getPerson().getBirthDate().getYear();
        //controlCard = personResult.getResult().get(0).getControlCard().get(0).getValue();                 
        results = new ArrayList<Result>();
        String _sex = personResult.getPerson().getSex();
        if (_sex.equals("M")) sex = Sex.Male;
        else if (_sex.equals("F")) sex = Sex.Female;
        firstName = personResult.getPerson().getName().getGiven();
        surname = personResult.getPerson().getName().getFamily();
        name = firstName + " " + surname;
        
        // TODO use Id
        if (personResult.getPerson().getId().size()>0) {
            id = personResult.getPerson().getId().get(0);
        }
        else {
            id = new Id();  
            id.setValue("");
        }
        
        if (personResult.getOrganisation()!=null){        
            organisation = personResult.getOrganisation();  
        }
        else {
            organisation = new Organisation();
            Id orgId = new Id();
            orgId.setValue("");
            organisation.setId(orgId);
            
            Country country = new Country();
            country.setValue("");
            country.setCode("");
            organisation.setCountry(country);
        }
    }
    
    public String getSex(){
        switch (sex){
            case Female:
                return "F";
            default:
                return "M";               
        }
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }
    
    public void addResult(Result result){
        results.add(result);
    }
     
    public int totalScore(int numberOfRaces){
        // We might not always want best 9 results, after 5 races we might want
        // to post the cumulative results with best 3 races counting.
        
        // Sorts results by score and return the sum of the highest numberOfRaces.
        Collections.sort(results, new Comparator<Result>() {
            @Override
            public int compare(Result r1, Result r2) {
                return r2.score - r1.score;
            }
        });
        
        totalScore = 0;
        for (int i=0; i<results.size(); i++){
            if (i >= numberOfRaces) break;
            
            totalScore += results.get(i).score;
        }
        
        return totalScore;
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
        
        /*
        There are 3 sections all relative to year 0 (y0)
        y0 to y0+27 : 0.012(y-y0)+0.376, m=0.0096, c=0.3488 (W)
        y0+28 to y0+57 : 0.01(y-y0+28)+0.7, m=0.008, c=0.56 (W)
        y0+58 to y0+71 : 1, 0.8 (W)
        y0+72 to y0+82 : -0.03(y-y0+72)+0.97, m=-0.024, c=0.776 (W)
        
        where y0 = currentYear-92 (in 2016 it was 1924)
        */
       
        double handicap = HandicapFactors.getHandicap(sex, yearOfBirth, currentYear);
        currentHandicap = handicap;
        return handicap;        
    }
 
    @Override
    public boolean equals(Object obj) {
        
        // Athlete objects are equal if the names are the same - check YOB as well?
        
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Athlete athlete = (Athlete) obj;
        
        // Names Identical
        boolean surnameMatch = this.surname.equalsIgnoreCase(athlete.surname);
        boolean fullNameMatch = this.name.equalsIgnoreCase(athlete.name);  
        // Eventor ID Number (athletes without an ID will be 0)
        //boolean idMatch = (this.id == athlete.id) && (this.id != 0);
        boolean idMatch = (this.id.getValue().equals(athlete.id.getValue()) && (!this.id.getValue().equals("0")));
        boolean yobMatch = (this.yearOfBirth == athlete.yearOfBirth) && (this.yearOfBirth != 0);
        
        
        return idMatch || (fullNameMatch && yobMatch); // Log
        
        /*
        // TODO : test for possible duplicates here...
        if (this.name.equalsIgnoreCase(athlete.name)){
        // log to file
        }
        if (this.surname.equalsIgnoreCase(athlete.surname)){
        // Surnames are the same now have a look at first names
        // First names have 3 letters in a row that are common
        for (int k=0; k<this.firstName.length()-2; k++){
        String s1 = this.firstName.substring(k, k+3);
        if (athlete.firstName.contains(s1)){
        }
        }
        // First names start with same letter
        if (this.firstName.startsWith(athlete.firstName.substring(0, 1))){
        }
        }
        if (this.controlCard == athlete.controlCard){
        // log to file
        }
        if (this.id == athlete.id){
        // Possible duplicate
        }
        return this.name.equalsIgnoreCase(athlete.name);
         */
    }
    /*public class athleteScoreComparator implements Comparator<Athlete> {
    
        @Override
        public int compare(Athlete athlete1, Athlete athlete2) {
            // TODO need to work out the index, not just 0
            return (athlete1.results.get(0).handicappedSpeed > athlete2.results.get(0).handicappedSpeed) ? 1 : 0;
        }  
    }*/

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }
    
    
}