
/*
 * @author: Mujtaba Ali (13p-6134)
 */
public class KeyGen extends DataEncryption{
	public int [] PC1 = { 57,   49,    41,   33,    25,    17,    9,
			            1,   58,    50,   42,    34,    26,   18,
			            10,    2,    59,   51,    43,    35,   27,
			            19,   11,     3,   60,    52,    44,   36,
			            63,   55,    47,   39,    31,    23,   15,
			             7,   62,    54,   46,    38,    30,   22,
			            14,    6,    61,   53,    45,    37,   29,
			            21,   13,     5,   28,    20,    12,    4,};
	
	public int [] PC2={14,    17,   11,    24,     1,    5,
						3,    28,   15,     6,    21,   10,
				       23,    19,   12,     4,    26,    8,
				       16,     7,   27,    20,    13,    2,
				       41,    52,   31,    37,    47,   55,
				       30,    40,   51,    45,    33,   48,
				       44,    49,   39,    56,    34,   53,
				       46,    42,   50,    36,    29,   32};

	public String [] key = new String[48];
	
	/*
	 * Generate 16 48-bit keys
	 */
	public void generateKey(String binKey){
		Conversion conv = new Conversion();
		binKey = conv.hexToBin(binKey);
		
		String k = permute(PC1,binKey);
		
		String [] C = new String[17];
		String [] D = new String[17];
		String [] K = new String[17 * 2];
		
		
		C[0] = k.substring(0, 28);
		D[0] = k.substring(28, 56);
		K[0] = C[0] + D[0];
		key[0] = permute(PC2, K[0]);
		
		// till C16 and D16 shifts	
		for(int i=1;i<C.length;i++){
			if(i==1 || i==2 || i==9 || i==16){
				C[i] = leftShift(C[i-1],1);
				D[i] = leftShift(D[i-1],1);
				K[i] = C[i] + D[i];
				key[i] = permute(PC2, K[i]);
		
			}
			else{
				C[i] = leftShift(C[i-1],2);
				D[i] = leftShift(D[i-1],2);
				K[i] = C[i] + D[i];
				key[i] = permute(PC2, K[i]);
		
			}
		
		}
		
	}
	
}
