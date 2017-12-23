package cn.edu.fudan.se.cochange_analysis.expression.parser;

import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.PrefixExpression.Operator;

import cn.edu.fudan.se.cochange_analysis.git.bean.ChangeOperationWithBLOBs;
import cn.edu.fudan.se.cochange_analysis.git.dao.ChangeOperationDAO;

public class ExpressionParser {
	public String content;

	public ExpressionParser() {
	}

	public ExpressionParser(String content) {
		this.content = content;
	}

	public static void main(String[] args) {
		String changeType = "CONDITION_EXPRESSION_CHANGE";
		String changedEntityType = "IF_STATEMENT";
		List<ChangeOperationWithBLOBs> cos = ChangeOperationDAO.selectByChangeTypeAndChangedEntityType(changeType,
				changedEntityType);

		for (ChangeOperationWithBLOBs co : cos) {
			String content = co.getChangedEntityContent();
			ExpressionParser parser = new ExpressionParser(content);
			Expression expression = parser.parse2Expression();
			ExpressionTree pTree = parser.parse2Tree(expression);
			if (pTree != null) {
				pTree.printTree();
				System.out.println();
			} else {
				System.out.println(content);
				System.out.println();
			}
		}
	}

	public Expression parse2Expression() {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		char[] src = content.toCharArray();
		parser.setSource(src);
		parser.setKind(ASTParser.K_EXPRESSION);
		ASTNode astNode = parser.createAST(null);
		if (astNode instanceof Expression) {
			Expression expression = (Expression) astNode;
			return expression;
		}
		return null;
	}

	public ExpressionTree parse2Tree(Expression expression) {
		if (expression == null)
			return null;
		ExpressionNode root = new ExpressionNode();
		ExpressionTree tree = new ExpressionTree(content, root);
		parseExpression(root, expression);
		return tree;
	}

