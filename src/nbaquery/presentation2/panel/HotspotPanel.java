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
		add_cards();
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
		progress_player.setBounds(290, 0, 140, 30);
		season_team.setBounds(430, 0, 140, 30);
		
		button_panel.add(daily_player);
		button_panel.add(season_player);
		button_panel.add(season_team);
		button_panel.add(progress_player);
		
		daily_player.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ConcisePara.type = CardType.PLAYER_RECT;
				ConcisePara.hotspot_type = HotspotType.DAILY_PLAYER;
				ConcisePanelFactory.create_panel(ConcisePara.type, true, true);
			}
		});
		
		season_player.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ConcisePara.type = CardType.PLAYER_RECT;
				ConcisePara.hotspot_type = HotspotType.SEASON_PLAYER;
				ConcisePanelFactory.create_panel(ConcisePara.type, true, true);
			}
		});
		
		season_team.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ConcisePara.type = CardType.TEAM_RECT;
				ConcisePara.hotspot_type = HotspotType.SEASON_TEAM;
				ConcisePanelFactory.create_panel(ConcisePara.type, true, true);
			}
		});

		progress_player.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ConcisePara.type = CardType.TEAM_RECT;
				ConcisePara.hotspot_type = HotspotType.PROGRESS_PLAYER;
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
				ConcisePanelFactory.create_panel(ConcisePara.type, true, true);
			}
		});
	}
		
	private void add_cards(){
		PlayerService ps = PanelSet.ps;
		TeamService ts = PanelSet.ts;
		switch(ConcisePara.hotspot_type){
		case DAILY_PLAYER:
			str = ps.searchForTodayHotPlayers(ConcisePara.hot_index);break;
		case SEASON_PLAYER:
			str = ps.searchForSeasonHotPlayers(ConcisePara.hot_index);break;
		case PROGRESS_PLAYER:
			str = ps.searchForProgressPlayers(ConcisePara.hot_index);break;
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
		valueBox = ComboBoxFactory.getInstance().createComboBox(305, 15, 100, 24, 
				new String[]{"����������",
				"��������",
				"�ȷ�����",
				"����",
				"����",
				"�ڳ�ʱ��",
				"Ͷ��������",
				"����������",
				"����������",
				"����",
				"����",
				"����",
				"��ñ",
				"ʧ��",
				"����",
				"�÷�",
				"Ч��",
				"GmSc",
				"��ʵ������",
				"Ͷ��Ч��",
				"������",
				"����������",
				"����������",
				"������",
				"������",
				"��ñ��",
				"ʧ����",
				"ʹ����",
				"��Աλ��",
				"����",
				"��/��/��",
				});
		search_panel.add(valueBox);
	}
	private void set_team_combobox(){
		valueBox = ComboBoxFactory.getInstance().createComboBox(20, 15, 100, 24, 
				new String[]{
				"����������",
				"�������",
				"��������",
				"Ͷ��������",
				"Ͷ�����ִ���",
				"����������",
				"���ֳ�����",
				"����������",
				"���������",
				"����������",
				"����������",
				"������",
				"������",
				"������",
				"��ñ��",
				"ʧ����",
				"������",
				"�����÷�",
				"Ͷ��������",
				"����������",
				"����������",
				"ʤ��",
				"�����غ�",
				"����Ч��",
				"����Ч��",
				"��������Ч��",
				"��������Ч��",
				"����Ч��",
				"����Ч��"});
		search_panel.add(valueBox);
	}
	
}
