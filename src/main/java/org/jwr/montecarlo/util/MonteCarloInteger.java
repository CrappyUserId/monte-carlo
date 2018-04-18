package org.jwr.montecarlo.util;

import org.jwr.montecarlo.MonteCarloInput;
import org.jwr.montecarlo.MonteCarloRange;
import org.jwr.montecarlo.ProbabilityDistribution;

public class MonteCarloInteger extends MonteCarloInput<Integer>
{
	public MonteCarloInteger(String name, int lowRange, int highRange,
			ProbabilityDistribution<Integer> distribution)
	{
		super(new MonteCarloRange<Integer>(lowRange, highRange), name, distribution);
	}
}
