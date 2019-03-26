import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        // 超时时间
        socket.setSoTimeout(3000);
        // 链接本地， 端口2000
        SocketAddress socketAddress = new InetSocketAddress(Inet4Address.getLocalHost(), 2000);
        socket.connect(socketAddress);

        System.out.println("已发起服务器连接，并进入后续流程");
        System.out.println(String.format("客户端信息：%s P:%d", socket.getLocalAddress(), socket.getLocalPort()));
        System.out.println(String.format("服务端信息：%s P:%d", socket.getInetAddress(), socket.getPort()));

        try {
            todo(socket);
        } catch (Exception e) {
            System.out.println("异常关闭");
        }

        socket.close();
        System.out.println("客户端已退出");
    }

    private static void todo(Socket client) throws IOException {
        // 客户端系统键盘输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        // 客户端系统输出流
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);

        // socket输入流（即socket的返回输出）
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        boolean flag = true;
        do {
            // 从键盘输入，输出给socket服务端
            String str = input.readLine();
            socketPrintStream.println(str);

            // 从服务器获得数据
            String echo = socketBufferedReader.readLine();
            if ("bye".equals(echo)) {
                flag = false;
            } else {
                System.out.println(echo);
            }
        } while (flag);

        socketPrintStream.close();
        socketBufferedReader.close();
    }
}
