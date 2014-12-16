package VideoSorter;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class SearchStringInterpreter {

	public SearchStringInterpreter(){
		
	}
	
	public List<Video> interpretString(String terms, List<Video> videos){
		
		 Stack<String> lexed = stringLex(terms.toLowerCase());
		 Stack<String> postFixed = stringPostfix(lexed);
		 
		 for (String s : postFixed){
			 System.out.println(s);
		 }
		  
		 return evaluatePostfix(postFixed, videos);
	}
	
	private List<Video> evaluatePostfix(Stack<String> terms, List<Video> videos){
		
		List<Video> result = new LinkedList<Video>();
		
		Stack<String> wait = new Stack<String>();
		Stack<Stack<Video>> operandWait = new Stack<Stack<Video>>();
		
		String[] tokens = new String[terms.size()];
		
		int index = 0;
		for (String s : terms){
			tokens[index++] = s;
		}
		
		for (int i = 0; i < tokens.length; i++){
			if (isOperand(tokens[i])){
				wait.push(tokens[i]);
			} else if(isOperator(tokens[i])){
				if (tokens[i].equals("and")){
					if (operandWait.isEmpty()){
						String operand1 = wait.pop();
						String operand2 = wait.pop();
						operandWait.push(new Stack<Video>());
						for (Video v : videos){
							if (v.metaData.anyMatch(operand1) && v.metaData.anyMatch(operand2))
								operandWait.peek().push(v);
						}
					}else{
						String match = wait.pop();
						operandWait.push(new Stack<Video>());
						for (Video v : videos){
							if (v.metaData.anyMatch(match)){
								operandWait.peek().push(v);
							}
						}
						operandWait.push(intersect(operandWait.pop(), operandWait.pop()));
					}
				} else if(tokens[i].equals("or")){
					if (operandWait.isEmpty()){
						String operand1 = wait.pop();
						String operand2 = wait.pop();
						operandWait.push(new Stack<Video>());
						for (Video v : videos){
							if (v.metaData.anyMatch(operand1) || v.metaData.anyMatch(operand2))
								operandWait.peek().push(v);
						}
					} else{
						String match = wait.pop();
						operandWait.push(new Stack<Video>());
						for (Video v : videos){
							if (v.metaData.anyMatch(match)){
								operandWait.peek().push(v);
							}
						}
						operandWait.push(union(operandWait.pop(), operandWait.pop()));
					}
				} else if (tokens[i].equals("not")){
					if (operandWait.isEmpty()){
						String operand = wait.pop();
						operandWait.push(new Stack<Video>());
						for (Video v : videos){
							if (!v.metaData.anyMatch(operand)){
								operandWait.peek().push(v);
							}
						}
					} else{
						operandWait.push(complement(operandWait.pop(), videos));
					}
				}
			}
		}
		
		for (Stack<Video> v : operandWait){
			for (Video q : v){
				result.add(q);
			}
		}
		
		return result;
	}
	
	private Stack<Video> intersect(Stack<Video> a, Stack<Video> b){
		Stack<Video> result = new Stack<Video>();
		
		for (Video v : a){
			for (Video v2 : b){
				if (v.equals(v2))
					result.push(v);
			}
		}
		
		return result;
	}
	
	private Stack<Video> union(Stack<Video> a, Stack<Video> b){
		Stack<Video> result = new Stack<Video>();
		
		for (Video v : a){
			result.push(v);
		}
		
		for (Video v : b){
			if (!result.contains(v))
				result.push(v);
		}
		
		return result;
	}
	
	private Stack<Video> complement(Stack<Video> a, List<Video> universe){
		Stack<Video> result = new Stack<Video>();
		
		for(Video v : universe){
			if (!a.contains(v)){
				result.add(v);
			}
		}
		
		return result;
	}
 
	private Stack<String> stringLex(String terms){

		Stack<String> result = new Stack<String>();
		String buffer = "";
		for (int a = 0; a < terms.length(); a++){
			if (terms.charAt(a) == '(' || terms.charAt(a) == ')'){
				if (buffer.length() != 0){
					result.push(buffer);
					buffer = "";
				} result.push(new String( new char[]{terms.charAt(a)}));
			}
			else if (terms.charAt(a) == ' '){
				if (buffer.length() != 0){
					result.push(buffer);
					buffer = "";
				}
			}else{
				buffer += terms.charAt(a);
			}
		}
		if (buffer.length() != 0){
			result.push(buffer);
		}
		return result;
	}

	private Stack<String> stringPostfix(Stack<String> terms){
		
		Stack<String> out = new Stack<String>();
		Stack<String> wait = new Stack<String>();
		
		String[] tokens = new String[terms.size()];
		
		int index = 0;
		for (String s : terms){
			tokens[index++] = s;
		}
		
		for (int i = 0; i < tokens.length; i++){
			if (isOperand(tokens[i])){
				out.push(tokens[i]);
			} else if (isOperator(tokens[i])){
				if (!wait.isEmpty() && getPriority(tokens[i]) <= getPriority(wait.peek())){
					while(!wait.isEmpty() && getPriority(tokens[i]) <= getPriority(wait.peek())){
						out.push(wait.pop());
					}
					wait.push(tokens[i]);
				} else{
					wait.push(tokens[i]);
				}
			} else if (isParenthesis(tokens[i])){
				if (tokens[i].equals("(")){
					wait.push(tokens[i]);
				} else if (tokens[i].equals(")")){
					String token = "";
					while (!(token = wait.pop()).equals("(")){
						out.push(token);
					}
				}
			}
		}
		
		while(!wait.isEmpty()){
			out.push(wait.pop());
		}
		
		return out;
	}
	
	private int getPriority(String operator){
		operator = operator.toLowerCase();
		
		if(operator.equals("not")){
			return 3;
		} else if (operator.equals("and") || operator.equals("or")){
			return 1;
		} else{
			return 0; //Ska aldrig hända
		}
	}
	
	private boolean isOperator(String term){
		term = term.toLowerCase();
		
		return term.equals("not") || term.equals("and") || term.equals("or");
	}
	
	private boolean isParenthesis(String term){
		
		return term.equals("(") || term.equals(")");
	}
	
	private boolean isOperand(String term){
		return !isOperator(term) && !isParenthesis(term);
	}
}
