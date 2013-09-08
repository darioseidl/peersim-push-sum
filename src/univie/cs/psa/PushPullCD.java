/*
 * Copyright (c) 2003-2005 The BISON Project
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package univie.cs.psa;

import peersim.cdsim.CDProtocol;
import peersim.core.Node;
import univie.cs.psa.utils.ProtocolUtils;
import univie.cs.psa.utils.AggregationProtocol;

public class PushPullCD implements AggregationProtocol, CDProtocol
{
	private double trueValue;
	private double value;

	public PushPullCD(String prefix)
	{}

	@Override
	public void nextCycle(Node self, int protocolID)
	{
		Node neighbor = ProtocolUtils.getRandomNeighbor(self, protocolID);

		if (neighbor != null)
		{
			PushPullCD neighborProtocol = (PushPullCD) neighbor.getProtocol(protocolID);

			double mean = (this.value + neighborProtocol.value) / 2;
			this.value = mean;
			neighborProtocol.value = mean;
		}
	}

	@Override
	public double getTrueValue()
	{
		return trueValue;
	}

	@Override
	public double getValue()
	{
		return value;
	}

	@Override
	public void setValue(double value)
	{
		this.trueValue = value;
		this.value = value;
	}

	@Override
	public double getWeight()
	{
		return Double.NaN;
	}

	@Override
	public void setWeight(double weight)
	{}

	@Override
	public double getEstimate()
	{
		return value;
	}

	@Override
	public Object clone()
	{
		Object o = null;
		try
		{
			o = super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
		return o;
	}
}
