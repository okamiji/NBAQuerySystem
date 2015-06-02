package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;

import nbaquery.logic.player.PlayerService;
import nbaquery.presentation.combobox.ComboBoxFactory;
import nbaquery.presentation.resource.ImageIconResource;
import nbaquery.presentation2.addedcard.Card;
import nbaquery.presentation2.card.CardCreator;
import nbaquery.presentation2.card.CardLocation;
import nbaquery.presentation2.util.CardType;

@SuppressWarnings("serial")
public class PlayerPanel extends ConcisePanel {

	boolean view_all;
	
	public PlayerPanel(CardType type_, boolean view_all_) {
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
		search_panel.setBounds(40, 25, 570, 60);
		
		positionBox = ComboBoxFactory.getInstance().createComboBox(20, 15, 80, 24, 
				new String[]{"ȫ��λ��", "ǰ��", "�з�", "����"});
	    search_panel.add(positionBox);
	    
		leagueBox = ComboBoxFactory.getInstance().createComboBox(115, 15, 80, 24, 
				new String[]{"ȫ������", "����", "����"});
		search_panel.add(leagueBox);
					
		typeBox = ComboBoxFactory.getInstance().createComboBox(210, 15, 80, 24, 
				new String[]{"ȫ������", "��������"});
		search_panel.add(typeBox);
		
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
		
		typeBox.setSelectedIndex(ConcisePara.player_isGross_index);
		valueBox.setSelectedIndex(ConcisePara.player_index_index);
		positionBox.setSelectedIndex(ConcisePara.player_position_index);
		leagueBox.setSelectedIndex(ConcisePara.player_league_index);
		
		
		descendButton = new JButton();
		descendButton.setIcon(ImageIconResource.getImageIcon("Img2/descend.png"));
		descendButton.setContentAreaFilled(false);
		descendButton.setBorder(null);
		descendButton.setBounds(420, 15, 24, 24);
		search_panel.add(descendButton);
		
		ascendButton = new JButton();
		ascendButton.setIcon(ImageIconResource.getImageIcon("Img2/ascend.png"));
		ascendButton.setContentAreaFilled(false);
		ascendButton.setBorder(null);
		ascendButton.setBounds(420, 15, 24, 24);
		search_panel.add(ascendButton);
		ascendButton.setVisible(false);
		
		ascendButton.setVisible(!ConcisePara.player_isUp);
		descendButton.setVisible(ConcisePara.player_isUp);
		
		descendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				descendButton.setVisible(false);
				ascendButton.setVisible(true);
				ConcisePara.player_isUp = false;
				responseMouseClicked();
			}
		});
		ascendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ascendButton.setVisible(false);
				descendButton.setVisible(true);
				ConcisePara.player_isUp = true;
				responseMouseClicked();
			}
		});	
		
		update();
		
		ItemListener l = new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				responseMouseClicked();
			}
	    };
		
	    positionBox.addItemListener(l);
	    
	    leagueBox.addItemListener(l);
	    
	    typeBox.addItemListener(l);
	    
	    valueBox.addItemListener(l);
	}
	
	public void responseMouseClicked() {
		
		int index = valueBox.getSelectedIndex();
		if(index > 0){
			ConcisePara.player_index = index + 2;
		}
		else{
			ConcisePara.player_index = index + 1;
		}
		
		if(((String)typeBox.getSelectedItem()).equals("ȫ������")){
			ConcisePara.player_isGross = true;
		}
		else{
			ConcisePara.player_isGross = false;
		}
		
		ConcisePara.player_position = lookups.get((String) positionBox.getSelectedItem());
		ConcisePara.player_league = lookups.get((String) leagueBox.getSelectedItem());		
		
		ConcisePara.player_item_name = (String)(valueBox.getSelectedItem());
		ConcisePara.view_all = false;
		
		ConcisePara.player_isGross_index = typeBox.getSelectedIndex();
		ConcisePara.player_index_index = valueBox.getSelectedIndex();
		ConcisePara.player_position_index = positionBox.getSelectedIndex();
		ConcisePara.player_league_index = leagueBox.getSelectedIndex();
		
		PanelSet.set_concise_invisible();
		ConcisePanelFactory.create_panel(type, view_all, false);
		
	}
	
	public void update()
	{
		typeBox.setSelectedIndex(ConcisePara.player_isGross_index);
		valueBox.setSelectedIndex(ConcisePara.player_index_index);
		positionBox.setSelectedIndex(ConcisePara.player_position_index);
		leagueBox.setSelectedIndex(ConcisePara.player_league_index);
	}
	
	private void add_cards(){
		PlayerService ps = PanelSet.ps;
		String[][] str = ps.searchForPlayers(ConcisePara.player_isGross, ConcisePara.player_index, ConcisePara.player_isUp, ConcisePara.player_position, ConcisePara.player_league);
		CardCreator creator = new CardCreator();
		ArrayList<Card> card_list = creator.create_needed_cards(ConcisePara.type, str, ConcisePara.view_all);
		CardLocation location = new CardLocation(ConcisePara.type);
		scr_height = location.get_total_height(card_list.size());
		for(int i=0; i<card_list.size(); i++){
			Card card = card_list.get(i);
			card_panel.add(card);
			card.setLocation(card.width, card.height);
		}
	}
}
