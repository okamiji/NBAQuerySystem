package nbaquery.data.query;

public class ExpressionDeriveColumnInfo extends DeriveColumnInfo
{
	public String statement;
	
	public ExpressionDeriveColumnInfo(String deriveColumns, Class<?> deriveClasses, String statement)
	{
		super(deriveColumns, deriveClasses);
		this.statement = statement;
	}
}
