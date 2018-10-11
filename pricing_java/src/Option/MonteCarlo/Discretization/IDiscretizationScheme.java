package Option.MonteCarlo.Discretization;

public interface IDiscretizationScheme {
  public double increment(double value, double maturity, double volatility, double riskFree);
}