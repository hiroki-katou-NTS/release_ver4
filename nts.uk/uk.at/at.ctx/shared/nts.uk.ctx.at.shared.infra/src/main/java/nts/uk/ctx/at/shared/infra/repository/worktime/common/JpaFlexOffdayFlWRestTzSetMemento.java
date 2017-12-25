///******************************************************************
// * Copyright (c) 2017 Nittsu System to present.                   *
// * All right reserved.                                            *
// *****************************************************************/
//package nts.uk.ctx.at.shared.infra.repository.worktime.common;
//
//import java.util.List;
//
//import nts.uk.ctx.at.shared.dom.worktime.common.BooleanGetAtr;
//import nts.uk.ctx.at.shared.dom.worktime.common.FlowRestTimezone;
//import nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezoneSetMemento;
//import nts.uk.ctx.at.shared.dom.worktime.common.TimezoneOfFixedRestTimeSet;
//import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexHaFixRest;
//import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexHaRestTime;
//import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexOdRestSet;
//import nts.uk.ctx.at.shared.infra.repository.worktime.common.JpaFlexOffdayTzOFRTimeSetSetMemento;
//
///**
// * The Class JpaFlexOffdayFlWRestTzSetMemento.
// */
//public class JpaFlexOffdayFlWRestTzSetMemento implements FlowWorkRestTimezoneSetMemento{
//	
//	/** The entity. */
//	private KshmtFlexHaRestTime entity;
//	
//	/** The entity fixed rests. */
//	List<KshmtFlexHaFixRest> entityFixedRests;
//	
//	/** The entity flow rests. */
//	List<KshmtFlexOdRestSet> entityFlowRests;
//	
//
//	/**
//	 * Instantiates a new jpa flex offday fl W rest tz set memento.
//	 *
//	 * @param entity the entity
//	 * @param entityFixedRests the entity fixed rests
//	 * @param entityFlowRests the entity flow rests
//	 */
//	public JpaFlexOffdayFlWRestTzSetMemento(KshmtFlexHaRestTime entity, List<KshmtFlexHaFixRest> entityFixedRests,
//			List<KshmtFlexOdRestSet> entityFlowRests) {
//		super();
//		this.entity = entity;
//		this.entityFixedRests = entityFixedRests;
//		this.entityFlowRests = entityFlowRests;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezoneSetMemento#
//	 * setFixRestTime(boolean)
//	 */
//	@Override
//	public void setFixRestTime(boolean fixRestTime) {
//		this.entity.setFixRestTime(BooleanGetAtr.getAtrByBoolean(fixRestTime));
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezoneSetMemento#
//	 * setFixedRestTimezone(nts.uk.ctx.at.shared.dom.worktime.common.
//	 * TimezoneOfFixedRestTimeSet)
//	 */
//	@Override
//	public void setFixedRestTimezone(TimezoneOfFixedRestTimeSet fixedRestTimezone) {
//		fixedRestTimezone.saveToMemento(new JpaFlexOffdayTzOFRTimeSetSetMemento(this.entityFixedRests));
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * nts.uk.ctx.at.shared.dom.worktime.common.FlowWorkRestTimezoneSetMemento#
//	 * setFlowRestTimezone(nts.uk.ctx.at.shared.dom.worktime.common.
//	 * FlowRestTimezone)
//	 */
//	@Override
//	public void setFlowRestTimezone(FlowRestTimezone flowRestTimezone) {
//		flowRestTimezone.saveToMemento(new JpaFlexOffdayFlowRestTzSetMemento(this.entity, this.entityFlowRests));
//	}
//
//}
