package nts.uk.ctx.at.shared.app.find.attendance;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.uk.ctx.at.shared.dom.attendance.AttendanceTypeRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
@Transactional
public class AttendanceTypeFinder {
	
	@Inject
	private AttendanceTypeRepository attendanceTypeRepository;
	
	public List<AttendanceTypeDto> getItemByType(int type){
		String companyID = AppContexts.user().companyId();
		return this.attendanceTypeRepository.getItemByType(companyID, type).stream()
			.map(x -> new AttendanceTypeDto(x.getCompanyId(), x.getAttendanceItemId(), x.getAttendanceItemType().value))
			.collect(Collectors.toList());
	}
	
}
