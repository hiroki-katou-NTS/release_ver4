/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.function.app.find.dailyworkschedule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.uk.ctx.at.function.app.command.dailyworkschedule.OutputItemDailyWorkScheduleCopyCommand;
import nts.uk.ctx.at.function.dom.attendancetype.AttendanceType;
import nts.uk.ctx.at.function.dom.attendancetype.AttendanceTypeRepository;
import nts.uk.ctx.at.function.dom.dailyattendanceitem.DailyAttendanceItem;
import nts.uk.ctx.at.function.dom.dailyattendanceitem.repository.DailyAttendanceItemNameDomainService;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.AuthorityDailyPerformanceFormat;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.AuthorityFomatDaily;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.primitivevalue.DailyPerformanceFormatCode;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.repository.AuthorityDailyPerformanceFormatRepository;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.repository.AuthorityFormatDailyRepository;
import nts.uk.ctx.at.function.dom.dailyworkschedule.AttendanceItemsDisplay;
import nts.uk.ctx.at.function.dom.dailyworkschedule.FormatPerformanceAdapter;
import nts.uk.ctx.at.function.dom.dailyworkschedule.FormatPerformanceImport;
import nts.uk.ctx.at.function.dom.dailyworkschedule.FreeSettingOfOutputItemForDailyWorkSchedule;
import nts.uk.ctx.at.function.dom.dailyworkschedule.FreeSettingOfOutputItemRepository;
import nts.uk.ctx.at.function.dom.dailyworkschedule.ItemSelectionType;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemDailyWorkSchedule;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemDailyWorkScheduleRepository;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingCode;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputStandardSettingOfDailyWorkSchedule;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputStandardSettingRepository;
import nts.uk.ctx.at.function.dom.dailyworkschedule.PrintRemarksContent;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.BusinessType;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.BusinessTypeFormatDaily;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.repository.BusinessTypeFormatDailyRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.repository.BusinessTypesRepository;
//import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.service.CompanyDailyItemService;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class OutputItemDailyWorkScheduleFinder.
 * @author HoangDD
 */
@Stateless
public class OutputItemDailyWorkScheduleFinder {
	
	/** The output item daily work schedule repository. */
	@Inject
	private OutputItemDailyWorkScheduleRepository outputItemDailyWorkScheduleRepository;
	
	@Inject
	private AttendanceTypeRepository attendanceTypeRepository;
	
//	@Inject
//	private OptionalItemRepository optionalItemRepository;
	
	@Inject
	private BusinessTypesRepository businessTypesRepository;
	
	@Inject
	private AuthorityDailyPerformanceFormatRepository authorityDailyPerformanceFormatRepository;
	
	@Inject
	private BusinessTypeFormatDailyRepository businessTypeFormatDailyRepository;
	
	@Inject
	private AuthorityFormatDailyRepository authorityFormatDailyRepository;
	
	@Inject
	private FormatPerformanceAdapter formatPerformanceAdapter;
	
	@Inject
	private DailyAttendanceItemNameDomainService dailyAttendanceItemNameDomainService;
	
	@Inject
	private CompanyDailyItemService companyDailyItemService;

	/** The output standard setting repository. */
	@Inject
	private OutputStandardSettingRepository outputStandardSettingRepository;
	
	/** The free setting of output item repository. */
	@Inject
	private FreeSettingOfOutputItemRepository freeSettingOfOutputItemRepository;
	
	// Input of algorithm when use enum ScreenUseAtr: 勤怠項目を利用する画面
	private static final int DAILY_WORK_SCHEDULE = 19;
	
	/** The Constant USE. */
	private static final int USE = 1;
	
	/** The Constant NOT_USE. */
	private static final int NOT_USE = 0;
	
	/** The Constant AUTHORITY. */
	// SettingUnitType.AUTHORITY.value
	private static final int AUTHORITY = 0;
	
	/** The Constant BUSINESS_TYPE. */
	// SettingUnitType.BUSINESS_TYPE.value
	private static final int BUSINESS_TYPE = 1;
	
	/** The Constant SHEET_NO_1. */
	private static final int SHEET_NO_1 = 1;
	
