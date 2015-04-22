package nbaquery.presentation2.panel;

import java.awt.Color;

import javax.swing.JLabel;

import nbaquery.presentation2.util.CardType;

public class AboutUsPanel extends ConcisePanel {
	
	public AboutUsPanel(CardType type, boolean view_all_cards) {
		super(type, view_all_cards);
		
		search_panel.setLayout(null);
		search_panel.setBackground(new Color(0, 0, 0, 0));
		search_panel.setBounds(190, 125, 570, 400);
		this.add(search_panel);
		
		
		
		JLabel label_info = new JLabel();
		
		label_info.setForeground(new Color(191, 211, 200));
		label_info.setBounds(220, 40, 200, 300);
		label_info.setText(set_text());
		
		search_panel.add(label_info);
	}
	
	private String set_text(){
		String set_text = "<html>";
		set_text += "<b>软工III大作业 - NBA查询系统</b>" + "<br/>" + "<br/>";
		set_text += "组07：" + "<br/>";
		set_text += "- 开发组成员：" + "<br/>";
		set_text += "- okamiji - 陈欣源" + "<br/>";
		set_text += "- DongCanRola - 董灿" + "<br/>";
		set_text += "- XNYu - 余泳桦" + "<br/>";
		set_text += "- aegistudio - 罗浩然" + "<br/>";
		
		return set_text;
	}
}