	private void parseExpression(ExpressionNode node, Expression expression) {
		// if ParenthesizedExpression, remove Parenthesis
		if (expression instanceof ParenthesizedExpression) {
			ParenthesizedExpression pex = (ParenthesizedExpression) expression;
			expression = pex.getExpression();
			parseExpression(node, expression);
		} else if (expression instanceof InfixExpression) { // InfixExpression
			// parseInfixExpression((InfixExpression) expression);
			InfixExpression iex = (InfixExpression) expression;
			// System.out.println("middle: " + iex.getOperator());
			Expression lhex = iex.getLeftOperand();
			Expression rhex = iex.getRightOperand();

			node.type = "InfixExpression";
			node.content = iex.getOperator().toString();
			node.left = new ExpressionNode();
			node.right = new ExpressionNode();
			parseExpression(node.left, lhex);
			parseExpression(node.right, rhex);
		} else if (expression instanceof SimpleName) {// SimpleName
			SimpleName sn = (SimpleName) expression;
			// System.out.println("SimpleName: " + sn.getIdentifier());

			node.type = "SimpleName";
			node.content = sn.getIdentifier();
			return;
		} else if (expression instanceof InstanceofExpression) {// InstanceofExpression
			InstanceofExpression iex = (InstanceofExpression) expression;
			Expression lhex = iex.getLeftOperand();
			Type rht = iex.getRightOperand();
			// System.out.println("Type: " + rht);
			node.type = "InstanceofExpression";
			node.content = "instanceof";
			node.left = new ExpressionNode();
			parseExpression(node.left, lhex);
			node.right = new ExpressionNode(rht.toString(), "Type");
		} else if (expression instanceof MethodInvocation) { // MethodInvocation
			MethodInvocation mi = (MethodInvocation) expression;
			// System.out.println("MethodInvocation: " + mi.getName());
			node.type = "MethodInvocation";
			node.content = mi.getName().toString();
			return;
		} else if (expression instanceof PrefixExpression) { // PrefixExpression
			PrefixExpression pex = (PrefixExpression) expression;
			Expression operand = pex.getOperand();
			Operator operator = pex.getOperator();
			// System.out.println("PrefixExpression: " + operator + " , " +
			// operand);
			// node.type = "PrefixExpression";
			// node.content = operator.toString();
			// node.left = new ExpressionNode();
			parseExpression(node, operand);
		} else if (expression instanceof ConditionalExpression) { // ConditionalExpression
			ConditionalExpression cex = (ConditionalExpression) expression;
			Expression eex = cex.getElseExpression();
			Expression tex = cex.getThenExpression();
			Expression ex = cex.getExpression();
			// System.out.println("ConditionalExpression: " + ex + " , " + tex +
			// " , " + eex);
			parseExpression(node, ex);
		} else if (expression instanceof QualifiedName) { // QualifiedName
			QualifiedName qn = (QualifiedName) expression;
			// System.out.println("QualifiedName :" + qn.getName());
			node.type = "QualifiedName";
			node.content = qn.getName().toString();
			return;
		} else if (expression instanceof CastExpression) { // CastExpression
			CastExpression cex = (CastExpression) expression;
			Expression ex = cex.getExpression();
			// System.out.println("CastExpression: " + ex);
			parseExpression(node, ex);
			return;
		} else if (expression instanceof FieldAccess) { // FieldAccess
			FieldAccess fa = (FieldAccess) expression;
			// System.out.println("FieldAccess: " + fa.getName());
			node.type = "FieldAccess";
			node.content = fa.getName().toString();
			return;
		} else if (expression instanceof BooleanLiteral) { // BooleanLiteral
			BooleanLiteral bl = (BooleanLiteral) expression;
			// System.out.println("BooleanLiteral: " + bl);
			node.type = "BooleanLiteral";
			node.content = bl.toString();
			return;
		} else if (expression instanceof SuperMethodInvocation) { // SuperMethodInvocation
			SuperMethodInvocation smi = (SuperMethodInvocation) expression;
			// System.out.println("SuperMethodInvocation: " + smi.getName());
			node.type = "SuperMethodInvocation";
			node.content = smi.getName().toString();
			return;
		} else if (expression instanceof ArrayAccess) { // ArrayAccess
			ArrayAccess aa = (ArrayAccess) expression;
			Expression array = aa.getArray();
			Expression index = aa.getIndex();
			// System.out.println("ArrayAccess: " + array + " , " + index);
			node.type = "ArrayAccess";
			node.content = array.toString();
			return;
		} else if (expression instanceof Assignment) { // Assignment
			Assignment as = (Assignment) expression;
			Expression lhex = as.getLeftHandSide();
			Expression rhex = as.getRightHandSide();
			// System.out.println("Assignment: " + lhex + " , " + rhex);
			parseExpression(node, rhex);
		} else if (expression instanceof ThisExpression) {
			ThisExpression tex = (ThisExpression) expression;
			// System.out.println("ThisExpression: " + tex);
			node.type = "ThisExpression";
			node.content = tex.toString();
			return;
		} else if (expression instanceof NumberLiteral) {
			NumberLiteral nl = (NumberLiteral) expression;
			// System.out.println("NumberLiteral: " + nl.getToken());
			node.type = "NumberLiteral";
			node.content = nl.toString();
			return;
		} else if (expression instanceof NullLiteral) {
			NullLiteral nl = (NullLiteral) expression;
			// System.out.println("NullLiteral: " + nl);
			node.type = "NullLiteral";
			node.content = nl.toString();
			return;
		} else if (expression instanceof CharacterLiteral) {
			CharacterLiteral cl = (CharacterLiteral) expression;
			// System.out.println("CharacterLiteral: " + cl.charValue() + "");
			node.type = "CharacterLiteral";
			node.content = cl.charValue() + "";
			return;
		} else if (expression instanceof TypeLiteral) {
			TypeLiteral tl = (TypeLiteral) expression;
			// System.out.println("TypeLiteral: " + tl.getType().toString());
			node.type = "TypeLiteral";
			node.content = tl.getType().toString();
			return;
		} else if (expression instanceof PostfixExpression) {
			PostfixExpression pex = (PostfixExpression) expression;
			org.eclipse.jdt.core.dom.PostfixExpression.Operator operator = pex.getOperator();
			Expression operand = pex.getOperand();
			// System.out.println("PostfixExpression: " + operator + " , " +
			// operand);
			parseExpression(node, operand);
		} else if (expression instanceof SuperFieldAccess) {
			SuperFieldAccess sfa = (SuperFieldAccess) expression;
			// System.out.println("SuperFieldAccess: " + sfa.getName());
			node.type = "SuperFieldAccess";
			node.content = sfa.getName().toString();
			return;
		} else {
			StringLiteral sl = (StringLiteral) expression;
			node.type = "StringLiteral";
			node.content = sl.getLiteralValue();
			return;
		}
	}
}
