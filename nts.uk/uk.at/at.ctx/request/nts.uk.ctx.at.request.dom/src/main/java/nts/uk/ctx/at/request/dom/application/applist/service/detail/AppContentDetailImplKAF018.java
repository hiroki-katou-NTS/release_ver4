package nts.uk.ctx.at.request.dom.application.applist.service.detail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.i18n.I18NText;
import nts.uk.ctx.at.request.dom.application.applist.service.OverTimeFrame;

/**
 * HOATT DOI UNG NOI DUNG DON XIN KAF018 GIONG VOI CMM045 Bug #106312 2019.04.08
 * 
 * @author hoatt
 */
@Stateless
public class AppContentDetailImplKAF018 implements AppContentDetailKAF018 {

	@Inject
	private AppDetailInfoRepository appDetailInfoRepo;

	/**
	 * get content OverTimeBf
	 * 残業申請 kaf005 - appType = 0
	 * @param companyID
	 * @param appId
	 * @param detailSet
	 * @return
	 */
	@Override
	public String getContentOverTimeBf(String companyId, String appId, int detailSet) {
		AppOverTimeInfoFull overTime = appDetailInfoRepo.getAppOverTimeInfo(companyId, appId);
		String time1 = overTime.getWorkClockFrom1() == "" ? ""
				: overTime.getWorkClockFrom1() + I18NText.getText("CMM045_100") + overTime.getWorkClockTo1();
		String time2 = overTime.getWorkClockFrom2() == "" ? ""
				: overTime.getWorkClockFrom2() + I18NText.getText("CMM045_100") + overTime.getWorkClockFrom2();
		String contentv4 = time1 + time2;
		String contentv42 = this.convertFrameTime(overTime.getLstFrame());
		// No.417
		String timeNo417 = this.displayTimeNo417(overTime.getTimeNo417());
		String cont1 = contentv42 == "" ? timeNo417 : timeNo417 == "" ? contentv42 : contentv42 + "　" + timeNo417;
		String setTrue = contentv4 == "" ? cont1 : cont1 == "" ? contentv4 : contentv4 + "　" + cont1;
		String appCt005 = detailSet == 1 ? setTrue : cont1;
		return appCt005;
	}
	/**
	 * get content absence
	 * 休暇申請 kaf006 - appType = 1
	 * @param appId
	 * @param detailSet
	 * @return
	 */
	@Override
	public String getContentAbsence(String companyId, String appId, int detailSet, int day) {
        String appContent006 = "";
        AppAbsenceFull absence = appDetailInfoRepo.getAppAbsenceInfo(companyId, appId, day);
        //ver46
        if(absence.getHolidayAppType() != 3){//休暇申請.休暇種類　≠ 特別休暇
            appContent006 = this.convertAbsenceNotSpecial(absence);
        }
        if(absence.getHolidayAppType() == 3){//休暇申請.休暇種類　＝ 特別休暇 ver39
            appContent006 = this.convertAbsenceSpecial(absence);
        }
		return appContent006;
	}
	/**
	 * get content work change
	 * 勤務変更申請 kaf007 - appType = 2
	 * @param appId
	 * @param detailSet
	 * @return
	 */
	@Override
	public String getContentWorkChange(String companyId, String appId, int detailSet) {
		AppWorkChangeFull wkChange = appDetailInfoRepo.getAppWorkChangeInfo(companyId, appId);
		String go1 = wkChange.getGoWorkAtr1() == 0 ? "" + I18NText.getText("CMM045_252") + wkChange.getWorkTimeStart1() :
				wkChange.getWorkTimeStart1();
		String back1 = wkChange.getBackHomeAtr1() == 0 ? I18NText.getText("CMM045_252") + wkChange.getWorkTimeEnd1() :
				wkChange.getWorkTimeEnd1();
		String time1 = go1 == "" ? "" : go1 + I18NText.getText("CMM045_100") + back1;
		String go2 = (wkChange.getGoWorkAtr2() == null || wkChange.getGoWorkAtr2() == 1) ? wkChange.getWorkTimeStart2() : 
				"" + I18NText.getText("CMM045_252") + wkChange.getWorkTimeStart2();
		String back2 = (wkChange.getBackHomeAtr2() == null || wkChange.getBackHomeAtr2() == 1) ? wkChange.getWorkTimeEnd2() :
				I18NText.getText("CMM045_252") + wkChange.getWorkTimeEnd2();
		String time2 = go2 == "" ? "" : go2 + I18NText.getText("CMM045_100") + back2;
		String breakTime = wkChange.getBreakTimeStart1() == "" ? "" : I18NText.getText("CMM045_251") +
				wkChange.getBreakTimeEnd1() + I18NText.getText("CMM045_100") + wkChange.getBreakTimeEnd1();
		String workName = wkChange.getWorkTypeName() == "" ? wkChange.getWorkTimeName() : wkChange.getWorkTimeName() == "" ?  
				wkChange.getWorkTypeName() : wkChange.getWorkTypeName() + "　" + wkChange.getWorkTimeName();
		String time = time1 == "" ? time2 : time2 == "" ? time1 : time1 + "　" + time2;
		String cont1 = workName == "" ? time : time == "" ? workName : workName + "　" + time;
        String cont2 = cont1 == "" ? breakTime : breakTime == "" ? cont1 : cont1 + "　" + breakTime;
		return cont2;
	}
	/**
	 * get content go back
	 * 直行直帰申請 kaf009 - appType = 4
	 * @param appId
	 * @param detailSet
	 * @return
	 */
	@Override
	public String getContentGoBack(String companyId, String appId, int detailSet) {
		AppGoBackInfoFull goBack = appDetailInfoRepo.getAppGoBackInfo(companyId, appId);
		String go1 = goBack.getGoWorkAtr1() == 1 ? goBack.getWorkTimeStart1() == "" ? I18NText.getText("CMM045_259") + goBack.getWorkTimeStart1() : I18NText.getText("CMM045_259") + "　" + goBack.getWorkTimeStart1() : "";
		String back1 = goBack.getBackHomeAtr1() == 1 ? goBack.getWorkTimeEnd1() == "" ? I18NText.getText("CMM045_260") + goBack.getWorkTimeEnd1() : I18NText.getText("CMM045_260") + "　" + goBack.getWorkTimeEnd1() : "";
		String go2 = goBack.getGoWorkAtr2() != null && goBack.getGoWorkAtr2() == 1 ? goBack.getWorkTimeStart2() == "" ? I18NText.getText("CMM045_259") + goBack.getWorkTimeStart2() : I18NText.getText("CMM045_259") + "　" + goBack.getWorkTimeStart2() : "";
        String back2 = goBack.getBackHomeAtr2() != null && goBack.getBackHomeAtr2() == 1 ? goBack.getWorkTimeEnd2() == "" ? I18NText.getText("CMM045_260") + goBack.getWorkTimeEnd2() : I18NText.getText("CMM045_260") + "　" + goBack.getWorkTimeEnd2() : "";
        String goback1 = go1 == "" ? back1 : back1 == "" ? go1 : go1 + "　" + back1;
        String goback2 = go2 == "" ? back2 : back2 == "" ? go2 : go2 + "　" + back2;
        String gobackA = goback1 == "" ? goback2 : goback2 == "" ? goback1 : goback1 + "　" + goback2;
		return gobackA;
	}
	/**
	 * get Content HdWorkBf
	 * 休日出勤時間申請 kaf010 - appTYpe = 6
	 * @param appId
	 * @param detailSet
	 * @return
	 */
	@Override
	public String getContentHdWorkBf(String companyId, String appId, int detailSet) {
		AppHolidayWorkFull hdWork = appDetailInfoRepo.getAppHolidayWorkInfo(companyId, appId);
		String ca1 = hdWork.getStartTime1() == "" ? "" : hdWork.getStartTime1() + I18NText.getText("CMM045_100") + hdWork.getEndTime1();
		String ca2 = hdWork.getStartTime2() == "" ? "" : hdWork.getStartTime2() + I18NText.getText("CMM045_100") + hdWork.getEndTime2();
		String name = hdWork.getWorkTypeName() == "" ? hdWork.getWorkTimeName() : hdWork.getWorkTimeName() == "" ?
				hdWork.getWorkTypeName() : hdWork.getWorkTypeName() + "　" + hdWork.getWorkTimeName();
		String caF = ca1 == "" ? ca2 : ca2 == "" ? ca1 : ca1 + "　" + ca2;
		String cont1 = name == "" ? caF : caF == "" ? name : name + "　" + caF;
		String appContent010 = cont1 == "" ? this.convertFrameTimeHd(hdWork.getLstFrame()) : cont1 + "　" + this.convertFrameTimeHd(hdWork.getLstFrame());
        //No.417
		String timeNo417 = this.displayTimeNo417(hdWork.getTimeNo417());
		String appCt010 = appContent010 + timeNo417;
		return appCt010;
	}

