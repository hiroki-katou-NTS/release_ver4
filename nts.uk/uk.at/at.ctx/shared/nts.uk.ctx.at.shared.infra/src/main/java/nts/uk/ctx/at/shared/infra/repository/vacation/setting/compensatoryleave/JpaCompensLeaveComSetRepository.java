/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.vacation.setting.compensatoryleave;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensLeaveComSetRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryLeaveComSetting;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave.KclmtAcquisitionCom;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave.KclmtCompensLeaveCom;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave.KctmtDigestTimeCom;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave.KocmtOccurrenceSet;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.compensatoryleave.KocmtOccurrenceSetPK;

/**
 * The Class JpaCompensLeaveComSetRepository.
 */
@Stateless
public class JpaCompensLeaveComSetRepository extends JpaRepository implements CompensLeaveComSetRepository {
    
    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.
     * CompensLeaveComSetRepository#insert(nts.uk.ctx.at.shared.dom.vacation.
     * setting.compensatoryleave.CompensatoryLeaveComSetting)
     */
    @Override
    public void insert(CompensatoryLeaveComSetting setting) {
        this.commandProxy().insert(this.toEntity(setting));
        this.getEntityManager().flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.
     * CompensLeaveComSetRepository#update(nts.uk.ctx.at.shared.dom.vacation.
     * setting.compensatoryleave.CompensatoryLeaveComSetting)
     */
    @Override
    public void update(CompensatoryLeaveComSetting setting) {
        this.commandProxy().update(this.toEntity(setting));
        this.getEntityManager().flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.
     * CompensLeaveComSetRepository#find(java.lang.String)
     */
    @SneakyThrows
    @Override
	public CompensatoryLeaveComSetting find(String companyId) {

		String sqlJdbcOc = "SELECT * FROM KOCMT_OCCURRENCE_SET WITH(INDEX(PK_KOCMT_OCCURRENCE_SET)) WHERE CID = ?";

		try (PreparedStatement stmtOc = this.connection().prepareStatement(sqlJdbcOc)) {
			stmtOc.setString(1, companyId);

			List<KocmtOccurrenceSet> listOccurrence = new NtsResultSet(stmtOc.executeQuery())
					.getList(rec -> {
						KocmtOccurrenceSet entity = new KocmtOccurrenceSet();
						KocmtOccurrenceSetPK kocmtOccurrenceSetPK = new KocmtOccurrenceSetPK();
						kocmtOccurrenceSetPK.setCid(rec.getString("CID"));
						kocmtOccurrenceSetPK.setOccurrType(rec.getInt("OCCURR_TYPE"));
						entity.setKocmtOccurrenceSetPK(kocmtOccurrenceSetPK);
						entity.setTransfType(rec.getInt("TRANSF_TYPE"));
						entity.setOneDayTime(rec.getLong("ONE_DAY_TIME"));
						entity.setHalfDayTime(rec.getLong("HALF_DAY_TIME"));
						entity.setCertainTime(rec.getLong("CERTAIN_TIME"));
						entity.setUseType(rec.getInt("USE_TYPE"));
						return entity;
					});

			String sqlJdbc = "SELECT *, KCLC.MANAGE_ATR KCLCMANAGE_ATR, KDTC.MANAGE_ATR KDTCMANAGE_ATR, "
					+ "KCLC.DEADL_CHECK_MONTH KCLCDEADL_CHECK_MONTH, KAC.DEADL_CHECK_MONTH KACDEADL_CHECK_MONTH "
					+ "FROM KCLMT_COMPENS_LEAVE_COM KCLC "
					+ "LEFT JOIN KCLMT_ACQUISITION_COM KAC ON KCLC.CID = KAC.CID "
					+ "LEFT JOIN KCTMT_DIGEST_TIME_COM KDTC ON KCLC.CID = KDTC.CID "
					+ "WHERE KCLC.CID = ?";

			try(PreparedStatement stmt = this.connection().prepareStatement(sqlJdbc)) {

				stmt.setString(1, companyId);
	
				Optional<KclmtCompensLeaveCom> result = new NtsResultSet(stmt.executeQuery())
						.getSingle(rec -> {
							KclmtAcquisitionCom kclmtAcquisitionCom = new KclmtAcquisitionCom();
							kclmtAcquisitionCom.setCid(rec.getString("CID"));
							kclmtAcquisitionCom.setExpTime(rec.getInt("EXP_TIME"));
							kclmtAcquisitionCom.setPreempPermitAtr(rec.getInt("PREEMP_PERMIT_ATR"));
							kclmtAcquisitionCom.setDeadlCheckMonth(rec.getInt("KACDEADL_CHECK_MONTH"));
	
							KctmtDigestTimeCom kctmtDigestTimeCom = new KctmtDigestTimeCom();
							kctmtDigestTimeCom.setCid(rec.getString("CID"));
							kctmtDigestTimeCom.setManageAtr(rec.getInt("KDTCMANAGE_ATR"));
							kctmtDigestTimeCom.setDigestiveUnit(rec.getInt("DIGESTIVE_UNIT"));
	
							KclmtCompensLeaveCom entity = new KclmtCompensLeaveCom();
							entity.setCid(rec.getString("CID"));
							entity.setManageAtr(rec.getInt("KCLCMANAGE_ATR"));
							entity.setDeadlCheckMonth(rec.getInt("KCLCDEADL_CHECK_MONTH"));
							entity.setKclmtAcquisitionCom(kclmtAcquisitionCom);
							entity.setKctmtDigestTimeCom(kctmtDigestTimeCom);
							entity.setListOccurrence(listOccurrence);
							return entity;
						});
	
				if (!result.isPresent()) {
					return null;
				}
	
				return new CompensatoryLeaveComSetting(new JpaCompensLeaveComGetMemento(result.get()));
			}
		}
	}

    /**
     * To entity.
     *
     * @param setting the setting
     * @return the kclmt compens leave com
     */
    private KclmtCompensLeaveCom toEntity(CompensatoryLeaveComSetting setting) {
        Optional<KclmtCompensLeaveCom> optinal = this.queryProxy().find(setting.getCompanyId(),
                KclmtCompensLeaveCom.class);
        KclmtCompensLeaveCom entity = null;
        if (optinal.isPresent()) {
            entity = optinal.get();
        } else {
            entity = new KclmtCompensLeaveCom();
        }
        JpaCompensLeaveComSetMemento memento = new JpaCompensLeaveComSetMemento(entity);
        setting.saveToMemento(memento);
        return entity;
    }

	@Override
	public void copyMasterData(String sourceCid, String targetCid, boolean isReplace) {
		// TODO Auto-generated method stub
		
	}
}
