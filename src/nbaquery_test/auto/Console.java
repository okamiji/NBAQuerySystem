package nbaquery_test.auto;

import java.io.PrintStream;
import java.util.TreeMap;

import test.data.PlayerHighInfo;
import test.data.PlayerHotInfo;
import test.data.PlayerKingInfo;
import test.data.PlayerNormalInfo;
import test.data.TeamHighInfo;
import test.data.TeamHotInfo;
import test.data.TeamNormalInfo;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.data.query.Query;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.NewTeamService;
import nbaquery.logic.team.TeamService;
import nbaquery_test.auto.token.*;
import nbaquery_test.auto.token.Number;

/**
 * Facade design pattern is used in automized test.
 * @author luohaoran
 */
public class Console
{
	final TreeMap<String, TestFacadeToken> tokens = new TreeMap<String, TestFacadeToken>();
	
	public TableHost tableHost = null;
	NewTeamService teamService;
	NewPlayerService playerService;
	
	public Console()
	{
		this(new TestFacadeToken[]{new TestInitializer(), new TestTerminator(),
				new Player(), new Team(), new Average(), new Total(),new All(),
				new Filter(), new High(), new Hot(), new King(), new Low(),
				new Number(),new Sort()});
	}
	
	public static final int HAS_QUERY_BIT = 1;		//MASK 00000001
	public static final int NO_QUERY = 0;			//00000000
	
	public static final int MODEL_TYPE_BIT = 3;		//MASK 00000011
	public static final int PLAYER = 1; 			//00000001
	public static final int TEAM = 3; 				//00000011
	
	public static final int DATA_TYPE_BIT = 4;		//MASK 00000100
	public static final int AVERAGE = 0;			//00000000
	public static final int TOTAL = 4;				//00000100
	
	public static final int HOT_TYPE_BIT = 8;		//MASK 00001000
	public static final int ALL = 0;				//00000000
	public static final int HOT = 8;				//00001000
	
	public static final int KING = 1;				
	public static final int NOKING = 0;
	
	public static final int NUMBER_TYPE_BIT = 16;		//MASK 00010000
	public static final int NONUMBER = 0;				//00000000
	public static final int NUMBER = 16;				//00010000
	
	public static final int HIGH_TYPE_BIT = 32;		//MASK 00100000
	public static final int LOW = 0;				//00000000
	public static final int HIGH = 32;				//00100000
	
	public static final int FILTER_TYPE_BIT = 64;		//MASK 01000000
	public static final int NOFILTER = 0;				//00000000
	public static final int FILTER = 64;				//01000000
	
	public static final int SORT_TYPE_BIT = 128;		//MASK 10000000
	public static final int NOSORT = 0;				//00000000
	public static final int SORT = 128;				//10000000
	
	public String[] fields = new String[2];
	public String filterField = null;
	public String sortField = null;
	
	public int dataNumber = 50;
	public int queryMode = 0;
	public int hotMode   = 0;
	boolean isGross=false;
	
	public Console(TestFacadeToken[] tokens)
	{
		for(TestFacadeToken token : tokens)
		{
			String[] names = token.getTokens();
			for(String name : names) this.tokens.put(name, token);
		}
	}
	
