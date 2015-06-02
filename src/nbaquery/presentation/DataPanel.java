package nbaquery.presentation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import nbaquery.logic.match.MatchService;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;

@SuppressWarnings("serial")
public class DataPanel extends JPanel implements TableModelListener {
	PlayerService ps;
	TeamService ts;
	MatchService ms;
	
	DataTableModel table_model;
	JTable table;
	JScrollPane scr;
	int head;
	
	Button player, defen, toulan, sanfen, faqiu, lanban, zhugong, gaimao, qiangduan, team;
	JTable table_showing;
	ImageIcon blue_chosen, black_chosen, not_chosen;
	
	
	public DataPanel(final PlayerService playerService, final TeamService teamService, final MatchService matchService){
		this.setSize(1100, 580);
		this.setLocation(50, 120);
		this.setLayout(null);
		this.setVisible(true);
		this.setBackground(new Color(0, 0, 0, 0));
		
		ps = playerService;
		ts = teamService;
		
		init_button();
		init_table();
	}
	private void init_button(){
		blue_chosen = new ImageIcon("img3/blue_chosen.png");
		black_chosen = new ImageIcon("img3/black_chosen.png");
		not_chosen = new ImageIcon("img3/data_not_chosen.png");
		
		player = new Button(not_chosen, blue_chosen, this);
		team = new Button(not_chosen, blue_chosen, this);
		
		defen = new Button(not_chosen, black_chosen, this);
		toulan = new Button(not_chosen, black_chosen, this);
		sanfen = new Button(not_chosen, black_chosen, this);
		faqiu = new Button(not_chosen, black_chosen, this);
		lanban = new Button(not_chosen, black_chosen, this);
		zhugong = new Button(not_chosen, black_chosen, this);
		gaimao = new Button(not_chosen, black_chosen, this);
		qiangduan = new Button(not_chosen, black_chosen, this);
		
		player.setBounds(38, 5, 59, 15);
		team.setBounds(104, 5, 59, 15);
		
		defen.setBounds(39, 25, 59, 15);
		toulan.setBounds(106, 25, 59, 15);
		sanfen.setBounds(173, 25, 59, 15);
		faqiu.setBounds(241, 25, 59, 15);
		lanban.setBounds(308, 25, 59, 15);
		zhugong.setBounds(375, 25, 59, 15);
		gaimao.setBounds(442, 25, 59, 15);
		qiangduan.setBounds(509, 25, 59, 15);
		
		player.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_button_image();
				player.setIcon(blue_chosen);
				
				
			}
		});

		team.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_button_image();
				team.setIcon(blue_chosen);
				
				
			}
		});
		defen.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_player_image();
				defen.setIcon(black_chosen);
				
				
			}
		});
		toulan.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_player_image();
				toulan.setIcon(black_chosen);
				
				
			}
		});
		sanfen.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_player_image();
				sanfen.setIcon(black_chosen);
				
				
			}
		});
		faqiu.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_player_image();
				faqiu.setIcon(black_chosen);
				
				
			}
		});
		lanban.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_player_image();
				lanban.setIcon(black_chosen);
				
				
			}
		});
		zhugong.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_player_image();
				zhugong.setIcon(black_chosen);
				
				
			}
		});
		gaimao.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_player_image();
				gaimao.setIcon(black_chosen);
				
				
			}
		});
		qiangduan.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_player_image();
				qiangduan.setIcon(black_chosen);
				
				
			}
		});
	}
	private void init_button_image(){
		player.setIcon(not_chosen);
		team.setIcon(not_chosen);
	}
	private void init_player_image(){
		defen.setIcon(not_chosen);
		toulan.setIcon(not_chosen);
		sanfen.setIcon(not_chosen);
		faqiu.setIcon(not_chosen);
		lanban.setIcon(not_chosen);
		zhugong.setIcon(not_chosen);
		gaimao.setIcon(not_chosen);
		qiangduan.setIcon(not_chosen);
	}
	
	private void init_table(){
		
	}
	
	public void paintComponent(Graphics g) {
		ImageIcon icon = new ImageIcon("img3/data.png");
		g.drawImage(icon.getImage(), 0, 0, getSize().width,getSize().height, this);
	}
	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
