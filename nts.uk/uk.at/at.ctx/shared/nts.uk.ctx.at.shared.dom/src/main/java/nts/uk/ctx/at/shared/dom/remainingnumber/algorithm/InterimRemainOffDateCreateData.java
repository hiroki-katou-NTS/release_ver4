package nts.uk.ctx.at.shared.dom.remainingnumber.algorithm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.FuriClassifi;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.NumberOfDaySuspension;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CareType;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CareUseDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CompanyHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.DayoffTranferInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.EmploymentHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.InforFormerRemainData;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.OccurrenceUseDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.SpecialHolidayUseDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.TranferTimeInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.VacationTimeInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.VacationTimeInforNew;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.VacationUsageTimeDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.WorkTypeRemainInfor;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.service.WorkingConditionService;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSet;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSetAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.subholtransferset.GetDesignatedTime;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeIsFluidWork;
import nts.uk.ctx.at.shared.dom.worktype.HolidayAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSet;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSetCheck;
import nts.uk.ctx.at.shared.dom.worktype.specialholidayframe.SpecialHdFrameNo;

public class InterimRemainOffDateCreateData {
	
	/**
	 * 指定日の暫定残数管理データを作成する
	 * @param cid
	 * @param sid
	 * @param baseDate
	 * @param dayOffTimeIsUse: 時間代休利用: True: 利用, False: 未利用 
	 * @param detailData
	 * @param callFunction 
	 * @return
	 */
	public static DailyInterimRemainMngData createData(RequireM9 require, String cid, String sid, GeneralDate baseDate, boolean dayOffTimeIsUse,
			InterimRemainCreateInfor detailData, CompanyHolidayMngSetting comHolidaySetting, EmploymentHolidayMngSetting employmentHolidaySetting, CallFunction callFunction) {
		//残数作成元情報を作成する
		InforFormerRemainData formerRemainData = createInforFormerRemainData(require, cid, sid, baseDate, detailData, dayOffTimeIsUse,
				comHolidaySetting, employmentHolidaySetting, callFunction);
		if(formerRemainData == null 
				|| formerRemainData.getWorkTypeRemain().isEmpty()) {
			return null;
		}
		//残数作成元情報から暫定残数管理データを作成する
		DailyInterimRemainMngData createDataInterimRemain = createDataInterimRemain(require, formerRemainData);
		return createDataInterimRemain;
	}
	
	/**
	 * 残数作成元情報を作成する
	 * @param sid
	 * @param baseDate
	 * @param detailData
	 * @param callFunction 
	 * @return
	 */
	public static InforFormerRemainData createInforFormerRemainData(
			RequireM9 require, String cid, String sid, GeneralDate baseDate,
			InterimRemainCreateInfor detailData, boolean dayOffTimeIsUse, CompanyHolidayMngSetting comHolidaySetting, 
			EmploymentHolidayMngSetting employmentHolidaySetting, CallFunction callFunction) {
		InforFormerRemainData outputData = new InforFormerRemainData(sid, 
				baseDate, 
				dayOffTimeIsUse, 
				new ArrayList<>(), 
				new ArrayList<>(), 
				new ArrayList<>(), 
				comHolidaySetting,
				employmentHolidaySetting);		
		//最新の勤務種類変更を伴う申請を抽出する
		AppRemainCreateInfor appWithWorkType = getAppWithWorkType(detailData.getAppData(), sid, baseDate);
		if(appWithWorkType == null) {
			//実績をチェックする
			if(detailData.getRecordData().isPresent()) {
				//実績から残数作成元情報を設定する
				return createInterimDataFromRecord(require, cid, detailData.getRecordData().get(), outputData);
			} else {
				//予定をチェックする
				if(detailData.getScheData().isPresent()) {
					//予定から残数作成元情報を設定する
					return createInterimDataFromSche(require, cid, detailData.getScheData().get(), outputData, dayOffTimeIsUse);
				}
			}
		} else {
			//最新の勤務種類変更を伴う申請から残数作成元情報を設定する
			CreateAtr createAtr = appWithWorkType.getPrePosAtr().equals(PrePostAtr.PREDICT)  ? CreateAtr.APPBEFORE : CreateAtr.APPAFTER;
			return createInterimDataFromApp(require, cid, detailData, appWithWorkType, detailData.getRecordData().map(x-> x).orElse(new RecordRemainCreateInfor()) ,
					outputData, dayOffTimeIsUse, createAtr);
		}
		return outputData;
	}
	
	/**
	 * 実績から残数作成元情報を設定する
	 * 
	 * @param require
	 * @param cid
	 *            会社ID
	 * @param recordData
	 *            残数作成元情報(実績)
	 * @param outputData
	 *            残数作成元情報
	 * @return 残数作成元情報
	 */
	public static InforFormerRemainData createInterimDataFromRecord(RequireM9 require, String cid,
			RecordRemainCreateInfor recordData, InforFormerRemainData outputData) {
		//アルゴリズム「勤務種類別残数情報を作成する」を実行する
		List<WorkTypeRemainInfor> dataDetail = createWorkTypeRemainInfor(require, cid, CreateAtr.RECORD, recordData.getWorkTypeCode() , Optional.ofNullable(recordData.getTimeDigestionUsageInfor()),recordData.getNumberDaySuspension());
		if(dataDetail.isEmpty()) {
			return outputData;
		}
		outputData.setWorkTypeRemain(dataDetail);
		//時間休暇使用時間を作成する
		
		List<VacationTimeInfor> vactionTime = createVacationUsageTime(require,cid, dataDetail.get(0).getCreateData(), recordData.getLstVacationTimeInfor(),
				recordData.getWorkTypeCode());
		
		//時間年休使用時間を設定する
		outputData.setVactionTime(vactionTime);
		
		//アルゴリズム「実績から代休振替情報を設定する」を実行する
		DayoffTranferInfor dayOffInfor = createDayOffTranferFromRecord(require, cid, CreateAtr.RECORD, recordData, outputData.isDayOffTimeIsUse());
		List<DayoffTranferInfor> lstOutput = new ArrayList<>();
		lstOutput.add(dayOffInfor);
		outputData.setDayOffTranfer(lstOutput);
		return outputData;
	}

	/**
	 * 時間休暇使用時間を作成する
	 * @param require 
	 * 
	 * @param workTypeCode
	 *            勤務種類コード
	 * @param vacationTimes
	 *            List<時間休暇使用情報>
	 * @param createAtr
	 *            作成元区分
	 * @param cid
	 *            会社ID
	 */
	private static List<VacationTimeInfor> createVacationUsageTime(RequireM8 require, String cid, CreateAtr createAtr, List<VacationTimeInforNew> vacationTimes, String workTypeCode) {
		
		List<VacationTimeInfor> result = new ArrayList<VacationTimeInfor>();
		//時間休暇使用時間を作成
		
		vacationTimes.forEach(vacationTime -> {
			HolidayType[] types = HolidayType.values();
			
			for (int i = 0; i < types.length; i++) {
				AttendanceTime useTime =  getUseTime(vacationTime , types[i]);
				//時間休暇使用時間を設定する
				
				VacationTimeInfor vacation =  SetVacationUsageTime(require,cid, vacationTime, createAtr, types[i], useTime, vacationTime.getSpcVacationFrameNo(),
						workTypeCode);
				if (vacation != null) {
					result.add(vacation);
				}
			}
			
		});
		return result;
	}

