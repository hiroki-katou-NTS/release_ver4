/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />
module nts.uk.at.view.kml001.a {
  export module viewmodel {
    import servicebase = kml001.shr.servicebase;
    import vmbase = kml001.shr.vmbase;
    export class ScreenModel extends ko.ViewModel {
      gridPersonCostList: KnockoutObservableArray<vmbase.GridPersonCostCalculation>;
      currentGridPersonCost: KnockoutObservable<string>;
      personCostList: KnockoutObservableArray<vmbase.PersonCostCalculation>;
      currentPersonCost: KnockoutObservable<vmbase.PersonCostCalculation> = ko.observable(null);
      premiumItems: KnockoutObservableArray<vmbase.PremiumItem>;
      lastStartDate: string;
      isInsert: KnockoutObservable<Boolean>;
      newStartDate: KnockoutObservable<string>;
      viewAttendanceItems: KnockoutObservableArray<KnockoutObservable<string>>;
      textKML001_40 = nts.uk.resource.getText("KML001_40");
      isLastItem: KnockoutObservable<Boolean> = ko.observable(false);
      standardDate: KnockoutObservable<string> = ko.observable(null);
      langId: KnockoutObservable<string> = ko.observable('ja');
      unitPriceOpt: KnockoutObservableArray<any> = ko.observableArray([]);
      selectedHistory: KnockoutObservable<vmbase.GridPersonCostCalculation> = ko.observable(null);

      constructor() {
        super();

        $('#formula-child-1').html(nts.uk.resource.getText('KML001_7').replace(/\n/g, '<br/>'));
        var self = this;
        self.personCostList = ko.observableArray([]);
        self.currentPersonCost = ko.observable(
          new vmbase.PersonCostCalculation('', '', "", "9999/12/31", 0, '', null, [], 1, 0, 1, 0, 0)
        );

        self.newStartDate = ko.observable(null);
        self.gridPersonCostList = ko.observableArray([]);
        self.currentGridPersonCost = ko.observable(null);
        self.premiumItems = ko.observableArray([]);
        self.isInsert = ko.observable(true);
        self.lastStartDate = "1900/01/01";
        self.viewAttendanceItems = ko.observableArray([
          ko.observable(''),
          ko.observable(''),
          ko.observable(''),
          ko.observable(''),
          ko.observable(''),
          ko.observable(''),
          ko.observable(''),
          ko.observable(''),
          ko.observable(''),
          ko.observable('')
        ]);

        self.unitPriceOpt([
          { code: 0, name: self.$i18n('KML001_22') },
          { code: 1, name: self.$i18n('KML001_23') },
          { code: 2, name: self.$i18n('KML001_24') },
          { code: 3, name: self.$i18n('KML001_26') },
          { code: 4, name: self.$i18n('KML001_25') }
        ]);

        self.currentPersonCost().unitPrice.subscribe((newValue) => {
          self.changeUnitPrice(newValue);
        });
        // change current personCostCalculation when grid is selected
        self.currentGridPersonCost.subscribe((newValue) => {
          if (_.isNil(newValue)) return;
          self.getPersonalDetails(newValue);
        });

        self.currentPersonCost().calculationSetting.subscribe((newValue) => {
          //self._calculationSetting(newValue);
          //console.log(newValue);
        });
      }

      /**
       * get data on start page
       */

