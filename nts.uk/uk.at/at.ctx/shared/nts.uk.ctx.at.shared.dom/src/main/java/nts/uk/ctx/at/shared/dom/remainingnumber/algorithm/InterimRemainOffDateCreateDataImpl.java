package nts.uk.ctx.at.shared.dom.remainingnumber.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.CompanyHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.DayoffTranferInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.EmploymentHolidayMngSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.InforFormerRemainData;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.OccurrenceUseDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.SpecialHolidayUseDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.TranferTimeInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.WorkTypeRemainInfor;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.service.WorkingConditionService;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSet;
import nts.uk.ctx.at.shared.dom.worktime.common.SubHolTransferSetAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.subholtransferset.GetDesignatedTime;
import nts.uk.ctx.at.shared.dom.worktime.service.WorkTimeIsFluidWork;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSet;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSetCheck;
@Stateless
public class InterimRemainOffDateCreateDataImpl implements InterimRemainOffDateCreateData{
	@Inject
	private WorkTypeRepository workTypeRepo;
	@Inject
	private WorkTimeIsFluidWork workTimeService;
	@Inject
	private GetDesignatedTime confirmTime;
	@Inject
	private WorkingConditionService conditionService;
	@Inject
	private TempRemainCreateEachData createEachData;
	@Inject
	private SpecialHolidayRepository holidayRepo;
	@Override
	public DailyInterimRemainMngData createData(String cid, String sid, GeneralDate baseDate, boolean dayOffTimeIsUse,
			InterimRemainCreateInfor detailData, CompanyHolidayMngSetting comHolidaySetting, EmploymentHolidayMngSetting employmentHolidaySetting) {
		//残数作成元情報を作成する
		InforFormerRemainData formerRemainData = this.createInforFormerRemainData(cid, sid, baseDate, detailData, dayOffTimeIsUse,
				comHolidaySetting, employmentHolidaySetting);
		if(formerRemainData == null 
				|| formerRemainData.getWorkTypeRemain().isEmpty()) {
			return null;
		}
		//残数作成元情報から暫定残数管理データを作成する
		DailyInterimRemainMngData createDataInterimRemain = this.createDataInterimRemain(formerRemainData);
		return createDataInterimRemain;
	}

