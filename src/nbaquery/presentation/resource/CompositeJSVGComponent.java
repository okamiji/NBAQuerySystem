package nbaquery.presentation.resource;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.net.URL;
import java.util.HashMap;

import org.apache.batik.swing.svg.JSVGComponent;

@SuppressWarnings("serial")
public class CompositeJSVGComponent extends Component
{
	URL jsvgPath;
	HashMap<Dimension, JSVGComponent> resources = new HashMap<Dimension, JSVGComponent>();
	
	public CompositeJSVGComponent(URL jsvgPath)
	{
		this.jsvgPath = jsvgPath;
	}
	
	public void paint(Graphics g)
	{
		JSVGComponent currentComponent = resources.get(this.getSize());
		if(currentComponent == null) try
		{
			JSVGComponent jsvgcomponent = new JSVGComponent(null, false, false);
			jsvgcomponent.loadSVGDocument(jsvgPath.toString());
			jsvgcomponent.setSize(this.getSize());
			resources.put(getSize(), jsvgcomponent);
			currentComponent = jsvgcomponent;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		currentComponent.repaint();
		currentComponent.paint(g);
	}
}
