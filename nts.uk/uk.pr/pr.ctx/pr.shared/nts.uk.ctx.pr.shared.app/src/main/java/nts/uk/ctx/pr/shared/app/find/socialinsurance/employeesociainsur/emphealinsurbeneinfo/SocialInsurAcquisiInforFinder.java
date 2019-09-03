package nts.uk.ctx.pr.shared.app.find.socialinsurance.employeesociainsur.emphealinsurbeneinfo;

import nts.uk.ctx.pr.shared.dom.socialinsurance.employeesociainsur.emphealinsurbeneinfo.SocialInsurAcquisiInfor;
import nts.uk.ctx.pr.shared.dom.socialinsurance.employeesociainsur.emphealinsurbeneinfo.SocialInsurAcquisiInforRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;


@Stateless
/**
* 社会保険取得時情報: Finder
*/
public class SocialInsurAcquisiInforFinder
{

    @Inject
    private SocialInsurAcquisiInforRepository finder;

    public Optional<SocialInsurAcquisiInfor> getSocialInsurAcquisiInforById(String cid, String employeeId){
        return finder.getSocialInsurAcquisiInforById(cid,employeeId);
    }

}
