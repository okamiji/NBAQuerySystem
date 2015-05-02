package nbaquery.presentation3.table;

import java.awt.Component;
import java.awt.Graphics;
import java.util.Date;

import javax.swing.JLabel;

import nbaquery.data.Column;
import nbaquery.data.Image;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.presentation.resource.JSVGComponentResource;

public class DefaultTableColumn implements DisplayTableColumn
{
	public int padding = 20;
	
	public final String columnDisplayName;
	public final String columnName;
	public Column tableColumn;
	
	public DefaultTableColumn(String displayName, String columnName)
	{
		this.columnDisplayName = displayName;
		this.columnName = columnName;
	}
	
	public void setTable(Table table)
	{
		if(this.tableColumn != null) 
			if(table == this.tableColumn.getDeclaringTable()) return;
		this.tableColumn = table.getColumn(columnName);
	}

	@Override
	public String getColumnName()
	{
		return this.columnDisplayName;
	}

	@Override
	public int getWidth(Graphics g)
	{
		return (int)(g.getFontMetrics().getStringBounds(columnDisplayName, g).getWidth() + padding);
	}

	protected JLabel displayComponent = new JLabel();
	protected ImageDisplayLabel imageComponent = new ImageDisplayLabel();
	
	@Override
	public Component render(DisplayTable table, Object value, int row, int column)
	{
		Object object = this.tableColumn.getAttribute(((Row)value));
		if(object == null) displayComponent.setText("");
		else
		{
			displayComponent.setText(object.toString());
			if(!object.getClass().equals(String.class))
				displayComponent.setHorizontalAlignment(JLabel.CENTER);
			
			if(object.getClass().equals(Float.class))
				displayComponent.setText(object.toString().substring(0, 5));
			
			if(object.getClass().equals(Date.class))
			{
				Date date = (Date)object;
				@SuppressWarnings("deprecation")
				String formatted = "%year-%month-%day"
					.replace("%year", Integer.toString(1900 + date.getYear()))
					.replace("%month", Integer.toString(1 + date.getMonth()))
					.replace("%day", Integer.toString(1 + date.getDate()));
				displayComponent.setText(formatted);
			}
			
			if(object.getClass().equals(Image.class))
			{
				Image image = (Image) object;
				if(image.getImageFile().getPath().endsWith(".svg"))
					return JSVGComponentResource.createJSVGComponent(image.toString());
				else
				{
					imageComponent.setImage(image);
					return imageComponent;
				}
			}
		}
		return displayComponent;
	}
}
