package nbaquery.data.query.expression;

import nbaquery.data.Row;

/**
 * All Logic Operators should have short path property!
 * @author aegistudio
 */
public interface LogicOperator extends Operator
{
	public class And extends BinaryOperator implements LogicOperator
	{
		@Override
		public Object calculate(Row... row)
		{
			Object leftResult = super.leftHand.calculate(row);
			if(leftResult != Boolean.TRUE) return Boolean.FALSE;
			
			return Boolean.TRUE == super.rightHand.calculate(row);
		}
	}
	
	public class Or extends BinaryOperator implements LogicOperator
	{
		@Override
		public Object calculate(Row... row)
		{
			Object leftResult = super.leftHand.calculate(row);
			if(leftResult == Boolean.TRUE) return Boolean.TRUE;
			
			return super.rightHand.calculate(row) == Boolean.TRUE;
		}
	}
	
	public class Not extends BinaryOperator implements LogicOperator
	{
		@Override
		public Object calculate(Row... row)
		{
			return super.rightHand.calculate(row) == Boolean.FALSE;
		}
	}
}
