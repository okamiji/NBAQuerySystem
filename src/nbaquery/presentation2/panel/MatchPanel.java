package nbaquery.presentation2.panel;

import java.awt.Color;

import nbaquery.presentation2.util.CardType;

public class MatchPanel extends ConcisePanel {
	CardType type;
	boolean view_all;
	
	public MatchPanel(CardType type, boolean view_more) {		
		super(type, view_more);
		add_cards();
		super.set_scr();
		
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(245, 245, 245));
		search_panel.setBounds(130, 20, 570, 60);
		
		
		
	}
	
	private void add_cards(){
		
	}

}
