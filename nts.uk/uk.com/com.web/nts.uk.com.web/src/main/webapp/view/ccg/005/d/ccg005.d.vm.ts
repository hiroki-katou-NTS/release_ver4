/// <reference path='../../../../lib/nittsu/viewcontext.d.ts' />
module nts.uk.at.view.ccg005.d.screenModel {

    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import modal = nts.uk.ui.windows.sub.modal;
   import Output = nts.uk.com.view.cdl008.a.viewmodel.OutPut;
  const API = {
    getFavoriteInformation: "screen/com/ccg005/get-favorite-information",
    save: "ctx/office/favorite/save",
    delete: "ctx/office/favorite/delete"
  };
  @bean()
  export class ViewModel extends ko.ViewModel {
    
    //favorite
    favoriteList: KnockoutObservableArray<FavoriteSpecifyData> = ko.observableArray([]);
    favoriteName: KnockoutObservable<string> = ko.observable("");
    selectedFavoriteOrder: KnockoutObservable<number> = ko.observable();
    selectedFavorite: KnockoutObservable<FavoriteSpecifyData> = ko.observable();

    //work place
    workPlaceIdList: KnockoutObservableArray<string> = ko.observableArray([]);
    choosenWkspNames: KnockoutObservableArray<string> = ko.observableArray([]);
    displayChoosenWkspName: KnockoutObservable<string> = ko.computed(() => {
      return this.choosenWkspNames().join('、');
    });

    //target select
    roundingRules: KnockoutObservableArray<any>= ko.observableArray([
      { code: TargetSelection.WORKPLACE, name: this.$i18n("CCG005_14") },
      { code: TargetSelection.AFFILIATION_WORKPLACE, name: this.$i18n("CCG005_16") }
    ]);
    selectedRuleCode: KnockoutObservable<number> = ko.observable(TargetSelection.WORKPLACE);
    buttonSelectRequire: KnockoutObservable<boolean> = ko.computed(() => this.selectedRuleCode() === 1);
    displayChoosenWkspNameRequire: KnockoutObservable<boolean> = ko.computed(() => this.selectedRuleCode() === 0);

    //grid column
    columns: KnockoutObservableArray<any> = ko.observableArray([
      { headerText: "id", key: "order", width: 150, hidden: true },
      { headerText: this.$i18n("CCG005_11"), key: "favoriteName", width: 150 },
    ]);

    //mode
    mode: KnockoutObservable<number> = ko.observable(Mode.UPDATE);

    mounted() {
      const vm = this;
      vm.$blockui("grayout");
      vm.$ajax(API.getFavoriteInformation).then((data: FavoriteSpecifyData[]) => {
        vm.favoriteList(data);
        if (data) {
          const firstOrder = data[0].order;
          vm.selectedFavoriteOrder(firstOrder);
          vm.bindingData(firstOrder);
        }
        vm.selectedFavoriteOrder.subscribe((order: number) => {
          vm.bindingData(order);
        });
        vm.$blockui("clear");
      })
    }

    private bindingData(order: number) {
      const vm = this;
      vm.$errors("clear");
      const currentFavor = _.find(vm.favoriteList(), (item => Number(item.order) === Number(order)));
      if (currentFavor) {
        vm.selectedFavorite(currentFavor);
        vm.favoriteName(currentFavor.favoriteName);
        vm.selectedRuleCode(currentFavor.targetSelection);
        vm.choosenWkspNames(currentFavor.wkspNames);
        vm.workPlaceIdList(currentFavor.workplaceId);
      }
    }

    private callData() {
      const vm = this;
      vm.$blockui("grayout");
      vm.$ajax(API.getFavoriteInformation).then((data: FavoriteSpecifyData[]) => {
        vm.favoriteList(data);
        vm.$blockui("clear");
      })
    }

    public onClickCancel() {
      this.$window.close();
    }

    public createNewFavorite() {
      const vm = this;
      vm.mode(Mode.INSERT);
      vm.favoriteName("");
      vm.choosenWkspNames([]);
      $("#D5_1").focus();
    }

