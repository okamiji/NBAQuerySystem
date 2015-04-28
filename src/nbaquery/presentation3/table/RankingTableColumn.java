package nbaquery.presentation3.table;

import java.awt.Component;
import java.awt.Graphics;

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

	JLabel label = new JLabel();
	{
		label.setHorizontalAlignment(JLabel.CENTER);
	}
	
	@Override
	public Component render(DisplayTable table, Object value, int row, int column)
	{
		if(table.tableModel instanceof PagedDisplayTableModel)
		{
			PagedDisplayTableModel theModel = (PagedDisplayTableModel) table.tableModel;
			label.setText(Integer.toString(theModel.getPageIndex() * theModel.getSectionPerPage() + row + 1));
		}
		else label.setText(Integer.toString(row + 1));
		return label;
	}
	
}
