package univie.cs.psa.utils;

/**
 * An interface for aggregation protocols to be observed by
 * {@link AggregationProtocolObserver}.
 * 
 * @author Dario Seidl
 * 
 */
public interface AggregationProtocol
{
	/**
	 * Returns the value as reported by the node, it should not be modified by
	 * the protocol.
	 */
	public double getTrueValue();

	/**
	 * Sets the value. The meaning of the value depends entirely on the
	 * implementation of the protocol.
	 */
	public void setValue(double value);

	/**
	 * Returns the value.
	 */
	public double getValue();

	/**
	 * Sets the weight. The meaning of the weight depends entirely on the
	 * implementation of the protocol.
	 */
	public void setWeight(double weight);

	/**
	 * Returns the weight.
	 */
	public double getWeight();

	/**
	 * Returns an estimation of the aggregate over all nodes.
	 */
	public double getEstimate();
}
