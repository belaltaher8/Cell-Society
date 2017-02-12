package cs.configuration.configs;

import cs.configuration.ConfigDoc;
import cs.configuration.XMLException;
import cs.configuration.XMLReader;

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
	
	public double getPercentTolerance() {
		return percentTolerance;
	}
}
