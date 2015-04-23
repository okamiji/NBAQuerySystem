package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;
import nbaquery.presentation.combobox.ComboBoxFactory;
import nbaquery.presentation2.addedcard.Card;
import nbaquery.presentation2.card.CardCreator;
import nbaquery.presentation2.card.CardLocation;
import nbaquery.presentation2.util.Button;
import nbaquery.presentation2.util.CardType;
import nbaquery.presentation2.util.HotspotType;
import static nbaquery.presentation2.util.HotspotType.*;

@SuppressWarnings("serial")
public class HotspotPanel extends ConcisePanel{

	boolean view_all;

	String[][] str = null;
	
	JButton daily_player, season_player, season_team, progress_player;
	JComboBox<String> box1, box2, box3, box4;
	
	boolean imageLoaded = false;
	static ImageIcon hotspot1, hotspot1_, hotspot2, hotspot2_, hotspot3, hotspot3_, hotspot4, hotspot4_;
	
	public HotspotPanel(CardType type_, boolean view_all_) {
		super(type_, view_all_);
		type = type_;
		view_all = view_all_;
		
		if(!imageLoaded)
		{
			hotspot1 = new ImageIcon("Img2/hotspot1.png");
			hotspot1_ = new ImageIcon("Img2/hotspot1_.png");
			hotspot2 = new ImageIcon("Img2/hotspot2.png");
			hotspot2_ = new ImageIcon("Img2/hotspot2_.png");
			hotspot3 = new ImageIcon("Img2/hotspot3.png");
			hotspot3_ = new ImageIcon("Img2/hotspot3_.png");
			hotspot4 = new ImageIcon("Img2/hotspot4.png");
			hotspot4_ = new ImageIcon("Img2/hotspot4_.png");
		}
	}
	
	public void run(){
		super.run();
		try {
			add_cards();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		super.set_scr();
		scr.setBounds(20, 120, 610, 400);
		
		button_panel.setLayout(null);
		button_panel.setBackground(new Color(0, 0, 0, 0));
		button_panel.setBounds(60, 70, 520, 50);
		
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(0, 0, 0, 0));
		search_panel.setBounds(40, 25, 570, 60);
		super.add(search_panel);
		
		init_combobox();
		
		daily_player = new Button("Img2/hotspot1.png", "Img2/hotspot1_.png", button_panel);
		season_player = new Button("Img2/hotspot2.png", "Img2/hotspot2_.png", button_panel);
		season_team = new Button("Img2/hotspot3.png", "Img2/hotspot3_.png", button_panel);
		progress_player = new Button("Img2/hotspot4.png", "Img2/hotspot4_.png", button_panel);

		daily_player.setBounds(0, 10, 120, 40);
		season_player.setBounds(120, 10, 120, 40);
		season_team.setBounds(240, 10, 120, 40);
		progress_player.setBounds(360, 10, 120, 40);
		
		daily_player.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				stateSwitch(DAILY_PLAYER);
			}
		});
		
		season_player.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				stateSwitch(SEASON_PLAYER);
			}
		});
		
		season_team.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				stateSwitch(SEASON_TEAM);
			}
		});

		progress_player.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				stateSwitch(PROGRESS_PLAYER);
			}
		});
		
		searchButton = new Button("Img2/search_button.png", "Img2/search_button_c.png", search_panel);
		searchButton.setBounds(460, 15, 72, 24);
		search_panel.add(searchButton);
		
		searchButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				switch(ConcisePara.hotspot_type){
				case DAILY_PLAYER: ConcisePara.hot_daily_player_index = box1.getSelectedIndex(); break;
				case SEASON_PLAYER: ConcisePara.hot_season_player_index = box2.getSelectedIndex(); break;
				case SEASON_TEAM: ConcisePara.hot_season_team_index = box3.getSelectedIndex(); break;
				case PROGRESS_PLAYER: ConcisePara.hot_progress_player_index = box4.getSelectedIndex(); break;
				default:break;
	
				}
				if(ConcisePara.hotspot_type.isPlayer){
					ConcisePara.player_index_index = valueBox.getSelectedIndex();
					ConcisePara.player_item_name = (String)(valueBox.getSelectedItem());
				}
				else if(ConcisePara.hotspot_type.isTeam){
					ConcisePara.team_index = valueBox.getSelectedIndex();
					ConcisePara.team_item_name = (String)(valueBox.getSelectedItem());
				}
				PanelSet.set_concise_invisible();
				ConcisePanelFactory.create_panel(ConcisePara.type, true, true);
			}
		});
	}
	
	public void stateSwitch(HotspotType stateSwitch)
	{
		if(stateSwitch == null) return;
		if(stateSwitch == ConcisePara.hotspot_type) return;
		ConcisePara.hotspot_type = stateSwitch;
		set_combobox_invisible();
		
		if(stateSwitch == HotspotType.DAILY_PLAYER)
		{
			daily_player.setIcon(hotspot1_);
			box1.setVisible(true);
			box1.setSelectedIndex(ConcisePara.hot_daily_player_index);
		}
		else daily_player.setIcon(hotspot1);
		
		if(stateSwitch == HotspotType.SEASON_PLAYER)
		{
			season_player.setIcon(hotspot2_);
			box2.setVisible(true);
			box2.setSelectedIndex(ConcisePara.hot_season_player_index);
		}
		else season_player.setIcon(hotspot2);
		
		if(stateSwitch == HotspotType.SEASON_TEAM)
		{
			season_team.setIcon(hotspot3_);
			box3.setVisible(true);
			box3.setSelectedIndex(ConcisePara.hot_season_team_index);
		}
		else season_team.setIcon(hotspot3);
		
		if(stateSwitch == HotspotType.PROGRESS_PLAYER)
		{
			progress_player.setIcon(hotspot4_);
			box4.setVisible(true);
			box4.setSelectedIndex(ConcisePara.hot_progress_player_index);
		}
		else progress_player.setIcon(hotspot4);
		
		if(ConcisePara.type.isFlat){
			ConcisePara.type = CardType.PLAYER_FLAT;
		}
		else if(ConcisePara.type.isRect){
			ConcisePara.type = CardType.PLAYER_RECT;
		}
		
		if(stateSwitch.isPlayer) ConcisePara.player_index_index = 0;
		else if(stateSwitch.isTeam) ConcisePara.team_index = 0;
		
		PanelSet.set_concise_invisible();
		ConcisePanelFactory.create_panel(ConcisePara.type, true, true);
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
	
	private void init_combobox(){
		box1 = ComboBoxFactory.getInstance().createComboBox(20, 15, 100, 24, 
				new String[]{
				"按得分排序",
				"篮板",
				"助攻",
				"盖帽",
				"抢断",
				});
		search_panel.add(box1);
		
		box2 = ComboBoxFactory.getInstance().createComboBox(20, 15, 100, 24, 
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
		search_panel.add(box2);
		
		box3 = ComboBoxFactory.getInstance().createComboBox(20, 15, 100, 24, 
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
		search_panel.add(box3);
		
		box4 = ComboBoxFactory.getInstance().createComboBox(20, 15, 100, 24, 
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
		search_panel.add(box4);
	}
	
	private void set_combobox_invisible(){
		box1.setVisible(false);
		box2.setVisible(false);
		box3.setVisible(false);
		box4.setVisible(false);
	}
}
