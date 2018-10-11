package Option.MonteCarlo;

import Option.MonteCarlo.Discretization.*;

public class SimulatedPrice {
  private double assetPrice;
  private double maturity;
  private double volatility;
  private double riskFree;
  private int steps;
  private IDiscretizationScheme scheme;
  public double[] simulatedPriceArray;  
  
  public SimulatedPrice(
    double assertPrice,
    double maturity,
    double volatility,
    double riskFree,
    int steps,
    IDiscretizationScheme scheme){
      this.assetPrice = assetPrice;
      this.maturity = maturity;
      this.volatility = volatility;
      this.riskFree = riskFree;
      this.steps = steps;
      this.scheme = scheme;
  }

  public void simulatePrice(){
    simulatedPriceArray = new double[steps];
    simulatedPriceArray[0] = assetPrice;
  
    for(int i=1; i<simulatedPriceArray.length; i++){
     simulatedPriceArray[i] = scheme.increment(simulatedPriceArray[i-1], maturity, volatility, riskFree); 
    }
  }
}