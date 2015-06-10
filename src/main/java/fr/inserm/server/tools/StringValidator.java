package fr.inserm.server.tools;

public class StringValidator {

	/**
	 * retourne la chaine de longueur max fixée par le nb de Chars. Si nb Chars
	 * > size de la chaine, retourne la chaine max possible.
	 * 
	 * @param text
	 * @param nbChars
	 * @return
	 */
	public static String truncate(String text, int nbChars) {
		String result = text;
		if (text.length() > nbChars) {
			result = text.substring(0, nbChars);
		}
		return result;
	}

}
