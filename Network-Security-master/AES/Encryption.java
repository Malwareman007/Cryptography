
class Encryption extends Conversion{
	
	/*
	 * Decrypts ciphertext given a ciphertext and set of round keys
	 */
	String deCipher(String msgState, String[]roundKey){
		msgState = binToHexAll( XOR( hexToBin(msgState), hexToBin(roundKey[10]) ) );
		for(int i=9;i>0;i--){
			msgState = transpose(msgState);
			msgState = performRightRotate(msgState);
			msgState = transpose(msgState);
			
			msgState = substitute(msgState,ISBOX);
			msgState = binToHexAll( XOR( hexToBin(msgState), hexToBin(roundKey[i]) ) );
			
			
			msgState = invMixColumn(msgState);
			msgState = transpose(msgState);
			
		}
		
		msgState = transpose(msgState);
		msgState = performRightRotate(msgState);
		msgState = transpose(msgState);
		
		msgState = substitute(msgState,ISBOX);
		msgState = binToHexAll( XOR( hexToBin(msgState), hexToBin(roundKey[0]) ) );
		
		System.out.println("Decrypted message : "+msgState);
		return msgState;
	}
	
	/*
	 * Encrypts message given a message and set of round keys
	 */
	String enCipher(String msgState, String [] roundKey){
		
		msgState = binToHexAll( XOR( hexToBin(msgState), hexToBin(roundKey[0]) ) );
		
		for(int i=1;i<roundKey.length-1;i++){
			msgState = substitute(msgState,SBOX);
			msgState = transpose(msgState);
			msgState = performLeftRotate(msgState);
			msgState = transpose(msgState);
			msgState = mixColumn(msgState);
			msgState = transpose(msgState);
			msgState = binToHexAll( XOR( hexToBin(msgState), hexToBin(roundKey[i]) ) );
		}
		
		msgState = substitute(msgState,SBOX);
		msgState = transpose(msgState);
		msgState = performLeftRotate(msgState);
		msgState = transpose(msgState);
		msgState = binToHexAll( XOR( hexToBin(msgState), hexToBin(roundKey[10]) ) );
		
		System.out.println("Encrypted message : "+msgState);
		return msgState;
	}
	
	/*
	 * performs the inverse mix column operation ( message * constant-matrix )
	 */
	String invMixColumn(String msg){
		String constant = "0E0B0D09"
						+ "090E0B0D"
						+ "0D090E0B"
						+ "0B0D090E";
		String m0 = "", m1 ="", m2="", m3 ="";
		String c0 = "", c1="", c2="", c3="";
		
		String temp="";
		for(int i=0;i<4;i++){
			c0 = constant.substring(i*8, i*8+2);
			c1 = constant.substring(i*8+2, i*8+4);
			c2 = constant.substring(i*8+4, i*8+6);
			c3 = constant.substring(i*8+6, i*8+8);
			for(int j=0;j<4;j++){
				
				
				m0 = msg.substring(j*8, j*8+2);
				m1 = msg.substring(j*8+2, j*8+4);
				m2 = msg.substring(j*8+4, j*8+6);
				m3 = msg.substring(j*8+6, j*8+8);
		
				temp += XOR(XOR(hexToBin(Multiply(m0,c0)),hexToBin(Multiply(m1,c1))),XOR(hexToBin(Multiply(m2,c2)),hexToBin(Multiply(m3,c3))));
			}
			
		}
		temp = binToHexAll(temp);
		
		return temp;
	}
	
	/*
	 * performs the mix column operation ( message * constant-matrix )
	 */
	String mixColumn(String msg){
		String constant = "02030101"
						+ "01020301"
						+ "01010203"
						+ "03010102";
		
		String m0 = "", m1 ="", m2="", m3 ="";
		String c0 = "", c1="", c2="", c3="";
		
		String temp="";
		
		for(int i=0;i<4;i++){
			
			c0 = constant.substring(i*8, i*8+2);
			c1 = constant.substring(i*8+2, i*8+4);
			c2 = constant.substring(i*8+4, i*8+6);
			c3 = constant.substring(i*8+6, i*8+8);
			// by implementing via look up tables L & E following 4-lines of code not needed
			c0 = hexToBin(c0);
			c1 = hexToBin(c1);
			c2 = hexToBin(c2);
			c3 = hexToBin(c3);
			
			for(int j=0;j<4;j++){
				
				m0 = msg.substring(j*8, j*8+2);
				m1 = msg.substring(j*8+2, j*8+4);
				m2 = msg.substring(j*8+4, j*8+6);
				m3 = msg.substring(j*8+6, j*8+8);
				// by implementing via look up tables L & E following 4-lines of code not needed
				m0 = hexToBin(m0);
				m1 = hexToBin(m1);
				m2 = hexToBin(m2);
				m3 = hexToBin(m3);
				// by implementing via look up tables L & E, hexToBin(binMultiply(c0,m0)) must be used bellow
				temp += XOR(XOR(binMultiply(m0,c0),binMultiply(m1,c1)),XOR(binMultiply(m2,c2),binMultiply(m3,c3)));
			}
			
		}
		temp = binToHexAll(temp);
		return temp;
	}
	
