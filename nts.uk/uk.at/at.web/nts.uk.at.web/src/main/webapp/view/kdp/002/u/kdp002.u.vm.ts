/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.kdp002.u {

	const API = {
		NOTIFICATION_STAMP: 'at/record/stamp/notification_by_stamp',
		UPDATE: 'at/record/stamp/notice/viewMessageNotice'
	};

	interface IParams {
		sid: string;
		data: IModel;
	}

	@bean()
	export class ViewModel extends ko.ViewModel {

		modelShowView: KnockoutObservableArray<IEmployeeIdSeen> = ko.observableArray([]);
		model: KnockoutObservableArray<IMsgNotices> = ko.observableArray([]);
		sid: string;
		modeNew: KnockoutObservable<boolean | null> = ko.observable(true);

		created(param: IParams) {
			const vm = this

			console.log(param);

			_.forEach(param.data.msgNotices, (value) => {
				
				if (value.message.targetInformation.destination == 2) {
					debugger;
					vm.modelShowView.push(value.message);
				}
			})

			vm.model(param.data.msgNotices);

			vm.sid = param.sid;

			vm.setModeNew(param.data.msgNotices);
		}

		setModeNew(param: IMsgNotices[]) {
			const vm = this;

			_.forEach(param, ((value) => {
				if (value.message.employeeIdSeen.length > 0) {
					vm.modeNew(false);
				}

			}));
		}

		closeDialog() {
			const vm = this;

			vm.$blockui('invisible')
				.then(() => {
					let msgInfors: Array<ICreatorAndDate> = [];

					_.forEach(ko.unwrap(vm.model), (value) => {

						if (value.flag) {
							var item: ICreatorAndDate = {
								creatorId: value.message.creatorID,
								inputDate: value.message.inputDate
							}

							msgInfors.push(item);
						}
					})

					const params = {
						msgInfors: msgInfors,
						sid: vm.sid
					}

					vm.$ajax(API.UPDATE, params)
						.done(() => {
							vm.$blockui('clear');
							vm.$window.close();
						});
				});
		}

		mounted() {
			$(document).ready(function () {
				$('.x-large').focus();
			});
		}
	}

	interface IModel {
		msgNotices: IMsgNotices[];
	}

	interface IMsgNotices {
		creator: string;
		flag: boolean;
		message: IEmployeeIdSeen;
	}

	interface IEmployeeIdSeen {
		endDate: string;
		inputDate: Date;
		modifiedDate: Date;
		notificationMessage: string;
		startDate: Date;
		targetInformation: ITargetInformation;
		employeeIdSeen: any;
	}

	interface ICreatorAndDate {
		creatorId: string;
		inputDate: Date;
	}
}
