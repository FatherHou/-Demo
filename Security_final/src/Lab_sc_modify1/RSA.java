/**
 * 
 */
package Lab_sc_modify1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author hou
 * RSA
 */
public class RSA {
	
	/*
	 * @author hou
	 * translate string to ASC
     * @param in
     * @return out of ASC
     */
	public String ascToString(BigInteger cT){
		char[] sta = String.valueOf(cT).toCharArray();
		String sta2 = "";
		for(int i=0;i<String.valueOf(cT).length();){
			if(sta[i]=='9'||sta[i]=='6'||sta[i]=='7'||sta[i]=='8'){
				String temp2 = "";
				temp2 += sta[i];
				temp2 += sta[i+1];
				int ti = Integer.valueOf(temp2);
				sta2 = sta2 + (char)ti;
				i += 2;
			}else if(sta[i]=='1'){
				String temp2 = "";
				temp2 += sta[i];
				temp2 += sta[i+1];
				temp2 += sta[i+2];
				int ti = Integer.valueOf(temp2);
				sta2 = sta2 + (char)ti;
				i += 3;
			}
		}
		return sta2;
	}
	
	/*
	 * @author hou
	 * translate string to ASC
     * @param in
     * @return out of ASC
     */
	public String stringToAsc(String in){
		StringBuffer sb = new StringBuffer();
		char[] ca = in.toCharArray();
		for(int i=0;i<ca.length;i++){
			sb.append((int)ca[i]);
		}
		return sb.toString();
	}
	
	/*
	 * @author hou
	 * gcd implement
     * @param a
     * @param b
     * @return Maximum common divisor
     */
	public BigInteger gcd(BigInteger a,BigInteger b){
		if(b.equals(BigInteger.ZERO)){
			return a;
		}else{
			return gcd(b,a.mod(b));
		}
	}
	
	/*
	 * @author hou
	 * find the integer solution of x and y in ax + by = 1.
     * @param a
     * @param b
     * @return x, y and r
     */
	public BigInteger[] exGcd(BigInteger a,BigInteger b){
		BigInteger x1,y1,x,y,r;
		if(b.equals(BigInteger.ZERO)){
			x1 = BigInteger.ONE;
			y1 = BigInteger.ZERO;
			x = x1;
			y = y1;
			r = a;
			BigInteger[] temp = {r,x,y};
			return temp;
		}else{
			BigInteger[] temp = exGcd(b,a.mod(b));
			r = temp[0];
			x1 = temp[1];
			y1 = temp[2];
			x = y1;
			y = x1.subtract(a.divide(b).multiply(y1));
			BigInteger[] temp2 = {r,x,y};
			return temp2;
		}
	}
	
	/*
	 * @author hou
	 * Mice with the power of an integer, like (x^e) mod n
     * @param base
     * @param mi
     * @param n
     * @return (base^e)mod n
     */
	public BigInteger expMode(BigInteger base, BigInteger mi, BigInteger n){
		char[] array = new StringBuilder(mi.toString(2)).reverse().toString().toCharArray();//二进制反转进行最后乘积
		List<BigInteger> baseArray = new ArrayList<BigInteger>() ;
		BigInteger preBase = base ;
		baseArray.add(preBase);
		for(int i=0;i<array.length-1;i ++){
			BigInteger nextBase = preBase.multiply(preBase).mod(n) ;
			baseArray.add(nextBase) ;
	        preBase = nextBase ;
		}
		BigInteger a_w_b = this.multi(baseArray.toArray(new BigInteger[baseArray.size()]), array, n) ;
		return a_w_b.mod(n) ;
	}
	
	/*
	 * @author hou
	 * Find the power, like (x^e)
     * @param array
     * @param bin_array
     * @param n
     * @return (array^n)
     */
	public BigInteger multi(BigInteger[] array, char[] bin_array, BigInteger n){
	    BigInteger result = BigInteger.ONE ;
	    for(int index = 0 ; index < array.length ; index ++){
	        BigInteger a = array[index] ;
	        if(bin_array[index] == '0'){
	            continue ;
	        }
	        result = result.multiply(a) ;
	        result = result.mod(n) ;
	    }
	    return result ;
	}
	
	/*
	 * @author hou
	 * Calculate a two-dimensional array containing the private and public keys
     * @param p
     * @param q
     * @return Public key and private key
     */
	public BigInteger[][] caKey(BigInteger p,BigInteger q){
		BigInteger n = p.multiply(q);
		BigInteger f = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		Random ra = new Random();
		BigInteger e = BigInteger.probablePrime(3,ra);
		int count = 3;
		while(f.remainder(e).equals(BigInteger.ZERO)){
			e = BigInteger.probablePrime(count,ra);
			count++;
		}
//		BigInteger e = r.nextInt(f-3)+2;
//		while(f%e == 0){
//			e = r.nextInt(f-3)+2;
//		}
//		BigInteger e = new BigInteger("3889");
		BigInteger[] rxy = exGcd(e,f);
		BigInteger r = rxy[0];
		BigInteger x = rxy[1];
		BigInteger y = rxy[2];
		BigInteger d = x;
		return new BigInteger[][]{{n , e}, {n , d}};
	}
	
	/*
	 * @author hou
	 * RSA encryption
     * @param plain
     * @param pubKey
     * @return cyper
     */
	public BigInteger enRsa(BigInteger plain,BigInteger[] pubKey){
		BigInteger n = pubKey[0];
		BigInteger e = pubKey[1];
		BigInteger cyper = expMode(plain,e,n);
		return cyper;
	}
	
	/*
	 * @author hou
	 * RSA decryption
     * @param cyper
     * @param priKey
     * @return plain
     */
	public BigInteger deRsa(BigInteger cyper,BigInteger[] priKey){
		BigInteger n = priKey[0];
		BigInteger d = priKey[1];
		BigInteger plain = expMode(cyper,d,n);
		return plain;
	}
}
