package nbaquery.presentation3;

import java.awt.Graphics;

import javax.swing.JLabel;

import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.PagedDisplayTableModel;

@SuppressWarnings("serial")
public class PageDisplayLabel extends JLabel
{
	DisplayTable table;
	public PageDisplayLabel(DisplayTable table)
	{
		this.table = table;
		this.setToolTipText("<html>还在找翻页按钮?<br>你<b>OUT</b>了！现在都用滚轮翻页！<br>滚轮上滚为上一页，滚轮下滚为下一页。</html>");
		this.setHorizontalAlignment(RIGHT);
	}
	
	public void paint(Graphics g)
	{
		if(table.tableModel instanceof PagedDisplayTableModel)
		{
			PagedDisplayTableModel model = (PagedDisplayTableModel) table.tableModel;
			String format = Integer.toString((model.getPageIndex() + 1)).concat("/")
					.concat(Integer.toString(model.getPageCount()));
			this.setText(format);
		}
		super.paint(g);
	}
}
