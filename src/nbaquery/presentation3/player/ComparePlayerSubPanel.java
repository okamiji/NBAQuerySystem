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
	public String[] keyword = new String[]{"player_name"};
	public boolean descend = false;
	
	public boolean shouldRedoQuery = true;
	
	public class ComparePlayerModel extends PresentationTableModel
	{
		public final HashMap<DisplayTableColumn, String[]> keywordMap = new HashMap<DisplayTableColumn, String[]>();
		
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

	public final PresentationTableModel basicData;
	public final PresentationTableModel analyticData;
	
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
				DefaultTableColumn season = columnModel.addColumn("赛季", "match_season");
				season.padding = 10;	keywordMap.put(season, new String[]{"match_season"});
				
				DefaultTableColumn portrait = columnModel.addColumn("", "player_portrait");
				portrait.padding = playerTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn player_name = columnModel.addColumn("球员名称", "player_name");
				player_name.padding = 70;	keywordMap.put(season, new String[]{"player_name"});
				
				DefaultTableColumn team_name = columnModel.addColumn("球队", "team_name_abbr");
				team_name.padding = 5;	keywordMap.put(team_name, new String[]{"team_name_abbr"});
				
				DualTableColumn attendance = new DualTableColumn("参赛", "first_count", "game_count", "%1-%2"); attendance.padding = 5;
				columnModel.addColumn(attendance);	keywordMap.put(attendance, new String[]{"game_count", "first_count"});
				
				GameTimeColumn game_time = new GameTimeColumn();
				columnModel.addColumn(game_time);	keywordMap.put(game_time, new String[]{});
				
				DualTableColumn foul_shoot = new DualTableColumn("罚球", "foul_shoot_score", "foul_shoot_count", "%1-%2"); foul_shoot.padding = 5;
				columnModel.addColumn(foul_shoot);
				
				DualTableColumn two_shoot = new DualTableColumn("投篮", "shoot_score", "shoot_count", "%1-%2"); two_shoot.padding = 5;
				columnModel.addColumn(two_shoot);
				
				DualTableColumn three_shoot = new DualTableColumn("三分", "three_shoot_score", "three_shoot_count", "%1-%2"); three_shoot.padding = 5;
				columnModel.addColumn(three_shoot);
				
				columnModel.addColumn("得分", "self_score").padding = 5;
				
				DualTableColumn attack_defence = new DualTableColumn("攻守", "attack_board", "defence_board", "%1-%2"); attack_defence.padding = 5;
				columnModel.addColumn(attack_defence);
				
				columnModel.addColumn("篮板", "total_board").padding = 6;
				columnModel.addColumn("助攻", "assist").padding = 6;
				columnModel.addColumn("盖帽", "cap").padding = 6;
				columnModel.addColumn("抢断", "steal").padding = 6;
				columnModel.addColumn("失误", "miss").padding = 6;
				columnModel.addColumn("犯规", "foul").padding = 6;
			}
		};
		
		analyticData = new ComparePlayerModel()
		{
			{
				setSectionPerPage(sectionPerPageParam);
				DefaultTableColumn season = columnModel.addColumn("赛季", "match_season");
				season.padding = 10;	keywordMap.put(season, new String[]{"match_season"});
				
				DefaultTableColumn portrait = columnModel.addColumn("", "player_portrait");
				portrait.padding = playerTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn player_name = columnModel.addColumn("球员名称", "player_name");
				player_name.padding = 70;	keywordMap.put(season, new String[]{"player_name"});
				
				DefaultTableColumn team_name = columnModel.addColumn("球队", "team_name_abbr");
				team_name.padding = 5;	keywordMap.put(team_name, new String[]{"team_name_abbr"});
				
				
			}
		};
		
		//XXX adding control panels.
		final DropList dataScope = new DropList(new String[]{"总数据", "场均数据"})
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
		
		final DropList dataModel = new DropList(new String[]{"基本数据", "分析数据"})
		{
			@Override
			protected void onSelectionChanged(int index)
			{
				if(index == 0)
				{
					playerTable.columnModel = basicData;
					playerTable.tableModel = basicData;
				}
				else
				{
					playerTable.columnModel = analyticData;
					playerTable.tableModel = analyticData;
				}
				shouldRedoQuery = true;
			}
		};
		dataModel.setBounds(84, 2, 80, 20);
		dataModel.setHorizontalAlignment(DropList.CENTER);
		this.add(dataModel);
	}
}
