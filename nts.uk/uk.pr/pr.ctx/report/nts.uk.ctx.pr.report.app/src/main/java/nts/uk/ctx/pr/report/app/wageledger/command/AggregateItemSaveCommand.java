/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.command;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.report.app.wageledger.command.dto.ItemSubjectDto;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItem;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemGetMemento;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemName;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubject;

/**
 * The Class AggregateItemSaveCommand.
 */
@Getter
@Setter
public class AggregateItemSaveCommand {
	
	/** The subject. */
	private ItemSubjectDto subject;
	
	/** The name. */
	private String name;
	
	/** The show name zero value. */
	private Boolean showNameZeroValue;
	
	/** The show value zero value. */
	private Boolean showValueZeroValue;
	
	/** The sub items. */
	private List<String> subItems;
	
	/** The is create mode. */
	private boolean isCreateMode;
	
	/**
	 * To domain.
	 *
	 * @param companyCode the company code
	 * @return the WL aggregate item
	 */
	public WLAggregateItem toDomain(String companyCode) {
		return new WLAggregateItem(new AggItemGetMementoImpl(this, companyCode));
	}
	
	/**
	 * The Class AggItemGetMementoImpl.
	 */
	public class AggItemGetMementoImpl implements WLAggregateItemGetMemento {
		
		/** The command. */
		private AggregateItemSaveCommand command;
		
		/** The company code. */
		private String companyCode;
		
		/**
		 * Instantiates a new agg item get memento impl.
		 *
		 * @param command the command
		 * @param companyCode the company code
		 */
		public AggItemGetMementoImpl(AggregateItemSaveCommand command, String companyCode) {
			super();
			this.command = command;
			this.companyCode = companyCode;
		}

		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemGetMemento
		 * #getSubItems()
		 */
		@Override
		public Set<String> getSubItems() {
			return new HashSet<String>(this.command.getSubItems());
		}
		
		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemGetMemento
		 * #getShowValueZeroValue()
		 */
		@Override
		public Boolean getShowValueZeroValue() {
			return this.command.getShowValueZeroValue();
		}
		
		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemGetMemento
		 * #getShowNameZeroValue()
		 */
		@Override
		public Boolean getShowNameZeroValue() {
			return this.command.getShowNameZeroValue();
		}
		
		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemGetMemento
		 * #getName()
		 */
		@Override
		public WLAggregateItemName getName() {
			return new WLAggregateItemName(this.command.getName());
		}

		/* (non-Javadoc)
		 * @see nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemGetMemento#getSubject()
		 */
		@Override
		public WLItemSubject getSubject() {
			return this.command.subject.toDomain(this.companyCode);
		}
	}

}
