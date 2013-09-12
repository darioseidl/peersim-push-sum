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

import peersim.config.Configuration;
import peersim.dynamics.WireGraph;
import peersim.graph.Graph;
import peersim.graph.UndirectedGraph;

/**
 * Creates a network topology of several clusters of nodes, which are fully
 * connected within the cluster, but any two clusters are only connected over a
 * single edge.
 * 
 * @author Dario Seidl
 * 
 */
public class WireClusters extends WireGraph
{
	private static final String PAR_CLUSTERS = "clusters";

	private final int clusters;

	/**
	 * The standard constructor called by the simulator, reading parameters from
	 * the configuration file.
	 */
	public WireClusters(String prefix)
	{
		super(prefix);
		clusters = Configuration.getInt(prefix + "." + PAR_CLUSTERS);
	}

	/**
	 * Adds the edges to the graph.
	 */
	@Override
	public void wire(Graph g)
	{
		if (g.size() % clusters != 0)
			throw new IllegalArgumentException("Graph size must be divisible by the number of clusters.");

		int nodesPerCluster = g.size() / clusters;

		for (int i = 0; i < clusters; i++)
			for (int j = 0; j < clusters; j++)
				if (i != j)
					g.setEdge(i * nodesPerCluster, j * nodesPerCluster);

		for (int i = 0; i < clusters; i++)
			for (int j = 0; j < nodesPerCluster; j++)
				for (int k = 0; k < nodesPerCluster; k++)
					if (j != k)
						g.setEdge(i * nodesPerCluster + j, i * nodesPerCluster + k);

		g = new UndirectedGraph(g);
	}
}
