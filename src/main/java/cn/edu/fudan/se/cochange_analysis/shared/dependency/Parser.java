package cn.edu.fudan.se.cochange_analysis.shared.dependency;

import org.eclipse.jdt.core.dom.*;

public class Parser {

	public static void main(String[] args) throws Exception {
		ASTParser parser = ASTParser.newParser(AST.JLS8); // ����Java���Թ淶�汾

		char[] src = "((! required) && (defaultValue != null))".toCharArray();
		parser.setSource(src);

		parser.setKind(ASTParser.K_EXPRESSION);

		Expression expression = (Expression) parser.createAST(null); // ���������IProgessMonitor,����GUI�Ľ�����ʾ,���ǲ���Ҫ�����null.
		// ����ֵ��AST�ĸ����

		ParenthesizedExpression pExpression = (ParenthesizedExpression) expression;
		InfixExpression wex = (InfixExpression) pExpression.getExpression();

		inorderPrint(wex);
		System.out.println();
	}

	private static void inorderPrint(InfixExpression wex) {
		System.out.println("middle: " + wex.getOperator());

		Expression lhs = wex.getLeftOperand();
		if (lhs instanceof InfixExpression)
			inorderPrint((InfixExpression) lhs);
		else
			System.out.println("left: " + lhs);

		Expression rhs = wex.getRightOperand();
		if (rhs instanceof InfixExpression)
			inorderPrint((InfixExpression) rhs);
		else
			System.out.println("right: " + rhs);
	}
}
