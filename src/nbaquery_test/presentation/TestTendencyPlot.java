package nbaquery_test.presentation;

import javax.swing.JFrame;

import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.launcher.Main;
import nbaquery.presentation4.plot.TendencyPlot;

public class TestTendencyPlot extends Main{

	public void loadPresentation()
	{
		JFrame fr = new JFrame();
		fr.setSize(600, 480);
		fr.setLayout(null);
		@SuppressWarnings("serial")
		TendencyPlot dp = new TendencyPlot(){

			@Override
			protected void onRowselected(Row r) {
				System.out.println(r);
			}};
		
		dp.setLocation(50, 20);
		dp.setSize(500, 400);
		fr.add(dp);
		fr.setVisible(true);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		while(fr.isDisplayable())
		{
			Table table = this.matchService.searchPerformancesByPlayer("Monta Ellis");
			dp.setModel(table, table.getColumn("three_shoot_rate"));	
			fr.repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] arguments) throws Exception
	{
		TestTendencyPlot tst = new TestTendencyPlot();
		tst.launch();
	}
}
