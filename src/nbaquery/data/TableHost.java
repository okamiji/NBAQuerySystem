package nbaquery.data;

import nbaquery.data.query.Query;

/**
 * <p>The owner of the tables, providing services of creating tables, from system
 * start-up, or user performed query.</p>
 * 
 * <p>Once the system is launched, several tables will be stored in the host, we 
 * would like to call them <b><i>metadata</i></b>, other data in the system could
 * be created by performing queries to the system.</p>
 * 
 * <p>The instantiation of TableHost objects are up to program configurations, and
 * the upper level of the system <b>should only</b> access TableHost objects through
 * this interface.</p>
 * 
 * <p>By performing <i>TableHost.getTable(tableName)</i>, a table matches the name 
 * will be retrieved, or a null will be retrieved if certain table doesn't exists.
 * The case of the table name does not matter.</p>
 * 
 * <p>By performing <i>TableHost.performQuery(query, tableName)</i>, the query will
 * be executed, and the query result is stored in the table host, with the given 
 * tableName parameter. Which means you can retrieve the query result by the name
 * you provided.</p>
 * 
 * <p>By performing <i>TableHost.deleteTable(tableName)</i>, a table naming the given
 * parameter will be deleted.</p>
 * 
 * <b>Warning: </b> making any attempt to replace or to remove a table of metadata,
 * you will be rewarded with a IllegalArgumentException.
 * 
 * <p>By performing <i>TableHost.nextDirtyDataInfo()</i>, a dirty data information
 * will be retrieved, and removed in the table host at the same time. Which means
 * you should process if you retrieve it.
 * 
 * @author luohaoran
 * @see nbaquery.data.Table
 * @see nbaquery.data.DirtyDataInfo
 */

public interface TableHost
{
	/**
	 * @param tableName the name of the table to retrieve.
	 * @return the requested table if exists, or null if not.
	 */
	public Table getTable(String tableName);
	
	public Column getColumn(String columnName);
	
	/**
	 * @param tableName the table to delete.
	 */
	public void deleteTable(String tableName);
	
	/**
	 * Perform a query and store it with the given table name. It will not disappear
	 * until you call <i>TableHost.deleteTable(tableName)</i> or shut down the system.
	 * @param query
	 * @param tableName
	 */
	public void performQuery(Query query, String tableName);
	
	/**
	 * @return the next dirty data information to process, or null if no dirty data or
	 * having finished process.
	 */
	public DirtyDataInfo nextDirtyDataInfo();
}
