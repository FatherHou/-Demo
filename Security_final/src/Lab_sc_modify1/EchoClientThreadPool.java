/**
 * 
 */
package Lab_sc_modify1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hou
 *
 */
public class EchoClientThreadPool {
	ServerSocket serverSocket;
	private final int PORT = 8080; // Port
	static final String HOST = "127.0.0.1"; //Address
	Socket socket = new Socket();
	ExecutorService executorService; //threadpool
	final int POOL_SIZE = 4; //Number of worker threads per processor thread pool
	static int k=0;//Judging what is the time to enter ciphertext(like first,second...)
    private BigInteger[] enPubkey = new BigInteger[2];
    private BigInteger[] enPrikey = new BigInteger[2];
    private String key = "";	//other's key
    private String myKey;	//my key
    private Certificate cer = new Certificate();	//other's certification
    private Certificate myCer = new Certificate();	//my certification
    private BigInteger[] dsPri;	//digital signature private key

	public EchoClientThreadPool() throws IOException {	//Create a client thread pool
		socket = new Socket();
		socket.connect(new InetSocketAddress(HOST, PORT));
		// Create a thread pool
		// Runtime's availableProcessors()'s method returns the number of processors available in the current system
		// The number of threads is determined by the JVM based on the system.
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * POOL_SIZE);
	}

	public static void main(String[] args) throws IOException {
		new EchoClientThreadPool().servic(); // initiate service
	}

	/**
	 * service implements
	 * @throws IOException 
	 */
	public void servic() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		PrintWriter pw = new PrintWriter(socket.getOutputStream());
//		String msg = null;
//		executorService.execute(new cHandler(socket,msg));
		Scanner in = new Scanner(System.in);
		String msg = null;
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		PrintWriter pw = new PrintWriter(bw, true);
		String[] split = new String[4];
		String cyperText = null;
		serverSocket = new ServerSocket(0);
