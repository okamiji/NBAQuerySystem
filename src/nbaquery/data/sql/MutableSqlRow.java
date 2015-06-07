package nbaquery.data.sql;

import java.sql.PreparedStatement;

import nbaquery.data.Table;

/**
 * This row can only be retrieved by calling createSqlRow method of MutableSqlTable
 * @author luohaoran
 */
public class MutableSqlRow implements SqlTableRow
{
	public final PreparedStatement statement;
	public final Object[] creations;
	public final Class<?>[] dataClasses;
	public int toMutate;
	public Table declaredTable;
	
	public MutableSqlRow(MutableSqlTable table, PreparedStatement statement, int attributes)
	{
		this.declaredTable = table;
		this.statement = statement;
		this.toMutate = attributes;
		this.creations = new Object[attributes];
		this.dataClasses = new Class<?>[attributes];
	}

	@Override
	public Table getDeclaredTable() {
		return this.declaredTable;
	}
	
	@Override
	public Object getAttribute(int index) {
		return creations[index - 1];
	}
	
	@Override
	public void setAttribute(int index, Class<?> dataClass, Object value) {
		if(this.dataClasses[index - 1] == null)
		{
			this.dataClasses[index - 1] = dataClass;
			toMutate --;
		}
		this.creations[index - 1] = value;
		
		if(toMutate == 0) synchronized(this.statement)
		{
			try
			{
				for(int i = 0; i < this.creations.length; i ++)
					SqlObjectConverter.converters.get(this.dataClasses[i])
						.write(statement, i + 1, this.creations[i]);
					this.statement.executeQuery();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public Object[] getAttributes() {
		return this.creations;
	}
	
}
