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
		
		
		typeBox.addItem("ȫ������");
		typeBox.addItem("��������");
		
		positionBox.addItem("ȫ��λ��");
		positionBox.addItem("ǰ��");
		positionBox.addItem("�з�");
		positionBox.addItem("����");
		
		leagueBox.addItem("ȫ������");
		leagueBox.addItem("����");
		leagueBox.addItem("����");
		
		valueBox.addItem("����������");
		valueBox.addItem("��������");
		valueBox.addItem("�ȷ�����");
		valueBox.addItem("����");
		valueBox.addItem("����");
		valueBox.addItem("�ڳ�ʱ��");
		valueBox.addItem("Ͷ��������");
		valueBox.addItem("����������");
		valueBox.addItem("����������");
		valueBox.addItem("����");
		valueBox.addItem("����");
		valueBox.addItem("����");
		valueBox.addItem("��ñ");
		valueBox.addItem("ʧ��");
		valueBox.addItem("����");
		valueBox.addItem("�÷�");
		valueBox.addItem("Ч��");
		valueBox.addItem("GmSc");
		valueBox.addItem("��ʵ������");
		valueBox.addItem("Ͷ��Ч��");
		valueBox.addItem("������");
		valueBox.addItem("����������");
		valueBox.addItem("����������");
		valueBox.addItem("������");
		valueBox.addItem("������");
		valueBox.addItem("��ñ��");
		valueBox.addItem("ʧ����");
		valueBox.addItem("ʹ����");
		valueBox.addItem("��Աλ��");
		valueBox.addItem("����");
		valueBox.addItem("��/��/��");

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
				if(((String)typeBox.getSelectedItem()).equals("ȫ������")){
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
