package nbaquery.presentation3.table;

import nbaquery.data.Cursor;

public class PagedDisplayTableModel implements DisplayTableModel
{
	protected int pageIndex = 0;
	protected int sectionPerPage = 5;
	
	protected Cursor rows;
	
	public void setRow(Cursor rows)
	{
		this.rows = rows;
		this.setPageIndex(pageIndex);
	}

	public void setPageIndex(int pageIndex)
	{
		if(pageIndex < 0) this.pageIndex = 0;
		else if(pageIndex >= getPageCount()) this.pageIndex = getPageCount() - 1;
		else this.pageIndex = pageIndex;
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
		this.rows.absolute(pageIndex * sectionPerPage + row);
		return this.rows.next();
	}

	@Override
	public int getRowCount()
	{
		if(getPageCount() == 0) return 0;
		if(this.pageIndex < getPageCount() - 1) return sectionPerPage;
		return rows.getLength() - sectionPerPage * (getPageCount() - 1);
	}
	
	public int getPageCount()
	{
		if(rows == null) return 0;
		return (rows.getLength() + sectionPerPage - 1) / sectionPerPage;
	}

	@Override
	public void onRepaint(DisplayTable table)
	{
		
	}
}
