package nbaquery.data.file;

import nbaquery.data.Cursor;
import nbaquery.data.Row;

public class FileTableCursor implements Cursor
{
	int pointer = 0;
	public final Row[] rows;
	
	public FileTableCursor(Row[] rows)
	{
		this.rows = rows;
	}
	
	@Override
	public boolean hasNext() {
		return pointer < getLength() && pointer >= 0;
	}

	@Override
	public Row next() {
		if(hasNext())
		{
			Row fetched = rows[pointer];
			pointer ++;
			return fetched;
		}
		else return null;
	}

	@Override
	public void absolute(int position) {
		this.pointer = position;
	}

	@Override
	public int getLength() {
		return rows.length;
	}

}
