package nts.uk.query.pub.exo.condset;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public interface ServerExternalOutputPub {

	ServerExternalOutputExport findExternalOutput(String cid, String conditionCd);

	void processAutoExecution(String conditionCd, DatePeriod period, GeneralDate baseDate,
			Integer categoryId);
}
