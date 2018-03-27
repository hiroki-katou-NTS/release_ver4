package nts.uk.ctx.at.record.app.find.divergencetime;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.app.find.divergence.time.DIvergenceTimeAttendanceItemDto;
import nts.uk.ctx.at.record.dom.divergencetime.service.attendance.AttendanceTypeDivergenceAdapter;
import nts.uk.ctx.at.record.dom.divergencetime.service.attendance.AttendanceTypeDivergenceAdapterDto;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class DIvergenceTimeAttendanceItemFinder {

	@Inject 
	private AttendanceTypeDivergenceAdapter atType;
	
//	public List<DIvergenceTimeAttendanceItemDto> getAllAtType(int screenUseAtr){
//		String companyId = AppContexts.user().companyId();
//		List<DIvergenceTimeAttendanceItemDto> data = atType.getItemByScreenUseAtr(companyId, screenUseAtr);
//		return data;
//	}
}
