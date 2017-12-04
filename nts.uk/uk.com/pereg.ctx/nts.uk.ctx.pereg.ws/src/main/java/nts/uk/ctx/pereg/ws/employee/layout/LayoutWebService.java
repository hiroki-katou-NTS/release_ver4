package nts.uk.ctx.pereg.ws.employee.layout;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pereg.app.find.layout.GetLayoutByCeateTypeDto;
import nts.uk.ctx.pereg.app.find.layout.LayoutFinder;
import nts.uk.ctx.pereg.app.find.layout.LayoutQuery;
import nts.uk.ctx.pereg.app.find.layout.dto.EmpMaintLayoutDto;
import nts.uk.ctx.pereg.app.find.layout.dto.SimpleEmpMainLayoutDto;
import nts.uk.ctx.pereg.app.find.layoutdef.NewLayoutDto;

@Path("bs/employee/layout")
@Produces(MediaType.APPLICATION_JSON)
public class LayoutWebService extends WebService {

	@Inject
	private LayoutFinder layoutFinder;

	@Path("getAllSimpleLayouts/{employeeId}")
	@POST
	public List<SimpleEmpMainLayoutDto> getSimpleLayoutList(@PathParam("employeeId") String employeeId) {
		return this.layoutFinder.getSimpleLayoutList(employeeId);
	}

	@Path("getByEmp")
	@POST
	public EmpMaintLayoutDto getByCreateType(LayoutQuery query) {
		return this.layoutFinder.getLayout(query);
	}

}
