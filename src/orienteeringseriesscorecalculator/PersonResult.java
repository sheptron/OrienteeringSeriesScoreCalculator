/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author shep
 */
public class PersonResult {
    
    int entryId;
    Person person;
    xResult result;
    Organisation organisation;
    
    public int getEntryId(){
        return entryId;
    }
    
    @XmlElement(name="EntryId")
    public void setEntryId(int entryId){
        this.entryId = entryId;
    }
    
    public Person getPerson(){
        return person;
    }
    
    @XmlElement(name="Person")
    public void setPerson(Person person) {
        this.person = person;
    }
    
    public xResult getResult(){
        return result;
    }
    
    @XmlElement(name="Result")
    public void setResult(xResult result) {
        this.result = result;
    }
    
    public Organisation getOrganisation(){
        return organisation;
    }
    
    @XmlElement(name="Organisation")
    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }
}
