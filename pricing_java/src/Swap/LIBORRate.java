package Swap;

import Entity.*;

import java.text.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.util.stream.*;
import java.io.*;

public final class LIBORRate {

  public static List<DateValuePair> loadCsvFile(String filepath){
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    List<DateValuePair> values = new ArrayList<DateValuePair>();

    try{
      FileReader in = new FileReader(filepath);
      BufferedReader br = new BufferedReader(in);
      String line = br.readLine(); //skip first row
      while((line=br.readLine()) != null){
        String[] a = line.split(",");
        LocalDate d = LocalDate.parse(a[0], df);
        double v = 0;      
        if (a[1]!=".") 
          v = Double.parseDouble(a[1])/100;           
        
        values.add(new DateValuePair(d, v));        
      }
  
      br.close();
    }
    catch(Exception ex){
    }
    return values;
  }
  
  public static List<DateValuePair> getLIBOR1M(){
    String filepath = "Resource/USD1MTD156N.csv";
    return loadCsvFile(filepath);
  }
  
  public static List<DateValuePair> getLIBOR3M(){
    String filepath = "Resource/USD3MTD156N.csv";
    return loadCsvFile(filepath);
  }
    
  public static List<DateValuePair> getLIBOR6M(){
    String filepath = "Resource/USD6MTD156N.csv";
    return loadCsvFile(filepath);
  }

  public static List<DateValuePair> getLIBOR9M(){
    String filepath = "Resource/USD9MTD156N.csv";
    return loadCsvFile(filepath);
  }  
  
  public static List<DateValuePair> getLIBOR12M(){
    String filepath = "Resource/USD12MD156N.csv";
    return loadCsvFile(filepath);
  }

}