	private static final String AUTHORITY_DEFINE = "権限";
	private static final String BUSINESS_TYPE_DEFINE = "勤務種別";
	/**
	 * Find by cid.
	 *
	 * @return the map
	 */
	public Map<String, Object> findByCid(Optional<String> layoutId, int selectionType) {	
		String companyID = AppContexts.user().companyId();
		String employeeId = AppContexts.user().employeeId();

		Map<String, Object> mapDtoReturn = new HashMap<>();
		
		List<OutputItemDailyWorkSchedule> lstDomainModel;
		
		// Input．項目選択種類をチェック (Check the selection type of Input.item)
		// Type: 定型選択の場合
		if (selectionType == ItemSelectionType.STANDARD_SELECTION.value) {

			// 定型設定の出力項目を取得 (Get the output item for fix-form setup)
			Optional<OutputStandardSettingOfDailyWorkSchedule> standardSetting = this.outputStandardSettingRepository
					.getStandardSettingByCompanyId(companyID);
			lstDomainModel = standardSetting.isPresent() ? standardSetting.get().getOutputItems() : new ArrayList<>();
		
		// Type: 自由設定の場合
		} else {
			
			// 社員IDから自由設定の出力項目を取得 (Get the output item for free setup from Company ID)
			Optional<FreeSettingOfOutputItemForDailyWorkSchedule> freeSetting = this.freeSettingOfOutputItemRepository
					.getFreeSettingByCompanyAndEmployee(companyID, employeeId);
			lstDomainModel = freeSetting.isPresent() ? freeSetting.get().getOutputItemDailyWorkSchedules() : new ArrayList<>();
		}
		
		// 取得したドメインモデル「日別勤務表の出力項目」からList<「出力項目設定>を作成 (Creat list [出力項目設定]from the
		// acquired domain model 「日別勤務表の出力項目」)
		mapDtoReturn.put("outputItemDailyWorkSchedule", lstDomainModel.stream()
				.map(domain -> {
					DataInforReturnDto dto = new DataInforReturnDto();
					dto.setCode(String.valueOf(domain.getItemCode().v()));
					dto.setName(domain.getItemName().v());
					dto.setLayoutId(domain.getOutputLayoutId());
					return dto;
				})
				.sorted(Comparator.comparing(DataInforReturnDto::getCode)).collect(Collectors.toList()));
		
		// 選択している項目情報を取得する(Get the selected information item)
		List<AttendanceType> lstAttendanceType = attendanceTypeRepository.getDailyAttendanceItem(companyID, layoutId, lstDomainModel);
		
		List<Integer> lstAttendanceID = lstAttendanceType.stream().map(domain -> domain.getAttendanceItemId()).collect(Collectors.toList());
 		
		List<DailyAttendanceItemDto> lstDailyAtdItemDto = companyDailyItemService.getDailyItems(companyID, Optional.empty(), lstAttendanceID, new ArrayList<>()).stream()
																.map(dto -> { 
																	DailyAttendanceItemDto dtoClientReturn = new DailyAttendanceItemDto();
																	dtoClientReturn.setCode(dto.getAttendanceItemDisplayNumber());
																	dtoClientReturn.setId(dto.getAttendanceItemId());
																	dtoClientReturn.setName(dto.getAttendanceItemName());
																	return dtoClientReturn;
																})
																.sorted(Comparator.comparing(DailyAttendanceItemDto::getCode))
																.collect(Collectors.toList()); 
		
		
		// ドメインモデル「日次の勤怠項目」をすべて取得する(Acquire all domain model "daily attendance items")
		// アルゴリズム「勤怠項目に対応する名称を生成する」
		if (!lstAttendanceID.isEmpty()) {
			mapDtoReturn.put("dailyAttendanceItem", lstDailyAtdItemDto);
		} else {
			mapDtoReturn.put("dailyAttendanceItem", Collections.emptyList());
		}		
		
		// find nothing
		return mapDtoReturn;
	}
	
