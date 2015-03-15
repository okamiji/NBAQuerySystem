package nbaquery.data.query;

import nbaquery.data.Table;

public class SetOperation implements Query
{
	public Table leftHand, rightHand;
	
	public EnumSetOperation operation;
	public enum EnumSetOperation
	{
		UNION, INTERSECTION, COMPLEMENT;
	}
	
	public SetOperation(Table leftHand, Table rightHand, EnumSetOperation operation)
	{
		this.leftHand = leftHand;
		this.rightHand = rightHand;
		this.operation = operation;
	}
}
