package nbaquery.logic;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.query.DeriveColumnInfo;

public class RateColumnInfo extends DeriveColumnInfo
{

	String dividendString, divisorString;
	public RateColumnInfo(String deriveColumn, String dividend, String divisor)
	{
		super(deriveColumn, Float.class);
		this.dividendString = dividend;
		this.divisorString = divisor;
	}
	
	Column dividend, divisor; 
	
	@Override
	public void retrieve(Table resultTable)
	{
		dividend = resultTable.getColumn(dividendString);
		divisor = resultTable.getColumn(divisorString);
	}

	@Override
	public void derive(Row resultRow)
	{
		Integer x = (Integer) dividend.getAttribute(resultRow);
		Integer y = (Integer) divisor.getAttribute(resultRow);
		getDeriveColumn().setAttribute(resultRow, 1.0f * x / y);
	}
}
