package cn.edu.fudan.se.cochange_analysis.expression.parser;

public class BinaryTree {
	private BinaryTreeNode root;

	public BinaryTree() {
	}

	public BinaryTree(BinaryTreeNode root) {
		this.root = root;
	}

	public boolean isSameTree(BinaryTree tree) {
		return isSameTreeNode(root, tree.getRoot());
	}

	private boolean isSameTreeNode(BinaryTreeNode node1, BinaryTreeNode node2) {
		if (node1 == null && node2 == null) {
			return true;
		}

		if (node1 == null && node2 != null) {
			return false;
		}

		if (node2 == null && node1 != null) {
			return false;
		}

		if (node1.getContent().equals(node2.getContent())) {
			return isSameTreeNode(node1.getLeft(), node2.getLeft())
					&& isSameTreeNode(node1.getRight(), node2.getRight());
		}
		return false;
	}

	public void inorderTraversal(BinaryTreeNode node) {
		if (node == null)
			return;
		System.out.println(node.getContent());
		inorderTraversal(node.getLeft());
		inorderTraversal(node.getRight());
	}

	public void preorderTraversal() {

	}

	public void postorderTraversal() {

	}

	public void printTree() {
		inorderTraversal(root);
	}

	public BinaryTreeNode getRoot() {
		return root;
	}

	public void setRoot(BinaryTreeNode root) {
		this.root = root;
	}
}