	private static AttendanceTime getUseTime(VacationTimeInforNew vacationTime, HolidayType holidayType) {
		switch (holidayType) {
		/** 年休 */
		case ANNUAL:
			return vacationTime.getNenkyuTime();
		/** 代休 */
		case SUBSTITUTE:
			return vacationTime.getKyukaTime();
		/** 特休 */
		case SPECIAL:
			return vacationTime.getSpecialHolidayUseTime();
		/** 60H超休 */
		case SIXTYHOUR:
			return vacationTime.getHChoukyuTime();
		/** 子の看護 */
		case CHILDCARE:
			return vacationTime.getTimeChildCareHolidayUseTime();
		/** 介護 */
		case CARE:
			return vacationTime.getTimeCareHolidayUseTime();
		default:
			return new AttendanceTime(0);
		}
	}

	/**
	 * 時間年休使用時間を設定する
	 * @param require 
	 * 
	 * @param workTypeCode
	 * @param optional
	 * @param useTime
	 * @param type 休暇種類
	 * @param createAtr
	 * @param vacationTime
	 * @param cid
	 * @return 
	 */
	private static VacationTimeInfor SetVacationUsageTime(RequireM8 require, String cid,
			VacationTimeInforNew vacationTime, CreateAtr createAtr, HolidayType type, AttendanceTime useTime,
			Optional<SpecialHdFrameNo> specialNo, String workTypeCode) {

		if (useTime.v() <= 0) {
			// 使用時間 <= 0
			return null;
		}
		Integer special = null;
		if (type.equals(HolidayType.SPECIAL)) {
			// 休暇種類 = 特休
			// 特休コード取得
			special = getSpecialByNo(require, cid, specialNo);
			if (special == null) {
				// 特別休暇＝設定なし
				return null;
			}
		}
		// 時間年休使用時間を設定する
		return new VacationTimeInfor(vacationTime.getTimeType(), createAtr, workTypeCode,
				Arrays.asList(new VacationUsageTimeDetail(type, useTime.v(), Optional.ofNullable(special))));
	}

	/**
	 * 
	 * @param require 
	 * @param cid 会社ID
	 * @param specialNo 特別休暇枠NO
	 * @return 
	 */
	private static Integer getSpecialByNo(RequireM8 require, String cid, Optional<SpecialHdFrameNo> specialNo) {
		if(!specialNo.isPresent()){
			return null;
		}
		List<Integer> specialHolidays = require.getSpecialHolidayNumber(cid, specialNo.get().v());
		if(specialHolidays.isEmpty()){
			return null;
		}
		
		return specialHolidays.get(0);
	}

	/**
	 * 勤務種類別残数情報を作成する
	 * 
	 * @param cid
	 *            会社ID
	 * @param createAtr
	 *            作成元区分
	 * @param workTypeCode
	 *            勤務種類コード
	 * @param numberOfDayOpt
	 *            振休振出として扱う日数
	 * @param timedigOpt
	 *            時間消化使用情報
	 * @return List<勤務種類別残数情報>
	 */
	public static List<WorkTypeRemainInfor> createWorkTypeRemainInfor(
			RequireM1 require, 
			String cid,
			CreateAtr createAtr, 
			String workTypeCode, 
			Optional<TimeDigestionUsageInfor> timedigOpt,
			Optional<NumberOfDaySuspension> numberOfDayOpt) {
		WorkTypeRemainInfor outputData = new WorkTypeRemainInfor(null, null, null, new ArrayList<>(), new ArrayList<>(),
				new ArrayList<>());
		List<WorkTypeRemainInfor> lstOutputData = new ArrayList<>();
		//ドメインモデル「勤務種類」を取得する
		Optional<WorkType> optWorkTypeData = require.workType(cid, workTypeCode);
		
		if(!optWorkTypeData.isPresent()) {
			return lstOutputData;
		}
		WorkType workType = optWorkTypeData.get();
		//勤務種類別残数情報を作成する
		outputData.setCreateData(createAtr);
		outputData.setWorkTypeCode(workTypeCode);
		outputData.setOccurrenceDetailData(createDetailData());
		
		FuriClassifi furiClass = numberOfDayOpt.map(x -> x.getClassifiction()).orElse(null);

		double days = numberOfDayOpt.map(x -> x.getDays().v()).orElse(Double.valueOf(0));
		
		if(workType.getDailyWork().isOneDay()) {
			//アルゴリズム「残数発生使用明細を作成する」を実行する (Thực hiện thuật toán "Tạo chi tiết sử dụng phát sinh số lượng tồn")
			outputData = createWithOneDayWorkType(require, cid, workType, WorkAtr.OneDay, 1, outputData, timedigOpt, furiClass,
					days);
			lstOutputData.add(outputData);
			//勤務区分をチェックする
			return lstOutputData;
		} else {
		
			//振休振出として扱う日数の内休暇系の勤務種類の日数を求める
			TotalNumberOfDay totalNumberOfDay = getNumberOfHoliday(numberOfDayOpt, workType, WorkAtr.Monring,
					WorkAtr.Afternoon, outputData);
			
			WorkTypeRemainInfor morning = null;
			WorkTypeRemainInfor after = null;
			//午前
			WorkTypeClassification workTypeMorning = workType.getDailyWork().getMorning();
			if(lstZansu().contains(workTypeMorning)) {
				
				//アルゴリズム「残数発生使用明細を作成する」を実行する (Thực hiện thuật toán 「残数発生使用明細を作成する」 )
				morning = createWithOneDayWorkType(require, cid, workType, WorkAtr.Monring, 0.5, outputData, timedigOpt, furiClass,
						totalNumberOfDay.getTotalNumberMorning());
				lstOutputData.add(morning);
			}
			//午後
			WorkTypeClassification workTypAfternoon = workType.getDailyWork().getAfternoon();
			if(lstZansu().contains(workTypAfternoon)) {
				//アルゴリズム「残数発生使用明細を作成する」を実行する(Thực hiện thuật toán 「残数発生使用明細を作成する」 )
				after = createWithOneDayWorkType(require, cid, workType, WorkAtr.Afternoon, 0.5, outputData, timedigOpt, furiClass,
						totalNumberOfDay.getTotalNumberAfternoon());
				lstOutputData.add(after);
			}
			//午前と午後で同じ勤務種類の残数発生明細をまとめる
			totalMorningAndAfternoonRemain(morning, after);
		}
		//勤務区分をチェックする
		return lstOutputData;
	}

	/**
	 * 午前と午後で同じ勤務種類の残数発生明細をまとめる
	 * 
	 * @param after
	 *            午前の残数発生使用明細
	 * @param morning
	 *            午後の残数発生使用明細
	 */
	private static void totalMorningAndAfternoonRemain(WorkTypeRemainInfor morning, WorkTypeRemainInfor after) {
		// 対象勤務種類
		if (morning == null) {
			return;
		}
		TargetWorkTypes().forEach(wkType -> {
			morning.getOccurrenceDetailData().stream()
					.filter(morningDetail -> morningDetail.getWorkTypeAtr().equals(wkType)).findFirst()
					.ifPresent(morningDetail -> {
						morningDetail.setDays(1);
					});
		});

	}
	
