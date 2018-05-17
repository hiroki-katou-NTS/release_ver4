package nts.uk.ctx.at.record.app.find.remainingnumber.paymana;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.remainingnumber.base.DigestionAtr;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.PayoutManagementData;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.PayoutManagementDataRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.PayoutSubofHDManaRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.PayoutSubofHDManagement;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class PayoutManagementDataFinder {
	
	@Inject
	private PayoutManagementDataRepository payoutManagementDataRepository;

	@Inject
	private PayoutSubofHDManaRepository payoutSubofHDManaRepository;
	/**
	 * ドメイン「振休管理データ」より紐付け対象となるデータを取得する
	 * @param sid
	 * @return List<PayoutManagementDataDto>
	 */
	public List<PayoutManagementDataDto> getBySidDatePeriod(String sid, String subOfHDID){
		List<PayoutManagementData> listPayout = payoutManagementDataRepository.getBySidDatePeriod(sid,subOfHDID,DigestionAtr.UNUSED.value);
		
		List<String> listPayoutSub = payoutSubofHDManaRepository.getBySubId(subOfHDID).stream()
				.map(i -> i.getPayoutId()).collect(Collectors.toList());
		
		return listPayout.stream().map(i->{
			PayoutManagementDataDto payout = PayoutManagementDataDto.createFromDomain(i);
			if (listPayoutSub.contains(i.getPayoutId())){
				payout.setLinked(true);
			}
			return payout;
			
		}).collect(Collectors.toList());
	}
	
	public List<PayoutManagementDataDto> getBysiDRemCod(String empId, int state) {
		String cid = AppContexts.user().companyId();

		return payoutManagementDataRepository.getSidWithCod(cid, empId, state).stream().map(item -> PayoutManagementDataDto.createFromDomain(item))
				.collect(Collectors.toList());
	}
	
	public List<PayoutManagementDataDto> getBySidDatePeriodDif(String empId, GeneralDate startDate, GeneralDate endDate, int state) {

		return payoutManagementDataRepository.getBySidDatePeriodDif(empId, startDate, endDate, state).stream().map(item -> PayoutManagementDataDto.createFromDomain(item))
				.collect(Collectors.toList());
	}
	
	public List<PayoutManagementDataDto> getBySidDatePeriodNoDigestion(String empId, GeneralDate startDate, GeneralDate endDate) {

		return payoutManagementDataRepository.getBySidDatePeriodNoDigestion(empId, startDate, endDate).stream().map(item -> PayoutManagementDataDto.createFromDomain(item))
				.collect(Collectors.toList());
	}
}
