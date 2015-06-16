package nbaquery_test.presentation;

import javax.swing.JFrame;

import nbaquery.data.Table;
import nbaquery.launcher.Main;
import nbaquery.presentation4.plot.DistributionPlot;

public class TestDistributionPlot extends Main{

	public void loadPresentation()
	{
		JFrame fr = new JFrame();
		fr.setSize(600, 480);
		fr.setLayout(null);
		Table table = this.playerService.searchForPlayers(true, new String[]{"three_shoot_rate"}, true, null, null);
		DistributionPlot dp = new DistributionPlot(20);
		dp.setModel(table, table.getColumn("three_shoot_rate"));	
		dp.setLocation(50, 20);
		dp.setSize(500, 400);
		fr.add(dp);
		fr.setVisible(true);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] arguments) throws Exception
	{
		TestDistributionPlot tst = new TestDistributionPlot();
		tst.launch();
	}
}
