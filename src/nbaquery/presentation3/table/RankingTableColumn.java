package nbaquery.presentation3.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public class RankingTableColumn implements DisplayTableColumn
{
	public int size = 30;
	
	@Override
	public String getColumnName()
	{
		return "  ";
	}

	@Override
	public int getWidth(Graphics g)
	{
		return size;
	}

	@SuppressWarnings("serial")
	public final JLabel label = new JLabel()
	{
		{
			this.setHorizontalAlignment(JLabel.CENTER);
			this.setForeground(Color.WHITE);
		}
		
		Color opaque = new Color(0, 0, 0, 0);
		public void paint(Graphics g)
		{
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Color background = this.getBackground();
			if(background != null)
			{
				g.setColor(background);
				g.fillOval(2, 2 + (this.getHeight() - size)/ 2, size - 4, size - 4);
			}
			
			this.setBackground(opaque);
			super.paint(g);
			this.setBackground(background);
		}
	};
	
	public static final Color gold = new Color(0xffd700);
	public static final Color silver = new Color(0xc0c0c0);
	public static final Color copper = new Color(0xd2b48c);
	
	@Override
	public Component render(DisplayTable table, Object value, int row, int column)
	{
		int index; 
		if(table.tableModel instanceof PagedDisplayTableModel)
		{
			PagedDisplayTableModel theModel = (PagedDisplayTableModel) table.tableModel;
			index = theModel.getPageIndex() * theModel.getSectionPerPage() + row + 1;
		}
		else index = row + 1;
		
		label.setText(Integer.toString(index));
		if(index == 1) label.setBackground(gold);
		else if(index == 2) label.setBackground(silver);
		else if(index == 3) label.setBackground(copper);
		else label.setBackground(null);
		
		return label;
	}
	
}