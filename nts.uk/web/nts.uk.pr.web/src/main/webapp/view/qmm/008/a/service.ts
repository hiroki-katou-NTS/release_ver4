module nts.uk.pr.view.qmm008.a {

    export module service {

        // Service paths.
        var servicePath = {
            getAllOfficeItem: "pr/insurance/social/findall",
            getAllHistoryOfOffice: "pr/insurance/social/history",
            getHealthInsuranceItemDetail: "ctx/pr/core/insurance/social/healthrate/findHealthInsuranceRate",
            getPensionItemDetail: "ctx/pr/core/insurance/social/pensionrate/findPensionRate",
            getAllRoundingItem: "list/rounding"
        };
        /**
         * Function is used to load all InsuranceOfficeItem by key.
         */
        export function findInsuranceOffice(key: string): JQueryPromise<Array<model.finder.InsuranceOfficeItemDto>> {

            // Init new dfd.
            var dfd = $.Deferred<Array<model.finder.InsuranceOfficeItemDto>>();

            var findPath = servicePath.getAllOfficeItem + ((key != null && key != '') ? ('?key=' + key) : '');

            // Call ajax.
            nts.uk.request.ajax(findPath).done(function(data) {

                // Convert json to model here.
                var OfficeItemList: Array<model.finder.InsuranceOfficeItemDto> = convertToTreeList(data);

                //TODO convert to child-parent array
                //                var OfficeItemList: Array<model.finder.InsuranceOfficeItemDto> =  [
                //                    new model.finder.InsuranceOfficeItemDto('id01', 'A 事業所', 'code1',[
                //                        new model.finder.InsuranceOfficeItemDto('child01', '2017/04 ~ 9999/12', 'chilcode1',[]),
                //                        new model.finder.InsuranceOfficeItemDto('child02', '2016/04 ~ 2017/03', 'chilcode2',[])
                //                    ]),
                //                    new model.finder.InsuranceOfficeItemDto('id02', 'B 事業所', 'code2',[])];

                // Resolve.
                dfd.resolve(OfficeItemList);
            });

            // Ret promise.
            return dfd.promise();
        }

        /**
        * Convert list from dto to treegrid
        */
        function convertToTreeList(data: Array<model.finder.InsuranceOfficeItemDto>): Array<model.finder.InsuranceOfficeItemDto> {
            var OfficeItemList: Array<model.finder.InsuranceOfficeItemDto> = [];
            // 
            data.forEach((item, index) => {
                OfficeItemList.push(new model.finder.InsuranceOfficeItemDto('id' + index, item.name, item.code, []));
            });
            return OfficeItemList;
        }

        /**
         * Function is used to load history of Office by office code.
         */
        export function findHistoryByOfficeCode(code: string): JQueryPromise<model.finder.InsuranceOfficeItemDto> {
            // Init new dfd.
            var dfd = $.Deferred<model.finder.InsuranceOfficeItemDto>();
            var findPath = servicePath.getAllHistoryOfOffice + "/" + code;
            // Call ajax.
            nts.uk.request.ajax(findPath).done(function(data) {
                // Resolve.
                dfd.resolve(data);
            });
            // Ret promise.
            return dfd.promise();
        }

        /**
         * Function is used to load all RoundingOption.
         */
        export function findAllRounding(): JQueryPromise<Array<model.finder.RoundingItemDto>> {

            // Init new dfd.
            var dfd = $.Deferred<Array<model.finder.RoundingItemDto>>();

            var findPath = servicePath.getAllRoundingItem;

            // Call ajax.
            //            nts.request.ajax(findPath).done(function(data) {
            var data = null;
            // Convert json to model here.
            var roundingList: Array<model.finder.RoundingItemDto> = [
                //                    new model.finder.RoundingItemDto('001', 'op1change'),
                //                    new model.finder.RoundingItemDto('002', 'op2'),
                //                    new model.finder.RoundingItemDto('003', 'op3')
            ];

            // Resolve.
            dfd.resolve(roundingList);
            //            });

            // Ret promise.
            return dfd.promise();
        }
        /**
         * Function is used to load health data of Office by office code.
         */
        export function getHealthInsuranceItemDetail(code: string): JQueryPromise<model.finder.HealthInsuranceRateDto> {
            // Init new dfd.
            var dfd = $.Deferred<model.finder.HealthInsuranceRateDto>();
            var findPath = servicePath.getHealthInsuranceItemDetail + "/" + code;
            // Call ajax.
            nts.uk.request.ajax(findPath).done(function(data) {
                // Convert json to model here.
                var healthInsuranceRateDetailData: model.finder.HealthInsuranceRateDto = data;
                // Resolve.
                dfd.resolve(healthInsuranceRateDetailData);
            });

            // Ret promise.
            return dfd.promise();
        }

        /**
        * Function is used to load pension  data of Office by office code.
        */
        export function getPensionItemDetail(code: string): JQueryPromise<model.finder.PensionRateDto> {
            // Init new dfd.
            var dfd = $.Deferred<model.finder.PensionRateDto>();
            var findPath = servicePath.getPensionItemDetail + "/" + code;
            // Call ajax.
            nts.uk.request.ajax(findPath).done(function(data) {
                // Convert json to model here.
                var pensionRateDetailData: model.finder.PensionRateDto = data;
                // Resolve.
                dfd.resolve(pensionRateDetailData);
            });
            // Ret promise.
            return dfd.promise();
        }

        /**
        * Model namespace.
        */
        export module model {
            export module finder {
                export class ChooseOption {
                    code: string;
                    name: string;
                }
                export class HistoryItemDto {
                    id: string;
                    name: string;
                    code: string;
                    childs: any;
                    codeName: string;
                    constructor(id: string, name: string, code: string, childs: Array<InsuranceOfficeItemDto>) {
                        this.id = id;
                        this.name = name;
                        this.code = code;
                        this.childs = childs;
                    }
                }
                //office DTO
                export class InsuranceOfficeItemDto {
                    id: string;
                    name: string;
                    code: string;
                    childs: any;
                    codeName: string;
                    constructor(id: string, name: string, code: string, childs: Array<InsuranceOfficeItemDto>) {
                        this.id = id;
                        this.name = name;
                        this.code = code;
                        this.childs = childs;
                        if (childs.length == 0) {
                            this.codeName = name;
                        } else {
                            this.codeName = code + "\u00A0" + "\u00A0" + "\u00A0" + name;
                        }
                    }
                }

                //Pension DTO
                export class PensionRateDto {
                    historyId: number;
                    companyCode: string;
                    officeCode: string;
                    applyRange: string;
                    autoCalculate: number;
                    fundInputOption: number;
                    premiumRateItems: Array<PensionRateItemDto>;
                    fundRateItems: Array<FundRateItemDto>;
                    roundingMethods: Array<RoundingDto>;
                    maxAmount: number;
                    officeRate: number;
                    //TODO this contructor for mock data,delete after use
                    constructor(historyId: number, companyCode: string, officeCode: string, applyRange: string, autoCalculate: number, funInputOption: number, premiumRateItems: Array<PensionRateItemDto>, fundRateItems: Array<FundRateItemDto>, roundingMethods: Array<RoundingDto>, maxAmount: number, officeRate: number) {
                        this.historyId = historyId;
                        this.companyCode = companyCode;
                        this.officeCode = officeCode;
                        this.applyRange = applyRange;
                        this.autoCalculate = autoCalculate;
                        this.fundInputOption = funInputOption;
                        this.premiumRateItems = premiumRateItems;
                        this.fundRateItems = fundRateItems;
                        this.roundingMethods = roundingMethods;
                        this.maxAmount = maxAmount;
                        this.officeRate = officeRate;
                    }
                }
                export class PensionRateItemDto {
                    pensionDeductCompanyRate: number;
                    pensionDeductPersonalRate: number;
                    pensionRaiseCompanyRate: number;
                    pensionRaisePersonalRate:number;
                    genderType: InsuranceGender;
                    payType: PaymentType;
                    constructor(payType: PaymentType, genderType: InsuranceGender) {
//                        this.chargeRate = chargeRate;
//                        this.groupType = groupType;
                        this.payType = payType;
                        this.genderType = genderType;
                    }
                }
                export class FundRateItemDto {
                    chargeRate: number;
                    groupType: GroupType;
                    chargeType: ChargeType;
                    genderType: InsuranceGender;
                    payType: PaymentType;
                    constructor(chargeRate: number, groupType: GroupType, chargeType: ChargeType, genderType: InsuranceGender, payType: PaymentType) {
                        this.chargeRate = chargeRate;
                        this.groupType = groupType;
                        this.chargeType = chargeType;
                        this.genderType = genderType;
                        this.payType = payType;
                    }
                }

                //health DTO
                export class HealthInsuranceRateDto {
                    historyId: number;
                    companyCode: string;
                    officeCode: string;
                    applyRange: string;
                    autoCalculate: number;
                    rateItems: Array<HealthInsuranceRateItemDto>;
                    roundingMethods: Array<RoundingDto>;
                    maxAmount: number;

                    //TODO this contructor for mock data,delete after use
                    constructor(historyId: number, companyCode: string, officeCode: string, applyRange: string, autoCalculate: number, rateItems: Array<HealthInsuranceRateItemDto>, roundingMethods: Array<RoundingDto>, maxAmount: number) {
                        this.historyId = historyId;
                        this.companyCode = companyCode;
                        this.officeCode = officeCode;
                        this.applyRange = applyRange;
                        this.autoCalculate = autoCalculate;
                        this.rateItems = rateItems;
                        this.roundingMethods = roundingMethods;
                        this.maxAmount = maxAmount;
                    }
                }
                export class HealthInsuranceRateItemDto {
                    chargeRate: number; //value of person or company
                    payType: PaymentType;
                    healthInsuranceType: HealthInsuranceType;
                    companyRate: number;
                    personalRate: number;
                    constructor(chargeRate: number, payType: PaymentType, healthInsuranceType: HealthInsuranceType) {
                        this.chargeRate = chargeRate;
                        this.payType = payType;
                        this.healthInsuranceType = healthInsuranceType;
                    }
                }
                export class chargeRateItemDto {
                    companyRate: number;
                    personalRate: number;
                    constructor(companyRate: number, personalRate: number) {
                        this.companyRate = companyRate;
                        this.personalRate = personalRate;
                    }
                }
                //common class for health and pension
                export class RoundingDto {
                    payType: PaymentType;
                    roundAtrs: RoundingItemDto;
                    constructor(payType: PaymentType, roundAtrs: RoundingItemDto) {
                        this.payType = payType;
                        this.roundAtrs = roundAtrs;
                    }
                }
                export class HealthInsuranceAvgearnDto {
                    historyId: number;
                    levelCode: number;
                    companyAvg: HealthInsuranceAvgearnValueDto;
                    personalAvg: HealthInsuranceAvgearnValueDto;
                }

                //                export class RoundingItemDto {
                //                    companyRoundAtr: number;
                //                    personalRoundAtr: number;
                //                }
                //                export class RoundingItemDto {
                //                    code: string;
                //                    name: string;
                //                    label: string;
                //
                //                    constructor(code: string, name: string) {
                //                        this.code = code;
                //                        this.name = name;
                //                    }
                //                }
                export class RoundingItemDto {
                    personalRoundAtr: string;
                    companyRoundAtr: string;
                }
                export class HealthInsuranceAvgearnValueDto {
                    healthGeneralMny: number;
                    healthNursingMny: number;
                    healthBasicMny: number;
                    healthSpecificMny: number;
                }
            }


            export enum PaymentType {
                Salary = 0,
                Bonus = 1
            }

            export enum HealthInsuranceType {
                General = 0,
                Nursing = 1,
                Basic = 2,
                Special = 3
            }
            export enum InsuranceGender {
                Male = 0,
                Female = 1,
                Unknow = 2
            }
            export enum ChargeType {
                Burden = 0,
                Exemption = 1
            }
            export enum GroupType {
                Personal = 0,
                Company = 1
            }
        }

    }
}
