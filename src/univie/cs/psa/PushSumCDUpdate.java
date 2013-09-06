package univie.cs.psa;

import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

public class PushSumCDUpdate implements Control
{
	private static final String PAR_PROT = "protocol";

	private final int protocolID;

	public PushSumCDUpdate(String prefix)
	{
		protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	@Override
	public boolean execute()
	{
		for (int i = 0; i < Network.size(); i++)
		{
			((PushSumCD) Network.get(i).getProtocol(protocolID)).update();
		}

		return false;
	}
}
