package nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.sysfixedcheckcondition.checkforagreement;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyRepository;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfMonthly;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.fixedcheckitem.checkprincipalunconfirm.ValueExtractAlarmWR;
import nts.uk.ctx.at.shared.dom.monthly.agreement.AgreementTimeStatusOfMonthly;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

@Stateless
public class CheckAgreementDefault implements CheckAgreementService {

	@Inject
	private AttendanceTimeOfMonthlyRepository attendanceTimeOfMonthlyRepo;
	
	@Override
	public Optional<ValueExtractAlarmWR> checkAgreement(String employeeID, int yearMonth,int closureId,ClosureDate closureDate) {
		//実績データに埋まっている「36協定エラー状態」を取得する : 207
		YearMonth ym = new YearMonth(yearMonth);
		Optional<AttendanceTimeOfMonthly> attdTimeOfMonthly = attendanceTimeOfMonthlyRepo.find(employeeID, ym,
				EnumAdaptor.valueOf(closureId, ClosureId.class),
				closureDate);
		if(!attdTimeOfMonthly.isPresent())
			return Optional.empty();
		//パラメータ．「36協定エラー状態」をチェックする
		AgreementTimeOfMonthly agreementTimeOfMonthly = attdTimeOfMonthly.get().getMonthlyCalculation().getAgreementTime();
		AgreementTimeStatusOfMonthly status = agreementTimeOfMonthly.getStatus();
		
		GeneralDate date = GeneralDate.ymd(ym.year(), ym.month(), 1);
		
		if(status == AgreementTimeStatusOfMonthly.NORMAL ||status == AgreementTimeStatusOfMonthly.NORMAL_SPECIAL ) {
			return Optional.empty();
		}else if(status == AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR || status == AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ERROR_SP ||
				status == AgreementTimeStatusOfMonthly.EXCESS_LIMIT_ALARM) {
			return Optional.of(new ValueExtractAlarmWR(null,
					employeeID,
					date,
					TextResource.localize("KAL010_100"),
					TextResource.localize("KAL010_273"),	
					TextResource.localize("KAL010_274"),
					null,null));
		}
		
		return Optional.of(new ValueExtractAlarmWR(null,
				employeeID,
				date,
				TextResource.localize("KAL010_100"),
				TextResource.localize("KAL010_273"),	
				TextResource.localize("KAL010_275"),
				null,null));
	}

}
