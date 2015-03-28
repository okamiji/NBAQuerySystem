package nbaqueryBusinessLogicService;
import nbaquery.data.Table;

public interface PlayerService {

	public String[][] searchForPlayers(boolean type,String head,boolean upOrDown,String position,String league);
	
}
