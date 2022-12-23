import java.io.*;
import java.util.Objects;

public class project5main {
    public static void main(String[] args) throws IOException {
        long time = System.currentTimeMillis();
        BufferedReader in = new BufferedReader(new FileReader(args[0]));
        PrintStream out = new PrintStream(new FileOutputStream(args[1]));

        int numCities = Integer.parseInt(in.readLine());
        int kingsLanding = numCities + 7;
        String[] numTroops = in.readLine().strip().split(" ");
        Network westeros = new Network(numCities + 8);

        // create edges from a virtual source to regions
        for (int i = 1; i < 7; i++)
            westeros.addEdge(0, i, Integer.parseInt(numTroops[i - 1]));

        // add the nodes and edges to the network
        for (int i = 1; i <= numCities + 6; i++) {
            String[] info = in.readLine().strip().split(" ");
            if (info.length == 1)
                continue;
            for (int j = 1; j <= info.length - 1; j += 2) {
                if (Objects.equals(info[j], "KL")) {
                    westeros.addEdge(i, kingsLanding, Integer.parseInt(info[j + 1]));
                } else {
                    int nodeNum = Integer.parseInt(info[j].substring(1));
                    westeros.addEdge(i, 7 + nodeNum, Integer.parseInt(info[j + 1]));
                }
            }
        }
        in.close();
        westeros.Dinic(kingsLanding);
        out.println(westeros.maxFlow);
        westeros.minCut(out);
        out.close();
        System.out.println(System.currentTimeMillis() - time);
    }
}
