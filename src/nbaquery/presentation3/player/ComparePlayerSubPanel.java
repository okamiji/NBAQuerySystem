package nbaquery.presentation3.player;

import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

import javax.swing.JPanel;

import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.DropList;
import nbaquery.presentation3.DualTableColumn;
import nbaquery.presentation3.GameTimeColumn;
import nbaquery.presentation3.PageDisplayLabel;
import nbaquery.presentation3.PresentationTableModel;
import nbaquery.presentation3.table.ColumnSelectionListener;
import nbaquery.presentation3.table.DefaultTableColumn;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.DisplayTableColumn;
import nbaquery.presentation3.table.TableSelectionListener;

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
		currentPlayerModel = model;
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
		this.playerTable.setSize(width, height - 20);
		this.playerTable.setLocation(2, 20 + 2);
		super.add(playerTable);
		
		playerTable.setRowHeight(playerTable.getHeight() / (sectionPerPageParam + 1));
		basicData = new ComparePlayerModel()
		{
			{
				setSectionPerPage(sectionPerPageParam);
				DefaultTableColumn season = columnModel.addColumn("\u8D5B\u5B63", "match_season");
				season.padding = 10;	keywordMap.put(season, new String[]{"match_season"});
				
				DefaultTableColumn portrait = columnModel.addColumn("", "player_portrait");
				portrait.padding = playerTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn player_name = columnModel.addColumn("\u7403\u5458\u540D\u79F0", "player_name");
				player_name.padding = 70;	keywordMap.put(season, new String[]{"player_name"});
				
				DefaultTableColumn logo = columnModel.addColumn("", "team_logo");
				logo.padding = playerTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn team_name = columnModel.addColumn("\u7403\u961F", "team_name_abbr");
				team_name.padding = 5;	keywordMap.put(team_name, new String[]{"team_name_abbr"});
				
				DualTableColumn attendance = new DualTableColumn("\u573A\u6B21", "first_count", "game_count", "%1-%2"); attendance.padding = 3;
				columnModel.addColumn(attendance);	keywordMap.put(attendance, new String[]{"game_count", "first_count"});
				
				GameTimeColumn game_time = new GameTimeColumn();
				columnModel.addColumn(game_time);	keywordMap.put(game_time, new String[]{"game_time"});
				
				DualTableColumn foul_shoot = new DualTableColumn("\u7F5A\u7403", "foul_shoot_score", "foul_shoot_count", "%1-%2"); foul_shoot.padding = 5;
				columnModel.addColumn(foul_shoot);	keywordMap.put(foul_shoot, new String[]{"foul_shoot_count", "foul_shoot_score"});
				
				DualTableColumn two_shoot = new DualTableColumn("\u6295\u7BEE", "shoot_score", "shoot_count", "%1-%2"); two_shoot.padding = 5;
				columnModel.addColumn(two_shoot);	keywordMap.put(two_shoot, new String[]{"shoot_count", "shoot_score"});
				
				DualTableColumn three_shoot = new DualTableColumn("\u4E09\u5206", "three_shoot_score", "three_shoot_count", "%1-%2"); three_shoot.padding = 5;
				columnModel.addColumn(three_shoot);	keywordMap.put(three_shoot, new String[]{"three_shoot_count", "three_shoot_score"});
				
				DefaultTableColumn self_score = columnModel.addColumn("\u5F97\u5206", "self_score");
				self_score.padding = 5;			keywordMap.put(self_score, new String[]{"self_score"});
				
				DualTableColumn attack_defence = new DualTableColumn("\u653B\u5B88", "attack_board", "defence_board", "%1-%2"); attack_defence.padding = 5;
				columnModel.addColumn(attack_defence);	keywordMap.put(attack_defence, new String[]{"attack_board", "defence_board"});
				
				DefaultTableColumn total_board = columnModel.addColumn("\u7BEE\u677F", "total_board");
				total_board.padding = 6;	keywordMap.put(total_board, new String[]{"total_board"});
				
				DefaultTableColumn assist = columnModel.addColumn("\u52A9\u653B", "assist");
				assist.padding = 6;		keywordMap.put(assist, new String[]{"assist"});
				
				DefaultTableColumn cap = columnModel.addColumn("\u76D6\u5E3D", "cap");
				cap.padding = 6;	keywordMap.put(cap, new String[]{"cap"});
				
				DefaultTableColumn steal = columnModel.addColumn("\u62A2\u65AD", "steal");
				steal.padding = 6;	keywordMap.put(steal, new String[]{"steal"});
				
				DefaultTableColumn miss = columnModel.addColumn("\u5931\u8BEF", "miss");
				miss.padding = 6;	keywordMap.put(miss, new String[]{"miss"});
				
				DefaultTableColumn foul = columnModel.addColumn("\u72AF\u89C4", "foul");
				foul.padding = 6;	keywordMap.put(foul, new String[]{"foul"});
			}
		};
		
		analyticData = new ComparePlayerModel()
		{
			{
				setSectionPerPage(sectionPerPageParam);
				DefaultTableColumn season = columnModel.addColumn("\u8D5B\u5B63", "match_season");
				season.padding = 10;	keywordMap.put(season, new String[]{"match_season"});
				
				DefaultTableColumn portrait = columnModel.addColumn("", "player_portrait");
				portrait.padding = playerTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn player_name = columnModel.addColumn("\u7403\u5458\u540D\u79F0", "player_name");
				player_name.padding = 70;	keywordMap.put(season, new String[]{"player_name"});
				
				DefaultTableColumn logo = columnModel.addColumn("", "team_logo");
				logo.padding = playerTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn team_name = columnModel.addColumn("\u7403\u961F", "team_name_abbr");
				team_name.padding = 5;	keywordMap.put(team_name, new String[]{"team_name_abbr"});
				
				DefaultTableColumn efficiency = columnModel.addColumn("Ч��", "efficiency");
				efficiency.padding = 2;			keywordMap.put(efficiency, new String[]{"efficiency"});
				
				DefaultTableColumn gmsc = columnModel.addColumn("GmSc", "gmsc_efficiency");
				gmsc.padding = 2;			keywordMap.put(gmsc, new String[]{"gmsc_efficiency"});
				
				DefaultTableColumn foul_shoot_rate = columnModel.addColumn("��������", "foul_shoot_rate");
				foul_shoot_rate.padding = 2;			keywordMap.put(foul_shoot_rate, new String[]{"foul_shoot_rate"});
				
				DefaultTableColumn shoot_rate = columnModel.addColumn("��������", "shoot_rate");
				shoot_rate.padding = 2;			keywordMap.put(shoot_rate, new String[]{"shoot_rate"});
				
				DefaultTableColumn three_shoot_rate = columnModel.addColumn("��������", "three_shoot_rate");
				three_shoot_rate.padding = 2;			keywordMap.put(three_shoot_rate, new String[]{"three_shoot_rate"});
				
				DefaultTableColumn true_shoot_rate = columnModel.addColumn("��ʵ����", "true_shoot_rate");
				true_shoot_rate.padding = 2;			keywordMap.put(true_shoot_rate, new String[]{"true_shoot_rate"});
				
				DefaultTableColumn shoot_efficiency = columnModel.addColumn("Ͷ����", "shoot_efficiency");
				shoot_efficiency.padding = 2;			keywordMap.put(shoot_efficiency, new String[]{"shoot_efficiency"});
				
				DefaultTableColumn assist_rate = columnModel.addColumn("������", "assist_rate");
				assist_rate.padding = 2;			keywordMap.put(assist_rate, new String[]{"assist_rate"});
				
				DefaultTableColumn steal_rate = columnModel.addColumn("������", "steal_rate");
				steal_rate.padding = 2;			keywordMap.put(steal_rate, new String[]{"steal_rate"});
				
				DefaultTableColumn cap_rate = columnModel.addColumn("��ñ��", "cap_rate");
				cap_rate.padding = 2;			keywordMap.put(cap_rate, new String[]{"cap_rate"});
				
				DefaultTableColumn miss_rate = columnModel.addColumn("ʧ����", "miss_rate");
				miss_rate.padding = 2;			keywordMap.put(miss_rate, new String[]{"miss_rate"});
				
				DefaultTableColumn usage = columnModel.addColumn("ʹ����", "usage");
				usage.padding = 2;			keywordMap.put(usage, new String[]{"usage"});
			}
		};
		
		//XXX adding control panels.
		final DropList dataScope = new DropList(new String[]{"����������", "��������"})
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
		
		final DropList matchArea = new DropList(new String[]{"��������", "��������", "��������"})
		{
			@Override
			protected void onSelectionChanged(int index)
			{
				if(index == 0) league = null;
				else if(index == 1) league = "E";
				else league = "W";
				shouldRedoQuery = true;
			}
		};
		matchArea.setBounds(166, 2, 80, 20);
		matchArea.setHorizontalAlignment(DropList.CENTER);
		this.add(matchArea);
		
		final DropList position = new DropList(new String[]{"����λ��", "ǰ��", "�з�", "����"})
		{
			@Override
			protected void onSelectionChanged(int index)
			{
				if(index == 0) ComparePlayerSubPanel.this.position = null;
				else if(index == 1) ComparePlayerSubPanel.this.position = "F";
				else if(index == 2) ComparePlayerSubPanel.this.position = "C";
				else ComparePlayerSubPanel.this.position = "G";
				shouldRedoQuery = true;
			}
		};
		position.setBounds(248, 2, 80, 20);
		position.setHorizontalAlignment(DropList.CENTER);
		this.add(position);
		
		final PageDisplayLabel page = new PageDisplayLabel(this.playerTable);
		page.setBounds(width - 82, 2, 80, 20);
		this.add(page);
		
		//XXX adding listeners.
		playerTable.addColumnSelectionListener(new ColumnSelectionListener()
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
		
		playerTable.addTableSelectionListener(new TableSelectionListener()
		{

			@Override
			public void onSelect(DisplayTable table, int row, int column,
					Object value, Point mousePoint)
			{
				if(column == 3 || column == 4) ComparePlayerSubPanel.this.detailedInfo.displayTeamInfo((Row)value, false);
				else ComparePlayerSubPanel.this.detailedInfo.displayPlayerInfo((Row)value, false);
			}
		});
		
		playerTable.addMouseWheelListener(new MouseWheelListener()
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
