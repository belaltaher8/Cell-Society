package cs.configuration;

import java.util.ArrayList;
import java.util.Collection;
import cs.model.Point;

/**
 * @author jaydoherty
 * The purpose of this class is just to aid in the readability of the XML file. It converts
 * the XML formatted neighbor list into an actual list of points. This functionality was extracted
 * because it is just used here and is not critical for understanding the rest of the XML parsing.
 */
public class XMLHelper {
	
	/**
	 * @param fullText : a plain string representation of the neighbor offsets
	 * @return a list of Points corresponding to each of the neighbor offsets
	 * @throws XMLException
	 * This is the one public method for this class that other classes can call. It just
	 * translates a properly formatted String into a list of points, or else throws an exception.
	 */
	public Collection<Point> getNeighborOffsets(String fullText) throws XMLException {
		Collection<Point> result;
		try {
	    	result = new ArrayList<Point>();
	    	
	    	Collection<String[]> xyPairs = splitTextIntoElementsBy(fullText, ",");
	    	
			for(String[] pair : xyPairs) {
				Integer x = Integer.parseInt(pair[0]);
	    		Integer y = Integer.parseInt(pair[1]);
	    		result.add(new Point(x,y));
			}
		} catch(NumberFormatException e) {
			result = null;
			throw new XMLException(e);
		}
    	return result;
	}
	
	private Collection<String[]> splitTextIntoElementsBy(String fullText, String regex) {
		Collection<String[]> elementPairings = new ArrayList<String[]>();
		
		String[] lines = splitLines(fullText);
		
		for(String entry : lines) {
			entry = ignoreExtraChars(entry);
			String[] pair = entry.split(regex);
			elementPairings.add(pair);
		}
		
		return elementPairings;
	}
	
	private String ignoreExtraChars(String verbose) {
		String result = removeWhiteSpace(verbose);
		result = result.replaceAll("[(){}]", "");
		return result;
	}
	
	private String removeWhiteSpace(String words){
		return words.replaceAll("\\s", "");
	}
	
	private String[] splitLines(String paragraph) {
    	paragraph = paragraph.trim();
    	return paragraph.split("\n");
    }
}
