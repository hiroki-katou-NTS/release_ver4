/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.vacation.setting.annualpaidleave;

import java.sql.PreparedStatement;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.AnnualPaidLeaveSettingRepository;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.annualpaidleave.KalmtAnnualPaidLeave;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.annualpaidleave.KmamtMngAnnualSet;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.annualpaidleave.KtvmtTimeAnnualSet;

/**
 * The Class JpaAnnualPaidLeaveSettingRepository.
 */
@Stateless
public class JpaAnnualPaidLeaveSettingRepository extends JpaRepository implements AnnualPaidLeaveSettingRepository {
    
    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.
     * AnnualPaidLeaveSettingRepository#add(nts.uk.ctx.at.shared.dom.vacation.
     * setting.annualpaidleave.AnnualPaidLeaveSetting)
     */
    @Override
    public void add(AnnualPaidLeaveSetting setting) {
        KalmtAnnualPaidLeave v = this.toEntity(setting);
        this.commandProxy().insert(v);
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.pr.core.dom.vacation.setting.annualpaidleave.
     * AnnualPaidLeaveSettingRepository#update(nts.uk.ctx.pr.core.dom.vacation.
     * setting.annualpaidleave.AnnualPaidLeaveSetting)
     */
    @Override
    public void update(AnnualPaidLeaveSetting setting) {
        this.commandProxy().update(this.toEntity(setting));
    }

    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.ctx.pr.core.dom.vacation.setting.annualpaidleave.
     * AnnualPaidLeaveSettingRepository#findByCompanyId(java.lang.String)
     */
    @SneakyThrows
    @Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public AnnualPaidLeaveSetting findByCompanyId(String companyId) {
		String sqlJdbc = "SELECT *, KMAS.ROUND_PRO_CLA KMASROUND_PRO_CLA, KTAS.ROUND_PRO_CLA KTASROUND_PRO_CLA "
				+ "FROM KALMT_ANNUAL_PAID_LEAVE KAPL "
				+ "LEFT JOIN KMAMT_MNG_ANNUAL_SET KMAS ON KAPL.CID = KMAS.CID "
				+ "LEFT JOIN KTVMT_TIME_ANNUAL_SET KTAS ON KAPL.CID = KTAS.CID "
				+ "WHERE KAPL.CID = ?";

		try (PreparedStatement stmt = this.connection().prepareStatement(sqlJdbc)) {

			stmt.setString(1, companyId);

			Optional<KalmtAnnualPaidLeave> result = new NtsResultSet(stmt.executeQuery())
					.getSingle(rec -> {
						KmamtMngAnnualSet kmamtMngAnnualSet = new KmamtMngAnnualSet();
						kmamtMngAnnualSet.setCid(rec.getString("CID"));
						kmamtMngAnnualSet.setHalfMaxGrantDay(rec.getDouble("HALF_MAX_GRANT_DAY"));
						kmamtMngAnnualSet.setHalfMaxDayYear(rec.getInt("HALF_MAX_DAY_YEAR"));
						kmamtMngAnnualSet.setHalfManageAtr(rec.getInt("HALF_MANAGE_ATR"));
						kmamtMngAnnualSet.setHalfMaxReference(rec.getInt("HALF_MAX_REFERENCE"));
						kmamtMngAnnualSet
								.setHalfMaxUniformComp(rec.getInt("HALF_MAX_UNIFORM_COMP"));
						kmamtMngAnnualSet.setIsWorkDayCal(rec.getInt("IS_WORK_DAY_CAL"));
						kmamtMngAnnualSet.setRetentionYear(rec.getInt("RETENTION_YEAR"));
						kmamtMngAnnualSet.setRemainingMaxDay(rec.getDouble("REMAINING_MAX_DAY"));
						kmamtMngAnnualSet
								.setNextGrantDayDispAtr(rec.getInt("NEXT_GRANT_DAY_DISP_ATR"));
						kmamtMngAnnualSet
								.setRemainingNumDispAtr(rec.getInt("REMAINING_NUM_DISP_ATR"));
						kmamtMngAnnualSet.setYearlyOfDays(rec.getDouble("YEARLY_OF_DAYS"));
						kmamtMngAnnualSet.setRoundProcessCla(rec.getInt("KMASROUND_PRO_CLA"));

						KtvmtTimeAnnualSet ktvmtTimeVacationSet = new KtvmtTimeAnnualSet();
						ktvmtTimeVacationSet.setCid(rec.getString("CID"));
						ktvmtTimeVacationSet.setTimeManageAtr(rec.getInt("TIME_MANAGE_ATR"));
						ktvmtTimeVacationSet.setTimeUnit(rec.getInt("TIME_UNIT"));
						ktvmtTimeVacationSet
								.setTimeMaxDayManageAtr(rec.getInt("TIME_MAX_DAY_MANAGE_ATR"));
						ktvmtTimeVacationSet
								.setTimeMaxDayReference(rec.getInt("TIME_MAX_DAY_REFERENCE"));
						ktvmtTimeVacationSet
								.setTimeMaxDayUnifComp(rec.getInt("TIME_MAX_DAY_UNIF_COMP"));
						ktvmtTimeVacationSet
								.setIsEnoughTimeOneDay(rec.getInt("IS_ENOUGH_TIME_ONE_DAY"));
						ktvmtTimeVacationSet.setRoundProcessCla(rec.getInt("KTASROUND_PRO_CLA"));

						KalmtAnnualPaidLeave entity = new KalmtAnnualPaidLeave();
						entity.setCid(rec.getString("CID"));
						entity.setPriorityType(rec.getInt("PRIORITY_TYPE"));
						entity.setManageAtr(rec.getInt("MANAGE_ATR"));
						entity.setKmamtMngAnnualSet(kmamtMngAnnualSet);
						entity.setKtvmtTimeVacationSet(ktvmtTimeVacationSet);
						return entity;
					});

			if (!result.isPresent()) {
				return null;
			}

			return new AnnualPaidLeaveSetting(
					new JpaAnnualPaidLeaveSettingGetMemento(result.get()));
		}
	}
    
    /**
     * To entity.
     *
     * @param setting the setting
     * @return the kalmt annual paid leave
     */
    private KalmtAnnualPaidLeave toEntity(AnnualPaidLeaveSetting setting) {
        Optional<KalmtAnnualPaidLeave> optinal = this.queryProxy().find(setting.getCompanyId(),
                KalmtAnnualPaidLeave.class);
        KalmtAnnualPaidLeave entity = null;
        if (optinal.isPresent()) {
            entity = optinal.get();
        } else {
            entity = new KalmtAnnualPaidLeave();
        }
        setting.saveToMemento(new JpaAnnualPaidLeaveSettingSetMemento(entity));
        return entity;
    }

	@Override
	public void copyMasterData(String sourceCid, String targetCid, boolean isReplace) {
		// TODO Auto-generated method stub
		
	}
    
}
