/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.function.app.find.dailyworkschedule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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
import nts.uk.ctx.at.function.dom.attendancetype.ScreenUseAtr;
import nts.uk.ctx.at.function.dom.dailyattendanceitem.repository.DailyAttendanceItemNameDomainService;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.AuthorityDailyPerformanceFormat;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.AuthorityFomatDaily;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.primitivevalue.DailyPerformanceFormatCode;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.repository.AuthorityDailyPerformanceFormatRepository;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.repository.AuthorityFormatDailyRepository;
import nts.uk.ctx.at.function.dom.dailyworkschedule.AttendanceItemsDisplay;
import nts.uk.ctx.at.function.dom.dailyworkschedule.FormatPerformanceAdapter;
import nts.uk.ctx.at.function.dom.dailyworkschedule.FormatPerformanceImport;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemDailyWorkSchedule;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemDailyWorkScheduleRepository;
import nts.uk.ctx.at.function.dom.dailyworkschedule.OutputItemSettingCode;
import nts.uk.ctx.at.function.dom.dailyworkschedule.PrintRemarksContent;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.BusinessType;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.BusinessTypeFormatDaily;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.repository.BusinessTypeFormatDailyRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.repository.BusinessTypesRepository;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemAtr;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.DailyAttendanceItem;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.repository.DailyAttendanceItemRepository;
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
	
	@Inject
	private OptionalItemRepository optionalItemRepository;
	
	@Inject
	private DailyAttendanceItemRepository dailyAttendanceItemRepository;
	
	@Inject
	private BusinessTypesRepository businessTypesRepository;
	
	@Inject
	private AuthorityDailyPerformanceFormatRepository authorityDailyPerformanceFormatRepository;
	
	@Inject
	private BusinessTypeFormatDailyRepository businessTypeFormatDailyRepository;
	
	@Inject
	private AuthorityFormatDailyRepository authorityFormatDailyRepository;
	
	@Inject
	private ErrorAlarmWorkRecordRepository errorAlarmWorkRecordRepository;
	
	@Inject
	private FormatPerformanceAdapter formatPerformanceAdapter;
	
	@Inject
	private DailyAttendanceItemNameDomainService dailyAttendanceItemNameDomainService;
	
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
	
	/**
	 * Find by cid.
	 *
	 * @return the map
	 */
	public Map<String, Object> findByCid() {
		String companyID = AppContexts.user().companyId();
		Map<String, Object> mapDtoReturn = new HashMap<>();
		
		// Start algorithm 画面で利用できる任意項目を含めた勤怠項目一覧を取得する
		// 対応するドメインモデル「画面で利用できる勤怠項目一覧」を取得する (get domain model đối ứng 「画面で利用できる勤怠項目一覧」 )
		List<AttendanceType> lstAttendanceType = attendanceTypeRepository.getItemByScreenUseAtr(companyID, DAILY_WORK_SCHEDULE);
		
		// Get domain 任意項目
		List<OptionalItem> lstOptionalItem = optionalItemRepository.findAll(companyID);
		
		Set<Integer> setScreenUseAtr = lstAttendanceType.stream().map(domain -> domain.getScreenUseAtr().value).collect(Collectors.toSet());
		
		List<OptionalItem> lstAttendanceTypeFilter = lstOptionalItem.stream()
																	.filter(domainOptionItem -> {
																			// 出勤簿時間　=　時間
																			if (domainOptionItem.getOptionalItemAtr().value == OptionalItemAtr.TIME.value && setScreenUseAtr.contains(ScreenUseAtr.WORK_TIME.value)) {
																				return true;
																			}
																			// 出勤簿回数　=　回数
																			if (domainOptionItem.getOptionalItemAtr().value == OptionalItemAtr.NUMBER.value && setScreenUseAtr.contains(ScreenUseAtr.ATTENDANCE_TIMES.value)) {
																				return true;
																			}
																			// 出勤簿金額　=　金額
																			if (domainOptionItem.getOptionalItemAtr().value == OptionalItemAtr.AMOUNT.value && setScreenUseAtr.contains(ScreenUseAtr.TOTAL_COMMUTING_AMOUNT.value)) {
																				return true;
																			}
																			// 日別勤務表　=　時間or回数or金額
																			if ((domainOptionItem.getOptionalItemAtr().value == OptionalItemAtr.TIME.value
																					|| domainOptionItem.getOptionalItemAtr().value == OptionalItemAtr.NUMBER.value 
																					|| domainOptionItem.getOptionalItemAtr().value == OptionalItemAtr.AMOUNT.value)
																				&& setScreenUseAtr.contains(ScreenUseAtr.DAILY_WORK_SCHEDULE.value)) {
																				return true;
																			}
																			return false;})
																	.map(domainOptionItem -> domainOptionItem)
																	.distinct()
																	.collect(Collectors.toList());
		// End algorithm 画面で利用できる任意項目を含めた勤怠項目一覧を取得する
		
		// get list attendanceId of OptionalItem 任意項目. according file 日次項目一覧.xls参照
		List<Integer> lstAttendanceID = lstAttendanceTypeFilter.stream()
				.map(domain -> domain.getOptionalItemNo().v() + 640)
				.collect(Collectors.toList());
		
		// get list attendanceId of AttendanceType 画面で利用できる勤怠項目一覧
		List<Integer> lstAttendanceID2 = lstAttendanceType.stream()
				.map(domain -> domain.getAttendanceItemId())
				.collect(Collectors.toList());
		lstAttendanceID.addAll(lstAttendanceID2);
		
		// ドメインモデル「日次の勤怠項目」をすべて取得する(Acquire all domain model "daily attendance items")
		// アルゴリズム「勤怠項目に対応する名称を生成する」
		if (!lstAttendanceID.isEmpty()) {
			mapDtoReturn.put("dailyAttendanceItem", dailyAttendanceItemNameDomainService.getNameOfDailyAttendanceItem(lstAttendanceID).stream()
					.map(domain -> {
						DailyAttendanceItemDto dto = new DailyAttendanceItemDto();
						dto.setCode(String.valueOf(domain.getAttendanceItemId()));
						dto.setName(domain.getAttendanceItemName());
						return dto;
					}).collect(Collectors.toList()));
		} else {
			mapDtoReturn.put("dailyAttendanceItem", Collections.emptyList());
		}		
		
		Map<String, String> mapCodeManeAttendance = convertListToMapAttendanceItem((List<DailyAttendanceItemDto>) mapDtoReturn.get("dailyAttendanceItem"));
		
		// get all domain 日別勤務表の出力項目
		List<OutputItemDailyWorkSchedule> lstOutputItemDailyWorkSchedule = this.outputItemDailyWorkScheduleRepository.findByCid(companyID);
		
		// if find
		if (!lstOutputItemDailyWorkSchedule.isEmpty()) {
			mapDtoReturn.put("outputItemDailyWorkSchedule", lstOutputItemDailyWorkSchedule.stream()
									.map(domain -> {
										OutputItemDailyWorkScheduleDto dto = new OutputItemDailyWorkScheduleDto();
										dto.setItemCode(domain.getItemCode().v());
										dto.setItemName(domain.getItemName().v());
										dto.setLstDisplayedAttendance(toDtoTimeitemTobeDisplay(domain.getLstDisplayedAttendance(), mapCodeManeAttendance));
										dto.setLstRemarkContent(toDtoPrintRemarksContent(domain.getLstRemarkContent()));
										dto.setWorkTypeNameDisplay(domain.getWorkTypeNameDisplay().value);
										return dto;
									})
									.sorted(Comparator.comparing(OutputItemDailyWorkScheduleDto::getItemCode))
									.collect(Collectors.toList()));
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
		
		if (!optFormatPerformanceImport.isPresent()) {
			return new ArrayList<>();
		}
		switch (optFormatPerformanceImport.get().getSettingUnitType()) 
		{
			case AUTHORITY: // In case of authority
				// Get domain 会社の日別実績の修正のフォーマット
				List<AuthorityDailyPerformanceFormat> lstAuthorityDailyPerformanceFormat = authorityDailyPerformanceFormatRepository.getListCode(companyId);
				return lstAuthorityDailyPerformanceFormat.stream()
							.map(obj -> {
								return new DataInforReturnDto(obj.getDailyPerformanceFormatCode().v(), obj.getDailyPerformanceFormatName().v());
							}).collect(Collectors.toList());
			case BUSINESS_TYPE: // In case of work type
				// Get doamin 勤務種別日別実績の修正のフォーマット
				List<BusinessTypeFormatDaily> lstBusinessTypeFormatDaily = businessTypeFormatDailyRepository.getBusinessTypeFormatByCompanyId(companyId);
				Set<String> setBusinessTypeFormatDailyCode = lstBusinessTypeFormatDaily.stream()
															.map(domain -> domain.getBusinessTypeCode().v())
															.collect(Collectors.toSet());
				
				// Get domain ドメインモデル「勤務種別」を取得する
				// businessTypeCode get from doamin 勤務種別日別実績の修正のフォーマット
				List<BusinessType> lstBusinessType = businessTypesRepository.findAll(companyId);
	
				return lstBusinessType.stream()
					.filter(domain -> setBusinessTypeFormatDailyCode.contains(domain.getBusinessTypeCode().v()))
					.map(domain -> new DataInforReturnDto(domain.getBusinessTypeCode().v(), domain.getBusinessTypeName().v()))
				.collect(Collectors.toList());
				default:
					return new ArrayList<>();
		}
	}
	
	// algorithm for screen D: copy
	public List<DataInforReturnDto> executeCopy(String codeCopy, String codeSourceSerivce, List<OutputItemDailyWorkScheduleCopyCommand> lstCommandCopy) {
		String companyId = AppContexts.user().companyId();
		
		// get domain 日別勤務表の出力項目
		Optional<OutputItemDailyWorkSchedule> optOutputItemDailyWorkSchedule = outputItemDailyWorkScheduleRepository.findByCidAndCode(companyId, new OutputItemSettingCode(codeCopy).v());
		
		if (optOutputItemDailyWorkSchedule.isPresent()) {
			throw new BusinessException("Msg_3");
		} else {
			return getDomConvertDailyWork(companyId, codeSourceSerivce, lstCommandCopy);
		}
	}
	
	// アルゴリズム「日別勤務表用フォーマットをコンバートする」を実行する(Execute algorithm "Convert daily work table format")
	private List<DataInforReturnDto> getDomConvertDailyWork(String companyId, String code, List<OutputItemDailyWorkScheduleCopyCommand> lstCommandCopy) {
		// Get domain 実績修正画面で利用するフォーマット from request list 402
		Optional<FormatPerformanceImport> optFormatPerformanceImport = formatPerformanceAdapter.getFormatPerformance(companyId);
		
		List<DataInforReturnDto> lstDataReturn = new ArrayList<>();
		
		if (optFormatPerformanceImport.isPresent()) {
			switch (optFormatPerformanceImport.get().getSettingUnitType()) 
			{
				case AUTHORITY: // In case of authority
					// ドメインモデル「会社の日別実績の修正のフォーマット」を取得する (Acquire the domain model "format of company's daily performance correction")
					// 「日別実績の修正の表示項目」から表示項目を取得する (Acquire display items from "display items for correction of daily performance")
					List<AuthorityFomatDaily>  lstAuthorityFomatDaily = authorityFormatDailyRepository.getAuthorityFormatDailyDetail(companyId, new DailyPerformanceFormatCode(code), new BigDecimal(SHEET_NO_1));
					lstAuthorityFomatDaily.sort(Comparator.comparing(AuthorityFomatDaily::getDisplayOrder));
					lstDataReturn = lstAuthorityFomatDaily.stream()
															.map(domain -> new DataInforReturnDto(domain.getAttendanceItemId()+"", ""))
															.collect(Collectors.toList());
					break;
				case BUSINESS_TYPE:
					// ドメインモデル「勤務種別日別実績の修正のフォーマット」を取得する (Acquire the domain model "Format of working type daily performance correction)
					// 「日別実績の修正の表示項目」から表示項目を取得する (Acquire display items from "display items for correction of daily performance")
					List<BusinessTypeFormatDaily> lstBusinessTypeFormatDaily = businessTypeFormatDailyRepository.getBusinessTypeFormatDailyDetail(companyId, new BusinessTypeCode(code).v(), new BigDecimal(SHEET_NO_1));
					lstBusinessTypeFormatDaily.sort(Comparator.comparing(BusinessTypeFormatDaily::getOrder));
					lstDataReturn = lstBusinessTypeFormatDaily.stream()
															.map(domain -> new DataInforReturnDto(domain.getAttendanceItemId()+"", ""))
															.collect(Collectors.toList());
					break;
				default:
					break;
			}
		}
		
		Map<String, String> mapCodeName =  lstCommandCopy.stream()
				.collect(Collectors.toMap(OutputItemDailyWorkScheduleCopyCommand::getCode, 
										  OutputItemDailyWorkScheduleCopyCommand::getName));
		lstDataReturn = lstDataReturn.stream()
				.filter(domain -> mapCodeName.containsKey(domain.getCode()))
				.map(domain -> {
					domain.setName(mapCodeName.get(domain.getCode()));
					return domain;
				}).collect(Collectors.toList());
		
		// 1Sheet目の表示項目を返り値とする (Coi hạng mục hiển thj của sheet đầu tiên là giá trị trả về)
		if (lstDataReturn.size() <= 48) {
			return lstDataReturn;
		}
		else { // 1Sheet目の表示項目の先頭48項目までを返り値とする(Lấy 48 hạng mục đầu trong sheet đầu tiên làm giá trị trả về)
			return lstDataReturn.stream().limit(48).collect(Collectors.toList());
		}
	}
	
	/**
	 * To dto timeitem tobe display.
	 *
	 * @param lstDomainObject the lst domain object
	 * @return the list
	 */
	private List<TimeitemTobeDisplayDto> toDtoTimeitemTobeDisplay(List<AttendanceItemsDisplay> lstDomainObject, Map<String, String> mapCodeManeAttendance) {
		return lstDomainObject.stream()
									.map(domain -> {
										TimeitemTobeDisplayDto dto = new TimeitemTobeDisplayDto();
										dto.setAttendanceDisplay(domain.getAttendanceDisplay());
										dto.setOrderNo(domain.getOrderNo());
										dto.setAttendanceName(mapCodeManeAttendance.get(String.valueOf(domain.getAttendanceDisplay())));
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
	
	/**
	 * Convert list to map attendance item.
	 *
	 * @param lst the lst
	 * @return the map
	 */
	private Map<String, String> convertListToMapAttendanceItem(List<DailyAttendanceItemDto> lst) {
		return lst.stream().collect(
                Collectors.toMap(DailyAttendanceItemDto::getCode, DailyAttendanceItemDto::getName));
	}
}
