var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var pr;
        (function (pr) {
            var view;
            (function (view) {
                var qpp007;
                (function (qpp007) {
                    var j;
                    (function (j) {
                        var service;
                        (function (service) {
                            var paths = {
                                findSalaryAggregateItem: "ctx/pr/report/salary/aggregate/item/findSalaryAggregateItem",
                                saveSalaryAggregateItem: "ctx/pr/report/salary/aggregate/item/save",
                                findAllMasterItem: "ctx/pr/report/masteritem/findAll"
                            };
                            function findSalaryAggregateItem(salaryAggregateItemInDto) {
                                return nts.uk.request.ajax(paths.findSalaryAggregateItem, salaryAggregateItemInDto);
                            }
                            service.findSalaryAggregateItem = findSalaryAggregateItem;
                            function saveSalaryAggregateItem(salaryAggregateItemSaveDto) {
                                var data = { salaryAggregateItemSaveDto: salaryAggregateItemSaveDto };
                                return nts.uk.request.ajax(paths.saveSalaryAggregateItem, data);
                            }
                            service.saveSalaryAggregateItem = saveSalaryAggregateItem;
                            function findAllMasterItem() {
                                return nts.uk.request.ajax(paths.findAllMasterItem);
                            }
                            service.findAllMasterItem = findAllMasterItem;
                            var model;
                            (function (model) {
                                var SalaryItemDto = (function () {
                                    function SalaryItemDto() {
                                    }
                                    return SalaryItemDto;
                                }());
                                model.SalaryItemDto = SalaryItemDto;
                                var SalaryAggregateItemFindDto = (function () {
                                    function SalaryAggregateItemFindDto() {
                                    }
                                    return SalaryAggregateItemFindDto;
                                }());
                                model.SalaryAggregateItemFindDto = SalaryAggregateItemFindDto;
                                var SalaryAggregateItemInDto = (function () {
                                    function SalaryAggregateItemInDto() {
                                    }
                                    return SalaryAggregateItemInDto;
                                }());
                                model.SalaryAggregateItemInDto = SalaryAggregateItemInDto;
                                var SalaryAggregateItemSaveDto = (function () {
                                    function SalaryAggregateItemSaveDto() {
                                    }
                                    return SalaryAggregateItemSaveDto;
                                }());
                                model.SalaryAggregateItemSaveDto = SalaryAggregateItemSaveDto;
                            })(model = service.model || (service.model = {}));
                        })(service = j.service || (j.service = {}));
                    })(j = qpp007.j || (qpp007.j = {}));
                })(qpp007 = view.qpp007 || (view.qpp007 = {}));
            })(view = pr.view || (pr.view = {}));
        })(pr = uk.pr || (uk.pr = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
//# sourceMappingURL=qpp007.j.service.js.map