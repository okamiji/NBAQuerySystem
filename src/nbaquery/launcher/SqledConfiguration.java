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
		
		Element defaultLoaders = dom.createElement("loaders");
		defaultLoaders.setAttribute("installer", "nbaquery.data.sql.loader.file.FileLoaderInstaller");
		dataConfig.appendChild(defaultLoaders);
		
		Node source = dom.createElement("source");
		source.setTextContent("/home/luohaoran/\u8FED\u4EE3\u4E00\u6570\u636E");
		defaultLoaders.appendChild(source);
		
		String[] defaultLoaderList = new String[]{
				"nbaquery.data.sql.loader.file.PlayerLoader",
		    	"nbaquery.data.sql.loader.file.TeamLoader",
		    	"nbaquery.data.sql.loader.file.MatchLoader"
		};
		for(String defaultLoader : defaultLoaderList)
		{
			Node loader = dom.createElement("loader");
			loader.setTextContent(defaultLoader);
			defaultLoaders.appendChild(loader);
		}
	}
	
	protected void setupLogicConfig(Document dom, Element logicConfig)
	{
		logicConfig.setAttribute("installer", "nbaquery.logic.PipelineInstaller");
		
		
		logicConfig.appendChild(dom.createComment("infrustructure"));
		
		logicConfig.appendChild(makePipelineNode(dom, 
				"match_natural_join_performance", 
				"nbaquery.logic.infrustructure.MatchNaturalJoinPerformance",
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
	
	public static void main(String[] arguments) throws Exception
	{
		new SqledConfiguration(new File("config.xml"));
	}
}
