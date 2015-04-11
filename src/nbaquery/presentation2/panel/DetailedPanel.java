package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nbaquery.presentation2.card.CardProperties;
import nbaquery.presentation2.card.InfoRetriever;
import nbaquery.presentation2.info.Player;
import nbaquery.presentation2.info.Team;
import nbaquery.presentation2.panel.ConcisePanel;
import nbaquery.presentation2.panel.PanelSet;

@SuppressWarnings("serial")
public class DetailedPanel extends JPanel{
	JPanel info_panel, data_panel;
	
	JButton exit_button, info_button, data_button;	
	
	JLabel direction_label, direction_label_copy, team_label, info_label;
	
	Player player = null;
	Team team = null;
	
	int scr_height;
	
	public DetailedPanel(Player get_player){		
		player = get_player;
		
		initialize_player();
	}
	public DetailedPanel(Team get_team){	
		team = get_team;
		
		initialize_team();
	}
	
	private void initialize_player(){
		initialize();
		set_player_info();
	}
	private void initialize_team(){
		initialize();
		set_team_info();
	}	
	private void initialize(){
		this.setBackground(new Color(0,0,0,0.0f));
		this.setLayout(null);
		this.setSize(587, 545);
		this.setLocation(127, 3);
		
		exit_button = new JButton("关闭");
		exit_button.setBounds(567, 3, 20, 20);
		this.add(exit_button);
		
		exit_button.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
			//	PanelSet.set_detailed_panel_visible(false, PanelSet.get_detailed_panel_size() - 1);
			//	PanelSet.delete_detailed_panel();
				PanelSet.remove_detailed();
				CardProperties.set_if_view_all(false);
				ConcisePlayerPanel cp = new ConcisePlayerPanel(1, PanelSet.get_view_limit());
				cp.init_player_panel();
				PanelSet.get_concise().run();
			}
		});
	}
	
	private void set_player_info(){
		info_button = new JButton("基本信息");
		info_button.setBounds(175, 3, 195, 30);
		this.add(info_button);
		
		data_button = new JButton("比赛数据");
		data_button.setBounds(370, 3, 195, 30);
		this.add(data_button);
		
		direction_label = new JLabel();
		direction_label.setText("NBA球员 < " + player.get_name());
		direction_label.setFont(new Font("微软雅黑",Font.BOLD, 13));
		direction_label.setForeground(new Color(30, 164, 88));
		direction_label.setBounds(10, 0, 160, 30);
		this.add(direction_label);
		
		info_panel = new JPanel();
		info_panel.setBackground(new Color(0, 0, 0, 0.0f));
		info_panel.setLayout(null);
		info_panel.setSize(575, 530);
		info_panel.setLocation(-5, 5);
		this.add(info_panel);
		info_panel.setVisible(false);
		
		data_panel = new JPanel();
		data_panel.setBackground(new Color(245, 245, 245));
		data_panel.setLayout(null);
		data_panel.setSize(575, 530);
		data_panel.setLocation(-5, 5);
		this.add(data_panel);
		data_panel.setVisible(false);

		//info 		
		
		
		team_label = new JLabel();
		//TODO
		team_label.setText("马神战队");
		team_label.setBounds(450, 50, 100, 100);
		info_panel.add(team_label);
		
		info_label = new JLabel();
		//TODO
		info_label.setText("info");
		info_label.setBounds(450, 100, 100, 100);
		info_panel.add(info_label);
		
		try{
			ImageIcon action = new ImageIcon(player.get_portrait_path());
			action.setImage(action.getImage().getScaledInstance(350, 550, Image.SCALE_DEFAULT));
			JLabel action_label = new JLabel(action);
			action_label.setSize(350, 550);
			action_label.setLocation(0, 0);
			info_panel.add(action_label);
			
			ImageIcon portrait = new ImageIcon(player.get_portrait_path());//TODO
			portrait.setImage(portrait.getImage().getScaledInstance(130, 100, Image.SCALE_DEFAULT));
			JLabel portrait_label = new JLabel(portrait);
			portrait_label.setSize(130, 100);
			portrait_label.setLocation(300, 50);
			info_panel.add(portrait_label);
		} catch (Exception e) {
			System.out.println("Image of " + player.get_name() + " can not be loaded.");
		}
		
		team_label.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				PanelSet.set_detailed_visible(false);
				InfoRetriever retriever = new InfoRetriever();
				Team set_team = retriever.get_team_by_name(team_label.getText());
				PanelSet.create_detailed_panel(set_team);
			}
		});
		
		//data
		
		//button
		info_button.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				info_panel.setVisible(true);
				data_panel.setVisible(false);
			}
		});
		data_button.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				info_panel.setVisible(false);
				data_panel.setVisible(true);
			}
		});
	}
	private void set_team_info(){
		//TODO
	}
	
}

