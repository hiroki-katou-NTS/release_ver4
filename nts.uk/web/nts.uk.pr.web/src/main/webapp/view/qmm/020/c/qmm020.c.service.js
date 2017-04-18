var qmm020;
(function (qmm020) {
    var c;
    (function (c) {
        var service;
        (function (service) {
            //duong dan   
            var paths = {
                getEmployAllotSettingHeaderList: "pr/core/allot/findallemployeeallotheader",
                getEmployAllotSettingDetailList: "pr/core/allot/findallemployeeallotdetail",
                getAllEmployeeAllotSettingList: "pr/core/allot/findAllEmployeeAllotSettingList/{0}",
                getMaxDate: "pr/core/allot/findallemployeeallotheaderMax"
            };
            /**
             * Get list payment date processing.
             */
            function getEmployeeAllotHeaderList() {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.getEmployAllotSettingHeaderList)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getEmployeeAllotHeaderList = getEmployeeAllotHeaderList;
            /**
             * Get employee list with payment doc, bunus doc
             */
            function getEmployeeAllotDetailList() {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.getEmployAllotSettingDetailList)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getEmployeeAllotDetailList = getEmployeeAllotDetailList;
            function getAllEmployeeAllotSetting(histId) {
                var dfd = $.Deferred();
                var _path = nts.uk.text.format(paths.getAllEmployeeAllotSettingList, histId);
                nts.uk.request.ajax(_path).done(function (res) {
                    dfd.resolve(res);
                }).fail(function (error) {
                    dfd.reject(error);
                });
                return dfd.promise();
            }
            service.getAllEmployeeAllotSetting = getAllEmployeeAllotSetting;
            function getAllotCompanyMaxDate() {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.getMaxDate)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getAllotCompanyMaxDate = getAllotCompanyMaxDate;
        })(service = c.service || (c.service = {}));
    })(c = qmm020.c || (qmm020.c = {}));
})(qmm020 || (qmm020 = {}));
