package Swap;

import java.util.Date.*;
import java.text.*;
import java.time.*;
import java.time.format.*;
import javafx.util.Pair.*;

class VanillaSwap {
  List<Pair<LocalDate, double>> libor3m;
  List<Pair<LocalDate, double>> libor6m;
  List<Pair<LocalDate, double>> libor9m;
  List<Pair<LocalDate, double>> libor12m;
  
  public void contructLIBORCurve(){
    libor3m = LIBORRate.getLIBOR3M();
    libor6m = LIBORRate.getLIBOR6M();
    libor9m = LIBORRate.getLIBOR9M();
    libor12m = LIBORRate.getLIBOR12M();
  }
   
  public List<Pair<LocalDate, double>> generateSwapCurve(LocalDate from_date, LocalDate to_date){        
    var current_date = from_date;    
    var swap_curve = new ArrayList<Pair<LocalDate, Double>>();
        
    while(current_date.compareTo(to_date) < 0){
      double l3m = libor3m.filter(t => t._1.compareTo(current_date) >= 0).head._2;
      double l6m = libor6m.filter(t => t._1.compareTo(current_date) >= 0).head._2;
      double l9m = libor9m.filter(t => t._1.compareTo(current_date) >= 0).head._2;
      double l12m = libor12m.filter(t => t._1.compareTo(current_date) >= 0).head._2;
      
      if(l3m==0)
        break;
      
      double floating_rate = List(l3m, l6m, l9m, l12m);
      double p = calcFairFixRate(floating_rate);
      swap_curve += current_date -> p;  

      current_date = current_date.plusDays(1);
    }
    
    return swap_curve.toList.toSeq;
  }
  
  private double calcFairFixRate(double[] floating_rate){
      int frequency = 4;
      int N = 12;

      double sum_discount_factor = 0.0;
      for (int i=0; i<frequency; i++)
      {
        val discount_factor = 1.0 / ( math.pow(1 + floating_rate(i) / N, (i + 1) * ( N / frequency)));
        sum_discount_factor += discount_factor;
      }

      double fixed_rate = (100 - 100 / ( 1 + floating_rate.last ) ) / sum_discount_factor * frequency;
      return fixed_rate;
  }
}