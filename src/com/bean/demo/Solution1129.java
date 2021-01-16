package com.bean.demo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Solution1129 {
	
	private int[] ans;

	public int[] shortestAlternatingPaths(int n, int[][] redEdges, int[][] blueEdges) {
		ans = new int[n];
		for (int i = 1; i < n; i++) {
			ans[i] = Integer.MAX_VALUE;
		}
		Map<Integer, Set<Integer>> redMap = new HashMap<>(16);
		Map<Integer, Set<Integer>> blueMap = new HashMap<>(16);
		for (int[] redEdge : redEdges) {
			redMap.putIfAbsent(redEdge[0], new HashSet<>());
			redMap.get(redEdge[0]).add(redEdge[1]);
		}
		for (int[] blueEdge : blueEdges) {
			blueMap.putIfAbsent(blueEdge[0], new HashSet<>());
			blueMap.get(blueEdge[0]).add(blueEdge[1]);
		}

		// 红边为起点
		int len = 0;
		boolean isRed = true;
		boolean[] visitedRed = new boolean[n];
		boolean[] visitedBlue = new boolean[n];
		visitedRed[0] = true;
		Queue<Integer> queue = new LinkedList<>();
		for (int[] edge : redEdges) {
			if (edge[0] == 0) {
				queue.offer(edge[1]);
			}
		}
		bfs(redMap, blueMap, len, isRed, visitedRed, visitedBlue, queue);

		// 蓝边为起点
		isRed = false;
		visitedRed = new boolean[n];
		visitedBlue = new boolean[n];
		len = 0;
		queue.clear();
		for (int[] edge : blueEdges) {
			if (edge[0] == 0) {
				queue.offer(edge[1]);
			}
		}
		bfs(redMap, blueMap, len, isRed, visitedRed, visitedBlue, queue);
		for (int i = 0; i < ans.length; i++) {
			if (ans[i] == Integer.MAX_VALUE) {
				ans[i] = -1;
			}
		}
		return ans;
	}

    private void bfs(Map<Integer, Set<Integer>> redMap,
                     Map<Integer, Set<Integer>> blueMap,
                     int len, boolean isRed,
                     boolean[] visitedRed, boolean[] visitedBlue,
                     Queue<Integer>queue) {
        while (!queue.isEmpty()) {
            int size = queue.size();
            len++;
            if (isRed) {
                // queue中节点为红边终点，即下一步应该走蓝边
                isRed = false;
                while (size-- > 0) {
                    int idx = queue.remove();
                    if (!visitedBlue[idx]) {
                        visitedBlue[idx] = true;
                        ans[idx] = Math.min(ans[idx], len);
                        Set<Integer> desSet = blueMap.get(idx);
                        if (desSet == null) {
                            continue;
                        }
                        for (int d : desSet) {
                            // 检查是否已经遍历过红边以d作为起点
                            if (!visitedRed[d]) {
                                queue.offer(d);
                            }
                        }
                    }
                }
            } else {
                // queue中节点为蓝边终点，即下一步应该走红边
                isRed = true;
                while (size-- > 0) {
                    int idx = queue.remove();
                    if (!visitedRed[idx]) {
                        visitedRed[idx] = true;
                        ans[idx] = Math.min(ans[idx], len);
                        Set<Integer> desSet = redMap.get(idx);
                        if (desSet == null) {
                            continue;
                        }
                        for (int d : desSet) {
                            // 检查是否已经遍历过蓝边以d作为起点
                            if (!visitedBlue[d]) {
                                queue.offer(d);
                            }
                        }
                    }
                }
            }
        }
    }
}
