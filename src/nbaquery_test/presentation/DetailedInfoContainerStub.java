package nbaquery_test.presentation;

import nbaquery.data.Row;
import nbaquery.logic.team.NewTeamService;
import nbaquery.logic.team.TeamService;
import nbaquery.presentation3.DetailedInfoContainer;

public class DetailedInfoContainerStub implements DetailedInfoContainer
{
	NewTeamService newTeamService;
	public DetailedInfoContainerStub(TeamService teamService)
	{
		this.newTeamService = (NewTeamService) teamService;
	}
	
	@Override
	public void displayPlayerInfo(Row player, boolean s)
	{
		System.out.println("=================================================================");
		System.out.println(player.getDeclaredTable().getColumn("player_name").getAttribute(player));
		System.out.println(player.getDeclaredTable().getColumn("player_age").getAttribute(player));
		System.out.println(player.getDeclaredTable().getColumn("player_exp").getAttribute(player));
		System.out.println(player.getDeclaredTable().getColumn("player_birth").getAttribute(player));
		System.out.println(player.getDeclaredTable().getColumn("player_school").getAttribute(player));
		System.out.println(player.getDeclaredTable().getColumn("player_height").getAttribute(player));
		System.out.println(player.getDeclaredTable().getColumn("player_weight").getAttribute(player));
		System.out.println("=================================================================");
	}

	@Override
	public void displayTeamInfo(Row team, boolean s)
	{
		System.out.println("=================================================================");
		System.out.println(team.getDeclaredTable().getColumn("team_name").getAttribute(team));
		System.out.println(team.getDeclaredTable().getColumn("team_name_abbr").getAttribute(team));
		System.out.println("=================================================================");
	}

	@Override
	public void displayMatchInfo(Row match, boolean stacked)
	{
		System.out.println("=================================================================");
		System.out.println(match.getDeclaredTable().getColumn("match_id").getAttribute(match));
		System.out.println(match.getDeclaredTable().getColumn("match_season").getAttribute(match));
		System.out.println(match.getDeclaredTable().getColumn("match_date").getAttribute(match));
		System.out.println(match.getDeclaredTable().getColumn("match_host_abbr").getAttribute(match));
		System.out.println(match.getDeclaredTable().getColumn("match_guest_abbr").getAttribute(match));
		System.out.println("=================================================================");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void displayTeamInfo(String teamNameOrAbbr, boolean isDescend,
			boolean stacked)
	{
		Row[] rows = newTeamService.searchInfoByName(teamNameOrAbbr, isDescend).getRows();
		if(rows.length > 0) this.displayTeamInfo(rows[0], stacked);
	}
	
}