	public void execute(java.io.PrintStream stdout, java.lang.String[] arguments)
	{
		queryMode = NO_QUERY;
		int pointer = 0;
		
		while(pointer < arguments.length)
		{
			TestFacadeToken token = tokens.get(arguments[pointer]);
			if(token == null) break;
			token.change(this, arguments, pointer);
			pointer += (token.params() + 1);
		}
		
		if((queryMode & HAS_QUERY_BIT) != NO_QUERY)
		{
			Table table = null;
			int model = queryMode & MODEL_TYPE_BIT;
			int data = queryMode & DATA_TYPE_BIT;
			int number = queryMode & NUMBER_TYPE_BIT;
			int hot = queryMode & HOT_TYPE_BIT;
			int high = queryMode & HIGH_TYPE_BIT;
			int filter = queryMode & FILTER_TYPE_BIT;
			int sort = queryMode & SORT_TYPE_BIT;
			
			String position=null,league=null,age=null;
			
			String[] sortFields = null;
			boolean[] sortDes = null;
			
			if(sortField!=null)
			{
				sortFields=sortField.split(",");
				sortDes=new boolean[sortFields.length];
			}
			else 
				sortDes=new boolean[]{false};
				
			if(data == AVERAGE) isGross = false;
			else if(data == TOTAL) isGross = true;
			
			if(model == PLAYER) {
				if(hot == HOT) {
					String hotFilterField = playerLookups.get(fields[0]);
					if (hotMode == NOKING){
						table = playerService.searchForProgressPlayers(hotLookups.get(hotFilterField));
						hotPlayerOut(table,stdout);
					}
					else{
						String kingField = fields[1];
						if(kingField.equals("-season"))
							table = playerService.searchForSeasonHotPlayers(hotFilterField);
						else
							table = playerService.searchForTodayHotPlayers(hotFilterField);
						kingPlayerOut(table,stdout);
						}
				}
				else if(hot == ALL){
					if(filter == FILTER){
						String[] filterFields = filterField.split(",");
						for(String s : filterFields){
							String[] s2 = s.split("\\.");
							if (s2[0].equals("position"))
								position = playerLookups.get(s2[1]);
							else if(s2[0].equals("league"))
								league = playerLookups.get(s2[1]);
							else if(s2[0].equals("age"))
								age = ageLookups.get(s2[1]);
						}	
					}
					
					if(sort == SORT){
						for(int i = 0;i<sortFields.length;i++){
							String[] s2 = sortFields[i].split("\\.");
							sortFields[i] = playerLookups.get(s2[0]);
							if(s2[1].equals("asc"))
								sortDes[i] = true;
							else
								sortDes[i] = false;
						}
					}
					else if(sort == NOSORT)
						if(high == HIGH)
							sortFields = new String[]{"true_shoot_rate"};
						else
							sortFields = new String[]{"self_score"};
					
					table = playerService.searchForPlayers(isGross, sortFields, sortDes, position, league);
					if(age!=null){
					SelectProjectQuery query = null;
					try {
						query = new SelectProjectQuery(age,table);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					tableHost.performQuery(query, "resultPlayer");
					table = tableHost.getTable("resultPlayer");
					}
					if(high == HIGH)
						highPlayerOut(table,stdout);
					else
						if(isGross)
							totalNormalPlayerOut(table,stdout);
						else
							avgNormalPlayerOut(table,stdout);
				}
			}
			else if(model == TEAM){
				if(hot == HOT){
					String hotFilterField = teamLookups.get(fields[0]);
					table = teamService.searchSeasonHotTeams(hotFilterField);
					hotTeamOut(table,stdout);
				}
				else if(hot == ALL){
					if(number == NONUMBER) dataNumber = 30;
					if(sort == SORT){
						for(int i = 0;i<sortFields.length;i++){
							String[] s2 = sortFields[i].split("\\.");
							sortFields[i] = playerLookups.get(s2[0]);
							if(s2[1].equals("asc"))
								sortDes[i] = true;
							else
								sortDes[i] = false;
							}
					}
					else if(sort == NOSORT){
						if(high == HIGH)
							sortFields = new String[]{"win_rate"};
						else
							sortFields = new String[]{"self_score"};
					}
					table = teamService.searchForTeams(isGross, sortFields, sortDes);
					if(high == HIGH)
						highTeamOut(table,stdout);
					else
						if(isGross)
							totalNormalTeamOut(table,stdout);
						else
							avgNormalTeamOut(table,stdout);
				}
			}
			
		}
	}
	
	void totalNormalPlayerOut(Table table,PrintStream stdout){
		Row[] rows = table.getRows();
		for(int i=0;i<Math.min(dataNumber,rows.length);i++){
			PlayerNormalInfo pni = new PlayerNormalInfo();
			Row row = rows[i];
			pni.setAge(((Integer)table.getColumn("PLAYER_AGE").getAttribute(row)));
			pni.setAssist((Integer)table.getColumn("assist").getAttribute(row));
			pni.setBlockShot((Integer)table.getColumn("cap").getAttribute(row));
			pni.setDefend((Integer)table.getColumn("defence_board").getAttribute(row));
			pni.setEfficiency((Integer)table.getColumn("efficiency").getAttribute(row));
			pni.setFault((Integer)table.getColumn("miss").getAttribute(row));
			pni.setFoul((Integer)table.getColumn("foul").getAttribute(row));
			pni.setMinute((Integer)table.getColumn("game_time_minute").getAttribute(row));
			pni.setName(((String)table.getColumn("PLAYER_NAME").getAttribute(row)));
			pni.setNumOfGame((Integer)table.getColumn("game_count").getAttribute(row));
			pni.setOffend((Integer)table.getColumn("attack_board").getAttribute(row));
			pni.setPenalty((Float)table.getColumn("foul_shoot_rate").getAttribute(row));
			pni.setPoint((Integer)table.getColumn("self_score").getAttribute(row));
			pni.setRebound((Integer)table.getColumn("total_board").getAttribute(row));
			pni.setShot((Float)table.getColumn("shoot_rate").getAttribute(row));
			pni.setStart((Integer)table.getColumn("first_count").getAttribute(row));
			pni.setSteal((Integer)table.getColumn("steal").getAttribute(row));
			pni.setTeamName(((String)table.getColumn("team_name").getAttribute(row)));
			pni.setThree((Float)table.getColumn("three_shoot_rate").getAttribute(row));
			stdout.print(pni);
		}
	}
	
	void avgNormalPlayerOut(Table table,PrintStream stdout){
		Row[] rows = table.getRows();
		for(int i=0;i<Math.min(dataNumber,rows.length);i++){
			PlayerNormalInfo pni = new PlayerNormalInfo();
			Row row = rows[i];
			pni.setAge(((Integer)table.getColumn("PLAYER_AGE").getAttribute(row)));
			pni.setAssist((Float)table.getColumn("assist").getAttribute(row));
			pni.setBlockShot((Float)table.getColumn("cap").getAttribute(row));
			pni.setDefend((Float)table.getColumn("defence_board").getAttribute(row));
			pni.setEfficiency((Float)table.getColumn("efficiency").getAttribute(row));
			pni.setFault((Float)table.getColumn("miss").getAttribute(row));
			pni.setFoul((Float)table.getColumn("foul").getAttribute(row));
			pni.setMinute((Float)table.getColumn("game_time_minute").getAttribute(row));
			pni.setName(((String)table.getColumn("PLAYER_NAME").getAttribute(row)));
			pni.setNumOfGame((Integer)table.getColumn("game_count").getAttribute(row));
			pni.setOffend((Float)table.getColumn("attack_board").getAttribute(row));
			pni.setPenalty((Float)table.getColumn("foul_shoot_rate").getAttribute(row));
			pni.setPoint((Float)table.getColumn("self_score").getAttribute(row));
			pni.setRebound((Float)table.getColumn("total_board").getAttribute(row));
			pni.setShot((Float)table.getColumn("shoot_rate").getAttribute(row));
			pni.setStart((Integer)table.getColumn("first_count").getAttribute(row));
			pni.setSteal((Float)table.getColumn("steal").getAttribute(row));
			pni.setTeamName(((String)table.getColumn("team_name").getAttribute(row)));
			pni.setThree((Float)table.getColumn("three_shoot_rate").getAttribute(row));
			stdout.print(pni);
		}
	}
	
	void highPlayerOut(Table table,PrintStream stdout){
		Row[] rows = table.getRows();
		for(int i=0;i<Math.min(dataNumber,rows.length);i++){
			PlayerHighInfo phi = new PlayerHighInfo();
			Row row = rows[i];
			phi.setAssistEfficient((Float)table.getColumn("assist_rate").getAttribute(row));
			phi.setBlockShotEfficient((Float)table.getColumn("cap_rate").getAttribute(row));
			phi.setDefendReboundEfficient((Float)table.getColumn("defence_board_efficiency").getAttribute(row));
			phi.setFaultEfficient((Float)table.getColumn("miss_rate").getAttribute(row));
			phi.setFrequency((Float)table.getColumn("usage").getAttribute(row));
			phi.setGmSc((Float)table.getColumn("gmsc_efficiency").getAttribute(row));
			phi.setLeague(((String)table.getColumn("team_sector").getAttribute(row)));
			phi.setName(((String)table.getColumn("PLAYER_NAME").getAttribute(row)));
			phi.setOffendReboundEfficient((Float)table.getColumn("attack_board_efficiency").getAttribute(row));
			phi.setPosition(((String)table.getColumn("player_position").getAttribute(row)));
			phi.setRealShot((Float)table.getColumn("true_shoot_rate").getAttribute(row));
			phi.setReboundEfficient((Float)table.getColumn("total_board_efficiency").getAttribute(row));
			phi.setShotEfficient((Float)table.getColumn("shoot_efficiency").getAttribute(row));
			phi.setStealEfficient((Float)table.getColumn("steal_rate").getAttribute(row));
			phi.setTeamName((String)table.getColumn("team_name").getAttribute(row));
			stdout.print(phi);
		}
	}
	
	void kingPlayerOut(Table table,PrintStream stdout){
		Row[] rows = table.getRows();
		for(int i=0;i<Math.min(dataNumber,rows.length);i++){
			Row row = rows[i];
			PlayerKingInfo pki = new PlayerKingInfo();
			pki.setField(fields[0]);
			pki.setName(((String)table.getColumn("PLAYER_NAME").getAttribute(row)));
			pki.setPosition(((String)table.getColumn("player_position").getAttribute(row)));
			pki.setTeamName(((String)table.getColumn("team_name").getAttribute(row)));
			pki.setValue(((Float)table.getColumn(playerLookups.get(fields[0])).getAttribute(row)));
			stdout.print(pki);
		}
	}
	
	void hotPlayerOut(Table table,PrintStream stdout){
		Row[] rows = table.getRows();
		for(int i=0;i<Math.min(dataNumber,rows.length);i++){
			Row row = rows[i];
			PlayerHotInfo phi = new PlayerHotInfo();
			
			phi.setField(fields[0]);
			phi.setName(((String)table.getColumn("PLAYER_NAME").getAttribute(row)));
			phi.setPosition(((String)table.getColumn("player_position").getAttribute(row)));
			phi.setTeamName(((String)table.getColumn("team_name").getAttribute(row)));
			phi.setUpgradeRate(((Float)table.getColumn(hotLookups.get(fields[0])).getAttribute(row)));
			phi.setValue(((Float)table.getColumn(playerLookups.get(fields[0])).getAttribute(row)));
			stdout.print(phi);
		}
	}
	
	void highTeamOut(Table table,PrintStream stdout){
		Row[] rows = table.getRows();
		for(int i=0;i<Math.min(dataNumber,rows.length);i++){
			Row row = rows[i];
			TeamHighInfo thi = new TeamHighInfo();
			
			thi.setAssistEfficient((Float)table.getColumn("assist_rate").getAttribute(row));
			thi.setDefendEfficient((Float)table.getColumn("defence_efficiency").getAttribute(row));
			thi.setDefendReboundEfficient((Float)table.getColumn("defence_board_efficiency").getAttribute(row));
			thi.setOffendEfficient((Float)table.getColumn("attack_efficiency").getAttribute(row));
			thi.setOffendReboundEfficient((Float)table.getColumn("attack_board_efficiency").getAttribute(row));
			thi.setOffendRound((Float)table.getColumn("attack_round").getAttribute(row));
			thi.setStealEfficient((Float)table.getColumn("steal_efficiency").getAttribute(row));
			thi.setTeamName(((String)table.getColumn("team_name").getAttribute(row)));
			thi.setWinRate((Float)table.getColumn("win_rate").getAttribute(row));
			stdout.print(thi);
		}
	}
	
	void hotTeamOut(Table table,PrintStream stdout){
		Row[] rows = table.getRows();
		for(int i=0;i<Math.min(dataNumber,rows.length);i++){
			Row row = rows[i];
			TeamHotInfo thi = new TeamHotInfo(); 
			
			thi.setField(fields[0]);
			thi.setLeague(((String)table.getColumn("team_sector").getAttribute(row)));
			thi.setTeamName(((String)table.getColumn("team_name").getAttribute(row)));
			thi.setValue(((Float)table.getColumn(teamLookups.get(fields[0])).getAttribute(row)));
			stdout.print(thi);
		}
	}
	
	void avgNormalTeamOut(Table table,PrintStream stdout){
		Row[] rows = table.getRows();
		for(int i=0;i<Math.min(dataNumber,rows.length);i++){
			Row row = rows[i];
			TeamNormalInfo tni = new TeamNormalInfo(); 
			
			tni.setAssist(((Float)table.getColumn("assist").getAttribute(row)));
			tni.setBlockShot(((Float)table.getColumn("cap").getAttribute(row)));
			tni.setDefendRebound(((Float)table.getColumn("defence_board").getAttribute(row)));
			tni.setFault(((Float)table.getColumn("miss").getAttribute(row)));
			tni.setFoul(((Float)table.getColumn("foul").getAttribute(row)));
			float numGame = (Float)table.getColumn("game").getAttribute(row);
			int num = (int)numGame;
			tni.setNumOfGame(num);
			tni.setOffendRebound(((Float)table.getColumn("attack_board").getAttribute(row)));
			tni.setPenalty(((Float)table.getColumn("foul_shoot_rate").getAttribute(row)));
			tni.setPoint(((Float)table.getColumn("self_score").getAttribute(row)));
			tni.setRebound(((Float)table.getColumn("total_board").getAttribute(row)));
			tni.setShot(((Float)table.getColumn("shoot_rate").getAttribute(row)));
			tni.setSteal(((Float)table.getColumn("steal").getAttribute(row)));
			tni.setTeamName(((String)table.getColumn("team_name").getAttribute(row)));
			tni.setThree(((Float)table.getColumn("three_shoot_rate").getAttribute(row)));
			stdout.print(tni);
		}
	}
	
	void totalNormalTeamOut(Table table,PrintStream stdout){
		Row[] rows = table.getRows();
		for(int i=0;i<Math.min(dataNumber,rows.length);i++){
			Row row = rows[i];
			TeamNormalInfo tni = new TeamNormalInfo(); 
			
			tni.setAssist(((Integer)table.getColumn("assist").getAttribute(row)));
			tni.setBlockShot(((Integer)table.getColumn("cap").getAttribute(row)));
			tni.setDefendRebound(((Integer)table.getColumn("defence_board").getAttribute(row)));
			tni.setFault(((Integer)table.getColumn("miss").getAttribute(row)));
			tni.setFoul(((Integer)table.getColumn("foul").getAttribute(row)));
			tni.setNumOfGame(((Integer)table.getColumn("game").getAttribute(row)));
			tni.setOffendRebound(((Integer)table.getColumn("attack_board").getAttribute(row)));
			tni.setPenalty(((Float)table.getColumn("foul_shoot_rate").getAttribute(row)));
			tni.setPoint(((Integer)table.getColumn("self_score").getAttribute(row)));
			tni.setRebound(((Integer)table.getColumn("total_board").getAttribute(row)));
			tni.setShot(((Float)table.getColumn("shoot_rate").getAttribute(row)));
			tni.setSteal(((Integer)table.getColumn("steal").getAttribute(row)));
			tni.setTeamName(((String)table.getColumn("team_name").getAttribute(row)));
			tni.setThree(((Float)table.getColumn("three_shoot_rate").getAttribute(row)));
			stdout.print(tni);
		}
	}
	
	
	
	public static void main(String[] arguments) throws Exception
	{
//		Console facade = new Console();
//		facade.execute(System.out, new String[]{"--datasource", "D:/迭代一数据"});
//		facade.execute(System.out, new String[]{"-player","-filter","age.<=22","-n","10"});
//		facade.execute(System.out, new String[]{"-player", "-all","-n","5","-filter","position.f"});
//		facade.execute(System.out, new String[]{"-player", "-avg",});
//		facade.execute(System.out, new String[]{"-team"});
//		facade.execute(System.out, new String[]{"-team", "-avg","-all","-n","10","-sort","shot.desc"});
//		facade.execute(System.out, new String[]{"--shutdown"});
	}
	
	public final TreeMap<String, String> ageLookups = new TreeMap<String, String>();{
		ageLookups.put("<=22","player_query_result.PLAYER_AGE<=22");
		ageLookups.put("22<X <=25","player_query_result.PLAYER_AGE>22 and player_query_result.PLAYER_AGE<=25");
		ageLookups.put("25<X <=30","player_query_result.PLAYER_AGE>25 and player_query_result.PLAYER_AGE<=30");
		ageLookups.put(">30","player_query_result.PLAYER_AGE>22");
	}
	
	public final TreeMap<String, String> hotLookups = new TreeMap<String, String>();{
		hotLookups.put("score","self_score_rate");
		hotLookups.put("rebound","total_board_rate");
		hotLookups.put("assist","assist_rate");
	}
	
	public final TreeMap<String, String> teamLookups = new TreeMap<String, String>();{
		teamLookups.put("All",null);
		teamLookups.put("point","self_score");
		teamLookups.put("rebound","total_board");
		teamLookups.put("assist","assist");
		teamLookups.put("blockshot","cap");
		teamLookups.put("steal","steal");
		teamLookups.put("foul","foul");
		teamLookups.put("fault","miss");
		teamLookups.put("shot","shoot_rate");
		teamLookups.put("three","three_shoot_rate");
		teamLookups.put("penalty","foul_shoot_rate");
		teamLookups.put("defendRebound","defence_board");
		teamLookups.put("offendRebound","attack_board");
		teamLookups.put("winRate","win_rate");
		teamLookups.put("offendRound","attack_round");
		teamLookups.put("offendReboundEfficient","attack_board_efficiency");
		teamLookups.put("defendReboundEfficient","defence_board_efficiency");
		teamLookups.put("assistEfficient","assist_efficiency");
		teamLookups.put("stealEfficient","steal_efficiency");
		teamLookups.put("offendEfficient","attack_efficiency");
		teamLookups.put("defendEfficient","defence_efficiency");
	}
	
	public final TreeMap<String, String> playerLookups = new TreeMap<String, String>();
	{
		playerLookups.put("All",null);
		playerLookups.put("F","F");
		playerLookups.put("G","G");
		playerLookups.put("C","C");
		playerLookups.put("score","self_score");
		playerLookups.put("rebound","total_board");
		playerLookups.put("assist","assist");
		playerLookups.put("west","w");
		playerLookups.put("east","e");
		playerLookups.put("age","player_age");
		playerLookups.put("point","self_score");
		playerLookups.put("blockshot","cap");
		playerLookups.put("steal","steal");
		playerLookups.put("foul","foul");
		playerLookups.put("fault","miss");
		playerLookups.put("minute","game_time_minute");
		playerLookups.put("efficent","efficiency");
		playerLookups.put("shot","shoot_rate");
		playerLookups.put("three","three_shoot_rate");
		playerLookups.put("penalty","foul_shoot_rate");
		playerLookups.put("doubleTwo","score_board_assist");
		playerLookups.put("realShot","true_shoot_rate");
		playerLookups.put("GmSc","gmsc_efficiency");
		playerLookups.put("shotEfficient","shoot_efficiency");
		playerLookups.put("reboundEfficient","total_board_efficiency");
		playerLookups.put("offendReboundEfficient","attack_board_efficiency");
		playerLookups.put("defendReboundEfficient","defence_board_efficiency");
		playerLookups.put("assistEfficient","assist_rate");
		playerLookups.put("stealEfficient","steal_rate");
		playerLookups.put("blockShotEfficient","cap_rate");
		playerLookups.put("faultEfficient","miss_rate");
		playerLookups.put("frequency","usage");
	}
}