	/**
	 * 対象勤務種類
	 */

	private static List<WorkTypeClassification> TargetWorkTypes() {

		List<WorkTypeClassification> result = new ArrayList<WorkTypeClassification>();
		result.add(WorkTypeClassification.AnnualHoliday);
		result.add(WorkTypeClassification.YearlyReserved);
		result.add(WorkTypeClassification.SubstituteHoliday);
		result.add(WorkTypeClassification.Pause);
		//không có 公休 
		//result.add(WorkTypeClassification.AnnualHoliday);
		result.add(WorkTypeClassification.HolidayWork);
		result.add(WorkTypeClassification.Shooting);

		return result;

	}

	/**
	 * 振休振出として扱う日数の内休暇系の勤務種類の日数を求める
	 * @param workTypAfternoon 
	 * @param workTypeMorning 
	 * @param workType 
	 * @param numberOfDay 
	 */
	private static TotalNumberOfDay getNumberOfHoliday(Optional<NumberOfDaySuspension> numberOfDay, WorkType workType,
			WorkAtr workAtrMorning, WorkAtr workAtrAfternoon, WorkTypeRemainInfor outputData) {

		TotalNumberOfDay result = new TotalNumberOfDay();
		WorkTypeClassification wkType = outputData.getWorkTypeClass();
		numberOfDay.ifPresent(x -> {
			if (x.getClassifiction().equals(FuriClassifi.DRAWER)) {
				// 1日の休暇系日数合計＝0
				double totalHoliday = 0;
				// 午前の勤務種類が休暇系か
				if (isHasWorkClass(wkType, workType, workAtrMorning)) {
					totalHoliday = totalHoliday + 0.5;
				}
				// 午後の勤務種類が休暇系か
				if (isHasWorkClass(wkType, workType, workAtrAfternoon)) {
					totalHoliday = totalHoliday + 0.5;
				}
				// INPUT．振休振出として扱う．日数 = 1 and 1日の休暇系日数合計 = 1
				if (Double.compare(totalHoliday, 1) == 0 && Double.compare(x.getDays().v(), 1) == 0) {
					result.setTotalNumberMorning(0.5);
					result.setTotalNumberAfternoon(0.5);
				}

				if ((Double.compare(totalHoliday, 0.5) == 0 && Double.compare(x.getDays().v(), 1) == 0)
						|| (Double.compare(totalHoliday, 1) == 0 && Double.compare(x.getDays().v(), 0.5) == 0)) {
					boolean isMorning = isHasWorkClass(wkType, workType, workAtrMorning);
					boolean isAfternoon = isHasWorkClass(wkType, workType, workAtrAfternoon);

					if ((isMorning && !isAfternoon) || (isMorning && isAfternoon)) {
						result.setTotalNumberMorning(0.5);
						result.setTotalNumberAfternoon(0);
					}
					if ((!isMorning && isAfternoon)) {
						result.setTotalNumberMorning(0);
						result.setTotalNumberAfternoon(0.5);
					}

				}

				// 1日の休暇系日数合計 = 0
				if (Double.compare(totalHoliday, 0) == 0) {
					result.setTotalNumberMorning(0);
					result.setTotalNumberAfternoon(0);
				}

			} else {
				if (x.getClassifiction().equals(FuriClassifi.SUSPENSION)) {
					result.setTotalNumberAfternoon(x.getDays().v());
					result.setTotalNumberMorning(x.getDays().v());
				}
			}
		});
		return result;
	}

	/**
	 * 残数発生使用明細を作成する
	 * 
	 * @param require
	 * @param cid
	 *            会社ID
	 * @param workType
	 *            勤務種類
	 * @param day
	 *            日数
	 * @param workAtr
	 * @param dataOutput
	 * @param timedigOpt
	 *            時間消化使用情報
	 * @param furiClassifi
	 *            振休振出として扱う区分
	 * @param usedDays
	 *            振休振出として扱う日数
	 * @return 
	 */
	public static WorkTypeRemainInfor createWithOneDayWorkType(RequireM8 require, String cid, WorkType workType, WorkAtr workAtr,
			double day, WorkTypeRemainInfor dataOutput, Optional<TimeDigestionUsageInfor> timedigOpt,
			FuriClassifi furiClassifi, Double usedDays) {
		
		WorkTypeClassification wkClasssifi = null;
				
		if (workAtr.equals(WorkAtr.OneDay)) {
			wkClasssifi = workType.getDailyWork().getOneDay();
		}
		if (workAtr.equals(WorkAtr.Monring)) {
			wkClasssifi = workType.getDailyWork().getMorning();
		}
		if (workAtr.equals(WorkAtr.Afternoon)) {
			wkClasssifi = workType.getDailyWork().getAfternoon();
		}
		
				
		dataOutput.setWorkTypeClass(wkClasssifi);
		//アルゴリズム「残数発生使用対象の勤務種類の分類かを判定」を実行する
		JudgmentTypeOfWorkType judmentType = judgmentType(wkClasssifi);
		if(judmentType == JudgmentTypeOfWorkType.REMAINOCCNOTCOVER) {
			return dataOutput;
		}
		
		//振休振出として扱う日数をチェックする
		checkNumberDays(cid, wkClasssifi, workAtr, furiClassifi, usedDays, workType, dataOutput);
		
		List<WorkTypeSet> workTypeSetList = workType.getWorkTypeSetList();
		
		
		//勤務種類の分類をチェックする
		switch (wkClasssifi) {
			case SpecialHoliday:
				//INPUT.勤務種類の分類＝特別休暇
				// 特休使用明細を追加する
				List<SpecialHolidayUseDetail> lstSpeUseDetail = new ArrayList<>(dataOutput.getSpeHolidayDetailData());
				
				workTypeSetList.stream().filter(x-> x.getWorkAtr().equals(WorkAtr.OneDay)).findFirst().ifPresent(x->{
					// アルゴリズム「特別休暇枠NOから特別休暇を取得する」を実行する
					List<Integer> holidaySpecialCd = require.getSpecialHolidayNumber(cid, x.getSumSpHodidayNo());
					if (!holidaySpecialCd.isEmpty()) {
						SpecialHolidayUseDetail detailData = new SpecialHolidayUseDetail(holidaySpecialCd.get(0), day);
						lstSpeUseDetail.add(detailData);
					}
				});

				dataOutput.setSpeHolidayDetailData(lstSpeUseDetail);
				//子の看護介護の残数発生使用明細を設定する
				setChildCare(require, cid, day, workTypeSetList, dataOutput);
				break;
	
			case HolidayWork:
				//代休を発生させるかをチェックする
				if (!workTypeSetList.stream().filter(x -> x.getGenSubHodiday().equals(WorkTypeSetCheck.CHECK))
						.collect(Collectors.toList()).isEmpty()) {
					// 勤務種類の分類に対応する残数発生使用明細を設定する
					setData(dataOutput, day, dataOutput.getWorkTypeClass());
				}
			case Holiday:
			case Pause:
			case Shooting:
				//公休の残数発生使用明細を設定する
				setNumberHolidays(cid, workType,workAtr, dataOutput,day);
				if (wkClasssifi.equals(WorkTypeClassification.Pause) || wkClasssifi.equals(WorkTypeClassification.Shooting)) {
					// 勤務種類の分類に対応する残数発生使用明細を設定する
					setData(dataOutput, day, wkClasssifi);
				}
				break;
				
			case TimeDigestVacation:
				//時間休暇使用時間詳細を設定する
				setTimeVacation(timedigOpt, dataOutput);
				// 勤務種類の分類に対応する残数発生使用明細を設定する
				setData(dataOutput, day, wkClasssifi);
				break;
			default:
				break;
			}

		return dataOutput;
	}

