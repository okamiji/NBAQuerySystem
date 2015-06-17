package nbaquery.presentation3.team;

import java.awt.Graphics;

import javax.swing.JPanel;

import nbaquery.logic.team.NewTeamService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.DisplayButton;
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

	public final DistributeTeamPanel graphPanel;
	
	public boolean useCompare = true;

	public final DisplayButton teamButton;
	
	public final DisplayButton diagram;
	public final DisplayButton statistic;
	
	public TeamPanel(DetailedInfoContainer infoContainer, NewTeamService teamService, DisplayButton teamButton)
	{
		this.setLayout(null);
		this.setBounds(0, 0, MainFrame.width - 1, MainFrame.height - 1);
		this.setBackground(MainFrame.transparent);
		
		this.hotspotTeam = new HotTeamSubPanel(teamService, infoContainer, 600, 240);
		this.hotspotTeam.setLocation(160, 480);
		this.hotspotTeam.setBackground(MainFrame.transparent);
		super.add(hotspotTeam);
		
		this.teamSubPanel = new CompareTeamSubPanel(teamService, infoContainer, 700, 600, 25);
		this.teamSubPanel.teamTable.setRowHeight(20);
		this.teamSubPanel.setLocation(60, 60);
		this.teamSubPanel.setBackground(MainFrame.transparent);
		super.add(teamSubPanel);
		
		/** To be modified **/
		this.graphPanel = new DistributeTeamPanel(teamService, infoContainer, 720, 600, 5);
		this.graphPanel.setLocation(40, 60);
		this.graphPanel.setBackground(MainFrame.transparent);
		super.add(graphPanel);
		
		this.teamButton = teamButton;
		super.add(this.teamButton);
		
		this.statistic = new DisplayButton("img3/statistic_idle.png", "img3/statistic_over.png")
		{
			@Override
			protected void activate() {
				useCompare = true;
			}
		};
		this.statistic.setLocation(-5, 60);
		super.add(statistic);
		
		this.diagram = new DisplayButton("img3/diagram_idle.png", "img3/diagram_over.png")
		{
			@Override
			protected void activate() {
				useCompare = false;
			}
		};
		this.diagram.setLocation(-5, 60 + this.statistic.getHeight());
		super.add(diagram);
	}
	
	public void paint(Graphics g)
	{
		g.setColor(MainFrame.backgroundColor);
		if(mode == HOT_SPOT)
		{
			this.hotspotTeam.setVisible(true);
			this.teamSubPanel.setVisible(false);
			this.graphPanel.setVisible(false);
			this.teamButton.setLocation(60, 590 - this.teamButton.getHeight()/2);
			this.diagram.setVisible(false);
			this.statistic.setVisible(false);
			g.fillRect(30, 470, 740, 240);
		}
		else if(mode == SHOW)
		{
			this.hotspotTeam.setVisible(false);
			this.statistic.setVisible(true);
			this.diagram.setVisible(true);
			
			if(useCompare)
			{
				this.teamSubPanel.setVisible(true);
				this.graphPanel.setVisible(false);
				this.statistic.setEnabled(false);
				this.diagram.setEnabled(true);
			}
			else 
			{
				this.teamSubPanel.setVisible(false);
				this.graphPanel.setVisible(true);
				this.statistic.setEnabled(true);
				this.diagram.setEnabled(false);				
			}
			
			g.fillRect(30, 40, 740, 600);
			g.fillRect(30, 650, 740, 60);
			this.teamButton.setLocation(710 - this.teamButton.getWidth() / 2, 680 - this.teamButton.getHeight() / 2);
			g.setColor(MainFrame.selectedShadow);
			g.fillRect(710 - this.teamButton.getWidth() / 2, 710, 60, 5);
		}
		else
		{
			this.hotspotTeam.setVisible(false);
			this.teamSubPanel.setVisible(false);
			this.graphPanel.setVisible(false);
			this.teamButton.setLocation(710 - this.teamButton.getWidth() / 2, 680 - this.teamButton.getHeight() / 2);
			this.diagram.setVisible(false);
			this.statistic.setVisible(false);
		}
		super.paint(g);
	}
}