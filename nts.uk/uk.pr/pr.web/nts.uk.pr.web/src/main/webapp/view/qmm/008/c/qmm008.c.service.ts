module nts.uk.pr.view.qmm008.c {

    export module service {

        // Service paths.
        var servicePath = {
            getAllOfficeItem: "pr/insurance/social/findall",
            getAllHistoryOfOffice: "pr/insurance/social/history",
            //get all heal insurance for get history
            getAllPensionOfficeAndHistory: "ctx/pr/core/insurance/social/pensionrate/findAllHistory",
            //get health and pension data 
            getPensionItemDetail: "ctx/pr/core/insurance/social/pensionrate/find",
            //register+ update pension
            registerPensionRate: "ctx/pr/core/insurance/social/pensionrate/create",
            updatePensionRate: "ctx/pr/core/insurance/social/pensionrate/update",
            removePensionRate: "ctx/pr/core/insurance/social/pensionrate/remove",
            getAllRoundingItem: "pr/insurance/social/find/rounding"
        };
        
        /**
         * Normal service.
         */
        export class Service extends base.simplehistory.service.BaseService<model.Office, model.Pension> {
            constructor(path: base.simplehistory.service.Path) {
                super(path);
            }

            /**
             * Find history by id.
             */
            findHistoryByUuid(id: string): JQueryPromise<model.finder.PensionRateDto> {
                return nts.uk.request.ajax(servicePath.getPensionItemDetail + "/" + id);
            }
        }

        /**
         * Service intance.
         */
        export var instance = new Service({
            historyMasterPath: 'ctx/pr/core/insurance/social/pensionrate/masterhistory',
            createHisotyPath: 'ctx/pr/core/insurance/social/pensionrate/history/create',
            deleteHistoryPath: 'ctx/pr/core/insurance/social/pensionrate/history/delete',
            updateHistoryStartPath: 'ctx/pr/core/insurance/social/pensionrate/history/update/start'
        });
        
        /**
         * Function is used to load all InsuranceOfficeItem by key.
         */
        export function findInsuranceOffice(key: string): JQueryPromise<Array<model.finder.InsuranceOfficeItemDto>> {
            var findPath = servicePath.getAllOfficeItem + ((key != null && key != '') ? ('?key=' + key) : '');
            return nts.uk.request.ajax(findPath);
        }

        /**
         * Function is used to load all RoundingOption.
         */
        export function findAllRounding(): JQueryPromise<Array<model.finder.Enum>> {
            var dfd = $.Deferred<Array<model.finder.Enum>>();
            var roundingList: Array<model.finder.Enum> = [
                new model.finder.Enum('0', '切捨て'),
                new model.finder.Enum('1', '切り上げ'),
                new model.finder.Enum('2', '四捨五入'),
                new model.finder.Enum('3', '五捨六入'),
                new model.finder.Enum('4', '五捨五超入')
            ];
            dfd.resolve(roundingList);
            return dfd.promise();
        }

        /**
        * Function is used to load pension  data of Office by office code.
        */
        export function getPensionItemDetail(code: string): JQueryPromise<model.finder.PensionRateDto> {
            var findPath = servicePath.getPensionItemDetail + "/" + code;
            return nts.uk.request.ajax(findPath);
        }

        /**
        * Function is used to load health data of Office by office code.
        */
        export function getAllPensionOfficeItem(): JQueryPromise<Array<model.finder.OfficeItemDto>> {
            var findPath = servicePath.getAllPensionOfficeAndHistory;
            return nts.uk.request.ajax(findPath);
        }

        /**
        * Function is used to save new Pension rate with office code and history id.
        */
        export function registerPensionRate(data: model.finder.PensionRateDto): JQueryPromise<model.finder.PensionRateDto> {
            return nts.uk.request.ajax(servicePath.registerPensionRate, data);
        }

        /**
        * Function is used to update new Pension rate with office code and history id.
        */
        export function updatePensionRate(data: model.finder.PensionRateDto): JQueryPromise<model.finder.PensionRateDto> {
            return nts.uk.request.ajax(servicePath.updatePensionRate, data);
        }
        /**
        * Function is used to update new Health insurance rate with office code and history id.
        */
        export function removePensionRate(historyId: string): JQueryPromise<model.finder.PensionRateDto> {
            var data = {historyId: historyId};
            return nts.uk.request.ajax(servicePath.removePensionRate, data);
        }
        /**
        * Model namespace.
        */
        export module model {
            export interface Office extends base.simplehistory.model.MasterModel<Pension> {};
            export interface Pension extends base.simplehistory.model.HistoryModel {};
            export module finder {
                //office DTO
                export class InsuranceOfficeItemDto {
                    id: string;
                    name: string;
                    code: string;
                    childs: any;
                    codeName: string;
                    constructor(id: string, name: string, code: string, childs: Array<InsuranceOfficeItemDto>, codeName: string) {
                        this.id = id;
                        this.name = name;
                        this.code = code;
                        this.childs = childs;
                        this.codeName = codeName;
                    }
                }

                //Pension DTO
                export class PensionRateDto {
                    historyId: string;
                    companyCode: string;
                    officeCode: string;
                    startMonth: string;
                    endMonth: string;
                    autoCalculate: number;
                    fundInputApply: number;
                    premiumRateItems: Array<PensionRateItemDto>;
                    fundRateItems: Array<FundRateItemDto>;
                    roundingMethods: Array<RoundingDto>;
                    maxAmount: number;
                    childContributionRate: number;
                    constructor(historyId: string, companyCode: string, officeCode: string, startMonth: string, endMonth: string, autoCalculate: number, fundInputApply: number, premiumRateItems: Array<PensionRateItemDto>, fundRateItems: Array<FundRateItemDto>, roundingMethods: Array<RoundingDto>, maxAmount: number, childContributionRate: number) {
                        this.historyId = historyId;
                        this.companyCode = companyCode;
                        this.officeCode = officeCode;
                        this.startMonth = startMonth;
                        this.endMonth = endMonth;
                        this.autoCalculate = autoCalculate;
                        this.fundInputApply = fundInputApply;
                        this.premiumRateItems = premiumRateItems;
                        this.fundRateItems = fundRateItems;
                        this.roundingMethods = roundingMethods;
                        this.maxAmount = maxAmount;
                        this.childContributionRate = childContributionRate;
                    }
                }
                export class PensionRateItemDto {
                    payType: string;
                    genderType: string;
                    personalRate: number;
                    companyRate: number;
                    constructor(payType: string, genderType: string, personalRate: number, companyRate: number) {
                        this.payType = payType;
                        this.genderType = genderType;
                        this.personalRate = personalRate;
                        this.companyRate = companyRate;
                    }
                }
                export class FundRateItemDto {
                    payType: string;
                    genderType: string;
                    burdenChargePersonalRate: number;
                    burdenChargeCompanyRate: number;
                    exemptionChargePersonalRate: number;
                    exemptionChargeCompanyRate: number;
                    constructor(payType: string, genderType: string, burdenChargePersonalRate: number, burdenChargeCompanyRate: number, exemptionChargePersonalRate: number, exemptionChargeCompanyRate: number) {
                        this.payType = payType;
                        this.genderType = genderType;
                        this.burdenChargePersonalRate = burdenChargePersonalRate;
                        this.burdenChargeCompanyRate = burdenChargeCompanyRate;
                        this.exemptionChargePersonalRate = exemptionChargePersonalRate;
                        this.exemptionChargeCompanyRate = exemptionChargeCompanyRate;
                    }
                }

                export class OfficeItemDto {
                    officeCode: string;
                    officeName: string;
                    listHistory: Array<HistoryDto>;
                }
                export class HistoryDto {
                    historyId: string;
                    startMonth: string;
                    endMonth: string;
                }

                //common class for health and pension
                export class RoundingDto {
                    payType: string;
                    roundAtrs: RoundingItemDto;
                    constructor(payType: string, roundAtrs: RoundingItemDto) {
                        this.payType = payType;
                        this.roundAtrs = roundAtrs;
                    }
                }
                export class RoundingItemDto {
                    personalRoundAtr: string;
                    companyRoundAtr: string;
                    constructor(personalRoundAtr: string, companyRoundAtr: string) {
                        this.personalRoundAtr = personalRoundAtr;
                        this.companyRoundAtr = companyRoundAtr;
                    }
                }
                export class AddNewHistoryDto{
                    officeCode: string;
                    startMonth: string;
                    endMonth: string;
                    isCloneData: boolean;
                }
                export class Enum {
                    code: string;
                    name: string;
                    label: string;

                    constructor(code: string, name: string) {
                        this.code = code;
                        this.name = name;
                    }
                }
            }
        }

    }
}
