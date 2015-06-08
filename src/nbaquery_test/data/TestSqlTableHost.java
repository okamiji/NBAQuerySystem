package nbaquery_test.data;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.query.AliasingQuery;
import nbaquery.data.query.NaturalJoinQuery;
import nbaquery.data.query.SelectProjectQuery;
import nbaquery.data.sql.BaseTableConstants;
import nbaquery.data.sql.MutableSqlTable;
import nbaquery.data.sql.SqlTableHost;
import nbaquery.data.sql.QuerySqlTable;
import nbaquery.data.sql.query.AliasingAlgorithm;
import nbaquery.data.sql.query.NaturalJoinAlgorithm;
import nbaquery.data.sql.query.SelectProjectAlgorithm;
import nbaquery.data.sql.query.SqlQueryAlgorithm;

public class TestSqlTableHost {
	
	public static void main(String[] arguments) throws Exception
	{
		System.out.print("password: ");
		java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
		SqlTableHost host = new SqlTableHost("localhost", "root", reader.readLine(),
				new BaseTableConstants[]{BaseTableConstants.player, BaseTableConstants.team},
				new SqlQueryAlgorithm<?>[]{new SelectProjectAlgorithm(), new AliasingAlgorithm(), new NaturalJoinAlgorithm()});
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
		QuerySqlTable viewTable = new QuerySqlTable(host, true, "sss", new String[]{"e", "f", "g"}, new Class<?>[]{Integer.class, String.class, Float.class},
				"select b as e, a as f, pow(b, 2) as g from zz", new String[]{"zz"});
		host.putTable("sss", table);
		
		NaturalJoinQuery naturalJoin = new NaturalJoinQuery(host.getTable("zz"), viewTable, new String[]{"b", "a"}, new String[]{"e", "f"});
		host.performQuery(naturalJoin, "ttt");
		
		Table resultTable = host.getTable("ttt");
		
		Column a = resultTable.getColumn("a");
		Column b = resultTable.getColumn("b");
		Column c = resultTable.getColumn("c");
		Column g = resultTable.getColumn("g");
		
		while(true)
			if(resultTable.hasTableChanged("x"))
				for(Row result : resultTable)
		{
			System.out.println(a.getAttribute(result) + " " + b.getAttribute(result) + " " + c.getAttribute(result) + " " +  g.getAttribute(result));
		}
	}
}
