module nts.uk.at.view.ksm015.c.viewmodel {
	export class ScreenModel {
		baseDate: KnockoutObservable<Date>;
		selectedWorkplaceId: KnockoutObservable<string>;
		baseDate: KnockoutObservable<Date>;
		alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
		treeGrid: TreeComponentOption;
		shiftColumns: Array<any>;
		shiftItems: KnockoutObservableArray<ShiftMaster>;
		selectedShiftMaster: KnockoutObservableArray<any>;
		constructor() {
			let self = this;
			self.baseDate = ko.observable(new Date());
			self.selectedWorkplaceId = ko.observableArray("");
			self.selectedWorkplaceId.subscribe((val) => {
				if (val) {
					let param = {
						workplaceId: val,
						targetUnit: TargetUnit.WORKPLACE
					}
					service.getShiftMasterByWorkplace(param)
						.done((data) => {
							data = _.sortBy(data, 'shiftMasterCode');
							self.shiftItems(data);
							self.selectedShiftMaster([]);
						});
				}
			});
			self.alreadySettingList = ko.observableArray([]);
			self.treeGrid = {
				isShowAlreadySet: true,
				isMultipleUse: true,
				isMultiSelect: false,
				treeType: 1,
				selectedWorkplaceId: self.selectedWorkplaceId,
				baseDate: self.baseDate,
				selectType: 3,
				isShowSelectButton: true,
				isDialog: false,
				alreadySettingList: self.alreadySettingList,
				maxRows: 15,
				tabindex: 1,
				systemType: 2
			};

			let ksm015Data = new Ksm015Data();
			self.shiftItems = ko.observableArray([]);
			self.shiftColumns = ko.observableArray(ksm015Data.shiftGridColumns);
			self.selectedShiftMaster = ko.observableArray([]);
			$('#tree-grid').ntsTreeComponent(self.treeGrid);
		}

		startPage(): JQueryPromise<any> {
			let self = this;

			let dfd = $.Deferred();
			dfd.resolve(1);
			return dfd.promise();
		}

		public registerOrd() {
			let self = this;
			if (self.shiftItems().length === 0) {
				nts.uk.ui.dialog.info({ messageId: "Msg_15" });
				return;
			}
			let param = {
				targetUnit: TargetUnit.WORKPLACE,
				workplaceId: self.selectedWorkplaceId(),
				shiftMasterCodes: _.map(self.shiftItems(), (val) => { return val.shiftMasterCode })
			};
			nts.uk.ui.block.grayout();
			service.registerOrg(param)
				.done(() => {
					nts.uk.ui.dialog.info({ messageId: "Msg_15" });
					self.selectedWorkplaceId.valueHasMutated();
				}).fail(function (error) {
					nts.uk.ui.dialog.alertError({ messageId: error.messageId });
				}).always(function () {
					nts.uk.ui.block.clear();
				});
		}

		public deleteShiftMaster() {
			nts.uk.ui.block.invisible();
			let self = this;
			let param = {
				targetUnit: TargetUnit.WORKPLACE,
				workplaceId: self.selectedWorkplaceId(),
				shiftMasterCodes: _.map(self.shiftItems(), (val) => { return val.shiftMasterCode })
			};
			nts.uk.ui.dialog.confirm({ messageId: "Msg_18" })
				.ifYes(() => {
					service.deleteOrg(param).done((data) => {
						nts.uk.ui.dialog.info({ messageId: "Msg_15" });
						self.selectedWorkplaceId.valueHasMutated();
						nts.uk.ui.block.clear();
					}).fail((res) => {
						nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function () { nts.uk.ui.block.clear(); });
					});
				}).ifNo(() => {
					nts.uk.ui.block.clear();
				});
		}

		public clearShiftMaster() {
			let self = this;
			if (this.selectedShiftMaster().length > 0) {
				self.shiftItems(_.filter(self.shiftItems(), (val) => { return self.selectedShiftMaster().indexOf(val.shiftMasterCode) === -1 }));
				self.selectedShiftMaster([]);
			}

		}

		public openDialogKDL044(): void {
			let self = this;
			// set update data input open dialog kdl044
			nts.uk.ui.windows.setShared('kdl044Data', {
				isMultiSelect: true,
				filter: 0,
				permission: true,
				shifutoCodes: _.map(self.shiftItems(), (val) => { return val.shiftMasterCode })
			}, true);

			nts.uk.ui.windows.sub.modal('/view/kdl/044/a/index.xhtml').onClosed(function (): any {
				//view all code of selected item 
				let isCancel = nts.uk.ui.windows.getShared('kdl044_IsCancel');
				if (!isCancel) {
					let shiftItems = nts.uk.ui.windows.getShared('kdl044ShifutoData');
					let selectedShiftMaster = nts.uk.ui.windows.getShared('kdl044ShifutoCodes');
					let currents = self.shiftItems();
					let differentFromCurrents = _.differenceWith(shiftItems, currents, (a, b) => { return a.shiftMasterCode === b.shiftMasterCode });
					currents = currents.concat(differentFromCurrents);
					currents = _.sortBy(currents, 'shiftMasterCode');
					self.shiftItems(currents);
				}
			});
		}

		/**
			* open dialog copy monthly pattern setting by on click button
			*/
		public openDialogCopy(): void {
			var self = this;
			if (!self.selectedShiftMaster()) {
				nts.uk.ui.dialog.alertError({ messageId: "Msg_189" });
				return;
			}

			let dataSource = self.shiftItems();
			let itemListSetting = _.map(self.alreadySettingList(), item => {
				return _.find(dataSource, i => i.code == item.code).id;
			});

			let object: IObjectDuplication = {
				code: self.selectedWorkplaceId(),
				name: dataSource.filter(e => e.shiftMasterCode == self.selectedShiftMaster())[0].shiftMasterName,
				targetType: TargetType.WORKPLACE,
				itemListSetting: itemListSetting,
				baseDate: self.baseDate()
			};

			// create object has data type IObjectDuplication and use:
			nts.uk.ui.windows.setShared("CDL023Input", object);

			// open dialog
			nts.uk.ui.windows.sub.modal('com', '/view/cdl/023/a/index.xhtml').onClosed(() => {
				// show data respond
				let lstSelection: any = nts.uk.ui.windows.getShared("CDL023Output");
				if (!nts.uk.util.isNullOrEmpty(lstSelection)) {
					// self.listDestSid(lstSelection);
					// self.copyMonthlyPatternSetting();
				}
			});
		}


	}
}