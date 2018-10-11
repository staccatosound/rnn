package Swap;

import java.text.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import javafx.util.Pair.*;

public final class LIBORRate {

  public static List<Pair<LocalDate, double>> loadCsvFile(string filepath){
    string[] lines = fromFile(filepath).getLines;
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    List<Pair<LocalDate, double>> values = new ArrayList<Pair<LocalDate, Double>>();
    for (string line : lines.stream.skip(1)) {
      string[] a = line.split(",");
      LocalDate d = LocalDate.parse(a(0), df);
      double v = 0;      
      if (a(1)!=".") 
        v = a(1).toDouble/100;           
      
      values.add(Pair(d, v));
    }
    return values;    
  }
  
  public static List<Pair<LocalDate, double>> getLIBOR1M(){
    string filepath = "Resource/USD1MTD156N.csv";
    return loadCsvFile(filepath);
  }
  
  public static List<Pair<LocalDate, double>> getLIBOR3M(){
    string filepath = "Resource/USD3MTD156N.csv";
    return loadCsvFile(filepath);
  }
    
  public static List<Pair<LocalDate, double>> getLIBOR6M(){
    string filepath = "Resource/USD6MTD156N.csv";
    return loadCsvFile(filepath);
  }

  public static List<Pair<LocalDate, double>> getLIBOR9M(){
    string filepath = "Resource/USD9MTD156N.csv";
    return loadCsvFile(filepath);
  }  
  
  public static List<Pair<LocalDate, double>> getLIBOR12M(){
    string filepath = "Resource/USD12MD156N.csv";
    return loadCsvFile(filepath);
  }

}