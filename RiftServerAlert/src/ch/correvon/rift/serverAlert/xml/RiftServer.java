package ch.correvon.rift.serverAlert.xml;

import ch.correvon.rift.serverAlert.tools.RiftPopulationEnum;

public class RiftServer
{
	public RiftServer()
	{

	}

	@Override public String toString()
	{
		return "name=" + this.name + 
			" online=" + this.online + 
			" locked=" + this.locked + 
			" population=" + this.population + 
			" queued=" + this.queued + 
			" language=" + this.language + 
			" pvp=" + this.pvp + 
			" rp=" + this.rp + 
			" recommend=" + this.recommend;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String mename)
	{
		this.name = mename;
	}

	public boolean isOnline()
	{
		return online;
	}

	public void setOnline(boolean online)
	{
		this.online = online;
	}

	public boolean isLocked()
	{
		return locked;
	}

	public void setLocked(boolean locked)
	{
		this.locked = locked;
	}

	public RiftPopulationEnum getPopulation()
	{
		return population;
	}

	public void setPopulation(RiftPopulationEnum population)
	{
		this.population = population;
	}

	public int getQueued()
	{
		return queued;
	}

	public void setQueued(int queued)
	{
		this.queued = queued;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public boolean isPvp()
	{
		return pvp;
	}

	public void setPvp(boolean pvp)
	{
		this.pvp = pvp;
	}

	public boolean isRp()
	{
		return rp;
	}

	public void setRp(boolean rp)
	{
		this.rp = rp;
	}

	public boolean isRecommend()
	{
		return recommend;
	}

	public void setRecommend(boolean recommend)
	{
		this.recommend = recommend;
	}

	private String name;
	private boolean online;
	private boolean locked;
	private RiftPopulationEnum population;
	private int queued;
	private String language;
	private boolean pvp;
	private boolean rp;
	private boolean recommend;
}
