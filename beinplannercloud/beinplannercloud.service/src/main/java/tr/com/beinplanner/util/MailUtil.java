package tr.com.beinplanner.util;

public class MailUtil {

	
	public static String generateFirmMail(String firmName) {
		
		
		firmName=firmName.replaceAll("ö", "o");
		firmName=firmName.replaceAll("ı", "i");
		firmName=firmName.replaceAll("ü", "u");
		firmName=firmName.replaceAll("ş", "s");
		firmName=firmName.replaceAll("ğ", "g");
		firmName=firmName.replaceAll("ç", "c");
		
		
		firmName=firmName.replaceAll("Ö", "o");
		firmName=firmName.replaceAll("İ", "i");
		firmName=firmName.replaceAll("Ü", "u");
		firmName=firmName.replaceAll("Ş", "s");
		firmName=firmName.replaceAll("Ğ", "g");
		firmName=firmName.replaceAll("Ç", "c");
		
		
		firmName=firmName.replaceAll(" ", "");
		
		firmName=firmName.toLowerCase();
		
		return "info@"+firmName+".com";
		
	}
}
