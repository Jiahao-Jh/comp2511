package unsw.gloriaromanus;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Graph implements Serializable{

    private static final long serialVersionUID = 1L;
    private int v;
    private LinkedList<Province> provinces;
    private Boolean[][] adj;
    
    public Graph() {
    }

    public void constructGraph(LinkedList<Province> pList, int v) throws IOException {
        this.v = v;
        provinces = pList;
        adj = new Boolean[v][v];
        for (int i = 0; i < v; i++) {
            for (int j = 0; j < v; j++) {
                adj[i][j] = false;
            }
        }
        String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
        JSONObject connections = new JSONObject(content);
        for (Province p : pList) {
            JSONObject connectedProvinces = connections.getJSONObject(p.getName());
            for (String name : connectedProvinces.keySet()) {
                int index = getIndex(name);
                if (index != -1) {
                    adj[provinces.indexOf(p)][index] = connectedProvinces.getBoolean(name);
                    adj[index][provinces.indexOf(p)] = connectedProvinces.getBoolean(name);
                }
            }
        }
    }

    public int reachable(Province start, Province dest) {
        int mpCost = 0;
        String startName = start.getName();
        String destName = dest.getName();
        int startInt = getIndex(startName);
        int destInt = getIndex(destName);
        int[] pred = new int[v];
        if (BFS(startInt, destInt, pred)) {
            int i = destInt;
            while (pred[i] != -1) {
                mpCost += 4;
                i = pred[i];
            }
        }
        return mpCost;
    }

    public boolean BFS(int start, int dest, int[] pred) {
        boolean seen[] = new boolean[v];
        LinkedList<Integer> queue = new LinkedList<Integer>();
        for (int i = 0; i < v; i++) { 
            seen[i] = false; 
            pred[i] = -1; 
        } 
        seen[start] = true; 
        queue.add(start);
        while (!queue.isEmpty()) {
            int curr = queue.removeFirst();
            for (int i = 0; i < v; i++) {
                if (adj[curr][i] && !seen[i]) {
                    queue.add(i);
                    pred[i] = curr;

                    if (i == dest) return true;
                }
            }
        }
        return false;
    } 

    public Boolean getBean(int x, int y) {
        return adj[x][y];
    }


    public int getIndex(String s) {
        for (Province p : provinces) {
            if (p.getName().equals(s))
                return provinces.indexOf(p);
        }
        return -1;
    }
}
