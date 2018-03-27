package nts.uk.ctx.at.record.pubimp.dailyperform.businesstype;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployee;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistory;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeEmpOfHistoryRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeOfEmployeeRepository;
import nts.uk.ctx.at.record.pub.dailyperform.businesstype.BusTypeOfEmpHimPub;
import nts.uk.ctx.at.record.pub.dailyperform.businesstype.BusTypeOfEmpHisDto;
import nts.uk.shr.com.history.DateHistoryItem;

public class BusTypeOfEmpHisPubIml implements BusTypeOfEmpHimPub {

	@Inject
	private BusinessTypeOfEmployeeRepository busTypeOfEmpRepo;

	@Inject
	private BusinessTypeEmpOfHistoryRepository busTypeEmpOfHisRepo;

	@Override
	public BusTypeOfEmpHisDto getData(String sId, GeneralDate baseDate) {
		BusTypeOfEmpHisDto bDto = new BusTypeOfEmpHisDto();
		List<BusinessTypeOfEmployee> bOfEmployees = new ArrayList<>();
		/* 対応するドメインモデル「社員の勤務種別の履歴」を取得する */
		Optional<BusinessTypeOfEmployeeHistory> optional = busTypeEmpOfHisRepo.findByBaseDate(baseDate, sId);
		if (!optional.isPresent()) {
			return null;
		}
		BusinessTypeOfEmployeeHistory bHistory = optional.get();
		/* 対応するドメインモデル「社員の勤務種別」を取得する */
		List<DateHistoryItem> dateHistoryItems = bHistory.getHistory();
		dateHistoryItems.stream().forEach(dateHistoryItem -> {
			Optional<BusinessTypeOfEmployee> optionalType = busTypeOfEmpRepo
					.findByHistoryId(dateHistoryItem.identifier());
			if (optionalType.isPresent()) {
				bOfEmployees.add(optionalType.get());
			}
		});
		bDto.setBuHistory(bHistory);
		bDto.setLsBusinessTypeOfEmpl(bOfEmployees);
		return bDto;
	}

}
