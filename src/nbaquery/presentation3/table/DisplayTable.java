package nbaquery.presentation3.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class DisplayTable extends Component
{
	//XXX Row Height.
	protected int rowHeight = 12;
	public void setRowHeight(int rowHeight)
	{
		this.rowHeight = rowHeight;
	}
	
	public int getRowHeight()
	{
		return this.rowHeight;
	}
	
	public final ArrayList<TableSelectionListener> tableSelection = new ArrayList<TableSelectionListener>();
	
	protected final MouseAdapter mouseAdapter = new MouseAdapter()
	{
		public void mousePressed(MouseEvent me)
		{
			Point point = me.getPoint();
			if(point.y <= metricOffset) return;
			
			int rowIndex = (point.y - metricOffset) / (rowHeight + interleave);
			if(rowIndex >= tableModel.getRowCount()) return;
			
			for(int i = 0; i < columnModel.getColumnCount(); i ++)
				if(xBeginOffset[i] <= point.x && point.x <= xBeginOffset[i + 1])
					synchronized(tableSelection)
					{
						for(TableSelectionListener tls : tableSelection)
							tls.onSelect(DisplayTable.this, rowIndex, i);
					}
		}
	};
	{
		this.addMouseListener(mouseAdapter);
	}
	
	public synchronized void addTableSelectionListener(TableSelectionListener tls)
	{
		tableSelection.add(tls);
	}
	
	public synchronized void removeTableSelectionListener(TableSelectionListener tls)
	{
		tableSelection.remove(tls);
	}
	
	
	//XXX Table Models.
	protected DisplayTableModel tableModel;
	protected DisplayTableColumnModel columnModel;
	protected int[] xBeginOffset;
	
	public DisplayTable(DisplayTableModel tableModel, DisplayTableColumnModel columnModel)
	{
		this.tableModel = tableModel;
		this.columnModel = columnModel;
	}
	
	//XXX Display Configuration
	public Color oddBackground = Color.GRAY;
	public Color evenBackground = new Color(0, 0, 0, 0);
	public Color headerDivisionLineColor = Color.BLACK;
	
	public int interleave = 1;
	public int rectDisplace = 3;
	
	protected int metricOffset;
	
	public void paint(Graphics g)
	{
		g.setColor(this.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		int columnCount = columnModel.getColumnCount();
		
		if(xBeginOffset == null || xBeginOffset.length != columnCount + 1)
			xBeginOffset = new int[columnCount + 1];
		
		g.setFont(this.getFont());
		
		int xOffsetCounter = 0;
		int i = 0;
		for(; i < columnCount; i ++)
		{
			xBeginOffset[i] = xOffsetCounter;
			xOffsetCounter += columnModel.getColumn(i).getWidth(g);
		}
		xBeginOffset[i] = xOffsetCounter;
		
		int yOffsetCounter = g.getFontMetrics().getHeight();
		
		for(i = 0; i < columnCount; i ++)
		{
			Rectangle2D rectangle = g.getFontMetrics().getStringBounds(columnModel.getColumn(i).getColumnName(), g);
			int halvedWidth = (int)(rectangle.getWidth() / 2.0);
			int midpoint = (xBeginOffset[i] + xBeginOffset[i + 1]) / 2;
			
			g.setColor(this.getForeground());
			g.drawString(columnModel.getColumn(i).getColumnName(), midpoint - halvedWidth, yOffsetCounter);
			
			g.setColor(headerDivisionLineColor);
			g.drawLine(xBeginOffset[i] + interleave, yOffsetCounter + 2, xBeginOffset[i + 1] - interleave, yOffsetCounter + 2);
			g.drawLine(xBeginOffset[i] + interleave, yOffsetCounter + 3, xBeginOffset[i + 1] - interleave, yOffsetCounter + 3);
		}
		
		yOffsetCounter += 4;
		yOffsetCounter += interleave;
		
		metricOffset = yOffsetCounter;
		
		for(int row = 0; row < tableModel.getRowCount(); row ++)
		{
			if((row & 1) == 0) g.setColor(evenBackground);
			else g.setColor(oddBackground);
			
			g.fillRoundRect(interleave, yOffsetCounter, 
					xBeginOffset[columnModel.getColumnCount()] - 2 *interleave,
					rowHeight, rectDisplace, rectDisplace);
			for(int column = 0; column < columnModel.getColumnCount(); column ++)
			{
				/*
				if((row & 1) == 0) g.setColor(evenBackground);
				else g.setColor(oddBackground);
				
				g.fillRoundRect(xBeginOffset[column] + interleave, yOffsetCounter, 
						xBeginOffset[column + 1] - xBeginOffset[column] - 2 *interleave,
						rowHeight, rectDisplace, rectDisplace);
				*/
	
				Component component = this.columnModel.getColumn(column).render(this, this.tableModel.getValueAt(this, row, column), row, column);
				component.setSize(xBeginOffset[column + 1] - xBeginOffset[column] - 2 *interleave, rowHeight);
				
				component.paint(g.create(xBeginOffset[column] + interleave, yOffsetCounter,
						xBeginOffset[column + 1] - xBeginOffset[column] - 2 *interleave, rowHeight));
			}
			yOffsetCounter += rowHeight + interleave;
		}
	}
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		
		frame.setLayout(null);
		frame.setSize(600, 480);
		DisplayTable dp = new DisplayTable(new DisplayTableModel()
		{

			@Override
			public Object getValueAt(DisplayTable table, int row, int column)
			{
				return row + column;
			}

			@Override
			public int getRowCount()
			{
				return 3;
			}
			
		}, new DisplayTableColumnModel()
		{
			@Override
			public DisplayTableColumn getColumn(int index)
			{
				return new DisplayTableColumn(){

					@Override
					public String getColumnName()
					{
						return "±íÍ·Ãû³Æ";
					}

					@Override
					public int getWidth(Graphics g)
					{
						return (int)(20 + g.getFontMetrics().getStringBounds(getColumnName(), g).getWidth());
					}

					@Override
					public Component render(DisplayTable table, Object value,
							int row, int column)
					{
						JLabel jl = new JLabel(value.toString());
						jl.setHorizontalAlignment(JLabel.CENTER);
						return jl;
					}};
			}

			@Override
			public int getColumnCount()
			{
				return 3;
			}
		});
		dp.setBounds(100, 100, 450, 350);
		frame.add(dp);
		
		dp.setBackground(Color.WHITE);
		dp.evenBackground = Color.WHITE.darker();
		dp.oddBackground = dp.evenBackground.darker();
		
		dp.setFont(new Font("Monospaced", 1, 1).deriveFont(16f).deriveFont(Font.PLAIN));
		
		dp.setForeground(Color.BLUE);
		dp.rowHeight = 20;
		
		dp.addTableSelectionListener(new TableSelectionListener()
		{

			@Override
			public void onSelect(DisplayTable table, int row, int column)
			{
				System.out.println(row + " " + column + " " + table.tableModel.getValueAt(table, row, column));
			}
			
		});
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		while(frame.isDisplayable())
		{
			frame.repaint();
		}
	}
	
}
