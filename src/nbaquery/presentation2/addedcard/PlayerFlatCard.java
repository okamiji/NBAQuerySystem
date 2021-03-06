package nbaquery.presentation2.addedcard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import nbaquery.presentation.resource.ImageIconResource;
import nbaquery.presentation2.info.Player;
import nbaquery.presentation2.panel.ConcisePara;
import nbaquery.presentation2.panel.PanelSet;

@SuppressWarnings("serial")
class PlayerFlatCard extends FlatCard {
	
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
		label_name.setForeground(Color.WHITE);
		label_name.setFont(new Font("΢���ź�",Font.PLAIN, 12));	
		label_name.setBounds(50, 1, 120, 30);
		shadow_label.add(label_name);
		
		try{
			pic = ImageIconResource.getImageIcon(player.get_portrait_path());
			pic.setImage(pic.getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT));
			label_pic = new JLabel(pic);
			label_pic.setBounds(8, 2, 35, 35);
			shadow_label.add(label_pic);
		} catch (Exception e) {
			System.out.println("Image of " + player.get_name() + " can not be loaded.");
		}
		
		label_info = new JLabel();
		set_player_text(player);
		label_info.setText(player_text);
		label_info.setFont(new Font("΢���ź�",Font.PLAIN, 12));	
		label_info.setBounds(180, -10, 340, 60);
		shadow_label.add(label_info);
		
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
		player_text += "��ӣ�" + player.get_team();
		player_text += "λ�ã�" + player_info[30] + "  " + "���ˣ�" + player_info[31] + "   ";
		player_text += "   ";
		if(ConcisePara.player_index_index != 0){
			player_text += ConcisePara.player_item_name + "��" + player_info[ConcisePara.player_index];
		}
		player_text += "</html>";
	}

}
