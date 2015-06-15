package nbaquery.data.sql.loader.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import nbaquery.data.sql.MutableSqlRow;
import nbaquery.data.sql.MutableSqlTable;
import nbaquery.data.sql.SqlTableColumn;
import nbaquery.data.sql.SqlTableHost;

public class MatchLoader implements SqlFileLoader
{

	final SqlTableHost host;
	
	SqlTableColumn identity;
	SqlTableColumn season;
	SqlTableColumn date;
	SqlTableColumn host_abbr;
	SqlTableColumn host_score;
	SqlTableColumn guest_abbr;
	SqlTableColumn guest_score;
	
	SqlTableColumn quarter_id;
	SqlTableColumn quarter_number;
	SqlTableColumn quarter_host_score;
	SqlTableColumn quarter_guest_score;
	
	SqlTableColumn performance_match_id;
	SqlTableColumn game_team;
	SqlTableColumn player_name;
	SqlTableColumn position;
	SqlTableColumn game_minute;
	SqlTableColumn game_second;
	SqlTableColumn shoot_score;
	SqlTableColumn shoot_count;
	SqlTableColumn three_shoot_score;
	SqlTableColumn three_shoot_count;
	SqlTableColumn foul_shoot_score;
	SqlTableColumn foul_shoot_count;
	SqlTableColumn attack_board;
	SqlTableColumn defence_board;
	SqlTableColumn total_board;
	SqlTableColumn assist;
	SqlTableColumn steal;
	SqlTableColumn cap;
	SqlTableColumn miss;
	SqlTableColumn foul;
	SqlTableColumn self_score;
	
	PreparedStatement get_max_match_id;
	int matchId = 1;
	
	public MatchLoader(SqlTableHost host) throws SQLException
	{
		this.host = host;
				
		identity = (SqlTableColumn) host.getTable("matches").getColumn("match_id");
		season = (SqlTableColumn)host.getTable("matches").getColumn("match_season");
		date = (SqlTableColumn)host.getTable("matches").getColumn("match_date");
		host_abbr = (SqlTableColumn)host.getTable("matches").getColumn("match_host_abbr");
		host_score = (SqlTableColumn)host.getTable("matches").getColumn("match_host_score");
		guest_abbr = (SqlTableColumn)host.getTable("matches").getColumn("match_guest_abbr");
		guest_score = (SqlTableColumn)host.getTable("matches").getColumn("match_guest_score");
		
		quarter_id = (SqlTableColumn)host.getTable("quarter_score").getColumn("match_id");
		quarter_number = (SqlTableColumn)host.getTable("quarter_score").getColumn("quarter_number");
		quarter_host_score = (SqlTableColumn)host.getTable("quarter_score").getColumn("quarter_host_score");
		quarter_guest_score = (SqlTableColumn)host.getTable("quarter_score").getColumn("quarter_guest_score");
		
		performance_match_id = (SqlTableColumn)host.getTable("performance").getColumn("match_id");
		game_team = (SqlTableColumn)host.getTable("performance").getColumn("team_name_abbr");
		player_name = (SqlTableColumn)host.getTable("performance").getColumn("player_name");
		position = (SqlTableColumn)host.getTable("performance").getColumn("player_position");
		game_minute = (SqlTableColumn)host.getTable("performance").getColumn("game_time_minute");
		game_second = (SqlTableColumn)host.getTable("performance").getColumn("game_time_second");
		shoot_score = (SqlTableColumn)host.getTable("performance").getColumn("shoot_score");
		shoot_count = (SqlTableColumn)host.getTable("performance").getColumn("shoot_count");
		three_shoot_score = (SqlTableColumn)host.getTable("performance").getColumn("three_shoot_score");
		three_shoot_count = (SqlTableColumn)host.getTable("performance").getColumn("three_shoot_count");
		foul_shoot_score = (SqlTableColumn)host.getTable("performance").getColumn("foul_shoot_score");
		foul_shoot_count = (SqlTableColumn)host.getTable("performance").getColumn("foul_shoot_count");
		attack_board = (SqlTableColumn)host.getTable("performance").getColumn("attack_board");
		defence_board = (SqlTableColumn)host.getTable("performance").getColumn("defence_board");
		total_board = (SqlTableColumn)host.getTable("performance").getColumn("total_board");
		assist = (SqlTableColumn)host.getTable("performance").getColumn("assist");
		steal = (SqlTableColumn)host.getTable("performance").getColumn("steal");
		cap = (SqlTableColumn)host.getTable("performance").getColumn("cap");
		miss = (SqlTableColumn)host.getTable("performance").getColumn("miss");
		foul = (SqlTableColumn)host.getTable("performance").getColumn("foul");
		self_score = (SqlTableColumn)host.getTable("performance").getColumn("self_score");
		
		ResultSet max_match_id = host.connection.createStatement().executeQuery("select max(match_id) from matches");
		max_match_id.next();	this.matchId = max_match_id.getInt(1) + 1;
	}
	
