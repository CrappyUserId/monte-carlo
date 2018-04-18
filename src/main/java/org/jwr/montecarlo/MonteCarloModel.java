package org.jwr.montecarlo;

import java.util.List;
import java.util.Map;

/**
 * Abstract class for a generic data model that can be input into a Monte Carlo 
 * simulation.  Requires that implementing classes specify the number and type 
 * of inputs, as well as provide methods for calculating results for individual
 * simulations, and aggregating results for all simulations.
 *
 * @param <R> The type of the result of a single simulation.
 * @param <T> The type of the simulation input parameters.
 * @param <F> The type of the final result of the simulations.
 */
public abstract class MonteCarloModel<R,T,F>
{	
	protected List<MonteCarloInput<T>> inputs;
	
	public MonteCarloModel(List<MonteCarloInput<T>> inputs)
	{
		this.inputs = inputs;
	}
	
	public List<MonteCarloInput<T>> getInputs()
	{
		return inputs;
	}	
	
	public abstract R getResult(Map<String, T> inputs);
	
	public abstract F aggregateResults(List<R> results);
}
