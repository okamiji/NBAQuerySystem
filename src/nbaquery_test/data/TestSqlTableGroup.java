package nbaquery_test.data;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.query.GroupQuery;
import nbaquery.data.sql.BaseTableConstants;
import nbaquery.data.sql.MutableSqlRow;
import nbaquery.data.sql.MutableSqlTable;
import nbaquery.data.sql.SqlTableHost;
import nbaquery.data.sql.query.AliasingAlgorithm;
import nbaquery.data.sql.query.DeriveAlgorithm;
import nbaquery.data.sql.query.GroupAlgorithm;
import nbaquery.data.sql.query.NaturalJoinAlgorithm;
import nbaquery.data.sql.query.SelectProjectAlgorithm;
import nbaquery.data.sql.query.SortAlgorithm;
import nbaquery.data.sql.query.SqlQueryAlgorithm;
import nbaquery.logic.SumColumnInfo;

public class TestSqlTableGroup {
	
	public static void main(String[] arguments) throws Exception
	{
		System.out.print("password: ");
		java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
		SqlTableHost host = new SqlTableHost("localhost", "root", reader.readLine(),
				new BaseTableConstants[]{BaseTableConstants.player, BaseTableConstants.team},
				new SqlQueryAlgorithm<?>[]{new SelectProjectAlgorithm(), new AliasingAlgorithm(),
				new NaturalJoinAlgorithm(), new SortAlgorithm(), new DeriveAlgorithm(), new GroupAlgorithm()});
		MutableSqlTable table = new MutableSqlTable(host, "z", new String[]{"b", "a", "c"}, new Class<?>[]{Integer.class, String.class, Integer.class},
				new String[]{"int", "char(8)", "int"}, "a");
		host.putTable("z", table);
		
		Column a = table.getColumn("a");
		Column c = table.getColumn("c");
		Column b = table.getColumn("b");
		
		for(int i = 0; i < 1000; i ++)
		{
			MutableSqlRow mtb = table.createRow();
			a.setAttribute(mtb, Integer.toString(i));
			c.setAttribute(mtb, i);
			b.setAttribute(mtb, i % 10);
			mtb.submit();
		}
		
		GroupQuery gp = new GroupQuery(table, new String[]{"b"}, new SumColumnInfo("s", "c"));
		host.performQuery(gp, "gz");
		Table resultTable = host.getTable("gz");
		
		b = resultTable.getColumn("b");
		Column s = resultTable.getColumn("s");
		
		while(true)
			if(resultTable.hasTableChanged("x"))
				for(Row result : resultTable)
		{
			System.out.println(b.getAttribute(result) + " " + s.getAttribute(result));
		}
	}
}
