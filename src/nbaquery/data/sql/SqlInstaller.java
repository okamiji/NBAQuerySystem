package nbaquery.data.sql;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import nbaquery.data.TableHost;
import nbaquery.data.sql.query.SqlQueryAlgorithm;
import nbaquery.launcher.Installer;
import nbaquery.launcher.XmlEventListener;
import nbaquery.launcher.XmlEventNotifier;

public class SqlInstaller implements Installer<TableHost>
{
	String host;
	String username;
	String password;
	
	ArrayList<BaseTableConstants> base_tables = new ArrayList<BaseTableConstants>();
	ArrayList<SqlQueryAlgorithm<?>> algorithms = new ArrayList<SqlQueryAlgorithm<?>>();
	
	Installer<?> loaderInstaller;
	Node loaderNode;
	
	@Override
	public TableHost install(Node documentNode, Object... params) throws Exception
	{
		XmlEventNotifier notifier = new XmlEventNotifier();
		notifier.registerListener("database", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception
			{
				XmlEventNotifier database = new XmlEventNotifier();
				database.registerListener("host", new XmlEventListener()
				{
					@Override
					public void onEncounter(Node node) throws Exception {
						host = node.getTextContent();
					}
				});
				database.registerListener("username", new XmlEventListener()
				{
					@Override
					public void onEncounter(Node node) throws Exception {
						username = node.getTextContent();
					}
				});
				database.registerListener("password", new XmlEventListener()
				{
					@Override
					public void onEncounter(Node node) throws Exception {
						password = node.getTextContent();
						if("".equals(password)) password = null;
					}
				});
				database.parse(node);
			}
		});
		
		notifier.registerListener("base_tables", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception {
				XmlEventNotifier base_tables =  new XmlEventNotifier();
				base_tables.registerListener("base_table", new XmlEventListener()
				{
					@Override
					public void onEncounter(Node node) throws Exception {
						Element base_table = (Element) node;
						Class<?> clazz = Class.forName(base_table.getAttribute("class"));
						BaseTableConstants constant = (BaseTableConstants) clazz.getField(base_table.getAttribute("field")).get(null);
						SqlInstaller.this.base_tables.add(constant);
					}
				});
				base_tables.parse(node);
			}
		});
		
		notifier.registerListener("algorithms", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception {
				XmlEventNotifier algorithms = new XmlEventNotifier();
				algorithms.registerListener("algorithm", new XmlEventListener()
				{
					@Override
					public void onEncounter(Node node) throws Exception {
						Element algorithm = (Element) node;
						SqlQueryAlgorithm<?> instance = (SqlQueryAlgorithm<?>) 
								Class.forName(algorithm.getTextContent()).newInstance();
						SqlInstaller.this.algorithms.add(instance);
					}
				});
				algorithms.parse(node);
			}
		});
		
		notifier.registerListener("loaders", new XmlEventListener()
		{
			@Override
			public void onEncounter(Node node) throws Exception {
				Element loaders = (Element) node;
				@SuppressWarnings("unchecked")
				Class<? extends Installer<?>> loaderClazz = (Class<? extends Installer<?>>) Class.forName(loaders.getAttribute("installer"));
				SqlInstaller.this.loaderInstaller = loaderClazz.newInstance();
				SqlInstaller.this.loaderNode = node;
			}
		});
		
		notifier.parse(documentNode);
		
		SqlTableHost tableHost = new SqlTableHost(host, username, password, base_tables.toArray(new BaseTableConstants[0]), algorithms.toArray(new SqlQueryAlgorithm<?>[0]));
		loaderInstaller.install(loaderNode, tableHost);
		return tableHost;
	}
}
