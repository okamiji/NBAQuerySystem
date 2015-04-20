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
import nbaquery.presentation2.util.CardType;
import nbaquery.presentation2.util.HotspotType;

public class HotspotPanel extends ConcisePanel{

	boolean view_all;

	String[][] str = null;
	
	JButton daily_player, season_player, season_team, progress_player;
	
	public HotspotPanel(CardType type_, boolean view_all_) {
		super(type_, view_all_);
		type = type_;
		view_all = view_all_;
	}
	
	public void run(){
		super.run();
		try {
			add_cards();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    scr.setBounds(110, 100, 600, 422);
		super.set_scr();
		button_panel.setLayout(null);
		search_panel.setBackground(new Color(245, 245, 245));
		button_panel.setBounds(130, 20, 570, 40);
		
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(245, 245, 245));
		search_panel.setBounds(130, 60, 570, 60);
		set_combobox();
		
		daily_player = new JButton("daily_player");
		season_player = new JButton("season_player");
		season_team = new JButton("season_team");
		progress_player = new JButton("progress_player");

		daily_player.setBounds(10, 0, 140, 30);
		season_player.setBounds(150, 0, 140, 30);
		season_team.setBounds(290, 0, 140, 30);
		progress_player.setBounds(430, 0, 140, 30);
		
		button_panel.add(daily_player);
		button_panel.add(season_player);
		button_panel.add(season_team);
		button_panel.add(progress_player);
		
		daily_player.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ConcisePara.hot_index = 0;
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
				ConcisePara.hot_index = 0;
				ConcisePara.player_index_index = 0;
				ConcisePara.player_item_name = "参赛场数";
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
				ConcisePara.hot_index = 0;
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
				ConcisePara.hot_index = 0;
				ConcisePara.player_index_index = 0;
				ConcisePara.player_item_name = "参赛场数";
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
				ConcisePara.hot_index = valueBox.getSelectedIndex();
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
			str = ps.searchForTodayHotPlayers(ConcisePara.hot_index);break;
		case SEASON_PLAYER:
			str = ps.searchForSeasonHotPlayers(ConcisePara.hot_index);break;
		case PROGRESS_PLAYER:
			str = ps.searchForProgressPlayers(ConcisePara.hot_index);
			if(str == null){
				System.out.println("is null");
			}
			for(int i=0; i<str.length; i++){
				for(int j=0; j<str[0].length; j++){
					System.out.println(str[i][j]);
				}
			}
			break;
		case SEASON_TEAM:
			str = ts.searchForSeasonHotTeams(ConcisePara.hot_index);break;
		default:
			break;
		}
		
		CardCreator creator = new CardCreator();
		ArrayList<Card> card_list = creator.create_needed_cards(type, str, view_all);
		CardLocation location = new CardLocation(type);
		scr_height = location.get_total_height(card_list.size());
		for(int i=0; i<card_list.size(); i++){
			Card card = card_list.get(i);
			concise_panel.add(card);
			card.setLocation(card.width, card.height);
		}
		concise_panel.repaint();
	}
	
	private void set_combobox(){
		switch(ConcisePara.hotspot_type){
		case DAILY_PLAYER:
			set_player_combobox();break;
		case SEASON_PLAYER:
			set_player_combobox();break;
		case PROGRESS_PLAYER:
			set_player_combobox();break;
		case SEASON_TEAM:
			set_team_combobox();break;
		default:
			break;
		}
		valueBox.setSelectedIndex(ConcisePara.hot_index);
	}
	private void set_player_combobox(){
		valueBox = ComboBoxFactory.getInstance().createComboBox(20, 15, 100, 24, 
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
	}
	private void set_team_combobox(){
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
	
}
