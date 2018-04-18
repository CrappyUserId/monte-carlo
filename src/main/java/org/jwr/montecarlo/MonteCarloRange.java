package org.jwr.montecarlo;

public class MonteCarloRange<T>
{
	private T low;
	private T high;
	
	public MonteCarloRange(T low, T high)
	{
		this.low = low;
		this.high = high;
	}

	public T getLow()
	{
		return low;
	}

	public T getHigh()
	{
		return high;
	}		
}
