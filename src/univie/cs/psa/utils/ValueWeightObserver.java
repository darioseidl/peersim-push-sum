package univie.cs.psa.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class ValueWeightObserver implements Control
{
	private static final String PAR_PROTOCOL = "protocol";
	private static final String PAR_ACCURACY = "accuracy";
	private static final String PAR_PLOTFILE = "plotfile";
	private static final String VAR_CLUSTERS = "CLUSTERS";

	public static void appendStringToFile(File file, String string)
	{
		try
		{
			FileUtils.writeStringToFile(file, string, true);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private final int protocolID;
	private final double accuracy;
	private final File plotFile;
	private final int clusters;

	public ValueWeightObserver(String name) throws FileNotFoundException
	{
		protocolID = Configuration.getPid(name + "." + PAR_PROTOCOL);
		accuracy = Configuration.getDouble(name + "." + PAR_ACCURACY, -1);

		plotFile = (Configuration.contains(name + "." + PAR_PLOTFILE)) ? new File(Configuration.getString(name + "." + PAR_PLOTFILE))
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
			ValueWeight protocol = (ValueWeight) Network.get(i).getProtocol(protocolID);
			trueValues.addValue(protocol.getTrueValue());
		}

		FormattedStatistics values = new FormattedStatistics();
		FormattedStatistics weights = new FormattedStatistics();
		FormattedStatistics estimates = new FormattedStatistics();
		FormattedStatistics errors = new FormattedStatistics();

		for (int i = 0; i < Network.size(); i++)
		{
			ValueWeight protocol = (ValueWeight) Network.get(i).getProtocol(protocolID);
			values.addValue(protocol.getValue());
			weights.addValue(protocol.getWeight());
			estimates.addValue(protocol.getEstimate());
			errors.addValue(protocol.getEstimate() - trueValues.getMean());
		}

		System.out.format("%16d %16s %s%n%16s %16s %s%n%16s %16s %s%n%16s %16s %s%n%16s %16s %s%n%n", time, "true:", trueValues, "",
				"value:", values, "", "weight:", weights, "", "estimate:", estimates, "", "errors:", errors);

		return !Double.isInfinite(estimates.getMax()) && estimates.getVariance() <= accuracy;
	}

	private boolean printPlotStats()
	{
		long time = peersim.core.CommonState.getTime();

		if (time == 0)
		{
			String line = FormattedStatistics.repeatChar('-',
					51 + FormattedStatistics.header().length() + FormattedStatistics.header("(err)").length());
			appendStringToFile(plotFile, String.format("#%15s %s %s %16s %16s%n#%s%n", "time", FormattedStatistics.header(),
					FormattedStatistics.header("(err)"), "true", "clusters", line));
		}

		FormattedStatistics trueValues = new FormattedStatistics();

		for (int i = 0; i < Network.size(); i++)
		{
			ValueWeight protocol = (ValueWeight) Network.get(i).getProtocol(protocolID);
			trueValues.addValue(protocol.getTrueValue());
		}

		FormattedStatistics estimates = new FormattedStatistics();
		FormattedStatistics errors = new FormattedStatistics();

		for (int i = 0; i < Network.size(); i++)
		{
			ValueWeight protocol = (ValueWeight) Network.get(i).getProtocol(protocolID);
			estimates.addValue(protocol.getEstimate());
			errors.addValue(protocol.getEstimate() - trueValues.getMean());
		}

		boolean stop = !Double.isInfinite(estimates.getMax()) && estimates.getVariance() <= accuracy;

		if (stop)
		{
			appendStringToFile(plotFile, String.format("%16d %s %s %16e %16d%n", time, estimates, errors, trueValues.getMean(), clusters));
		}

		return stop;
	}
}
