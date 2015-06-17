package nbaquery.presentation3.player;

import java.awt.Graphics;

import javax.swing.JPanel;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.logic.player.NewPlayerService;
import nbaquery.presentation3.DetailedInfoContainer;
import nbaquery.presentation3.DropList;
import nbaquery.presentation4.plot.DistributionPlot;

@SuppressWarnings("serial")
public class DistributePlayerPanel extends JPanel
{
	NewPlayerService playerService;
	DetailedInfoContainer detailedInfo;
	
	public boolean isGross = true;
	public String league = null;
	public String position = null;
	public boolean descend = false;
	
	public boolean shouldRedoQuery = true;
	
	String currentKeyword;

	DistributionPlot distributionPlot;
	
	public DistributePlayerPanel(NewPlayerService playerService, DetailedInfoContainer detailedInfo, int width, int height)
	{
		this.playerService = playerService;
		this.detailedInfo = detailedInfo;
		
		this.setSize(width, height);
		this.setLayout(null);
		
		/*
		this.playerTable = new DisplayTable();
		this.playerTable.setSize(width, height - 20);
		this.playerTable.setLocation(2, 20 + 2);
		super.add(playerTable);
		
		playerTable.setRowHeight(playerTable.getHeight() / (sectionPerPageParam + 1));
		basicData = new ComparePlayerModel()
		{

		
		analyticData = new ComparePlayerModel()
		{
			{
				setSectionPerPage(sectionPerPageParam);
				DefaultTableColumn season = columnModel.addColumn("\u8D5B\u5B63", "match_season");
				season.padding = 10;	keywordMap.put(season, new String[]{"match_season"});
				
				DefaultTableColumn portrait = columnModel.addColumn("", "player_portrait");
				portrait.padding = playerTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn player_name = columnModel.addColumn("\u7403\u5458\u540D\u79F0", "player_name");
				player_name.padding = 70;	keywordMap.put(season, new String[]{"player_name"});
				
				DefaultTableColumn logo = columnModel.addColumn("", "team_logo");
				logo.padding = playerTable.getHeight() / (sectionPerPage + 1);
				
				DefaultTableColumn team_name = columnModel.addColumn("\u7403\u961F", "team_name_abbr");
				team_name.padding = 5;	keywordMap.put(team_name, new String[]{"team_name_abbr"});
				
				DefaultTableColumn efficiency = columnModel.addColumn("\u6548\u7387", "efficiency");
				efficiency.padding = 2;			keywordMap.put(efficiency, new String[]{"efficiency"});
				
				DefaultTableColumn gmsc = columnModel.addColumn("GmSc", "gmsc_efficiency");
				gmsc.padding = 2;			keywordMap.put(gmsc, new String[]{"gmsc_efficiency"});
				
				DefaultTableColumn foul_shoot_rate = columnModel.addColumn("\u7F5A\u7403\u547D\u4E2D", "foul_shoot_rate");
				foul_shoot_rate.padding = 2;			keywordMap.put(foul_shoot_rate, new String[]{"foul_shoot_rate"});
				
				DefaultTableColumn shoot_rate = columnModel.addColumn("\u4E8C\u5206\u547D\u4E2D", "shoot_rate");
				shoot_rate.padding = 2;			keywordMap.put(shoot_rate, new String[]{"shoot_rate"});
				
				DefaultTableColumn three_shoot_rate = columnModel.addColumn("\u4E09\u5206\u547D\u4E2D", "three_shoot_rate");
				three_shoot_rate.padding = 2;			keywordMap.put(three_shoot_rate, new String[]{"three_shoot_rate"});
				
				DefaultTableColumn true_shoot_rate = columnModel.addColumn("\u771F\u5B9E\u547D\u4E2D", "true_shoot_rate");
				true_shoot_rate.padding = 2;			keywordMap.put(true_shoot_rate, new String[]{"true_shoot_rate"});
				
				DefaultTableColumn shoot_efficiency = columnModel.addColumn("\u6295\u7BEE\u6548\u7387", "shoot_efficiency");
				shoot_efficiency.padding = 2;			keywordMap.put(shoot_efficiency, new String[]{"shoot_efficiency"});
				
				DefaultTableColumn assist_rate = columnModel.addColumn("\u52A9\u653B\u6548\u7387", "assist_rate");
				assist_rate.padding = 2;			keywordMap.put(assist_rate, new String[]{"assist_rate"});
				
				DefaultTableColumn steal_rate = columnModel.addColumn("\u62A2\u65AD\u6548\u7387", "steal_rate");
				steal_rate.padding = 2;			keywordMap.put(steal_rate, new String[]{"steal_rate"});
				
				DefaultTableColumn cap_rate = columnModel.addColumn("\u76D6\u5E3D\u6548\u7387", "cap_rate");
				cap_rate.padding = 2;			keywordMap.put(cap_rate, new String[]{"cap_rate"});
				
				DefaultTableColumn miss_rate = columnModel.addColumn("\u5931\u8BEF\u7387", "miss_rate");
				miss_rate.padding = 2;			keywordMap.put(miss_rate, new String[]{"miss_rate"});
				
				DefaultTableColumn usage = columnModel.addColumn("\u4F7F\u7528\u7387", "usage_rate");
				usage.padding = 2;			keywordMap.put(usage, new String[]{"usage_rate"});
			}
		};
		*/
		
		//XXX adding control panels.
		final DropList dataScope = new DropList(new String[]{"\u603B\u6570\u636E", "\u573A\u5747\u6570\u636E"})
		{
			@Override
			protected void onSelectionChanged(int index)
			{
				isGross = (index == 0);
				shouldRedoQuery = true;
			}
		};
		dataScope.setBounds(2, 2, 80, 20);
		dataScope.setHorizontalAlignment(DropList.CENTER);
		this.add(dataScope);
		
		
		final String[] keywordName = new String[]{"self_score", "total_board", "assist", "cap", "steal", "miss", "foul", "efficiency", "gmsc_efficiency",
				"foul_shoot_rate", "shoot_rate", "three_shoot_rate", "true_shoot_rate", "shoot_efficiency", "assist_rate", "steal_rate", "cap_rate", "miss_rate", "usage_rate"};
		String[] keywordLabel = new String[]{"\u5F97\u5206", "\u7BEE\u677F", "\u52A9\u653B", "\u76D6\u5E3D", "\u62A2\u65AD", "\u5931\u8BEF", "\u72AF\u89C4", "\u6548\u7387", "GmSc",
				"\u7F5A\u7403\u547D\u4E2D", "\u4E8C\u5206\u547D\u4E2D", "\u4E09\u5206\u547D\u4E2D", "\u771F\u5B9E\u547D\u4E2D", "\u6295\u7BEE\u6548\u7387", "\u52A9\u653B\u6548\u7387", "\u62A2\u65AD\u6548\u7387", "\u76D6\u5E3D\u6548\u7387", "\u5931\u8BEF\u7387", "\u4F7F\u7528\u7387"};
		
		final DropList dataModel = new DropList(keywordLabel)
		{
			@Override
			protected void onSelectionChanged(int index)
			{
				currentKeyword = keywordName[index];
				shouldRedoQuery = true;
			}
		};
		dataModel.setBounds(width - 82, 2, 80, 20);
		dataModel.setHorizontalAlignment(DropList.CENTER);
		this.add(dataModel);
		
		this.currentKeyword = keywordName[0];
		
		final DropList matchArea = new DropList(new String[]{"\u6240\u6709\u8D5B\u533A", "\u4E1C\u90E8\u8D5B\u533A", "\u897F\u90E8\u8D5B\u533A"})
		{
			@Override
			protected void onSelectionChanged(int index)
			{
				if(index == 0) league = null;
				else if(index == 1) league = "E";
				else league = "W";
				shouldRedoQuery = true;
			}
		};
		matchArea.setBounds(84, 2, 80, 20);
		matchArea.setHorizontalAlignment(DropList.CENTER);
		this.add(matchArea);
		
		final DropList position = new DropList(new String[]{"\u6240\u6709\u4F4D\u7F6E", "\u524D\u950B", "\u4E2D\u950B", "\u540E\u536B"})
		{
			@Override
			protected void onSelectionChanged(int index)
			{
				if(index == 0) DistributePlayerPanel.this.position = null;
				else if(index == 1) DistributePlayerPanel.this.position = "F";
				else if(index == 2) DistributePlayerPanel.this.position = "C";
				else DistributePlayerPanel.this.position = "G";
				shouldRedoQuery = true;
			}
		};
		position.setBounds(166, 2, 80, 20);
		position.setHorizontalAlignment(DropList.CENTER);
		this.add(position);
		
		this.distributionPlot = new DistributionPlot(20);
		this.distributionPlot.setBounds(0, 32, width, height - 64);
		this.add(distributionPlot);
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		if(shouldRedoQuery)
		{
			Table table = this.playerService.searchForPlayers(isGross, null, true, position, league);
			Column column = table.getColumn(currentKeyword);
			this.distributionPlot.setModel(table, column);
			shouldRedoQuery = false;
		}
	}
}
