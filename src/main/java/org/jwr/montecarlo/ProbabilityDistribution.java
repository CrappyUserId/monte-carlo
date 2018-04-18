package org.jwr.montecarlo;

public interface ProbabilityDistribution<T>
{
	public T generateSample(MonteCarloRange<T> range);
}
