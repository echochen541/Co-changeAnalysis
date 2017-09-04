package cn.edu.fudan.se.cochange_analysis.shared.dependency;

import org.eclipse.jdt.core.dom.*;

public class Parser {

	public static void main(String[] args) throws Exception {
		ASTParser parser = ASTParser.newParser(AST.JLS8); // 设置Java语言规范版本

		char[] src = "((! required) && (defaultValue != null))".toCharArray();
		parser.setSource(src);

		parser.setKind(ASTParser.K_EXPRESSION);

		Expression expression = (Expression) parser.createAST(null); // 这个参数是IProgessMonitor,用于GUI的进度显示,我们不需要，填个null.
		// 返回值是AST的根结点

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
