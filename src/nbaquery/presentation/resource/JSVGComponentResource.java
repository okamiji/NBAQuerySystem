package nbaquery.presentation.resource;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.batik.swing.svg.JSVGComponent;

public class JSVGComponentResource
{
	private static final TreeMap<String, JSVGComponent> svg
		= new TreeMap<String, JSVGComponent>();
	
	@SuppressWarnings("deprecation")
	public static JSVGComponent createJSVGComponent(String svgFile)
	{
		JSVGComponent component;
		if((component = svg.get(svgFile)) == null)
		{
			component = new JSVGComponent(null, false, false);
			if(svgFile != null)
			{
				File f = new File(svgFile);
				try
				{
		            component.loadSVGDocument(f.toURL().toString());
		        }
				catch (IOException ex)
				{
		            ex.printStackTrace();
		        }
			}
			svg.put(svgFile, component);
		}
		component.repaint();
		return component;
	}
}
