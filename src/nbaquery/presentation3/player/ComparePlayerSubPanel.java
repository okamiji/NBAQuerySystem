package nbaquery.presentation3.player;

import javax.swing.JPanel;

import nbaquery.data.Table;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.DualTableColumn;
import nbaquery.presentation3.GameTimeColumn;
import nbaquery.presentation3.PresentationTableModel;
import nbaquery.presentation3.table.DisplayTable;

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
	
	public ComparePlayerSubPanel(NewPlayerService playerService, DetailedInfoContainer detailedInfo, int width, int height, int sectionPerPage)
	{
		this.playerService = playerService;
		this.detailedInfo = detailedInfo;
		
		this.setSize(width, height);
		this.setLayout(null);
		
		this.playerTable = new DisplayTable();
		this.playerTable.setSize(width, height - 20);
		this.playerTable.setLocation(0, 0);
		super.add(playerTable);
		
		PresentationTableModel model = new PresentationTableModel()
		{
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
		};
		model.setSectionPerPage(sectionPerPage);
		playerTable.setRowHeight(playerTable.getHeight() / (sectionPerPage + 1));
		
		this.playerTable.tableModel = model;
		this.playerTable.columnModel = model;
		
		model.columnModel.addColumn("赛季", "match_season").padding = 10;
		model.columnModel.addColumn("", "player_portrait").padding = playerTable.getHeight() / (sectionPerPage + 1);
		model.columnModel.addColumn("球员名称", "player_name").padding = 70;
		model.columnModel.addColumn("球队", "team_name_abbr");
		
		GameTimeColumn game_time = new GameTimeColumn();
		model.columnModel.addColumn(game_time);
		
		DualTableColumn foul_shoot = new DualTableColumn("罚球", "foul_shoot_score", "foul_shoot_count", "%1-%2"); foul_shoot.padding = 10;
		model.columnModel.addColumn(foul_shoot);
		
		DualTableColumn two_shoot = new DualTableColumn("投篮", "shoot_score", "shoot_count", "%1-%2"); two_shoot.padding = 10;
		model.columnModel.addColumn(two_shoot);
		
		DualTableColumn three_shoot = new DualTableColumn("三分", "three_shoot_score", "three_shoot_count", "%1-%2"); three_shoot.padding = 10;
		model.columnModel.addColumn(three_shoot);
	}
}
