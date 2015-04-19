package nbaquery.presentation2.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nbaquery.presentation2.info.Match;
import nbaquery.presentation2.info.Player;
import nbaquery.presentation2.info.Team;
import nbaquery.presentation2.panel.PanelSet;

@SuppressWarnings("serial")
public class DetailedPanel extends JPanel{
	JPanel info_panel, data_panel;
	
	JButton exit_button, info_button, data_button;
	
	Player player = null;
	Team team = null;
	Match match = null;
	
	int scr_height;
	
	JLabel direction_label;
	
	boolean is_portrait;
	JButton view_action_pic;
	JLabel info_label, team_label;
	JLabel portrait_label, action_label;
	ImageIcon portrait, action;
	
	public DetailedPanel(Player get_player){		
		player = get_player;
		
		initialize_player();
	}
	public DetailedPanel(Team get_team){	
		team = get_team;
		
		initialize_team();
	}
	public DetailedPanel(Match get_match){
		match = get_match;
		
		initialize_match();
	}
	
	private void initialize_player(){
		initialize();
		set_player_info();
	}
	private void initialize_team(){
		initialize();
		set_team_info();
	}	
	private void initialize_match(){
		initialize();
		set_match_info();
	}
	private void initialize(){
		this.setBackground(new Color(0,0,0,0.0f));
		this.setLayout(null);
		this.setSize(587, 545);
		this.setLocation(127, 3);
		
		exit_button = new JButton("关闭");
		exit_button.setBounds(567, 3, 20, 20);
		this.add(exit_button);
		
	/*	exit_button.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				PanelSet.remove_detailed();
				CardProperties.set_if_view_all(false);
				ConcisePanel cp = new ConcisePlayerPanel(1, PanelSet.get_view_limit());
				cp.init();
				PanelSet.get_concise().run();
			}
		});*/
	}
	
	private void set_player_info(){
		info_button = new JButton("基本信息");
		info_button.setBounds(175, 3, 195, 30);
		this.add(info_button);
		
		data_button = new JButton("比赛数据");
		data_button.setBounds(370, 3, 195, 30);
		this.add(data_button);
		
		direction_label = new JLabel();
		direction_label.setText("球员 < " + player.get_name());
		direction_label.setFont(new Font("微软雅黑",Font.PLAIN, 13));
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

		//info panel
		
		
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
		
		view_action_pic = new JButton("查看动作");
		view_action_pic.setBounds(450, 130, 60, 30);

		is_portrait = true;
		portrait = new ImageIcon(player.get_portrait_path());
		action = new ImageIcon(player.get_action_path());
		portrait.setImage(portrait.getImage().getScaledInstance(130, 200, Image.SCALE_DEFAULT));
		action.setImage(action.getImage().getScaledInstance(130, 200, Image.SCALE_DEFAULT));
		
		portrait_label = new JLabel(portrait);
		portrait_label.setSize(130, 200);
		portrait_label.setLocation(0, 50);
		info_panel.add(portrait_label);
		action_label = new JLabel(action);
		action_label.setSize(130, 200);
		action_label.setLocation(0, 50);
		info_panel.add(action_label);
		action_label.setVisible(false);
		
		portrait_label.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(is_portrait){
					action_label.setVisible(true);
					portrait_label.setVisible(false);
					is_portrait = false;
				}
				else{
					action_label.setVisible(false);
					portrait_label.setVisible(true);
					is_portrait = true;
				}
			}
		});
		
		team_label.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				PanelSet.set_detailed_visible(false);
			//	InfoRetriever retriever = new InfoRetriever();
			//	Team set_team = retriever.get_team_by_name(team_label.getText());
			//	PanelSet.create_detailed_panel(set_team);
			}
		});
		
		//data panel
		
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
	private void set_match_info(){
		
	}
}

