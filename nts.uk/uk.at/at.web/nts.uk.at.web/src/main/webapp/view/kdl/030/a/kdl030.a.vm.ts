/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.view.kdl030.a.viewmodel {
    import getShared = nts.uk.ui.windows.getShared;
    import setShared = nts.uk.ui.windows.setShared;
    import dialog = nts.uk.ui.dialog;
    import getMessage = nts.uk.resource.getMessage;

    @bean()
    export class Kdl030AViewModel extends ko.ViewModel {
        appIDLst: Array<string> = [];
        isAgentMode: boolean = false;
        appSendMailByEmpLst: KnockoutObservableArray<any> = ko.observableArray([]);
        isSendApplicant: KnockoutObservable<boolean> = ko.observable(false);
        mailContent: KnockoutObservable<String> = ko.observable(null);
        appEmailSet: any = null;
        isOpEmployee: boolean = false;
		isMultiEmp: boolean = false;

        created() {
            const vm = this;
            let param = getShared("KDL030_PARAM");
            vm.appIDLst = param.appIDLst;
            vm.isAgentMode = param.isAgentMode;
			vm.isMultiEmp = param.isMultiEmp;
            //recheck agentMode
            if (vm.isAgentMode && param.employeeInfoLst.length == 1) {
                if (param.employeeInfoLst[0].scd == __viewContext.user.employeeCode) {
                    vm.isAgentMode = false;
                }
            }
            if (!_.isNil(param.appDispInfoStartupOutput)) {
                vm.isOpEmployee = param.appDispInfoStartupOutput.appDispInfoNoDateOutput.opEmployeeInfo != null;
                if (param.appDispInfoStartupOutput.appDispInfoNoDateOutput.employeeInfoLst[0].scd != __viewContext.user.employeeCode) {
                    vm.isAgentMode = true;
                }
            }

            if (!_.isEmpty(vm.appIDLst)) {
                vm.$ajax(API.applicationForSendByAppID, { appIDLst: vm.appIDLst, isMultiMode: vm.isMultiEmp }).done((result) => {
                    vm.mailContent(result.mailTemplate);
                    vm.appEmailSet = result.appEmailSet;
                    let appSendMails = [];
                    if (!_.isNil(param.employeeInfoLst) && param.employeeInfoLst.length > 1) {
                        _.forEach(param.employeeInfoLst, emp => {
                            let x = result.appSendMailByEmpLst.filter(app => app.applicantName.localeCompare(emp.bussinessName) == 0);
                            appSendMails.push(x[0]);
                        })
                    } else {
                        appSendMails = result.appSendMailByEmpLst;
                    }
                    _.forEach(appSendMails, appSendMailByEmp => {
                        _.forEach(appSendMailByEmp.approvalRoot.listApprovalPhaseStateDto, phase => {
                            _.forEach(phase.listApprovalFrame, frame => {
                                _.forEach(frame.listApprover, approver => {
                                    approver.handleSendMail = false;
                                });
                            });
                        });
                    });

                    vm.appSendMailByEmpLst(ko.mapping.fromJS(appSendMails)());

                    if (vm.isAgentMode || vm.isOpEmployee) {
                        vm.isSendApplicant(true);
                    }

                    if ($('#checkSendApplicant').length > 0) {
                        $('#checkSendApplicant').focus();
                    } else if ($('.switchBtn').length > 0) {
                        $('.switchBtn')[0].focus();
                    }
                });
            }
        }

        isFirstIndexPhase(appSendMailByEmp, loopPhase, loopFrame, loopApprover) {
            const vm = this;
            let firstIndex = _.chain(appSendMailByEmp.approvalRoot.listApprovalPhaseStateDto()).orderBy(x => x.phaseOrder()).first().value().phaseOrder();
            return loopPhase.phaseOrder() == firstIndex && vm.isFirstIndexFrame(loopPhase, loopFrame, loopApprover);
        }

        isFirstIndexFrame(loopPhase, loopFrame, loopApprover) {
            const vm = this;
//            if(_.size(loopFrame.listApprover()) > 1) {
//                return _.findIndex(loopFrame.listApprover(), o => o == loopApprover) == 0;
//            }
            let firstIndex = _.chain(loopPhase.listApprovalFrame()).filter(x => _.size(x.listApprover()) > 0).orderBy(x => x.frameOrder()).first().value().frameOrder();
//            let approver = _.find(loopPhase.listApprovalFrame(), o => o == loopFrame);
//            if(approver) {
//                return approver.frameOrder() == firstIndex;
//            }
//            return false;
            return loopFrame.frameOrder() == firstIndex && vm.isFirstIndexApprover(loopFrame, loopApprover);
        }

        isFirstIndexApprover(loopFrame, loopApprover) {
            // return loopApprover.approverInListOrder() == 1;
            return _.findIndex(loopFrame.listApprover(), o => o == loopApprover) == 0;
        }

        getFrameIndex(loopPhase, loopFrame, loopApprover) {
//            if(_.size(loopFrame.listApprover()) > 1) {
//                return _.findIndex(loopFrame.listApprover(), o => o == loopApprover);
//            }
            return loopFrame.frameOrder();
        }

        phaseCount(listPhase) {
            const vm = this;
            let count = 0;
            _.forEach(listPhase, phase => {
                count += vm.frameCount(phase.listApprovalFrame());
            });
            return count;
        }

        frameCount(listFrame) {
            const vm = this;
            let listExist = _.filter(listFrame, x => _.size(x.listApprover()) > 0);
            return _.chain(listExist).map(o => vm.approverCount(o.listApprover())).sum().value();
//            if(_.size(listExist) > 1) {
//                return _.size(listExist);
//            }
//            return _.chain(listExist).map(o => vm.approverCount(o.listApprover())).value()[0];
        }

        approverCount(listApprover) {
            return _.chain(listApprover).countBy().values().value()[0];
        }

        getApproverAtr(approver) {
            if (approver.approvalAtrName() != '未承認') {
                if (approver.agentName().length > 0) {
                    if (approver.agentMail().length > 0) {
                        return approver.agentName() + '(@)';
                    } else {
                        return approver.agentName();
                    }
                } else {
                    if (approver.approverMail().length > 0) {
                        return approver.approverName() + '(@)';
                    } else {
                        return approver.approverName();
                    }
                }
            } else {
                var s = '';
                s = s + approver.approverName();
                if (approver.approverMail().length > 0) {
                    s = s + '(@)';
                }
                if (approver.representerName().length > 0) {
                    if (approver.representerMail().length > 0) {
                        s = s + '(' + approver.representerName() + '(@))';
                    } else {
                        s = s + '(' + approver.representerName() + ')';
                    }
                }
                return s;
            }
        }

        checkApproverFive = ko.computed(function () {
            let count = 0;
            this.appSendMailByEmpLst().forEach(app => {
                app.approvalRoot.listApprovalPhaseStateDto().forEach(lst => {
                     lst.listApprovalFrame().forEach(lstAF => {
                        count += lstAF.listApprover().length;
                    })
                })
            })
            return count === 5 ? 'border-b-none' : '';
        }, this)

        checkApproverMoreFive = ko.computed(function () {
            let count = 0;
            let self = this;
            self.appSendMailByEmpLst().forEach(app => {
                app.approvalRoot.listApprovalPhaseStateDto().forEach(lst => {
                    lst.listApprovalFrame().forEach(lstAF => {
                        count += lstAF.listApprover().length;
                    })
                })
            })
            if (count > 5) {
                return (self.isAgentMode || self.isOpEmployee) ? 'min-774' : 'min-671';
            }
            return "";
        }, this)

        checkApproverTwo(appSendMailByEmp) {
            let count = 0;
            appSendMailByEmp.approvalRoot.listApprovalPhaseStateDto().forEach(lst => {
                lst.listApprovalFrame().forEach(lstAF => {
                    count += lstAF.listApprover().length;
                })
            })

            return count > 1 ? 'break-all' : 'limited-label';
        }

        getApplicantName(appSendMailByEmp: any) {
            return appSendMailByEmp.applicantName()
                .concat(nts.uk.util.isNullOrEmpty(appSendMailByEmp.applicantMail()) ? '' : '(@)');
        }

        getPhaseLabel(phaseOrder) {
            const vm = this;
            switch (phaseOrder) {
                case 1:
                    return vm.$i18n("KAF000_4");
                case 2:
                    return vm.$i18n("KAF000_5");
                case 3:
                    return vm.$i18n("KAF000_6");
                case 4:
                    return vm.$i18n("KAF000_7");
                case 5:
                    return vm.$i18n("KAF000_8");
                default:
                    return "";
            }
        }

        getApproverLabel(loopPhase, loopFrame, loopApprover) {
            const vm = this;
            let index = vm.getFrameIndex(loopPhase, loopFrame, loopApprover);
            // case group approver
//            if(_.size(loopFrame.listApprover()) > 1) {
//                index++;
//            }
            if (index <= 10) {
                return vm.$i18n("KAF000_9", [index + '']);
            }
            return "";
        }

        getApprovalDateFormat(loopApprover) {
            const vm = this;
            if (_.isNull(loopApprover.approvalDate()) || _.isUndefined(loopApprover.approvalDate()) || _.isEmpty(loopApprover.approvalDate())) {
                return '';
            }
            return moment(loopApprover.approvalDate()).format('YYYY/MM/DD HH:mm');
        }

        // アルゴリズム「メール送信」を実行する
        sendMail() {
            var self = this;
            //validate
            $(".A4_2").trigger("validate");
            if (nts.uk.ui.errors.hasError()) {
                return;
            }

            let appInfoLst: Array<any> = [];
            _.forEach(self.appSendMailByEmpLst(), appSendMailByEmp => {
                let approverInfoLst: Array<any> = [],
                    application = ko.mapping.toJS(appSendMailByEmp.application),
                    applicantMail = appSendMailByEmp.applicantMail();
                _.forEach(appSendMailByEmp.approvalRoot.listApprovalPhaseStateDto(), phase => {
                    _.forEach(phase.listApprovalFrame(), frame => {
                        _.forEach(frame.listApprover(), approver => {
                            if (approver.handleSendMail()) {
                                let approverID = approver.approverID(),
                                    approverMail = approver.approverMail(),
                                    approverName = approver.approverName();
                                approverInfoLst.push({approverID, approverMail, approverName});
                            }
                        });
                    });
                });
                appInfoLst.push({approverInfoLst, application, applicantMail});
            });

            let mailTemplate = self.mailContent(),
                appEmailSet = self.appEmailSet,
                sendMailApplicant = self.isSendApplicant(),
                command: any = {mailTemplate, appEmailSet, appInfoLst, sendMailApplicant};

            nts.uk.ui.block.invisible();
            service.sendMail(command).done(function (result) {
                nts.uk.ui.block.clear();
                // TO DO
                if (result) {
                    // 成功
                    let successList: Array<string> = [];
                    // 送信失敗 
                    let failedList: Array<string> = [];
                    // メール送信時のエラーチェック
                    if (result.successList) {
                        _.forEach(result.successList, x => {
                            successList.push(x);
                        });
                    }
                    if (result.errorList) {
                        _.forEach(result.errorList, x => {
                            failedList.push(x);
                        });
                    }
                    setShared("KDL030_PARAM_RES", command);
                    self.handleSendMailResult(successList, failedList);
                }
            }).fail(function (res: any) {
                nts.uk.ui.block.clear();
                if (res.messageId == 'Msg_1309' || res.messageId == 'Msg_14') {//エラーメッセージを表示する（Msg_1309）
                    dialog.alertError({messageId: res.messageId});
                } else {
                    //Msg1057
                    dialog.alertError({messageId: res.messageId}).then(() => {
                        nts.uk.ui.windows.close();
                    });
                    ;
                }
            });

        }

        cancel() {
            nts.uk.ui.windows.close();
        }

        handleSendMailResult(successList, failedList) {
            let numOfSuccess = successList.length;
            let numOfFailed = failedList.length
            let sucessListAsStr = "";
            //送信出来た人があったかチェックする
            //送信できた人なし
            if (numOfSuccess > 0) {//送信できた人あり
                //情報メッセージ（Msg_207）を画面表示する
                dialog.info({messageId: "Msg_207"}).then(() => {
                    //アルゴリズム「送信・送信後チェック」で溜め込んだ社員名があったかチェックする
                    if (numOfFailed > 0) {//溜め込んだ社員名無しあり
                        //エラーメッセージ（Msg_651）と溜め込んだ社員名をエラーダイアログに出力する
                        dialog.alertError({
                            message: getMessage('Msg_651') + "\n" + failedList.join('\n'),
                            messageId: "Msg_651"
                        }).then(() => {
                            nts.uk.ui.windows.close();
                        });
                    } else {
                        nts.uk.ui.windows.close();
                    }
                });
            }
        }
    }

    const API = {
        applicationForSendByAppID: "at/request/application/getApplicationForSendByAppID",
        sendMail: "at/request/mail/send"
    }
}

