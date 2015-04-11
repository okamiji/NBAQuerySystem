package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import nbaquery.presentation.MyBasicComboBoxUI;
import nbaquery.presentation2.card.CardProperties;

public class ConcisePlayerPanel extends ConcisePanel {

	public ConcisePlayerPanel(int get_player, int get_view_limit) {
		super(get_player, get_view_limit);
	}

	public void init_player_panel(){
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(245, 245, 245));
		search_panel.setBounds(130, 20, 570, 60);
		
		positionBox = new JComboBox<String>();
		positionBox.setBounds(20, 15, 80, 24);
	    positionBox.setUI(new MyBasicComboBoxUI(positionBox));
	    search_panel.add(positionBox);
	    
		leagueBox = new JComboBox<String>();
		leagueBox.setBounds(115, 15, 80, 24);
		leagueBox.setUI(new MyBasicComboBoxUI(leagueBox));
		search_panel.add(leagueBox);
					
		typeBox = new JComboBox<String>();
		typeBox.setBounds(210, 15, 80, 24);
		typeBox.setUI(new MyBasicComboBoxUI(typeBox));
		search_panel.add(typeBox);
		
		valueBox = new JComboBox<String>();
		valueBox.setBounds(305, 15, 100, 24);
		valueBox.setUI(new MyBasicComboBoxUI(valueBox));
		search_panel.add(valueBox);
		
		
		typeBox.addItem("全局数据");
		typeBox.addItem("场均数据");
		
		positionBox.addItem("全部位置");
		positionBox.addItem("前锋");
		positionBox.addItem("中锋");
		positionBox.addItem("后卫");
		
		leagueBox.addItem("全部联盟");
		leagueBox.addItem("东部");
		leagueBox.addItem("西部");
		
		valueBox.addItem("按名称排序");
		valueBox.addItem("参赛场数");
		valueBox.addItem("先发场数");
		valueBox.addItem("篮板");
		valueBox.addItem("助攻");
		valueBox.addItem("在场时间");
		valueBox.addItem("投篮命中率");
		valueBox.addItem("三分命中率");
		valueBox.addItem("罚球命中率");
		valueBox.addItem("进攻");
		valueBox.addItem("防守");
		valueBox.addItem("抢断");
		valueBox.addItem("盖帽");
		valueBox.addItem("失误");
		valueBox.addItem("犯规");
		valueBox.addItem("得分");
		valueBox.addItem("效率");
		valueBox.addItem("GmSc");
		valueBox.addItem("真实命中率");
		valueBox.addItem("投篮效率");
		valueBox.addItem("篮板率");
		valueBox.addItem("进攻篮板率");
		valueBox.addItem("防守篮板率");
		valueBox.addItem("助攻率");
		valueBox.addItem("抢断率");
		valueBox.addItem("盖帽率");
		valueBox.addItem("失误率");
		valueBox.addItem("使用率");
		valueBox.addItem("球员位置");
		valueBox.addItem("联盟");
		valueBox.addItem("分/板/助");

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
				ConcisePlayerPanel cp = new ConcisePlayerPanel(1, PanelSet.get_view_limit());
				cp.init_player_panel();
				PanelSet.get_concise().run();
				
			}
		});
		
	}
	
}
