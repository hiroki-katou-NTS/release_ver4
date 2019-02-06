package nts.uk.ctx.at.record.dom.workrecord.erroralarm;
/**
 * 日次の抽出するデータの条件
 * @author tutk
 *
 */
public enum WorkRecordFixedCheckItem {
	
	WORK_TYPE_NOT_REGISTERED(1,"勤務種類未登録"),
	
	WORKING_HOURS_NOT_REGISTERED(2,"就業時間帯未登録"),
	
	UNIDENTIFIED_PERSON(3,"本人未確認"),
	
	DATA_CHECK(4,"管理者未確認"),
	
	ADMINISTRATOR_NOT_CONFIRMED(5,"データのチェック"),
	
	CONTINUOUS_VATATION_CHECK(6,"連続休暇チェック");
	
	public int value;
	
	public String nameId;
	
	private WorkRecordFixedCheckItem (int value,String nameId) {
		this.value = value;
		this.nameId = nameId;
	}
}
