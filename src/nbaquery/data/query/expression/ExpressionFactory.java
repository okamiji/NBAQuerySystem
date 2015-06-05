package nbaquery.data.query.expression;

import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeMap;

import nbaquery.data.Column;
import nbaquery.data.Table;
import nbaquery.data.TableHost;

public class ExpressionFactory
{
	int[] partitionList = new int[0x7F];
	final int NORMAL_SEQUENCE = 0;
	final int BLANK_SEQUENCE = 1;
	final int OPERATOR_SEQUENCE = 2;
	final int QUOTE_SEQUENCE = 3;
	final int PARENTHESIS_SEQUENCE = 4;
	{
		for(int i = 0; i < 0x7F; i ++)
			partitionList[i] = NORMAL_SEQUENCE;
		char[] blanks = new char[]{' ', '\t', '\n', '\b', '\0'};
		for(int i = 0; i < blanks.length; i ++) partitionList[blanks[i]] = BLANK_SEQUENCE;
		
		char[] operators = new char[]{'&', '|', '!', '+', '-', '*', '/', '=', '>', '<'};
		for(int i = 0; i < operators.length; i ++) partitionList[operators[i]] = OPERATOR_SEQUENCE;
		
		char[] parenthesis = new char[]{'(', ')', '[', ']', '{', '}'};
		for(int i = 0; i < parenthesis.length; i ++) partitionList[parenthesis[i]] = PARENTHESIS_SEQUENCE;
		
		partitionList['\''] = QUOTE_SEQUENCE;
		partitionList['\"'] = QUOTE_SEQUENCE;
	}
	
	protected ExpressionFactory()	{	}
	
	public Operator parse(TableHost tableHost, Table[] enrolledTables, String expression) throws Exception
	{
		String[] analysis = this.lexicalAnalyse(expression);
		ArrayList<String> preprocessed = new ArrayList<String>();
		boolean shouldAppendCheck = true;
		String prefix = null;
		for(String analyse : analysis)
		{
			if(shouldAppendCheck)
			{
				shouldAppendCheck = false;
				if(analyse.equals("+") || analyse.equals("-") || analyse.equals("!"))
				{
					prefix = analyse;
					continue;
				}
			}
			if(prefix != null)
			{
				preprocessed.add(prefix + analyse);
				prefix = null;
			}
			else preprocessed.add(analyse);
			if(analyse.equals("(")) shouldAppendCheck = true;
			else
			{
				Class<? extends Operator> clazz = classInstantializer.get(analyse);
				if(clazz == null) continue;
				if(BinaryOperator.class.isAssignableFrom(clazz)) shouldAppendCheck = true;
			}
				
		}
		Operator[] reverse = toReversePolishForm(tableHost, enrolledTables, preprocessed.toArray(new String[0]));
		
		Stack<Operator> workingStack = new Stack<Operator>();
		for(int i = 0; i < reverse.length; i ++)
		{
			if(		reverse[i] instanceof ConstantOperator
				||	reverse[i] instanceof ColumnOperator) workingStack.push(reverse[i]);
			else if(reverse[i] instanceof BinaryOperator)
			{
				Operator rightOperand = workingStack.pop();
				Operator leftOperand = workingStack.pop();
				BinaryOperator binOperator = (BinaryOperator)reverse[i];
				binOperator.leftHand = leftOperand;
				binOperator.rightHand = rightOperand;
				Operator result;
				if(leftOperand instanceof ConstantOperator && rightOperand instanceof ConstantOperator)
				{
					Object resultValue = binOperator.calculate();
					ConstantOperator constOperator = new ConstantOperator();
					constOperator.constant = resultValue; 
					result = constOperator;
				}
				else result = binOperator;
				workingStack.push(result);
			}
		}
		
		return workingStack.get(0);
	}
	
	public String[] lexicalAnalyse(String string)
	{
		char[] chars = string.toCharArray();
		boolean insideQuote = false;
		int state = -1;	StringBuilder builder = new StringBuilder();
		ArrayList<String> splitter = new ArrayList<String>();
		for(int i = 0; i < chars.length; i ++) if(insideQuote)
		{
			builder.append(chars[i]);
			if(partitionList[chars[i]] == QUOTE_SEQUENCE)
			{
				insideQuote = false;
				splitter.add(new String(builder));
				builder = new StringBuilder();
			}
		}
		else if(partitionList[chars[i]] != BLANK_SEQUENCE)
		{
			if(state == -1) state = partitionList[chars[i]];
			if(state == PARENTHESIS_SEQUENCE) state = -1;
			if(state != partitionList[chars[i]])
			{
				if(builder.length() > 0)
				{
					splitter.add(new String(builder));
					builder = new StringBuilder();
				}
				state = partitionList[chars[i]];
			}
			builder.append(chars[i]);
			if(partitionList[chars[i]] == QUOTE_SEQUENCE) insideQuote = true;
		}
		else
		{
			if(builder.length() > 0)
			{
				splitter.add(new String(builder));
				builder = new StringBuilder();
				state = -1;
			}
		}
		
		if(builder.length() > 0) splitter.add(new String(builder));
		return splitter.toArray(new String[0]);
	}
	
	TreeMap<String, Integer> incomingPriority = new TreeMap<String, Integer>();
	TreeMap<String, Integer> instackPriority = new TreeMap<String, Integer>();
	TreeMap<String, Class<? extends Operator>> classInstantializer = new TreeMap<String, Class<? extends Operator>>();
	
