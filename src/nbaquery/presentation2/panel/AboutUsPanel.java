package nbaquery.presentation2.panel;

import java.awt.Color;

import javax.swing.JLabel;

import nbaquery.presentation2.util.CardType;

@SuppressWarnings("serial")
public class AboutUsPanel extends ConcisePanel {
	
	public AboutUsPanel(CardType type, boolean view_all_cards) {
		super(type, view_all_cards);
		
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(0, 0, 0, 0));
		search_panel.setBounds(40, 80, 570, 350);
		search_panel.setVisible(true);
		
		JLabel label_info = new JLabel();
		
		label_info.setForeground(new Color(191, 211, 200));
		label_info.setBounds(200, 40, 200, 300);
		label_info.setText(set_text());
		
		search_panel.add(label_info);
	}
	
	private String set_text(){
		String set_text = "<html>";
		set_text += "<b>��III����ҵ - NBA��ѯϵͳ</b>" + "<br/>" + "<br/>";
		set_text += "��07��" + "<br/>";
		set_text += "- �������Ա��" + "<br/>";
		set_text += "- okamiji - ����Դ" + "<br/>";
		set_text += "- DongCanRola - ����" + "<br/>";
		set_text += "- XNYu - ��Ӿ��" + "<br/>";
		set_text += "- aegistudio - �޺�Ȼ" + "<br/>";
		
		return set_text;
	}
}
