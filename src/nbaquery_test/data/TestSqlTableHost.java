package nbaquery_test.data;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.sql.MutableSqlRow;
import nbaquery.data.sql.MutableSqlTable;
import nbaquery.data.sql.SqlTableHost;

public class TestSqlTableHost {
	
	public static void main(String[] arguments) throws Exception
	{
		System.out.print("password: ");
		java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
		SqlTableHost host = new SqlTableHost("localhost", "root", reader.readLine());
		MutableSqlTable table = new MutableSqlTable(host, "zz", new String[]{"b", "a", "c"}, new Class<?>[]{Integer.class, String.class, Float.class},
				new String[]{"int", "char(8)", "real"}, "a");
		
		Column a = table.getColumn("a");
		Column c = table.getColumn("c");
		Column b = table.getColumn("b");
		
		for(int i = 0; i < 20; i ++)
		{
			MutableSqlRow mtb = table.createRow();
			a.setAttribute(mtb, Integer.toString(i));
			c.setAttribute(mtb, 20f * i);
			b.setAttribute(mtb, i);
			mtb.submit();
		}
		
		for(Row result : table)
		{
			System.out.println(a.getAttribute(result) + " " + b.getAttribute(result) + " " +  c.getAttribute(result));
		}
		
		//host.deleteTable("zz");
	}
}
