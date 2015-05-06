package nbaquery.presentation3.match;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nbaquery.data.Table;
import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.DropList;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.TableSelectionListener;

@SuppressWarnings("serial")
public abstract class CompareMatchSubPanel extends JPanel
{
	protected DetailedInfoContainer container;
	protected final NewMatchService matchService;
	
	protected DisplayTable displayTable;
	protected MatchTableModel matchTableModel;
	protected DropList monthSelector;
	
	public boolean shouldRedoQuery = true;
	public boolean shouldReEnter = true;
	
	protected int year;
	protected int month;

	public final JLabel toSymbol = new JLabel("~");
	public final JLabel seasonText = new JLabel("Èü¼¾");
	
	public final JTextField fromSeason = new JTextField(), toSeason = new JTextField();
	public final MatchEnumerator enumerator;
	
	public CompareMatchSubPanel(DetailedInfoContainer container, NewMatchService matchService, int width, int height, int componentWidth, boolean shouldStack)
	{
		this.container = container;
		this.matchService = matchService;
		this.setSize(width, height);
		this.setLayout(null);
		
		matchTableModel = new MatchTableModel((width - componentWidth - 4) / 7);
		
		displayTable = new DisplayTable(matchTableModel, matchTableModel);
		displayTable.setBounds(2, 22, width - componentWidth - 4, height - 26);
		displayTable.setRowHeight(displayTable.getHeight() / 7);
		this.add(displayTable);
		
		this.enumerator = new MatchEnumerator(container, componentWidth, height - 4, false, shouldStack);
		this.enumerator.setLocation(width - componentWidth - 2, 1);
		this.add(enumerator);
		
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
		this.monthSelector.setBounds(width - componentWidth - 8 - 80, 2, 80, 20);
		this.monthSelector.setHorizontalAlignment(DropList.CENTER);
		this.add(monthSelector);
		
		fromSeason.setBounds(2, 2, 30, 20);
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
		fromSeason.setHorizontalAlignment(JTextField.CENTER);
		this.add(fromSeason);
		
		toSymbol.setBounds(34, 2, 20, 20);
		toSymbol.setHorizontalAlignment(JLabel.CENTER);
		this.add(toSymbol);
		
		toSeason.setBounds(56, 2, 30, 20);
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
		toSeason.setHorizontalAlignment(JTextField.CENTER);
		this.add(toSeason);
		
		seasonText.setBounds(88, 2, 30, 20);
		seasonText.setHorizontalAlignment(JLabel.CENTER);
		this.add(seasonText);
		
		//XXX adding table listeners.
		this.displayTable.addMouseWheelListener(new MouseWheelListener()
		{
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0)
			{
				int delta = arg0.getUnitsToScroll() / arg0.getScrollAmount();
				
				month += delta;
				if(month >= 12)
				{
					year += month / 12;
					month %= 12;
				}
				else if(month < 0)
				{
					int monthRemainder = month % 12;
					monthRemainder += 12;
					year += (month - monthRemainder) / 12;
					month = monthRemainder;
				}
				fireTableSwitch();
			}
		});
		
		this.displayTable.addTableSelectionListener(new TableSelectionListener()
		{

			@Override
			public void onSelect(DisplayTable table, int row, int column,
					Object value, Point mousePoint)
			{
				MatchStrip strip = (MatchStrip)value;
				enumerator.setMatchStrip(strip);
			}
			
		});
	}
	
	protected void fireTableSwitch()
	{
		if(month < 6)
		{
			int fromSeasonValue = year - 2001;
			int toSeasonValue = year - 2000;
			if(fromSeasonValue >= 10)
				this.fromSeason.setText("" + fromSeasonValue);
			else this.fromSeason.setText("0" + fromSeasonValue);
			
			if(toSeasonValue >= 10)
				this.toSeason.setText("" + toSeasonValue);
			else this.toSeason.setText("0" + toSeasonValue);
		}
		else
		{
			int fromSeasonValue = year - 2000;
			int toSeasonValue = year - 1999;
			if(fromSeasonValue >= 10)
				this.fromSeason.setText("" + fromSeasonValue);
			else this.fromSeason.setText("0" + fromSeasonValue);
			
			if(toSeasonValue >= 10)
				this.toSeason.setText("" + toSeasonValue);
			else this.toSeason.setText("0" + toSeasonValue);
		}
		
		if(this.monthSelector != null) 
			this.monthSelector.setText(this.monthSelector.selections[month]);
		
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
		if(v && !this.isVisible()) shouldReEnter = true;
		super.setVisible(v);
	}
}