      startPage(): JQueryPromise<any> {

        var self = this;
        var dfd = $.Deferred();

        var dfdPersonCostCalculationSelect = servicebase.personCostCalculationSelect();

        self.$blockui('grayout');

        $.when(dfdPersonCostCalculationSelect)
          .done((data) => {
            // PersonCostCalculationSelect: Done
            if (!_.isNil(data.lisHist)) {
              self.loadHistoryListPanel(data.lisHist, null);
              self.isInsert(false);
            } else self.$blockui('hide');

            let height__: number = $(window).width() > 1366 ? 464 : 224;
            $("#premium-set-tbl").ntsFixedTable({ height: height__ });

            dfd.resolve();
          })
          .fail((res1, res2) => {
            if (!_.isNil(res1) && !_.isNil(res2)) {
              nts.uk.ui.dialog.alertError(res1.message + '\n' + res2.message).then(() => {
                if (res2.messageId === 'Msg_2027' || res1.messageId === 'Msg_2027') {
                  nts.uk.request.jump('com', '/view/ccg/008/a/index.xhtml');
                }
              });
            } else if (_.isNil(res1) && !_.isNil(res2)) {
              self.$dialog.error({ messageId: res2.messageId }).then(() => {
                nts.uk.request.jump('com', '/view/ccg/008/a/index.xhtml');
              });
            } else if (!_.isNil(res1) && _.isNil(res2)) {
              self.$dialog.error({ messageId: res1.messageId }).then(() => {
                nts.uk.request.jump('com', '/view/ccg/008/a/index.xhtml');
              });
            }

            dfd.reject();
          });

        return dfd.promise();
      }

