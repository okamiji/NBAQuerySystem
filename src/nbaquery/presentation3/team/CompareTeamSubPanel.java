package nbaquery.presentation3.team;

import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

import javax.swing.JPanel;

import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.logic.team.NewTeamService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.DropList;
import nbaquery.presentation3.DualTableColumn;
import nbaquery.presentation3.PresentationTableModel;
import nbaquery.presentation3.table.ColumnSelectionListener;
import nbaquery.presentation3.table.DefaultTableColumn;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.DisplayTableColumn;
import nbaquery.presentation3.table.TableSelectionListener;

@SuppressWarnings("serial")
public class CompareTeamSubPanel extends JPanel
{
	NewTeamService teamService;
	DetailedInfoContainer detailedInfo;
	public final DisplayTable teamTable;
	
	public boolean isGross = true;
	public String league = null;
	public String position = null;
	public boolean descend = false;
	
	public boolean shouldRedoQuery = true;
	
	protected CompareTeamModel currentPlayerModel;
	public void switchToModel(CompareTeamModel model)
	{
		currentPlayerModel = model;
		teamTable.columnModel = model;
		teamTable.tableModel = model;
	}
	
	public class CompareTeamModel extends PresentationTableModel
	{
		public final HashMap<DisplayTableColumn, String[]> keywordMap = new HashMap<DisplayTableColumn, String[]>();
		public String[] keyword = new String[]{"team_name"};
		
		@Override
		public void onRepaint(DisplayTable table)
		{
			if(CompareTeamSubPanel.this.teamService.shouldRedoQuery(this) || shouldRedoQuery)
			{
				Table resultTable = CompareTeamSubPanel.this.teamService
					.searchForTeams(isGross, keyword, descend);
				
				this.updateTable(resultTable);
				shouldRedoQuery = false;
			}
		}
	}

	public final CompareTeamModel basicData;
	public final CompareTeamModel analyticData;
	
