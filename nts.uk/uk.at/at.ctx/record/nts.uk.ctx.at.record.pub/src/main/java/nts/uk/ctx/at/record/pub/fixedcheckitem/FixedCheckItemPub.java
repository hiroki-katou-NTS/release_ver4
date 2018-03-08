package nts.uk.ctx.at.record.pub.fixedcheckitem;

import java.util.List;

import nts.arc.time.GeneralDate;

public interface FixedCheckItemPub {
	//1.勤務種類未登録
	public ValueExtractAlarmWRPubExport  checkWorkTypeNotRegister(String workplaceID,String employeeID,GeneralDate date,String workTypeCD);
	//2.就業時間帯未登録
	public ValueExtractAlarmWRPubExport  checkWorkTimeNotRegister(String workplaceID,String employeeID,GeneralDate date,String workTimeCD);
	//3.本人未確認チェック
	public List<ValueExtractAlarmWRPubExport> checkPrincipalUnconfirm(String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
	//4.管理者未確認チェック
	public List<ValueExtractAlarmWRPubExport> checkAdminUnverified(String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
	//5.データのチェック
	public List<ValueExtractAlarmWRPubExport> checkingData(List<ValueExtractAlarmWRPubExport> listValue,String workplaceID,String employeeID,GeneralDate startDate,GeneralDate endDate);
}
