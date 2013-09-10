/*
 * Copyright (c) 2013 Faculty of Computer Science, University of Vienna
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package univie.cs.psps.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import peersim.config.Configuration;
import peersim.graph.GraphIO;
import peersim.reports.GraphObserver;

/**
 * An extension of {@link GraphObserver} to save the network graph to a .dot
 * file, intended for plotting with Graphviz.
 * <p>
 * This control expects the following parameters in the configuration file:
 * <p>
 * <blockquote>{@code filename} - the name of the .dot file written by this
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
	 *            the prefix for this control in the configuration file.
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
