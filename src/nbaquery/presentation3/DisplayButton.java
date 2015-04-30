package nbaquery.presentation3;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;

import nbaquery.presentation.resource.ImageIconResource;

@SuppressWarnings("serial")
public abstract class DisplayButton extends Component implements MouseListener
{
	public DisplayButton(String idle, String hanging)
	{
		this(idle, hanging, hanging);
	}
	
	public static final int IDLE = 0;
	public static final int HANGING = 1;
	public static final int PRESSED = 2;
	public static final int SIZE_OF_STATE = 3;
	
	int state = IDLE;
	ImageIcon[] icons = new ImageIcon[SIZE_OF_STATE];
	
	public DisplayButton(String idle, String hanging, String pressed)
	{
		this.icons[IDLE] = ImageIconResource.getImageIcon(idle);
		this.icons[HANGING] = ImageIconResource.getImageIcon(hanging);
		this.icons[PRESSED] = ImageIconResource.getImageIcon(pressed);
		this.setSize(this.icons[IDLE].getIconWidth(), this.icons[IDLE].getIconHeight());
		this.addMouseListener(this);
	}
	
	public void paint(Graphics g)
	{
		g.drawImage(this.icons[state].getImage(), 0, 0, null);
	}

	protected abstract void activate();
	
	boolean hasExited = true;
	
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		//this.activate();
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		this.state = HANGING;
		this.hasExited = false;
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		this.state = IDLE;
		this.hasExited = true;
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		this.state = PRESSED;
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		if(hasExited) this.state = IDLE;
		else
		{
			this.activate();
			this.state = HANGING;
		}
	}
}
