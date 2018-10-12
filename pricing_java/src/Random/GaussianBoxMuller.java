package Random;
import java.lang.Math.*;

public class GaussianBoxMuller {
  public static double random(){
    double x = 0.0;
    double y = 0.0;
    double square = 1.0;
    
    while(square >= 1){
     x = 2 * Math.random() - 1; 
     y = 2 * Math.random() - 1; 
     square = (x * x) + (y * y);
    }
   
    return x * Math.sqrt(-2 * Math.log(square) / square);
  }
}