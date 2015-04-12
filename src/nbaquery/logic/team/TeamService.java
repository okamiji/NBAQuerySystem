package nbaquery.logic.team;

public interface TeamService
{
	public String[][] searchForTeams(boolean isGross, int index, boolean isUp);
	public String[][] searchForSeasonHotTeams(int index);
	public String[] searchForOneTeam(String teamName);
}
