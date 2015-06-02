package nbaquery.presentation2.addedcard;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import nbaquery.presentation.resource.ImageIconResource;
import nbaquery.presentation2.info.Player;
import nbaquery.presentation2.panel.PanelSet;

@SuppressWarnings("serial")
public class PlayerofMatchCard extends RectCard {
	String[] player_info;
	JLabel label_name, label_pic, label_info;
	ImageIcon pic;
	String player_text;
	
	public void create_card(Object obj) {
		
		this.setSize(260, 200);
		this.setLayout(null);
		this.setVisible(true);
		this.setBackground(new Color(0,0,0,0.0f));
		shadow_label = new JLabel(ImageIconResource.getImageIcon("Img2/card_shadow_big.png"));
		this.add(shadow_label);
		shadow_label.setBounds(0, 0, 260, 200);
		
		
		player_info = (String[]) obj;
		
		label_name = new JLabel();
		label_name.setText(player_info[1] + " " + player_info[2] + " " + player_info[7] + " : " + player_info[8]);
		label_name.setBackground(new Color(90, 225, 149));
		label_name.setOpaque(true);
		label_name.setForeground(Color.white);
		label_name.setFont(new Font("΢���ź�",Font.PLAIN, 12));	
		label_name.setBounds(14, 11, 232, 30);
		shadow_label.add(label_name);

		set_text();
		label_info = new JLabel(player_text);
		label_info.setForeground(new Color(191, 211, 200));
		label_info.setFont(new Font("΢���ź�",Font.PLAIN, 12));	
		label_info.setBounds(5, 48, 255, 137);
		label_info.setHorizontalAlignment(JLabel.CENTER);
		shadow_label.add(label_info);
		
		this.addMouseListener(new MouseAdapter(){
			@SuppressWarnings("unused")
			public void mouseClicked(MouseEvent e) {
				PanelSet.set_concise_invisible();
				String[] str = PanelSet.get_player_service().searchForOnePlayer(player_info[8]);
				Player player = new Player(str);
				String[] test = player.get_player_info();
				PanelSet.create_detailed_panel(player);
			}
		});
		
	}

	private void set_text(){
		player_text = "<html><center>";
		player_text += "λ�ã� " + player_info[9] + " ";
		player_text += "�ϳ�ʱ�䣺 " + player_info[10] + "�� " + player_info[11] + "��<br/>";
		player_text += "���ֽ���/�������� " + player_info[12] + " /  " + player_info[13] + "<br/>";
		player_text += "���ֽ���/�������� " + player_info[14] + " / " + player_info[15] + "<br/>";
		player_text += "�������/�������� " + player_info[16] + " /  " + player_info[17] + "<br/>";
		player_text += "����/����/�����壺 " + player_info[18] + " / " + player_info[19] + " / " + player_info[20] + "<br/>";
		player_text += "������ " + player_info[21] + " ���ϣ� " + player_info[22] + " ��ñ��" + player_info[23] + "<br/>";
		player_text += "ʧ��" + player_info[24] + " ���棺" + player_info[25] + "<br/>";
		player_text += "���˵÷֣�" + player_info[26] + "<br/>";
		player_text += "</center></html>";
	}

}


