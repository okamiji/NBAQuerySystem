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
		else if(constant instanceof String) return String.format("'%s'", (String)constant);
		else if(constant instanceof Integer) return String.format("%d", (int)constant);
		else if(constant instanceof Float) return String.format("%f", (float)constant);
		else if(constant instanceof Character) return String.format("%d", (int)((char)constant));
		return null;
	}
}
