package cn.edu.fudan.se.cochange_analysis.shared.dependency;

import java.util.List;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.PrefixExpression.Operator;

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeOperationWithBLOBs;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeOperationDAO;

public class Parser {

	public static void main(String[] args) throws Exception {
		String changeType = "STATEMENT_DELETE";
		String changedEntityType = "IF_STATEMENT";
		List<ChangeOperationWithBLOBs> cos = ChangeOperationDAO.selectByChangeTypeAndChangedEntityType(changeType,
				changedEntityType);

		for (ChangeOperationWithBLOBs co : cos) {
			String expressionStr = co.getChangedEntityContent();
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			char[] src = expressionStr.toCharArray();
			parser.setSource(src);
			parser.setKind(ASTParser.K_EXPRESSION);
			ASTNode astNode = parser.createAST(null);

			if (astNode instanceof Expression) {
				Expression expression = (Expression) astNode;

				while (expression instanceof ParenthesizedExpression) {
					ParenthesizedExpression pex = (ParenthesizedExpression) expression;
					expression = pex.getExpression();
				}

				if (expression instanceof InfixExpression) {
					System.out.println(expression);
					parseExpression(expression);
					System.out.println();
				}
			}
		}
	}

	private static void parseExpression(Expression expression) {
		// if ParenthesizedExpression, remove Parenthesis
		if (expression instanceof ParenthesizedExpression) {
			ParenthesizedExpression pex = (ParenthesizedExpression) expression;
			expression = pex.getExpression();
			parseExpression(expression);
		} else if (expression instanceof InfixExpression) { // InfixExpression
			// parseInfixExpression((InfixExpression) expression);
			InfixExpression iex = (InfixExpression) expression;
			System.out.println("middle: " + iex.getOperator());
			Expression lhex = iex.getLeftOperand();
			Expression rhex = iex.getRightOperand();
			parseExpression(lhex);
			parseExpression(rhex);
		} else if (expression instanceof SimpleName) {// SimpleName
			SimpleName sn = (SimpleName) expression;
			System.out.println("SimpleName: " + sn.getIdentifier());
		} else if (expression instanceof InstanceofExpression) {// InstanceofExpression
			InstanceofExpression iex = (InstanceofExpression) expression;
			Expression lhex = iex.getLeftOperand();
			Type rht = iex.getRightOperand();
			System.out.println("Type: " + rht);
			parseExpression(lhex);
		} else if (expression instanceof MethodInvocation) { // MethodInvocation
			MethodInvocation mi = (MethodInvocation) expression;
			System.out.println("MethodInvocation: " + mi.getName());
		} else if (expression instanceof PrefixExpression) { // PrefixExpression
			PrefixExpression pex = (PrefixExpression) expression;
			Expression operand = pex.getOperand();
			Operator operator = pex.getOperator();
			System.out.println("PrefixExpression: " + operator + " , " + operand);
			parseExpression(operand);
		} else if (expression instanceof ConditionalExpression) { // ConditionalExpression
			ConditionalExpression cex = (ConditionalExpression) expression;
			Expression eex = cex.getElseExpression();
			Expression tex = cex.getThenExpression();
			Expression ex = cex.getExpression();
			System.out.println("ConditionalExpression: " + ex + " , " + tex + " , " + eex);
			parseExpression(ex);
		} else if (expression instanceof QualifiedName) { // QualifiedName
			QualifiedName qn = (QualifiedName) expression;
			System.out.println("QualifiedName :" + qn.getName());
		} else if (expression instanceof CastExpression) { // CastExpression
			CastExpression cex = (CastExpression) expression;
			Expression ex = cex.getExpression();
			System.out.println("CastExpression: " + ex);
			parseExpression(ex);
		} else if (expression instanceof FieldAccess) { // FieldAccess
			FieldAccess fa = (FieldAccess) expression;
			System.out.println("FieldAccess: " + fa.getName());
		} else if (expression instanceof BooleanLiteral) { // BooleanLiteral
			BooleanLiteral bl = (BooleanLiteral) expression;
			System.out.println("BooleanLiteral: " + bl);
		} else if (expression instanceof SuperMethodInvocation) { // SuperMethodInvocation
			SuperMethodInvocation smi = (SuperMethodInvocation) expression;
			System.out.println("SuperMethodInvocation: " + smi.getName());
		} else if (expression instanceof ArrayAccess) { // ArrayAccess
			ArrayAccess aa = (ArrayAccess) expression;
			Expression array = aa.getArray();
			Expression index = aa.getIndex();
			System.out.println("ArrayAccess: " + array + " , " + index);
		} else if (expression instanceof Assignment) { // Assignment
			Assignment as = (Assignment) expression;
			Expression lhex = as.getLeftHandSide();
			Expression rhex = as.getRightHandSide();
			System.out.println("Assignment: " + lhex + " , " + rhex);
			parseExpression(rhex);
		} else if (expression instanceof ThisExpression) {
			ThisExpression tex = (ThisExpression) expression;
			System.out.println("ThisExpression: " + tex);
		} else if (expression instanceof NumberLiteral) {
			NumberLiteral nl = (NumberLiteral) expression;
			System.out.println("NumberLiteral: " + nl.getToken());
		} else if (expression instanceof NullLiteral) {
			NullLiteral nl = (NullLiteral) expression;
			System.out.println("NullLiteral: " + nl);
		} else if (expression instanceof CharacterLiteral) {
			CharacterLiteral cl = (CharacterLiteral) expression;
			System.out.println("CharacterLiteral: " + cl.charValue() + "");
		} else if (expression instanceof TypeLiteral) {
			TypeLiteral tl = (TypeLiteral) expression;
			System.out.println("TypeLiteral: " + tl.getType().toString());
		} else if (expression instanceof PostfixExpression){
			PostfixExpression pex = (PostfixExpression)expression;
			org.eclipse.jdt.core.dom.PostfixExpression.Operator operator = pex.getOperator();
			Expression operand = pex.getOperand();
			System.out.println("PostfixExpression: " + operator + " , " + operand);
			parseExpression(operand);
		} else {
			SuperFieldAccess sfa = (SuperFieldAccess)expression;
			System.out.println("SuperFieldAccess: " + sfa.getName());
		}
	}
}
