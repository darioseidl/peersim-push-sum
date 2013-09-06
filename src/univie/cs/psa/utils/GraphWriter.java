package univie.cs.psa.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import peersim.config.Configuration;
import peersim.graph.GraphIO;
import peersim.reports.GraphObserver;

public class GraphWriter extends GraphObserver
{
	private static final String PAR_FILENAME = "filename";

	private final String filename;

	public GraphWriter(String name)
	{
		super(name);
		filename = Configuration.getString(name + "." + PAR_FILENAME);
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
