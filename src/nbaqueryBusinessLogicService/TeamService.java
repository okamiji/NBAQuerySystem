package nbaqueryBusinessLogicService;
import nbaquery.data.Table;

public interface TeamService {
	
	public String[][] searchForTeams(boolean type,String head,boolean upOrDown);

}
