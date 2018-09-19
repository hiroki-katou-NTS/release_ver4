package nts.uk.ctx.core.app.find.socialinsurance.contributionrate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.core.app.find.socialinsurance.contributionrate.dto.ContributionByGradeDto;
import nts.uk.ctx.core.app.find.socialinsurance.contributionrate.dto.ContributionRateDto;
import nts.uk.ctx.core.app.find.socialinsurance.contributionrate.dto.ContributionRateHistoryDto;
import nts.uk.ctx.core.app.find.socialinsurance.contributionrate.dto.SocialInsuranceOfficeDto;
import nts.uk.ctx.core.dom.socialinsurance.contribution.ContributionByGrade;
import nts.uk.ctx.core.dom.socialinsurance.contribution.ContributionRate;
import nts.uk.ctx.core.dom.socialinsurance.contribution.ContributionRateHistory;
import nts.uk.ctx.core.dom.socialinsurance.contribution.ContributionRateHistoryRepository;
import nts.uk.ctx.core.dom.socialinsurance.socialinsuranceoffice.SocialInsuranceOffice;
import nts.uk.ctx.core.dom.socialinsurance.socialinsuranceoffice.SocialInsuranceOfficeRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ContributionRateFinder {
	@Inject
	private ContributionRateHistoryRepository contributionRateHistoryRepository;

	@Inject
	private SocialInsuranceOfficeRepository socialInsuranceOfficeRepository;

	public ContributionRateDto findContributionRateByHistoryID(String historyId) {
		List<ContributionByGrade> contributionByGrade = contributionRateHistoryRepository
				.getContributionByGradeByHistoryId(historyId);
		Optional<ContributionRate> contributionRate = contributionRateHistoryRepository
				.getContributionRateByHistoryId(historyId);
		
		if (contributionRate.isPresent()) {
			List<ContributionByGradeDto> contributionByGradeDto = new ArrayList<>();
			contributionByGradeDto = contributionByGrade.stream()
					.map(x -> new ContributionByGradeDto(x.getWelfarePensionGrade(), x.getChildCareContribution().v()))
					.collect(Collectors.toList());

			ContributionRateDto contributionRateDto = new ContributionRateDto(historyId,
					contributionRate.get().getChildContributionRatio().v(),
					contributionRate.get().getAutomaticCalculationCls().value, contributionByGradeDto);
			return contributionRateDto;
		}
		return null;
	}

	public List<SocialInsuranceOfficeDto> findOfficeByCompanyId() {
		List<SocialInsuranceOfficeDto> socialInsuranceDtoList = new ArrayList<>();
		List<SocialInsuranceOffice> socialInsuranceOfficeList = socialInsuranceOfficeRepository
				.findByCid(AppContexts.user().companyId());
		socialInsuranceOfficeList.forEach(office -> {
			Optional<ContributionRateHistory> contributionRateHistory = contributionRateHistoryRepository
					.getContributionRateHistoryByOfficeCode(office.getCode().v());
			socialInsuranceDtoList.add(new SocialInsuranceOfficeDto(office.getCode().v(), office.getName().v(),
					ContributionRateHistoryDto.fromDomainToDto(contributionRateHistory, office.getCode().v())));
		});
		return socialInsuranceDtoList;
	}
}
