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
class XFee {
    String Name;
    String amount;

    public String getName() {
        return Name;
    }

    @XmlElement(name="Name")
    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAmount() {
        return amount;
    }

    @XmlElement(name="Amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    
}
