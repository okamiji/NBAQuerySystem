package nbaquery.presentation2.addedcard;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;

import org.apache.batik.swing.svg.JSVGComponent;

import nbaquery.presentation2.info.Team;
import nbaquery.presentation2.panel.PanelSet;


@SuppressWarnings("serial")
class TeamFlatCard extends FlatCard {

	Team team;
	
	JLabel label_name, label_info;
	String team_text;
	
	@SuppressWarnings("deprecation")
	public void create_card(Object obj) {
		super.create_card(obj);
		
		team = (Team) obj;
		
		label_name = new JLabel();
		label_name.setText(" " + (team.get_index() + 1) + ". " + team.get_name());
		label_name.setBackground(new Color(90, 225, 149));
		label_name.setOpaque(true);
		label_name.setForeground(Color.WHITE);
		label_name.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));	
		label_name.setBounds(50, 1, 100, 30);
		shadow_label.add(label_name);
		
		JSVGComponent svgComponent = new JSVGComponent(null, false, false);
		String path = team.get_portrait_path();
		if(path != null){
			File f = new File(path);
			try {
	            svgComponent.loadSVGDocument(f.toURL().toString());
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
			svgComponent.setBounds(8, 2, 35, 35);
			shadow_label.add(svgComponent);
			shadow_label.repaint();
		}
		
		label_info = new JLabel();
		set_team_text(team);
		label_info.setText(team_text);
		label_info.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));	
		shadow_label.add(label_info);
		label_info.setBounds(180, -10, 340, 60);
		
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				System.out.println("A new detailed card has been created.");
				PanelSet.set_concise_invisible();
				PanelSet.create_detailed_panel(team);
			}
		});
	}

	private void set_team_text(Team team){
	//	String[] team_info = team.get_team_info();
	//	int item_index = CardProperties.get_team_index_index();
	//	String item_name = CardProperties.get_team_item_name();
		team_text = "<html>";
	//	team_text += item_name + "£º" + team_info[item_index + 2];
		team_text += "</html>";
	}
	
}