      private uuid() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
          var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
          return v.toString(16);
        });
      }

      /**
       * get list item for each premium setting
       */
      private getItem(iDList: Array<number>, index: number) {
        var self = this;
        var dfd = $.Deferred();
        if (iDList.length != 0) {
          self.currentPersonCost().premiumSets()[index].attendanceItems.removeAll();
          servicebase.getAttendanceItems(iDList)
            .done(function (res: Array<any>) {
              let newList: any = [], arrSort = [];
              // fixbug 105897: sort base displayNumber
              arrSort = _.orderBy(res, ['attendanceItemDisplayNumber'], ['asc']);
              arrSort.forEach(function (item) {
                newList.push(new vmbase.AttendanceItem(item.attendanceItemId, item.attendanceItemName));
              });
              self.currentPersonCost().premiumSets()[index].attendanceItems(newList);
              self.createViewAttendanceItems(newList, index);
              dfd.resolve();
            })
            .fail(function (res) {
              nts.uk.ui.dialog.alertError(res.message);
              dfd.reject();
            });
        } else {
          self.createViewAttendanceItems([], index);
          dfd.resolve();
        }
        return dfd.promise();
      }

      registerUpdateData() {
        let self = this;

        $('.field-required').trigger('validate');
        if (nts.uk.ui.errors.hasError()) return;

        //save or update data
        let premiumSettingList: Array<any> = [];
        _.forEach(self.currentPersonCost().premiumSets(), (item) => {

          let attendanceItems: any = [];
          _.forEach(item.attendanceItems(), (x) => {
            attendanceItems.push(x.shortAttendanceID);
          });

          premiumSettingList.push({
            iD: item.displayNumber(),
            name: item.name(),
            rate: item.rate(),
            unitPrice: item.unitPrice(), //0 -> 4
            useAtr: item.useAtr(),
            attendanceItems: attendanceItems
          });
        });

        let personCostRoundingSetting: any = {
          unitPriceRounding: self.currentPersonCost().personCostRoundingSetting().roundingUnitPrice, // 0 -> 2
          unit: self.currentPersonCost().personCostRoundingSetting().unit, // 1, 10, 100, 1000,
          rounding: self.currentPersonCost().personCostRoundingSetting().inUnits // 0 -> 9
        };

        let startDate = moment.utc(self.currentPersonCost().startDate(), 'YYYY-MM-DD').toISOString();
        if (_.isNil(startDate)) {
          startDate = moment.utc(self.selectedHistory().startDate, 'YYYY-MM-DD').toISOString();
        }

        let params: any = {
          startDate: startDate,
          historyID: self.currentPersonCost().historyID(),
          unitPrice: self.currentPersonCost().unitPrice(),
          howToSetUnitPrice: self.currentPersonCost().calculationSetting(),
          workingHoursUnitPrice: self.currentPersonCost().workingHour(),
          memo: self.currentPersonCost().memo(),
          personCostRoundingSetting: personCostRoundingSetting,
          premiumSets: premiumSettingList
        };

        self.$blockui('show');
        servicebase.personCostCalculationUpdate(params)
          .done(() => {
            self.$dialog.info({ messageId: "Msg_15" }).then(() => {
              self.$blockui('hide');
              self.reloadHistoryList();
            });
          })
          .fail((error) => {
            self.$dialog.info({ messageId: error.messageId }).then(() => {
              self.$blockui('hide');
            });
          });
      }

      reloadHistoryList() {
        const self = this;
        self.$blockui('grayout');
        servicebase.personCostCalculationSelect().done((data) => {
          if (!_.isNil(data.lisHist)) {
            self.loadHistoryListPanel(data.lisHist, null);
            self.isInsert(false);
          }
        }).always(() => self.$blockui('hide'));
      }
      /**
       * open premium dialog
       */
      premiumDialog(): void {
        nts.uk.ui.block.invisible();
        var self = this;
        /* let currentIndex = _.findIndex(self.personCostList(), function (item) { return item.historyID() == self.currentPersonCost().historyID() });
        let index = currentIndex ? currentIndex : 0;  */

        let oldPremiumSets = self.clonePersonCostCalculation(self.currentPersonCost()).premiumSets();
        nts.uk.ui.windows.setShared('isInsert', self.isInsert());
        nts.uk.ui.windows.sub.modal("/view/kml/001/b/index.xhtml", { title: "割増項目の設定", dialogClass: "no-close" }).onClosed(function () {
          nts.uk.ui.block.invisible();
          self.langId(nts.uk.ui.windows.getShared("KML001_B_LANGID"));
          if (nts.uk.ui.windows.getShared('updatePremiumSeting') == true) {
            nts.uk.ui.errors.clearAll();
            var dfdPremiumItemSelect = servicebase.premiumItemSelect();
            var dfdPersonCostCalculationSelect = servicebase.personCostCalculationSelect();
            $.when(dfdPremiumItemSelect, dfdPersonCostCalculationSelect).done((premiumItemSelectData, dfdPersonCostCalculationSelectData) => {
              // Premium Item Select: Done
              self.premiumItems.removeAll();
              premiumItemSelectData.forEach(function (item) {
                self.premiumItems.push(
                  new vmbase.PremiumItem(
                    item.companyID,
                    item.displayNumber,
                    item.name,
                    item.useAtr,
                    false,
                    item.unitPrice
                  ));
              });
              // PersonCostCalculationSelect: Done
              if (!dfdPersonCostCalculationSelectData.length) {
                self.currentPersonCost().premiumSets.removeAll();
                self.premiumItems().forEach(function (item) {
                  if (item.useAtr()) {
                    self.currentPersonCost().premiumSets.push(
                      new vmbase.PremiumSetting("", "", item.displayNumber(), 100, item.name(), item.useAtr(), [], item.unitPrice())
                    );
                  }
                });
                $("#memo").focus();
              } else {
                if (self.isInsert()) {
                  self.currentPersonCost().premiumSets.removeAll();
                  self.premiumItems().forEach(function (item) {
                    if (item.useAtr()) {
                      let currentIndexSet = _.find(oldPremiumSets, function (o) { return o.displayNumber() == item.displayNumber(); });
                      if (nts.uk.util.isNullOrUndefined(currentIndexSet)) {
                        self.currentPersonCost().premiumSets.push(
                          new vmbase.PremiumSetting("", "", item.displayNumber(), 100, item.name(), item.useAtr(), [], item.unitPrice()));
                      } else {
                        self.currentPersonCost().premiumSets.push(currentIndexSet);
                      }
                    }
                  });
                  self.currentPersonCost().premiumSets.valueHasMutated();
                  self.currentPersonCost().premiumSets().forEach((item, index) => {
                    self.createViewAttendanceItems(item.attendanceItems(), index);
                  });
                  $("#memo").focus();
                } else {
                  self.reloadHistoryList();
                  $("#memo").focus();
                }
              }
              nts.uk.ui.block.clear();
            }).fail((res1, res2) => {
              nts.uk.ui.dialog.alertError(res1.message + '\n' + res2.message).then(function () { nts.uk.ui.block.clear(); });
            });
          } else {
            nts.uk.ui.block.clear();
          }
          self.setTabindex();
        });
      }

      /**
       * open create dialog
       */
      createDialog() {
        const self = this;

        const latestHistory = _.head(self.gridPersonCostList());

        const params = {
          latestHistory: latestHistory,
          personalCost: self.currentPersonCost(),
          size: _.size(self.gridPersonCostList()),
          isLastItem: self.isLastItem()
        };

        nts.uk.ui.windows.setShared('PERSONAL_HISTORY', ko.toJS(params));
        nts.uk.ui.windows.sub.modal("/view/kml/001/c/index.xhtml", { title: "履歴の追加", dialogClass: "no-close" })
          .onClosed(() => {
            const personal_cloned = nts.uk.ui.windows.getShared('PERSONAL_CLONED');
            if (!_.isNil(personal_cloned)) {
              if (!personal_cloned) self.createViewAttendanceItemsDefault();
              self.reloadHistoryList();
            }
          });
      }

      /**
       * open edit dialog 
       */
      editDialog(): void {

        var self = this;
        let personCostList: Array<any> = [];
        const gridPersonCostList = self.gridPersonCostList();

        if (gridPersonCostList.length > 1) {
          personCostList.push(gridPersonCostList[0]);
          personCostList.push(gridPersonCostList[1]);
        } else if (gridPersonCostList.length > 0) {
          personCostList.push(gridPersonCostList[0]);
        }

        nts.uk.ui.windows.setShared('personCostList', personCostList);
        nts.uk.ui.windows.setShared('currentPersonCost', vmbase.ProcessHandler.toObjectPersonCost(self.currentPersonCost()));
        nts.uk.ui.windows.sub.modal("/view/kml/001/d/index.xhtml", { title: "履歴の編集", dialogClass: "no-close" }).onClosed(function () {
          let editedIndex = nts.uk.ui.windows.getShared('isEdited');
          if (editedIndex != null) { // when data is edited
            nts.uk.ui.errors.clearAll();
            self.reloadHistoryList();
          }
        });
      }

      /**
       * open select item dialog
       */
      selectDialog(data, index): void {
        nts.uk.ui.block.invisible();
        var self = this;
        let currentList = [];
        ko.utils.arrayForEach(data.attendanceItems(), function (attendanceItem: vmbase.AttendanceItem) {
          currentList.push(attendanceItem.shortAttendanceID);
        });
        nts.uk.ui.windows.setShared('SelectedAttendanceId', currentList, true);
        nts.uk.ui.windows.setShared('Multiple', true);
        servicebase.getAttendanceItemByType(0)
          .done(function (res: Array<any>) {
            nts.uk.ui.block.clear();
            nts.uk.ui.windows.setShared('AllAttendanceObj', _.map(res, function (item) { return item.attendanceItemId }));
            nts.uk.ui.windows.sub.modal("/view/kdl/021/a/index.xhtml", { title: "割増項目の設定", dialogClass: "no-close" }).onClosed(function () {
              let newList = nts.uk.ui.windows.getShared('selectedChildAttendace');
              nts.uk.ui.block.invisible();
              _.defer(() => {
                if (newList != null) {
                  nts.uk.ui.errors.clearAll();
                  if (newList.length != 0) {
                    if (!_.isEqual(newList, currentList)) {
                      //clone Knockout Object
                      //self.currentPersonCost().startDate(self.newStartDate());
                      //self.currentPersonCost(self.clonePersonCostCalculation(self.currentPersonCost()));
                      //elf.newStartDate(self.currentPersonCost().startDate());
                      self.getItem(newList, index);
                    }
                  } else {
                    self.currentPersonCost().premiumSets()[index].attendanceItems([]);
                  }
                }
                nts.uk.ui.block.clear();
              });
              //self.setTabindex();
            });
          }).fail(function (res) {
            nts.uk.ui.dialog.alertError(res.message).then(function () { nts.uk.ui.block.clear(); });
          });

      }

      checkLastItem() {
        var self = this;
        let index = _.findIndex(self.gridPersonCostList(), function (o) { return self.currentGridPersonCost() === o.dateRange; });
        self.isLastItem(index === 0);
      }

      /**
       * clone PersonCostCalculation Object
       */
      private clonePersonCostCalculation(object: vmbase.PersonCostCalculation): vmbase.PersonCostCalculation {
        var self = this;
        let result = vmbase.ProcessHandler.fromObjectPerconCost(
          _.cloneDeep(
            vmbase.ProcessHandler.toObjectPersonCost(object)));
        return result;
      }

      private createViewAttendanceItems(attendances: Array<vmbase.AttendanceItem>, index: number): void {
        var self = this;
        let a = [];
        let s = '';
        ko.utils.arrayForEach(attendances, (attendanceItem) => {
          if (nts.uk.util.isNullOrEmpty(s)) { s = attendanceItem.name; } else { s += ' + ' + attendanceItem.name; }
        });
        a.push(s);
        self.viewAttendanceItems()[index](a);
      }

      private setTabindex(): void {
        return;
      }

      showExportBtn() {
        if (nts.uk.util.isNullOrUndefined(__viewContext.user.role.attendance)
          && nts.uk.util.isNullOrUndefined(__viewContext.user.role.payroll)
          && nts.uk.util.isNullOrUndefined(__viewContext.user.role.officeHelper)
          && nts.uk.util.isNullOrUndefined(__viewContext.user.role.personnel)) {
          $("#print-button").hide();
        } else {
          $("#print-button").show();
        }
      }

      /**
    * closeDialog
    */
      public opencdl028Dialog() {
        var self = this;
        let params = {
          //    date: moment(new Date()).toDate(),
          mode: 1 //basedate
        };
        nts.uk.ui.windows.setShared("CDL028_INPUT", params);
        nts.uk.ui.windows.sub.modal("com", "/view/cdl/028/a/index.xhtml").onClosed(function () {
          var params = nts.uk.ui.windows.getShared("CDL028_A_PARAMS");
          if (!nts.uk.util.isNullOrUndefined(params) && !nts.uk.util.isNullOrUndefined(params.standardDate)) {
            self.exportExcel(params.standardDate);
          }
        });

      }

      public exportExcel(param): void {
        var self = this;
        let params = ({
          baseDate: param,
          languageId: _.isNil(self.langId()) ? "ja" : self.langId()
        });
        nts.uk.ui.block.grayout();
        servicebase.saveAsExcel(params).done(function () {
        }).fail(function (error) {
          nts.uk.ui.dialog.alertError({ messageId: error.messageId });
        }).always(function () {
          nts.uk.ui.block.clear();
        });

      }

      changeUnitPrice(newPrice: number) {
        const self = this;
        _.forEach(self.currentPersonCost().premiumSets(), (item, index) => {
          self.currentPersonCost().premiumSets()[index].unitPrice(newPrice);
        });
      }

      loadHistoryListPanel(dataResource: Array<any>, currentHistory: string) {
        const self = this;

        let historyItem = [];
        self.gridPersonCostList.removeAll();
        if (!_.isNil(dataResource) && dataResource.length > 0) {
          let tempHistory: string = currentHistory;
          dataResource.forEach(function (item, index) {
            //create key
            if (tempHistory === null) tempHistory = item.startDate + self.textKML001_40 + item.endDate;
            historyItem.push(
              new vmbase.GridPersonCostCalculation(
                item.startDate + self.textKML001_40 + item.endDate,
                item.companyID,
                item.historyID,
                item.startDate,
                item.endDate
              )
            )
          });

          self.gridPersonCostList(_.orderBy(historyItem, 'endDate', 'desc'));
          if (currentHistory === null) {
            self.currentGridPersonCost(tempHistory);
            self.selectedHistory(_.head(self.gridPersonCostList()));//latest
            self.isLastItem(true);
          }
        }
      }

      getPersonalDetails(value: string) {
        const self = this;

        if (value != null) {

          nts.uk.ui.errors.clearAll();
          self.checkLastItem();

          self.$blockui('grayout');

          let findHistory = _.find(self.gridPersonCostList(), (x) => x.dateRange === value);
          self.selectedHistory(findHistory);
          servicebase.findByHistoryID({ historyID: findHistory.historyId }).done((data) => {
            if (data) {
              self.getPersonalCostCalculatorDetails(data);
            }
            self.$blockui('hide');
          }).fail(res => { })
            .always(() => {
              self.$blockui('hide');
            });

          self.isInsert(false);
          $("#memo").focus();

        } else {
          $("#startDateInput").focus();
        }
      }

      getPersonalCostCalculatorDetails(data: any) {
        const self = this;
        if (!_.isNil(data)) {

          let premiumSets: Array<vmbase.PremiumSettingInterface> = [];

          self.createViewAttendanceItemsDefault(data.premiumSets.length);

          data.premiumSets.forEach((item, index) => {
            let attendanceNames: Array<vmbase.AttendanceItem> = [];
            let listAttendance: Array<string> = [];

            _.forEach(item.attendanceNames, (item) => {
              attendanceNames.push(new vmbase.AttendanceItem(item.attendanceItemId, item.attendanceItemName));
            });

            self.createViewAttendanceItems(attendanceNames, index);

            let premiumSetting: vmbase.PremiumSettingInterface = {};
            premiumSetting.companyID = data.companyID;
            premiumSetting.historyID = data.historyID;
            premiumSetting.displayNumber = item.id;
            premiumSetting.rate = item.rate;
            premiumSetting.name = item.name;
            premiumSetting.unitPrice = item.unitPrice;
            premiumSetting.useAtr = item.useAtr;
            premiumSetting.attendanceItems = attendanceNames;
            premiumSets.push(premiumSetting);
          });

          let tempPerson: vmbase.PersonCostCalculationInterface = {};
          tempPerson.companyID = data.companyID;
          tempPerson.historyID = data.historyID;
          tempPerson.startDate = data.startDate;  //startDate
          tempPerson.endDate = data.endDate;
          tempPerson.unitPrice = data.unitPrice;
          tempPerson.memo = data.memo;
          tempPerson.premiumSets = premiumSets;
          tempPerson.calculationSetting = data.howToSetUnitPrice;
          tempPerson.roundingUnitPrice = data.personCostRoundingSetting.unitPriceRounding;
          tempPerson.unit = data.personCostRoundingSetting.unit;
          tempPerson.inUnits = data.personCostRoundingSetting.rounding;
          tempPerson.workingHour = data.workingHoursUnitPrice;
          tempPerson.personCostRoundingSetting = data.personCostRoundingSetting;

          self.currentPersonCost().updateData(tempPerson);
        }
      }

      getCompanyPremiumList() {
        const self = this;

        servicebase.premiumItemSelect().done((data) => {
          if (data) {
            data.forEach(function (item) {
              self.premiumItems.push(
                new vmbase.PremiumItem(
                  item.companyID,
                  item.displayNumber,
                  item.name,
                  item.useAtr,
                  false,
                  0
                ));
            });
          }
        });
      }

      createViewAttendanceItemsDefault(length: number = 10) {
        const self = this;
        //self.viewAttendanceItems.removeAll();
        _.forEach(self.viewAttendanceItems(), (x, index) => {
          self.viewAttendanceItems()[index]('');
        });
      }
    }
  }
}