package demo19506;

import java.util.*;
import java.util.LinkedList;
import base.*;

public class HubDemo extends Hub 
{
	LinkedList<Truck> trucksHubQueue;
	Hub lastHub;

	public HubDemo(Location loc) 
	{
		super(loc);
		trucksHubQueue = new LinkedList<>();
		
	}

	@Override
	public synchronized boolean add(Truck truck) 
	{
		if(trucksHubQueue.size() < this.getCapacity())
		{
			trucksHubQueue.add(truck);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void remove(Truck removedTruck) 
	{
		if(removedTruck!=null)
		{
			trucksHubQueue.remove(removedTruck);	//Remove the truck from the hub queue
			this.lastHub = removedTruck.getLastHub();
			Hub destHub = Network.getNearestHub(removedTruck.getDest());
			Highway highway = getNextHighway(this,destHub);
			if(highway!=null)
			{
				if(highway.add(removedTruck))
				{
					removedTruck.enter(highway);
				}
			}
		}

	}

	@Override
	public Highway getNextHighway(Hub from, Hub dest) 
	{
		//Getting all outgoing highways from the current hub
		ArrayList<Highway> highways = from.getHighways();

		//Removing the highway connecting currentHub to lastHub the truck just came from
		for(Highway hwy: highways)
		{
			if(hwy.getEnd().equals(this.lastHub))
			{
				highways.remove(hwy);	//Removing it if found
				if(highways.size()==0)
				{
					highways.add(hwy);	//If that's the only possible highway we are adding it back
				}
				break;
			}
		}

		//Sorting highways acc to whose endHub is the one nearest to destinationHub
		ArrayList<Highway> sortHwys = new ArrayList<>(highways);
		Collections.sort(sortHwys, new SortHighways(dest));

		for(Highway hwy: sortHwys)
		{
			if(hwy.hasCapacity())
			{
				//returning the highway closest to destination and which has capacity
				return hwy;	
			}
		}
		return null;
	}

	@Override
	protected void processQ(int deltaT) 
	{
		Truck removedTruck = trucksHubQueue.peek();	//get the head of the queue
		this.remove(removedTruck);
	}

	
}

class SortHighways implements Comparator<Highway>
{
	private Hub destHub;

	SortHighways(Hub dest)
	{
		this.destHub = dest;
	}

	public int compare(Highway hwy1, Highway hwy2)
	{
		return destHub.getLoc().distSqrd(hwy1.getEnd().getLoc()) - destHub.getLoc().distSqrd(hwy2.getEnd().getLoc());
	}
}
