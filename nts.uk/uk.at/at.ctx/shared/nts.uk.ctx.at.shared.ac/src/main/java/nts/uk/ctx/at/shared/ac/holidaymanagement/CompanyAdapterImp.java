package nts.uk.ctx.at.shared.ac.holidaymanagement;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyAdapter;
import nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyDto;
import nts.uk.ctx.bs.company.pub.company.BeginOfMonthExport;
import nts.uk.ctx.bs.company.pub.company.ICompanyPub;

/**
 * The Class CompanyAdapterImp.
 */
@Stateless
public class CompanyAdapterImp implements CompanyAdapter{

	/** The company pub. */
	@Inject ICompanyPub companyPub;
	
	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.shared.dom.adapter.holidaymanagement.CompanyAdapter#getFirstMonth(java.lang.String)
	 */
	@Override
	public CompanyDto getFirstMonth(String companyId) {
		BeginOfMonthExport beginOfMonthExport = companyPub.getBeginOfMonth(companyId);
		CompanyDto companyDto = new CompanyDto();
		companyDto.setStartMonth(beginOfMonthExport.getStartMonth());
		return companyDto;
	}

	/** 暦上の年月を渡して、年度に沿った年月を取得する */
	@Override
	public YearMonth getYearMonthFromCalenderYM(String companyId, YearMonth yearMonth) {
		
		// 「会社情報」を取得する　→　期首月
		CompanyDto companyDto = this.getFirstMonth(companyId);
		int startMonth = companyDto.getStartMonth();
		
		// 年度　←　年月.年
		int year = yearMonth.year();
		
		// 年月.月と期首月を比較する
		if (yearMonth.month() < startMonth) year--;
		
		// 取得した年度＋年月.月を返す
		return YearMonth.of(year, yearMonth.month()); 
	}
}
