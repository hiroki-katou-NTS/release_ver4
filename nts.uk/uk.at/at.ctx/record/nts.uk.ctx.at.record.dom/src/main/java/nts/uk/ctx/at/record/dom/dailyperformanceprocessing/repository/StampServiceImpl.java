package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.StampReflectRangeOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.TimeZoneOutput;
import nts.uk.ctx.at.record.dom.stamp.StampItem;
import nts.uk.ctx.at.record.dom.stamp.StampRepository;
import nts.uk.ctx.at.record.dom.stamp.card.StampCardItem;
import nts.uk.ctx.at.record.dom.stamp.card.StampCardRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageResource;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class StampServiceImpl implements StampDomainService {
	@Inject
	private ErrMessageInfoRepository errRepo;
	@Inject
	private StampRepository stampRepo;
	@Inject
	private StampCardRepository stampCardRepo;

	public List<StampItem> handleData(StampReflectRangeOutput s, ExecutionType reCreateAttr,
			String empCalAndSumExecLogID, GeneralDate date, String employeeId, String companyId) {
		if (s == null) {
			ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeId, empCalAndSumExecLogID,
					new ErrMessageResource("009"), EnumAdaptor.valueOf(0, ExecutionContent.class), date,
					new ErrMessageContent(TextResource.localize("Msg_466")));
			errRepo.add(employmentErrMes);
			return null;
		}
		List<StampCardItem> lstStampCardItem = this.stampCardRepo.findByEmployeeID(employeeId);
		ArrayList<String> stampNumber = new ArrayList<String>();
		if (lstStampCardItem != null) {
			int stampCardSize = lstStampCardItem.size();
			if (stampCardSize > 0) {
				if (stampCardSize > 10) {
					stampCardSize = 10;
				}
				for (int i = 0; i < stampCardSize; i++) {
					stampNumber.add(lstStampCardItem.get(i).getCardNumber().v());
				}

			}
		}

		// if (stampNumber == null || stampNumber.isEmpty()) {
		// ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeId,
		// empCalAndSumExecLogID,
		// new ErrMessageResource("008"), EnumAdaptor.valueOf(0,
		// ExecutionContent.class), date,
		// new ErrMessageContent(TextResource.localize("Msg_433")));
		// errRepo.add(employmentErrMes);
		// return null;
		// }

		List<StampItem> lstStampItemOutput = new ArrayList<StampItem>();

		if (stampNumber != null && !stampNumber.isEmpty()) {

			List<StampItem> lstStampItem = this.stampRepo.findByListCardNo(stampNumber);

			if (reCreateAttr.value == 0) {
				lstStampItem.forEach(x -> {
					int attendanceClock = x.getAttendanceTime().v();
					TimeZoneOutput stampRange = s.getStampRange();

					if (DateUtils.isSameDay(x.getDate().date(), date.date())
							&& attendanceClock >= stampRange.getStart().v().intValue()
							&& attendanceClock <= stampRange.getEnd().v().intValue()
							&& x.getReflectedAtr().value == 0) {
						lstStampItemOutput.add(x);
					}

				});
				lstStampItemOutput.sort(Comparator.comparing(StampItem::getDate));
				lstStampItemOutput.sort(Comparator.comparing(StampItem::getAttendanceTime));

				return lstStampItemOutput;
			}

			lstStampItem.forEach(x -> {
				int attendanceClock = x.getAttendanceTime().v().intValue();
				TimeZoneOutput stampRange = s.getStampRange();
				if (DateUtils.isSameDay(x.getDate().date(), date.date())
						&& attendanceClock >= stampRange.getStart().v().intValue()
						&& attendanceClock <= stampRange.getEnd().v().intValue()) {
					lstStampItemOutput.add(x);
				}
			});
			lstStampItemOutput.sort(Comparator.comparing(StampItem::getDate));
			lstStampItemOutput.sort(Comparator.comparing(StampItem::getAttendanceTime));
		}
		return lstStampItemOutput;
	}

}
