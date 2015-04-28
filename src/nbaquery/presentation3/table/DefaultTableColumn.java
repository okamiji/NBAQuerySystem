package nbaquery.presentation3.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import nbaquery.data.Column;
import nbaquery.data.Image;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.presentation.resource.ImageIconResource;
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
	@SuppressWarnings("serial")
	protected JLabel imageComponent = new JLabel()
	{
		public void paint(Graphics g)
		{
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getHeight(), this.getHeight());
			if(this.getIcon() == null) return;
			g.drawImage(((ImageIcon)this.getIcon()).getImage()
					.getScaledInstance(getHeight(), getHeight(), java.awt.Image.SCALE_DEFAULT)
					, 0, 0, null);
		}
	};
	
	@Override
	public Component render(DisplayTable table, Object value, int row, int column)
	{
		Object object = (this.tableColumn.getAttribute(((Row)value)));
		if(object == null) displayComponent.setText("");
		else
		{
			displayComponent.setText(object.toString());
			if(!object.getClass().equals(String.class))
				displayComponent.setHorizontalAlignment(JLabel.CENTER);
			
			if(object.getClass().equals(Image.class))
			{
				Image image = (Image) object;
				if(image.getImageFile().getPath().endsWith(".svg"))
					return JSVGComponentResource.createJSVGComponent(image.toString());
				else
				{
					ImageIcon icon = ImageIconResource.getImageIcon(image.toString());
					imageComponent.setIcon(icon);
					return imageComponent;
				}
			}
		}
		return displayComponent;
	}
}
