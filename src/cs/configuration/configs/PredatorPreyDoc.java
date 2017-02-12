package cs.configuration.configs;

import cs.configuration.ConfigDoc;
import cs.configuration.XMLException;
import cs.configuration.XMLReader;

public class PredatorPreyDoc extends ConfigDoc {
	public static final int FISH_BREED_DEFAULT = 8;
	public static final int SHARK_BREED_DEFAULT = 15;
	public static final int SHARK_STARVE_DEFAULT = 5;
	
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
			FISH_BREED_INTERVAL = FISH_BREED_DEFAULT;
		}
		
		try {
			SHARK_BREED_INTERVAL = this.getReader().getInt("SHARK_BREED_INTERVAL", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			SHARK_BREED_INTERVAL = SHARK_BREED_DEFAULT;
		}
		
		try {
			SHARK_STARVE_INTERVAL = this.getReader().getInt("SHARK_STARVE_INTERVAL", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			SHARK_STARVE_INTERVAL = SHARK_STARVE_DEFAULT;
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
