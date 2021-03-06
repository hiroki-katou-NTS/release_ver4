package nts.uk.ctx.pr.transfer.ws.rsdttaxpayee;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.transfer.app.find.rsdttaxpayee.EmpInfoDeptDto;
import nts.uk.ctx.pr.transfer.app.find.rsdttaxpayee.RsdtTaxPayAmountFinder;
import nts.uk.ctx.pr.transfer.app.find.rsdttaxpayee.RsdtTaxPayAmountParam;
import nts.uk.ctx.pr.transfer.dom.rsdttaxpayee.service.RsdtTaxPayAmountDto;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("transfer/rsdttaxpayee")
@Produces("application/json")
public class RsdtTaxPayAmountWebService extends WebService {

    @Inject
    private RsdtTaxPayAmountFinder rsdtTaxPayAmountFinder;

    @POST
    @Path("getEmpInfoDept")
    public List<EmpInfoDeptDto> getEmpInfoDept(RsdtTaxPayAmountParam param) {
        return rsdtTaxPayAmountFinder.getEmpInfoDept(param);
    }

    @POST
    @Path("getRsdtTaxPayAmount")
    public List<RsdtTaxPayAmountDto> getRsdtTaxPayAmount(RsdtTaxPayAmountParam param) {
        return rsdtTaxPayAmountFinder.getRsdtTaxPayAmount(param);
    }
}
