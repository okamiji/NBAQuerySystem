package nbaquery.data.sql;

import java.sql.PreparedStatement;
import java.util.HashMap;

import nbaquery.data.Table;
import nbaquery.data.Trigger;

/**
 * This row can only be retrieved by calling createSqlRow method of MutableSqlTable
 * @author luohaoran
 */
public class MutableSqlRow implements SqlTableRow
{
	public final PreparedStatement statement;
	public final Object[] creations;
	public final SqlObjectConverter<?>[] converters;
	public MutableSqlTable declaredTable;
	
	public static final HashMap<PreparedStatement, Thread> batchUpdateThread
		= new HashMap<PreparedStatement, Thread>();
	
	public MutableSqlRow(MutableSqlTable table, PreparedStatement statement, int attributes)
	{
		this.declaredTable = table;
		this.statement = statement;
		this.creations = new Object[attributes];
		this.converters = new SqlObjectConverter<?>[attributes];
	}

	@Override
	public Table getDeclaredTable() {
		return this.declaredTable;
	}
	
	@Override
	public Object getAttribute(int index, SqlObjectConverter<?> converter) {
		return converter.convertObjectDirect(creations[index - 1]);
	}
	
	@Override
	public void setAttribute(int index, SqlObjectConverter<?> converter, Object value) {
		if(this.converters[index - 1] == null)
			this.converters[index - 1] = converter;
		this.creations[index - 1] = value;
	}

	/**
	 * Only when the submit method is called will the result be submit to the sql
	 * server.
	 */
	public void submit()
	{
		try
		{
			for(Trigger trigger : this.declaredTable.triggers)
			{
				if(trigger.checkCondition(this)) continue;
				trigger.doCorrection(this);
			}
			
			synchronized(this.statement)
			{
				this.statement.clearParameters();
				for(int i = 0; i < this.creations.length; i ++) try
				{
					this.converters[i].write(statement, i + 1, this.creations[i]);
				}
				catch(NullPointerException e)
				{
					System.err.println("insertion null at " + i + " of table " + declaredTable.getTableName());
				}
				this.statement.addBatch();
			}
			
			if(batchUpdateThread.get(statement) == null)
			{
				Thread batchUpdate = new Thread()
				{
					@Override
					public void run()
					{
						try
						{	
							Thread.sleep(100);
						}
						catch(Exception e)
						{
						}
						try
						{
							statement.executeBatch();
						}
						catch (Exception e) {
							System.err.println("Error occurs while operating on ".concat(declaredTable.getTableName()));
							e.printStackTrace();
						}
						batchUpdateThread.remove(statement);
						declaredTable.notifier.clear();
					}
				};
				batchUpdateThread.put(this.statement, batchUpdate);
				batchUpdate.start();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public Object[] getAttributes() {
		return this.creations;
	}
	
}
