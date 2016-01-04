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
public class XPersonResult {
    
    int entryId = 0;
    XPerson person = new XPerson();
    XResult result;
    XOrganisation organisation = new XOrganisation();
    
    public XPersonResult()
    {
        XOrganisation organisation = new XOrganisation();
        organisation.setName("");
        organisation.setShortName("");
        organisation.setId("");
        this.organisation = organisation;
    }    
    public int getEntryId(){
        return entryId;
    }
    
    @XmlElement(name="EntryId")
    public void setEntryId(int entryId){
        this.entryId = entryId;
    }
    
    public XPerson getPerson(){
        return person;
    }
    
    @XmlElement(name="Person")
    public void setPerson(XPerson person) {
        this.person = person;
    }
    
    public XResult getResult(){
        return result;
    }
    
    @XmlElement(name="Result")
    public void setResult(XResult result) {
        this.result = result;
    }
    
    public XOrganisation getOrganisation(){
        if (this.organisation == null){
            XOrganisation organisation = new XOrganisation();
            organisation.setName("");
            organisation.setShortName("");
            organisation.setId("");
            this.organisation = organisation;
        }
        
        return this.organisation;
    }
    
    @XmlElement(name="Organisation")
    public void setOrganisation(XOrganisation organisation) {
        this.organisation = organisation;
    }
}
