package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import nbaquery.presentation.combobox.ComboBoxFactory;
import nbaquery.presentation2.addedcard.CardType;

public class ConciseTeamPanel extends ConcisePanel {

	CardType type;
	boolean view_all;
	
	public ConciseTeamPanel(CardType type_, boolean view_all_) {
		super(type_, view_all_);
		
		type = type_;
		view_all = view_all_;
	}
	
	public void run(){
		super.run();
		
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(245, 245, 245));
		search_panel.setBounds(130, 20, 570, 60);

		typeBox = ComboBoxFactory.getInstance().createComboBox(20, 15, 80, 24, 
				new String[]{"全局数据", "场均数据"});
		search_panel.add(typeBox);

		valueBox = ComboBoxFactory.getInstance().createComboBox(115, 15, 110, 24, 
				new String[]{
				"按比赛场数排序",
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
/*
		boolean isGross = CardProperties.get_team_isGross();
		if(isGross){
			typeBox.setSelectedIndex(0);
		}
		else{
			typeBox.setSelectedIndex(1);
		}
		
		PanelSet.get_concise().valueBox.setSelectedIndex(CardProperties.get_team_index_index());				
		isUp = CardProperties.get_team_isUp();
		PanelSet.get_concise().ascendButton.setVisible(!isUp);
		PanelSet.get_concise().descendButton.setVisible(isUp);
		*/
		descendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ascendButton.setVisible(true);
				descendButton.setVisible(false);
				isUp = false;
			}
		});
		ascendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ascendButton.setVisible(false);
				descendButton.setVisible(true);
				isUp = true;
			}
		});
		
		searchButton = new JButton();
		searchButton.setIcon(new ImageIcon("Img2/search_button.png"));
		searchButton.setContentAreaFilled(false);
		searchButton.setBorder(null);
		searchButton.setBounds(460, 15, 72, 24);
		search_panel.add(searchButton);
		
		searchButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
			/*	boolean isGross = false;
				if(((String)typeBox.getSelectedItem()).equals("全局数据")){
					isGross = true;
				}
				
				int value_index = valueBox.getSelectedIndex();
				CardProperties.set_team_index(value_index + 2);
				CardProperties.set_team_isGross(isGross);
				CardProperties.set_team_isUp(isUp);
				
				CardProperties.set_team_index_index(value_index);
				//Fetch item name from certain combo box, which is given to Card and added when setting information of each card.
				CardProperties.set_team_item_name((String)(valueBox.getSelectedItem()));
				//Certain number of cards are released each time.
				CardProperties.set_if_view_all(false);
				*/
				PanelSet.set_concise_invisible();
				ConcisePanelFactory.create_panel(type, view_all);
				PanelSet.get_concise().run();
				
			}
		});
	}
}