    public saveFavorite() {
      const vm = this;
      if(!vm.displayChoosenWkspNameRequire()) {
        $('#D5_4').ntsError('set', {messageId:"Msg_2076"});
      }
      vm.$validate().then((valid: boolean) => {
        if (valid) {
          //new item
          if (vm.mode() === Mode.INSERT) {
            const favoriteSpecify = new FavoriteSpecifyData({
              favoriteName: vm.favoriteName(),
              creatorId: __viewContext.user.employeeId,
              inputDate: moment.utc().toISOString(),
              targetSelection: vm.selectedRuleCode(),
              workplaceId: vm.selectedRuleCode() === TargetSelection.WORKPLACE ? vm.workPlaceIdList() : [],
              order: vm.favoriteList()[vm.favoriteList().length - 1].order + 1,
              wkspNames: vm.choosenWkspNames()
            });
            vm.favoriteList.push(favoriteSpecify);
            //Update UI
            vm.selectedFavoriteOrder(favoriteSpecify.order);
            vm.mode(Mode.UPDATE);
          } else {
            _.map(vm.favoriteList(), (item => {
              if (item.order === Number(vm.selectedFavoriteOrder())) {
                item.favoriteName = vm.favoriteName();
                item.targetSelection = vm.selectedRuleCode();
                item.workplaceId = vm.selectedRuleCode() === TargetSelection.WORKPLACE ? vm.workPlaceIdList() : [];
              }
            }));
          }
          //re set order for item
          _.map(vm.favoriteList(), (item, index) => {
            item.order = index;
          });
           //Call API
          vm.$blockui("grayout");
          vm.$ajax(API.save, vm.favoriteList()).then(() => {
            vm.callData();
            vm.$blockui("clear");
            return vm.$dialog.info({ messageId: "Msg_15" });
          });
        }
      });
    }

    public deleteFavorite() {
      const vm = this;
      vm.$dialog.confirm({ messageId: "Msg_18" }).then((result) => {
        if (result === "yes") {
          const currentFavor = _.find(vm.favoriteList(), (item => Number(item.order) === Number(vm.selectedFavoriteOrder())));
          if (currentFavor) {
            const favoriteSpecify = new FavoriteSpecifyDelCommand({
              creatorId: currentFavor.creatorId,
              inputDate: currentFavor.inputDate
            });
            vm.$blockui("grayout");
            vm.$ajax(API.delete, favoriteSpecify).then(() => {
              //set selected to 0 when delete
              if (vm.favoriteList()) {
                vm.selectedFavoriteOrder(0);
              }
              vm.callData();
              vm.$blockui("clear");
              return vm.$dialog.info({ messageId: "Msg_16" });
            });
          }
        }
      });
    }
  
    openCDL008Dialog(): void {
      const vm = this;
      const inputCDL008: any = {
        startMode: 0, // 起動モード : 職場 (WORKPLACE = 0)
        isMultiple: true, //選択モード : 複数選択
        showNoSelection: false, //未選択表示 : 非表示
        selectedCodes: vm.workPlaceIdList(), //選択済項目 : 先頭
        isShowBaseDate: true, //基準日表示区分 : 表示
        baseDate: moment.utc().toISOString(), // 基準日 : システム日
        selectedSystemType: 2, //システム区分 : 就業
        isrestrictionOfReferenceRange: false // 参照範囲の絞込 : しない
      };
      setShared('inputCDL008', inputCDL008);
      modal('/view/cdl/008/a/index.xhtml').onClosed(() => {
        const isCancel = getShared('CDL008Cancel');
        if (isCancel) {
          return;
        }
        const selectedInfo: Output[] = getShared('workplaceInfor');
        const listWorkPlaceId: string[] = [];
        const listWorkPlaceName: string[] = [];
        _.map(selectedInfo, ((item: Output) => {
          listWorkPlaceId.push(item.id);
          listWorkPlaceName.push(item.displayName);
          
        }))
        vm.workPlaceIdList(listWorkPlaceId);
        vm.choosenWkspNames(listWorkPlaceName);
      });
    }

  }

  enum TargetSelection {
    // 職場
    WORKPLACE = 0,

    // 所属職場
    AFFILIATION_WORKPLACE = 1
  }

  enum Mode {
    INSERT = 0,
    UPDATE = 1
  }
  class FavoriteSpecifyData {

    // お気に入り名
    favoriteName: string;

    // 作��D
    creatorId: string;

    // 入力日
    inputDate: string;

    // 対象選�
    targetSelection: number;

    // 職場ID
    workplaceId: string[];

    // 頺
    order: number;

    wkspNames: string[];

    constructor(init?: Partial<FavoriteSpecifyData>) {
      $.extend(this, init);
    }
  }

  class FavoriteSpecifyDelCommand {
    // 作��D
    creatorId: string;

    // 入力日
    inputDate: string;

    constructor(init?: Partial<FavoriteSpecifyDelCommand>) {
      $.extend(this, init);
    }
  }
}
