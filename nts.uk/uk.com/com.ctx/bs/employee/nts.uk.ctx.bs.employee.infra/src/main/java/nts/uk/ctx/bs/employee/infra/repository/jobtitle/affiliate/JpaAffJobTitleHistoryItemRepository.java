package nts.uk.ctx.bs.employee.infra.repository.jobtitle.affiliate;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryItem;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryItemRepository;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.affiliate.BsymtAffJobTitleHistItem;

@Stateless
public class JpaAffJobTitleHistoryItemRepository extends JpaRepository
		implements AffJobTitleHistoryItemRepository {
	
	private static final String GET_BY_SID_DATE = "select hi from BsymtAffJobTitleHistItem hi"
			+ " inner join BsymtAffJobTitleHist h on hi.hisId = h.hisId"
			+ " where hi.sid = :sid and h.strDate <= :referDate and h.endDate >= :referDate";
	
//	private static final String GET_BY_JID_DATE = "select hi from BsymtAffJobTitleHistItem hi"
//			+ " inner join BsymtAffJobTitleHist h on hi.hisId = h.hisId"
//			+ " where hi.jobTitleId = :jobTitleId and h.strDate <= :referDate and h.endDate >= :referDate";
	
	private static final String GET_ALL_BY_SID = "select hi from BsymtAffJobTitleHistItem hi"
			+ " where hi.sid = :sid";
	
//	private static final String GET_BY_LIST_EID_DATE = "select hi from BsymtAffJobTitleHistItem hi"
//			+ " inner join BsymtAffJobTitleHist h on hi.hisId = h.hisId"
//			+ " where h.sid IN :lstSid and h.strDate <= :referDate and h.endDate >= :referDate";
	
	private static final String GET_ALL_BY_HISTID = "select hi from BsymtAffJobTitleHistItem hi"
			+ " where hi.hisId IN :histIds";
	
//	private static final String GET_BY_LIST_JOB = "select hi from BsymtAffJobTitleHistItem hi"
//			+ " where hi.hisId = :histId AND  hi.jobTitleId IN :jobTitleIds";
	
	/**
	 * Convert from domain to entity
	 * 
	 * @param domain
	 * @return
	 */
	private BsymtAffJobTitleHistItem toEntity(AffJobTitleHistoryItem domain) {
		return new BsymtAffJobTitleHistItem(domain.getHistoryId(), domain.getEmployeeId(), domain.getJobTitleId(),
				domain.getNote().v());
	}

	/**
	 * Update entity
	 * 
	 * @param domain
	 * @param entity
	 */
	private void updateEntity(AffJobTitleHistoryItem domain, BsymtAffJobTitleHistItem entity) {
		entity.jobTitleId = domain.getJobTitleId();
		entity.note = domain.getNote().v();
	}

//	 Merge BSYMT_AFF_JOB_HIST To BSYMT_AFF_JOB_HIST_ITEM  because response
//	 new Insert Method ↓
//	         ClassName  : JpaAffJobTitleHistoryRepository
//	         MethodName : addToMerge
//	@Override
//	public void add(AffJobTitleHistoryItem domain) {
//		this.commandProxy().insert(toEntity(domain));
//	}

	@Override
	public void update(AffJobTitleHistoryItem domain) {

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

	private AffJobTitleHistoryItem toDomain(BsymtAffJobTitleHistItem ent) {
		return AffJobTitleHistoryItem.createFromJavaType(ent.hisId, ent.sid, ent.jobTitleId,
				ent.note);
	}

	@Override
	public Optional<AffJobTitleHistoryItem> findByHitoryId(String historyId) {
		Optional<BsymtAffJobTitleHistItem> optionData = this.queryProxy().find(historyId,
				BsymtAffJobTitleHistItem.class);
		if (optionData.isPresent()) {
			return Optional.of(toDomain(optionData.get()));
		}
		return Optional.empty();
	}
	
	@Override
	public Optional<AffJobTitleHistoryItem> getByEmpIdAndReferDate(String employeeId, GeneralDate referDate) {
		Optional<BsymtAffJobTitleHistItem> optionData = this.queryProxy()
				.query(GET_BY_SID_DATE, BsymtAffJobTitleHistItem.class).setParameter("sid", employeeId)
				.setParameter("referDate", referDate).getSingle();
		if (optionData.isPresent()) {
			BsymtAffJobTitleHistItem ent = optionData.get();
			return Optional.of(AffJobTitleHistoryItem.createFromJavaType(ent.hisId, ent.sid,
					ent.jobTitleId, ent.note));
		}
		return Optional.empty();
	}

	@Override
	@SneakyThrows
	public List<AffJobTitleHistoryItem> getByJobIdAndReferDate(String jobId, GeneralDate referDate) {
		
		try (PreparedStatement stmt = this.connection().prepareStatement(
				"select * from BSYMT_AFF_JOB_HIST_ITEM i" + 
				" inner join BSYMT_AFF_JOB_HIST h" + 
				" on h.HIST_ID = i.HIST_ID" + 
				" where i.JOB_TITLE_ID = ?" + 
				" and h.START_DATE <= ?" + 
				" and h.END_DATE >= ?")) {
			stmt.setString(1, jobId);
			stmt.setDate(2, Date.valueOf(referDate.toLocalDate()));
			stmt.setDate(3, Date.valueOf(referDate.toLocalDate()));
			
			List<AffJobTitleHistoryItem> lstObj = new NtsResultSet(stmt.executeQuery()).getList(rec -> {
				return AffJobTitleHistoryItem.createFromJavaType(
						rec.getString("HIST_ID"),
						rec.getString("SID"),
						rec.getString("JOB_TITLE_ID"),
						rec.getString("NOTE"));
			});
	
			return lstObj.isEmpty() ? null : lstObj;
		}
	}

	@Override
	public List<AffJobTitleHistoryItem> getAllBySid(String sid) {
		List<BsymtAffJobTitleHistItem> optionData = this.queryProxy()
				.query(GET_ALL_BY_SID, BsymtAffJobTitleHistItem.class)
				.setParameter("sid", sid).getList();
		
		List<AffJobTitleHistoryItem> lstAffJobTitleHistoryItems = new ArrayList<>();
		
		if (optionData != null && !optionData.isEmpty()) {
			optionData.stream().forEach((item) -> {
				lstAffJobTitleHistoryItems.add(AffJobTitleHistoryItem.createFromJavaType(item.hisId, item.sid, item.jobTitleId, item.note));
			});
		}
		
		if (lstAffJobTitleHistoryItems != null && !lstAffJobTitleHistoryItems.isEmpty()) {
			return lstAffJobTitleHistoryItems;
		}
		return null;
	}

	@Override
	public List<AffJobTitleHistoryItem> getAllByListSidDate(List<String> lstSid, GeneralDate referDate) {
		List<AffJobTitleHistoryItem> data = new ArrayList<>();
		CollectionUtil.split(lstSid, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			try (PreparedStatement statement = this.connection().prepareStatement(
						"SELECT hi.HIST_ID, hi.SID, hi.JOB_TITLE_ID, hi.NOTE from BSYMT_AFF_JOB_HIST_ITEM hi"
						+ " INNER JOIN BSYMT_AFF_JOB_HIST h ON hi.HIST_ID = h.HIST_ID"
                        + " WHERE h.SID IN (" + subList.stream().map(s -> "?").collect(Collectors.joining(",")) + ")"
                        + " AND h.START_DATE <= ? and h.END_DATE >= ? "
                    )) {
                for (int i = 0; i < subList.size(); i++) {
                    statement.setString(i + 1, subList.get(i));
                }
                statement.setDate(subList.size()+1, Date.valueOf(referDate.localDate()));
                statement.setDate(subList.size()+2, Date.valueOf(referDate.localDate()));
				data.addAll(new NtsResultSet(statement.executeQuery()).getList(rec -> {
					return AffJobTitleHistoryItem.createFromJavaType(rec.getString("HIST_ID"), rec.getString("SID"),
																	rec.getString("JOB_TITLE_ID"), rec.getString("NOTE"));
				}));
			}catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		
		return data;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<AffJobTitleHistoryItem> findByHitoryIds(List<String> historyIds) {
		if (historyIds.isEmpty()) {
			return new ArrayList<>();
		}
		
		List<AffJobTitleHistoryItem> results = new ArrayList<>();
		CollectionUtil.split(historyIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subIds -> {
			List<AffJobTitleHistoryItem> subResults = this.queryProxy()
				.query(GET_ALL_BY_HISTID, BsymtAffJobTitleHistItem.class).setParameter("histIds", subIds)
				.getList(ent -> toDomain(ent));
			
			results.addAll(subResults);
		});

		return results;
	}
	
	// request list 515
	@Override
	public List<AffJobTitleHistoryItem> findHistJob(String companyId, GeneralDate baseDate, List<String> jobIds) {
		List<AffJobTitleHistoryItem> resultList = new ArrayList<>();
		CollectionUtil.split(jobIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			
			String sql = "select i.HIST_ID, i.SID, i.JOB_TITLE_ID, i.NOTE"
					+ " from BSYMT_AFF_JOB_HIST_ITEM i"
					+ " inner join BSYMT_AFF_JOB_HIST h"
					+ " on i.HIST_ID = h.HIST_ID"
					+ " where h.CID = ?"
					+ " and h.START_DATE <= ?"
					+ " and h.END_DATE >= ?"
					+ " and i.JOB_TITLE_ID in (" + NtsStatement.In.createParamsString(subList) + ")";
			try (PreparedStatement stmt = this.connection().prepareStatement(sql)) {
				
				stmt.setString(1, companyId);
				stmt.setDate(2, Date.valueOf(baseDate.toLocalDate()));
				stmt.setDate(3, Date.valueOf(baseDate.toLocalDate()));
				for (int i = 0; i < subList.size(); i++) {
					stmt.setString(4 + i, subList.get(i));
				}
				
				resultList.addAll(new NtsResultSet(stmt.executeQuery()).getList(r -> {
					return AffJobTitleHistoryItem.createFromJavaType(
							r.getString("HIST_ID"),
							r.getString("SID"),
							r.getString("JOB_TITLE_ID"),
							r.getString("NOTE"));
				}));
				
				
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
		
		return resultList;
	}

}
