package nbaquery.presentation4.plot;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

import nbaquery.data.*;

@SuppressWarnings("serial")
public class DistributionPlot extends Component
{
	private final int intervals;
	
	public static Color chartColor = new Color(1.0f, 1.0f, 1.0f, 0.4f);
	public static Color HistroColor = Color.gray;
	public static Color addonColor = Color.orange;
	public static Color frameColor = Color.black;
	
	public DistributionPlot(int intervals)
	{
		this.intervals = intervals;
		this.plotPercentages = new float[intervals];
	}
	
	private float minimal;
	private float maximal;
	
	private float[] plotPercentages;
	
	private Float[] plotNumbers;
	private Row[] correspondingRows;
	
	private float mean;
	private float deviation;
	
	public void paint(Graphics g)
	{
		g.setColor(chartColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int mainPlotAreaX = 40;
		int mainPlotAreaY = 0;
		int mainPlotAreaWidth = getWidth() - 40;
		int mainPlotAreaHeight = getHeight() - 30;
		
		Graphics mainPlotArea = g.create(mainPlotAreaX, mainPlotAreaY, mainPlotAreaWidth, mainPlotAreaHeight);
		
		for(int i = 0; i < intervals; i ++)
		{
			mainPlotArea.setColor(HistroColor);
			mainPlotArea.fillRect(i * mainPlotAreaWidth / intervals, (int)(mainPlotAreaHeight * (1 - plotPercentages[i])),
					mainPlotAreaWidth / intervals, (int)(mainPlotAreaHeight * plotPercentages[i]));
			mainPlotArea.setColor(frameColor);
			mainPlotArea.drawRect(i * mainPlotAreaWidth / intervals, (int)(mainPlotAreaHeight * (1 - plotPercentages[i])),
					mainPlotAreaWidth / intervals, (int)(mainPlotAreaHeight * plotPercentages[i]));
		}
		
		mainPlotArea.setColor(addonColor);
		float meanPercent =  (mean - minimal) / (maximal - minimal);
		mainPlotArea.drawLine((int)(mainPlotAreaWidth * meanPercent), 0, (int)(mainPlotAreaWidth * meanPercent), mainPlotAreaHeight);
		
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
		
		mainPlotArea.setColor(frameColor);
		mainPlotArea.drawRect(0, 0, mainPlotAreaWidth - 1, mainPlotAreaHeight - 1);
	}

	private Table model;
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
		double sum_square = 0f;
		
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
			sum_square += plotNumber * plotNumber;
		}
	
		this.plotNumbers = plotNumbers.toArray(new Float[0]);
		this.correspondingRows = correspondingRows.toArray(new Row[0]);
		this.mean = (float) (sum / this.plotNumbers.length);
		this.deviation = (float) Math.sqrt(sum_square / (this.plotNumbers.length - 1));
		float onePoint = 1.0f / this.plotNumbers.length;
		
		for(int i = 0; i < intervals; i ++) plotPercentages[i] = 0.0f;
		for(Float number : this.plotNumbers)
		{
			int intervalIndex = (int)(this.intervals * (number - this.minimal) /(this.maximal - this.minimal));
			if(intervalIndex >= intervals) intervalIndex = intervals - 1;
			this.plotPercentages[intervalIndex] += onePoint;
		}
	}
}
