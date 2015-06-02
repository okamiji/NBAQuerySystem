package nbaquery.presentation2.addedcard;

import java.awt.Color;

import javax.swing.JLabel;

import nbaquery.presentation.resource.ImageIconResource;

@SuppressWarnings("serial")
class FlatCard extends Card{

	JLabel shadow_label;
	
	public void create_card(Object obj) {		
		this.setSize(530, 40);
		this.setLayout(null);
		this.setVisible(true);this.setBackground(new Color(0, 0, 0, 0.0f));
		shadow_label = new JLabel(ImageIconResource.getImageIcon("Img2/card_shadow2.png"));
		this.add(shadow_label);
		shadow_label.setBounds(0, 0, 530, 40);
	}

}
