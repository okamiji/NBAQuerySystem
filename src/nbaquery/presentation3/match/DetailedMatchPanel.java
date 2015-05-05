package nbaquery.presentation3.match;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JPanel;

import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.DualTableColumn;
import nbaquery.presentation3.PresentationTableModel;
import nbaquery.presentation3.table.ColumnSelectionListener;
import nbaquery.presentation3.table.DefaultTableColumn;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.DisplayTableColumn;

@SuppressWarnings("serial")
public class DetailedMatchPanel extends JPanel
{
	MatchComponent matchComponent;
	
	static int logoArea = 100;
	static int quarterScoreArea = 100;
	
	public final DisplayTable quarter, performance;
	public final DetailedInfoContainer container;
	public final NewMatchService matchService;
	
	String[] header = new String[]{"team_name_abbr"};
	int legacyIndex = 1;
	boolean descend = true;
	boolean shouldRedoQuery = true;
	HashMap<DisplayTableColumn, String[]> keywords = new HashMap<DisplayTableColumn, String[]>();
	
	public DetailedMatchPanel(DetailedInfoContainer container, NewMatchService matchService, int width, int height)
	{
		this.container = container;
		this.matchService = matchService;
		
		super.setSize(width, height);
		super.setLayout(null);
		
		//XXX allocate tables.
		quarter = new DisplayTable();
		quarter.setSize(width - 4, quarterScoreArea);
		quarter.setLocation(2, logoArea + 4);
		this.add(quarter);
		
		performance = new DisplayTable();
		performance.setSize(width - 4, height - 8 - quarterScoreArea - logoArea);
		performance.setLocation(2, logoArea + quarterScoreArea + 6);
		performance.addColumnSelectionListener(new ColumnSelectionListener()
		{
			@Override
			public void onSelect(DisplayTable table, int column,
					Point mousePoint)
			{
				String[] newHeader = keywords.get(table.columnModel.getColumn(column));
				if(newHeader == null) return;
				if(legacyIndex == column) descend = !descend;
				else
				{
					header = newHeader;
					legacyIndex = column;
				}
				shouldRedoQuery = true;
			}
		});
		performance.setRowHeight(18);
		this.add(performance);
	}
	
	public void setRow(final Row row)
	{
		if(this.matchComponent != null) this.remove(matchComponent);
		this.matchComponent = new MatchComponent(container, row, false);
		this.matchComponent.shouldDisplayTime = true;
		this.matchComponent.setSize(this.getWidth() - 2, logoArea);
		this.matchComponent.setLocation(2, 2);
		MouseListener[] listeners = this.matchComponent.getListeners(MouseListener.class);
		if(listeners.length > 0) this.matchComponent.removeMouseListener(listeners[0]);
		this.matchComponent.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent me)
			{
				if(me.getPoint().x < matchComponent.getWidth() / 2)
				{
					String hostName = (String) row.getDeclaredTable().getColumn("match_host_abbr").getAttribute(row);
					container.displayTeamInfo(hostName, true, true);
				}
				else
				{
					String guestName = (String) row.getDeclaredTable().getColumn("match_guest_abbr").getAttribute(row);
					container.displayTeamInfo(guestName, true, true);
				}
			}
		});
		this.add(matchComponent);
		
		PresentationTableModel quarterModel = new PresentationTableModel()
		{
			{
				this.columnModel.addColumn(new DefaultTableColumn("场次", "quarter_number")
				{
					public int getWidth(Graphics g)
					{
						return 20;
					}
				});
				
				this.columnModel.addColumn(new DefaultTableColumn("主场队伍得分", "quarter_host_score")
				{
					public int getWidth(Graphics g)
					{
						return (DetailedMatchPanel.this.getWidth() - 20)/2 - 1;
					}
				});
				
				this.columnModel.addColumn(new DefaultTableColumn("客场队伍得分", "quarter_guest_score")
				{
					public int getWidth(Graphics g)
					{
						return (DetailedMatchPanel.this.getWidth() - 20)/2 - 1;
					}
				});
			}
			
			boolean notGotten = true;
			
			@Override
			public void onRepaint(DisplayTable table)
			{
				if(notGotten)
				{
					int matchId = (int) row.getDeclaredTable().getColumn("match_id").getAttribute(row);
					this.updateTable(matchService.searchQuarterScoreByID(matchId));
					notGotten = false;
				}
			}
		};
		this.quarter.tableModel = quarterModel;
		this.quarter.columnModel = quarterModel;
		
		PresentationTableModel performanceModel = new PresentationTableModel()
		{
			int matchId;
			{
				matchId = (int) row.getDeclaredTable().getColumn("match_id").getAttribute(row);
				keywords.clear();
				
				columnModel.addColumn("", "team_logo").padding = 20;
				
				
				DefaultTableColumn column = columnModel.addColumn("球队", "team_name_abbr");
				column.padding = 5;
				keywords.put(column, new String[]{"team_name_abbr"});
				
				columnModel.addColumn("", "player_portrait").padding = 20;
				column = columnModel.addColumn("球员名称", "player_name");
				column.padding = 40;
				keywords.put(column, new String[]{"player_name"});
				
				DualTableColumn dual = new DualTableColumn("上场时间", "game_time_minute", "game_time_second", "%1'%2\"");
				columnModel.addColumn(dual);
				keywords.put(dual, new String[]{"game_time_minute", "game_time_second"});
			}
			
			@Override
			public void onRepaint(DisplayTable table)
			{
				if(shouldRedoQuery)
				{
					Table result = matchService.searchPerformanceByID(matchId, header, descend);
					this.updateTable(result);
					sectionPerPage = result.getRows().length;
					shouldRedoQuery = false;
				}
			}
		};
		this.performance.tableModel = performanceModel;
		this.performance.columnModel = performanceModel;
	}
}
