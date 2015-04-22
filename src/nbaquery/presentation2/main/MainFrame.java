package nbaquery.presentation2.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;


import nbaquery.logic.match.MatchService;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;
import nbaquery.presentation2.util.CardType;
import nbaquery.presentation2.card.CardLocation;
import nbaquery.presentation2.util.Button;
import nbaquery.presentation2.panel.ConcisePanelFactory;
import nbaquery.presentation2.panel.ConcisePara;
import nbaquery.presentation2.panel.PanelSet;

public class MainFrame {
	
	static JFrame frame;
	static JPanel button_left_panel, button_right_panel;
	ImageIcon background_img;
	JLabel background_label;
	
	static CardLocation location = null;
	
	static Button button1, button2, button3, button4, button5;
	static Button exit, mini, show;


	static MouseListener listener1 = null;
	static MouseListener listener2 = null;
	static MouseListener listener3 = null;
	static MouseListener listener4 = null;
	static MouseListener listener5 = null;
	
	int xOld,yOld;
	
	public MainFrame(final PlayerService playerService, final TeamService teamService, final MatchService matchService){
		PanelSet.set_player_service(playerService);
		PanelSet.set_team_service(teamService);
		PanelSet.set_match_service(matchService);
	}
	
	public void run(){
		initialize();
		refresh.start();
	}
	
