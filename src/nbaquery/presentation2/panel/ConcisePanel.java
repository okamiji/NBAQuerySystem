package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import nbaquery.presentation2.util.CardType;
import nbaquery.presentation2.addon.GoodLookingScrollBar;
import nbaquery.presentation2.panel.PanelSet;

@SuppressWarnings("serial")
public class ConcisePanel extends JPanel {
	CardType type;
	
	JPanel search_panel, button_panel, card_panel;
	JScrollPane scr;
	
	boolean if_view_all;
	int is_player;
	
	int scr_height;
	int view_limit;

	JComboBox<String> positionBox, leagueBox, typeBox;
	public JComboBox<String> valueBox;
	JButton ascendButton, descendButton, searchButton;
	
	boolean isUp = true;
	
	public ConcisePanel(CardType type, boolean view_all_cards){	
		search_panel = new JPanel();
		button_panel = new JPanel();
		card_panel = new JPanel();
		card_panel.setBackground(new Color(0, 0, 0, 0.0f));
		card_panel.setLayout(null);
	    this.add(search_panel);
	    this.add(button_panel);
	    
		scr_height = 550;
		PanelSet.set_concise(this);
		
		this.setSize(650, 500);
		this.setLocation(160, 55);
		this.setLayout(null);
		this.setVisible(true);
		this.setBackground(new Color(0, 0, 0, 0));
	}
	
	protected void run(){
	    scr = new JScrollPane(card_panel, 
	    		ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
	    		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scr.setBounds(20, 70, 610, 400);//+70, +65
	    scr.setBorder(null);
	    scr.setBackground(new Color(0, 0, 0, 0));
	    scr.setVerticalScrollBar(new GoodLookingScrollBar());
	    scr.setOpaque(true);
	    this.add(scr);
	}
	
	void set_scr(){
	    card_panel.setPreferredSize(new Dimension(scr.getWidth() - 50, scr_height));
	    card_panel.revalidate();
	    card_panel.repaint();
	    card_panel.setVisible(true);
	    scr.setVisible(true);
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
}

