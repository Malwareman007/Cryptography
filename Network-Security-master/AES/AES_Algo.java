
public class AES_Algo{
	

	public static void main(String[] args) {
		
//		String key =  /*"2b7e151628aed2a6abf7158809cf4f3c";*/"5468617473206D79204B756E67204675";
//		String message=/*"3243f6a8885a308d313198a2e0370734";*/"54776F204F6E65204E696E652054776F";
		key = key.toUpperCase();
		message = message.toUpperCase();
		KeyGen kg = new KeyGen();
		Conversion conv = new Conversion();
		kg.expandKey(key);
		
		Encryption enc = new Encryption();
		message = enc.enCipher(message, kg.roundKey);
		enc.deCipher(message, kg.roundKey);
		
		
	}

}
