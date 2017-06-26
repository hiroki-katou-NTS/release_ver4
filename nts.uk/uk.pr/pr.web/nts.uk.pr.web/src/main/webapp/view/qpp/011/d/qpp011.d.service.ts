module qpp011.d.service {
    var paths = {
        find: "pr/core/rule/law/tax/residential/output/find",
        add: "pr/core/rule/law/tax/residential/output/add",
        update: "pr/core/rule/law/tax/residential/output/update",
        findallresidential: "pr/core/residential/findallresidential",
        getlistLocation: "pr/core/residential/getlistLocation",
    }

    /**
     * Get list payment date processing.
     */
    export function findresidentialTax(resimentTaxCode: string, yearMonth: string): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.find + "/" + resimentTaxCode + "/" + yearMonth)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    export function getlistLocation(): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.getlistLocation)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    export function findAllResidential(): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.findallresidential)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    export function add(residentialTax: any): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.add, residentialTax)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export function update(residentialTax:any): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.update, residentialTax)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    export module model {
           export class residentialTax {
            resimentTaxCode: KnockoutObservable<string>;
            yearMonth: KnockoutObservable<number>;
            taxPayRollMoney: KnockoutObservable<number>;
            taxBonusMoney: KnockoutObservable<number>;
            taxOverDueMoney: KnockoutObservable<number>;
            taxDemandChargeMoyney: KnockoutObservable<number>;
            address: KnockoutObservable<string>;
            dueDate: KnockoutObservable<Date>;
            headcount: KnockoutObservable<number>;
            retirementBonusAmout: KnockoutObservable<number>;
            cityTaxMoney: KnockoutObservable<number>;
            prefectureTaxMoney: KnockoutObservable<number>;
            constructor(resimentTaxCode: string, yearMonth: number,taxPayRollMoney: number, taxBonusMoney: number, taxOverDueMoney: number, taxDemandChargeMoyney: number, address: string, dueDate: Date, headcount: number, retirementBonusAmout: number, cityTaxMoney: number, prefectureTaxMoney: number) {
                this.resimentTaxCode = ko.observable(resimentTaxCode);
                this.yearMonth = ko.observable(yearMonth);
                this.taxPayRollMoney = ko.observable(taxPayRollMoney);
                this.taxBonusMoney = ko.observable(taxBonusMoney);
                this.taxOverDueMoney = ko.observable(taxOverDueMoney);
                this.taxDemandChargeMoyney = ko.observable(taxDemandChargeMoyney);
                this.address = ko.observable(address);
                this.dueDate = ko.observable(dueDate);
                this.headcount = ko.observable(headcount);
                this.retirementBonusAmout = ko.observable(retirementBonusAmout);
                this.cityTaxMoney = ko.observable(cityTaxMoney);
                this.prefectureTaxMoney = ko.observable(prefectureTaxMoney);
            }     
    }
    }


}