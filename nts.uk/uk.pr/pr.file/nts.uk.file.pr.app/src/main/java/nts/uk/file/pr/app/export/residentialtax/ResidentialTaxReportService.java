package nts.uk.file.pr.app.export.residentialtax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.core.dom.enums.CategoryAtr;
import nts.uk.ctx.pr.core.dom.enums.PayBonusAtr;
import nts.uk.file.pr.app.export.residentialtax.data.CompanyDto;
import nts.uk.file.pr.app.export.residentialtax.data.PaymentDetailDto;
import nts.uk.file.pr.app.export.residentialtax.data.PersonResitaxDto;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentTaxReportData;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentialTaxDto;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentialTaxSlipDto;
import nts.uk.file.pr.app.export.residentialtax.data.RetirementPaymentDto;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.japanese.JapaneseErasProvider;

@Stateless
public class ResidentialTaxReportService extends ExportService<ResidentialTaxQuery> {
	@Inject
	private ResidentialTaxGenerator generate;
	@Inject
	private ResidentialTaxReportRepository residentialTaxRepo;
	
	@Inject
	private JapaneseErasProvider eraProvider;

	@Override
	protected void handle(ExportServiceContext<ResidentialTaxQuery> context) {

		String companyCode = AppContexts.user().companyCode();
		int year;
		Map<String, Double> totalSalaryPaymentAmount = new HashMap<>();
		Map<String, Double> totalDeliveryAmountRetirement = new HashMap<>();
		Map<String, Double> totalActualRecieveMnyMap = new HashMap<>();
		Map<String, Integer> deliveryNumber = new HashMap<>();

		// get query
		ResidentialTaxQuery query = context.getQuery();
		
		String[]  yearMonth = query.getYearMonth().split("/");
		year = Integer.parseInt(yearMonth[0]);
		int yearM = Integer.parseInt(yearMonth[0] + yearMonth[1]);

		if (CollectionUtil.isEmpty(query.getResidentTaxCodeList())) {
			throw new BusinessException(new RawErrorMessage("データがありません。"));//ERO１０
		}
		
		// get residential tax
		List<ResidentialTaxDto> residentTaxList = residentialTaxRepo.findResidentTax(companyCode,
				query.getResidentTaxCodeList());
		CompanyDto company = null;
		if (query.getCompanyLogin() == 1) {
			// get company SEL_3
			company = residentialTaxRepo.findCompany(companyCode);
		} else {
			// get QCPMT_REGAL_DOC_COM.SEL2
			company = residentialTaxRepo.findRegalCompany(companyCode, query.getRegalDocCompanyCode());
		}

		// QTXMT_RESIDENTIAL_TAXSLIP.SEL_2
		List<ResidentialTaxSlipDto> residentialTaxSlipDto = residentialTaxRepo.findResidentialTaxSlip(companyCode,
				yearM, query.getResidentTaxCodeList());
		if (CollectionUtil.isEmpty(residentialTaxSlipDto)) {
			throw new BusinessException(new RawErrorMessage("データがありません。"));//ERO１０
		}

		// PPRMT_PERSON_RESITAX.SEL_1
		List<PersonResitaxDto> personResidentTaxList = residentialTaxRepo.findPersonResidentTax(companyCode, year,
				query.getResidentTaxCodeList());
		
		if (CollectionUtil.isEmpty(personResidentTaxList)) {
			throw new BusinessException(new RawErrorMessage("データがありません。"));//ERO１０
		}

		Map<String, List<PersonResitaxDto>> personResidentTaxListMap = personResidentTaxList.stream()
				.collect(Collectors.groupingBy(PersonResitaxDto::getResidenceCode, Collectors.toList()));

		//String processingYearMonth = String.valueOf(query.getProcessingYearMonth());
		String[]  processingYearMonth = query.getProcessingYearMonth().split("/");
		int processingYear = Integer.valueOf(Integer.parseInt(processingYearMonth[0]));
		int processingMonth = Integer.valueOf(Integer.parseInt(processingYearMonth[1]));
		int processingYM = Integer.parseInt(processingYearMonth[0] + processingYearMonth[1]);
		//20170311
		GeneralDate baseRangeStartYearMonth = GeneralDate.ymd(processingYear, processingMonth, 11);
		for (ResidentialTaxSlipDto residentialTax : residentialTaxSlipDto) {

			List<PersonResitaxDto> personResitaxList = personResidentTaxListMap.get(residentialTax.getResiTaxCode());
			if (CollectionUtil.isEmpty(personResitaxList)) {
				continue;
			}
			
			List<String> personIdList = personResitaxList.stream().map(x -> x.getPersonID())
					.collect(Collectors.toList());

			List<PaymentDetailDto> paymentDetailList = residentialTaxRepo.findPaymentDetail(companyCode, personIdList,
					PayBonusAtr.SALARY, processingYM, CategoryAtr.DEDUCTION, "F108");

			double totalValue = paymentDetailList.stream().mapToDouble(x -> x.getValue().doubleValue()).sum();
			totalSalaryPaymentAmount.put(residentialTax.getResiTaxCode(),
					totalValue + residentialTax.getTaxPayrollMny().doubleValue());
			

			// RETIREMENT_PAYMENT(退職金明細データ).SEL-2
			List<RetirementPaymentDto> retirementPaymentList = residentialTaxRepo.findRetirementPaymentList(companyCode,
					personIdList, baseRangeStartYearMonth, query.getEndDate());
			double totalCityTaxMny = retirementPaymentList.stream().mapToInt(x -> x.getCityTaxMny())
					.sum();
			double totalPrefectureTaxMny = retirementPaymentList.stream()
					.mapToInt(x -> x.getPrefectureTaxMny()).sum();
			totalDeliveryAmountRetirement.put(residentialTax.getResiTaxCode(),
					totalPrefectureTaxMny + totalCityTaxMny + residentialTax.getTaxRetirementMny().doubleValue());
			double totalActualRecieveMny = retirementPaymentList.stream()
					.mapToInt(x -> x.getActualRecieveMny()).sum();
			totalActualRecieveMnyMap.put(residentialTax.getResiTaxCode(), totalActualRecieveMny);
			deliveryNumber.put(residentialTax.getResiTaxCode(),residentialTax.getHeadCount().intValue());

		}
		

		// return
		List<ResidentTaxReportData> reportDataList = new ArrayList<ResidentTaxReportData>();

		Map<String, ResidentialTaxSlipDto> residentialTaxSlipMap = residentialTaxSlipDto.stream()
				.collect(Collectors.toMap(ResidentialTaxSlipDto::getResiTaxCode, y -> y));


		for (ResidentialTaxDto residentialTax : residentTaxList) {
			ResidentialTaxSlipDto residentialTaxSlip = residentialTaxSlipMap.get(residentialTax.getResidenceTaxCode());
			if (residentialTaxSlip == null) {
				continue;
			}
			
			List<PersonResitaxDto> personResitaxList = personResidentTaxListMap.get(residentialTax.getResidenceTaxCode());
			if (CollectionUtil.isEmpty(personResitaxList)) {
				continue;
			}
			
			Double salaryPaymentAmount = totalSalaryPaymentAmount.get(residentialTax.getResidenceTaxCode());
			Double deliveryAmountRetirement = totalDeliveryAmountRetirement.get(residentialTax.getResidenceTaxCode());
			
			Double totalAmountTobePaid = (salaryPaymentAmount == null ? 0 : salaryPaymentAmount)
					+ (deliveryAmountRetirement == null ? 0 : deliveryAmountRetirement);
			Integer deliveryNumberString = deliveryNumber.get(residentialTax.getResidenceTaxCode());
			Double actualRecieveMnyMap = totalActualRecieveMnyMap.get(residentialTax.getResidenceTaxCode());
			ResidentTaxReportData reportData = new ResidentTaxReportData();
			// DBD_001 residenceTaxCode
			reportData.setResidenceTaxCode(residentialTax.getResidenceTaxCode());
			// DBD_002 resiTaxAutonomy
			reportData.setResiTaxAutonomy(residentialTax.getResiTaxAutonomy());
			// DBD_003 companyAccountNo
			reportData.setCompanyAccountNo(residentialTax.getCompanyAccountNo());
			// DBD_004 registeredName
			reportData.setRegisteredName(residentialTax.getRegisteredName());
			// DBD_005 companySpecifiedNo
			reportData.setCompanySpecifiedNo(residentialTax.getCompanySpecifiedNo());
			// DBD_006 salaryPaymentAmount
			reportData.setSalaryPaymentAmount(salaryPaymentAmount);
			// DBD_007 deliveryAmountRetirement
			reportData.setDeliveryAmountRetirement(deliveryAmountRetirement);
			// DBD_008 postal
			reportData.setPostal(company.getPostal());
			// DBD_009 address1
			reportData.setAddress1(company.getAddress1());
			// DBD_010 address2
			reportData.setAddress2(company.getAddress2());
			// DBD_011 companyName
			reportData.setCompanyName(company.getCompanyName());
			// DBD_012 cordinatePostOffice
			reportData.setCordinatePostOffice(residentialTax.getCordinatePostOffice());
			// DBD_013 cordinatePostalCode
			reportData.setCordinatePostalCode(residentialTax.getCordinatePostalCode());
			// DBD_014 deliveryNumber
			reportData.setDeliveryNumber((deliveryNumberString == null ? 0 : deliveryNumberString.toString()) +"人");
			// DBD_015 actualRecieveMny
			reportData.setActualRecieveMny(actualRecieveMnyMap);
			// DBD_016 cityTaxMny
			reportData.setCityTaxMny(residentialTaxSlip.getCityTaxMny().doubleValue());
			// DBD_017 prefectureTaxMny
			reportData.setPrefectureTaxMny(residentialTaxSlip.getPrefectureTaxMny().doubleValue());
			// DBD_018 taxOverdueMny
			reportData.setTaxOverdueMny(residentialTaxSlip.getTaxOverdueMny().doubleValue());
			// DBD_019 taxDemandChargeMny
			reportData.setTaxDemandChargeMny(residentialTaxSlip.getTaxDemandChargeMny().doubleValue());
			// DBD_020 filingDate
			String getDueDateJapan = this.eraProvider.toJapaneseDate(residentialTaxSlip.getDueDate()).toString();
			reportData.setFilingDate(getDueDateJapan);
			// CTR_001 designatedYM
			reportData.setDesignatedYM(query.getProcessingYearMonthJapan()+"分");
			// CTR_002 totalAmountTobePaid
			reportData.setTotalAmountTobePaid(totalAmountTobePaid);
			// CTR_003 dueDate
			reportData.setDueDate(query.getEndDateJapan());

			reportDataList.add(reportData);
		}

		this.generate.generate(context.getGeneratorContext(), reportDataList);
	}

}
