/**
 * 
 */
package Lab_sc_modify1;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;

/**
 * @author hou
 *
 */
public class Certificate {
	private String key;
	private BigInteger[] pubKey;
	private String hash;
	private String enAlgorithm;
	private boolean time;
	private Date nowTime;
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
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}
	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}
	/**
	 * @return the enAlgorithm
	 */
	public String getEnAlgorithm() {
		return enAlgorithm;
	}
	/**
	 * @param enAlgorithm the enAlgorithm to set
	 */
	public void setEnAlgorithm(String enAlgorithm) {
		this.enAlgorithm = enAlgorithm;
	}
	/**
	 * @return the time
	 */
	public boolean isTime() {
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(boolean time) {
		this.time = time;
	}
	
	/**
	 * @return the nowTime
	 */
	public Date getNowTime() {
		return nowTime;
	}
	/**
	 * @param nowTime the nowTime to set
	 */
	public void setNowTime(Date nowTime) {
		this.nowTime = nowTime;
	}

	
	public void timeOut(Date myTime){
		long nh = 1000*60*60;
		long nd = 1000*24*60*60;
		long diff = myTime.getTime() - nowTime.getTime();
		long hour = diff%nd/nh;
		if(hour > 1){
			this.setTime(false);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Certificate [key=" + key + ", pubKey=" + Arrays.toString(pubKey) + ", hash=" + hash + ", enAlgorithm="
				+ enAlgorithm + ", time=" + time + ", nowTime=" + nowTime + "]";
	}
	
	/**
	 * 
	 */
	public Certificate() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param hash
	 * @param enAlgorithm
	 * @param nowTime
	 * @param time
	 */
	public Certificate(String hash, String enAlgorithm) {
		super();
		this.hash = hash;
		this.enAlgorithm = enAlgorithm;
	}
	
}
