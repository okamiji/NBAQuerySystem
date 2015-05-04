package nbaquery.presentation3.match;

import java.awt.Graphics;

import javax.swing.JPanel;

import nbaquery.data.Table;
import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation3.DropList;
import nbaquery.presentation3.table.DisplayTable;

@SuppressWarnings("serial")
public abstract class CompareMatchSubPanel extends JPanel
{
	protected final NewMatchService matchService;
	
	protected DisplayTable displayTable;
	protected MatchTableModel matchTableModel;
	protected DropList monthSelector;
	
	public boolean shouldRedoQuery = true;
	public boolean shouldReEnter = true;
	
	protected int year;
	protected int month;
	
	public CompareMatchSubPanel(NewMatchService matchService, int width, int height, int componentWidth)
	{
		this.matchService = matchService;
		this.setSize(width, height);
		this.setLayout(null);
		
		matchTableModel = new MatchTableModel((width - componentWidth - 4) / 7 - 1);
		
		displayTable = new DisplayTable(matchTableModel, matchTableModel);
		displayTable.setBounds(2, 22, width - componentWidth - 4, height - 26);
		displayTable.setRowHeight(displayTable.getHeight() / 7);
		this.add(displayTable);
		
		this.monthSelector = new DropList(new String[]{
				"January", "February", "March", "April", "May", "June", "July", "Auguest", 
				"September", "October", "November", "December"})
		{

			@Override
			protected void onSelectionChanged(int index)
			{
				month = index;
				shouldRedoQuery = true;
				fireTableSwitch();
			}
		};
		this.monthSelector.setBounds(width - componentWidth - 6 - 100, 2, 100, 20);
		this.add(monthSelector);
	}
	
	protected void fireTableSwitch()
	{
		this.matchTableModel.switchToDate(year, month);
	}
	
	public void paint(Graphics g)
	{
		if(shouldReEnter)
		{
			reEnter();
			shouldReEnter = false;
		}
		
		if(matchService.shouldRedoQuery(this) || shouldRedoQuery)
		{
			this.matchTableModel.setTable(this.redoQuery());
			fireTableSwitch();
			shouldRedoQuery = false;
		}
		super.paint(g);
	}
	
	protected abstract void reEnter();
	
	protected abstract Table redoQuery();
	
	public void setVisible(boolean v)
	{
		super.setVisible(v);
		if(v) shouldReEnter = true;
	}
}
