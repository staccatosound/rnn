package Option.MonteCarlo;

import Option.*;
import Option.MonteCarlo.Discretization.*;
import Common.*;
import java.lang.Math.*;
import java.util.stream.*;

public class MonteCarloSimulator {

  private double assetPrice;
  private double strike;
  private double maturity;
  private double volatility;
  private double riskFree;
  private int steps;
  private OptionType optionType;
  private int nSims;
  private IDiscretizationScheme scheme;    
  private SimulatedPrice[] priceSimArray;
  
  public MonteCarloSimulator(
    double assertPrice,
    double strike,
    double maturity,
    double volatility,
    double riskFree,
    int steps,
    OptionType optionType,
    int nSims,
    IDiscretizationScheme scheme){
      this.assetPrice = assetPrice;
      this.strike = strike;
      this.maturity = maturity;
      this.volatility = volatility;
      this.riskFree = riskFree;
      this.steps = steps;
      this.optionType = optionType;
      this.nSims = nSims;
      this.scheme = scheme;
  }

  public void runSimulation(){    
    priceSimArray = new SimulatedPrice[nSims];

    for(int i=0; i<nSims; i++){
      SimulatedPrice simPrice = new SimulatedPrice(assetPrice, maturity, volatility, riskFree, steps, scheme);
      priceSimArray[i] = simPrice;
    }

    for(int i=0; i<nSims; i++){
      priceSimArray[i].simulatePrice();
    }        
  }
  
  public double calcOptionValue(){
    double value = 0.0;
    
    switch(optionType){
      case Call: 
        value = Stream.of(priceSimArray).map(a -> a.simulatedPriceArray[steps-1] - strike).filter(b -> b > 0).mapToDouble(d->d).sum() / nSims;
        break;

      case Put:
        value = Stream.of(priceSimArray).map(a -> strike - a.simulatedPriceArray[steps-1]).filter(b -> b > 0).mapToDouble(d->d).sum() / nSims;
        break;
    }
    double discount = getDiscountFactor(riskFree, steps, maturity);    
    return value * discount;
  }
  
  private double getDiscountFactor(double riskFree, int steps, double maturity){    
    return Math.exp(-riskFree * steps * maturity / Const.kDaysInAYear);
  }  
}