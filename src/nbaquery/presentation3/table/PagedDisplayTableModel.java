package nbaquery.presentation3.table;

import nbaquery.data.Row;

public class PagedDisplayTableModel implements DisplayTableModel
{
	protected int pageIndex = 0;
	protected int sectionPerPage = 5;
	
	protected Row[] row;

	public void setPageIndex(int pageIndex)
	{
		if(pageIndex < 0 || pageIndex >= getPageCount()) return;
		this.pageIndex = pageIndex;
	}
	
	public int getPageIndex()
	{
		return this.pageIndex;
	}
	
	public void setSectionPerPage(int sectionPerPage)
	{
		if(sectionPerPage <= 0) return;
		this.sectionPerPage = sectionPerPage;
	}
	
	public int getSectionPerPage()
	{
		return this.sectionPerPage;
	}
	
	@Override
	public Object getValueAt(DisplayTable table, int row, int column)
	{
		return this.row[pageIndex * sectionPerPage + row];
	}

	@Override
	public int getRowCount()
	{
		if(getPageCount() == 0) return 0;
		if(this.pageIndex < getPageCount() - 1) return sectionPerPage;
		return row.length % sectionPerPage;
	}
	
	public int getPageCount()
	{
		if(row == null) return 0;
		return (row.length + sectionPerPage - 1) / sectionPerPage;
	}

	@Override
	public void onRepaint(DisplayTable table)
	{
		
	}
}
