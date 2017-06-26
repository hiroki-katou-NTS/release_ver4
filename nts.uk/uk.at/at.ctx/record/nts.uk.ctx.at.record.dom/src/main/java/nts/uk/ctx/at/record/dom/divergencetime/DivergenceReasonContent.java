package nts.uk.ctx.at.record.dom.divergencetime;

import nts.arc.primitive.StringPrimitiveValue;
import nts.arc.primitive.constraint.StringMaxLength;

@StringMaxLength(60)
public class DivergenceReasonContent extends StringPrimitiveValue<DivergenceReasonContent> {

		/** serialVersionUID */
		private static final long serialVersionUID = 1L;

		public DivergenceReasonContent(String rawValue) {
			super(rawValue);
		}

	}