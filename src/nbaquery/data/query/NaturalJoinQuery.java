package nbaquery.data.query;

import nbaquery.data.Table;

public class NaturalJoinQuery implements Query
{
	public Table leftTable;
	public Table rightTable;
	public String[] joinColumnLeft;
	public String[] joinColumnRight;
	
	public NaturalJoinQuery(Table leftTable, Table rightTable, String[] joinColumnLeft, String[] joinColumnRight)
	{
		this.leftTable = leftTable;
		this.rightTable = rightTable;
		this.joinColumnLeft = joinColumnLeft;
		this.joinColumnRight = joinColumnRight;
	}
}
