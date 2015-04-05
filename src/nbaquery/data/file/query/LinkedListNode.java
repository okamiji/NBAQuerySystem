package nbaquery.data.file.query;

public class LinkedListNode
{
	public long keyword;
	public LinkedListNode next;
	
	public boolean isOrdered()
	{
		LinkedListNode iterator = this.next;
		long previousKeyword = keyword;
		while(iterator != null)
		{
			if(previousKeyword > iterator.keyword) return false;
			previousKeyword = iterator.keyword;
			iterator = iterator.next;
		}
		return true;
	}
}
