module nts.uk.pr.view.qmm017.l {
    export module service {
        var paths = {
            getListCompanyUnitPrice: "pr/proto/unitprice/findbymonth/",
            getListPersonalUnitPrice: "pr/core/rule/employment/unitprice/personal/find/all",
            getListItemMaster: "pr/core/item/findall/category/"
        }

        export function getListCompanyUnitPrice(baseDate): JQueryPromise<Array<model.CompanyUnitPriceDto>> {
            var dfd = $.Deferred<Array<model.CompanyUnitPriceDto>>();
            nts.uk.request.ajax("pr", paths.getListCompanyUnitPrice + baseDate)
                .done(function(res: Array<model.CompanyUnitPriceDto>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        export function getListPersonalUnitPrice(): JQueryPromise<Array<model.PersonalUnitPriceDto>> {
            var dfd = $.Deferred<Array<model.PersonalUnitPriceDto>>();
            nts.uk.request.ajax("pr", paths.getListPersonalUnitPrice)
                .done(function(res: Array<model.PersonalUnitPriceDto>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        export function getListItemMaster(categoryAtr): JQueryPromise<Array<model.ItemMasterDto>> {
            var dfd = $.Deferred<Array<model.ItemMasterDto>>();
            nts.uk.request.ajax("pr", paths.getListItemMaster + categoryAtr)
                .done(function(res: Array<model.ItemMasterDto>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
    }

    export module model {
        export class CompanyUnitPriceDto {
            unitPriceCode: string;
            unitPriceName: string;
        }

        export class PersonalUnitPriceDto {
            personalUnitPriceCode: string;
            personalUnitPriceName: string;
        }

        export class ItemMasterDto {
            itemCode: string;
            itemName: string;
        }
    }
}