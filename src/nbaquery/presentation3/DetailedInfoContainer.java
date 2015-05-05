package nbaquery.presentation3;

import nbaquery.data.Row;

public interface DetailedInfoContainer
{
	public void displayPlayerInfo(Row player, boolean stacked);
	
	public void displayTeamInfo(Row team, boolean stacked);
	
	public void displayTeamInfo(String teamNameOrAbbr, boolean isAbbr, boolean stacked);
	/*	{
	 * 		Row[] rows = newTeamService.searchInfoByName(teamNameOrAbbr, isAbbr).getRows();
	 *		if(rows.length > 0) this.displayTeamInfo(rows[0], stacked);
	 *	}
	 */
	
	public void displayMatchInfo(Row match, boolean stacked);
}
