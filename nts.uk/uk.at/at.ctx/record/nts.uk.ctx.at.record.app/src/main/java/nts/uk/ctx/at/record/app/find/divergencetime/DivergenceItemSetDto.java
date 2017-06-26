package nts.uk.ctx.at.record.app.find.divergencetime;

import lombok.Value;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceItemSet;

@Value
public class DivergenceItemSetDto {
	/*会社ID*/
	private String companyId;
	/*乖離時間ID*/
	private int divTimeId;
	/*勤怠項目ID*/
	private int attendanceId;
	public static DivergenceItemSetDto fromDomain(DivergenceItemSet domain){
		return new DivergenceItemSetDto(domain.getCompanyId(),
					domain.getDivTimeId(),
					domain.getDivergenceItemId()
					);
	}
}
