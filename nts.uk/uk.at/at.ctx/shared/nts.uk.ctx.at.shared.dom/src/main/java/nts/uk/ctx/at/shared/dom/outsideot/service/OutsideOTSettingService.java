package nts.uk.ctx.at.shared.dom.outsideot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSetting;
import nts.uk.ctx.at.shared.dom.outsideot.OutsideOTSettingRepository;
import nts.uk.ctx.at.shared.dom.outsideot.breakdown.OutsideOTBRDItem;

@Stateless
public class OutsideOTSettingService {

	@Inject
	private OutsideOTSettingRepository outsideOTSettingRepository;

	/**
	 * 36協定対象項目一覧を取得
	 * 
	 * @param companyId
	 * @return 36協定時間対象項目
	 */
	public Time36AgreementTargetItem getTime36AgreementTargetItem(String companyId) {
		List<Integer> overtimeFrNo = new ArrayList<>();
		List<Integer> breakFrNo = new ArrayList<>();
		List<MonthlyItems> overTimeItems = MonthlyItems.findOverTime();
		List<MonthlyItems> breakTimeItems = MonthlyItems.findBreakTime();
		boolean targetFlex = false;

		// ドメインモデル「時間外超過設定」を取得する
		Optional<OutsideOTSetting> outsideOTSettingOtp = outsideOTSettingRepository.findById(companyId);
		if (!outsideOTSettingOtp.isPresent()) {
			return new Time36AgreementTargetItem(overtimeFrNo, breakFrNo, targetFlex);
		}
		// 内訳項目に設定されている勤怠項目IDを全て取得
		List<Integer> itemIdList = this.getAttendanceItemIdList(outsideOTSettingOtp.get());
		// 取得した勤怠項目IDから36協定時間対象と判断
		for (Integer itemId : itemIdList) {
			Optional<MonthlyItems> itemOverTimeOtp = overTimeItems
					.stream().filter(x -> x.itemId == itemId)
					.findFirst();
			if (itemOverTimeOtp.isPresent()) {
				overtimeFrNo.add(itemOverTimeOtp.get().frameNo);
			}
			Optional<MonthlyItems> itemBreakTimeOtp = breakTimeItems
					.stream().filter(x -> x.itemId == itemId)
					.findFirst();
			if (itemBreakTimeOtp.isPresent()) {
				breakFrNo.add(itemBreakTimeOtp.get().frameNo);
			}
		}
		Optional<Integer> flexExessOtp = itemIdList.stream()
			.filter(x -> {
				boolean result = (x == MonthlyItems.FLEX_EXCESS_TIME.itemId);
				result = result || (x == MonthlyItems.LEGAL_FLEX_TIME.itemId);
				result = result || (x == MonthlyItems.ILLEGAL_FLEX_TIME.itemId);
				return result;
			})
			.findFirst();
		if(flexExessOtp.isPresent()){
			targetFlex = true;
		}
		// 36協定時間対象項目を返す		
		return new Time36AgreementTargetItem(overtimeFrNo, breakFrNo, targetFlex);
	}

	/**
	 * 内訳項目に設定されている勤怠項目IDを全て取得
	 */
	private List<Integer> getAttendanceItemIdList(OutsideOTSetting outsideOTSetting) {
		List<Integer> itemIdList = new ArrayList<>();
		// ○属性「内訳項目一覧」を取得
		for (OutsideOTBRDItem item : outsideOTSetting.getBreakdownItems()) {
			// ○属性「集計項目一覧」を取得
			itemIdList.addAll(item.getAttendanceItemIds());
		}
		return itemIdList;
	}

}
