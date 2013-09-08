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
	 * Returns an estimation of the aggregate over all nodes.
	 */
	public double getEstimate();
}
