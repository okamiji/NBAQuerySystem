package nbaquery.data;

import java.io.File;

public class Image
{
	File imageFile;
	public File getImageFile()
	{
		return imageFile;
	}
	
	public Image(File file)
	{
		this.imageFile = file;
	}
}
