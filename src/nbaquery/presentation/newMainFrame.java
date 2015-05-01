package nbaquery.presentation;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import nbaquery.presentation2.panel.ConcisePara;
import nbaquery.presentation2.panel.PanelSet;

public class newMainFrame {
	
	JFrame frame;
	JPanel button_panel, panel_showing;
	ImageIcon background_img, button_chosen, button_not_chosen;
	JLabel background_label;
	Button button_match, button_data, button_team, button_player, button_hotspot, exit, mini, show;
	
	PlayerService ps;
	TeamService ts;
	MatchService ms;
	
	int xOld,yOld;
	
	public newMainFrame(final PlayerService playerService, final TeamService teamService, final MatchService matchService){
		ps = playerService;
		ts = teamService;
		ms = matchService;
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
		
		background_img = new ImageIcon("img3/main_frame.png");
		int background_width = background_img.getIconWidth();
		int background_height = background_img.getIconHeight();

		Color bac = new Color(0,0,0,0.0f); 
		
		frame = new JFrame();
		frame.setUndecorated(true);
		frame.setBackground(bac);
		frame.setBounds(80, 0, background_width, background_height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("NBAQuerySyetem");
		frame.setVisible(true);		
		frame.setLayout(null);
		frame.setResizable(false);
		
		((JPanel)frame.getContentPane()).setOpaque(false);
		background_label=new JLabel(background_img);
		frame.getLayeredPane().add(background_label, new Integer(Integer.MIN_VALUE));
		background_label.setBounds(0, 0, background_width, background_height); 
		
		button_panel = new JPanel();
		button_panel.setBounds(0, 0, 1200, 120);
		button_panel.setLayout(null);
		button_panel.setVisible(true);
		button_panel.setOpaque(false);
		frame.add(button_panel);
		
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
	
	private void initialize_buttons(){
		button_chosen = new ImageIcon("img3/button_chosen.png");
		button_not_chosen = new ImageIcon("img3/button_not_chosen.png");
		
		button_match = new Button(button_not_chosen, button_chosen, button_panel);
		button_data = new Button(button_not_chosen, button_chosen, button_panel);
		button_team = new Button(button_not_chosen, button_chosen, button_panel);
		button_player = new Button(button_not_chosen, button_chosen, button_panel);
		button_hotspot = new Button(button_not_chosen, button_chosen, button_panel);
		button_match.setBounds(63, 80, 108, 26);
		button_data.setBounds(213, 80, 108, 26);
		button_team.setBounds(348, 80, 108, 26);
		button_player.setBounds(470, 80, 108, 26);
		button_hotspot.setBounds(604, 80, 108, 26);
		
		exit = new Button(null, null, button_panel);
		mini = new Button(null, null, button_panel);
		show = new Button(null, null, button_panel);
		exit.setBounds(118, 44, 24, 25);
		mini.setBounds(98, 44, 24, 25);
		show.setBounds(14, 50, 24, 25);

		button_match.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_button();
				button_match.setIcon(button_chosen);
				
				
			}
		});
		
		button_data.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_button();
				button_data.setIcon(button_chosen);
				
				JPanel data = new DataPanel(ps, ts, ms);
				change_panel(data);
			}
		});
		
		button_team.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {	
				init_button();
				button_team.setIcon(button_chosen);
				
				
				
				
			}
		});
		
		button_player.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_button();
				button_player.setIcon(button_chosen);
				
			}
		});

		button_hotspot.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				init_button();
				button_hotspot.setIcon(button_chosen);
				
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
	
	
	private void change_panel(JPanel get_panel){
		if(panel_showing != null){
			frame.remove(panel_showing);
		}
		panel_showing = get_panel;
		frame.add(panel_showing);
	}
	
	private void init_button(){
		button_match.setIcon(button_not_chosen);
		button_data.setIcon(button_not_chosen);
		button_team.setIcon(button_not_chosen);
		button_player.setIcon(button_not_chosen);
		button_hotspot.setIcon(button_not_chosen);
	}
	
}
