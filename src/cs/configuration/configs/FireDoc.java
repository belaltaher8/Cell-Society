package cs.configuration.configs;

import cs.configuration.ConfigDoc;
import cs.configuration.XMLException;
import cs.configuration.XMLReader;

/**
 * @author jaydoherty
 * This class extends the ordinary ConfigDoc and adds "probCatch" for the Spreading Fire simulation.
 * Again, the purpose of this class is to provide a centralized location for all parameters so that
 * the model and view always agree.
 */
public class FireDoc extends ConfigDoc {

	private double probCatch;
	
	public FireDoc(XMLReader reader) throws XMLException {
		super(reader);
	}

	@Override
	protected void initParams() throws XMLException {
		super.initParams();
		
		try {
			probCatch = this.getReader().getDouble("PROB_CATCH", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			probCatch = 0.5;
		}
	}
	
	
	/**
	 * @return the probability of trees catching fire for this simulation
	 */
	public double getProbCatch() {
		return probCatch;
	}
}
