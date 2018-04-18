package org.jwr.montecarlo.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.jwr.montecarlo.MonteCarloEngine;
import org.jwr.montecarlo.MonteCarloInput;
import org.jwr.montecarlo.MonteCarloModel;
import org.jwr.montecarlo.util.DistributionRandomDouble;
import org.jwr.montecarlo.util.MonteCarloDouble;

/**
 * Reference implementation of the MonteCarloModel interface.  Calculates Pi
 * by randomly dropping (x,y) points inside of a square, and then determining 
 * the ratio of points within the quarter of a circle that fits in the square 
 * to the total number of points dropped.  This ratio multiplied by 4 will
 * approximate Pi.
 */
public class PiCalculator extends MonteCarloModel<Boolean, Double, Double>
{
	public static final String X_COORD = "x";
	public static final String Y_COORD = "y";

	public PiCalculator()
	{
		super(new ArrayList<MonteCarloInput<Double>>());
		inputs.add(new MonteCarloDouble(X_COORD, 0.0, 1.0, new DistributionRandomDouble()));
		inputs.add(new MonteCarloDouble(Y_COORD, 0.0, 1.0, new DistributionRandomDouble()));
	}

	@Override
	public Boolean getResult(Map<String, Double> inputs)
	{
		double x = inputs.get(X_COORD);
		double y = inputs.get(Y_COORD);
		double euclid = Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2)));

		return euclid < 1.0;
	}

	@Override
	public Double aggregateResults(List<Boolean> results)
	{
		double insideCount = 0.0;
		double totalCount = results.size();
		
		for (Boolean b : results)
		{
			if (b)
			{
				insideCount += 1.0;
			}
		}
		// Need to multiply result by 4 since we only dealt with a quarter of the circle.
		return 4 * (insideCount / totalCount);
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException
	{
		PiCalculator pi = new PiCalculator();
		long s1 = System.currentTimeMillis();
		double r1 = MonteCarloEngine.calculate(pi, 8000000);
		long t1 = System.currentTimeMillis() - s1;		
		System.out.println("regular="+r1+", time="+t1);
		
		long s2 = System.currentTimeMillis();
		double r2 = MonteCarloEngine.calculateThreaded(pi, 100000);
		long t2 = System.currentTimeMillis() - s2;		
		System.out.println("threaded="+r2+", time="+t2);
	}
}
