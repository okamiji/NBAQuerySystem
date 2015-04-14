package nbaquery.presentation2.panel;

import nbaquery.presentation2.addedcard.CardType;

public class ConcisePanelFactory {
	public static void create_panel(CardType type, boolean view_all){
		ConcisePanel panel = null;
		switch(type){
		case PLAYER_FLAT:
			if(ConcisePara.type != CardType.PLAYER_RECT) ConcisePara.type = type;
			panel = new ConcisePlayerPanel(type, view_all);break;
		case PLAYER_RECT:
			if(ConcisePara.type != CardType.PLAYER_FLAT) ConcisePara.type = type;
			panel = new ConcisePlayerPanel(type, view_all);break;
		case TEAM_FLAT:
			if(ConcisePara.type != CardType.TEAM_RECT) ConcisePara.type = type;
			panel = new ConciseTeamPanel(type, view_all);break;
		case TEAM_RECT:
			if(ConcisePara.type != CardType.TEAM_FLAT) ConcisePara.type = type;
			panel = new ConciseTeamPanel(type, view_all);break;
			//TODO match
		default:
			break;
		}
		panel.run();
	}
}
