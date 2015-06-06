package nbaquery.data;

import java.util.Iterator;

public interface Cursor extends Iterator<Row>
{
	/**
	 * Set the position of the cursor to specific position.
	 * The position starts from zero.
	 */
	public void absolute(int position);
	
	/**
	 * Get the total records of the cursor.
	 * @return the length of the cursor.
	 */
	public int getLength();
}
