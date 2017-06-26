package nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth;

import java.math.BigDecimal;
import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.payday.Payday;

public interface PaydayRepository {

	BigDecimal select1(String companyCode, int processingNo, int payBonusAtr, int processingYm, int sparePayAtr);

	Payday select3(String companyCode, int processingNo, int payBonusAtr, int processingYm, int sparePayAtr);

	List<Payday> select2(String companyCode, int payBonusAtr, int processingYm, int sparePayAtr);

	List<Payday> select4(String companyCode, int processingNo);

	List<Payday> select5(String companyCode, int processingNo, int payBonusAtr);

	List<Payday> select6(String companyCode, int processingNo, int processingYm);

	List<Payday> select7(String companyCode, int processingNo, int payBonusAtr, GeneralDate strYmd, GeneralDate endYmd);

	List<Payday> select11(String companyCode, int processingNo, int payBonusAtr, GeneralDate strYmd, GeneralDate endYmd);
	
	List<Payday> select12(String companyCode, int payBonusAtr, int sparePayAtr);

	void insert1(Payday domain);
	
	void update1(Payday domain);
	
	void delete1(Payday domain);
}
