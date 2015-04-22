package nbaquery.presentation2.panel;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nbaquery.logic.match.MatchService;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;
import nbaquery.presentation2.info.Match;
import nbaquery.presentation2.info.Player;
import nbaquery.presentation2.info.Team;
import nbaquery.presentation2.panel.ConcisePanel;
import nbaquery.presentation2.panel.DetailedPanel;
import nbaquery.presentation2.panel.PanelSet;

public class PanelSet {	
	private static PanelSet instance = null;

	public static PlayerService ps;
	public static TeamService ts;
	public static MatchService ms;
	
	private static JFrame frame;
	private static ConcisePanel concise;
	
	private static ArrayList<JPanel> detailed_list;

	private static JScrollPane concise_scr;
	private static JPanel concise_search_panel;
	private static JPanel concise_button_panel;
	
	private PanelSet(){
	}
	
	public static PanelSet get_instance(JFrame set_frame){
		if(instance == null){
			instance = new PanelSet();
			
			frame = set_frame;
			
			detailed_list = new ArrayList<JPanel>();
        }
        return instance;
	}
	public static PanelSet get_instance(){
		return instance;
	}
	
	public static JFrame get_frame(){
		return frame;
	}
	
	public static void set_concise(ConcisePanel concise_panel){
		concise = concise_panel;
	}
	public static ConcisePanel get_concise(){
		return concise;
	}
	public static void set_concise_invisible(){
		if(concise != null){
			concise_scr = concise.get_scr();
			concise_search_panel = concise.get_search_panel();
			concise_button_panel = concise.get_button_panel();
			
			if(concise.get_button_panel() != null){
				concise.get_button_panel().setVisible(false);
			}
			if(concise.get_scr() != null){
				concise.get_scr().setVisible(false);
				frame.remove(concise.get_scr());
			}
			concise.set_search_invisible();
			frame.remove(concise.get_search_panel());
			frame.revalidate();
			frame.repaint();
		}
	}

	public static void create_detailed_panel(Player player){
		JPanel detailed_added_panel = new JPanel();
		detailed_added_panel.setBackground(new Color(0,0,0,0.0f));
		detailed_added_panel.setLayout(null);
		detailed_added_panel.setSize(784, 545);
		detailed_added_panel.setLocation(0, 0);

		DetailedPanel detailed_panel = new DetailedPanel(player);
		detailed_added_panel.add(detailed_panel);
		detailed_list.add(detailed_added_panel);
		
		frame.add(detailed_list.get(detailed_list.size() - 1));
	}
	public static void create_detailed_panel(Team team){
		JPanel detailed_added_panel = new JPanel();
		detailed_added_panel.setBackground(new Color(0,0,0,0.0f));
		detailed_added_panel.setLayout(null);
		detailed_added_panel.setSize(784, 545);
		detailed_added_panel.setLocation(0, 0);

		DetailedPanel detailed_panel = new DetailedPanel(team);
		detailed_added_panel.add(detailed_panel);
		detailed_list.add(detailed_added_panel);
		
		frame.add(detailed_list.get(detailed_list.size() - 1));
	}
	public static void create_detailed_panel(Match match){
		JPanel detailed_added_panel = new JPanel();
		detailed_added_panel.setBackground(new Color(0,0,0,0.0f));
		detailed_added_panel.setLayout(null);
		detailed_added_panel.setSize(784, 545);
		detailed_added_panel.setLocation(0, 0);

		DetailedPanel detailed_panel = new DetailedPanel(match);
		detailed_added_panel.add(detailed_panel);
		detailed_list.add(detailed_added_panel);
		
		frame.add(detailed_list.get(detailed_list.size() - 1));
		
	}
	public static void set_all_detailed_panel_invisible(){
		for(JPanel detailed : detailed_list){
			frame.remove(detailed);
		}
	}
	public static void detailed_exit(){
		if(detailed_list.size() != 0){
			frame.remove(detailed_list.get(detailed_list.size() - 1));
			detailed_list.remove(detailed_list.size() - 1);
		}
		
		if(detailed_list.size() != 0){
			frame.add(detailed_list.get(detailed_list.size() - 1));
		}
		else{
			frame.add(concise_button_panel);
			frame.add(concise_scr);
			frame.add(concise_search_panel);
		}
	}
	
	public static void set_player_service(PlayerService get_ps){
		ps = get_ps;
	}
	public static PlayerService get_player_service(){
		return ps;
	}
	public static void set_team_service(TeamService get_ts){
		ts = get_ts;
	}
	public static TeamService get_team_service(){
		return ts;
	}
	public static void set_match_service(MatchService get_ms){
		ms = get_ms;
	}
	public static MatchService get_match_service(){
		return ms;
	}
	
}