	@Override
	public InforFormerRemainData createInforFormerRemainData(String cid, String sid, GeneralDate baseDate,
			InterimRemainCreateInfor detailData, boolean dayOffTimeIsUse, CompanyHolidayMngSetting comHolidaySetting, EmploymentHolidayMngSetting employmentHolidaySetting) {
		InforFormerRemainData outputData = new InforFormerRemainData(sid, 
				baseDate, 
				dayOffTimeIsUse, 
				new ArrayList<>(), 
				Optional.empty(), 
				new ArrayList<>(), 
				comHolidaySetting,
				employmentHolidaySetting);		
		//最新の勤務種類変更を伴う申請を抽出する
		AppRemainCreateInfor appWithWorkType = this.getAppWithWorkType(detailData.getAppData(), sid, baseDate);
		if(appWithWorkType == null) {
			//実績をチェックする
			if(detailData.getRecordData().isPresent()) {
				//実績から残数作成元情報を設定する
				return this.createInterimDataFromecord(cid, detailData.getRecordData().get(), outputData);
			} else {
				//予定をチェックする
				if(detailData.getScheData().isPresent()) {
					//予定から残数作成元情報を設定する
					return this.createInterimDataFromSche(cid, detailData.getScheData().get(), outputData, dayOffTimeIsUse);
				}
			}
		} else {
			//最新の勤務種類変更を伴う申請から残数作成元情報を設定する
			CreateAtr createAtr = appWithWorkType.getPrePosAtr() == PrePostAtr.PREDICT ? CreateAtr.APPBEFORE : CreateAtr.APPAFTER;
			return this.createInterimDataFromApp(cid, detailData, appWithWorkType, outputData, dayOffTimeIsUse, createAtr);
		}
		return outputData;
	}
	/**
	 * 最新の勤務種類変更を伴う申請を抽出する
	 * @param lstAppData
	 * @return
	 */
	private AppRemainCreateInfor getAppWithWorkType(List<AppRemainCreateInfor> lstAppData, String sid, GeneralDate baseDate){
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

	@Override
	public InforFormerRemainData createInterimDataFromecord(String cid,RecordRemainCreateInfor recordData, InforFormerRemainData outputData) {
		//アルゴリズム「勤務種類別残数情報を作成する」を実行する
		List<WorkTypeRemainInfor> dataDetail = this.createWorkTypeRemainInfor(cid, CreateAtr.RECORD, recordData.getWorkTypeCode());
		if(dataDetail.isEmpty()) {
			return outputData;
		}
		outputData.setWorkTypeRemain(dataDetail);
		//アルゴリズム「実績から代休振替情報を設定する」を実行する
		DayoffTranferInfor dayOffInfor = this.createDayOffTranferFromRecord(cid, CreateAtr.RECORD, recordData, outputData.isDayOffTimeIsUse());
		List<DayoffTranferInfor> lstOutput = new ArrayList<>();
		lstOutput.add(dayOffInfor);
		outputData.setDayOffTranfer(lstOutput);
		return outputData;
	}

	@Override
	public List<WorkTypeRemainInfor> createWorkTypeRemainInfor(String cid, CreateAtr createAtr, String workTypeCode) {
		WorkTypeRemainInfor outputData = new WorkTypeRemainInfor(null, null,null, new ArrayList<>(), new ArrayList<>());
		List<WorkTypeRemainInfor> lstOutputData = new ArrayList<>();
		//ドメインモデル「勤務種類」を取得する
		Optional<WorkType> optWorkTypeData = workTypeRepo.findByPK(cid, workTypeCode);
		
		if(!optWorkTypeData.isPresent()) {
			return lstOutputData;
		}
		WorkType workTypeData = optWorkTypeData.get();
		//勤務種類別残数情報を作成する
		outputData.setCreateData(createAtr);
		outputData.setWorkTypeCode(workTypeCode);
		outputData.setOccurrenceDetailData(this.createDetailData());
		WorkTypeClassification workTypeClass = WorkTypeClassification.Attendance;
		if(workTypeData.getDailyWork().isOneDay()) {
			workTypeClass = workTypeData.getDailyWork().getOneDay();
			outputData.setWorkTypeClass(workTypeClass);
			this.createWithOneDayWorkType(cid, workTypeData, outputData);
			lstOutputData.add(outputData);
			//勤務区分をチェックする
			return lstOutputData;
		} else {
			//午前
			WorkTypeClassification workTypeMorning = workTypeData.getDailyWork().getMorning();
			if(this.lstZansu().contains(workTypeMorning)) {
				WorkTypeRemainInfor morning = new WorkTypeRemainInfor(outputData.getWorkTypeCode(), workTypeMorning, createAtr, 
						outputData.getOccurrenceDetailData(), outputData.getSpeHolidayDetailData()); 
				this.createWithOneDayWorkType(cid, workTypeData, morning);
				lstOutputData.add(morning);
			}
			//午後
			WorkTypeClassification workTypAfternoon = workTypeData.getDailyWork().getAfternoon();
			if(this.lstZansu().contains(workTypAfternoon)) {
				WorkTypeRemainInfor after =  new WorkTypeRemainInfor(outputData.getWorkTypeCode(), workTypAfternoon, createAtr,
						outputData.getOccurrenceDetailData(), outputData.getSpeHolidayDetailData());
				this.createWithOneDayWorkType(cid, workTypeData, after);
				lstOutputData.add(after);
			}
		}
		//勤務区分をチェックする
		return lstOutputData;
	}

	@Override
	public void createWithOneDayWorkType(String cid, WorkType workType, WorkTypeRemainInfor dataOutput) {
		//アルゴリズム「残数発生使用対象の勤務種類の分類かを判定」を実行する
		JudgmentTypeOfWorkType judmentType = this.judgmentType(dataOutput.getWorkTypeClass());
		if(judmentType == JudgmentTypeOfWorkType.REMAINOCCNOTCOVER) {
			return;
		}
		List<WorkTypeSet> workTypeSetList = workType.getWorkTypeSetList();
		double days = 0;
		if(workType.getDailyWork().isOneDay()) {
			days = 1.0;
		} else {
			days = 0.5;
		}
		//勤務種類の分類をチェックする
		if(dataOutput.getWorkTypeClass() == WorkTypeClassification.SpecialHoliday) {
			//特休使用明細を追加する
			List<SpecialHolidayUseDetail> lstSpeUseDetail = new ArrayList<>(dataOutput.getSpeHolidayDetailData());
			for (WorkTypeSet x : workTypeSetList) {
				//アルゴリズム「特別休暇枠NOから特別休暇を取得する」を実行する
				List<Integer> holidaySpecialCd = holidayRepo.findBySphdSpecLeave(cid, x.getSumSpHodidayNo());				
				if(!holidaySpecialCd.isEmpty()) {
					SpecialHolidayUseDetail detailData = new SpecialHolidayUseDetail(holidaySpecialCd.get(0), days);
					lstSpeUseDetail.add(detailData);
				}
			}
			dataOutput.setSpeHolidayDetailData(lstSpeUseDetail);
			return;
		} else if(dataOutput.getWorkTypeClass() == WorkTypeClassification.HolidayWork) {
			//代休を発生させるかをチェックする
			if(!workTypeSetList.stream().filter(x -> x.getGenSubHodiday() == WorkTypeSetCheck.NO_CHECK)
					.collect(Collectors.toList()).isEmpty()) {
				return;
			}			
		}

		this.setData(dataOutput, days, dataOutput.getWorkTypeClass());
	}	
	/**
	 * 残数発生使用対象の勤務種類の分類かを判定
	 * @param typeAtr
	 * @return
	 */
	private JudgmentTypeOfWorkType judgmentType(WorkTypeClassification typeAtr) {
		if(typeAtr == WorkTypeClassification.Absence
				|| typeAtr == WorkTypeClassification.AnnualHoliday
				|| typeAtr == WorkTypeClassification.Pause
				|| typeAtr == WorkTypeClassification.SpecialHoliday
				|| typeAtr == WorkTypeClassification.SubstituteHoliday
				|| typeAtr == WorkTypeClassification.TimeDigestVacation
				|| typeAtr == WorkTypeClassification.YearlyReserved) {
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
	private List<OccurrenceUseDetail> createDetailData() {
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

	private List<WorkTypeClassification> lstZansu(){
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
	@Override
	public WorkTypeRemainInfor createWithHaftDayWorkType(WorkType workType, WorkTypeRemainInfor dataOutput) {
		//午前
		//アルゴリズム「残数発生使用対象の勤務種類の分類かを判定」を実行する
		JudgmentTypeOfWorkType judgmentTypeAM = this.judgmentType(workType.getDailyWork().getMorning());
		if(judgmentTypeAM == JudgmentTypeOfWorkType.REMAINOCCNOTCOVER) {
			return dataOutput;
		}
		//勤務種類の分類に対応する残数発生使用明細を設定する		
		this.setData(dataOutput, 0.5, workType.getDailyWork().getMorning());
		//午後
		//アルゴリズム「残数発生使用対象の勤務種類の分類かを判定」を実行する
		JudgmentTypeOfWorkType judgmentTypePM = this.judgmentType(workType.getDailyWork().getAfternoon());
		if(judgmentTypePM == JudgmentTypeOfWorkType.REMAINOCCNOTCOVER) {
			return dataOutput;
		}
		//勤務種類の分類に対応する残数発生使用明細を設定する
		this.setData(dataOutput, 0.5, workType.getDailyWork().getAfternoon());
		return dataOutput;
	}
	/**
	 * 勤務種類の分類に対応する残数発生使用明細を設定する
	 * @param dataOutput
	 * @param days
	 * @param workTypeClass
	 * @return
	 */
	private void setData(WorkTypeRemainInfor dataOutput, double days, WorkTypeClassification workTypeClass) {
		dataOutput.setOccurrenceDetailData(dataOutput.getOccurrenceDetailData().stream().map(x -> {
			if(x.getWorkTypeAtr() == workTypeClass) {
				return new OccurrenceUseDetail(days, true, x.getWorkTypeAtr());
			} 
			return x;
		}).collect(Collectors.toList()));
		/*for (OccurrenceUseDetail detail : dataOutput.getOccurrenceDetailData()) {
			if(detail.getWorkTypeAtr() == workTypeClass) {
				dataOutput.getOccurrenceDetailData().remove(detail);
				detail.setDays(days);
				detail.setUseAtr(true);
				dataOutput.getOccurrenceDetailData().add(detail);
				break;
			}
		}*/
	}

	@Override
	public DayoffTranferInfor createDayOffTranferFromRecord(String cid, CreateAtr createAtr, RecordRemainCreateInfor recordData,
			boolean dayOffTimeIsUse) {
		//代休振替情報を作成する
		Optional<TranferTimeInfor> tranferBreakTime = Optional.of(new TranferTimeInfor(createAtr, recordData.getTransferTotal(), Optional.empty()));
		DayoffTranferInfor outputData = new DayoffTranferInfor(recordData.getWorkTimeCode(), tranferBreakTime, Optional.empty());
		//アルゴリズム「代休振替時間を算出する」を実行する
		String workTimeCode = recordData.getWorkTimeCode().isPresent() ? recordData.getWorkTimeCode().get() : "";
		tranferBreakTime = this.calDayoffTranferTime(cid, createAtr, workTimeCode, recordData.getTransferTotal(), DayoffChangeAtr.BREAKTIME);
		outputData.setTranferBreakTime(tranferBreakTime);
		//アルゴリズム「実績から振替残業時間を作成する」を実行する
		if(dayOffTimeIsUse) {
			Optional<TranferTimeInfor> tranferOvertime = this.calDayoffTranferTime(cid, createAtr, workTimeCode, recordData.getTransferOvertimesTotal(), DayoffChangeAtr.OVERTIME);
			outputData.setTranferOverTime(tranferOvertime);
		}
		return outputData;
	}

	@Override
	public DailyInterimRemainMngData createDataInterimRemain(InforFormerRemainData inforData) {
		DailyInterimRemainMngData outputData = new DailyInterimRemainMngData(Optional.empty(), new ArrayList<>(), Optional.empty(), 
				Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), new ArrayList<>());
		if(inforData.getWorkTypeRemain().isEmpty()) {
			return null;
		}
		for (WorkTypeRemainInfor workTypeInfor : inforData.getWorkTypeRemain()) {
			switch (workTypeInfor.getWorkTypeClass()) {
			case Pause:
				outputData = createEachData.createInterimAbsData(inforData, WorkTypeClassification.Pause, outputData);
				break;
			case SubstituteHoliday:	
				outputData = createEachData.createInterimDayOffData(inforData, WorkTypeClassification.SubstituteHoliday, outputData);
				break;
			case AnnualHoliday:
				outputData = createEachData.createInterimAnnualHoliday(inforData, WorkTypeClassification.AnnualHoliday, outputData);
				break;
			case YearlyReserved:
				outputData = createEachData.createInterimReserveHoliday(inforData, WorkTypeClassification.YearlyReserved,	outputData);
				break;
			case SpecialHoliday:
				outputData = createEachData.createInterimSpecialHoliday(inforData, WorkTypeClassification.SpecialHoliday, outputData);
				break;
			case Shooting:
				outputData = createEachData.createInterimRecData(inforData, WorkTypeClassification.Shooting, outputData);
				break;
			case HolidayWork:
				outputData = createEachData.createInterimBreak(inforData, WorkTypeClassification.HolidayWork, outputData);
				default:
					break;
			}
		}
		return outputData;
	}

	

	@Override
	public InforFormerRemainData createInterimDataFromSche(String cid, ScheRemainCreateInfor scheData,
			InforFormerRemainData outputData, boolean dayOffTimeIsUse) {
		String workTimeCode = scheData.getWorkTimeCode().isPresent() ? scheData.getWorkTimeCode().get() : "";
		//アルゴリズム「勤務種類別残数情報を作成する」を実行する
		List<WorkTypeRemainInfor> remainInfor = this.createWorkTypeRemainInfor(cid, CreateAtr.SCHEDULE, scheData.getWorkTypeCode());
		//勤務種類別残数情報を設定する
		outputData.setWorkTypeRemain(remainInfor);
		//アルゴリズム「就業時間帯から代休振替情報を作成する」を実行する
		List<DayoffTranferInfor> tranferData = this.createDayoffFromWorkTime(cid, remainInfor, workTimeCode, null, 
				CreateAtr.SCHEDULE, null, dayOffTimeIsUse);
		outputData.setDayOffTranfer(tranferData);
		return outputData;
	}

	@Override
	public List<DayoffTranferInfor> createDayoffFromWorkTime(String cid, List<WorkTypeRemainInfor> remainInfor, String workTimeCode,
			Integer timeSetting, CreateAtr createAtr, Integer timeOverSetting, boolean dayOffTimeIsUse) {
		List<DayoffTranferInfor> lstOutput = new ArrayList<>();
		for (WorkTypeRemainInfor workTypeInfor : remainInfor) {
			//アルゴリズム「代休を発生させる勤務種類かを判定する」を実行する
			boolean chkDayOff = this.checkDayoffOcc(workTypeInfor);
			if(!chkDayOff) {
				continue;
			}
			Optional<TranferTimeInfor> transferBreak = Optional.of(new TranferTimeInfor(createAtr, timeSetting, Optional.empty()));
			Optional<TranferTimeInfor> transferOver = Optional.of(new TranferTimeInfor(createAtr, timeOverSetting, Optional.empty()));
			//振替可能時間をチェックする
			if(timeSetting == null) {
				//アルゴリズム「所定時間を取得」を実行する
				timeSetting = workTimeService.getTimeByWorkTimeTypeCode(workTimeCode, workTypeInfor.getWorkTypeCode());
				transferBreak.get().setTranferTime(timeSetting);
			}
			if(timeOverSetting != null && dayOffTimeIsUse) {
				//振替残業時間を作成する
				transferOver = this.calDayoffTranferTime(cid, createAtr, workTimeCode, timeOverSetting, DayoffChangeAtr.OVERTIME);
			}
			//アルゴリズム「代休振替時間を算出する」を実行する
			transferBreak = this.calDayoffTranferTime(cid, createAtr, workTimeCode, timeSetting, DayoffChangeAtr.BREAKTIME);
			
			lstOutput.add(new DayoffTranferInfor(Optional.of(workTimeCode), transferBreak, transferOver));
		}
		
		return lstOutput;
	}
	/**
	 * 代休を発生させる勤務種類かを判定する
	 * @param remainInfor
	 * @return
	 */
	private boolean checkDayoffOcc(WorkTypeRemainInfor remainInfor) {
		List<OccurrenceUseDetail> lstChk = remainInfor.getOccurrenceDetailData()
				.stream()
				.filter(x -> x.getWorkTypeAtr() == WorkTypeClassification.HolidayWork && x.isUseAtr())
				.collect(Collectors.toList());
		if(lstChk.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Optional<TranferTimeInfor> calDayoffTranferTime(String cid, CreateAtr createAtr,String workTimeCode, Integer timeSetting, DayoffChangeAtr dayoffChange) {
		//アルゴリズム「代休振替設定を取得」を実行する
		Optional<SubHolTransferSet> optDayOffTranferSetting = confirmTime.get(cid, workTimeCode);
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
			return Optional.of(new TranferTimeInfor(createAtr, this.processCertainTime(transferSetting, timeSetting), Optional.empty()));
		} else {
			//指定時間の振替処理を行う
			return Optional.of(this.processDesignationTime(transferSetting, timeSetting, createAtr));			
		}
	}
	/**
	 * 一定時間の振替処理を行う
	 * @param transferSetting
	 * @param timeSetting 振替可能時間
	 * @return
	 */
	private int processCertainTime(SubHolTransferSet transferSetting, Integer timeSetting) {
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
	private TranferTimeInfor processDesignationTime(SubHolTransferSet transferSetting, Integer timeSetting, CreateAtr createAtr) {
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
	
	@Override
	public InforFormerRemainData createInterimDataFromApp(String cid, InterimRemainCreateInfor createInfo, AppRemainCreateInfor appInfor,
			InforFormerRemainData outputData, boolean dayOffTimeIsUse, CreateAtr createAtr) {
		String workTypeCode = appInfor.getWorkTypeCode().isPresent() ? appInfor.getWorkTypeCode().get() : "";
		//アルゴリズム「勤務種類別残数情報を作成する」を実行する
		List<WorkTypeRemainInfor> remainInfor = this.createWorkTypeRemainInfor(cid, createAtr, workTypeCode);
		if(remainInfor.isEmpty()) {
			return null;
		}
		outputData.setWorkTypeRemain(remainInfor);
		//申請種類をチェックする
		if(appInfor.getAppType() == ApplicationType.BREAK_TIME_APPLICATION) {
			// 休日出勤申請から代休振替情報を作成する
			List<DayoffTranferInfor> tranferInforFromHoliday = this.tranferInforFromHolidayWork(cid, dayOffTimeIsUse, appInfor,
					remainInfor, createAtr);
			outputData.setDayOffTranfer(tranferInforFromHoliday);
		} else {
			//休日出勤以外の申請から代休振替情報を作成する
			List<DayoffTranferInfor> tranferInforNotFromHoliday = this.transferInforFromNotHolidayWork(cid, dayOffTimeIsUse, appInfor, 
					createInfo, remainInfor, createAtr);
			outputData.setDayOffTranfer(tranferInforNotFromHoliday);
		}
		
		return outputData;
	}

	@Override
	public List<DayoffTranferInfor> tranferInforFromHolidayWork(String cid, boolean dayOffTimeIsUse, AppRemainCreateInfor appInfor,
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
		
		
		return this.createDayoffFromWorkTime(cid, remainInfor, workTimeCode, breakTime, createAtr, 0, dayOffTimeIsUse);
	}

	@Override
	public List<DayoffTranferInfor> transferInforFromNotHolidayWork(String cid, boolean dayOffTimeIsUse, AppRemainCreateInfor appInfor,
			InterimRemainCreateInfor createInfo, List<WorkTypeRemainInfor> remainInfor, CreateAtr createAtr) {
		// 休出以外の申請が利用する振替用就業時間帯コードを取得する
		String workTimeCode = this.workTimeCode(createInfo, appInfor.getSid(), appInfor.getAppDate());
		//実績をチェックする
		if(createInfo.getRecordData().isPresent()) {
			//アルゴリズム「就業時間帯から代休振替情報を作成する」を実行する
			RecordRemainCreateInfor recordData = createInfo.getRecordData().get();
			return this.createDayoffFromWorkTime(cid, remainInfor, workTimeCode, recordData.getTransferTotal(), createAtr, recordData.getTransferOvertimesTotal(), dayOffTimeIsUse);			
		} else {
			//アルゴリズム「就業時間帯から代休振替情報を作成する」を実行する
			return this.createDayoffFromWorkTime(cid, remainInfor, workTimeCode, null, createAtr, null, dayOffTimeIsUse);
		}
	}
	
	private String workTimeCode(InterimRemainCreateInfor createInfo, String sid, GeneralDate baseDate) {
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
				Optional<WorkingConditionItem> coditionInfo = conditionService.findWorkConditionByEmployee(sid, baseDate);
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
}
