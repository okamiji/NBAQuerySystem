package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import nbaquery.presentation.combobox.ComboBoxFactory;
import nbaquery.presentation2.card.CardProperties;

public class ConcisePlayerPanel extends ConcisePanel {

	public ConcisePlayerPanel(int get_player, int get_view_limit) {
		super(get_player, get_view_limit);
	}

	public void init(){
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
		
		int[] set_combobox = CardProperties.get_player_combobox_index();
		typeBox.setSelectedIndex(set_combobox[0]);
		valueBox.setSelectedIndex(set_combobox[1]);
		positionBox.setSelectedIndex(set_combobox[2]);
		leagueBox.setSelectedIndex(set_combobox[3]);
		
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
		
		isUp = CardProperties.get_player_isUp();
		ascendButton.setVisible(!isUp);
		descendButton.setVisible(isUp);
		
		descendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				descendButton.setVisible(false);
				ascendButton.setVisible(true);
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
				
				int index = valueBox.getSelectedIndex();
				if(index > 0){
					CardProperties.set_player_index(index + 2);
				}
				else{
					CardProperties.set_player_index(index + 1);
				}
				
				boolean isGross = false;
				if(((String)typeBox.getSelectedItem()).equals("全局数据")){
					isGross=true;
				}
				
				String position = lookups.get((String) positionBox.getSelectedItem());
				String league = lookups.get((String) leagueBox.getSelectedItem());		
				
				CardProperties.set_player_isUp(isUp);
				CardProperties.set_player_isGross(isGross);
				CardProperties.set_player_position(position);
				CardProperties.set_player_league(league);

				//Fetch item name from certain combo box, which is given to Card and added when setting information of each card.
				CardProperties.set_player_item_name((String)(valueBox.getSelectedItem()));
				//Certain number of cards are released each time.
				CardProperties.set_if_view_all(false);
				


				int i1 = typeBox.getSelectedIndex();
				int i2 = valueBox.getSelectedIndex();
				int i3 = positionBox.getSelectedIndex();
				int i4 = leagueBox.getSelectedIndex();
				CardProperties.set_player_combobox_index(i1, i2, i3, i4);
			//	System.out.println("set  " + i1 + " " + i2 + " " + i3 + " " + i4);
				
				PanelSet.set_concise_invisible();
				ConcisePanel cp = new ConcisePlayerPanel(1, PanelSet.get_view_limit());
				cp.init();
				PanelSet.get_concise().run();
				
			}
		});
		
	}
	
}
