package nbaquery.presentation3.player;

import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nbaquery.data.Image;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation.resource.ImageIconResource;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.DisplayButton;
import nbaquery.presentation3.DropMenu;
import nbaquery.presentation3.PresentationTableModel;
import nbaquery.presentation3.table.ColumnSelectionListener;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.RankingTableColumn;
import nbaquery.presentation3.table.TableSelectionListener;

@SuppressWarnings("serial")
public class HotPlayerSubPanel extends JPanel
{
	NewPlayerService playerService;
	DisplayTable playerTable;
	DetailedInfoContainer infoContainer;
	
	HotPlayerSection currentSection;
	
	boolean shouldRedoQuery = true;
	
	HotPlayerSection todayHotPlayer = new HotPlayerSection(new String[]{"µÃ·Ö", "Àº°å", "Öú¹¥", "ÇÀ¶Ï", "¸ÇÃ±"}
	, new String[]{"self_score", "total_board", "assist", "steal", "cap"})
	{
		{
			prefix = "½ñÈÕ";
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
						setHeadDisplay();
					}
				}
			};
		}
	};
	
	HotPlayerSection seasonHotPlayer = new HotPlayerSection(new String[]{"µÃ·Ö", "Àº°å", "Öú¹¥", "ÇÀ¶Ï", "¸ÇÃ±"}
	, new String[]{"self_score", "total_board", "assist", "steal", "cap"})
	{
		{
			prefix = "Èü¼¾";
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
						setHeadDisplay();
					}
				}
			};
		}
	};
	
	HotPlayerSection progressPlayer = new HotPlayerSection(new String[]{"µÃ·ÖÌáÉý", "Àº°åÌáÉý", "Öú¹¥ÌáÉý"}
	, new String[]{"self_score_rate", "total_board_rate", "assist_rate"})
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
								.searchForProgressPlayers(tableColumn[selectedIndex]);
						
						this.updateTable(resultTable);
						shouldRedoQuery = false;
						setHeadDisplay();
					}
				}
			};
		}
	};
	
	public final JLabel imageDisplay = new JLabel();
	public final JLabel description = new JLabel();
	public final JLabel playerName = new JLabel();
	
	public void setHeadDisplay()
	{
		if(playerTable == null || playerTable.tableModel == null) return;
		description.setText(this.currentSection.getDescriptionText());
		if(playerTable.tableModel.getRowCount() > 0)
		{
			Row row = (Row) playerTable.tableModel.getValueAt(playerTable, 0, 0);
			if(row != null)
			{
				Image icon = (Image) row.getDeclaredTable().getColumn("player_portrait").getAttribute(row);
				imageDisplay.setIcon(new ImageIcon(
						ImageIconResource.getImageIcon(icon.toString()).getImage()
							.getScaledInstance(imageDisplay.getWidth(), imageDisplay.getHeight(), java.awt.Image.SCALE_SMOOTH)
						));
				playerName.setText((String) row.getDeclaredTable().getColumn("player_name").getAttribute(row));
			}
			return;
		}
		imageDisplay.setIcon(null);
		playerName.setText("");
	}
	
	public HotPlayerSubPanel(NewPlayerService playerService, DetailedInfoContainer infoContainer, int width, int height)
	{
		//XXX layout sub panel.
		this.playerService = playerService;
		this.infoContainer = infoContainer;
		super.setLayout(null);
		super.setSize(width, height);
		
		//XXX layout the table.
		this.playerTable = new DisplayTable();
		this.playerTable.setSize((int) (0.60 * width), height);
		this.playerTable.setLocation((int) (0.30 * width), 0);
		
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
		progressPlayer.tableSwitch
			.setBounds((int)(0.90 * width), (int)(5.0/6 * height), (int)(0.1 * width), (int)(0.1 * width));
		super.add(todayHotPlayer.tableSwitch);
		super.add(seasonHotPlayer.tableSwitch);
		super.add(progressPlayer.tableSwitch);
		
		//XXX adding player showcase
		imageDisplay.setBounds(5, (int)(0.1 * height + 5), (int)((0.3 * width) - 10), (int)(0.5 * height - 5));
		description.setBounds(5, (int)(0.5 * height + 10), (int)((0.3 * width) - 10), (int)(0.25 * height));
		description.setHorizontalAlignment(JLabel.CENTER);
		playerName.setBounds(5, (int)(0.65 * height), (int)((0.3 * width) - 10), (int)(0.25 * height));
		playerName.setHorizontalAlignment(JLabel.CENTER);
		super.add(imageDisplay);
		super.add(description);
		super.add(playerName);
		
		//XXX adding detailed info container reaction.
		playerTable.addTableSelectionListener(new TableSelectionListener()
		{

			@Override
			public void onSelect(DisplayTable table, int row, int column,
					Object value, Point mousePoint)
			{
				if(value == null) return;
				Row rowObject = (Row)value;
				HotPlayerSubPanel.this.infoContainer.displayPlayerInfo(rowObject, false);
			}
			
		});
		
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
		this.setHeadDisplay();
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
		
		protected String prefix = "";
		public String getDescriptionText()
		{
			return prefix.concat(tableHeader[selectedIndex]).concat("Íõ");
		}
	}
}
