package nbaquery.logic.player;

import nbaquery.data.Column;
import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;
import nbaquery.logic.average_player.AveragePlayer;
import nbaquery.logic.gross_player.GrossPlayer;
import nbaquery.logic.hot_player_today.HotPlayerToday;
import nbaquery.logic.progress_player.ProgressPlayer;

//XXX �������Ҫ�󣺱Ƚ������ǰ�������1�ո񣬶���֮�������һ�ո�û�²�Ҫ��ʼ��Ϊnull��
public class PlayerServiceAdapter extends NewPlayerServiceAdapter implements PlayerService, NewPlayerService
{
	public String[] columnNames, hotColumnNames, progressColumnNames, playerInfoColumnNames;
	
	public PlayerServiceAdapter(TableHost tableHost, GrossPlayer gross, AveragePlayer average, HotPlayerToday hot, ProgressPlayer progress)
	{
		this(tableHost, gross, average,hot,progress, new String[]
		{
				"match_season",//"����"
				"player_name",//"����"
				"team_name",//"���"
				"game_count",//"��������"
				"first_count",//"�ȷ�����"
				"total_board",//"����"
				"assist",//"����"
				"game_time",//"�ڳ�ʱ��"
				"shoot_rate",//"Ͷ��������"
				"three_shoot_rate",//"����������"
				"foul_shoot_rate",//"����������"
				"attack_board",//"����"
				"defence_board",//"����"
				"steal",//"����"
				"cap",//"��ñ"
				"miss",//"ʧ��"
				"foul",//"����"
				"self_score",//"�÷�"
				"efficiency",//"Ч��"
				"gmsc_efficiency",//"GmSc"
				"true_shoot_rate",//"��ʵ������"
				"shoot_efficiency",//"Ͷ��Ч��"
				"total_board_efficiency",//"������"
				"attack_board_efficiency",//"����������"
				"defence_board_efficiency",//"����������"
				"assist_rate",//"������"
				"steal_rate",//"������"
				"cap_rate",//"��ñ��"
				"miss_rate",//"ʧ����"
				"usage",//"ʹ����"
				"player_position",//"��Աλ��"
				"team_sector",//"����"
				"score_board_assist",//"��/��/��"
				"player_portrait", //ͷ��
				"player_action"	//ȫ����
		},
		new String[]{
				"player_name"	, //��Ա����
				"player_number",  //���±��
				"player_position",//��Աλ��
				"player_height"	, //��Ա���
				"player_weight"	, //��Ա����
				"player_birth"	, //��������
				"player_age"	, //����
				"player_exp"	, //����
				"player_school"	, //��ҵѧУ
				"player_portrait",//������
				"player_action"	, //ȫ����
				"self_score",//"�÷�"
				"total_board",//"����"
				"assist",//"����"
				"cap",//"��ñ"
				"steal",//"����"
				
		},new String[]{
				"player_name"	, //��Ա����
				"player_number",  //���±��
				"player_position",//��Աλ��
				"player_height"	, //��Ա���
				"player_weight"	, //��Ա����
				"player_birth"	, //��������
				"player_age"	, //����
				"player_exp"	, //����
				"player_school"	, //��ҵѧУ
				"player_portrait",//������
				"player_action"	, //ȫ����
				"self_score_now",//���峡�����÷�
				"total_board_now",//"���峡����"
				"assist_now",//"���峡����"
				"self_score_before",//�����÷�
				"total_board_before",//"����"
				"assist_before",//"����"
				"self_score_rate",//�����÷ֽ�����
				"total_board_rate",//"���������"
				"assist_rate",//"����������"
				"team_name_abbr"//�������
				
		},new String[]{
				"player_name"	, //��Ա����
				"player_number",  //���±��
				"player_position",//��Աλ��
				"player_height"	, //��Ա���
				"player_weight"	, //��Ա����
				"player_birth"	, //��������
				"player_age"	, //����
				"player_exp"	, //����
				"player_school"	, //��ҵѧУ
				"player_portrait",//������
				"player_action"	, //ȫ����
		});
	}
	
	private PlayerServiceAdapter(TableHost tableHost, GrossPlayer gross, AveragePlayer average, HotPlayerToday hot, ProgressPlayer progress,
			String[] columnNames, String[] hotColumnNames, String[] progressColumnNames, String[] playerInfoColumnNames)
	{
		
		super(tableHost, gross, average, hot, progress);
		this.hotColumnNames = hotColumnNames;
		this.progressColumnNames = progressColumnNames;
		this.columnNames = columnNames;
		this.playerInfoColumnNames = playerInfoColumnNames;
	}
	
