package nbaquery.presentation;

import org.w3c.dom.Node;

import nbaquery.launcher.Installer;
import nbaquery.launcher.Main;
import nbaquery.logic.match.MatchService;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;

public class PresentationInstaller implements Installer<Object>
{

	@Override
	public Object install(Node documentNode, Object... params) throws Exception
	{
		Main main = (Main) params[0];
		new MainFrame((PlayerService)main.playerService, (TeamService)main.teamService, (MatchService)main.matchService);
		return null;
	}

}
