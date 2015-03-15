package nbaquery.data.query.expression;

import nbaquery.data.Row;

public class ConstantOperator implements Operator
{
	public Object constant;
	
	public Object calculate(Row... row)
	{
		return constant;
	}
}
