package nts.uk.ctx.at.shared.infra.repository.flex;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.flex.FlexWorkMntSetRepository;
import nts.uk.ctx.at.shared.dom.flex.FlexWorkSet;
import nts.uk.ctx.at.shared.infra.entity.flex.KshstFlexWorkSetting;
import nts.uk.ctx.at.shared.infra.entity.flex.KshstFlexWorkSettingPK;

/**
 * The Class JpaFlexWorkMntSetting.
 * @author HoangNDH
 */
@Stateless
public class JpaFlexWorkMntSetting extends JpaRepository implements FlexWorkMntSetRepository {

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.flex.FlexWorkMntSetRepository#find(java.lang.String)
	 */
	@Override
	public Optional<FlexWorkSet> find(String companyId) {
		return this.queryProxy()
				.find(new KshstFlexWorkSettingPK(companyId), KshstFlexWorkSetting.class)
				.map(x -> convertToDomain(x));
	}
	
	/**
	 * Convert to domain.
	 *
	 * @param setting the setting
	 * @return the flex work set
	 */
	public FlexWorkSet convertToDomain(KshstFlexWorkSetting setting) {
		return FlexWorkSet.createFromJavaType(setting.getId().getCid(), setting.getManageFlexWork());
	}
	
	/**
	 * Convert to db type.
	 *
	 * @param setting the setting
	 * @return the kshst flex work setting
	 */
	public KshstFlexWorkSetting convertToDbType(FlexWorkSet setting) {
		KshstFlexWorkSetting entity = new KshstFlexWorkSetting();
		KshstFlexWorkSettingPK primaryKey = new KshstFlexWorkSettingPK();
		primaryKey.setCid(setting.getCompanyId().v());
		entity.setId(primaryKey);
		entity.setManageFlexWork(setting.getUseFlexWorkSetting().value);
		return entity;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.flex.FlexWorkMntSetRepository#add(nts.uk.ctx.at.shared.dom.flex.FlexWorkSet)
	 */
	@Override
	public void add(FlexWorkSet flexWorkSetting) {
		KshstFlexWorkSetting setting = convertToDbType(flexWorkSetting);
		this.commandProxy().insert(setting);
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.flex.FlexWorkMntSetRepository#update(nts.uk.ctx.at.shared.dom.flex.FlexWorkSet)
	 */
	@Override
	public void update(FlexWorkSet flexWorkSetting) {
		KshstFlexWorkSetting setting = convertToDbType(flexWorkSetting);
		this.commandProxy().update(setting);
	}
	
}
