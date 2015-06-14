package nbaquery.data.file;

import java.util.Collection;
import java.util.TreeMap;

import nbaquery.data.Column;
import nbaquery.data.Cursor;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.TableHost;

public class AliasedTable extends KeywordTable
{
	public final KeywordTable sourceTable;
	public final TreeMap<String, FileTableColumn> columns = new TreeMap<String, FileTableColumn>();
	public final FileTableColumn aliasedKeyword;
	
	public AliasedTable(KeywordTable sourceTable, String[] columns, String[] aliases){
		super((FileTableHost) sourceTable.getTableHost(), new String[0], new Class<?>[0], null);
		this.sourceTable = sourceTable;
		
		FileTableColumn keyword = null;
		for(int i = 0; i < columns.length; i ++)
		{
			Column theColumn = sourceTable.getColumn(columns[i]);
			if(theColumn == null) continue;
			AliasedColumn result = new AliasedColumn(this, (FileTableColumn) theColumn, aliases[i]);
			this.columns.put(aliases[i].toLowerCase(), result);
			if(theColumn instanceof KeywordColumn) keyword = result;
		}
		aliasedKeyword = keyword;
	}

	@Override
	public Collection<FileTableColumn> getColumns() {
		return this.columns.values();
	}

	@Override
	public Cursor getRows() {
		return this.sourceTable.getRows();
	}

	@Override
	public FileTableColumn getColumn(String columnName) {
		return this.columns.get(columnName);
	}

	@Override
	public TableHost getTableHost() {
		return sourceTable.getTableHost();
	}

	@Override
	public boolean hasTableChanged(Object accessor) {
		return sourceTable.hasTableChanged(accessor);
	}
	
	public class AliasedColumn extends FileTableColumn
	{
		public final AliasedTable aliasTable;
		public final Column column;
		public final String alias;
		
		public AliasedColumn(AliasedTable aliasTable, FileTableColumn column, String alias)
		{
			super(column.getDeclaringTable(), column.getDataClass(), column.columnIndex, column.getColumnName());
			this.aliasTable = aliasTable;
			this.column = column;
			this.alias = alias.toLowerCase();
		}
		
		@Override
		public String getColumnName() {
			return this.alias;
		}

		@Override
		public Class<?> getDataClass() {
			return this.column.getDataClass();
		}

		@Override
		public Table getDeclaringTable() {
			return this.aliasTable;
		}

		@Override
		public Object getAttribute(Row row) {
			return column.getAttribute(row);
		}

		@Override
		public void setAttribute(Row row, Object value) {
			column.setAttribute(row, value);
		}
	}

	@Override
	public FileTableColumn getKeyword() {
		return this.aliasedKeyword;
	}
}