	@Override
	public String[][] searchForPlayers(boolean isGross, int head, boolean isUp, String position, String league)
	{
		if(head < 0) head = 1;
		if(head > columnNames.length) return null;
		
		Table queryResult = this.searchForPlayers(isGross, new String[]{columnNames[head]}, isUp, position, league);
		Cursor rows = queryResult.getRows();
		
		String[][] returnValue = new String[rows.getLength()][columnNames.length];
		Column[] columns = new Column[columnNames.length];
		for(int i = 0; i < columnNames.length; i ++)
			columns[i] = queryResult.getColumn(columnNames[i]);
		for(int row = 0; row < rows.getLength(); row ++)
		{
			Row current = rows.next();
			for(int column = 0; column < columns.length; column ++)
			{
				Object value = columns[column].getAttribute(current);
				if(value != null) returnValue[row][column] = value.toString();
			}
		}
		
		//tableHost.deleteTable("player_query_result");
		return returnValue;
	}
		
	@Override
	public String[][] searchForTodayHotPlayers(int head) {
		if(head < 0) head = 1;
		if(head > hotColumnNames.length)
			return null;
		
		Table queryResult = this.searchForTodayHotPlayers(hotColumnNames[head]);
		
		Cursor rows = queryResult.getRows();
		int columnNumber=hotColumnNames.length;
		String[][] returnValue = new String[rows.getLength()][columnNumber];
		Column[] columns = new Column[columnNumber];
		for(int i = 0; i < columnNumber; i ++){
			columns[i] = queryResult.getColumn(hotColumnNames[i]);
		}
		for(int row = 0; row < rows.getLength(); row ++)
		{
			rows.absolute(row);
			Row current = rows.next();
			for(int column = 0; column < columns.length; column ++)
			{
				if(columns[column]!=null){
				Object value = columns[column].getAttribute(current);
				if(value != null) returnValue[row][column] = value.toString();
				}
			}
		}
		
		//tableHost.deleteTable("hot_player_query_result");
		return returnValue;
	}
	
	@Override
	public String[][] searchForProgressPlayers(int head) throws Exception {
		if(head < 0) head = 1;
		if(head > progressColumnNames.length)
			return null;
		
		Table queryResult = this.searchForProgressPlayers(progressColumnNames[head]);
		
		Cursor rows = queryResult.getRows();
		int columnNumber=progressColumnNames.length;
		String[][] returnValue = new String[rows.getLength()][columnNumber];
		Column[] columns = new Column[columnNumber];
		for(int i = 0; i < columnNumber; i ++){
			columns[i] = queryResult.getColumn(progressColumnNames[i]);
		}
		for(int row = 0; row < rows.getLength(); row ++){
			Row currentRow = rows.next();
			for(int column = 0; column < columns.length; column ++)
			{
				if(columns[column]!=null){
				Object value = columns[column].getAttribute(currentRow);
				if(value != null)
					returnValue[row][column] = value.toString();
				}
			}
		}
		//tableHost.deleteTable("progress_player_query_result");
		return returnValue;
	}
	
	public String[][] searchForSeasonHotPlayers(int head){
		String[][] strs=this.searchForPlayers(true, head, false, null, null);
		String[][] result=new String[5][columnNames.length];
		for(int i=0;i<5;i++)
			for(int j=0;j<strs[i].length;j++)
				result[i][j]=strs[i][j];
		return result;
	}
	
	@Override
	public String[] searchForOnePlayer(String playerName)
	{
		Table queryResult = this.searchForOnePlayerTable(playerName);
		Cursor rows = queryResult.getRows();
		int columnNumber=playerInfoColumnNames.length;
		Column[] columns = new Column[columnNumber];
		for(int i = 0; i < columnNumber; i ++)
			columns[i] = queryResult.getColumn(playerInfoColumnNames[i]);
		if(rows.getLength() == 1)
		{
			String[] returnValue = new String[columnNumber];
			for(int column = 0; column < columns.length; column ++)
			{
				Object value = columns[column].getAttribute(rows.next());
				if(value != null) returnValue[column] = value.toString();
			}
			return returnValue;
		}
		else return null;
	}
}
