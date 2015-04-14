package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import nbaquery.logic.player.PlayerService;
import nbaquery.presentation2.addedcard.Card;
import nbaquery.presentation2.addedcard.CardType;
import nbaquery.presentation2.card.CardCreator;
import nbaquery.presentation2.card.CardLocation;
import nbaquery.presentation2.panel.PanelSet;

public class ConcisePanel {
	private JFrame frame;
	
	protected JPanel concise_panel, search_panel;
	private JScrollPane scr;
	
	boolean if_view_all;
	int is_player;
	
	int scr_height;
	int view_limit;

	JComboBox<String> positionBox, leagueBox, typeBox;
	public JComboBox<String> valueBox;
	JButton ascendButton, descendButton, searchButton;
	
	boolean isUp = true;
	
	public ConcisePanel(CardType type, boolean view_more){		
		frame = PanelSet.get_frame();
		concise_panel = new JPanel();
		search_panel = new JPanel();
		
		scr_height = 550;
		PanelSet.set_concise(this);

		concise_panel.setLayout(null);
		concise_panel.setBackground(new Color(245, 245, 245));
		concise_panel.setBounds(0, 60, 600, 481);
	}
	
	protected void run(){
	    scr = new JScrollPane(concise_panel, 
	    		ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
	    		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scr.setBounds(110, 60, 600, 482);
	    scr.setBorder(null);
	    
	    //TODO
	    if(scr.getCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER) == null) {
            @SuppressWarnings("serial")
			Component component = new JLabel("") {

                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    Paint oldPaint = g2.getPaint();
                    Rectangle bounds = getBounds();
                    Paint backgroupRectPaint = new GradientPaint(0, 0, new Color(90, 225, 149),
                            bounds.width, bounds.height, new Color(152, 152, 152));
                    g2.setPaint(backgroupRectPaint);
                    g2.fillRect(0, 0, bounds.width, bounds.height);
                    g2.setPaint(oldPaint);

                }
            };
            scr.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, component);
        }
	    
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
		PlayerService ps = PanelSet.ps;
		String[][] str = ps.searchForPlayers(ConcisePara.player_isGross, ConcisePara.player_index, ConcisePara.player_isUp, ConcisePara.player_position, ConcisePara.player_league);
		CardCreator creator = new CardCreator();
		ArrayList<Card> card_list = creator.create_needed_cards(ConcisePara.type, str, ConcisePara.view_all);
		CardLocation location = new CardLocation(ConcisePara.type);
		scr_height = location.get_total_height(card_list.size());
		for(int i=0; i<card_list.size(); i++){
			Card card = card_list.get(i);
			concise_panel.add(card);
			card.setLocation(card.width, card.height);
		}
		concise_panel.repaint();
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
}

