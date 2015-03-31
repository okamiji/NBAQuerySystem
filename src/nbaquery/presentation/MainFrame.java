package nbaquery.presentation;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

import nbaquery.logic.IBusinessLogic;
import nbaqueryBusinessLogicService.PlayerService;
import nbaqueryBusinessLogicService.TeamService;

import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.plaf.FontUIResource;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Enumeration;


public class MainFrame extends JFrame{

	JPanel mainPanel,containPanel,shadowPanel;
	JButton playerButton,teamButton;
	JTextField searchField ;
	
	//LogicService lcs;
	private JTextField textField;
	boolean isTeam=true;
	PlayerService ps;
	TeamService ts;
	int xOld,yOld;
	Color background=new Color(33,122,197);
	Color white = new Color(230,230,230);
	Font font=new Font("造字工房尚黑G0v1细体", Font.PLAIN, 18);
	/**
	 * Create the application.
	 */
	/*public MainFrame(LogicService lcs) {
		initialize();
		this.lcs = lcs;
	}*/
	
	public static void setFon(Font fnt){
		FontUIResource fontRes = new FontUIResource(fnt);
		Enumeration<Object> keys;
			for(keys = UIManager.getDefaults().keys(); keys.hasMoreElements();){
				Object key = keys.nextElement();
				Object value = UIManager.get(key);
				if(value instanceof FontUIResource)
					UIManager.put(key, fontRes);
			}
		}	
	
	public MainFrame(PlayerService pls,TeamService tms){
		this.ps=pls;
		this.ts=tms;
		this.setBounds(100, 100, 1000, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("NBAData");
		this.setUndecorated(true);
		mainPanel = new JPanel() {  
            @Override  
            protected void paintComponent(Graphics g) {  
            	ImageIcon icon = new ImageIcon("C:/Users/小南/Desktop/大作业UI/background.png");
                Image img = icon.getImage();  
                g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver());  
            }
        };
        mainPanel.setSize(1000, 700);
        mainPanel.setLocation(0,0);
        mainPanel.setLayout(null);
        mainPanel.setBackground(white);
        
        shadowPanel = new JPanel() {  
            @Override  
            protected void paintComponent(Graphics g) {  
            	ImageIcon icon = new ImageIcon("C:/Users/小南/Desktop/大作业UI/矩形3.png");
                Image img = icon.getImage();  
                g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver());  
            }
        };
        shadowPanel.setSize(1000,700);
        shadowPanel.setLocation(0,0);
        shadowPanel.setLayout(null);
        shadowPanel.setBackground(white);
        
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("\u6587\u4EF6");
		menuBar.add(mnNewMenu);
		
		JMenuItem menuItem = new JMenuItem("\u6DFB\u52A0\u6570\u636E");
		mnNewMenu.add(menuItem);
		
		JMenu menu = new JMenu("\u8BBE\u7F6E");
		menuBar.add(menu);
		
		JMenu menu_1 = new JMenu("...");
		menuBar.add(menu_1);
		this.getContentPane().setLayout(null);
		
		teamButton = new JButton("\u7403\u961F\u6570\u636E",new ImageIcon("C:/Users/小南/Desktop/大作业UI/a.png"));
		teamButton.setBounds(0, 130, 202, 150);
		teamButton.setFocusPainted(false);
		teamButton.setBorderPainted(false);
		teamButton.setContentAreaFilled(false);
		teamButton.addActionListener(new TeamListener());
		this.getContentPane().add(teamButton);
		
		playerButton= new JButton("\u7403\u5458\u6570\u636E",new ImageIcon("C:/Users/小南/Desktop/大作业UI/球员按钮发光.png"));
		playerButton.setBounds(0, 280, 202, 150);
		playerButton.addActionListener(new PlayerListener());
		playerButton.setFocusPainted(false);
		playerButton.setBorderPainted(false);
		playerButton.setContentAreaFilled(false);
		playerButton.addMouseListener(new mouseInListener());
		this.getContentPane().add(playerButton);
		
		JButton button_1 = new JButton("\u5176\u4ED6\u529F\u80FD",new ImageIcon("C:/Users/小南/Desktop/大作业UI/热点普通.png"));
		button_1.setBounds(0, 430, 202, 150);
		button_1.setFocusPainted(false);
		button_1.setBorderPainted(false);
		button_1.setContentAreaFilled(false);
		this.getContentPane().add(button_1);
		button_1.addActionListener(new exitButtonListener());
		
		
		this.add(mainPanel);
		//-------------可拖动-------------
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			xOld = e.getX();
			yOld = e.getY();
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e) {
			int xOnScreen = e.getXOnScreen();
			int yOnScreen = e.getYOnScreen();
			int xx = xOnScreen - xOld;
			int yy = yOnScreen - yOld;
			MainFrame.this.setLocation(xx, yy);
		}
		});
		
		setFon(font);


	}
	
	
	class PlayerListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			newPlayerPanel();
		}
		
		public void newPlayerPanel(){
			mainPanel.removeAll();
			PlayerTablePanel d=new PlayerTablePanel(ps);
			d.setBounds(200,130,800,450);
			d.setBackground(white);
			//d.setOpaque(false);
			mainPanel.add(d);
			mainPanel.paintComponents(mainPanel.getGraphics());
			mainPanel.repaint();
			mainPanel.add(teamButton);
			mainPanel.add(playerButton);
			isTeam=false;
		}
	}
	
	class TeamListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			newTeamPanel();
		}
		
		public void newTeamPanel(){
			mainPanel.removeAll();
			TeamTablePanel d=new TeamTablePanel(ts);
			d.setBounds(200,130,800,450);
			d.setBackground(white);
			//d.setOpaque(false);
			mainPanel.add(d);
			mainPanel.paintComponents(mainPanel.getGraphics());
			mainPanel.repaint();
			mainPanel.add(playerButton);
			mainPanel.add(teamButton);
			isTeam=true;
		}
		
	}
	
	class mouseInListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	class exitButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
		}

	}
	

	
	
}
