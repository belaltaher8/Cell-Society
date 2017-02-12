package cs.configuration.configs;

import cs.configuration.ConfigDoc;
import cs.configuration.XMLException;
import cs.configuration.XMLReader;

public class PredatorPreyDoc extends ConfigDoc {

	private int FISH_BREED_INTERVAL;
	private int SHARK_BREED_INTERVAL;
	private int SHARK_STARVE_INTERVAL;
	
	public PredatorPreyDoc(XMLReader reader) throws XMLException {
		super(reader);
	}
	
	@Override
	protected void initParams() throws XMLException {
		super.initParams();
		
		try {
			FISH_BREED_INTERVAL = this.getReader().getInt("FISH_BREED_INTERVAL", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			FISH_BREED_INTERVAL = 5;
		}
		
		try {
			SHARK_BREED_INTERVAL = this.getReader().getInt("SHARK_BREED_INTERVAL", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			SHARK_BREED_INTERVAL = 10;
		}
		
		try {
			SHARK_STARVE_INTERVAL = this.getReader().getInt("SHARK_STARVE_INTERVAL", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			SHARK_STARVE_INTERVAL = 5;
		}
	}
	
    public int getFishBreedInterval() {
    	return FISH_BREED_INTERVAL;
    }
    public int getSharkBreedInterval() {
    	return SHARK_BREED_INTERVAL;
    }
    public int getSharkStarveInterval() {
    	return SHARK_STARVE_INTERVAL;
    }
}
