package nbaquery.data.query;

import nbaquery.data.Table;
import nbaquery.data.query.expression.Operator;

public abstract class JoinQuery implements Query
{
	public Table leftTable, rightTable;
	
	public String[] projectColumns;
	
	public Operator expression;
	
	public EnumJoinType joinType;
	public enum EnumJoinType
	{
		INNER_JOIN,
		OUTER_JOIN,
		LEFT_OUTER_JOIN,
		RIGHT_OUTER_JOIN;
	}
}
