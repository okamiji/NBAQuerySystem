package nbaquery.presentation3;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public abstract class DropList extends JLabel
{
	public final String[] selections;
	public final DropMenu dropMenu;
	
	public DropList(String[] selections)
	{
		this.selections = selections;
		dropMenu = new DropMenu(selections)
		{
			@Override
			protected void onSelectedItem(int itemIndex)
			{
				DropList.this.setText(DropList.this.selections[itemIndex]);
				DropList.this.onSelectionChanged(itemIndex);
			}
		};
		
		this.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent me)
			{
				Point loc = me.getLocationOnScreen();
				dropMenu.popupWindow(loc.x, loc.y);
			}
		});
		
		this.setText(selections[0]);
		this.onSelectionChanged(0);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
	}
	
	protected abstract void onSelectionChanged(int index);
}
