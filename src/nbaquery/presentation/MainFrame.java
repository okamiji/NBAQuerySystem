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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;

import nbaquery.logic.match.MatchService;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;
import nbaquery.presentation.resource.ImageIconResource;

import javax.swing.JTextField;
import javax.swing.plaf.FontUIResource;







import java.util.Enumeration;


@SuppressWarnings("serial")
public class MainFrame extends JFrame{

	JPanel mainPanel,containPanel,shadowPanel,systemPanel,sPanel;
	JButton playerButton,teamButton,exitButton,vanishButton,logoButton;
	JTextField searchField ;
	
	//LogicService lcs;
	@SuppressWarnings("unused")
	private JTextField textField;
	boolean isTeam=false;
	int LocationX;
	int LocationY;
	PlayerService player_service;
	TeamService team_service;
	MatchService match_service;
	int xOld,yOld;
	Color background=new Color(33,122,197);
	Color white = new Color(245,245,245);
	Color gray = new Color(41,48,62);
	Color blue = new Color(16,133,253);
	Font font=new Font("�������˺ڼ���", Font.BOLD, 16);
	Toolkit toolkit=Toolkit.getDefaultToolkit();
    Dimension screen=toolkit.getScreenSize();
	
	/*
	 * ��������ķ���
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
	/*
	public static void main(String[] args){
		MainFrame f=new MainFrame();
	}
	*/
	

	@SuppressWarnings({ "unused" })
	public MainFrame(PlayerService playerService, TeamService teamService, MatchService matchService){
		UIManager.put("ComboBox.background", new Color(0,0,0,0));
		this.player_service = playerService;
		this.team_service = teamService;
		this.match_service = matchService;
		Color back = new Color(0,0,0,0.0f);
		Color white = new Color(245,245,245);
		Color gray = new Color(41,48,62);
		Color blue = new Color(16,133,253);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1118,618);
		this.setResizable(false);
		this.setTitle("NBAQuery");
		
		//����Ϊ����
        this.LocationX=(screen.width -this.getWidth())/2;
        this.LocationY=(screen.height -this.getHeight())/2;       
        this.setLocation(this.LocationX,this.LocationY);
	    //�������
        this.setLayout(null);
        this.setUndecorated(true);
        this.setOpacity(0.92f);//͸����
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
        
		teamButton = new JButton("\u7403\u961F\u6570\u636E",new ImageIcon("IMGS/���δѡ��.png"));
		teamButton.setBounds(0, 130, 202, 150);
		teamButton.setFocusPainted(false);
		teamButton.setBorderPainted(false);
		teamButton.setContentAreaFilled(false);
		teamButton.addMouseListener(new TeamMouseListener());
		teamButton.addActionListener(new TeamListener());
		mainPanel.add(teamButton);
		
		playerButton= new JButton("\u7403\u5458\u6570\u636E",new ImageIcon("IMGS/��Աδѡ��.png"));
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
		
		/*JButton button_1 = new JButton("\u5176\u4ED6\u529F\u80FD",new ImageIcon("IMGS/�ȵ���ͨ.png"));
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
		//-------------���϶�-------------
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
			PlayerTablePanel d=new PlayerTablePanel(player_service);
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
			TeamTablePanel d=new TeamTablePanel(team_service);
			d.setSize(850,650);
			d.setBackground(background);
			d.setOpaque(false);
			shadowPanel.add(d);
			shadowPanel.repaint();
			shadowPanel.revalidate();
			//teamButton.setIcon(new ImageIcon("IMGS/���ѡ��.png"));
			//teamButton.repaint();
			isTeam=true;
		}
		
	}
	
	class PlayerMouseListener extends MouseAdapter{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			teamButton.setIcon(new ImageIcon("IMGS/���δѡ��.png"));
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			playerButton.setIcon(new ImageIcon("IMGS/��Աѡ��.png"));
			playerButton.repaint();
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			if(!isTeam)
				return;
			playerButton.setIcon(new ImageIcon("IMGS/��Աδѡ��.png"));
			playerButton.repaint();
		}
	}
	
	class vanishButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			setExtendedState(JFrame.ICONIFIED);
		}
	}
	
	class exitListener extends MouseAdapter {
		ImageIcon enterIcon = ImageIconResource.getImageIcon("IMGS/close2.png");
		ImageIcon leaveIcon = ImageIconResource.getImageIcon("IMGS/close.png");
		@Override
		public void mouseEntered(MouseEvent arg0) {
			exitButton.setIcon(enterIcon);
			exitButton.repaint();
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			exitButton.setIcon(leaveIcon);
			exitButton.repaint();
		}

	}
	
	class vanishListener extends MouseAdapter {

	

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



	}
	
	class TeamMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			playerButton.setIcon(new ImageIcon("IMGS/��Աδѡ��.png"));
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			teamButton.setIcon(new ImageIcon("IMGS/���ѡ��.png"));
			teamButton.repaint();
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			if(isTeam)
				return;
			teamButton.setIcon(new ImageIcon("IMGS/���δѡ��.png"));
			teamButton.repaint();
		}

	}
	

	class exitButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
		}

	}
	

	
	
}
