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
    	
    	String[] lines = splitLines(fullText);
    	
		for(String entry : lines) {
			entry = entry.replaceAll("\\s", "");
			String[] pair = entry.split(",");
			Integer x = Integer.parseInt(pair[0]);
    		Integer y = Integer.parseInt(pair[1]);
    		result.add(new Point(x,y));
		}
    	
    	return result;
    }
	
	public Map<Integer, Double> getProbabilitiesMap(String fullText) {
    	Map<Integer, Double> result = new HashMap<Integer, Double>();
    	
    	String[] lines = splitLines(fullText);
		for(String entry : lines) {
			String[] pair = getKeyValuePair(entry);
			Integer key = Integer.parseInt(pair[0]);
    		Double val = Double.parseDouble(pair[1]);
    		result.put(key, val);
		}
    	
    	return result;
    }
	
	public Map<Triple, Integer> getNextStateMap(String fullText) {
    	Map<Triple, Integer> result = new HashMap<Triple, Integer>();
    	
    	String[] lines = splitLines(fullText);
		for(String entry : lines) {
			String[] pair = getKeyValuePair(entry);
			Triple key = parseTriple(pair[0]);
    		Integer val = Integer.parseInt(pair[1]);
    		result.put(key, val);
		}
    	
    	return result;
    }
	
	private String[] splitLines(String paragraph) {
    	paragraph = paragraph.trim();
    	return paragraph.split("\n");
    }
    
    private String[] getKeyValuePair(String entry) {
    	entry = entry.replaceAll("\\s", "");
    	return entry.split("->");
    }
    
    private Triple parseTriple(String xyz) {
    	String[] vals = xyz.split(",");
    	int x = Integer.parseInt(vals[0]);
    	int y = Integer.parseInt(vals[1]);
    	int z = Integer.parseInt(vals[2]);
    	return new Triple(x,y,z);
    }
}
