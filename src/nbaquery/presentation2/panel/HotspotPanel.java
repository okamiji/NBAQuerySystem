package nbaquery.presentation2.panel;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JButton;

import nbaquery.logic.team.TeamService;
import nbaquery.presentation2.addedcard.CardType;
import nbaquery.presentation2.card.CardCreator;
import nbaquery.presentation2.card.CardLocation;
import nbaquery.presentation2.addedcard.Card;


public class HotspotPanel extends ConcisePanel {

	CardType type;
	boolean view_all;
	
	JButton daily_player, season_player, daily_team, season_team;
	
	public HotspotPanel(CardType type, boolean view_all_cards) {
		super(type, view_all_cards);
		add_cards();
		super.set_scr();
		
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(245, 245, 245));
		search_panel.setBounds(130, 20, 570, 60);
		
		daily_player = new JButton("daily_player");
		season_player = new JButton("season_player");
		daily_team = new JButton("daily_team");
		season_team = new JButton("season_team");

		daily_player.setBounds(10, 5, 120, 40);
		season_player.setBounds(10, 5, 120, 40);
		daily_team.setBounds(10, 5, 120, 40);
		season_team.setBounds(10, 5, 120, 40);
		
		search_panel.add(daily_player);
		search_panel.add(season_player);
		search_panel.add(daily_team);
		search_panel.add(season_team);
		
		concise_panel.add(search_panel);
	}

	private void add_cards(){
		TeamService ts = PanelSet.ts;
		String[][] str = ts.searchForSeasonHotTeams(ConcisePara.team_hot_index);
		CardCreator creator = new CardCreator();
		ArrayList<Card> card_list = creator.create_needed_cards(type, str, view_all);
		CardLocation location = new CardLocation(ConcisePara.type);
		scr_height = location.get_total_height(card_list.size());
		for(int i=0; i<card_list.size(); i++){
			Card card = card_list.get(i);
			concise_panel.add(card);
			card.setLocation(card.width, card.height);
		}
		concise_panel.repaint();
	}
}
