package nts.uk.ctx.at.function.infra.repository.monthlyworkschedule;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.ItemSelectionEnum;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.OutputItemMonthlyWorkSchedule;
import nts.uk.ctx.at.function.dom.monthlyworkschedule.OutputItemMonthlyWorkScheduleRepository;
import nts.uk.ctx.at.function.infra.entity.monthlyworkschedule.KfnmtRptWkMonOut;
import nts.uk.ctx.at.function.infra.entity.monthlyworkschedule.KfnmtRptWkMonOutAtd;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class JpaOutputItemMonthlyWorkScheduleRepository extends JpaRepository
		implements OutputItemMonthlyWorkScheduleRepository {

	/** The Constant FIND_BY_SELECTION_CID. */
	private static final String FIND_BY_SELECTION_CID = "SELECT c FROM KfnmtRptWkMonOut c"
			+ " WHERE c.companyID = :companyID "
			+ " AND c.itemType = :itemType";

	/** The Constant FIND_BY_SELECTION_CID_SID. */
	private static final String FIND_BY_SELECTION_CID_SID = "SELECT c FROM KfnmtRptWkMonOut c"
			+ " WHERE c.companyID = :companyID"
			+ " AND c.sid = :employeeID"
			+ " AND c.itemType = :itemType";

	/** The Constant FIND_BY_SELECTION_CID_CODE. */
	private static final String FIND_BY_SELECTION_CID_CODE = "SELECT c FROM KfnmtRptWkMonOut c"
			+ " WHERE c.companyID = :companyID"
			+ " AND c.itemType = :itemType"
			+ " AND c.itemCode = :itemCode";

	/** The Constant FIND_BY_SELECTION_CID_CODE_SID. */
	private static final String FIND_BY_SELECTION_CID_CODE_SID = "SELECT c FROM KfnmtRptWkMonOut c"
			+ " WHERE c.companyID = :companyID"
			+ " AND c.itemCode = :itemCode"
			+ " AND c.sid = :employeeID"
			+ " AND c.itemType = :itemType";
	
	/** The Constant FIND_BY_LAYOUTID. */
	private static final String FIND_BY_LAYOUTID = "SELECT c FROM KfnmtRptWkMonOuttd c"
			+ " WHERE c.pk.layoutID = :layoutID";

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.function.dom.monthlyworkschedule.
	 * OutputItemMonthlyWorkScheduleRepository#add(nts.uk.ctx.at.function.dom.
	 * monthlyworkschedule.OutputItemMonthlyWorkSchedule)
	 */
	@Override
	public void add(OutputItemMonthlyWorkSchedule domain) {
		domain.setLayoutID(UUID.randomUUID().toString());
		this.commandProxy().insert(this.toEntity(domain));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.function.dom.monthlyworkschedule.
	 * OutputItemMonthlyWorkScheduleRepository#update(nts.uk.ctx.at.function.dom
	 * .monthlyworkschedule.OutputItemMonthlyWorkSchedule)
	 */
	@Override
	public void update(OutputItemMonthlyWorkSchedule domain) {
		List<KfnmtRptWkMonOutAtd> otdList = this.queryProxy().query(FIND_BY_LAYOUTID, KfnmtRptWkMonOutAtd.class)
											.setParameter("layoutID", domain.getLayoutID())
											.getList();
		
		this.commandProxy().removeAll(otdList);
		this.getEntityManager().flush();
		this.commandProxy().update(toEntity(domain));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.function.dom.monthlyworkschedule.
	 * OutputItemMonthlyWorkScheduleRepository#delete(nts.uk.ctx.at.function.dom
	 * .monthlyworkschedule.OutputItemMonthlyWorkSchedule)
	 */
	@Override
	public void delete(OutputItemMonthlyWorkSchedule domain) {
		this.commandProxy().remove(this.toEntity(domain));

	}

	/**
	 * To domain.
	 *
	 * @param entity the entity
	 * @return the output item monthly work schedule
	 */
	private OutputItemMonthlyWorkSchedule toDomain(KfnmtRptWkMonOut entity) {
		return new OutputItemMonthlyWorkSchedule(entity);
	}

	/**
	 * To entity.
	 *
	 * @param domain the domain
	 * @return the kfnmt monthly work sche
	 */
	private KfnmtRptWkMonOut toEntity(OutputItemMonthlyWorkSchedule domain) {
		KfnmtRptWkMonOut entity = new KfnmtRptWkMonOut();
		String contractCd = AppContexts.user().contractCode();
		entity.setContractCd(contractCd);
		domain.saveToMemento(entity);
		return entity;
	}

	@Override
	public List<OutputItemMonthlyWorkSchedule> findBySelectionAndCidAndSid(
			  ItemSelectionEnum itemSelectionEnum
			, String companyId
			, String employeeId) {
		if (itemSelectionEnum == ItemSelectionEnum.STANDARD_SELECTION) {
			return this.queryProxy().query(FIND_BY_SELECTION_CID, KfnmtRptWkMonOut.class)
					.setParameter("companyID", companyId)
					.setParameter("itemType", itemSelectionEnum.value)
					.getList(item -> new OutputItemMonthlyWorkSchedule(item));
		}
		if (itemSelectionEnum == ItemSelectionEnum.FREE_SETTING) {
			return this.queryProxy().query(FIND_BY_SELECTION_CID_SID, KfnmtRptWkMonOut.class)
					.setParameter("companyID", companyId)
					.setParameter("employeeID", employeeId)
					.setParameter("itemType", itemSelectionEnum.value)
					.getList(item -> new OutputItemMonthlyWorkSchedule(item));
		}
		return Collections.emptyList();
	}

	@Override
	public void deleteBySelectionAndCidAndSidAndCode(
			  ItemSelectionEnum itemSelectionEnum
			, String companyId
			, String itemCode
			, String employeeId) {
		Optional<KfnmtRptWkMonOut> kfnmtRptWkMonOut = Optional.empty();
		if (itemSelectionEnum == ItemSelectionEnum.STANDARD_SELECTION) {
			kfnmtRptWkMonOut = this.queryProxy().query(FIND_BY_SELECTION_CID_CODE, KfnmtRptWkMonOut.class)
					.setParameter("companyID", companyId)
					.setParameter("itemType", itemSelectionEnum.value)
					.setParameter("itemCode", itemCode)
					.getSingle();
		}
		if (itemSelectionEnum == ItemSelectionEnum.FREE_SETTING) {
			kfnmtRptWkMonOut = this.queryProxy().query(FIND_BY_SELECTION_CID_CODE_SID, KfnmtRptWkMonOut.class)
					.setParameter("companyID", companyId).setParameter("employeeID", employeeId)
					.setParameter("itemCode", itemCode)
					.setParameter("itemType", itemSelectionEnum.value)
					.getSingle();
		}
		if (kfnmtRptWkMonOut.isPresent()) {
			this.commandProxy().remove(kfnmtRptWkMonOut.get());
		}
	}

	@Override
	public Optional<OutputItemMonthlyWorkSchedule> findBySelectionAndCidAndSidAndCode(
			  ItemSelectionEnum itemSelectionEnum
			, String companyId
			, String itemCode
			, String employeeId) {

		// 定型選択の場合
		if (itemSelectionEnum == ItemSelectionEnum.STANDARD_SELECTION) {
			return this.queryProxy().query(FIND_BY_SELECTION_CID_CODE, KfnmtRptWkMonOut.class)
					.setParameter("companyID", companyId)
					.setParameter("itemType", itemSelectionEnum.value)
					.setParameter("itemCode", itemCode)
					.getSingle(entity -> this.toDomain(entity));
		}

		// 自由設定の場合
		if (itemSelectionEnum == ItemSelectionEnum.FREE_SETTING) {
			return this.queryProxy().query(FIND_BY_SELECTION_CID_CODE_SID, KfnmtRptWkMonOut.class)
					.setParameter("companyID", companyId)
					.setParameter("employeeID", employeeId)
					.setParameter("itemCode", itemCode)
					.setParameter("itemType", itemSelectionEnum.value)
					.getSingle(entity -> this.toDomain(entity));
		}

		return Optional.empty();
	}

}
