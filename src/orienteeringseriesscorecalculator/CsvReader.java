/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orienteeringseriesscorecalculator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author shep
 */
public class CsvReader {

public static ArrayList<ArrayList<String>> parseDataFromCsvFile(String FILENAME)
{
     ArrayList<ArrayList<String>> dataFromFile=new ArrayList<ArrayList<String>>();
     try{
         Scanner scanner=new Scanner(new FileReader(FILENAME));
         scanner.useDelimiter(";");

         while(scanner.hasNext())
         {
            String dataInRow=scanner.nextLine();
            String []dataInRowArray=dataInRow.split(";");
            ArrayList<String> rowDataFromFile=new ArrayList<String>(Arrays.asList(dataInRowArray));
            dataFromFile.add(rowDataFromFile);
         }
         scanner.close();
     }catch (FileNotFoundException e){
        e.printStackTrace();
     }
     return dataFromFile;
}
    
}
