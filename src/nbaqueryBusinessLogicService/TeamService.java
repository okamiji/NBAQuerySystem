package nbaqueryBusinessLogicService;
import nbaquery.data.Table;

public interface TeamService {
	
	public Table searchForTeams(boolean type,String head,boolean upOrDown,String position,String league);

}
