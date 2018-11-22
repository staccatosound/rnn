package Option.MonteCarlo.Discretization;

import Common.*;
import Random.*;
import java.lang.Math.*;

public class EulerDiscretization implements IDiscretizationScheme {
  
  @Override
  public double increment(double value, double maturity, double volatility, double riskFree){    
    return value * (1 
        + riskFree * maturity / Const.kDaysInAYear
        + volatility * Math.sqrt(maturity / Const.kDaysInAYear) * GaussianBoxMuller.random());
  }  
  
}