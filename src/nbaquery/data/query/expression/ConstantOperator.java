package nbaquery.data.query.expression;

import nbaquery.data.Row;

public class ConstantOperator implements Operator
{
	public Object constant;
	
	public Object calculate(Row... row)
	{
		return constant;
	}
	
	@Override
	public String rebuild() {
		if(constant == null) return "null";
		else if(constant instanceof String) return String.format("'%s'", constant);
		else if(constant instanceof Integer) return String.format("%d", constant);
		else if(constant instanceof Float) return String.format("%f", constant);
		return null;
	}
}
