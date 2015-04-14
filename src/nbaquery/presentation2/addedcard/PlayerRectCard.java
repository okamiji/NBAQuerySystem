package nbaquery.presentation2.addedcard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import nbaquery.presentation2.info.Player;
import nbaquery.presentation2.panel.ConcisePara;
import nbaquery.presentation2.panel.PanelSet;

@SuppressWarnings("serial")
class PlayerRectCard extends RectCard {

	Player player;
	
	JLabel label_name, label_pic, label_info;
	ImageIcon pic;
	String player_text;
	
	public void create_card(Object obj) {
		super.create_card(obj);
		
		player = (Player) obj;
		
		label_name = new JLabel();
		label_name.setText(" " + (player.get_index() + 1) + ". " + player.get_name());
		label_name.setBackground(new Color(90, 225, 149));
		label_name.setOpaque(true);
		label_name.setForeground(Color.white);
		label_name.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));	
		label_name.setBounds(117, 3, 140, 30);
		shadow_label.add(label_name);
		
		try{
			pic = new ImageIcon(player.get_portrait_path());
			pic.setImage(pic.getImage().getScaledInstance(100, 85, Image.SCALE_DEFAULT));
			label_pic = new JLabel(pic);
			label_pic.setBounds(10, 8, 100, 85);
			shadow_label.add(label_pic);
		} catch (Exception e) {
			System.out.println("Image of " + player.get_name() + " can not be loaded.");
		}
		
		label_info = new JLabel();
		set_player_text(player);
		label_info.setText(player_text);
		label_info.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));	
		shadow_label.add(label_info);
		label_info.setBounds(115, 32, 150, 60);
		
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				PanelSet.set_concise_invisible();
				PanelSet.create_detailed_panel(player);
			}
		});
		
	}

	private void set_player_text(Player player){
		String[] player_info = player.get_player_info();
		player_text = "<html>";
		player_text += "Çò¶Ó£º" + player.get_team();
		player_text += "<br/>";
		if(ConcisePara.player_position_index != 0){
			player_text += "Î»ÖÃ£º" + player_info[30] + "  ";
		}
		if(ConcisePara.player_league_index != 0){
			player_text += "ÁªÃË£º" + player_info[31];	
			player_text += "<br/>";
		}
		if(ConcisePara.player_index_index != 0){
			if(player_text.substring(player_text.length() - 2, player_text.length()).equals("  ")){
				player_text += "<br/>";
			}
			player_text += ConcisePara.player_item_name + "£º" + player_info[ConcisePara.player_index];
		}
		player_text += "</html>";
	}

}
