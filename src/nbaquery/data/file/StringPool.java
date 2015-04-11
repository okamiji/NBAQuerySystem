package nbaquery.data.file;

import java.util.TreeMap;

public class StringPool
{
	public static final TreeMap<String, String> stringPool = new TreeMap<String, String>();
	
	public static String createSeasonFromPool(String string)
	{
		String pooledString = stringPool.get(string);
		if(pooledString != null) return pooledString;
		else
		{
			stringPool.put(string, string);
			return string;
		}
	}
}
