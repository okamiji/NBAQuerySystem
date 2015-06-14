package nbaquery.data.file;

import java.util.ArrayList;
import java.util.TreeMap;

public enum EnumTable
{
	TEAM, PLAYER, MATCHES, QUARTER_SCORE, PERFORMANCE;
	
	private String[] attributes;
	private Class<?>[] dataClasses;
	private EnumAttribute primaryKey;
	
	public String[] getTableAttributes()
	{
		return attributes;
	}
	
	public Class<?>[] getDataClasses()
	{
		return dataClasses;
	}
	
	public EnumAttribute getPrimaryKey()
	{
		return this.primaryKey;
	}
	
	static
	{
		TreeMap<EnumTable, ArrayList<String>> mapperAttribute = new TreeMap<EnumTable, ArrayList<String>>();
		TreeMap<EnumTable, ArrayList<Class<?>>> mapperClass = new TreeMap<EnumTable, ArrayList<Class<?>>>();
		for(EnumTable table : EnumTable.values())
		{
			mapperAttribute.put(table, new ArrayList<String>());
			mapperClass.put(table, new ArrayList<Class<?>>());
		}
		for(EnumAttribute attribute : EnumAttribute.values()) for(int i = 0; i < attribute.entity.length; i ++)
		{
			mapperAttribute.get(attribute.entity[i]).add(attribute.toString().toUpperCase());
			if(attribute.isPrimaryKey[i]) attribute.entity[i].primaryKey = attribute;
			mapperClass.get(attribute.entity[i]).add(attribute.dataClass);
		}
		for(EnumTable table : EnumTable.values())
		{
			table.attributes = mapperAttribute.get(table).toArray(new String[0]);
			table.dataClasses = mapperClass.get(table).toArray(new Class<?>[0]);
		}
	}
}
