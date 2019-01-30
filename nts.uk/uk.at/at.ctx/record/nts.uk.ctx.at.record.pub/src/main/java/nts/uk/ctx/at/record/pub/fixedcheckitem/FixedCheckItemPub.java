package nts.uk.ctx.at.record.pub.fixedcheckitem;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface FixedCheckItemPub {
	//1.勤務種類未登録
	public Optional<ValueExtractAlarmWRPubExport>  checkWorkTypeNotRegister(String workplaceID,String employeeID,GeneralDate date,String workTypeCD);
	//2.就業時間帯未登録
	public Optional<ValueExtractAlarmWRPubExport>  checkWorkTimeNotRegister(String workplaceID,String employeeID,GeneralDate date,String workTimeCD);
	//3.本人未確認チェック
	public List<ValueExtractAlarmWRPubExport> checkPrincipalUnconfirm(String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
	//4.管理者未確認チェック
	public List<ValueExtractAlarmWRPubExport> checkAdminUnverified(String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
	//5.データのチェック
	public List<ValueExtractAlarmWRPubExport> checkingData(String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
	//4.管理者未確認チェック update
	public List<ValueExtractAlarmWRPubExport> checkAdminUnverified(String workplaceID,String employeeID,DatePeriod datePeriod);
	// 大塚用連続休暇チェック
	public List<ValueExtractAlarmWRPubExport> checkContinuousVacation(String employeeID,DatePeriod datePeriod);
}
