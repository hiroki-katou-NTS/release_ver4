package nts.uk.ctx.bs.employee.infra.repository.jobtitle.affiliate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.ver1.AffJobTitleHistoryItemRepository_ver1;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.ver1.AffJobTitleHistoryItem_ver1;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.affiliate.BsymtAffJobTitleHistItem;

@Stateless
public class JpaAffJobTitleHistoryItemRepository_v1 extends JpaRepository
		implements AffJobTitleHistoryItemRepository_ver1 {
	
	private final String GET_BY_SID_DATE = "select hi from BsymtAffJobTitleHistItem hi"
			+ " inner join BsymtAffJobTitleHist h on hi.hisId = h.hisId"
			+ " where hi.sid = :sid and h.strDate <= :referDate and h.endDate >= :referDate";
	
	private final String GET_BY_JID_DATE = "select hi from BsymtAffJobTitleHistItem hi"
			+ " inner join BsymtAffJobTitleHist h on hi.hisId = h.hisId"
			+ " where hi.jobTitleId = :jobTitleId and h.strDate <= :referDate and h.endDate >= :referDate";
	
	private final String GET_ALL_BY_SID = "select hi from BsymtAffJobTitleHistItem hi"
			+ " where hi.sid = :sid";
	
	private final String GET_BY_LIST_EID_DATE = "select hi from BsymtAffJobTitleHistItem hi"
			+ " inner join BsymtAffJobTitleHist h on hi.hisId = h.hisId"
			+ " where h.sid IN :lstSid and h.strDate <= :referDate and h.endDate >= :referDate";

	/**
	 * Convert from domain to entity
	 * 
	 * @param domain
	 * @return
	 */
	private BsymtAffJobTitleHistItem toEntity(AffJobTitleHistoryItem_ver1 domain) {
		return new BsymtAffJobTitleHistItem(domain.getHistoryId(), domain.getEmployeeId(), domain.getJobTitleId(),
				domain.getNote().v());
	}

	/**
	 * Update entity
	 * 
	 * @param domain
	 * @param entity
	 */
	private void updateEntity(AffJobTitleHistoryItem_ver1 domain, BsymtAffJobTitleHistItem entity) {
		if (domain.getJobTitleId() != null){
			entity.jobTitleId = domain.getJobTitleId();
		}
		if (domain.getNote() != null){
			entity.note = domain.getNote().v();
		}
	}

	@Override
	public void add(AffJobTitleHistoryItem_ver1 domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	@Override
	public void update(AffJobTitleHistoryItem_ver1 domain) {

		Optional<BsymtAffJobTitleHistItem> existItem = this.queryProxy().find(domain.getHistoryId(),
				BsymtAffJobTitleHistItem.class);

		if (!existItem.isPresent()) {
			throw new RuntimeException("invalid BsymtAffJobTitleHistItem");
		}
		updateEntity(domain, existItem.get());

		this.commandProxy().update(existItem.get());
	}

	@Override
	public void delete(String histId) {
		Optional<BsymtAffJobTitleHistItem> existItem = this.queryProxy().find(histId, BsymtAffJobTitleHistItem.class);

		if (!existItem.isPresent()) {
			throw new RuntimeException("invalid BsymtAffJobTitleHistItem");
		}

		this.commandProxy().remove(BsymtAffJobTitleHistItem.class, histId);
	}

	private AffJobTitleHistoryItem_ver1 toDomain(BsymtAffJobTitleHistItem ent) {
		return AffJobTitleHistoryItem_ver1.createFromJavaType(ent.hisId, ent.sid, ent.jobTitleId,
				ent.note);
	}

	@Override
	public Optional<AffJobTitleHistoryItem_ver1> findByHitoryId(String historyId) {
		Optional<BsymtAffJobTitleHistItem> optionData = this.queryProxy().find(historyId,
				BsymtAffJobTitleHistItem.class);
		if (optionData.isPresent()) {
			return Optional.of(toDomain(optionData.get()));
		}
		return Optional.empty();
	}
	
	@Override
	public Optional<AffJobTitleHistoryItem_ver1> getByEmpIdAndReferDate(String employeeId, GeneralDate referDate) {
		Optional<BsymtAffJobTitleHistItem> optionData = this.queryProxy()
				.query(GET_BY_SID_DATE, BsymtAffJobTitleHistItem.class).setParameter("sid", employeeId)
				.setParameter("referDate", referDate).getSingle();
		if (optionData.isPresent()) {
			BsymtAffJobTitleHistItem ent = optionData.get();
			return Optional.of(AffJobTitleHistoryItem_ver1.createFromJavaType(ent.hisId, ent.sid,
					ent.jobTitleId, ent.note));
		}
		return Optional.empty();
	}

	@Override
	public List<AffJobTitleHistoryItem_ver1> getByJobIdAndReferDate(String jobId, GeneralDate referDate) {
		List<BsymtAffJobTitleHistItem> lstData = this.queryProxy()
				.query(GET_BY_JID_DATE, BsymtAffJobTitleHistItem.class).setParameter("jobTitleId", jobId)
				.setParameter("referDate", referDate).getList();
		List<AffJobTitleHistoryItem_ver1> lstObj = new ArrayList<>();
		if (!lstData.isEmpty()) {
			for (BsymtAffJobTitleHistItem ent : lstData) {
				lstObj.add(
						AffJobTitleHistoryItem_ver1.createFromJavaType(ent.hisId, ent.sid, ent.jobTitleId, ent.note));
			}
		}
		return lstObj;
	}

	@Override
	public List<AffJobTitleHistoryItem_ver1> getAllBySid(String sid) {
		List<BsymtAffJobTitleHistItem> optionData = this.queryProxy()
				.query(GET_ALL_BY_SID, BsymtAffJobTitleHistItem.class)
				.setParameter("sid", sid).getList();
		
		List<AffJobTitleHistoryItem_ver1> lstAffJobTitleHistoryItems = new ArrayList<>();
		
		if (optionData != null && !optionData.isEmpty()) {
			optionData.stream().forEach((item) -> {
				lstAffJobTitleHistoryItems.add(AffJobTitleHistoryItem_ver1.createFromJavaType(item.hisId, item.sid, item.jobTitleId, item.note));
			});
		}
		
		if (lstAffJobTitleHistoryItems != null && !lstAffJobTitleHistoryItems.isEmpty()) {
			return lstAffJobTitleHistoryItems;
		}
		return null;
	}

	@Override
	public List<AffJobTitleHistoryItem_ver1> getAllByListSidDate(List<String> lstSid, GeneralDate referDate) {
		List<BsymtAffJobTitleHistItem> data = this.queryProxy()
				.query(GET_BY_LIST_EID_DATE, BsymtAffJobTitleHistItem.class)
				.setParameter("sid", lstSid.toArray().toString())
				.setParameter("referDate", referDate).getList();
		
		List<AffJobTitleHistoryItem_ver1> lstAffJobTitleHistoryItems = new ArrayList<>();
		
		if (data != null && !data.isEmpty()) {
			data.stream().forEach((item) -> {
				lstAffJobTitleHistoryItems.add(AffJobTitleHistoryItem_ver1.createFromJavaType(item.hisId, item.sid, item.jobTitleId, item.note));
			});
		}
		
		if (lstAffJobTitleHistoryItems != null && !lstAffJobTitleHistoryItems.isEmpty()) {
			return lstAffJobTitleHistoryItems;
		}
		return null;
	}
}
