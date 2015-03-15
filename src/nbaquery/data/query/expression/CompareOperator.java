package nbaquery.data.query.expression;

import nbaquery.data.Row;

public interface CompareOperator extends Operator
{
	public class Equal extends BinaryOperator implements CompareOperator
	{
		@Override
		public Object calculate(Row... row)
		{
			Object leftResult = super.leftHand.calculate(row);
			Object rightResult = super.rightHand.calculate(row);
			
			if(leftResult == null) return rightResult == null;
			else return leftResult.equals(rightResult);
		}
	}
	
	public class NotEqual extends BinaryOperator implements CompareOperator
	{
		@Override
		public Object calculate(Row... row)
		{
			Object leftResult = super.leftHand.calculate(row);
			Object rightResult = super.rightHand.calculate(row);
			
			if(leftResult == null) return rightResult != null;
			else return !leftResult.equals(rightResult);
		}
	}
	
	public class Greater extends BinaryOperator implements CompareOperator
	{
		@SuppressWarnings({"rawtypes", "unchecked"})
		@Override
		public Object calculate(Row... row)
		{
			Comparable leftResult = (Comparable)super.leftHand.calculate(row);
			Comparable rightResult = (Comparable)super.rightHand.calculate(row);
			
			if(leftResult == null) return rightResult == null;
			else return leftResult.compareTo(rightResult) > 0;
		}
	}
	
	public class GreaterEq extends BinaryOperator implements CompareOperator
	{
		@SuppressWarnings({"rawtypes", "unchecked"})
		@Override
		public Object calculate(Row... row)
		{
			Comparable leftResult = (Comparable)super.leftHand.calculate(row);
			Comparable rightResult = (Comparable)super.rightHand.calculate(row);
			
			if(leftResult == null) return rightResult == null;
			else return leftResult.compareTo(rightResult) >= 0;
		}
	}
	
	public class Less extends BinaryOperator implements CompareOperator
	{
		@SuppressWarnings({"rawtypes", "unchecked"})
		@Override
		public Object calculate(Row... row)
		{
			Comparable leftResult = (Comparable)super.leftHand.calculate(row);
			Comparable rightResult = (Comparable)super.rightHand.calculate(row);
			
			if(leftResult == null) return rightResult == null;
			else return leftResult.compareTo(rightResult) < 0;
		}
	}
	
	public class LessEq extends BinaryOperator implements CompareOperator
	{
		@SuppressWarnings({"rawtypes", "unchecked"})
		@Override
		public Object calculate(Row... row)
		{
			Comparable leftResult = (Comparable)super.leftHand.calculate(row);
			Comparable rightResult = (Comparable)super.rightHand.calculate(row);
			
			if(rightResult == null) return leftResult == null;
			else return rightResult.compareTo(leftResult) >= 0;
		}
	}
}
