package nbaquery.data.file.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import nbaquery.data.file.EnumTable;
import nbaquery.data.file.FileTableColumn;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.KeywordTable;
import nbaquery.data.file.MultivaluedTable;
import nbaquery.data.file.StringPool;
import nbaquery.data.file.Tuple;

/**
 * This class will natural join match and performance on start up.
 * @author luohaoran
 */

public class MatchNaturalJoinPerformanceLoader implements FileLoader
{

	final FileTableHost host;
	
	FileTableColumn identity;
	FileTableColumn season;
	FileTableColumn date;
	FileTableColumn host_abbr;
	FileTableColumn host_score;
	FileTableColumn guest_abbr;
	FileTableColumn guest_score;
	
	FileTableColumn quarter_id;
	FileTableColumn quarter_number;
	FileTableColumn quarter_host_score;
	FileTableColumn quarter_guest_score;
	
	FileTableColumn performance_match_id;
	FileTableColumn game_team;
	FileTableColumn player_name;
	FileTableColumn position;
	FileTableColumn game_minute;
	FileTableColumn game_second;
	FileTableColumn shoot_score;
	FileTableColumn shoot_count;
	FileTableColumn three_shoot_score;
	FileTableColumn three_shoot_count;
	FileTableColumn foul_shoot_score;
	FileTableColumn foul_shoot_count;
	FileTableColumn attack_board;
	FileTableColumn defence_board;
	FileTableColumn total_board;
	FileTableColumn assist;
	FileTableColumn steal;
	FileTableColumn cap;
	FileTableColumn miss;
	FileTableColumn foul;
	FileTableColumn self_score;
	
	public MatchNaturalJoinPerformanceLoader(FileTableHost host)
	{
		this.host = host;
		
		this.host.makeProtectedTable(EnumTable.QUARTER_SCORE.toString(), 
				this.host.getTableFromPreset(EnumTable.QUARTER_SCORE));

		String[] performanceAttributes = EnumTable.PERFORMANCE.getTableAttributes();
		Class<?>[] performanceClasses = EnumTable.PERFORMANCE.getDataClasses();
		
		String[] matchAttributes = EnumTable.MATCH.getTableAttributes();
		Class<?>[] matchClasses = EnumTable.MATCH.getDataClasses();
		
		ArrayList<String> joinedAttributes = new ArrayList<String>();
		ArrayList<Class<?>> joinedClasses = new ArrayList<Class<?>>();
		
		for(int i = 0; i < performanceAttributes.length; i ++)
		{
			joinedAttributes.add(performanceAttributes[i]);
			joinedClasses.add(performanceClasses[i]);
		}
		
		for(int i = 0; i < matchAttributes.length; i ++)
			if(!matchAttributes[i].equalsIgnoreCase("match_id"))
		{
			joinedAttributes.add(matchAttributes[i]);
			joinedClasses.add(matchClasses[i]);
		}
		
		MultivaluedTable match_natural_join_performance 
			= new MultivaluedTable(host, joinedAttributes.toArray(new String[0]),
					joinedClasses.toArray(new Class<?>[0]));
		
		this.host.makeProtectedTable("match_natural_join_performance", match_natural_join_performance);
		
		identity = match_natural_join_performance.getColumn("match_id");
		season = match_natural_join_performance.getColumn("match_season");
		date = match_natural_join_performance.getColumn("match_date");
		host_abbr = match_natural_join_performance.getColumn("match_host_abbr");
		host_score = match_natural_join_performance.getColumn("match_host_score");
		guest_abbr = match_natural_join_performance.getColumn("match_guest_abbr");
		guest_score = match_natural_join_performance.getColumn("match_guest_score");
		
		quarter_id = host.getColumn("quarter_score.match_id");
		quarter_number = host.getColumn("quarter_score.quarter_number");
		quarter_host_score = host.getColumn("quarter_score.quarter_host_score");
		quarter_guest_score = host.getColumn("quarter_score.quarter_guest_score");
		
		performance_match_id = match_natural_join_performance.getColumn("match_id");
		game_team = match_natural_join_performance.getColumn("team_name_abbr");
		player_name = match_natural_join_performance.getColumn("player_name");
		position = match_natural_join_performance.getColumn("player_position");
		game_minute = match_natural_join_performance.getColumn("game_time_minute");
		game_second = match_natural_join_performance.getColumn("game_time_second");
		shoot_score = match_natural_join_performance.getColumn("shoot_score");
		shoot_count = match_natural_join_performance.getColumn("shoot_count");
		three_shoot_score = match_natural_join_performance.getColumn("three_shoot_score");
		three_shoot_count = match_natural_join_performance.getColumn("three_shoot_count");
		foul_shoot_score = match_natural_join_performance.getColumn("foul_shoot_score");
		foul_shoot_count = match_natural_join_performance.getColumn("foul_shoot_count");
		attack_board = match_natural_join_performance.getColumn("attack_board");
		defence_board = match_natural_join_performance.getColumn("defence_board");
		total_board = match_natural_join_performance.getColumn("total_board");
		assist = match_natural_join_performance.getColumn("assist");
		steal = match_natural_join_performance.getColumn("steal");
		cap = match_natural_join_performance.getColumn("cap");
		miss = match_natural_join_performance.getColumn("miss");
		foul = match_natural_join_performance.getColumn("foul");
		self_score = match_natural_join_performance.getColumn("self_score");
	}
	
