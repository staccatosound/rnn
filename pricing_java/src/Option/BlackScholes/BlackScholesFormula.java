package Option.BlackScholes;

import Option.*;
import java.lang.Math.*;

public final class BlackScholesFormula {
  
  //formula from https://en.wikipedia.org/wiki/Normal_distribution#Cumulative_distribution_function 
  public static double calcOptionValue(double assetPrice, double strike, double maturity, double volatility, double riskFree, OptionType optionType){
    
    double sigma2 = volatility * volatility;
    double N = 1.0 / Math.sqrt(2.0 * Math.PI * sigma2);
    double d1 = (Math.log(strike / assetPrice) + (riskFree + 0.5 * sigma2) ) / (volatility * Math.sqrt(maturity));
    double d2 = d1 - volatility * Math.sqrt(maturity);
    
    double value = 0.0;

    switch(optionType){
      case Call: 
        value = assetPrice * CDF(d1) - strike * Math.exp(-riskFree * maturity) * CDF(d2);
        break;
      case Put: 
        value = assetPrice * Math.exp(-riskFree * maturity) * CDF(-d2) - strike * CDF(-d1);
        break;  
    }
    
    return value;
  }
  
  private static double CDF(double x){
    double sum = x;
    double value = x;
    
    for(int i=1; i<=100; i++){
      value *= x * x / (2.0 * i + 1.0);
      sum += value;
    }
    
    return 0.5 + (sum / Math.sqrt(2.0 * Math.PI)) * Math.exp(-(x * x) / 2.0);
  }
  
}