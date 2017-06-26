/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.pr.app.export.insurance.salary;

import java.util.List;

import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearn;
import nts.uk.file.pr.app.export.insurance.data.SocialInsuMLayoutReportData;
import nts.uk.file.pr.app.export.insurance.data.SocialInsuReportData;

/**
 * The Interface SalarySocialInsuranceRepository.
 */
public interface SocialInsuRepository {

    
    /**
     * Checks if is available data.
     *
     * @param companyCode the company code
     * @param loginPersonId the login person id
     * @param salaryQuery the salary query
     * @return true, if is available data
     */
    boolean isAvailableData(String companyCode, String loginPersonId, SocialInsuQuery salaryQuery);
    
    /**
     * Find report data.
     *
     * @param companyCode the company code
     * @param loginPersonId the login person id
     * @param salaryQuery the salary query
     * @param healInsuAvgearns the heal insu avgearns
     * @param pensionAvgearns the pension avgearns
     * @return the list
     */
    List<SocialInsuReportData> findReportData(String companyCode, String loginPersonId,
            SocialInsuQuery salaryQuery, List<HealthInsuranceAvgearn> healInsuAvgearns,
            List<PensionAvgearn> pensionAvgearns);
    
    /**
     * Find report M layout.
     *
     * @param companyCode the company code
     * @param loginPid the login pid
     * @param salaryQuery the salary query
     * @param healInsuAvgearns the heal insu avgearns
     * @param pensionAvgearns the pension avgearns
     * @return the social insu M layout report data
     */
    SocialInsuMLayoutReportData findReportMLayout(String companyCode, String loginPid,
            SocialInsuQuery salaryQuery, List<HealthInsuranceAvgearn> healInsuAvgearns,
            List<PensionAvgearn> pensionAvgearns);
}
