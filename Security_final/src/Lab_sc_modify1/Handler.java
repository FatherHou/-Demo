package Lab_sc_modify1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * server thread
 * 
 * @author hou
 *
 */
public class Handler implements Runnable { // The thread responsible for communicating with a single client
	private Socket socket;
	BufferedReader br;
	BufferedWriter bw;
	PrintWriter pw;
	private List<User> userList;
	private User user;
	private List<PublicKey> pubKeyList;
	private List<Socket> soList;
	private List<User> oLuserList = new ArrayList<User>();
	private ServerSocket serverSocket;
	private String cer;
//	private String info;

	public Handler(Socket socket, List<User> userList, List<User> oLuserList, User user, List<PublicKey> pubKeyList, List<Socket> soList, ServerSocket serverSocket) {
		this.socket = socket;
		this.userList = userList;
		this.user = user;
		this.pubKeyList = pubKeyList;
		this.soList = soList;
		this.oLuserList = oLuserList;
		this.serverSocket = serverSocket;
//		this.info = info;
	}

	public void initStream() throws IOException { // Initialize the input and output stream object method
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bw = new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream()));
		pw = new PrintWriter(bw, true);
	}
	
	
	public void run() { // Executed content
		try {
			System.out.println("New connection,Address:" + socket.getInetAddress() + ":"
					+ socket.getPort()); //Client information
			initStream(); // Initialize the input and output stream object
			String info = null;
			String[] split ;
			while(true){
				pw.println(user.getName());
				info = user.getBr().readLine();
				split = info.split(" ");
				if(split[0].equals("register")){
					boolean flag2 = true;
					for(int i=0;i<userList.size();i++){
						if(userList.get(i).getName().equals(split[1])){
							pw.println("Same name.");
							flag2 = false;
						}
					}
					if(flag2 == true){
						user.setName(split[1]);
						user.setPassword(split[2]);
						user.setLock(true);
						userList.add(user);
						oLuserList.add(user);
						pw.println("register Success");
						break;
					}
					continue;
				}else if(split[0].equals("login")){
					boolean flag2 = true;
					boolean usExFlag = false;
					for(int i=0;i<userList.size();i++){
						if(userList.get(i).getName().equals(split[1])){
							if(userList.get(i).getPassword().equals(split[2])){
								usExFlag = true;
								Socket ts = user.getSocket();
								userList.get(i).setSocket(ts);
								userList.get(i).setLock(true);
								oLuserList.add(userList.get(i));
								flag2 = false;
								pw.println("login Success");
							}else{
								pw.println("Wrong password.");
							}
						}
					}
					if(usExFlag == false){
						pw.println("Unknown name.");
					}
					if(flag2 != false)
						break;
					continue;
				}
			}
			for(int i=0;i<userList.size();i++){
				if(userList.get(i).getName().equals(user.getName())){
					userList.get(i).setPort(socket.getPort());
				}
			}
			
			String reciever = null;
			int flag = 0;
			for(int i=0;i<userList.size();i++){
				System.out.println(userList.get(i).getName()+" "+userList.get(i).getPassword()+" "+userList.get(i).isLock());
			}
			for(int i=0;i<soList.size();i++){
				System.out.println(soList.get(i).getInetAddress() + " " + soList.get(i).getPort());
			}

			int flag2 = 0;
			while (true) {
				info = user.getBr().readLine();
				split = info.split(" ");
				if(flag2 == 0){
					for(int i=0;i<oLuserList.size();i++){
						if(oLuserList.get(i).getName().equals(user.getName())){
							System.out.println("ano:" + split[0]);
							oLuserList.get(i).setAnoPort(Integer.valueOf(split[0]));
						}
					}
					flag2++;
				}else if(flag2 == 1){
					BigInteger[] pubKey = {new BigInteger(split[0]),new BigInteger(split[1])};
					for(int i=0;i<oLuserList.size();i++){
						if(oLuserList.get(i).getName().equals(user.getName())){
							oLuserList.get(i).setDsKey(pubKey);
						}
					}
					flag2++;
				}else if(flag2 == 2){
					for(int i=0;i<oLuserList.size();i++){
						if(oLuserList.get(i).getName().equals(user.getName())){
							System.out.println(info);
							Certificate cer = new Certificate(split[0],split[1]);
							if(split.length == 3){
								cer.setKey(split[2]);
							}else if(split.length == 4){
								BigInteger[] pubKey = {new BigInteger(split[2]),new BigInteger(split[3])};
								cer.setPubKey(pubKey);
								pubKeyList.add(new PublicKey(user.getName(),pubKey));
							}else{
								
							}
							oLuserList.get(i).setCer(cer);
						}
					}
					flag2++;
//				}else if(flag2 == 2){
//					RSA rsa = new RSA();
//					BigInteger p = new BigInteger(split[0]);
//					BigInteger q = new BigInteger(split[1]);
//					BigInteger[][] key = rsa.caKey(p,q);
//					BigInteger[] pubKey = key[0];
//					pubKeyList.add(new PublicKey(user.getName(),pubKey));
//					flag2++;
				}else{
					if(info.equals("request")){
						for(int i=0;i<oLuserList.size();i++){
							if(oLuserList.get(i).getName().equals(user.getName()) == false){
								pw = user.getPw();
								pw.println("request " + oLuserList.get(i).getName()+",Port="+oLuserList.get(i).getPort()+",anotherPort="+oLuserList.get(i).getAnoPort()+",state="+oLuserList.get(i).isLock());
							}
						}
					}else if(info.equals("quit")){
						for(int i=0;i<oLuserList.size();i++){
							if(oLuserList.get(i).getName().equals(user.getName())){
								oLuserList.remove(oLuserList.get(i));
							}
						}
					}
					if(split[0].equals("connect")){
						int ano = 0;
						for(int i=0;i<oLuserList.size();i++){
							if(oLuserList.get(i).getName().equals(split[1])){
								ano = oLuserList.get(i).getAnoPort();
							}
						}
						boolean flag4 = false;
						for(int i=0;i<oLuserList.size();i++){
//							for(int j=0;j<pubKeyList.size();j++){
//								if(oLuserList.get(i).getName().equals(user.getName())&&pubKeyList.get(j).getUser().equals(split[1])){
//									flag4 = true;
//									pw = oLuserList.get(i).getPw();
//									pw.println("sender " + ano + " " + String.valueOf(pubKeyList.get(j).getPubKey()[0]) + " " + String.valueOf(pubKeyList.get(j).getPubKey()[1]) + " " + oLuserList.get(i).getCer().getHash() + " " + oLuserList.get(i).getCer().getEnAlgorithm());
//								}
//								if(oLuserList.get(i).getName().equals(split[1])&&pubKeyList.get(j).getUser().equals(user.getName())){
//									flag4 = true;
//									System.out.println(user.getName());
//									pw = oLuserList.get(i).getPw();
//									pw.println("reciever " + oLuserList.get(i).getPort() + " " + String.valueOf(pubKeyList.get(j).getPubKey()[0]) + " " + String.valueOf(pubKeyList.get(j).getPubKey()[1]) + " " + oLuserList.get(i).getCer().getHash() + " " + oLuserList.get(i).getCer().getEnAlgorithm());
//								}
//							}
							if(oLuserList.get(i).getName().equals(user.getName())){
								for(int j=0;j<oLuserList.size();j++){
									if(oLuserList.get(j).getName().equals(split[1])){
										flag4 = true;
										pw = oLuserList.get(i).getPw();
										if(oLuserList.get(j).getCer().getEnAlgorithm().equals("0")||oLuserList.get(j).getCer().getEnAlgorithm().equals("1")||oLuserList.get(j).getCer().getEnAlgorithm().equals("3")){
											pw.println("sender " + ano + " " + oLuserList.get(j).getCer().getHash() + " " + oLuserList.get(j).getCer().getEnAlgorithm() + " " + oLuserList.get(j).getCer().getKey() + " " + String.valueOf(oLuserList.get(j).getDsKey()[0]) + " " + String.valueOf(oLuserList.get(j).getDsKey()[1]));
										}else if(oLuserList.get(j).getCer().getEnAlgorithm().equals("2")){
											pw.println("sender " + ano + " " + oLuserList.get(j).getCer().getHash() + " " + oLuserList.get(j).getCer().getEnAlgorithm() + " " + String.valueOf(oLuserList.get(j).getCer().getPubKey()[0]) + " " + String.valueOf(oLuserList.get(j).getCer().getPubKey()[1]) + " " + String.valueOf(oLuserList.get(j).getDsKey()[0]) + " " + String.valueOf(oLuserList.get(j).getDsKey()[1]));
										}else if(oLuserList.get(j).getCer().getEnAlgorithm().equals("4")){
											pw.println("sender " + ano + " " + oLuserList.get(j).getCer().getHash() + " " + oLuserList.get(j).getCer().getEnAlgorithm() + " " + String.valueOf(oLuserList.get(j).getDsKey()[0]) + " " + String.valueOf(oLuserList.get(j).getDsKey()[1]));
										}
									}
								}	
							}
							if(oLuserList.get(i).getName().equals(split[1])){
								for(int j=0;j<oLuserList.size();j++){
									if(oLuserList.get(j).getName().equals(user.getName())){
										flag4 = true;
										pw = oLuserList.get(i).getPw();
										if(oLuserList.get(j).getCer().getEnAlgorithm().equals("0")||oLuserList.get(j).getCer().getEnAlgorithm().equals("1")||oLuserList.get(j).getCer().getEnAlgorithm().equals("3")){
											pw.println("reciever " + ano + " " + oLuserList.get(j).getCer().getHash() + " " + oLuserList.get(j).getCer().getEnAlgorithm() + " " + oLuserList.get(j).getCer().getKey() + " " + String.valueOf(oLuserList.get(j).getDsKey()[0]) + " " + String.valueOf(oLuserList.get(j).getDsKey()[1]));
										}else if(oLuserList.get(j).getCer().getEnAlgorithm().equals("2")){
											pw.println("reciever " + ano + " " + oLuserList.get(j).getCer().getHash() + " " + oLuserList.get(j).getCer().getEnAlgorithm() + " " + String.valueOf(oLuserList.get(j).getCer().getPubKey()[0]) + " " + String.valueOf(oLuserList.get(j).getCer().getPubKey()[1]) + " " + String.valueOf(oLuserList.get(j).getDsKey()[0]) + " " + String.valueOf(oLuserList.get(j).getDsKey()[1]));
										}else if(oLuserList.get(j).getCer().getEnAlgorithm().equals("4")){
											pw.println("reciever " + ano + " " + oLuserList.get(j).getCer().getHash() + " " + oLuserList.get(j).getCer().getEnAlgorithm() + " " + String.valueOf(oLuserList.get(j).getDsKey()[0]) + " " + String.valueOf(oLuserList.get(j).getDsKey()[1]));
										}
									}
								}	
							}
							
						}
						if(flag4 == false){
							pw = user.getPw();
							pw.println("No such person.");
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != socket) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
}