	private String displayTimeNo417(TimeNo417 timeNo417) {
		if (timeNo417 == null) {
			return "";
		}
		if (timeNo417.getTime36() <= 0) {// 「時間外時間の詳細」．36時間 > 0 の場合 追加
			return "";
		}
		if (timeNo417.getNumOfYear36Over() == 0) {// 未超過の場合
			return "<br/>" + I18NText.getText("CMM045_282") + this.convertTime_Short_HM(timeNo417.getTotalOv()) + "　"
					+ I18NText.getText("CMM045_283")
					+ I18NText.getText("CMM045_284", String.valueOf(timeNo417.getNumOfYear36Over()));
		}
		// 超過した場合
		String a1 = I18NText.getText("CMM045_282") + this.convertTime_Short_HM(timeNo417.getTotalOv()) + "　"
				+ I18NText.getText("CMM045_283")
				+ I18NText.getText("CMM045_284", String.valueOf(timeNo417.getNumOfYear36Over()));
		List<Integer> listMY = timeNo417.getLstOverMonth().stream().sorted().collect(Collectors.toList());
		List<Integer> lstMonth = listMY.stream().map(c -> c % 100).collect(Collectors.toList());
		String a2 = "";
		for (Integer mon : lstMonth) {
			a2 = a2 == "" ? I18NText.getText("CMM045_285", String.valueOf(mon))
					: a2 + "、" + I18NText.getText("CMM045_285", String.valueOf(mon));
		}
		return a2 == "" ? "<br/>" + a1 : "<br/>" + a1 + "(" + a2 + ")";
	}

