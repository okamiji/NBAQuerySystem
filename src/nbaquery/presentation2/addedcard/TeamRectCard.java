package nbaquery.presentation2.addedcard;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;

import org.apache.batik.swing.svg.JSVGComponent;

import nbaquery.presentation.resource.JSVGComponentResource;
import nbaquery.presentation2.info.Team;
import nbaquery.presentation2.panel.ConcisePara;
import nbaquery.presentation2.panel.PanelSet;

@SuppressWarnings("serial")
class TeamRectCard extends RectCard {

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
		label_name.setForeground(Color.white);
		label_name.setFont(new Font("Î¢ÈíÑÅºÚ",Font.BOLD, 12));	
		label_name.setBounds(112, 6, 139, 30);
		shadow_label.add(label_name);

		String path = team.get_portrait_path();
		if(path != null){
			Component svgComponent = JSVGComponentResource.createJSVGComponent(path);
			svgComponent.setBounds(10, 6, 100, 85);
			shadow_label.add(svgComponent);
		}
		
		label_info = new JLabel();
		set_team_text(team);
		label_info.setForeground(new Color(191, 211, 200));
		label_info.setText(team_text);
		label_info.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));	
		shadow_label.add(label_info);
		label_info.setBounds(115, 32, 150, 60);
		
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				PanelSet.set_concise_invisible();
				PanelSet.create_detailed_panel(team);
			}
		});
	}
	
	private void set_team_text(Team team){
		if(!ConcisePara.is_hot){
			String[] team_info = team.get_team_info();
			int item_index = ConcisePara.team_index;
			String item_name = ConcisePara.team_item_name;
			team_text = "<html>";
			team_text += item_name + "£º" + team_info[item_index];
			team_text += "</html>";
		}
		else{
			team_text = "";
		}
	}
}
