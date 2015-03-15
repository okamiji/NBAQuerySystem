package nbaquery.data.file.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import nbaquery.data.file.FileTableColumn;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.KeywordTable;
import nbaquery.data.file.Tuple;

public class MatchLoader implements FileLoader
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
	
	public MatchLoader(FileTableHost host)
	{
		this.host = host;
		identity = host.getColumn("match.match_id");
		season = host.getColumn("match.match_season");
		date = host.getColumn("match.match_date");
		host_abbr = host.getColumn("match.match_host_abbr");
		host_score = host.getColumn("match.match_host_score");
		guest_abbr = host.getColumn("match.match_guest_abbr");
		guest_score = host.getColumn("match.match_guest_score");
		
		quarter_id = host.getColumn("quarter_score.match_id");
		quarter_number = host.getColumn("quarter_score.quarter_number");
		quarter_host_score = host.getColumn("quarter_score.quarter_host_score");
		quarter_guest_score = host.getColumn("quarter_score.quarter_guest_score");
		
		performance_match_id = host.getColumn("performance.match_id");
		game_team = host.getColumn("performance.team_name_abbr");
		player_name = host.getColumn("performance.player_name");
		position = host.getColumn("performance.player_position");
		game_minute = host.getColumn("performance.game_time_minute");
		game_second = host.getColumn("performance.game_time_second");
		shoot_score = host.getColumn("performance.shoot_score");
		shoot_count = host.getColumn("performance.shoot_count");
		three_shoot_score = host.getColumn("performance.three_shoot_score");
		three_shoot_count = host.getColumn("performance.three_shoot_count");
		foul_shoot_score = host.getColumn("performance.foul_shoot_score");
		foul_shoot_count = host.getColumn("performance.foul_shoot_count");
		attack_board = host.getColumn("performance.attack_board");
		defence_board = host.getColumn("performance.defence_board");
		total_board = host.getColumn("performance.total_board");
		assist = host.getColumn("performance.assist");
		steal = host.getColumn("performance.steal");
		cap = host.getColumn("performance.cap");
		miss = host.getColumn("performance.miss");
		foul = host.getColumn("performance.foul");
		self_score = host.getColumn("performance.self_score");
	}
	
	public void load(File root)
	{
		KeywordTable matchTable = (KeywordTable) host.getTable("match");
		KeywordTable quarterTable = (KeywordTable) host.getTable("quarter_score");
		KeywordTable performanceTable = (KeywordTable) host.getTable("performance");
		
		File[] files = new File(root, "matches").listFiles();
		
		long matchId = 1;
		
		for(File file : files) if(!file.isDirectory()) try
		{
			this.record(file, matchId, matchTable, quarterTable, performanceTable);
			matchId ++;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			break;
		}
		System.gc();
	}
	
	public void record(File file, long matchId, KeywordTable matchTable, KeywordTable quarterTable, KeywordTable performanceTable) throws Exception
	{
		if(!file.getName().matches("[0-9]{2}-[0-9]{2}_[0-9]{2}-[0-9]{2}_[A-Z]+-[A-Z]+")) return;
		Tuple tuple = matchTable.createTuple();
		
		//Getting data from the filename.
		identity.setAttribute(tuple, matchId);
		
		String[] splitted = file.getName().split("_", 3);
		season.setAttribute(tuple, splitted[0]);
		date.setAttribute(tuple, splitted[1]);
		String[] duals = splitted[2].split("-", 2);
		host_abbr.setAttribute(tuple, duals[0]);
		guest_abbr.setAttribute(tuple, duals[1]);
		
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
		
		host_score.setAttribute(tuple, Integer.parseInt(scores[0]));
		guest_score.setAttribute(tuple, Integer.parseInt(scores[1]));
		
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
				Tuple performance = performanceTable.createTuple();
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
}
