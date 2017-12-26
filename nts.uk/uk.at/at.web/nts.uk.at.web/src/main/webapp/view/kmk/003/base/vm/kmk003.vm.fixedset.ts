module nts.uk.at.view.kmk003.a {
    
    import DeductionTimeDto = service.model.common.DeductionTimeDto;
    import EmTimeZoneSetDto = service.model.common.EmTimeZoneSetDto;
    import OverTimeOfTimeZoneSetDto = service.model.common.OverTimeOfTimeZoneSetDto;
    import HDWorkTimeSheetSettingDto = service.model.common.HDWorkTimeSheetSettingDto;
    import StampReflectTimezoneDto = service.model.common.StampReflectTimezoneDto;
    
    import FixedWorkSettingDto = service.model.fixedset.FixedWorkSettingDto;
    import FixOffdayWorkTimezoneDto = service.model.fixedset.FixOffdayWorkTimezoneDto;
    import FixRestTimezoneSetDto = service.model.fixedset.FixRestTimezoneSetDto;
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
    import TimeRangeModelConverter = nts.uk.at.view.kmk003.a.viewmodel.common.TimeRangeModelConverter;
    import FixedWorkTimezoneSetModel = nts.uk.at.view.kmk003.a.viewmodel.common.FixedWorkTimezoneSetModel;
    
    export module viewmodel {
        export module fixedset {
            
            export class FixOffdayWorkTimezoneModel {
                restTimezone: FixRestTimezoneSetModel;
                lstWorkTimezone: HDWorkTimeSheetSettingModel[];
                
                constructor() {
                    this.restTimezone = new FixRestTimezoneSetModel();
                    this.lstWorkTimezone = [];
                }
                
                updateData(data: FixOffdayWorkTimezoneDto) {
                    this.restTimezone.updateData(data.restTimezone);
                    this.updateHDTimezone(data.lstWorkTimezone);
                }
                
                updateHDTimezone(lstWorkTimezone: HDWorkTimeSheetSettingDto[]) {
                    for (var dataDTO of lstWorkTimezone) {
                        var dataModel: HDWorkTimeSheetSettingModel = this.getHDTimezoneByWorktimeNo(dataDTO.workTimeNo);
                        if (dataModel) {
                            dataModel.updateData(dataDTO);
                        }
                        else {
                            dataModel = new HDWorkTimeSheetSettingModel();
                            dataModel.updateData(dataDTO);
                            this.lstWorkTimezone.push(dataModel);
                        }
                    }
                }

                getHDTimezoneByWorktimeNo(worktimeNo: number): HDWorkTimeSheetSettingModel {
                    return _.find(this.lstWorkTimezone, hdtimezone => hdtimezone.workTimeNo() == worktimeNo);
                }
                toDto(): FixOffdayWorkTimezoneDto {
                    let lstWorkTimezone: HDWorkTimeSheetSettingDto[] = _.map(this.lstWorkTimezone, (dataModel) => dataModel.toDto());
                    
                    let dataDTO: FixOffdayWorkTimezoneDto = {
                        restTimezone: this.restTimezone.toDto(),
                        lstWorkTimezone: lstWorkTimezone
                    };
                    return dataDTO;
                }
                
                resetData() {
                    this.restTimezone.resetData();
                    this.lstWorkTimezone = [];
                }
            }
            
            export class FixHalfDayWorkTimezoneModel {
                restTimezone: FixRestTimezoneSetModel;
                workTimezone: FixedWorkTimezoneSetModel;
                dayAtr: KnockoutObservable<number>;
                
                constructor() {
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
            
            export class FixRestTimezoneSetModel extends TimeRangeModelConverter<DeductionTimeModel> {
                lstTimezone: KnockoutObservableArray<DeductionTimeModel>;
                
                constructor() {
                    super();
                    this.lstTimezone = ko.observableArray([]);
                }

                toListTimeRange(): KnockoutObservableArray<KnockoutObservable<TimeRangeModel>> {
                    let self = this;
                    let mapped = ko.observableArray(_.map(self.lstTimezone(), tz => self.toTimeRangeItem(tz.start(), tz.end())));
                    return mapped;
                }

                fromListTimeRange(newList: Array<KnockoutObservable<TimeRangeModel>>): Array<DeductionTimeModel> {
                    return _.map(newList, newVl => {
                        let vl = new DeductionTimeModel();
                        vl.start(newVl().startTime);
                        vl.end(newVl().endTime);
                        return vl;
                    });
                }
                
                updateData(data: FixRestTimezoneSetDto) {
                    let mapped = _.map(data.lstTimezone, (dataDTO) => {
                        let dataModel: DeductionTimeModel = new DeductionTimeModel();
                        dataModel.updateData(dataDTO);
                        return dataModel;
                    });  
                    this.lstTimezone(mapped);
                }
                
                toDto(): FixRestTimezoneSetDto {
                    let lstTimezone: DeductionTimeDto[] = _.map(this.lstTimezone(), dataModel => dataModel.toDto());
                    
                    let dataDTO: FixRestTimezoneSetDto = {
                        lstTimezone: lstTimezone
                    };
                    return dataDTO;
                }
                
                resetData(){
                    this.lstTimezone([]);    
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
                
                constructor() {
                    this.workTimeCode = ko.observable('');
                    this.offdayWorkTimezone = new FixOffdayWorkTimezoneModel();
                    this.commonSetting = new WorkTimezoneCommonSetModel();
                    this.useHalfDayShift = ko.observable(false);
                    this.fixedWorkRestSetting = new FixedWorkRestSetModel();
                    this.lstHalfDayWorkTimezone = FixHalfDayWorkTimezoneModel.getDefaultData();
                    this.lstStampReflectTimezone = [];
                    this.legalOTSetting = ko.observable(0);
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
                }

                updateListHalfDay(lstHalfDayWorkTimezone: FixHalfDayWorkTimezoneDto[]): void {
                    lstHalfDayWorkTimezone.sort(item => item.dayAtr);
                    this.getHDWtzOneday().updateData(lstHalfDayWorkTimezone[0]);
                    this.getHDWtzMorning().updateData(lstHalfDayWorkTimezone[1]);
                    this.getHDWtzAfternoon().updateData(lstHalfDayWorkTimezone[2]);
                }
                
                toDto(): FixedWorkSettingDto {
                    let lstHalfDayWorkTimezone: FixHalfDayWorkTimezoneDto[] = _.map(this.lstHalfDayWorkTimezone, (dataModel) => dataModel.toDto());
                    let lstStampReflectTimezone: StampReflectTimezoneDto[] = _.map(this.lstStampReflectTimezone, (dataModel) => dataModel.toDto());
                    
                    let dataDTO: FixedWorkSettingDto = {
                        workTimeCode: this.workTimeCode(),                       
                        offdayWorkTimezone: this.offdayWorkTimezone.toDto(),
                        commonSetting: this.commonSetting.toDto(),
                        useHalfDayShift: this.useHalfDayShift(),
                        fixedWorkRestSetting: this.fixedWorkRestSetting.toDto(),
                        lstHalfDayWorkTimezone: lstHalfDayWorkTimezone,
                        lstStampReflectTimezone: lstStampReflectTimezone,
                        legalOTSetting: this.legalOTSetting()
                    };
                    return dataDTO;
                }
                
                resetData(){
                    this.workTimeCode('');
                    this.offdayWorkTimezone.resetData();
                    this.commonSetting.resetData();
                    this.useHalfDayShift(false);
                    this.fixedWorkRestSetting.resetData();
                    this.getHDWtzOneday().resetData();
                    this.getHDWtzMorning().resetData();
                    this.getHDWtzAfternoon().resetData();
                    this.lstHalfDayWorkTimezone = [];
                    this.lstStampReflectTimezone = [];
                }
            }
            
        }
    }
}