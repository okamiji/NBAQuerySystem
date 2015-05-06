package nbaquery.presentation3;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Stack;

import javax.swing.JPanel;

import nbaquery.data.Row;
import nbaquery.logic.match.NewMatchService;
import nbaquery.logic.team.NewTeamService;
import nbaquery.presentation3.match.DetailedMatchPanel;
import nbaquery.presentation3.player.DetailedPlayerPanel;
import nbaquery.presentation3.team.DetailedTeamPanel;

@SuppressWarnings("serial")
public class DetailedInfoPanel extends JPanel implements DetailedInfoContainer 
{
	public static final int NONE = 0;
	public final DetailedMatchPanel matchDisplay;		public static final int MATCH = 1;
	public final DetailedPlayerPanel playerDisplay;		public static final int PLAYER = 2;
	public final DetailedTeamPanel teamDisplay;			public static final int TEAM = 3;
	public final NewTeamService teamService;
	
	public final DisplayButton close;
	public final DisplayButton backward;
	
	public DetailedInfoPanel(NewMatchService service, NewTeamService teamService)
	{
		this.setSize(550, 640);
		this.setLayout(null);
		this.setBackground(MainFrame.transparent);
		
		this.matchDisplay = new DetailedMatchPanel(this, service, 500, 600);
		this.matchDisplay.setBackground(MainFrame.transparent);
		this.matchDisplay.setBounds(50, 20, 500, 600);
		this.add(matchDisplay);
		
		this.playerDisplay = new DetailedPlayerPanel(this, service, 400, 600);
		this.playerDisplay.setBackground(MainFrame.transparent);
		this.playerDisplay.setBounds(150, 20, 400, 600);
		this.playerDisplay.match.setBackground(MainFrame.transparent);
		this.add(playerDisplay);
		
		this.teamDisplay = new DetailedTeamPanel(this, service, 400, 600);
		this.teamDisplay.setBackground(MainFrame.transparent);
		this.teamDisplay.setBounds(150, 20, 400, 600);
		this.teamDisplay.match.setBackground(MainFrame.transparent);
		this.add(teamDisplay);
		
		this.teamService = teamService;
		
		this.close = new DisplayButton("img3/exit_idle.png", "img3/exit_over.png")
		{
			@Override
			protected void activate()
			{
				isClosed = true;
				currentDisplayType = NONE;
				rowStack.clear();
				typeStack.clear();
			}
		};
		this.add(this.close);
		
		this.backward = new DisplayButton("img3/back_idle.png", "img3/back_over.png")
		{
			@Override
			protected void activate()
			{
				popStack();
			}
		};
		this.add(backward);
	}

	protected Stack<Row> rowStack = new Stack<Row>();
	protected Stack<Integer> typeStack = new Stack<Integer>();
	
	protected int currentDisplayType = NONE;
	protected boolean isClosed = true;
	
	protected void pushStack(Row row, Integer type, boolean stacked)
	{
		if(!stacked)
		{
			rowStack.clear();
			typeStack.clear();
		}
		rowStack.push(row);	typeStack.push(type);
	}
	
	@Override
	public void displayPlayerInfo(Row player, boolean stacked)
	{
		this.pushStack(player, PLAYER, stacked);
		this.playerDisplay.setRow(player);
		this.currentDisplayType = PLAYER;
		this.isClosed = false;
	}
	
	@Override
	public void displayTeamInfo(Row team, boolean stacked)
	{
		this.pushStack(team, TEAM, stacked);
		this.teamDisplay.setRow(team);
		this.currentDisplayType = TEAM;
		this.isClosed = false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void displayTeamInfo(String teamNameOrAbbr, boolean isAbbr, boolean stacked)
	{
		Row[] rows = teamService.searchInfoByName(teamNameOrAbbr, isAbbr).getRows();
		if(rows.length > 0) this.displayTeamInfo(rows[0], stacked);
	}

	@Override
	public void displayMatchInfo(Row match, boolean stacked)
	{
		this.pushStack(match, MATCH, stacked);
		this.matchDisplay.setRow(match);
		this.currentDisplayType = MATCH;
		this.isClosed = false;
	}
	
	public static final Color background = new Color(1.0f, 1.0f, 1.0f, 0.9f);
	public void paint(Graphics g)
	{
		if(this.isClosed)
		{
			this.playerDisplay.setVisible(false);
			this.teamDisplay.setVisible(false);
			this.matchDisplay.setVisible(false);
			this.close.setVisible(false);
			this.backward.setVisible(false);
		}
		else
		{
			this.playerDisplay.setVisible(currentDisplayType == PLAYER);
			this.teamDisplay.setVisible(currentDisplayType == TEAM);
			this.matchDisplay.setVisible(currentDisplayType == MATCH);
			this.close.setVisible(currentDisplayType != NONE);
			this.backward.setVisible(currentDisplayType != NONE);
			
			if(this.currentDisplayType != NONE)
			{
				g.setColor(background);
				if(this.currentDisplayType != MATCH)
				{
					g.fillRect(135, 0, 420, 640);
					g.fillRect(100, 0, 30, 75);
					this.close.setLocation(100, 5);
					this.backward.setLocation(100, 35);
				}
				else
				{
					g.fillRect(35, 0, 520, 640);
					g.fillRect(0, 0, 30, 75);
					this.close.setLocation(0, 5);
					this.backward.setLocation(0, 35);
				}
			}
		}
		super.paint(g);
	}
	
	public void popStack()
	{
		if(!rowStack.isEmpty())
		{
			rowStack.pop();
			typeStack.pop();
			
			if(rowStack.isEmpty())
			{
				this.currentDisplayType = NONE;
				this.isClosed = true;
				return;
			}
			
			Row currentRow = rowStack.pop();
			this.currentDisplayType = typeStack.pop();
			if(this.currentDisplayType == PLAYER) this.displayPlayerInfo(currentRow, true);
			if(this.currentDisplayType == TEAM) this.displayTeamInfo(currentRow, true);
			if(this.currentDisplayType == MATCH) this.displayMatchInfo(currentRow, true);
		}
		else
		{
			this.currentDisplayType = NONE;
			this.isClosed = true;
		}
	}
}
