package cs.configuration.configs;

import cs.configuration.ConfigDoc;
import cs.configuration.XMLException;
import cs.configuration.XMLReader;

/**
 * @author jaydoherty
 * This class extends the ordinary ConfigDoc and adds breeding timers and starving timers for the fish
 * and sharks in the Predator-Prey simulation. Again, the purpose of this class is to provide a centralized 
 * location for all parameters so that the model and view always agree.
 */
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
	
	
    /**
     * @return the number of time steps necessary for fish to breed
     */
    public int getFishBreedInterval() {
    	return FISH_BREED_INTERVAL;
    }
    
    /**
     * @return the number of time steps necessary for sharks to breed
     */
    public int getSharkBreedInterval() {
    	return SHARK_BREED_INTERVAL;
    }
    
    /**
     * @return the number of time steps necessary for sharks to starve
     */
    public int getSharkStarveInterval() {
    	return SHARK_STARVE_INTERVAL;
    }
}
