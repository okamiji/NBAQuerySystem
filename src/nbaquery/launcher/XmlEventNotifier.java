package nbaquery.launcher;

import java.util.TreeMap;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlEventNotifier
{
	private final TreeMap<String, XmlEventListener> encounter
		= new TreeMap<String, XmlEventListener>();
	
	public void parse(Node node) throws Exception
	{
		NodeList children = node.getChildNodes();
		for(int i = 0; i < children.getLength(); i ++)
		{
			Node current = children.item(i);
			if(current.getNodeType() != Node.ELEMENT_NODE) continue;
			XmlEventListener saxListener = encounter.get(current.getNodeName());
			if(saxListener == null)
				throw new Exception("Undefined node \"" + current.getNodeName() + "\" under node \"" + node.getNodeName() + "\"!");
			saxListener.onEncounter(current);
		}
	}
	
	public void registerListener(String tag, XmlEventListener listener)
	{
		this.encounter.put(tag, listener);
	}
	
	public void unregisterListener(String tag)
	{
		this.encounter.remove(tag);
	}
}
