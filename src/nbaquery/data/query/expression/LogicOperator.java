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

		@Override
		public String rebuild() {
			return "(" + this.leftHand.rebuild() + " and " + this.rightHand.rebuild() + ")";
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
		
		@Override
		public String rebuild() {
			return "(" + this.leftHand.rebuild() + " or " + this.rightHand.rebuild() + ")";
		}
	}
	
	public class Not extends BinaryOperator implements LogicOperator
	{
		@Override
		public Object calculate(Row... row)
		{
			return super.rightHand.calculate(row) == Boolean.FALSE;
		}
		
		@Override
		public String rebuild() {
			return "(not " + this.rightHand.rebuild() + ")";
		}
	}
}
