package nbaquery.data.file;

import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.Trigger;

public class Tuple implements Row
{
	public Table table;
	public Object[] attributes;
	
	@Override
	public Object[] getAttributes()
	{
		return attributes;
	}

	@Override
	public Table getDeclaredTable()
	{
		return table;
	}
	
	public void submit()
	{
		KeywordTable kwTable = (KeywordTable) table;
		for(Trigger trigger : kwTable.triggers)
		{
			if(trigger.checkCondition(this)) continue;
			trigger.doCorrection(this);
		}
	}
}
