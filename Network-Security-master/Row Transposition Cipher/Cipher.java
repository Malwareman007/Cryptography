import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Cipher {

	public static void main(String[] args) throws NumberFormatException, IOException {
		 
		 RowTranspositionCipher rtc = new RowTranspositionCipher();
		 rtc.intro();
	        
       
	}

}



public class RowTranspositionCipher {
	char [][] matrix;
	int noRows, noCols;
	public void setMatrix(String msg, String key){
		int rmdr;
		noCols = key.length();
		if((rmdr=msg.length()%noCols) == 0)
			noRows = msg.length()/noCols;
		else {
			rmdr = msg.length()%noCols;
			noRows = (msg.length()/noCols)+1;
			for(int i=1;i<=noCols-rmdr;i++)
				msg += "x";
		}
		
		matrix = new char[noRows][noCols];
		for(int i=0;i<noRows;i++){
			for(int j=0;j<noCols;j++){
				matrix[i][j] = msg.charAt(i*noCols+j);
				System.out.print(matrix[i][j]+ " ");
			}
			System.out.println("");
		}
		
	}
	
	
	public String encrypt(String msg, String key){
		setMatrix(msg, key);
		String enc="";
		int c=0;
		for(int i=0;i<noCols;i++){
			//System.out.print((int)key.charAt(i) - 48);
			for(int k=0;k<noCols;k++){
				
				if((int)key.charAt(k) - 48 == i+1){
					c = k;	
				}
			}
			
			for(int j=0;j<noRows;j++){
				enc += matrix[j][c];
			}
		}
		System.out.println("Encrypted message : "+enc);
		return enc;
	}
	
	public void decrypt(String msg, String key){
		String part, dec="";
		int c=0;
		for(int i=0;i<noCols;i++){
			part = msg.substring(i*noRows, noRows*(i+1));
	//		System.out.println(part);
			
			for(int k=0;k<noCols;k++){
				
				if((int)key.charAt(k) - 48 == i+1){
					c = k;	
				}
			}

			for(int j=0;j<noRows;j++){
				matrix[j][c] = part.charAt(j);
			}
			
		}
		
		for(int i=0;i<noRows;i++)
			for(int j=0;j<noCols;j++)
				dec += matrix[i][j];
		System.out.println("Decrypted message : "+dec);
	}
	
	public void intro() throws IOException{
		System.out.println("\t ROW TRANSPOSITION CIPHER");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter the message: ");
        String str = in.readLine();
        String message = str.replace(" ", "").toLowerCase();
       System.out.println(message + " : "+ message.length());
        System.out.println("Enter the key: ");
        String key = in.readLine();
        key = key.toLowerCase();
        
        
        decrypt(encrypt(message, key),key);
	}
}
