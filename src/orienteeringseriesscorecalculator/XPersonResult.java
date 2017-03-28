/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import IofXml30.java.Id;
import IofXml30.java.Organisation;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author shep
 */
public class XPersonResult {
    
    int entryId = 0;
    XPerson person = new XPerson();
    XResult result;
    Organisation organisation = new Organisation();
    
    public XPersonResult()
    {
        Organisation organisation = new Organisation();
        organisation.setName("");
        organisation.setShortName("");
        Id id = new Id();
        id.setValue("");
        organisation.setId(id);        
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
    
    public Organisation getOrganisation(){
        if (this.organisation == null){
            Organisation organisation = new Organisation();
            organisation.setName("");
            organisation.setShortName("");
            Id id = new Id();
            id.setValue("");
            organisation.setId(id);
            this.organisation = organisation;
        }
        
        return this.organisation;
    }
    
    @XmlElement(name="Organisation")
    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }
}
