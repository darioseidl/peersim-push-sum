package univie.cs.psa;

import peersim.config.Configuration;
import peersim.config.FastConfig;
import peersim.core.Node;
import peersim.edsim.EDProtocol;
import peersim.edsim.EDSimulator;
import peersim.transport.Transport;
import peersim.vector.VectControl;
import univie.cs.psa.msg.TimerMessage;
import univie.cs.psa.msg.ValueWeightMessage;
import univie.cs.psa.utils.AggregationProtocol;
import univie.cs.psa.utils.ProtocolUtils;

/**
 * Cycle-driven Push-Sum.<br/>
 * <br/>
 * In each step, each node sums up the received values and weights of the
 * previous step and then sends half of it's value and half of it's weight to a
 * randomly selected neighbor and to itself. <br/>
 * <br/>
 * This protocol expects the following additional parameters in the
 * configuration file: <blockquote><code>step</code> - the step size<br/>
 * </blockquote>
 * 
 * @author Dario Seidl
 * 
 */
public class PushSumED implements AggregationProtocol, EDProtocol
{
	private static final String PAR_STEP = "step";

	private static int stepSize;

	private double trueValue;
	private double value;
	private double weight;

	private double valueBuffer;
	private double weightBuffer;

	/**
	 * The standard constructor called by the simulator, reading parameters from
	 * the configuration file.
	 * 
	 * @param prefix
	 *            The prefix for this control in the configuration file.
	 */
	public PushSumED(String prefix)
	{
		stepSize = Configuration.getInt(prefix + "." + PAR_STEP);
	}

	@Override
	public void processEvent(Node self, int protocolID, Object event)
	{
		//a timer message signalling the start of a new step
		if (event instanceof TimerMessage)
		{
			Node neighbor = ProtocolUtils.getRandomNeighbor(self, protocolID);

			if (neighbor != null)
			{
				//sum up
				value = valueBuffer;
				weight = weightBuffer;

				//send to self
				valueBuffer = value / 2;
				weightBuffer = weight / 2;

				//send to neighbor
				Transport transport = (Transport) self.getProtocol(FastConfig.getTransport(protocolID));
				transport.send(self, neighbor, new ValueWeightMessage(value / 2, weight / 2), protocolID);

			}

			//schedule a timer message for the next step
			EDSimulator.add(stepSize, new TimerMessage(), self, protocolID);

		}
		//a message from a neighbor
		else if (event instanceof ValueWeightMessage)
		{
			ValueWeightMessage message = (ValueWeightMessage) event;

			valueBuffer += message.getValue();
			weightBuffer += message.getWeight();
		}
	}

	/**
	 * Returns the unmodified value of the node.
	 */
	@Override
	public double getTrueValue()
	{
		return trueValue;
	}

	/**
	 * Returns the local estimate of the mean value of all nodes in the network.
	 * The estimate is obtained by dividing value by weight.
	 */
	@Override
	public double getEstimate()
	{
		return value / weight;
	}

	/**
	 * Setter to initialize the value of this node. Called by subclasses of
	 * {@link VectControl}.
	 */
	public void initializeValue(double value)
	{
		trueValue = value;
		this.value = value;
		valueBuffer = value;
	}

	/**
	 * Setter to initialize the weight of this node. Called by subclasses of
	 * {@link VectControl}.
	 */
	public void initializeWeight(double weight)
	{
		this.weight = weight;
		weightBuffer = weight;
	}

	@Override
	public String toString()
	{
		return String.format("(%e, %e)", value, weight);
	}

	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
	}
}
