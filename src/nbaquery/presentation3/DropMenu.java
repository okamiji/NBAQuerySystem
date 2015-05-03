package nbaquery.presentation3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class DropMenu extends JFrame implements MouseListener, MouseMotionListener
{
	public final Component[] components;
	
	protected int rowHeight = 25;
	protected int width = 125;
	
	protected int currentSelectedItem = -1;
	
	Runnable repaintThread = new Runnable()
	{
		public void run()
		{
			while(DropMenu.this.isVisible()) try
			{
				DropMenu.this.repaint();
				Thread.sleep(100);
			}
			catch(Exception e)
			{
				
			}
		}
	};
	
	public DropMenu(String[] items)
	{
		this(convertPureJLabel(items));
	}
	
	public DropMenu(Component[] components)
	{
		if(components == null || components.length == 0)
			throw new NullPointerException();
		
		super.setUndecorated(true);
		super.setAlwaysOnTop(true);
		this.components = components;
		
		for(Component component : components)
			if(width < component.getWidth()) width = component.getWidth();
		
		super.setSize(width, rowHeight* components.length);
		super.addMouseListener(this);
		super.addWindowFocusListener(new WindowFocusListener()
		{

			@Override
			public void windowGainedFocus(WindowEvent arg0)
			{
				
			}

			@Override
			public void windowLostFocus(WindowEvent arg0)
			{
				DropMenu.this.setVisible(false);
			}
			
		});
		super.add(contentPanel);
	}
	
	public void setVisible(boolean v)
	{
		super.setVisible(v);
		if(v)
		{
			new Thread(this.repaintThread).start();
		}
	}
	
	JPanel contentPanel = new JPanel()
	{
		public void paint(Graphics g)
		{
			super.paint(g);
			for(int i = 0; i < components.length; i ++)
			{
				components[i].setSize(width, rowHeight);
				if(currentSelectedItem == i)
				{
					g.setColor(Color.BLUE);
					g.fillRect(0, rowHeight * i, width, rowHeight);
				}
				components[i].paint(g.create(0, rowHeight * i, width, rowHeight));
			}
		}
	};
	
	protected abstract void onSelectedItem(int itemIndex);
	
	public void popupWindow(int x, int y)
	{
		this.setLocation(x, y);
		this.setVisible(true);
	}
	
	public static void main(String[] arguments)
	{
		new DropMenu(new String[]{"aaa", "bbb", "ccc", "ddd", "eee", "fff"})
		{
			@Override
			protected void onSelectedItem(int itemIndex)
			{
				System.out.println(itemIndex);
			}
		}
		.popupWindow(20, 200);
	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		//DO NOTHING
	}

	@Override
	public void mouseMoved(MouseEvent arg0)
	{
		int y = arg0.getY();
		int index = y / rowHeight;
		if(0 <= index && index < this.components.length)
			this.currentSelectedItem = index;
		
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		this.onSelectedItem(currentSelectedItem);
		this.setVisible(false);
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		this.addMouseMotionListener(this);
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		this.removeMouseMotionListener(this);
		this.currentSelectedItem = -1;
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		
	}
	
	protected static Component[] convertPureJLabel(String[] components)
	{
		if(components == null) return null;
		Component[] returnValue = new Component[components.length];
		for(int i = 0; i < components.length; i ++)
			returnValue[i] = new JLabel(components[i]);
		return returnValue;
	}
}
