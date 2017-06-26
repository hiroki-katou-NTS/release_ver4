/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.insurance.salary;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
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
    
    /** The insu avgearn repo. */
    @Inject
    private HealthInsuranceAvgearnRepository insuAvgearnRepo;
    
    /** The pens avgearn repo. */
    @Inject
    private PensionAvgearnRepository pensAvgearnRepo;

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
        List<HealthInsuranceAvgearn> healInsuAvgearns = insuAvgearnRepo.findByOfficeCodes(companyCode, officeCodes);
        if (healInsuAvgearns.isEmpty()) {
            throw new BusinessException(new RawErrorMessage("対象データがありません。"));
        }
        return healInsuAvgearns;
    }
    
    /**
     * Find pens avgearn by office.
     *
     * @param officeCodes the office codes
     * @return the list
     */
    public List<PensionAvgearn> findPensAvgearnByOffice(List<String> officeCodes) {
        String companyCode = AppContexts.user().companyCode();
        List<PensionAvgearn> pensionAvgearns = pensAvgearnRepo.findbyOfficeCodes(companyCode, officeCodes);
        if (pensionAvgearns.isEmpty()) {
            throw new BusinessException(new RawErrorMessage("対象データがありません。"));
        }
        return pensionAvgearns;
    }
}
