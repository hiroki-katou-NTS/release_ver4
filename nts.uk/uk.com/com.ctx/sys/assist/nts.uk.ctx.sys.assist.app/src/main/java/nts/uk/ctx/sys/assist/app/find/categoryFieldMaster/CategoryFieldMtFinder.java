package nts.uk.ctx.sys.assist.app.find.categoryFieldMaster;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.sys.assist.dom.categoryFieldMaster.CategoryFieldMtRepository;
@Stateless
/**
* カテゴリ項目マスタ
*/
public class CategoryFieldMtFinder
{

    @Inject
    private CategoryFieldMtRepository finder;

    

}
