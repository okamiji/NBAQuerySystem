package nbaquery.presentation3.player;

import javax.swing.JPanel;

import nbaquery.data.Table;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.PresentationTableModel;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.RankingTableColumn;

@SuppressWarnings("serial")
public class HotPlayerSubPanel extends JPanel
{
	NewPlayerService playerService;
	DisplayTable playerTable;
	
	PresentationTableModel todayHotPlayerModel;
	String todayHotPlayerSorting = "self_score";
	
	public HotPlayerSubPanel(NewPlayerService playerService, int width, int height)
	{
		//XXX layout sub panel.
		this.playerService = playerService;
		super.setLayout(null);
		super.setSize(width, height);
		
		//XXX layout the table.
		this.playerTable = new DisplayTable();
		this.playerTable.setSize((int) (0.7 * width), height);
		this.playerTable.setLocation((int) (0.3 * width), 0);
		
		this.playerTable.setRowHeight(height / 6);
		super.add(this.playerTable);
		
		todayHotPlayerModel = new PresentationTableModel()
		{
			boolean shouldRedoTodayHotplayerQuery = true;
			
			{
				this.setPageIndex(0);
				this.setSectionPerPage(5);
				
				this.columnModel.addColumn("球员名称", "player_name").padding = 80;
				this.columnModel.addColumn("个人得分", "self_score");
				this.columnModel.addColumn("助攻", "assist");
				this.columnModel.addColumn("盖帽", "cap");
				this.columnModel.addColumn("篮板", "total_board");
				
				this.columnModel.addColumn(new RankingTableColumn(), 0);
			}
			
			@Override
			public void onRepaint(DisplayTable table)
			{
				if(shouldRedoTodayHotplayerQuery ||
						HotPlayerSubPanel.this.playerService.shouldRedoQuery(this))
				{
					Table resultTable = HotPlayerSubPanel.this.playerService
							.searchForTodayHotPlayers(todayHotPlayerSorting);
					
					this.updateTable(resultTable);
					shouldRedoTodayHotplayerQuery = false;
				}
			}
		};
		
		//XXX initialize it to today hot player.
		this.switchTableModel(todayHotPlayerModel);
	}
	
	public void switchTableModel(PresentationTableModel tableModel)
	{
		this.playerTable.columnModel = tableModel;
		this.playerTable.tableModel = tableModel;
	}
}
