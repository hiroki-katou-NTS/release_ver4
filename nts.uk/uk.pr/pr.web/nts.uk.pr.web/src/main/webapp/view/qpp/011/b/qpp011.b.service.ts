module qpp011.b.service {
    var paths = {
        find: "pr/core/rule/law/tax/residential/output/find",
        add: "pr/core/rule/law/tax/residential/output/add",
        update: "pr/core/rule/law/tax/residential/output/update",
        findAllResidential: "pr/core/residential/findallresidential",
        findAllLinebank: "basic/system/bank/linebank/findAll",
        findBankAll: "basic/system/bank/find/all",
        findLinebank: "basic/system/bank/linebank/find",
        getlistLocation: "pr/core/residential/getlistLocation",
        currentProcessingNo: "pr/proto/paymentdatemaster/processing/findbylogin",
        findallRegalDoc: "pr/core/rule/law/tax/residential/output/findallRegalDoc",
        saveAsPdf: "screen/pr/QPP011/saveAsPdf",
        saveText: "screen/pr/QPP011/savePaymentData"
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

    export function findallRegalDoc(): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.findallRegalDoc)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    export function getCurrentProcessingNo(): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.currentProcessingNo)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    export function findLinebank(lineBankCode: string): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax("com", paths.findLinebank + "/" + lineBankCode)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    export function findAllResidential(): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.findAllResidential)
            .done(function(res: Array<any>) {
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
    export function findAllLinebank(): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax("com", paths.findAllLinebank)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    export function findBankAll(): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax("com", paths.findBankAll)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    export function add(residentialTax: model.residentialTax): JQueryPromise<any> {
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

    export function update(residentialTax: model.residentialTax): JQueryPromise<any> {
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

    export function saveAsPdf(command: any): JQueryPromise<any> {
        return nts.uk.request.exportFile(paths.saveAsPdf, command);
    }
    
    export function saveText(command: any): JQueryPromise<any> {
        return nts.uk.request.exportFile(paths.saveText, command);
    }

    export module model {
        export class residentialTax {
            resimentTaxCode: string;

            yearMonth: number;

            taxBonusMoney: number;

            taxOverDueMoney: number;

            taxDemandChargeMoyney: number;

            address: string;

            dueDate: number;

            headcount: number;

            retirementBonusAmout: number;

            cityTaxMoney: number;

            prefectureTaxMoney: number;

            constructor(resimentTaxCode: string, yearMonth: number, taxBonusMoney: number, taxOverDueMoney: number, taxDemandChargeMoyney: number, address: string, dueDate: number, headcount: number, retirementBonusAmout: number, cityTaxMoney: number, prefectureTaxMoney: number) {
                this.resimentTaxCode = resimentTaxCode;
                this.yearMonth = yearMonth;
                this.taxBonusMoney = taxBonusMoney;

                this.taxOverDueMoney = taxOverDueMoney;

                this.taxDemandChargeMoyney = taxDemandChargeMoyney;

                this.address = address;

                this.dueDate = dueDate;

                this.headcount = headcount;

                this.retirementBonusAmout = retirementBonusAmout;

                this.cityTaxMoney = cityTaxMoney;

                this.prefectureTaxMoney = prefectureTaxMoney;
            }
        }


    }


}