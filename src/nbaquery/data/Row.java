package nbaquery.data;

/**
 * <p>A certain row in the table, containing certain data of the row.</p>
 * 
 * <p>To get the certain attribute in the row, it's better to perform 
 * <i>Column.getAttribute(row)</i> than call <i>Row.getAttributes()</i> directly.<p>
 * 
 * <p>To set a certain attribute, it's better to perform <i>Column.setAttribute(row)</i>,
 * rather than updating the object array obtained by <i>Row.getAttribute()</i>.</p>
 * @author luohaoran
 * 
 * @see nbaquery.data.Column
 * @see nbaquery.data.Table
 */

public interface Row
{
	public Object[] getAttributes();
	
	public Table getDeclaredTable();
}
