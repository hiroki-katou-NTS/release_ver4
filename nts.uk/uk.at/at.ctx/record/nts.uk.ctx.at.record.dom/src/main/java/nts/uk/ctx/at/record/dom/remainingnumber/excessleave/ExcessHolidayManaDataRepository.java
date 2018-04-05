package nts.uk.ctx.at.record.dom.remainingnumber.excessleave;

import java.util.List;

public interface ExcessHolidayManaDataRepository {
	
	// ドメインモデル「超過有休管理データ」を取得する
	// 社員ID=パラメータ「社員ID」
	// 期限切れ状態=使用可能

	List<ExcessHolidayManagementData> getBySidNotExp(String cid, String sid);
}
