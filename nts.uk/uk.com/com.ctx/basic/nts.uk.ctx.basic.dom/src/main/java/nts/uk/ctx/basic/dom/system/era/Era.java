package nts.uk.ctx.basic.dom.system.era;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;

public class Era extends AggregateRoot {
	@Getter
	@Setter
	private EraName eraName;
	@Getter
	@Setter
	private String eraHist;
	@Getter
	@Setter
	private EraMark eraMark;
	@Getter
	@Setter
	private GeneralDate startDate;
	@Getter
	@Setter
	private GeneralDate endDate;
	@Getter
	@Setter
	private FixAttribute fixAttribute;

	public Era(EraName eraName, String eraHist, EraMark eraMark, GeneralDate startDate, GeneralDate endDate,
			FixAttribute fixAttribute) {
		super();
		this.eraName = eraName;
		this.eraHist = eraHist;
		this.eraMark = eraMark;
		this.startDate = startDate;
		this.endDate = endDate;
		this.fixAttribute = fixAttribute;
	}

	// public Era(GeneralDate startDate) {
	// super();
	// this.startDate = startDate;
	// }

	public Era(String eraHist) {
		super();
		this.eraHist = eraHist;
	}

	// convert data to era type
	public static Era createFromDataType(String eraName, String eraHist, String eraMark, GeneralDate startDate,
			GeneralDate endDate, int fixAttribute) {
		return new Era(new EraName(eraName), eraHist, new EraMark(eraMark), startDate, endDate,
				EnumAdaptor.valueOf(fixAttribute, FixAttribute.class));

	}

	public static Era createFromDataType(String eraHist) {
		return new Era(eraHist);

	}

	public static Era createFromDataType(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

}
