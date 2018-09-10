package nts.uk.ctx.bs.employee.infra.repository.jobtitle.affiliate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistory;
import nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.AffJobTitleHistoryRepository;
import nts.uk.ctx.bs.employee.infra.entity.jobtitle.affiliate.BsymtAffJobTitleHist;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaAffJobTitleHistoryRepository extends JpaRepository implements AffJobTitleHistoryRepository {

	private static final String QUERY_GET_AFFJOBTITLEHIST_BYSID = "SELECT jb FROM BsymtAffJobTitleHist jb"
			+ " WHERE jb.sid = :sid and jb.cid = :cid ORDER BY jb.strDate";
	
	private static final String QUERY_GET_AFFJOBTITLEHIST_BYSID_DESC = QUERY_GET_AFFJOBTITLEHIST_BYSID + " DESC";
	
	private static final String GET_BY_SID_DATE = "select h from BsymtAffJobTitleHist h"
			+ " where h.sid = :sid and h.strDate <= :standardDate and h.endDate >= :standardDate";
	
	private static final String GET_BY_LISTSID_DATE = "SELECT h FROM BsymtAffJobTitleHist h"
			+ " where h.sid IN :lstSid AND h.strDate <= :standardDate and h.endDate >= :standardDate";
	
	private static final String GET_BY_LISTSIDS_JOBIDS_DATE = "SELECT h FROM BsymtAffJobTitleHist h LEFT JOIN BsymtAffJobTitleHistItem i ON h.hisId = i.hisId "
			+ " where h.sid IN :lstSid AND i.jobTitleId IN :lstJobTitleId AND h.strDate <= :standardDate and h.endDate >= :standardDate";
	
	private static final String GET_BY_HID_SID = "select h from BsymtAffJobTitleHist h"
			+ " where h.sid = :sid and h.hisId = :hisId";
	
	private static final String GET_BY_LIST_HID_SID = "select h from BsymtAffJobTitleHist h"
			+ " where h.sid IN :sids and h.hisId IN :hisIds";
	
	private static final String GET_BY_EMPIDS_PERIOD = "select h from BsymtAffJobTitleHist h"
			+ " where h.sid IN :lstSid and h.strDate <= :endDate and h.endDate >= :startDate"
			+ " ORDER BY h.sid, h.strDate";
	
	/**
	 * Convert from domain to entity
	 * @param employeeId
	 * @param listHist
	 * @return
	 */
	private AffJobTitleHistory toAffJobTitleHist(List<BsymtAffJobTitleHist> listHist) {
		AffJobTitleHistory domain = new AffJobTitleHistory(listHist.get(0).getCid(), listHist.get(0).getSid(), new ArrayList<>());
		DateHistoryItem dateItem = null;
		for (BsymtAffJobTitleHist item : listHist) {
			dateItem = new DateHistoryItem(item.getHisId(), new DatePeriod(item.getStrDate(), item.getEndDate()));
			domain.getHistoryItems().add(dateItem);
		}
		return domain;
	}

	/**
	 * Convert from domain to BsymtAffJobTitleHist entity
	 * @param sId
	 * @param domain
	 * @return
	 */
	private BsymtAffJobTitleHist toEntity(String cid, String sId, DateHistoryItem domain) {
		return new BsymtAffJobTitleHist(domain.identifier(), sId, cid, domain.start(), domain.end());
	}

	@Override
	public Optional<AffJobTitleHistory> getListBySid(String cid, String sid) {
		List<BsymtAffJobTitleHist> listHist = this.queryProxy()
				.query(QUERY_GET_AFFJOBTITLEHIST_BYSID, BsymtAffJobTitleHist.class)
				.setParameter("cid", cid).setParameter("sid", sid).getList();
		if (listHist != null && !listHist.isEmpty()) {
			return Optional.of(toAffJobTitleHist(listHist));
		}
		return Optional.empty();
	}
	
	@Override
	public Optional<AffJobTitleHistory> getListBySidDesc(String cid, String sid) {
		List<BsymtAffJobTitleHist> listHist = this.queryProxy()
				.query(QUERY_GET_AFFJOBTITLEHIST_BYSID_DESC, BsymtAffJobTitleHist.class)
				.setParameter("cid", cid).setParameter("sid", sid).getList();
		if (listHist != null && !listHist.isEmpty()) {
			return Optional.of(toAffJobTitleHist(listHist));
		}
		return Optional.empty();
	}

	@Override
	public void add(String cid, String sid, DateHistoryItem item) {
		this.commandProxy().insert(toEntity(cid, sid, item));
	}

	@Override
	public void update(DateHistoryItem item) {
		Optional<BsymtAffJobTitleHist> itemToBeUpdated = this.queryProxy().find(item.identifier(),
				BsymtAffJobTitleHist.class);

		if (!itemToBeUpdated.isPresent()) {
			throw new RuntimeException("Invalid BsymtAffJobTitleHist");
		}
		// Update entity
		updateEntity(item, itemToBeUpdated.get());
		this.commandProxy().update(itemToBeUpdated.get());
	}

	@Override
	public void delete(String histId) {
		Optional<BsymtAffJobTitleHist> itemToBeDeleted = this.queryProxy().find(histId, BsymtAffJobTitleHist.class);

		if (!itemToBeDeleted.isPresent()) {
			throw new RuntimeException("Invalid BsymtAffJobTitleHist");
		}
		this.commandProxy().remove(BsymtAffJobTitleHist.class, histId);
	}

	/**
	 * Update entity from domain
	 * @param employeeID
	 * @param item
	 * @return
	 */
	private void updateEntity(DateHistoryItem item, BsymtAffJobTitleHist entity) {
		entity.setStrDate(item.start());
		entity.setEndDate(item.end());
	}

	@Override
	public Optional<AffJobTitleHistory> getByHistoryId(String historyId) {
		Optional<BsymtAffJobTitleHist> optionData = this.queryProxy().find(historyId, BsymtAffJobTitleHist.class);
		if (optionData.isPresent()) {
			return Optional.of(toDomain(optionData.get()));
		}
		return Optional.empty();
	}

	private AffJobTitleHistory toDomain(BsymtAffJobTitleHist ent) {
		AffJobTitleHistory domain = new AffJobTitleHistory(ent.getCid(), ent.getSid(), new ArrayList<>());
		DateHistoryItem dateItem = new DateHistoryItem(ent.getHisId(),
				new DatePeriod(ent.getStrDate(), ent.getEndDate()));

		domain.getHistoryItems().add(dateItem);

		return domain;
	}

	@Override
	public Optional<AffJobTitleHistory> getByEmpIdAndStandardDate(String employeeId, GeneralDate standardDate) {
		Optional<BsymtAffJobTitleHist> optionaData = this.queryProxy()
				.query(GET_BY_SID_DATE, BsymtAffJobTitleHist.class)
				.setParameter("sid", employeeId).setParameter("standardDate", standardDate).getSingle();
		if ( optionaData.isPresent()) {
			return Optional.of(toDomain(optionaData.get()));
		}
		return Optional.empty();
	}

	@Override
	public List<AffJobTitleHistory> getAllBySid(String sid) {
		String cid = AppContexts.user().companyId();
		List<AffJobTitleHistory> lstAffJobTitleHistory = new ArrayList<>();
		List<BsymtAffJobTitleHist> listHist = this.queryProxy()
				.query(QUERY_GET_AFFJOBTITLEHIST_BYSID, BsymtAffJobTitleHist.class)
				.setParameter("cid", cid).setParameter("sid", sid).getList();
		if (listHist != null && !listHist.isEmpty()) {
			for (BsymtAffJobTitleHist item : listHist) {
				AffJobTitleHistory domain = new AffJobTitleHistory(item.getCid(), item.getSid(), new ArrayList<>());
				DateHistoryItem dateItem = null;
				dateItem = new DateHistoryItem(item.getHisId(), new DatePeriod(item.getStrDate(), item.getEndDate()));
				domain.getHistoryItems().add(dateItem);
				lstAffJobTitleHistory.add(domain);
			}
		}
		if (lstAffJobTitleHistory != null && !lstAffJobTitleHistory.isEmpty()) {
			return lstAffJobTitleHistory;
		}
		return null;
	}

	// TODO: HoangDD check lại, không nhất thiết phải truyền cả SID
	@Override
	public Optional<AffJobTitleHistory> getListByHidSid(String hid, String sid) {
		Optional<BsymtAffJobTitleHist> optHist = this.queryProxy()
				.query(GET_BY_HID_SID, BsymtAffJobTitleHist.class)
				.setParameter("hisId", hid).setParameter("sid", sid).getSingle();
		if (optHist.isPresent()) {
			BsymtAffJobTitleHist affJobTitleHist = optHist.get();
			List<BsymtAffJobTitleHist> listHist = new ArrayList<>();
			listHist.add(affJobTitleHist);
			return Optional.of(toAffJobTitleHist(listHist)); 
		}
		return Optional.empty();
	}
	
	@Override
	public List<AffJobTitleHistory> getListByListHidSid(List<String> hids, List<String> sids) {
		if(hids.isEmpty() || sids.isEmpty())
			return Collections.emptyList();
		
		List<BsymtAffJobTitleHist> optHist = this.queryProxy()
				.query(GET_BY_LIST_HID_SID, BsymtAffJobTitleHist.class)
				.setParameter("hisIds", hids).setParameter("sids", sids).getList();
		List<AffJobTitleHistory> listAffJobTitleHistory = new ArrayList<>();
		for(String sid :sids ) {
			List<BsymtAffJobTitleHist> listBsymtAffJobTitleHist = new ArrayList<>();
			for(BsymtAffJobTitleHist bsymtAffJobTitleHist :optHist ) {
				if(sid.equals(bsymtAffJobTitleHist.getSid())) {
					listBsymtAffJobTitleHist.add(bsymtAffJobTitleHist);
				}
			}
			if(!listBsymtAffJobTitleHist.isEmpty()) {
				listAffJobTitleHistory.add(toAffJobTitleHist(listBsymtAffJobTitleHist));
			}
		}
		return listAffJobTitleHistory;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.
	 * AffJobTitleHistoryRepository#searchJobTitleHistory(nts.arc.time.
	 * GeneralDate, java.util.List)
	 */
	@Override
	public List<AffJobTitleHistory> searchJobTitleHistory(GeneralDate baseDate, List<String> employeeIds, List<String> jobTitleIds) {
		// Check conditions
		if (CollectionUtil.isEmpty(employeeIds) || CollectionUtil.isEmpty(jobTitleIds)) {
			return Collections.emptyList();
		}
		
		// Split employee id list.
		List<BsymtAffJobTitleHist> resultList = new ArrayList<>();
		
		CollectionUtil.split(employeeIds, 1000, subList -> {
			CollectionUtil.split(jobTitleIds, 1000, jobSubList -> {
				resultList.addAll(this.queryProxy()
						.query(GET_BY_LISTSIDS_JOBIDS_DATE, BsymtAffJobTitleHist.class)
						.setParameter("lstSid", subList).setParameter("lstJobTitleId", jobSubList)
						.setParameter("standardDate", baseDate).getList());
			});
		});
			
		// Return
		return resultList.stream().map(entity -> this.toDomain(entity)).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.bs.employee.dom.jobtitle.affiliate.
	 * AffJobTitleHistoryRepository#findAllJobTitleHistory(nts.arc.time.
	 * GeneralDate, java.util.List)
	 */
	@Override
	public List<AffJobTitleHistory> findAllJobTitleHistory(GeneralDate baseDate, List<String> employeeIds) {
		if (CollectionUtil.isEmpty(employeeIds)) {
			return new ArrayList<>();
		}
		
		// Split employee id list.
		List<BsymtAffJobTitleHist> resultList = new ArrayList<>();
		CollectionUtil.split(employeeIds, 1000, subList -> {
			resultList.addAll(this.queryProxy().query(GET_BY_LISTSID_DATE, BsymtAffJobTitleHist.class)
					.setParameter("lstSid", subList).setParameter("standardDate", baseDate).getList());
		});
		return resultList.stream().map(entity -> this.toDomain(entity)).collect(Collectors.toList());
	}
	
	@Override
	public List<AffJobTitleHistory> getByEmployeeListPeriod(List<String> employeeIds, DatePeriod period) {
		Map<String, List<BsymtAffJobTitleHist>> entitiesByEmployee = this.queryProxy()
				.query(GET_BY_EMPIDS_PERIOD, BsymtAffJobTitleHist.class).setParameter("lstSid", employeeIds)
				.setParameter("startDate", period.start()).setParameter("endDate", period.end()).getList().stream()
				.collect(Collectors.groupingBy(x -> x.sid));

		String companyId = AppContexts.user().companyId();

		List<AffJobTitleHistory> resultList = new ArrayList<>();

		entitiesByEmployee.forEach((employeeId, entitiesOfEmp) -> {
			List<DateHistoryItem> historyItems = convertToDateHistoryItem(entitiesOfEmp);
			resultList.add(new AffJobTitleHistory(companyId, employeeId, historyItems));
		});
		return resultList;
	}
	
	private List<DateHistoryItem> convertToDateHistoryItem(List<BsymtAffJobTitleHist> entities) {
		return entities.stream()
				.map(ent -> new DateHistoryItem(ent.bsymtAffJobTitleHistItem.hisId,
						new DatePeriod(ent.strDate, ent.endDate)))
				.collect(Collectors.toList());
	}



}
