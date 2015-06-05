package nbaquery.data.query;

import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.query.expression.ExpressionFactory;
import nbaquery.data.query.expression.Operator;

public class ExpressionDeriveColumnInfo extends DeriveColumnInfo
{
	public String statement;
	public Operator operatorStatement;
	
	public ExpressionDeriveColumnInfo(String deriveColumns, Class<?> deriveClasses, String statement)
		throws Exception
	{
		super(deriveColumns, deriveClasses);
		this.statement = statement;
	}

	@Override
	public void retrieve(Table resultTable)
	{
		try
		{
			operatorStatement = ExpressionFactory.getInstance().parse(
					resultTable.getTableHost(), new Table[]{resultTable}, statement);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void derive(Row resultRow)
	{
		Object value = operatorStatement.calculate(resultRow);
		this.getDeriveColumn().setAttribute(resultRow, value);
	}
}