	public CompareTeamSubPanel(NewTeamService playerService, DetailedInfoContainer detailedInfo, int width, int height, final int sectionPerPageParam)
	{
		this.teamService = playerService;
		this.detailedInfo = detailedInfo;
		
		this.setSize(width, height);
		this.setLayout(null);
		
		this.teamTable = new DisplayTable();
		this.teamTable.setSize(width, height - 20);
		this.teamTable.setLocation(2, 20 + 2);
		super.add(teamTable);
		
		teamTable.setRowHeight(teamTable.getHeight() / (sectionPerPageParam + 1));
		basicData = new CompareTeamModel()
		{
			{
				setSectionPerPage(sectionPerPageParam);
				DefaultTableColumn season = columnModel.addColumn("赛季", "match_season");
				season.padding = 10;	keywordMap.put(season, new String[]{"match_season"});
				
				DefaultTableColumn logo = columnModel.addColumn("", "team_logo");
				logo.padding = teamTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn team_name = columnModel.addColumn("球队名称", "team_name");
				team_name.padding = 40;	keywordMap.put(team_name, new String[]{"team_name"});
				
				DefaultTableColumn team_name_abbr = columnModel.addColumn("缩写", "team_name_abbr");
				team_name_abbr.padding = 10;	keywordMap.put(team_name_abbr, new String[]{"team_name_abbr"});
				
				DualTableColumn attendance = new DualTableColumn("参赛", "win", "game", "%1-%2"); attendance.padding = 15;
				columnModel.addColumn(attendance);	keywordMap.put(attendance, new String[]{"game", "win"});
				
				DualTableColumn foul_shoot = new DualTableColumn("罚球", "foul_shoot_score", "foul_shoot", "%1-%2"); foul_shoot.padding = 10;
				columnModel.addColumn(foul_shoot);	keywordMap.put(foul_shoot, new String[]{"foul_shoot", "foul_shoot_score"});
				
				DualTableColumn two_shoot = new DualTableColumn("投篮", "shoot_score", "shoot", "%1-%2"); two_shoot.padding = 10;
				columnModel.addColumn(two_shoot);	keywordMap.put(two_shoot, new String[]{"shoot", "shoot_score"});
				
				DualTableColumn three_shoot = new DualTableColumn("三分", "three_shoot_score", "three_shoot", "%1-%2"); three_shoot.padding = 10;
				columnModel.addColumn(three_shoot);	keywordMap.put(three_shoot, new String[]{"three_shoot", "three_shoot_score"});
				
				DefaultTableColumn self_score = columnModel.addColumn("得分", "self_score");
				self_score.padding = 10;			keywordMap.put(self_score, new String[]{"self_score"});
				
				DualTableColumn attack_defence = new DualTableColumn("攻守", "attack_board", "defence_board", "%1-%2"); attack_defence.padding = 15;
				columnModel.addColumn(attack_defence);	keywordMap.put(attack_defence, new String[]{"attack_board", "defence_board"});
				
				DefaultTableColumn total_board = columnModel.addColumn("篮板", "total_board");
				total_board.padding = 10;	keywordMap.put(total_board, new String[]{"total_board"});
				
				DefaultTableColumn assist = columnModel.addColumn("助攻", "assist");
				assist.padding = 10;		keywordMap.put(assist, new String[]{"assist"});
				
				DefaultTableColumn cap = columnModel.addColumn("盖帽", "cap");
				cap.padding = 10;	keywordMap.put(cap, new String[]{"cap"});
				
				DefaultTableColumn steal = columnModel.addColumn("抢断", "steal");
				steal.padding = 10;	keywordMap.put(steal, new String[]{"steal"});
				
				DefaultTableColumn miss = columnModel.addColumn("失误", "miss");
				miss.padding = 10;	keywordMap.put(miss, new String[]{"miss"});
				
				DefaultTableColumn foul = columnModel.addColumn("犯规", "foul");
				foul.padding = 10;	keywordMap.put(foul, new String[]{"foul"});
			}
		};
		
		analyticData = new CompareTeamModel()
		{
			{
				setSectionPerPage(sectionPerPageParam);
				DefaultTableColumn season = columnModel.addColumn("赛季", "match_season");
				season.padding = 10;	keywordMap.put(season, new String[]{"match_season"});
				
				DefaultTableColumn logo = columnModel.addColumn("", "team_logo");
				logo.padding = teamTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn team_name = columnModel.addColumn("球队名称", "team_name");
				team_name.padding = 40;	keywordMap.put(team_name, new String[]{"team_name"});
				
				DefaultTableColumn team_name_abbr = columnModel.addColumn("缩写", "team_name_abbr");
				team_name_abbr.padding = 10;	keywordMap.put(team_name_abbr, new String[]{"team_name_abbr"});
			}
		};
		
		//XXX adding control panels.
		final DropList dataScope = new DropList(new String[]{"赛季总数据", "场均数据"})
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
					switchToModel(basicData);
				else
					switchToModel(analyticData);
				shouldRedoQuery = true;
			}
		};
		dataModel.setBounds(84, 2, 80, 20);
		dataModel.setHorizontalAlignment(DropList.CENTER);
		this.add(dataModel);
		
		//XXX adding listeners.
		teamTable.addColumnSelectionListener(new ColumnSelectionListener()
		{
			int legacyColumn = 2;
			@Override
			public void onSelect(DisplayTable table, int column,
					Point mousePoint)
			{
				currentPlayerModel.keyword = currentPlayerModel.keywordMap
						.get(currentPlayerModel.columnModel.getColumn(column));
				
				if(legacyColumn == column) descend = !descend;
				legacyColumn = column;
				
				shouldRedoQuery = true;
			}
		});
		
		teamTable.addTableSelectionListener(new TableSelectionListener()
		{

			@Override
			public void onSelect(DisplayTable table, int row, int column,
					Object value, Point mousePoint)
			{
				CompareTeamSubPanel.this.detailedInfo.displayTeamInfo((Row)value, false);
			}
		});
		
		teamTable.addMouseWheelListener(new MouseWheelListener()
		{
			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0)
			{
				currentPlayerModel.setPageIndex(currentPlayerModel.getPageIndex() + 
						arg0.getUnitsToScroll() / arg0.getScrollAmount());
			}
		});
	}
}