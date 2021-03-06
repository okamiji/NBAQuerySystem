package nbaquery.presentation3.table;

import java.util.ArrayList;
import java.util.Iterator;

import nbaquery.data.Table;

public class DefaultTableColumnModel implements DisplayTableColumnModel
{
	public final ArrayList<DisplayTableColumn> columns = new ArrayList<DisplayTableColumn>();
	
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
		Iterator<DisplayTableColumn> iterator = this.columns.iterator();
		while(iterator.hasNext())
		{
			DisplayTableColumn column = iterator.next();
			if(!(column instanceof DefaultTableColumn)) continue;
			if(((DefaultTableColumn)column).columnName.equalsIgnoreCase(name))
				iterator.remove();
		}
	}
	
	public void addColumn(DisplayTableColumn column, int index)
	{	
		removeColumn(column);
		columns.add(index, column);
	}
	
	public void addColumn(DisplayTableColumn column)
	{	
		removeColumn(column);
		columns.add(column);
	}
	
	public void removeColumn(DisplayTableColumn column)
	{
		if(column instanceof DefaultTableColumn)
			this.removeColumn(((DefaultTableColumn) column).columnName);
		else this.columns.remove(column);
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
		for(DisplayTableColumn column : columns)
			if(column instanceof DefaultTableColumn)
				((DefaultTableColumn) column).setTable(table);
	}
	
	@Override
	public void onRepaint(DisplayTable table)
	{
		
	}
	
}
