package org.jwr.montecarlo.util;

import org.jwr.montecarlo.MonteCarloRange;
import org.jwr.montecarlo.ProbabilityDistribution;

public class DistributionRandomDouble implements ProbabilityDistribution<Double>
{
	@Override
	public Double generateSample(MonteCarloRange<Double> range)
	{
		return (range.getLow() + (range.getHigh() - range.getLow())) * Math.random();
	}
}
