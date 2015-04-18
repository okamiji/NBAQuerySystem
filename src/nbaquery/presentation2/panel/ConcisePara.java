package nbaquery.presentation2.panel;

import nbaquery.presentation2.util.CardType;
import nbaquery.presentation2.util.HotspotType;

public class ConcisePara {
	public static CardType type = CardType.PLAYER_RECT;
	public static boolean view_all = false;
	
	public static boolean player_isGross = true;
	public static int player_index = -1;
	public static boolean player_isUp = true;
	public static String player_position = null;
	public static String player_league = null;
	
	public static int player_isGross_index = 0;
	public static int player_index_index = 0;
	public static int player_position_index = 0;
	public static int player_league_index = 0;
	
	public static String player_item_name = "按名称排序";
	
	public static boolean team_isGross = true;
	public static int team_index = 0;
	public static boolean team_isUp = true;
	
	public static int team_isGross_index = 0;
	
	public static String team_item_name = "按赛季排序";
	
	
	public static HotspotType hotspot_type = HotspotType.SEASON_TEAM;
	
	public static int hot_index = 0;
	
	public static void switch_type(){
		if(type.equals(CardType.PLAYER_FLAT)){
			type = CardType.PLAYER_RECT;
		}
		else if(type.equals(CardType.PLAYER_RECT)){
			type = CardType.PLAYER_FLAT;
		}
		else if(type.equals(CardType.TEAM_FLAT)){
			type = CardType.TEAM_RECT;
		}
		else if(type.equals(CardType.TEAM_RECT)){
			type = CardType.TEAM_FLAT;
		}
	}
}
