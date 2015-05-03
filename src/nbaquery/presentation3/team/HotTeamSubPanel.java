package nbaquery.presentation3.team;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;

import nbaquery.data.Image;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.logic.team.NewTeamService;
import nbaquery.presentation.resource.JSVGComponentResource;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.DisplayButton;
import nbaquery.presentation3.DropMenu;
import nbaquery.presentation3.PresentationTableModel;
import nbaquery.presentation3.table.ColumnSelectionListener;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.RankingTableColumn;
import nbaquery.presentation3.table.TableSelectionListener;

@SuppressWarnings("serial")
public class HotTeamSubPanel extends JPanel
{
	NewTeamService teamService;
	DisplayTable teamTable;
	DetailedInfoContainer infoContainer;
	
	HotTeamSection currentSection;
	
	boolean shouldRedoQuery = true;
	
	HotTeamSection seasonHotTeam = new HotTeamSection(new String[]{"µÃ·Ö", "Àº°å", "½ø¹¥", "·ÀÊØ", "Öú¹¥", "ÇÀ¶Ï", "¸ÇÃ±"}
	, new String[]{"self_score", "total_board", "attack_board", "defence_board", "assist", "steal", "cap"})
	{
		{
			prefix = "Èü¼¾";
			tableModel = new PresentationTableModel()
			{
				{
					this.setPageIndex(0);
					this.setSectionPerPage(5);
					this.columnModel.addColumn("Çò¶ÓÃû³Æ", "team_name").padding = 80;
					this.columnModel.addColumn(new RankingTableColumn(), 0);
				}
				
				@Override
				public void onRepaint(DisplayTable table)
				{
					if(shouldRedoQuery ||
							HotTeamSubPanel.this.teamService.shouldRedoQuery(this))
					{
						Table resultTable = HotTeamSubPanel.this.teamService
								.searchSeasonHotTeams(tableColumn[selectedIndex]);
						
						this.updateTable(resultTable);
						shouldRedoQuery = false;
						setHeadDisplay();
					}
				}
			};
		}
	};
	
	public class JSVGDisplay extends Component
	{
		public Component jsvgComponent;
		
		public void paint(Graphics g)
		{
			g.clearRect(0, 0, this.getWidth(), this.getHeight());
			if(jsvgComponent != null)
			{
				jsvgComponent.setBounds(0, 0, this.getWidth(), this.getHeight());
				jsvgComponent.paint(g.create(0, 0, this.getWidth(), this.getHeight()));
			}
		}
	};
	
	public final JSVGDisplay imageDisplay = new JSVGDisplay();
	public final JLabel description = new JLabel();
	public final JLabel teamName = new JLabel();
	
	public void setHeadDisplay()
	{
		if(teamTable == null || teamTable.tableModel == null) return;
		description.setText(this.currentSection.getDescriptionText());
		if(teamTable.tableModel.getRowCount() > 0)
		{
			Row row = (Row) teamTable.tableModel.getValueAt(teamTable, 0, 0);
			if(row != null)
			{
				Image icon = (Image) row.getDeclaredTable().getColumn("team_logo").getAttribute(row);
				Component displayComponent = JSVGComponentResource.createJSVGComponent(icon.toString());
				imageDisplay.jsvgComponent = displayComponent;
				teamName.setText((String) row.getDeclaredTable().getColumn("team_name").getAttribute(row));
			}
			return;
		}
		teamName.setText("");
	}
	
	public HotTeamSubPanel(NewTeamService teamService, DetailedInfoContainer infoContainer, int width, int height)
	{
		//XXX layout sub panel.
		this.teamService = teamService;
		this.infoContainer = infoContainer;
		super.setLayout(null);
		super.setSize(width, height);
		
		//XXX layout the table.
		this.teamTable = new DisplayTable();
		this.teamTable.setSize((int) (0.60 * width), height);
		this.teamTable.setLocation((int) (0.30 * width), 0);
		
		this.teamTable.setRowHeight(height / 6);
		
		this.teamTable.addColumnSelectionListener(new ColumnSelectionListener()
		{
			@Override
			public void onSelect(DisplayTable table, int column,
					Point mousePoint)
			{
				if(column > 1)
					currentSection.theDropMenu.popupWindow(teamTable.getX() + mousePoint.x,
							teamTable.getY() + mousePoint.y);
			}
		});
		
		super.add(this.teamTable);
		
		//XXX adding player showcase
		imageDisplay.setBounds(5, (int)(0.1 * height + 5), (int)((0.3 * width) - 10), (int)(0.5 * height - 5));
		description.setBounds(5, (int)(0.5 * height + 10), (int)((0.3 * width) - 10), (int)(0.25 * height));
		description.setHorizontalAlignment(JLabel.CENTER);
		teamName.setBounds(5, (int)(0.65 * height), (int)((0.3 * width) - 10), (int)(0.25 * height));
		teamName.setHorizontalAlignment(JLabel.CENTER);
		super.add(imageDisplay);
		super.add(description);
		super.add(teamName);
		
		//XXX adding detailed info container reaction.
		teamTable.addTableSelectionListener(new TableSelectionListener()
		{

			@Override
			public void onSelect(DisplayTable table, int row, int column,
					Object value, Point mousePoint)
			{
				if(value == null) return;
				Row rowObject = (Row)value;
				HotTeamSubPanel.this.infoContainer.displayTeamInfo(rowObject, false);
			}
			
		});
		
		//XXX initialize it to season hot team.
		this.switchSection(seasonHotTeam);
	}
	
	public void switchSection(HotTeamSection section)
	{
		if(this.currentSection != null)
			this.currentSection.tableSwitch.setEnabled(true);
			
		this.currentSection = section;
		this.currentSection.tableSwitch.setEnabled(false);
		this.teamTable.columnModel = section.tableModel;
		this.teamTable.tableModel = section.tableModel;
		section.update();
		this.setHeadDisplay();
	}
	
	public class HotTeamSection
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
				switchSection(HotTeamSection.this);
			}
		};
		
		public HotTeamSection(String[] tableHeader, String[] tableColumn)
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
