package nbaquery.data.query;

import nbaquery.data.Table;
import nbaquery.data.query.expression.ExpressionFactory;
import nbaquery.data.query.expression.Operator;

public class SelectProjectQuery implements Query
{
	public Table table;
	
	public String[] projectColumns;
	
	public Operator expression;
	
	public SelectProjectQuery(Table table, String... projectColumns)
	{
		this.table = table;
		this.projectColumns = projectColumns;
	}
	
	public SelectProjectQuery(String statement, Table table, String... projectColumns) throws Exception
	{
		this.table = table;
		this.expression = ExpressionFactory.getInstance().parse(table.getTableHost(), new Table[]{table}, statement);
		this.projectColumns = projectColumns;
	}
}
