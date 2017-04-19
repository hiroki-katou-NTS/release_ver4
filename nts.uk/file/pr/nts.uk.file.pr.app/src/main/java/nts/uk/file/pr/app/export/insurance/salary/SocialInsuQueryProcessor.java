/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
/**
 * 
 */
package nts.uk.file.pr.app.export.insurance.salary;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnRepository;
import nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSetting;
import nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingRepository;
import nts.uk.file.pr.app.export.insurance.data.ChecklistPrintSettingDto;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SocialInsuQueryProcessor.
 */

@Stateless
public class SocialInsuQueryProcessor {
    
    /** The checklist print setting repository. */
    @Inject
    private ChecklistPrintSettingRepository printSettingRepository;
    
    /** The repository insu avgearn. */
    @Inject
    private HealthInsuranceAvgearnRepository repositoryInsuAvgearn;
    
    /** The repository pens avgearn. */
    @Inject
    private PensionAvgearnRepository repositoryPensAvgearn;

    /**
     * Find configure output setting.
     *
     * @return the checklist print setting dto
     */
    public ChecklistPrintSettingDto findConfigureOutputSetting() {
      String companyCode = AppContexts.user().companyCode();
        Optional<ChecklistPrintSetting> printSetting = printSettingRepository.findByCompanyCode(companyCode);
        ChecklistPrintSettingDto dto = new ChecklistPrintSettingDto();
        if (printSetting.isPresent()) {
            ChecklistPrintSetting domain = printSetting.get();
            dto.setShowCategoryInsuranceItem(domain.getShowCategoryInsuranceItem());
            dto.setShowDetail(domain.getShowDetail());
            dto.setShowOffice(domain.getShowOffice());
            dto.setShowTotal(domain.getShowTotal());
            dto.setShowDeliveryNoticeAmount(domain.getShowDeliveryNoticeAmount());
        } else {
            dto.setShowCategoryInsuranceItem(true);
            dto.setShowDetail(true);
            dto.setShowOffice(true);
            dto.setShowTotal(true);
            dto.setShowDeliveryNoticeAmount(true);
        }
      return dto;
    }
    
    /**
     * Find heal insu avgearn by office.
     *
     * @param officeCodes the office codes
     * @return the list
     */
    public List<HealthInsuranceAvgearn> findHealInsuAvgearnByOffice(List<String> officeCodes) {
        String companyCode = AppContexts.user().companyCode();
        List<HealthInsuranceAvgearn> healInsuAngearns = repositoryInsuAvgearn.findByOffice(companyCode, officeCodes);
        if (healInsuAngearns.isEmpty()) {
            throw new BusinessException("対象データがありません。");
        }
        return healInsuAngearns;
    }
    
    /**
     * Find pens avgearn by office.
     *
     * @param officeCodes the office codes
     * @return the list
     */
    public List<PensionAvgearn> findPensAvgearnByOffice(List<String> officeCodes) {
        String companyCode = AppContexts.user().companyCode();
        List<PensionAvgearn> pensionAvgearns = repositoryPensAvgearn.findByOffice(companyCode, officeCodes);
        if (pensionAvgearns.isEmpty()) {
            throw new BusinessException("対象データがありません。");
        }
        return pensionAvgearns;
    }
}
