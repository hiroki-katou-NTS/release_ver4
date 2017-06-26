package nts.uk.ctx.basic.app.find.system.era;

import java.time.LocalDate;
import java.util.Date;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.system.era.Era;

@Value
public class EraDto {
	 String eraName;
	 String eraHist;
	 String eraMark;
	 GeneralDate startDate;
	 GeneralDate endDate;
	 int fixAttribute;
	 
	
	public static EraDto fromDomain(Era era){
		//System.out.print(era.getEndDate().year());
		return new EraDto(era.getEraName().v(),
				era.getEraHist(),
				era.getEraMark().v(),
				era.getStartDate(),
				era.getEndDate(),
				era.getFixAttribute().value);
	}


	public EraDto(String eraName, String eraHist, String eraMark, GeneralDate startDate, GeneralDate endDate, int fixAttribute) {
		super();
		this.eraName = eraName;
		this.eraHist = eraHist;
		this.eraMark = eraMark;
		this.startDate = startDate;
		this.endDate = endDate;
		this.fixAttribute = fixAttribute;
	}
	
}
