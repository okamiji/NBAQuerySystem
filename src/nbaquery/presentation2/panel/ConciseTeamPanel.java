package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import nbaquery.presentation.MyBasicComboBoxUI;
import nbaquery.presentation2.card.CardProperties;

public class ConciseTeamPanel extends ConcisePanel {

	public ConciseTeamPanel(int get_player, int get_view_limit) {
		super(get_player, get_view_limit);
	}
	
	public void init_team_panel(){	
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(245, 245, 245));
		search_panel.setBounds(130, 20, 570, 60);

		typeBox = new JComboBox<String>();
		typeBox.setBounds(20, 15, 80, 24);
		typeBox.setUI(new MyBasicComboBoxUI(typeBox));
		search_panel.add(typeBox);

		typeBox.addItem("ȫ������");
		typeBox.addItem("��������");

		valueBox = new JComboBox<String>();
		valueBox.setBounds(115, 15, 110, 24);
		valueBox.setUI(new MyBasicComboBoxUI(valueBox));
		search_panel.add(valueBox);
		
		valueBox.addItem("��������������");
		valueBox.addItem("Ͷ��������");
		valueBox.addItem("Ͷ�����ִ���");
		valueBox.addItem("����������");
		valueBox.addItem("���ֳ�����");
		valueBox.addItem("����������");
		valueBox.addItem("���������");
		valueBox.addItem("����������");
		valueBox.addItem("����������");
		valueBox.addItem("������");
		valueBox.addItem("������");
		valueBox.addItem("������");
		valueBox.addItem("��ñ��");
		valueBox.addItem("ʧ����");
		valueBox.addItem("������");
		valueBox.addItem("�����÷�");
		valueBox.addItem("Ͷ��������");
		valueBox.addItem("����������");
		valueBox.addItem("����������");
		valueBox.addItem("ʤ��");
		valueBox.addItem("�����غ�");
		valueBox.addItem("����Ч��");
		valueBox.addItem("����Ч��");
		valueBox.addItem("��������Ч��");
		valueBox.addItem("��������Ч��");
		valueBox.addItem("����Ч��");
		valueBox.addItem("����Ч��");

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

		boolean isGross = CardProperties.get_team_isGross();
		if(isGross){
			typeBox.setSelectedIndex(0);
		}
		else{
			typeBox.setSelectedIndex(1);
		}
		
		PanelSet.get_concise().valueBox.setSelectedIndex(CardProperties.get_team_index_index());				
		isUp = CardProperties.get_team_isUp();
		PanelSet.get_concise().ascendButton.setVisible(!isUp);
		PanelSet.get_concise().descendButton.setVisible(isUp);
		
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
				boolean isGross = false;
				if(((String)typeBox.getSelectedItem()).equals("ȫ������")){
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
				ConciseTeamPanel cp = new ConciseTeamPanel(2, PanelSet.get_view_limit());
				cp.init_team_panel();
				PanelSet.get_concise().run();
				
			}
		});
	}
}
