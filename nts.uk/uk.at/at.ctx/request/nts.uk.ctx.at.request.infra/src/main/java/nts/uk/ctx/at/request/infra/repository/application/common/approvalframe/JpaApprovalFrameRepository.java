package nts.uk.ctx.at.request.infra.repository.application.common.approvalframe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.request.dom.application.common.appapprovalphase.AppApprovalPhase;
import nts.uk.ctx.at.request.dom.application.common.approvalframe.ApprovalFrame;
import nts.uk.ctx.at.request.dom.application.common.approvalframe.ApprovalFrameRepository;
import nts.uk.ctx.at.request.infra.entity.application.common.approvalframe.KrqdtApprovalFrame;
import nts.uk.ctx.at.request.infra.entity.application.common.approvalframe.KrqdtApprovalFramePK;
/**
 * 
 * @author hieult
 *
 */
@Stateless
public class JpaApprovalFrameRepository extends JpaRepository implements ApprovalFrameRepository {

	private final String SELECT = "SELECT c FROM KrqdtApprovalFrame c ";
	private final String SELECT_SINGLE = "SELECT c FROM KrqdtApprovalFrame c"
			+ " WHERE c.krqdtApprovalFramePK.companyID = :companyID"
			+ " AND c.krqdtApprovalFramePK.phaseID = :phaseID"
			+ " AND c.krqdtApprovalFramePK.dispOrder = :dispOrder "
			+ " AND c.krqdtApprovalFramePK.approverSID = :approverSID ";
	//get List Phase
	private final String SELECT_BY_PHASE_ID = SELECT
			+ " WHERE c.krqdtApprovalFramePK.companyID = :companyID"
			+ " AND c.krqdtApprovalFramePK.phaseID = :phaseID";
	
	private final String SELECT_ALL_BY_COMPANY = SELECT + " WHERE c.krqdtAppLateOrLeavePK.companyID = :companyID";

	@Override
	public Optional<ApprovalFrame> findByCode(String companyID, String phaseID, int dispOrder,String approverSID) {
		return this.queryProxy().query(SELECT_SINGLE, KrqdtApprovalFrame.class).setParameter("companyID", companyID)
				.setParameter("phaseID", phaseID)
				.setParameter("dispOrder", dispOrder)
				.setParameter("approverSID", approverSID)
				.getSingle(c -> toDomain(c));
	}

	@Override
	public void create(ApprovalFrame approvalFrame, String phaseID) {
		this.commandProxy().insert(toEntity(approvalFrame, phaseID));

	}

	@Override
	public void update(ApprovalFrame approvalFrame, String phaseID) {
		KrqdtApprovalFrame newEntity = toEntity(approvalFrame, phaseID);
		KrqdtApprovalFrame updateEntity = this.queryProxy()
				.find(newEntity.krqdtApprovalFramePK, KrqdtApprovalFrame.class).get();
		this.commandProxy().update(updateEntity);

	}

	@Override
	public void delete(ApprovalFrame approvalFrame) {
		this.commandProxy().remove(KrqdtApprovalFrame.class, new KrqdtApprovalFramePK(approvalFrame.getCompanyID(), approvalFrame.getFrameID()));
		this.getEntityManager().flush();
	}

	@Override
	public List<ApprovalFrame> getAllApproverByPhaseID(String companyID, String phaseID) {
		return this.queryProxy().query(SELECT_BY_PHASE_ID, KrqdtApprovalFrame.class)
				.setParameter("companyID", companyID)
				.setParameter("phaseID", phaseID)
				.getList(c -> toDomain(c));
	}

	private ApprovalFrame toDomain(KrqdtApprovalFrame entity) {
		return ApprovalFrame.createFromJavaType(
				entity.krqdtApprovalFramePK.companyID,
				entity.krqdtApprovalFramePK.frameID, 
				entity.dispOrder, 
				null);
	}

	private KrqdtApprovalFrame toEntity(ApprovalFrame domain, String phaseID) {
		return new KrqdtApprovalFrame(
				new KrqdtApprovalFramePK(domain.getCompanyID(), domain.getFrameID()),
				phaseID,
				domain.getDispOrder());
	}
	
	/**
	 * get List Frame By Phase ID
	 */
	@Override
	public List<ApprovalFrame> findByPhaseID(String companyID, String phaseID) {
		return this.queryProxy().query(SELECT_BY_PHASE_ID, KrqdtApprovalFrame.class)
				.setParameter("companyID", companyID)
				.setParameter("phaseID", phaseID)
				.getList(c -> toDomain(c));
	}

	@Override
	public Optional<ApprovalFrame> findByCode(String companyID, String phaseID, String dispOrder) {
		return null;
	}

	@Override
	public List<ApprovalFrame> getListFrameByListPhase(String companyID,List<String> listPhaseID) {
		
		List<ApprovalFrame> listFrame = new ArrayList<>();
		for(String phaseID :listPhaseID) {
			List<ApprovalFrame> approvalFrame = findByPhaseID( companyID,phaseID);
			listFrame.addAll(approvalFrame);
		}
		return listFrame;
	}

	@Override
	public List<List<ApprovalFrame>> getListFrameByListPhase1(String companyID, List<String> listPhaseID) {
		List<List<ApprovalFrame>> listListFrame = new ArrayList<>();
		for(String phaseID :listPhaseID) {
			List<ApprovalFrame> listFrame = new ArrayList<>();
			List<ApprovalFrame> approvalFrame = findByPhaseID( companyID,phaseID);
			listFrame.addAll(approvalFrame);
			listListFrame.add(listFrame);
		}
		return listListFrame;
	}



}
