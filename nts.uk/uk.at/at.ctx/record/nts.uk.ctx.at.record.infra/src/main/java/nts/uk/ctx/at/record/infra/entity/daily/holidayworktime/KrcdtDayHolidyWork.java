package nts.uk.ctx.at.record.infra.entity.daily.holidayworktime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayMidnightWork;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkTimeOfDaily;
import nts.uk.ctx.at.record.infra.entity.daily.actualworktime.KrcdtDayAttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.StaturoryAtrOfHolidayWork;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCDT_DAY_HOLIDYWORK")
public class KrcdtDayHolidyWork extends UkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/*主キー*/
	@EmbeddedId
	public KrcdtDayHolidyWorkPK krcdtDayHolidyWorkPK;
	/*休日出勤時間1*/
	@Column(name = "HOLI_WORK_TIME_1")
	public int holiWorkTime1;
	/*休日出勤時間2*/
	@Column(name = "HOLI_WORK_TIME_2")
	public int holiWorkTime2;
	/*休日出勤時間3*/
	@Column(name = "HOLI_WORK_TIME_3")
	public int holiWorkTime3;
	/*休日出勤時間4*/
	@Column(name = "HOLI_WORK_TIME_4")
	public int holiWorkTime4;
	/*休日出勤時間5*/
	@Column(name = "HOLI_WORK_TIME_5")
	public int holiWorkTime5;
	/*休日出勤時間6*/
	@Column(name = "HOLI_WORK_TIME_6")
	public int holiWorkTime6;
	/*休日出勤時間7*/
	@Column(name = "HOLI_WORK_TIME_7")
	public int holiWorkTime7;
	/*休日出勤時間8*/
	@Column(name = "HOLI_WORK_TIME_8")
	public int holiWorkTime8;
	/*休日出勤時間9*/
	@Column(name = "HOLI_WORK_TIME_9")
	public int holiWorkTime9;
	/*休日出勤時間10*/
	@Column(name = "HOLI_WORK_TIME_10")
	public int holiWorkTime10;
	/*振替時間1*/
	@Column(name = "TRANS_TIME_1")
	public int transTime1;
	/*振替時間2*/
	@Column(name = "TRANS_TIME_2")
	public int transTime2;
	/*振替時間3*/
	@Column(name = "TRANS_TIME_3")
	public int transTime3;
	/*振替時間4*/
	@Column(name = "TRANS_TIME_4")
	public int transTime4;
	/*振替時間5*/
	@Column(name = "TRANS_TIME_5")
	public int transTime5;
	/*振替時間6*/
	@Column(name = "TRANS_TIME_6")
	public int transTime6;
	/*振替時間7*/
	@Column(name = "TRANS_TIME_7")
	public int transTime7;
	/*振替時間8*/
	@Column(name = "TRANS_TIME_8")
	public int transTime8;
	/*振替時間9*/
	@Column(name = "TRANS_TIME_9")
	public int transTime9;
	/*振替時間10*/
	@Column(name = "TRANS_TIME_10")
	public int transTime10;
	/*計算休日出勤時間1*/
	@Column(name = "CALC_HOLI_WORK_TIME_1")
	public int calcHoliWorkTime1;
	/*計算休日出勤時間2*/
	@Column(name = "CALC_HOLI_WORK_TIME_2")
	public int calcHoliWorkTime2;
	/*計算休日出勤時間3*/
	@Column(name = "CALC_HOLI_WORK_TIME_3")
	public int calcHoliWorkTime3;
	/*計算休日出勤時間4*/
	@Column(name = "CALC_HOLI_WORK_TIME_4")
	public int calcHoliWorkTime4;
	/*計算休日出勤時間5*/
	@Column(name = "CALC_HOLI_WORK_TIME_5")
	public int calcHoliWorkTime5;
	/*計算休日出勤時間6*/
	@Column(name = "CALC_HOLI_WORK_TIME_6")
	public int calcHoliWorkTime6;
	/*計算休日出勤時間7*/
	@Column(name = "CALC_HOLI_WORK_TIME_7")
	public int calcHoliWorkTime7;
	/*計算休日出勤時間8*/
	@Column(name = "CALC_HOLI_WORK_TIME_8")
	public int calcHoliWorkTime8;
	/*計算休日出勤時間9*/
	@Column(name = "CALC_HOLI_WORK_TIME_9")
	public int calcHoliWorkTime9;
	/*計算休日出勤時間10*/
	@Column(name = "CALC_HOLI_WORK_TIME_10")
	public int calcHoliWorkTime10;
	/*計算振替時間1*/
	@Column(name = "CALC_TRANS_TIME_1")
	public int calcTransTime1;
	/*計算振替時間2*/
	@Column(name = "CALC_TRANS_TIME_2")
	public int calcTransTime2;
	/*計算振替時間3*/
	@Column(name = "CALC_TRANS_TIME_3")
	public int calcTransTime3;
	/*計算振替時間4*/
	@Column(name = "CALC_TRANS_TIME_4")
	public int calcTransTime4;
	/*計算振替時間5*/
	@Column(name = "CALC_TRANS_TIME_5")
	public int calcTransTime5;
	/*計算振替時間6*/
	@Column(name = "CALC_TRANS_TIME_6")
	public int calcTransTime6;
	/*計算振替時間7*/
	@Column(name = "CALC_TRANS_TIME_7")
	public int calcTransTime7;
	/*計算振替時間8*/
	@Column(name = "CALC_TRANS_TIME_8")
	public int calcTransTime8;
	/*計算振替時間9*/
	@Column(name = "CALC_TRANS_TIME_9")
	public int calcTransTime9;
	/*計算振替時間10*/
	@Column(name = "CALC_TRANS_TIME_10")
	public int calcTransTime10;
	/*事前申請時間1*/
	@Column(name = "PRE_APP_TIME_1")
	public int preAppTime1;
	/*事前申請時間2*/
	@Column(name = "PRE_APP_TIME_2")
	public int preAppTime2;
	/*事前申請時間3*/
	@Column(name = "PRE_APP_TIME_3")
	public int preAppTime3;
	/*事前申請時間4*/
	@Column(name = "PRE_APP_TIME_4")
	public int preAppTime4;
	/*事前申請時間5*/
	@Column(name = "PRE_APP_TIME_5")
	public int preAppTime5;
	/*事前申請時間6*/
	@Column(name = "PRE_APP_TIME_6")
	public int preAppTime6;
	/*事前申請時間7*/
	@Column(name = "PRE_APP_TIME_7")
	public int preAppTime7;
	/*事前申請時間8*/
	@Column(name = "PRE_APP_TIME_8")
	public int preAppTime8;
	/*事前申請時間9*/
	@Column(name = "PRE_APP_TIME_9")
	public int preAppTime9;
	/*事前申請時間10*/
	@Column(name = "PRE_APP_TIME_10")
	public int preAppTime10;
	/*法定内休日出勤深夜*/
	@Column(name = "LEG_HOLI_WORK_MIDN")
	public int legHoliWorkMidn;
	/*法定外休日出勤深夜*/
	@Column(name = "ILLEG_HOLI_WORK_MIDN")
	public int illegHoliWorkMidn;
	/*祝日日出勤深夜*/
	@Column(name = "PB_HOLI_WORK_MIDN")
	public int pbHoliWorkMidn;
	/*計算法定内休日出勤深夜*/
	@Column(name = "CALC_LEG_HOLI_WORK_MIDN")
	public int calcLegHoliWorkMidn;
	/*計算法定外休日出勤深夜*/
	@Column(name = "CALC_ILLEG_HOLI_WORK_MIDN")
	public int calcIllegHoliWorkMidn;
	/*計算祝日日出勤深夜*/
	@Column(name = "CALC_PB_HOLI_WORK_MIDN")
	public int calcPbHoliWorkMidn;
	/*休日出勤拘束時間*/
	@Column(name = "HOLI_WORK_BIND_TIME")
	public int holiWorkBindTime;
	
	@OneToOne(mappedBy="krcdtDayHolidyWork")
	public KrcdtDayAttendanceTime krcdtDayAttendanceTime;
	
	@Override
	protected Object getKey() {
		return this.krcdtDayHolidyWorkPK;
	}
	
	
	public static KrcdtDayHolidyWork create(String employeeId, GeneralDate date, HolidayWorkTimeOfDaily domain) {
		val entity = new KrcdtDayHolidyWork();
		/*主キー*/
		entity.krcdtDayHolidyWorkPK = new KrcdtDayHolidyWorkPK(employeeId,date);
		entity.setData(domain);
		
		return entity;
	}


	public void setData(HolidayWorkTimeOfDaily domain) {
		HolidayWorkFrameTime frame1 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 1);
		HolidayWorkFrameTime frame2 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 2);
		HolidayWorkFrameTime frame3 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 3);
		HolidayWorkFrameTime frame4 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 4);
		HolidayWorkFrameTime frame5 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 5);
		HolidayWorkFrameTime frame6 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 6);
		HolidayWorkFrameTime frame7 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 7);
		HolidayWorkFrameTime frame8 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 8);
		HolidayWorkFrameTime frame9 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 9);
		HolidayWorkFrameTime frame10 = getHolidayTimeFrame(domain.getHolidayWorkFrameTime(), 10);
		/*休日出勤時間*/
		this.holiWorkTime1 = frame1.getHolidayWorkTime().isPresent() || frame1.getHolidayWorkTime().get().getTime() == null ? 0 : frame1.getHolidayWorkTime().get().getTime().valueAsMinutes();
		this.holiWorkTime2 = frame2.getHolidayWorkTime().isPresent() || frame2.getHolidayWorkTime().get().getTime() == null ? 0 : frame2.getHolidayWorkTime().get().getTime().valueAsMinutes();
		this.holiWorkTime3 = frame3.getHolidayWorkTime().isPresent() || frame3.getHolidayWorkTime().get().getTime() == null ? 0 : frame3.getHolidayWorkTime().get().getTime().valueAsMinutes();
		this.holiWorkTime4 = frame4.getHolidayWorkTime().isPresent() || frame4.getHolidayWorkTime().get().getTime() == null ? 0 : frame4.getHolidayWorkTime().get().getTime().valueAsMinutes();
		this.holiWorkTime5 = frame5.getHolidayWorkTime().isPresent() || frame5.getHolidayWorkTime().get().getTime() == null ? 0 : frame5.getHolidayWorkTime().get().getTime().valueAsMinutes();
		this.holiWorkTime6 = frame6.getHolidayWorkTime().isPresent() || frame6.getHolidayWorkTime().get().getTime() == null ? 0 : frame6.getHolidayWorkTime().get().getTime().valueAsMinutes();
		this.holiWorkTime7 = frame7.getHolidayWorkTime().isPresent() || frame7.getHolidayWorkTime().get().getTime() == null ? 0 : frame7.getHolidayWorkTime().get().getTime().valueAsMinutes();
		this.holiWorkTime8 = frame8.getHolidayWorkTime().isPresent() || frame8.getHolidayWorkTime().get().getTime() == null ? 0 : frame8.getHolidayWorkTime().get().getTime().valueAsMinutes();
		this.holiWorkTime9 = frame9.getHolidayWorkTime().isPresent() || frame9.getHolidayWorkTime().get().getTime() == null ? 0 : frame9.getHolidayWorkTime().get().getTime().valueAsMinutes();
		this.holiWorkTime10 = frame10.getHolidayWorkTime().isPresent() || frame10.getHolidayWorkTime().get().getTime() == null ? 0 : frame10.getHolidayWorkTime().get().getTime().valueAsMinutes();
		/*振替時間*/
		this.transTime1 = frame1.getTransferTime().isPresent() || frame1.getTransferTime().get().getTime() == null ? 0 : frame1.getTransferTime().get().getTime().valueAsMinutes();
		this.transTime2 = frame2.getTransferTime().isPresent() || frame2.getTransferTime().get().getTime() == null ? 0 : frame2.getTransferTime().get().getTime().valueAsMinutes();
		this.transTime3 = frame3.getTransferTime().isPresent() || frame3.getTransferTime().get().getTime() == null ? 0 : frame3.getTransferTime().get().getTime().valueAsMinutes();
		this.transTime4 = frame4.getTransferTime().isPresent() || frame4.getTransferTime().get().getTime() == null ? 0 : frame4.getTransferTime().get().getTime().valueAsMinutes();
		this.transTime5 = frame5.getTransferTime().isPresent() || frame5.getTransferTime().get().getTime() == null ? 0 : frame5.getTransferTime().get().getTime().valueAsMinutes();
		this.transTime6 = frame6.getTransferTime().isPresent() || frame6.getTransferTime().get().getTime() == null ? 0 : frame6.getTransferTime().get().getTime().valueAsMinutes();
		this.transTime7 = frame7.getTransferTime().isPresent() || frame7.getTransferTime().get().getTime() == null ? 0 : frame7.getTransferTime().get().getTime().valueAsMinutes();
		this.transTime8 = frame8.getTransferTime().isPresent() || frame8.getTransferTime().get().getTime() == null ? 0 : frame8.getTransferTime().get().getTime().valueAsMinutes();
		this.transTime9 = frame9.getTransferTime().isPresent() || frame9.getTransferTime().get().getTime() == null ? 0 : frame9.getTransferTime().get().getTime().valueAsMinutes();
		this.transTime10 = frame10.getTransferTime().isPresent() || frame10.getTransferTime().get().getTime() == null ? 0 : frame10.getTransferTime().get().getTime().valueAsMinutes();
		/*計算休日出勤時間*/
		this.calcHoliWorkTime1 = frame1.getHolidayWorkTime().isPresent() || frame1.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame1.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
		this.calcHoliWorkTime2 = frame2.getHolidayWorkTime().isPresent() || frame2.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame2.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
		this.calcHoliWorkTime3 = frame3.getHolidayWorkTime().isPresent() || frame3.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame3.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
		this.calcHoliWorkTime4 = frame4.getHolidayWorkTime().isPresent() || frame4.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame4.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
		this.calcHoliWorkTime5 = frame5.getHolidayWorkTime().isPresent() || frame5.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame5.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
		this.calcHoliWorkTime6 = frame6.getHolidayWorkTime().isPresent() || frame6.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame6.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
		this.calcHoliWorkTime7 = frame7.getHolidayWorkTime().isPresent() || frame7.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame7.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
		this.calcHoliWorkTime8 = frame8.getHolidayWorkTime().isPresent() || frame8.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame8.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
		this.calcHoliWorkTime9 = frame9.getHolidayWorkTime().isPresent() || frame9.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame9.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
		this.calcHoliWorkTime10 = frame10.getHolidayWorkTime().isPresent() || frame10.getHolidayWorkTime().get().getCalcTime() == null ? 0 : frame10.getHolidayWorkTime().get().getCalcTime().valueAsMinutes();
		/*計算振替時間*/
		this.calcTransTime1 = frame1.getTransferTime().isPresent() || frame1.getTransferTime().get().getCalcTime() == null ? 0 : frame1.getTransferTime().get().getCalcTime().valueAsMinutes();
		this.calcTransTime2 = frame2.getTransferTime().isPresent() || frame2.getTransferTime().get().getCalcTime() == null ? 0 : frame2.getTransferTime().get().getCalcTime().valueAsMinutes();
		this.calcTransTime3 = frame3.getTransferTime().isPresent() || frame3.getTransferTime().get().getCalcTime() == null ? 0 : frame3.getTransferTime().get().getCalcTime().valueAsMinutes();
		this.calcTransTime4 = frame4.getTransferTime().isPresent() || frame4.getTransferTime().get().getCalcTime() == null ? 0 : frame4.getTransferTime().get().getCalcTime().valueAsMinutes();
		this.calcTransTime5 = frame5.getTransferTime().isPresent() || frame5.getTransferTime().get().getCalcTime() == null ? 0 : frame5.getTransferTime().get().getCalcTime().valueAsMinutes();
		this.calcTransTime6 = frame6.getTransferTime().isPresent() || frame6.getTransferTime().get().getCalcTime() == null ? 0 : frame6.getTransferTime().get().getCalcTime().valueAsMinutes();
		this.calcTransTime7 = frame7.getTransferTime().isPresent() || frame7.getTransferTime().get().getCalcTime() == null ? 0 : frame7.getTransferTime().get().getCalcTime().valueAsMinutes();
		this.calcTransTime8 = frame8.getTransferTime().isPresent() || frame8.getTransferTime().get().getCalcTime() == null ? 0 : frame8.getTransferTime().get().getCalcTime().valueAsMinutes();
		this.calcTransTime9 = frame9.getTransferTime().isPresent() || frame9.getTransferTime().get().getCalcTime() == null ? 0 : frame9.getTransferTime().get().getCalcTime().valueAsMinutes();
		this.calcTransTime10 = frame10.getTransferTime().isPresent() || frame10.getTransferTime().get().getCalcTime() == null ? 0 : frame10.getTransferTime().get().getCalcTime().valueAsMinutes();
		/*事前申請時間*/
		this.preAppTime1 = frame1.getBeforeApplicationTime().get() == null ? 0 : frame1.getBeforeApplicationTime().get().valueAsMinutes();
		this.preAppTime2 = frame2.getBeforeApplicationTime().get() == null ? 0 : frame2.getBeforeApplicationTime().get().valueAsMinutes();
		this.preAppTime3 = frame3.getBeforeApplicationTime().get() == null ? 0 : frame3.getBeforeApplicationTime().get().valueAsMinutes();
		this.preAppTime4 = frame4.getBeforeApplicationTime().get() == null ? 0 : frame4.getBeforeApplicationTime().get().valueAsMinutes();
		this.preAppTime5 = frame5.getBeforeApplicationTime().get() == null ? 0 : frame5.getBeforeApplicationTime().get().valueAsMinutes();
		this.preAppTime6 = frame6.getBeforeApplicationTime().get() == null ? 0 : frame6.getBeforeApplicationTime().get().valueAsMinutes();
		this.preAppTime7 = frame7.getBeforeApplicationTime().get() == null ? 0 : frame7.getBeforeApplicationTime().get().valueAsMinutes();
		this.preAppTime8 = frame8.getBeforeApplicationTime().get() == null ? 0 : frame8.getBeforeApplicationTime().get().valueAsMinutes();
		this.preAppTime9 = frame9.getBeforeApplicationTime().get() == null ? 0 : frame9.getBeforeApplicationTime().get().valueAsMinutes();
		this.preAppTime10 = frame10.getBeforeApplicationTime().get() == null ? 0 : frame10.getBeforeApplicationTime().get().valueAsMinutes();	
		
		this.legHoliWorkMidn  = 0;
		this.calcLegHoliWorkMidn = 0;
		this.illegHoliWorkMidn = 0;
		this.calcIllegHoliWorkMidn = 0;
		this.pbHoliWorkMidn = 0;
		this.calcPbHoliWorkMidn = 0;
		
		if(domain.getHolidayMidNightWork().isPresent()) {
			Optional<HolidayWorkMidNightTime> within = getHolidayMidNightWork(domain.getHolidayMidNightWork().get(), StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork);
			Optional<HolidayWorkMidNightTime> excess = getHolidayMidNightWork(domain.getHolidayMidNightWork().get(), StaturoryAtrOfHolidayWork.ExcessOfStatutoryHolidayWork);
			Optional<HolidayWorkMidNightTime> publicWork = getHolidayMidNightWork(domain.getHolidayMidNightWork().get(), StaturoryAtrOfHolidayWork.PublicHolidayWork);
			if(within.isPresent()) {
				if(within.get().getTime() != null){
					/*法定内休日出勤深夜*/
					this.legHoliWorkMidn = within.get().getTime().getTime() == null ? null : within.get().getTime().getTime().valueAsMinutes();
					/*計算法定内休日出勤深夜*/
					this.calcLegHoliWorkMidn = within.get().getTime().getCalcTime() == null ? 0 : within.get().getTime().getCalcTime().valueAsMinutes();
				}
			}
			if(excess.isPresent()) {
				if(excess != null){
					/*法定外休日出勤深夜*/
					this.illegHoliWorkMidn = excess.get().getTime().getTime() == null ? 0 : excess.get().getTime().getTime().valueAsMinutes();
					/*計算法定外休日出勤深夜*/
					this.calcIllegHoliWorkMidn = excess.get().getTime().getCalcTime() == null ? 0 : excess.get().getTime().getCalcTime().valueAsMinutes();
				}
			}
			if(publicWork.isPresent())
			{
				if(publicWork != null){
					/*祝日日出勤深夜*/
					this.pbHoliWorkMidn = publicWork.get().getTime().getTime() == null ? 0 : publicWork.get().getTime().getTime().valueAsMinutes();
					/*計算祝日日出勤深夜*/
					this.calcPbHoliWorkMidn = publicWork.get().getTime().getCalcTime() == null ? 0 : publicWork.get().getTime().getCalcTime().valueAsMinutes();
				}
			}
		}
		
		/*休日出勤拘束時間*/
		this.holiWorkBindTime = domain.getHolidayTimeSpentAtWork() == null ? 0 : domain.getHolidayTimeSpentAtWork().valueAsMinutes();
	}


	private Optional<HolidayWorkMidNightTime> getHolidayMidNightWork(HolidayMidnightWork domain, StaturoryAtrOfHolidayWork statutoryAttr) {
		return domain.getHolidayWorkMidNightTime().stream().filter(tc -> tc.getStatutoryAtr().equals(statutoryAttr)).findFirst();
	}


	private HolidayWorkFrameTime getHolidayTimeFrame(List<HolidayWorkFrameTime> frames, int frameNo) {
		return frames.stream().filter(tc -> tc.getHolidayFrameNo().v() == frameNo).findFirst().get();
	}
	
	
	public HolidayWorkTimeOfDaily toDomain() {
		
		List<HolidayWorkFrameTimeSheet> holidayWorkFrameTimeSheetList = new ArrayList<>();//KrcdtDayHolidyWorkTsのtoDomainを呼べる？
		
		List<HolidayWorkFrameTime> holiWorkFrameTimeList = new ArrayList<>();
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(1),
														   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime1),new AttendanceTime(this.calcHoliWorkTime1))),
														   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime1),new AttendanceTime(this.calcTransTime1))),
														   Finally.of(new AttendanceTime(this.preAppTime1))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(2),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime2),new AttendanceTime(this.calcHoliWorkTime2))),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime2),new AttendanceTime(this.calcTransTime2))),
				   										   Finally.of(new AttendanceTime(this.preAppTime2))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(3),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime3),new AttendanceTime(this.calcHoliWorkTime3))),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime3),new AttendanceTime(this.calcTransTime3))),
				   										   Finally.of(new AttendanceTime(this.preAppTime3))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(4),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime4),new AttendanceTime(this.calcHoliWorkTime4))),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime4),new AttendanceTime(this.calcTransTime4))),
				   										   Finally.of(new AttendanceTime(this.preAppTime4))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(5),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime5),new AttendanceTime(this.calcHoliWorkTime5))),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime5),new AttendanceTime(this.calcTransTime5))),
				   										   Finally.of(new AttendanceTime(this.preAppTime5))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(6),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime6),new AttendanceTime(this.calcHoliWorkTime6))),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime6),new AttendanceTime(this.calcTransTime6))),
				   										   Finally.of(new AttendanceTime(this.preAppTime6))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(7),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime7),new AttendanceTime(this.calcHoliWorkTime7))),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime7),new AttendanceTime(this.calcTransTime7))),
				   										   Finally.of(new AttendanceTime(this.preAppTime7))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(8),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime8),new AttendanceTime(this.calcHoliWorkTime8))),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime8),new AttendanceTime(this.calcTransTime8))),
				   										   Finally.of(new AttendanceTime(this.preAppTime8))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(9),
														   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime9),new AttendanceTime(this.calcHoliWorkTime9))),
														   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime9),new AttendanceTime(this.calcTransTime9))),
														   Finally.of(new AttendanceTime(this.preAppTime9))));
		holiWorkFrameTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(10),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.holiWorkTime10),new AttendanceTime(this.calcHoliWorkTime10))),
				   										   Finally.of(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.transTime10),new AttendanceTime(this.calcTransTime10))),
				   										   Finally.of(new AttendanceTime(this.preAppTime10))));
		
		List<HolidayWorkMidNightTime> holidayWorkMidNightTimeList = new ArrayList<>();
		holidayWorkMidNightTimeList.add(new HolidayWorkMidNightTime(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.legHoliWorkMidn),new AttendanceTime(this.calcLegHoliWorkMidn)),
																	StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork));
		holidayWorkMidNightTimeList.add(new HolidayWorkMidNightTime(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.illegHoliWorkMidn),new AttendanceTime(this.calcIllegHoliWorkMidn)),
																	StaturoryAtrOfHolidayWork.ExcessOfStatutoryHolidayWork));
		holidayWorkMidNightTimeList.add(new HolidayWorkMidNightTime(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(this.pbHoliWorkMidn),new AttendanceTime(this.calcPbHoliWorkMidn)),
																	StaturoryAtrOfHolidayWork.PublicHolidayWork));
		
		return new HolidayWorkTimeOfDaily(holidayWorkFrameTimeSheetList,holiWorkFrameTimeList,Finally.of(new HolidayMidnightWork(holidayWorkMidNightTimeList)), new AttendanceTime(this.holiWorkBindTime));
	}
	
	
	
}