package nbaquery.presentation2.panel;

import nbaquery.presentation2.addedcard.CardType;

public class ConcisePanelFactory {
	public static void create_panel(CardType type, boolean view_all){
		ConcisePanel panel = null;
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
		panel.run();
	}
}