	/**
	 * Gets the format daily performance.
	 *
	 * @return the format daily performance
	 */
	// algorithm for screen D: start screen
	public List<DataInforReturnDto> getFormatDailyPerformance() {
		String companyId = AppContexts.user().companyId();
		// Get domain 実績修正画面で利用するフォーマット from request list 402
		Optional<FormatPerformanceImport> optFormatPerformanceImport = formatPerformanceAdapter.getFormatPerformance(companyId);
		
		List<DataInforReturnDto> lstData;
		
		if (!optFormatPerformanceImport.isPresent()) {
			return new ArrayList<>();
		}
		switch (optFormatPerformanceImport.get().getSettingUnitType()) 
		{
			case AUTHORITY: // In case of authority
				// Get domain 会社の日別実績の修正のフォーマット
				List<AuthorityDailyPerformanceFormat> lstAuthorityDailyPerformanceFormat = authorityDailyPerformanceFormatRepository.getListCode(companyId);
				lstData = lstAuthorityDailyPerformanceFormat.stream()
							.map(obj -> {
								DataInforReturnDto dto = new DataInforReturnDto();
								dto.setCode(obj.getDailyPerformanceFormatCode().v());
								dto.setName(obj.getDailyPerformanceFormatName().v());
								return dto;
							}).collect(Collectors.toList());
				if (lstData.isEmpty()) {
					throw new BusinessException("Msg_1410", new String[]{AUTHORITY_DEFINE});
				} 
				return lstData;
			case BUSINESS_TYPE: // In case of work type
				// Get doamin 勤務種別日別実績の修正のフォーマット
				List<BusinessTypeFormatDaily> lstBusinessTypeFormatDaily = businessTypeFormatDailyRepository.getBusinessTypeFormatByCompanyId(companyId);
				Set<String> setBusinessTypeFormatDailyCode = lstBusinessTypeFormatDaily.stream()
															.map(domain -> domain.getBusinessTypeCode().v())
															.collect(Collectors.toSet());
				
				// Get domain ドメインモデル「勤務種別」を取得する
				// businessTypeCode get from doamin 勤務種別日別実績の修正のフォーマット
				List<BusinessType> lstBusinessType = businessTypesRepository.findAll(companyId);
	
				lstData = lstBusinessType.stream()
					.filter(domain -> setBusinessTypeFormatDailyCode.contains(domain.getBusinessTypeCode().v()))
					.map(domain -> {
						DataInforReturnDto dto = new DataInforReturnDto();
						dto.setCode(domain.getBusinessTypeCode().v());
						dto.setName(domain.getBusinessTypeName().v());
						return dto;
					})
				.collect(Collectors.toList());
				if (lstData.isEmpty()) {
					throw new BusinessException("Msg_1410", new String[]{BUSINESS_TYPE_DEFINE});
				}
				return lstData;
			default:
				return new ArrayList<>();
		}
	}
	
	// algorithm for screen D: copy
	public DataReturnDto executeCopy(String codeCopy, String codeSourceSerivce) {
		String companyId = AppContexts.user().companyId();
		
		// get domain 日別勤務表の出力項目
		Optional<OutputItemDailyWorkSchedule> optOutputItemDailyWorkSchedule = outputItemDailyWorkScheduleRepository.findByCidAndCode(companyId, new OutputItemSettingCode(codeCopy).v());
		
		if (optOutputItemDailyWorkSchedule.isPresent()) {
			throw new BusinessException("Msg_3");
		} else {
			DataReturnDto dataReturnDto = getDomConvertDailyWork(companyId, codeSourceSerivce);
			//List<DataInforReturnDto> lstData = getDomConvertDailyWork(companyId, codeSourceSerivce);
			if (dataReturnDto.getDataInforReturnDtos().isEmpty()) {
				throw new BusinessException("Msg_1411");
			} 
			return dataReturnDto;
		}
	}
	
