package nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.mergetable.RemainMerge;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialHolidayRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialHolidayRemainDataRepository;
import nts.uk.shr.com.time.calendar.period.YearMonthPeriod;

@Stateless
public class SpecialHolidayRemainDataSeviceImpl implements SpecialHolidayRemainDataSevice{
	@Inject
	private SpecialHolidayRemainDataRepository speRemainDataRepo;
	
	@Override
	public List<SpecialHolidayRemainDataOutput> getSpeHoliOfConfirmedMonthly(String sid, YearMonth startMonth,
			YearMonth endMonth) {
		List<SpecialHolidayRemainDataOutput> lstOutData = new ArrayList<>();
		for (YearMonth month = startMonth; month.lessThanOrEqualTo(endMonth); month = month.addMonths(1)) {
			
			//ドメインモデル「特別休暇月別残数データ」を取得
			List<SpecialHolidayRemainData> lstRemainData = speRemainDataRepo.findByYearMonthOrderByStartYmd(sid, month);
			for (SpecialHolidayRemainData remainData : lstRemainData) {
				SpecialHolidayRemainDataOutput dataOut = new SpecialHolidayRemainDataOutput();
				List<SpecialHolidayRemainDataOutput> lstTmp = new ArrayList<>();
				for (SpecialHolidayRemainDataOutput tmpData : lstOutData) {
					if(tmpData.getSpecialHolidayCd() == remainData.getSpecialHolidayCd() && tmpData.getYm() == month && tmpData.getSid() == remainData.getSid()) {
						lstTmp.add(tmpData);
					}
				}
				if(lstTmp.isEmpty()) {
					dataOut.setSid(sid);
					dataOut.setYm(month);
					dataOut.setSpecialHolidayCd(remainData.getSpecialHolidayCd());
					dataOut.setUseDays(remainData.getSpecialLeave().getUseNumber().getUseDays().getUseDays().v());
					dataOut.setBeforeUseDays(remainData.getSpecialLeave().getBeforeRemainGrant().getDays().v());
					dataOut.setAfterUseDays(remainData.getSpecialLeave().getAfterRemainGrant().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getDays().v() : 0);
					if (remainData.getSpecialLeave().getUseNumber().getUseTimes().isPresent()){
						val useTimes = remainData.getSpecialLeave().getUseNumber().getUseTimes().get();
						dataOut.setUseTimes(useTimes.getUseTimes().v());
						dataOut.setBeforeUseTimes(useTimes.getBeforeUseGrantTimes().v());
						dataOut.setAfterUseTimes(useTimes.getAfterUseGrantTimes().isPresent() ? useTimes.getAfterUseGrantTimes().get().v() : 0 );
						dataOut.setUseNumber(useTimes.getUseNumber().v());
						dataOut.setFactUseNumber(useTimes.getUseNumber().v());
					}
					else {
						dataOut.setUseTimes(0);
						dataOut.setBeforeUseTimes(0);
						dataOut.setAfterUseTimes(0);
						dataOut.setUseNumber(0);
						dataOut.setFactUseNumber(0);
					}
					dataOut.setFactUseDays(remainData.getActualSpecial().getUseNumber().getUseDays().getUseDays().v());
					dataOut.setRemainDays(remainData.getSpecialLeave().getRemain().getDays().v());
					dataOut.setRemainTimes(remainData.getSpecialLeave().getRemain().getTime().isPresent() ? remainData.getSpecialLeave().getRemain().getTime().get().v() : 0);
					dataOut.setFactRemainDays(remainData.getActualSpecial().getRemain().getDays().v());
					dataOut.setFactRemainTimes(remainData.getActualSpecial().getRemain().getTime().isPresent() ? remainData.getActualSpecial().getRemain().getTime().get().v() : 0);
					dataOut.setBeforeRemainDays(remainData.getSpecialLeave().getBeforeRemainGrant().getDays().v());
					dataOut.setBeforeRemainTimes(remainData.getSpecialLeave().getBeforeRemainGrant().getTime().isPresent() ? remainData.getSpecialLeave().getBeforeRemainGrant().getTime().get().v() : 0);
					dataOut.setBeforeFactRemainDays(remainData.getActualSpecial().getBeforRemainGrant().getDays().v());
					dataOut.setBeforeFactRemainTimes(remainData.getActualSpecial().getBeforRemainGrant().getTime().isPresent() ? remainData.getActualSpecial().getBeforRemainGrant().getTime().get().v() : 0);
					dataOut.setAfterRemainDays(remainData.getSpecialLeave().getAfterRemainGrant().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getDays().v() : 0);
					dataOut.setAfterRemainTimes(remainData.getSpecialLeave().getAfterRemainGrant().isPresent() && remainData.getSpecialLeave().getAfterRemainGrant().get().getTime().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getTime().get().v() : 0);
					dataOut.setAfterFactRemainDays(remainData.getActualSpecial().getAfterRemainGrant().isPresent() ? remainData.getActualSpecial().getAfterRemainGrant().get().getDays().v() : 0);
					dataOut.setNotUseDays(remainData.getSpecialLeave().getUnDegestionNumber().getDays().v());
					dataOut.setNotUseTime(remainData.getSpecialLeave().getUnDegestionNumber().getTimes().isPresent() ? remainData.getSpecialLeave().getUnDegestionNumber().getTimes().get().v() : 0);
					dataOut.setGrantAtr(remainData.isGrantAtr() ? 1 : 0);
					dataOut.setGrantDays(remainData.getGrantDays().isPresent() ? remainData.getGrantDays().get().v() : 0);
					lstOutData.add(dataOut);
				} else {
					SpecialHolidayRemainDataOutput tmp = lstTmp.get(0);
					lstOutData.remove(tmp);
					tmp.setUseDays(tmp.getUseDays() + remainData.getSpecialLeave().getUseNumber().getUseDays().getUseDays().v());
					tmp.setBeforeUseDays(tmp.getBeforeUseDays() + remainData.getSpecialLeave().getBeforeRemainGrant().getDays().v());
					tmp.setAfterUseDays(tmp.getAfterUseDays() + (remainData.getSpecialLeave().getAfterRemainGrant().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getDays().v() : 0));
					if (remainData.getSpecialLeave().getUseNumber().getUseTimes().isPresent()){
						val useTimes = remainData.getSpecialLeave().getUseNumber().getUseTimes().get();
						tmp.setUseTimes(tmp.getUseTimes() + useTimes.getUseTimes().v());
						tmp.setBeforeUseTimes(tmp.getBeforeUseTimes() + useTimes.getBeforeUseGrantTimes().v());
						tmp.setAfterUseTimes(tmp.getAfterUseTimes() + (useTimes.getAfterUseGrantTimes().isPresent() ? useTimes.getAfterUseGrantTimes().get().v() : 0 ));
						tmp.setUseNumber(tmp.getUseNumber() + useTimes.getUseNumber().v());
						tmp.setFactUseNumber(tmp.getFactUseNumber() + useTimes.getUseNumber().v());
					}
					else {
						tmp.setUseTimes(tmp.getUseTimes());
						tmp.setBeforeUseTimes(tmp.getBeforeUseTimes());
						tmp.setAfterUseTimes(tmp.getAfterUseTimes());
						tmp.setUseNumber(tmp.getUseNumber());
						tmp.setFactUseNumber(tmp.getFactUseNumber());
					}
					tmp.setFactUseDays(tmp.getFactUseDays() + remainData.getActualSpecial().getUseNumber().getUseDays().getUseDays().v());
					tmp.setRemainDays(tmp.getRemainDays() + remainData.getSpecialLeave().getRemain().getDays().v());
					tmp.setRemainTimes(tmp.getRemainTimes() + (remainData.getSpecialLeave().getRemain().getTime().isPresent() ? remainData.getSpecialLeave().getRemain().getTime().get().v() : 0));
					tmp.setFactRemainDays(tmp.getFactRemainDays() + remainData.getActualSpecial().getRemain().getDays().v());
					tmp.setFactRemainTimes(tmp.getAfterFactRemainTimes() + (remainData.getActualSpecial().getRemain().getTime().isPresent() ? remainData.getActualSpecial().getRemain().getTime().get().v() : 0));
					tmp.setBeforeRemainDays(tmp.getBeforeRemainDays() + remainData.getSpecialLeave().getBeforeRemainGrant().getDays().v());
					tmp.setBeforeRemainTimes(tmp.getBeforeRemainTimes() + (remainData.getSpecialLeave().getBeforeRemainGrant().getTime().isPresent() ? remainData.getSpecialLeave().getBeforeRemainGrant().getTime().get().v() : 0));
					tmp.setBeforeFactRemainDays(tmp.getBeforeFactRemainDays() + remainData.getActualSpecial().getBeforRemainGrant().getDays().v());
					tmp.setBeforeFactRemainTimes(tmp.getBeforeFactRemainTimes() + (remainData.getActualSpecial().getBeforRemainGrant().getTime().isPresent() ? remainData.getActualSpecial().getBeforRemainGrant().getTime().get().v() : 0));
					tmp.setAfterRemainDays(tmp.getAfterRemainDays() + (remainData.getSpecialLeave().getAfterRemainGrant().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getDays().v() : 0));
					tmp.setAfterRemainTimes(tmp.getAfterRemainTimes() + (remainData.getSpecialLeave().getAfterRemainGrant().isPresent() && remainData.getSpecialLeave().getAfterRemainGrant().get().getTime().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getTime().get().v() : 0));
					tmp.setAfterFactRemainDays(tmp.getAfterFactRemainDays() + (remainData.getActualSpecial().getAfterRemainGrant().isPresent() ? remainData.getActualSpecial().getAfterRemainGrant().get().getDays().v() : 0));
					tmp.setNotUseDays(tmp.getNotUseDays() + remainData.getSpecialLeave().getUnDegestionNumber().getDays().v());
					tmp.setNotUseTime(tmp.getNotUseTime() + (remainData.getSpecialLeave().getUnDegestionNumber().getTimes().isPresent() ? remainData.getSpecialLeave().getUnDegestionNumber().getTimes().get().v() : 0));
					tmp.setGrantAtr(tmp.getGrantAtr() == 1 ? 1 : (remainData.isGrantAtr() ? 1 : 0));
					tmp.setGrantDays(tmp.getGrantDays() + (remainData.getGrantDays().isPresent() ? remainData.getGrantDays().get().v() : 0));
					lstOutData.add(tmp);
				}
				
			}
		}
		return lstOutData;
	}
	@Override
	public List<SpecialHolidayRemainDataOutput> getSpeHoliOfPeriodAndCodes(String sid, YearMonth startMonth,
			YearMonth endMonth, List<Integer> speCodes) {
		List<SpecialHolidayRemainDataOutput> lstOutput = new ArrayList<>();
		speCodes.stream().forEach(x -> {
			List<SpecialHolidayRemainDataOutput> lstOutputCode = this.getSpeHoliOfConfirmedMonthly(sid, startMonth, endMonth, x);
			if(!lstOutputCode.isEmpty()) {
				lstOutput.addAll(lstOutputCode);
			}
		});
		return lstOutput;
	}
	@Override
	public List<SpecialHolidayRemainDataOutput> getSpeHoliOfConfirmedMonthly(String sid, YearMonth startMonth,
			YearMonth endMonth, Integer speCode) {
		List<SpecialHolidayRemainDataOutput> lstOutData = new ArrayList<>();
		for (YearMonth month = startMonth; month.lessThanOrEqualTo(endMonth); month = month.addMonths(1)) {
			
			//ドメインモデル「特別休暇月別残数データ」を取得
			List<SpecialHolidayRemainData> lstRemainData = speRemainDataRepo.getByYmCode(sid, month, speCode);
			for (SpecialHolidayRemainData remainData : lstRemainData) {
				SpecialHolidayRemainDataOutput dataOut = new SpecialHolidayRemainDataOutput();
				List<SpecialHolidayRemainDataOutput> lstTmp = new ArrayList<>();
				for (SpecialHolidayRemainDataOutput tmpData : lstOutData) {
					if(tmpData.getSpecialHolidayCd() == remainData.getSpecialHolidayCd() && tmpData.getYm() == month && tmpData.getSid() == remainData.getSid()) {
						lstTmp.add(tmpData);
					}
				}
				if(lstTmp.isEmpty()) {
					dataOut.setSid(sid);
					dataOut.setYm(month);
					dataOut.setSpecialHolidayCd(remainData.getSpecialHolidayCd());
					dataOut.setUseDays(remainData.getSpecialLeave().getUseNumber().getUseDays().getUseDays().v());
					dataOut.setBeforeUseDays(remainData.getSpecialLeave().getBeforeRemainGrant().getDays().v());
					dataOut.setAfterUseDays(remainData.getSpecialLeave().getAfterRemainGrant().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getDays().v() : 0);
					if (remainData.getSpecialLeave().getUseNumber().getUseTimes().isPresent()){
						val useTimes = remainData.getSpecialLeave().getUseNumber().getUseTimes().get();
						dataOut.setUseTimes(useTimes.getUseTimes().v());
						dataOut.setBeforeUseTimes(useTimes.getBeforeUseGrantTimes().v());
						dataOut.setAfterUseTimes(useTimes.getAfterUseGrantTimes().isPresent() ? useTimes.getAfterUseGrantTimes().get().v() : 0 );
						dataOut.setUseNumber(useTimes.getUseNumber().v());
						dataOut.setFactUseNumber(useTimes.getUseNumber().v());
					}
					else {
						dataOut.setUseTimes(0);
						dataOut.setBeforeUseTimes(0);
						dataOut.setAfterUseTimes(0);
						dataOut.setUseNumber(0);
						dataOut.setFactUseNumber(0);
					}
					dataOut.setFactUseDays(remainData.getActualSpecial().getUseNumber().getUseDays().getUseDays().v());
					dataOut.setRemainDays(remainData.getSpecialLeave().getRemain().getDays().v());
					dataOut.setRemainTimes(remainData.getSpecialLeave().getRemain().getTime().isPresent() ? remainData.getSpecialLeave().getRemain().getTime().get().v() : 0);
					dataOut.setFactRemainDays(remainData.getActualSpecial().getRemain().getDays().v());
					dataOut.setFactRemainTimes(remainData.getActualSpecial().getRemain().getTime().isPresent() ? remainData.getActualSpecial().getRemain().getTime().get().v() : 0);
					dataOut.setBeforeRemainDays(remainData.getSpecialLeave().getBeforeRemainGrant().getDays().v());
					dataOut.setBeforeRemainTimes(remainData.getSpecialLeave().getBeforeRemainGrant().getTime().isPresent() ? remainData.getSpecialLeave().getBeforeRemainGrant().getTime().get().v() : 0);
					dataOut.setBeforeFactRemainDays(remainData.getActualSpecial().getBeforRemainGrant().getDays().v());
					dataOut.setBeforeFactRemainTimes(remainData.getActualSpecial().getBeforRemainGrant().getTime().isPresent() ? remainData.getActualSpecial().getBeforRemainGrant().getTime().get().v() : 0);
					dataOut.setAfterRemainDays(remainData.getSpecialLeave().getAfterRemainGrant().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getDays().v() : 0);
					dataOut.setAfterRemainTimes(remainData.getSpecialLeave().getAfterRemainGrant().isPresent() && remainData.getSpecialLeave().getAfterRemainGrant().get().getTime().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getTime().get().v() : 0);
					dataOut.setAfterFactRemainDays(remainData.getActualSpecial().getAfterRemainGrant().isPresent() ? remainData.getActualSpecial().getAfterRemainGrant().get().getDays().v() : 0);
					dataOut.setNotUseDays(remainData.getSpecialLeave().getUnDegestionNumber().getDays().v());
					dataOut.setNotUseTime(remainData.getSpecialLeave().getUnDegestionNumber().getTimes().isPresent() ? remainData.getSpecialLeave().getUnDegestionNumber().getTimes().get().v() : 0);
					dataOut.setGrantAtr(remainData.isGrantAtr() ? 1 : 0);
					dataOut.setGrantDays(remainData.getGrantDays().isPresent() ? remainData.getGrantDays().get().v() : 0);
					lstOutData.add(dataOut);
				} else {
					SpecialHolidayRemainDataOutput tmp = lstTmp.get(0);
					lstOutData.remove(tmp);
					tmp.setUseDays(tmp.getUseDays() + remainData.getSpecialLeave().getUseNumber().getUseDays().getUseDays().v());
					tmp.setBeforeUseDays(tmp.getBeforeUseDays() + remainData.getSpecialLeave().getBeforeRemainGrant().getDays().v());
					tmp.setAfterUseDays(tmp.getAfterUseDays() + (remainData.getSpecialLeave().getAfterRemainGrant().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getDays().v() : 0));
					if (remainData.getSpecialLeave().getUseNumber().getUseTimes().isPresent()){
						val useTimes = remainData.getSpecialLeave().getUseNumber().getUseTimes().get();
						tmp.setUseTimes(tmp.getUseTimes() + useTimes.getUseTimes().v());
						tmp.setBeforeUseTimes(tmp.getBeforeUseTimes() + useTimes.getBeforeUseGrantTimes().v());
						tmp.setAfterUseTimes(tmp.getAfterUseTimes() + (useTimes.getAfterUseGrantTimes().isPresent() ? useTimes.getAfterUseGrantTimes().get().v() : 0 ));
						tmp.setUseNumber(tmp.getUseNumber() + useTimes.getUseNumber().v());
						tmp.setFactUseNumber(tmp.getFactUseNumber() + useTimes.getUseNumber().v());
					}
					else {
						tmp.setUseTimes(tmp.getUseTimes());
						tmp.setBeforeUseTimes(tmp.getBeforeUseTimes());
						tmp.setAfterUseTimes(tmp.getAfterUseTimes());
						tmp.setUseNumber(tmp.getUseNumber());
						tmp.setFactUseNumber(tmp.getFactUseNumber());
					}
					tmp.setFactUseDays(tmp.getFactUseDays() + remainData.getActualSpecial().getUseNumber().getUseDays().getUseDays().v());
					tmp.setRemainDays(tmp.getRemainDays() + remainData.getSpecialLeave().getRemain().getDays().v());
					tmp.setRemainTimes(tmp.getRemainTimes() + (remainData.getSpecialLeave().getRemain().getTime().isPresent() ? remainData.getSpecialLeave().getRemain().getTime().get().v() : 0));
					tmp.setFactRemainDays(tmp.getFactRemainDays() + remainData.getActualSpecial().getRemain().getDays().v());
					tmp.setFactRemainTimes(tmp.getAfterFactRemainTimes() + (remainData.getActualSpecial().getRemain().getTime().isPresent() ? remainData.getActualSpecial().getRemain().getTime().get().v() : 0));
					tmp.setBeforeRemainDays(tmp.getBeforeRemainDays() + remainData.getSpecialLeave().getBeforeRemainGrant().getDays().v());
					tmp.setBeforeRemainTimes(tmp.getBeforeRemainTimes() + (remainData.getSpecialLeave().getBeforeRemainGrant().getTime().isPresent() ? remainData.getSpecialLeave().getBeforeRemainGrant().getTime().get().v() : 0));
					tmp.setBeforeFactRemainDays(tmp.getBeforeFactRemainDays() + remainData.getActualSpecial().getBeforRemainGrant().getDays().v());
					tmp.setBeforeFactRemainTimes(tmp.getBeforeFactRemainTimes() + (remainData.getActualSpecial().getBeforRemainGrant().getTime().isPresent() ? remainData.getActualSpecial().getBeforRemainGrant().getTime().get().v() : 0));
					tmp.setAfterRemainDays(tmp.getAfterRemainDays() + (remainData.getSpecialLeave().getAfterRemainGrant().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getDays().v() : 0));
					tmp.setAfterRemainTimes(tmp.getAfterRemainTimes() + (remainData.getSpecialLeave().getAfterRemainGrant().isPresent() && remainData.getSpecialLeave().getAfterRemainGrant().get().getTime().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getTime().get().v() : 0));
					tmp.setAfterFactRemainDays(tmp.getAfterFactRemainDays() + (remainData.getActualSpecial().getAfterRemainGrant().isPresent() ? remainData.getActualSpecial().getAfterRemainGrant().get().getDays().v() : 0));
					tmp.setNotUseDays(tmp.getNotUseDays() + remainData.getSpecialLeave().getUnDegestionNumber().getDays().v());
					tmp.setNotUseTime(tmp.getNotUseTime() + (remainData.getSpecialLeave().getUnDegestionNumber().getTimes().isPresent() ? remainData.getSpecialLeave().getUnDegestionNumber().getTimes().get().v() : 0));
					tmp.setGrantAtr(tmp.getGrantAtr() == 1 ? 1 : (remainData.isGrantAtr() ? 1 : 0));
					tmp.setGrantDays(tmp.getGrantDays() + (remainData.getGrantDays().isPresent() ? remainData.getGrantDays().get().v() : 0));
					lstOutData.add(tmp);
				}
				
			}
		}
		return lstOutData;
	}
	/**
	 * @author hoatt
	 * Doi ung response KDR001
	 * RequestList263: 社員の月毎の確定済み特別休暇を取得する - ver2
	 * @param sid 社員ID
	 * @param startMonth 年月期間
	 * @param endMonth 年月期間
	 * @return
	 */
	@Override
	public List<SpecialHolidayRemainDataOutput> getSpeHdOfConfMonVer2(String sid, YearMonthPeriod period, Map<YearMonth, List<RemainMerge>> mapRemainMer) {
		List<SpecialHolidayRemainDataOutput> lstOutData = new ArrayList<>();
		for (Map.Entry<YearMonth, List<RemainMerge>> entry : mapRemainMer.entrySet()) {
			//ドメインモデル「特別休暇月別残数データ」を取得
			List<SpecialHolidayRemainData> lstRemainData = new ArrayList<>();
			for(RemainMerge speMer : entry.getValue()){
				lstRemainData.addAll(speMer.getSpecialHolidayRemainList());
			}
			YearMonth month = entry.getKey();
			for (SpecialHolidayRemainData remainData : lstRemainData) {
				SpecialHolidayRemainDataOutput dataOut = new SpecialHolidayRemainDataOutput();
				List<SpecialHolidayRemainDataOutput> lstTmp = new ArrayList<>();
				for (SpecialHolidayRemainDataOutput tmpData : lstOutData) {
					if(tmpData.getSpecialHolidayCd() == remainData.getSpecialHolidayCd() && tmpData.getYm() == month && tmpData.getSid() == remainData.getSid()) {
						lstTmp.add(tmpData);
					}
				}
				if(lstTmp.isEmpty()) {
					dataOut.setSid(sid);
					dataOut.setYm(month);
					dataOut.setSpecialHolidayCd(remainData.getSpecialHolidayCd());
					dataOut.setUseDays(remainData.getSpecialLeave().getUseNumber().getUseDays().getUseDays().v());
					dataOut.setBeforeUseDays(remainData.getSpecialLeave().getBeforeRemainGrant().getDays().v());
					dataOut.setAfterUseDays(remainData.getSpecialLeave().getAfterRemainGrant().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getDays().v() : 0);
					if (remainData.getSpecialLeave().getUseNumber().getUseTimes().isPresent()){
						val useTimes = remainData.getSpecialLeave().getUseNumber().getUseTimes().get();
						dataOut.setUseTimes(useTimes.getUseTimes().v());
						dataOut.setBeforeUseTimes(useTimes.getBeforeUseGrantTimes().v());
						dataOut.setAfterUseTimes(useTimes.getAfterUseGrantTimes().isPresent() ? useTimes.getAfterUseGrantTimes().get().v() : 0 );
						dataOut.setUseNumber(useTimes.getUseNumber().v());
						dataOut.setFactUseNumber(useTimes.getUseNumber().v());
					}
					else {
						dataOut.setUseTimes(0);
						dataOut.setBeforeUseTimes(0);
						dataOut.setAfterUseTimes(0);
						dataOut.setUseNumber(0);
						dataOut.setFactUseNumber(0);
					}
					dataOut.setFactUseDays(remainData.getActualSpecial().getUseNumber().getUseDays().getUseDays().v());
					dataOut.setRemainDays(remainData.getSpecialLeave().getRemain().getDays().v());
					dataOut.setRemainTimes(remainData.getSpecialLeave().getRemain().getTime().isPresent() ? remainData.getSpecialLeave().getRemain().getTime().get().v() : 0);
					dataOut.setFactRemainDays(remainData.getActualSpecial().getRemain().getDays().v());
					dataOut.setFactRemainTimes(remainData.getActualSpecial().getRemain().getTime().isPresent() ? remainData.getActualSpecial().getRemain().getTime().get().v() : 0);
					dataOut.setBeforeRemainDays(remainData.getSpecialLeave().getBeforeRemainGrant().getDays().v());
					dataOut.setBeforeRemainTimes(remainData.getSpecialLeave().getBeforeRemainGrant().getTime().isPresent() ? remainData.getSpecialLeave().getBeforeRemainGrant().getTime().get().v() : 0);
					dataOut.setBeforeFactRemainDays(remainData.getActualSpecial().getBeforRemainGrant().getDays().v());
					dataOut.setBeforeFactRemainTimes(remainData.getActualSpecial().getBeforRemainGrant().getTime().isPresent() ? remainData.getActualSpecial().getBeforRemainGrant().getTime().get().v() : 0);
					dataOut.setAfterRemainDays(remainData.getSpecialLeave().getAfterRemainGrant().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getDays().v() : 0);
					dataOut.setAfterRemainTimes(remainData.getSpecialLeave().getAfterRemainGrant().isPresent() && remainData.getSpecialLeave().getAfterRemainGrant().get().getTime().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getTime().get().v() : 0);
					dataOut.setAfterFactRemainDays(remainData.getActualSpecial().getAfterRemainGrant().isPresent() ? remainData.getActualSpecial().getAfterRemainGrant().get().getDays().v() : 0);
					dataOut.setNotUseDays(remainData.getSpecialLeave().getUnDegestionNumber().getDays().v());
					dataOut.setNotUseTime(remainData.getSpecialLeave().getUnDegestionNumber().getTimes().isPresent() ? remainData.getSpecialLeave().getUnDegestionNumber().getTimes().get().v() : 0);
					dataOut.setGrantAtr(remainData.isGrantAtr() ? 1 : 0);
					dataOut.setGrantDays(remainData.getGrantDays().isPresent() ? remainData.getGrantDays().get().v() : 0);
					lstOutData.add(dataOut);
				} else {
					SpecialHolidayRemainDataOutput tmp = lstTmp.get(0);
					lstOutData.remove(tmp);
					tmp.setUseDays(tmp.getUseDays() + remainData.getSpecialLeave().getUseNumber().getUseDays().getUseDays().v());
					tmp.setBeforeUseDays(tmp.getBeforeUseDays() + remainData.getSpecialLeave().getBeforeRemainGrant().getDays().v());
					tmp.setAfterUseDays(tmp.getAfterUseDays() + (remainData.getSpecialLeave().getAfterRemainGrant().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getDays().v() : 0));
					if (remainData.getSpecialLeave().getUseNumber().getUseTimes().isPresent()){
						val useTimes = remainData.getSpecialLeave().getUseNumber().getUseTimes().get();
						tmp.setUseTimes(tmp.getUseTimes() + useTimes.getUseTimes().v());
						tmp.setBeforeUseTimes(tmp.getBeforeUseTimes() + useTimes.getBeforeUseGrantTimes().v());
						tmp.setAfterUseTimes(tmp.getAfterUseTimes() + (useTimes.getAfterUseGrantTimes().isPresent() ? useTimes.getAfterUseGrantTimes().get().v() : 0 ));
						tmp.setUseNumber(tmp.getUseNumber() + useTimes.getUseNumber().v());
						tmp.setFactUseNumber(tmp.getFactUseNumber() + useTimes.getUseNumber().v());
					}
					else {
						tmp.setUseTimes(tmp.getUseTimes());
						tmp.setBeforeUseTimes(tmp.getBeforeUseTimes());
						tmp.setAfterUseTimes(tmp.getAfterUseTimes());
						tmp.setUseNumber(tmp.getUseNumber());
						tmp.setFactUseNumber(tmp.getFactUseNumber());
					}
					tmp.setFactUseDays(tmp.getFactUseDays() + remainData.getActualSpecial().getUseNumber().getUseDays().getUseDays().v());
					tmp.setRemainDays(tmp.getRemainDays() + remainData.getSpecialLeave().getRemain().getDays().v());
					tmp.setRemainTimes(tmp.getRemainTimes() + (remainData.getSpecialLeave().getRemain().getTime().isPresent() ? remainData.getSpecialLeave().getRemain().getTime().get().v() : 0));
					tmp.setFactRemainDays(tmp.getFactRemainDays() + remainData.getActualSpecial().getRemain().getDays().v());
					tmp.setFactRemainTimes(tmp.getAfterFactRemainTimes() + (remainData.getActualSpecial().getRemain().getTime().isPresent() ? remainData.getActualSpecial().getRemain().getTime().get().v() : 0));
					tmp.setBeforeRemainDays(tmp.getBeforeRemainDays() + remainData.getSpecialLeave().getBeforeRemainGrant().getDays().v());
					tmp.setBeforeRemainTimes(tmp.getBeforeRemainTimes() + (remainData.getSpecialLeave().getBeforeRemainGrant().getTime().isPresent() ? remainData.getSpecialLeave().getBeforeRemainGrant().getTime().get().v() : 0));
					tmp.setBeforeFactRemainDays(tmp.getBeforeFactRemainDays() + remainData.getActualSpecial().getBeforRemainGrant().getDays().v());
					tmp.setBeforeFactRemainTimes(tmp.getBeforeFactRemainTimes() + (remainData.getActualSpecial().getBeforRemainGrant().getTime().isPresent() ? remainData.getActualSpecial().getBeforRemainGrant().getTime().get().v() : 0));
					tmp.setAfterRemainDays(tmp.getAfterRemainDays() + (remainData.getSpecialLeave().getAfterRemainGrant().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getDays().v() : 0));
					tmp.setAfterRemainTimes(tmp.getAfterRemainTimes() + (remainData.getSpecialLeave().getAfterRemainGrant().isPresent() && remainData.getSpecialLeave().getAfterRemainGrant().get().getTime().isPresent() ? remainData.getSpecialLeave().getAfterRemainGrant().get().getTime().get().v() : 0));
					tmp.setAfterFactRemainDays(tmp.getAfterFactRemainDays() + (remainData.getActualSpecial().getAfterRemainGrant().isPresent() ? remainData.getActualSpecial().getAfterRemainGrant().get().getDays().v() : 0));
					tmp.setNotUseDays(tmp.getNotUseDays() + remainData.getSpecialLeave().getUnDegestionNumber().getDays().v());
					tmp.setNotUseTime(tmp.getNotUseTime() + (remainData.getSpecialLeave().getUnDegestionNumber().getTimes().isPresent() ? remainData.getSpecialLeave().getUnDegestionNumber().getTimes().get().v() : 0));
					tmp.setGrantAtr(tmp.getGrantAtr() == 1 ? 1 : (remainData.isGrantAtr() ? 1 : 0));
					tmp.setGrantDays(tmp.getGrantDays() + (remainData.getGrantDays().isPresent() ? remainData.getGrantDays().get().v() : 0));
					lstOutData.add(tmp);
				}
			}
		}
		return lstOutData;
	}

}
