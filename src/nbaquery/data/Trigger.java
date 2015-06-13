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
	
	
	public static final Trigger board = new Trigger()
	{

		Column attack_board, defence_board, total_board;
		
		@Override
		public void retrieve(Table table) {
			attack_board = table.getColumn("attack_board");
			defence_board = table.getColumn("defence_board");
			total_board = table.getColumn("total_board");
		}

		@Override
		public boolean checkCondition(Row row)
		{
			return (Integer)attack_board.getAttribute(row) + (Integer)defence_board.getAttribute(row) == (Integer)total_board.getAttribute(row);
		}

		@Override
		public void doCorrection(Row row)
		{
			total_board.setAttribute(row, (Integer)attack_board.getAttribute(row) + (Integer)defence_board.getAttribute(row));
		}
		
	};
}
