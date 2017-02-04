package cellsociety_team09.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import cellsociety_team09.model.Point;
import cellsociety_team09.model.Triple;

public class MapMaker {
	public Collection<Point> getNeighborOffsets(String fullText) {
    	Collection<Point> result = new ArrayList<Point>();
    	
    	Collection<String[]> xyPairs = splitTextIntoElementsBy(fullText, ",");
    	
		for(String[] pair : xyPairs) {
			Integer x = Integer.parseInt(pair[0]);
    		Integer y = Integer.parseInt(pair[1]);
    		result.add(new Point(x,y));
		}
    	
    	return result;
    }
	
	public Map<Integer, Double> getProbabilitiesMap(String fullText) {
    	Map<Integer, Double> result = new HashMap<Integer, Double>();
    	    	
    	Collection<String[]> keyValuePairs = splitTextIntoElementsBy(fullText, "->");
    	
		for(String[] pair : keyValuePairs) {
			Integer key = Integer.parseInt(pair[0]);
    		Double val = Double.parseDouble(pair[1]);
    		result.put(key, val);
		}
    	
    	return result;
    }
	
	public Map<Triple, Integer> getNextStateMap(String fullText) {
    	Map<Triple, Integer> result = new HashMap<Triple, Integer>();
    	
    	Collection<String[]> keyValuePairs = splitTextIntoElementsBy(fullText, "->");
    	
    	for(String[] pair : keyValuePairs) {
    		Triple key = parseTriple(pair[0]);
    		Integer val = Integer.parseInt(pair[1]);
    		result.put(key, val);
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
		result = result.replaceAll("(", "");
		result = result.replaceAll(")", "");
		result = result.replaceAll("{", "");
		result = result.replaceAll("}", "");
		return result;
	}
	
	private String removeWhiteSpace(String words){
		return words.replaceAll("\\s", "");
	}
	
	private String[] splitLines(String paragraph) {
    	paragraph = paragraph.trim();
    	return paragraph.split("\n");
    }
    
    private Triple parseTriple(String xyz) {
    	String[] vals = xyz.split(",");
    	int x = Integer.parseInt(vals[0]);
    	int y = Integer.parseInt(vals[1]);
    	int z = Integer.parseInt(vals[2]);
    	return new Triple(x,y,z);
    }
}
