import java.util.*;
import java.io.*;
public class Main {
    static int MAX_DISTANCE = Integer.MAX_VALUE;
    static Integer startNode = -1;
    static Integer n;
    static int[] dijkstra;
    static boolean[] visit;
    static Node[] nodes;
    static TreeSet<Product> products = new TreeSet<>();

    static class Product implements Comparable<Product> {
        Integer id;
        Integer sales;
        Integer end;
        Integer distance;
        Product(){
        }
        Product(Integer id, Integer sales, Integer end){
            this.id = id;
            this.sales = sales;
            this.end = end;
            this.distance = visit[end] ? dijkstra[end] : MAX_DISTANCE;
        }
        @Override
        public int compareTo(Product another) {
            Integer thisNetIncome = sales - distance;
            Integer anotherNetIncome = another.sales - another.distance;
            if(thisNetIncome.equals(anotherNetIncome)){
                return this.id.compareTo(another.id);
            }
            return (anotherNetIncome).compareTo(thisNetIncome);
        }
    }

    static class Node{
        Map<Integer, Integer> nextNodes = new HashMap<>();

        void addNode(Integer nextId, Integer cost){
            nextNodes.merge(nextId, cost, (oV, nV) -> oV < nV ? oV : nV);
        }
    }

    static class Pair implements Comparable<Pair> {
        Integer left;
        Integer right;

        Pair(){}
        Pair(Integer left, Integer right){
            this.left = left;
            this.right = right;
        }
        @Override
        public int compareTo(Pair other) {
            return left.compareTo(other.left);
        }
    }

    static boolean doDijkstra(Integer newStartNode){
        if (startNode == newStartNode) return false;
        startNode = newStartNode;
        dijkstra = new int[n];
        visit = new boolean[n];
        visit[startNode] = true;

        Queue<Pair> pq = new PriorityQueue<>();
        pq.add(new Pair(0, startNode));

        while (!pq.isEmpty()) {
            Pair now = pq.poll();
            if(dijkstra[now.right] != 0 && dijkstra[now.right] < now.left) continue;
            visit[now.right] = true;
            for(Integer next: nodes[now.right].nextNodes.keySet()){
                if(visit[next]) continue;
                Integer nextValue = nodes[now.right].nextNodes.get(next);
                if(dijkstra[next] != 0 && nextValue + now.left > dijkstra[next]) continue;
                dijkstra[next] = nextValue + now.left;
                pq.add(new Pair(dijkstra[next], next));
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;
        int Q = Integer.parseInt(br.readLine());

        st = new StringTokenizer(br.readLine());
        st.nextToken();

        n = Integer.parseInt(st.nextToken());
        nodes = new Node[n];
        
        // 노드 초기화
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
        }

        int m = Integer.parseInt(st.nextToken());

        while(m-- > 0){
            int vi = Integer.parseInt(st.nextToken());
            int ui = Integer.parseInt(st.nextToken());
            int wi = Integer.parseInt(st.nextToken());

            nodes[vi].addNode(ui, wi);
            nodes[ui].addNode(vi, wi);
        }
        doDijkstra(0);

        while(Q-- > 1){
            st = new StringTokenizer(br.readLine());
            String input = st.nextToken();
            if(input.equals("500")){
                int newNode = Integer.parseInt(st.nextToken());
                if(doDijkstra(newNode)){
                    TreeSet<Product> newProducts = new TreeSet<>();
                    for(Product p: products){
                        newProducts.add(new Product(p.id, p.sales, p.end));
                    }
                    products = newProducts;
                }
            }
            if(input.equals("200")){
                Integer id = Integer.parseInt(st.nextToken());
                Integer sales = Integer.parseInt(st.nextToken());
                Integer destId = Integer.parseInt(st.nextToken());
                Product newProduct = new Product(id, sales, destId);
                products.add(newProduct);
            }
            if(input.equals("300")){
                Integer id = Integer.parseInt(st.nextToken());
                for(Product p : products){
                    if(id.equals(p.id)) {
                        products.remove(p);
                        break;
                    }
                }
            }
            if(input.equals("400")){
                if(products.isEmpty()) {
                    bw.write("-1\n");
                    continue;
                }
                Product first = products.pollFirst();
                if(first == null || first.distance > first.sales){
                    bw.write("-1\n");
                    products.add(first);
                }
                else{
                    bw.write(first.id+"\n");
                }
            }
        }
        bw.flush();
        bw.close();
    }
}