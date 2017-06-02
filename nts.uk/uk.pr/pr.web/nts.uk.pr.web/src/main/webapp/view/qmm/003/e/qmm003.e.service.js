var qmm003;
(function (qmm003) {
    var e;
    (function (e) {
        var service;
        (function (service) {
            var paths = {
                getResidentalTaxList: "pr/core/residential/findallresidential",
                getRegionPrefecture: "pr/core/residential/getlistLocation",
                getResidentalTaxListByReportCode: "pr/core/residential/findallresidential1/{0}",
                updateReportCode: "pr/core/residential/updatereportCode",
                update: "pr/core/rule/law/tax/residential/input/update2",
                getPersionResidentalTax: "pr/core/rule/law/tax/residential/input/findAll/{0}/{1}"
            };
            //get person residential Tax
            function getPersonResidentialTax(yearKey, residenceCode) {
                var dfd = $.Deferred();
                var _path = nts.uk.text.format(paths.getPersionResidentalTax, yearKey, residenceCode);
                nts.uk.request.ajax(_path)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getPersonResidentialTax = getPersonResidentialTax;
            /**
             * Get list ResidentialTax.
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
            /**
             * Get list ResidentialTax.
             */
            function getResidentalTaxListByReportCode(resiTaxReportCode) {
                var dfd = $.Deferred();
                var _path = nts.uk.text.format(paths.getResidentalTaxListByReportCode, resiTaxReportCode);
                nts.uk.request.ajax(_path)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getResidentalTaxListByReportCode = getResidentalTaxListByReportCode;
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
            /**
            * update ReportCode
            */
            function updateReportCode(resiTaxCode, resiTaxReportCode) {
                var dfd = $.Deferred();
                var residential = {
                    resiTaxCode: resiTaxCode,
                    resiTaxReportCode: resiTaxReportCode
                };
                nts.uk.request.ajax(paths.updateReportCode, residential)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.updateReportCode = updateReportCode;
            //UPD-2
            /**
        * update ReportCode
        */
            function updateResidenceCode(residenceCode, personId, yearKey) {
                var dfd = $.Deferred();
                var obj = {
                    residenceCode: residenceCode,
                    yearKey: yearKey,
                    personId: personId
                };
                nts.uk.request.ajax(paths.update, obj)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.updateResidenceCode = updateResidenceCode;
            function updateAllReportCode(param, resiTaxReportCode, yearKey) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.updateReportCode, { resiTaxCodes: param, resiTaxReportCode: resiTaxReportCode, yearKey: yearKey })
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.updateAllReportCode = updateAllReportCode;
            var model;
            (function (model) {
                var ResidentialTax = (function () {
                    function ResidentialTax() {
                    }
                    ResidentialTax.prototype.contructor = function (companyCode, resiTaxCode, resiTaxAutonomy, resiTaxAutonomyKana, prefectureCode, resiTaxReportCode, registeredName, companyAccountNo, companySpecifiedNo, cordinatePostalCode, cordinatePostOffice, memo) {
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
            })(model = service.model || (service.model = {}));
        })(service = e.service || (e.service = {}));
    })(e = qmm003.e || (qmm003.e = {}));
})(qmm003 || (qmm003 = {}));
//# sourceMappingURL=qmm003.e.service.js.map