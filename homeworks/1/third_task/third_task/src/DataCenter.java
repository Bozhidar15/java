public class DataCenter {
    public static int getCommunicatingServersCount(int[][] map){
        int sum=0;
        if (map == null) {
            return -1;
        }
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 0) {
                    continue;
                }
                boolean ThereIsOne=false;
                for (int p = 0; p < map[i].length; p++) {
                if (map[i][p] == 1&&p!=j) {
                    sum++;
                    ThereIsOne = true;
                    break;
                }
                }
                if (ThereIsOne){
                    continue;
                }
                for (int p = 0; p < map[i].length; p++) {
                    if (map[p][j] == 1&&p!=i) {
                        sum++;
                        break;
                    }
                }
            }
        }
        return sum;
    }
    public static void main(String[] args) {
        System.out.println(getCommunicatingServersCount(new int[][]{ {1, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 1} }));
    }
}