package nbaquery.presentation3.team;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.logic.match.NewMatchService;
import nbaquery.presentation.resource.JSVGComponentResource;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.KeyValueDisplay;
import nbaquery.presentation3.match.CompareMatchSubPanel;

@SuppressWarnings("serial")
public class DetailedTeamPanel extends JPanel
{
	static final String[] keyName = new String[]{"team_name", "team_location", "team_host", "team_match_area", "team_sector", "team_foundation"};
	static final String[] keyDisplayName = new String[]{"\u7403\u961F\u540D\u79F0", "\u6240\u5C5E\u5DDE", "\u4E3B\u573A\u9986\u540D\u79F0", "\u8D5B\u533A", "\u8054\u76DF", "\u521B\u5EFA\u5E74\u4EFD"};
	
	KeyValueDisplay[] configuration = new KeyValueDisplay[keyName.length];
	
	static int upperHeight = 250;
	static int upperWidth = 250;
	
	public final CompareMatchSubPanel match;
	Row teamRow;
	
	public final JPanel logoDisplay = new JPanel()
	{
		public void paint(Graphics g)
		{
			this.getComponent(0).setLocation(0, 0);
			this.getComponent(0).setSize(this.getSize());
			super.paint(g);
		}
	};
	
	public DetailedTeamPanel(final DetailedInfoContainer container, NewMatchService matchService, int width, int height)
	{
		this.setSize(width, height);
		this.setLayout(null);
		
		for(int i = 0; i < keyName.length; i ++) 
		{
			if(keyName[i].equals("team_match_area"))
			{
				configuration[i] = new KeyValueDisplay(keyDisplayName[i], keyName[i])
				{
					public String convertValueToString(Object value)
					{
						if(value == null) return "";
						if(value.toString().equals("E")) return "\u4E1C\u90E8";
						else return "\u897F\u90E8";
					}
				};
			}
			else configuration[i] = new KeyValueDisplay(keyDisplayName[i], keyName[i]);
			
			
			configuration[i].setSize(upperWidth, upperHeight / keyName.length - 1);
			configuration[i].setLocation(width - upperWidth - 2, 2 + (upperHeight / keyName.length) * i);
			this.add(configuration[i]);
		}
		
		int theWidth = width - upperWidth - 6;
		this.logoDisplay.setBounds(2, 2 + (upperHeight - theWidth) / 2, theWidth, theWidth);
		this.add(this.logoDisplay);
		
		match = new CompareMatchSubPanel(container, matchService, width - 4, height - upperHeight - 6, 170, true)
		{
			{
				this.displayTable.oddBackground = this.displayTable.evenBackground = new Color(0, 0, 0, 0);
				this.enumerator.sectionPerEnumerator = 6;
			}
			Table reEnteredTable = null;
			@Override
			protected void reEnter()
			{
				reEnteredTable = super.matchService.searchMatchesByTeamNameAbbr(
						(String) teamRow.getDeclaredTable().getColumn("team_name_abbr").getAttribute(teamRow));
				Cursor rows = reEnteredTable.getRows();
				Row firstRow = rows.next();
				String season = (String) reEnteredTable.getColumn("match_season").getAttribute(firstRow);
				String date = (String) reEnteredTable.getColumn("match_date").getAttribute(firstRow);
				
				String[] fromAndTo = season.split("-");
				int from = Integer.parseInt(fromAndTo[0]) + 2000;
				int to = Integer.parseInt(fromAndTo[1]) + 2000;
				
				String[] monthAndDay = date.split("-");
				month = Integer.parseInt(monthAndDay[0]) - 1;
				
				if(month >= 6) year = from;
				else year = to;
			}

			@Override
			protected Table redoQuery()
			{
				if(reEnteredTable != null)
				{
					Table theTable = reEnteredTable;
					reEnteredTable = null;
					return theTable;
				}
				else
				{
					return super.matchService.searchMatchesByTeamNameAbbr(
						(String) teamRow.getDeclaredTable().getColumn("team_name_abbr").getAttribute(teamRow));
				}
			}
		};
		match.setBounds(2, upperHeight + 4,  width - 2, height - upperHeight - 6);
		this.add(match);
	}
	
	public void setRow(Row row)
	{
		if(row != null)
		{
			this.teamRow = row;
			match.shouldRedoQuery = true;
			this.logoDisplay.removeAll();
			
			for(int i = 0; i < keyName.length; i ++) configuration[i].setRow(teamRow);
			Object image = this.teamRow.getDeclaredTable().getColumn("team_logo").getAttribute(teamRow);
			if(image != null)
				this.logoDisplay.add(JSVGComponentResource.createJSVGComponent(image.toString()));
		}
	}
}
