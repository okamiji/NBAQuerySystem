package nbaquery.data.file;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Node;
import nbaquery.launcher.Installer;
import nbaquery.launcher.XmlEventListener;
import nbaquery.launcher.XmlEventNotifier;

public class FileInstaller implements Installer<FileTableHost>
{
	String source = null;
	ArrayList<Class<?>> loaders = new ArrayList<Class<?>>();
	ArrayList<Class<?>> algorithms = new ArrayList<Class<?>>();
	
	@Override
	public FileTableHost install(Node documentNode, Object... params) throws Exception
	{
		XmlEventNotifier notifier = new XmlEventNotifier();
		notifier.registerListener("source", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception {
				source = node.getTextContent();
			}
		});
		notifier.registerListener("loaders", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception {
				parseLoaders(node);
			}
		});
		notifier.registerListener("algorithms", new XmlEventListener()
		{

			@Override
			public void onEncounter(Node node) throws Exception {
				parseAlgorithms(node);
			}
		});
		notifier.parse(documentNode);
		
		return new FileTableHost(new File(source), loaders.toArray(new Class<?>[0]), algorithms.toArray(new Class<?>[0]));
	}

	protected void parseLoaders(Node loaders) throws Exception
	{
		XmlEventNotifier loaderNotifier = new XmlEventNotifier();
		loaderNotifier.registerListener("loader", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception {
				FileInstaller.this.loaders.add(Class.forName(node.getTextContent()));
			}
		});
		loaderNotifier.parse(loaders);
	}
	
	protected void parseAlgorithms(Node algorithms) throws Exception
	{
		XmlEventNotifier algorithmNotifier = new XmlEventNotifier();
		algorithmNotifier.registerListener("algorithm", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception {
				FileInstaller.this.algorithms.add(Class.forName(node.getTextContent()));
			}
		});
		algorithmNotifier.parse(algorithms);
	}
}