//		while (true) {
			try{
				cHandler subC = new cHandler(socket,serverSocket,enPubkey,enPrikey,cer,key,dsPri,myCer);
				executorService.execute(subC); // Give execution to the thread pool to maintain
				//
				System.out.println("Please chose format:login/register [username] [password].");
				while(true){	//send name and password
					msg=in.nextLine();
					if(subC.isValue()==true){
						break;
					}
					split = msg.split(" ");
					MD5 md5 = new MD5();
					msg = "";
					split[2] = md5.start(split[2]);
					for(int i=0;i<3;i++){
						if(i != 2)
							msg += split[i] + " ";
						else
							msg += split[i];
					}
					pw.println(msg);
					System.out.println("If you recieve successful message, maybe you should input a word to resolve blocking.");
				}
				//
				pw.println(serverSocket.getLocalPort());
				RSA rsa = new RSA();
				BigInteger[][] keyC;
				BigInteger[] pubKeyC;
				while(true){	//send client's digital signature
					BigInteger bb1 = BigInteger.probablePrime(128, new Random());
					BigInteger bb2 = BigInteger.probablePrime(128, new Random());
					keyC = rsa.caKey(bb1,bb2);
					pubKeyC = keyC[0];
					dsPri = keyC[1];
					if(keyC[1][1].compareTo(BigInteger.ZERO) != 1){
						System.out.println("d from caculating your p and q is a negative number, please input again.");
						continue;
					}
					break;
				}
				System.out.println(pubKeyC[0]+" "+pubKeyC[1]+" "+dsPri[1]);
				pw.println(String.valueOf(pubKeyC[0]) + " " + String.valueOf(pubKeyC[1]));
				System.out.println("Plaese choose your certificate.Input follow format:[MD5/SHA-1/SHA-2] [0/1/2/3/4]");
				System.out.println("0 is Ceasar,1 is Playfair,2 is RSA,3 is DES,4 is IDEA.");
				while(true){	//send certificate information
					boolean flag = true;
					msg = in.nextLine();
					split = msg.split(" ");
					if(split.length!=2){
						System.out.println("wrong formate.");
						flag = false;
					}
					myCer.setHash(split[0]);
					myCer.setEnAlgorithm(split[1]);
					myCer.setNowTime(new Date());
					myCer.setTime(true);
					if(flag == false){
						continue;
					}else{
						if(myCer.getEnAlgorithm().equals("0")||myCer.getEnAlgorithm().equals("1")||myCer.getEnAlgorithm().equals("3")){
							System.out.println("Please input a [key].");
							String msg2 = in.nextLine();
							myKey = msg2;
							myCer.setKey(msg2);
							pw.println(msg + " " + msg2);
						}else if(myCer.getEnAlgorithm().equals("2")){
							BigInteger[][] skey;
							String st = "";
							while(true){
								BigInteger b1 = BigInteger.probablePrime(128, new Random());
								BigInteger b2 = BigInteger.probablePrime(128, new Random());
								BigInteger[][] key = rsa.caKey(b1,b2);
								BigInteger[] pubKey = key[0];
								BigInteger[] priKey = key[1];
								if(priKey[1].compareTo(BigInteger.ZERO) != 1){
									System.out.println("d from caculating your p and q is a negative number, please input again.");
									continue;
								}
								skey = key;
								st = pubKey[0] + " " + pubKey[1];
								break;
							}
							myCer.setPubKey(skey[0]);
							enPrikey = skey[1]; 
							pw.println(msg + " " + st);
						}else{
							pw.println(msg);
						}
						break;
					}
				}
				System.out.println("Please input format:connect [username] or request or quit.");
				boolean flag = false;
				boolean flag2 = false;
				while(true){	//send the message you input
					msg = in.nextLine();
					split = msg.split(" ");
					if(flag2 == true){
						if(subC.isJudgec() == false){
							if(msg.equals("1")){	//refuse message
								System.out.println("You refuse the message.");
								subC.setJudgec(true);
								continue;
							}else if(msg.equals("0")){	// change your certification
								System.out.println("Digital signature:true");
								if(cer.getEnAlgorithm().equals("0")){
									Ceasar cs = new Ceasar();
									BigInteger cyText = new BigInteger(subC.getRecMsg());
									BigInteger dct = rsa.deRsa(cyText, subC.getDsKey());
									String sta2 = rsa.ascToString(dct);
									System.out.println(cs.deCeasar(sta2,Integer.valueOf(key)));
									System.out.println();
								}else if(cer.getEnAlgorithm().equals("1")){
									Playfair pf = new Playfair();
									BigInteger cyText = new BigInteger(subC.getRecMsg());
									BigInteger dct = rsa.deRsa(cyText, subC.getDsKey());
									String sta2 = rsa.ascToString(dct);
									System.out.println(pf.decrypt(sta2,key));
									System.out.println();
								}else if(cer.getEnAlgorithm().equals("2")){
//									BigInteger[] pubKey = enPubkey;
//								    BigInteger[] priKey = enPrikey;
//								    BigInteger cyText = new BigInteger(subC.getRecMsg());
//								    BigInteger digSig = new BigInteger(subC.getExMsg());
//									BigInteger cT = rsa.deRsa(cyText,priKey);
//									BigInteger dS = rsa.enRsa(digSig,pubKey);
//									String sta2 = rsa.ascToString(cT);
//									System.out.println(sta2 + ", Digital Signature: " +String.valueOf(cT).equals(String.valueOf(dS)) + ".");
								}else if(cer.getEnAlgorithm().equals("3")){
									DES des = new DES();
//									System.out.println(split[2] + " | " + split[3] + " | " + split[4]);
									String[] temp = subC.getRecMsg().split(",");
									int[] temp2 = new int[temp.length];
									byte[] temp3 = new byte[temp.length];
									for(int i=0;i<temp.length;i++){
										temp2[i] = Integer.valueOf(temp[i]);
										temp3[i] = (byte)temp2[i];
									}
							        des.generateKeys(key);
									byte[] p=des.split(temp3,0,Integer.valueOf(subC.getExMsg()));
							        byte[] p_d=new byte[p.length];
							        System.arraycopy(p,0,p_d,0,p.length);
							        System.out.println("明文: "+new String(p));
									System.out.println(new String(p));
								}else if(cer.getEnAlgorithm().equals("4")){
									IDEA idea = new IDEA();
									String[] temp = subC.getRecMsg().split(",");
									int[] temp2 = new int[temp.length];
									byte[] temp3 = new byte[temp.length];
									for(int i=0;i<temp.length;i++){
										temp2[i] = Integer.valueOf(temp[i]);
										temp3[i] = (byte)temp2[i];
									}
									byte[] key = { 19, 88, 75, 62, 53, 41, 33, 22, 41, 30, 14, 22, 3, 43, 51, 26 };
									char[] z = new char[53];	//子密钥
									z = idea.Creat_encrypt_sub_k(z,key);
									char[] z1 = new char[53];
									z1 = idea.Creat_decrypt_sub_k(z,z1);
									char temp4,X1,X2,X3,X4;
									X1 = (char)temp3[0];
									X1 <<= 8;
									temp4 = (char) temp3[1];
									temp4 &= 0xFF;
									X1 |= temp4;
									X2 = (char) temp3[2];
									X2 <<= 8;
									temp4 = (char) temp3[3];
									temp4 &= 0xFF;
									X2 |= temp4;
									X3 = (char) temp3[4];
									X3 <<= 8;
									temp4 = (char) temp3[5];
									temp4 &= 0xFF;
									X3 |= temp4;
									X4 = (char) temp3[6];
									X4 <<= 8;
									temp4 = (char) temp3[7];
									temp4 &= 0xFF;
									X4 |= temp4;
									temp3 = idea.Decrypt(X1,X2,X3,X4,z1,temp3);
//									System.out.print(split[0] + " send to you: ");
									for(int i=0;i<temp3.length;i++){
										System.out.print((char)temp3[i]);
									}
									System.out.println();
								}
								myCer.setHash(cer.getHash());
								myCer.setEnAlgorithm(cer.getEnAlgorithm());
//								cer = new Certificate(split[0],split[1],new Date(),true);
								if(myCer.getEnAlgorithm().equals("0")||myCer.getEnAlgorithm().equals("1")||myCer.getEnAlgorithm().equals("3")){
									myKey = subC.getKey();
									myCer.setKey(myKey);
									String st = "change";
									pw.println(st);
								}else if(myCer.getEnAlgorithm().equals("2")){
									BigInteger[][] skey;
									String st = "";
									while(true){
										BigInteger b1 = BigInteger.probablePrime(128, new Random());
										BigInteger b2 = BigInteger.probablePrime(128, new Random());
										BigInteger[][] key = rsa.caKey(b1,b2);
										BigInteger[] pubKey = key[0];
										BigInteger[] priKey = key[1];
										if(priKey[1].compareTo(BigInteger.ZERO) != 1){
											System.out.println("d from caculating your p and q is a negative number, please input again.");
											continue;
										}
										skey = key;
										st = "change " + pubKey[0] + " " + pubKey[1];
										pw.println(st);
										break;
									}
									myCer.setPubKey(skey[0]);
									subC.setPrivateKey(skey[1]);
									enPrikey = skey[1]; 
								}else{
									String st = "change";
									pw.println(st);
								}
								myCer.setNowTime(new Date());
								myCer.setTime(true);
								subC.setJudgec(true);
								System.out.println("Change successful!");
							}else{
								System.out.println("Please input [0] or [1]");
							}
							continue;
						}
						if(split[0].equals("send")){	//send file
							File file = new File(split[1]);
							FileHash fh = new FileHash();
							String hc = fh.getFileMD5(file);
							System.out.println(hc);
							BigInteger[] priKey = dsPri;
							BigInteger cT = rsa.enRsa(new BigInteger(rsa.stringToAsc(hc)),priKey);
							System.out.println(rsa.stringToAsc(hc));
							msg = split[0] + " "  + split[1] + " " + cT;
							pw.println(msg);
							DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
							FileInputStream fis = new FileInputStream(file);
							byte[] sendBytes = new byte[1024];
							int length = 0;
							long size = file.length();
							long size2 = 0;
				            while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
				            	size2 += (long)length; 
				            	dos.write(sendBytes, 0, length);
				            	dos.flush();
				            	if(size2 == size){
				            		break;
				            	}
				            }
				            System.out.println("Send success.");
				            continue;
						}
						
						//encrypte and send message
						if(myCer.getEnAlgorithm().equals("0")){
							Ceasar cs = new Ceasar();
							cyperText = cs.enCeasar(msg,Integer.valueOf(myKey));
							pw.println(rsa.enRsa(new BigInteger(rsa.stringToAsc(cyperText)), dsPri)+"$"+cer.getEnAlgorithm()+"$"+cyperText);
							System.out.println("The cyperText is:" + cyperText);
						}else if(myCer.getEnAlgorithm().equals("1")){
							Playfair pf = new Playfair();
							cyperText = pf.encrypt(msg,myKey);
							pw.println(rsa.enRsa(new BigInteger(rsa.stringToAsc(cyperText)), dsPri)+"$"+cer.getEnAlgorithm()+"$"+cyperText);
							System.out.println("The cyperText is:" + cyperText);
						}else if(myCer.getEnAlgorithm().equals("3")){
							DES des = new DES();
							System.out.println("plaintext: "+msg);
					        des.generateKeys(myKey);
					        byte[] c=des.split(msg.getBytes(),1,msg.getBytes().length);
					        int[] a = new int[c.length];
					        for(int i=0;i<c.length;i++){
					        	a[i] = (int)c[i];
					        }
					        String post = "";
					        for(int i=0;i<a.length;i++){
					        	if(i!=a.length-1)
					        		post=post + String.valueOf(a[i]) + ",";
					        	else
					        		post=post + a[i];
					        }
					        System.out.println("cypertext: "+new String(c));
					        System.out.println(post);
					        pw.println(post+"$"+cer.getEnAlgorithm()+"$"+msg.getBytes().length+"$"+new String(c));
						}else if(myCer.getEnAlgorithm().equals("4")){
							IDEA idea = new IDEA();
							byte[] plain2 = msg.getBytes();
							if(plain2.length!=8){
								System.out.println("The palin must be 64 bit!");
								continue;
							}
//							byte[] plain2 = {119, 22, 33, 49, 115, 62, 73, 81};
							byte[] key = { 19, 88, 75, 62, 53, 41, 33, 22, 41, 30, 14, 22, 3, 43, 51, 26 };
							byte[] cyper = new byte[plain2.length];
							byte[] tempKey = new byte[key.length];
							for(int i=0;i<key.length;i++){
								tempKey[i] = key[i];
							}
							char[] z = new char[53];	//子密钥
							z = idea.Creat_encrypt_sub_k(z,tempKey);
							char temp,X1,X2,X3,X4;
							X1 = (char)plain2[0];
							X1 <<= 8;
							temp = (char) plain2[1];
							temp &= 0xFF;
							X1 |= temp;
							X2 = (char) plain2[2];
							X2 <<= 8;
							temp = (char) plain2[3];
							temp &= 0xFF;
							X2 |= temp;
							X3 = (char) plain2[4];
							X3 <<= 8;
							temp = (char) plain2[5];
							temp &= 0xFF;
							X3 |= temp;
							X4 = (char) plain2[6];
							X4 <<= 8;
							temp = (char) plain2[7];
							temp &= 0xFF;
							X4 |= temp;
							cyper = idea.Encrypt(X1,X2,X3,X4,z,cyper);
							for(int i=0;i<cyper.length;i++)
								System.out.print(cyper[i] + " ");
							int[] a = new int[cyper.length];
					        for(int i=0;i<cyper.length;i++){
					        	a[i] = (int)cyper[i];
					        }
					        String post = "";
					        for(int i=0;i<a.length;i++){
					        	post=post + String.valueOf(a[i]) + ",";
					        }
					        pw.println(post+"$"+cer.getEnAlgorithm()+"$"+new String(cyper));
						}else if(myCer.getEnAlgorithm().equals("2")){
							BigInteger cT = null;
							BigInteger dS = null;
							BigInteger[] pubKey = subC.getPublicKey();
							if(cer.getEnAlgorithm().equals("2")==false){
								System.out.println("Your partner is not RSA user.Wait for change and then send again.");
								pw.println(" "+"$"+cer.getEnAlgorithm()+"$"+" ");
								continue;
							}
							BigInteger[] priKey = enPrikey;
							if(priKey[1].compareTo(BigInteger.ZERO) != 1){
								System.out.println("d from caculating your p and q is a negative number, please input again.");
								continue;
							}
						    BigInteger plainText = new BigInteger(rsa.stringToAsc(msg));
						    if(plainText.compareTo(pubKey[0]) != -1){
								System.out.println("e from caculating your p and q isn't less than n, please input again.");
								continue;
							}
							cT = rsa.enRsa(plainText,pubKey);
							dS = rsa.deRsa(plainText,priKey);
//							BigInteger cT2;
//							cT2 = plainText.modPow(pubKey[1], pubKey[0]);
//							System.out.println(cT2);
							pw.println(cT+"$"+cer.getEnAlgorithm()+"$"+dS);
							System.out.println("The cyperText is:" + cT);
							System.out.println("The encripte DS is:" + dS);
						}
					}else{
						pw.println(msg); //Sent to the server
						//It is here because it is asynchronous. If it is placed at the end or at the beginning, one socket connection is updated and the other is not updated.
						//so you need to enter the confirmation message and prove that both parties have been updated before you can enter
						if(flag == true){	//Because I want to block the verification information once, prevent the next loop from over-capturing my child thread, the child thread is replaced and I can't receive the newly opened port of the user sent by the server.
							//change socket to a new TCP connection
							bw = new BufferedWriter(new OutputStreamWriter(	
									subC.getSocket2().getOutputStream()));
							pw = new PrintWriter(bw, true);
							socket = subC.getSocket2();
							cer = subC.getCer();
							key = subC.getKey();
							subC = new cHandler(subC.getSocket2(),serverSocket,subC.getPublicKey(),enPrikey,cer,key,subC.getDsKey(),myCer);
							subC.setConnect2(true);
							enPubkey = subC.getPublicKey();
							executorService.execute(subC);
							flag = false;
							flag2 = true;
							System.out.println("Connect success!");
						}
						if(split[0].equals("connect")){
							flag = true;
							System.out.println("Please input 'sure' to assure the connection.");
						}else if(split[0].equals("hear")){
							flag = true;
							System.out.println("Please input 'sure' to assure the connection.");
						}else if(msg.equals("quit")){
							System.exit(0);
						}
					}
				}
			}finally {
				if (null != socket) {
					try {
						socket.close(); //disconnect,close the socket
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
//		}
	}
}
