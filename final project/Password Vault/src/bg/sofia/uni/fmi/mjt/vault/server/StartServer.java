package bg.sofia.uni.fmi.mjt.vault.server;

public class StartServer {
    public static void main(String[] args) {
        final int port = 8888;
        Server server = new Server(port);
        server.start();
    }
}
