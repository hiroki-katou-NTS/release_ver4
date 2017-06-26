module nts.uk.pr.view.qmm008.b {

    export module service {

        // Service paths.
        var servicePath = {
            getAllOfficeItem: "pr/insurance/social/findall",
            getAllHistoryOfOffice: "pr/insurance/social/history",
            //get all heal insurance for get history
            getAllHealthOfficeAndHistory: "ctx/pr/core/insurance/social/healthrate/findAllHistory",
            getHealthInsuranceItemDetail: "ctx/pr/core/insurance/social/healthrate/find",
            //register+ update health
            registerHealthRate: "ctx/pr/core/insurance/social/healthrate/create",
            updateHealthRate: "ctx/pr/core/insurance/social/healthrate/update",
            removeHealthRate: "ctx/pr/core/insurance/social/healthrate/remove",
            getAllRoundingItem: "pr/insurance/social/find/rounding"
        };
        
        /**
         * Normal service.
         */
        export class Service extends base.simplehistory.service.BaseService<model.Office, model.Health> {
            constructor(path: base.simplehistory.service.Path) {
                super(path);
            }

            /**
             * Find history by id.
             */
            findHistoryByUuid(id: string): JQueryPromise<model.finder.HealthInsuranceRateDto> {
                return nts.uk.request.ajax(servicePath.getHealthInsuranceItemDetail + "/" + id);
            }
        }

        /**
         * Service intance.
         */
        export var instance = new Service({
            historyMasterPath: 'ctx/pr/core/insurance/social/healthrate/masterhistory',
            createHisotyPath: 'ctx/pr/core/insurance/social/healthrate/history/create',
            deleteHistoryPath: 'ctx/pr/core/insurance/social/healthrate/history/delete',
            updateHistoryStartPath: 'ctx/pr/core/insurance/social/healthrate/history/update/start'
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
         * Function is used to load health data of Office by office code.
         */
        export function getHealthInsuranceItemDetail(code: string): JQueryPromise<model.finder.HealthInsuranceRateDto> {
            var dfd = $.Deferred<model.finder.HealthInsuranceRateDto>();
            var findPath = servicePath.getHealthInsuranceItemDetail + "/" + code;
            return nts.uk.request.ajax(findPath);
        }
        
        /**
        * Function is used to load health data of Office by office code.
        */
        export function getAllHealthOfficeItem(): JQueryPromise<Array<model.finder.OfficeItemDto>> {
            var dfd = $.Deferred<Array<model.finder.OfficeItemDto>>();
            var findPath = servicePath.getAllHealthOfficeAndHistory;
            return nts.uk.request.ajax(findPath);
        }

        /**
        * Function is used to save new Health insurance rate with office code and history id.
        */
        export function registerHealthRate(data: model.finder.AddNewHistoryDto): JQueryPromise<model.finder.AddNewHistoryDto> {
            return nts.uk.request.ajax(servicePath.registerHealthRate, data);
        }

        /**
        * Function is used to update new Health insurance rate with office code and history id.
        */
        export function updateHealthRate(data: model.finder.HealthInsuranceRateDto): JQueryPromise<model.finder.HealthInsuranceRateDto> {
            return nts.uk.request.ajax(servicePath.updateHealthRate, data);
        }
        
        /**
        * Function is used to update new Health insurance rate with office code and history id.
        */
        export function removeHealthRate(historyId: string): JQueryPromise<model.finder.HealthInsuranceRateDto> {
            var data = {historyId: historyId};
            return nts.uk.request.ajax(servicePath.removeHealthRate,data);
        }
        
        /**
        * Model namespace.
        */
        export module model {
            export interface Office extends base.simplehistory.model.MasterModel<Health> {};
            export interface Health extends base.simplehistory.model.HistoryModel {};
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
                //health DTO
                export class HealthInsuranceRateDto {
                    historyId: string;
                    companyCode: string;
                    officeCode: string;
                    startMonth: string;
                    endMonth: string;
                    autoCalculate: number;
                    rateItems: Array<HealthInsuranceRateItemDto>;
                    roundingMethods: Array<RoundingDto>;
                    maxAmount: number;
                    constructor(historyId: string, companyCode: string, officeCode: string, startMonth: string, endMonth: string, autoCalculate: number, rateItems: Array<HealthInsuranceRateItemDto>, roundingMethods: Array<RoundingDto>, maxAmount: number) {
                        this.historyId = historyId;
                        this.companyCode = companyCode;
                        this.officeCode = officeCode;
                        this.startMonth = startMonth;
                        this.endMonth = endMonth;
                        this.autoCalculate = autoCalculate;
                        this.rateItems = rateItems;
                        this.roundingMethods = roundingMethods;
                        this.maxAmount = maxAmount;
                    }
                }
                export class HealthInsuranceRateItemDto {
                    payType: string;
                    insuranceType: string;
                    chargeRate: ChargeRateItemDto;
                    constructor(payType: string, insuranceType: string, chargeRate: ChargeRateItemDto) {
                        this.chargeRate = chargeRate;
                        this.payType = payType;
                        this.insuranceType = insuranceType;
                    }
                }
                export class ChargeRateItemDto {
                    companyRate: number;
                    personalRate: number;
                    constructor(companyRate: number, personalRate: number) {
                        this.companyRate = companyRate;
                        this.personalRate = personalRate;
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
                export class AddNewHistoryDto {
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
