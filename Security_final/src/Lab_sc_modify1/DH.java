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
 *
 */
public class DH {
	
	/*
	 * @author hou
	 * Mice with the power of an integer, like (x^e) mod n
     * @param base
     * @param mi
     * @param n
     * @return (base^e)mod n
     */
	public static BigInteger expMode(BigInteger base, BigInteger mi, BigInteger n){
		char[] array = new StringBuilder(mi.toString(2)).reverse().toString().toCharArray();//二进制反转进行最后乘积
		List<BigInteger> baseArray = new ArrayList<BigInteger>() ;
		BigInteger preBase = base ;
		baseArray.add(preBase);
		for(int i=0;i<array.length-1;i ++){
			BigInteger nextBase = preBase.multiply(preBase).mod(n) ;
			baseArray.add(nextBase) ;
	        preBase = nextBase ;
		}
		BigInteger a_w_b = multi(baseArray.toArray(new BigInteger[baseArray.size()]), array, n) ;
		return a_w_b.mod(n) ;	//2321 mod 2537 = 2321
	}
	
	/*
	 * @author hou
	 * Find the power, like (x^e)
     * @param array
     * @param bin_array
     * @param n
     * @return (array^n)
     */
	public static BigInteger multi(BigInteger[] array, char[] bin_array, BigInteger n){
	    BigInteger result = BigInteger.ONE ;
	    for(int index = 0 ; index < array.length ; index ++){
	        BigInteger a = array[index] ;
	        if(bin_array[index] == '0'){
	            continue ;
	        }
	        result = result.multiply(a) ;	//(−431)×(−988)×(−601)≡2321(mod2537)  
	        result = result.mod(n) ;	//上式等于(((-432)mod2537 x (-988))mod2537 x(-601)mod2537 = 2321 )
	    }
	    return result ;	//2321
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BigInteger p,q;
		BigInteger g;
		do{
			q = BigInteger.probablePrime(50, new Random());
			p = q.multiply(BigInteger.ONE.add(BigInteger.ONE)).add(BigInteger.ONE);
		}while(p.isProbablePrime(128));
		System.out.println("p: "+p);
		System.out.println(q);
		do{
			g= BigInteger.probablePrime(50, new Random());
		}while(g.compareTo(p.subtract(BigInteger.ONE))!=-1||expMode(g,BigInteger.ONE.add(BigInteger.ONE),p)==BigInteger.ONE||expMode(g,BigInteger.ONE.add(BigInteger.ONE),q)==BigInteger.ONE);
		System.out.println("Original root: "+g);
		
		BigInteger a = BigInteger.probablePrime(50, new Random());
		System.out.println("a: "+a);
		BigInteger b = BigInteger.probablePrime(50, new Random());
		System.out.println("b: "+b);
		BigInteger pubA = expMode(g,a,p);
		System.out.println("pubA: "+pubA);
		BigInteger pubB = expMode(g,b,p);
		System.out.println("pubB: "+pubB);
		BigInteger KA = expMode(pubB,a,p);
		System.out.println("KA: "+KA);
		BigInteger KB = expMode(pubA,b,p);
		System.out.println("KB: "+KB);
	}

}
