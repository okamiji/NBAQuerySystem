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
	
	public static final Trigger self_score = new Trigger()
	{
		Column self_score;
		Column foul_shoot_score;
		Column shoot_score;
		Column three_shoot_score;
		
		@Override
		public void retrieve(Table table) {
			self_score = table.getColumn("self_score");
			foul_shoot_score = table.getColumn("foul_shoot_count");
			shoot_score = table.getColumn("shoot_count");
			three_shoot_score = table.getColumn("three_shoot_count");
		}

		@Override
		public boolean checkCondition(Row row)
		{
			return 1 * (Integer)foul_shoot_score.getAttribute(row) + 2 * (Integer)shoot_score.getAttribute(row)
					+ 3 * (Integer)three_shoot_score.getAttribute(row) == (Integer)self_score.getAttribute(row); 
		}

		@Override
		public void doCorrection(Row row)
		{
			self_score.setAttribute(row, 1 * (Integer)foul_shoot_score.getAttribute(row) + 2 * (Integer)shoot_score.getAttribute(row)
					+ 3 * (Integer)three_shoot_score.getAttribute(row)); 
		}
	};	
}
