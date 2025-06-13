import java.io.*;
import java.util.*;

public class Main {
    
    static int N, Q, map[][], count[];
    // 상, 하, 좌, 우
    static int dx[] = {-1, 1, 0, 0};
    static int dy[] = {0, 0, -1, 1}; 
    static StringBuffer sb = new StringBuffer();

    public static class Node implements Comparable<Node> {
        int x, y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Node n) {
            if (Integer.compare(n.y, this.y) == 0) {
                return Integer.compare(this.x, n.x);
            }
            else return Integer.compare(n.y, this.y);
        }

        @Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Node)) return false;
			Node node = (Node) o;
			return x == node.x && y == node.y;
		}
		
		@Override
		public int hashCode() {
			return 31 * x + y;
		}
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        

        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());
        map = new int[N][N];
        count = new int[Q + 1];

        for (int q = 1; q <= Q; q++) {
            st = new StringTokenizer(br.readLine());
            int r1 = Integer.parseInt(st.nextToken());
            int c1 = Integer.parseInt(st.nextToken());
            int r2 = Integer.parseInt(st.nextToken());
            int c2 = Integer.parseInt(st.nextToken());
            putMicro(r1, c1, r2, c2, q);
            map = moveMicro();
            // System.out.println("===after putMicro===");
            // debuging();
            recordResult();

            
        }
        System.out.print(sb.toString());
    }

    public static void debuging() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                System.out.print(map[r][c] + " ");
            }
            System.out.println();
        }
    }

    public static void putMicro(int r1, int c1, int r2, int c2, int order) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        ArrayList<Integer> deleteList = new ArrayList<Integer>();

        for (int r = r1; r < r2; r++) {
            for (int c = c1; c < c2; c++) {

                if (map[r][c] != 0) {
                    count[map[r][c]]--;
                    if (!list.contains(map[r][c])) {
                        list.add(map[r][c]);
                    }
                }
                map[r][c] = order;
            }
        }
        count[order] = (r2 - r1) * (c2 - c1);

        int tempR1 = r1 - 1 < 0 ? r1 : r1 - 1;
        int tempC1 = c1 - 1 < 0 ? c1 : c1 - 1;
        int tempR2 = r2 + 1 >= N ? r2 : r2 + 1;
        int tempC2 = c2 + 1 >= N ? c2 : c2 + 1;

        for (int r = tempR1; r < tempR2; r++) {
            for (int c = tempC1; c < tempC2; c++) {
                int num = map[r][c];
                // for (int m = 0; m < deleteList.size(); m++) {
                //     System.out.println("deleteList");
                //     System.out.println(deleteList.get(m));
                // }
                if (num != 0 && list.contains(num) && !deleteList.contains(num)) {
                    
                    ArrayDeque<Node> dq = new ArrayDeque<Node>();
                    boolean visited[][] = new boolean[N][N];
                    dq.offer(new Node(r, c));
                    visited[r][c] = true;
                    int cnt = 1;
                    while (!dq.isEmpty()) {
                        Node node = dq.poll();

                        for (int d = 0; d < 4; d++) {
                            int nx = node.x + dx[d];
                            int ny = node.y + dy[d];

                            if (nx < 0 || nx >= N || ny < 0 || ny >= N || visited[nx][ny]) continue;
                            if (map[nx][ny] == num) {
                                dq.offer(new Node(nx, ny));
                                cnt++;
                                visited[nx][ny] = true;
                            }
                        }
                    }
                    // System.out.println("num" + num + ", cnt = " + cnt);
                    if (cnt != count[num]) {
                        deleteList.add(num);
                        count[num] = 0;
                    }
                }
            }
        }

        for (int i = 0; i < deleteList.size(); i++) {
            int dNum = deleteList.get(i);
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    if (map[r][c] == dNum) {
                        map[r][c] = 0;
                    }
                }
            }
        }

        
        
        // System.out.println("===putMicro===");
        // debuging();
    }

    public static int[][] moveMicro() {
        // System.out.println(Arrays.toString(count));
        PriorityQueue<Node> pq = new PriorityQueue<Node>();
        int newMap[][] = new int[N][N];

        for (int i = 1; i <= Q; i++) {
            if (count[i] > 0) {
                pq.offer(new Node(i, count[i]));
            }
        }

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            // System.out.println("pq: " + node.x + ", " + node.y);

            int minR = N;
            int minC = N;
            int maxR = 0;
            int maxC = 0;

            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    if (map[r][c] == node.x) {
                        minR = Math.min(minR, r);
                        minC = Math.min(minC, c);
                        maxR = Math.max(maxR, r);
                        maxC = Math.max(maxC, c);
                    }
                }
            }

            // System.out.println("math : " + minR + ", " + minC + ", " + maxR + ", " + maxC);

            int startR = -1;
            int startC = -1;
            f1: for (int r = 0; r < N; r++) {
                f2: for (int c = 0; c < N; c++) {
                    if (newMap[r][c] == 0) {
                        if (c + maxC - minC + 1 <= N && r + maxR - minR + 1 <= N) {
                            startR = r;
                            startC = c;
                            break f1;
                        }
                        // else break f2;
                    }
                }
                if (r + maxR - minR + 1 > N) break;
            }
            if (startR == -1 && startC == -1) continue;
            else {
                // System.out.println("start: " + startR + ", " + startC);
                for (int r = 0; r <= maxR - minR; r++) {
                    for (int c = 0; c <= maxC - minC; c++) {
                        if (map[minR + r][minC + c] == node.x) {
                            newMap[startR + r][startC + c] = node.x;
                        }
                    }
                }
            }
            
        }

        return newMap;
    }

    public static void recordResult() {
        HashSet<Node> hs = new HashSet<Node>();
        boolean[][] visited;
        int result = 0;
        ArrayDeque<Node> dq = new ArrayDeque<Node>();

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (map[r][c] != 0) {
                    visited = new boolean[N][N];
                    int n = map[r][c];

                    dq.offer(new Node(r, c));
                    visited[r][c] = true;
                    
                    while (!dq.isEmpty()) {
                        Node node = dq.poll();
                        for (int d = 0; d < 4; d++) {
                            int nx = node.x + dx[d];
                            int ny = node.y + dy[d];

                            if (nx < 0 || nx >= N || ny < 0 || ny >= N || visited[nx][ny]) continue;
                            if (map[nx][ny] == n) {
                                dq.offer(new Node(nx, ny));
                                visited[nx][ny] = true;
                            } else {
                                if (map[nx][ny] != 0) {
                                    int a = Math.min(n, map[nx][ny]);
                                    int b = Math.max(n, map[nx][ny]);
                                    hs.add(new Node(a, b));
                                }
                            }
                        }
                    }

                }
            }
        }

        Iterator<Node> iterator = hs.iterator();

        while (iterator.hasNext()) {
            Node element = iterator.next();

            // System.out.println("!!" + element.x + ", " + element.y);
            result += count[element.x] * count[element.y];
        }
        sb.append(result + "\n");
    }
}