	Thread refresh = new Thread()
	{
		public void run()
		{
			while(frame.isVisible()) try
			{
				frame.repaint();
				Thread.sleep(16);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	};
	
	public void initialize(){
		
		background_img = new ImageIcon("Img2/main_frame.png");
		int background_width = background_img.getIconWidth();
		int background_height = background_img.getIconHeight();

		Color bac = new Color(0,0,0,0.0f); 
		
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setBackground(bac);
		frame.setBounds(270, 80, background_width, background_height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("NBAQuerySyetem");
		frame.setVisible(true);		
		frame.setLayout(null);
		frame.setResizable(false);
//		frame.setOpacity(0.95f);
		
		((JPanel)frame.getContentPane()).setOpaque(false);
		background_label=new JLabel(background_img);
		frame.getLayeredPane().add(background_label, new Integer(Integer.MIN_VALUE));
		background_label.setBounds(0, 0, background_width, background_height); 
		
		button_left_panel = new JPanel();
		button_left_panel.setBounds(0, 0, 115, background_height);
		button_left_panel.setLayout(null);
		button_left_panel.setVisible(true);
		button_left_panel.setOpaque(false);
		frame.add(button_left_panel);

		button_right_panel = new JPanel();
		button_right_panel.setBounds(714, 0, 180, 90);
		button_right_panel.setLayout(null);
		button_right_panel.setVisible(true);
		button_right_panel.setOpaque(false);
		frame.add(button_right_panel);
		
		initialize_buttons();
		

		//-------------ø…Õœ∂Ø-------------
		frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			xOld = e.getX();
			yOld = e.getY();
			}
		});
		frame.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e) {
			int xOnScreen = e.getXOnScreen();
			int yOnScreen = e.getYOnScreen();
			int xx = xOnScreen - xOld;
			int yy = yOnScreen - yOld;
			frame.setLocation(xx, yy);
		}
		});
		
		Font font=new Font("Œ¢»Ì—≈∫⁄", Font.BOLD, 12);
		set_font(font);
		
		PanelSet.get_instance(frame);
		
	}
	
	private static void initialize_buttons(){
		button1 = new Button("Img2/blank_big.png", "Img2/button_chosen.png", button_left_panel);
		button2 = new Button("Img2/blank_big.png", "Img2/button_chosen.png", button_left_panel);
		button3 = new Button("Img2/blank_big.png", "Img2/button_chosen.png", button_left_panel);
		button4 = new Button("Img2/blank_big.png", "Img2/button_chosen.png", button_left_panel);
		button5 = new Button("Img2/blank_big.png", "Img2/button_chosen.png", button_left_panel);
		button1.setBounds(35, 105, 82, 82);
		button2.setBounds(35, 192, 82, 82);
		button3.setBounds(35, 279, 82, 82);
		button4.setBounds(35, 366, 82, 82);
		button5.setBounds(35, 453, 82, 82);
		
		exit = new Button("Img2/blank_small.png", "Img2/line.png", button_right_panel);
		mini = new Button("Img2/blank_small.png", "Img2/line.png", button_right_panel);
		show = new Button("Img2/blank_small.png", "Img2/line.png", button_right_panel);
		exit.setBounds(120, 34, 24, 25);
		mini.setBounds(100, 34, 24, 25);
		show.setBounds(10, 45, 24, 25);

		button1.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_button();
				button1.setIcon(new ImageIcon("Img2/button_chosen.png"));
				
				if(ConcisePara.type.equals(CardType.PLAYER_FLAT) || (ConcisePara.type.equals(CardType.TEAM_FLAT))){
					ConcisePara.type = CardType.MATCH_FLAT;
				}
				else if(ConcisePara.type.equals(CardType.PLAYER_RECT) || (ConcisePara.type.equals(CardType.TEAM_RECT))){
					ConcisePara.type = CardType.MATCH_RECT;
				}
				ConcisePara.view_all = false;
				ConcisePanelFactory.create_panel(ConcisePara.type, false, false);
				
				listener1 = button1.getMouseListeners()[1];
				button1.removeMouseListener(button1.getMouseListeners()[1]);
			}
		});
		
		button2.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_button();
				button2.setIcon(new ImageIcon("Img2/button_chosen.png"));
				
				if(ConcisePara.type.equals(CardType.PLAYER_FLAT) || (ConcisePara.type.equals(CardType.MATCH_FLAT))){
					ConcisePara.type = CardType.TEAM_FLAT;
				}
				else if(ConcisePara.type.equals(CardType.PLAYER_RECT) || (ConcisePara.type.equals(CardType.MATCH_RECT))){
					ConcisePara.type = CardType.TEAM_RECT;
				}
				ConcisePara.view_all = false;
				ConcisePanelFactory.create_panel(ConcisePara.type, false, false);
				
				listener2 = button2.getMouseListeners()[1];
				button2.removeMouseListener(button2.getMouseListeners()[1]);
			}
		});
		
		button3.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {	
				init_button();
				button3.setIcon(new ImageIcon("Img2/button_chosen.png"));
				
				if(ConcisePara.type.equals(CardType.TEAM_FLAT) || (ConcisePara.type.equals(CardType.MATCH_FLAT))){
					ConcisePara.type = CardType.PLAYER_FLAT;
				}
				else if(ConcisePara.type.equals(CardType.TEAM_RECT) || (ConcisePara.type.equals(CardType.MATCH_RECT))){
					ConcisePara.type = CardType.PLAYER_RECT;
				}
				ConcisePara.view_all = false;
				ConcisePanelFactory.create_panel(ConcisePara.type, false, false);
				
				
				listener3 = button3.getMouseListeners()[1];
				button3.removeMouseListener(button3.getMouseListeners()[1]);
			}
		});
		
		button4.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_button();
				button4.setIcon(new ImageIcon("Img2/button_chosen.png"));
				
				ConcisePara.type = CardType.TEAM_RECT;
				ConcisePanelFactory.create_panel(ConcisePara.type, true, true);
				ConcisePara.view_all = true;
				
				listener4 = button4.getMouseListeners()[1];
				button4.removeMouseListener(button4.getMouseListeners()[1]);
			}
		});

		button5.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_button();
				button5.setIcon(new ImageIcon("Img2/button_chosen.png"));

				listener5 = button5.getMouseListeners()[1];
				button5.removeMouseListener(button5.getMouseListeners()[1]);
			}
		});
		exit.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		
		mini.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				frame.setExtendedState(JFrame.ICONIFIED);
			}
		});
		show.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				ConcisePara.switch_type();
			}
		});
		
	}
	
	public static void set_font(Font fnt){
		FontUIResource fontRes = new FontUIResource(fnt);
		Enumeration<Object> keys;
		for(keys = UIManager.getDefaults().keys(); keys.hasMoreElements();){
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if(value instanceof FontUIResource){
				UIManager.put(key, fontRes);
			}
		}
	}	
	
	private static void add_listeners(){
		if(listener1 != null && (button1.getMouseListeners().length <= 1)){
			button1.addMouseListener(listener1);
		}
		if(listener2 != null && (button2.getMouseListeners().length <= 1)){
			button2.addMouseListener(listener2);
		}
		if(listener3 != null && (button3.getMouseListeners().length <= 1)){
			button3.addMouseListener(listener3);
		}
		if(listener4 != null && (button4.getMouseListeners().length <= 1)){
			button4.addMouseListener(listener4);
		}
		if(listener5 != null && (button5.getMouseListeners().length <= 1)){
			button5.addMouseListener(listener5);
		}
	}
	
	private static void init_button(){
		add_listeners();
		PanelSet.set_concise_invisible();
		button1.setIcon(new ImageIcon("Img2/blank_big.png"));
		button2.setIcon(new ImageIcon("Img2/blank_big.png"));
		button3.setIcon(new ImageIcon("Img2/blank_big.png"));
		button4.setIcon(new ImageIcon("Img2/blank_big.png"));
		button5.setIcon(new ImageIcon("Img2/blank_big.png"));
	}
	
}
