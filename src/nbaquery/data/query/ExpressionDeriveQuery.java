package nbaquery.data.query;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.query.expression.BinaryOperator;
import nbaquery.data.query.expression.ColumnOperator;
import nbaquery.data.query.expression.ExpressionFactory;
import nbaquery.data.query.expression.Operator;

public class ExpressionDeriveQuery extends DeriveQuery
{
	public Operator[] expressions;
	public Column[] columns;
	
	public ExpressionDeriveQuery(String[] deriveExpressions, Table table, String[] deriveColumns, Class<?>[] deriveClasses, String... projectColumns) throws Exception
	{
		super(table, deriveColumns, deriveClasses, projectColumns);
		
		expressions = new Operator[deriveColumns.length];
		columns = new Column[deriveColumns.length];
		for(int i = 0; i < deriveExpressions.length; i ++)
			expressions[i] = ExpressionFactory.getInstance().parse(table.getTableHost(), deriveExpressions[i]);
	}

	public ExpressionDeriveQuery(Table table, DeriveColumnInfo[] deriveColumns, String... projectColumns) throws Exception
	{
		super(table, deriveColumns, projectColumns);
		
		expressions = new Operator[deriveColumns.length];
		columns = new Column[deriveColumns.length];
		for(int i = 0; i < deriveColumns.length; i ++)
			expressions[i] = ExpressionFactory.getInstance().parse(table.getTableHost(),
					((ExpressionDeriveColumnInfo)deriveColumns[i]).statement);
	}
	
	public void retrieve(Table table)
	{
		for(int i = 0; i < super.deriveColumns.length; i ++)
			columns[i] = table.getColumn(super.deriveColumns[i]);
		for(int i = 0; i < expressions.length; i ++)
			replaceTraversal(table, expressions[i]);
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
	public void derive(Row row)
	{
		for(int i = 0; i < expressions.length; i ++)
			columns[i].setAttribute(row, expressions[i].calculate(row));
	}	
}
