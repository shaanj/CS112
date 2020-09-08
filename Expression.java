package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	expr = expr.replaceAll(" ", "");
    	expr = expr.replaceAll("\t", "");
    	//getting rid of the empty space and the \t in delims since they are not integral to the problem
    	
    	ArrayList<String> StrAL = new ArrayList<String>();
    	String str; //string to add tokens to.
    	StringTokenizer Stokes = new StringTokenizer(expr, " +-/*()[]", true); 
    	
    	while(Stokes.hasMoreTokens()) {
    		str = Stokes.nextToken();
    		Character chara = str.charAt(0);
    		StrAL.add(str);
    	}
    	
    		//for(int i = 0; i < StrAL.length(); i++) {
    			//	System.out.println(StrAL.get(i));
    			//System.out.println();
    			//print for loop and print statements to see contents of the ArrayList
    	//}
    	
    	for(int i = 0; i < StrAL.size(); i++) {
    		str = StrAL.get(i);
    		Character chara = str.charAt(0);
    		
    		if(!(Character.isUpperCase(chara) || Character.isLowerCase(chara)))
    		{
    			continue;
    		}
    			//checking to see if the character at the first index is a letter
    			//if not, continue 
    		
    		int k = i+1;
    		
    		if(k < StrAL.size())
    		{
    			if(StrAL.get(k).equals("[") && !arrays.contains(new Array(str))) 
    			{
    				arrays.add(new Array(str));		
    			}
    			// adding new arrays into the array ArrayList
    			//only happens when the opening bracket, [, is used at the
    			
    			
    			else if (!vars.contains(new Variable(str))) 
    			{
    				vars.add(new Variable(str));
    			}
    		} 
    		// adding new vars into the variables ArrayList
    		else if (!vars.contains(new Variable(str))) 
    		{
    			vars.add(new Variable(str));
    			//same exact code as above
    		}
    	}
    	
    	//System.out.println(Arrays: ")
        //for(int i = 0; i < arrays.size(); i++) {
		//	System.out.println(arrays.get(i));
    	// printing the contents of the array ArrayList
    	
    	//System.out.println(Vars: ")
        //for(int i = 0; i < vars.size(); i++) {
		//	System.out.println(vars.get(i));
    	// printing the contents of the variables ArrayList
    	
    }	
    	
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	expr = expr.replaceAll(" ", "");
    	expr = expr.replaceAll("\t", "");
    	//same as the comment in the initial method
    	
    	ArrayList<String> iht = new ArrayList<String>();
    	String str1;
    	StringTokenizer Stokes = new StringTokenizer(expr, " +-/*()[]", true); 
    	
    	while(Stokes.hasMoreTokens()) 
    	{
    		str1 = Stokes.nextToken();
    		Character chara = str1.charAt(0);
    		iht.add(str1);
    	}
    	
    	float answer = 0;
    	answer = Arreval(iht, vars, arrays);
    	return answer;
    	
    }
    	
    
    
    private static float Arreval(ArrayList<String> iht, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	int i, j, a, b;
    	float answer = 0;
    	String str1;
    	String str2;
    	ArrayList<String> last = new ArrayList<String>();
    	
    	for(i = 0; i < iht.size(); i++) {
    		a = i + 1;
    		str1 = iht.get(i);
    		
    		if(a < iht.size()) 
    		{
    			if(iht.get(a).equals("(")) 
    			{
    				int open = 0;
    				int closed = 0;
    				//variables to count the amount of open and closed parentheses/brackets in the expression
    				
    				for(j = a; j < iht.size(); j++) {
    					
    					str2 = iht.get(j);
    					
    					if(str2.equals("[") || str2.equals("(")) 
    					{
    						open++;
    						//adding 1 to "open" if the start of an array or an expression with () is seen
    					}
    					
    					else if(str2.equals("]") || str2.equals(")")) 
    					{
    						closed++;
    						//adding 1 to "open" if the start of an array or an expression with () is seen
    					}
    					
    					if(open == closed)
    					{
    					break;	
    					}
    				}
    				ArrayList<String> iht2 = new ArrayList<String>(iht.subList(a+1, j));
    				//this line makes a new arraylist
    				//the new arraylist is the size of everything that follows the 
    				//parentheses to the end
    				
    				float hol = Arreval(iht2, vars, arrays);
    				float value = getArrVal(arrays, str1, hol);
    				last.add(Float.toString(value));
    				//adding the new value into the new arraylist made at the start
    				i = j;
    			}
    			else
    			{
    			last.add(str1);	
    			}
    		}
    		else
    		{
    		last.add(str1);	
    		}
    	}
    	
    	Stack<String> ops = new Stack<String>();
    	Stack<String> opers = new Stack<String>();
    	//used to push and pop results of calculations 
    	
    	for(i = 0; i < last.size(); i++) {
    		str1 = last.get(i);
    		
    		if(!isOperator(str1)) {
    			opers.push(str1);
    		}
    		
    		else
    		{
    			if(last.get(i).equals(")"))
    			{
    				while(!ops.peek().equals("(")) 
    				{
    					String newstring2 = opers.pop();
    					String newstring1 = opers.pop();
    					String operator = ops.pop();
    					float tis = calculation(newstring1, operator, newstring2, vars);
    					opers.push(Float.toString(tis));
    					//pushing the result into the opers stack.
    					//using the 2 strings input into the opers stack, the terms.
    					//and the symbol which was pushed into the symbols stack.
    				}
    				ops.pop();
    			}
    			else if(str1.equals("(")) 
    			{
    			ops.push(str1);
    			//pushing the string into the ops stack.
    			}
    			else
    			{
    				while(!ops.isEmpty()) 
    				{
    					String tops = ops.peek();
    					if(!Prio(tops, str1)) 
    					{
    					break;	
    					}
    					String newstring2 = opers.pop();
    					String newstring1 = opers.pop();
    					String operator = ops.pop();
    					float tis = calculation(newstring1, operator, newstring2, vars);
    					opers.push(Float.toString(tis));
    					//same as above
    				}
    				ops.push(str1);
    			}
    		}
    	}
    	
    	while(!ops.isEmpty()) 
    	{
    		String newstring2 = opers.pop();
			String newstring1 = opers.pop();
			String operator = ops.pop();
			float tis = calculation(newstring1, operator, newstring2, vars);
			opers.push(Float.toString(tis));
    	}
    	
    	if(!isNumber(opers.peek())) 
    	{
    		str1 = opers.peek();
    		answer = getVarVal(vars, str1);
    	}
    	
    	else
    	{
    	answer = Float.parseFloat(opers.pop());	
    	}
    	
    	return answer;
    }
    
    
    
    
    private static boolean isNumber(String string) {
    	
    	try 
    	{
    		float give = Float.parseFloat(string);
    	}
    	
    	catch(NumberFormatException no) 
    	{
    		return false;
    	}
    	
    	return true;
    	
    }
    
    
    private static float getArrVal(ArrayList<Array> arrays, String given, float index) 
    {
    	Array arr = new Array(given);
    	float answer = 0;
    	int currindex = (int) index;
    	
    	for(int i = 0; i < arrays.size(); i++) {
    		if(arrays.get(i).equals(arr)) 
    		{
    			answer = arrays.get(i).values[currindex];
    		}
    	}
    	
    	return answer;
    }
    
    	
    private static float calculation (String str1, String given, String str2, ArrayList<Variable> vars) {
    	float a;
    	float b;
    	float calculated = 0;
    	
    	if (isNumber(str1)) 
    	{
    		a = Float.parseFloat(str1);
    	} else {
    		a = getVarVal(vars, str1);
    	}
    	
    	if (isNumber(str2)) 
    	{
    		b = Float.parseFloat(str2);
    	} else {
    		b = getVarVal(vars, str2);
    	}
    	
    	if(given.equals("*")) 
    	{
    		calculated = a*b;
    	}
    	
    	else if(given.equals("+")) 
    	{
    		calculated = a+b;
    	}
    	
    	else if(given.equals("/")) 
    	{
    		calculated = a/b;
    	}
    	
    	else if(given.equals("-")) 
    	{
    		calculated = a-b;
    	}
    	return calculated;
    }
    
    private static float getVarVal(ArrayList<Variable> vars, String given) 
    {
    	Variable V = new Variable(given);
    	float answer = 0;
    	
    	for(int i = 0; i < vars.size(); i++) {
    		if(vars.get(i).equals(V)) 
    		{
    			answer = vars.get(i).value;
    		}
    	}
    	
    	return answer;
    }
    
    private static boolean isOperator(String str) 
    {
    	String operands[] = {"+", "-", "*", "/", "(", ")"};
    	for(int i = 0; i < 6; i++) {
    		if(str.equals(operands[i]))
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    private static boolean Prio(String given1, String given2) 
    {
    	if(given1.equals("(")) 
    	{
    		return false;
    	}
    	
    	String operands[] = new String[] {"*", "/", "+", "-"};
    	int i, j = 0;
    	for(i = 0; i < 4; i++)
    	{
    		if(given1.equals(operands[i])) 
    		{
    			break;
    		}
    	
    	
    	for(j = 0; j < 4; j++)
    	{
    		if(given2.equals(operands[j])) 
    		{
    			break;
    		}
    	

    	}
    }
    	if(i <= j) 
    	{
    		return true;
    	}
    	
    	return false;
}
}