package nbaquery.presentation2.panel;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;
import nbaquery.presentation2.info.Player;
import nbaquery.presentation2.info.Team;
import nbaquery.presentation2.panel.ConcisePanel;
import nbaquery.presentation2.panel.DetailedPanel;
import nbaquery.presentation2.panel.PanelSet;

public class PanelSet {	
	//Confirm that all panels needed have been initialized.
	//Provide an access to each panel/frame.
	
	private static PanelSet instance = null;

	public static PlayerService ps;
	private static TeamService ts;
	
	private static JFrame frame;
	private static ConcisePanel concise;
	
	private static ArrayList<JPanel> detailed_list;

	static int view_limit;
	
	private PanelSet(){
	}
	
	public static PanelSet get_instance(JFrame set_frame){
		if(instance == null){
			instance = new PanelSet();
			
			frame = set_frame;
			
			detailed_list = new ArrayList<JPanel>();
			
			view_limit = 7;
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
			concise.get_scr().setVisible(false);
			concise.set_search_invisible();
			frame.remove(concise.get_scr());
			frame.remove(concise.get_search_panel());
			frame.revalidate();
			frame.repaint();
		}
	}

	public static void create_detailed_panel(Player player){
		JPanel detailed_added_panel = new JPanel();
		detailed_added_panel.setBackground(new Color(0,0,0,0.0f));
		detailed_added_panel.setLayout(null);
		detailed_added_panel.setSize(714, 545);
		detailed_added_panel.setLocation(0, 0);

		DetailedPanel detailed_panel = new DetailedPanel(player);
		detailed_added_panel.add(detailed_panel);
		detailed_list.add(detailed_added_panel);
		
		frame.add(detailed_list.get(detailed_list.size() - 1));
	}
	public static void create_detailed_panel(Team team){
	}
	public static void set_detailed_visible(boolean is_visible){
		detailed_list.get(detailed_list.size() - 1).setVisible(is_visible);
	}
	public static void set_all_detailed_panel_invisible(){
		for(JPanel detailed : detailed_list){
			frame.remove(detailed);
		}
	}
	public static void remove_detailed(){
		detailed_list.get(detailed_list.size() - 1).setVisible(false);
		frame.remove(detailed_list.get(detailed_list.size() - 1));
	}

	public static int get_view_limit(){
		return view_limit;
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
	
}
