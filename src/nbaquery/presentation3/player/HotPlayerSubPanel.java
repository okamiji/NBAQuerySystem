package nbaquery.presentation3.player;

import java.awt.Point;

import javax.swing.JPanel;

import nbaquery.data.Table;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.DisplayButton;
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
	
	HotPlayerSection seasonHotPlayer = new HotPlayerSection(new String[]{"µÃ·Ö", "Àº°å", "Öú¹¥", "ÇÀ¶Ï", "¸ÇÃ±"}
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
								.searchForSeasonHotPlayers(tableColumn[selectedIndex]);
						
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
		this.playerTable.setSize((int) (0.55 * width), height);
		this.playerTable.setLocation((int) (0.35 * width), 0);
		
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
		
		//XXX adding sidebar buttons.
		todayHotPlayer.tableSwitch
			.setBounds((int)(0.90 * width), (int)(1.0/6 * height), (int)(0.1 * width), (int)(0.1 * width));
		seasonHotPlayer.tableSwitch
			.setBounds((int)(0.90 * width), (int)(1.0/2 * height), (int)(0.1 * width), (int)(0.1 * width));
		super.add(todayHotPlayer.tableSwitch);
		super.add(seasonHotPlayer.tableSwitch);
		
		//XXX initialize it to today hot player.
		this.switchSection(todayHotPlayer);
	}
	
	public void switchSection(HotPlayerSection section)
	{
		if(this.currentSection != null)
			this.currentSection.tableSwitch.setEnabled(true);
		this.currentSection = section;
		this.currentSection.tableSwitch.setEnabled(false);
		this.playerTable.columnModel = section.tableModel;
		this.playerTable.tableModel = section.tableModel;
		section.update();
	}
	
	public class HotPlayerSection
	{
		public static final String idleSwitch = "img3/switch_idle.png";
		public static final String hangingSwitch = "img3/switch_hanging.png";
		public static final String pressedSwitch = "img3/switch_pressed.png";
		
		PresentationTableModel tableModel;
		String[] tableHeader;
		String[] tableColumn;
		int selectedIndex = 0;
		private int legacy = -1;
		DisplayButton tableSwitch = new DisplayButton(idleSwitch, hangingSwitch, pressedSwitch)
		{
			@Override
			protected void activate()
			{
				switchSection(HotPlayerSection.this);
			}
		};
		
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
