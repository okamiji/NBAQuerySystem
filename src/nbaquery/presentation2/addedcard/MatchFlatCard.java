package nbaquery.presentation2.addedcard;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import nbaquery.presentation2.info.Match;

@SuppressWarnings("serial")
class MatchFlatCard extends FlatCard {

	Match match;
	
	JLabel match_info, label_pic1, label_pic2, label_info;
	ImageIcon pic;
	String match_text;
	
	public void create_card(Object obj) {
		super.create_card(obj);
		
		match = (Match) obj;
		
	}

}

