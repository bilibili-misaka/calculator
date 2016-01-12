package com.example.calculator.utils;

import java.math.BigDecimal;
import java.util.Stack;

import com.example.calculator.MainActivity;

import android.widget.TextView;

public class ExpressionUtil {
	public static void getResult(TextView text1,TextView text2){
		String st = text2.getText().toString();
        int count1 = 0;
        int count2 = 0;
        char[] ch = st.toCharArray();
        for (int i = 0; i < ch.length - 1; i++) {
            if (!((ch[i] >= '0' && ch[i] <= '9') || ch[i] == '(' || ch[i] == ')' || ch[i] == '+' || ch[i] == '-' || ch[i] == '*' || ch[i] == '/')) {
            	text1.setText("error!");
            	text2.setText("");
                return;
            } else if ((ch[i] == '+' || ch[i] == '-' || ch[i] == '*' || ch[i] == '/' || ch[i] == '(') && (ch[i + 1] == '+' || ch[i + 1] == '-' || ch[i + 1] == '*' || ch[i + 1] == '/' || ch[i + 1] == ')')) {
            	text1.setText("error!");
            	text2.setText("");
                return;
            } else if (ch[i] == ')' && ch[i + 1] == '(') {
            	text1.setText("error!");
            	text2.setText("");
                return;
            }
            if (ch[i] == '(') {
                count1++;
            }
            if (ch[i] == ')') {
                count2++;
            }

        }
        if (ch[ch.length - 1] == '(') {
            count1++;
        }
        if (ch[ch.length - 1] == ')') {
            count2++;
        }
        if (count1 != count2) {
        	text1.setText("error!");
        	text2.setText("");
            return;
        }

        text1.setText(MainActivity.delete0(opera2(st)));
	}
	
	private static String opera2(String s) {
        Stack<String> stack = new Stack<String>();// 运算符
        Stack<String> stack2 = new Stack<String>();// 运算数字

        char[] c = s.toCharArray();
        convert(stack, stack2, c);

        while (!stack.empty()) {
            stack2.push(stack.pop());
        }

        return calc(stack2);

    }

    public static void convert(Stack<String> stack, Stack<String> stack2, char[] c) {
        String st = "";
        boolean flag = false;
        for (int i = 0; i < c.length; i++) {
            if (c[i] >= '0' && c[i] <= '9') {
                st += c[i];
                flag = false;
            } else if (c[i] == '+' || c[i] == '-' || c[i] == '*' || c[i] == '/') {
                flag = true;
                if (flag && st != "") {
                    stack2.push(st);
                    st = "";
                }

                while (c[i] != ' ') {
                    if (stack.empty()) {
                        stack.push(c[i] + "");
                        c[i] = ' ';
                    } else if (stack.peek().charAt(0) == '(') {
                        stack.push(c[i] + "");
                        c[i] = ' ';
                    } else if (priority(c[i], stack.peek().charAt(0)) >= 0) {
                        stack.push(c[i] + "");
                        c[i] = ' ';

                    } else if (priority(c[i], stack.peek().charAt(0)) < 0) {
                        stack2.push(stack.pop());
                    }
                }

            } else if (c[i] == '(') {
                flag = true;
                if (flag && st != "") {
                    stack2.push(st);
                    st = "";
                }
                stack.push(c[i] + "");
            } else if (c[i] == ')') {
                flag = true;
                if (flag && st != "") {
                    stack2.push(st);
                    st = "";
                }
                while (!stack.empty()) {
                    if (stack.peek().charAt(0) != '(') {
                        stack2.push(stack.pop());
                    } else if (stack.peek().charAt(0) == '(') {
                        stack.pop();
                        break;
                    }

                }

            }

        }
        if (st != "") {
            stack2.push(st);
            st = "";
        }
    }

    // 优先级的判定
    public static int priority(char c, char s) {

        if (c == '+') {
            return -1;
        } else if (c == '-') {
            return -1;
        } else if (c == '*') {
            if (s == '*' || s == '/') {
                return -1;
            } else {
                return 1;
            }
        } else if (s == '/') {
            if (s == '*' || s == '/') {
                return -1;
            } else {
                return 1;
            }
        }
        return 0;

    }

    public static String calc(Stack<String> stack2) {
        Stack<String> stack = new Stack<String>();

        String num1 = "";
        String num2 = "";
        String s = "";
        String result = "";
        while (!stack2.empty()) {
            stack.push(stack2.pop());
        }

        while (!stack.empty()) {
            String st = stack.peek();
            if (!st.equals("+") && !st.equals("-") && !st.equals("*") && !st.equals("/")) {
                stack2.push(stack.pop());
            } else {
                num1 = stack2.pop();
                num2 = stack2.pop();
                s = stack.pop();
                result = opera(num2, num1, s);
                stack2.push(result);
            }

        }

        return result;
    }

    public static String opera(String num1, String num2, String s) {
        String result = "";
        BigDecimal b1 = new BigDecimal(num1);
        BigDecimal b2 = new BigDecimal(num2);
        char c = s.charAt(0);
        switch (c) {
            case '+':
                result = b1.add(b2) + "";
                break;
            case '-':
                result = b1.subtract(b2) + "";
                break;
            case '*':
                result = b1.multiply(b2) + "";
                break;
            case '/':
                result = b1.divide(b2) + "";
                break;
            case '^':
                result = b1.pow(b2.intValue()) + "";
            default:
                break;
        }
        return result;
    }
	
	
}
