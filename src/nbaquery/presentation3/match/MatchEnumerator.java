package nbaquery.presentation3.match;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JLabel;

import nbaquery.data.Row;
import nbaquery.presentation3.DetailedInfoContainer;

@SuppressWarnings("serial")
public class MatchEnumerator extends Component
{
	public int sectionPerEnumerator = 6;
	int bias = 0;
	final boolean isHorizontal;
	protected MatchComponent[] matchComponents;
	protected DetailedInfoContainer container;
	
	protected JLabel upArrow = new JLabel("¡ü");
	protected JLabel downArrow = new JLabel("¡ý");
	protected JLabel leftArrow = new JLabel("¡û");
	protected JLabel rightArrow = new JLabel("¡ú");
	protected boolean shouldStack;
	
	public MatchEnumerator(DetailedInfoContainer container, int width, int height, boolean isHorizontal, boolean shouldStack)
	{
		this.setSize(width, height);
		this.isHorizontal = isHorizontal;
		this.shouldStack = shouldStack;
		this.container = container;
		
		this.upArrow.setSize(width - 4, 20);
		this.upArrow.setHorizontalAlignment(JLabel.CENTER);
		this.downArrow.setSize(width - 4, 20);
		this.downArrow.setHorizontalAlignment(JLabel.CENTER);
		this.leftArrow.setSize(20, height - 4);
		this.upArrow.setVerticalAlignment(JLabel.CENTER);
		this.rightArrow.setSize(20, height - 4);
		this.downArrow.setVerticalAlignment(JLabel.CENTER);
		
		if(isHorizontal) this.addMouseListener(horizontalListener);
		else this.addMouseListener(verticalListener);
		
		this.addMouseWheelListener(new MouseWheelListener()
		{
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0)
			{
				move(arg0.getUnitsToScroll() / arg0.getScrollAmount());
			}
		});
	}
	
	MouseAdapter verticalListener = new MouseAdapter()
	{
		public void mousePressed(MouseEvent e)
		{
			int y = e.getPoint().y;
			if(y <= 22) move(-1);
			else if(y >= getHeight() - 22) move(1);
			else if(matchComponents != null)
			{
				int index = (int)(((float)(y - 22)) / (getHeight() - 22) * sectionPerEnumerator) + bias;
				if(index >= 0 && index < matchComponents.length)
					container.displayMatchInfo(matchComponents[index].match, shouldStack);
			}
		}
	};
	
	MouseAdapter horizontalListener = new MouseAdapter()
	{
		public void mousePressed(MouseEvent e)
		{
			int x = e.getPoint().x;
			if(x <= 22) move(-1);
			else if(x >= getWidth() - 22) move(1);
			else if(matchComponents != null)
			{
				int index = (int)(((float)(x - 22)) / (getWidth() - 22) * sectionPerEnumerator) + bias;
				if(index >= 0 && index < matchComponents.length)
					container.displayMatchInfo(matchComponents[index].match, shouldStack);
			}
		}
	};
	
	public void move(int delta)
	{
		bias += delta;
		if(this.matchComponents != null)
			if(bias + sectionPerEnumerator > matchComponents.length)
				bias = matchComponents.length - sectionPerEnumerator;
		if(bias < 0) bias = 0;
	}
	
	public void setMatchStrip(MatchStrip strip)
	{
		this.matchComponents = null;
		bias = 0;
		if(strip != null)
		{
			ArrayList<Row> matches = strip.matches;
			if(matches != null) 
			{
				MatchComponent[] components = new MatchComponent[matches.size()];
				for(int i = 0; i < matches.size(); i ++) components[i] = new MatchComponent(container, matches.get(i), shouldStack);
				this.matchComponents = components;
			}
		}
	}
	
	public void paint(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
		
		if(matchComponents != null)
		{
			if(isHorizontal)
			{
				if(bias > 0) this.leftArrow.paint(g.create(2, 2, 20, getHeight() - 4));
				if(bias + sectionPerEnumerator < matchComponents.length)
					this.rightArrow.paint(g.create(getWidth() - 22, 2, 20, getHeight() - 4));
				
				int componentWidth = (getWidth() - 44) / sectionPerEnumerator - 1;
				for(int i = 0; i < sectionPerEnumerator; i ++)
				{
					if(bias + i < matchComponents.length)
					{
						matchComponents[bias + i].setSize(componentWidth, getHeight() - 4);
						matchComponents[bias + i].paint(g.create(24 + (componentWidth) * i, 2, componentWidth, getHeight() - 4));
					}
				}
			}
			else
			{
				if(bias > 0) this.upArrow.paint(g.create(2, 2, getWidth() - 4, 20));
				if(bias + sectionPerEnumerator < matchComponents.length)
					this.downArrow.paint(g.create(2, getHeight() - 22, getWidth() - 4, 20));
				
				//XXX Actual size should be (width - 4, height - 40 - 4)
				//XXX Size of every component should be (width - 4, (height - 44) / sectionPerEnumerator - 1).
				int componentHeight = (getHeight() - 44) / sectionPerEnumerator - 1;
				for(int i = 0; i < sectionPerEnumerator; i ++)
				{
					if(bias + i < matchComponents.length)
					{
						matchComponents[bias + i].setSize(getWidth() - 4, componentHeight);
						matchComponents[bias + i].paint(g.create(2, 24 + (componentHeight) * i, getWidth() - 4, componentHeight));
					}
				}
			}
		}
	}
}
