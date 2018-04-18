package org.jwr.montecarlo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Runs a Monte Carlo method simulation using an object implementing
 * {@link MonteCarloModel}.  Conducts a set number of trials, aggregates
 * the results, and results them.
 */
public class MonteCarloEngine
{
	/**
	 * Takes a complete MonteCarloModel and generates simulation results by
	 * running the desired number of experiments and aggregating results. 
	 * @param model the MonteCarloModel to use.
	 * @param iterations the desired number of experiments to run.
	 * @return The aggregated result.
	 */
	public static <R,T,F> F calculate(MonteCarloModel<R,T,F> model, int iterations)
	{
		List<R> results = new ArrayList<R>(iterations);
		List<MonteCarloInput<T>> inputs = model.getInputs();
		Map<String, T> inputMap = new HashMap<String, T>();

		for (int i = 0; i < iterations; i++)
		{
			inputMap.clear();
			for (MonteCarloInput<T> input : inputs)
			{
				inputMap.put(input.getName(), input.generateSample());
			}
			results.add(model.getResult(inputMap));
		}

		return model.aggregateResults(results);
	}
	
	/**
	 * Takes a complete MonteCarloModel and generates simulation results by
	 * running the desired number of experiments across a set number of threads. 
	 * @param model the MonteCarloModel to use.
	 * @param iterations the desired number of experiments to run in each thread.
	 * @param threadCount the desired number of threads to use.
	 * @return The aggregated result.
	 * @throws InterruptedException if a thread is interrupted.
	 * @throws ExecutionException if an error is encountered while running the threads.
	 */
	public static <R,T,F> F calculateThreaded(MonteCarloModel<R,T,F> model, int iterations, int threadCount) 
			throws InterruptedException, ExecutionException
	{		
		// Create each thread, and have it call single-threaded calculate method with its own portion of iterations.				
		List<MonteCarloThread<R, T, F>> threads = new ArrayList<MonteCarloThread<R, T, F>>();		
		for (int i = 0; i < threadCount; i++)
		{
			threads.add(new MonteCarloThread<R,T,F>(model, iterations));
		}
		
		// Submit the threads to an executor.
		ExecutorService executorService = Executors.newCachedThreadPool();
		List<Future<List<R>>> results = null;
		results = executorService.invokeAll(threads);
		
		// Collect results.
		List<R> finalResults = new ArrayList<R>();
		for (Future<List<R>> result : results)
		{
			finalResults.addAll(result.get());
		}		
		
		// Shutdown thread pool.
		executorService.shutdown();
		
		return model.aggregateResults(finalResults);
	}
	
	/**
	 * Takes a complete MonteCarloModel and generates simulation results by
	 * running the desired number of experiments across all available cores
	 * and aggregating results. 
	 * @param model the MonteCarloModel to use.
	 * @param iterations the desired number of experiments to run in each thread.
	 * @return The aggregated result.
	 * @throws InterruptedException if a thread is interrupted.
	 * @throws ExecutionException if an error is encountered while running the threads.
	 */
	public static <R,T,F> F calculateThreaded(MonteCarloModel<R,T,F> model, int iterations) 
			throws InterruptedException, ExecutionException
	{		
		// Determine optimal number of threads.
		int cores = Runtime.getRuntime().availableProcessors();		
		return calculateThreaded(model, iterations, cores);
	}
	
	/**
	 * Worker thread for running Monte Carlo simulations and returning the results.
	 */
	private static class MonteCarloThread<R,T,F> implements Callable<List<R>>
	{
		private int iterations;
		private MonteCarloModel<R,T,F> model;
		
		public MonteCarloThread(MonteCarloModel<R,T,F> model, int iterations)
		{
			this.model = model;
			this.iterations = iterations;
		}
		
		@Override
		public List<R> call() throws Exception
		{
			List<R> results = new ArrayList<R>(iterations);
			List<MonteCarloInput<T>> inputs = model.getInputs();
			Map<String, T> inputMap = new HashMap<String, T>();

			for (int i = 0; i < iterations; i++)
			{
				inputMap.clear();
				for (MonteCarloInput<T> input : inputs)
				{
					inputMap.put(input.getName(), input.generateSample());
				}
				results.add(model.getResult(inputMap));
			}			
			
			return results;
		}		
	}
}
