package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import nbaquery.presentation.combobox.ComboBoxFactory;
import nbaquery.logic.match.MatchService;
import nbaquery.presentation2.addedcard.Card;
import nbaquery.presentation2.card.CardCreator;
import nbaquery.presentation2.card.CardLocation;
import nbaquery.presentation2.util.Button;
import nbaquery.presentation2.util.CardType;

public class MatchPanel extends ConcisePanel {

	boolean view_all;
	
	public MatchPanel(CardType type_, boolean view_all_) {		
		super(type_, view_all_);
		
		type = type_;
		view_all = view_all_;	
		
	}
	
	public void run(){
		super.run();
	    add_cards();
	    super.set_scr();
	    
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(0, 0, 0, 0));
		search_panel.setBounds(190, 75, 570, 60);
		
		valueBox = ComboBoxFactory.getInstance().createComboBox(20, 15, 100, 24, 
				new String[]{
				"赛季",
				"比赛日期",
				"主场队伍缩写",
				"客场队伍缩写",
				"主场队伍得分",
				"客场队伍得分",
				"球队缩写",
				"球员名称",
				"球员位置",
				"上场时间（分钟部分）",
				"上场时间（秒钟部分）",
				"二分进球数",
				"二分出手数",
				"三分进球数",
				"三分出手数",
				"罚球进球数",
				"罚球出手数",
				"进攻篮板",
				"防守篮板",
				"总篮板",
				"助攻",
				"抢断",
				"盖帽",
				"失误",
				"犯规",
				"个人得分"
				});
		search_panel.add(valueBox);
		
		valueBox.setSelectedIndex(ConcisePara.match_index);
		
		descendButton = new JButton();
		descendButton.setIcon(new ImageIcon("Img2/descend.png"));
		descendButton.setContentAreaFilled(false);
		descendButton.setBorder(null);
		descendButton.setBounds(420, 15, 24, 24);
		search_panel.add(descendButton);
		
		ascendButton = new JButton();
		ascendButton.setIcon(new ImageIcon("Img2/ascend.png"));
		ascendButton.setContentAreaFilled(false);
		ascendButton.setBorder(null);
		ascendButton.setBounds(420, 15, 24, 24);
		search_panel.add(ascendButton);
		ascendButton.setVisible(false);
		
		ascendButton.setVisible(!ConcisePara.match_isUp);
		descendButton.setVisible(ConcisePara.match_isUp);
		
		descendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				descendButton.setVisible(false);
				ascendButton.setVisible(true);
				ConcisePara.match_isUp = false;
			}
		});
		ascendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ascendButton.setVisible(false);
				descendButton.setVisible(true);
				ConcisePara.match_isUp = true;
			}
		});
		
		searchButton = new Button("Img2/search_button.png", "Img2/search_button_c.png", search_panel);
		searchButton.setBounds(460, 15, 72, 24);		
		
		searchButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				
				ConcisePara.match_index = valueBox.getSelectedIndex();
				ConcisePara.match_item_name = (String)(valueBox.getSelectedItem());
				ConcisePara.view_all = false;
				
				valueBox.setSelectedIndex(ConcisePara.match_index);
				
				PanelSet.set_concise_invisible();
				ConcisePanelFactory.create_panel(type, view_all, false);
				
			}
		});
		
	}
	
	private void add_cards(){
		MatchService ms = PanelSet.ms;
	//	String[][] str = ms.searchForMatchs(0, true);
		String[][] str = ms.searchForMatchs(ConcisePara.match_index, ConcisePara.match_isUp);
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

}
