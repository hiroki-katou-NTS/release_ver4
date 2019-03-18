package nts.uk.ctx.at.record.ac.workrecord;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.workrecord.operationsetting.AppWithDetailExporAdp;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.ApplicationTypeAdapter;
import nts.uk.ctx.at.request.pub.screen.ApplicationPub;

@Stateless
public class ApplicationTypePubImpl implements ApplicationTypeAdapter {

	@Inject
	private ApplicationPub appPub;

	@Override
	public List<AppWithDetailExporAdp> getAppWithOvertimeInfo(String companyID) {
		return appPub.getAppWithOvertimeInfo(companyID).stream()
				.map(item -> new AppWithDetailExporAdp(item.getAppType(), item.getAppName(), item.getOvertimeAtr(),
						item.getStampAtr())).collect(Collectors.toList());
	}

}
