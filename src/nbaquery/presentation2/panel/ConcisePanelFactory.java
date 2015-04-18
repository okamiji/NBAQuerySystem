package nbaquery.presentation2.panel;

import nbaquery.presentation2.util.CardType;

public class ConcisePanelFactory {
	public static void create_panel(CardType type, boolean view_all, boolean is_hotspot){
		ConcisePanel panel = null;
		if(!is_hotspot){
			switch(type){
			case PLAYER_FLAT:
				panel = new PlayerPanel(type, view_all);break;
			case PLAYER_RECT:
				panel = new PlayerPanel(type, view_all);break;
			case TEAM_FLAT:
				panel = new TeamPanel(type, view_all);break;
			case TEAM_RECT:
				panel = new TeamPanel(type, view_all);break;
				//TODO match
			default:
				break;
			}
		}
		else{
			panel = new HotspotPanel(type, view_all);
		}
		panel.run();
	}
}
