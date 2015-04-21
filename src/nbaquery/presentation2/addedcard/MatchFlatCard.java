package nbaquery.presentation2.addedcard;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;

import org.apache.batik.swing.svg.JSVGComponent;

import nbaquery.presentation2.info.Match;
import nbaquery.presentation2.panel.PanelSet;

@SuppressWarnings("serial")
class MatchFlatCard extends FlatCard {

	Match match;
	
	JLabel label_name, label_pic1, label_pic2, label_info;
	String match_text;
	
	@SuppressWarnings("deprecation")
	public void create_card(Object obj) {
		super.create_card(obj);
		
		match = (Match) obj;

		label_name = new JLabel();
		label_name.setText("");
		label_name.setBackground(new Color(90, 225, 149));
		label_name.setOpaque(true);
		label_name.setForeground(Color.WHITE);
		label_name.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));	
		label_name.setBounds(50, 1, 120, 30);
		shadow_label.add(label_name);
		/*
		JSVGComponent svgComponent1 = new JSVGComponent(null, false, false);
		String path1 = match.get_portrait_path();
		if(path1 != null){
			File f = new File(path1);
			try {
	            svgComponent1.loadSVGDocument(f.toURL().toString());
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
			//the location is to be changed
			svgComponent1.setBounds(8, 2, 35, 35);
			shadow_label.add(svgComponent1);
			shadow_label.repaint();
		}*/
		/*
		JSVGComponent svgComponent2 = new JSVGComponent(null, false, false);
		String path2 = match.get_portrait_path();
		if(path2 != null){
			File f = new File(path2);
			try {
	            svgComponent2.loadSVGDocument(f.toURL().toString());
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
			//the location is to be changed
			svgComponent2.setBounds(8, 2, 35, 35);
			shadow_label.add(svgComponent2);
			shadow_label.repaint();
		}
		*/
		label_info = new JLabel();
		set_match_text(match);
		label_info.setText(match_text);
		label_info.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));	
		label_info.setBounds(180, -10, 340, 60);
		shadow_label.add(label_info);
		
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				PanelSet.set_concise_invisible();
				PanelSet.create_detailed_panel(match);
			}
		});
		
		
	}

	
	private void set_match_text(Match match){
		
	}
}

