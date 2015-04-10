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

import nbaquery.presentation.MyBasicComboBoxUI;
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
	String item_name;
	
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
		
		descendButton = new JButton();
		descendButton.setBackground(Color.BLACK);
		descendButton.setBounds(420, 15, 24, 24);
		search_panel.add(descendButton);
		
		ascendButton = new JButton();
		ascendButton.setBackground(Color.WHITE);
		ascendButton.setBounds(420, 15, 24, 24);
		search_panel.add(ascendButton);
		ascendButton.setVisible(false);
		
		descendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ascendButton.setVisible(true);
				descendButton.setVisible(false);
				isUp = true;
			}
		});
		ascendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ascendButton.setVisible(false);
				descendButton.setVisible(true);
				isUp = false;
				
				//TODO upppppp?
			}
		});
		
		searchButton = new JButton("NBAQuery");
		searchButton.setBounds(460, 15, 72, 24);
		search_panel.add(searchButton);		
		
		searchButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				item_name = (String)(valueBox.getSelectedItem());
				
				boolean isGross = false;
				if(((String)typeBox.getSelectedItem()).equals("全局数据")){
					isGross=true;
				}
				String position = lookups.get((String) positionBox.getSelectedItem());
				String league = lookups.get((String) leagueBox.getSelectedItem());		
				int index = valueBox.getSelectedIndex();
				
				CardProperties.set_player_isUp(isUp);
				CardProperties.set_player_index(index);
				CardProperties.set_player_isGross(isGross);
				CardProperties.set_player_position(position);
				CardProperties.set_player_league(league);
				
				PanelSet.get_concise().set_combobox();
				PanelSet.set_concise_invisible();
				@SuppressWarnings("unused")
				ConcisePanel cp = new ConcisePanel(1, PanelSet.get_view_limit());
				PanelSet.get_concise().run();
				PanelSet.get_concise().get_combobox();
			}
		});
		
	}
	public String get_item_name(){
		return item_name;
	}
	
	private void init_team_panel(){	
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(245, 245, 245));
		search_panel.setBounds(130, 20, 570, 60);

		typeBox = new JComboBox<String>();
		typeBox.setBounds(20, 10, 80, 24);
		typeBox.setUI(new MyBasicComboBoxUI(typeBox));
		search_panel.add(typeBox);

		typeBox.addItem("全局数据");
		typeBox.addItem("场均数据");
		
		searchButton = new JButton("",new ImageIcon("IMGS/search.png"));
		searchButton.setBounds(500, 10, 35, 40);
		searchButton.setFocusPainted(false);
		searchButton.setBorderPainted(false);
		searchButton.setContentAreaFilled(false);
		search_panel.add(searchButton);
		
		searchButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
			/*	position=lookups.get((String) positionBox.getSelectedItem());
				league=lookups.get((String) leagueBox.getSelectedItem());
				
				if(((String)typeBox.getSelectedItem()).equals("全局数据"))
					isGlobal=true;
				else
					isGlobal=false;	*/
				/*
				strs=ps.searchForPlayers(isGlobal,head, upDown, position, league);
				updateTable(strs);*/
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
		int[] get_index = CardProperties.get_player_combobox_index();
		int i1 = 0;
		int i2 = 0;
		int i3 = 0;
		int i4 = 0;
		if(get_index[0] == 0){
			i1 = typeBox.getSelectedIndex();
		}
		if(get_index[1] == 0){
			i2 = valueBox.getSelectedIndex();
		}
		if(get_index[2] == 0){
			i3 = positionBox.getSelectedIndex();
		}
		if(get_index[3] == 0){
			i4 = leagueBox.getSelectedIndex();
		}
		CardProperties.set_player_combobox_index(i1, i2, i3, i4);
		for(int i=0; i<get_index.length; i++){
			System.out.println(get_index[i]);
		}
	}
	
	public void get_combobox(){
		int[] set_combobox = CardProperties.get_player_combobox_index();
		typeBox.setSelectedIndex(set_combobox[0]);
		valueBox.setSelectedIndex(set_combobox[1]);
		positionBox.setSelectedIndex(set_combobox[2]);
		leagueBox.setSelectedIndex(set_combobox[3]);
		
		isUp = CardProperties.get_player_isUp();
		ascendButton.setVisible(isUp);
		descendButton.setVisible(!isUp);
	}
	
}

