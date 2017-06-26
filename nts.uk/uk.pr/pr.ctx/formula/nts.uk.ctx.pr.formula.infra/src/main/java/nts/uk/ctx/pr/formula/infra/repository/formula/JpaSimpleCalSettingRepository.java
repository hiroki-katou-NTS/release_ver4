package nts.uk.ctx.pr.formula.infra.repository.formula;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.formula.dom.formula.SimpleCalSetting;
import nts.uk.ctx.pr.formula.dom.repository.SimpleCalSettingRepository;
import nts.uk.ctx.pr.formula.infra.entity.formula.QcfstSimpleCalSetting;

@Stateless
public class JpaSimpleCalSettingRepository extends JpaRepository implements SimpleCalSettingRepository {
	private static final String FIND_ALL;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM QcfstSimpleCalSetting a ");
		FIND_ALL = builderString.toString();
	}

	@Override
	public List<SimpleCalSetting> findAll() {
		return this.queryProxy().query(FIND_ALL, QcfstSimpleCalSetting.class).getList(f -> toDomain(f));
	}

	private SimpleCalSetting toDomain(QcfstSimpleCalSetting qcfstSimpleCalSetting) {
		SimpleCalSetting simpleCalSetting = SimpleCalSetting.createFromJavaType(
				qcfstSimpleCalSetting.qcfstSimpleCalSettingPK.itemCode, qcfstSimpleCalSetting.itemName);
		return simpleCalSetting;
	}
}
