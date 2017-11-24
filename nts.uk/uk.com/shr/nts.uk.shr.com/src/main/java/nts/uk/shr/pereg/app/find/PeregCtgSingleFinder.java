package nts.uk.shr.pereg.app.find;

public interface PeregCtgSingleFinder <R, Q> extends PeregFinder <R, Q>{

	R getCtgSingleData(Q query);
	
	@SuppressWarnings("unchecked")
	default R handleCtgSingProcessor(Object query) {
		return this.getCtgSingleData((Q) query);
	}
}
