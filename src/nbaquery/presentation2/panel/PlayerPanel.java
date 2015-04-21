package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import nbaquery.logic.player.PlayerService;
import nbaquery.presentation.combobox.ComboBoxFactory;
import nbaquery.presentation2.addedcard.Card;
import nbaquery.presentation2.card.CardCreator;
import nbaquery.presentation2.card.CardLocation;
import nbaquery.presentation2.util.CardType;
import nbaquery.presentation2.util.Button;

public class PlayerPanel extends ConcisePanel {

	boolean view_all;
	
	public PlayerPanel(CardType type_, boolean view_all_) {
		super(type_, view_all_);
		
		type = type_;
		view_all = view_all_;
	}

	public void run(){
		super.run();
	    add_cards();
	    super.set_scr();
	    
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(245, 245, 245));
		search_panel.setBounds(130, 20, 570, 60);
		
		positionBox = ComboBoxFactory.getInstance().createComboBox(20, 15, 80, 24, 
				new String[]{"全部位置", "前锋", "中锋", "后卫"});
	    search_panel.add(positionBox);
	    
		leagueBox = ComboBoxFactory.getInstance().createComboBox(115, 15, 80, 24, 
				new String[]{"全部联盟", "东部", "西部"});
		search_panel.add(leagueBox);
					
		typeBox = ComboBoxFactory.getInstance().createComboBox(210, 15, 80, 24, 
				new String[]{"全局数据", "场均数据"});
		search_panel.add(typeBox);
		
		valueBox = ComboBoxFactory.getInstance().createComboBox(305, 15, 100, 24, 
				new String[]{"按名称排序",
				"参赛场数",
				"先发场数",
				"篮板",
				"助攻",
				"在场时间",
				"投篮命中率",
				"三分命中率",
				"罚球命中率",
				"进攻",
				"防守",
				"抢断",
				"盖帽",
				"失误",
				"犯规",
				"得分",
				"效率",
				"GmSc",
				"真实命中率",
				"投篮效率",
				"篮板率",
				"进攻篮板率",
				"防守篮板率",
				"助攻率",
				"抢断率",
				"盖帽率",
				"失误率",
				"使用率",
				"球员位置",
				"联盟",
				"分/板/助",
				});
		search_panel.add(valueBox);
		
		typeBox.setSelectedIndex(ConcisePara.player_isGross_index);
		valueBox.setSelectedIndex(ConcisePara.player_index_index);
		positionBox.setSelectedIndex(ConcisePara.player_position_index);
		leagueBox.setSelectedIndex(ConcisePara.player_league_index);
		
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
		
		ascendButton.setVisible(!ConcisePara.player_isUp);
		descendButton.setVisible(ConcisePara.player_isUp);
		
		descendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				descendButton.setVisible(false);
				ascendButton.setVisible(true);
				ConcisePara.player_isUp = false;
			}
		});
		ascendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ascendButton.setVisible(false);
				descendButton.setVisible(true);
				ConcisePara.player_isUp = true;
			}
		});
		
		searchButton = new Button("Img2/search_button.png", "Img2/search_button_c.png", search_panel);
		searchButton.setBounds(460, 15, 72, 24);		
		
		searchButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				
				int index = valueBox.getSelectedIndex();
				if(index > 0){
					ConcisePara.player_index = index + 2;
				}
				else{
					ConcisePara.player_index = index + 1;
				}
				
				if(((String)typeBox.getSelectedItem()).equals("全局数据")){
					ConcisePara.player_isGross = true;
				}
				else{
					ConcisePara.player_isGross = false;
				}
				
				ConcisePara.player_position = lookups.get((String) positionBox.getSelectedItem());
				ConcisePara.player_league = lookups.get((String) leagueBox.getSelectedItem());		
				
				ConcisePara.player_item_name = (String)(valueBox.getSelectedItem());
				ConcisePara.view_all = false;
				
				ConcisePara.player_isGross_index = typeBox.getSelectedIndex();
				ConcisePara.player_index_index = valueBox.getSelectedIndex();
				ConcisePara.player_position_index = positionBox.getSelectedIndex();
				ConcisePara.player_league_index = leagueBox.getSelectedIndex();
				
				typeBox.setSelectedIndex(ConcisePara.player_isGross_index);
				valueBox.setSelectedIndex(ConcisePara.player_index_index);
				positionBox.setSelectedIndex(ConcisePara.player_position_index);
				leagueBox.setSelectedIndex(ConcisePara.player_league_index);
				
				PanelSet.set_concise_invisible();
				ConcisePanelFactory.create_panel(type, view_all, false);
				
			}
		});
		
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
}
