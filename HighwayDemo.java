package demo19506;

import java.util.LinkedList;
import base.*;

class HighwayDemo extends Highway 
{
	LinkedList<Truck> trucksHwyQueue = new LinkedList<>();

	@Override
	public synchronized boolean hasCapacity() 
	{
		if(trucksHwyQueue.size() < this.getCapacity())
		{
			return true;
		}
		else return false;
	}

	@Override
	public synchronized boolean add(Truck truck) 
	{
		if(this.hasCapacity())
		{
			trucksHwyQueue.add(truck);
			return true;
		}
		else return false;
	}

	@Override
	public synchronized void remove(Truck truck) 
	{
		trucksHwyQueue.remove(truck);
	}

}
