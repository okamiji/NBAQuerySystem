package nbaquery.data.sql.query;

import nbaquery.data.Table;
import nbaquery.data.query.Query;

public abstract class SqlQueryAlgorithm<Q extends Query>
{
	@SuppressWarnings("unchecked")
	public final Table perform(String tableName, Object query)
	{
		try
		{
			return this.perform(tableName, (Q)query);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public abstract Table perform(String tableName, Q query) throws Exception;
	
	public abstract Class<Q> getQueryClass();
}
