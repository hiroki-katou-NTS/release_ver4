module qmm003.a.service {
    var paths = {
        getResidentialTaxList: "pr/core/residential/findallresidential",
        getRegionPrefecture: "pr/core/residential/getlistLocation",
        getResidentialDetail: "pr/core/residential/findResidentialTax/{0}",
        getResidentalTaxList: "pr/core/residential/findallByCompanyCode",
        addResidential: "pr/core/residential/addresidential",
        updateResidential: "pr/core/residential/updateresidential",
        updateReportCode: "pr/core/residential/updatereportCode",
        deleteResidential: "pr/core/residential/deleteresidential"
    }

    /**
     * Get list residential companyCode !=0000.
     */
    export function getResidentialTax(): JQueryPromise<Array<model.ResidentialTaxDto>> {
        var dfd = $.Deferred<Array<model.ResidentialTaxDto>>();
        nts.uk.request.ajax(paths.getResidentialTaxList)
            .done(function(res: Array<model.ResidentialTaxDto>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /**
 * Get list  ResidentialTax companyCode == 0000.
 */
    export function getResidentialTaxCCD(): JQueryPromise<Array<model.ResidentialTaxDto>> {
        var dfd = $.Deferred<Array<model.ResidentialTaxDto>>();
        nts.uk.request.ajax(paths.getResidentalTaxList)
            .done(function(res: Array<model.ResidentialTaxDto>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    /**
     * get japan location data
     */
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
     * add  Residential
     */

    export function addResidential(residential: model.ResidentialTaxDetailDto) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.addResidential, residential).done(function(res: Array<any>) {
            dfd.resolve(res);
        })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /**
     * update ResidentialTax
     */
    export function updateData(residential: model.ResidentialTaxDetailDto) {
        let dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.updateResidential, residential)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /**
     * delete Residential
     */
    export function deleteResidential(param: Array<string>) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.deleteResidential, { resiTaxCodes: param }).done(function(res: Array<any>) {
            dfd.resolve(res);
        })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    /**
     * get ResidentialTax Detail
     */
    export function getResidentialTaxDetail(resiTaxCode: string): JQueryPromise<model.ResidentialTaxDetailDto> {
        var dfd = $.Deferred<qmm003.a.service.model.ResidentialTaxDetailDto>();
        var objectLayout = { resiTaxCode: resiTaxCode };
        var _path = nts.uk.text.format(paths.getResidentialDetail, resiTaxCode);
        nts.uk.request.ajax(_path)
            .done(function(res: model.ResidentialTaxDetailDto) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export module model {
        export class ResidentialTaxDetailDto {
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
        export class ResidentialTaxDto {
            resiTaxCode: string;
            resiTaxAutonomy: string;
            prefectureCode: string;
            contructor(resiTaxCode: string, resiTaxAutonomy: string, prefectureCode: string) {
                this.resiTaxCode = resiTaxCode;
                this.resiTaxAutonomy = resiTaxAutonomy;
                this.prefectureCode = prefectureCode;
            }
        }

    }

}