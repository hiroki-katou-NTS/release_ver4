package nts.uk.ctx.at.schedule.dom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.ConfirmedAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ScheRemainCreateInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.TreatmentOfVacation;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.service.RemainCreateInforByScheData;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
@Stateless
public class RemainCreateInforByScheDataImpl implements RemainCreateInforByScheData{
	@Inject
	private BasicScheduleRepository scheRepos;
	@Override
	public List<ScheRemainCreateInfor> createRemainInfor(String cid, String sid, DatePeriod dateData) {
		//ドメインモデル「勤務予定基本情報」を取得する
		List<BasicSchedule> lstScheData = scheRepos.getBasicScheduleBySidPeriodDate(sid, dateData);
		
		return this.lstResult(lstScheData, sid);
	}
	@Override
	public List<ScheRemainCreateInfor> createRemainInfor(String cid, String sid, List<GeneralDate> dates) {

		//ドメインモデル「勤務予定基本情報」を取得する
		List<BasicSchedule> lstScheData = scheRepos.getBasicScheduleBySidPeriodDate(sid, dates);
		return this.lstResult(lstScheData, sid);
	}

	private List<ScheRemainCreateInfor> lstResult(List<BasicSchedule> lstScheData, String sid){
		List<ScheRemainCreateInfor> lstOutputData = new ArrayList<>();
		lstScheData.stream().forEach(scheData -> {
			ScheRemainCreateInfor outData = new ScheRemainCreateInfor(sid,
					scheData.getDate(),
					scheData.getWorkTypeCode(),
					Optional.of(TreatmentOfVacation.AFTERNOONPICKUP),//TODO xac nhan lai vi domain duoc sua nhung chua code
					scheData.getWorkTimeCode() == null ? Optional.empty() : Optional.of(scheData.getWorkTimeCode()),
					false,
					scheData.getConfirmedAtr() == ConfirmedAtr.CONFIRMED ? true : false); //TODO xac nhna lai
			lstOutputData.add(outData);
		});		
		return lstOutputData;
	}
}
