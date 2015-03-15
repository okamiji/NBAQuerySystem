package nbaquery.data.query.expression;

import nbaquery.data.Row;

public abstract class BinaryOperator implements Operator
{
	public Operator leftHand;
	public Operator rightHand;
	
	@Override
	public abstract Object calculate(Row... row);	
}
