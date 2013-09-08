package univie.cs.psa.msg;

/**
 * A message to initialize the weights for the Push-Sum algorithm.
 * 
 * @author Dario Seidl
 * 
 */
public class InitializationMessage
{
	private final double weight;

	public InitializationMessage(double weight)
	{
		this.weight = weight;
	}

	public double getWeight()
	{
		return weight;
	}
}