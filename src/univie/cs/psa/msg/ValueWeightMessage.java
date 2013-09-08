package univie.cs.psa.msg;

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