package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;
import nbaquery.presentation.combobox.ComboBoxFactory;
import nbaquery.presentation2.addedcard.Card;
import nbaquery.presentation2.card.CardCreator;
import nbaquery.presentation2.card.CardLocation;
import nbaquery.presentation2.util.Button;
import nbaquery.presentation2.util.CardType;
import nbaquery.presentation2.util.HotspotType;

@SuppressWarnings("serial")
public class HotspotPanel extends ConcisePanel{

	boolean view_all;

	String[][] str = null;
	
	JButton daily_player, season_player, season_team, progress_player;
	
	public HotspotPanel(CardType type_, boolean view_all_) {
		super(type_, view_all_);
		type = type_;
		view_all = view_all_;
		//TODO
//		ConcisePara.is_hot = false;
	}
	
	public void run(){
		super.run();
		try {
			add_cards();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		super.set_scr();
		
		scr.setBounds(60, 70, 590, 400);
		
		button_panel.setLayout(null);
		button_panel.setBackground(new Color(0, 0, 0, 0));
		button_panel.setBounds(0, 80, 82, 328);
		
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(0, 0, 0, 0));
		search_panel.setBounds(40, 25, 570, 60);
		
		daily_player = new Button("Img2/hotspot1.png", "Img2/hotspot1_.png", button_panel);
		season_player = new Button("Img2/hotspot2.png", "Img2/hotspot2_.png", button_panel);
		season_team = new Button("Img2/hotspot3.png", "Img2/hotspot3_.png", button_panel);
		progress_player = new Button("Img2/hotspot4.png", "Img2/hotspot4_.png", button_panel);

		daily_player.setBounds(0, 0, 82, 82);
		season_player.setBounds(0, 70, 82, 82);
		season_team.setBounds(0, 140, 82, 82);
		progress_player.setBounds(0, 210, 82, 82);

		set_progress_player_combobox();
		daily_player.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				daily_player.setIcon(new ImageIcon("Img2/hotspot1_.png"));
///				ConcisePara.is_hot = true;
				//TODO
				valueBox.setVisible(false);
				remove(valueBox);
				set_daily_player_combobox();
				valueBox.setSelectedIndex(ConcisePara.hot_season_player_index);
				
				ConcisePara.hot_daily_player_index = 0;
				ConcisePara.player_index_index = 0;
				ConcisePara.player_item_name = "参赛场数";
				if(ConcisePara.type.equals(CardType.TEAM_FLAT) || (ConcisePara.type.equals(CardType.PLAYER_FLAT))){
					ConcisePara.type = CardType.PLAYER_FLAT;
				}
				else if(ConcisePara.type.equals(CardType.TEAM_RECT) || (ConcisePara.type.equals(CardType.PLAYER_RECT))){
					ConcisePara.type = CardType.PLAYER_RECT;
				}
				ConcisePara.hotspot_type = HotspotType.DAILY_PLAYER;
				PanelSet.set_concise_invisible();
				ConcisePanelFactory.create_panel(ConcisePara.type, true, true);
			}
		});
		
		season_player.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				season_player.setIcon(new ImageIcon("Img2/hotspot2_.png"));
//				ConcisePara.is_hot = true;
				//TODO
				valueBox.setVisible(false);
				remove(valueBox);
				set_season_player_combobox();
				valueBox.setSelectedIndex(ConcisePara.hot_daily_player_index);
				
				ConcisePara.hot_season_player_index = 0;
				ConcisePara.player_index_index = 0;
				ConcisePara.player_item_name = "按参赛场数排序";
				if(ConcisePara.type.equals(CardType.TEAM_FLAT) || (ConcisePara.type.equals(CardType.PLAYER_FLAT))){
					ConcisePara.type = CardType.PLAYER_FLAT;
				}
				else if(ConcisePara.type.equals(CardType.TEAM_RECT) || (ConcisePara.type.equals(CardType.PLAYER_RECT))){
					ConcisePara.type = CardType.PLAYER_RECT;
				}
				ConcisePara.hotspot_type = HotspotType.SEASON_PLAYER;
				PanelSet.set_concise_invisible();
				ConcisePanelFactory.create_panel(ConcisePara.type, true, true);
			}
		});
		
		season_team.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_buttons();
				season_team.setIcon(new ImageIcon("Img2/hotspot3_.png"));
//				ConcisePara.is_hot = true;
				//TODO

				valueBox.setVisible(false);
				remove(valueBox);
				set_season_team_combobox();
				valueBox.setSelectedIndex(ConcisePara.hot_season_team_index);
				
				ConcisePara.hot_season_team_index = 0;
				ConcisePara.team_index = 0;
				ConcisePara.team_item_name = "按赛季排序";
				if(ConcisePara.type.equals(CardType.TEAM_FLAT) || (ConcisePara.type.equals(CardType.PLAYER_FLAT))){
					ConcisePara.type = CardType.TEAM_FLAT;
				}
				else if(ConcisePara.type.equals(CardType.TEAM_RECT) || (ConcisePara.type.equals(CardType.PLAYER_RECT))){
					ConcisePara.type = CardType.TEAM_RECT;
				}
				ConcisePara.hotspot_type = HotspotType.SEASON_TEAM;
				PanelSet.set_concise_invisible();
				ConcisePanelFactory.create_panel(ConcisePara.type, true, true);
			}
		});

		progress_player.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				progress_player.setIcon(new ImageIcon("Img2/hotspot4_.png"));
