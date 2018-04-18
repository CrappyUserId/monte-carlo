package org.jwr.montecarlo.util;

import org.jwr.montecarlo.MonteCarloInput;
import org.jwr.montecarlo.MonteCarloRange;
import org.jwr.montecarlo.ProbabilityDistribution;

public class MonteCarloDouble extends MonteCarloInput<Double>
{
	public MonteCarloDouble(String name, double lowRange, double highRange, ProbabilityDistribution<Double> distribution)
	{
		super(new MonteCarloRange<Double>(lowRange, highRange), name, distribution);
	}
}
