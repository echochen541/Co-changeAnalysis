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
		BinaryTree bTree1 = convert2BinaryTree(this);
		BinaryTree bTree2 = convert2BinaryTree(pTree2);
		return bTree1.isSameTree(bTree2);
	}

	public static BinaryTree convert2BinaryTree(ExpressionTree eTree) {
		BinaryTreeNode bTreeRoot = convert2BinaryNode(eTree.root);
		BinaryTree bTree = new BinaryTree();
		bTree.setRoot(bTreeRoot);
		return bTree;
	}

	public static BinaryTreeNode convert2BinaryNode(ExpressionNode eNode) {
		BinaryTreeNode bNode = new BinaryTreeNode();
		convert2BinaryNodeHelper(eNode, bNode);
		return bNode;
	}

	public static void convert2BinaryNodeHelper(ExpressionNode eNode, BinaryTreeNode bNode) {
		if (eNode == null) {
			return;
		}

		if (eNode.getLeft() == null && eNode.getRight() == null) {
			// if (eNode.getType().equals("MethodInvocation") ||
			// eNode.getType().equals("SuperMethodInvocation")) {
			bNode.setContent(eNode.getContent());
			// } else {
			bNode.setContent("T");
			// }
			return;
		}

		bNode.setContent(eNode.getContent());

		if (eNode.getLeft() != null) {
			bNode.setLeft(convert2BinaryNode(eNode.getLeft()));
		}

		if (eNode.getRight() != null) {
			bNode.setRight(convert2BinaryNode(eNode.getRight()));
		}
	}
}
