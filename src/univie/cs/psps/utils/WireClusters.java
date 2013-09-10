package univie.cs.psps.utils;

import peersim.config.Configuration;
import peersim.dynamics.WireGraph;
import peersim.graph.Graph;
import peersim.graph.UndirectedGraph;

public class WireClusters extends WireGraph
{
	private static final String PAR_CLUSTERS = "clusters";

	private final int clusters;

	public WireClusters(String prefix)
	{
		super(prefix);
		clusters = Configuration.getInt(prefix + "." + PAR_CLUSTERS);
	}

	@Override
	public void wire(Graph g)
	{
		if (g.size() % clusters != 0)
		{
			throw new IllegalArgumentException("Graph size must be divisible by number of clusters.");
		}

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
