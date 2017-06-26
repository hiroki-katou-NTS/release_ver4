module nts.qmm017 {
    export module service {
        var paths: any = {
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
            getListSystemVariable: "pr/formula/systemvariable/getAll",
            getSimpleCalSetting: "pr/formula/simplecalsetting/getAll"
        }

        export function getAllFormula(): JQueryPromise<Array<model.FormulaDto>> {
            var dfd = $.Deferred<Array<model.FormulaDto>>();
            nts.uk.request.ajax("pr", paths.getAllFormula)
                .done(function(res: Array<model.FormulaDto>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        export function findFormula(formulaCode: string, historyId: string): JQueryPromise<Array<model.FormulaDto>> {
            var dfd = $.Deferred<Array<model.FormulaDto>>();
            nts.uk.request.ajax("pr", paths.findFormula + "/" + formulaCode + "/" + historyId)
                .done(function(res: Array<model.FormulaDto>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        export function getFormulaDetail(formulaCode: string, historyId: string, difficultyAtr: number): JQueryPromise<model.FormulaDetailDto> {
            var dfd = $.Deferred<model.FormulaDetailDto>();
            nts.uk.request.ajax("pr", paths.getFormulaDetail + "/" + formulaCode + "/" + historyId + "/" + difficultyAtr)
                .done(function(res) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        export function getFormulaEasyDetail(formulaCode: string, historyId: string, easyFormulaCode: string): JQueryPromise<model.FormulaEasyDetailDto> {
            var dfd = $.Deferred<model.FormulaEasyDetailDto>();
            nts.uk.request.ajax("pr", paths.getFormulaEasyDetail + formulaCode + "/" + historyId + "/" + easyFormulaCode)
                .done(function(res) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        export function findOtherFormulas(formulaCode: string, baseYearMonth: number) {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax("pr", paths.findOtherFormulas + formulaCode + "/" + baseYearMonth)
                .done(function(res) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        export function registerFormulaMaster(command): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.registerFormulaMaster, command).done(function() {
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);
            });
            return dfd.promise();
        }

        export function updateFormulaMaster(command): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.updateFormula, command).done(function() {
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);
            });
            return dfd.promise();
        }

        export function getListCompanyUnitPrice(baseYm): JQueryPromise<Array<model.CompanyUnitPriceDto>> {
            var dfd = $.Deferred<Array<model.CompanyUnitPriceDto>>();
            nts.uk.request.ajax("pr", paths.getListCompanyUnitPrice + baseYm)
                .done(function(res: Array<model.CompanyUnitPriceDto>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        export function getListPersonalUnitPrice(): JQueryPromise<Array<model.PersonalUnitPriceDto>> {
            var dfd = $.Deferred<Array<model.PersonalUnitPriceDto>>();
            nts.uk.request.ajax("pr", paths.getListPersonalUnitPrice)
                .done(function(res: Array<model.PersonalUnitPriceDto>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        export function getListItemMaster(categoryAtr): JQueryPromise<Array<model.ItemMasterDto>> {
            var dfd = $.Deferred<Array<model.ItemMasterDto>>();
            nts.uk.request.ajax("pr", paths.getListItemMaster + categoryAtr)
                .done(function(res: Array<model.ItemMasterDto>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        export function getListWageTable(baseYm): JQueryPromise<Array<model.WageTableDto>> {
            var dfd = $.Deferred<Array<model.WageTableDto>>();
            nts.uk.request.ajax("pr", paths.getListWageTable + baseYm)
                .done(function(res: Array<model.WageTableDto>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        export function getListSystemVariable(): JQueryPromise<Array<model.SystemVariableDto>> {
            var dfd = $.Deferred<Array<model.SystemVariableDto>>();
            nts.uk.request.ajax("pr", paths.getListSystemVariable)
                .done(function(res: Array<model.SystemVariableDto>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        
        export function getListSimpleCalSetting(): JQueryPromise<Array<model.SimpleCalSettingDto>> {
            var dfd = $.Deferred<Array<model.SimpleCalSettingDto>>();
            nts.uk.request.ajax("pr", paths.getSimpleCalSetting)
                .done(function(res: Array<model.SimpleCalSettingDto>) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
    }

    export module model {
        export class FormulaDto {
            ccd: string;
            formulaCode: string;
            formulaName: string;
            difficultyAtr: number;
            historyId: string;
            startDate: number;
            endDate: number;
            conditionAtr: number;
            refMasterNo: number;
            constructor() {

            }
        }

        export class FormulaHistoryDto {
            companyCode: string;
            formulaCode: string;
            historyId: string;
            startDate: number;
            endDate: number;
            constructor() {

            }
        }

        export class FormulaDetailDto {
            // formula easy
            easyFormula: Array<FormulaEasyDto>;
            // formula manual
            formulaContent: string;
            referenceMonthAtr: number;
            roundAtr: number;
            roundDigit: number;
        }

        export class FormulaEasyDto {
            easyFormulaCode: string;
            value: number;
            referenceMasterNo: string;
            fixFormulaAtr: number;
            formulaDetail: FormulaEasyDetailDto;
            constructor() {
            }
        }

        export class FormulaEasyDetailDto {
            easyFormulaCode: string;
            easyFormulaName: string;
            easyFormulaTypeAtr: number;
            baseFixedAmount: number;
            baseAmountDevision: number;
            baseFixedValue: number;
            baseValueDevision: number;
            premiumRate: number;
            roundProcessingDevision: number;
            coefficientDivision: number;
            coefficientFixedValue: number;
            adjustmentDevision: number;
            totalRounding: number;
            referenceItemCodes: Array<string>;
            constructor() {
                var self = this;
                self.easyFormulaCode = '';
                self.easyFormulaName = '';
                self.easyFormulaTypeAtr = 0;
                self.baseFixedAmount = 0;
                self.baseAmountDevision = 0;
                self.baseFixedValue = 0;
                self.baseValueDevision = 0;
                self.premiumRate = 0;
                self.roundProcessingDevision = 0;
                self.coefficientDivision = 0;
                self.coefficientFixedValue = 0;
                self.adjustmentDevision = 0;
                self.totalRounding = 0;
                self.referenceItemCodes = [];
            }
        }

        export class CompanyUnitPriceDto {
            unitPriceCode: string;
            unitPriceName: string;
        }

        export class PersonalUnitPriceDto {
            personalUnitPriceCode: string;
            personalUnitPriceName: string;
            memo: string;
        }

        export class ItemMasterDto {
            itemCode: string;
            itemName: string;
        }

        export class WageTableDto {
            code: string;
            name: string;
        }

        export class SystemVariableDto {
            systemVariableName: string;
            systemVariableCode: string;
            result: string;
        }
        
        export class SimpleCalSettingDto {
            itemCode: string;
            itemName: string;
        }
    }
}