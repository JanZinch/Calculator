package com.ivan.calculator;

import android.util.Log;

import java.util.LinkedList;
import java.util.Stack;

import java.lang.Math;

public class Calculator {


    public double Radians(double value) {

        final double factor = Math.PI / 180;
        return value * factor;
    }

    public int GetPriority(char symbol) {

        switch (symbol) {

            case 's':

                return 5;

            case '^':

                return 4;

            case '*':

            case '/':

                return 3;

            case '-':

            case '+':

                return 2;

            case '(':

                return 1;
        }

        return 0;
    }

    private boolean IsSeparator(char s) {

        return s == '.';
    }

    private boolean IsOperator(char s) {

        return s == '+' || s == '-' || s == '*' || s == '/' || s == '^';
    }

    private boolean IsOperand(char s) {

        return s >= '0' && s <= '9';
    }

    private boolean IsBracket(char s) {

        return s == '(' || s == ')';
    }

    boolean IsFunction(char f, char s, char t) {

        if (f == 's' && s == 'i' && t == 'n') {

            return true;
        }
        else {

            return false;
        }
    }

    boolean IsReductFunction(char s) {

        return s == 's' || s == 'c';
    }


    public void UpdateInput(StringBuffer source){

        try{


            int i;
            LinkedList<Integer> points = new LinkedList<Integer>();

            source.append("EN");
            source.insert(0, 'b');

            for(i = 1; i < source.length(); i++){

                if (source.charAt(i) == '.'){

                    if(!IsOperand(source.charAt(i-1))){

                        source.insert(i, '0');
                    }
                }
            }

            for(i = 1; i < source.length(); i++) {

                if (source.charAt(i) == '.') {

                    if (!IsOperand(source.charAt(i + 1))) {

                        points.addLast(i);
                    }
                }
            }

            for(Integer it : points){

                source.deleteCharAt(it);
            }

            for (i = 1;  i < source.length(); i++) {

                if(source.charAt(i) == '-' && !IsOperand(source.charAt(i - 1)) && source.charAt(i-1) != '('){

                    source.insert(i, '(');
                }
            }

            for(i = 1; i < source.length(); i++){

                if (source.charAt(i) == '('){

                    if(!IsOperator(source.charAt(i-1))  && source.charAt(i-1) != '(' && source.charAt(i-1) != 'b'){

                        source.insert(i, '*');
                    }
                }
            }

            for(i = 1; i < source.length(); i++){

                if (source.charAt(i) == ')'){

                    if(!IsOperator(source.charAt(i+1)) && source.charAt(i+1) != ')' && source.charAt(i+1) != 'E'){

                        source.insert(i + 1, '*');
                    }
                }
            }

            for(i = 0; i < source.length(); i++){

                if (source.charAt(i) == 'b'){

                    source.deleteCharAt(i);
                }
            }


        }
        catch (Exception ex){


        }

    }



    boolean CheckSource(char[] exp) {

        int leftBracketsCounter = 0;

        if (exp.length == 0) {

            return false;
        }
        else if (IsOperator(exp[0]) || IsSeparator(exp[0])) {

            return false;
        }

        for (int i = 0; i < exp.length - 2; i++) {

            if (IsOperator(exp[i])) {

                if (!IsOperand(exp[i + 1]) && exp[i + 1] != '(' && !IsReductFunction(exp[i+1])) {

                    return false;
                }
            }
            else if (IsSeparator(exp[i])) {

                if (!IsOperand(exp[i - 1]) || !IsOperand(exp[i + 1])) {

                    return false;
                }
            }
            else if (exp[i] == '(') {

                if (exp[i + 1] != '-' && !IsOperand(exp[i + 1]) && exp[i + 1] != '(') {

                    return false;
                }

                leftBracketsCounter++;

            }
            else if (exp[i] == ')') {

                if (!IsOperator(exp[i + 1]) && exp[i + 1] != ')' && exp[i+1] != 'E') {

                    return false;
                }

                if (leftBracketsCounter != 0) {

                    leftBracketsCounter--;
                }
                else {

                    return false;
                }
            }
            else if (IsOperand(exp[i])) {

                if (exp[i + 1] == '(') {

                    return false;
                }
            }
            else if (IsFunction(exp[i], exp[i+1], exp[i+2])) {

                i += 2;

                if (exp[i + 1] != '(') {

                    return false;
                }

            }
            else {

                return false;
            }
        }

        return true;
    }


    private LinkedList<String> CreatePolishNotation(char[] exp){

        String element = "";

        LinkedList<String> PolishNotation = new LinkedList<String>();
        Stack<Character> Stack = new Stack<Character>();

        for (int i = 0; i < exp.length; i++) {

            if (IsOperand(exp[i]) || IsSeparator(exp[i])) {

                element += exp[i];

                if (!IsOperand(exp[i + 1]) && !IsSeparator(exp[i + 1])) {

                    PolishNotation.addLast(element);
                    element = "";
                }
            }
            else {

                if (exp[i] == '(') {

                    Stack.push(exp[i]);
                }
                else if (IsOperator(exp[i])) {

                    if (exp[i - 1] == '(') {

                        element += '0';
                        PolishNotation.addLast(element);
                        element = "";
                    }

                    while (!Stack.empty()) {

                        if (GetPriority(Stack.peek()) >= GetPriority(exp[i])) {

                            element += Stack.peek();
                            PolishNotation.addLast(element);
                            Stack.pop();
                            element = "";
                        }

                        else break;
                    }

                    Stack.push(exp[i]);
                }
                else if (exp[i] == ')') {

                    while (Stack.peek() != '(') {

                        element += Stack.peek();
                        PolishNotation.addLast(element);
                        Stack.pop();
                        element = "";
                    }
                }
            }
        }

        while (!Stack.empty()) {

            element += Stack.peek();
            PolishNotation.addLast(element);
            Stack.pop();
            element = "";
        }

        PolishNotation.remove("(");
        PolishNotation.remove(")");

        return PolishNotation;
    }

    double Calculation(char[] exp) throws NumberFormatException, SecurityException {

        double a, b;
        Stack<Double> stack = new Stack<Double>();

        LinkedList<String> PolishNotation = CreatePolishNotation(exp);

        for (String var : PolishNotation) {

            if (IsOperator(var.charAt(0))) {

                a = stack.peek();
                stack.pop();
                b = stack.peek();
                stack.pop();

                if (var.charAt(0) == '+') {

                    stack.push(b + a);
                }
                else if (var.charAt(0) == '-') {

                    stack.push(b - a);
                }
                else if (var.charAt(0) == '*') {

                    stack.push(b * a);
                }
                else if (var.charAt(0) == '/') {

                    if(a == 0){

                        if(b==0) throw new SecurityException();
                        else throw new NumberFormatException();
                    }

                    stack.push(b / a);
                }
                else if (var.charAt(0) == '^') {

                    stack.push(Math.pow(b, a));
                }

            }
            else if(IsOperand(var.charAt(0))) {

                stack.push(Double.parseDouble(var));
            }

        }

        return stack.peek();
    }


}
