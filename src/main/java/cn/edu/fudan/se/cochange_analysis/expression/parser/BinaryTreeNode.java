package cn.edu.fudan.se.cochange_analysis.expression.parser;

public class BinaryTreeNode {
	private BinaryTreeNode left;
	private BinaryTreeNode right;
	private String content;

	public BinaryTreeNode() {
	}

	public BinaryTreeNode(BinaryTreeNode left, BinaryTreeNode right, String content) {
		this.left = left;
		this.right = right;
		this.content = content;
	}

	public BinaryTreeNode(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "BinaryTreeNode [left=" + left + ", right=" + right + ", content=" + content + "]";
	}

	public BinaryTreeNode getLeft() {
		return left;
	}

	public void setLeft(BinaryTreeNode left) {
		this.left = left;
	}

	public BinaryTreeNode getRight() {
		return right;
	}

	public void setRight(BinaryTreeNode right) {
		this.right = right;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
