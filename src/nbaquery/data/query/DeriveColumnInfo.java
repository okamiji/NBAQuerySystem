package nbaquery.data.query;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;

public abstract class DeriveColumnInfo
{
	public String deriveColumn;
	public Class<?> deriveClass;
	public Column resultColumn;
	
	public DeriveColumnInfo(String deriveColumn, Class<?> deriveClasses)
	{
		this.deriveClass = deriveClasses;
		this.deriveColumn = deriveColumn;
	}
	
	public abstract void retrieve(Table resultTable);
	
	public abstract void derive(Row resultRow);
	
	public Column getDeriveColumn()
	{
		return resultColumn;
	}
}
