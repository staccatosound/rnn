package Entity;

import java.time.*;
import java.time.format.*;

public class DateValuePair{
  public LocalDate date;
  public double value;

  public DateValuePair(LocalDate date, double value){
    this.date = date;
    this.value = value;
  }
}