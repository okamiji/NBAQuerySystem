package nbaquery.presentation3.player;

import javax.swing.JPanel;

import nbaquery.logic.player.NewPlayerService;

@SuppressWarnings("serial")
public class HotPlayerSubPanel extends JPanel
{
	NewPlayerService playerService;
	
	public HotPlayerSubPanel(NewPlayerService playerService)
	{
		this.playerService = playerService;
	}
}
