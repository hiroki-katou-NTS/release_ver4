module nts.uk.at.view.kfp001.d {
    import getText = nts.uk.resource.getText;
    export module viewmodel {
        export class ScreenModel {
            aggrFrameCode: KnockoutObservable<string>;
            optionalAggrName: KnockoutObservable<string>;
            startDate: KnockoutObservable<string>;
            endDate: KnockoutObservable<string>;
            peopleNo: KnockoutObservable<number> = ko.observable(0);
            mode: KnockoutObservable<number> = ko.observable(0);
            listEmp: KnockoutObservableArray<any>;
            executionId: KnockoutObservable<string>;
            listSelect: KnockoutObservableArray<any> = ko.observableArray([]);
            listSelectedEmpId: KnockoutObservableArray<any> = ko.observableArray([]);
            listAggr: KnockoutObservableArray<any> = ko.observableArray([]);
            peopleCount: KnockoutObservable<string> = ko.observable('');
            presenceOfError: KnockoutObservable<string> = ko.observable('');
            executionStatus: KnockoutObservable<string> = ko.observable('');
            addErrorInforCommand: KnockoutObservable<any> = ko.observable({});

            constructor() {
                var self = this;
                self.aggrFrameCode = ko.observable('');
                self.optionalAggrName = ko.observable('');
                self.startDate = ko.observable('');
                self.endDate = ko.observable('');
                self.listEmp = ko.observableArray([]);
                self.executionId = ko.observable('');
            }
            start() {
                let self = this;
                $("#button-2D").focus();
                let peopleCo = nts.uk.ui.windows.getShared("KFP001_DATAC_SELECT");
                if (peopleCo != self.peopleNo()) {
                    self.peopleNo(peopleCo);
                }
            }

            addData() {
                nts.uk.ui.block.invisible();
                let self = this;

                let listEmployeeId = _.map(_.filter(self.listEmp(), (v) => _.includes(self.listSelect(), v.employeeCode)), (item) => {
                    return item.employeeId;
                });
                self.listSelectedEmpId(listEmployeeId);


                let listEmployee = [];
                _.forEach(self.listEmp(), function(item) {
                    listEmployee.push(item.employeeId);
                });

                let targetDto = {
                    executionEmpId: self.executionId(),
                    employeeId: self.listSelectedEmpId(),
                    state: 0
                }

                let executionDto = {
                    aggrFrameCode: self.aggrFrameCode(),
                    executionAtr: 1,
                    executionStatus: 0,
                    presenceOfError: 1
                }
                let aggrPeriodDto = {
                    aggrFrameCode: self.aggrFrameCode(),
                    optionalAggrName: self.optionalAggrName(),
                    startDate: moment(self.startDate()).utc(),
                    endDate: moment(self.endDate()).utc(),
                    peopleNo: self.peopleNo()

                }

                let resourceId = nts.uk.util.randomId().slice(0, 10);
                if (self.aggrFrameCode() == '001') {
                    self.addErrorInforCommand({
                        resourceId: resourceId,
                        periodArrgLogId: self.executionId(),
                        processDay: moment(self.startDate()).utc(),
                        errorMess: 'Loi roi'
                    })
                } else {
                    self.addErrorInforCommand({})
                }

                var addAggrPeriodCommand = {
                    mode: self.mode(),
                    aggrPeriodCommand: aggrPeriodDto,
                    targetCommand: targetDto,
                    executionCommand: executionDto,
                    inforCommand: self.addErrorInforCommand()

                }


                service.addOptionalAggrPeriod(addAggrPeriodCommand).done(function(data) {
                    self.mode(1);

                    let exc = {
                        presenceOfError: self.presenceOfError(),
                        executionStatus: self.executionStatus()
                    }
                    nts.uk.ui.windows.setShared("KFP001_DATAD", data);
                    nts.uk.ui.windows.setShared("KFP001_DATA_EXC", exc);
                    nts.uk.ui.windows.setShared("KFP001_DATAE", addAggrPeriodCommand);
                    nts.uk.ui.windows.sub.modal('/view/kfp/001/e/index.xhtml');
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                }).always(function() {
                    nts.uk.ui.block.clear();
                })

                //                // Test data error !!!
                //                if (self.aggrFrameCode() == '001') {
                //                    let resourceId = nts.uk.util.randomId().slice(0, 10);
                //
                //                    let addErrorInforCommand = {
                //                        resourceId: resourceId,
                //                        periodArrgLogId: self.executionId(),
                //                        processDay: moment(self.startDate()).utc(),
                //                        errorMess: 'Loi roi'
                //                    }
                //
                //                    service.addErr(addErrorInforCommand).done(function(dataErr) {
                //                    });
                //
                //                }
                nts.uk.ui.block.clear();

            }

            opendScreenF() {
                nts.uk.request.jump("/view/kfp/001/b/index.xhtml");
            }

            prevC() {
                $("#wizard").ntsWizard("prev").done(function() {
                });
            }

            addListError(errorsRequest: Array<string>) {
                var self = this;
                var errors = [];
                _.forEach(errorsRequest, function(err) {
                    errors.push({ message: nts.uk.resource.getMessage(err), messageId: err, supplements: {} });
                });

                nts.uk.ui.dialog.bundledErrors({ errors: errors });
            }

        }


    }
}
