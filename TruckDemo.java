package demo19506;
import java.util.*;
import base.*;

class TruckDemo extends Truck 
{
	private static int currTime = 0;
	private Hub lastHub;
	private Highway currHwy;
	private boolean onHighway;
	private boolean waitingOnHwy;
	private boolean waitingOnHub;
	private int speed;
	private int distTravelled;

	TruckDemo()
	{
		lastHub=null;
		onHighway=false;
		waitingOnHub=false;
		waitingOnHwy=false;
	}

	@Override
	protected void update(int deltaT) 
	{
		currTime = currTime + deltaT;

		if(currTime < this.getStartTime())
		{
			return;
		}
		else
		{
			int currX = this.getLoc().getX();
			int currY = this.getLoc().getY();
			Hub nearestHub = Network.getNearestHub(this.getSource());

			if(currX==this.getSource().getX() && currY==this.getSource().getY())
			{
				if(nearestHub.add(this))
				{
					this.setLoc(nearestHub.getLoc());
					this.waitingOnHub = true;
					this.onHighway = false;
				}
			}
			else 
			{
				Hub destHub = Network.getNearestHub(this.getDest());
				if(currX==destHub.getLoc().getX() && currY==destHub.getLoc().getY())
				{
					this.setLoc(this.getDest());
					this.waitingOnHub = false;
					this.onHighway = false;
					this.waitingOnHwy=false;
					this.lastHub=destHub;
				}
				else if(onHighway)
				{
					int startX = currHwy.getStart().getLoc().getX();
					int startY = currHwy.getStart().getLoc().getY();
					int endX = currHwy.getEnd().getLoc().getX();
					int endY = currHwy.getEnd().getLoc().getY();

					double hypo = Math.sqrt((endY-startY)*(endY-startY)+(endX-startX)*(endX-startX));

					int distanceToTravel = this.speed*deltaT/100;
					int destX, destY;

					destX = currX + (int)(distanceToTravel*(endX-startX)/hypo);
					destY = currY + (int)(distanceToTravel*(endY-startY)/hypo);
						
					
					double hwyLength = Math.sqrt( Math.pow(startX-endX, 2) + Math.pow(startY-endY,2));
					this.distTravelled = this.distTravelled + distanceToTravel;

					if(distTravelled >= hwyLength)
					{
						Hub endHub = this.currHwy.getEnd();
						if(endHub.add(this))
						{
							this.waitingOnHub=true;
							this.onHighway=false;
							this.waitingOnHwy=false;
							this.currHwy.remove(this);
						}
						else
						{
							this.onHighway=false;
							this.waitingOnHwy=true;
						}
						
						this.setLoc(endHub.getLoc());
					}
					else
					{
						Location destLoc = new Location(destX,destY);
						this.setLoc(destLoc);
						this.onHighway = true;
						this.waitingOnHub=false;
						this.waitingOnHwy=false;
						
					}

				}
				else if(waitingOnHwy)
				{
					Hub endHub = this.currHwy.getEnd();
					if(endHub.add(this))
					{
						this.waitingOnHub=true;
						this.onHighway=false;
						this.waitingOnHwy=false;
						this.currHwy.remove(this);
					}
					else
					{
						this.onHighway=false;
						this.waitingOnHwy=true;
					}
					
					this.setLoc(endHub.getLoc());

				}
			}
		}
		
	}

	public Hub getLastHub()
	{
		return lastHub;
	}

	public void enter(Highway hwy)
	{
		this.currHwy = hwy;
		this.waitingOnHub=false;
		this.onHighway = true;
		this.waitingOnHwy=false;
		this.lastHub = hwy.getStart();
		this.speed = hwy.getMaxSpeed();
		this.distTravelled = 0;
	}

	public String getTruckName() {
		return "Truck19506";
	}
}
