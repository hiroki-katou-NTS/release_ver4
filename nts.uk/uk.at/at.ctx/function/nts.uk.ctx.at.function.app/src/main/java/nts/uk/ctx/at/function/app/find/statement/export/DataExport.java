/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.function.app.find.statement.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.statement.EmployeeGeneralInfoAdapter;
import nts.uk.ctx.at.function.dom.statement.WkpHistWithPeriodAdapter;
import nts.uk.ctx.at.function.dom.statement.dtoimport.EmployeeGeneralInfoImport;
import nts.uk.ctx.at.function.dom.statement.dtoimport.ExWorkPlaceHistoryImport;
import nts.uk.ctx.at.function.dom.statement.dtoimport.ExWorkplaceHistItemImport;
import nts.uk.ctx.at.function.dom.statement.dtoimport.WkpHistWithPeriodImport;
import nts.uk.ctx.at.function.dom.statement.dtoimport.WkpInfoHistImport;
import nts.uk.ctx.at.record.dom.stamp.StampAtr;
import nts.uk.ctx.at.record.dom.stamp.StampItem;
import nts.uk.ctx.at.record.dom.stamp.StampRepository;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCard;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCardRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampDakokuRepository;
//import nts.uk.ctx.at.record.dom.worklocation.WorkLocation;
//import nts.uk.ctx.at.record.dom.worklocation.WorkLocationRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.arc.time.calendar.period.DatePeriod;

/**
 * The Class DataExport.
 */
@Stateless
public class DataExport {

	/** The stamp card repository. */
	@Inject
	private StampCardRepository stampCardRepository;

	/** The work location repository. */
	// @Inject
	// private WorkLocationRepository workLocationRepository;

	/** The work time setting repository. */
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;

	/** The stamp repository. */
	@Inject
	private StampRepository stampRepository;

	/** The employee general info adapter. */
	@Inject
	private EmployeeGeneralInfoAdapter employeeGeneralInfoAdapter;

	/** The wkp hist with period adapter. */
	@Inject
	private WkpHistWithPeriodAdapter wkpHistWithPeriodAdapter;

	@Inject
	private StampDakokuRepository stampDakokuRepository;

	/** The Constant GET_EMPLOYMENT. */
	// ?????????????????????
	private static final boolean GET_EMPLOYMENT = false;

	/** The Constant GET_CLASSIFICATION. */
	// ?????????????????????
	private static final boolean GET_CLASSIFICATION = false;

	/** The Constant GET_POSITION. */
	// ?????????????????????
	private static final boolean GET_POSITION = false;

	/** The Constant GET_WORKPLACE. */
	// ?????????????????????
	private static final boolean GET_WORKPLACE = true;

	/** The Constant GET_DEPARTMENT. */
	// ?????????????????????
	private static final boolean GET_DEPARTMENT = false;

	private Map<String, List<WkpInfoHistImport>> mapWkpIdWkpInfo = new HashMap<>();
	// ????????????
	// private List<StampItem> lstStampItem = new ArrayList<>();
	private List<Stamp> lstStamp = new ArrayList<>();
	private Map<String, List<String>> mapEmpIdWkpId = new HashMap<>();
	private GeneralDate dateStampItem;
	// private Map<String, DatePeriod> mapWkpIdPeriod;
	private Map<String, List<ExWorkplaceHistItemImport>> mapEmpIdWkps;

