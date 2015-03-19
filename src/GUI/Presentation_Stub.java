package GUI;

public class Presentation_Stub {
	String[][] strs=new String[50][31];
	
	public String[][] searchForPlayers(boolean type,String head,boolean upDown,String position,String league){
		for(int i=0;i<50;i++)
			for(int j=0;j<31;j++)
				strs[i][j]=Integer.toString(i);
		return strs;
	}
	
	public String[][] searchForTeams(boolean type,String head,boolean upDown){
		for(int i=0;i<50;i++)
			for(int j=0;j<31;j++)
					strs[i][j]=Integer.toString(i);
		return strs;
	}
}
