package nbaquery.presentation3.table;

import java.util.ArrayList;
import java.util.Iterator;

import nbaquery.data.Table;

public class DefaultTableColumnModel implements DisplayTableColumnModel
{
	public final ArrayList<DefaultTableColumn> columns = new ArrayList<DefaultTableColumn>();
	
	@Override
	public DisplayTableColumn getColumn(int index)
	{
		if(index >= 0 && index < getColumnCount()) return columns.get(index);
		else return null;
	}

	@Override
	public int getColumnCount()
	{
		return columns.size();
	}

	public DefaultTableColumn addColumn(String displayName, String name, int index)
	{
		this.removeColumn(name);
		DefaultTableColumn returned = new DefaultTableColumn(displayName, name);
		this.columns.add(index, returned);
		return returned;
	}
	
	public void removeColumn(String name)
	{
		Iterator<DefaultTableColumn> iterator = this.columns.iterator();
		while(iterator.hasNext())
		{
			DefaultTableColumn column = iterator.next();
			if(column.getColumnName().equalsIgnoreCase(name))
				iterator.remove();
		}
	}
	
	public DefaultTableColumn addColumn(String displayName, String name)
	{
		this.removeColumn(name);
		DefaultTableColumn returned = new DefaultTableColumn(displayName, name);
		this.columns.add(returned);
		return returned;
	}
	
	public void setTable(Table table)
	{
		for(DefaultTableColumn column : columns)
			column.setTable(table);
	}
	
	@Override
	public void onRepaint(DisplayTable table)
	{
		
	}
	
}
