package nbaquery.launcher;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This file generates configuration file with system
 * default value and parse it into critical configurations.
 * @author luohaoran
 */

public class Configuration
{
	private final Document document;
	public Configuration(File config) throws Exception
	{
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
		
		if(!config.exists())
		{
			this.generateDefaultConfig(domBuilder, config);
			domBuilder = domFactory.newDocumentBuilder();
		}
		
		document = domBuilder.parse(config);
	}
	
	public Document getDocument()
	{
		return this.document;
	}
	
	protected void generateDefaultConfig(DocumentBuilder domBuilder, File config) throws Exception
	{
		Document dom = domBuilder.newDocument();
		Node root = dom.createElement("nbaquerysystem");
		dom.appendChild(root);
		
		Element data = dom.createElement("data");
		this.setupDataConfig(dom, data);
		root.appendChild(data);
		
		Element logic = dom.createElement("logic");
		this.setupLogicConfig(dom, logic);
		root.appendChild(logic);
		
		Element uinterface = dom.createElement("interface");
		this.setupInterfaceConfig(dom, uinterface);
		root.appendChild(uinterface);
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");  
		
		config.createNewFile();
		PrintWriter printWriter = new PrintWriter(new FileWriter(config));
		DOMSource domSource = new DOMSource(dom);
		StreamResult domResult = new StreamResult(printWriter);
		transformer.transform(domSource, domResult);
	}
	
	protected void setupInterfaceConfig(Document dom, Element interfaceConfig)
	{
		interfaceConfig.setAttribute("installer", "nbaquery.presentation3.InterfaceInstaller");
	}
	
	protected void setupLogicConfig(Document dom, Element logicConfig)
	{
		logicConfig.setAttribute("installer", "nbaquery.logic.LogicInstaller");
	}
	
	protected void setupDataConfig(Document dom, Element dataConfig)
	{
		dataConfig.setAttribute("installer", "nbaquery.data.file.FileInstaller");
		
		Node dataSource = dom.createElement("source");
		dataSource.setTextContent("/home/luohaoran/\u8FED\u4EE3\u4E00\u6570\u636E");
		dataConfig.appendChild(dataSource);
		
		Node dataLoaders = dom.createElement("loaders");
		String[] defaultLoaders = new String[]{
				"nbaquery.data.file.loader.MatchNaturalJoinPerformanceLoader",
				"nbaquery.data.file.loader.PlayerLoader",
				"nbaquery.data.file.loader.TeamLoader"
		};
		for(String current : defaultLoaders)
		{
			Node loader = dom.createElement("loader");
			loader.setTextContent(current);
			dataLoaders.appendChild(loader);
		}
		dataConfig.appendChild(dataLoaders);
		
		Node dataAlgorithms = dom.createElement("algorithms");
		String[] defaultAlgorithms = new String[]{
				"nbaquery.data.file.query.DeriveAlgorithm",
				"nbaquery.data.file.query.GroupAlgorithm",
				"nbaquery.data.file.query.NaturalJoinAlgorithm",
				"nbaquery.data.file.query.SelectProjectAlgorithm",
				"nbaquery.data.file.query.SortAlgorithm"
		};
		for(String current : defaultAlgorithms)
		{
			Node algorithm = dom.createElement("algorithm");
			algorithm.setTextContent(current);
			dataAlgorithms.appendChild(algorithm);
		}
		dataConfig.appendChild(dataAlgorithms);
	}
	
	public static void main(String[] arguments) throws Exception
	{
		new Configuration(new File("config.xml"));
	}
}
