/**
 * 
 */
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
import nts.uk.file.pr.app.export.residentialtax.data.ResidentTaxReport;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentTaxTextCommonData;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentTaxTextData;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentialTaxDto;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentialTaxSlipDto;
import nts.uk.file.pr.app.export.residentialtax.data.RetirementPaymentDto;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.japanese.JapaneseErasProvider;

/**
 * Outputting Payment Data (納付データの出力)
 * 
 * @author sonnh
 *
 */
@Stateless
public class OutputPaymentDataReportService extends ExportService<ResidentialTaxQuery> {
	@Inject
	private OutputPaymentDataGenerator generator;
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
		Map<String, Integer> personnelMap = new HashMap<>();
		Map<String, Double> totalCityTaxMnyMap = new HashMap<>();
		Map<String, Double> totalPrefectureTaxMnyMap = new HashMap<>();
		Map<String, Long> numberRetireesMap = new HashMap<>();
		// get query
		ResidentialTaxQuery query = context.getQuery();

		String[] yearMonth = query.getYearMonth().split("/");
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
			throw new BusinessException(new RawErrorMessage("データがありません。"));// ERO１０
		}

		// PPRMT_PERSON_RESITAX.SEL_1
		List<PersonResitaxDto> personResidentTaxList = residentialTaxRepo.findPersonResidentTax(companyCode, year,
				query.getResidentTaxCodeList());

		if (CollectionUtil.isEmpty(personResidentTaxList)) {
			throw new BusinessException(new RawErrorMessage("データがありません。"));// ERO１０
		}

		Map<String, List<PersonResitaxDto>> personResidentTaxListMap = personResidentTaxList.stream()
				.collect(Collectors.groupingBy(PersonResitaxDto::getResidenceCode, Collectors.toList()));

		// String processingYearMonth =
		// String.valueOf(query.getProcessingYearMonth());
		String[] processingYearMonth = query.getProcessingYearMonth().split("/");
		int processingYear = Integer.valueOf(Integer.parseInt(processingYearMonth[0]));
		int processingMonth = Integer.valueOf(Integer.parseInt(processingYearMonth[1]));
		int processingYM = Integer.parseInt(processingYearMonth[0] + processingYearMonth[1]);
		// 20170311
		GeneralDate baseRangeStartYearMonth = GeneralDate.ymd(processingYear, processingMonth, 11);
		for (ResidentialTaxSlipDto residentialTax : residentialTaxSlipDto) {
			long numberRetirees;
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
			if(residentialTax.getTaxRetirementMny().doubleValue() != 0){
                  numberRetirees = retirementPaymentList.size();
			}else {
				  long numberPeopleZero = retirementPaymentList.stream()
	                     .filter(x -> x.getCityTaxMny() > 0  && x.getPrefectureTaxMny()> 0)
	                     .count();
				  numberRetirees = retirementPaymentList.size() - numberPeopleZero;
			}
			
			double totalCityTaxMny = retirementPaymentList.stream().mapToInt(x -> x.getCityTaxMny())
					.sum();
			double totalPrefectureTaxMny = retirementPaymentList.stream()
					.mapToInt(x -> x.getPrefectureTaxMny()).sum();
			
			totalCityTaxMnyMap.put(residentialTax.getResiTaxCode(), totalCityTaxMny);
			
			totalPrefectureTaxMnyMap.put(residentialTax.getResiTaxCode(), totalPrefectureTaxMny);
			
			totalDeliveryAmountRetirement.put(residentialTax.getResiTaxCode(),
					totalPrefectureTaxMny + totalCityTaxMny + residentialTax.getTaxRetirementMny().doubleValue());
			
			double totalActualRecieveMny = retirementPaymentList.stream()
					.mapToInt(x -> x.getActualRecieveMny()).sum();
			
			numberRetireesMap.put(residentialTax.getResiTaxCode(), numberRetirees);
			totalActualRecieveMnyMap.put(residentialTax.getResiTaxCode(), totalActualRecieveMny);
			deliveryNumber.put(residentialTax.getResiTaxCode(), paymentDetailList.size());
			personnelMap.put(residentialTax.getResiTaxCode(), retirementPaymentList.size());

		}

		ResidentTaxReport report = new ResidentTaxReport();

		List<ResidentTaxTextData> dataList = new ArrayList<ResidentTaxTextData>();
		for (ResidentialTaxDto residentialTax : residentTaxList) {
			
			Double salaryPaymentAmount = totalSalaryPaymentAmount.get(residentialTax.getResidenceTaxCode());
			if (salaryPaymentAmount  == null) {
				continue;
			}
			Double deliveryAmountRetirement = totalDeliveryAmountRetirement.get(residentialTax.getResidenceTaxCode());
			Double totalCityTaxMny = totalCityTaxMnyMap.get(residentialTax.getResidenceTaxCode());
			Double totalPrefectureTaxMny = totalPrefectureTaxMnyMap.get(residentialTax.getResidenceTaxCode());
			String deliveryNumberString = deliveryNumber.get(residentialTax.getResidenceTaxCode()).toString();
			String personnelString = personnelMap.get(residentialTax.getResidenceTaxCode()).toString();
			Double actualRecieveMnyMap = totalActualRecieveMnyMap.get(residentialTax.getResidenceTaxCode());
			String numberRetireesString = numberRetireesMap.get(residentialTax.getResidenceTaxCode()).toString();
			ResidentTaxTextData data = new ResidentTaxTextData();
			data.setMunicipalCode(residentialTax.getResidenceTaxCode());
			data.setCityName(residentialTax.getResiTaxAutonomyKnName());
			data.setDesignatedNumber(residentialTax.getCompanySpecifiedNo());
			data.setNumberSalaries(deliveryNumberString);
			data.setSalaryAmount(salaryPaymentAmount);
			data.setNumberRetirees(numberRetireesString);
			data.setRetirementAmount(deliveryAmountRetirement);
			data.setTotalNumberSalaReti(deliveryNumberString);
			data.setTotalSalaryRetiAmount(salaryPaymentAmount + deliveryAmountRetirement);
			data.setPersonnel(personnelString);
			data.setPayment(actualRecieveMnyMap);
			data.setCityTownTax(totalCityTaxMny);
			data.setPrefecturalTax(totalPrefectureTaxMny);
			dataList.add(data);
		}

		ResidentTaxTextCommonData common = new ResidentTaxTextCommonData();

		Integer totalNumSalaryMi = deliveryNumber.entrySet().stream().mapToInt(x -> x.getValue().intValue()).sum();
		Double totalSalaryAmount = totalSalaryPaymentAmount.entrySet().stream()
				.mapToDouble(x -> x.getValue().doubleValue())
				.sum();
		Integer totalNumberRetirees = numberRetireesMap.entrySet().stream().mapToInt(x -> x.getValue().intValue()).sum();
		Double totalRetirementAmount = totalDeliveryAmountRetirement.entrySet().stream()
				.mapToDouble(x -> x.getValue().doubleValue())
				.sum();
		Integer totalNumberSala = totalNumSalaryMi + totalNumberRetirees;
		Double totalSalaRetiAmount = totalSalaryAmount + totalRetirementAmount;
		common.setTypeCode(query.getTypeCode());
		common.setClientCode(query.getClientCode());
		common.setDesBranchNumber(query.getDestinationBranchNumber());
		//Convert time Japan
		Integer yearMonthJapan = this.eraProvider.toJapaneseDate(query.getEndDate()).toYearMonthInt();
		common.setPaymentDueDate(yearMonthJapan.toString());
		common.setPaymentMonth(yearMonthJapan.toString().substring(0, 4));
		//
		common.setClientName(company.getCompanyName());
		common.setClientAddress(company.getAddress1() + company.getAddress2());
		common.setTotalNumSalaryMi(totalNumSalaryMi.toString());
		common.setTotalSalaryAmount(totalSalaryAmount);
		common.setTotalNumberRetirees(totalNumberRetirees.toString());
		common.setTotalRetirementAmount(totalRetirementAmount);
		common.setTotalNumberSala(totalNumberSala.toString());
		common.setTotalSalaRetiAmount(totalSalaRetiAmount);
		report.setCommon(common);
		report.setData(dataList);

		this.generator.generate(context.getGeneratorContext(), report);
	}
	
}
