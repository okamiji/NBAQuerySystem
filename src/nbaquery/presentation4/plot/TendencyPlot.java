package nbaquery.presentation4.plot;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import nbaquery.data.*;

@SuppressWarnings("serial")
public abstract class TendencyPlot extends Component
{
	public static Color chartColor = new Color(1.0f, 1.0f, 1.0f, 0.4f);
	public static Color lineColor = Color.gray;
	public static Color sampleColor = nbaquery.presentation3.MainFrame.selectedShadow;
	public static Color sampleSelectedColor = nbaquery.presentation3.MainFrame.selectedShadow.brighter();
	public static Color frameColor = Color.black;
	
	public static int horizontalPadding = 10;
	public static int verticalPadding = 10;
	
	
	private float minimal;
	private float maximal;
	

	private Float[] plotNumbers;
	private Row[] correspondingRows;
		
	public void setModel(Table model, Column column)
	{
		if(model == null) return;
		Class<?> clazz = column.getDataClass();
		boolean isInteger = false;
		if(clazz.equals(Integer.class)) isInteger = true;
		else if(!clazz.equals(Float.class)) isInteger = false;
		
		ArrayList<Float> plotNumbers = new ArrayList<Float>();
		ArrayList<Row> correspondingRows = new ArrayList<Row>();
		
		this.minimal = Float.MAX_VALUE;
		this.maximal = -Float.MAX_VALUE;
		
		for(Row row : model)
		{
			float plotNumber;
			Object obj = column.getAttribute(row);
			if(obj == null) continue;
			
			if(isInteger) plotNumber = (float)((Integer)obj);
			else plotNumber = (Float)obj;
			
			if(!Float.isFinite(plotNumber)) continue;
			
			plotNumbers.add(plotNumber);
			correspondingRows.add(row);
			
			if(plotNumber > this.maximal) this.maximal = plotNumber;
			if(plotNumber < this.minimal) this.minimal = plotNumber;
		}
	
		this.plotNumbers = plotNumbers.toArray(new Float[0]);
		this.correspondingRows = correspondingRows.toArray(new Row[0]);
		
		MouseAdapter mouse = new MouseAdapter()
		{
			public void mouseMoved(MouseEvent me)
			{
				float value = TendencyPlot.this.plotNumbers.length * (me.getX() - horizontalPadding) / (getWidth() - horizontalPadding);
				if(value - Math.floor(value) > 0.5f) currentIndex = (int)(value + 0.5f);
				else currentIndex = (int) value;
			}
			
			public void mouseEntered(MouseEvent me)
			{
				addMouseMotionListener(this);
			}
			
			public void mouseExited(MouseEvent me)
			{
				currentIndex = -1;
				removeMouseMotionListener(this);
			}
			
			public void mousePressed(MouseEvent me)
			{
				if(currentIndex >= 0 && currentIndex < TendencyPlot.this.plotNumbers.length)
					TendencyPlot.this.onRowselected(TendencyPlot.this.correspondingRows[currentIndex]);
			}
		};
		this.addMouseListener(mouse);
	}
	
	protected abstract void onRowselected(Row r);
	
	public static int idleSize = 2;
	public static int selectedSize = 4;
	
	int currentIndex = -1;
	
	public void paint(Graphics g)
	{
		g.setColor(chartColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int mainPlotAreaX = horizontalPadding;
		int mainPlotAreaY = verticalPadding;
		int mainPlotAreaWidth = getWidth() - horizontalPadding * 2;
		int mainPlotAreaHeight = getHeight() - verticalPadding * 2;
		
		Graphics mainPlotArea = g.create(mainPlotAreaX, mainPlotAreaY, mainPlotAreaWidth, mainPlotAreaHeight);
		
		for(int i = 0; i < this.plotNumbers.length; i ++)
		{
			int size;
			if(currentIndex != i)
			{
				mainPlotArea.setColor(sampleColor);
				size = idleSize;
			}
			else
			{
				mainPlotArea.setColor(sampleSelectedColor);
				size = selectedSize;
			}
			
			mainPlotArea.fillRect((mainPlotAreaWidth * i)/ (this.plotNumbers.length - 1) - size, 
					(int)(mainPlotAreaHeight * (1 - ((plotNumbers[i] - this.minimal) / (this.maximal - this.minimal)))) - size,
					size * 2, size * 2);
			
			mainPlotArea.setColor(lineColor);
			if(i > 0)
				mainPlotArea.drawLine((mainPlotAreaWidth * (i - 1))/ (this.plotNumbers.length - 1),
						(int)(mainPlotAreaHeight * (1 - ((plotNumbers[i - 1] - this.minimal) / (this.maximal - this.minimal)))),
						(mainPlotAreaWidth * i)/ (this.plotNumbers.length - 1), 
						(int)(mainPlotAreaHeight * (1 - ((plotNumbers[i] - this.minimal) / (this.maximal - this.minimal)))));
			
		}
		
		mainPlotArea.setColor(frameColor);
		mainPlotArea.drawRect(0, 0, mainPlotAreaWidth - 1, mainPlotAreaHeight - 1);
		
		if(currentIndex >= 0)
		{
			String plotNumberString = Float.toString(plotNumbers[currentIndex]);
			Rectangle2D b = g.getFontMetrics().getStringBounds(plotNumberString, g);
			g.setColor(frameColor);
			g.drawString(plotNumberString.length() > 5? plotNumberString.substring(0, 5) : plotNumberString,
				mainPlotAreaX + (int)(mainPlotAreaWidth * (currentIndex)/ (this.plotNumbers.length - 1) - b.getWidth() / 2), 
				mainPlotAreaY + (int)(mainPlotAreaHeight * (1 - ((plotNumbers[currentIndex] - this.minimal) / (this.maximal - this.minimal))) + b.getHeight() / 2));
		}
	}
}
