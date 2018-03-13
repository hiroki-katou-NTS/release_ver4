module nts.uk.at.view.kmk003.a {
    
    import EmTimeZoneSetDto = service.model.common.EmTimeZoneSetDto;
    import OverTimeOfTimeZoneSetDto = service.model.common.OverTimeOfTimeZoneSetDto;
    import HDWorkTimeSheetSettingDto = service.model.common.HDWorkTimeSheetSettingDto;
    import StampReflectTimezoneDto = service.model.common.StampReflectTimezoneDto;
    import OverTimeCalcNoBreakDto = service.model.common.OverTimeCalcNoBreakDto;
    import ExceededPredAddVacationCalcDto = service.model.common.ExceededPredAddVacationCalcDto;
    import FixedWorkCalcSettingDto = service.model.common.FixedWorkCalcSettingDto;
    
    import FixOffdayWorkTimezoneDto = service.model.fixedset.FixOffdayWorkTimezoneDto;
    import FixedWorkTimezoneSetDto = service.model.fixedset.FixedWorkTimezoneSetDto;
    import FixHalfDayWorkTimezoneDto = service.model.fixedset.FixHalfDayWorkTimezoneDto;
    
    import WorkTimezoneCommonSetModel = nts.uk.at.view.kmk003.a.viewmodel.common.WorkTimezoneCommonSetModel;
    import TimeRangeModel = nts.uk.at.view.kmk003.a.viewmodel.common.TimeRangeModel;
    import FixedWorkRestSetModel = nts.uk.at.view.kmk003.a.viewmodel.common.FixedWorkRestSetModel;
    import HDWorkTimeSheetSettingModel = nts.uk.at.view.kmk003.a.viewmodel.common.HDWorkTimeSheetSettingModel;
    import StampReflectTimezoneModel = nts.uk.at.view.kmk003.a.viewmodel.common.StampReflectTimezoneModel;
    import DeductionTimeModel = nts.uk.at.view.kmk003.a.viewmodel.common.DeductionTimeModel;
    import EmTimeZoneSetModel = nts.uk.at.view.kmk003.a.viewmodel.common.EmTimeZoneSetModel;
    import OverTimeOfTimeZoneSetModel = nts.uk.at.view.kmk003.a.viewmodel.common.OverTimeOfTimeZoneSetModel;
    import FixedWorkTimezoneSetModel = nts.uk.at.view.kmk003.a.viewmodel.common.FixedWorkTimezoneSetModel;
    import FixedWorkCalcSettingModel = nts.uk.at.view.kmk003.a.viewmodel.common.FixedWorkCalcSettingModel;
    import OtherFlowColumnSetting = nts.uk.at.view.kmk003.a.viewmodel.common.OtherFlowColumnSetting;
    import OffdayWorkTimeConverter = nts.uk.at.view.kmk003.a.viewmodel.common.OffdayWorkTimeConverter;
    import FixRestTimezoneSetModel = nts.uk.at.view.kmk003.a.viewmodel.common.FixRestTimezoneSetModel;
    import BaseDataModel = nts.uk.at.view.kmk003.a.viewmodel.common.BaseDataModel;
    import FixedTableDataConverterNew = nts.uk.at.view.kmk003.a.viewmodel.common.FixedTableDataConverterNew;
    
    import FixedWorkSettingDto = service.model.fixedset.FixedWorkSettingDto;
    export module viewmodel {
        export module fixedset {
            
            export class FixOffdayWorkTimezoneModel extends OffdayWorkTimeConverter {
                restTimezone: FixRestTimezoneSetModel;
                lstWorkTimezone: KnockoutObservableArray<HDWorkTimeSheetSettingModel>;
                
                constructor() {
                    super();
                    this.restTimezone = new FixRestTimezoneSetModel();
                    this.lstWorkTimezone = this.originalList;
                }
                
                updateData(data: FixOffdayWorkTimezoneDto) {
                    this.restTimezone.updateData(data.restTimezone);
                    this.updateHDTimezone(data.lstWorkTimezone);
                }
                
                updateHDTimezone(lstWorkTimezone: HDWorkTimeSheetSettingDto[]) {
                    var dataModelHDTimezone: HDWorkTimeSheetSettingModel[] = [];
                    for (var dataDTO of lstWorkTimezone) {
                        var dataModel: HDWorkTimeSheetSettingModel = new HDWorkTimeSheetSettingModel();
                        dataModel.updateData(dataDTO);
                        dataModelHDTimezone.push(dataModel);
                    }
                    this.lstWorkTimezone(dataModelHDTimezone);
                }

                toDto(): FixOffdayWorkTimezoneDto {
                    let lstWorkTimezone: HDWorkTimeSheetSettingDto[] = _.map(this.lstWorkTimezone(), (dataModel) => dataModel.toDto());

                    let dataDTO: FixOffdayWorkTimezoneDto = {
                        restTimezone: this.restTimezone.toDto(),
                        lstWorkTimezone: lstWorkTimezone
                    };
                    return dataDTO;
                }

                resetData() {
                    this.restTimezone.resetData();
                    this.lstWorkTimezone([]);
                }

            }

            export class FixHalfDayWorkTimezoneModel extends BaseDataModel {
                restTimezone: FixRestTimezoneSetModel;
                workTimezone: FixedWorkTimezoneSetModel;
                dayAtr: KnockoutObservable<number>;
                
                constructor() {
                    super();
                    this.restTimezone = new FixRestTimezoneSetModel();
                    this.workTimezone = new FixedWorkTimezoneSetModel();
                    this.dayAtr = ko.observable(0);
                }

                static getDefaultData(): Array<FixHalfDayWorkTimezoneModel> {
                    let oneday = new FixHalfDayWorkTimezoneModel();
                    oneday.dayAtr(0);
                    let morning = new FixHalfDayWorkTimezoneModel();
                    morning.dayAtr(1);
                    let afternoon = new FixHalfDayWorkTimezoneModel();
                    afternoon.dayAtr(2);
                    let list: any[] = [];
                    list.push(oneday);
                    list.push(morning);
                    list.push(afternoon);
                    return list;
                }
                
                updateData(data: FixHalfDayWorkTimezoneDto) {
                    this.restTimezone.updateData(data.restTimezone);
                    this.workTimezone.updateData(data.workTimezone);
                    this.dayAtr(data.dayAtr);
                }
                
                toDto(): FixHalfDayWorkTimezoneDto {
                    let dataDTO: FixHalfDayWorkTimezoneDto = {
                        restTimezone: this.restTimezone.toDto(),
                        workTimezone: this.workTimezone.toDto(),
                        dayAtr: this.dayAtr()
                    };
                    return dataDTO;
                }
                resetData(){
                    this.restTimezone.resetData();
                    this.workTimezone.resetData();    
                }
            }
            
            export class FixedWorkSettingModel {
                workTimeCode: KnockoutObservable<string>;
                offdayWorkTimezone: FixOffdayWorkTimezoneModel;
                commonSetting: WorkTimezoneCommonSetModel;
                useHalfDayShift: KnockoutObservable<boolean>;
                fixedWorkRestSetting: FixedWorkRestSetModel;
                lstHalfDayWorkTimezone: FixHalfDayWorkTimezoneModel[];
                lstStampReflectTimezone: StampReflectTimezoneModel[];
                legalOTSetting: KnockoutObservable<number>;
                calculationSetting: FixedWorkCalcSettingModel;
                
                constructor() {
                    this.workTimeCode = ko.observable('');
                    this.offdayWorkTimezone = new FixOffdayWorkTimezoneModel();
                    this.commonSetting = new WorkTimezoneCommonSetModel();
                    this.useHalfDayShift = ko.observable(false);
                    this.fixedWorkRestSetting = new FixedWorkRestSetModel();
                    this.lstHalfDayWorkTimezone = FixHalfDayWorkTimezoneModel.getDefaultData();
                    this.lstStampReflectTimezone = [];
                    this.legalOTSetting = ko.observable(0);
                    // Update phase 2
                    this.calculationSetting = new FixedWorkCalcSettingModel();
                }

                public getHDWtzOneday(): FixHalfDayWorkTimezoneModel {
                    let self = this;
                    return _.find(self.lstHalfDayWorkTimezone, time => time.dayAtr() == 0);
                }
                public getHDWtzMorning(): FixHalfDayWorkTimezoneModel {
                    let self = this;
                    return _.find(self.lstHalfDayWorkTimezone, time => time.dayAtr() == 1);
                }
                public getHDWtzAfternoon(): FixHalfDayWorkTimezoneModel {
                    let self = this;
                    return _.find(self.lstHalfDayWorkTimezone, time => time.dayAtr() == 2);
                }

                updateData(data: FixedWorkSettingDto) {
                    this.workTimeCode(data.workTimeCode);                                        
                    this.offdayWorkTimezone.updateData(data.offdayWorkTimezone);
                    this.commonSetting.updateData(data.commonSetting);
                    this.useHalfDayShift(data.useHalfDayShift);                    
                    this.fixedWorkRestSetting.updateData(data.fixedWorkRestSetting);                                       
                    this.updateListHalfDay(data.lstHalfDayWorkTimezone);
                    this.lstStampReflectTimezone = _.map(data.lstStampReflectTimezone, (dataDTO) => {
                        let dataModel: StampReflectTimezoneModel = new StampReflectTimezoneModel();
                        dataModel.updateData(dataDTO);
                        return dataModel;
                    });                                    
                    this.legalOTSetting(data.legalOTSetting);
                    // Update phase 2
                    this.calculationSetting.updateData(data.calculationSetting);
                }

                updateListHalfDay(lstHalfDayWorkTimezone: FixHalfDayWorkTimezoneDto[]): void {
                    let self = this;
                    _.forEach(lstHalfDayWorkTimezone, item => {
                        switch (item.dayAtr) {
                            case 0:
                                this.getHDWtzOneday().updateData(item);
                                break;
                            case 1:
                                this.getHDWtzMorning().updateData(item);
                                break;
                            case 2:
                                this.getHDWtzAfternoon().updateData(item);
                                break;
                        }
                    });
                }
                
                toDto(commonSetting: WorkTimezoneCommonSetModel): FixedWorkSettingDto {
                    let lstHalfDayWorkTimezone: FixHalfDayWorkTimezoneDto[] = _.map(this.lstHalfDayWorkTimezone, (dataModel) => dataModel.toDto());
                    let lstStampReflectTimezone: StampReflectTimezoneDto[] = _.map(this.lstStampReflectTimezone, (dataModel) => dataModel.toDto());
                    
                    let dataDTO: FixedWorkSettingDto = {
                        workTimeCode: this.workTimeCode(),                       
                        offdayWorkTimezone: this.offdayWorkTimezone.toDto(),
                        commonSetting: commonSetting.toDto(),
                        useHalfDayShift: this.useHalfDayShift(),
                        fixedWorkRestSetting: this.fixedWorkRestSetting.toDto(),
                        lstHalfDayWorkTimezone: lstHalfDayWorkTimezone,
                        lstStampReflectTimezone: lstStampReflectTimezone,
                        legalOTSetting: this.legalOTSetting(),
                        // Update phase 2
                        calculationSetting: this.calculationSetting.toDto()
                    };
                    return dataDTO;
                }
                
                resetData(){
                    this.offdayWorkTimezone.resetData();
                    this.commonSetting.resetData();
                    this.useHalfDayShift(false);
                    this.fixedWorkRestSetting.resetData();
                    this.getHDWtzOneday().resetData();
                    this.getHDWtzMorning().resetData();
                    this.getHDWtzAfternoon().resetData();
                    this.lstStampReflectTimezone = [];
                    //update ver7.2 
                    this.legalOTSetting(0);
                    // Update phase 2
                    this.calculationSetting.resetData();
                }
            }
        }
    }
}