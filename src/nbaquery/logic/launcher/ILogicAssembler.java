package nbaquery.logic.launcher;

import nbaquery.data.TableHost;
import nbaquery.logic.team.TeamService;

public interface ILogicAssembler
{
	public void assemble(TableHost tableHost);
	
	public TeamService getTeamService();
}
