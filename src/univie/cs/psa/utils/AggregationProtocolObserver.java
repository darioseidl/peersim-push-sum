package univie.cs.psa.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

/**
 * Observes the results of an {@link AggregationProtocol} and determines when to
 * stop the execution of the protocol.<br/>
 * <br/>
 * This control expects the following parameters in the configuration file:
 * <blockquote><code>protocol</code> - the name of protocol to observe<br/>
 * <code>precision</code> - stop the execution when the variance of all
 * estimates is less than or equal to <code>precision</code><br/>
 * <code>plotfile</code> - (optional) name of a file to write the output to. If
 * a plotfile is set, the output will be formatted for Gnuplot.<br/>
 * </blockquote>
 * 
 * @author Dario Seidl
 * 
 */
public class AggregationProtocolObserver implements Control
{
	private static final String PAR_PROTOCOL = "protocol";
	private static final String PAR_PRECISION = "precision";
	private static final String PAR_PLOTFILE = "plotfile";
	private static final String VAR_CLUSTERS = "CLUSTERS";

	private final int protocolID;
	private final double precision;
	private final File plotFile;
	private final int clusters;

	/**
	 * The standard constructor called by the simulator, reading parameters from
	 * the configuration file.
	 * 
	 * @param prefix
	 *            The prefix for this control in the configuration file.
	 */
	public AggregationProtocolObserver(String prefix) throws FileNotFoundException
	{
		protocolID = Configuration.getPid(prefix + "." + PAR_PROTOCOL);
		precision = Configuration.getDouble(prefix + "." + PAR_PRECISION, -1);

		plotFile = (Configuration.contains(prefix + "." + PAR_PLOTFILE)) ? new File(Configuration.getString(prefix + "." + PAR_PLOTFILE))
				: null;

		clusters = Configuration.getInt(VAR_CLUSTERS, 1);
	}

	@Override
	public boolean execute()
	{
		if (plotFile != null)
		{
			return printPlotStats();
		}
		else
		{
			return printDetailedStats();
		}
	}

	private boolean printDetailedStats()
	{
		long time = peersim.core.CommonState.getTime();

		if (time == 0)
		{
			String line = FormattedStatistics.repeatChar('-', 34 + FormattedStatistics.header().length());
			System.out.format("%n%16s %16s %s %n%s%n", "time", "", FormattedStatistics.header(), line);
		}

		FormattedStatistics trueValues = new FormattedStatistics();

		for (int i = 0; i < Network.size(); i++)
		{
			AggregationProtocol protocol = (AggregationProtocol) Network.get(i).getProtocol(protocolID);
			trueValues.addValue(protocol.getTrueValue());
		}

		FormattedStatistics values = new FormattedStatistics();
		FormattedStatistics weights = new FormattedStatistics();
		FormattedStatistics estimates = new FormattedStatistics();
		FormattedStatistics errors = new FormattedStatistics();

		for (int i = 0; i < Network.size(); i++)
		{
			AggregationProtocol protocol = (AggregationProtocol) Network.get(i).getProtocol(protocolID);
			values.addValue(protocol.getValue());
			weights.addValue(protocol.getWeight());
			estimates.addValue(protocol.getEstimate());
			errors.addValue(protocol.getEstimate() - trueValues.getMean());
		}

		System.out.format("%16d %16s %s%n%16s %16s %s%n%16s %16s %s%n%16s %16s %s%n%16s %16s %s%n%n", time, "true:", trueValues, "",
				"value:", values, "", "weight:", weights, "", "estimate:", estimates, "", "errors:", errors);

		return !Double.isInfinite(estimates.getMax()) && estimates.getVariance() <= precision;
	}

	private boolean printPlotStats()
	{
		try
		{
			long time = peersim.core.CommonState.getTime();

			if (time == 0)
			{
				String line = FormattedStatistics.repeatChar('-',
						51 + FormattedStatistics.header().length() + FormattedStatistics.header("(err)").length());
				FileUtils.writeStringToFile(
						plotFile,
						String.format("#%15s %s %s %16s %16s%n#%s%n", "time", FormattedStatistics.header(),
								FormattedStatistics.header("(err)"), "true", "clusters", line), true);
			}

			FormattedStatistics trueValues = new FormattedStatistics();

			for (int i = 0; i < Network.size(); i++)
			{
				AggregationProtocol protocol = (AggregationProtocol) Network.get(i).getProtocol(protocolID);
				trueValues.addValue(protocol.getTrueValue());
			}

			FormattedStatistics estimates = new FormattedStatistics();
			FormattedStatistics errors = new FormattedStatistics();

			for (int i = 0; i < Network.size(); i++)
			{
				AggregationProtocol protocol = (AggregationProtocol) Network.get(i).getProtocol(protocolID);
				estimates.addValue(protocol.getEstimate());
				errors.addValue(protocol.getEstimate() - trueValues.getMean());
			}

			boolean stop = !Double.isInfinite(estimates.getMax()) && estimates.getVariance() <= precision;

			if (stop)
			{
				FileUtils.writeStringToFile(plotFile,
						String.format("%16d %s %s %16e %16d%n", time, estimates, errors, trueValues.getMean(), clusters), true);
			}

			return stop;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
