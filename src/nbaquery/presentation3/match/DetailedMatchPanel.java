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
import nbaquery.presentation3.table.TableSelectionListener;

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
		performance.addTableSelectionListener(new TableSelectionListener()
		{

			@Override
			public void onSelect(DisplayTable table, int row, int column, Object value, Point mousePoint)
			{
				if(column == 0 || column == 1)		//Team Logo / Team Name
					DetailedMatchPanel.this.container.displayTeamInfo((Row)value, true);
				else DetailedMatchPanel.this.container.displayPlayerInfo((Row)value, true);
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
				this.columnModel.addColumn(new DefaultTableColumn("\u8282\u6B21", "quarter_number")
				{
					public int getWidth(Graphics g)
					{
						return 20;
					}
				});
				
				this.columnModel.addColumn(new DefaultTableColumn("\u4E3B\u573A\u5F97\u5206", "quarter_host_score")
				{
					public int getWidth(Graphics g)
					{
						return (DetailedMatchPanel.this.getWidth() - 20)/2 - 1;
					}
				});
				
				this.columnModel.addColumn(new DefaultTableColumn("\u5BA2\u573A\u5F97\u5206", "quarter_guest_score")
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
				
				
				DefaultTableColumn column = columnModel.addColumn("\u961F\u4F0D", "team_name_abbr");
				column.padding = 5;
				keywords.put(column, new String[]{"team_name_abbr"});
				
				columnModel.addColumn("", "player_portrait").padding = 20;
				column = columnModel.addColumn("\u7403\u5458", "player_name");
				column.padding = 40;
				keywords.put(column, new String[]{"player_name"});
				
				DualTableColumn dual = new DualTableColumn("\u4E0A\u573A\u65F6\u95F4", "game_time_minute", "game_time_second", "%1'%2\"");
				columnModel.addColumn(dual);
				keywords.put(dual, new String[]{"game_time_minute", "game_time_second"});
				
				final int dualWidth = 32;
				
				dual = new DualTableColumn("\u7F5A\u7403", "foul_shoot_score", "foul_shoot_count", "%1-%2")
				{
					public int getWidth(Graphics g)
					{
						return dualWidth;
					}
				};
				columnModel.addColumn(dual);
				keywords.put(dual, new String[]{"foul_shoot_score", "foul_shoot_count"});
				
				dual = new DualTableColumn("\u4E8C\u5206", "shoot_score", "shoot_count", "%1-%2")
				{
					public int getWidth(Graphics g)
					{
						return dualWidth;
					}
				};
				columnModel.addColumn(dual);
				keywords.put(dual, new String[]{"shoot_score", "shoot_count"});
				
				dual = new DualTableColumn("\u4E09\u5206", "three_shoot_score", "three_shoot_count", "%1-%2")
				{
					public int getWidth(Graphics g)
					{
						return dualWidth;
					}
				};
				dual.padding = 0;
				columnModel.addColumn(dual);
				keywords.put(dual, new String[]{"three_shoot_score", "three_shoot_count"});
				
				dual = new DualTableColumn("\u653B\u5B88", "attack_board", "defence_board", "%1-%2")
				{
					public int getWidth(Graphics g)
					{
						return dualWidth;
					}
				};
				dual.padding = 0;
				columnModel.addColumn(dual);
				keywords.put(dual, new String[]{"attack_board", "defence_board"});
				
				column = columnModel.addColumn("\u7BEE\u677F", "total_board");
				column.padding = 0;
				keywords.put(column, new String[]{"total_board"});
				
				column = columnModel.addColumn("\u52A9\u653B", "assist");
				column.padding = 0;
				keywords.put(column, new String[]{"assist"});
				
				column = columnModel.addColumn("\u76D6\u5E3D", "cap");
				column.padding = 0;
				keywords.put(column, new String[]{"cap"});
				
				column = columnModel.addColumn("\u62A2\u65AD", "steal");
				column.padding = 0;
				keywords.put(column, new String[]{"steal"});
				
				column = columnModel.addColumn("\u5931\u8BEF", "miss");
				column.padding = 0;
				keywords.put(column, new String[]{"miss"});
				
				column = columnModel.addColumn("\u72AF\u89C4", "foul");
				column.padding = 0;
				keywords.put(column, new String[]{"foul"});
				
				column = columnModel.addColumn("\u5F97\u5206", "self_score");
				column.padding = 0;
				keywords.put(column, new String[]{"self_score"});
			}
			
			@Override
			public void onRepaint(DisplayTable table)
			{
				if(shouldRedoQuery)
				{
					Table result = matchService.searchPerformanceByID(matchId, header, descend);
					this.updateTable(result);
					sectionPerPage = result.getRows().getLength();
					shouldRedoQuery = false;
				}
			}
		};
		this.performance.tableModel = performanceModel;
		this.performance.columnModel = performanceModel;
		this.shouldRedoQuery = true;
	}
}
