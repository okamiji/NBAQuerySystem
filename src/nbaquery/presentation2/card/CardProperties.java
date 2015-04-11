package nbaquery.presentation2.card;

public class CardProperties {
	private static boolean player_isGross = true;
	private static int player_index = -1;//for logic service
	private static boolean player_isUp = true;
	private static String player_position = null;
	private static String player_league = null;
	
	private static int player_isGross_index = 0;
	private static int player_index_index = 0;
	private static int player_position_index = 0;
	private static int player_league_index = 0;	
	private static String player_item_name = "按名称排序";
	
	private static boolean team_isGross = true;
	private static int team_index = 0;
	private static boolean team_isUp = true;

	private static int team_index_index = 0;
	private static String team_item_name = "按比赛场数排序";
	
	private static int cards_per_row = 2;
	
	private static boolean if_view_all = false;
	private static int view_limit = 19;
	private static boolean if_view_more_card_needed = false;
	private static boolean view_more_forbid = false;
	
	public static void set_player_isGross(boolean set_player_isGross){
		player_isGross = set_player_isGross;
	}
	public static void set_player_index(int set_player_index){
		player_index = set_player_index;
	}
	public static void set_player_isUp(boolean set_player_isUp){
		player_isUp = set_player_isUp;
	}
	public static void set_player_position(String set_player_position){
		player_position = set_player_position;
	}
	public static void set_player_league(String set_player_league){
		player_league = set_player_league;
	}
	
	public static void set_player_combobox_index(int i1, int i2, int i3, int i4){
		player_isGross_index = i1;
		player_index_index = i2;
		player_position_index = i3;
		player_league_index = i4;	
	}
	public static int[] get_player_combobox_index(){
		int[] return_int = {player_isGross_index, player_index_index, player_position_index, player_league_index};
		return return_int;
	}
	
	public static void set_player_item_name(String set_name){
		player_item_name = set_name;
	}
	public static String get_player_item_name(){
		return player_item_name;
	}
	
	public static void set_team_item_name(String set_name){
		team_item_name = set_name;
	}
	public static String get_team_item_name(){
		return team_item_name;
	}
	

	public static void set_team_isGross(boolean set_team_isGross){
		team_isGross = set_team_isGross;
	}
	public static void set_team_index(int set_team_index){
		team_index = set_team_index;
	}
	public static void set_team_isUp(boolean set_team_isUp){
		team_isUp = set_team_isUp;
	}
	
	public static void set_team_index_index(int set_team_index_index){
		team_index_index = set_team_index_index;
	}
	public static int get_team_index_index(){
		return team_index_index;
	}
	
	public static boolean get_player_isGross(){
		return player_isGross;
	}
	public static int get_player_index(){
		return player_index;
	}
	public static boolean get_player_isUp(){
		return player_isUp;
	}
	public static String get_player_position(){
		return player_position;
	}
	public static String get_player_league(){
		return player_league;
	}
	
	public static boolean get_team_isGross(){
		return team_isGross;
	}
	public static int get_team_index(){
		return team_index;
	}
	public static boolean get_team_isUp(){
		return team_isUp;
	}
	
	
	public static void set_cards_per_row(int set_cards_per_row){
		cards_per_row = set_cards_per_row;
	}
	public static int get_cards_per_row(){
		return cards_per_row;
	}
	
	public static void set_if_view_all(boolean set_if_view_all){
		if_view_all = set_if_view_all;
	}
	public static boolean get_if_view_all(){
		return if_view_all;
	}
	
	public static void set_view_limit(int set_view_limit){
		view_limit = set_view_limit;
	}
	public static int get_view_limit(){
		return view_limit;
	}
	
	public static void set_if_view_more_card_needed(boolean bool){
		if_view_more_card_needed = bool;
	}
	public static boolean get_if_view_more_card_needed(){
		return if_view_more_card_needed;
	}
	
	public static void set_view_more_forbid(boolean bool){
		view_more_forbid = bool;
	}
	public static boolean get_view_more_forbid(){
		return view_more_forbid;
	}
	
}
