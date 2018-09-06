package nts.uk.ctx.pr.core.dom.laborinsurance;

import java.util.List;

/**
* 雇用保険料率
*/
public interface EmpInsurBusBurRatioRepository
{

    
    List<EmpInsurBusBurRatio> getEmpInsurBusBurRatioByHisId(String hisId);

    void add(EmpInsurBusBurRatio domain);

    void update(EmpInsurBusBurRatio domain);

    void remove(String hisId, int empPreRateId);

}
