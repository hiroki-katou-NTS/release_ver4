package nts.uk.ctx.at.record.dom.remainingnumber.paymana;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.remainingnumber.base.DigestionAtr;
import nts.uk.ctx.at.record.dom.remainingnumber.base.HolidayAtr;
import nts.uk.ctx.at.record.dom.remainingnumber.base.TargetSelectionAtr;
import nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.AddSubHdManagementService;
import nts.uk.ctx.at.record.dom.remainingnumber.subhdmana.ItemDays;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class PayoutManagementDataService {
	
	@Inject
	private ClosureService closureService;

	@Inject
	private PayoutManagementDataRepository payoutManagementDataRepository;

	@Inject
	private SubstitutionOfHDManaDataRepository substitutionOfHDManaDataRepository;

	@Inject
	private PayoutSubofHDManaRepository payoutSubofHDManaRepository;

	@Inject
	private AddSubHdManagementService addSubHdManagementService;
	
	private static final int EMPTY = 0;
	
	private List<String> checkHolidate(Boolean pickUp, Boolean pause,Boolean checkedSplit, Double subDays, Double requiredDays, Double occurredDays){
		List<String> errors = new ArrayList<String>();
		if (pause) {
			if (checkedSplit) {
				if (ItemDays.HALF_DAY.value.equals(subDays)) {
					if (!ItemDays.HALF_DAY.value.equals(requiredDays)){
						errors.add("Msg_1256_SubDays");
						return errors;
					} else {
					errors.add("Msg_1256_RequiredDays");
					return errors;
					}
				}
			}
		}
		if (pickUp) {
			if (checkedSplit) {
				if (!ItemDays.HALF_DAY.value.equals(occurredDays)) {
					errors.add("Msg_1256_OccurredDays");
					return errors;
				}
			} else {
				if (!ItemDays.HALF_DAY.value.equals(occurredDays)) {
					errors.add("Msg_1257_OccurredDays");
					return errors;
				}	
			}
		}
		
		return errors;
}
	
	private boolean checkInfoPayMana(PayoutManagementData domain) {
		Optional<PayoutManagementData> payout = payoutManagementDataRepository.find(domain.getCID(), domain.getSID(),
				domain.getPayoutDate());
		if (payout.isPresent()) {
			return true;
		}
		return false;
	}

	public boolean checkInfoSubPayMana(SubstitutionOfHDManagementData subDomain) {
		Optional<SubstitutionOfHDManagementData> subPayout = substitutionOfHDManaDataRepository.find(subDomain.getCid(),
				subDomain.getSID(), subDomain.getHolidayDate());
		if (subPayout.isPresent()) {
			return true;
		}
		return false;
	}
	
	public List<String> addPayoutManagement(Boolean pickUp, Boolean pause,Boolean checkedSplit, PayoutManagementData payMana,SubstitutionOfHDManagementData subMana,
				SubstitutionOfHDManagementData splitMana, Double occurredDays, Double subDays, Double remainDays, Double requiredDays,  int closureId) {
		List<String> errors = new ArrayList<String>();
		YearMonth processYearMonth = GeneralDate.today().yearMonth();
		Optional<GeneralDate> closureDate = this.getClosureDate(closureId, processYearMonth);
		if (pickUp) {
			if (this.checkDateClosing(payMana.getDayoffDate(), closureDate, closureId))
				errors.add("Msg_740");
			if (this.checkInfoPayMana(payMana)) {
				errors.add("Msg_737_PayMana");
			}
			if (pause) {
				if (this.checkDateClosing(subMana.getDayoffDate(), closureDate, closureId))
					errors.add("Msg_744");
				if (payMana.getDayoffDate().equals(subMana.getDayoffDate())) {
					errors.add("Msg_729_SubPay");
				}
				if (this.checkInfoSubPayMana(subMana)) {
					errors.add("Msg_737_SubPay");
				}
			}
		} else {
			if (pause) {
				if (this.checkDateClosing(subMana.getDayoffDate(), closureDate, closureId))
					errors.add("Msg_744");
				if (payMana.getDayoffDate().equals(subMana.getDayoffDate())) {
					errors.add("Msg_729_SubPay");
				}
				if (this.checkInfoSubPayMana(subMana)) {
					errors.add("Msg_737_SubPay");
				}
			}
			else {
				errors.add("Msg_725_SubPay");
			}
		}
		errors.addAll(checkHolidate(pickUp, pause, checkedSplit, subDays, requiredDays, occurredDays ));
		if (errors.isEmpty()) {
			if (pickUp) {
				payoutManagementDataRepository.create(payMana);
			}
			if (pause) {
				substitutionOfHDManaDataRepository.create(subMana);
			}
			if (checkedSplit) {
				substitutionOfHDManaDataRepository.create(splitMana);
			}
			if (pause && pickUp) {
				Double usedDay = subDays + subDays;
				if (checkedSplit) {
					usedDay = subDays;
					PayoutSubofHDManagement paySplit = new PayoutSubofHDManagement(payMana.getPayoutId(), splitMana.getSubOfHDID(), BigDecimal.valueOf(usedDay), TargetSelectionAtr.MANUAL.value);
					payoutSubofHDManaRepository.add(paySplit);
				} else {
					PayoutSubofHDManagement paySub = new PayoutSubofHDManagement(payMana.getPayoutId(), subMana.getSubOfHDID(), BigDecimal.valueOf(usedDay), TargetSelectionAtr.MANUAL.value);
					payoutSubofHDManaRepository.add(paySub);
				}
			}
		}
		
		return errors;
	}
	
	public Optional<GeneralDate> getClosureDate(int closureId, YearMonth processYearMonth) {
		DatePeriod closurePeriod = closureService.getClosurePeriod(closureId, processYearMonth);
		if (Objects.isNull(closurePeriod)) {
			return Optional.empty();
		}
		return Optional.of(closurePeriod.start());
	}
	
	public boolean checkDateClosing(GeneralDate date, Optional<GeneralDate> closureDate, int closureId) {
		YearMonth processYearMonth = GeneralDate.today().yearMonth();
		if (!closureDate.isPresent()) {
			closureDate = this.getClosureDate(closureId, processYearMonth);
		}
		if (date.after(closureDate.get())) {
			return true;
		}
		return false;
	}

	/**
	 * KDM001 screen G
	 */
	private List<String> checkClosureDate(int closureId, GeneralDate dayoffDate) {
		List<String> errorList = new ArrayList<>();
		YearMonth processYearMonth = GeneralDate.today().yearMonth();
		Optional<GeneralDate> closureDate = addSubHdManagementService.getClosureDate(closureId, processYearMonth);
		if (closureDate.isPresent()) {
			if (dayoffDate.compareTo(closureDate.get()) >= 0) {
				errorList.add("Msg_740");
			}
		}
		return errorList;
	}

	private List<String> checkBox(boolean checkBox, int lawAtr, GeneralDate dayoffDate, GeneralDate expiredDate,
			double unUsedDays) {
		List<String> errorList = new ArrayList<>();
		if (checkBox) {
			if (lawAtr == HolidayAtr.PUBLICHOLIDAY.value) {
				errorList.add("Msg_1212");
				return errorList;
			} else if (dayoffDate.compareTo(expiredDate) > 0) {
				errorList.add("Msg_825");
			}
			return errorList;
		} else {
			if (unUsedDays == EMPTY) {
				errorList.add("Msg_1213");
			}
			return errorList;
		}
	}

	public List<String> update(PayoutManagementData data, int closureId, boolean checkBox) {
		List<String> errorList = new ArrayList<>();
		List<String> errorListClosureDate = checkClosureDate(closureId, data.getPayoutDate().getDayoffDate().get());
		if (!errorListClosureDate.isEmpty()) {
			errorList.addAll(errorListClosureDate);
			return errorList;
		} else {
			List<String> errorListCheckBox = checkBox(checkBox, data.getLawAtr().value,
					data.getPayoutDate().getDayoffDate().get(), data.getExpiredDate(), data.getUnUsedDays().v());
			if (!errorListCheckBox.isEmpty()) {
				errorList.addAll(errorListCheckBox);
				return errorList;
			} else {
				payoutManagementDataRepository.update(data);
				return errorList;
			}
		}
	}

	/**
	 * Ｆ．振休管理データの紐付設定（振出選択）登録処理
	 */
	public void insertPayoutSubofHD(String sid, String subId, Double remainNumber, List<SubOfHDManagement> subOfHDId) {
		List<PayoutSubofHDManagement>  listPayoutSub = payoutSubofHDManaRepository.getBySubId(subId);
		if (!listPayoutSub.isEmpty()) {
			payoutSubofHDManaRepository.deleteBySubID(subId);
		}
		
		// Set all item to free
		listPayoutSub.forEach(item->{
			// Update remain days 振出管理データ
			Optional<PayoutManagementData> payoutMan = payoutManagementDataRepository.findByID(item.getPayoutId());
			if (payoutMan.isPresent()) {
				payoutMan.get().setRemainNumber(Double.valueOf(item.getUsedDays().v().intValue()));
				payoutMan.get().setStateAtr(DigestionAtr.UNUSED.value);
				payoutManagementDataRepository.update(payoutMan.get());
			}
		});
		subOfHDId.forEach(i -> {
			payoutSubofHDManaRepository.add(new PayoutSubofHDManagement(i.getPayoutId(), subId,
					new BigDecimal(i.getOccurredDays()), TargetSelectionAtr.MANUAL.value));
			// Update remain days 振出管理データ
			Optional<PayoutManagementData> payoutMan = payoutManagementDataRepository.findByID(i.getPayoutId());
			if (payoutMan.isPresent()) {
				payoutMan.get().setRemainNumber(0d);
				payoutMan.get().setStateAtr(DigestionAtr.USED.value);
				payoutManagementDataRepository.update(payoutMan.get());
			}
		});
		// Update 振休管理データ
		Optional<SubstitutionOfHDManagementData> subofHD = substitutionOfHDManaDataRepository.findByID(subId);
		if (subofHD.isPresent()) {
			subofHD.get().setRemainsDay(remainNumber);
			substitutionOfHDManaDataRepository.update(subofHD.get());
		}

	}
}
