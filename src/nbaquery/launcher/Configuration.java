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
		interfaceConfig.setAttribute("installer", "nbaquery.presentation3.PresentationInstaller");
	}
	
	protected void setupLogicConfig(Document dom, Element logicConfig)
	{
		logicConfig.setAttribute("installer", "nbaquery.logic.PipelineInstaller");
		
		
		logicConfig.appendChild(dom.createComment("infrustructure"));
		
		logicConfig.appendChild(makePipelineNode(dom, 
				"match_natural_join_performance", 
				"nbaquery.logic.infrustructure.DirectMatchNaturalJoinPerformance",
				new String[]{})
		);
		logicConfig.appendChild(makePipelineNode(dom,
				"match_team_performance",
				"nbaquery.logic.infrustructure.MatchTeamPerformance",
				new String[]{"match_natural_join_performance"})
		);
		logicConfig.appendChild(makePipelineNode(dom,
				"rival_team_performance",
				"nbaquery.logic.infrustructure.RivalTeamPerformance",
				new String[]{"match_team_performance"})
		);
		logicConfig.appendChild(makePipelineNode(dom,
				"rival_team_natural_join",
				"nbaquery.logic.infrustructure.RivalTeamNaturalJoin",
				new String[]{"rival_team_performance", "match_team_performance"})
		);
		logicConfig.appendChild(makePipelineNode(dom,
				"player_performance",
				"nbaquery.logic.infrustructure.PlayerPerformance",
				new String[]{"match_natural_join_performance", "match_team_performance", "rival_team_performance"})
		);
		
		
		logicConfig.appendChild(dom.createComment("gross_team"));
		
		logicConfig.appendChild(makePipelineNode(dom,
				"gross_team_performance",
				"nbaquery.logic.gross_team.GrossTeamPerformance",
				new String[]{"match_team_performance"})
		);
		logicConfig.appendChild(makePipelineNode(dom,
				"gross_rival_performance",
				"nbaquery.logic.gross_team.GrossRivalPerformance",
				new String[]{"rival_team_performance"})
		);
		logicConfig.appendChild(makePipelineNode(dom,
				"gross_team_natural_join",
				"nbaquery.logic.gross_team.GrossTeamNaturalJoin",
				new String[]{"gross_rival_performance", "gross_team_performance"})
		);
		logicConfig.appendChild(makePipelineNode(dom,
				"gross_team",
				"nbaquery.logic.gross_team.GrossTeam",
				new String[]{"gross_team_natural_join"})
		);
		
		
		logicConfig.appendChild(dom.createComment("gross_player"));
		
		logicConfig.appendChild(makePipelineNode(dom,
				"gross_player_performance",
				"nbaquery.logic.gross_player.GrossPlayerPerformance",
				new String[]{"player_performance"})
		);
		logicConfig.appendChild(makePipelineNode(dom,
				"gross_player",
				"nbaquery.logic.gross_player.GrossPlayer",
				new String[]{"gross_player_performance"})
		);
		
		
		logicConfig.appendChild(dom.createComment("average_team"));
		
		logicConfig.appendChild(makePipelineNode(dom,
				"derived_team_performance",
				"nbaquery.logic.average_team.DerivedTeamPerformance",
				new String[]{"rival_team_natural_join"})
		);
		logicConfig.appendChild(makePipelineNode(dom,
				"average_team",
				"nbaquery.logic.average_team.AverageTeam",
				new String[]{"derived_team_performance"})
		);

		
		logicConfig.appendChild(dom.createComment("average_player"));
		
		logicConfig.appendChild(makePipelineNode(dom,
				"derive_player_performance",
				"nbaquery.logic.average_player.DerivedPlayerPerformance",
				new String[]{"player_performance"})
		);
		logicConfig.appendChild(makePipelineNode(dom,
				"average_player",
				"nbaquery.logic.average_player.AveragePlayer",
				new String[]{"derive_player_performance"})
		);
		
		
		logicConfig.appendChild(dom.createComment("hot_player"));
		
		logicConfig.appendChild(makePipelineNode(dom,
				"hot_player_today_p_s",
				"nbaquery.logic.hot_player_today.HotPlayerTodayPerformanceSelect",
				new String[]{"match_natural_join_performance"})
		);
		logicConfig.appendChild(makePipelineNode(dom,
				"hot_player_today",
				"nbaquery.logic.hot_player_today.HotPlayerToday",
				new String[]{"hot_player_today_p_s"})
		);
		
		
		logicConfig.appendChild(dom.createComment("progress_player"));
		
		logicConfig.appendChild(makePipelineNode(dom,
				"progress_player_group",
				"nbaquery.logic.progress_player.ProgressPlayerGroup",
				new String[]{"match_natural_join_performance"})
		);
		logicConfig.appendChild(makePipelineNode(dom,
				"progress_player",
				"nbaquery.logic.progress_player.ProgressPlayer",
				new String[]{"progress_player_group"})
		);
		
		
		logicConfig.appendChild(dom.createComment("hot_team"));
		
		logicConfig.appendChild(makePipelineNode(dom,
				"hot_team_select",
				"nbaquery.logic.hot_team_today.HotTeamTodayPerformanceSelect",
				new String[]{"match_natural_join_performance"})
		);
		logicConfig.appendChild(makePipelineNode(dom,
				"hot_team_today",
				"nbaquery.logic.hot_team_today.HotTeamToday",
				new String[]{"hot_team_select"})
		);
		
		Element player_service = dom.createElement("player_service");
		player_service.setAttribute("class", "nbaquery.logic.player.NewPlayerServiceAdapter");
		this.createDependenciesUnder(dom, player_service, new String[]{"gross_player", "average_player", "hot_player_today", "progress_player"});
		logicConfig.appendChild(player_service);
		
		Element team_service = dom.createElement("team_service");
		team_service.setAttribute("class", "nbaquery.logic.team.NewTeamServiceAdapter");
		this.createDependenciesUnder(dom, team_service, new String[]{"gross_team", "average_team", "hot_team_today"});
		logicConfig.appendChild(team_service);
	
		Element match_service = dom.createElement("match_service");
		match_service.setAttribute("class", "nbaquery.logic.match.NewMatchServiceAdapter");
		logicConfig.appendChild(match_service);
	}
	
	protected void createDependenciesUnder(Document dom, Node parent, String[] dependencies)
	{
		if(dependencies != null && dependencies.length > 0)
			for(String dependency : dependencies)
			{
				Element dependencyNode = dom.createElement("dependency");
				dependencyNode.setTextContent(dependency);
				parent.appendChild(dependencyNode);
			}
	}
	
	protected Node makePipelineNode(Document dom, String pipelineName, String pipelineClazz, String[] dependencies)
	{
		Element element = dom.createElement("pipeline");
		element.setAttribute("name", pipelineName);
		element.setAttribute("class", pipelineClazz);
		
		this.createDependenciesUnder(dom, element, dependencies);
		return element;
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
				"nbaquery.data.file.query.SortAlgorithm",
				"nbaquery.data.file.query.AliasAlgorithm"
		};
		for(String current : defaultAlgorithms)
		{
			Node algorithm = dom.createElement("algorithm");
			algorithm.setTextContent(current);
			dataAlgorithms.appendChild(algorithm);
		}
		dataConfig.appendChild(dataAlgorithms);
	}
	
	/**
	 * Used For Testing Configuration Format Only!!
	 * @param arguments
	 * @throws Exception
	 */
	public static void main(String[] arguments) throws Exception
	{
		new Configuration(new File("config.xml"));
	}
	
}
