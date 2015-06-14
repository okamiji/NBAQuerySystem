package nbaquery.statistics;

import java.util.ArrayList;
import java.util.HashMap;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.launcher.Main;

public class GetData extends Main{
	
	public void loadPresentation(){

	}
	
	public ArrayList<String> get_player_name(){
		
		ArrayList<String> player_name = new ArrayList<String>();
		
		Table tbl_player = this.playerService.searchForPlayers(false, new String[]{"player_name"}, false, null, null);
		Column player_name_column = tbl_player.getColumn("player_name");
		
		for(Row row : tbl_player){
			String name = (String) player_name_column.getAttribute(row);
			player_name.add(name);
		}
		
		return player_name;
	}
	
	public HashMap<String, ArrayList<Float>> get_all_player_data(){
		
		Table tbl = this.playerService.searchForPlayers(false, new String[]{"player_name"}, false, null, null);
		Column player_name_column = tbl.getColumn("player_name");
		Column efficiency_column = tbl.getColumn("efficiency");
		Column gmsc_column = tbl.getColumn("gmsc_efficiency");
		Column foul_shoot_rate_column = tbl.getColumn("foul_shoot_rate");
		Column shoot_rate_column = tbl.getColumn("shoot_rate");
		Column three_shoot_rate_column = tbl.getColumn("three_shoot_rate");
		Column true_shoot_rate_column = tbl.getColumn("true_shoot_rate");
		Column shoot_efficiency_column = tbl.getColumn("shoot_efficiency");
		Column assist_rate_column = tbl.getColumn("assist_rate");
		Column steal_rate_column = tbl.getColumn("steal_rate");
		Column cap_rate_column = tbl.getColumn("cap_rate");
		Column miss_rate_column = tbl.getColumn("miss_rate");
		Column usage_column = tbl.getColumn("usage_rate");

		HashMap<String , ArrayList<Float>> player_map = new HashMap<String , ArrayList<Float>>(); 
		
		for(Row row : tbl){
			String name = (String) player_name_column.getAttribute(row);			
			Float efficiency = (Float) efficiency_column.getAttribute(row);
			Float gmsc =(Float) gmsc_column.getAttribute(row);
			Float foul_shoot_rate = (Float) foul_shoot_rate_column.getAttribute(row);
			Float shoot_rate = (Float) shoot_rate_column.getAttribute(row);
			Float three_shoot_rate = (Float) three_shoot_rate_column.getAttribute(row);
			Float true_shoot_rate = (Float) true_shoot_rate_column.getAttribute(row);
			Float shoot_efficiency = (Float) shoot_efficiency_column.getAttribute(row);
			Float assist_rate = (Float) assist_rate_column.getAttribute(row);
			Float steal_rate = (Float) steal_rate_column.getAttribute(row);
			Float cap_rate = (Float) cap_rate_column.getAttribute(row);
			Float miss_rate = (Float) miss_rate_column.getAttribute(row);
			Float usage = (Float) usage_column.getAttribute(row);
			
			ArrayList<Float> list = new ArrayList<Float>();
			list.add(efficiency);
			list.add(gmsc);
			list.add(foul_shoot_rate);
			list.add(shoot_rate);
			list.add(three_shoot_rate);
			list.add(true_shoot_rate);
			list.add(shoot_efficiency);
			list.add(assist_rate);
			list.add(steal_rate);
			list.add(cap_rate);
			list.add(miss_rate);
			list.add(usage);
			player_map.put(name, list);
		}
		
		return player_map;
	}
	
	public HashMap<String, ArrayList<Float>> get_single_player_data(String name){

		Table tbl = this.matchService.searchPerformancesByPlayer(name);
		
		HashMap<String , ArrayList<Float>> player_map = new HashMap<String , ArrayList<Float>>(); 
		
		Column get_time_minute_column = tbl.getColumn("game_time_minute");
		Column get_time_second_column = tbl.getColumn("game_time_second");
		Column foul_shoot_score_column = tbl.getColumn("foul_shoot_score");
		Column foul_shoot_count_column = tbl.getColumn("foul_shoot_count");
		Column shoot_score_column = tbl.getColumn("shoot_score");
		Column shoot_count_column = tbl.getColumn("shoot_count");
		Column three_shoot_score_column = tbl.getColumn("three_shoot_score");
		Column three_shoot_count_column = tbl.getColumn("three_shoot_count");
		Column attack_board_column = tbl.getColumn("attack_board");
		Column defence_board_column = tbl.getColumn("defence_board");
		Column total_board_column = tbl.getColumn("total_board");
		Column assist_column = tbl.getColumn("assist");
		Column cap_column = tbl.getColumn("cap");
		Column steal_column = tbl.getColumn("steal");
		Column miss_column = tbl.getColumn("miss");
		Column foul_column = tbl.getColumn("foul");
		Column self_score_column = tbl.getColumn("self_score");

		ArrayList<Float> list = new ArrayList<Float>();
		
		for(Row row : tbl)
		{
			Integer get_time_minute = (Integer) get_time_minute_column.getAttribute(row);
			Integer get_time_second = (Integer) get_time_second_column.getAttribute(row);
			Integer foul_shoot_score = (Integer) foul_shoot_score_column.getAttribute(row);
			Integer foul_shoot_count = (Integer) foul_shoot_count_column.getAttribute(row);
			Integer shoot_score = (Integer) shoot_score_column.getAttribute(row);
			Integer shoot_count = (Integer) shoot_count_column.getAttribute(row);
			Integer three_shoot_score = (Integer) three_shoot_score_column.getAttribute(row);
			Integer three_shoot_count = (Integer) three_shoot_count_column.getAttribute(row);
			Integer attack_board = (Integer) attack_board_column.getAttribute(row);
			Integer defence_board = (Integer) defence_board_column.getAttribute(row);
			Integer total_board = (Integer) total_board_column.getAttribute(row);
			Integer assist = (Integer) assist_column.getAttribute(row);
			Integer cap = (Integer) cap_column.getAttribute(row);
			Integer steal = (Integer) steal_column.getAttribute(row);
			Integer miss = (Integer) miss_column.getAttribute(row);
			Integer foul = (Integer) foul_column.getAttribute(row);
			Integer self_score = (Integer) self_score_column.getAttribute(row);
			
			
			list.add((float)get_time_minute);
			list.add((float)get_time_second);
			list.add((float)foul_shoot_score);
			list.add((float)foul_shoot_count); 
			list.add((float)shoot_score);
			list.add((float)shoot_count);
			list.add((float)three_shoot_score);
			list.add((float)three_shoot_count); 
			list.add((float)attack_board);
			list.add((float)defence_board);
			list.add((float)total_board);
			list.add((float)assist); 
			list.add((float)cap);
			list.add((float)steal);
			list.add((float)miss);
			list.add((float)foul);
			list.add((float)self_score);
		}
		player_map.put(name, list);
		
		return player_map;
	}
}
