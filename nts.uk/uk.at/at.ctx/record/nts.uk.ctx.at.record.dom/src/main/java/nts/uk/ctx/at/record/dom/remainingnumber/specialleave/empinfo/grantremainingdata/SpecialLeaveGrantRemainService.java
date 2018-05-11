package nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.grantremainingdata;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.grantremainingdata.remainingnumber.TimeOfRemain;

@Stateless
public class SpecialLeaveGrantRemainService {
	@Inject
	private SpecialLeaveGrantRepository specialLeaveGrantRepo;

	public String calDayTime(String sid, int specialCD) {
		List<SpecialLeaveGrantRemainingData> grantRemains = specialLeaveGrantRepo.getAllByExpStatus(sid, specialCD,
				LeaveExpirationStatus.AVAILABLE.value);

		Double result = grantRemains.stream()
				.mapToDouble(item -> item.getDetails().getRemainingNumber().getDayNumberOfRemain().v()).sum();
		List<SpecialLeaveGrantRemainingData> grantRemainsTemp = grantRemains.stream()
				.filter(i -> i.getDetails().getRemainingNumber().timeOfRemain.isPresent()).collect(Collectors.toList());

		Integer minute = grantRemainsTemp.stream().mapToInt(i -> {
			TimeOfRemain timeOfRemain =  i.getDetails().getRemainingNumber().getTimeOfRemain().get();
			return (timeOfRemain.valueAsMinutes());
		}).sum();

		
		Integer hours = minute / 60 ;
		minute = minute % 60;

		return result.toString() + "日と　" + (minute > 0 ? hours : "-"+hours) + ":" + (Math.abs(minute) < 10 ? ("0" + Math.abs(minute)) : (Math.abs(minute) + ""));
	}

}
