package cn.edu.fudan.se.cochange_analysis.main;

import java.util.Scanner;

public class MSTest {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		while (in.hasNext()) {
			int n = in.nextInt();
			int[] nums = new int[n];
			for (int i = 0; i < n; i++) {
				nums[i] = in.nextInt();
			}
			System.out.println(solve(nums, n));
		}
	}

	public static int solve(int[] nums, int n) {
		int[] sum = new int[n + 1];
		for (int i = 0; i < n; i++) {
			sum[i + 1] = sum[i] + nums[i];
		}

		int result = 0;
		for (int i = 1; i < n - 1; i++) {
			for (int j = i + 1; j < n; j++) {
				// 0 ~ i - 1
				int s1 = sum[i] - sum[0];
				// i ~ j - 1
				int s2 = sum[j] - sum[i];
				// j ~ n - 1
				int s3 = sum[n] - sum[j];
				if (isValid(s1, s2, s3))
					result++;
			}
		}
		return result;
	}

	private static boolean isValid(int s1, int s2, int s3) {
		if (Math.abs(s1 - s2) <= 1 && Math.abs(s2 - s3) <= 1 && Math.abs(s3 - s1) <= 1)
			return true;
		else
			return false;
	}
}
