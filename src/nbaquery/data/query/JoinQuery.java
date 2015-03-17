package nbaquery.data.query;

import nbaquery.data.Table;
import nbaquery.data.query.expression.Operator;

public class JoinQuery implements Query
{
	public Table leftTable, rightTable;
	
	public String[] projectColumns;
	
	public Operator expression;
}
