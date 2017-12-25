///******************************************************************
// * Copyright (c) 2017 Nittsu System to present.                   *
// * All right reserved.                                            *
// *****************************************************************/
//package nts.uk.ctx.at.shared.infra.repository.worktime.common;
//
//import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
//import nts.uk.ctx.at.shared.dom.worktime.common.FlowRestSettingSetMemento;
//import nts.uk.ctx.at.shared.infra.entity.worktime.flexset.KshmtFlexHaRestTime;
//
///**
// * The Class JpaFlexOffdayFlowRestSettingSetMemento.
// */
//public class JpaFlexHAFlowRestSettingSetMemento implements FlowRestSettingSetMemento{
//	
//	/** The entity. */
//	private KshmtFlexHaRestTime entity;
//
//	/**
//	 * Instantiates a new jpa flex offday flow rest setting set memento.
//	 *
//	 * @param entity the entity
//	 */
//	public JpaFlexHAFlowRestSettingSetMemento(KshmtFlexHaRestTime entity) {
//		super();
//		this.entity = entity;
//	}
//
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowRestSettingSetMemento#
//	 * setFlowRestTime(nts.uk.ctx.at.shared.dom.common.time.AttendanceTime)
//	 */
//	@Override
//	public void setFlowRestTime(AttendanceTime time) {
//		this.entity.setAfterRestTime(time.valueAsMinutes());
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see nts.uk.ctx.at.shared.dom.worktime.common.FlowRestSettingSetMemento#
//	 * setFlowPassageTime(nts.uk.ctx.at.shared.dom.common.time.AttendanceTime)
//	 */
//	@Override
//	public void setFlowPassageTime(AttendanceTime time) {
//		this.entity.setAfterPassageTime(time.valueAsMinutes());
//	}
//
//	
//}
