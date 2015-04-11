package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import nbaquery.presentation.combobox.ComboBoxFactory;
import nbaquery.presentation.combobox.MyBasicComboBoxUI;
import nbaquery.presentation2.card.Card;
import nbaquery.presentation2.card.CardLocation;
import nbaquery.presentation2.card.CardProperties;
import nbaquery.presentation2.panel.PanelSet;

public class ConcisePanel {
	//Has been added to chosen panel when initialized.

	private JFrame frame;
	
	private JPanel concise_panel, search_panel;
	private JScrollPane scr;
	
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
	}
	
	public void run(){
		
		concise_panel = new JPanel();
		concise_panel.setLayout(null);
		concise_panel.setBackground(new Color(245, 245, 245));
		concise_panel.setBounds(0, 60, 600, 481);

		search_panel = new JPanel();
		
		switch(player_or_team_or_match){
		case 1:
			init_player_panel();
			break;
		case 2:
			init_team_panel();
			break;
		case 3:
			//TODO
			break;
		}
		
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
	
	
	private void init_player_panel(){
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(245, 245, 245));
		search_panel.setBounds(130, 20, 570, 60);
		/*
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
		*/
		//XXX------------------------------------------------------------
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
		
		//XXX------------------------------------------------------------
		
		descendButton = new JButton();
		descendButton.setIcon(new ImageIcon("Img2/descend.png"));
		descendButton.setContentAreaFilled(false);
		descendButton.setBounds(420, 15, 24, 24);
		search_panel.add(descendButton);
		
		ascendButton = new JButton();
		ascendButton.setIcon(new ImageIcon("Img2/ascend.png"));
		ascendButton.setContentAreaFilled(false);
		ascendButton.setBounds(420, 15, 24, 24);
		search_panel.add(ascendButton);
		ascendButton.setVisible(false);
		
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
				
				PanelSet.get_concise().set_combobox();
				PanelSet.set_concise_invisible();
				@SuppressWarnings("unused")
				ConcisePanel cp = new ConcisePanel(1, PanelSet.get_view_limit());
				PanelSet.get_concise().run();
				PanelSet.get_concise().get_combobox();
			}
		});
		
	}
	
	private void init_team_panel(){	
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(245, 245, 245));
		search_panel.setBounds(130, 20, 570, 60);
		/*
		typeBox = new JComboBox<String>();
		typeBox.setBounds(20, 15, 80, 24);
		typeBox.setUI(new MyBasicComboBoxUI(typeBox));
		search_panel.add(typeBox);

		typeBox.addItem("全局数据");
		typeBox.addItem("场均数据");

		valueBox = new JComboBox<String>();
		valueBox.setBounds(115, 15, 110, 24);
		valueBox.setUI(new MyBasicComboBoxUI(valueBox));
		search_panel.add(valueBox);
		
		valueBox.addItem("按比赛场数排序");
		valueBox.addItem("投篮命中数");
		valueBox.addItem("投篮出手次数");
		valueBox.addItem("三分命中数");
		valueBox.addItem("三分出手数");
		valueBox.addItem("罚球命中数");
		valueBox.addItem("罚球出手数");
		valueBox.addItem("进攻篮板数");
		valueBox.addItem("防守篮板数");
		valueBox.addItem("篮板数");
		valueBox.addItem("助攻数");
		valueBox.addItem("抢断数");
		valueBox.addItem("盖帽数");
		valueBox.addItem("失误数");
		valueBox.addItem("犯规数");
		valueBox.addItem("比赛得分");
		valueBox.addItem("投篮命中率");
		valueBox.addItem("三分命中率");
		valueBox.addItem("罚球命中率");
		valueBox.addItem("胜率");
		valueBox.addItem("进攻回合");
		valueBox.addItem("进攻效率");
		valueBox.addItem("防守效率");
		valueBox.addItem("进攻篮板效率");
		valueBox.addItem("防守篮板效率");
		valueBox.addItem("抢断效率");
		valueBox.addItem("助攻效率");
		*/
		
		//XXX------------------------------------------------------------------------
		
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
		
		//XXX------------------------------------------------------------------------
		
		descendButton = new JButton();
		descendButton.setIcon(new ImageIcon("Img2/descend.png"));
		descendButton.setContentAreaFilled(false);
		descendButton.setBounds(420, 15, 24, 24);
		search_panel.add(descendButton);
		
		ascendButton = new JButton();
		ascendButton.setIcon(new ImageIcon("Img2/ascend.png"));
		ascendButton.setContentAreaFilled(false);
		ascendButton.setBounds(420, 15, 24, 24);
		search_panel.add(ascendButton);
		ascendButton.setVisible(false);
		
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
		searchButton.setBounds(460, 15, 72, 24);
		search_panel.add(searchButton);
		
		searchButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				boolean isGross = false;
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
				
				PanelSet.set_concise_invisible();
				@SuppressWarnings("unused")
				ConcisePanel cp = new ConcisePanel(2, PanelSet.get_view_limit());
				PanelSet.get_concise().run();
				
				PanelSet.get_concise().valueBox.setSelectedIndex(CardProperties.get_team_index_index());				
				isUp = CardProperties.get_team_isUp();
				PanelSet.get_concise().ascendButton.setVisible(!isUp);
				PanelSet.get_concise().descendButton.setVisible(isUp);
			}
		});
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
		int i1 = typeBox.getSelectedIndex();
		int i2 = valueBox.getSelectedIndex();
		int i3 = positionBox.getSelectedIndex();
		int i4 = leagueBox.getSelectedIndex();
		CardProperties.set_player_combobox_index(i1, i2, i3, i4);
	}
	
	public void get_combobox(){
		int[] set_combobox = CardProperties.get_player_combobox_index();
		typeBox.setSelectedIndex(set_combobox[0]);
		valueBox.setSelectedIndex(set_combobox[1]);
		positionBox.setSelectedIndex(set_combobox[2]);
		leagueBox.setSelectedIndex(set_combobox[3]);
		
		isUp = CardProperties.get_player_isUp();
		ascendButton.setVisible(!isUp);
		descendButton.setVisible(isUp);
	}
	
}

