package nbaquery.presentation2.addedcard;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
class RectCard extends Card {

	JLabel shadow_label;
	public void create_card(Object obj) {
		this.setSize(260, 100);
		this.setLayout(null);
		this.setVisible(true);
		this.setBackground(new Color(0,0,0,0.0f));
		shadow_label = new JLabel(new ImageIcon("Img2/card_shadow.png"));
		this.add(shadow_label);
		shadow_label.setBounds(0, 0, 260, 100);
		
	}

}
