/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.shortworktime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistSetMemento;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHist;
import nts.uk.ctx.at.shared.infra.entity.shortworktime.BshmtWorktimeHistPK;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * The Class JpaSWorkTimeHistSetMemento.
 */
public class JpaSWorkTimeHistSetMemento implements SWorkTimeHistSetMemento {

	/** The company id. */
	private String companyId;

	/** The entity. */
	private List<BshmtWorktimeHist> entities;

	/**
	 * Instantiates a new jpa S work time hist set memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaSWorkTimeHistSetMemento(String companyId, List<BshmtWorktimeHist> entities) {
		this.companyId = companyId;
		entities.stream().forEach(item -> {
			if (item.getBshmtWorktimeHistPK() == null) {
				item.setBshmtWorktimeHistPK(new BshmtWorktimeHistPK());
			}
		});
		this.entities = entities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistSetMemento#
	 * setEmployeeId(java.lang.String)
	 */
	@Override
	public void setEmployeeId(String empId) {
		this.entities.stream().forEach(item -> {
			BshmtWorktimeHistPK bshmtWorktimeHistPK = item.getBshmtWorktimeHistPK();
			bshmtWorktimeHistPK.setSid(empId);
			item.setBshmtWorktimeHistPK(bshmtWorktimeHistPK);
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistSetMemento#
	 * setHistoryItem(nts.uk.shr.com.history.DateHistoryItem)
	 */
	@Override
	public void setHistoryItems(List<DateHistoryItem> historyItems) {
		List<String> histIds = historyItems.stream().map(DateHistoryItem::identifier)
				.collect(Collectors.toList());

		Map<String, DatePeriod> mapHistoryItems = historyItems.stream()
				.collect(Collectors.toMap(DateHistoryItem::identifier, DateHistoryItem::span));

		// Remove not save entities
		this.entities = this.entities.stream()
				.filter(item -> histIds.contains(item.getBshmtWorktimeHistPK().getHistId()))
				.collect(Collectors.toList());

		List<String> entityHistIds = new ArrayList<>();

		this.entities.stream().forEach(item -> {
			BshmtWorktimeHistPK kshmtWorkingCondPK = item.getBshmtWorktimeHistPK();
			histIds.add(kshmtWorkingCondPK.getHistId());
			if (mapHistoryItems.keySet().contains(kshmtWorkingCondPK.getHistId())) {
				item.setStrYmd(mapHistoryItems.get(kshmtWorkingCondPK.getHistId()).start());
				item.setEndYmd(mapHistoryItems.get(kshmtWorkingCondPK.getHistId()).end());
			}
		});

		historyItems.stream().forEach(item -> {
			if (!entityHistIds.contains(item.identifier())) {
				BshmtWorktimeHist entity = new BshmtWorktimeHist();
				BshmtWorktimeHistPK kshmtWorkingCondPK = new BshmtWorktimeHistPK(companyId,
						item.identifier());
				entity.setBshmtWorktimeHistPK(kshmtWorkingCondPK);
				entity.setCId(companyId);
				entity.setStrYmd(item.start());
				entity.setEndYmd(item.end());
				this.entities.add(entity);
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.shared.dom.shortworktime.SWorkTimeHistSetMemento#
	 * setCompanyId(java.lang.String)
	 */
	@Override
	public void setCompanyId(String cid) {
		this.entities.stream().forEach(item -> {
			item.setCId(cid);
		});
	}

}
