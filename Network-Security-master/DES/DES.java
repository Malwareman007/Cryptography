

public class DES{

	public static void main(String[] args) throws IOException {
		
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter 64-bit Hexadecimal Key : ");
	    	String Key = in.readLine();
	//	Key = "133457799BBCDFF1";
		Key = Key.toUpperCase();
		KeyGen gen = new KeyGen();
		gen.generateKey(Key);
		
		
		System.out.print("Enter 64-bit Hexadecimal message : ");
	    	String Msg = in.readLine();
	//	Msg = "0123456789ABCDEF";
		Msg = Msg.toUpperCase();
	
		
		DataEncryption enc = new DataEncryption();
		String encryptedMsg="";
			
		    System.out.println("\t 1 Encrypt\n\t 2 Decrypt");
			int opt = Integer.parseInt(in.readLine());
			if(opt ==1){
			encryptedMsg = enc.enCipher(Msg, gen.key);
			System.out.println("Encrypted Message : "+encryptedMsg);
			}
			else if(opt==2){
			String decryptedMsg = enc.deCipher(Msg, gen.key);
			
			System.out.println("Decrypted Message : "+decryptedMsg);
			}
		

	}

}
