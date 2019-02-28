package nts.uk.ctx.at.record.app.find.workrecord.operationsetting;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumConstant;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.AppWithDetailExporAdp;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.ApplicationTypeAdapter;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ApplicationTypeFinder {
	
	@Inject
	private ApplicationTypeAdapter adapter;
	
	
	public List<EnumConstant> getAppWithOvertimeInfo(){
		String companyId = AppContexts.user().companyId();
		List<Integer> itemIds = Arrays.asList(0,1,2,3,4,6,7,14);
		List<AppWithDetailExporAdp> lstApp = adapter.getAppWithOvertimeInfo(companyId).stream()
				.map(x -> {
					x.setAppType(convertTypeUi(x));
					return x;
				}).filter(x -> itemIds.contains(x.getAppType())).collect(Collectors.toList());
		List<EnumConstant> result =  lstApp.stream().map(x -> {
			return new EnumConstant(x.getAppType(), x.getAppName(), x.getOvertimeAtr() == null ? "" : x.getOvertimeAtr().toString());
		}).collect(Collectors.toList());
		return result;
	}
	
	public int convertTypeUi(AppWithDetailExporAdp dto) {
		switch (dto.getAppType()) {
		case 0:
			return dto.getOvertimeAtr() -1;
		case 7:
			return dto.getAppType() + dto.getStampAtr() + 1;
		case 6:
			return 7;
		case 1:
		case 2:
		case 4:
			return dto.getAppType() + 2;
		case 10:
			return 14;
			

		default:
			return dto.getAppType() + 999;
		}
	}
	
}
