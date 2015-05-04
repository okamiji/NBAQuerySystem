package nbaquery.presentation.resource;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.apache.batik.swing.svg.JSVGComponent;

@SuppressWarnings("serial")
public class PaintedJSVG extends Component
{
	Image originalImage;
	protected int width = 300;
	protected int height = 300;
	
	public PaintedJSVG(JSVGComponent jsvgComponent)
	{
		jsvgComponent.repaint();
		jsvgComponent.setSize(width, height);
		originalImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		jsvgComponent.paint(originalImage.getGraphics());
		jsvgComponent.dispose();
	}
	
	int sizeXFlag = 0, sizeYFlag = 0;
	Image currentImage;
	
	public void paint(Graphics g)
	{
		if(sizeXFlag != this.getWidth() || sizeYFlag != this.getHeight())
		{
			sizeXFlag = this.getWidth();
			sizeYFlag = this.getHeight();
			currentImage = originalImage.getScaledInstance(sizeXFlag, sizeYFlag, Image.SCALE_SMOOTH);
		}
		g.drawImage(currentImage, 0, 0, sizeXFlag, sizeYFlag, null);
	}
}
