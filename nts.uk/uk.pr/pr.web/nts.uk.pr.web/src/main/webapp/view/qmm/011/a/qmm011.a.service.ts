module nts.uk.pr.view.qmm011.a {
    export module service {

        var paths: any = {
            findAllUnemployeeInsuranceRateHistory: "pr/insurance/labor/unemployeerate/history/findall",
            findUnemployeeInsuranceRateHistory: "pr/insurance/labor/unemployeerate/history/find",
            updateUnemployeeInsuranceRateHistory: "pr/insurance/labor/unemployeerate/history/update",
            detailUnemployeeInsuranceRateHistory: "pr/insurance/labor/unemployeerate/detail",
            addUnemployeeInsuranceRate: "pr/insurance/labor/unemployeerate/add",
            copyUnemployeeInsuranceRate: "pr/insurance/labor/unemployeerate/copy",
            deleteUnemployeeInsurance: "pr/insurance/labor/unemployeerate/delete",
            updateUnemployeeInsuranceRate: "pr/insurance/labor/unemployeerate/update",
            findAllAccidentInsuranceRateHistory: "pr/insurance/labor/accidentrate/history/findall",
            findAccidentInsuranceRateHistory: "pr/insurance/labor/accidentrate/history/find",
            updateAccidentInsuranceRateHistory: "pr/insurance/labor/accidentrate/history/update",
            findAccidentInsuranceRate: "pr/insurance/labor/accidentrate/find",
            addAccidentInsuranceRate: "pr/insurance/labor/accidentrate/add",
            copyAccidentInsuranceRate: "pr/insurance/labor/accidentrate/copy",
            updateAccidentInsuranceRate: "pr/insurance/labor/accidentrate/update",
            findAllInsuranceBusinessType: "pr/insurance/labor/businesstype/findall",
            deleteAccidentInsuranceRate: "pr/insurance/labor/accidentrate/delete"
        };

        //connection service update history 
        export function updateUnemployeeInsuranceRateHistory(unemployeeInsuranceHistoryUpdateDto: model.UnemployeeInsuranceHistoryUpdateDto): JQueryPromise<void> {
            //call service server
            return nts.uk.request.ajax(paths.updateUnemployeeInsuranceRateHistory, unemployeeInsuranceHistoryUpdateDto);
        }

        //connection service update history
        export function updateAccidentInsuranceRateHistory(accidentInsuranceHistoryUpdateDto: model.AccidentInsuranceHistoryUpdateDto) {
            //call service server
            return nts.uk.request.ajax(paths.updateAccidentInsuranceRateHistory, accidentInsuranceHistoryUpdateDto);
        }

        //connection service delete AccidentInsuranceRate
        export function deleteAccidentInsuranceRate(accidentInsuranceRateDeleteDto: model.AccidentInsuranceRateDeleteDto)
            : JQueryPromise<void> {
            var data = { accidentInsuranceRateDeleteDto: accidentInsuranceRateDeleteDto };
            //call service server
            return nts.uk.request.ajax(paths.deleteAccidentInsuranceRate, data);
        }

        //connection service delete UnemployeeInsurance
        export function deleteUnemployeeInsurance(unemployeeInsuranceDeleteDto: model.UnemployeeInsuranceDeleteDto)
            : JQueryPromise<void> {
            return nts.uk.request.ajax(paths.deleteUnemployeeInsurance, unemployeeInsuranceDeleteDto);
        }

        //Function connection service add Unemployee Insurance Rate
        export function addUnemployeeInsuranceRate(
            unemployeeInsuranceRateModel: viewmodel.UnemployeeInsuranceRateModel)
            : JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            var data = {
                unemployeeInsuranceRate
                : service.convertUnemployeeInsuranceRateModelDTO(unemployeeInsuranceRateModel)
            };
            return nts.uk.request.ajax(paths.addUnemployeeInsuranceRate, data);
        }

        //Function connection service update Unemployee Insurance Rate
        export function updateUnemployeeInsuranceRate(
            unemployeeInsuranceRateModel: viewmodel.UnemployeeInsuranceRateModel): JQueryPromise<void> {
            var data = {
                unemployeeInsuranceRate: service.convertUnemployeeInsuranceRateModelDTO(unemployeeInsuranceRateModel)
            };
            return nts.uk.request.ajax(paths.updateUnemployeeInsuranceRate, data);
        }

        //Function connection service FindAll Labor Insurance Office
        export function findAllUnemployeeInsuranceRateHistory(): JQueryPromise<model.UnemployeeInsuranceHistoryDto[]> {
            var dfd = $.Deferred<model.UnemployeeInsuranceHistoryDto[]>();
            nts.uk.request.ajax(paths.findAllUnemployeeInsuranceRateHistory)
                .done(function(res: model.HistoryInsuranceFindOutDto[]) {
                    var convertRes: model.UnemployeeInsuranceHistoryDto[];
                    convertRes = [];
                    for (var itemRes of res) {
                        var UnemployeeInsuranceHistoryDto: model.UnemployeeInsuranceHistoryDto;
                        UnemployeeInsuranceHistoryDto = new model.UnemployeeInsuranceHistoryDto();
                        UnemployeeInsuranceHistoryDto.setDataHistory(itemRes);
                        convertRes.push(UnemployeeInsuranceHistoryDto);
                    }
                    dfd.resolve(convertRes);
                }).fail(function(res) {
                    dfd.reject(res);
                });
            return dfd.promise();
        }

        //Function connection service Find History By historyId
        export function findUnemployeeInsuranceRateHistory(historyId: string)
            : JQueryPromise<model.UnemployeeInsuranceHistoryDto> {
            var dfd = $.Deferred<model.UnemployeeInsuranceHistoryDto>();
            nts.uk.request.ajax(paths.findUnemployeeInsuranceRateHistory + "/" + historyId)
                .done(function(res: model.HistoryInsuranceFindOutDto) {
                    var UnemployeeInsuranceHistoryDto: model.UnemployeeInsuranceHistoryDto;
                    UnemployeeInsuranceHistoryDto = new model.UnemployeeInsuranceHistoryDto();
                    UnemployeeInsuranceHistoryDto.setDataHistory(res);
                    dfd.resolve(UnemployeeInsuranceHistoryDto);
                }).fail(function(res) {
                    dfd.reject(res);
                });
            return dfd.promise();
        }

        //Funtion connection service detail by historyId 
        export function detailUnemployeeInsuranceRateHistory(historyId: string)
            : JQueryPromise<model.UnemployeeInsuranceRateDto> {
            var dfd = $.Deferred<model.UnemployeeInsuranceRateDto>();
            nts.uk.request.ajax(paths.detailUnemployeeInsuranceRateHistory + "/" + historyId)
                .done(function(res: model.UnemployeeInsuranceRateFindOutDto) {
                    var unemployeeInsuranceRateDto: model.UnemployeeInsuranceRateDto;
                    unemployeeInsuranceRateDto = new model.UnemployeeInsuranceRateDto();
                    unemployeeInsuranceRateDto.historyInsurance = new model.UnemployeeInsuranceHistoryDto();
                    unemployeeInsuranceRateDto.historyInsurance.historyId = res.historyInsurance.historyId;
                    unemployeeInsuranceRateDto.historyInsurance.startMonth = res.historyInsurance.startMonth;
                    unemployeeInsuranceRateDto.historyInsurance.endMonth = res.historyInsurance.endMonth;
                    unemployeeInsuranceRateDto.rateItems = res.rateItems;
                    unemployeeInsuranceRateDto.version = res.version;
                    dfd.resolve(unemployeeInsuranceRateDto);
                }).fail(function(res) {
                    dfd.reject(res);
                });
            return dfd.promise();

        }

        //Function connection service copy 
        export function copyAccidentInsuranceRate(accidentInsuranceRateCopyDto: model.AccidentInsuranceRateCopyDto): JQueryPromise<void> {
            return nts.uk.request.ajax(paths.copyAccidentInsuranceRate, accidentInsuranceRateCopyDto);
        }

        //Function connection service add Accident Insurance Rate
        export function addAccidentInsuranceRate(
            accidentInsuranceRateModel: viewmodel.AccIRModel): JQueryPromise<void> {
            var data = {
                accidentInsuranceRate: service.convertAccidentInsuranceRateModelDTO(accidentInsuranceRateModel)
            };
            return nts.uk.request.ajax(paths.addAccidentInsuranceRate, data);
        }


        //Function connection service copy 
        export function copyUnemployeeInsuranceRate(unemployeeInsuranceRateCopyDto: model.UnemployeeInsuranceRateCopyDto): JQueryPromise<void> {
            return nts.uk.request.ajax(paths.copyUnemployeeInsuranceRate, unemployeeInsuranceRateCopyDto);
        }


        //Function connection service update Accident Insurance Rate
        export function updateAccidentInsuranceRate(
            accidentInsuranceRateModel: viewmodel.AccIRModel): JQueryPromise<void> {
            var data = {
                accidentInsuranceRate: service.convertAccidentInsuranceRateModelDTO(accidentInsuranceRateModel)
            };
            return nts.uk.request.ajax(paths.updateAccidentInsuranceRate, data);
        }

        //Function connection service find All AccidentInsuranceHistory 
        export function findAllAccidentInsuranceRateHistory(): JQueryPromise<model.AccidentInsuranceHistoryDto[]> {
            var dfd = $.Deferred<model.AccidentInsuranceHistoryDto[]>();
            nts.uk.request.ajax(paths.findAllAccidentInsuranceRateHistory)
                .done(function(res: model.HistoryInsuranceFindOutDto[]) {
                    var convertRes: model.AccidentInsuranceHistoryDto[];
                    convertRes = [];
                    for (var itemRes of res) {
                        var AccidentInsuranceHistoryDto: model.AccidentInsuranceHistoryDto;
                        AccidentInsuranceHistoryDto = new model.AccidentInsuranceHistoryDto();
                        AccidentInsuranceHistoryDto.setDataHistory(itemRes);
                        convertRes.push(AccidentInsuranceHistoryDto);
                    }
                    dfd.resolve(convertRes);
                }).fail(function(res) {
                    dfd.reject(res);
                });
            return dfd.promise();
        }

        //Function connection service find AccidentInsuranceHistory
        export function findAccidentInsuranceRateHistory(historyId: string)
            : JQueryPromise<model.AccidentInsuranceHistoryDto> {
            var dfd = $.Deferred<model.AccidentInsuranceHistoryDto>();
            nts.uk.request.ajax(paths.findAccidentInsuranceRateHistory + "/" + historyId)
                .done(function(res: model.HistoryInsuranceFindOutDto) {
                    var AccidentInsuranceHistoryDto: model.AccidentInsuranceHistoryDto;
                    AccidentInsuranceHistoryDto = new model.AccidentInsuranceHistoryDto();
                    AccidentInsuranceHistoryDto.setDataHistory(res);
                    dfd.resolve(AccidentInsuranceHistoryDto);
                    //xyz
                }).fail(function(res) {
                    dfd.reject(res);
                });
            return dfd.promise();
        }

        //Function connection service detail AccidentInsuranceHistory
        export function findAccidentInsuranceRate(historyId: string): JQueryPromise<model.AccidentInsuranceRateDto> {
            var dfd = $.Deferred<model.AccidentInsuranceRateDto>();
            nts.uk.request.ajax(paths.findAccidentInsuranceRate + "/" + historyId)
                .done(function(res: model.AccidentInsuranceRateFindOutDto) {
                    var accidentInsuranceRateDto: model.AccidentInsuranceRateDto;
                    accidentInsuranceRateDto = new model.AccidentInsuranceRateDto();
                    accidentInsuranceRateDto.historyInsurance = new model.AccidentInsuranceHistoryDto();
                    accidentInsuranceRateDto.historyInsurance.historyId = res.historyInsurance.historyId;
                    accidentInsuranceRateDto.historyInsurance.endMonth = res.historyInsurance.endMonth;
                    accidentInsuranceRateDto.historyInsurance.startMonth = res.historyInsurance.startMonth;
                    accidentInsuranceRateDto.rateItems = res.rateItems;
                    accidentInsuranceRateDto.version = res.version;
                    dfd.resolve(accidentInsuranceRateDto);
                    //xyz
                }).fail(function(res: any) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        //Function convert Model => DTO (AccidentInsuranceHistoryDto)
        export function convertUnemployeeInsuranceHistoryDto(UnemployeeInsuranceHistoryModel: viewmodel.UnemployeeInsuranceHistoryModel)
            : model.UnemployeeInsuranceHistoryDto {
            var historyDto: model.UnemployeeInsuranceHistoryDto;
            historyDto = new model.UnemployeeInsuranceHistoryDto();
            historyDto.historyId = UnemployeeInsuranceHistoryModel.historyId();
            historyDto.startMonth = UnemployeeInsuranceHistoryModel.startMonth();
            historyDto.endMonth = UnemployeeInsuranceHistoryModel.endMonth();
            return historyDto;
        }

        //Function convert Model => DTO (UnemployeeInsuranceRateItemSettingModel)
        export function convertUnemployeeInsuranceRateItemSettingModelDTO(
            unemployeeInsuranceRateItemSettingModel: viewmodel.UnemployeeInsuranceRateItemSettingModel)
            : model.UnemployeeInsuranceRateItemSettingDto {
            var unemployeeInsuranceRateItemSettingDto: model.UnemployeeInsuranceRateItemSettingDto;
            unemployeeInsuranceRateItemSettingDto
                = new model.UnemployeeInsuranceRateItemSettingDto(unemployeeInsuranceRateItemSettingModel.roundAtr(),
                    unemployeeInsuranceRateItemSettingModel.rate());
            return unemployeeInsuranceRateItemSettingDto;
        }

        //Function convert Model => DTO (UnemployeeInsuranceRateItemModel)
        export function convertUnemployeeInsuranceRateItemModelDTO(careerGroup: number,
            unemployeeInsuranceRateItemModel: viewmodel.UnemployeeInsuranceRateItemModel):
            model.UnemployeeInsuranceRateItemDto {
            var unemployeeInsuranceRateItemDto: model.UnemployeeInsuranceRateItemDto;
            unemployeeInsuranceRateItemDto = new model.UnemployeeInsuranceRateItemDto(careerGroup,
                service.convertUnemployeeInsuranceRateItemSettingModelDTO(unemployeeInsuranceRateItemModel.companySetting),
                service.convertUnemployeeInsuranceRateItemSettingModelDTO(unemployeeInsuranceRateItemModel.personalSetting));
            return unemployeeInsuranceRateItemDto;
        }

        //Function convert Model => DTO (UnemployeeInsuranceRateModel)
        export function convertUnemployeeInsuranceRateModelDTO(
            unemployeeInsuranceRateModel: viewmodel.UnemployeeInsuranceRateModel)
            : model.UnemployeeInsuranceRateDto {
            var unemployeeInsuranceRateDto: model.UnemployeeInsuranceRateDto;
            unemployeeInsuranceRateDto = new model.UnemployeeInsuranceRateDto();
            unemployeeInsuranceRateDto.historyInsurance
                = convertUnemployeeInsuranceHistoryDto(unemployeeInsuranceRateModel.unemployeeInsuranceHistoryModel);
            unemployeeInsuranceRateDto.version = unemployeeInsuranceRateModel.version();
            unemployeeInsuranceRateDto.rateItems = [];
            unemployeeInsuranceRateDto.rateItems
                .push(service.convertUnemployeeInsuranceRateItemModelDTO(
                    model.CareerGroupDto.Agroforestry,
                    unemployeeInsuranceRateModel.unemployeeInsuranceRateItemAgroforestryModel));
            unemployeeInsuranceRateDto.rateItems
                .push(service.convertUnemployeeInsuranceRateItemModelDTO(
                    model.CareerGroupDto.Contruction, unemployeeInsuranceRateModel.
                        unemployeeInsuranceRateItemContructionModel));
            unemployeeInsuranceRateDto.rateItems
                .push(service.convertUnemployeeInsuranceRateItemModelDTO(
                    model.CareerGroupDto.Other,
                    unemployeeInsuranceRateModel.unemployeeInsuranceRateItemOtherModel));
            return unemployeeInsuranceRateDto;
        }

        //Function convert Model => DTO (InsuBizRateItemModel) 
        export function convertInsuBizRateItemModelDTO(insuBizType: number,
            accidentInsuranceRateDetailModel: viewmodel.AccIRDetailModel)
            : model.InsuBizRateItemDto {
            var insuBizRateItemDto: model.InsuBizRateItemDto;
            insuBizRateItemDto = new model.InsuBizRateItemDto(insuBizType,
                accidentInsuranceRateDetailModel.insuRate(), accidentInsuranceRateDetailModel.insuRound(),
                accidentInsuranceRateDetailModel.insuranceBusinessType());
            return insuBizRateItemDto;
        }

        //Function convert Model => DTO (AccidentInsuranceHistoryDto)
        export function convertAccidentInsuranceHistoryDto(
            accidentInsuranceRateHistoryModel: viewmodel.AccIRHistoryModel)
            : model.AccidentInsuranceHistoryDto {
            var accidentInsuranceHistoryDto: model.AccidentInsuranceHistoryDto;
            accidentInsuranceHistoryDto = new model.AccidentInsuranceHistoryDto();
            accidentInsuranceHistoryDto.historyId = accidentInsuranceRateHistoryModel.historyId();
            accidentInsuranceHistoryDto.startMonthRage = accidentInsuranceRateHistoryModel.startMonthRage();
            accidentInsuranceHistoryDto.startMonth = accidentInsuranceRateHistoryModel.startMonth();
            accidentInsuranceHistoryDto.endMonthRage = accidentInsuranceRateHistoryModel.endMonthRage();
            accidentInsuranceHistoryDto.endMonth = accidentInsuranceRateHistoryModel.endMonth();
            return accidentInsuranceHistoryDto;
        }

        //Function convert Model => DTO (AccidentInsuranceModel)
        export function convertAccidentInsuranceRateModelDTO(
            accidentInsuranceRateModel: viewmodel.AccIRModel)
            : model.AccidentInsuranceRateDto {
            var accidentInsuranceRateDto: model.AccidentInsuranceRateDto;
            accidentInsuranceRateDto = new model.AccidentInsuranceRateDto();
            accidentInsuranceRateDto.historyInsurance
                = convertAccidentInsuranceHistoryDto(accidentInsuranceRateModel.accidentInsuranceRateHistoryModel);
            accidentInsuranceRateDto.version = accidentInsuranceRateModel.version();
            accidentInsuranceRateDto.rateItems = [];
            accidentInsuranceRateDto.rateItems
                .push(service.convertInsuBizRateItemModelDTO(model.BusinessTypeEnumDto.Biz1St,
                    accidentInsuranceRateModel.biz1StModel));
            accidentInsuranceRateDto.rateItems
                .push(service.convertInsuBizRateItemModelDTO(model.BusinessTypeEnumDto.Biz2Nd,
                    accidentInsuranceRateModel.biz2NdModel));
            accidentInsuranceRateDto.rateItems
                .push(service.convertInsuBizRateItemModelDTO(model.BusinessTypeEnumDto.Biz3Rd,
                    accidentInsuranceRateModel.biz3RdModel));
            accidentInsuranceRateDto.rateItems
                .push(service.convertInsuBizRateItemModelDTO(model.BusinessTypeEnumDto.Biz4Th,
                    accidentInsuranceRateModel.biz4ThModel));
            accidentInsuranceRateDto.rateItems
                .push(service.convertInsuBizRateItemModelDTO(model.BusinessTypeEnumDto.Biz5Th,
                    accidentInsuranceRateModel.biz5ThModel));
            accidentInsuranceRateDto.rateItems
                .push(service.convertInsuBizRateItemModelDTO(model.BusinessTypeEnumDto.Biz6Th,
                    accidentInsuranceRateModel.biz6ThModel));
            accidentInsuranceRateDto.rateItems
                .push(service.convertInsuBizRateItemModelDTO(model.BusinessTypeEnumDto.Biz7Th,
                    accidentInsuranceRateModel.biz7ThModel));
            accidentInsuranceRateDto.rateItems
                .push(service.convertInsuBizRateItemModelDTO(model.BusinessTypeEnumDto.Biz8Th,
                    accidentInsuranceRateModel.biz8ThModel));
            accidentInsuranceRateDto.rateItems
                .push(service.convertInsuBizRateItemModelDTO(model.BusinessTypeEnumDto.Biz9Th,
                    accidentInsuranceRateModel.biz9ThModel));
            accidentInsuranceRateDto.rateItems
                .push(service.convertInsuBizRateItemModelDTO(model.BusinessTypeEnumDto.Biz10Th,
                    accidentInsuranceRateModel.biz10ThModel));
            return accidentInsuranceRateDto;
        }

        //Function find all Insurance Business Type
        export function findAllInsuranceBusinessType(): JQueryPromise<model.InsuranceBusinessTypeDto> {
            var dfd = $.Deferred<model.InsuranceBusinessTypeDto>();
            nts.uk.request.ajax(paths.findAllInsuranceBusinessType)
                .done(function(res: model.InsuranceBusinessTypeDto) {
                    dfd.resolve(res);
                    //xyz
                }).fail(function(res: any) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }

        export module model {

            export class YearMonth {

                year: number;
                month: number;

                constructor(year: number, month: number) {
                    this.year = year;
                    this.month = month;
                }
            }

            export class MonthRange {

                startMonth: YearMonth;
                endMonth: YearMonth;

                constructor(startMonth: YearMonth, endMonth: YearMonth) {
                    this.startMonth = startMonth;
                    this.endMonth = endMonth;
                }
            }

            export class RoundingMethodDto {

                code: number;
                name: string;

                constructor(code: number, name: string) {
                    this.code = code;
                    this.name = name;
                }
            }

            export class UnemployeeInsuranceRateItemSettingDto {

                roundAtr: number;
                rate: number;

                constructor(roundAtr: number, rate: number) {
                    this.roundAtr = roundAtr;
                    this.rate = rate;
                }
            }

            export class UnemployeeInsuranceRateItemDto {

                careerGroup: number;
                companySetting: UnemployeeInsuranceRateItemSettingDto;
                personalSetting: UnemployeeInsuranceRateItemSettingDto;

                constructor(careerGroup: number, companySetting: UnemployeeInsuranceRateItemSettingDto,
                    personalSetting: UnemployeeInsuranceRateItemSettingDto) {
                    this.careerGroup = careerGroup;
                    this.companySetting = companySetting;
                    this.personalSetting = personalSetting;
                }
            }

            export class UnemployeeInsuranceRateCopyDto {
                /** The history id copy. */
                historyIdCopy: string;

                /** The start month. */
                startMonth: number;

                /** The add new. */
                addNew: boolean;

                constructor() {
                    this.historyIdCopy = '';
                    this.startMonth = 0;
                    this.addNew = false;
                }
            }
            export class AccidentInsuranceRateCopyDto {
                /** The history id copy. */
                historyIdCopy: string;

                /** The start month. */
                startMonth: number;

                /** The add new. */
                addNew: boolean;

                constructor() {
                    this.historyIdCopy = '';
                    this.startMonth = 0;
                    this.addNew = false;
                }
            }

            export class HistoryInsuranceDto {

                historyId: string;
                startMonthRage: string;
                startMonth: number;
                endMonthRage: string;
                endMonth: number;
                inforMonthRage: string;

                constructor() {
                    this.historyId = '';
                    this.startMonthRage = '';
                    this.endMonthRage = '';
                    this.inforMonthRage = '';
                }

                setDataHistory(historyData: HistoryInsuranceFindOutDto) {
                    this.historyId = historyData.historyId;
                    this.startMonthRage = nts.uk.time.formatYearMonth(historyData.startMonth);
                    this.endMonthRage = nts.uk.time.formatYearMonth(historyData.endMonth);
                    this.inforMonthRage = this.startMonthRage + ' ~ ' + this.endMonthRage;
                    this.startMonth = historyData.startMonth;
                    this.endMonth = historyData.endMonth;
                }
            }

            export class HistoryInsuranceFindOutDto {
                historyId: string;
                startMonth: number;
                endMonth: number;
            }

            export class HistoryInsuranceInDto {
                historyId: string;
                startMonth: number;
                endMonth: number;
            }

            export class UnemployeeInsuranceHistoryDto extends HistoryInsuranceDto {
            }

            export class UnemployeeInsuranceHistoryFindInDto {
                historyId: string;
                companyCode: string;
            }

            export class UnemployeeInsuranceHistoryUpdateDto {
                historyId: string;
                startMonth: number;
                endMonth: number;
            }
            export class AccidentInsuranceHistoryUpdateDto {
                historyId: string;
                startMonth: number;
                endMonth: number;
            }

            export class UnemployeeInsuranceRateFindOutDto {
                historyInsurance: HistoryInsuranceFindOutDto;
                rateItems: UnemployeeInsuranceRateItemDto[];
                version: number;
            }
            export class AccidentInsuranceRateDeleteDto {
                code: string;
                version: number;
            }
            export class UnemployeeInsuranceDeleteDto {
                code: string;
                version: number;
            }
            export class UnemployeeInsuranceRateDto {
                historyInsurance: UnemployeeInsuranceHistoryDto;
                rateItems: UnemployeeInsuranceRateItemDto[];
                version: number;
            }

            export class AccidentInsuranceHistoryDto extends HistoryInsuranceDto {
            }

            export class UnemployeeInsuranceFindInDto {
                historyId: string;
                companyCode: string;
            }

            export class AccidentInsuranceRateDto {
                historyInsurance: AccidentInsuranceHistoryDto;
                rateItems: InsuBizRateItemDto[];
                version: number;
            }

            export class AccidentInsuranceRateFindOutDto {
                historyInsurance: HistoryInsuranceFindOutDto;
                rateItems: InsuBizRateItemDto[];
                version: number;
            }

            export class AccidentInsuranceRateHistoryFindInDto {
                historyId: string;
                companyCode: string;
            }

            export class InsuBizRateItemDto {

                /** The insu biz type. */
                insuBizType: number;
                /** The insu rate. */
                insuRate: number;
                /** The insu round. */
                insuRound: number;
                /** The insurance business type. */
                insuranceBusinessType: string;

                constructor(insuBizType: number, insuRate: number, insuRound: number, insuranceBusinessType: string) {
                    this.insuBizType = insuBizType;
                    this.insuRate = insuRate;
                    this.insuRound = insuRound;
                    this.insuranceBusinessType = insuranceBusinessType;
                }
            }

            export class InsuranceBusinessType {

                /** The biz order. */
                bizOrder: number;
                /** The biz name. */
                bizName: string;

                constructor(bizOrder: number, bizName: string) {
                    this.bizOrder = bizOrder;
                    this.bizName = bizName;
                }
            }

            export enum CareerGroupDto {
                Agroforestry = 0,
                Other = 1,
                Contruction = 2
            }

            export enum BusinessTypeEnumDto {
                /** The Biz 1 st. */
                Biz1St = 1,
                /** The Biz 2 nd. */
                Biz2Nd = 2,
                /** The Biz 3 rd. */
                Biz3Rd = 3,
                /** The Biz 4 th. */
                Biz4Th = 4,
                /** The Biz 5 th. */
                Biz5Th = 5,
                /** The Biz 6 th. */
                Biz6Th = 6,
                /** The Biz 7 th. */
                Biz7Th = 7,
                /** The Biz 8 th. */
                Biz8Th = 8,
                /** The Biz 9 th. */
                Biz9Th = 9,
                /** The Biz 10 th. */
                Biz10Th = 10
            }

            export enum TypeHistory {
                HistoryUnemployee = 1,
                HistoryAccident = 2
            }

            export enum TypeActionInsuranceRate {
                add = 1,
                update = 2
            }

            export class InsuranceBusinessTypeDto {

                bizNameBiz1St: string;
                /** The biz name biz 2 nd. */
                bizNameBiz2Nd: string;
                /** The biz name biz 3 rd. */
                bizNameBiz3Rd: string;
                /** The biz name biz 4 th. */
                bizNameBiz4Th: string;
                /** The biz name biz 5 th. */
                bizNameBiz5Th: string;
                /** The biz name biz 6 th. */
                bizNameBiz6Th: string;
                /** The biz name biz 7 th. */
                bizNameBiz7Th: string;
                /** The biz name biz 8 th. */
                bizNameBiz8Th: string;
                /** The biz name biz 9 th. */
                bizNameBiz9Th: string;
                /** The biz name biz 10 th. */
                bizNameBiz10Th: string;
                /** version*/
                version: number;

                constructor(bizNameBiz1St: string,
                    bizNameBiz2Nd: string, bizNameBiz3Rd: string,
                    bizNameBiz4Th: string, bizNameBiz5Th: string,
                    bizNameBiz6Th: string, bizNameBiz7Th: string,
                    bizNameBiz8Th: string, bizNameBiz9Th: string,
                    bizNameBiz10Th: string, version: number) {
                    this.bizNameBiz1St = bizNameBiz1St;
                    this.bizNameBiz2Nd = bizNameBiz2Nd;
                    this.bizNameBiz3Rd = bizNameBiz3Rd;
                    this.bizNameBiz4Th = bizNameBiz4Th;
                    this.bizNameBiz5Th = bizNameBiz5Th;
                    this.bizNameBiz6Th = bizNameBiz6Th;
                    this.bizNameBiz7Th = bizNameBiz7Th;
                    this.bizNameBiz8Th = bizNameBiz8Th;
                    this.bizNameBiz9Th = bizNameBiz9Th;
                    this.bizNameBiz10Th = bizNameBiz10Th;
                    this.version = version;
                }
            }
        }
    }
}