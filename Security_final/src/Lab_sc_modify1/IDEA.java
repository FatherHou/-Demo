/**
 * 
 */
package Lab_sc_modify1;

/**
 * @author hou
 *
 */
public class IDEA {

	/*
	 * @author hou
	 * Encryption
     * @param X1,X2,X3,X4,Z,c_string
     * @return encryption
     */
	byte[] Encrypt(char X1,char X2,char X3,char X4,char[] Z,byte[] c_string) {
		// 本函数的目的是加密
		char temp1,temp2,temp3,temp4,temp5,temp6,temp7,temp8,temp9,temp10;
		// 下面做8圈叠代
		for (int num = 0; num <= 7; num++) {
			temp1 = multiply(Z[1 + num * 6], X1); // 1
			temp2 = (char) (X2 + Z[2 + num * 6]); // 2
			temp3 = (char) (X3 + Z[3 + num * 6]); // 3
			temp4 = multiply(Z[4 + num * 6], X4); // 4
			temp5 = (char) (temp1 ^ temp3); // 5
			temp6 = (char) (temp2 ^ temp4); // 6
			temp7 = multiply(Z[5 + num * 6], temp5); // 7
			temp8 = (char) (temp7 + temp6);
			temp9 = multiply(Z[6 + num * 6], temp8);
			temp10 = (char) (temp7 + temp9);
 
			X1 = (char) (temp1 ^ temp9);
			if (num != 7) {
				X2 = (char) (temp3 ^ temp9);
				X3 = (char) (temp2 ^ temp10);
			} else {
				X2 = (char) (temp2 ^ temp10);
				X3 = (char) (temp3 ^ temp9);
			}
			X4 = (char) (temp4 ^ temp10);
		} // end of 8 times
 
		// 输出变换
		X1 = multiply(Z[49], X1);
		X2 += Z[50];
		X3 += Z[51];
		X4 = multiply(Z[52], X4);
 
		// 把X1,X2,X3,X4复制到c_string中。c_string byte数组8字节,X1 char16字节,需要移位
		// now,creat c_string from X1..X4;
		c_string[1] = (byte) X1;
		c_string[0] = (byte) (X1 >> 8);
		c_string[3] = (byte) X2;
		c_string[2] = (byte) (X2 >> 8);
		c_string[5] = (byte) X3;
		c_string[4] = (byte) (X3 >> 8);
		c_string[7] = (byte) X4;
		c_string[6] = (byte) (X4 >> 8);
		return c_string;
		// end of encryption
	}
	
	
	/*
	 * @author hou
	 * Decryption
     * @param X1,X2,X3,X4,Z_1,c_string
     * @return decryption
     */
	byte[] Decrypt(char X1,char X2,char X3,char X4,char[] Z_1,byte[]c_string) { // 本函数的目的是解密
		char temp1,temp2,temp3,temp4,temp5,temp6,temp7,temp8,temp9,temp10;
		// 下面做8圈叠代
		for (int num = 0; num <= 7; num++) {
			temp1 = multiply(Z_1[1 + num * 6], X1); // 1
			temp2 = (char) (X2 + Z_1[2 + num * 6]); // 2
			temp3 = (char) (X3 + Z_1[3 + num * 6]); // 3
			temp4 = multiply(Z_1[4 + num * 6], X4); // 4
			temp5 = (char) (temp1 ^ temp3); // 5
			temp6 = (char) (temp2 ^ temp4); // 6
			temp7 = multiply(Z_1[5 + num * 6], temp5); // 7
			temp8 = (char) (temp7 + temp6);
			temp10 = multiply(Z_1[6 + num * 6], temp8);
			temp9 = (char) (temp7 + temp10);
 
			X1 = (char) (temp1 ^ temp10);
			if (num != 7) {
				X2 = (char) (temp3 ^ temp10);
				X3 = (char) (temp2 ^ temp9);
			} else {
				X2 = (char) (temp2 ^ temp9);
				X3 = (char) (temp3 ^ temp10);
			}
			X4 = (char) (temp4 ^ temp9);
		} // end of 8 times
 
		// 输出变换
		X1 = multiply(Z_1[49], X1);
		X2 += Z_1[50];
		X3 += Z_1[51];
		X4 = multiply(Z_1[52], X4);
 
		// 把X1,X2,X3,X4复制到c_string中。
		// now,creat c_string from X1..X4;
		c_string[1] = (byte) X1;
		c_string[0] = (byte) (X1 >> 8);
		c_string[3] = (byte) X2;
		c_string[2] = (byte) (X2 >> 8);
		c_string[5] = (byte) X3;
		c_string[4] = (byte) (X3 >> 8);
		c_string[7] = (byte) X4;
		c_string[6] = (byte) (X4 >> 8);
		// end of decryption
		return c_string;
	}
	
	/*
	 * @author hou
	 * Char multiply
     * @param input1,input2
     * @return input1
     */
	private char multiply(char input1, char input2) {
		long p = input1 * input2;
		if (p == 0)
			input2 = (char) (65537 - input1 - input2);
		else {
			input1 = (char) (p >> 16);
			input2 = (char) p;
			input1 = (char) (input2 - input1);
 
			if (input2 < input1)
				input1 += 65537;
		}
		return input1;
	}
	
