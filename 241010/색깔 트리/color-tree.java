import java.util.*;
import java.io.*;

public class Main {
	static int Q, valueSum, bit[] = {0, 16, 8, 4, 2, 1}, test[] = {18, 17, 3, 5, 4, 11};
	static ArrayList<ArrayList<Node>> tree = new ArrayList<>();
	static ArrayList<Integer> root_id = new ArrayList<>();
	static StringBuilder sb = new StringBuilder();

	public static class Node {
		int m_id, p_id, color, max_depth, p_depth;
		int colors[] = {0, 0, 0, 0, 0, 0};
		public Node(int m_id, int p_id, int color, int max_depth, int p_depth) {
			this.m_id = m_id;
			this.p_id = p_id;
			this.color = color;
			this.max_depth = max_depth;
			this.p_depth = p_depth;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		Q = Integer.parseInt(st.nextToken());

		// 그래프 초기화
		for (int i = 0; i <= 100000; i++) {
			tree.add(new ArrayList<Node>());
		}

		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			int orderNum = Integer.parseInt(st.nextToken());
			int m_id, p_id, color, max_depth;

			switch (orderNum) {
			// 노드 추가
			case 100: {
				m_id = Integer.parseInt(st.nextToken());
				p_id = Integer.parseInt(st.nextToken());
				color = Integer.parseInt(st.nextToken());
				max_depth = Integer.parseInt(st.nextToken());
				addNode(m_id, p_id, color, max_depth);
				break;
			}
			// 색깔 변경
			case 200: {
				m_id = Integer.parseInt(st.nextToken());
				color = Integer.parseInt(st.nextToken());
				changeColor(m_id, color);
				break;
			}
			// 색깔 조회
			case 300: {
				m_id = Integer.parseInt(st.nextToken());
				getColor(m_id);
				break;
			}
			// 점수 조회
			case 400: {
				for (int j = 0; j < root_id.size(); j++)
					getScore(root_id.get(j));
				sb.append(valueSum + "\n");	
				valueSum = 0;
				break;
			}
			}
		}
		System.out.println(sb);
	}

	public static void addNode(int m_id, int p_id, int color, int max_depth) {
		// 루트 노드라면
		if (p_id == -1) {
			root_id.add(m_id);
			tree.get(m_id).add(new Node(m_id, p_id, color, max_depth, max_depth));
		} else {
			Node parentNode = tree.get(p_id).get(0);
			int depth = Math.min(parentNode.max_depth, parentNode.p_depth);
			if (depth < 2)
				return;
			else {
				Node node = new Node(m_id, p_id, color, max_depth, depth - 1);
				tree.get(p_id).add(node);
				tree.get(m_id).add(node);
			}
		}
	}

	public static void changeColor(int m_id, int color) {
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(m_id);
		
		while (!q.isEmpty()) {
			int n = q.poll();
			tree.get(n).get(0).color = color;
			for (int i = 1; i < tree.get(n).size(); i++) {
				q.add(tree.get(n).get(i).m_id);
			}
		}
	}

	public static void getColor(int m_id) {
		sb.append(tree.get(m_id).get(0).color + "\n");
	}

	public static int getScore(int id) {
		// 리프 노드라면
		if (tree.get(id).size() == 1) {
			valueSum += 1;
			return bit[tree.get(id).get(0).color];
		}
		
		int colors = bit[tree.get(id).get(0).color];
		for (int i = 1; i < tree.get(id).size(); i++) {
			int c = getScore(tree.get(id).get(i).m_id);
			colors |= c;
		}
		
		int sum = 0;
		for (int i = 1; i <= 5; i++) {
			if ((colors & bit[i]) != 0)
				sum++;
		}

		valueSum += (int) Math.pow(sum, 2);

		return colors;
	}
}