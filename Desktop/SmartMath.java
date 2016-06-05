import java.util.HashMap;
import java.util.Stack;
import java.util.Set;
import java.util.Scanner;
public class SmartMath{
	Stack<Double> numberStack; //stack for storing double numbers
	Stack<Character> operatorStack; //stack for storing operators
	HashMap<Character, Integer> priorityMap; //map for priority
	Set<Character> operatorSet;
	/* setting up inital state */
	void Init(){
		numberStack=new Stack<Double>();
		operatorStack=new Stack<Character>();
		priorityMap=new HashMap<Character, Integer>();
		priorityMap.put('+', 1);
		priorityMap.put('-', 1);
		priorityMap.put('*', 2);
		priorityMap.put('/', 2);
		priorityMap.put('^', 3);
		priorityMap.put('(', 4);
		priorityMap.put(')', 0);
		operatorSet=priorityMap.keySet();
	}
	/*parse the expression and do the math, with help from doCalc() */
	double doMath(String s){
		Init();
		String tempNumber=""; //the variable used to store temporarily numbers more than 1 digit
		for (int idx=0; idx<s.length(); idx++){
			Character mychar=s.charAt(idx);
			if (mychar==' ')
				continue;
			if (operatorSet.contains(mychar)){
				if (tempNumber.length()!=0){
					try{
						numberStack.push(Double.parseDouble(tempNumber));
						tempNumber="";
					}catch(Exception e){
						System.err.println("unrecognized character found! please check and remove.");
						System.exit(1);
					}
				}					
				doCalc(mychar);
			}
			else{
				tempNumber+=mychar; //store 'number' char into tempNubmer
				/* for treatment of the last number in the input string,
				 * I don't know how to optimized the code now, looks silly but works.
				 */
				if(idx==s.length()-1&&tempNumber.length()!=0){
					try{
						numberStack.push(Double.parseDouble(tempNumber));
						tempNumber="";
					}catch(Exception e){
						System.err.println("unrecognized character found! please check and remove.");
						System.exit(1);
					}
				}			
			}
		}
		//if there is any operator in operatorStack, do the rest of calculation; 
		//the operatorStack should be "()" free now
		while(!operatorStack.isEmpty())
			numberStack.push(basicCal(numberStack.pop(), numberStack.pop(), operatorStack.pop())); //last step of calculaiton;
		return numberStack.pop();
	}
	/*check the stack, and do calculation based on operator priority*/
	void doCalc(Character operator){
		if(operatorStack.isEmpty()){
			operatorStack.push(operator);
			return;
		}
		Character lastOperator=operatorStack.peek();
		if (priorityMap.get(operator)>priorityMap.get(lastOperator)){
			operatorStack.push(operator);
			return;
		}
		if(operator.equals(')')){
			while(!operatorStack.peek().equals('(')) //one ')' must remove its paired '('
				numberStack.push(basicCal(numberStack.pop(), numberStack.pop(), operatorStack.pop()));
			operatorStack.pop(); //remove that '('
			return;
		}
		if(priorityMap.get(operator)<=priorityMap.get(lastOperator)){
			if (lastOperator.equals('('))
				operatorStack.push(operator);
			else{
				numberStack.push(basicCal(numberStack.pop(), numberStack.pop(), operatorStack.pop()));
				operatorStack.push(operator);
			}
		}
	}
	/*do calculaiton, based on two numbers and a operator*/		
	double basicCal(double a, double b, char operator){
		switch(operator){
			case '^': return Math.pow(b,a);
			case '+': return b+a;
			case '-': return b-a;
			case '*': return b*a;
			case '/': return b/a;
			default: return 0.0;
		}
	}
	public static void main(String args[]){
		System.out.println("Please input your mathmetical expression: ");
		Scanner scanner=new Scanner(System.in);
		String yourExpression=scanner.next();
		SmartMath smartMath=new SmartMath();
		//String mys="4+(3+2)*6/(2^3-5)+(4*(9-7))";
		//System.out.println(smartMath.doMath(mys));
		//String mys2="14+4-10*(4.5-1)";
		//System.out.println(smartMath.doMath(mys2));
		System.out.println(smartMath.doMath(yourExpression));
	}
}