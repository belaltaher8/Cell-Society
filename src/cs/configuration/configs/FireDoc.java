package cs.configuration.configs;

import cs.configuration.ConfigDoc;
import cs.configuration.XMLException;
import cs.configuration.XMLReader;

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
	
	public double getProbCatch() {
		return probCatch;
	}
}
