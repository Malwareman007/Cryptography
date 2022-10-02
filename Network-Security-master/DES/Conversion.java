
public class Conversion {

	
	
	/*
	 * Convert hexadecimal string into binary string
	 */
	public String hexToBin(String hex){
	    int intHex;
	    String binPart = "";
	    String binary = "";
	    hex = hex.trim();
	    hex = hex.replaceFirst("0x", "");

	    for(int i = 0; i < hex.length(); i++){
	        intHex = Integer.parseInt(""+hex.charAt(i),16);
	        binPart = Integer.toBinaryString(intHex);
	        while(binPart.length() < 4){
	            binPart = "0" + binPart;
	        }
	        binary += binPart;
	    }
	    return binary;
	}

	/*
	 * Convert decimal into binary string 0-16
	 */
	public String decToBin(int num){
		String binStr="";
		if(num==0)
			binStr = "0000";
		else if(num==1)
			binStr = "0001";
		else if(num==2)
			binStr = "0010";
		else if(num==3)
			binStr = "0011";
		else if(num==4)
			binStr = "0100";
		else if(num==5)
			binStr = "0101";
		else if(num==6)
			binStr = "0110";
		else if(num==7)
			binStr = "0111";
		else if(num==8)
			binStr = "1000";
		else if(num==9)
			binStr = "1001";
		else if(num==10)
			binStr = "1010";
		else if(num==11)
			binStr = "1011";
		else if(num==12)
			binStr = "1100";
		else if(num==13)
			binStr = "1101";
		else if(num==14)
			binStr = "1110";
		else if(num==15)
			binStr = "1111";
		return binStr;
	}
	
	/*
	 * Convert binary string into hexadecimal string 0-F
	 */
	public String binToHex(String bin){
		String hex="";
		if(bin.equals("0000"))
			hex = "0";
		else if(bin.equals("0001"))
			hex = "1";
		else if(bin.equals("0010"))
			hex = "2";
		else if(bin.equals("0011"))
			hex = "3";
		else if(bin.equals("0100"))
			hex = "4";
		else if(bin.equals("0101"))
			hex = "5";
		else if(bin.equals("0110"))
			hex = "6";
		else if(bin.equals("0111"))
			hex = "7";
		else if(bin.equals("1000"))
			hex = "8";
		else if(bin.equals("1001"))
			hex = "9";
		else if(bin.equals("1010"))
			hex = "A";
		else if(bin.equals("1011"))
			hex = "B";
		else if(bin.equals("1100"))
			hex = "C";
		else if(bin.equals("1101"))
			hex = "D";
		else if(bin.equals("1110"))
			hex = "E";
		else if(bin.equals("1111"))
			hex = "F";
		
		return hex;
	}
	
	
}
