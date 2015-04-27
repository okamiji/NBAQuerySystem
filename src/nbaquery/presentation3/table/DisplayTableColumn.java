package nbaquery.presentation3.table;

import java.awt.Component;
import java.awt.Graphics;

public interface DisplayTableColumn
{
	public String getColumnName();

	public int getWidth(Graphics g);

	public Component render(DisplayTable table, Object value, int row, int column);
}
