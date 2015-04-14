package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import nbaquery.presentation.combobox.ComboBoxFactory;
import nbaquery.presentation2.addedcard.CardType;

public class ConcisePlayerPanel extends ConcisePanel {

	CardType type;
	boolean view_all;
	
	public ConcisePlayerPanel(CardType type_, boolean view_all_) {
		super(type_, view_all_);
		
		type = type_;
		view_all = view_all_;
	}

	public void run(){
		super.run();
		
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(245, 245, 245));
		search_panel.setBounds(130, 20, 570, 60);
		
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
		
		ascendButton.setVisible(!ConcisePara.player_isUp);
		descendButton.setVisible(ConcisePara.player_isUp);
		
		descendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				descendButton.setVisible(false);
				ascendButton.setVisible(true);
				ConcisePara.player_isUp = false;
			}
		});
		ascendButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ascendButton.setVisible(false);
				descendButton.setVisible(true);
				ConcisePara.player_isUp = true;
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
				
				typeBox.setSelectedIndex(ConcisePara.player_isGross_index);
				valueBox.setSelectedIndex(ConcisePara.player_index_index);
				positionBox.setSelectedIndex(ConcisePara.player_position_index);
				leagueBox.setSelectedIndex(ConcisePara.player_league_index);
				
				PanelSet.set_concise_invisible();
				ConcisePanelFactory.create_panel(type, view_all);
				
			}
		});
		
	}
	
}
