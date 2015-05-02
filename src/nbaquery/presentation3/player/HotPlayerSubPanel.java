package nbaquery.presentation3.player;

import java.awt.Point;

import javax.swing.JPanel;

import nbaquery.data.Table;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.DropMenu;
import nbaquery.presentation3.PresentationTableModel;
import nbaquery.presentation3.table.ColumnSelectionListener;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.RankingTableColumn;

@SuppressWarnings("serial")
public class HotPlayerSubPanel extends JPanel
{
	NewPlayerService playerService;
	DisplayTable playerTable;
	
	HotPlayerSection currentSection;
	
	boolean shouldRedoQuery = true;
	
	HotPlayerSection todayHotPlayer = new HotPlayerSection(new String[]{"µÃ·Ö", "Àº°å", "Öú¹¥", "ÇÀ¶Ï", "¸ÇÃ±"}
	, new String[]{"self_score", "total_board", "assist", "steal", "cap"})
	{
		{
			tableModel = new PresentationTableModel()
			{
				
				{
					this.setPageIndex(0);
					this.setSectionPerPage(5);
					this.columnModel.addColumn("ÇòÔ±Ãû³Æ", "player_name").padding = 80;
					this.columnModel.addColumn(new RankingTableColumn(), 0);
				}
				
				@Override
				public void onRepaint(DisplayTable table)
				{
					if(shouldRedoQuery ||
							HotPlayerSubPanel.this.playerService.shouldRedoQuery(this))
					{
						Table resultTable = HotPlayerSubPanel.this.playerService
								.searchForTodayHotPlayers(tableColumn[selectedIndex]);
						
						this.updateTable(resultTable);
						shouldRedoQuery = false;
					}
				}
			};
		}
	};
	
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
		
		this.playerTable.addColumnSelectionListener(new ColumnSelectionListener()
		{
			@Override
			public void onSelect(DisplayTable table, int column,
					Point mousePoint)
			{
				if(column > 1)
					currentSection.theDropMenu.popupWindow(playerTable.getX() + mousePoint.x,
							playerTable.getY() + mousePoint.y);
			}
		});
		
		super.add(this.playerTable);
		
		//XXX initialize it to today hot player.
		this.switchSection(todayHotPlayer);
	}
	
	public void switchSection(HotPlayerSection section)
	{
		this.currentSection = section;
		this.playerTable.columnModel = section.tableModel;
		this.playerTable.tableModel = section.tableModel;
		section.update();
	}
	
	public class HotPlayerSection
	{
		PresentationTableModel tableModel;
		String[] tableHeader;
		String[] tableColumn;
		int selectedIndex = 1;
		private int legacy = -1;
		
		public HotPlayerSection(String[] tableHeader, String[] tableColumn)
		{
			this.tableHeader = tableHeader;
			this.tableColumn = tableColumn;
			this.initDropMenu();
		}
		
		public void update()
		{
			if(legacy != selectedIndex)
			{
				if(legacy >= 0) tableModel.columnModel.removeColumn(tableColumn[legacy]);
				legacy = selectedIndex;
				tableModel.columnModel.addColumn(tableHeader[legacy], tableColumn[legacy]);
			}
		}
		
		DropMenu theDropMenu;
		public void initDropMenu()
		{
			this.theDropMenu = new DropMenu(tableHeader)
			{
				@Override
				protected void onSelectedItem(int itemIndex)
				{
					selectedIndex = itemIndex;
					switchSection(currentSection);
					shouldRedoQuery = true;
				}
			};
		}
	}
}
