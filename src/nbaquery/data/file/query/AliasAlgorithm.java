package nbaquery.data.file.query;

import nbaquery.data.Table;
import nbaquery.data.file.AliasedTable;
import nbaquery.data.file.KeywordTable;
import nbaquery.data.query.AliasingQuery;
import nbaquery.data.query.Query;

public class AliasAlgorithm implements FileTableAlgorithm
{
	@Override
	public Table perform(Query query) {
		AliasingQuery aliasing = (AliasingQuery)query;
		return new AliasedTable((KeywordTable) aliasing.table,
				aliasing.columnNames, aliasing.aliases);
	}

	@Override
	public Class<? extends Query> getProcessingQuery() {
		return AliasingQuery.class;
	}
}
