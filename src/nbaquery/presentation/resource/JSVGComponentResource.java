package nbaquery.presentation.resource;

import java.awt.Component;
import java.io.File;
import java.net.MalformedURLException;
import java.util.TreeMap;

public class JSVGComponentResource
{
	private static final TreeMap<String, Component> svg
		= new TreeMap<String, Component>();
	
	@SuppressWarnings("deprecation")
	public static Component createJSVGComponent(String svgFile)
	{
		Component component;
		if((component = svg.get(svgFile)) == null)
		{
			try
			{
				svg.put(svgFile, component = new CompositeJSVGComponent(new File(svgFile).toURL()));
			}
			catch(MalformedURLException e)
			{
				e.printStackTrace();
			}
		}
		
		return component;
	}
}
