package nbaquery.data.query;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.query.expression.BinaryOperator;
import nbaquery.data.query.expression.ColumnOperator;
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
					resultTable.getTableHost(), statement);
			replaceTraversal(resultTable, operatorStatement);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void replaceTraversal(Table table, Operator op)
	{
		if(op == null) return;
		else if(op instanceof BinaryOperator)
		{
			replaceTraversal(table, ((BinaryOperator) op).leftHand);
			replaceTraversal(table, ((BinaryOperator) op).rightHand);
		}
		else if(op instanceof ColumnOperator)
		{
			String columnName = ((ColumnOperator)op).column.getColumnName();
			Column column = table.getColumn(columnName);
			if(column != null) ((ColumnOperator) op).column = column;
		}
	}
	
	@Override
	public void derive(Row resultRow)
	{
		Object value = operatorStatement.calculate(resultRow);
		this.getDeriveColumn().setAttribute(resultRow, value);
	}
}
