package nts.uk.file.pr.app.export.retirementpayment;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.uk.file.pr.app.export.retirementpayment.data.CompanyMasterDto;
import nts.uk.file.pr.app.export.retirementpayment.data.PersonalBasicDto;
import nts.uk.file.pr.app.export.retirementpayment.data.RetirePayItemDto;
import nts.uk.file.pr.app.export.retirementpayment.data.RetirementPaymentDto;
import nts.uk.file.pr.app.export.retirementpayment.data.RetirementPaymentReportData;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class RetirementPaymentExportService extends ExportService<RetirementPaymentQuery> {

	@Inject
	private RetirementPaymentRepository retirementPaymentRepository;

	@Inject
	private RetirementPaymentReportGenerator retirementPaymentReportGenerator;

	@Override
	protected void handle(ExportServiceContext<RetirementPaymentQuery> context) {
		List<String> lstPersonId = context.getQuery().getLstPersonId();
		String companyCode = AppContexts.user().companyCode();
		List<RetirementPaymentReportData> dataSource = new ArrayList<>();
		List<RetirePayItemDto> lstRetirePayItemDto = this.retirementPaymentRepository.getListRetirePayItem(companyCode);
		List<RetirementPaymentDto> lstRetirementPaymentDto = this.retirementPaymentRepository.getRetirementPayment(
				companyCode, lstPersonId, context.getQuery().getStartDate(), context.getQuery().getEndDate());
		// check empty list person
		if (lstPersonId.size() < 1) {
			throw new BusinessException(new RawErrorMessage("社員選択が選択されていません。"));
		}
		// check if missing the target data
		if (lstRetirementPaymentDto.size() < lstPersonId.size()) {
			throw new BusinessException(new RawErrorMessage("対象データがありません。"));
		}
		// validate range date
		if (context.getQuery().getStartDate().after(context.getQuery().getEndDate())) {
			throw new BusinessException(new RawErrorMessage("範囲の指定が正しくありません。"));
		}
		// build report datasource
		for (String personId : lstPersonId) {
			RetirementPaymentReportData retirementPaymentReportData = new RetirementPaymentReportData();
			// get dtos
			RetirementPaymentDto retirementPaymentDto = lstRetirementPaymentDto.stream()
					.filter(dto -> dto.getPersonId().equals(personId)).findFirst().orElse(null);
			PersonalBasicDto personalBasicDto = new PersonalBasicDto("従業員名 " + personId.charAt(35), "2005年 3月 1日",
					"2017年 12月 31日", "自己都合（転職）", "0年0ヶ 月", "0年0ヶ 月");
			CompanyMasterDto companyMasterDto = new CompanyMasterDto("日通システム株式会社", "愛知県名古屋市中区栄", "ナディアパークビジネスセンタービル９F");
			// set dtos to report data
			retirementPaymentReportData.setRetirementPaymentDto(retirementPaymentDto);
			retirementPaymentReportData.setCompanyMasterDto(companyMasterDto);
			retirementPaymentReportData.setPersonalBasicDto(personalBasicDto);
			// add to list
			dataSource.add(retirementPaymentReportData);
		}
		this.retirementPaymentReportGenerator.generate(context.getGeneratorContext(), dataSource, lstRetirePayItemDto);
	}

}
