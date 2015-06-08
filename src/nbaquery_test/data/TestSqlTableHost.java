package nbaquery_test.data;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.sql.BaseTableConstants;
import nbaquery.data.sql.MutableSqlTable;
import nbaquery.data.sql.SqlTableHost;
import nbaquery.data.sql.QuerySqlTable;

public class TestSqlTableHost {
	
	public static void main(String[] arguments) throws Exception
	{
		System.out.print("password: ");
		java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
		SqlTableHost host = new SqlTableHost("localhost", "root", reader.readLine(), new BaseTableConstants[]{BaseTableConstants.player, BaseTableConstants.team});
		MutableSqlTable table = new MutableSqlTable(host, "zz", new String[]{"b", "a", "c"}, new Class<?>[]{Integer.class, String.class, Float.class},
				new String[]{"int", "char(8)", "real"}, "a");
		host.putTable("zz", table);
		
		/*
		Column a = table.getColumn("a");
		Column c = table.getColumn("c");
		Column b = table.getColumn("b");
		
		for(int i = 60; i < 1000; i ++)
		{
			MutableSqlRow mtb = table.createRow();
			a.setAttribute(mtb, Integer.toString(i));
			c.setAttribute(mtb, 20f * i);
			b.setAttribute(mtb, i);
			mtb.submit();
		}
		*/
		QuerySqlTable viewTable = new QuerySqlTable(host, false, "sss", new String[]{"e", "f", "g"}, new Class<?>[]{Integer.class, String.class, Float.class},
				"select b as e, a as f, b / c as g from zz order by e desc", new String[]{"zz"});
		host.putTable("sss", table);
		
		Column e = viewTable.getColumn("e");
		Column f = viewTable.getColumn("f");
		Column g = viewTable.getColumn("g");
		
		while(true)
			if(viewTable.hasTableChanged("x"))
				for(Row result : viewTable)
		{
			System.out.println(e.getAttribute(result) + " " + f.getAttribute(result) + " " +  g.getAttribute(result));
		}
	}
}
