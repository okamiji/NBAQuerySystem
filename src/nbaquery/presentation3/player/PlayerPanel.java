package nbaquery.presentation3.player;

import java.awt.Graphics;

import javax.swing.JPanel;

import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.DisplayButton;
import nbaquery.presentation3.MainFrame;

@SuppressWarnings("serial")
public class PlayerPanel extends JPanel
{
	
	public static final int HOT_SPOT = 0;
	public static final int SHOW = 1;
	public static final int MINIMIZE = 2;
	
	public int mode = HOT_SPOT;
	
	public final HotPlayerSubPanel hotspotPlayer;
	public final ComparePlayerSubPanel playerSubPanel;
	public final DisplayButton playerButton;
	
	public PlayerPanel(DetailedInfoContainer infoContainer, NewPlayerService playerService, DisplayButton playerButton)
	{
		this.setLayout(null);
		this.setBounds(0, 0, MainFrame.width - 1, MainFrame.height - 1);
		this.setBackground(MainFrame.transparent);
		
		this.hotspotPlayer = new HotPlayerSubPanel(playerService, infoContainer, 600, 240);
		this.hotspotPlayer.setLocation(160, 200);
		this.hotspotPlayer.setBackground(MainFrame.transparent);
		super.add(hotspotPlayer);
		
		this.playerSubPanel = new ComparePlayerSubPanel(playerService, infoContainer, 720, 600, 25);
		this.playerSubPanel.playerTable.setRowHeight(20);
		this.playerSubPanel.setLocation(40, 60);
		this.playerSubPanel.setBackground(MainFrame.transparent);
		super.add(playerSubPanel);
		
		this.playerButton = playerButton;
		super.add(playerButton);
	}
	
	public void paint(Graphics g)
	{
		g.setColor(MainFrame.backgroundColor);
		if(mode == HOT_SPOT)
		{
			this.hotspotPlayer.setVisible(true);
			this.playerSubPanel.setVisible(false);
			this.playerButton.setLocation(60, 320 - this.playerButton.getHeight()/2);
			g.fillRect(30, 190, 740, 260);
		}
		else if(mode == SHOW)
		{
			this.hotspotPlayer.setVisible(false);
			this.playerSubPanel.setVisible(true);
			g.fillRect(30, 40, 740, 600);
			g.fillRect(30, 650, 740, 60);
			this.playerButton.setLocation(400 - this.playerButton.getWidth() / 2, 680 - this.playerButton.getHeight() / 2);
			g.setColor(MainFrame.selectedShadow);
			g.fillRect(370, 710, 60, 5);
		}
		else
		{
			this.hotspotPlayer.setVisible(false);
			this.playerSubPanel.setVisible(false);
			this.playerButton.setLocation(400 - this.playerButton.getWidth() / 2, 680 - this.playerButton.getHeight() / 2);
		}
		super.paint(g);
	}
}
