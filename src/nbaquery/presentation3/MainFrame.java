package nbaquery.presentation3;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import nbaquery.data.Row;
import nbaquery.logic.match.NewMatchService;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.logic.team.NewTeamService;
import nbaquery.presentation.resource.ImageIconResource;
import nbaquery.presentation3.match.MatchPanel;
import nbaquery.presentation3.player.PlayerPanel;
import nbaquery.presentation3.team.TeamPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame
{
	public static final int width = 800;
	public static final int height = 720;
	
	NewPlayerService newPlayerService;
	NewTeamService newTeamService;
	NewMatchService newMatchService;
	
	public DisplayButton closeButton, minimizeButton;
	
	public final DetailedInfoPanel detailedDisplay;
	
	PlayerPanel playerPanel;	DisplayButton playerButton;		public static final int PLAYER_VIEW = 1;
	MatchPanel matchPanel;		DisplayButton matchButton;		public static final int TEAM_VIEW = 2;
	TeamPanel teamPanel;		DisplayButton teamButton;		public static final int MATCH_VIEW = 3;
	public static final int HOTSPOT = 0;
	
	int currentView = HOTSPOT;
	
	public void swapView(int target)
	{
		if(target == HOTSPOT)
		{
			playerPanel.mode = PlayerPanel.HOT_SPOT;
			teamPanel.mode = TeamPanel.HOT_SPOT;
			matchPanel.mode = MatchPanel.HOT_SPOT;
		}
		else if(target == PLAYER_VIEW)
		{
			playerPanel.mode = PlayerPanel.SHOW;
			teamPanel.mode = TeamPanel.MINIMIZE;
			matchPanel.mode = MatchPanel.MINIMIZE;
		}
		else if(target == TEAM_VIEW)
		{
			playerPanel.mode = PlayerPanel.MINIMIZE;
			teamPanel.mode = TeamPanel.SHOW;
			matchPanel.mode = MatchPanel.MINIMIZE;
		}
		else if(target == MATCH_VIEW)
		{
			playerPanel.mode = PlayerPanel.MINIMIZE;
			teamPanel.mode = TeamPanel.MINIMIZE;
			matchPanel.mode = MatchPanel.SHOW;
		}
		currentView = target;
	}
	
	Image background;
	JPanel framePanel = new JPanel()
	{
		{
			setBackground(transparent);
		}
		public void paint(Graphics g)
		{
			g.drawImage(background, 0, 0, width, height, null);
			super.paint(g);
		}
	};
	
	public static final Color backgroundColor = new Color(1.0f, 1.0f, 1.0f, 0.5f);
	public static final Color transparent = new Color(0, 0, 0, 0);
	
	public MainFrame(NewPlayerService newPlayerService,
			NewTeamService newTeamService, NewMatchService newMatchService)
	{
		super();
		
		//XXX setting up business logic layer services.
		this.newPlayerService = newPlayerService;
		this.newTeamService = newTeamService;
		this.newMatchService = newMatchService;
		
		//XXX setting up basic parameters of the frame.
		super.setLayout(null);
		super.setSize(width, height);
		super.setUndecorated(true);
		Dimension screenSize = super.getToolkit().getScreenSize();
		super.setLocation((screenSize.width - super.getWidth()) / 2, (screenSize.height - super.getHeight()) / 2);
		super.setAlwaysOnTop(true);
		
		//XXX adding frame panel.
		framePanel.setLayout(null);
		framePanel.setSize(width, height);
		super.add(framePanel);
		
		//XXX adding background to the frame.
		background = ImageIconResource.getImageIcon("img3/background.jpg").getImage();
		
		//XXX adding extended state button to the screen.
		closeButton = new DisplayButton("img3/exit_idle.png", "img3/exit_over.png")
		{
			@Override
			protected void activate()
			{
				System.exit(0);
			}
		};
		closeButton.setLocation(super.getWidth() - closeButton.getWidth() - 3, 3);
		this.add(closeButton);
		
		minimizeButton = new DisplayButton("img3/minimize_idle.png", "img3/minimize_over.png")
		{
			@Override
			protected void activate()
			{
				MainFrame.this.setExtendedState(JFrame.ICONIFIED);
			}
		};
		minimizeButton.setLocation(super.getWidth() - closeButton.getWidth() - minimizeButton.getWidth() - 6, 3);
		this.add(minimizeButton);
		
		//XXX adding detailed info container.
		this.detailedDisplay = new DetailedInfoPanel(newMatchService, newTeamService);
		this.detailedDisplay.setLocation(this.getWidth() - this.detailedDisplay.getWidth(), (this.getHeight() - this.detailedDisplay.getHeight()) / 2);
		this.add(this.detailedDisplay);
		
		//XXX adding functional panels.

		this.matchButton = new DisplayButton("img3/match_idle.png", "img3/match_idle.png")	//XXX TODO TEMPORARILY THE SAME
		{
			@Override
			protected void activate()
			{
				if(currentView == MATCH_VIEW) swapView(HOTSPOT);
				else swapView(MATCH_VIEW);
			}
		};
		this.matchPanel = new MatchPanel(this.detailedDisplay, newMatchService, matchButton);
		this.add(matchPanel);
		

		this.playerButton = new DisplayButton("img3/player_idle.png", "img3/player_idle.png")	//XXX TODO TEMPORARILY THE SAME
		{
			@Override
			protected void activate()
			{
				if(currentView == PLAYER_VIEW) swapView(HOTSPOT);
				else swapView(PLAYER_VIEW);				
			}
		};
		
		this.playerPanel = new PlayerPanel(this.detailedDisplay, newPlayerService, this.playerButton);
		this.add(playerPanel);
		
		
		this.teamButton = new DisplayButton("img3/team_idle.png", "img3/team_idle.png")	//XXX TODO TEMPORARILY THE SAME
		{
			@Override
			protected void activate()
			{
				if(currentView == TEAM_VIEW) swapView(HOTSPOT);
				else swapView(TEAM_VIEW);
			}
		};
		
		this.teamPanel = new TeamPanel(this.detailedDisplay, newTeamService, teamButton);
		this.add(teamPanel);
		
		//XXX start refresh thread.
		this.refresh.start();
	}
	
	public Component add(Component c)
	{
		return this.framePanel.add(c);
	}
	
	Thread refresh = new Thread()
	{
		public void run()
		{
			while(true) try
			{
				if(MainFrame.this.isVisible()) MainFrame.this.repaint();
				Thread.sleep(10);
			}
			catch(Exception e)
			{
				
			}
		}
	};
	
	MouseAdapter windowMove = new MouseAdapter()
	{
		int renderThreshold = 30;
		
		int x, y;
		boolean shouldMove = false;
		public void mousePressed(MouseEvent me)
		{
			if(!shouldMove) if(me.getY() < renderThreshold)
			{
				x = me.getXOnScreen();
				y = me.getYOnScreen();
				shouldMove = true;
			}
		}
		
		public void mouseReleased(MouseEvent me)
		{
			if(shouldMove)
			{
				MainFrame.this.setLocation(MainFrame.this.getX() + me.getXOnScreen() - x,
						MainFrame.this.getY() + me.getYOnScreen() - y);
				shouldMove = false;
			}
		}
	};
	{
		this.addMouseListener(windowMove);
	}
}
