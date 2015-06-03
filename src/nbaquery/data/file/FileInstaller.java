package nbaquery.data.file;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import nbaquery.launcher.Installer;

public class FileInstaller implements Installer<FileTableHost>
{
	String source = null;
	ArrayList<Class<?>> loaders = new ArrayList<Class<?>>();
	ArrayList<Class<?>> algorithms = new ArrayList<Class<?>>();
	
	@Override
	public FileTableHost install(Node documentNode, Object... params) throws Exception
	{
		NodeList nodelist = documentNode.getChildNodes();
		for(int i = 0; i < nodelist.getLength(); i ++)
		{
			Node node = nodelist.item(i);
			if(node.getNodeType() == Node.TEXT_NODE) continue;
			if(node.getNodeType() == Node.COMMENT_NODE) continue;
			switch(node.getNodeName())
			{
				case "loaders":
					this.parseLoaders(node);
				break;
				case "algorithms":
					this.parseAlgorithms(node);
				break;
				case "source":
					source = node.getTextContent();
				break;
				default:
					throw new Exception("Unrecognized node \"" + node.getNodeName() + "\" under data!");
			}
		}
		return new FileTableHost(new File(source), loaders.toArray(new Class<?>[0]), algorithms.toArray(new Class<?>[0]));
	}

	protected void parseLoaders(Node loaders) throws Exception
	{
		NodeList nodelist = loaders.getChildNodes();
		for(int i = 0; i < nodelist.getLength(); i ++)
		{
			Node node = nodelist.item(i);
			if(node.getNodeType() == Node.TEXT_NODE) continue;
			if(node.getNodeType() == Node.COMMENT_NODE) continue;
			if(!node.getNodeName().equals("loader")) throw new Exception("Not allowed node \"" + node.getNodeName() + "\" under loaders.");
			this.loaders.add(Class.forName(node.getTextContent()));
		}
	}
	
	protected void parseAlgorithms(Node algorithms) throws Exception
	{
		NodeList nodelist = algorithms.getChildNodes();
		for(int i = 0; i < nodelist.getLength(); i ++)
		{
			Node node = nodelist.item(i);
			if(node.getNodeType() == Node.TEXT_NODE) continue;
			if(node.getNodeType() == Node.COMMENT_NODE) continue;
			if(!node.getNodeName().equals("algorithm")) throw new Exception("Not allowed node \"" + node.getNodeName() + "\" under algorithms.");
			this.algorithms.add(Class.forName(node.getTextContent()));
		}
	}
}
