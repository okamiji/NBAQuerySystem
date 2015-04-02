package nbaquery_test.presentation;

import nbaquery.logic.player.PlayerService;
import nbaquery.logic.team.TeamService;

public class Presentation_Stub implements PlayerService , TeamService{
	String[][] strs=new String[50][35];
	
	public String[][] searchForPlayers(boolean type,int head,boolean upDown,String position,String league){
		for(int i=0;i<50;i++)
			for(int j=0;j<35;j++)
				strs[i][j]=Integer.toString(i);
		return strs;
	}
	
	public String[][] searchForTeams(boolean type,int head,boolean upDown){
		for(int i=0;i<50;i++)
			for(int j=0;j<34;j++)
					strs[i][j]=Integer.toString(i);
		return strs;
	}
}
