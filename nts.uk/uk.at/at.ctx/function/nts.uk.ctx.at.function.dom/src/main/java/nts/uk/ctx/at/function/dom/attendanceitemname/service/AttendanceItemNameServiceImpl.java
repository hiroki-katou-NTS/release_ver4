package nts.uk.ctx.at.function.dom.attendanceitemname.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.function.dom.adapter.DailyAttendanceItemAdapter;
import nts.uk.ctx.at.function.dom.adapter.DivergenceTimeAdapter;
import nts.uk.ctx.at.function.dom.adapter.DivergenceTimeAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.OptionalItemAdapter;
import nts.uk.ctx.at.function.dom.adapter.OptionalItemImport;
import nts.uk.ctx.at.function.dom.adapter.PremiumItemFuncAdapter;
import nts.uk.ctx.at.function.dom.adapter.PremiumItemFuncAdapterDto;
import nts.uk.ctx.at.function.dom.adapter.SpecificDateAdapter;
import nts.uk.ctx.at.function.dom.adapter.SpecificDateImport;
import nts.uk.ctx.at.function.dom.attendanceitemframelinking.AttendanceItemLinking;
import nts.uk.ctx.at.function.dom.attendanceitemframelinking.enums.FrameCategory;
import nts.uk.ctx.at.function.dom.attendanceitemframelinking.enums.TypeOfItem;
import nts.uk.ctx.at.function.dom.attendanceitemframelinking.repository.AttendanceItemLinkingRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.BPTimeItemRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.timeitem.BonusPayTimeItem;
import nts.uk.ctx.at.shared.dom.monthlyattditem.MonthlyAttendanceItemRepository;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrame;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameRepository;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSetting;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSettingRepository;
import nts.uk.ctx.at.shared.dom.outsideot.breakdown.OutsideOTBRDItem;
import nts.uk.ctx.at.shared.dom.outsideot.overtime.Overtime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.attendanceitemname.AttItemName;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimes;
import nts.uk.ctx.at.shared.dom.scherec.totaltimes.TotalTimesRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHoliday;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayRepository;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrame;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrameRepository;
import nts.uk.ctx.at.shared.dom.worktype.absenceframe.AbsenceFrame;
import nts.uk.ctx.at.shared.dom.worktype.absenceframe.AbsenceFrameRepository;
import nts.uk.ctx.at.shared.dom.worktype.specialholidayframe.SpecialHolidayFrame;
import nts.uk.ctx.at.shared.dom.worktype.specialholidayframe.SpecialHolidayFrameRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class AttendanceItemNameServiceImpl implements AttendanceItemNameService {
	@Inject
	private DailyAttendanceItemAdapter dailyAttendanceItemAdapter;

	@Inject
	private MonthlyAttendanceItemRepository monthlyAttendanceItemRepository;

	@Inject
	private AttendanceItemLinkingRepository attendanceItemLinkingRepository;

	@Inject
	private DivergenceTimeAdapter divergenceTimeAdapter;

	@Inject
	private PremiumItemFuncAdapter premiumItemFuncAdapter;

	@Inject
	private BPTimeItemRepository bPTimeItemRepository;

	@Inject
	private OvertimeWorkFrameRepository overtimeFrameRepository;

	@Inject
	private OptionalItemAdapter optionalItemAdapter;

	@Inject
	private WorkdayoffFrameRepository workdayoffFrameRepository;

	@Inject
	private SpecificDateAdapter specificDateAdapter;

	@Inject
	private OutsideOTSettingRepository outsideOTSettingRepository;

	@Inject
	private AbsenceFrameRepository absenceFrameRepository;

	@Inject
	private SpecialHolidayFrameRepository specialHolidayFrameRepo;

	@Inject
	private TotalTimesRepository totalTimesRepository;

	@Inject
	private SpecialHolidayRepository specialHolidayRepository;

	@Override
	public List<AttItemName> getNameOfAttendanceItem(List<Integer> attendanceItemIds, TypeOfItem type) {
		List<AttItemName> attendanceItems = this.getAttendanceItemName(attendanceItemIds, type);
		// 対応するドメインモデル 「勤怠項目と枠の紐付け」 を取得する
		List<AttendanceItemLinking> attendanceItemAndFrameNos = this.attendanceItemLinkingRepository
				.getFullDataByAttdIdAndType(attendanceItemIds, type);
		return this.getNameOfAttendanceItem(attendanceItems, attendanceItemAndFrameNos);
	}
	
	@Override
	public List<AttItemName> getNameOfAttendanceItem(TypeOfItem type) {
		List<AttItemName> attendanceItems = this.getAttendanceItemName(type);
		// 対応するドメインモデル 「勤怠項目と枠の紐付け」 を取得する
		List<AttendanceItemLinking> attendanceItemAndFrameNos = this.attendanceItemLinkingRepository
				.getFullDataByAttdIdAndType(attendanceItems.stream().map(c -> c.getAttendanceItemId()).collect(Collectors.toList()), type);
		return this.getNameOfAttendanceItem(attendanceItems, attendanceItemAndFrameNos);
	}

	@Override
	public List<AttItemName> getNameOfAttendanceItem(TypeOfItem type, List<AttItemName> attendanceItems) {
		List<Integer> attendanceItemIds = attendanceItems.stream().map(x -> x.getAttendanceItemId())
				.collect(Collectors.toList());
		attendanceItems = this.getAttendanceItemName(attendanceItems);
		// 対応するドメインモデル 「勤怠項目と枠の紐付け」 を取得する
		List<AttendanceItemLinking> attendanceItemAndFrameNos = this.attendanceItemLinkingRepository
				.getFullDataByAttdIdAndType(attendanceItemIds, type);
		return this.getNameOfAttendanceItem(attendanceItems, attendanceItemAndFrameNos);
	}

	@Override
	public List<AttItemName> getNameOfAttendanceItem(List<AttItemName> attendanceItems,
			List<AttendanceItemLinking> attendanceItemAndFrameNos) {
		LoginUserContext login = AppContexts.user();
		String companyId = login.companyId();
		attendanceItems = this.getAttendanceItemName(attendanceItems);
		Map<Integer, AttItemName> mapAttendanceItems = attendanceItems.stream()
				.collect(Collectors.toMap(AttItemName::getAttendanceItemId, x -> x));
		Map<Integer, AttendanceItemLinking> mapItemLinking = attendanceItemAndFrameNos.stream()
				.collect(Collectors.toMap(AttendanceItemLinking::getAttendanceItemId, x -> x));
		for (AttendanceItemLinking link : attendanceItemAndFrameNos) {
			int id = link.getAttendanceItemId();
			if (mapAttendanceItems.containsKey(id)) {
				mapAttendanceItems.get(id).setFrameCategory(link.getFrameCategory().value);
				mapAttendanceItems.get(id).setTypeOfAttendanceItem(link.getTypeOfAttendanceItem().value);
			} /*else {
				mapAttendanceItems.get(id).setFrameCategory(null);
				mapAttendanceItems.get(id).setTypeOfAttendanceItem(null);
			}*/
		}

		List<Integer> frameNos = attendanceItemAndFrameNos.stream().map(f -> {
			return f.getFrameNo().v();
		}).distinct().collect(Collectors.toList());

		if (attendanceItemAndFrameNos.isEmpty()) {
			return attendanceItems;
		}

		List<OvertimeWorkFrame> overtimeWorkFrames = this.overtimeFrameRepository
				.getOvertimeWorkFrameByFrameNos(companyId, frameNos);
		Function<OvertimeWorkFrame, Integer> function = overTime -> overTime.getOvertimeWorkFrNo().v().intValue();
		// 残業 0 + 残業振替 1
		Map<Integer, OvertimeWorkFrame> overTimes = overtimeWorkFrames.stream()
				.collect(Collectors.toMap(function, x -> x));

		List<WorkdayoffFrame> workdayoffFrames = this.workdayoffFrameRepository.getWorkdayoffFrameBy(companyId,
				frameNos);
		Function<WorkdayoffFrame, Integer> fun = workDay -> workDay.getWorkdayoffFrNo().v().intValue();
		// 休出 2 + 休出振替 3
		Map<Integer, WorkdayoffFrame> leave = workdayoffFrames.stream().collect(Collectors.toMap(fun, x -> x));

		// 割増項目 4
		Map<Integer, PremiumItemFuncAdapterDto> premiumItemnames = this.premiumItemFuncAdapter
				.getPremiumItemName(companyId, frameNos).stream()
				.collect(Collectors.toMap(PremiumItemFuncAdapterDto::getDisplayNumber, x -> x));

		// 加給時間項目 5
		Map<Integer, BonusPayTimeItem> bonusPayTimeItems = this.bPTimeItemRepository
				.getListBonusPayTimeItemName(companyId, frameNos).stream()
				.collect(Collectors.toMap(BonusPayTimeItem::getId, x -> x));

		// 特定加給時間項目 6
		Map<Integer, BonusPayTimeItem> specialBonusPayTimeItem = this.bPTimeItemRepository
				.getListSpecialBonusPayTimeItemName(companyId, frameNos).stream()
				.collect(Collectors.toMap(BonusPayTimeItem::getId, x -> x));

		// 乖離時間 7
		Map<Integer, DivergenceTimeAdapterDto> divergenceTimes = this.divergenceTimeAdapter
				.getDivergenceTimeName(companyId, frameNos).stream()
				.collect(Collectors.toMap(DivergenceTimeAdapterDto::getDivTimeId, x -> x, (x, y) -> x));

		// 任意項目 8
		Map<Integer, OptionalItemImport> optionalItems = this.optionalItemAdapter.findOptionalItem(companyId, frameNos)
				.stream().collect(Collectors.toMap(OptionalItemImport::getOptionalItemNo, x -> x));

		// 特定日 10
		Map<Integer, SpecificDateImport> specificDates = this.specificDateAdapter.getSpecificDate(companyId, frameNos)
				.stream().collect(Collectors.toMap(SpecificDateImport::getSpecificDateItemNo, x -> x));

		// 超過時間 : 時間外超過設定 11
		List<OvertimeDto> overtimesSetting;
		List<OutsideOTBRDItemDto> outsideOTBRDItem;
		Optional<OutsideOTSetting> outsideOTSetting = outsideOTSettingRepository.findByIdV2(companyId);
		if (outsideOTSetting.isPresent()) {
			overtimesSetting = outsideOTSetting.get().getOvertimes().stream().map(c -> convertFromOvertime(c))
					.collect(Collectors.toList());
			outsideOTBRDItem = outsideOTSetting.get().getBreakdownItems().stream()
					.map(c -> convertToOutsideOTBRDItem(c)).collect(Collectors.toList());
		} else {
			overtimesSetting = new ArrayList<>();
			outsideOTBRDItem = new ArrayList<>();
		}

		// 欠勤 12
		Map<Integer, AbsenceFrame> absenceFrame = this.absenceFrameRepository
				.findAbsenceFrameByListFrame(companyId, frameNos).stream()
				.collect(Collectors.toMap(AbsenceFrame::getAbsenceFrameNo, x -> x));

		// 特別休暇枠 13
		Map<Integer, SpecialHolidayFrame> specialHolidayFrame = this.specialHolidayFrameRepo
				.findHolidayFrameByListFrame(companyId, frameNos).stream()
				.collect(Collectors.toMap(SpecialHolidayFrame::getSpecialHdFrameNo, x -> x));

		// 回数集計 14
		Map<Integer, TotalTimes> totalTimes = this.totalTimesRepository.getTotalTimesDetailByListNo(companyId, frameNos)
				.stream().collect(Collectors.toMap(TotalTimes::getTotalCountNo, x -> x));
		// 特別休暇 15
		Map<Integer, SpecialHoliday> specialHoliday = new HashMap<>();
		this.specialHolidayRepository.findByListCode(companyId, frameNos).forEach(x -> {
			specialHoliday.put(x.getSpecialHolidayCode().v(), x);
		});

		attendanceItems = mapAttendanceItems.values().stream().collect(Collectors.toList());
		for (AttItemName item : attendanceItems) {
			int attId = item.getAttendanceItemId();
			String attName = item.getAttendanceItemName();
			AttendanceItemLinking itemLink;
			if (item.getFrameCategory() == null) {
				continue;
			}
			FrameCategory frCtg = EnumAdaptor.valueOf(item.getFrameCategory(), FrameCategory.class);
			switch (frCtg) {
			case OverTime:
				itemLink = mapItemLinking.get(attId);
				if (overTimes.containsKey(itemLink.getFrameNo().v())) {
					item.setAttendanceItemName(MessageFormat.format(attName,
							overTimes.get(itemLink.getFrameNo().v()).getOvertimeWorkFrName()));
				}
				break;
			case OverTimeTranfer:
				itemLink = mapItemLinking.get(attId);
				if (overTimes.containsKey(itemLink.getFrameNo().v())) {
					item.setAttendanceItemName(MessageFormat.format(attName,
							overTimes.get(itemLink.getFrameNo().v()).getTransferFrName()));
				}
				break;
			case Rest:
				itemLink = mapItemLinking.get(attId);
				if (leave.containsKey(itemLink.getFrameNo().v())) {
					item.setAttendanceItemName(
							MessageFormat.format(attName, leave.get(itemLink.getFrameNo().v()).getWorkdayoffFrName()));
				}
				break;
			case RestTranfer:
				itemLink = mapItemLinking.get(attId);
				if (leave.containsKey(itemLink.getFrameNo().v())) {
					item.setAttendanceItemName(
							MessageFormat.format(attName, leave.get(itemLink.getFrameNo().v()).getTransferFrName()));
				}
				break;
			case ExtraItem:
				itemLink = mapItemLinking.get(attId);
				if (premiumItemnames.containsKey(itemLink.getFrameNo().v())) {
					item.setAttendanceItemName(MessageFormat.format(attName,
							premiumItemnames.get(itemLink.getFrameNo().v()).getPremiumItemname()));
				}
				break;
			case AddtionTimeItem:
				itemLink = mapItemLinking.get(attId);
				if (bonusPayTimeItems.containsKey(itemLink.getFrameNo().v())) {
					item.setAttendanceItemName(MessageFormat.format(attName,
							bonusPayTimeItems.get(itemLink.getFrameNo().v()).getTimeItemName().v()));
				}
				break;
			case SpecificAddtionTimeItem:
				itemLink = mapItemLinking.get(attId);
				if (specialBonusPayTimeItem.containsKey(itemLink.getFrameNo().v())) {
					item.setAttendanceItemName(MessageFormat.format(attName,
							specialBonusPayTimeItem.get(itemLink.getFrameNo().v()).getTimeItemName().v()));
				}
				break;
			case DivergenceTimeItem:
				itemLink = mapItemLinking.get(attId);
				if (divergenceTimes.containsKey(itemLink.getFrameNo().v())) {
					item.setAttendanceItemName(MessageFormat.format(attName,
							divergenceTimes.get(itemLink.getFrameNo().v()).getDivTimeName()));
				}
				break;
			case AnyItem:
				itemLink = mapItemLinking.get(attId);
				if (optionalItems.containsKey(itemLink.getFrameNo().v())) {
					// get value
					OptionalItemImport optItem = optionalItems.get(itemLink.getFrameNo().v());
					String unit = StringUtil.isNullOrEmpty(optItem.getOptionalItemUnit(), true) ? ""
							: "（" + optItem.getOptionalItemUnit() + "）";
					// set value
					item.setAttendanceItemName(MessageFormat.format(attName, optItem.getOptionalItemName() + unit));
				}
				break;
			case GoOut:
				break;
			case SpecificDate:
				itemLink = mapItemLinking.get(attId);
				if (specificDates.containsKey(itemLink.getFrameNo().v())) {
					item.setAttendanceItemName(MessageFormat.format(attName,
							specificDates.get(itemLink.getFrameNo().v()).getSpecificName()));
				}
				break;
			case ExcessTime:
				String overTimeName = "";
				String outsideOTBRDItemName = "";
				itemLink = mapItemLinking.get(attId);
				// fixbug 101557
				Optional<OvertimeDto> optOvertimeDto = overtimesSetting.stream().filter(x -> x.getOvertimeNo() == itemLink.getFrameNo().v()).findFirst();
				if(optOvertimeDto.isPresent()){
					overTimeName = optOvertimeDto.get().getName();
				} else {
					overTimeName = TextResource.localize("KDW003_125", itemLink.getFrameNo().v().toString());
				}
				
				if (itemLink.getPreliminaryFrameNO() != null && itemLink.getPreliminaryFrameNO().isPresent()){
					Optional<OutsideOTBRDItemDto> optOutsideOTBRDItemDto = outsideOTBRDItem.stream().filter(x-> x.getBreakdownItemNo() == itemLink.getPreliminaryFrameNO().get().v()).findFirst();
					if(optOutsideOTBRDItemDto.isPresent()){
						outsideOTBRDItemName = optOutsideOTBRDItemDto.get().getName();
					} else {
						outsideOTBRDItemName = TextResource.localize("KDW003_126", itemLink.getPreliminaryFrameNO().get().v().toString());
					}
				}
				
				item.setAttendanceItemName(MessageFormat.format(attName, overTimeName, outsideOTBRDItemName));
				break;
			case Absence:
				itemLink = mapItemLinking.get(attId);
				if (absenceFrame.containsKey(itemLink.getFrameNo().v())) {
					item.setAttendanceItemName(MessageFormat.format(attName,
							absenceFrame.get(itemLink.getFrameNo().v()).getAbsenceFrameName().v()));
				}
				break;
			case SpecialHolidayFrame:
				itemLink = mapItemLinking.get(attId);
				if (specialHolidayFrame.containsKey(itemLink.getFrameNo().v())) {
					item.setAttendanceItemName(MessageFormat.format(attName,
							specialHolidayFrame.get(itemLink.getFrameNo().v()).getSpecialHdFrameName().v()));
				}
				break;
			case TotalCount:
				itemLink = mapItemLinking.get(attId);
				if (totalTimes.containsKey(itemLink.getFrameNo().v())) {
					item.setAttendanceItemName(MessageFormat.format(attName,
							totalTimes.get(itemLink.getFrameNo().v()).getTotalTimesName().v()));
				}
				break;
			case SpecialHoliday:
				itemLink = mapItemLinking.get(attId);
				if (specialHoliday.containsKey(itemLink.getFrameNo().v())) {
					item.setAttendanceItemName(MessageFormat.format(attName,
							specialHoliday.get(itemLink.getFrameNo().v()).getSpecialHolidayName().v()));
				} else {
					String sphdName = MessageFormat.format("特別休暇{0}", itemLink.getFrameNo().v());
					item.setAttendanceItemName(MessageFormat.format(attName, sphdName));
				}
				break;
			}
		}
		return attendanceItems;
	}

	private OvertimeDto convertFromOvertime(Overtime overtime) {
		return new OvertimeDto(overtime.isSuperHoliday60HOccurs(), overtime.getUseClassification().value,
				overtime.getName().v(), overtime.getOvertime().v(), overtime.getOvertimeNo().value);
	}

	private OutsideOTBRDItemDto convertToOutsideOTBRDItem(OutsideOTBRDItem outsideOTBRDItem) {
		return new OutsideOTBRDItemDto(outsideOTBRDItem.getUseClassification().value,
				outsideOTBRDItem.getBreakdownItemNo().value, outsideOTBRDItem.getName().v(),
				outsideOTBRDItem.getProductNumber().value, outsideOTBRDItem.getAttendanceItemIds());
	}

	private List<AttItemName> getAttendanceItemName(List<Integer> attendanceItemIds, TypeOfItem type) {
		String companyId = AppContexts.user().companyId();
		List<AttItemName> attendanceItemList = new ArrayList<>();

		switch (type) {
		case Daily:
			attendanceItemList = this.dailyAttendanceItemAdapter.getDailyAttendanceItem(companyId, attendanceItemIds)
					.stream().map(item -> {
						AttItemName dto = new AttItemName();
						dto.setAttendanceItemId(item.getAttendanceItemId());
						dto.setAttendanceItemName(this.formatName(item.getAttendanceName()));
						dto.setAttendanceItemDisplayNumber(item.getDisplayNumber());
						dto.setUserCanUpdateAtr(item.getUserCanUpdateAtr());
						dto.setNameLineFeedPosition(item.getNameLineFeedPosition());
						return dto;
					}).collect(Collectors.toList());
			break;
		case Monthly:
			attendanceItemList = this.monthlyAttendanceItemRepository
					.findByAttendanceItemId(companyId, attendanceItemIds).stream().map(item -> {
						AttItemName dto = new AttItemName();
						dto.setAttendanceItemId(item.getAttendanceItemId());
						dto.setAttendanceItemName(this.formatName(item.getAttendanceName().v()));
						dto.setAttendanceItemDisplayNumber(item.getDisplayNumber());
						dto.setUserCanUpdateAtr(item.getUserCanUpdateAtr().value);
						dto.setNameLineFeedPosition(item.getNameLineFeedPosition());
						return dto;
					}).collect(Collectors.toList());
			break;
		default:
			break;
		}

		return attendanceItemList;
	}
	
	private List<AttItemName> getAttendanceItemName(TypeOfItem type) {
		String companyId = AppContexts.user().companyId();
		List<AttItemName> attendanceItemList = new ArrayList<>();

		switch (type) {
		case Daily:
			attendanceItemList = this.dailyAttendanceItemAdapter.getDailyAttendanceItemList(companyId)
					.stream().map(item -> {
						AttItemName dto = new AttItemName();
						dto.setAttendanceItemId(item.getAttendanceItemId());
						dto.setAttendanceItemName(this.formatName(item.getAttendanceName()));
						dto.setAttendanceItemDisplayNumber(item.getDisplayNumber());
						dto.setUserCanUpdateAtr(item.getUserCanUpdateAtr());
						dto.setNameLineFeedPosition(item.getNameLineFeedPosition());
						return dto;
					}).collect(Collectors.toList());
			break;
		case Monthly:
			attendanceItemList = this.monthlyAttendanceItemRepository.findAll(companyId).stream().map(item -> {
						AttItemName dto = new AttItemName();
						dto.setAttendanceItemId(item.getAttendanceItemId());
						dto.setAttendanceItemName(this.formatName(item.getAttendanceName().v()));
						dto.setAttendanceItemDisplayNumber(item.getDisplayNumber());
						dto.setUserCanUpdateAtr(item.getUserCanUpdateAtr().value);
						dto.setNameLineFeedPosition(item.getNameLineFeedPosition());
						return dto;
					}).collect(Collectors.toList());
			break;
		default:
			break;
		}

		return attendanceItemList;
	}
	
	private List<AttItemName> getAttendanceItemName(List<AttItemName> attendanceItem) {
		for (AttItemName attItemName : attendanceItem) {
			attItemName.setAttendanceItemName(this.formatName(attItemName.getAttendanceItemName()));
		}
		return attendanceItem;
	}

	private String formatName(String name) {
		if (name.indexOf("{#") >= 0) {
			int startLocation = name.indexOf("{");
			int endLocation = name.indexOf("}");
			name = name.replace(name.substring(startLocation, endLocation + 1),
					TextResource.localize(name.substring(startLocation + 2, endLocation)));
		}
		return name;
	}
}
