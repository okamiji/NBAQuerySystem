package nbaquery.presentation3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Date;

import javax.swing.JLabel;

import nbaquery.data.Column;
import nbaquery.data.Row;

@SuppressWarnings("serial")
public class KeyValueDisplay extends Component
{
	String keyName;
	String keyDisplayName;
	
	JLabel keyLabel = new JLabel();
	JLabel valueLabel = new JLabel();
	
	static Color prototypeKeyLabelBackground = Color.BLACK;
	static Color prototypeKeyLabelForeground = Color.WHITE;
	static Color prototypeValueLabelBackground = Color.WHITE;
	static Color prototypeValueLabelForeground = Color.BLACK;
	
	int padding = 1;
	int margin = 20;
	
	public KeyValueDisplay(String keyDisplayName, String keyName)
	{
		this.keyName = keyName;
		this.keyDisplayName = keyDisplayName;
		
		this.keyLabel.setText(keyDisplayName);
		keyLabel.setHorizontalAlignment(JLabel.CENTER);
		keyLabel.setVerticalAlignment(JLabel.CENTER);
		keyLabel.setForeground(prototypeKeyLabelForeground);
		keyLabel.setBackground(prototypeKeyLabelBackground);
		
		valueLabel.setVerticalAlignment(JLabel.CENTER);
		valueLabel.setForeground(prototypeValueLabelForeground);
		valueLabel.setBackground(prototypeValueLabelBackground);
	}
	
	String value = "";
	public void setRow(Row row)
	{
		if(row == null) return;
		Column column = row.getDeclaredTable().getColumn(keyName);
		if(column == null) return;
		Object value = column.getAttribute(row);
		this.value = this.convertValueToString(value);
		this.valueLabel.setText(this.value);
	}
	
	
	@SuppressWarnings("deprecation")
	protected String convertValueToString(Object value)
	{
		if(value == null) return "";
		if(value instanceof Date)
		{
			Date date = (Date) value;
			return String.format("%d - %d - %d", 1900 + date.getYear(), date.getMonth() + 1, date.getDate() + 1);
		}
		else return value.toString();
	}
	
	public void paint(Graphics g)
	{
		g.setColor(valueLabel.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		Font keyFont = keyLabel.getFont();	int width;
		if(keyFont != null) width = (int) g.getFontMetrics(keyFont).getStringBounds(keyDisplayName, g).getWidth();
		else width = (int) g.getFontMetrics().getStringBounds(keyDisplayName, g).getWidth();
		
		this.keyLabel.setSize(width + margin * 2, this.getHeight());
		Graphics keyArea = g.create(0, 0, width + margin * 2, this.getHeight());
		
		keyArea.setColor(keyLabel.getBackground());
		keyArea.fillRect(0, 0, this.keyLabel.getWidth(), this.keyLabel.getHeight());
		this.keyLabel.paint(keyArea);
		
		this.valueLabel.setSize(this.getWidth() - this.keyLabel.getWidth() - padding - 2 * margin, this.getHeight());
		Graphics valueArea = g.create(this.keyLabel.getWidth() + padding + margin, 0,
				this.getWidth() - this.keyLabel.getWidth() - padding, this.getHeight());
		
		this.valueLabel.paint(valueArea);
	}
}
