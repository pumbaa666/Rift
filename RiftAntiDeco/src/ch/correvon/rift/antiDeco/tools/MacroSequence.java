package ch.correvon.rift.antiDeco.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MacroSequence
{
	public MacroSequence()
	{
		this.initialSequence = new ArrayList<MacroSequenceElement>(50);
	}
	
	public void add(MacroSequenceElement element)
	{
		this.initialSequence.add(element);
	}
	
	public List<MacroSequenceElement> getInitialSequence()
	{
		return this.initialSequence;
	}
	
	public List<MacroSequenceElement> getSequence()
	{
		List<MacroSequenceElement> newSequence = new ArrayList<>(this.initialSequence.size() * 3);

		for(MacroSequenceElement element:this.initialSequence)
			for(int i = 0; i < Helper.getRandomNumberInRange(element.getRepeatStart(), element.getRepeatEnd()); i++)
				newSequence.add(element);
		
		return newSequence;
	}
	
	public List<MacroSequenceElement> getRandomizedSequence()
	{
		List<MacroSequenceElement> newSequence = this.getSequence();
		Collections.shuffle(newSequence);
		return newSequence;
	}
		
	private List<MacroSequenceElement> initialSequence;
}
