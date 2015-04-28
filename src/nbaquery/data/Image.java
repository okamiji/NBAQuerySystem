package nbaquery.data;

import java.io.File;

public class Image
{
	File imageFile;
	public File getImageFile()
	{
		return imageFile;
	}
	
	public String toString()
	{
		if(imageFile == null) return null;
		else return imageFile.getAbsolutePath();
	}
	
	public Image(File file)
	{
		this.imageFile = file;
	}
	
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Image)) return false;
		return ((Image)obj).imageFile.getPath().equals(imageFile.getPath());
	}
	
	public int hashCode()
	{
		return imageFile.getPath().hashCode();
	}
}
