package nts.uk.screen.at.app.dailyperformance.correction.lock;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.uk.screen.at.app.dailyperformance.correction.dto.DPDataDto;
import nts.uk.screen.at.app.dailyperformance.correction.text.DPText;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CheckLockDataDaily {

	public boolean checkLockInPeriod(List<DPDataDto> lstData, DatePeriod periodCheck) {
		if(periodCheck == null) return false;
		List<String> lstSateLock = lstData.stream().filter(
				x -> x.getDate().afterOrEquals(periodCheck.start()) && x.getDate().beforeOrEquals(periodCheck.end()))
				.map(x -> x.getState()).collect(Collectors.toList());
		for(String state : lstSateLock) {
			String[] lstSateChild = state.split("|");
			if(lstSateChild.length == 0) return false;
			if(!lockExceptAppCon(Arrays.asList(lstSateChild))) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean lockExceptAppCon(List<String> lstSateChild) {
		for(String lock : lstSateChild) {
			if(DPText.LIST_LOCK_EDIT_FLEX.contains(lock)) {
				return true;
			}
		}
		return false;
	}
}
