package nbaquery_test.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nbaquery.data.file.query.LinkedListNode;
import nbaquery.data.file.query.RadixSort;

public class TestRadixSort
{
	LinkedListNode dummyHead;
	
	@Before
	public void setupNodes()
	{
		dummyHead = new LinkedListNode();
		
		for(int i = 0; i < 30000; i ++)
		{ 
			LinkedListNode node = new LinkedListNode();
			node.keyword = (int) ((Math.random() - 1.0f) * 100000);
			node.next = dummyHead.next;
			dummyHead.next = node;
		}
	}
	
	long elapse;
	long nanoElapse;
	
	@Test
	public void main()
	{
		long begin = System.currentTimeMillis();
		long beginNano = System.nanoTime();
		RadixSort.sort(dummyHead);
		elapse = System.currentTimeMillis() - begin;
		nanoElapse = System.nanoTime() - beginNano;
	}
	
	@After
	public void print()
	{
		LinkedListNode radixSortResult = dummyHead.next;
		while(radixSortResult != null)
		{
			System.out.println(radixSortResult.keyword);
			radixSortResult = radixSortResult.next;
		}
		System.out.println("elapsed time: " + elapse + " ms / " + nanoElapse + " ns.");
	}
}
