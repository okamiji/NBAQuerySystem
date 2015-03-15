package nbaquery.data.file.query;

import nbaquery.data.Table;
import nbaquery.data.query.Query;

public interface FileTableAlgorithm
{
	public Table perform(Query query);
	
	public Class<? extends Query> getProcessingQuery();
}
