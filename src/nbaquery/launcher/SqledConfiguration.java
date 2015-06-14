package nbaquery.launcher;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SqledConfiguration extends Configuration
{
	public SqledConfiguration(File config) throws Exception {
		super(config);
	}
	
	public void setupDataConfig(Document dom, Element dataConfig)
	{
		dataConfig.setAttribute("installer", "nbaquery.data.sql.SqlInstaller");
		
		Node database = dom.createElement("database");
		dataConfig.appendChild(database);
		
		Node host = dom.createElement("host");
		host.setTextContent("127.0.0.1");
		database.appendChild(host);
		
		Node username = dom.createElement("username");
		username.setTextContent("root");
		database.appendChild(username);
		
		Node password = dom.createElement("password");
		password.setTextContent("");
		database.appendChild(password);
		
		Node base_tables = dom.createElement("base_tables");
		dataConfig.appendChild(base_tables);
		
		String[] fields = new String[]{"player", "team", "match", "quarter_score", "performance"};
		for(String field : fields)
		{
			Element base_table = dom.createElement("base_table");
			base_table.setAttribute("class", "nbaquery.data.sql.BaseTableConstants");
			base_table.setAttribute("field", field);
			base_tables.appendChild(base_table);
		}
		
		Node algorithms = dom.createElement("algorithms");
		dataConfig.appendChild(algorithms);
		
		String[] algorithmsList = new String[]{
				"nbaquery.data.sql.query.AliasingAlgorithm",
				"nbaquery.data.sql.query.DeriveAlgorithm",
				"nbaquery.data.sql.query.GroupAlgorithm",
				"nbaquery.data.sql.query.NaturalJoinAlgorithm",
				"nbaquery.data.sql.query.SelectProjectAlgorithm",
				"nbaquery.data.sql.query.SortAlgorithm",
		};
		for(String algorithm : algorithmsList)
		{
			Element algorithm_node = dom.createElement("algorithm");
			algorithm_node.setTextContent(algorithm);
			algorithms.appendChild(algorithm_node);
		}
	}
	
	public static void main(String[] arguments) throws Exception
	{
		new SqledConfiguration(new File("config.xml"));
	}
}
