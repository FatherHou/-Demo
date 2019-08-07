/**
 * 
 */
package Lab_sc_modify1;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hou
 *
 */
public class Playfair {
	/**
     * Process plaintext, repeat letter middle insert X, length not even add X
     * @param Plain
     * @return String
     * @author hou
     */
    public static String dealP(String Plain){
        Plain=Plain.toUpperCase();//Convert plaintext to uppercase
        Plain=Plain.replaceAll("[^A-Z]", "");//Remove all non-alphabetic characters 
        StringBuilder stringB=new StringBuilder(Plain);     
//        for(int i=1;i<stringB.length();i=i+2){
//            //If a pair of plaintext letters is repeated, insert a padding character between the plaintext letters.
//            if(stringB.charAt(i)==stringB.charAt(i-1)){
//                stringB.insert(i,'X');               
//            }
//        } 
        //If the processed plaintext length is not even, add the letter x after it.
        if(stringB.length()%2!=0){
            stringB.append('X');
        }
        String plain2=stringB.toString();
        return plain2;
    }

    /**
     * Process the key, convert J to I, remove duplicate letters
     * @param Key
     * @return List<Character>
     * @author hou
     */
    public static List<Character> dealK(String Key){
        Key=Key.toUpperCase();//Convert key to uppercase
        Key=Key.replaceAll("[^A-Z]", "");//Remove all non-alphabetic characters   
//        Key=Key.replaceAll("J","I");//Convert J to I
        List<Character> list=new ArrayList<Character>();
        char[] ca=Key.toCharArray();
        int len=ca.length;
        for(int i=0;i<len;i++){
            //除去重复出现的字母
            if(!list.contains(ca[i])){
                list.add(ca[i]);
            }
        }
//        System.out.println("Processed key："+list);
        return list;
    }

    /**
     * 将The secret letters are added to the 5×5 matrix one by one, and the remaining space will be the un-added English letters.
     * Add in the order of a-z. (I and J are treated as the same I)
     * @param Key
     * @return 5*5matrix
     * @author hou
     */
    public static char[][] matrix(String Key){
        List<Character> keyWord=dealK(Key);
        //Add the letters that appear in K
        List<Character> list=new ArrayList<Character>(keyWord);
        //Add the remaining letters sorted alphabetically
        for(char ch='A';ch<='Z';ch++){
            if(!list.contains(ch)){
                list.add(ch);
            }
        }
        char[][] matrix=new char[4][7];
        int count=0;
        char a = '1';
        char b = '2';
        for(int i=0;i<4;i++){
            for(int j=0;j<7;j++){
            	if(count<list.size())
            		matrix[i][j]=list.get(count++);
            	else if(count == list.size()){
            		matrix[i][j] = a;
            		count++;
            	}else{
            		matrix[i][j] = b;
            		count++;
            	}
            }
        }
        System.out.println("Matrix:");
        showMatrix(matrix);
        return matrix;
    } 

    /**
     * Print matrix
     * @param matrix
     * @author hou
     */
    public static void showMatrix(char[][] matrix){
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[i].length;j++){
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }
    }

    /**
     * Encrypt the plaintext pair according to the playfair algorithm
     * @param matrix
     * @param ch1
     * @param ch2
     * @return String
     * @author hou
     */
    public static String getPosition(char[][] matrix,char ch1,char ch2){     
        //获取明文对在矩阵中的位置
        int r1=0,c1=0,r2=0,c2=0;
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[i].length;j++){
                if(ch1==matrix[i][j]){
                    r1=i;
                    c1=j;
                }
                if(ch2==matrix[i][j]){
                    r2=i;
                    c2=j;
                }
            }
        }
        StringBuilder stringB=new StringBuilder();
        //进行规制匹配，得到密文对               
        if(r1==r2&&c1!=c2){
        	if(r1==3){
    			stringB.append(matrix[r1][(c1+1)%7]);
	        	stringB.append(matrix[r1][(c2+1)%7]);
        	}else{
	            //明文字母对的两个字母在同一行，则截取右边的字母
	        	stringB.append(matrix[r1][(c1+1)%7]);
	        	stringB.append(matrix[r1][(c2+1)%7]);
        	}
        }else if(c1==c2&&r1!=r2){
    		//明文字母对的两个字母在同一列，则截取下方的字母
        	stringB.append(matrix[(r1+1)%4][c1]);
        	stringB.append(matrix[(r2+1)%4][c1]);
        }else if(c1==c2&&r1==r2){
        	stringB.append(matrix[(r1+1)%4][c1]);
        	stringB.append(matrix[(r2+1)%4][c1]);
        }else{
            //明文字母所形成的矩形对角线上的两个字母，任意选择两种方向 
            //明文对中的每一个字母将由与其同行，且与另一个字母同列的字母代替
    		stringB.append(matrix[r1][c2]);
        	stringB.append(matrix[r2][c1]);
            //sb.append(matrix[r2][c1]);
            //sb.append(matrix[r1][c2]);
        }
