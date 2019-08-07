package Lab_sc_modify1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cHandler implements Runnable {
	private Socket socket;
	private ServerSocket serverSocket;
	BufferedReader br;
	BufferedWriter bw;
	PrintWriter pw;
	private BigInteger[] publicKey;
	private BigInteger[] privateKey;
	private String key;
//	private String msg;
	static int k=0;//Judging what is the time to enter ciphertext(like first,second...)
	private Socket socket2;
    private boolean value = false;
    private boolean connect2 = false;
    private boolean judgec = true;
    private String transAlg;
    private Certificate cer;
    private String recMsg;
    private String exMsg;
    private BigInteger[] dsKey;
    private Certificate myCer;

	public cHandler(Socket socket, ServerSocket serverSocket, BigInteger[] publicKey, BigInteger[] privateKey, Certificate cer, String key, BigInteger[] dsKey, Certificate myCer) {
		this.socket = socket;
		this.serverSocket = serverSocket;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.cer = cer;
		this.key = key;
		this.dsKey = dsKey;
		this.myCer = myCer;
	}

	public void initStream() throws IOException { // Initialize the input and output stream object method
		bw = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		br = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		pw = new PrintWriter(bw, true);
	}
	
	

	/**
	 * @return the exMsg
	 */
	public String getExMsg() {
		return exMsg;
	}

	/**
	 * @param exMsg the exMsg to set
	 */
	public void setExMsg(String exMsg) {
		this.exMsg = exMsg;
	}

	/**
	 * @return the recMsg
	 */
	public String getRecMsg() {
		return recMsg;
	}

	/**
	 * @param recMsg the recMsg to set
	 */
	public void setRecMsg(String recMsg) {
		this.recMsg = recMsg;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the socket2
	 */
	public Socket getSocket2() {
		return socket2;
	}

	/**
	 * @param socket2 the socket2 to set
	 */
	public void setSocket2(Socket socket2) {
		this.socket2 = socket2;
	}
	
	public boolean isValue(){
		return value;
	}
	
	

	/**
	 * @return the connect2
	 */
	public boolean isConnect2() {
		return connect2;
	}

	/**
	 * @param connect2 the connect2 to set
	 */
	public void setConnect2(boolean connect2) {
		this.connect2 = connect2;
	}

	/**
	 * @return the cer
	 */
	public Certificate getCer() {
		return cer;
	}

	/**
	 * @param cer the cer to set
	 */
	public void setCer(Certificate cer) {
		this.cer = cer;
	}

	
	
	/**
	 * @return the judgec
	 */
	public boolean isJudgec() {
		return judgec;
	}

	/**
	 * @param judgec the judgec to set
	 */
	public void setJudgec(boolean judgec) {
		this.judgec = judgec;
	}
	
	

	/**
	 * @return the transAlg
	 */
	public String getTransAlg() {
		return transAlg;
	}

	/**
	 * @param transAlg the transAlg to set
	 */
	public void setTransAlg(String transAlg) {
		this.transAlg = transAlg;
	}
	
	

	/**
	 * @return the dsKey
	 */
	public BigInteger[] getDsKey() {
		return dsKey;
	}

	/**
	 * @param dsKey the dsKey to set
	 */
	public void setDsKey(BigInteger[] dsKey) {
		this.dsKey = dsKey;
	}
	
	

	/**
	 * @return the privateKey
	 */
	public BigInteger[] getPrivateKey() {
		return privateKey;
	}

	/**
	 * @param privateKey the privateKey to set
	 */
	public void setPrivateKey(BigInteger[] privateKey) {
		this.privateKey = privateKey;
	}

	public void run() { // Executed content
		try {
			initStream();
			String cyperText = null;
			BigInteger cT;
			BigInteger dS;
			String[] split = new String[6];
			while (true) {
				cyperText = br.readLine();
				split = cyperText.split(" ");
//				System.out.println("1"+cyperText);
				if(connect2 == false){
					if(split[0].equals("sender")){
						cer.setHash(split[2]);
						cer.setEnAlgorithm(split[3]);
						cer.setNowTime(new Date());
						cer.setTime(true);
						this.setCer(cer);
//						System.out.println(serverSocket.getLocalPort());
						Socket socket2 = new Socket();
//						System.out.println(Integer.valueOf(split[1]));
						socket2.connect(new InetSocketAddress("127.0.0.1",Integer.valueOf(split[1]) ));
						OutputStream os = socket2.getOutputStream();
//						System.out.println(socket2.getInetAddress()+" "+socket2.getLocalAddress().getHostAddress()+" "+socket2.getPort()+" "+socket2.getLocalPort());
						os.write("Please input 'hear' to complete connection.".getBytes());
						this.setSocket2(socket2);
						if(split[3].equals("2")){
							BigInteger[] temp = {new BigInteger(split[4]),new BigInteger(split[5])};
							this.setPublicKey(temp);
							BigInteger[] tempB = {new BigInteger(split[6]),new BigInteger(split[7])};
							this.setDsKey(tempB);
						}else if(split[3].equals("0")||split[3].equals("1")||split[3].equals("3")){
							this.setKey(split[4]);
							BigInteger[] tempB = {new BigInteger(split[5]),new BigInteger(split[6])};
							this.setDsKey(tempB);
						}else{
							BigInteger[] tempB = {new BigInteger(split[4]),new BigInteger(split[5])};
							this.setDsKey(tempB);
						}
						connect2 = true;
						
					}else if(split[0].equals("reciever")){
						Socket socket2 = new Socket();
						System.out.println(serverSocket.getLocalPort());
						socket2 = serverSocket.accept();
						InputStream is = socket2.getInputStream();
						byte[] buf = new byte[1000];
						int length = is.read(buf);
						System.out.println(new String(buf, 0, length));
						this.setSocket2(socket2);
						if(split[3].equals("2")){
							BigInteger[] temp = {new BigInteger(split[4]),new BigInteger(split[5])};
							this.setPublicKey(temp);
							BigInteger[] tempB = {new BigInteger(split[6]),new BigInteger(split[7])};
							this.setDsKey(tempB);
						}else if(split[3].equals("0")||split[3].equals("1")||split[3].equals("3")){
							this.setKey(split[4]);
							BigInteger[] tempB = {new BigInteger(split[5]),new BigInteger(split[6])};
							this.setDsKey(tempB);
						}else{
							BigInteger[] tempB = {new BigInteger(split[4]),new BigInteger(split[5])};
							this.setDsKey(tempB);
						}
						connect2 = true;
						
						cer.setHash(split[2]);
						cer.setEnAlgorithm(split[3]);
						cer.setNowTime(new Date());
						cer.setTime(true);
						this.setCer(cer);
					}else if(split[0].equals("login")){
						value = true;
					}else if(split[0].equals("register")){
						value = true;
					}else if(split[0].equals("request")){
						System.out.println(split[1]);
					}
				}else{
					//receive file
					if(split[0].equals("send")&&split.length==3){
						System.out.println("Recieve a file.");
						String[] split2 = split[1].split("\\.");
						if(split2.length==2){
							split2[0] = split2[0] + "33";
							split[1] = split2[0] + "." +split2[1];
						}else{
							System.out.println("Send file wrong.");
							continue;
						}
						File file = new File(split[1]);
						DataInputStream dis = new DataInputStream(socket.getInputStream());
						FileOutputStream fos = new FileOutputStream(file);
						byte[] inputByte = new byte[1024];
						int length = 0;
						while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
		                    fos.write(inputByte, 0, length);
		                    fos.flush();
		                    if(length<1024){
		                    	break;
		                    }
		                }
						FileHash fh = new FileHash();
						String hc = fh.getFileMD5(file);
						RSA rsa = new RSA();
						BigInteger[] pubKey = dsKey;
					    BigInteger cyText = new BigInteger(split[2]);
						cT = rsa.deRsa(cyText,pubKey);
						String hc2 = rsa.stringToAsc(hc);
						if(hc2.equals(String.valueOf(cT))){
							System.out.println("Hash certification:true");
						}else{
							System.out.println("Hash certification:false");
						}
						System.out.println("完成接收");
						continue;
					}
					//judge if need to change certification
					if(split[0].equals("change")||cyperText.equals("change")){
						cer.setHash(myCer.getHash());
						cer.setEnAlgorithm(myCer.getEnAlgorithm());
						if(cer.getEnAlgorithm().equals("0")||cer.getEnAlgorithm().equals("1")||cer.getEnAlgorithm().equals("3")){
							key = myCer.getKey();
							cer.setKey(key);
						}else if(cer.getEnAlgorithm().equals("2")){
							BigInteger[] changePub = {new BigInteger(split[1]),new BigInteger(split[2])};
							publicKey = changePub;
							cer.setPubKey(publicKey);
						}
						cer.setNowTime(new Date());
						cer.setTime(true);
						System.out.println("Change successful!");
						continue;
					}
					String[] newSplit = cyperText.split("\\$");
					if(newSplit.length>1){
//						System.out.println(newSplit[1]+" "+cer.getEnAlgorithm());
						//judge if the algorithm is different
						if(newSplit[1].equals(cer.getEnAlgorithm()) == false){
							this.setJudgec(false);
							this.setTransAlg(newSplit[1]);
							this.setRecMsg(newSplit[0]);
							if(newSplit.length>2)
								this.setExMsg(newSplit[2]);
							System.out.println("Your algorithm is different.Please choose [0] or [1].");
							System.out.println("[0] is change your certificate's algorithm.[1] is refuse the message.");					
							continue;
						}
					}
					RSA rsa = new RSA();
					//receive encrypted message and decrypte it
					if(cer.getEnAlgorithm().equals("0")){
						Ceasar cs = new Ceasar();
						BigInteger cyText = new BigInteger(newSplit[0]);
						BigInteger dct = rsa.deRsa(cyText, dsKey);
						String sta2 = rsa.ascToString(dct);
						if(sta2.equals(newSplit[2])){
							System.out.println("Digital signature:true.");
						}
						System.out.println(cs.deCeasar(sta2,Integer.valueOf(key))); //输出服务器返回的消息
						System.out.println();
					}else if(cer.getEnAlgorithm().equals("1")){
						Playfair pf = new Playfair();
						BigInteger cyText = new BigInteger(newSplit[0]);
						BigInteger dct = rsa.deRsa(cyText, dsKey);
						String sta2 = rsa.ascToString(dct);
						if(sta2.equals(newSplit[2])){
							System.out.println("Digital signature:true.");
						}
						System.out.println(pf.decrypt(sta2,key)); //输出服务器返回的消息
						System.out.println();
					}else if(cer.getEnAlgorithm().equals("2")){
						BigInteger[] pubKey = publicKey;
					    BigInteger[] priKey = privateKey;
					    BigInteger cyText = new BigInteger(newSplit[0]);
					    BigInteger digSig = new BigInteger(newSplit[2]);
						cT = rsa.deRsa(cyText,priKey);
						dS = rsa.enRsa(digSig,pubKey);
						String sta2 = rsa.ascToString(cT);
						System.out.println(sta2 + ", Digital Signature: " +String.valueOf(cT).equals(String.valueOf(dS)) + ".");
					}else if(cer.getEnAlgorithm().equals("3")){
						DES des = new DES();
						String[] temp = newSplit[0].split(",");
						int[] temp2 = new int[temp.length];
						byte[] temp3 = new byte[temp.length];
						for(int i=0;i<temp.length;i++){
							temp2[i] = Integer.valueOf(temp[i]);
							temp3[i] = (byte)temp2[i];
						}
						if(new String(temp3).equals(newSplit[3])){
							System.out.println("Digital signature:true.");
						}
				        des.generateKeys(key);
						byte[] p=des.split(temp3,0,Integer.valueOf(newSplit[2]));
				        byte[] p_d=new byte[p.length];
				        System.arraycopy(p,0,p_d,0,p.length);
						System.out.println(new String(p));
					}else if(cer.getEnAlgorithm().equals("4")){
						IDEA idea = new IDEA();
						String[] temp = newSplit[0].split(",");
						int[] temp2 = new int[temp.length];
						byte[] temp3 = new byte[temp.length];
						for(int i=0;i<temp.length;i++){
							temp2[i] = Integer.valueOf(temp[i]);
							temp3[i] = (byte)temp2[i];
						}
						if(new String(temp3).equals(newSplit[2])){
							System.out.println("Digital signature:true.");
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
//						System.out.print(split[0] + " send to you: ");
						for(int i=0;i<temp3.length;i++){
							System.out.print((char)temp3[i]);
						}
						System.out.println();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * @return the publicKey
	 */
	public BigInteger[] getPublicKey() {
		return publicKey;
	}

	/**
	 * @param publicKey the publicKey to set
	 */
	public void setPublicKey(BigInteger[] publicKey) {
		this.publicKey = publicKey;
	}


}
