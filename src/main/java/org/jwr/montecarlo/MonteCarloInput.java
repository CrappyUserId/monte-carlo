package org.jwr.montecarlo;

/**
 * Encapsulates a single parameter input to a Monte Carlo model, including
 * the range of values to be generated, and the probability distribution
 * strategy to be used for parameter generation.
 * 
 * @param <T> The type of the parameter to be used.
 */
public abstract class MonteCarloInput<T>
{
	private MonteCarloRange<T> range;
	private String name;
	private ProbabilityDistribution<T> distribution;	
	
	public MonteCarloInput(MonteCarloRange<T> range, String name,
			ProbabilityDistribution<T> distribution)
	{
		this.range = range;
		this.name = name;
		this.distribution = distribution;
	}

	public MonteCarloRange<T> getRange()
	{
		return range;
	}
	
	public String getName()
	{
		return name;
	}
	
	public ProbabilityDistribution<T> getProbabilityDistribution()
	{
		return distribution;
	}
	
	public T generateSample()
	{
		return distribution.generateSample(range);
	}
}