//        sb.append(' ');
        return stringB.toString();
    }

    /**
     * Decrypt the ciphertext pair according to the playfair algorithm
     * @param matrix
     * @param ch1
     * @param ch2
     * @return Clear text
     * @author hou
     */
    public static String getdPosition(char[][] matrix,char ch1,char ch2){     
        //Get the location of the ciphertext pair in the matrix
        int r1=0,c1=0,r2=0,c2=0;
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[i].length;j++){
                if(ch1==matrix[i][j]){
                    r1=i;
                    c1=j;
                }
                if(ch2==matrix[i][j]){
                    r2=i;
                    c2=j;
                }
            }
        }
        StringBuilder stringB=new StringBuilder();
        //Carry out regulatory matching and get plain text pair            
        if(r1==r2&&c1!=c2){
            //明文字母对的两个字母在同一行，则截取右边的字母
    		//只会出现c是1-5的情况，由0-4右移得出
        	stringB.append(matrix[r1][(c1-1+7)%7]);
        	stringB.append(matrix[r1][(c2-1+7)%7]);
        }else if(c1==c2&&r1!=r2){
    		//明文字母对的两个字母在同一列，则截取下方的字母
        	stringB.append(matrix[(r1-1+4)%4][c1]);
        	stringB.append(matrix[(r2-1+4)%4][c1]);
        }else if(c1==c2&&r1==r2){
        	stringB.append(matrix[(r1-1+4)%4][c1]);
        	stringB.append(matrix[(r2-1+4)%4][c1]);
        }else{
            //明文字母所形成的矩形对角线上的两个字母，任意选择两种方向 
            //明文对中的每一个字母将由与其同行，且与另一个字母同列的字母代替
    		stringB.append(matrix[r1][c2]);
        	stringB.append(matrix[r2][c1]);
            //sb.append(matrix[r2][c1]);
            //sb.append(matrix[r1][c2]);
        }
        return stringB.toString();
    }
    
    /**
     * Encrypt the plaintext
     * @param Plain
     * @param Key
     * @return cyperText
     * @author hou
     */
    public String encrypt(String Plain,String Key){       
        char[] ca=dealP(Plain).toCharArray();       
        char[][] matrix=matrix(Key);
        StringBuilder stringB=new StringBuilder();
        for(int i=0;i<ca.length;i=i+2){
        	stringB.append(getPosition(matrix,ca[i],ca[i+1]));
        }
        return stringB.toString();
    }


    /**
     * decript plaintext
     * @param cyper
     * @param key
     * @return Plaintext
     * @author hou
     */
    public String decrypt(String cyper,String key){
    	cyper=cyper.toUpperCase();
//    	cyper=cyper.replaceAll("[^A-Z]", "");//Remove all non-alphabetic characters
        char[] ca=cyper.toCharArray();
        char[][] matrix=matrix(key);
        StringBuilder stringB=new StringBuilder();
        for(int i=0;i<ca.length;i=i+2){
        	stringB.append(getdPosition(matrix,ca[i],ca[i+1]));
        }
        return stringB.toString();
    }
}
