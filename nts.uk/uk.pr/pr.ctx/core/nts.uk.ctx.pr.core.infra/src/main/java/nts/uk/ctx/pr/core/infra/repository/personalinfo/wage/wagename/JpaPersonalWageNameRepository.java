package nts.uk.ctx.pr.core.infra.repository.personalinfo.wage.wagename;

import java.util.List;
import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.personalinfo.wage.wagename.PersonalWageNameMaster;
import nts.uk.ctx.pr.core.dom.personalinfo.wage.wagename.PersonalWageNameRepository;
import nts.uk.ctx.pr.core.infra.entity.personalinfo.wage.QpwmtPersonalWageName;

@Stateless
public class JpaPersonalWageNameRepository extends JpaRepository implements PersonalWageNameRepository {
	private final String SELECT_NO_WHERE = "SELECT c FROM QpwmtPersonalWageName c";
	private final String SELECT_ALL_PERSONAL = SELECT_NO_WHERE
			+ " WHERE c.qpwmtPersonalWageNamePK.ccd = :companyCode"
			+ " AND c.qpwmtPersonalWageNamePK.ctgAtr = :categoryAtr";
	

	@Override
	public List<PersonalWageNameMaster> getPersonalWageName(String companyCode, int categoryAtr) {
		return this.queryProxy().query(SELECT_ALL_PERSONAL, QpwmtPersonalWageName.class)
				.setParameter("companyCode", companyCode)
				.setParameter("categoryAtr", categoryAtr)
				.getList(c -> toDomain(c));
	}
	
	private static PersonalWageNameMaster toDomain(QpwmtPersonalWageName entity){
		val domain = PersonalWageNameMaster.createFromJavaType(
				entity.qpwmtPersonalWageNamePK.ccd,
				entity.qpwmtPersonalWageNamePK.ctgAtr,
				entity.qpwmtPersonalWageNamePK.pWageCd,
				entity.pWageName);
		entity.toDomain(domain);
		return domain;
	}


}
