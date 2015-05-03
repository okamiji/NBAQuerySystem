package nbaquery.presentation.resource;

import java.util.TreeMap;

import javax.swing.ImageIcon;

public final class ImageIconResource
{
	private static final TreeMap<String, ImageIcon> image
		= new TreeMap<String, ImageIcon>();
	
	public static ImageIcon getImageIcon(String imageFile)
	{
		if(imageFile == null) return null;
		ImageIcon img;
		if((img = image.get(imageFile)) == null)
		{
			img = new ImageIcon(imageFile);
			image.put(imageFile, img);
		}
		return img;
	}
}