	private String convertTime_Short_HM(int time) {
		return (time / 60 + ":" + (time % 60 < 10 ? "0" + time % 60 : time % 60));
	}

	/**
	 * convert frame time over time
	 */
	private String convertFrameTime(List<OverTimeFrame> lstFrame) {
		String framName = "";
		// int time = 0;
		int count = 0;
		List<OverTimeFrame> lstSort = this.sortFrameTime(lstFrame, 0);
		for (OverTimeFrame item : lstSort) {
			if (item.getApplicationTime() != 0) {// 時間外深夜時間
				if (count < 3) {
					// ver42
					framName += item.getName() + this.convertTime_Short_HM(item.getApplicationTime()) + "　";
				}
				// time += item.getApplicationTime();
				count += 1;
			}
		}
		int other = count > 3 ? count - 3 : 0;
		String otherInfo = other > 0 ? I18NText.getText("CMM045_231", String.valueOf(other)) : "";
		String result = framName + otherInfo;
		return result;
	}

	private List<OverTimeFrame> sortFrameTime(List<OverTimeFrame> lstFrame, int appType) {
		List<OverTimeFrame> result = new ArrayList<>();
		List<OverTimeFrame> lstA0 = lstFrame.stream().filter(c -> c.getAttendanceType() == 0)
				.collect(Collectors.toList());// RESTTIME
		List<OverTimeFrame> lstA1 = lstFrame.stream().filter(c -> c.getAttendanceType() == 1)
				.collect(Collectors.toList());// NORMALOVERTIME
		List<OverTimeFrame> lstA2 = lstFrame.stream().filter(c -> c.getAttendanceType() == 2)
				.collect(Collectors.toList());// BREAKTIME
		List<OverTimeFrame> lstA3 = lstFrame.stream().filter(c -> c.getAttendanceType() == 3)
				.collect(Collectors.toList());// BONUSPAYTIME
		List<OverTimeFrame> lstA4 = lstFrame.stream().filter(c -> c.getAttendanceType() == 4)
				.collect(Collectors.toList());// BONUSSPECIALDAYTIME
		
		Collections.sort(lstA0, Comparator.comparing(OverTimeFrame::getFrameNo));
		Collections.sort(lstA1, Comparator.comparing(OverTimeFrame::getFrameNo));
		Collections.sort(lstA2, Comparator.comparing(OverTimeFrame::getFrameNo));
		Collections.sort(lstA3, Comparator.comparing(OverTimeFrame::getFrameNo));
		Collections.sort(lstA4, Comparator.comparing(OverTimeFrame::getFrameNo));

		if (appType == 0) {// overtime
			// push list A1 (残業時間)
			result.addAll(lstA1);
			// push list A2 (休出時間)
			result.addAll(lstA2);
			// push list A3 (加給時間)
			result.addAll(lstA3);
			// push list A4 (特定日加給時間)
			result.addAll(lstA4);
		} else {// holiday
			// push list A2 (休出時間)
			result.addAll(lstA2);
			// push list A1 (残業時間)
			result.addAll(lstA1);
			// push list A3 (加給時間)
			result.addAll(lstA3);
		}
		return result;
	}
    //休暇申請.休暇種類　≠ 特別休暇 ver46
    private String convertAbsenceNotSpecial(AppAbsenceFull absence){
        return absence.getWorkTypeName();
    }
    //※休暇申請.休暇種類　＝ 特別休暇 ver39
    private String convertAbsenceSpecial(AppAbsenceFull absence){
        String day = absence.getMournerFlag() == true ? I18NText.getText("CMM045_277") + "　" +  absence.getDay() + I18NText.getText("CMM045_278") : absence.getDay() + I18NText.getText("CMM045_278");
        String name = absence.getWorkTypeName() == "" ? absence.getRelationshipName() : absence.getRelationshipName() == "" ? absence.getWorkTypeName() : absence.getWorkTypeName() + "　" + absence.getRelationshipName();
        String result = name == "" ? day : name + "　" + day;
        return result;
    }
    /**
     * convert frame time holiday work
     */
    private String convertFrameTimeHd(List<OverTimeFrame> lstFrame){
        String framName = "";
        int count = 0;
        List<OverTimeFrame> lstSort = this.sortFrameTime(lstFrame, 6);
        for(OverTimeFrame item : lstSort){
        	if (item.getApplicationTime() != 0) {
                if (count < 3) {
                    framName += item.getName() + this.convertTime_Short_HM(item.getApplicationTime()) + "　";
                }
//                time += item.getApplicationTime();
                count += 1;
            }
        }
        int other = count > 3 ? count - 3 : 0;
        String otherInfo = other > 0 ? I18NText.getText("CMM045_231", String.valueOf(other)) : "";
        //#102010
        String result = framName + otherInfo;
        return result;
    }
}
