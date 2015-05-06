package nbaquery.presentation3.player;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.DetailedInfoContainer;
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
	
	public PlayerPanel(DetailedInfoContainer infoContainer, NewPlayerService playerService)
	{
		this.setLayout(null);
		this.setBounds(0, 0, MainFrame.width - 1, MainFrame.height - 1);
		this.setBackground(new Color(0, 0, 0, 0));
		
		this.hotspotPlayer = new HotPlayerSubPanel(playerService, infoContainer, 600, 240);
		this.hotspotPlayer.setLocation(160, 180);
		super.add(hotspotPlayer);
		
		this.playerSubPanel = new ComparePlayerSubPanel(playerService, infoContainer, 720, 600, 25);
		this.playerSubPanel.setLocation(60, 60);
		super.add(playerSubPanel);
	}
	
	public void paint(Graphics g)
	{
		if(mode == HOT_SPOT)
		{
			this.hotspotPlayer.setVisible(true);
			this.playerSubPanel.setVisible(false);
		}
		else if(mode == SHOW)
		{
			this.hotspotPlayer.setVisible(false);
			this.playerSubPanel.setVisible(true);
		}
		else
		{
			this.hotspotPlayer.setVisible(false);
			this.playerSubPanel.setVisible(false);
		}
		super.paint(g);
	}
}
