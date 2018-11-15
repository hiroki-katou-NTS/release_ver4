module nts.uk.pr.view.qmm019.e {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;

    export module service {
        var paths = {
            getStatementItem: "core/wageprovision/statementlayout/getStatementItem",
            getDeductionItemStById: "core/wageprovision/statementlayout/getDeductionItemStById/{0}/{1}",
            getAllBreakdownItemSetById: "ctx/pr/core/breakdownItem/getAllBreakdownItemSetById/{0}/{1}",
            getSalIndAmountNameById: "core/wageprovision/statementlayout/getSalIndAmountNameById/{0}/{1}",
            getFormulaById: "core/wageprovision/statementlayout/getFormulaById/{0}",
            getWageTableById: "core/wageprovision/statementlayout/getWageTableById/{0}",
        }

        export function getStatementItem(dataDto: any): JQueryPromise<any> {
            return ajax('pr', paths.getStatementItem, dataDto);
        }

        export function getDeductionItemStById(categoryAtr: number, itemNameCode: string): JQueryPromise<any> {
            let _path = format(paths.getDeductionItemStById, categoryAtr, itemNameCode);
            return ajax('pr', _path);
        }

        export function getAllBreakdownItemSetById(categoryAtr: number, itemNameCode: string): JQueryPromise<any> {
            let _path = format(paths.getAllBreakdownItemSetById, categoryAtr, itemNameCode);
            return ajax('pr', _path);
        }

        export function getSalIndAmountNameById(individualPriceCode: string, cateIndicator: number): JQueryPromise<any> {
            let _path = format(paths.getSalIndAmountNameById, individualPriceCode, cateIndicator);
            return ajax('pr', _path);
        }

        export function getFormulaById(formulaCode: string): JQueryPromise<any> {
            let _path = format(paths.getFormulaById, formulaCode);
            return ajax('pr', _path);
        }

        export function getWageTableById(wageTableCode: string): JQueryPromise<any> {
            let _path = format(paths.getWageTableById, wageTableCode);
            return ajax('pr', _path);
        }
    }
}