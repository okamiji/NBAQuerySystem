package nbaquery.presentation3.table;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import nbaquery.data.Image;

@SuppressWarnings("serial")
public class ImageDisplayLabel extends JLabel
{
	static HashMap<Image, java.awt.Image> buffers = new HashMap<Image, java.awt.Image>();  
	Image image;
	
	public void setImage(Image image)
	{
		this.image = image;
	}
	
	public void paint(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getHeight(), this.getHeight());
		java.awt.Image img = this.getImage();
		if(img == null) return;
		g.drawImage(img , 0, 0, this.getHeight(), this.getHeight(), null);
	}
	
	protected java.awt.Image getImage()
	{
		if(image == null) return null;
		
		if(buffers.containsKey(image)) return buffers.get(image);

		java.awt.Image img = null;
		{
			try
			{
				img = ImageIO.read(image.getImageFile());
			}
			catch(IOException e)
			{
			
			}
			buffers.put(image, img);
		}
		return img;
	}
}
