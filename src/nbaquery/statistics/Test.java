package nbaquery.statistics;

public class Test {
	public static void main(String[] args) throws Exception{
	//	PlayerStatistics ps = new PlayerStatistics();
	//	ps.draw_line_chart("Aaron Brooks");
		GetData getData = new GetData();
		getData.launch();
	//	System.out.println(getData.get_all_player_data());
		System.out.println(getData.get_single_player_data("Aaron Brooks"));
	}
}
