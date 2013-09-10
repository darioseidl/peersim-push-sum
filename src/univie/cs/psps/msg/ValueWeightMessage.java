package univie.cs.psps.msg;

/**
 * A message carrying a value and a weight as used in the Push-Sum algorithm.
 * 
 * @author Dario Seidl
 * 
 */
public class ValueWeightMessage
{
	private final double value;
	private final double weight;

	public ValueWeightMessage(double value, double weight)
	{
		this.value = value;
		this.weight = weight;
	}

	public double getValue()
	{
		return value;
	}

	public double getWeight()
	{
		return weight;
	}
}