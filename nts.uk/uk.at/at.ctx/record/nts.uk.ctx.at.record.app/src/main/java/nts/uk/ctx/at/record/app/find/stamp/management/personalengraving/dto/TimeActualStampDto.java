package nts.uk.ctx.at.record.app.find.stamp.management.personalengraving.dto;

import java.util.Optional;

import lombok.Data;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;

/**
 * @author anhdt
 *
 */
@Data
public class TimeActualStampDto {
	
	private Integer numberOfReflectionStamp;
	
	private Integer actualAfterRoundingTime;
	private Integer actualTimeWithDay;
	private String actualLocationCode;
	private Integer actualStampSourceInfo;
	
	private Integer stampAfterRoundingTime;
	private Integer stampTimeWithDay;
	private String stampLocationCode;
	private Integer stampStampSourceInfo;
	
	public TimeActualStampDto (Optional<TimeActualStamp> oDomain) {
		if(oDomain.isPresent()) {
			TimeActualStamp domain = oDomain.get();
			
			Optional<WorkStamp> oActualWt = domain.getActualStamp();
			if(oActualWt.isPresent()) {
				WorkStamp actualWt = oActualWt.get();
				this.actualAfterRoundingTime = actualWt.getAfterRoundingTime().v();
				this.actualTimeWithDay = actualWt.getTimeWithDay().v();
				this.actualLocationCode = actualWt.getLocationCode().isPresent() ? actualWt.getLocationCode().get().v() : null;
				this.actualStampSourceInfo = actualWt.getStampSourceInfo().value;
			}
			
			Optional<WorkStamp> oStampWt = domain.getStamp();
			if(oStampWt.isPresent()) {
				WorkStamp stampWt = oStampWt.get();
				this.stampAfterRoundingTime = stampWt.getAfterRoundingTime().v();
				this.stampTimeWithDay = stampWt.getTimeWithDay().v();
				this.stampLocationCode = stampWt.getLocationCode().isPresent() ? stampWt.getLocationCode().get().v() : null;
				this.stampStampSourceInfo = stampWt.getStampSourceInfo().value;
			}
			
		}
	}
	
}
