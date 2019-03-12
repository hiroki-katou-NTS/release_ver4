package nts.uk.ctx.pr.shared.app.find.payrollgeneralpurposeparameters;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.shared.dom.salgenpurposeparam.SalGenParaValueRepository;
import nts.uk.ctx.pr.shared.dom.salgenpurposeparam.SalGenParaYMHistRepository;

@Stateless
/**
* 給与汎用パラメータ値: Finder
*/
public class SalGenParaValueFinder
{

    @Inject
    private SalGenParaYMHistRepository finder;

    public SalGenParaValueDto getAllSalGenParaValue(String hisId){



        return SalGenParaValueDto.fromDomain(finder.getSalGenParaValueById(hisId).get());
    }

}
