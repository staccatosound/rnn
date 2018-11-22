package Option.Binomial;

import Option.*;

public class BinomialTree {
    
  private double assetPrice;
  private double strike; 
  private double maturity; 
  private double volatility; 
  private double riskFree; 
  private int steps;
  private OptionType optionType;

  public BinomialTree(
    double assetPrice, 
    double strike, 
    double maturity, 
    double volatility, 
    double riskFree, 
    int steps,
    OptionType optionType)
  {
      this.assetPrice = assetPrice;
      this.strike = strike;
      this.maturity = maturity;
      this.volatility = volatility;
      this.riskFree = riskFree;
      this.steps = steps;
      this.optionType = optionType;
  }

  public double calcOptionValue(){
    double total = 0.0;
    double u = Math.exp(this.volatility * Math.sqrt(this.maturity / this.steps));
    double d = 1 / u;
    double f = getFutureValue(1.0, this.riskFree, this.maturity / this.steps);
    double p = (f-d)/(u-d);
    
    for (int i=0; i<this.steps;i++){
      double X = this.assetPrice * Math.pow(u, i) * Math.pow(d, this.steps - i);
      double payoff = getPayoff(X, this.strike, this.optionType);
      double node = getNodeProbability(i, this.steps, p);
      total += node * payoff;
    }
    
    double presentValue = getPresentValue(total, this.riskFree, this.maturity);    
    return presentValue;
  }
    
  private double getBinomialCoefficient(int m, int n){
    double coef = 1.0;    
    for(int i=0; i<m; i++){
      coef *=  (n - i);
      coef /=  (m - i);
    }    
    return coef;
  }
  
  private double getNodeProbability(int m, int n, double p){
    return getBinomialCoefficient(m, n) * Math.pow(p, m) * Math.pow(1-p, n - m);
  }
 
  private double getPayoff(double X, double S, OptionType optionType){
    switch(optionType){
      case Call: return Math.max(0.0, X-S);
      case Put: return Math.max(0.0, S-X);
      default: return 0.0;
    }
  }
  
  private double getFutureValue(double P, double r, double n){
    return P * Math.pow(1.0 + r, n);    
  }

  private double getPresentValue(double F, double r, double n){
    return F / Math.pow(1.0 + r, n);    
  }  
}