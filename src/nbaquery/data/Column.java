package nbaquery.data;

/**
 * Providing services of modifying and accessing rows in the table.
 * @author luohaoran
 */

public interface Column
{
	/**
	 * Getting the tagged name of the column.
	 * return the name of the column
	 */
	public String getColumnName();
	
	/**
	 * Getting the declared class of the column.
	 * @return the data type of the column.
	 */
	public Class<?> getDataClass();
	
	/**
	 * Getting the declared table of the column.
	 * @return which table does the column belong to.
	 */
	public Table getDeclaringTable();
	
	/**
	 * Getting the attribute in the row corresponding to the column.
	 * @param row a specific row in the table.
	 * @return the attribute the column belongs to.
	 */
	public Object getAttribute(Row row);
	
	/**
	 * Setting the attribute in the row corresponding to the column.
	 * @param row a specific row in the table.
	 * @param value the attribute to modify.
	 */
	public void setAttribute(Row row, Object value);
}