	/**
	 * Gets the target data.
	 *
	 * @param lstEmployeeId
	 *            the lst employee id
	 * @param startDate
	 *            the start date
	 * @param endDate
	 *            the end date
	 * @param cardNumNotRegister
	 *            the card num not register
	 * @return the target data
	 */
	// ??????????????????????????????????????? (X??? l?? l???y data ?????i t?????ng statement list)
	public List<StatementList> getTargetData(List<EmployeeInfor> lstEmployeeInfor, GeneralDate startDate,
			GeneralDate endDate, boolean cardNumNotRegister) {

		if (CollectionUtil.isEmpty(lstEmployeeInfor) && !cardNumNotRegister) {
			return Collections.emptyList();
		}

		String companyId = AppContexts.user().companyId();
		String contractCode = AppContexts.user().contractCode();
		DatePeriod datePeriod = new DatePeriod(startDate, endDate);
		List<String> lstEmployeeId = new ArrayList<>();
		Map<String, String> mapEmpIdCd = new HashMap<>();
		Map<String, String> mapEmpIdName = new HashMap<>();

		List<String> lstWkpID = new ArrayList<>();

		// convert list to map store data of employee
		lstEmployeeInfor.stream().forEach(dto -> {
			String employeeId = dto.getEmployeeID();
			lstEmployeeId.add(employeeId);
			mapEmpIdCd.put(employeeId, dto.getEmployeeCD());
			mapEmpIdName.put(employeeId, dto.getEmployeeName());
		});

		// data repare for export
		List<StatementList> dataReturn = new ArrayList<>();

		// Input.?????????NO????????????????????????(phan ??oan Input.card NO ch??a ????ng ky)
		if (cardNumNotRegister) {
			// ?????????????????????????????????????????????????????????(get domain model ?????????????????????)
			List<StampCard> lstStampCard = stampCardRepository.getLstStampCardByContractCode(contractCode);

			// ????????????????????????????????????????????????(get domain model ????????????)
			// lstStampItem = stampRepository.findByDateCompany(companyId,
			// convertGDT(startDate, "start"), convertGDT(endDate, "end"));
			lstStamp = stampDakokuRepository.getByDateperiod(companyId, new DatePeriod(startDate, endDate));
			// filter list StampItem have ??????????????? but don't exist in StampCard
			lstStamp = getStampItemExcludeStampCard(lstStamp, lstStampCard);
		} else {
			// Imported??????????????????????????? ???????????????(get Imported???????????????????????????)
			EmployeeGeneralInfoImport employeeGeneralInfoDto = employeeGeneralInfoAdapter.getEmployeeGeneralInfo(
					lstEmployeeId, datePeriod, GET_EMPLOYMENT, GET_CLASSIFICATION, GET_POSITION, GET_WORKPLACE,
					GET_DEPARTMENT);

			// ?????????????????????????????????????????????????????????????????????ID???????????????(extract using workplace ID from get ???????????????????????????-
			// history information of employee)

			List<ExWorkPlaceHistoryImport> exWorkPlaceHistoryImports = employeeGeneralInfoDto
					.getExWorkPlaceHistoryImports().stream().collect(Collectors.toList());

			mapEmpIdWkps = exWorkPlaceHistoryImports.stream().collect(Collectors
					.toMap(ExWorkPlaceHistoryImport::getEmployeeId, ExWorkPlaceHistoryImport::getWorkplaceItems));

			mapEmpIdWkpId = exWorkPlaceHistoryImports.stream().collect(
					Collectors.toMap(ExWorkPlaceHistoryImport::getEmployeeId, listItem -> listItem.getWorkplaceItems()
							.stream().map(dto -> dto.getWorkplaceId()).collect(Collectors.toList())));

			List<ExWorkplaceHistItemImport> lstTemp = new ArrayList<>();
			exWorkPlaceHistoryImports.forEach(domain -> {
				lstTemp.addAll(domain.getWorkplaceItems());
			});

			// mapWkpIdPeriod =
			lstTemp.stream().collect(Collectors.toMap(ExWorkplaceHistItemImport::getWorkplaceId,
					ExWorkplaceHistItemImport::getPeriod, (objDup1, objDup2) -> {
						return objDup2;
					}));

			mapEmpIdWkpId.entrySet().stream().forEach(dto -> lstWkpID.addAll(dto.getValue()));

			// Imported???????????????????????????????????????(get Imported????????????????????????)
			List<WkpHistWithPeriodImport> lstWkpHistWithPeriodExport = wkpHistWithPeriodAdapter
					.getLstHistByWkpsAndPeriod(lstWkpID, datePeriod);
			mapWkpIdWkpInfo = lstWkpHistWithPeriodExport.stream().collect(
					Collectors.toMap(WkpHistWithPeriodImport::getWkpId, WkpHistWithPeriodImport::getWkpInfoHistLst));

			// ?????????????????????????????????????????????????????????(get domain model?????????????????????)
			List<StampCard> lstStampCard = stampCardRepository.getLstStampCardByLstSidAndContractCd(lstEmployeeId,
					contractCode);

			// ????????????????????????????????????????????????(get domain model ????????????)
			List<String> lstStampCardNumber = lstStampCard.stream().map(domain -> domain.getStampNumber().v())
					.collect(Collectors.toList());
			// lstStampItem = stampRepository.findByEmployeeID_Fix(companyId,
			// lstStampCardNumber, convertGDT(startDate, "start"), convertGDT(endDate,
			// "end"));
			lstStamp = stampDakokuRepository.getByCardAndPeriod(companyId, lstStampCardNumber,
					new DatePeriod(startDate, endDate));
		}

		// ??????????????????????????????????????????????????????(get domain model ??????????????????- workplace) : only comment.

		// List<String> lstWorktimeCode = lstStampItem.stream().map(domain ->
		// domain.getSiftCd().v()).distinct().collect(Collectors.toList());
		List<String> lstWorktimeCode = lstStamp.stream().filter(c -> c.getRefActualResults().getWorkTimeCode().isPresent())
				.map(domain -> domain.getRefActualResults().getWorkTimeCode().get().v()).distinct()
				.collect(Collectors.toList());

		// ??????????????????????????????????????????????????????????????????(get domain model ??????????????????????????????- setting time zone lam
		// vi???c)
		Map<String, String> mapWorkCdWorkName = workTimeSettingRepository
				.getListWorkTimeSetByListCode(companyId, lstWorktimeCode).stream()
				.collect(Collectors.toMap(item -> item.getWorktimeCode().v(),
						item -> item.getWorkTimeDisplayName().getWorkTimeName().v()));

		// ???????????????????????????????????????????????????(setting get data on stamp list)
		if (cardNumNotRegister) {
			lstStamp.stream().forEach(domain -> {
				StatementList dto = new StatementList();
				dto.setCardNo(domain.getCardNumber().v());
				dto.setDate(domain.getStampDateTime());
				dto.setAtdType(getAtdType(EnumAdaptor.valueOf(domain.getType().getChangeClockArt().value, StampAtr.class)));
				dto.setWorkTimeZone(mapWorkCdWorkName.get(domain.getRefActualResults().getWorkTimeCode().get().v()));
				dto.setTime(convertToTime(domain.getStampDateTime().clockHourMinute().v()));
				dataReturn.add(dto);
			});
		} else {
			//comment vi k duoc su dung n???a
//			lstStamp.stream().forEach(objStampItem -> {
//				String employeeId = objStampItem.getEmployeeId();
//				if (mapEmpIdWkps.get(employeeId) != null) {
//					mapEmpIdWkps.get(employeeId).forEach(obj -> {
//						String wkpId = obj.getWorkplaceId();
//						// Check workplace is exist
//						if (mapWkpIdWkpInfo.containsKey(wkpId)) {
//							dateStampItem = objStampItem.getStampDateTime().toDate();
//							// Date period Employee corresponding to workplace
//							DatePeriod wkpDatePeriod = obj.getPeriod();
//							WkpInfoHistImport obj2 = mapWkpIdWkpInfo.get(wkpId).get(0);
//							if (wkpDatePeriod.start().beforeOrEquals(dateStampItem)
//									&& wkpDatePeriod.end().afterOrEquals(dateStampItem)
//							/*
//							 * && wkpDatePeriod.start().after(obj2.getPeriod().start()) &&
//							 * wkpDatePeriod.end().before(obj2.getPeriod().end())
//							 */
//							) {
//								StatementList dto = new StatementList();
//								dto.setWkpCode(obj2.getWkpCode());
//								dto.setWkpName(obj2.getWkpDisplayName());
//								dto.setEmpCode(mapEmpIdCd.get(employeeId));
//								dto.setEmpName(mapEmpIdName.get(employeeId));
//								dto.setCardNo(objStampItem.getCardNumber().v());
//								dto.setDate(objStampItem.getStampDateTime());
//								dto.setAtdType(getAtdType(
//										EnumAdaptor.valueOf(objStampItem.getType().getChangeClockArt().value, StampAtr.class)));
//								dto.setWorkTimeZone(mapWorkCdWorkName.get(objStampItem.getRefActualResults().getWorkTimeCode().get().v()));
//								dto.setTime(convertToTime(objStampItem.getStampDateTime().clockHourMinute().v()));
//								dataReturn.add(dto);
//							}
//						}
//					});
//				}
//			});
		}

		return dataReturn;
	}