	/**
	 * 子の看護介護の残数発生使用明細を設定する
	 * 
	 * @param dataOutput
	 * @param cid
	 *            会社ID
	 * @param workTypeSetList
	 *            一日の勤務
	 * @param day
	 *            日数
	 * @param require 
	 */
	private static void setChildCare(RequireM8 require, String cid, double day, List<WorkTypeSet> workTypeSetList,
			WorkTypeRemainInfor dataOutput) {
		
		workTypeSetList.forEach(x -> {
			// 子の看護介護の勤務種類か判断する
			if (require.checkChildCare(x, cid)) {
				dataOutput.setChildCareDetailData(Arrays.asList(new CareUseDetail(CareType.ChildNursing, day)));
			}
		});
		
	}

	/**
	 * 公休の残数発生使用明細を設定する
	 * 
	 * @param cid
	 * @param workType
	 * @param workAtr
	 * @param dataOutput
	 * @param day
	 */
	private static void setNumberHolidays(String cid, WorkType workType, WorkAtr workAtr,
			WorkTypeRemainInfor dataOutput, double day) {
		//公休消化するか確認
		if(checkIsHoliday(workType, workAtr)){
			dataOutput.getOccurrenceDetailData().forEach(x->{
				x.setWorkTypeAtr(WorkTypeClassification.Holiday);
				x.setUseAtr(true);
				x.setDays(day);
			});
		}
	}
	
	/**
	 * 時間休暇使用時間明細を設定する
	 * 
	 * @param timedigOpt
	 *            時間消化使用情報
	 * @param dataOutput 
	 */
	private static void setTimeVacation(Optional<TimeDigestionUsageInfor> timedigOpt, WorkTypeRemainInfor dataOutput) {
		dataOutput.getOccurrenceDetailData().forEach(x -> {
			x.setVacationUsageTimeDetails(createVacationUsageTimeDetails(timedigOpt));
		});
	}

	private static List<VacationUsageTimeDetail> createVacationUsageTimeDetails(
			Optional<TimeDigestionUsageInfor> timedigOpt) {

		List<VacationUsageTimeDetail> result = new ArrayList<VacationUsageTimeDetail>();

		result.add(new VacationUsageTimeDetail(HolidayType.ANNUAL, timedigOpt.map(x -> x.getNenkyuTime()).orElse(0),
				Optional.empty()));

		result.add(new VacationUsageTimeDetail(HolidayType.SUBSTITUTE, timedigOpt.map(x -> x.getKyukaTime()).orElse(0),
				Optional.empty()));

		result.add(new VacationUsageTimeDetail(HolidayType.SIXTYHOUR,
				timedigOpt.map(x -> x.getHChoukyuTime()).orElse(0), Optional.empty()));

		result.add(new VacationUsageTimeDetail(HolidayType.CHILDCARE,
				timedigOpt.map(x -> x.getChildCareTime()).orElse(0), Optional.empty()));

		result.add(new VacationUsageTimeDetail(HolidayType.CARE, timedigOpt.map(x -> x.getLongCareTime()).orElse(0),
				Optional.empty()));

		return result;
	}

	/**
	 * 振休振出として扱う日数をチェックする - thực ra hàm này là hàm set nhưng tiếng nhật tên như
	 * vậy nên cứ để như thế
	 * 
	 * @param cid
	 *            会社ID
	 * @param workClass
	 *            勤務種類の分類
	 * @param workAtr
	 * @param furiClassifi
	 *            振休振出として扱う区分
	 * @param usedDays
	 *            振休振出として扱う日数
	 * @param workType
	 * @param dataOutput
	 *            勤務種類別残数情報
	 */
	private static void checkNumberDays(String cid, WorkTypeClassification workClass,
			WorkAtr workAtr, FuriClassifi furiClassifi, Double usedDays, WorkType workType, WorkTypeRemainInfor dataOutput) {
		// 振休振出として扱う区分 = NULL
		if (furiClassifi == null) {
			return;
		}
		// 振休振出として扱う区分 != NULL
		if (furiClassifi.equals(FuriClassifi.SUSPENSION)) {
			// 区分 ＝ 振休
			// 勤務種類が休日出勤か
			if (workClass.isHolidayWork()) {
				dataOutput.getOccurrenceDetailData().forEach(x -> {
					x.setWorkTypeAtr(WorkTypeClassification.Pause);
					x.setUseAtr(true);
					x.setDays(usedDays);
				});
			}
		}

		if (furiClassifi.equals(FuriClassifi.DRAWER)) {
			// 区分 = 振出
			// 勤務種類が休暇系か
			if (workClass.isHolidayWorkType()) {
				// 勤務種類が休日か
				if (isHasWorkClass(workClass, workType, workAtr)) {
					dataOutput.getOccurrenceDetailData().forEach(x -> {
						x.setWorkTypeAtr(WorkTypeClassification.Shooting);
						x.setUseAtr(true);
						x.setDays(usedDays);
					});
				}
			}

		}

	}
	
	/**
	 * 勤務種類が休暇系か判断
	 * 
	 * @param workAtr
	 *            workAtr
	 * @param workType
	 *            勤務種類の分類
	 * @param wkClass
	 *            勤務種類の分類
	 */
	
