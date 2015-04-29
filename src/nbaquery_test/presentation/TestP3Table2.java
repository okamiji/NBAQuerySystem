package nbaquery_test.presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;

import nbaquery.data.Table;
import nbaquery.data.file.FileTableHost;
import nbaquery.data.file.loader.MatchNaturalJoinPerformanceLoader;
import nbaquery.data.file.loader.PlayerLoader;
import nbaquery.data.file.loader.TeamLoader;
import nbaquery.data.query.SortQuery;
import nbaquery.presentation3.PresentationTableModel;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.RankingTableColumn;

public class TestP3Table2
{
	public static void main(String[] arguments)
	{
		JFrame jframe = new JFrame();
		jframe.setLayout(null);
		jframe.setSize(600, 480);
		
		String root = "D:\\迭代一数据";
		final FileTableHost host = new FileTableHost(new File(root), new Class<?>[]{TeamLoader.class, 
			PlayerLoader.class, MatchNaturalJoinPerformanceLoader.class});
		
		Table playerTable = host.getTable("team");
		final SortQuery sq = new SortQuery(playerTable, "team_name", true);
		
		final PresentationTableModel model = new PresentationTableModel()
		{
			{
				setSectionPerPage(5);
				columnModel.addColumn("球队名称", "team_name");
				columnModel.addColumn("  ", "team_logo", 0);
				columnModel.addColumn(new RankingTableColumn(), 0);
			}
			
			@Override
			public void onRepaint(DisplayTable table)
			{
				if(host.getTable("team").hasTableChanged(this))
				{
					host.performQuery(sq, "team_result");
					Table theTable = host.getTable("team_result");
					super.updateTable(theTable);
				}
			}
		};
		
		DisplayTable table = new DisplayTable(model, model);
		table.setRowHeight(35);
		
		jframe.add(table);
		table.setBounds(0, 40, 600, 480);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setVisible(true);
		
		JButton jbp = new JButton("+");
		jbp.setBounds(0, 0, 60, 40);
		jbp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				model.setPageIndex(model.getPageIndex() + 1);
			}
		});
		jframe.add(jbp);
		JButton jbm = new JButton("-");
		jbm.setBounds(60, 0, 60, 40);
		jframe.add(jbm);
		jbm.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				model.setPageIndex(model.getPageIndex() - 1);
			}
		});
		
		while(true) try
		{
			jframe.repaint();
			Thread.sleep(100);
		}
		catch(Exception e)
		{
			
		}
	}
}
