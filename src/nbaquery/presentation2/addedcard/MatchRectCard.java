package nbaquery.presentation2.addedcard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.apache.batik.swing.svg.JSVGComponent;

import nbaquery.presentation2.info.Match;
import nbaquery.presentation2.panel.PanelSet;

@SuppressWarnings("serial")
class MatchRectCard extends RectCard {

	Match match;
	
	JLabel label_name, label_pic, label_info;
	ImageIcon pic;
	String match_text;
	
	@SuppressWarnings("deprecation")
	public void create_card(Object obj) {
		super.create_card(obj);
		
		match = (Match) obj;
		
		label_name = new JLabel();
		label_name.setText(match.get_season() + " " + match.get_date());
		label_name.setBackground(new Color(90, 225, 149));
		label_name.setOpaque(true);
		label_name.setForeground(Color.white);
		label_name.setFont(new Font("Î¢ÈíÑÅºÚ",Font.BOLD, 12));	
		label_name.setBounds(92, 6, 74, 30);
		shadow_label.add(label_name);

		String[] path = match.get_logo();
		
		JSVGComponent svgComponent1 = new JSVGComponent(null, false, false);
		if(path[0] != null){
			File f = new File(path[0]);
			try {
	            svgComponent1.loadSVGDocument(f.toURL().toString());
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
			svgComponent1.setBounds(10, 8, 50, 40);
			shadow_label.add(svgComponent1);
			shadow_label.repaint();
		}
		JSVGComponent svgComponent2 = new JSVGComponent(null, false, false);
		if(path[0] != null){
			File f = new File(path[0]);
			try {
	            svgComponent2.loadSVGDocument(f.toURL().toString());
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
			svgComponent2.setBounds(70, 8, 50, 40);
			shadow_label.add(svgComponent2);
			shadow_label.repaint();
		}
		label_info = new JLabel();
		set_player_text(match);
		label_info.setForeground(new Color(191, 211, 200));
		label_info.setText(match_text);
		label_info.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));	
		shadow_label.add(label_info);
		label_info.setBounds(92, 32, 215, 60);//112
		
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				PanelSet.set_concise_invisible();
				PanelSet.create_detailed_panel(match);
			}
		});
		
	}

	private void set_player_text(Match match){
		match_text = "<html> ";
		match_text += match.get_team()[0] + " : " + match.get_team()[1];
		match_text += "<br/> ";
		match_text += match.get_score()[0] + " : " + match.get_score()[1];
		match_text += "</html>";
	}

}

