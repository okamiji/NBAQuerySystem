package nbaquery.data.query;

public class DeriveColumnInfo
{
	public String deriveColumns;
	public Class<?> deriveClasses;
	
	public DeriveColumnInfo(String deriveColumns, Class<?> deriveClasses)
	{
		this.deriveClasses = deriveClasses;
		this.deriveColumns = deriveColumns;
	}
}
