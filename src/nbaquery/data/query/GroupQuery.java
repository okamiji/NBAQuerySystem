package nbaquery.data.query;

import nbaquery.data.Row;
import nbaquery.data.Table;

public class GroupQuery implements Query
{
	public Table table;
	public String collapseColumn[];
	public String derivedColumn[];
	public Class<?> derivedClass[];
	public GroupColumnInfo[] groupColumnInfos;
	
	public GroupQuery(){}
	
	public GroupQuery(Table table, String[] collapseColumn, GroupColumnInfo... groupColumnInfos)
	{
		this.table = table;
		this.collapseColumn = collapseColumn;
		this.groupColumnInfos = groupColumnInfos;
		this.derivedColumn = new String[this.groupColumnInfos.length];
		this.derivedClass = new Class<?>[this.groupColumnInfos.length];
		for(int i = 0; i < this.groupColumnInfos.length; i ++)
		{
			this.derivedColumn[i] = this.groupColumnInfos[i].deriveColumnName;
			this.derivedClass[i] = this.groupColumnInfos[i].deriveColumnClass;
		}
	}
	
	public void retrieve(Table resultTable)
	{
		for(int i = 0; i < groupColumnInfos.length; i ++)
		{
			groupColumnInfos[i].resultColumn = resultTable.getColumn(derivedColumn[i]);
			groupColumnInfos[i].retrieve(table, resultTable);
		}
	}
	
	public void collapse(Row[] rows, Row resultRow)
	{
		for(int i = 0; i < groupColumnInfos.length; i ++)
			groupColumnInfos[i].collapse(rows, resultRow);
	}
}
