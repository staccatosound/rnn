import Option.*;
import Option.Binomial.*;
import Option.MonteCarlo.*;
import Option.MonteCarlo.Discretization.*;
import Option.BlackScholes.*;

import java.time.*;
import java.time.format.*;

public class Main{

  public static void main(String[] args) {
      double X = 100; 
      double S = 95;
      double maturity = 0.5;
      double volatility = 0.3;
      double riskFree = 0.08;
      OptionType optionType = OptionType.Call;
      
      // runBinomial(X, S, maturity, volatility, riskFree, optionType);
      // runMonteCarlo(X, S, maturity, volatility, riskFree, optionType);
      // runBlackScholes(X, S, maturity, volatility, riskFree, optionType);
      runVanillaSwap();
  }

  private static void runBinomial(double X, double S, double maturity, double volatility, double riskFree, OptionType optionType){
    System.out.println("Binomial");
    for(int step=0; step < 1000; step++){
      BinomialTree tree = new BinomialTree(X, S, maturity, volatility, riskFree, step, optionType);
      double value = tree.calcOptionValue();
      System.out.println(value);
    }    
  }
  
  private static void runMonteCarlo(double X, double S, double maturity, double volatility, double riskFree, OptionType optionType){    
    System.out.println("Monte Carlo");
    int steps = 252;
    int nSims = 1000;
    IDiscretizationScheme scheme = new EulerDiscretization();

    for(int i=0; i<nSims; i++){
      MonteCarloSimulator simulator = new MonteCarloSimulator(X, S, maturity, volatility, riskFree, steps, optionType, i, scheme);
      simulator.runSimulation();
      double value = simulator.calcOptionValue();
      System.out.println(value);      
    }
  }
  
  private static void runBlackScholes(double X, double S, double maturity, double volatility, double riskFree, OptionType optionType){    
    double value = BlackScholesFormula.calcOptionValue(X, S, maturity, volatility, riskFree, optionType);
    System.out.println("Black Scholes");
    System.out.println(value);
  }
  
  private static void runVanillaSwap(){
    System.out.println("Vanilla Swap");
    
    LocalDate from_date = LocalDate.of(1986, 1, 1);
    LocalDate to_date = LocalDate.of(2013, 5, 31);
    
    VanillaSwap vs = new VanillaSwap();
    // vs.contructLIBORCurve();
    // val swap_curve = vs.generateSwapCurve(from_date, to_date);
    
    // for(s <- swap_curve ){
    //   System.out.println(s);
    // }    
  }

}

