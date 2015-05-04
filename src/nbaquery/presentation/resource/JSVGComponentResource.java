package nbaquery.presentation.resource;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.batik.swing.svg.JSVGComponent;

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
			JSVGComponent jsvgcomponent = new JSVGComponent(null, false, false);
			if(svgFile != null)
			{
				File f = new File(svgFile);
				try
				{
		            jsvgcomponent.loadSVGDocument(f.toURL().toString());
		        }
				catch (IOException ex)
				{
		            ex.printStackTrace();
		        }
			}
			//svg.put(svgFile, (component = new PaintedJSVG(jsvgcomponent)));
			svg.put(svgFile, component = jsvgcomponent);
		}
		component.repaint();
		return component;
	}
}
