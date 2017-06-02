var qmm003;
(function (qmm003) {
    var b;
    (function (b) {
        var service;
        (function (service) {
            var paths = {
                getResidentalTaxList: "pr/core/residential/findallByCompanyCode",
                getResidentialDetail: "pr/core/residential/findResidentialTax/{0}/{1}",
                getRegionPrefecture: "pr/core/residential/getlistLocation"
            };
            /**
             * Get list  ResidentialTax
             */
            function getResidentialTax() {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.getResidentalTaxList)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getResidentialTax = getResidentialTax;
            function getRegionPrefecture() {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.getRegionPrefecture)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getRegionPrefecture = getRegionPrefecture;
            function getResidentialTaxDetail(companyCd, resiTaxCode) {
                var dfd = $.Deferred();
                var objectLayout = { resiTaxCode: resiTaxCode };
                var _path = nts.uk.text.format(paths.getResidentialDetail, companyCd, resiTaxCode);
                nts.uk.request.ajax(_path)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getResidentialTaxDetail = getResidentialTaxDetail;
            var model;
            (function (model) {
                var ResidentialTax = (function () {
                    function ResidentialTax() {
                    }
                    ResidentialTax.prototype.contructor = function (companyCode, resiTaxCode, resiTaxAutonomy, prefectureCode, resiTaxReportCode, registeredName, companyAccountNo, companySpecifiedNo, cordinatePostalCode, cordinatePostOffice, memo) {
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
                    };
                    return ResidentialTax;
                }());
                model.ResidentialTax = ResidentialTax;
                var RegionObject = (function () {
                    function RegionObject() {
                    }
                    RegionObject.prototype.contructor = function (regionCode, regionName, prefectures) {
                        this.regionCode = regionCode;
                        this.regionName = regionName;
                        this.prefectures = prefectures;
                    };
                    return RegionObject;
                }());
                model.RegionObject = RegionObject;
                var PrefectureObject = (function () {
                    function PrefectureObject() {
                    }
                    PrefectureObject.prototype.contructor = function (prefectureCode, prefectureName) {
                        this.prefectureCode = prefectureCode;
                        this.prefectureName = prefectureName;
                    };
                    return PrefectureObject;
                }());
                model.PrefectureObject = PrefectureObject;
                var ResidentialTaxDto = (function () {
                    function ResidentialTaxDto() {
                    }
                    ResidentialTaxDto.prototype.contructor = function (resiTaxCode, resiTaxAutonomy, prefectureCode) {
                        this.resiTaxCode = resiTaxCode;
                        this.resiTaxAutonomy = resiTaxAutonomy;
                        this.prefectureCode = prefectureCode;
                    };
                    return ResidentialTaxDto;
                }());
                model.ResidentialTaxDto = ResidentialTaxDto;
            })(model = service.model || (service.model = {}));
        })(service = b.service || (b.service = {}));
    })(b = qmm003.b || (qmm003.b = {}));
})(qmm003 || (qmm003 = {}));
//# sourceMappingURL=qmm003.b.service.js.map