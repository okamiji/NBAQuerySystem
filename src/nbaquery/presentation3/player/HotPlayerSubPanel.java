package nbaquery.presentation3.player;

import java.awt.Graphics;

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
	
	PresentationTableModel currentTableModel;
	
	public HotPlayerSubPanel(NewPlayerService playerService, int width, int height)
	{
		this.playerService = playerService;
		super.setLayout(null);
		super.setSize(width, height);
		
		todayHotPlayerModel = new PresentationTableModel()
		{
			boolean shouldRedoTodayHotplayerQuery = true;
			
			{
				this.setPageIndex(0);
				this.setSectionPerPage(5);
				
				this.columnModel.addColumn("球员名称", "player_name");
				this.columnModel.addColumn("个人得分", "self_score");
				
				this.columnModel.addColumn(new RankingTableColumn(), 0);
			}
			
			@Override
			public void onRepaint(DisplayTable table)
			{
				if(shouldRedoTodayHotplayerQuery ||
						HotPlayerSubPanel.this.playerService.shouldRedoQuery(HotPlayerSubPanel.this))
				{
					Table resultTable = HotPlayerSubPanel.this.playerService
							.searchForTodayHotPlayers(todayHotPlayerSorting);
					this.updateTable(resultTable);
					shouldRedoTodayHotplayerQuery = false;
				}
			}
		};
	}
	
	public void paint(Graphics g)
	{
		
		super.paint(g);
	}
}
