package nbaquery.presentation2.addon;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nbaquery.presentation.combobox.ComboBoxFactory;

@SuppressWarnings("serial")
public class DateSelector extends JPanel
{
	public final CalendarSelector calendarSelector;
	public final JPanel seasonSelector;
	
	public final JLabel seasonLabel = new JLabel("Èü¼¾");
	public final JLabel dashLabel = new JLabel(" - ");
	
	public final JTextField fromSeason = new JTextField();
	public final JTextField toSeason = new JTextField();
	
	public final JLabel monthLabel = new JLabel("ÔÂ·Ý");
	public final JComboBox<?> monthCombo
		= ComboBoxFactory.getInstance().createComboBox(0, 0, 0, 0, new String[]{"January", "Febrary", "March", "April", "May", "June", "July", "Auguest", "September", "October", "November", "December"});
	
	@SuppressWarnings("deprecation")
	public DateSelector()
	{
		super.setLayout(new BorderLayout());
		this.calendarSelector = new CalendarSelector();
		super.add(calendarSelector, BorderLayout.CENTER);
		
		this.seasonSelector = new JPanel();
		seasonSelector.add(seasonLabel);
		seasonSelector.add(fromSeason);
		fromSeason.setHorizontalAlignment(JTextField.CENTER);
		seasonSelector.add(dashLabel);
		toSeason.setHorizontalAlignment(JTextField.CENTER);
		seasonSelector.add(toSeason);
		seasonSelector.add(monthLabel);
		seasonSelector.add(monthCombo);
		
		super.add(seasonSelector, BorderLayout.PAGE_START);
		
		Date today = new Date();
		year = today.getYear() + 1900;
		month = today.getMonth();
		
		this.setDisplay();
		
		Integer date = today.getDate();
		for(int r = 0; r < calendarSelector.table.getRowCount(); r ++)
			for(int c = 0; c < calendarSelector.table.getRowCount(); c ++)
		{
			if(date == calendarSelector.table.getValueAt(r, c))
			{
				calendarSelector.pointerRow = r;
				calendarSelector.pointerColumn = c;
				calendarSelector.date = date;
			}
		}
		
		monthCombo.addItemListener(new ItemListener()
		{
			int index = -1;
			@Override
			public void itemStateChanged(ItemEvent arg0)
			{
				if(index != monthCombo.getSelectedIndex())
				{
					index = monthCombo.getSelectedIndex();
					month = monthCombo.getSelectedIndex();
					setDisplay();
				}
			}
		});
		
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
				setDisplay();
			}
		});
		
		
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
				setDisplay();
			}
		});
	}
	
	public void setDisplay()
	{
		if(month < 6)
		{
			this.fromSeason.setText("" + (year - 2001));
			this.toSeason.setText("" + (year - 2000));
		}
		else
		{
			this.fromSeason.setText("" + (year - 2000));
			this.toSeason.setText("" + (year - 1999));
		}
		this.monthCombo.setSelectedIndex(month);
		if(legacyYear != year || legacyMonth != month)
		{
			this.calendarSelector.setDate(year, month);
			legacyYear = year;
			legacyMonth = month;
		}
	}
	
	int year, month;
	int legacyYear, legacyMonth;
	
	public void paint(Graphics g)
	{
		this.seasonSelector.setSize(this.getWidth(), 25);
		seasonLabel.setBounds(2, 0, 30, this.seasonSelector.getHeight());
		fromSeason.setBounds(30, 0, (int) ((this.getWidth() - 70) * 0.3), this.seasonSelector.getHeight());
		dashLabel.setBounds(fromSeason.getX() + fromSeason.getWidth() + 2, 0, 10, this.seasonSelector.getHeight());
		toSeason.setBounds(dashLabel.getX() + dashLabel.getWidth() + 2, 0, (int)((this.getWidth() - 70 ) * 0.3), this.seasonSelector.getHeight());
		monthLabel.setBounds(toSeason.getX() + toSeason.getWidth() + 2, 0, 30, this.seasonSelector.getHeight());
		monthCombo.setBounds(monthLabel.getX() + monthLabel.getWidth() + 2, 0, (int)((this.getWidth() - 70) * 0.4) + 10, this.seasonSelector.getHeight());
		super.paint(g);
	}
}
