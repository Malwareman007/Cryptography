/*
 * @author: Mujtaba Ali
 */


class KeyGen extends Conversion{
	String []roundKey = new String[11];
	
	String []Rcon = {"01000000","02000000","04000000","08000000","10000000","20000000","40000000","80000000","1b000000","36000000"};

	
	/*
	 * Expands cipher key to get multiple keys for multiple rounds
	 */
	void expandKey(String key){
		roundKey[0] = key;
		for(int i=1;i<11;i++){
			key = getRoundKey(key, i-1);
		}
	}
	
	/*
	 * returns key for each round
	 * r = round number
	 */
	String getRoundKey(String key, int r){
		String []w = new String[8];
		for(int i=0;i<w.length/2;i++){
			w[i] = key.substring(i*8, i*8 + 8);
		}
		
		w[4] = binToHexAll( XOR( hexToBin(w[0]), hexToBin(g(w[3],r)) ) );
		for(int i=1;i<=3;i++){
			w[i+4] = binToHexAll( XOR( hexToBin(w[i+3]), hexToBin(w[i]) ) );
		}
		roundKey[r+1] = (w[4] + w[5] + w[6] + w[7]);
		return roundKey[r+1];
	}
	
	/*
	 * returns word after performing left shift, substitution (SBOX) and XOR with round constant
	 */
	String g(String w3, int r){
		String gw3 = "";
		w3 = leftRotate(w3,2);
		gw3 = substitute(w3,SBOX);
		gw3 = binToHexAll( XOR( hexToBin(gw3), hexToBin(Rcon[r]) ) );	
		return gw3;
	}
	
}