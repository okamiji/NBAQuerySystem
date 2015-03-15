package nbaquery.data;

/**
 * <p>Providing services of modifying and accessing rows in the table.</p>
 * 
 * <p>By calling <i>Column.getAttribute(row)</i>, you could get the 
 * corresponding row in the table, while you could set by calling 
 * <i>Column.setAttribute(row)</i>.</p>
 * 
 * <p>The column could be obtained by calling <i>Table.getColumn(columnName)</i>,
 * or <i>TableHost.getColumn(columnName)</i>. The latter method should pass
 * "tableName.columnName" as parameter if it is ambiguous, or a wrong answer
 * will be returned.</p>
 * 
 * <p>Both column and row have its belonging table, and a column can only get
 * a the attribute of a row if their belonging tables are the same, or a null
 * will be returned.</p>
 * 
 * @author luohaoran
 * @see nbaquery.data.Row
 * @see nbaquery.data.Table
 * @see nbaquery.data.TableHost
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