//				ConcisePara.is_hot = true;
				//TODO

				valueBox.setVisible(false);
				remove(valueBox);
				set_progress_player_combobox();
				valueBox.setSelectedIndex(ConcisePara.hot_progress_player_index);
				
				ConcisePara.hot_progress_player_index = 0;
				ConcisePara.player_index_index = 0;
				ConcisePara.player_item_name = "按参赛场数排序";
				if(ConcisePara.type.equals(CardType.TEAM_FLAT) || (ConcisePara.type.equals(CardType.PLAYER_FLAT))){
					ConcisePara.type = CardType.PLAYER_FLAT;
				}
				else if(ConcisePara.type.equals(CardType.TEAM_RECT) || (ConcisePara.type.equals(CardType.PLAYER_RECT))){
					ConcisePara.type = CardType.PLAYER_RECT;
				}
				ConcisePara.hotspot_type = HotspotType.PROGRESS_PLAYER;
				PanelSet.set_concise_invisible();
				ConcisePanelFactory.create_panel(ConcisePara.type, true, true);
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
				switch(ConcisePara.hotspot_type){
				case DAILY_PLAYER: ConcisePara.hot_daily_player_index = valueBox.getSelectedIndex(); break;
				case SEASON_PLAYER: ConcisePara.hot_season_player_index = valueBox.getSelectedIndex(); break;
				case SEASON_TEAM: ConcisePara.hot_season_team_index = valueBox.getSelectedIndex(); break;
				case PROGRESS_PLAYER: ConcisePara.hot_progress_player_index = valueBox.getSelectedIndex(); break;
				default:break;
				}
				if(ConcisePara.hotspot_type.equals(HotspotType.PROGRESS_PLAYER) || (ConcisePara.hotspot_type.equals(HotspotType.DAILY_PLAYER)) 
						|| (ConcisePara.hotspot_type.equals(HotspotType.SEASON_PLAYER))){
					ConcisePara.player_index_index = valueBox.getSelectedIndex();
					ConcisePara.player_item_name = (String)(valueBox.getSelectedItem());
				}
				else if(ConcisePara.hotspot_type.equals(HotspotType.SEASON_TEAM)){
					ConcisePara.team_index = valueBox.getSelectedIndex();
					ConcisePara.team_item_name = (String)(valueBox.getSelectedItem());
				}
				PanelSet.set_concise_invisible();
				ConcisePanelFactory.create_panel(ConcisePara.type, true, true);
			}
		});
	}
		
	private void add_cards() throws Exception{
		PlayerService ps = PanelSet.ps;
		TeamService ts = PanelSet.ts;
		switch(ConcisePara.hotspot_type){
		case DAILY_PLAYER:
			str = ps.searchForTodayHotPlayers(ConcisePara.hot_daily_player_index);break;
		case SEASON_PLAYER:
			str = ps.searchForSeasonHotPlayers(ConcisePara.hot_season_player_index);
			//TODO
			if(str == null){
				System.out.println("is null");
			}
			for(int i=0; i<str.length; i++){
				for(int j=0; j<str[0].length; j++){
					System.out.println( i + "  " + j + " "+ str[i][j]);
				}
			}
			//TODO
			break;
			
		case PROGRESS_PLAYER:
			str = ps.searchForProgressPlayers(ConcisePara.hot_progress_player_index);
		//TODO
			if(str == null){
				System.out.println("is null");
			}
			for(int i=0; i<str.length; i++){
				for(int j=0; j<str[0].length; j++){
					System.out.println(str[i][j]);
				}
			}
			//TODO
			break;
		case SEASON_TEAM:
			str = ts.searchForSeasonHotTeams(ConcisePara.hot_season_team_index);break;
		default:
			break;
		}
		
		CardCreator creator = new CardCreator();
		ConcisePara.is_hot = true;
		ArrayList<Card> card_list = creator.create_needed_cards(type, str, view_all);
		CardLocation location = new CardLocation(type);
		scr_height = location.get_total_height(card_list.size());
		for(int i=0; i<card_list.size(); i++){
			Card card = card_list.get(i);
			card_panel.add(card);
			card.setLocation(card.width, card.height);
		}
		ConcisePara.is_hot = false;
	}
	private void set_daily_player_combobox(){
		valueBox = ComboBoxFactory.getInstance().createComboBox(20, 15, 100, 24, 
				new String[]{
				"按得分排序",
				"篮板",
				"助攻",
				"盖帽",
				"抢断",
				});
		search_panel.add(valueBox);
	}
	private void set_season_player_combobox(){
		valueBox = ComboBoxFactory.getInstance().createComboBox(20, 15, 100, 24, 
				new String[]{
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
	}
	private void set_season_team_combobox(){
		valueBox = ComboBoxFactory.getInstance().createComboBox(20, 15, 100, 24, 
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
	}
	
	private void set_progress_player_combobox(){
		valueBox = ComboBoxFactory.getInstance().createComboBox(20, 15, 100, 24, 
				new String[]{
				"按近五场场均得分排序",
				"近五场篮板",
				"近五场助攻",
				"场均得分",
				"篮板",
				"助攻",
				"场均得分进步率",
				"篮板进步率",
				"助攻进步率",
				});
		search_panel.add(valueBox);
	}
	private void init_buttons(){
		daily_player.setIcon(new ImageIcon("Img2/hotspot1.png"));
		season_player.setIcon(new ImageIcon("Img2/hotspot2.png"));
		season_team.setIcon(new ImageIcon("Img2/hotspot3.png"));
		progress_player.setIcon(new ImageIcon("Img2/hotspot4.png"));

	}
}
