package Lab_sc_modify1;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * tcp echo test, server side with thread pool
 * 
 * @author hou
 *
 */
public class EchoServerThreadPool {
	ServerSocket serverSocket;
	private final int PORT = 8080; // port
	ExecutorService executorService; // Thread pool
	final int POOL_SIZE = 4; // Number of worker threads per processor thread pool
	static int count = 1;

	public EchoServerThreadPool() throws IOException {
		serverSocket = new ServerSocket(PORT); // Create a client thread pool
		// Create a thread pool
		// Runtime's availableProcessors()'s method returns the number of processors available in the current system
		// The number of threads is determined by the JVM based on the system.
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * POOL_SIZE);
		System.out.println("Server initiate.");
	}

	public static void main(String[] args) throws IOException {
		new EchoServerThreadPool().servic(); // initiate service
	}

	/**
	 * service implements
	 */
	public void servic() {
		Socket socket = null;
		List<User> userList = new ArrayList<User>();
		List<PublicKey> pubKeyList = new ArrayList<PublicKey>();
		List<Socket> soList = new ArrayList<Socket>();
		List<User> oLuserList = new ArrayList<User>();
		while (true) {
			try {
				socket = serverSocket.accept(); // wait for user connection
				soList.add(socket);
				User user = new User(("user" + count),socket);
				count++;
//				userList.add(user);
//				String info = user.getBr().readLine();
				executorService.execute(new Handler(socket,userList,oLuserList,user,pubKeyList,soList,serverSocket)); //Give execution to the thread pool to maintain
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
