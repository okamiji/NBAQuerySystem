package nbaquery.presentation4.plot;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import nbaquery.data.*;

@SuppressWarnings("serial")
public class DistributionPlot extends Component
{
	private final int intervals;
	
	public static Color chartColor = new Color(1.0f, 1.0f, 1.0f, 0.4f);
	public static Color histroColor = Color.gray;
	public static Color addonColor = nbaquery.presentation3.MainFrame.selectedShadow;
	public static Color shadowColor = nbaquery.presentation3.MainFrame.selectedShadow.brighter();
	public static Color frameColor = Color.black;
	
	public static int horizontalPadding = 0;
	
	public DistributionPlot(int intervals)
	{
		this.intervals = intervals;
		this.plotPercentages = new float[intervals];
		MouseAdapter mouse = new MouseAdapter()
		{
			public void mouseMoved(MouseEvent me)
			{
				currentIndex = DistributionPlot.this.intervals * (me.getX() - horizontalPadding) / (getWidth() - horizontalPadding);
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
		};
		this.addMouseListener(mouse);
	}
	
	private float minimal;
	private float maximal;
	
	private float[] plotPercentages;
	
	private Float[] plotNumbers;
	@SuppressWarnings("unused")
	private Row[] correspondingRows;
	
	private float mean;
	private float deviation;
	
	private int currentIndex = -1;
	
	public void paint(Graphics g)
	{
		g.setColor(chartColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int mainPlotAreaX = horizontalPadding;
		int mainPlotAreaY = 0;
		int mainPlotAreaWidth = getWidth() - horizontalPadding;
		int mainPlotAreaHeight = getHeight();
		
		Graphics mainPlotArea = g.create(mainPlotAreaX, mainPlotAreaY, mainPlotAreaWidth, mainPlotAreaHeight);
		
		int width = mainPlotAreaWidth / intervals;
		for(int i = 0; i < intervals; i ++)
		{
			int positionX = i * mainPlotAreaWidth / intervals;
			if(currentIndex == i)
			{
				mainPlotArea.setColor(shadowColor);
				mainPlotArea.fillRect(positionX, 0, width, mainPlotAreaHeight);
				
				mainPlotArea.setColor(frameColor);
				mainPlotArea.drawString(String.format("%.1f%%", plotPercentages[i] * 100), positionX, (int)(mainPlotAreaHeight * (1 - plotPercentages[i])) - 5);
			
				String upperBound = Float.toString(minimal + (maximal - minimal) * (i + 1) / intervals);
				mainPlotArea.drawString(upperBound.length() < 5? upperBound : upperBound.substring(0, 5), 
						positionX, 125);
				
				mainPlotArea.drawString("~", positionX + width / 2 - 5, 112);
				
				String lowerBound = Float.toString(minimal + (maximal - minimal) * i / intervals);
				mainPlotArea.drawString(lowerBound.length() < 5? lowerBound : lowerBound.substring(0, 5), 
						positionX, 100);
			}
			
			mainPlotArea.setColor(histroColor);
			mainPlotArea.fillRect(positionX, (int)(mainPlotAreaHeight * (1 - plotPercentages[i])),
					width, (int)(mainPlotAreaHeight * plotPercentages[i]));
			mainPlotArea.setColor(frameColor);
			mainPlotArea.drawRect(positionX, (int)(mainPlotAreaHeight * (1 - plotPercentages[i])),
					width, (int)(mainPlotAreaHeight * plotPercentages[i]));
		}
		
		mainPlotArea.setColor(addonColor);
		float meanPercent =  (mean - minimal) / (maximal - minimal);
		mainPlotArea.drawLine((int)(mainPlotAreaWidth * meanPercent), 0, (int)(mainPlotAreaWidth * meanPercent), mainPlotAreaHeight);
		
		mainPlotArea.drawLine(mainPlotAreaWidth - 120, 15, mainPlotAreaWidth - 100, 15);
		mainPlotArea.drawString(" \u03BC = ".concat(Float.toString(mean)), mainPlotAreaWidth - 100, 20);
		
		float deviationPercent = (mean + deviation - minimal) / (maximal - minimal);
		if(deviationPercent <= 1.0f)
		{
			int current = 0;
			while(current < mainPlotAreaHeight)
			{
				mainPlotArea.drawLine((int)(mainPlotAreaWidth * deviationPercent), current,
						(int)(mainPlotAreaWidth * deviationPercent), current + 10);
				current += 20;
			}
		}
		
		deviationPercent = (mean - deviation - minimal) / (maximal - minimal);
		if(deviationPercent >= 0.f)
		{
			int current = 0;
			while(current < mainPlotAreaHeight)
			{
				mainPlotArea.drawLine((int)(mainPlotAreaWidth * deviationPercent), current,
						(int)(mainPlotAreaWidth * deviationPercent), current + 10);
				current += 20;
			}
		}
		
		mainPlotArea.drawLine(mainPlotAreaWidth - 120, 35, mainPlotAreaWidth - 111, 35);
		mainPlotArea.drawLine(mainPlotAreaWidth - 106, 35, mainPlotAreaWidth - 100, 35);
		mainPlotArea.drawString(" \u03C3 = ".concat(Float.toString(deviation)), mainPlotAreaWidth - 100, 40);
		
		mainPlotArea.drawString(" min = ".concat(Float.toString(minimal)), mainPlotAreaWidth - 100, 60);
		mainPlotArea.drawString(" max = ".concat(Float.toString(maximal)), mainPlotAreaWidth - 100, 80);
		
		mainPlotArea.setColor(frameColor);
		mainPlotArea.drawRect(0, 0, mainPlotAreaWidth - 1, mainPlotAreaHeight - 1);
	}

	@SuppressWarnings("unused")
	private Table model;
	@SuppressWarnings("unused")
	private Column column;
	
	public void setModel(Table model, Column column)
	{
		if(model == null) return;
		Class<?> clazz = column.getDataClass();
		boolean isInteger = false;
		if(clazz.equals(Integer.class)) isInteger = true;
		else if(!clazz.equals(Float.class)) isInteger = false;
		
		this.model = model;
		this.column = column;
		
		ArrayList<Float> plotNumbers = new ArrayList<Float>();
		ArrayList<Row> correspondingRows = new ArrayList<Row>();
		
		this.minimal = Float.MAX_VALUE;
		this.maximal = -Float.MAX_VALUE;
		
		double sum = 0f;
		
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
			
			sum += plotNumber;
		}
	
		this.plotNumbers = plotNumbers.toArray(new Float[0]);
		this.correspondingRows = correspondingRows.toArray(new Row[0]);
		this.mean = (float) (sum / this.plotNumbers.length);
		float onePoint = 1.0f / this.plotNumbers.length;
		
		for(int i = 0; i < intervals; i ++) plotPercentages[i] = 0.0f;
		
		double sum_square = 0f;
		for(Float number : this.plotNumbers)
		{
			int intervalIndex = (int)(this.intervals * (number - this.minimal) /(this.maximal - this.minimal));
			if(intervalIndex >= intervals) intervalIndex = intervals - 1;
			this.plotPercentages[intervalIndex] += onePoint;
			sum_square += (number - mean) * (number - mean);
		}
		this.deviation = (float) Math.sqrt(sum_square /(this.plotNumbers.length - 1));
	}
	
}
