package nts.uk.screen.at.app.dailyperformance.correction.kdw003b;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.enums.EnumConstant;
import nts.uk.ctx.at.request.app.find.application.applicationlist.AppWithDetailExportDto;
import nts.uk.ctx.at.request.app.find.application.applicationlist.ApplicationListForScreen;
import nts.uk.screen.at.app.dailyperformance.correction.DailyPerformanceScreenRepo;
import nts.uk.screen.at.app.dailyperformance.correction.datadialog.DataDialogWithTypeProcessor;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author sonnh1
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DailyPerformErrorReferFinder {

	@Inject
	private DailyPerformanceScreenRepo repo;
	
	@Inject
	private ApplicationListForScreen applicationListForScreen;
	
	@Inject
	private DataDialogWithTypeProcessor dataProcess;

	public DailyPerformErrorReferDto findByCidAndListErrCd(List<String> listErrorCode) {
		String companyId = AppContexts.user().companyId();
		String employeeIdLogin = AppContexts.user().employeeId();
		DailyPerformErrorReferDto dto = new DailyPerformErrorReferDto();
		List<AppWithDetailExportDto> lstApp = applicationListForScreen.getAppWithOvertimeInfo(companyId).stream()
				.map(x -> {
					x.setAppType(dataProcess.convertTypeUi(x));
					return x;
				}).collect(Collectors.toList());
		Map<Integer, String> nameWithType = lstApp.stream().collect(Collectors.toMap(x -> x.getAppType(), x -> x.getAppName()));
		Map<String, List<EnumConstant>> map = this.repo.findErAlApplicationByCidAndListErrCd(companyId, listErrorCode, nameWithType);
		dto.setEmployeeIdLogin(employeeIdLogin);
		dto.setMapErrCdAppTypeCd(map);
		return dto;
	}
}
