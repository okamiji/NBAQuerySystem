package nbaquery.presentation2.addon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class CalendarSelector extends JPanel
{
	public final JTable table = new JTable()
	{
		public boolean isCellEditable(int row, int column)
		{
			return false;
		}
	};
	
	public String[] daysInWeek = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	
	public Color selectedBackground = Color.BLUE;
	public Color selectedForeground = Color.WHITE;

	public Color normalBackground = Color.WHITE;
	public Color normalForeground = Color.BLACK;
	
	public final Calendar calendar = Calendar.getInstance();
	
	public CalendarSelector()
	{
		this.setLayout(new BorderLayout());
		table.setBackground(new Color(0, 0, 0, 0));
		super.add(table.getTableHeader(), BorderLayout.PAGE_START);
		super.add(table, BorderLayout.CENTER);
		
		table.setDefaultRenderer(Object.class, new TableCellRenderer()
		{
			@Override
			public Component getTableCellRendererComponent(JTable arg0, Object arg1,
					boolean arg2, boolean arg3, int arg4, int arg5)
			{
				final Integer value = (Integer) arg0.getValueAt(arg4, arg5);
				if(value != null)
				{
					JLabel label = new JLabel(Integer.toString(value));
					JPanel panel = new JPanel();
					panel.setLayout(null);
					
					label.setFont(table.getFont());
					label.setHorizontalAlignment(JLabel.CENTER);
					panel.add(label);
					label.setLocation(0, 0);
					label.setSize(table.getColumnModel().getColumn(arg5).getWidth(), table.getRowHeight(arg4));
					
					if(arg4 == pointerRow && arg5 == pointerColumn)
					{
						label.setForeground(selectedForeground);
						panel.setBackground(selectedBackground);
					}
					else
					{
						label.setForeground(normalForeground);
						panel.setBackground(normalBackground);
					}
					return panel;
				}
				else return null;
			}
		});
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		
		table.addMouseListener(mouseAdapter);
		table.addMouseMotionListener(mouseAdapter);
	}
	
	MouseAdapter mouseAdapter = new MouseAdapter()
	{
		boolean mouseDown = false;
		public void mousePressed(MouseEvent e)
		{
			mouseEvent(e);
			mouseDown = true;
		}
		
		public void mouseReleased(MouseEvent e)
		{
			mouseDown = false;
		}
		
		public void mouseDragged(MouseEvent e)
		{
			if(mouseDown) mouseEvent(e);
		}
	};
	
	public void mouseEvent(MouseEvent e)
	{
		Point pointer = e.getPoint();
		int pointerRow = table.rowAtPoint(pointer);
		if(!(pointerRow >= 0 && pointerRow < table.getRowCount())) return;  
		
		int pointerColumn = table.columnAtPoint(pointer);
		if(!(pointerColumn >= 0 && pointerColumn < table.getColumnCount())) return; 
			
		Object value = table.getValueAt(pointerRow, pointerColumn);
		if(value != null)
		{
			CalendarSelector.this.pointerRow = pointerRow;
			CalendarSelector.this.pointerColumn = pointerColumn;
			date = (Integer) value;
		}
	}
	
	int pointerRow, pointerColumn;
	int date = 0;
	
	/**
	 * Set the display date of the calendar.
	 * @param year using actual census year (like 2015).
	 * @param month using the offset to January (like February is 1).
	 */
	
	public void setDate(int year, int month)
	{
		pointerRow = pointerColumn = -1;
		DefaultTableModel model = new DefaultTableModel();
		for(String day : daysInWeek) model.addColumn(day);
		
		calendar.set(year, month, 1);
		int firstDayInMonth = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
		
		int thisMonthDate = calendar.get(Calendar.DAY_OF_YEAR);
		calendar.add(Calendar.MONTH, 1);
		int nextMonthDate = calendar.get(Calendar.DAY_OF_YEAR);
		
		int monthDays = nextMonthDate - thisMonthDate;
		if(monthDays < 0)
		{
			calendar.add(Calendar.DATE, -1);
			monthDays += calendar.get(Calendar.DAY_OF_YEAR);
		}
	
		monthDays -= 1;
		
		int actualCounter = 0;
		while(actualCounter < monthDays + firstDayInMonth)
		{
			Object[] sevenDay = new Object[7];
			for(int i = 0; i < 7; i ++)
			{
				int dayInMonth = actualCounter + i - firstDayInMonth;
				if(dayInMonth < 0 || dayInMonth > monthDays) sevenDay[i] = null;
				else sevenDay[i] = dayInMonth + 1;
			}
			actualCounter += 7;
			model.addRow(sevenDay);
		}
		
		table.setModel(model);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		table.setSize(this.getSize());
		table.setRowHeight(this.getSize().height / (table.getRowCount() + 1));
	}
}