	/**
	 * Convert to time.
	 *
	 * @param totalMinute
	 *            the total minute
	 * @return the string
	 */
	// convert number to hour
	private String convertToTime(int totalMinute) {
		int MINUTES_IN_AN_HOUR = 60;
		int hours = totalMinute / MINUTES_IN_AN_HOUR;
		int minutes = totalMinute % MINUTES_IN_AN_HOUR;

		return hours + ":" + (minutes == 0 ? "00" : minutes < 10 ? "0" + minutes : minutes);
	}

	/**
	 * Gets the stamp item exclude stamp card.
	 *
	 * @param lstStampItem
	 *            the lst stamp item
	 * @param lstStampCard
	 *            the lst stamp card
	 * @return the stamp item exclude stamp card
	 */
	private List<Stamp> getStampItemExcludeStampCard(List<Stamp> lstStamp, List<StampCard> lstStampCard) {
		Set<String> setStampCard = lstStampCard.stream().map(domain -> domain.getStampNumber().v())
				.collect(Collectors.toSet());

		return lstStamp.stream().filter(domain -> !setStampCard.contains(domain.getCardNumber().v()))
				.collect(Collectors.toList());
	}

	/**
	 * Convert GDT.
	 *
	 * @param date
	 *            the date
	 * @return the general date time
	 */
	private GeneralDateTime convertGDT(GeneralDate date, String type) {
		if (type.compareTo("start") == 0) {
			return GeneralDateTime.ymdhms(date.year(), date.month(), date.day(), 0, 0, 0);
		}
		return GeneralDateTime.ymdhms(date.year(), date.month(), date.day(), 23, 59, 59);

	}

	/**
	 * Gets the atd type.
	 *
	 * @param type
	 *            the type
	 * @return the atd type
	 */
	private String getAtdType(StampAtr type) {
		String result = StringUtils.EMPTY;
		switch (type) {
		case ATTENDANCE:
			result = TextResource.localize("Com_WorkIn");
			break;
		case WORKONTIME:
			result = TextResource.localize("Com_WorkOut");
			break;
		case INTRODUCTION:
			result = TextResource.localize("Com_GateIn");
			break;
		case EXIT:
			result = TextResource.localize("Com_GateOut");
			break;
		case GOINGOUT:
			result = TextResource.localize("Com_Out");
			break;
		case RETURN:
			result = TextResource.localize("Com_In");
			break;
		case SUPPORT_START:
			result = "";
			break;
		case EMERGENCY_START:
			result = TextResource.localize("Com_ExtraIn");
			break;
		case SUPPORT_END:
			result = "";
			break;
		case EMERGENCY_END:
			result = TextResource.localize("Com_ExtraOut");
			break;
		case PCLOGON:
			result = TextResource.localize("Com_LogOn");
			break;
		case PCLOGOFF:
			result = TextResource.localize("Com_LogOff");
			break;
		}
		return result;
	}
}
