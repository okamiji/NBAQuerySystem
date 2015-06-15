package nbaquery.data.sql.loader.file;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Node;

import nbaquery.data.sql.loader.file.SqlFileLoader;
import nbaquery.data.sql.SqlTableHost;
import nbaquery.launcher.Installer;
import nbaquery.launcher.XmlEventListener;
import nbaquery.launcher.XmlEventNotifier;

public class FileLoaderInstaller implements Installer<Object>{

	String datasource;
	ArrayList<Class<? extends SqlFileLoader>> loaderClasses = new ArrayList<Class<? extends SqlFileLoader>>();
	@Override
	public Object install(Node documentNode, Object... params) throws Exception {
		SqlTableHost tableHost = (SqlTableHost) params[0];
		XmlEventNotifier notifier = new XmlEventNotifier();
		notifier.registerListener("source", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception {
				datasource = node.getTextContent();
			}
		});
		notifier.registerListener("loader", new XmlEventListener()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void onEncounter(Node node) throws Exception {
				loaderClasses.add((Class<? extends SqlFileLoader>) Class.forName(node.getTextContent()));
			}
		});
		notifier.parse(documentNode);
		
		for(Class<? extends SqlFileLoader> loaderClass : loaderClasses)
		{
			if(!SqlFileLoader.class.isAssignableFrom(loaderClass)) continue;
			final SqlFileLoader loader;
			try
			{
				loader = (SqlFileLoader) loaderClass.getConstructor(SqlTableHost.class).newInstance(tableHost);
				Thread runningThread = new Thread()
				{
					public void run()
					{
						try {
							loader.setRoot(new File(datasource));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				runningThread.start();
				runningThread.join();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				continue;
			}
		}
		
		return null;
	}

}
