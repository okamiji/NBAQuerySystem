package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import nbaquery.presentation2.card.Card;
import nbaquery.presentation2.card.CardLocation;
import nbaquery.presentation2.panel.PanelSet;

public class ConcisePanel {
	//Has been added to chosen panel when initialized.

	JFrame frame;
	
	JPanel concise_panel;

	JPanel search_panel;
	JScrollPane scr;
	
	boolean if_view_all;
	int player_or_team_or_match;
	
	int scr_height;
	int view_limit;

	JComboBox<String> positionBox, leagueBox, typeBox;
	public JComboBox<String> valueBox;
	JButton ascendButton, descendButton, searchButton;
	
	boolean isUp = true;
	
	public ConcisePanel(int get_player, int get_view_limit){
		frame = PanelSet.get_frame();
		
		player_or_team_or_match = get_player;
		
		scr_height = 550;
		view_limit = get_view_limit;
		
		PanelSet.set_concise(this);

		concise_panel = new JPanel();
		concise_panel.setLayout(null);
		concise_panel.setBackground(new Color(245, 245, 245));
		concise_panel.setBounds(0, 60, 600, 481);

		search_panel = new JPanel();
		
	}
	
	public void run(){
		
	    scr = new JScrollPane(concise_panel, 
	    		ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
	    		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scr.setBounds(110, 60, 600, 482);
	    scr.setBorder(null);
	    frame.add(scr);
	    
	    frame.add(search_panel);
	    frame.revalidate();
	    frame.repaint();
	    
	    add_cards();
	    
	    concise_panel.setPreferredSize(new Dimension(scr.getWidth() - 50, scr_height));
	}
	
	public JPanel get_search_panel(){
		return search_panel;
	}
	public JScrollPane get_scr(){
		return scr;
	}
	public void set_scr(boolean is_visible){
		scr.setVisible(is_visible);
	}
	
	private void add_cards(){
		CardLocation location = new CardLocation(player_or_team_or_match);
		location.run();
		ArrayList<Card> list = location.get_card_list();
		for(int i=0; i<list.size(); i++){
			Integer[] card_location = location.get_location(i);
			Card card = list.get(i);
			concise_panel.add(card);
			card.setLocation(card_location[0], card_location[1]);
		}
		location.set_total_height(list.size());
		concise_panel.repaint();
		
		scr_height = location.get_total_height();
	}
	
	

	public void set_search_invisible(){
		search_panel.setVisible(false);
	}
	
	public final TreeMap<String, String> lookups = new TreeMap<String, String>();{
		lookups.put("全部位置", null);
		lookups.put("全部联盟", null);
		lookups.put("前锋", "F");
		lookups.put("中锋", "C");
		lookups.put("后卫", "G");
		
		lookups.put("F", "Forward");
		lookups.put("C", "Center");
		lookups.put("G", "Guard");
		
		lookups.put("东部", "E");
		lookups.put("西部", "W");
	}

	
	public void set_combobox(){
	}
	
	public void get_combobox(){
		
	}
	
}