	int matchId = 1;
	
	public void record(File file, int matchId, KeywordTable quarterTable, MultivaluedTable matchTable) throws Exception
	{
		if(!file.getName().matches("[0-9]{2}-[0-9]{2}_[0-9]{2}-[0-9]{2}_[A-Z]+-[A-Z]+")) return;
		
		String[] splitted = file.getName().split("_", 3);
		
		//String season_str = splitted[0];
		String season_str = StringPool.createSeasonFromPool(splitted[0]);
		//String date_str = splitted[1];
		String date_str = StringPool.createSeasonFromPool(splitted[1]);
		
		String[] duals = splitted[2].split("-", 2);
		//String host_abbr_str = duals[0];
		//String guest_abbr_str = duals[1];
		String host_abbr_str = StringPool.createSeasonFromPool(duals[0]);
		String guest_abbr_str = StringPool.createSeasonFromPool(duals[1]);
		
		
		//Getting data from the file.
		BufferedReader br = new BufferedReader(new FileReader(file));
		String currentLine = null;
		
		currentLine = br.readLine();
		if(currentLine == null)
		{
			br.close();
			return;
		}
		String[] scores = tokenize(currentLine).get(2).split("-");
		
		int host_score_int = Integer.parseInt(scores[0]);
		int guest_score_int = Integer.parseInt(scores[1]);
		
		currentLine = br.readLine();
		if(currentLine == null)
		{
			br.close();
			return;
		}
		ArrayList<String> tokens = tokenize(currentLine);
		for(int i = 0; i < tokens.size(); i ++)
		{
			scores = tokens.get(i).split("-");
			Tuple scoreTuple = quarterTable.createTuple();
			quarter_id.setAttribute(scoreTuple, matchId);
			quarter_number.setAttribute(scoreTuple, i + 1);
			quarter_host_score.setAttribute(scoreTuple, Integer.parseInt(scores[0]));
			quarter_guest_score.setAttribute(scoreTuple, Integer.parseInt(scores[1]));
		}
		
		String currentTeam = null;
		while((currentLine = br.readLine()) != null)
		{
			if(currentLine.equals(duals[0]) || currentLine.equals(duals[1]))
				currentTeam = currentLine;
			else
			{
				tokens = tokenize(currentLine);
				Tuple performance = matchTable.createTuple();
				Tuple tuple = performance;
				
				performance_match_id.setAttribute(performance, matchId);
				game_team.setAttribute(performance, currentTeam);
				player_name.setAttribute(performance, tokens.get(0));
				position.setAttribute(performance, tokens.get(1));
				
				if(tokens.get(2).matches("[0-9]+:[0-9]+"))
				{
					String[] game_time = tokens.get(2).split(":");
					game_minute.setAttribute(performance, game_time[0]);
					game_second.setAttribute(performance, game_time[1]);
				}
				else host.processDirtyData(performance, game_minute, tokens.get(2));
				
				shoot_score.setAttribute(performance, tokens.get(3));
				shoot_count.setAttribute(performance, tokens.get(4));
				
				three_shoot_score.setAttribute(performance, tokens.get(5));
				three_shoot_count.setAttribute(performance, tokens.get(6));
				
				foul_shoot_score.setAttribute(performance, tokens.get(7));
				foul_shoot_count.setAttribute(performance, tokens.get(8));
				
				attack_board.setAttribute(performance, tokens.get(9));
				defence_board.setAttribute(performance, tokens.get(10));
				total_board.setAttribute(performance, tokens.get(11));
				
				assist.setAttribute(performance, tokens.get(12));
				steal.setAttribute(performance, tokens.get(13));
				cap.setAttribute(performance, tokens.get(14));
				
				miss.setAttribute(performance, tokens.get(15));
				foul.setAttribute(performance, tokens.get(16));
				self_score.setAttribute(performance, tokens.get(17));
				
				season.setAttribute(tuple, season_str);
				//season.setAttribute(tuple, season_ins);
				date.setAttribute(tuple, date_str);
				
				host_abbr.setAttribute(tuple, host_abbr_str);
				guest_abbr.setAttribute(tuple, guest_abbr_str);
				
				host_score.setAttribute(tuple, host_score_int);
				guest_score.setAttribute(tuple, guest_score_int);
			}
		}
		br.close();
	}
	
	public ArrayList<String> tokenize(String toBuild)
	{
		char[] build = toBuild.toCharArray();
		ArrayList<String> splitted = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < build.length; i ++)
			if(build[i] != ';') builder.append(build[i]);
			else
			{
				splitted.add(builder.toString());
				builder = new StringBuilder();
			}
		return splitted;
	}

	public void setRoot(File root)
	{
		FileMonitor fileMonitor = new FileMonitor(new File(root, "matches"), this);
		fileMonitor.start();
	}
	
	@Override
	public void load(File aFile) throws Exception
	{
		KeywordTable quarterTable = (KeywordTable) host.getTable("quarter_score");
		MultivaluedTable joinedTable = (MultivaluedTable) host.getTable("match_natural_join_performance");
		
		this.record(aFile, matchId, quarterTable, joinedTable);
		matchId ++;
	}
}
