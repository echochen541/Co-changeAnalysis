package cn.edu.fudan.se.cochange_analysis.expression.parser;

public class ExpressionTree {
	public String content;
	public ExpressionNode root;

	public ExpressionTree() {
	}

	public ExpressionTree(String content, ExpressionNode root) {
		this.root = root;
		this.content = content;
	}

	public void inorderTraversal(ExpressionNode node) {
		if (node == null)
			return;
		System.out.println(node.type + " , " + node.content);
		inorderTraversal(node.left);
		inorderTraversal(node.right);
	}

	public void preorderTraversal() {

	}

	public void postorderTraversal() {

	}

	public void printTree() {
		System.out.println("Expression£º " + content);
		inorderTraversal(root);
	}

	public boolean isSameExpressionTree(ExpressionTree pTree2) {
		ExpressionNode root1 = root;
		ExpressionNode root2 = pTree2.root;
		
		return false;
	}
}
