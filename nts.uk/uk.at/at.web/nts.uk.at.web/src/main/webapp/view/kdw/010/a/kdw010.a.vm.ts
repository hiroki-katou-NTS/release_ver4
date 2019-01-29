module nts.uk.at.view.kdw010.a {
    import blockUI = nts.uk.ui.block;
    import getShared = nts.uk.ui.windows.getShared;
    import setShared = nts.uk.ui.windows.setShared;
    import modal = nts.uk.ui.windows.sub.modal;
    import errors = nts.uk.ui.errors;

    const NOT_USE = 1;
    const TARGET_TYPE = 1;
    const IGNORE_TYPE = 2;
    const JOIN_CHARACTOR = ", ";
    export module viewmodel {
        export class ScreenModel {
            useSet: KnockoutObservableArray<any>;
            selectUse: KnockoutObservable<number>;
            enableState: KnockoutObservable<boolean>;
            displayMessege: KnockoutObservable<String>;
            listWorkTypeDto: KnockoutObservableArray<model.OtkWorkTypeDto>;
            joinNameTargetWorkType: KnockoutObservable<String>;
            joinNameIgnoreWorkType: KnockoutObservable<String>;
            targetType: number;
            ignoreType: number;
            selectedCodeTargetWorkType: KnockoutObservableArray<String>;
            selectedCodeIgnoreWorkType: KnockoutObservableArray<String>;
            targetWorkTypes: KnockoutObservableArray<model.OtkWorkTypeDto>;
            ignoreWorkTypes: KnockoutObservableArray<model.OtkWorkTypeDto>;
            updateMode: KnockoutObservable<boolean>;
            maxContinuousDay: any;
            tempContinuousHolCheckSet: model.ContinuousHolCheckSet;


            constructor() {
                var self = this;
                self.useSet = ko.observableArray([
                    { code: '1', name: nts.uk.resource.getText("Enum_UseAtr_Use") },
                    { code: '0', name: nts.uk.resource.getText("Enum_UseAtr_NotUse") },
                ]);
                self.maxContinuousDay = {
                    value: ko.observable(0),
                    enable: ko.observable(true)
                }
                self.selectUse = ko.observable(1);
                self.enableState = ko.observable(true);
                self.displayMessege = ko.observable('');
                self.listWorkTypeDto = ko.observableArray([]);
                self.joinNameTargetWorkType = ko.observable('');
                self.joinNameIgnoreWorkType = ko.observable('');
                self.targetType = TARGET_TYPE;
                self.ignoreType = IGNORE_TYPE;
                self.selectedCodeTargetWorkType = ko.observableArray([]);
                self.selectedCodeIgnoreWorkType = ko.observableArray([]);
                self.targetWorkTypes = ko.observableArray([]);
                self.ignoreWorkTypes = ko.observableArray([]);
                self.updateMode = ko.observable(false);
                self.tempContinuousHolCheckSet = null;

                self.selectUse.subscribe(function(codeChanged) {
                    if (codeChanged == 1) {
                        self.enableState(true);
                    } else {
                    	$('.nts-input').ntsError('clear');
                        self.enableState(false);
                    }
                });
                self.targetWorkTypes.subscribe(function(newTargetWorkTypes: Array<model.OtkWorkTypeDto>) {
                    self.joinNameTargetWorkType(self.joinNameWorkTypeMethod(newTargetWorkTypes));
                    self.selectedCodeTargetWorkType(newTargetWorkTypes.map(e => e.code));
                });
                self.ignoreWorkTypes.subscribe(function(newIgnoreWorkTypes: Array<model.OtkWorkTypeDto>) {
                    self.joinNameIgnoreWorkType(self.joinNameWorkTypeMethod(newIgnoreWorkTypes));
                    self.selectedCodeIgnoreWorkType(newIgnoreWorkTypes.map(e => e.code));
                });

            }
            startPage(): JQueryPromise<any> {
                blockUI.invisible();
                var self = this;
                var dfd = $.Deferred();

                service.findContinuousHolCheckSet().done(function(continuousHolCheckSetDto: model.ContinuousHolCheckSet) {
                    if (!nts.uk.util.isNullOrEmpty(continuousHolCheckSetDto) && continuousHolCheckSetDto.targetWorkType != null) {
                        self.tempContinuousHolCheckSet = continuousHolCheckSetDto;
                        console.log(this.tempContinuousHolCheckSet);
                        self.updateMode(true);
                        self.maxContinuousDay.value(continuousHolCheckSetDto.maxContinuousDays);
                        self.displayMessege(continuousHolCheckSetDto.displayMessage);
                        var useSetNum = continuousHolCheckSetDto.useAtr ? 1 : 0;
                        self.selectUse(useSetNum);
                    }
                    service.findAllWorkTypeDto().done(function(listWorkType: Array<model.OtkWorkTypeDto>) {
                        if (!_.isEmpty(listWorkType)) {
                            self.listWorkTypeDto(listWorkType);
                            //set list targetWorkType
                            var targetWorkTypes = self.findWorkTypeDtoByCode(continuousHolCheckSetDto.targetWorkType);
                            self.targetWorkTypes(targetWorkTypes);

                            //set list ignoreWorkType
                            var ignoreWorkTypes = self.findWorkTypeDtoByCode(continuousHolCheckSetDto.ignoreWorkType);
                            self.ignoreWorkTypes(ignoreWorkTypes);

                        }
                        blockUI.clear();
                    });

                });

                dfd.resolve();
                return dfd.promise();
            }
            //find list workType by list code
            findWorkTypeDtoByCode(codes: Array<String>): Array<model.OtkWorkTypeDto> {
                var self = this;
                var dataWorkType = self.listWorkTypeDto();
                if (_.isEmpty(dataWorkType)) {
                    return null;
                }
                var listWorkTypeResult: Array<model.OtkWorkTypeDto> = [];
                _.forEach(codes, function(value) {
                    var workType = dataWorkType.filter(e => e.code == value)[0];
                    if (!_.isEmpty(workType)) {
                        listWorkTypeResult.push(workType);
                    }
                    else {
                      var workTypeMaster =  {
                            code : value,
                            name : value + 'マスタ未登録'
                      };  
                      listWorkTypeResult.push(workTypeMaster);  
                    }
                });
                return listWorkTypeResult;
            }
            //joint name of work type list
            joinNameWorkTypeMethod(listWorkType: Array<model.OtkWorkTypeDto>): String {
                var joinName: String = '';
                if (!_.isEmpty(listWorkType)) {
                    joinName = listWorkType[0].name;
                    for (let i = 1; i < listWorkType.length; i++) {
                        joinName += JOIN_CHARACTOR + listWorkType[i].name;
                    }
                }
                return joinName;
            }
            //open dialog Kdl002
            openKDL002Dialog(typeWorkType: any) {
                var self = this;
                setShared('KDL002_Multiple', true);
                setShared('KDL002_isAcceptSelectNone', true);
                //all possible items
                var posibleItems = self.listWorkTypeDto().map(e => e.code);
                setShared('KDL002_AllItemObj', posibleItems);
                //selected items
                var selectedCode = typeWorkType == TARGET_TYPE ? self.selectedCodeTargetWorkType() : self.selectedCodeIgnoreWorkType();
                setShared('KDL002_SelectedItemId', selectedCode);
                modal('/view/kdl/002/a/index.xhtml').onClosed(function(): any {
                    var newItemSelected = getShared('KDL002_SelectedNewItem');
                    //defined target or ignore work type
                    if (typeWorkType == TARGET_TYPE) {
                        self.targetWorkTypes(newItemSelected);
                        errors.clearAll();
                    } else {
                        self.ignoreWorkTypes(newItemSelected);
                    }
                });
            }
            //btn register click
            registration() {
                blockUI.invisible();
                var self = this;
                if (self.selectUse() == NOT_USE) $('.nts-input').trigger("validate");
                _.defer(() => {
                    if (!$('.nts-editor').ntsError("hasError")) {
                        var useAtr = self.selectUse() == 1 ? true : false;
                        var continuousHolCheckSet: model.ContinuousHolCheckSet = new model.ContinuousHolCheckSet(self.selectedCodeTargetWorkType(), self.selectedCodeIgnoreWorkType(), useAtr, self.displayMessege(), self.maxContinuousDay.value(), self.updateMode());
                        //set before value if maxContinuousDays is valid
                        if (isNaN(continuousHolCheckSet.maxContinuousDays)) continuousHolCheckSet.maxContinuousDays = self.tempContinuousHolCheckSet.maxContinuousDays;
                        service.saveContinuousHolCheckSet(continuousHolCheckSet).done(function() {
                            self.updateMode(true);
                            nts.uk.ui.dialog.info({ messageId: 'Msg_15' });
                        });
                    }
                });
            	blockUI.clear();
            }
        }
    }
}