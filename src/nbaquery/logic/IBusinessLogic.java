package nbaquery.logic;

public interface IBusinessLogic
{
	public String[][] searchForPlayers(boolean type,String head,boolean upDown,String position,String league);
	
	public String[][] searchForTeams(boolean type,String head,boolean upDown);
}
