package nbaquery.presentation2.addedcard;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import nbaquery.presentation.resource.ImageIconResource;
import nbaquery.presentation2.info.Match;
import nbaquery.presentation2.panel.PanelSet;


@SuppressWarnings("serial")
public class MatchofPlayerCard extends RectCard {

	Match match;
	
	JLabel label_name, label_pic, label_info;
	ImageIcon pic;
	String match_text;
	
	public void create_card(Object obj) {
		
		this.setSize(260, 200);
		this.setLayout(null);
		this.setVisible(true);
		this.setBackground(new Color(0,0,0,0.0f));
		shadow_label = new JLabel(ImageIconResource.getImageIcon("Img2/card_shadow_big.png"));
		this.add(shadow_label);
		shadow_label.setBounds(0, 0, 260, 200);
		
		
		match = (Match) obj;
		
		label_name = new JLabel();
		label_name.setText("  " + match.get_season() + "  " + match.get_date() + "  " + match.get_team()[0] + " : " + match.get_team()[1] + "  " + match.get_score()[0] + " : " + match.get_score()[1]);
		label_name.setBackground(new Color(90, 225, 149));
		label_name.setOpaque(true);
		label_name.setForeground(Color.white);
		label_name.setFont(new Font("΢���ź�",Font.PLAIN, 12));	
		label_name.setBounds(14, 11, 232, 30);
		shadow_label.add(label_name);

		set_text(match);
		label_info = new JLabel(match_text);
		label_info.setForeground(new Color(191, 211, 200));
		label_info.setFont(new Font("΢���ź�",Font.PLAIN, 12));	
		label_info.setBounds(5, 48, 255, 137);
		label_info.setHorizontalAlignment(JLabel.CENTER);
		shadow_label.add(label_info);
		
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				PanelSet.set_concise_invisible();
				PanelSet.create_detailed_panel(match);
			}
		});
		
	}

	private void set_text(Match match){
		
		String[] match_info = match.get_match_info();
		match_text = "<html><center>";
		match_text += "λ�ã� " + match_info[9] + " ";
		match_text += "�ϳ�ʱ�䣺 " + match_info[10] + "�� " + match_info[11] + "��<br/>";
		match_text += "���ֽ���/�������� " + match_info[12] + " /  " + match_info[13] + "<br/>";
		match_text += "���ֽ���/�������� " + match_info[14] + " / " + match_info[15] + "<br/>";
		match_text += "�������/�������� " + match_info[16] + " /  " + match_info[17] + "<br/>";
		match_text += "����/����/�����壺 " + match_info[18] + " / " + match_info[19] + " / " + match_info[20] + "<br/>";
		match_text += "������ " + match_info[21] + " ���ϣ� " + match_info[22] + " ��ñ��" + match_info[23] + "<br/>";
		match_text += "ʧ��" + match_info[24] + " ���棺" + match_info[25] + "<br/>";
		match_text += "���˵÷֣�" + match_info[26] + "<br/>";
		match_text += "</center></html>";
		
	}

}


