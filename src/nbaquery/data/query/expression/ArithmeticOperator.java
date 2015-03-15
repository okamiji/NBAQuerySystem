package nbaquery.data.query.expression;

import nbaquery.data.Row;

public interface ArithmeticOperator extends Operator
{
	public class Add extends BinaryOperator implements ArithmeticOperator
	{
		@Override
		public Object calculate(Row... row)
		{
			Object leftResult = super.leftHand.calculate(row);
			Object rightResult = super.rightHand.calculate(row);
			
			if(leftResult instanceof Integer && rightResult instanceof Integer)
				return (int)leftResult + (int)rightResult;
			else if(leftResult instanceof Integer)
				leftResult = (float)(int)leftResult;
			else if(rightResult instanceof Integer)
				rightResult = (float)(int)rightResult;
			return (float)leftResult + (float)rightResult;
		}
	}
	
	public class Sub extends BinaryOperator implements ArithmeticOperator
	{
		@Override
		public Object calculate(Row... row)
		{
			Object leftResult = super.leftHand.calculate(row);
			Object rightResult = super.rightHand.calculate(row);
			
			if(leftResult instanceof Integer && rightResult instanceof Integer)
				return (int)leftResult - (int)rightResult;
			else if(leftResult instanceof Integer)
				leftResult = (float)(int)leftResult;
			else if(rightResult instanceof Integer)
				rightResult = (float)(int)rightResult;
			return (float)leftResult - (float)rightResult;
		}
	}
	
	public class Mult extends BinaryOperator implements ArithmeticOperator
	{
		@Override
		public Object calculate(Row... row)
		{
			Object leftResult = super.leftHand.calculate(row);
			Object rightResult = super.rightHand.calculate(row);
			if(leftResult instanceof Integer && rightResult instanceof Integer)
				return (int)leftResult * (int)rightResult;
			else if(leftResult instanceof Integer)
				leftResult = (float)(int)leftResult;
			else if(rightResult instanceof Integer)
				rightResult = (float)(int)rightResult;
			return (float)leftResult * (float)rightResult;
		}
	}
	
	public class Div extends BinaryOperator implements ArithmeticOperator
	{
		@Override
		public Object calculate(Row... row)
		{
			Object leftResult = super.leftHand.calculate(row);
			Object rightResult = super.rightHand.calculate(row);
			
			if(leftResult instanceof Integer && rightResult instanceof Integer)
				return (int)leftResult / (int)rightResult;
			else if(leftResult instanceof Integer)
				leftResult = (float)(int)leftResult;
			else if(rightResult instanceof Integer)
				rightResult = (float)(int)rightResult;
			return (float)leftResult / (float)rightResult;
		}
	}
	
	public class Pow extends BinaryOperator implements ArithmeticOperator
	{
		@Override
		public Object calculate(Row... row)
		{
			Object leftResult = super.leftHand.calculate(row);
			Object rightResult = super.rightHand.calculate(row);
			if(leftResult instanceof Integer && rightResult instanceof Integer)
				return Math.pow((int)leftResult, (int)rightResult);
			else if(leftResult instanceof Integer)
				leftResult = (float)(int)leftResult;
			else if(rightResult instanceof Integer)
				rightResult = (float)(int)rightResult;
			return Math.pow((float)leftResult, (float)rightResult);
		}
	}
}
