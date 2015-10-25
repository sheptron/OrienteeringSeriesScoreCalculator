/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

/**
 *
 * @author shep
 */
public class ResultsPrinter {
    
    /*
     <table style="width:100%">
  <tr>
    <th>Firstname</th>
    <th>Lastname</th>
    <th>Points</th>
  </tr>
  <tr>
    <td>Eve</td>
    <td>Jackson</td>
    <td>94</td>
  </tr>
</table> 
    */
 
	public static String array2HTML(Object[][] array){
		StringBuilder html = new StringBuilder(
				"<table>");
		for(Object elem:array[0]){
			html.append("<th>" + elem.toString() + "</th>");
		}
		for(int i = 1; i < array.length; i++){
			Object[] row = array[i];
			html.append("<tr>");
			for(Object elem:row){
				html.append("<td>" + elem.toString() + "</td>");
			}
			html.append("</tr>");
		}
		html.append("</table>");
		return html.toString();
	}
 
	public static void main(String[] args){
		Object[][] ints = {{"","X","Y","Z"},{1,1,2,3},{2,4,5,6},{3,7,8,9},{4,10,11,12}};
		System.out.println(array2HTML(ints));
	}

    
}
