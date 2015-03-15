package nbaquery_test.data;

import nbaquery.data.Column;
import nbaquery.data.Row;
import nbaquery.data.Table;
import nbaquery.data.file.KeywordTable;
import nbaquery.data.file.MultivaluedTable;
import nbaquery.data.file.Tuple;
import nbaquery.data.file.query.SetOperationAlgorithm;
import nbaquery.data.query.SetOperation;

import org.junit.Before;
import org.junit.Test;

public class TestSetOperations
{
	SetOperationAlgorithm algorithm;
	MultivaluedTable mvt;
	MultivaluedTable mvt2;
	
	KeywordTable kwt;
	KeywordTable kwt2;
	
	@Before
	public void setUp() throws Exception
	{
		System.out.println("Check");
		
		//-----------------------------------------------------------------------
		
		mvt = new MultivaluedTable(null, new String[]{"A", "B"}, new Class<?>[]{String.class, String.class});
		Column a = mvt.getColumn("A");	Column b = mvt.getColumn("B");
		Tuple tup = mvt.createTuple();
		a.setAttribute(tup, "X");	b.setAttribute(tup, "Y");
		
		tup = mvt.createTuple();
		a.setAttribute(tup, "Z");	b.setAttribute(tup, "W");
		
		System.out.println("mvt");
		for(Row row : mvt.getRows()) System.out.println(a.getAttribute(row) + " " + b.getAttribute(row));
		
		//-----------------------------------------------------------------------
		
		mvt2 = new MultivaluedTable(null, new String[]{"A", "B"}, new Class<?>[]{String.class, String.class});
		Column a2 = mvt2.getColumn("A");	Column b2 = mvt2.getColumn("B");
		
		tup = mvt2.createTuple();
		a2.setAttribute(tup, "M"); b2.setAttribute(tup, "N");
		
		System.out.println("mvt2");
		for(Row row : mvt2.getRows()) System.out.println(a2.getAttribute(row) + " " + b2.getAttribute(row));
		
		//-----------------------------------------------------------------------
		
		kwt = new KeywordTable(null, new String[]{"A", "B"}, new Class<?>[]{String.class, String.class}, "A");
		Column a3 = kwt.getColumn("A");		Column b3 = kwt.getColumn("B");
		
		tup = kwt.createTuple();
		a3.setAttribute(tup, "R"); 		b3.setAttribute(tup, "S");
		
		tup = kwt.createTuple();
		a3.setAttribute(tup, "Q"); 		b3.setAttribute(tup, "R");
		
		//-----------------------------------------------------------------------
		
		kwt2 = new KeywordTable(null, new String[]{"A", "B"}, new Class<?>[]{String.class, String.class}, "A");
		Column a4 = kwt2.getColumn("A");		Column b4 = kwt2.getColumn("B");
		
		tup = kwt2.createTuple();
		a4.setAttribute(tup, "R"); 		b4.setAttribute(tup, "S");
		
		tup = kwt2.createTuple();
		a4.setAttribute(tup, "L"); 		b4.setAttribute(tup, "M");
		
		algorithm = new SetOperationAlgorithm();
	}
	
	@Test
	public void test()
	{
		SetOperation setop = new SetOperation(mvt, mvt2, SetOperation.EnumSetOperation.INTERSECTION);
		
		Table table = algorithm.perform(setop);
		Column a_res = table.getColumn("A"); Column b_res = table.getColumn("B");
		
		System.out.println("result(intersection)");
		for(Row row : table.getRows()) System.out.println(a_res.getAttribute(row) + " " + b_res.getAttribute(row));
		
		setop.operation = SetOperation.EnumSetOperation.UNION;
		table = algorithm.perform(setop);
		a_res = table.getColumn("A"); b_res = table.getColumn("B");
		System.out.println("result(union)");
		for(Row row : table.getRows()) System.out.println(a_res.getAttribute(row) + " " + b_res.getAttribute(row));
		
		setop.operation = SetOperation.EnumSetOperation.COMPLEMENT;
		table = algorithm.perform(setop);
		a_res = table.getColumn("A"); b_res = table.getColumn("B");
		System.out.println("result(complement)");
		for(Row row : table.getRows()) System.out.println(a_res.getAttribute(row) + " " + b_res.getAttribute(row));
		
		setop.leftHand = kwt; setop.rightHand = kwt2;
		
		setop.operation = SetOperation.EnumSetOperation.UNION;
		table = algorithm.perform(setop);
		a_res = table.getColumn("A"); b_res = table.getColumn("B");
		System.out.println("result2(union)");
		for(Row row : table.getRows()) System.out.println(a_res.getAttribute(row) + " " + b_res.getAttribute(row));
		
		setop.operation = SetOperation.EnumSetOperation.INTERSECTION;
		table = algorithm.perform(setop);
		a_res = table.getColumn("A"); b_res = table.getColumn("B");
		System.out.println("result2(intersection)");
		for(Row row : table.getRows()) System.out.println(a_res.getAttribute(row) + " " + b_res.getAttribute(row));
		
		setop.operation = SetOperation.EnumSetOperation.COMPLEMENT;
		table = algorithm.perform(setop);
		a_res = table.getColumn("A"); b_res = table.getColumn("B");
		System.out.println("result2(complement)");
		for(Row row : table.getRows()) System.out.println(a_res.getAttribute(row) + " " + b_res.getAttribute(row));
	}
}
