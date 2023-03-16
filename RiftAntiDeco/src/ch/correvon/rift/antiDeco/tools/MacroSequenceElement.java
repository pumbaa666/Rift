package ch.correvon.rift.antiDeco.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MacroSequenceElement
{
	public MacroSequenceElement(MacroSequenceElementType type, int key, int delayStart, int delayStop)
	{
		this(type, key, delayStart, delayStop, 1, 1);
	}
	
	public MacroSequenceElement(MacroSequenceElementType type, int key, int delayStart, int delayStop, int repeatStart, int repeatEnd)
	{
		this.type = type;
		this.key = key;
		this.delayStart = delayStart;
		this.delayStop = delayStop;
		this.repeatStart = repeatStart;
		this.repeatEnd = repeatEnd;
	}

	public MacroSequenceElement(List<MacroSequenceElement> suite, int repeatStart, int repeatEnd)
	{
		this.type = MacroSequenceElementType.STRING;
		this.suite = new ArrayList<MacroSequenceElement>(suite);
		this.repeatStart = repeatStart;
		this.repeatEnd = repeatEnd;
		Collections.copy(this.suite, suite);
	}
	
	public MacroSequenceElement(MacroSequenceElement element)
	{
		this.type = element.getType();
		this.key = element.getKey();
		this.delayStart = element.getDelayStart();
		this.delayStop = element.getDelayStop();
		this.repeatStart = element.getRepeatStart();
		this.repeatEnd = element.getRepeatEnd();
	}
	
	public MacroSequenceElementType getType()
	{
		return this.type;
	}
	
	public int getKey()
	{
		return this.key;
	}

	public int getDelayStart()
	{
		return this.delayStart;
	}
	
	public void setDelayStart(int delayStart)
	{
		this.delayStart = delayStart;
	}
	
	public int getDelayStop()
	{
		return this.delayStop;
	}
	
	public void setDelayStop(int delayStop)
	{
		this.delayStop = delayStop;
	}
	
	public int getRepeatStart()
	{
		return this.repeatStart;
	}
	
	public int getRepeatEnd()
	{
		return this.repeatEnd;
	}
	
	public List<MacroSequenceElement> getSuite()
	{
		return this.suite;
	}
	
	@Override public String toString()
	{
		if(this.type == MacroSequenceElementType.STRING)
			return ""+this.type+";"+this.getSuite().toString()+";"+this.repeatStart+"~"+this.repeatEnd+"";
		return ""+this.type+";"+this.key+";"+this.delayStart+"~"+this.delayStop+";"+this.repeatStart+"~"+this.repeatEnd+"";
	}
	
	private MacroSequenceElementType type;
	private int key;
	private int delayStart;
	private int delayStop;
	private int repeatStart;
	private int repeatEnd;
	private List<MacroSequenceElement> suite;
	
	public enum MacroSequenceElementType {
		KEYBOARD,
		MOUSE,
		STRING;
	}
}
