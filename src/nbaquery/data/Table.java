package nbaquery.data;

import java.util.Collection;

/**
 * <p>Providing services of accessing rows and columns in the table. </p>
 * 
 * <p>A table is a abstraction of the query result, analogous to a table in the
 * relational database in concept. A table consists of a set of columns and rows. 
 * The column represents a header of the table while the row represents a certain
 * record in the table.
 * In this model, the table also refers to its own host.</p>
 * 
 * <p>A table can only be retrieved through a table host, which is a abstract factory
 * for tables. To say a table is of a certain "Style" means that it operates on either
 * a file system or a database system. The underlying implementation may be different, 
 * but the interface is still the same, however. The user <b>should</b> always access
 * table through interfaces.</p>
 * 
 * @author luohaoran
 * @see nbaquery.data.TableHost
 * @see nbaquery.data.Row
 * @see nbaquery.data.Column
 */

/* <p><i><b>中文版</b></i></p>
 * <p>提供对表格中的行与列的访问服务。</p>
 * 
 * <p>一个表格是对一个查询结果的抽象，类似于关系型数据库中的表格。一个表格由列与行的集合组成，一个列代表一个表头，而一行代表
 * 表格中的一则记录。在这个模型中，表格也持有对其表格宿主的引用。</p>
 * 
 * <p>一个表格可以通过其表格宿主获得。表格宿主是表格的抽象工厂。一个表格是属于某一“风格”的意思是，一个表格在文件系统抑或数据库
 * 系统上工作。具体的实现可能会不同，但是他们的接口总是相同的。用户应该用表格的抽象接口访问表格。</p>
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
	 * If the requested column doesn't exists, a null will be returned.
	 * @param columnName the name of the column to retrieve
	 * @return specific column in the table.
	 */
	public Column getColumn(String columnName);
	
	/**
	 * Rename specific column from the table.<br>
	 * @param columnName the original name of the column
	 * @param newColumnName the new name for the column
	 */
	public void renameColumn(String columnName, String newColumnName);
	
	/**
	 * Retrieve get the table host of the table.
	 * @return the host of the table.
	 */
	
	public TableHost getTableHost();
	
	public boolean hasTableChanged(Object accessor);
}
