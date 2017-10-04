package nts.uk.ctx.bs.person.ws.person.setting.regHistory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import find.person.setting.regHistory.EmpRegHistoryDto;
import find.person.setting.regHistory.EmpRegHistoryFinder;
import nts.arc.layer.ws.WebService;

@Path("ctx/bs/person/info/setting/regisHistory")
@Produces("application/json")
public class EmpRegHistoryWebService extends WebService {

	@Inject
	private EmpRegHistoryFinder finder;

	@POST
	@Path("getLastRegHistory")
	public EmpRegHistoryDto getLastRegHistory() {
		return this.finder.getLastRegHistory();
	}

}