	/*
	 * @author hou
	 * Generate an subkey of encryption 
     * @param Z,k_string
     * @return Z
     */
	char[] Creat_encrypt_sub_k(char[] Z,byte[] k_string) {
		// 本函数的目的是生成加密子密钥
		// creat encrypt sub key and store to Z[57]
		char temp;
		byte temp1, temp2, temp3;
		int i;
		int num;
		for (num = 0; num <= 6; num++) {
			Z[1 + num * 8] = (char) k_string[0];
			Z[1 + num * 8] <<= 8;
			temp = (char) k_string[1];
			temp = (char) (temp & 0x00FF);	//16位0x00FF
			Z[1 + num * 8] |= temp;
 
			Z[2 + num * 8] = (char) k_string[2];
			Z[2 + num * 8] <<= 8;
			temp = (char) k_string[3];
			temp = (char) (temp & 0x00FF);
			Z[2 + num * 8] |= temp;
 
			Z[3 + num * 8] = (char) k_string[4];
			Z[3 + num * 8] <<= 8;
			temp = (char) k_string[5];
			temp = (char) (temp & 0x00FF);
			Z[3 + num * 8] |= temp;
 
			Z[4 + num * 8] = (char) k_string[6];
			Z[4 + num * 8] <<= 8;
			temp = (char) k_string[7];
			temp = (char) (temp & 0x00FF);
			Z[4 + num * 8] |= temp;
			if (num != 6) {	//最后一次变换只需变换8位,后面8位不需要变换
				Z[5 + num * 8] = (char) k_string[8];
				Z[5 + num * 8] <<= 8;
				temp = (char) k_string[9];
				temp = (char) (temp & 0x00FF);
				Z[5 + num * 8] |= temp;
			}
			if (num != 6) {
				Z[6 + num * 8] = (char) k_string[10];
				Z[6 + num * 8] <<= 8;
				temp = (char) k_string[11];
				temp = (char) (temp & 0x00FF);
				Z[6 + num * 8] |= temp;
			}
			if (num != 6) {
				Z[7 + num * 8] = (char) k_string[12];
				Z[7 + num * 8] <<= 8;
				temp = (char) k_string[13];
				temp = (char) (temp & 0x00FF);
				Z[7 + num * 8] |= temp;
			}
			if (num != 6) {
				Z[8 + num * 8] = (char) k_string[14];
				Z[8 + num * 8] <<= 8;
				temp = (char) k_string[15];
				temp = (char) (temp & 0x00FF);
				Z[8 + num * 8] |= temp;
			}
 
			// now,start to left move 25 bit
			// first left move 24 bit
			temp1 = k_string[0];	//3 byte,24 bit
			temp2 = k_string[1];
			temp3 = k_string[2];
			for (i = 0; i <= 12; i++)
				k_string[i] = k_string[i + 3];
			k_string[13] = temp1;
			k_string[14] = temp2;
			k_string[15] = temp3;
 
			// then left move 1 bit,sum 25 bit
			byte store_bit[] = new byte[16]; // store k_strings first bit.
			for (i = 15; i >= 0; i--) {
				// from high bit to low
				store_bit[i] = (byte) (k_string[i] >> 7);
				store_bit[i] &= 0x01;
				k_string[i] <<= 1;
				if (i != 15)
					k_string[i] += store_bit[i + 1];
			}
			k_string[15] += store_bit[0];
			// complete to left move 25 bit
		} // All encrypt sub key created
		return Z;
	}
	
	/*
	 * @author hou
	 * Generate an subkey of decryption 
     * @param Z,Z_1
     * @return Z_!
     */
	char[] Creat_decrypt_sub_k(char[] Z,char[]Z_1) {
		// 本函数的目的是生成解密子密钥
		Z_1[1] = inv(Z[49]);
		Z_1[2] = (char) (0 - Z[50]);
		Z_1[3] = (char) (0 - Z[51]);
		Z_1[4] = inv(Z[52]);
		Z_1[5] = (char) (Z[47]);
		Z_1[6] = Z[48];
 
		for (int i = 1; i <= 7; i++) {
			Z_1[1 + i * 6] = inv(Z[49 - i * 6]);
			Z_1[2 + i * 6] = (char) (0 - Z[51 - i * 6]);
			Z_1[3 + i * 6] = (char) (0 - Z[50 - i * 6]);
			Z_1[4 + i * 6] = inv(Z[52 - i * 6]);
			Z_1[5 + i * 6] = Z[47 - i * 6];
			Z_1[6 + i * 6] = Z[48 - i * 6];
		}
		Z_1[49] = inv(Z[1]);
		Z_1[50] = (char) (0 - Z[2]);
		Z_1[51] = (char) (0 - Z[3]);
		Z_1[52] = inv(Z[4]);
		return Z_1;
	}
	/*
	 * @author hou
	 * Inversion matrix
     * @param x
     * @return x^-1
     */
	private char inv(char x) {
		char t0, t1, q, y;
		if (x <= 1)
			return x; /* 0 and 1 are self-inverse */
		t1 = (char) (0x10001 / x);	//65537 0x1001
		/* Since x >= 2, this fits into 16 bits */
		y = (char) (0x10001 % x);
		if (y == 1)
			return (char) (1 - t1);
		t0 = 1;
		do {
			q = (char) (x / y);
			x = (char) (x % y);
			t0 += (char) (q * t1);
			if (x == 1)
				return t0;
			q = (char) (y / x);
			y = (char) (y % x);
			t1 += (char) (q * t0);
		} while (y != 1);
		return (char) (1 - t1);
	}
	
}
