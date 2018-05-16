package nts.uk.ctx.at.record.dom.remainingnumber.paymana;

import java.util.List;
import java.util.Optional;

public interface SubstitutionOfHDManaDataRepository {
	
	// ドメインモデル「振休管理データ」を取得
	List<SubstitutionOfHDManagementData> getBysiDRemCod(String cid, String sid);
	
	// ドメインモデル「振休管理データ」を作成する
	void create(SubstitutionOfHDManagementData domain);
	
	List<SubstitutionOfHDManagementData> getBysiD(String cid, String sid);
	
	void delete(String sid);
	
	void update(SubstitutionOfHDManagementData domain);
	
	Optional<SubstitutionOfHDManagementData> findByID(String Id);
}
