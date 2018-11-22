package Swap;

import Entity.*;

import java.util.*;
import java.util.Date.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.stream.*;
import java.io.*;

public class VanillaSwap {
  List<DateValuePair> libor3m;
  List<DateValuePair> libor6m;
  List<DateValuePair> libor9m;
  List<DateValuePair> libor12m;
  
  public void contructLIBORCurve(){
    libor3m = LIBORRate.getLIBOR3M();
    libor6m = LIBORRate.getLIBOR6M();
    libor9m = LIBORRate.getLIBOR9M();
    libor12m = LIBORRate.getLIBOR12M();
  }
   
  public List<DateValuePair> generateSwapCurve(LocalDate from_date, LocalDate to_date){        
    LocalDate current_date = from_date;    
    List<DateValuePair> swap_curve = new ArrayList<DateValuePair>();
        
    while(current_date.compareTo(to_date) < 0){
      final LocalDate final_current_date = current_date;
      DateValuePair temp; 
      temp = libor3m.stream().filter(t -> final_current_date.compareTo(t.date) < 0).findFirst().orElse(null);
      double l3m = temp != null? temp.value : 0.0;

      temp = libor6m.stream().filter(t -> final_current_date.compareTo(t.date) < 0).findFirst().orElse(null);
      double l6m = temp != null? temp.value : 0.0;

      temp = libor9m.stream().filter(t -> final_current_date.compareTo(t.date) < 0).findFirst().orElse(null);
      double l9m = temp != null? temp.value : 0.0;

      temp = libor12m.stream().filter(t -> final_current_date.compareTo(t.date) < 0).findFirst().orElse(null);
      double l12m = temp != null? temp.value : 0.0;
      
      if(l3m==0)
        break;
      
      double[] floating_rate = new double[]{l3m, l6m, l9m, l12m};
      System.out.println(floating_rate);
      double p = calcFairFixRate(floating_rate);
      swap_curve.add(new DateValuePair(current_date, p));  

      current_date = current_date.plusDays(1);
    }
    
    return swap_curve;
  }
  
  private double calcFairFixRate(double[] floating_rate){
      int frequency = 4;
      int N = 12;

      double sum_discount_factor = 0.0;
      for (int i=0; i<frequency; i++)
      {
        double discount_factor = 1.0 / ( Math.pow(1 + floating_rate[i] / N, (i + 1) * ( N / frequency)));
        sum_discount_factor += discount_factor;
      }

      double fixed_rate = (100 - 100 / ( 1 + floating_rate[floating_rate.length-1] ) ) / sum_discount_factor * frequency;
      return fixed_rate;
  }
}