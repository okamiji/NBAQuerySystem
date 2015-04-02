package nbaquery.presentation;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
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

	JPanel mainPanel,containPanel,shadowPanel,systemPanel,sPanel;
	JButton playerButton,teamButton,exitButton,vanishButton,logoButton;
	JTextField searchField ;
	
	//LogicService lcs;
	private JTextField textField;
	boolean isTeam=false;
	int LocationX;
	int LocationY;
	PlayerService ps;
	TeamService ts;
	int xOld,yOld;
	Color background=new Color(33,122,197);
	Color white = new Color(245,245,245);
	Color gray = new Color(41,48,62);
	Color blue = new Color(16,133,253);
	Font font=new Font("方正正纤黑简体", Font.BOLD, 16);
	Toolkit toolkit=Toolkit.getDefaultToolkit();
    Dimension screen=toolkit.getScreenSize();
	
	/*
	 * 设置字体的方法
	 */
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
		UIManager.put("ComboBox.background", new Color(0,0,0,0));
		this.ps=pls;
		this.ts=tms;
		Color back = new Color(0,0,0,0.0f);
		Color white = new Color(245,245,245);
		Color gray = new Color(41,48,62);
		Color blue = new Color(16,133,253);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1118,618);
		this.setResizable(false);
		this.setTitle("NBAQuery");
		
		//下面为居中
        this.LocationX=(screen.width -this.getWidth())/2;
        this.LocationY=(screen.height -this.getHeight())/2;       
        this.setLocation(this.LocationX,this.LocationY);
	    //居中完毕
        this.setLayout(null);
        this.setUndecorated(true);
        this.setOpacity(0.92f);//透明度
        this.setBackground(back);
        
        mainPanel = new JPanel();
        mainPanel.setSize(200, 580);
        mainPanel.setLocation(10,20);
        mainPanel.setLayout(null);
        mainPanel.setBackground(gray);
        mainPanel.setOpaque(true);
        
        sPanel = new JPanel(){  
            @Override  
            protected void paintComponent(Graphics g) {  
            	ImageIcon icon = new ImageIcon("IMGS/shadow.png");
                Image img = icon.getImage();  
                g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver());  
            }
        };
        sPanel.setSize(1118, 618);
        sPanel.setLocation(0,0);
        sPanel.setLayout(null);
        sPanel.setOpaque(true);
        
        shadowPanel = new JPanel() {  
            @Override  
            protected void paintComponent(Graphics g) {  
            	ImageIcon icon = new ImageIcon("IMGS/7.png");
                Image img = icon.getImage();  
                g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver());  
            }
        };
        shadowPanel.setSize(850, 600);
        shadowPanel.setLocation(210,10);
        shadowPanel.setLayout(null);
        shadowPanel.setBackground(white);

        systemPanel = new JPanel();
        systemPanel.setSize(50, 580);
        systemPanel.setLocation(1060,20);
        systemPanel.setLayout(null);
        systemPanel.setBackground(gray);
        
		teamButton = new JButton("\u7403\u961F\u6570\u636E",new ImageIcon("IMGS/球队未选中.png"));
		teamButton.setBounds(0, 130, 202, 150);
		teamButton.setFocusPainted(false);
		teamButton.setBorderPainted(false);
		teamButton.setContentAreaFilled(false);
		teamButton.addMouseListener(new TeamMouseListener());
		teamButton.addActionListener(new TeamListener());
		mainPanel.add(teamButton);
		
		playerButton= new JButton("\u7403\u5458\u6570\u636E",new ImageIcon("IMGS/球员未选中.png"));
		playerButton.setBounds(0, 280, 202, 150);
		playerButton.addActionListener(new PlayerListener());
		playerButton.setFocusPainted(false);
		playerButton.setBorderPainted(false);
		playerButton.setContentAreaFilled(false);
		playerButton.addMouseListener(new PlayerMouseListener());
		mainPanel.add(playerButton);
		
		logoButton= new JButton("",new ImageIcon("IMGS/logo.png"));
		logoButton.setBounds(20,0,150,120);
		logoButton.setFocusPainted(false);
		logoButton.setBorderPainted(false);
		logoButton.setContentAreaFilled(false);
		mainPanel.add(logoButton);
		
	 	exitButton = new JButton("", new ImageIcon("IMGS/close.png"));
		exitButton.setForeground(gray);
		exitButton.setBackground(gray);
		exitButton.setSize(30,30);
		exitButton.setLocation(10, 25);
		exitButton.setBorderPainted(false);
		exitButton.setFocusPainted(false);
		exitButton.setContentAreaFilled(false);
		exitButton.addActionListener(new exitButtonListener());
		exitButton.addMouseListener(new exitListener());
		systemPanel.add(exitButton);
		
		vanishButton = new JButton("", new ImageIcon("IMGS/vanish.png"));
		vanishButton.setForeground(gray);
		vanishButton.setBackground(gray);
		vanishButton.setSize(30,30);
		vanishButton.setLocation(10, 65);
		vanishButton.setBorderPainted(false);
		vanishButton.setFocusPainted(false);
		vanishButton.setContentAreaFilled(false);
		vanishButton.addActionListener(new vanishButtonListener());
		vanishButton.addMouseListener(new vanishListener());
		systemPanel.add(vanishButton);	
		
		/*JButton button_1 = new JButton("\u5176\u4ED6\u529F\u80FD",new ImageIcon("IMGS/热点普通.png"));
		button_1.setBounds(0, 430, 202, 150);
		button_1.setFocusPainted(false);
		button_1.setBorderPainted(false);
		button_1.setContentAreaFilled(false);
		this.getContentPane().add(button_1);
		button_1.addActionListener(new exitButtonListener());
		*/
		this.add(shadowPanel);
		this.add(mainPanel);
		this.add(systemPanel);
		this.add(sPanel);
		this.setVisible(true);
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
			shadowPanel.removeAll();
			PlayerTablePanel d=new PlayerTablePanel(ps);
			d.setSize(850,650);
			d.setOpaque(false);
			shadowPanel.add(d);
			shadowPanel.paintComponents(shadowPanel.getGraphics());
			shadowPanel.repaint();
			isTeam=false;
		}
	}
	
	class TeamListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			newTeamPanel();
		}
		
		public void newTeamPanel(){
			shadowPanel.removeAll();
			TeamTablePanel d=new TeamTablePanel(ts);
			d.setSize(850,650);
			d.setBackground(background);
			d.setOpaque(false);
			shadowPanel.add(d);
			shadowPanel.repaint();
			shadowPanel.revalidate();
			//teamButton.setIcon(new ImageIcon("IMGS/球队选中.png"));
			//teamButton.repaint();
			isTeam=true;
		}
		
	}
	
	class PlayerMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			teamButton.setIcon(new ImageIcon("IMGS/球队未选中.png"));
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			playerButton.setIcon(new ImageIcon("IMGS/球员选中.png"));
			playerButton.repaint();
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			if(!isTeam)
				return;
			playerButton.setIcon(new ImageIcon("IMGS/球员未选中.png"));
			playerButton.repaint();
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
	
	class vanishButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			setExtendedState(JFrame.ICONIFIED);
		}
	}
	
	class exitListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			exitButton.setIcon(new ImageIcon("IMGS/close2.png"));
			exitButton.repaint();
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
			exitButton.setIcon(new ImageIcon("IMGS/close.png"));
			exitButton.repaint();
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
	
	class vanishListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			vanishButton.setIcon(new ImageIcon("IMGS/vanish2.png"));
			vanishButton.repaint();
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			vanishButton.setIcon(new ImageIcon("IMGS/vanish.png"));
			vanishButton.repaint();
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
	
	class TeamMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			playerButton.setIcon(new ImageIcon("IMGS/球员未选中.png"));
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			teamButton.setIcon(new ImageIcon("IMGS/球队选中.png"));
			teamButton.repaint();
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			if(isTeam)
				return;
			teamButton.setIcon(new ImageIcon("IMGS/球队未选中.png"));
			teamButton.repaint();
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