	// アルゴリズム「日別勤務表用フォーマットをコンバートする」を実行する(Execute algorithm "Convert daily work table format")
	private DataReturnDto getDomConvertDailyWork(String companyId, String codeSourceSerivce) {
		// Get domain 実績修正画面で利用するフォーマット from request list 402
		Optional<FormatPerformanceImport> optFormatPerformanceImport = formatPerformanceAdapter.getFormatPerformance(companyId);
		
		DataReturnDto dataReturnDto = new DataReturnDto();
		List<DataInforReturnDto> lstDataReturn = new ArrayList<>();
		
		if (optFormatPerformanceImport.isPresent()) {
			switch (optFormatPerformanceImport.get().getSettingUnitType()) 
			{
				case AUTHORITY: // In case of authority
					// ドメインモデル「会社の日別実績の修正のフォーマット」を取得する (Acquire the domain model "format of company's daily performance correction")
					// 「日別実績の修正の表示項目」から表示項目を取得する (Acquire display items from "display items for correction of daily performance")
					List<AuthorityFomatDaily>  lstAuthorityFomatDaily = authorityFormatDailyRepository.getAuthorityFormatDailyDetail(companyId, new DailyPerformanceFormatCode(codeSourceSerivce), new BigDecimal(SHEET_NO_1));
					lstAuthorityFomatDaily.sort(Comparator.comparing(AuthorityFomatDaily::getDisplayOrder));
					lstDataReturn = lstAuthorityFomatDaily.stream()
															.map(domain -> {
																DataInforReturnDto dto = new DataInforReturnDto();
																dto.setId(domain.getAttendanceItemId());
																return dto;
															})
															.collect(Collectors.toList());
					break;
				case BUSINESS_TYPE:
					// ドメインモデル「勤務種別日別実績の修正のフォーマット」を取得する (Acquire the domain model "Format of working type daily performance correction)
					// 「日別実績の修正の表示項目」から表示項目を取得する (Acquire display items from "display items for correction of daily performance")
					List<BusinessTypeFormatDaily> lstBusinessTypeFormatDaily = businessTypeFormatDailyRepository.getBusinessTypeFormatDailyDetail(companyId, new BusinessTypeCode(codeSourceSerivce).v(), new BigDecimal(SHEET_NO_1));
					lstBusinessTypeFormatDaily.sort(Comparator.comparing(BusinessTypeFormatDaily::getOrder));
					lstDataReturn = lstBusinessTypeFormatDaily.stream()
															.map(domain -> {
																DataInforReturnDto dto = new DataInforReturnDto();
																dto.setId(domain.getAttendanceItemId());
																return dto;
															})
															.collect(Collectors.toList());
					break;
				default:
					break;
			}
		}
		
		// get list data was showed in kwr001
		List<AttendanceType> lstAttendanceType = attendanceTypeRepository.getItemByScreenUseAtr(companyId, DAILY_WORK_SCHEDULE);
		List<Integer> lstAttendanceID = lstAttendanceType.stream().map(domain -> domain.getAttendanceItemId()).collect(Collectors.toList());
		
		List<OutputItemDailyWorkScheduleCopyCommand> lstCommandCopy = companyDailyItemService.getDailyItems(companyId, Optional.empty(), lstAttendanceID, new ArrayList<>()).stream()
												.map(dto -> { 
													OutputItemDailyWorkScheduleCopyCommand dtoClientReturn = new OutputItemDailyWorkScheduleCopyCommand();
													dtoClientReturn.setCode(String.valueOf(dto.getAttendanceItemDisplayNumber()));
													dtoClientReturn.setId(dto.getAttendanceItemId());
													dtoClientReturn.setName(dto.getAttendanceItemName());
													return dtoClientReturn;
												}).collect(Collectors.toList());
		
		
		Map<Integer, String> mapIdName =  lstCommandCopy.stream()
				.collect(Collectors.toMap(OutputItemDailyWorkScheduleCopyCommand::getId, 
										  OutputItemDailyWorkScheduleCopyCommand::getName));
		// compare data return from kdw008 to kwr001
		// if item of kwr008 exist in kwr001, it will be save
		int sizeData = lstDataReturn.size();
		lstDataReturn = lstDataReturn.stream()
				.filter(domain -> mapIdName.containsKey(domain.getId()))
				.map(domain -> {
					domain.setName(mapIdName.get(domain.getId()));
					return domain;
				}).collect(Collectors.toList());
		
		if(sizeData !=lstDataReturn.size()){
			List<String> lstMsgErr = new ArrayList<String>();
			lstMsgErr.add("Msg_1476");
			dataReturnDto.setMsgErr(lstMsgErr);
		}
		// 1Sheet目の表示項目を返り値とする (Coi hạng mục hiển thj của sheet đầu tiên là giá trị trả về)
		
		dataReturnDto.setDataInforReturnDtos(lstDataReturn);
		
		
		if (lstDataReturn.size() <= 48) {
			return dataReturnDto;
		}
		else { // 1Sheet目の表示項目の先頭48項目までを返り値とする(Lấy 48 hạng mục đầu trong sheet đầu tiên làm giá trị trả về)
			List<DataInforReturnDto> lstDto = dataReturnDto.getDataInforReturnDtos().stream().limit(48)
					.collect(Collectors.toList());
			dataReturnDto.setDataInforReturnDtos(lstDto);
			return dataReturnDto;
		}
	}
	
