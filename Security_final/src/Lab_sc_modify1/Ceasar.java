/**
 * 
 */
package Lab_sc_modify1;

/**
 * @author hou
 * Ceasar
 */
public class Ceasar {
	/*
	 * @author hou
	 * Ceasar encryption
     * @param text
     * @param key
     * @return encrption text
     */
	public String enCeasar(String text,int key){
		text = text.toUpperCase();
		char[] ntext = text.toCharArray();
		for(int i=0;i<ntext.length;i++){
			int temp = 0;
			temp = (int)ntext[i] + key;
			if(temp > 90){
				temp = temp - 26;
			}else if(temp < 65){
				temp = temp + 26;
			}
			ntext[i] = (char)temp;
		}
		text = new String(ntext);
		return text;
	}
	
	/*
	 * @author hou
	 * Ceasar decryption
     * @param text
     * @param key
     * @return decrption text
     */
	public String deCeasar(String text,int key){
		text = text.toUpperCase();
		char[] ntext = text.toCharArray();
		for(int i=0;i<ntext.length;i++){
			int temp = 0;
			temp = (int)ntext[i] - key;
			if(temp < 65){
				temp = temp + 26;
			}else if(temp > 90){
				temp = temp - 26;
			}
			ntext[i] = (char)temp;
		}
		text = new String(ntext);
		return text;
	}
}
