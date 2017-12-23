package cn.edu.fudan.se.cochange_analysis.expression.parser;

public class ExpressionNode {
	public String content;
	public String type;
	public ExpressionNode left;
	public ExpressionNode right;
	
	public ExpressionNode() {
	}
	
	public ExpressionNode(String content, String type) {
		this.content = content;
		this.type = type;
	}

	public ExpressionNode(String type, String content, ExpressionNode left, ExpressionNode right) {
		this.type = type;
		this.content = content;
		this.left = left;
		this.right = right;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ExpressionNode getLeft() {
		return left;
	}

	public void setLeft(ExpressionNode left) {
		this.left = left;
	}

	public ExpressionNode getRight() {
		return right;
	}

	public void setRight(ExpressionNode right) {
		this.right = right;
	}
}
