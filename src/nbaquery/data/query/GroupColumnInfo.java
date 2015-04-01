package nbaquery.data.query;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;

public abstract class GroupColumnInfo
{
	public String deriveColumnName;
	public Class<?> deriveColumnClass;
	public Column resultColumn;
	
	public GroupColumnInfo(String deriveColumnName, Class<?> deriveColumnClass)
	{
		this.deriveColumnName = deriveColumnName;
		this.deriveColumnClass = deriveColumnClass;
	}
	
	public abstract void retrieve(Table originalTable, Table resultTable);
	
	public abstract void collapse(Row[] rows, Row resultRow);
	
	public Column getGroupColumn()
	{
		return this.resultColumn;
	}
}
