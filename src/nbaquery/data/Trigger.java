package nbaquery.data;

/**
 * A trigger is used to ensure database integrity by
 * correcting data if things go wrong.
 * When using, the condition will be checked at first
 * and if false, the correction will be done.
 * @author luohaoran
 */

public interface Trigger
{
	public void retrieve(Table table);
	
	public boolean checkCondition(Row row);
	
	public void doCorrection(Row row);
}
