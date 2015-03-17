package nbaquery.data.query;

import nbaquery.data.Table;
import nbaquery.data.query.expression.ExpressionFactory;
import nbaquery.data.query.expression.Operator;

public class JoinQuery implements Query
{
	public Table leftTable, rightTable;
	
	public String[] projectColumns;
	
	public Operator expression;
	
	public JoinQuery(Table leftTable, Table rightTable, String... projectColumns) throws Exception
	{
		this.leftTable = leftTable;
		this.rightTable = rightTable;
		this.projectColumns = projectColumns;
	}
	
	public JoinQuery(String statement, Table leftTable, Table rightTable, String... projectColumns) throws Exception
	{
		this(leftTable, rightTable, projectColumns);
		this.expression = ExpressionFactory.getInstance().parse(leftTable.getTableHost(), statement);
	}
}
