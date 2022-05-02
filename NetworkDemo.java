package demo19506;
import java.util.*;
import base.*;

class NetworkDemo extends Network
{
	private ArrayList<Hub> hubs = new ArrayList<>();
	private ArrayList<Highway> highways = new ArrayList<>();
	private ArrayList<Truck> trucks = new ArrayList<>();

	public void add(Hub hub)
	{
		hubs.add(hub);
	}

	public void add(Highway hwy)
	{
		highways.add(hwy);
	}

	public void add(Truck truck)
	{
		trucks.add(truck);
	}

	public void start()
	{
		for(Hub hub: hubs)
		{
			hub.start();
		}
		
		for(Truck truck: trucks)
		{
			truck.start();
		}
	}

	public void redisplay(Display disp)
	{
		for(Hub hub: hubs)
		{
			hub.draw(disp);
		}

		for(Truck truck: trucks)
		{
			truck.draw(disp);
		}

		for(Highway hwy: highways)
		{
			hwy.draw(disp);
		}
	}
	
	protected Hub findNearestHubForLoc(Location loc)
	{
		ArrayList<Hub> nextHubs = new ArrayList<>(hubs);

		//Removing the hub which might overlap with the given location
		for(Hub hub: nextHubs)
		{
			if(hub.getLoc().getX()==loc.getX() && hub.getLoc().getY()==loc.getY())
			{
				nextHubs.remove(hub);
				break;
			}
		}

		//Sorting the hubs to get the nearest Hub
		Collections.sort(nextHubs, new SortNearestHubs(loc));
	
		return nextHubs.get(0);
	}
}

class SortNearestHubs implements Comparator<Hub>
{
	private Location loc;

	SortNearestHubs(Location loc)
	{
		this.loc =loc;
	}

	public int compare(Hub hub1, Hub hub2)
	{
		return loc.distSqrd(hub1.getLoc()) - loc.distSqrd(hub2.getLoc());
	}
}