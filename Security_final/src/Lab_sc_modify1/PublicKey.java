/**
 * 
 */
package Lab_sc_modify1;

import java.math.BigInteger;

/**
 * @author hou
 *
 */
public class PublicKey {
	private String user;
	private BigInteger[] pubKey;
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
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
	 * @param user
	 * @param pubKey
	 */
	public PublicKey(String user, BigInteger[] pubKey) {
		super();
		this.user = user;
		this.pubKey = pubKey;
	}
	
}
