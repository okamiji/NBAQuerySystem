package nbaquery_test.logic;

import java.io.File;

import nbaquery.data.TableHost;
import nbaquery.data.file.FileTableHost;
import nbaqueryBusinessLogic.PlayerLogic;

import org.junit.*;

public class TestPlayerLogic
{
	static TableHost theHost;
	static PlayerLogic logic;
	
	@BeforeClass
	public static void setup() throws Exception
	{
		System.out.println("Initializing...");
		theHost = new FileTableHost(new File("D:\\迭代一数据"));
		logic = new PlayerLogic(theHost);
	}
	
	@Test
	public void test() throws Exception
	{
		String[][] result = logic.searchForPlayers(true, "three_shoot_count", true, null, null);
		out(result);
	}
		
	public void out(String[][] result)
	{
		for(int i = 0; i < result.length; i ++)
		{
			for(int j = 0; j < result[i].length; j ++)
				System.out.print(result[i][j]);
			System.out.println();
		}
	}
}
