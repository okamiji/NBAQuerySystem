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

/* <p><i><b>���İ�</b></i></p>
 * <p>�ṩ�Ա���е������еķ��ʷ���</p>
 * 
 * <p>һ������Ƕ�һ����ѯ����ĳ��������ڹ�ϵ�����ݿ��еı��һ������������еļ�����ɣ�һ���д���һ����ͷ����һ�д���
 * ����е�һ���¼�������ģ���У����Ҳ���ж��������������á�</p>
 * 
 * <p>һ��������ͨ������������á���������Ǳ��ĳ��󹤳���һ�����������ĳһ����񡱵���˼�ǣ�һ��������ļ�ϵͳ�ֻ����ݿ�
 * ϵͳ�Ϲ����������ʵ�ֿ��ܻ᲻ͬ���������ǵĽӿ�������ͬ�ġ��û�Ӧ���ñ��ĳ���ӿڷ��ʱ��</p>
 */

public interface Table extends Iterable<Row>
{
	/**
	 * Retrieve all columns from the tale.
	 * @return columns in the table.
	 */
	public Collection<? extends Column> getColumns();
	
	/**
	 * Retrieve all rows from the table.
	 * <b>Warning: </b>the method iterator() should call this method.
	 * @return rows in the table.
	 */
	public Cursor getRows();
	
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
	//public void renameColumn(String columnName, String newColumnName);
	
	/**
	 * Retrieve get the table host of the table.
	 * @return the host of the table.
	 */
	
	public TableHost getTableHost();
	
	public boolean hasTableChanged(Object accessor);
	
	public String getTableName();
	
	public void registerTrigger(Trigger trigger);
}
