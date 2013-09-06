package univie.cs.psa.utils;

import peersim.vector.SingleValue;

public interface ValueWeight extends SingleValue
{
	public double getTrueValue();

	@Override
	public double getValue();

	@Override
	public void setValue(double value);

	public double getWeight();

	public void setWeight(double weight);

	public double getEstimate();
}
