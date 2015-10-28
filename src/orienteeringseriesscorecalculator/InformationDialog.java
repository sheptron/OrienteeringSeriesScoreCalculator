/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import javax.swing.JOptionPane;

/**
 *
 * @author shep
 */
public class InformationDialog {

    public static void infoBox(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "Orienteering Series Score Calculator: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}
