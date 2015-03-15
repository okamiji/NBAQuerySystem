package nbaquery.data;

import java.util.Collection;

/**
 * Providing services of accessing rows and columns in the table.
 * @author luohaoran
 */

public interface Table
{
	/**
	 * Retrieve all columns from the tale.
	 * @return columns in the table.
	 */
	public Collection<? extends Column> getColumns();
	
	/**
	 * Retrieve all rows from the table.
	 * @return rows in the table.
	 */
	public Row[] getRows();
	
	/**
	 * Retrieve specific column from the table.<br>
	 * <b>Warnings: </b> it's better to store the retrieved column in a
	 * proper place so that the efficiency will be higher. <br>
	 * If the requested column doesn't exists, null will be returned.
	 * @param columnName the name of the column to retrieve
	 * @return 
	 */
	public Column getColumn(String columnName);
	
	public TableHost getTableHost();
}
