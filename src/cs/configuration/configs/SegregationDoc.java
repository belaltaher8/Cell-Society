package cs.configuration.configs;

import cs.configuration.ConfigDoc;

import cs.configuration.XMLException;
import cs.configuration.XMLReader;
/**
 * @author jaydoherty
 * This class extends the ordinary ConfigDoc and adds the percent of different neighbors that will be tolerated
 * in the Segregation simulation. Again, the purpose of this class is to provide a centralized location for all 
 * parameters so that the model and view always agree.
 */
public class SegregationDoc extends ConfigDoc {

	private double percentTolerance;
	
	public SegregationDoc(XMLReader reader) throws XMLException {
		super(reader);
	}

	@Override
	protected void initParams() throws XMLException {
		super.initParams();
		
		try {
			percentTolerance = this.getReader().getDouble("PERCENT_TOLERANCE", XMLReader.FIRST_OCCURRENCE_IN_FILE);
		} catch(XMLException e) {
			percentTolerance = 0.5;
		}
	}
	
	/**
	 * @return the threshold of different neighbors at which segregation cells become unhappy
	 */
	public double getPercentTolerance() {
		return percentTolerance;
	}
}
