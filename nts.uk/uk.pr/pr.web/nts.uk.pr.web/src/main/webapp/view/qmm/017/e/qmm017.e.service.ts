module nts.uk.pr.view.qmm017.e.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    import standardAmountClsEnum = nts.uk.pr.view.qmm017.share.model.STANDARD_AMOUNT_CLS

    let paths = {
        getAllStatementItemData: "ctx/pr/core/wageprovision/statementitem/getAllStatementItemData/{0}/{1}",
        getAllPayrollUnitPriceByCID: "core/wageprovision/companyuniformamount/getAllPayrollUnitPriceByCID",
        getAllUnitPriceName: "ctx/pr/core/wageprovision/unitpricename/getAllUnitPriceName/{0}"
    }

    export function getTargetItemCodeList (standardAmountCls: number) : JQueryPromise<any> {
        if (standardAmountCls == standardAmountClsEnum.DEDUCTION_ITEM || standardAmountCls == standardAmountClsEnum.PAYMENT_ITEM){
            // PAYMENT_ITEM(0, "支給項目")
            return getAllStatementItemData(0, false);
        }
        if (standardAmountCls == standardAmountClsEnum.INDIVIDUAL_UNIT_PRICE_ITEM) return getAllUnitPriceName(false);
        return getAllPayrollUnitPrice();
    }

    export function getAllStatementItemData(categoryAtr: number, isdisplayAbolition: boolean): JQueryPromise<any> {
        let _path = format(paths.getAllStatementItemData, categoryAtr, isdisplayAbolition);
        return ajax('pr', _path);
    }

    export function getAllPayrollUnitPrice() : JQueryPromise<any> {
        return nts.uk.request.ajax(paths.getAllPayrollUnitPriceByCID);
    }

    export function getAllUnitPriceName(isdisplayAbolition: boolean) : JQueryPromise<any> {
        let _path = nts.uk.text.format(paths.getAllUnitPriceName, isdisplayAbolition);
        return nts.uk.request.ajax("pr", _path);
    }
}
