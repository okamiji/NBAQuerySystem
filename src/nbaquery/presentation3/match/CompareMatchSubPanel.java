package nbaquery.presentation3.match;

import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

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
	
	protected JTextField fromSeason = new JTextField(), toSeason = new JTextField();
	
	public CompareMatchSubPanel(NewMatchService matchService, int width, int height, int componentWidth)
	{
		this.matchService = matchService;
		this.setSize(width, height);
		this.setLayout(null);
		
		matchTableModel = new MatchTableModel((width - componentWidth - 4) / 7);
		
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
		
		fromSeason.setBounds(2, 2, 100, 20);
		fromSeason.addFocusListener(new FocusListener()
		{
			boolean shouldTest = false;
			@Override
			public void focusGained(FocusEvent arg0)
			{
				shouldTest = true;
			}

			@Override
			public void focusLost(FocusEvent arg0)
			{
				if(!shouldTest) return;
				shouldTest = false;
				try
				{
					int fromSeasonValue = Integer.parseInt(fromSeason.getText());
					if(month < 6) year = fromSeasonValue + 2001;
					else year = fromSeasonValue + 2000;
				}
				catch(Exception e)
				{
					
				}
				fireTableSwitch();
			}
		});
		this.add(fromSeason);
		
		toSeason.setBounds(104, 2, 100, 20);
		toSeason.addFocusListener(new FocusListener()
		{
			boolean shouldTest = false;
			@Override
			public void focusGained(FocusEvent arg0)
			{
				shouldTest = true;
			}

			@Override
			public void focusLost(FocusEvent arg0)
			{
				if(!shouldTest) return;
				shouldTest = false;
				try
				{
					int toSeasonValue = Integer.parseInt(toSeason.getText());
					if(month < 6) year = toSeasonValue + 2000;
					else year = toSeasonValue + 1999;
				}
				catch(Exception e)
				{
					
				}
				fireTableSwitch();
			}
		});
		this.add(toSeason);
	}
	
	protected void fireTableSwitch()
	{
		if(month < 6)
		{
			int fromSeasonValue = year - 2001;
			int toSeasonValue = year - 2000;
			if(fromSeasonValue > 10)
				this.fromSeason.setText("" + fromSeasonValue);
			else this.fromSeason.setText("0" + fromSeasonValue);
			
			if(toSeasonValue > 10)
				this.toSeason.setText("" + toSeasonValue);
			else this.toSeason.setText("0" + toSeasonValue);
		}
		else
		{
			int fromSeasonValue = year - 2000;
			int toSeasonValue = year - 1999;
			if(fromSeasonValue > 10)
				this.fromSeason.setText("" + fromSeasonValue);
			else this.fromSeason.setText("0" + fromSeasonValue);
			
			if(toSeasonValue > 10)
				this.toSeason.setText("" + toSeasonValue);
			else this.toSeason.setText("0" + toSeasonValue);
		}
		try
		{
		if(this.monthSelector != null) 
			this.monthSelector.setText(this.monthSelector.selections[month]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
