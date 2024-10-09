import java.util.*;
import java.io.*;

public class Main {
	static int R, C, K, map[][], r, c, d, dp[], result;
	// 상, 우, 하, 좌
	static int dr[] = {-1, 0, 1, 0};
	static int dc[] = {0, 1, 0, -1};
	static boolean stop;
	
	public static class Node {
		int x, y;
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		R = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		map = new int[R + 3][C + 1];
		dp = new int[K + 1];
		result = 0;
		int depth = 0;
		
		for (int i = 1; i <= K; i++) {
			st = new StringTokenizer(br.readLine());
			r = 1;
			c = Integer.parseInt(st.nextToken());
			d = Integer.parseInt(st.nextToken());
			stop = false;

			while (true) {
				if (!goDown()) {
					if (!turnLeft()) {
						// 오른쪽 회전까지 불가능하면 더이상 이동 불가능
						if (!turnRight()) {
							stop = true;
						}
					}
				}
				if (stop) {
					// 골렘의 몸의 일부가 숲을 벗어나면 map, dp 초기화
					if (r  <= 3) {
						for (int x = 0; x < R + 3; x++) {
							Arrays.fill(map[x], 0);
						}
						Arrays.fill(dp, 0);
						depth = 0;
					}
					// map에 블럭 쌓기
					else {
						map[r][c] = i;
						for (int j = 0; j < 4; j++) {
							// 출구는 -1로 표시
							if (j == d) map[r + dr[j]][c + dc[j]] = i * -1;
							else map[r + dr[j]][c + dc[j]] = i;
						}
						depth = moveFairy();
						dp[i] = depth;
						result += depth;
					}
					break;
				}
			}
		}
		System.out.println(result);
	}
	
	public static boolean goDown() {
		// 바닥에 도착하면 stop
		if (r + dr[2] == R + 2) {
			stop = true;
			return true;
		}
		int r1 = r + dr[2] + dr[3], c1 = c + dc[2] + dc[3];
		int r2 = r + dr[2] * 2, c2 = c + dc[2] * 2;
		int r3 = r + dr[1] + dr[2], c3 = c + dc[1] + dc[2];

		// 이동할 수 있다면
		if (map[r1][c1] == 0 && map[r2][c2] == 0 && map[r3][c3] == 0) {
			// 좌표 이동
			r += dr[2];
			c += dc[2];
			return true;
		}
		
		return false;
	}
	
	public static boolean turnLeft() {
		int r1 = r + dr[0] + dr[3], c1 = c + dc[0] + dc[3];
		int r2 = r + dr[3] * 2, c2 = c + dc[3] * 2;
		int r3 = r + dr[2] + dr[3], c3 = c + dc[2] + dc[3];
		
		// 왼쪽 범위 벗어난 경우
		if (c2 == 0) return false;
		
		// 왼쪽으로 이동 가능한 경우
		if (map[r1][c1] == 0 && map[r2][c2] == 0 && map[r3][c3] == 0) {
			int r4 = r2 + dr[2], c4 = c2 + dc[2];
			int r5 = r3 + dr[2], c5 = c3 + dc[2];
			
			// 아래로도 이동 가능한 경우
			if (map[r4][c4] == 0 && map[r5][c5] == 0) {
				r = r3;
				c = c3;
				d = d > 0 ? d - 1 : 3;
				return true;
			}
		}
		return false;
	}
	
	public static boolean turnRight() {
		int r1 = r + dr[0] + dr[1], c1 = c + dc[0] + dc[1];
		int r2 = r + dr[1] * 2, c2 = c + dc[1] * 2;
		int r3 = r + dr[1] + dr[2], c3 = c + dc[1] + dc[2];
		
		// 오른쪽 범위 벗어난 경우
		if (c2 == C + 1) return false;
		
		// 오른쪽으로 이동 가능한 경우
		if (map[r1][c1] == 0 && map[r2][c2] == 0 && map[r3][c3] == 0) {
			int r4 = r2 + dr[2], c4 = c2 + dc[2];
			int r5 = r3 + dr[2], c5 = c3 + dc[2];
			
			// 아래로도 이동 가능한 경우
			if (map[r4][c4] == 0 && map[r5][c5] == 0) {
				r = r3;
				c = c3;
				d = d < 3 ? d + 1 : 0;
				return true;
			}
		}
		return false;
	}
	
	// bfs로 이동할 수 있는 최대 깊이 탐색
	public static int moveFairy() {
//		// 다른 골렘으로 이동할 수 없는 경우의 최대 깊이
//		int depth = r - 1; 
		boolean[][] visited = new boolean[R + 3][C + 1];
		Queue<Node> q = new LinkedList<Node>();
		int depth = 0;
		
		visited[r][c] = true;
		q.add(new Node(r, c));
		
		while (!q.isEmpty()) {
			Node n = q.poll();
			depth = Math.max(depth, n.x);
			for (int i = 0; i < 4; i++) {
				int nr = n.x + dr[i];
				int nc = n.y + dc[i];
				// map 범위를 벗어나면 pass
				if (nr < 3 || nr > R + 2 || nc < 1 || nc > C) continue;
				// 빈 칸이면 pass
				if (map[nr][nc] == 0) continue;
				if (!visited[nr][nc]) {
					if (Math.abs(map[nr][nc]) == Math.abs(map[n.x][n.y])) {
						visited[nr][nc] = true;
						q.add(new Node(nr, nc));
					}
					
					if (map[n.x][n.y] < 0) {
						visited[nr][nc] = true;
						q.add(new Node(nr, nc));
					}
				}
			}
		}
		
		return depth - 2;
	}
}