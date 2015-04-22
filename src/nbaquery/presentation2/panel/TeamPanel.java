package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import nbaquery.logic.team.TeamService;
import nbaquery.presentation.combobox.ComboBoxFactory;
import nbaquery.presentation2.addedcard.Card;
import nbaquery.presentation2.util.Button;
import nbaquery.presentation2.util.CardType;
import nbaquery.presentation2.card.CardCreator;
import nbaquery.presentation2.card.CardLocation;

public class TeamPanel extends ConcisePanel {

	CardType type;
	boolean view_all;
	
	public TeamPanel(CardType type_, boolean view_all_) {
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
		
		typeBox = ComboBoxFactory.getInstance().createComboBox(20, 15, 80, 24, 
				new String[]{"全局数据", "场均数据"});
		search_panel.add(typeBox);

		valueBox = ComboBoxFactory.getInstance().createComboBox(115, 15, 110, 24, 
				new String[]{
				"按赛季排序",
				"球队名称",
				"比赛场数",
				"投篮命中数",
				"投篮出手次数",
				"三分命中数",
				"三分出手数",
				"罚球命中数",
				"罚球出手数",
				"进攻篮板数",
				"防守篮板数",
				"篮板数",
				"助攻数",
				"抢断数",
				"盖帽数",
				"失误数",
				"犯规数",
				"比赛得分",
				"投篮命中率",
				"三分命中率",
				"罚球命中率",
				"胜率",
				"进攻回合",
				"进攻效率",
				"防守效率",
				"进攻篮板效率",
				"防守篮板效率",
				"抢断效率",
				"助攻效率"});
		
		search_panel.add(valueBox);

		typeBox.setSelectedIndex(ConcisePara.team_isGross_index);
		valueBox.setSelectedIndex(ConcisePara.team_index);
		
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
		
		ascendButton.setVisible(!ConcisePara.team_isUp);
		descendButton.setVisible(ConcisePara.team_isUp);
		
		
		descendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ascendButton.setVisible(true);
				descendButton.setVisible(false);
				ConcisePara.team_isUp = false;
			}
		});
		ascendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ascendButton.setVisible(false);
				descendButton.setVisible(true);
				ConcisePara.team_isUp = true;
			}
		});
		
		searchButton = new Button("Img2/search_button.png", "Img2/search_button_c.png", search_panel);
		searchButton.setBounds(460, 15, 72, 24);		
		
		searchButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {

				ConcisePara.team_index = valueBox.getSelectedIndex();
				
				ConcisePara.team_item_name = (String)(valueBox.getSelectedItem());
				
				if(((String)typeBox.getSelectedItem()).equals("全局数据")){
					ConcisePara.team_isGross = true;
				}
				else{
					ConcisePara.team_isGross = false;
				}
				
				PanelSet.set_concise_invisible();
				ConcisePanelFactory.create_panel(type, ConcisePara.view_all, false);
				
			}
		});
	}
	
	private void add_cards(){
		TeamService ts = PanelSet.ts;
		String[][] str = ts.searchForTeams(ConcisePara.team_isGross, ConcisePara.team_index, ConcisePara.team_isUp);
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
