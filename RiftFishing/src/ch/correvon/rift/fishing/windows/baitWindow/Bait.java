package ch.correvon.rift.fishing.windows.baitWindow;

public class Bait
{
	public Bait(String name, int x, int y)
	{
		this(name, x, y, 0, 0, 0, 0);
	}

	public Bait(Bait bait)
	{
		this(bait.getName(), bait.getX(), bait.getY(), bait.getDurationTime(), bait.getDurationNb(), bait.getUsedTime(), bait.getUsedNb());
	}
	
	public Bait(String name, int x, int y, int durationTime, int durationNb, int usedTime, int usedNb)
	{
		this.name = name;
		this.x = x;
		this.y = y;
		this.durationTime = durationTime;
		this.durationNb = durationNb;
		this.usedTime = usedTime;
		this.usedNb = usedNb;
		
		this.startedUse = 0;
		this.restingTime = "";
	}

	@Override public boolean equals(Object bait)
	{
		Bait b = (Bait)bait;
		return b.getName().equals(this.name)
			&& b.getX() == this.x
			&& b.getY() == this.y
			&& b.getDurationNb() == this.durationNb
			&& b.getDurationTime() == this.durationTime;
	}
	
	public boolean use()
	{
		this.restingTime = "";
		
		if(this.durationNb > 0)
		{
			if(this.usedNb >= this.durationNb)
				return false;
			this.usedNb++;
			this.restingTime += "Utilisé " + this.usedNb + " fois sur " + this.durationNb + ". ";
		}
		
		if(this.durationTime != 0)
		{
			long currentUsedTime = (System.currentTimeMillis() - this.startedUse) / 1000; // temps en secondes
			long maxTime = this.durationTime * 60;
			System.out.println("commencé depuis " + currentUsedTime + " sec. Doit durer " + maxTime);
			if(currentUsedTime >= maxTime)
				return false;
			this.restingTime += "Utilisé depuis " + currentUsedTime + " sec, doit durer " + maxTime + "sec. ";
		}
		
		return true;
	}
	
	public String getRestingTime()
	{
		return this.restingTime;
	}
	
	public void reset()
	{
		this.usedNb = 1;
		this.startedUse = System.currentTimeMillis();
	}
	
	@Override public String toString()
	{
		return "name="+this.name+",x="+this.x+",y="+this.y+",durationTime="+this.durationTime+",durationNb="+this.durationNb;
	}

	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getX()
	{
		return x;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public int getDurationTime()
	{
		return durationTime;
	}
	
	public void setDurationTime(int durationTime)
	{
		this.durationTime = durationTime;
	}
	
	public int getDurationNb()
	{
		return durationNb;
	}
	
	public void setDurationNb(int durationNb)
	{
		this.durationNb = durationNb;
	}

	public int getUsedTime()
	{
		return usedTime;
	}

	public void setUsedTime(int usedTime)
	{
		this.usedTime = usedTime;
	}
	

	public int getUsedNb()
	{
		return usedNb;
	}

	public void setUsedNb(int usedNb)
	{
		this.usedNb = usedNb;
	}

	private String name;
	private int x;
	private int y;
	private int durationTime;
	private int durationNb;
	private int usedTime;
	private int usedNb;
	private long startedUse;
	private String restingTime;
}
