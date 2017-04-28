var nts;
(function (nts) {
    var qmm017;
    (function (qmm017) {
        var service;
        (function (service) {
            var paths = {
                getAllFormula: "pr/formula/formulaMaster/getAllFormula",
                findFormula: "pr/formula/formulaMaster/findFormula",
                getFormulaDetail: "pr/formula/formulaMaster/getFormulaDetail",
                registerFormulaMaster: "pr/formula/formulaMaster/addFormulaMaster",
                updateFormula: "pr/formula/formulaMaster/updateFormulaMaster",
                getListCompanyUnitPrice: "pr/proto/unitprice/findbymonth/",
                getListPersonalUnitPrice: "pr/core/rule/employment/unitprice/personal/find/all",
                getListItemMaster: "pr/core/item/findall/category/",
                findOtherFormulas: "pr/formula/formulaMaster/findOtherFormulas/",
                getListWageTable: "pr/proto/wagetable/findbymonth/",
                getFormulaEasyDetail: "pr/formula/formulaMaster/getFormulaEasyDetail/",
                getListSystemVariable: "pr/formula/systemvariable/getAll"
            };
            function getAllFormula() {
                var dfd = $.Deferred();
                nts.uk.request.ajax("pr", paths.getAllFormula)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getAllFormula = getAllFormula;
            function findFormula(formulaCode, historyId) {
                var dfd = $.Deferred();
                nts.uk.request.ajax("pr", paths.findFormula + "/" + formulaCode + "/" + historyId)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.findFormula = findFormula;
            function getFormulaDetail(formulaCode, historyId, difficultyAtr) {
                var dfd = $.Deferred();
                nts.uk.request.ajax("pr", paths.getFormulaDetail + "/" + formulaCode + "/" + historyId + "/" + difficultyAtr)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getFormulaDetail = getFormulaDetail;
            function getFormulaEasyDetail(formulaCode, historyId, easyFormulaCode) {
                var dfd = $.Deferred();
                nts.uk.request.ajax("pr", paths.getFormulaEasyDetail + formulaCode + "/" + historyId + "/" + easyFormulaCode)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getFormulaEasyDetail = getFormulaEasyDetail;
            function findOtherFormulas(formulaCode, baseYearMonth) {
                var dfd = $.Deferred();
                nts.uk.request.ajax("pr", paths.findOtherFormulas + formulaCode + "/" + baseYearMonth)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.findOtherFormulas = findOtherFormulas;
            function registerFormulaMaster(command) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.registerFormulaMaster, command).done(function () {
                    dfd.resolve();
                }).fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.registerFormulaMaster = registerFormulaMaster;
            function updateFormulaMaster(command) {
                var dfd = $.Deferred();
                nts.uk.request.ajax(paths.updateFormula, command).done(function () {
                    dfd.resolve();
                }).fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.updateFormulaMaster = updateFormulaMaster;
            function getListCompanyUnitPrice(baseYm) {
                var dfd = $.Deferred();
                nts.uk.request.ajax("pr", paths.getListCompanyUnitPrice + baseYm)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getListCompanyUnitPrice = getListCompanyUnitPrice;
            function getListPersonalUnitPrice() {
                var dfd = $.Deferred();
                nts.uk.request.ajax("pr", paths.getListPersonalUnitPrice)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getListPersonalUnitPrice = getListPersonalUnitPrice;
            function getListItemMaster(categoryAtr) {
                var dfd = $.Deferred();
                nts.uk.request.ajax("pr", paths.getListItemMaster + categoryAtr)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getListItemMaster = getListItemMaster;
            function getListWageTable(baseYm) {
                var dfd = $.Deferred();
                nts.uk.request.ajax("pr", paths.getListWageTable + baseYm)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getListWageTable = getListWageTable;
            function getListSystemVariable() {
                var dfd = $.Deferred();
                nts.uk.request.ajax("pr", paths.getListSystemVariable)
                    .done(function (res) {
                    dfd.resolve(res);
                })
                    .fail(function (res) {
                    dfd.reject(res);
                });
                return dfd.promise();
            }
            service.getListSystemVariable = getListSystemVariable;
        })(service = qmm017.service || (qmm017.service = {}));
        var model;
        (function (model) {
            var FormulaDto = (function () {
                function FormulaDto() {
                }
                return FormulaDto;
            }());
            model.FormulaDto = FormulaDto;
            var FormulaHistoryDto = (function () {
                function FormulaHistoryDto() {
                }
                return FormulaHistoryDto;
            }());
            model.FormulaHistoryDto = FormulaHistoryDto;
            var FormulaDetailDto = (function () {
                function FormulaDetailDto() {
                }
                return FormulaDetailDto;
            }());
            model.FormulaDetailDto = FormulaDetailDto;
            var FormulaEasyDto = (function () {
                function FormulaEasyDto() {
                }
                return FormulaEasyDto;
            }());
            model.FormulaEasyDto = FormulaEasyDto;
            var FormulaEasyDetailDto = (function () {
                function FormulaEasyDetailDto() {
                }
                return FormulaEasyDetailDto;
            }());
            model.FormulaEasyDetailDto = FormulaEasyDetailDto;
            var CompanyUnitPriceDto = (function () {
                function CompanyUnitPriceDto() {
                }
                return CompanyUnitPriceDto;
            }());
            model.CompanyUnitPriceDto = CompanyUnitPriceDto;
            var PersonalUnitPriceDto = (function () {
                function PersonalUnitPriceDto() {
                }
                return PersonalUnitPriceDto;
            }());
            model.PersonalUnitPriceDto = PersonalUnitPriceDto;
            var ItemMasterDto = (function () {
                function ItemMasterDto() {
                }
                return ItemMasterDto;
            }());
            model.ItemMasterDto = ItemMasterDto;
            var WageTableDto = (function () {
                function WageTableDto() {
                }
                return WageTableDto;
            }());
            model.WageTableDto = WageTableDto;
            var SystemVariableDto = (function () {
                function SystemVariableDto() {
                }
                return SystemVariableDto;
            }());
            model.SystemVariableDto = SystemVariableDto;
        })(model = qmm017.model || (qmm017.model = {}));
    })(qmm017 = nts.qmm017 || (nts.qmm017 = {}));
})(nts || (nts = {}));
