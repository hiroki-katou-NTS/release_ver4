package nts.uk.ctx.at.schedule.infra.repository.schedule.setting.modify;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.DateAuthority;
import nts.uk.ctx.at.schedule.dom.schedule.setting.modify.control.DateAuthorityRepository;
import nts.uk.ctx.at.schedule.infra.entity.schedule.setting.modify.KscmtScheAuthDate;
import nts.uk.ctx.at.schedule.infra.entity.schedule.setting.modify.KscstScheDateAuthorityPK;
@Stateless
public class JpaDateAuthorityRepository extends JpaRepository implements DateAuthorityRepository{
	private static final String SELECT_BY_CID;
	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT e");
		builderString.append(" FROM KscmtScheAuthDate e");
		builderString.append(" WHERE e.kscstScheDateAuthorityPK.companyId = :companyId");
		builderString.append(" AND e.kscstScheDateAuthorityPK.roleId = :roleId");
		SELECT_BY_CID = builderString.toString();
	}
	/**
	 * Convert to Domain 
	 * @param schemodifyDeadline
	 * @return
	 */
	private DateAuthority convertToDomain(KscmtScheAuthDate kscstScheDateAuthority) {
		DateAuthority dateAuthority = DateAuthority.createFromJavaType(
				kscstScheDateAuthority.kscstScheDateAuthorityPK.companyId, 
				kscstScheDateAuthority.kscstScheDateAuthorityPK.roleId, 
				kscstScheDateAuthority.availableDate,
				kscstScheDateAuthority.kscstScheDateAuthorityPK.functionNoDate
				
				);
		return dateAuthority;
	}
	
	/**
	 * Convert to Database Type
	 * @param schemodifyDeadline
	 * @return
	 */
	private KscmtScheAuthDate convertToDbType(DateAuthority dateAuthority) {
		KscmtScheAuthDate scheDateAuthority = new KscmtScheAuthDate();
		KscstScheDateAuthorityPK scheDateAuthorityPK = new KscstScheDateAuthorityPK(dateAuthority.getCompanyId(), dateAuthority.getRoleId(),dateAuthority.getFunctionNoDate());
		scheDateAuthority.availableDate = dateAuthority.getAvailableDate();
		scheDateAuthority.kscstScheDateAuthorityPK = scheDateAuthorityPK;
		return scheDateAuthority;
	}
	
	/**
	 * Find by Company Id
	 */
	@Override
	public List<DateAuthority> findByCompanyId(String companyId, String roleId) {
		return this.queryProxy().query(SELECT_BY_CID, KscmtScheAuthDate.class).setParameter("companyId", companyId)
				.setParameter("roleId", roleId)
				.getList(c -> convertToDomain(c));
	}
	
	/**
	 * Add Schemodify Deadline
	 */
	@Override
	public void add(DateAuthority author) {
		this.commandProxy().insert(convertToDbType(author));
	}

	/**
	 * Update Schemodify Deadline
	 */
	@Override
	public void update(DateAuthority author) {
		KscstScheDateAuthorityPK primaryKey = new KscstScheDateAuthorityPK(author.getCompanyId(), author.getRoleId(), author.getFunctionNoDate());
		KscmtScheAuthDate entity = this.queryProxy().find(primaryKey, KscmtScheAuthDate.class).get();
				entity.availableDate = author.getAvailableDate();
				entity.kscstScheDateAuthorityPK = primaryKey;
		this.commandProxy().update(entity);
	}
	
	/**
	 * Find one Schemodify Deadline by companyId and roleId
	 */
	@Override
	public Optional<DateAuthority> findByCId(String companyId, String roleId, int functionNoDate) {
		return this.queryProxy().find(new KscstScheDateAuthorityPK(companyId, roleId ,functionNoDate), KscmtScheAuthDate.class)
				.map(c -> convertToDomain(c));
	}

}
