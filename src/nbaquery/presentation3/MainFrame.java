package nbaquery.presentation3;

import java.awt.Dimension;
import javax.swing.JFrame;

import nbaquery.logic.match.NewMatchService;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.logic.team.NewTeamService;

@SuppressWarnings("serial")
public class MainFrame extends JFrame
{
	NewPlayerService newPlayerService;
	NewTeamService newTeamService;
	NewMatchService newMatchService;
	
	public DisplayButton closeButton, minimizeButton;
	
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
		super.setSize(600, 720);
		super.setUndecorated(true);
		Dimension screenSize = super.getToolkit().getScreenSize();
		super.setLocation((screenSize.width - super.getWidth()) / 2, (screenSize.height - super.getHeight()) / 2);
		super.setAlwaysOnTop(true);
		
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
		super.add(closeButton);
		
		minimizeButton = new DisplayButton("img3/minimize_idle.png", "img3/minimize_over.png")
		{
			@Override
			protected void activate()
			{
				MainFrame.this.setExtendedState(JFrame.ICONIFIED);
			}
		};
		minimizeButton.setLocation(super.getWidth() - closeButton.getWidth() - minimizeButton.getWidth() - 6, 3);
		super.add(minimizeButton);
		
		//XXX start refresh thread.
		this.refresh.start();
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
	
	public static void main(String[] arguments)
	{
		new MainFrame(null, null, null).setVisible(true);
	}
}
