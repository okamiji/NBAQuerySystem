package nbaquery.data;

public interface Row
{
	public Object[] getAttributes();
	
	public Table getDeclaredTable();
}
