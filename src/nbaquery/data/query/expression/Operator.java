package nbaquery.data.query.expression;

import nbaquery.data.Row;

public interface Operator
{
	public Object calculate(Row... row);
}
