package ch.correvon.rift.helper;

import java.util.List;

import ch.correvon.rift.parsing.subObject.MultiLanguageText;

public class ListHelper
{
	public static MultiLanguageText getExistingMLIfExist(MultiLanguageText ml, List<MultiLanguageText> listML)
	{
		int index = listML.indexOf(ml);
		if(index == -1)
		{
			listML.add(ml);
			return ml;
		}
		return listML.get(index);
	}
	
	public static String printList(List<?> list)
	{
		if(list == null || list.size() == 0)
			return "";
		
		String result = "";
		for(Object o:list)
			result += o + "\n";
		return result;	
	}

}
