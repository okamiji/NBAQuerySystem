package nbaquery.data.file.query;

public class RadixSort
{
	public static void sort(LinkedListNode dummyHead)
	{
		if(dummyHead == null) return;
		if(dummyHead.next == null) return;
		
		final int length = Integer.SIZE / 8;
		for(int i = 0; i < length; i ++)
		{
			LinkedListNode iterator = dummyHead.next;
			
			LinkedListNode[] bucketHead = new LinkedListNode[0x0100];
			LinkedListNode[] bucketTail = new LinkedListNode[0x0100];
			
			while(iterator != null)
			{
				LinkedListNode nextNode = iterator.next;
				int bucketIndex = 0x00FF & (iterator.keyword >> (i << 3));
				if(i == length - 1) bucketIndex = 0x00FF - bucketIndex;	//Sign bit process.
				
				if(bucketHead[bucketIndex] != null)
				{
					bucketTail[bucketIndex].next = iterator;
					bucketTail[bucketIndex] = iterator;
					iterator.next = null;
				}
				else
				{
					bucketHead[bucketIndex] = iterator;
					bucketTail[bucketIndex] = iterator;
					iterator.next = null;
				}
				iterator = nextNode;
			}
			
			dummyHead.next = null;
			LinkedListNode tailNode = null;
			
			for(int j = 0; j < 0x0100; j ++) if(bucketHead[j] != null)
			{
				if(dummyHead.next == null) dummyHead.next = bucketHead[j];
				if(tailNode != null) tailNode.next = bucketHead[j];
				tailNode = bucketTail[j];
			}
			
			if(dummyHead.next.isOrdered()) break;
		}
	}
}
