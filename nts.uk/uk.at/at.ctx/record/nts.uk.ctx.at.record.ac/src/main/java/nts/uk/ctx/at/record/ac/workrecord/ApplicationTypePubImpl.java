package nts.uk.ctx.at.record.ac.workrecord;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.workrecord.operationsetting.AppWithDetailExporAdp;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.ApplicationTypeAdapter;
import nts.uk.ctx.at.request.pub.screen.ApplicationPub;
import nts.uk.ctx.bs.employee.pub.company.StatusOfEmployee;
import nts.uk.ctx.bs.employee.pub.company.SyCompanyPub;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ApplicationTypePubImpl implements ApplicationTypeAdapter {

	@Inject
	private ApplicationPub appPub;
	
	@Inject
	private SyCompanyPub syCompanyPub;

	@Override
	public List<AppWithDetailExporAdp> getAppWithOvertimeInfo(String companyID) {
		return appPub.getAppWithOvertimeInfo(companyID).stream()
				.map(item -> new AppWithDetailExporAdp(item.getAppType(), item.getAppName(), item.getOvertimeAtr(),
						item.getStampAtr())).collect(Collectors.toList());
	}

	@Override
	public Map<String, List<DatePeriod>> getListAffComHistByListSidAndPeriod(List<String> sid, DatePeriod datePeriod) {
		// TODO Auto-generated method stub
		return syCompanyPub.GetListAffComHistByListSidAndPeriod(sid, datePeriod).stream().collect(Collectors.toMap(StatusOfEmployee::getEmployeeId, StatusOfEmployee::getListPeriod));
	}

}
