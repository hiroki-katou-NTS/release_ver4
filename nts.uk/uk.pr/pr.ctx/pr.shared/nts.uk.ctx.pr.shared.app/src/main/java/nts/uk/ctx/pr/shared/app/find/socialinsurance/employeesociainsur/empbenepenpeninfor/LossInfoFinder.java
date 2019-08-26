package nts.uk.ctx.pr.shared.app.find.socialinsurance.employeesociainsur.empbenepenpeninfor;

import nts.uk.ctx.pr.shared.app.find.socialinsurance.employeesociainsur.emphealinsurbeneinfo.EmpBasicPenNumInforDto;
import nts.uk.ctx.pr.shared.app.find.socialinsurance.employeesociainsur.emphealinsurbeneinfo.HealthInLossInfoDto;
import nts.uk.ctx.pr.shared.app.find.socialinsurance.employeesociainsur.emphealinsurbeneinfo.MultiEmpWorkInfoDto;
import nts.uk.ctx.pr.shared.dom.socialinsurance.employeesociainsur.empbenepenpeninfor.WelfPenInsLossIf;
import nts.uk.ctx.pr.shared.dom.socialinsurance.employeesociainsur.empbenepenpeninfor.welfPenInsLossIfRepository;
import nts.uk.ctx.pr.shared.dom.socialinsurance.employeesociainsur.emphealinsurbeneinfo.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

@Stateless
public class LossInfoFinder {

    @Inject
    private welfPenInsLossIfRepository finderWelPen;

    @Inject
    private HealthInsLossInfoRepository finderHealth;

    @Inject
    private MultiEmpWorkInfoRepository finderMultiWork;

    @Inject
    private EmpBasicPenNumInforRepository finderBacsicPen;

    public LossInfoDto getLossInfoById(String empId){

        Optional<WelfPenInsLossIf> domainWelPen = finderWelPen.getWelfPenLossInfoById(empId);
        Optional<HealthInsLossInfo> domainHealth = finderHealth.getHealthInsLossInfoById(empId);
        Optional<MultiEmpWorkInfo> domainMultiWork = finderMultiWork.getMultiEmpWorkInfoById(empId);
        Optional<EmpBasicPenNumInfor> domainBasicPen = finderBacsicPen.getEmpBasicPenNumInforById(empId);

        if(domainWelPen.isPresent() && domainHealth.isPresent() && domainBasicPen.isPresent() && domainMultiWork.isPresent()){
            return new LossInfoDto(
                    HealthInLossInfoDto.fromDomain(domainHealth.get()),
                    WelfPenInsLossIfDto.fromDomain(domainWelPen.get()),
                    EmpBasicPenNumInforDto.fromDomain(domainBasicPen.get()),
                    MultiEmpWorkInfoDto.fromDomain(domainMultiWork.get()),
                    1);
        }
        return null;
    }

}
