module qmm003.e.service {
    var paths = {
        getResidentalTaxList: "pr/core/residential/findallresidential",
        getRegionPrefecture: "pr/core/residential/getlistLocation",
        getResidentalTaxListByReportCode: "pr/core/residential/findallresidential1/{0}",
        updateReportCode: "pr/core/residential/updatereportCode",
        update: "pr/core/rule/law/tax/residential/input/update2",
        getPersionResidentalTax: "pr/core/rule/law/tax/residential/input/findAll/{0}/{1}"
    }

    //get person residential Tax
    export function getPersonResidentialTax(yearKey: number, residenceCode: string):
        JQueryPromise<Array<any>> {
        var dfd = $.Deferred<any>();
        var _path = nts.uk.text.format(paths.getPersionResidentalTax, yearKey, residenceCode);
        nts.uk.request.ajax(_path)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();

    }

    /**
     * Get list ResidentialTax.
     */
    export function getResidentialTax(): JQueryPromise<Array<model.ResidentialTax>> {
        var dfd = $.Deferred<Array<qmm003.a.service.model.ResidentialTaxDetailDto>>();
        nts.uk.request.ajax(paths.getResidentalTaxList)
            .done(function(res: Array<qmm003.a.service.model.ResidentialTaxDetailDto>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /**
     * Get list ResidentialTax.
     */
    export function getResidentalTaxListByReportCode(resiTaxReportCode: string): JQueryPromise<Array<model.ResidentialTax>> {
        var dfd = $.Deferred<Array<model.ResidentialTax>>();
        var _path = nts.uk.text.format(paths.getResidentalTaxListByReportCode, resiTaxReportCode);
        nts.uk.request.ajax(_path)
            .done(function(res: Array<model.ResidentialTax>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }


    export function getRegionPrefecture(): JQueryPromise<Array<model.RegionObject>> {
        var dfd = $.Deferred<Array<model.RegionObject>>();
        nts.uk.request.ajax(paths.getRegionPrefecture)
            .done(function(res: Array<model.RegionObject>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /**
    * update ReportCode
    */
    export function updateReportCode(resiTaxCode: string, resiTaxReportCode: string) {
        let dfd = $.Deferred<Array<any>>();
        var residential = {
            resiTaxCode: resiTaxCode,
            resiTaxReportCode: resiTaxReportCode
        };
        nts.uk.request.ajax(paths.updateReportCode, residential)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    //UPD-2
    /**
* update ReportCode
*/
    export function updateResidenceCode(residenceCode: string, personId: string, yearKey: number) {
        let dfd = $.Deferred<any>();
        var obj = {
            residenceCode: residenceCode,
            yearKey: yearKey,
            personId: personId
        };
        nts.uk.request.ajax(paths.update, obj)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export function updateAllReportCode(param: Array<string>, resiTaxReportCode: string, yearKey: number) {
        let dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.updateReportCode, { resiTaxCodes: param, resiTaxReportCode: resiTaxReportCode, yearKey: yearKey })
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }


    export module model {
        export class ResidentialTax {
            companyCode: string;
            resiTaxCode: string;
            resiTaxAutonomy: string;
            resiTaxAutonomyKana: string;
            prefectureCode: string;
            resiTaxReportCode: string;
            registeredName: string;
            companyAccountNo: string;
            companySpecifiedNo: string;
            cordinatePostalCode: string;
            cordinatePostOffice: string;
            memo: string;
            contructor(companyCode: string, resiTaxCode: string, resiTaxAutonomy: string,
                resiTaxAutonomyKana: string,
                prefectureCode: string, resiTaxReportCode: string,
                registeredName: string, companyAccountNo: string, companySpecifiedNo: string,
                cordinatePostalCode: string, cordinatePostOffice: string, memo: string) {
                this.companyCode = companyCode;
                this.resiTaxCode = resiTaxCode;
                this.resiTaxAutonomy = resiTaxAutonomy;
                this.resiTaxAutonomyKana = resiTaxAutonomyKana;
                this.prefectureCode = prefectureCode;
                this.resiTaxReportCode = resiTaxReportCode;
                this.registeredName = registeredName;
                this.companyAccountNo = companyAccountNo;
                this.companySpecifiedNo = companySpecifiedNo;
                this.cordinatePostalCode = cordinatePostalCode;
                this.cordinatePostOffice = cordinatePostOffice;
                this.memo = memo;
            }
        }
        export class RegionObject {
            regionCode: string;
            regionName: string;
            prefectures: Array<PrefectureObject>;
            contructor(regionCode: string, regionName: string, prefectures: Array<PrefectureObject>) {
                this.regionCode = regionCode;
                this.regionName = regionName;
                this.prefectures = prefectures;
            }
        }

        export class PrefectureObject {
            prefectureCode: string;
            prefectureName: string;
            contructor(prefectureCode: string, prefectureName: string) {
                this.prefectureCode = prefectureCode;
                this.prefectureName = prefectureName;
            }

        }

    }

}