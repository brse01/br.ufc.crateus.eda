package br.ufc.crateus.eda.utils;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class ExprTree {

	private static Scanner scanner;

	static class Node {
		char ch;
		int valeu = 0;
		Node left, right;

		Node(char ch, int v) {
			this.ch = ch;
			this.valeu = v;
			right = left = null;
		}
	}

	private static boolean isOperator(char ch) {
		return ch == '+' || ch == '-' || ch == '*' || ch == '/';
	}

	public static void inorder(Node r) {
		if (r != null) {
			inorder(r.left);
			if (!isOperator(r.ch))
				System.out.print(r.ch);
			inorder(r.right);
		}
	}

	private static Node posfixToTree(char[] posfix) {
		Stack<Node> stack = new Stack<>();
		for (int i = 0; i < posfix.length; i++) {
			char ch = posfix[i];
			if (!isOperator(ch))
				stack.push(new Node(ch, 0));
			else {
				Node node = new Node(ch, 0);
				node.right = stack.pop();
				node.left = stack.pop();
				stack.push(node);
			}
		}
		return stack.pop();
	}

	private static char[] infixToPosfix(char[] infix) {
		char[] stack = new char[infix.length];
		char[] posfix = new char[infix.length];

		int t = 0, j = 0;
		stack[t++] = infix[0];

		for (int i = 1; i < infix.length; i++) {
			char ch = infix[i];
			if (ch == '(')
				stack[t++] = ch;
			else if (ch == ')') {
				while (stack[t - 1] != '(')
					posfix[j++] = stack[--t];
				t--;
			} else if (ch == '+' || ch == '-') {
				while (stack[t - 1] != '(')
					posfix[j++] = stack[--t];
				stack[t++] = ch;
			} else if (ch == '*' || ch == '/') {
				while (stack[t - 1] != '(' && stack[t - 1] != '+' && stack[t - 1] != '-')
					posfix[j++] = stack[--t];
				stack[t++] = ch;
			} else
				posfix[j++] = ch;
		}

		return Arrays.copyOf(posfix, j);
	}

	private static void update(Node r, char key, int valor) {
		if (r != null) {
			if (key == r.ch)
				r.valeu = valor;
			update(r.left, key, valor);
			update(r.right, key, valor);
		}
	}

	private static Node Menu(char[] posfix) {

		int value;
		scanner = new Scanner(System.in);
		Node r = posfixToTree(posfix);
		for (int i = 0; i < posfix.length; i++) {
			if (!isOperator(posfix[i])) {
				System.out.println("Digite o valor a ser atribuido: " + posfix[i]);
				value = scanner.nextInt();
				update(r, posfix[i], value);
			}
		}
		return r;
	}

	private static int calculate(Node r) {
		int result = 0;
		if (r != null) {
			char ch = r.ch;
			if (isOperator(ch)) {
				switch (ch) {
				case '+':
					result = calculate(r.left) + calculate(r.right);
					break;
				case '-':
					result = calculate(r.left) - calculate(r.right);
					break;
				case '*':
					result = calculate(r.left) * calculate(r.right);
					break;
				case '/':
					int calcu = calculate(r.right);
					if (calcu != 0)
						result = calculate(r.left) / calcu;
					break;
				}
			} else
				return r.valeu;
		}
		return result;
	}

	public static void main(String[] args) {
		String strInfix;
		scanner = new Scanner(System.in);
		System.out.println("Digite a express�o a ser calculada ENTRE ()");
		strInfix = scanner.next();
		Node r = Menu(infixToPosfix(strInfix.toCharArray()));
		System.out.println("RESULTADO E:" + calculate(r));
	}
}