	/**
	 * To dto timeitem tobe display.
	 *
	 * @param lstDomainObject the lst domain object
	 * @return the list
	 */
	private List<TimeitemTobeDisplayDto> toDtoTimeitemTobeDisplay(List<AttendanceItemsDisplay> lstDomainObject, Map<Integer, String> mapCodeManeAttendance) {
		return lstDomainObject.stream()
									.map(domain -> {
										TimeitemTobeDisplayDto dto = new TimeitemTobeDisplayDto();
										dto.setAttendanceDisplay(domain.getAttendanceDisplay());
										dto.setOrderNo(domain.getOrderNo());
										dto.setAttendanceName(mapCodeManeAttendance.get(domain.getAttendanceDisplay()));
										return dto;
									})
									.sorted(Comparator.comparing(TimeitemTobeDisplayDto::getOrderNo))
									.collect(Collectors.toList());
	}
	
	/**
	 * To dto print remarks content.
	 *
	 * @param lstDomainObject the lst domain object
	 * @return the list
	 */
	private List<PrintRemarksContentDto> toDtoPrintRemarksContent(List<PrintRemarksContent> lstDomainObject) {
		return lstDomainObject.stream()
								.map(domain -> {
									PrintRemarksContentDto dto = new PrintRemarksContentDto();
									dto.setPrintItem(domain.getPrintItem().value);
									dto.setUsedClassification(domain.isUsedClassification() ? USE : NOT_USE);
									return dto;
								})
								.sorted(Comparator.comparing(PrintRemarksContentDto::getPrintItem))
								.collect(Collectors.toList());
	} 
	
	public OutputItemDailyWorkScheduleDto findByCodeId(String code) {
		String companyId = AppContexts.user().companyId();
		OutputItemDailyWorkScheduleDto dtoOIDW = new OutputItemDailyWorkScheduleDto();
		OutputItemDailyWorkSchedule domainOIDW = outputItemDailyWorkScheduleRepository.findByCidAndCode(companyId, code).get();
		
		Map<Integer, String> mapIdNameAttendance = dailyAttendanceItemNameDomainService.getNameOfDailyAttendanceItem(domainOIDW.getLstDisplayedAttendance()
																					.stream()
																					.map(atdId -> atdId.getAttendanceDisplay())
																					.collect(Collectors.toList()))
											.stream()
											.collect(Collectors.toMap(DailyAttendanceItem::getAttendanceItemId, DailyAttendanceItem::getAttendanceItemName));
		
		dtoOIDW.setItemCode(domainOIDW.getItemCode().v());
		dtoOIDW.setItemName(domainOIDW.getItemName().v());
		dtoOIDW.setLstDisplayedAttendance(toDtoTimeitemTobeDisplay(domainOIDW.getLstDisplayedAttendance(), mapIdNameAttendance));
		dtoOIDW.setLstRemarkContent(toDtoPrintRemarksContent(domainOIDW.getLstRemarkContent()));
		dtoOIDW.setWorkTypeNameDisplay(domainOIDW.getWorkTypeNameDisplay().value);
		dtoOIDW.setRemarkInputNo(domainOIDW.getRemarkInputNo().value);
		return dtoOIDW;
	}
	
	/**
	 * Get output standard setting
	 *
	 * @param companyId 会社ID
	 * @return 「日別勤務表の出力項目定型設定」 dto
	 */
	public OutputStandardSettingOfDailyWorkScheduleDto getStandardSetting(String companyId) {
		
		// call ドメインモデル「日別勤務表の出力項目定型設定」を取得 (Get domain model 「日別勤務表の出力項目定型設定」)
		Optional<OutputStandardSettingOfDailyWorkSchedule> oDomain = this.outputStandardSettingRepository
				.getStandardSettingByCompanyId(companyId);

		return oDomain.map(d -> OutputStandardSettingOfDailyWorkScheduleDto.toStandardDto(d)).orElse(null);
	}

	/**
	 * Gets the free setting.
	 *
	 * @param companyId the company id
	 * @param employeeId the employee id
	 * @return the free setting
	 */
	public FreeSettingOfOutputItemForDailyWorkScheduleDto getFreeSetting(String companyId, String employeeId) {
		Optional<FreeSettingOfOutputItemForDailyWorkSchedule> oDomain = this.freeSettingOfOutputItemRepository
				.getFreeSettingByCompanyAndEmployee(companyId, employeeId);

		return oDomain.map(d -> FreeSettingOfOutputItemForDailyWorkScheduleDto.toFreeSettingDto(d)).orElse(null);
	}

}
