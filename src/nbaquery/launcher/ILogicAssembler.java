package nbaquery.launcher;

import nbaquery.data.TableHost;
import nbaquery.logic.match.MatchService;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.NewTeamService;
import nbaquery.logic.team.TeamService;

public interface ILogicAssembler
{
	public void assemble(TableHost tableHost);
	
	public TeamService getTeamService();
	
	public PlayerService getPlayerService();
	
	public MatchService getMatchService();

	public NewPlayerService getNPS();
	
	public NewTeamService getNTS();
}
