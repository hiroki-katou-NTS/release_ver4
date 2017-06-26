module qmm003.c.service {
    var paths = {
        getResidentalTaxList: "pr/core/residential/findallresidential",
        getResidentialDetail: "pr/core/residential/findResidentialTax/{0}",
        getRegionPrefecture: "pr/core/residential/getlistLocation"
    }

    /**
     * Get list payment date processing.
     */
    export function getResidentialTax(): JQueryPromise<Array<model.ResidentialTaxDto>> {
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

    export function getResidentialTaxDetail(resiTaxCode: string): JQueryPromise<model.ResidentialTax> {
        var dfd = $.Deferred<qmm003.c.service.model.ResidentialTax>();
        var objectLayout = { resiTaxCode: resiTaxCode };
        var _path = nts.uk.text.format(paths.getResidentialDetail, resiTaxCode);
        nts.uk.request.ajax(_path)
            .done(function(res: model.ResidentialTax) {
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
                prefectureCode: string, resiTaxReportCode: string,
                registeredName: string, companyAccountNo: string, companySpecifiedNo: string,
                cordinatePostalCode: string, cordinatePostOffice: string, memo: string) {
                this.companyCode = companyCode;
                this.resiTaxCode = resiTaxCode;
                this.resiTaxAutonomy = resiTaxAutonomy;
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