package nts.uk.ctx.at.record.dom.remainingnumber.paymana;

import java.util.List;
import java.util.Optional;

public interface PayoutManagementDataRepository {
	
	// ドメインモデル「振出管理データ」を取得
	List<PayoutManagementData> getSidWithCod(String cid, String sid, int state);
	
	// ドメインモデル「振出管理データ」を作成する
	void create(PayoutManagementData domain);
	
	List<PayoutManagementData> getSid(String cid, String sid);
	
	void delete(String sid);
	
	void update(PayoutManagementData domain);
	
	Optional<PayoutManagementData> findByID(String ID);
}
