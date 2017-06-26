package nts.uk.ctx.basic.app.command.system.era;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.system.era.Era;
@Value
public class UpdateEraCommand {
	private String eraName;
	private String eraHist;
	private String eraMark;
	private GeneralDate startDate;
	private GeneralDate endDate;
	private int fixAttribute;
	public Era toDomain(){
		Era domain = Era.createFromDataType(this.eraName, this.eraHist, this.eraMark, this.startDate, this.endDate, this.fixAttribute);
		return domain;
	}
}
