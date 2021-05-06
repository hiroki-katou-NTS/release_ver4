package nts.uk.screen.at.app.query.kdp.kdps01.b;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.record.dom.adapter.employee.EmployeeRecordAdapter;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCard;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampCardRepository;
import nts.uk.ctx.at.record.dom.stamp.card.stampcard.StampNumber;
import nts.uk.ctx.at.record.dom.stampmanagement.workplace.WorkLocationRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampDakokuRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampRecord;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.StampRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.EmployeeStampInfo;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.GetListStampEmployeeService;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.domainservice.StampInfoDisp;
import nts.uk.screen.at.app.query.kdp.kdp001.a.EmployeeStampInfoDto;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author sonnlb
 *         UKDesign.UniversalK.就業.KDP_打刻.KDPS01_打刻入力(スマホ).B:打刻結果確認.メニュー別OCD.打刻結果(スマホ)の確認画面を表示する
 */
@Stateless
public class DisplayConfirmStampResultScreenB {

	@Inject
	private StampCardRepository stampCardRepo;

	@Inject
	private StampRecordRepository stampRecordRepo;

	@Inject
	private StampDakokuRepository stampDakokuRepo;

	@Inject
	private WorkLocationRepository workLocationRepo;
	
	@Inject
	private EmployeeRecordAdapter sysEmpPub;

	/**
	 * 打刻結果(スマホ)の打刻情報を取得する
	 * 
	 * @param date
	 * @return
	 * 
	 * @return ドメインモデル：社員の打刻情報
	 * 
	 *         勤務場所名
	 */
	public DisplayConfirmStampResultScreenBDto getStampInfoResult(DatePeriod period) {
		// 取得する(@Require, 社員ID, 年月日)
		List<EmployeeStampInfo> empDatas = new ArrayList<>();
		EmpStampDataRequiredImpl empStampDataR = new EmpStampDataRequiredImpl(stampCardRepo, stampRecordRepo,
				stampDakokuRepo);
		List<GeneralDate> betweens = period.datesBetween();
		betweens.sort((d1, d2) -> d2.compareTo(d1));
		for (GeneralDate date : betweens) {
			
			Optional<EmployeeStampInfo> employeeStampData = GetListStampEmployeeService.get(empStampDataR,
					AppContexts.user().employeeId(), date);
			if (employeeStampData.isPresent()) {
				empDatas.add(employeeStampData.get());
			}
		}
		
		String workLocationCd = empDatas.stream()
				.map(x -> x.getListStampInfoDisp())
				.flatMap(Collection::stream)
				.sorted(Comparator.comparing(StampInfoDisp::getStampDatetime))
				.findFirst()
				.map(x -> x.getStamp()
						.stream()
						.findFirst()
						.map(stamp -> stamp.getRefActualResults().getWorkInforStamp()
							.map(wInfo -> wInfo.getWorkLocationCD()
									.map(workLoc -> workLoc != null ? workLoc.v() : null)
									.orElse(null))
							.orElse(null))
						.orElse(null))
				.orElse(null);
		
		String workLocationName = this.workLocationRepo.findByCode(AppContexts.user().companyId(), workLocationCd)
				.map(x -> x.getWorkLocationName() != null ? x.getWorkLocationName().v() : null).orElse("");
		
		//thêm 1 xử lý dể lấy username nữa;
		
		return new DisplayConfirmStampResultScreenBDto(
				empDatas.stream().map(x -> EmployeeStampInfoDto.fromDomain(x)).collect(Collectors.toList()),
				workLocationCd, workLocationName, this.sysEmpPub.getPersonInfor(AppContexts.user().employeeId()));
	}

	@AllArgsConstructor
	private class EmpStampDataRequiredImpl implements GetListStampEmployeeService.Require {

		@Inject
		protected StampCardRepository stampCardRepo;

		@Inject
		protected StampRecordRepository stampRecordRepo;

		@Inject
		protected StampDakokuRepository stampDakokuRepo;

		@Override
		public List<StampCard> getListStampCard(String sid) {
			return stampCardRepo.getListStampCard(sid);
		}

		@Override
		public List<StampRecord> getStampRecord(List<StampNumber> stampNumbers, GeneralDate date) {
			return stampRecordRepo.get(AppContexts.user().contractCode(), stampNumbers, date);
		}

		@Override
		public List<Stamp> getStamp(List<StampNumber> stampNumbers, GeneralDate date) {
			return stampDakokuRepo.get(AppContexts.user().contractCode(), stampNumbers, date);
		}

	}

}
