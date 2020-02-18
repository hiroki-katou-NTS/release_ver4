package nts.uk.ctx.hr.develop.infra.repository.sysoperationset.businessrecognition;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.hr.develop.dom.sysoperationset.businessrecognition.MenuApprovalSettings;
import nts.uk.ctx.hr.develop.dom.sysoperationset.businessrecognition.MenuApprovalSettingsRepository;
import nts.uk.ctx.hr.develop.dom.sysoperationset.businessrecognition.algorithm.dto.BusinessApprovalSettingsDto;
import nts.uk.ctx.hr.develop.infra.entity.sysoperationset.businessrecognition.JcmmtMenuApr;

@Stateless
public class JpaMenuApprovalSettingsRepositoryImpl extends JpaRepository implements MenuApprovalSettingsRepository {

	private static final String GET_BUSINESS_APPROVAL_SETTINGS = "SELECT DISTINCT " 
			+ "ma, " 
			+ "l.rptLayouCd, "
			+ "l.rptLayouName, " 
			+ "sm.displayName, " 
			+ "sm.screenID, "
			+ "mo.useApproval, " 
			+ "mo.NO_RANK_ORDER, " 

			+ "FROM JcmmtMenuApr ma JOIN CcgstStandardMenu sm "
			+ "ON ma.programId = sm.programId "
			+ "AND ma.screenId = sm.screenID " 
			+ "LEFT OUTER JOIN JhnmtRptLayout l "
			+ "ON ma.rptLayoutId = l.jhnmtRptLayoutPk.rptLayoutId  "
			+ "LEFT OUTER JOIN JcmctMenuOperation mo "
			+ "ON ma.programId = mo.jcmctMenuOperationPK.programId "

			+ "WHERE " + "ma.pkJcmmtMenuApr.cId = :cId ";
	
	/* (non-Javadoc)
	 * sql for 業務承認設定を取得する algorithm
	 */
	@Override
	public List<BusinessApprovalSettingsDto> getBusinessApprovalSettings(String cid) {
		return this.getEntityManager().createQuery(GET_BUSINESS_APPROVAL_SETTINGS, Object[].class)
				.setParameter("cId", cid)
				.getResultList().stream().map(c-> this.joinObjectToDomain(c)).collect(Collectors.toList());
	}

	private BusinessApprovalSettingsDto joinObjectToDomain(Object[] object) {
		JcmmtMenuApr menuApr = (JcmmtMenuApr) object[0];
		return new BusinessApprovalSettingsDto(
				menuApr.toDomain(),
				object[1] == null ? "" : (String) object[1], 
				object[2] == null ? "" : (String) object[2],
				object[3] == null ? "" : (String) object[3],
				object[4] == null ? "" : (String) object[4], 
				object[5] == null ? false : (int) object[5] == 1,
				object[6] == null ? "" : (String) object[6]);
				
	}

	@Override
	public void update(List<MenuApprovalSettings> domain) {
		List<JcmmtMenuApr> entity = domain.stream().map(c -> JcmmtMenuApr.toEntity(c)).collect(Collectors.toList());
		this.commandProxy().updateAll(entity);
	}
}
