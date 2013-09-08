package univie.cs.psa.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import peersim.config.Configuration;
import peersim.graph.GraphIO;
import peersim.reports.GraphObserver;

/**
 * An extension of {@link peersim.reports.GraphObserver} to save the network
 * graph to a .dot file, intended for plotting with Graphviz.<br/>
 * <br/>
 * This control expects the following parameters in the configuration file:
 * <blockquote><code>filename</code> - the name of the .dot file written by this
 * class.<br/>
 * </blockquote>
 * 
 * @author Dario Seidl
 * 
 */
public class GraphWriter extends GraphObserver
{
	private static final String PAR_FILENAME = "filename";

	private final String filename;

	/**
	 * The standard constructor called by the simulator, reading parameters from
	 * the configuration file.
	 * 
	 * @param prefix
	 *            The prefix for this control in the configuration file.
	 */
	public GraphWriter(String prefix)
	{
		super(prefix);
		filename = Configuration.getString(prefix + "." + PAR_FILENAME);
	}

	@Override
	public boolean execute()
	{
		try
		{
			super.updateGraph();
			FileOutputStream fout = new FileOutputStream(filename);
			PrintStream print = new PrintStream(fout);
			GraphIO.writeDOT(super.g, print);
			fout.close();

			return false;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
