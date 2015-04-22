package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import nbaquery.presentation2.util.CardType;
import nbaquery.presentation2.addon.GoodLookingScrollBar;
import nbaquery.presentation2.panel.PanelSet;

public class ConcisePanel {
	JFrame frame;

	CardType type;
	
	JPanel concise_panel, search_panel, button_panel;
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
		frame = PanelSet.get_frame();
		concise_panel = new JPanel();
		search_panel = new JPanel();
		button_panel = new JPanel();
		
		scr_height = 550;
		PanelSet.set_concise(this);

		concise_panel.setLayout(null);
		concise_panel.setBackground(new Color(0, 0, 0, 0));
	}
	
	protected void run(){
	    scr = new JScrollPane(concise_panel, 
	    		ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
	    		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scr.setBounds(180, 125, 610, 400);//+70, +65
	    scr.setBorder(null);
	    scr.setBackground(new Color(0, 0, 0, 0));
	    scr.setOpaque(true);
	    scr.setVerticalScrollBar(new GoodLookingScrollBar());
	    
	    frame.add(scr);
	    
	    frame.add(search_panel);
	    frame.add(button_panel);
	    frame.revalidate();
	    frame.repaint();
	    
	}
	void set_button_panel(){
		
	}
	void set_scr(){
	    concise_panel.setPreferredSize(new Dimension(scr.getWidth() - 50, scr_height));
	}
	
	public JPanel get_search_panel(){
		return search_panel;
	}
	public JPanel get_button_panel(){
		return button_panel;
	}
	public JScrollPane get_scr(){
		return scr;
	}
	public void set_scr(boolean is_visible){
		scr.setVisible(is_visible);
	}
	
	public void set_search_invisible(){
		search_panel.setVisible(false);
	}
	
	public final TreeMap<String, String> lookups = new TreeMap<String, String>();{
		lookups.put("ȫ��λ��", null);
		lookups.put("ȫ������", null);
		lookups.put("ǰ��", "F");
		lookups.put("�з�", "C");
		lookups.put("����", "G");
		
		lookups.put("F", "Forward");
		lookups.put("C", "Center");
		lookups.put("G", "Guard");
		
		lookups.put("����", "E");
		lookups.put("����", "W");
	}
}

