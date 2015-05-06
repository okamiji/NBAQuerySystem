package nbaquery.presentation3.team;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import nbaquery.logic.team.NewTeamService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.MainFrame;

@SuppressWarnings("serial")
public class TeamPanel extends JPanel
{
	
	public static final int HOT_SPOT = 0;
	public static final int SHOW = 1;
	public static final int MINIMIZE = 2;
	
	public int mode = HOT_SPOT;
	
	public final HotTeamSubPanel hotspotTeam;
	public final CompareTeamSubPanel teamSubPanel;
	
	public TeamPanel(DetailedInfoContainer infoContainer, NewTeamService teamService)
	{
		this.setLayout(null);
		this.setBounds(0, 0, MainFrame.width - 1, MainFrame.height - 1);
		this.setBackground(new Color(0, 0, 0, 0));
		
		this.hotspotTeam = new HotTeamSubPanel(teamService, infoContainer, 600, 240);
		this.hotspotTeam.setLocation(160, 480);
		super.add(hotspotTeam);
		
		this.teamSubPanel = new CompareTeamSubPanel(teamService, infoContainer, 720, 600, 25);
		this.teamSubPanel.setLocation(60, 120);
		super.add(teamSubPanel);
	}
	
	public void paint(Graphics g)
	{
		if(mode == HOT_SPOT)
		{
			this.hotspotTeam.setVisible(true);
			this.teamSubPanel.setVisible(false);
		}
		else if(mode == SHOW)
		{
			this.hotspotTeam.setVisible(false);
			this.teamSubPanel.setVisible(true);
		}
		else
		{
			this.hotspotTeam.setVisible(false);
			this.teamSubPanel.setVisible(false);
		}
		super.paint(g);
	}
}