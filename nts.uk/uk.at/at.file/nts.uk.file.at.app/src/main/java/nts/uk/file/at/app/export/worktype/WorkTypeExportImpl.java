package nts.uk.file.at.app.export.worktype;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;
import nts.uk.ctx.at.shared.dom.worktype.absenceframe.AbsenceFrame;
import nts.uk.ctx.at.shared.dom.worktype.absenceframe.AbsenceFrameRepository;
import nts.uk.ctx.at.shared.dom.worktype.specialholidayframe.SpecialHolidayFrame;
import nts.uk.ctx.at.shared.dom.worktype.specialholidayframe.SpecialHolidayFrameRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.report.masterlist.annotation.DomainID;
import nts.uk.shr.infra.file.report.masterlist.data.ColumnTextAlign;
import nts.uk.shr.infra.file.report.masterlist.data.MasterData;
import nts.uk.shr.infra.file.report.masterlist.data.MasterHeaderColumn;
import nts.uk.shr.infra.file.report.masterlist.data.MasterListData;
import nts.uk.shr.infra.file.report.masterlist.webservice.MasterListExportQuery;

/**
 * Work type export implements
 * 
 * @author sonnh
 *
 */
@Stateless
@DomainID(value = "WorkType")
public class WorkTypeExportImpl implements MasterListData {

	@Inject
	private WorkTypeReportRepository workTypeReportRepository;

	@Inject
	private AbsenceFrameRepository absenceFrameRepository;

	@Inject
	private SpecialHolidayFrameRepository specialHolidayFrameRepository;
	
	private static final String hyphen = "-";

