package nbaquery.presentation3.player;

import java.util.HashMap;

import javax.swing.JPanel;

import nbaquery.data.Table;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.DropList;
import nbaquery.presentation3.DualTableColumn;
import nbaquery.presentation3.GameTimeColumn;
import nbaquery.presentation3.PresentationTableModel;
import nbaquery.presentation3.table.DefaultTableColumn;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.DisplayTableColumn;

@SuppressWarnings("serial")
public class ComparePlayerSubPanel extends JPanel
{
	NewPlayerService playerService;
	DetailedInfoContainer detailedInfo;
	public final DisplayTable playerTable;
	
	public boolean isGross = true;
	public String league = null;
	public String position = null;
	public boolean descend = false;
	
	public boolean shouldRedoQuery = true;
	
	protected ComparePlayerModel currentPlayerModel;
	public void switchToModel(ComparePlayerModel model)
	{
		playerTable.columnModel = model;
		playerTable.tableModel = model;
	}
	
	public class ComparePlayerModel extends PresentationTableModel
	{
		public final HashMap<DisplayTableColumn, String[]> keywordMap = new HashMap<DisplayTableColumn, String[]>();
		public String[] keyword = new String[]{"player_name"};
		
		@Override
		public void onRepaint(DisplayTable table)
		{
			if(ComparePlayerSubPanel.this.playerService.shouldRedoQuery(this) || shouldRedoQuery)
			{
				Table resultTable = ComparePlayerSubPanel.this.playerService
					.searchForPlayers(isGross, keyword, descend, position, league);
				
				this.updateTable(resultTable);
				shouldRedoQuery = false;
			}
		}
	}

	public final ComparePlayerModel basicData;
	public final ComparePlayerModel analyticData;
	
	public ComparePlayerSubPanel(NewPlayerService playerService, DetailedInfoContainer detailedInfo, int width, int height, final int sectionPerPageParam)
	{
		this.playerService = playerService;
		this.detailedInfo = detailedInfo;
		
		this.setSize(width, height);
		this.setLayout(null);
		
		this.playerTable = new DisplayTable();
		this.playerTable.setSize(width, height - 24);
		this.playerTable.setLocation(2, 20 + 2);
		super.add(playerTable);
		
		playerTable.setRowHeight(playerTable.getHeight() / (sectionPerPageParam + 1));
		basicData = new ComparePlayerModel()
		{
			{
				setSectionPerPage(sectionPerPageParam);
				DefaultTableColumn season = columnModel.addColumn("����", "match_season");
				season.padding = 10;	keywordMap.put(season, new String[]{"match_season"});
				
				DefaultTableColumn portrait = columnModel.addColumn("", "player_portrait");
				portrait.padding = playerTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn player_name = columnModel.addColumn("��Ա����", "player_name");
				player_name.padding = 70;	keywordMap.put(season, new String[]{"player_name"});
				
				DefaultTableColumn team_name = columnModel.addColumn("���", "team_name_abbr");
				team_name.padding = 5;	keywordMap.put(team_name, new String[]{"team_name_abbr"});
				
				DualTableColumn attendance = new DualTableColumn("����", "first_count", "game_count", "%1-%2"); attendance.padding = 5;
				columnModel.addColumn(attendance);	keywordMap.put(attendance, new String[]{"game_count", "first_count"});
				
				GameTimeColumn game_time = new GameTimeColumn();
				columnModel.addColumn(game_time);	keywordMap.put(game_time, new String[]{"game_time"});
				
				DualTableColumn foul_shoot = new DualTableColumn("����", "foul_shoot_score", "foul_shoot_count", "%1-%2"); foul_shoot.padding = 5;
				columnModel.addColumn(foul_shoot);	keywordMap.put(foul_shoot, new String[]{"foul_shoot_count", "foul_shoot_score"});
				
				DualTableColumn two_shoot = new DualTableColumn("Ͷ��", "shoot_score", "shoot_count", "%1-%2"); two_shoot.padding = 5;
				columnModel.addColumn(two_shoot);	keywordMap.put(two_shoot, new String[]{"shoot_count", "shoot_score"});
				
				DualTableColumn three_shoot = new DualTableColumn("����", "three_shoot_score", "three_shoot_count", "%1-%2"); three_shoot.padding = 5;
				columnModel.addColumn(three_shoot);	keywordMap.put(three_shoot, new String[]{"three_shoot_count", "three_shoot_score"});
				
				DefaultTableColumn self_score = columnModel.addColumn("�÷�", "self_score");
				self_score.padding = 5;			keywordMap.put(self_score, new String[]{"self_score"});
				
				DualTableColumn attack_defence = new DualTableColumn("����", "attack_board", "defence_board", "%1-%2"); attack_defence.padding = 5;
				columnModel.addColumn(attack_defence);	keywordMap.put(attack_defence, new String[]{"attack_board", "defence_board"});
				
				DefaultTableColumn total_board = columnModel.addColumn("����", "total_board");
				total_board.padding = 6;	keywordMap.put(total_board, new String[]{"total_board"});
				
				DefaultTableColumn assist = columnModel.addColumn("����", "assist");
				assist.padding = 6;		keywordMap.put(assist, new String[]{"assist"});
				
				DefaultTableColumn cap = columnModel.addColumn("��ñ", "cap");
				cap.padding = 6;	keywordMap.put(cap, new String[]{"cap"});
				
				DefaultTableColumn steal = columnModel.addColumn("����", "steal");
				steal.padding = 6;	keywordMap.put(steal, new String[]{"steal"});
				
				DefaultTableColumn miss = columnModel.addColumn("ʧ��", "miss");
				miss.padding = 6;	keywordMap.put(miss, new String[]{"miss"});
				
				DefaultTableColumn foul = columnModel.addColumn("����", "foul");
				foul.padding = 6;	keywordMap.put(foul, new String[]{"foul"});
			}
		};
		
		analyticData = new ComparePlayerModel()
		{
			{
				setSectionPerPage(sectionPerPageParam);
				DefaultTableColumn season = columnModel.addColumn("����", "match_season");
				season.padding = 10;	keywordMap.put(season, new String[]{"match_season"});
				
				DefaultTableColumn portrait = columnModel.addColumn("", "player_portrait");
				portrait.padding = playerTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn player_name = columnModel.addColumn("��Ա����", "player_name");
				player_name.padding = 70;	keywordMap.put(season, new String[]{"player_name"});
				
				DefaultTableColumn team_name = columnModel.addColumn("���", "team_name_abbr");
				team_name.padding = 5;	keywordMap.put(team_name, new String[]{"team_name_abbr"});
			}
		};
		
		//XXX adding control panels.
		final DropList dataScope = new DropList(new String[]{"������", "��������"})
		{
			@Override
			protected void onSelectionChanged(int index)
			{
				isGross = (index == 0);
				shouldRedoQuery = true;
			}
		};
		dataScope.setBounds(2, 2, 80, 20);
		dataScope.setHorizontalAlignment(DropList.CENTER);
		this.add(dataScope);
		
		final DropList dataModel = new DropList(new String[]{"��������", "��������"})
		{
			@Override
			protected void onSelectionChanged(int index)
			{
				if(index == 0)
					switchToModel(basicData);
				else
					switchToModel(analyticData);
				shouldRedoQuery = true;
			}
		};
		dataModel.setBounds(84, 2, 80, 20);
		dataModel.setHorizontalAlignment(DropList.CENTER);
		this.add(dataModel);
	}
}
