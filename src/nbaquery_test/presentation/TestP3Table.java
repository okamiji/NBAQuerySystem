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
import nbaquery.presentation3.table.DefaultTableColumnModel;
import nbaquery.presentation3.table.DisplayTable;
import nbaquery.presentation3.table.PagedDisplayTableModel;

public class TestP3Table
{
	public static void main(String[] arguments)
	{
		JFrame jframe = new JFrame();
		jframe.setLayout(null);
		jframe.setSize(600, 480);
		
		String root = "D:\\迭代一数据";
		FileTableHost host = new FileTableHost(new File(root), new Class<?>[]{TeamLoader.class, 
			PlayerLoader.class, MatchNaturalJoinPerformanceLoader.class});
		
		Table playerTable = host.getTable("player");
		
		DisplayTable table = new DisplayTable();
		DefaultTableColumnModel columnModel = ((DefaultTableColumnModel)table.columnModel);
		columnModel.addColumn("  ", "player_portrait").padding = 35;
		columnModel.addColumn("球员名称", "player_name").padding = 80;
		columnModel.addColumn("位置", "player_position").padding = 0;
		columnModel.addColumn("球衣编号", "player_number").padding = 0;
		
		table.setRowHeight(35);
		
		final PagedDisplayTableModel tableModel = ((PagedDisplayTableModel)table.tableModel);
		
		tableModel.setSectionPerPage(5);
		columnModel.setTable(playerTable);
		
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
				tableModel.setPageIndex(tableModel.getPageIndex() + 1);
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
				tableModel.setPageIndex(tableModel.getPageIndex() - 1);
			}
		});
		
		while(true) try
		{
			tableModel.setRow(playerTable.getRows());
			jframe.repaint();
			Thread.sleep(100);
		}
		catch(Exception e)
		{
			
		}
	}
}
