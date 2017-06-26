/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.infra.repository.insurance;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSetting;
import nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingRepository;
import nts.uk.ctx.pr.report.infra.entity.insurance.QismtChecklistPrintSet;

/**
 * The Class JpaChecklistPrintSettingRepository.
 */
@Stateless
public class JpaChecklistPrintSettingRepository extends JpaRepository implements ChecklistPrintSettingRepository {

	/*
	 * (non-Javadoc)
	 * @see
	 * nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingRepository#create
	 * (nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSetting)
	 */
	@Override
	public void create(ChecklistPrintSetting printSetting) {
		QismtChecklistPrintSet entity = new QismtChecklistPrintSet();

		// Convert to entity.
		printSetting.saveToMemento(new JpaChecklistPrintSettingSetMemento(entity));

		// insert to DB.
		this.commandProxy().insert(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingRepository#update
	 * (nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSetting)
	 */
	@Override
	public void update(ChecklistPrintSetting printSetting) {
		Optional<QismtChecklistPrintSet> entityOptional = this.queryProxy().find(printSetting.getCompanyCode(),
				QismtChecklistPrintSet.class);

		// Check entity optional.
		if (entityOptional.isPresent()) {
			QismtChecklistPrintSet entity = entityOptional.get();
			printSetting.saveToMemento(new JpaChecklistPrintSettingSetMemento(entity));
			this.commandProxy().insert(entity);
			return;
		}
		throw new RuntimeException("Can not update entity not exist!");
	}

	/*
	 * (non-Javadoc)
	 * @see nts.uk.ctx.pr.report.dom.insurance.ChecklistPrintSettingRepository#
	 * findByCompanyCode(java.lang.String)
	 */
	@Override
	public Optional<ChecklistPrintSetting> findByCompanyCode(String companyCode) {
		Optional<QismtChecklistPrintSet> entityOptional = this.queryProxy().find(companyCode,
				QismtChecklistPrintSet.class);

		// Check entity optional.
		ChecklistPrintSetting printSetting = null;
		if (entityOptional.isPresent()) {
			QismtChecklistPrintSet entity = entityOptional.get();
			printSetting = new ChecklistPrintSetting(new JpaChecklistPrintSettingGetMemento(entity));
		}

		// Return optional.
		return Optional.ofNullable(printSetting);
	}

}
