package cs.configuration;

import java.util.ArrayList;
import java.util.Collection;
import cs.model.Point;

public class XMLHelper {
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
