module nts.uk.at.view.kaf000.a.component6.viewmodel {

    @component({
        name: 'kaf011-a-component6',
        template: `
            <div id="kaf011-a-component6">
                <div id="listApproverRootState" class="table" data-bind="if: approvalRootState().length != 0 && approvalRootDisp">
                    <div class="cell" id="label-1">
                        <div class="cell valign-center" data-bind="ntsFormLabel: {}, text: $i18n('KAF000_37')"></div>
                    </div>
                    <div class="cell" id="table-1">
                        <table id="table-phase">
                            <thead>
                                <tr class="bg-green">
                                    <th style="width: 111px;"></th>
                                    <th style="width: 111px;"></th>
                                    <th style="width: 230px;" data-bind="text: $i18n('KAF000_3')"></th>
                                </tr>
                            </thead>
                            <tbody data-bind="foreach: { data: approvalRootState, as: 'loopPhase' }">
                                <!-- ko foreach: {data: loopPhase.listApprovalFrame, as: 'loopFrame'} -->
                                    <!-- ko foreach: {data: loopFrame.listApprover, as: 'loopApprover'} -->
                                    <tr>
                                        <!-- ko if: $component.isFirstIndexFrame(loopPhase, loopFrame, loopApprover) -->
                                            <td data-bind="attr: {rowspan: ko.pureComputed(function() { return $component.frameCount(loopPhase.listApprovalFrame())}) },
                                            text: $component.getPhaseLabel(loopPhase.phaseOrder())" style="width: 111px;" class="bg-green"></td>
                                        <!-- /ko -->
                                        <td data-bind="text: $component.getApproverLabel(loopPhase, loopFrame, loopApprover)"
                                            style="width: 111px; text-align: left; padding-left: 5px;" class="bg-green"></td>
                                        <td>
                                            <span class="limited-label" style="width: 230px; text-align: left; padding-left: 5px;"
                                                data-bind="text: ko.pureComputed(function() { return $component.getApproverAtr(loopApprover)})">
                                            </span>
                                        </td>
                                    </tr>
                                    <!-- /ko -->
                                <!-- /ko -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        `
    })
    class Kaf000AComponent6ViewModel extends ko.ViewModel {
		appType: KnockoutObservable<number> = null;
        approvalRootState: KnockoutObservableArray<any>;
        appDispInfoStartupOutput: any;
		isAgentMode: KnockoutObservable<boolean>;
		approvalRootDisp: KnockoutObservable<boolean>;
        created(params: any) {
            const vm = this;
			vm.appType = params.appType;
			vm.approvalRootState = ko.observableArray([]);
            vm.appDispInfoStartupOutput = params.appDispInfoStartupOutput;
			vm.isAgentMode = params.isAgentMode ? params.isAgentMode : ko.observable(false);
			vm.approvalRootDisp = ko.observable(false);
			
            vm.appDispInfoStartupOutput.subscribe(value => {
                if(!_.isEmpty(value.appDispInfoWithDateOutput.opListApprovalPhaseState)) {
                    vm.approvalRootState(ko.mapping.fromJS(value.appDispInfoWithDateOutput.opListApprovalPhaseState)());
                } else {
                    vm.approvalRootState([]);
                }
				vm.approvalRootDisp(!vm.isAgentMode() || _.size(value.appDispInfoNoDateOutput.employeeInfoLst)<=1);
            });
        }

        mounted() {
            const vm = this;
        }

        isFirstIndexFrame(loopPhase, loopFrame, loopApprover) {
            if(_.size(loopFrame.listApprover()) > 1) {
                return _.findIndex(loopFrame.listApprover(), o => o == loopApprover) == 0;
            }
            let firstIndex = _.chain(loopPhase.listApprovalFrame()).filter(x => _.size(x.listApprover()) > 0).orderBy(x => x.frameOrder()).first().value().frameOrder();
            let approver = _.find(loopPhase.listApprovalFrame(), o => o == loopFrame);
            if(approver) {
                return approver.frameOrder() == firstIndex;
            }
            return false;
        }

        getFrameIndex(loopPhase, loopFrame, loopApprover) {
            if(_.size(loopFrame.listApprover()) > 1) {
                return _.findIndex(loopFrame.listApprover(), o => o == loopApprover);
            }
            return loopFrame.frameOrder();
        }

        frameCount(listFrame) {
            const vm = this;
            let listExist = _.filter(listFrame, x => _.size(x.listApprover()) > 0);
            if(_.size(listExist) > 1) {
                return _.size(listExist);
            }
            return _.chain(listExist).map(o => vm.approverCount(o.listApprover())).value()[0];
        }

        approverCount(listApprover) {
            return _.chain(listApprover).countBy().values().value()[0];
        }

        getApproverAtr(approver) {
            if(approver.approvalAtrName() !='未承認'){
                if(approver.agentName().length > 0){
                    if(approver.agentMail().length > 0){
                        return approver.agentName() + '(@)';
                    } else {
                        return approver.agentName();
                    }
                } else {
                    if(approver.approverMail().length > 0){
                        return approver.approverName() + '(@)';
                    } else {
                        return approver.approverName();
                    }
                }
            } else {
                var s = '';
                s = s + approver.approverName();
                if(approver.approverMail().length > 0){
                    s = s + '(@)';
                }
                if(approver.representerName().length > 0){
                    if(approver.representerMail().length > 0){
                        s = s + '(' + approver.representerName() + '(@))';
                    } else {
                        s = s + '(' + approver.representerName() + ')';
                    }
                }
                return s;
            }
        }

        getPhaseLabel(phaseOrder) {
            const vm = this;
            switch(phaseOrder) {
                case 1: return vm.$i18n("KAF000_4");
                case 2: return vm.$i18n("KAF000_5");
                case 3: return vm.$i18n("KAF000_6");
                case 4: return vm.$i18n("KAF000_7");
                case 5: return vm.$i18n("KAF000_8");
                default: return "";
            }
        }

        getApproverLabel(loopPhase, loopFrame, loopApprover) {
            const vm = this;
           	let index = vm.getFrameIndex(loopPhase, loopFrame, loopApprover);
            // case group approver
            if(_.size(loopFrame.listApprover()) > 1) {
                index++;
            }
            if(index <= 10){
                return vm.$i18n("KAF000_9",[index+'']);
            }
            return "";
        }
    }
}