	private static boolean isHasWorkClass(WorkTypeClassification wkClass, WorkType workType, WorkAtr workAtr) {

		// 勤務種類が休暇系か
		if (wkClass.isHolidayWorkType()) {
			// 勤務種類 = 休暇系
			if (wkClass.isHoliday()) {
				// 勤務種類 = 休日
				// 公休かチェック
				return decideDigestHolidays(workType, workAtr);
			}
			// その他
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 公休消化するか判断
	 * 
	 * @param workType
	 * @param workAtr
	 * @return
	 */
	private static boolean decideDigestHolidays(WorkType workType, WorkAtr workAtr) {

		if (workAtr.equals(WorkAtr.OneDay)) {
			// 公休消化するか判断（１日）
			return checkIsHoliday(workType, WorkAtr.OneDay);
		}

		return checkIsHoliday(workType, WorkAtr.Monring) || checkIsHoliday(workType, WorkAtr.Afternoon);

	}

	/**
	 * 公休消化するか確認
	 * 
	 * @param workType
	 * @param workAtr
	 * @return
	 */
	private static boolean checkIsHoliday(WorkType workType, WorkAtr workAtr) {

		//WorkType.workTypeSetList.filter(WorkAtr=input.WorkAtr).map(c->c.休日区分)
		
		List<HolidayAtr> holidays = workType.getWorkTypeSetList().stream()
				.filter(ts -> ts.getWorkAtr().equals(workAtr)).map(x -> x.getHolidayAtr())
				.collect(Collectors.toList());

		if (holidays.isEmpty()) {
			return false;
		}
		
		//WorkType.workTypeSetList.filter(WorkAtr=input.WorkAtr).map(c->c.公休を消化する)
		return workType.getWorkTypeSetList().stream().filter(ts -> ts.getWorkAtr().equals(workAtr)).findFirst()
				.map(x -> x.getDigestPublicHd().equals(WorkTypeSetCheck.CHECK)).orElse(false);

	}

	/**
	 * 午前と午後勤務時の残数発生使用明細を作成する
	 * @param workType
	 * @param dataOutput
	 * @return
	 */
	public static WorkTypeRemainInfor createWithHaftDayWorkType(WorkType workType, WorkTypeRemainInfor dataOutput) {
		//午前
		//アルゴリズム「残数発生使用対象の勤務種類の分類かを判定」を実行する
		JudgmentTypeOfWorkType judgmentTypeAM = judgmentType(workType.getDailyWork().getMorning());
		if(judgmentTypeAM == JudgmentTypeOfWorkType.REMAINOCCNOTCOVER) {
			return dataOutput;
		}
		//勤務種類の分類に対応する残数発生使用明細を設定する		
		setData(dataOutput, 0.5, workType.getDailyWork().getMorning());
		//午後
		//アルゴリズム「残数発生使用対象の勤務種類の分類かを判定」を実行する
		JudgmentTypeOfWorkType judgmentTypePM = judgmentType(workType.getDailyWork().getAfternoon());
		if(judgmentTypePM == JudgmentTypeOfWorkType.REMAINOCCNOTCOVER) {
			return dataOutput;
		}
		//勤務種類の分類に対応する残数発生使用明細を設定する
		setData(dataOutput, 0.5, workType.getDailyWork().getAfternoon());
		return dataOutput;
	}
	
	/**
	 * 実績から代休振替情報を作成する
	 * @param recordData
	 * @param dayOffTimeIsUse 時間代休利用
	 * @return
	 */
	public static DayoffTranferInfor createDayOffTranferFromRecord(RequireM4 require, String cid, 
			CreateAtr createAtr, RecordRemainCreateInfor recordData,
			boolean dayOffTimeIsUse) {
		//代休振替情報を作成する
		Optional<TranferTimeInfor> tranferBreakTime = Optional.of(new TranferTimeInfor(createAtr, recordData.getTransferTotal(), Optional.empty()));
		DayoffTranferInfor outputData = new DayoffTranferInfor(recordData.getWorkTimeCode(), tranferBreakTime, Optional.empty());
		//アルゴリズム「代休振替時間を算出する」を実行する
		String workTimeCode = recordData.getWorkTimeCode().isPresent() ? recordData.getWorkTimeCode().get() : "";
		
		tranferBreakTime = calDayoffTranferTime(require, cid, createAtr, workTimeCode, recordData.getTransferTotal(), DayoffChangeAtr.BREAKTIME);
		outputData.setTranferBreakTime(tranferBreakTime);
		//アルゴリズム「実績から振替残業時間を作成する」を実行する
		if(dayOffTimeIsUse) {
			Optional<TranferTimeInfor> tranferOvertime = calDayoffTranferTime(require, cid, createAtr, workTimeCode, recordData.getTransferOvertimesTotal(), DayoffChangeAtr.OVERTIME);
			outputData.setTranferOverTime(tranferOvertime);
		}
		return outputData;
	}

	/**
	 * 残数作成元情報から暫定残数管理データを作成する
	 * @param inforData
	 * @return
	 */
	public static DailyInterimRemainMngData createDataInterimRemain(RequireM6 require, InforFormerRemainData inforData) {
		DailyInterimRemainMngData outputData = new DailyInterimRemainMngData(Optional.empty(), new ArrayList<>(), Optional.empty(), 
				Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), new ArrayList<>());
		
		List<WorkTypeRemainInfor> workTypeRemain = inforData.getWorkTypeRemain();
		
		if(!workTypeRemain.isEmpty()) {
		//暫定残数管理データ(List)を作成する
		for (WorkTypeRemainInfor workTypeInfor : workTypeRemain) {
			WorkTypeClassification wkCls = workTypeInfor.getWorkTypeClass();
			//各種暫定残数管理データを作成する
			switch (wkCls) {
				//振出
				case Shooting:
					outputData = TempRemainCreateEachData.createInterimRecData(require, inforData, wkCls, outputData);
					break;
				// 休出
				case HolidayWork:
					outputData = TempRemainCreateEachData.createInterimBreak(require, inforData, wkCls, outputData);
					break;
				// 休日
				case Holiday:
					outputData = TempRemainCreateEachData.createInterimHolidayData(inforData, wkCls, outputData);
					break;
				// 振休
				case Pause:
					outputData = TempRemainCreateEachData.createInterimAbsData(inforData, wkCls, outputData);
					break;
				// 代休
				case SubstituteHoliday:
					outputData = TempRemainCreateEachData.createInterimDayOffData(inforData, wkCls, outputData);
					break;
				// 特休
				case SpecialHoliday:
					outputData = TempRemainCreateEachData.createInterimSpecialHoliday(inforData, wkCls, outputData);
					break;
				// 年休
				case AnnualHoliday:
					outputData = TempRemainCreateEachData.createInterimAnnualHoliday(inforData, wkCls, outputData);
					break;
				// 積立年休
				case YearlyReserved:
					outputData = TempRemainCreateEachData.createInterimReserveHoliday(inforData, wkCls, outputData);
					break;
				// 時間消化
				case TimeDigestVacation:
					outputData = TempRemainCreateEachData.createInterimDigestVacation(inforData, wkCls, outputData);
					break;
			
				default:
					break;
				}
			
				List<CareUseDetail> cares = workTypeInfor.getChildCareDetailData();
				if (!cares.isEmpty()) {
					for (CareUseDetail care : cares) {
						switch (care.getCareType()) {
						// 子の看護
						case ChildNursing:
							TempRemainCreateEachData.createInterimChildNursing(inforData, care, wkCls, outputData);
						// 介護
						case Nursing:
							TempRemainCreateEachData.createInterimNursing(inforData, care, wkCls, outputData);
						}
					}
				}
			}
		}

		//時間暫定残数管理データを作成する
		
		// 時間年休
		outputData.getRecAbsData().addAll(TempRemainCreateEachData.createAnnualHolidayTime(inforData));
		// 時間代休
		outputData.getRecAbsData().addAll(TempRemainCreateEachData.createSubstituteHolidayTime(inforData));
		// 時間特休
		outputData.getRecAbsData().addAll(TempRemainCreateEachData.createSpecialHolidayTime(inforData));
		// 60H超休
		outputData.getRecAbsData().addAll(TempRemainCreateEachData.createHolidayOver60hTime(inforData));
		// 時間子の看護
		outputData.getRecAbsData().addAll(TempRemainCreateEachData.createChildCareTime(inforData));
		// 時間介護
		outputData.getRecAbsData().addAll(TempRemainCreateEachData.createCareTime(inforData));
		//作成された暫定残数管理データをListに追加する
		
		
		return outputData;
	}

	

	

	/**
	 * 予定から残数作成元情報を設定する
	 * @param scheData
	 * @param outputData
	 * @return
	 */
	public static InforFormerRemainData createInterimDataFromSche(RequireM6 require, String cid, ScheRemainCreateInfor scheData,
			InforFormerRemainData outputData, boolean dayOffTimeIsUse) {
		String workTimeCode = scheData.getWorkTimeCode().isPresent() ? scheData.getWorkTimeCode().get() : "";
		//アルゴリズム「勤務種類別残数情報を作成する」を実行する
		List<WorkTypeRemainInfor> remainInfor = createWorkTypeRemainInfor(require, cid, CreateAtr.SCHEDULE, scheData.getWorkTypeCode(), Optional.ofNullable(scheData.getTimeDigestionUsageInfor()),scheData.getNumberDaySuspension());
		
		//勤務種類別残数情報を設定する
		outputData.setWorkTypeRemain(remainInfor);
		
		//時間休暇使用時間を作成する
		List<VacationTimeInfor> vactionTime = createVacationUsageTime(require, cid, remainInfor.get(0).getCreateData(),
				scheData.getLstVacationTimeInfor(), scheData.getWorkTypeCode());
		// 時間年休使用時間を設定する
		outputData.setVactionTime(vactionTime);
		//アルゴリズム「就業時間帯から代休振替情報を作成する」を実行する
		List<DayoffTranferInfor> tranferData = createDayoffFromWorkTime(require, cid, remainInfor, workTimeCode, null, 
				CreateAtr.SCHEDULE, null, dayOffTimeIsUse);
		outputData.setDayOffTranfer(tranferData);
		return outputData;
	}

	/**
	 * 就業時間帯から代休振替情報を作成する
	 * 
	 * @param cid:             会社ID
	 * @param remainInfor
	 * @param workTimeCode
	 * @param isTranferTime:   True: 設定あり, False: 設定なし
	 * @param timeSetting:     休出振替可能時間
	 * @param timeOverSetting: 実績残業振替時間
	 * @param dayOffTimeIsUse: 時間代休利用
	 * @return
	 */
	public static List<DayoffTranferInfor> createDayoffFromWorkTime(RequireM5 require, String cid, 
			List<WorkTypeRemainInfor> remainInfor, String workTimeCode,
			Integer timeSetting, CreateAtr createAtr, Integer timeOverSetting, boolean dayOffTimeIsUse) {
		
		List<DayoffTranferInfor> lstOutput = new ArrayList<>();
		for (WorkTypeRemainInfor workTypeInfor : remainInfor) {
			//アルゴリズム「代休を発生させる勤務種類かを判定する」を実行する
			boolean chkDayOff = checkDayoffOcc(workTypeInfor);
			if(!chkDayOff) {
				continue;
			}
			//振替休出時間を作成する
			Optional<TranferTimeInfor> transferBreak = createTranferBreak(timeSetting, require, workTimeCode,
					workTypeInfor,createAtr,cid);
			//振替残業時間を作成する
			Optional<TranferTimeInfor> transferOver = createTranferOver(timeOverSetting, require, workTimeCode,
					workTypeInfor,createAtr,cid,dayOffTimeIsUse);
			
			if (transferBreak.isPresent() || transferOver.isPresent()) {
				lstOutput.add(new DayoffTranferInfor(Optional.of(workTimeCode), transferBreak, transferOver));
			}
		}
		
		return lstOutput;
	}

	private static Optional<TranferTimeInfor> createTranferOver(Integer timeOverSetting, RequireM5 require,
			String workTimeCode, WorkTypeRemainInfor workTypeInfor, CreateAtr createAtr, String cid,
			boolean dayOffTimeIsUse) {

		// 振替可能時間をチェックする
		if (timeOverSetting == null) {
			return Optional.empty();
		}

		if (dayOffTimeIsUse) {
			// アルゴリズム「代休振替時間を算出する」を実行する
			Optional<TranferTimeInfor> calTime = calDayoffTranferTime(require, cid, createAtr, workTimeCode,
					timeOverSetting, DayoffChangeAtr.BREAKTIME);
			if (calTime.isPresent()) {
				return calTime;
			}
		}

		return Optional.empty();
	}

	/**
	 * 振替休出時間を作成する
	 * 
	 * @param timeSetting
	 * @param require
	 * @param workTimeCode
	 * @param workTypeInfor
	 * @param createAtr
	 * @param cid
	 * @return
	 */
	private static Optional<TranferTimeInfor> createTranferBreak(Integer timeSetting, RequireM5 require,
			String workTimeCode, WorkTypeRemainInfor workTypeInfor, CreateAtr createAtr, String cid) {

		
		// 振替可能時間をチェックする
		if (timeSetting == null) {
			// INPUT.休出振替可能時間＝設定なし
			// アルゴリズム「所定時間を取得」を実行する
			timeSetting = WorkTimeIsFluidWork.getTimeByWorkTimeTypeCode(require, workTimeCode,
					workTypeInfor.getWorkTypeCode());
		}

		// アルゴリズム「代休振替時間を算出する」を実行する
		Optional<TranferTimeInfor> calTime = calDayoffTranferTime(require, cid, createAtr, workTimeCode, timeSetting,
				DayoffChangeAtr.BREAKTIME);

		if (calTime.isPresent()) {
			// 終了状態：正常終了
			return  calTime;
		}

		return  Optional.empty();
	}

	/**
	 * 代休振替時間を算出する
	 * @param workTimeCode
	 * @param timeSetting
	 * @param dayoffChange
	 * @return
	 */
	public static Optional<TranferTimeInfor> calDayoffTranferTime(RequireM4 require, String cid, CreateAtr createAtr,String workTimeCode, Integer timeSetting, DayoffChangeAtr dayoffChange) {
		//アルゴリズム「代休振替設定を取得」を実行する
		Optional<SubHolTransferSet> optDayOffTranferSetting = GetDesignatedTime.get(require, cid, workTimeCode);
		if(!optDayOffTranferSetting.isPresent()) {
			return Optional.empty();
		}
		//使用区分をチェックする
		SubHolTransferSet transferSetting = optDayOffTranferSetting.get();
		if(!transferSetting.isUseDivision()) {
			return Optional.empty();
		}
		//振替区分をチェックする
		if(transferSetting.getSubHolTransferSetAtr() == SubHolTransferSetAtr.CERTAIN_TIME_EXC_SUB_HOL) {
			//一定時間の振替処理を行う
			return Optional.of(new TranferTimeInfor(createAtr, processCertainTime(transferSetting, timeSetting), Optional.empty()));
		} else {
			//指定時間の振替処理を行う
			return Optional.of(processDesignationTime(transferSetting, timeSetting, createAtr));			
		}
	}
	
	/**
	 * 最新の勤務種類変更を伴う申請から残数作成元情報を設定する
	 * @param appInfor
	 * @param recordRemainCreateInfor 
	 * @param outputData
	 * @param dayOffTimeIsUse
	 * @return
	 */
	public static InforFormerRemainData createInterimDataFromApp(RequireM7 require, String cid, InterimRemainCreateInfor createInfo, AppRemainCreateInfor appInfor,
			RecordRemainCreateInfor recordData, InforFormerRemainData outputData, boolean dayOffTimeIsUse, CreateAtr createAtr) {
		String workTypeCode = appInfor.getWorkTypeCode().map(x -> x).orElse("");
		//アルゴリズム「勤務種類別残数情報を作成する」を実行する
		List<WorkTypeRemainInfor> remainInfor = createWorkTypeRemainInfor(require, cid, createAtr, workTypeCode,
				appInfor.getTimeDigestionUsageInfor(), recordData.getNumberDaySuspension());
		if(remainInfor.isEmpty()) {
			return null;
		}
		outputData.setWorkTypeRemain(remainInfor);

		//時間休暇使用時間を作成する
		List<VacationTimeInfor> vactionTimes = createVacationUsageTime(require,cid, remainInfor.get(0).getCreateData(), appInfor.getVacationTimes(),
				workTypeCode);
		//時間年休使用時間を設定する
		outputData.setVactionTime(vactionTimes);
		//申請種類をチェックする
		if(appInfor.getAppType() == ApplicationType.BREAK_TIME_APPLICATION) {
			// 休日出勤申請から代休振替情報を作成する
			List<DayoffTranferInfor> tranferInforFromHoliday = tranferInforFromHolidayWork(require, cid, dayOffTimeIsUse, appInfor,
					remainInfor, createAtr);
			outputData.setDayOffTranfer(tranferInforFromHoliday);
		} else {
			// 休日出勤以外の申請から代休振替情報を作成する
			List<DayoffTranferInfor> tranferInforNotFromHoliday = transferInforFromNotHolidayWork(
					require, cid, dayOffTimeIsUse, appInfor, createInfo, remainInfor, createAtr);
			outputData.setDayOffTranfer(tranferInforNotFromHoliday);
		}
		
		return outputData;
	}


	/**
	 * 休日出勤申請から代休振替情報を作成する
	 * @param dayOffTimeIsUse
	 * @param appInfor
	 * @return
	 */
	public static List<DayoffTranferInfor> tranferInforFromHolidayWork(RequireM5 require, String cid, 
			boolean dayOffTimeIsUse, AppRemainCreateInfor appInfor,
			List<WorkTypeRemainInfor> remainInfor, CreateAtr createAtr) {
		Integer breakTime = 0;
		//時間代休利用をチェックする
		if(dayOffTimeIsUse) {
			breakTime = appInfor.getAppOvertimeTimeTotal().isPresent() ? appInfor.getAppOvertimeTimeTotal().get() : 0;
		} else {
			breakTime = appInfor.getAppBreakTimeTotal().isPresent() ? appInfor.getAppBreakTimeTotal().get() : 0;
		}
		//アルゴリズム「就業時間帯から代休振替情報を作成する」を実行する
		String workTimeCode = appInfor.getWorkTimeCode().isPresent() ? appInfor.getWorkTimeCode().get() : "";
		
		
		return createDayoffFromWorkTime(require, cid, remainInfor, workTimeCode, breakTime, createAtr, 0, dayOffTimeIsUse);
	}

	/**
	 * 休日出勤以外の申請から代休振替情報を作成する
	 * @param dayOffTimeIsUse
	 * @param appInfor
	 * @param remainInfor
	 * @return
	 */
	public static List<DayoffTranferInfor> transferInforFromNotHolidayWork(RequireM3 require, String cid, 
			boolean dayOffTimeIsUse, AppRemainCreateInfor appInfor,
			InterimRemainCreateInfor createInfo, List<WorkTypeRemainInfor> remainInfor, CreateAtr createAtr) {
		// 休出以外の申請が利用する振替用就業時間帯コードを取得する
		String workTimeCode = workTimeCode(require, createInfo, appInfor.getSid(), appInfor.getAppDate());
		//実績をチェックする
		if(createInfo.getRecordData().isPresent()) {
			//アルゴリズム「就業時間帯から代休振替情報を作成する」を実行する
			RecordRemainCreateInfor recordData = createInfo.getRecordData().get();
			return createDayoffFromWorkTime(require, cid, remainInfor, workTimeCode, recordData.getTransferTotal(), createAtr, recordData.getTransferOvertimesTotal(), dayOffTimeIsUse);			
		} else {
			//アルゴリズム「就業時間帯から代休振替情報を作成する」を実行する
			return createDayoffFromWorkTime(require, cid, remainInfor, workTimeCode, null, createAtr, null, dayOffTimeIsUse);
		}
	}

	/**
	 * 最新の勤務種類変更を伴う申請を抽出する
	 * @param lstAppData
	 * @return
	 */
	private static AppRemainCreateInfor getAppWithWorkType(List<AppRemainCreateInfor> lstAppData, String sid, GeneralDate baseDate){
		if(lstAppData.isEmpty()) {
			return null;
		}
		//残数関連の申請を抽出する
		List<AppRemainCreateInfor> lstAppInfor = lstAppData.stream()
				.filter(x -> x.getSid().equals(sid) 
						&& x.getWorkTypeCode().isPresent() 
						&& (x.getAppDate().equals(baseDate)
								|| (x.getStartDate().isPresent() 
										&& x.getEndDate().isPresent()
										&& x.getStartDate().get().beforeOrEquals(baseDate)
										&& x.getEndDate().get().afterOrEquals(baseDate)
										)
								)
						)
				.collect(Collectors.toList());
		if(lstAppInfor.isEmpty()) {
			return null;
		}		
		lstAppInfor = lstAppInfor.stream().sorted((a, b) -> a.getInputDate().compareTo(b.getInputDate()))
				.collect(Collectors.toList());
		AppRemainCreateInfor appInfor = lstAppInfor.get(lstAppInfor.size() - 1);
		return appInfor;
		
	}

	/**
	 * 残数発生使用対象の勤務種類の分類かを判定
	 * @param typeAtr
	 * @return
	 */
	private static JudgmentTypeOfWorkType judgmentType(WorkTypeClassification typeAtr) {
		if(typeAtr == WorkTypeClassification.Absence
				|| typeAtr == WorkTypeClassification.AnnualHoliday
				|| typeAtr == WorkTypeClassification.Pause
				|| typeAtr == WorkTypeClassification.SpecialHoliday
				|| typeAtr == WorkTypeClassification.SubstituteHoliday
				|| typeAtr == WorkTypeClassification.TimeDigestVacation
				|| typeAtr == WorkTypeClassification.YearlyReserved
				|| typeAtr == WorkTypeClassification.Holiday) {
			return JudgmentTypeOfWorkType.REMAIN;
		} else if (typeAtr == WorkTypeClassification.HolidayWork
				|| typeAtr == WorkTypeClassification.Shooting) {
			return JudgmentTypeOfWorkType.REMAINOCC;
		} else {
			return JudgmentTypeOfWorkType.REMAINOCCNOTCOVER;
		}
	}
	
	/**
	 * 勤務種類別残数情報を作成する
	 * @return
	 */
	private static List<OccurrenceUseDetail> createDetailData() {
		List<OccurrenceUseDetail> occurrenceDetailData = new ArrayList<>();
		OccurrenceUseDetail detailData = new OccurrenceUseDetail(0, false, WorkTypeClassification.AnnualHoliday);
		occurrenceDetailData.add(detailData);
		detailData = new OccurrenceUseDetail(0, false, WorkTypeClassification.YearlyReserved);
		occurrenceDetailData.add(detailData);
		detailData = new OccurrenceUseDetail(0, false, WorkTypeClassification.SubstituteHoliday);
		occurrenceDetailData.add(detailData);
		detailData = new OccurrenceUseDetail(0, false, WorkTypeClassification.Absence);
		occurrenceDetailData.add(detailData);
		detailData = new OccurrenceUseDetail(0, false, WorkTypeClassification.Pause);
		occurrenceDetailData.add(detailData);
		detailData = new OccurrenceUseDetail(0, false, WorkTypeClassification.TimeDigestVacation);
		occurrenceDetailData.add(detailData);
		detailData = new OccurrenceUseDetail(0, false, WorkTypeClassification.HolidayWork);
		occurrenceDetailData.add(detailData);
		detailData = new OccurrenceUseDetail(0, false, WorkTypeClassification.Shooting);
		occurrenceDetailData.add(detailData);
		return occurrenceDetailData;
	}

	private static List<WorkTypeClassification> lstZansu(){
		List<WorkTypeClassification> lstOutput = new ArrayList<>();
		lstOutput.add(WorkTypeClassification.AnnualHoliday);
		lstOutput.add(WorkTypeClassification.YearlyReserved);
		lstOutput.add(WorkTypeClassification.SubstituteHoliday);
		lstOutput.add(WorkTypeClassification.Absence);
		lstOutput.add(WorkTypeClassification.Pause);
		lstOutput.add(WorkTypeClassification.TimeDigestVacation);
		lstOutput.add(WorkTypeClassification.HolidayWork);
		lstOutput.add(WorkTypeClassification.Shooting);
		lstOutput.add(WorkTypeClassification.SpecialHoliday);
		return lstOutput;
	}

	/**
	 * 勤務種類の分類に対応する残数発生使用明細を設定する
	 * @param dataOutput
	 * @param days
	 * @param workTypeClass
	 * @return
	 */
	private static void setData(WorkTypeRemainInfor dataOutput, double days, WorkTypeClassification workTypeClass) {
		dataOutput.setOccurrenceDetailData(dataOutput.getOccurrenceDetailData().stream().map(x -> {
			if (x.getWorkTypeAtr().equals(workTypeClass)) {
				OccurrenceUseDetail useDetail = new OccurrenceUseDetail(days, true, x.getWorkTypeAtr());
				useDetail.setVacationUsageTimeDetails(x.getVacationUsageTimeDetails());
				return useDetail;
			}
			return x;
		}).collect(Collectors.toList()));
	}

	/**
	 * 代休を発生させる勤務種類かを判定する
	 * @param remainInfor
	 * @return
	 */
	private static boolean checkDayoffOcc(WorkTypeRemainInfor remainInfor) {
		List<OccurrenceUseDetail> lstChk = remainInfor.getOccurrenceDetailData()
				.stream()
				.filter(x -> x.getWorkTypeAtr().equals(WorkTypeClassification.HolidayWork) && x.isUseAtr())
				.collect(Collectors.toList());
		if(lstChk.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 一定時間の振替処理を行う
	 * @param transferSetting
	 * @param timeSetting 振替可能時間
	 * @return
	 */
	private static int processCertainTime(SubHolTransferSet transferSetting, Integer timeSetting) {
		//一定時間をチェックする
		//振替可能時間と一定時間を比較する
		if(transferSetting.getCertainTime().v() > 0
				&& timeSetting >= transferSetting.getCertainTime().v()) {
			return transferSetting.getCertainTime().v();
		}				
		return 0;
	}
	
	/**
	 * 指定時間の振替処理を行う
	 * @param transferSetting
	 * @param timeSetting 振替可能時間
	 * @param createAtr
	 * @return
	 */
	private static TranferTimeInfor processDesignationTime(SubHolTransferSet transferSetting, 
			Integer timeSetting, CreateAtr createAtr) {
		//代休振替時間と代休振替日数をクリアする
		TranferTimeInfor outData = new TranferTimeInfor(createAtr, 0, Optional.of((double) 0));
		//1日の時間をチェックする
		if(transferSetting.getDesignatedTime().getOneDayTime().v() <= 0) {
			return outData;
		}
		//振替可能時間と1日の時間を比較する
		if(timeSetting < transferSetting.getDesignatedTime().getOneDayTime().v()) {
			//半日の時間をチェックする
			//振替可能時間と半日の時間を比較する
			if(transferSetting.getDesignatedTime().getHalfDayTime().v() > 0
					&& timeSetting >= transferSetting.getDesignatedTime().getHalfDayTime().v()) {
				outData.setDays(Optional.of(0.5));
				outData.setTranferTime(transferSetting.getDesignatedTime().getHalfDayTime().v());
			}			
		} else {
			outData.setDays(Optional.of(1.0));
			outData.setTranferTime(transferSetting.getDesignatedTime().getOneDayTime().v());
		}
		return outData;
	}
	
	private static String workTimeCode(RequireM2 require, InterimRemainCreateInfor createInfo, String sid, GeneralDate baseDate) {
		//実績をチェックする
		if(createInfo.getRecordData().isPresent()
				&& createInfo.getRecordData().get().getWorkTimeCode().isPresent()) {
			String workTimeCode = createInfo.getRecordData().get().getWorkTimeCode().get();
			return workTimeCode;
		} else {
			//予定をチェックする
			if(createInfo.getScheData().isPresent()
					&& createInfo.getScheData().get().getWorkTimeCode().isPresent()) {
				return createInfo.getScheData().get().getWorkTimeCode().get();
			} else {
				//アルゴリズム「社員の労働条件を取得する」を実行する
				//振替用就業時間帯コード：労働条件項目.区分別勤務.休日出勤時.就業時間帯コード
				Optional<WorkingConditionItem> coditionInfo = WorkingConditionService.findWorkConditionByEmployee(
						require, sid, baseDate);
				
				if(!coditionInfo.isPresent()) {
					return "";
				} else {
					// chưa check null nên bị exception,get ra nhưng không set vào đâu cả ?
					//coditionInfo.get().getWorkCategory().getHolidayWork().getWorkTimeCode();
				}
			}
		}
		return "";
	}

	
	public static interface RequireM9 extends RequireM6, RequireM7 {
		
	}
	
	public static interface RequireM8 {
		
		List<Integer> getSpecialHolidayNumber(String cid, int sphdSpecLeaveNo);

		boolean checkChildCare(WorkTypeSet wkSet, String cid);
	}
	
	public static interface RequireM7 extends RequireM1, RequireM3 {
		
	}
	
	public static interface RequireM6 extends TempRemainCreateEachData.RequireM1, RequireM1, RequireM5 {
	}
	
	public static interface RequireM5 extends WorkTimeIsFluidWork.RequireM1, RequireM4 {
	}
	
	public static interface RequireM4 extends GetDesignatedTime.RequireM2 {
	}
	
	public static interface RequireM3 extends RequireM2, RequireM5 {
	}
	
	public static interface RequireM2 extends WorkingConditionService.RequireM1 {
	}
	

	public static interface RequireM1 extends RequireM8 {

		Optional<WorkType> workType(String companyId, String workTypeCd);
	}
}