	/*
	 * multiply two binary numbers and returns the product
	 */
	String binMultiply(String multiplicand, String multiplier){
	/*// by implementing via lookup tables L & E bellow commented code should be used instead of the rest in this method
		
		String prod="";
		if(multiplicand.equals("00"))
			prod = "00";
		else if(multiplicand.equals("01"))
			prod = multiplier;
		else
			prod = E(add(L(multiplicand) , L(multiplier)));
		return prod;
	*/	
		String prod="";
		String xorTerm = "00011011";
		if(multiplicand.charAt(0)=='1'){
			if(multiplier.equals("00000011")){
				prod = leftShift(multiplicand,1);
				prod = XOR(prod, multiplicand);
				prod = XOR(prod,xorTerm);
			}
			else if(multiplier.equals("00000010")){
				prod = leftShift(multiplicand,1);
				prod = XOR(prod,xorTerm);
			}
			else if(multiplier.equals("00000001")){
				prod = multiplicand;
			}
		}
		
		else if(multiplicand.charAt(0)=='0'){
			if(multiplier.equals("00000011")){
				prod = XOR( multiplicand, leftShift(multiplicand,1));
			}
			else if(multiplier.equals("00000010")){
				prod = leftShift(multiplicand,1);
			}
			else if(multiplier.equals("00000001")){
				prod = multiplicand;
			}
		}
		return prod;
		
	}
	
	
	
	/*
	 * performs the multiplication via lookup tables L & E
	 */
	String Multiply(String multiplicand, String multiplier){
		String prod="";
		if(multiplicand.equals("00"))
			prod = "00";
		else if(multiplicand.equals("01"))
			prod = multiplier;
		else
			prod = E(add(L(multiplicand) , L(multiplier)));
			
		return prod;
	}
	
	/*
	 * returns result from E lookup table given 1-byte hexadecimal number
	 */
	String E(String hex){
		String temp="";
		int row = Integer.parseInt(hex.substring(0, 1), 16);
		int col = Integer.parseInt(hex.substring(1, 2), 16);
		temp = E[row][col];
		return temp;
	}
	
	/*
	 * returns result from L lookup table given 1-byte hexadecimal number
	 */
	String L(String hex){
		String temp="";
		int row = Integer.parseInt(hex.substring(0, 1), 16);
		int col = Integer.parseInt(hex.substring(1, 2), 16);
		temp = L[row][col];
		return temp;
	}
	
	/*
	 * performs binary left shift
	 */
	String leftShift(String word, int times){
		String l = "";
		for(int i=0;i<times;i++){
			l += '0';
		}
		String r = word.substring(times);
		return (r+l);
	}
	
	/*
	 * performs byte wise left rotate of the complete state matrix (message)
	 */
	String performLeftRotate(String msg){
		String temp="";
		for(int i=0;i<msg.length()/8;i++){
			temp += leftRotate(msg.substring(i*8, i*8+8), i*2);
		}
		return temp;
	}
	
	/*
	 * performs byte wise right rotate of the complete state matrix (message)
	 */
	String performRightRotate(String msg){
		String temp="";
		for(int i=0;i<msg.length()/8;i++){
			temp += rightRotate(msg.substring(i*8, i*8+8), i*2);
		}
		return temp;
	}
	
	/*
	 * returns transpose of a matrix (byte-wise)
	 */
	String transpose(String msg){
		String temp = "";
		for(int i=0;i<4;i++){
			temp += msg.substring(i*2, i*2+2) + msg.substring(i*2+8, i*2+8+2) + msg.substring(i*2+16, i*2+16+2) + msg.substring(i*2+24, i*2+24+2);
		}
		return temp;
	}
}
