package nbaquery.presentation2.panel;

import java.awt.Color;

import javax.swing.JLabel;

import nbaquery.presentation2.util.CardType;

@SuppressWarnings("serial")
public class AboutUsPanel extends ConcisePanel {
	
	public AboutUsPanel(CardType type, boolean view_all_cards) {
		super(type, view_all_cards);
		
		JLabel label_info = new JLabel();
		
		label_info.setForeground(new Color(191, 211, 200));
		label_info.setBounds(220, 40, 200, 300);
		label_info.setText(set_text());
		
		this.add(label_info);
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