	{
		putRPNConvertInfo(12, 1, null, "(");
		putRPNConvertInfo(1, 12, null, ")");
		putRPNConvertInfo(0, 0, null, "");
		
		putRPNConvertInfo(2, 3, LogicOperator.And.class, "&", "&&", "and");
		putRPNConvertInfo(2, 3, LogicOperator.Or.class, "|", "||", "or");
		putRPNConvertInfo(2, 3, LogicOperator.Not.class, "!", "not");
		
		putRPNConvertInfo(4, 5, CompareOperator.Equal.class, "=", "==", "in");
		putRPNConvertInfo(4, 5, CompareOperator.NotEqual.class, "!=", "<>");
		putRPNConvertInfo(4, 5, CompareOperator.Greater.class, ">");
		putRPNConvertInfo(4, 5, CompareOperator.GreaterEq.class, ">=");
		putRPNConvertInfo(4, 5, CompareOperator.Less.class, "<");
		putRPNConvertInfo(4, 5, CompareOperator.LessEq.class, "<=");
		
		putRPNConvertInfo(6, 7, ArithmeticOperator.Add.class, "+");
		putRPNConvertInfo(6, 7, ArithmeticOperator.Sub.class, "-");
		
		putRPNConvertInfo(8, 9, ArithmeticOperator.Mult.class, "*");
		putRPNConvertInfo(8, 9, ArithmeticOperator.Div.class, "/");
		
		putRPNConvertInfo(10, 11, ArithmeticOperator.Pow.class, "**");
	}
	
	void putRPNConvertInfo(int incoming, int instack, Class<? extends Operator> inst, String... notations)
	{
		for(int i = 0; i < notations.length; i ++)
		{
			incomingPriority.put(notations[i], incoming);
			instackPriority.put(notations[i], instack);
			classInstantializer.put(notations[i], inst);
		}
	}
	
	public Operator[] toReversePolishForm(TableHost tableHost, Table[] enrolledTables, String[] units) throws Exception
	{
		ArrayList<Operator> reversedPolishForm = new ArrayList<Operator>();
		String[] stack = new String[units.length];
		
		int i = 0, j = 0;
		stack[j] = "";
		
		while(! (i >= units.length && "".equals(stack[j])))
		{
			String currentPointerLowered;
			String currentPointer;
			if(i < units.length)
			{
				currentPointer = units[i];
				currentPointerLowered = currentPointer.toLowerCase();
			}
			else
			{
				currentPointer = "";
				currentPointerLowered = "";
			}
			
			Integer incomingPriority = this.incomingPriority.get(currentPointerLowered);
			Integer instackPriority = this.instackPriority.get(stack[j]);
			
			if(incomingPriority == null)
			{
				if(currentPointer.matches("[+|-]?[0-9]+"))
				{
					ConstantOperator constant = new ConstantOperator();
					constant.constant = Integer.parseInt(currentPointerLowered);
					reversedPolishForm.add(constant);
				}
				else if(currentPointer.matches("[+|-]?[0-9]*[.][0-9]*[F|f]?"))
				{
					ConstantOperator constant = new ConstantOperator();
					constant.constant = Float.parseFloat(currentPointerLowered);
					reversedPolishForm.add(constant);
				}
				else if(currentPointer.matches("\'.*\'") || currentPointer.matches("\".*\""))
				{
					ConstantOperator constant = new ConstantOperator();
					String theString = currentPointer.substring(1, currentPointer.length() - 1);
					if(theString.length() > 1) constant.constant = theString;
					else if(theString.length() == 1) constant.constant = theString.charAt(0);
					else constant.constant = '\0';
					reversedPolishForm.add(constant);
				}
				else if(currentPointerLowered.equals("true"))
				{
					ConstantOperator constant = new ConstantOperator();
					constant.constant = true;
					reversedPolishForm.add(constant);
				}
				else if(currentPointerLowered.equals("false"))
				{
					ConstantOperator constant = new ConstantOperator();
					constant.constant = false;
					reversedPolishForm.add(constant);
				}
				else if(currentPointerLowered.equals("null"))
				{
					ConstantOperator constant = new ConstantOperator();
					constant.constant = null;
					reversedPolishForm.add(constant);
				}
				else if(currentPointerLowered.equals("+inf"))
				{
					ConstantOperator constant = new ConstantOperator();
					constant.constant = Float.POSITIVE_INFINITY;
					reversedPolishForm.add(constant);
				}
				else if(currentPointerLowered.equals("-inf"))
				{
					ConstantOperator constant = new ConstantOperator();
					constant.constant = Float.NEGATIVE_INFINITY;
					reversedPolishForm.add(constant);
				}
				else if(currentPointerLowered.equals("NaN"))
				{
					ConstantOperator constant = new ConstantOperator();
					constant.constant = Float.NaN;
					reversedPolishForm.add(constant);
				}
				else
				{
					Column column = null;
					if(currentPointerLowered.matches(".+[.].+"))
						column = tableHost.getColumn(currentPointerLowered);
					else 
					{
						for(Table table : enrolledTables)
						{
							column = table.getColumn(currentPointerLowered);
							if(column != null) break;
						}
					}
					if(column == null) throw new Exception("Unknown symbol " + currentPointerLowered);
					ColumnOperator columnOperator = new ColumnOperator();
					columnOperator.column = column;
					reversedPolishForm.add(columnOperator);
				}
				i ++;
			}
			else
			{
				if(incomingPriority > instackPriority)
				{
					j ++; stack[j] = currentPointerLowered;
					i ++;
				}
				else if(incomingPriority == instackPriority)
				{
					if("(".equals(stack[j])) i ++;
					j --;
				}
				else
				{
					Operator operator = classInstantializer.get(stack[j]).getConstructor().newInstance();
					reversedPolishForm.add(operator); j --;
				}
			}
		}
		return reversedPolishForm.toArray(new Operator[0]);
	}

	private static final ExpressionFactory instance = new ExpressionFactory();
	
	public static ExpressionFactory getInstance()
	{
		return instance;
	}
}

