package nts.uk.screen.at.app.query.kmp.kmp001.c;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocation;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampDakokuRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampRecord;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.GetNewestStampNotRegisteredService;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.StampInfoDisp;
import nts.uk.shr.com.context.AppContexts;

/**
 * UKDesign.UniversalK.就業.KDP_打刻.KMP001_IDカードの登録.C：IDカード未登録打刻指定.メニュー別OCD.初期表示を行う
 * 
 * @author chungnt
 *
 */
@Stateless
public class CardUnregistered {

	@Inject
	private StampRecordRepository recordRepo;

	@Inject
	private StampDakokuRepository dakokuRepo;

	@Inject
	private WorkLocationRepository workLocationRepo;

	public List<CardUnregisteredDto> getAll(DatePeriod period) {
		RetrieveNoStampCardRegisteredServiceRequireImpl require = new RetrieveNoStampCardRegisteredServiceRequireImpl(
				recordRepo, dakokuRepo);
		List<CardUnregisteredDto> dto = new ArrayList<>();
		String companyID = AppContexts.user().companyId();

		// 1: 取得する(@Require, 期間): List<表示する打刻情報>
		List<StampInfoDisp> stampInfoDisps = GetNewestStampNotRegisteredService.get(require, period);

		if (stampInfoDisps.isEmpty()) {
			return dto;
		}

		return stampInfoDisps.stream().map(m -> {
			CardUnregisteredDto cardUnregisteredDto = new CardUnregisteredDto();
			cardUnregisteredDto.setInfoLocation(this.getNameWork(companyID, m));
			cardUnregisteredDto.setStampNumber(m.getStampNumber().v());
			cardUnregisteredDto.setStampAtr(m.getStampAtr());
			cardUnregisteredDto.setStampDatetime(m.getStampDatetime());
			return cardUnregisteredDto;
		}).collect(Collectors.toList());

	}


	private String getNameWork(String companyID, StampInfoDisp disp) {
		List<Stamp> stamps = disp.getStamp();
		List<String> nameWorks = new ArrayList<>();
		
		if (!stamps.isEmpty()) {
			for (int i = 0; i < stamps.size(); i++) {
				if (!stamps.get(i).getRefActualResults().getWorkLocationCD().isPresent()) {
					continue;
				}
				Optional<WorkLocation> work = workLocationRepo.findByCode(companyID,
						stamps.get(i).getRefActualResults().getWorkLocationCD().get().v());
				if(work.isPresent()) {
					nameWorks.add(work.get().getWorkLocationName().v());
				}
			}
		}
		
		if(!nameWorks.isEmpty()) {
			for (int i = 0 ; i < nameWorks.size() ; i ++) {
				if (!nameWorks.get(i).equals("")) {
					return nameWorks.get(i);
				}
			}
		}

		return "";
	}

	@AllArgsConstructor
	private class RetrieveNoStampCardRegisteredServiceRequireImpl
			implements GetNewestStampNotRegisteredService.Require {

		@Inject
		private StampRecordRepository recordRepo;

		@Inject
		private StampDakokuRepository dakokuRepo;

		@Override
		public List<StampRecord> getStempRcNotResgistNumber(DatePeriod period) {
			return recordRepo.getStempRcNotResgistNumber(AppContexts.user().contractCode(), period);
		}

		@Override
		public List<Stamp> getStempRcNotResgistNumberStamp(String contractCode, DatePeriod period) {
			return dakokuRepo.getStempRcNotResgistNumberStamp(AppContexts.user().contractCode(), period);
		}
	}
}