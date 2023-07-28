package bg.sofia.uni.fmi.mjt.vault.server;

import bg.sofia.uni.fmi.mjt.vault.exception.NoValidOperationException;
import bg.sofia.uni.fmi.mjt.vault.exception.ChangePasswordException;
import bg.sofia.uni.fmi.mjt.vault.exception.DisconnectOrLogoutException;
import bg.sofia.uni.fmi.mjt.vault.exception.SendMessageException;
import bg.sofia.uni.fmi.mjt.vault.exception.ReadingDataFromFileException;
import bg.sofia.uni.fmi.mjt.vault.exception.ShutDownServerException;
import bg.sofia.uni.fmi.mjt.vault.users.UserProfile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Server {
    private static final int BUFFER_SIZE = 1024;
    private static final String HOST = "localhost";
    private final int port; //create and change
    private boolean isServerWorking;
    private ByteBuffer buffer;
    private Selector selector;

    private AccountsForServer accountsForServer;

    private Map<SocketAddress, UserProfile> clientsInTheSystem;

    public Server(int port) {
        this.port = port;
    }

    public void start() {

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            clientsInTheSystem = new HashMap<>();
            accountsForServer = new AccountsForServer();
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            buffer = ByteBuffer.allocateDirect(BUFFER_SIZE); //allocateDirect
            isServerWorking = true;
            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();

                            ServerSignUpMessages signUp = new ServerSignUpMessages(clientChannel, accountsForServer);
                            SocketAddress currentAddress = clientChannel.socket().getRemoteSocketAddress();
                            //clientChannel.getRemoteAddress();

                            String clientInput = getClientInput(clientChannel);
                            if (clientInput == null) {
                                continue;
                            }

                            System.out.println("Client: " + currentAddress.toString() + System.lineSeparator());
                            System.out.println(clientInput + System.lineSeparator());
                            String returnMessage;
                            if (clientsInTheSystem.containsKey(currentAddress)) {
                                try {
                                    returnMessage = clientsInTheSystem.get(currentAddress).getOption(clientInput);
                                    System.out.println(returnMessage);
                                } catch (NoValidOperationException e) {
                                    writeClientOutput(clientChannel, e.getMessage());
                                    System.out.println(e.getMessage());
                                } catch (DisconnectOrLogoutException e) {
                                    clientsInTheSystem.remove(currentAddress);
                                    System.out.println("Successful..." + System.lineSeparator());
                                } catch (ChangePasswordException e) {
                                    if (signUp.changePassword(e.getMessage())) {
                                        String mess = "Successful changing password!"
                                                + System.lineSeparator();
                                        writeClientOutput(clientChannel, mess);
                                        System.out.println(mess);
                                    } else {
                                        String mess = "Couldn't change your password!"
                                                + System.lineSeparator();
                                        writeClientOutput(clientChannel, mess);
                                        System.out.println(mess);
                                    }
                                } catch (SendMessageException e) {
                                    throw new IOException();
                                }
                            } else {
                                try {
                                    if (signUp.getOption(clientInput)) {
                                        clientsInTheSystem.put(currentAddress,
                                                new UserProfile(signUp.getCurrentClientData(), clientChannel));
                                        writeClientOutput(clientChannel, "Welcome to the server!"
                                                + System.lineSeparator());
                                        System.out.println("Client is in the system!" + System.lineSeparator());
                                    } else {
                                        System.out.println("Disconnect or didn't accomplish to login/register" +
                                                System.lineSeparator());
                                    }
                                } catch (ReadingDataFromFileException e) {
                                    writeClientOutput(clientChannel, "Couldn't login, please try again later!" +
                                            System.lineSeparator());
                                    System.out.println(e.getMessage());
                                } catch (NoValidOperationException e) {
                                    writeClientOutput(clientChannel, e.getMessage());
                                    System.out.println(e.getMessage());
                                } catch (ShutDownServerException e) {
                                    isServerWorking = false;
                                    break;
                                } catch (SendMessageException e) {
                                    throw new IOException("error while sending message");
                                }
                            }

                        } else if (key.isAcceptable()) {
                            accept(selector, key);
                        }
                        System.out.println(System.lineSeparator());
                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to start server", e);
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector)
            throws IOException {
        channel.bind(new InetSocketAddress(HOST, port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }
}