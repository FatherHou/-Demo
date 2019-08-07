/**
 * 
 */
package Lab_sc_modify1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;

/**
 * @author hou
 *
 */
public class User {
	private String name;
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;
	private PrintWriter pw;
	private BigInteger[] pubKey;
	private BigInteger[] priKey;
	private int port;
	private int anoPort;
	private boolean lock = false;
	private String password;
	private Certificate cer;
	private BigInteger[] dsKey;
	
	
	
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
	 * @return the lock
	 */
	public boolean isLock() {
		return lock;
	}
	/**
	 * @param lock the lock to set
	 */
	public void setLock(boolean lock) {
		this.lock = lock;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the anoPort
	 */
	public int getAnoPort() {
		return anoPort;
	}
	/**
	 * @param anoPort the anoPort to set
	 */
	public void setAnoPort(int anoPort) {
		this.anoPort = anoPort;
	}
	/**
	 * @return the pubKey
	 */
	public BigInteger[] getPubKey() {
		return pubKey;
	}
	/**
	 * @param pubKey the pubKey to set
	 */
	public void setPubKey(BigInteger[] pubKey) {
		this.pubKey = pubKey;
	}
	/**
	 * @return the priKey
	 */
	public BigInteger[] getPriKey() {
		return priKey;
	}
	/**
	 * @param priKey the priKey to set
	 */
	public void setPriKey(BigInteger[] priKey) {
		this.priKey = priKey;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}
	/**
	 * @param socket the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	/**
	 * @return the br
	 */
	public BufferedReader getBr() {
		return br;
	}
	/**
	 * @param br the br to set
	 */
	public void setBr(BufferedReader br) {
		this.br = br;
	}
	/**
	 * @return the bw
	 */
	public BufferedWriter getBw() {
		return bw;
	}
	/**
	 * @param bw the bw to set
	 */
	public void setBw(BufferedWriter bw) {
		this.bw = bw;
	}
	/**
	 * @return the pw
	 */
	public PrintWriter getPw() {
		return pw;
	}
	/**
	 * @param pw the pw to set
	 */
	public void setPw(PrintWriter pw) {
		this.pw = pw;
	}
	/**
	 * @param name
	 * @param socket
	 * @param br
	 * @param bw
	 * @param pw
	 * @throws IOException 
	 */
	public User(String name, Socket socket) throws IOException {
		super();
		this.name = name;
		this.socket = socket;
		this.br = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		this.bw = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));;
		this.pw = new PrintWriter(bw, true);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [name=" + name + "]";
	}
	
}