	@Override
	public List<MasterHeaderColumn> getHeaderColumns(MasterListExportQuery arg0) {

		List<MasterHeaderColumn> columns = new ArrayList<>();
		columns.add(new MasterHeaderColumn("コード", TextResource.localize("KMK007_66"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("名称", TextResource.localize("KMK007_67"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("略名", TextResource.localize("KMK007_68"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("記号", TextResource.localize("KMK007_69"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("備考", TextResource.localize("KMK007_70"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("廃止区分", TextResource.localize("KMK007_71"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("出勤率の計算方法", TextResource.localize("KMK007_72"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("対象範囲", TextResource.localize("KMK007_73"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("1日", TextResource.localize("KMK007_74"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午前", TextResource.localize("KMK007_75"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午後", TextResource.localize("KMK007_76"),
				ColumnTextAlign.CENTER, "", true));

		columns.add(new MasterHeaderColumn("１日の休日区分", TextResource.localize("KMK007_77"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("1日の日勤・夜勤時間を求める", TextResource.localize("KMK007_78"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("1日の出勤時刻を直行とする", TextResource.localize("KMK007_79"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("1日の退勤時刻を直帰とする", TextResource.localize("KMK007_80"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("1日の公休を消化する", TextResource.localize("KMK007_81"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("1日の代休を発生させる", TextResource.localize("KMK007_82"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("1日の欠勤の集計枠", TextResource.localize("KMK007_83"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("1日の特別休暇の集計枠", TextResource.localize("KMK007_84"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("1日の休業区分", TextResource.localize("KMK007_85"),
				ColumnTextAlign.CENTER, "", true));

		columns.add(new MasterHeaderColumn("午前の日勤・夜勤時間を求める", TextResource.localize("KMK007_86"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午前の出勤時刻を直行とする", TextResource.localize("KMK007_87"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午前の退勤時刻を直帰とする", TextResource.localize("KMK007_88"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午前の休日日数を数える", TextResource.localize("KMK007_89"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午前の公休を消化する", TextResource.localize("KMK007_90"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午前の代休を発生させる", TextResource.localize("KMK007_91"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午前の欠勤の集計枠", TextResource.localize("KMK007_92"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午前の特別休暇の集計枠", TextResource.localize("KMK007_93"),
				ColumnTextAlign.CENTER, "", true));

		columns.add(new MasterHeaderColumn("午後の日勤・夜勤時間を求める", TextResource.localize("KMK007_94"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午後の出勤時刻を直行とする", TextResource.localize("KMK007_95"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午後の退勤時刻を直帰とする", TextResource.localize("KMK007_96"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午後の休日日数を数える", TextResource.localize("KMK007_97"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午後の公休を消化する", TextResource.localize("KMK007_98"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午後の代休を発生させる", TextResource.localize("KMK007_99"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午後の欠勤の集計枠", TextResource.localize("KMK007_100"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("午後の特別休暇の集計枠", TextResource.localize("KMK007_101"),
				ColumnTextAlign.CENTER, "", true));

		columns.add(new MasterHeaderColumn("他言語名称", TextResource.localize("KMK007_102"),
				ColumnTextAlign.CENTER, "", true));
		columns.add(new MasterHeaderColumn("他言語略名", TextResource.localize("KMK007_103"),
				ColumnTextAlign.CENTER, "", true));
		return columns;
	}

	@Override
	public List<MasterData> getMasterDatas(MasterListExportQuery query) {

		String languageId = query.getLanguageId();
		String companyId = AppContexts.user().companyId();

		List<MasterData> datas = new ArrayList<>();
		List<WorkTypeReportData> listWorkTypeReport = workTypeReportRepository.findAllWorkType(companyId, languageId);
		List<WorkTypeReportData> dispOrderList = listWorkTypeReport
				.stream().filter(x -> x.getDispOrder() != null).sorted(Comparator
						.comparing(WorkTypeReportData::getDispOrder))
				.collect(Collectors.toList());
		
		List<WorkTypeReportData> dispOrderNullLst =  listWorkTypeReport.stream().filter(x->x.getDispOrder() == null).sorted(Comparator
				.comparing(WorkTypeReportData::getWorkTypeCode)).collect(Collectors.toList());
		dispOrderList.addAll(dispOrderNullLst);
		
		listWorkTypeReport = dispOrderList;
		
		if (CollectionUtil.isEmpty(listWorkTypeReport)) {
			throw new BusinessException("Msg_393");
		} else {
			List<AbsenceFrame> datasAbsenceFrame = absenceFrameRepository.findAll(companyId);
			Map<Integer, AbsenceFrame> datasAbsenceFrameMap = datasAbsenceFrame.stream()
					.collect(Collectors.toMap(AbsenceFrame::getAbsenceFrameNo, Function.identity()));
			List<SpecialHolidayFrame> datasSpecialHlFrame = specialHolidayFrameRepository.findAll(companyId);
			Map<Integer, SpecialHolidayFrame> datasSpecialHlFrameMap = datasSpecialHlFrame.stream()
					.collect(Collectors.toMap(SpecialHolidayFrame::getSpecialHdFrameNo, Function.identity()));

			listWorkTypeReport.stream().forEach(c -> {

				Map<String, Object> data = new HashMap<>();
				data.put("コード", c.getWorkTypeCode());
				data.put("名称", c.getName());
				data.put("略名", c.getAbbreviationName());
				data.put("記号", c.getSymbolicName());
				data.put("備考", c.getMemo());
				if (c.getDeprecate() == 1) {
					data.put("廃止区分", "✓");
				} else {
					data.put("廃止区分", "");
				}
				data.put("出勤率の計算方法", TextResource.localize(c.getCalculateMethod().nameId));
				data.put("対象範囲", TextResource.localize(c.getWorkAtr().nameId));
				if (c.getWorkAtr() == WorkTypeUnit.OneDay) {
					data.put("1日", TextResource.localize(c.getOneDayCls().nameId));
					if (c.getOneDayCls() == WorkTypeClassification.HolidayWork
							|| c.getOneDayCls() == WorkTypeClassification.Holiday) {
						data.put("１日の休日区分", TextResource.localize(c.getOneDayHolidayAtr().nameId));
					} else {
						data.put("１日の休日区分", "");
					}
					if (c.getOneDayCls() == WorkTypeClassification.Absence) {
						AbsenceFrame absenceFrameOneDay = datasAbsenceFrameMap.get(c.getOneDaySumAbsenseNo());
						data.put("1日の欠勤の集計枠", absenceFrameOneDay != null ? absenceFrameOneDay.getAbsenceFrameName() : "－");
					} else {
						data.put("1日の欠勤の集計枠", "");
					}

					if (c.getOneDayCls() == WorkTypeClassification.SpecialHoliday) {
						SpecialHolidayFrame specialHlFrameOneDay = datasSpecialHlFrameMap
								.get(c.getOneDaySumSpHodidayNo());
						data.put("1日の特別休暇の集計枠", specialHlFrameOneDay != null ? specialHlFrameOneDay.getSpecialHdFrameName() : "－");
					} else {
						data.put("1日の特別休暇の集計枠", "");
					}

					if (c.getAfternoonCls() == WorkTypeClassification.Closure) {
						data.put("1日の休業区分", TextResource.localize(c.getOneDaycloseAtr().nameId));
					} else {
						data.put("1日の休業区分", "");
					}
					workTypeSetOneDayPrint(data, c);
					workTypeSetHalfDayPrint(data, c);
				} else {
					data.put("午前", TextResource.localize(c.getMorningCls().nameId));
					data.put("午後", TextResource.localize(c.getAfternoonCls().nameId));
					if (c.getMorningCls() == WorkTypeClassification.Absence) {
						AbsenceFrame absenceFrameMorning = c.getMorningSumAbsenseNo() != null ? datasAbsenceFrameMap.get(c.getMorningSumAbsenseNo()) : null;
						data.put("午前の欠勤の集計枠", absenceFrameMorning != null ? absenceFrameMorning.getAbsenceFrameName() : "－");
					} else {
						data.put("午前の欠勤の集計枠", "");
					}

					if (c.getMorningCls() == WorkTypeClassification.SpecialHoliday) {
						SpecialHolidayFrame specialHlFrameMorning = c.getMorningSumSpHodidayNo() != null ? datasSpecialHlFrameMap
								.get(c.getMorningSumSpHodidayNo()) : null;
						data.put("午前の特別休暇の集計枠", specialHlFrameMorning != null ? specialHlFrameMorning.getSpecialHdFrameName() : "－");
					} else {
						data.put("午前の特別休暇の集計枠", "");
					}
					if (c.getAfternoonCls() == WorkTypeClassification.Absence) {
						AbsenceFrame absenceFrameAfternoon = c.getAfternoonSumAbsenseNo() != null ? datasAbsenceFrameMap.get(c.getAfternoonSumAbsenseNo()) : null;
						data.put("午後の欠勤の集計枠", absenceFrameAfternoon != null ? absenceFrameAfternoon.getAbsenceFrameName() : "－");
					} else {
						data.put("午後の欠勤の集計枠", "");
					}
					if (c.getAfternoonCls() == WorkTypeClassification.SpecialHoliday) {
						SpecialHolidayFrame specialHlFrameAfternoon = c.getAfternoonSumSpHodidayNo() != null ? datasSpecialHlFrameMap
								.get(c.getAfternoonSumSpHodidayNo()) : null;
						data.put("午後の特別休暇の集計枠", specialHlFrameAfternoon != null ? specialHlFrameAfternoon.getSpecialHdFrameName() : "－");
					} else {
						data.put("午後の特別休暇の集計枠", "");
					}
					workTypeSetOneDayPrint(data, c);
					workTypeSetHalfDayPrint(data, c);
				}
				data.put("他言語名称", c.getOtherLangName());
				data.put("他言語略名", c.getOtherLangShortName());
				datas.add(new MasterData(data, null, ""));
			});
		}
		return datas;
	}

	/**
	 * change true false -> "○" "-"
	 * @param check
	 * @return
	 */
	private static String checkButtonCheck(Integer check) {
		if (check == null) {
			return "";
		}
		
		if (check == 1) {
			return "○";
		}
		return hyphen;	
	}

	/**
	 * Set data work type set one day 
	 * @param data
	 * @param datareport
	 */
	private static void workTypeSetOneDayPrint(Map<String, Object> data, WorkTypeReportData datareport) {
		if (datareport.getWorkAtr() == WorkTypeUnit.OneDay) {
			data.put("午前", hyphen);
			data.put("午後", hyphen);
			data.put("1日の日勤・夜勤時間を求める", checkButtonCheck(datareport.getOneDayDayNightTimeAsk()));
			data.put("1日の出勤時刻を直行とする", checkButtonCheck(datareport.getOneDayAttendanceTime()));
			data.put("1日の退勤時刻を直帰とする", checkButtonCheck(datareport.getOneDayTimeLeaveWork()));
			data.put("1日の公休を消化する", checkButtonCheck(datareport.getOneDayDigestPublicHd()));
			data.put("1日の代休を発生させる", checkButtonCheck(datareport.getOneDayGenSubHodiday()));
		} else {
			data.put("１日の休日区分", "");
			data.put("1日の日勤・夜勤時間を求める", hyphen);
			data.put("1日の出勤時刻を直行とする", hyphen);
			data.put("1日の退勤時刻を直帰とする", hyphen);
			data.put("1日の公休を消化する", hyphen);
			data.put("1日の代休を発生させる", hyphen);
			data.put("1日の欠勤の集計枠", "");
			data.put("1日の特別休暇の集計枠", "");
			data.put("1日の休業区分", "");
		}
	}

	/**
	 * Set data work type set half day 
	 * @param data
	 * @param datareport
	 */
	private static void workTypeSetHalfDayPrint(Map<String, Object> data, WorkTypeReportData datareport) {
		if (datareport.getWorkAtr() == WorkTypeUnit.OneDay) {
			data.put("午前の日勤・夜勤時間を求める", hyphen);
			data.put("午前の出勤時刻を直行とする", hyphen);
			data.put("午前の退勤時刻を直帰とする", hyphen);
			data.put("午前の休日日数を数える", hyphen);
			data.put("午前の公休を消化する", hyphen);
			data.put("午前の代休を発生させる", hyphen);
			data.put("午前の欠勤の集計枠", "");
			data.put("午後の日勤・夜勤時間を求める", hyphen);
			data.put("午後の出勤時刻を直行とする", hyphen);
			data.put("午後の退勤時刻を直帰とする", hyphen);
			data.put("午後の休日日数を数える", hyphen);
			data.put("午後の公休を消化する", hyphen);
			data.put("午後の代休を発生させる", hyphen);
			data.put("午前の欠勤の集計枠", "");
			data.put("午前の特別休暇の集計枠", "");
		} else {
			data.put("1日", hyphen);
			data.put("午前の日勤・夜勤時間を求める", checkButtonCheck(datareport.getMorningDayNightTimeAsk()));
			data.put("午前の出勤時刻を直行とする", checkButtonCheck(datareport.getMorningAttendanceTime()));
			data.put("午前の退勤時刻を直帰とする", checkButtonCheck(datareport.getMorningTimeLeaveWork()));
			data.put("午前の休日日数を数える", checkButtonCheck(datareport.getMorningCountHodiday()));
			data.put("午前の公休を消化する", checkButtonCheck(datareport.getMorningDigestPublicHd()));
			data.put("午前の代休を発生させる", checkButtonCheck(datareport.getMorningGenSubHodiday()));
			data.put("午後の日勤・夜勤時間を求める", checkButtonCheck(datareport.getAfternoonDayNightTimeAsk()));
			data.put("午後の出勤時刻を直行とする", checkButtonCheck(datareport.getAfternoonAttendanceTime()));
			data.put("午後の退勤時刻を直帰とする", checkButtonCheck(datareport.getAfternoonTimeLeaveWork()));
			data.put("午後の休日日数を数える", checkButtonCheck(datareport.getAfternoonCountHodiday()));
			data.put("午後の公休を消化する", checkButtonCheck(datareport.getAfternoonDigestPublicHd()));
			data.put("午後の代休を発生させる", checkButtonCheck(datareport.getAfternoonGenSubHodiday()));
		}
	}

}
