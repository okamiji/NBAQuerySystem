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
import nbaquery.presentation3.PageDisplayLabel;
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
				DefaultTableColumn season = columnModel.addColumn("\u8D5B\u5B63", "match_season");
				season.padding = 10;	keywordMap.put(season, new String[]{"match_season"});
				
				DefaultTableColumn logo = columnModel.addColumn("", "team_logo");
				logo.padding = teamTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn team_name = columnModel.addColumn("\u7403\u961F\u540D\u79F0", "team_name");
				team_name.padding = 40;	keywordMap.put(team_name, new String[]{"team_name"});
				
				DefaultTableColumn team_name_abbr = columnModel.addColumn("\u7F29\u5199", "team_name_abbr");
				team_name_abbr.padding = 10;	keywordMap.put(team_name_abbr, new String[]{"team_name_abbr"});
				
				DualTableColumn attendance = new DualTableColumn("\u80DC\u573A", "win", "game", "%1-%2"); attendance.padding = 15;
				columnModel.addColumn(attendance);	keywordMap.put(attendance, new String[]{"game", "win"});
				
				DualTableColumn foul_shoot = new DualTableColumn("\u7F5A\u7403", "foul_shoot_score", "foul_shoot", "%1-%2"); foul_shoot.padding = 10;
				columnModel.addColumn(foul_shoot);	keywordMap.put(foul_shoot, new String[]{"foul_shoot", "foul_shoot_score"});
				
				DualTableColumn two_shoot = new DualTableColumn("\u4E8C\u5206", "shoot_score", "shoot", "%1-%2"); two_shoot.padding = 10;
				columnModel.addColumn(two_shoot);	keywordMap.put(two_shoot, new String[]{"shoot", "shoot_score"});
				
				DualTableColumn three_shoot = new DualTableColumn("\u4E09\u5206", "three_shoot_score", "three_shoot", "%1-%2"); three_shoot.padding = 10;
				columnModel.addColumn(three_shoot);	keywordMap.put(three_shoot, new String[]{"three_shoot", "three_shoot_score"});
				
				DefaultTableColumn self_score = columnModel.addColumn("\u5F97\u5206", "self_score");
				self_score.padding = 10;			keywordMap.put(self_score, new String[]{"self_score"});
				
				DualTableColumn attack_defence = new DualTableColumn("\u653B\u5B88", "attack_board", "defence_board", "%1-%2"); attack_defence.padding = 15;
				columnModel.addColumn(attack_defence);	keywordMap.put(attack_defence, new String[]{"attack_board", "defence_board"});
				
				DefaultTableColumn total_board = columnModel.addColumn("\u7BEE\u677F", "total_board");
				total_board.padding = 10;	keywordMap.put(total_board, new String[]{"total_board"});
				
				DefaultTableColumn assist = columnModel.addColumn("\u52A9\u653B", "assist");
				assist.padding = 10;		keywordMap.put(assist, new String[]{"assist"});
				
				DefaultTableColumn cap = columnModel.addColumn("\u76D6\u5E3D", "cap");
				cap.padding = 10;	keywordMap.put(cap, new String[]{"cap"});
				
				DefaultTableColumn steal = columnModel.addColumn("\u62A2\u65AD", "steal");
				steal.padding = 10;	keywordMap.put(steal, new String[]{"steal"});
				
				DefaultTableColumn miss = columnModel.addColumn("\u5931\u8BEF", "miss");
				miss.padding = 10;	keywordMap.put(miss, new String[]{"miss"});
				
				DefaultTableColumn foul = columnModel.addColumn("\u72AF\u89C4", "foul");
				foul.padding = 10;	keywordMap.put(foul, new String[]{"foul"});
			}
		};
		
		analyticData = new CompareTeamModel()
		{
			{
				setSectionPerPage(sectionPerPageParam);
				DefaultTableColumn season = columnModel.addColumn("\u8D5B\u5B63", "match_season");
				season.padding = 10;	keywordMap.put(season, new String[]{"match_season"});
				
				DefaultTableColumn logo = columnModel.addColumn("", "team_logo");
				logo.padding = teamTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn team_name = columnModel.addColumn("\u7403\u961F\u540D\u79F0", "team_name");
				team_name.padding = 40;	keywordMap.put(team_name, new String[]{"team_name"});
				
				DefaultTableColumn win_rate = columnModel.addColumn("\u80DC\u7387", "win_rate");
				win_rate.padding = 5;	keywordMap.put(win_rate, new String[]{"win_rate"});
				
				DefaultTableColumn assist_efficiency = columnModel.addColumn("\u52A9\u653B\u6548\u7387", "assist_efficiency");
				assist_efficiency.padding = 0;	keywordMap.put(assist_efficiency, new String[]{"assist_efficiency"});
				
				DefaultTableColumn steal_efficiency = columnModel.addColumn("\u62A2\u65AD\u6548\u7387", "steal_efficiency");
				steal_efficiency.padding = 0;	keywordMap.put(steal_efficiency, new String[]{"steal_efficiency"});
				
				DefaultTableColumn attack_board_efficiency = columnModel.addColumn("\u8FDB\u653B\u7BEE\u677F\u7387", "attack_board_efficiency");
				attack_board_efficiency.padding = 0;	keywordMap.put(attack_board_efficiency, new String[]{"attack_board_efficiency"});
				
				DefaultTableColumn defence_board_efficiency = columnModel.addColumn("\u9632\u5B88\u7BEE\u677F\u7387", "defence_board_efficiency");
				defence_board_efficiency.padding = 0;	keywordMap.put(defence_board_efficiency, new String[]{"defence_board_efficiency"});
				
				DefaultTableColumn attack_efficiency = columnModel.addColumn("\u8FDB\u653B\u6548\u7387", "attack_efficiency");
				attack_efficiency.padding = 0;	keywordMap.put(attack_efficiency, new String[]{"attack_efficiency"});

				DefaultTableColumn defence_efficiency = columnModel.addColumn("\u9632\u5B88\u6548\u7387", "defence_efficiency");
				defence_efficiency.padding = 0;	keywordMap.put(defence_efficiency, new String[]{"defence_efficiency"});
				
				DefaultTableColumn attack_round = columnModel.addColumn("\u8FDB\u653B\u56DE\u5408", "attack_round");
				attack_round.padding = 0;	keywordMap.put(attack_round, new String[]{"attack_round"});
				
				DefaultTableColumn defence_round = columnModel.addColumn("\u9632\u5B88\u56DE\u5408", "defence_round");
				defence_round.padding = 0;	keywordMap.put(defence_round, new String[]{"defence_round"});
				
				DefaultTableColumn foul_shoot_rate = columnModel.addColumn("\u7F5A\u7403\u547D\u4E2D", "foul_shoot_rate");
				foul_shoot_rate.padding = 0;	keywordMap.put(foul_shoot_rate, new String[]{"foul_shoot_rate"});
				
				DefaultTableColumn shoot_rate = columnModel.addColumn("\u4E8C\u5206\u547D\u4E2D", "shoot_rate");
				shoot_rate.padding = 0;	keywordMap.put(shoot_rate, new String[]{"shoot_rate"});
				
				DefaultTableColumn three_shoot_rate = columnModel.addColumn("\u4E09\u5206\u547D\u4E2D", "three_shoot_rate");
				three_shoot_rate.padding = 0;	keywordMap.put(three_shoot_rate, new String[]{"three_shoot_rate"});

			}
		};
		
		//XXX adding control panels.
		final DropList dataScope = new DropList(new String[]{"\u603B\u6570\u636E", "\u573A\u5747\u6570\u636E"})
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
		
		final DropList dataModel = new DropList(new String[]{"\u57FA\u672C\u6570\u636E", "\u5206\u6790\u6570\u636E"})
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
		
		final PageDisplayLabel page = new PageDisplayLabel(this.teamTable);
		page.setBounds(width - 82, 2, 80, 20);
		this.add(page);
		
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