	public void record(File file, int matchId, MutableSqlTable matchTable, MutableSqlTable quarterTable, MutableSqlTable performanceTable) throws Exception
	{
		if(!file.getName().matches("[0-9]{2}-[0-9]{2}_[0-9]{2}-[0-9]{2}_[A-Z]+-[A-Z]+")) return;
		MutableSqlRow row = matchTable.createRow();
		
		//Getting data from the filename.
		identity.setAttribute(row, matchId);
		
		String[] splitted = file.getName().split("_", 3);
		season.setAttribute(row, splitted[0]);
		//season.setAttribute(row, StringPool.createSeasonFromPool(splitted[0]));
		date.setAttribute(row, splitted[1]);
		//date.setAttribute(row, StringPool.createSeasonFromPool(splitted[1]));
		String[] duals = splitted[2].split("-", 2);
		host_abbr.setAttribute(row, duals[0]);
		guest_abbr.setAttribute(row, duals[1]);
		//host_abbr.setAttribute(row, StringPool.createSeasonFromPool(duals[0]));
		//guest_abbr.setAttribute(row, StringPool.createSeasonFromPool(duals[1]));
		
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
		
		host_score.setAttribute(row, Integer.parseInt(scores[0]));
		guest_score.setAttribute(row, Integer.parseInt(scores[1]));
		row.submit();
		
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
			MutableSqlRow scoreRow = quarterTable.createRow();
			quarter_id.setAttribute(scoreRow, matchId);
			quarter_number.setAttribute(scoreRow, i + 1);
			quarter_host_score.setAttribute(scoreRow, Integer.parseInt(scores[0]));
			quarter_guest_score.setAttribute(scoreRow, Integer.parseInt(scores[1]));
			scoreRow.submit();
		}
		
		String currentTeam = null;
		while((currentLine = br.readLine()) != null)
		{
			if(currentLine.equals(duals[0]) || currentLine.equals(duals[1]))
				currentTeam = currentLine;
			else
			{
				tokens = tokenize(currentLine);
				MutableSqlRow performance = performanceTable.createRow();
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
				else
				{
					game_minute.setAttribute(performance, 0);
					game_second.setAttribute(performance, 0);	
				}
				
				shoot_score.setAttribute(performance, tokens.get(3));
				shoot_count.setAttribute(performance, tokens.get(4));
				
				three_shoot_score.setAttribute(performance, tokens.get(5));
				three_shoot_count.setAttribute(performance, tokens.get(6));
				
				shoot_score.setAttribute(performance,
						(Integer)shoot_score.getAttribute(performance) - (Integer)three_shoot_score.getAttribute(performance));
				shoot_count.setAttribute(performance,
						(Integer)shoot_count.getAttribute(performance) - (Integer)three_shoot_count.getAttribute(performance));
				
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
				performance.submit();
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
	
	public void setRoot(File root) throws Exception
	{
		SqlFileMonitor fileMonitor = new SqlFileMonitor(new File(root, "matches"), this, host);
		fileMonitor.start();
	}

	@Override
	public synchronized void load(File aFile) throws Exception
	{
		MutableSqlTable matchTable = (MutableSqlTable) host.getTable("matches");
		MutableSqlTable quarterTable = (MutableSqlTable) host.getTable("quarter_score");
		MutableSqlTable performanceTable = (MutableSqlTable) host.getTable("performance");
		
		this.record(aFile, matchId, matchTable, quarterTable, performanceTable);
		matchId ++;
	}

	@Override
	public String getLoaderName() {
		return "match";
	}
}
