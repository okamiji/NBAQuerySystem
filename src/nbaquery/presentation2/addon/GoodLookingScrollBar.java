package nbaquery.presentation2.addon;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JScrollBar;

@SuppressWarnings("serial")
public class GoodLookingScrollBar extends JScrollBar
{
	public static Image scrollSlide;
	
	public static Image upArrow, downArrow;
	
	public GoodLookingScrollBar()
	{
		super();
	}
	
	public void paint(Graphics g)
	{
		//Formula : begin height = value / maximum, end height = value + visible_amount / maximum.
		g.setColor(Color.GREEN);
		int actualHeight = getHeight() - 2 * getWidth();
		
		float beginPosition = 1.0f * getValue() * actualHeight/ getMaximum();
		float scrollHeight = 1.0f * getVisibleAmount() * actualHeight / getMaximum();
		
		if(upArrow != null) g.drawImage(upArrow, 0, 0, getWidth(), getWidth(), null);
		if(scrollSlide != null) g.drawImage(scrollSlide, 0, getWidth() + (int) (beginPosition), this.getWidth(), (int)(scrollHeight), null);
		if(downArrow != null) g.drawImage(downArrow, 0, getWidth() + actualHeight, getWidth(), getWidth(), null);
	}
}
