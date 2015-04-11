package nbaquery.presentation2.card;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.batik.swing.svg.JSVGComponent;

import nbaquery.presentation2.info.Player;
import nbaquery.presentation2.info.Team;
import nbaquery.presentation2.panel.ConcisePanel;
import nbaquery.presentation2.panel.ConcisePlayerPanel;
import nbaquery.presentation2.panel.ConciseTeamPanel;
import nbaquery.presentation2.panel.PanelSet;

@SuppressWarnings("serial")
public class Card extends JPanel {
	String player_text, team_text, view_more_text;
	
	JLabel label_name, label_pic, label_info;
	JLabel shadow_label;
	ImageIcon pic;
	
	int per_row;
	
	public Card(){
		per_row = CardProperties.get_cards_per_row();
		
		switch(per_row){
		case 1:
			this.setSize(530, 40);
			break;
		case 2:
			this.setSize(260, 100);
			break;
		}	
		this.setLayout(null);
		this.setVisible(true);
		
	}
	
	public void set_player_info(final Player player, final int index_){
		
		if(per_row == 2){
			this.setBackground(new Color(0,0,0,0.0f));
			shadow_label = new JLabel(new ImageIcon("Img2/card_shadow.png"));
			this.add(shadow_label);
			shadow_label.setBounds(0, 0, 260, 100);
			
			label_name = new JLabel();
			label_name.setText(" " + index_ + ". " + player.get_name());
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
			label_info.setBounds(120, 30, 140, 60);
			
			this.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e) {
					System.out.println("A new detailed card has been created.");
					PanelSet.set_concise_invisible();
					PanelSet.create_detailed_panel(player);
				}
			});
		}
		else if(per_row == 1){
			this.setBackground(new Color(0, 0, 0, 0.0f));
			shadow_label = new JLabel(new ImageIcon("Img2/card_shadow2.png"));
			this.add(shadow_label);
			shadow_label.setBounds(0, 0, 530, 40);
			
			label_name = new JLabel();
			label_name.setText(" " + index_ + ". " + player.get_name());
			label_name.setBackground(new Color(90, 225, 149));
			label_name.setOpaque(true);
			label_name.setForeground(Color.WHITE);
			label_name.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));	
			label_name.setBounds(50, 1, 120, 30);
			shadow_label.add(label_name);
			
			try{
				pic = new ImageIcon(player.get_portrait_path());
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
			label_info.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));	
			label_info.setBounds(180, -10, 340, 60);
			shadow_label.add(label_info);
			
			this.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e) {
					System.out.println("A new detailed card has been created.");
					PanelSet.set_concise_invisible();
					PanelSet.create_detailed_panel(player);
				}
			});
			
		}
		
	}
	@SuppressWarnings("deprecation")
	public void set_team_info(final Team team, final int index_){
		
		if(per_row == 2){
			this.setBackground(new Color(0,0,0,0.0f));
			shadow_label = new JLabel(new ImageIcon("Img2/card_shadow.png"));
			this.add(shadow_label);
			shadow_label.setBounds(0, 0, 260, 100);
			
			label_name = new JLabel();
			label_name.setText(" " + index_ + ". " + team.get_name());
			label_name.setBackground(new Color(90, 225, 149));
			label_name.setOpaque(true);
			label_name.setForeground(Color.white);
			label_name.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));	
			label_name.setBounds(117, 3, 140, 30);
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
				svgComponent.setBounds(10, 8, 100, 85);
				shadow_label.add(svgComponent);
				shadow_label.repaint();
			}
			
			label_info = new JLabel();
			set_team_text(team);
			label_info.setText(team_text);
			label_info.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));	
			shadow_label.add(label_info);
			label_info.setBounds(120, 30, 130, 60);
			
			this.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e) {
					System.out.println("A new detailed card has been created.");
					PanelSet.set_concise_invisible();
					PanelSet.create_detailed_panel(team);
				}
			});
		}
		else if(per_row == 1){
			this.setBackground(new Color(0, 0, 0, 0.0f));
			shadow_label = new JLabel(new ImageIcon("Img2/card_shadow2.png"));
			this.add(shadow_label);
			shadow_label.setBounds(0, 0, 530, 40);
			
			label_name = new JLabel();
			label_name.setText(" " + index_ + ". " + team.get_name());
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
		
	}
	public void set_view_more(final int player_or_team){
		if(per_row == 2){
			this.setBackground(new Color(0, 0, 0, 0.0f));
			shadow_label = new JLabel(new ImageIcon("Img2/card_shadow.png"));
			this.add(shadow_label);
			shadow_label.setBounds(0, 0, 260, 100);
			
			label_name = new JLabel();
			label_name.setText("²é¿´¸ü¶à");
			label_name.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));	
			label_name.setBounds(110, 10, 140, 50);
			shadow_label.add(label_name);
			
			this.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e) {
					PanelSet.set_concise_invisible();
					
					if(player_or_team == 1){
						CardProperties.set_if_view_all(true);
						ConcisePlayerPanel cp = new ConcisePlayerPanel(1, PanelSet.get_view_limit());
						cp.init_player_panel();
						PanelSet.get_concise().run();
					}
					else if(player_or_team == 2){
						CardProperties.set_if_view_all(true);
						ConciseTeamPanel cp = new ConciseTeamPanel(2, PanelSet.get_view_limit());
						cp.init_team_panel();
						PanelSet.get_concise().run();
					}
				}
			});
		}
		else if(per_row == 1){
			this.setBackground(new Color(0,0,0,0.0f));
			shadow_label = new JLabel(new ImageIcon("Img2/card_shadow2.png"));
			this.add(shadow_label);
			shadow_label.setBounds(0, 0, 530, 40);
			
			label_name = new JLabel();
			label_name.setText("²é¿´¸ü¶à");
			label_name.setFont(new Font("Î¢ÈíÑÅºÚ",Font.PLAIN, 12));
			label_name.setBounds(225, -5, 140, 50);
			shadow_label.add(label_name);
			
			this.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent e) {
					PanelSet.set_concise_invisible();
					CardProperties.set_if_view_all(true);
					
					if(player_or_team == 1){
						@SuppressWarnings("unused")
						ConcisePanel cp = new ConcisePanel(1, PanelSet.get_view_limit());
					}
					else if(player_or_team == 2){
						@SuppressWarnings("unused")
						ConcisePanel cp = new ConcisePanel(2, PanelSet.get_view_limit());
					}
					PanelSet.get_concise().run();
				}
			});
		}
		
	}

	private void set_player_text(Player player){
		String[] player_info = player.get_player_info();
		int[] player_index = CardProperties.get_player_combobox_index();
		player_text = "<html>";
		player_text += "Çò¶Ó£º" + player.get_team();
		player_text_gap();
		if(player_index[2] != 0){
			player_text += "Î»ÖÃ£º" + player_info[30];
			player_text_gap();
		}
		if(player_index[3] != 0){
			player_text += "ÁªÃË£º" + player_info[31];	
			player_text_gap();
		}
		if(player_index[1] != 0){
			int index = player_index[1] + 2;
			player_text += CardProperties.get_player_item_name() + "£º" + player_info[index];
			player_text_gap();
		}
		player_text += "</html>";
	}
	private void player_text_gap(){
		if(per_row == 2){
			player_enter();
		}
		else if(per_row == 1){
			player_space();
		}
	}
	private void player_enter(){
		player_text += "<br/>";
	}
	private void player_space(){
		player_text += "   ";
	}
	
	private void set_team_text(Team team){
		String[] team_info = team.get_team_info();
		int item_index = CardProperties.get_team_index_index();
		String item_name = CardProperties.get_team_item_name();
		team_text = "<html>";
		team_text += item_name + "£º" + team_info[item_index + 2];
		team_text += "</html>";
	}
}
