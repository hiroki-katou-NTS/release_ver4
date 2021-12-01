module nts.uk.at.view.kaf002_ref.m.viewmodel {
    const template = `

<div
	id="kaf002TabPanel"
	data-bind="ntsTabPanel: { dataSource:  $component.tabs, active: selectedTab }" style="width: 450px">
	<div data-bind="if: comment1().content != ''" >
		<div
			data-bind="text: comment1().content,
					   style: {color: comment1().color,
					   marginBottom:'9px',
					   fontWeight: comment1().isBold ? 'bold' : 'normal'}"
			class="label"
			style="white-space: break-spaces; width: auto !important">
			
		</div>
	</div>

	<div data-bind="foreach: $component.nameGrids">
		<div
			data-bind="css: { 'hidden': $component.selectedTab() !== 'tab-' + ($index() + 1) }">
			<table data-bind="attr: { 'id': $data }"></table>
		</div>
	</div>

	<div data-bind="if: comment2().content != ''" >
		<div
			data-bind="text: comment2().content,
					   style: {color: comment2().color,
					   marginTop:'10px',
					   fontWeight: comment2().isBold ? 'bold' : 'normal'}"
			class="label"
			style="white-space: break-spaces; width: auto !important">
			
		</div>
	</div>


</div>
`;

    @component({
        name: 'kaf002-m',
        template: template
    })
    class Kaf002MViewModel extends ko.ViewModel {
        tabs: KnockoutObservableArray<any> = ko.observableArray([]);

        selectedTab: KnockoutObservable<string>;

        // set enable checkbox all
        enableList: Array<KnockoutObservable<boolean>>;

        // tab can load more >=10

        isLinkList: Array<boolean>;

        nameGrids: KnockoutObservableArray<any>;

        //set param
        // display column checkbox
        isVisibleComlumn: boolean;

        //list data
        // set param
        dataSource: Array<Array<GridItem>> = [];

        dataSourceOb: KnockoutObservableArray<any>;
        // set param
        tabMs: Array<TabM>;

        isPreAtr: KnockoutObservable<boolean>;
        tabsTemp: any;
        selectedTemp: any;
        reasonList: Array<GoOutTypeDispControl>;
        mode: KnockoutObservable<number>;

		comment1: KnockoutObservable<Comment> = ko.observable(new Comment('', true, ''));
        comment2: KnockoutObservable<Comment> = ko.observable(new Comment('', true, ''));

        created(params: any) {

            const self = this;
			
			const comment1 = params.comment1 as KnockoutObservable<Comment>;
			self.comment1(ko.unwrap(comment1));
			comment1.subscribe((comment) => {
				if (comment) {
					self.comment1(comment);
				}
			})
			const comment2 = params.comment2 as KnockoutObservable<Comment>;
			self.comment2(ko.unwrap(comment2));
			comment2.subscribe((comment) => {
				if (comment) {
					self.comment2(comment);
				}
			})


            self.mode = params.mode;
            self.reasonList = params.reasonList
            self.tabMs = params.tabMs;
            self.isPreAtr = params.isPreAtr;
            self.isVisibleComlumn = params.isVisibleComlumn;
            self.dataSourceOb = params.dataSourceOb;
            self.dataSource = self.dataSourceOb();
            self.dataSourceOb.subscribe((data) => {
                if (ko.toJS(data)) {
                    self.dataSource = self.dataSourceOb();
                    self.loadAll();
					
            	}
            });
            self.initDataSource();
            // param of parent
            let nameGridsArray = [] as Array<any>;
            let paramTabs = [] as Array<any>;
            self.enableList = [];
            self.isLinkList = [];
            _.each(self.tabMs, (item, index) => {

                let paramTab = {
                    id: 'tab-' + String(index + 1),
                    title: item.title, content: '.tab-content-' + String(index + 1),
                    enable: ko.observable(item.enable),
                    visible: ko.observable(item.visible)
                };
                paramTabs.push(paramTab);
                self.enableList.push(ko.observable(false));
                self.isLinkList.push(true);
                nameGridsArray.push('grid' + String(index + 1));
            });
            self.nameGrids = ko.observableArray(nameGridsArray);
            self.tabs(paramTabs);
            // must assign param.tabs at mounted since tabs is not render
            self.tabsTemp = params.tabs;
            // select first tab
            self.selectedTab = ko.observable(paramTabs[0].id);
            self.selectedTemp = params.selectedTab;
            self.selectedTab.subscribe(value => {
				if (self.isPreAtr()) {
					if (self.selectedTab() == 'tab-2') {
							$('#kaf002TabPanel').width(580)							
					}
                    else if (self.selectedTab() === 'tab-1' || self.selectedTab() === 'tab-6') {
                        $('#kaf002TabPanel').width(900);
                    }
                    else {
                        $('#kaf002TabPanel').width(450)							
					}
				
				} else {
					if (self.selectedTab() == 'tab-2') {
							$('#kaf002TabPanel').width(680)							
					}
                    else if (self.selectedTab() === 'tab-1' || self.selectedTab() === 'tab-6') {
                        $('#kaf002TabPanel').width(1000);
                    }
                    else {
							$('#kaf002TabPanel').width(550)							
					}
				}
				if (value) {
					
					if (value == 'tab-1') {
	                        self.selectedTemp(0);
	                   
	                } else if (value == 'tab-2') {
	                    self.selectedTemp(1);
	                   
	                } else if (value == 'tab-3') {
	                    self.selectedTemp(5);
	                   
	                } else if (value == 'tab-4') {
	                    self.selectedTemp(2);
	                   
	                } else if (value == 'tab-5') {
	                    self.selectedTemp(4);
	                    
	                } else if (value === 'tab-6') {
	                    self.selectedTemp(STAMPTYPE.CHEERING);
	                }
				}
				

                
            })
            self.isPreAtr.subscribe((value) => {
                if (!_.isNull(value) && self.mode() == 0) {
                    self.loadAll();
					
                }

            });



        }
        createdReasonItem(reasonList: Array<GoOutTypeDispControl>) {
            let comboItems = [] as Array<any>;
            _.forEach(reasonList, (e: GoOutTypeDispControl) => {
                if (e.goOutType == 0) {
                    comboItems.push(new ItemModel('0', '私用'));
                } else if (e.goOutType == 1) {
                    comboItems.push(new ItemModel('1', '公用'));
                } else if (e.goOutType == 2) {
                    comboItems.push(new ItemModel('2', '有償'));
                } else if (e.goOutType == 3) {
                    comboItems.push(new ItemModel('3', '組合'));
                }
            });
            return comboItems;
        }
        doSomething(s: Array<GridItem>) {
            const self = this;
            if (!s) {
                return;
            }
            const index = s[0].index;
            self.isLinkList[index] = false;
			_.forEach(s, (item: GridItem) => {
				const start = (Number)(item.startTimeRequest());
				const end = (Number)(item.endTimeRequest());
				if (_.isNaN(start) || start >= 7200 || start <= -1200) {
					item.startTimeRequest(null);
				}
				if (_.isNaN(end) || end >= 7200 || end <= -1200) {
					item.endTimeRequest(null);
				} 
			})
            self.loadGrid(ko.toJS(self.nameGrids)[index], s, s[0].typeStamp);
            self.binding();
            self.disableControl();
        }
        binding() {
            let self = this;
            _.each(document.getElementsByClassName('startTime'), (item, index) => {
                if (!$('.startTime')[index]) return;
                ko.cleanNode($('.startTime')[index]);
                ko.applyBindings(self, item);
            });

            _.each(document.getElementsByClassName('endTime'), (item, index) => {
                if (!$('.endTime')[index]) return;
                ko.cleanNode($('.endTime')[index]);
                ko.applyBindings(self, item);
            });

            _.each(document.getElementsByClassName('flag'), (item, index) => {
                if (!$('.flag')[index]) return;
                ko.cleanNode($('.flag')[index]);
                ko.applyBindings(self, item);
            });
            _.each(ko.toJS(self.nameGrids), (item, index) => {
                if (!$('#' + item + '_flag')[0]) return;
                ko.cleanNode($('#' + item + '_flag')[0]);
                ko.applyBindings(self, document.getElementById(item + '_flag'));
            });
        }
        mounted() {
            const self = this;
            // change tabs by root component
            self.tabsTemp(self.tabs());
            self.loadAll();
			self.selectedTab(_.find(self.tabs(), item => item.visible()).id)

        }
        loadAll() {
            const self = this;
            if (self.isPreAtr()) {
				if (self.selectedTab() == 'tab-2') {
						$('#kaf002TabPanel').width(580)							
				}
                else if (self.selectedTab() === 'tab-1' || self.selectedTab() === 'tab-6') {
                    $('#kaf002TabPanel').width(900);
                }
                else {
						$('#kaf002TabPanel').width(450)							
				}
				
			} else {
				if (self.selectedTab() == 'tab-2') {
						$('#kaf002TabPanel').width(680)							
				}
                else if (self.selectedTab() === 'tab-1' || self.selectedTab() === 'tab-6') {
                    $('#kaf002TabPanel').width(1000);
                }
                else {
						$('#kaf002TabPanel').width(550)							
				}
			}
            if (_.isEmpty(self.dataSource)) return;

            _.each(self.dataSource, (item, index) => {
                if (!_.isEmpty(item)) {
                    _.forEach(item, (i, indexI) => {
                        let startRequest = i.startTimeRequest();
                        let endRequest = i.endTimeRequest();
                        if (_.isNaN(Number(startRequest)) || Number(startRequest) > 4319 || Number(startRequest) < (-720)) {
                            i.startTimeRequest(null);
                        }
                        if (_.isNaN(Number(endRequest)) || Number(endRequest) > 4319 || Number(endRequest) < (-720)) {
                            i.endTimeRequest(null);
                        }
                        if (self.mode() == 0) {
                            if (i.typeStamp == STAMPTYPE.GOOUT_RETURNING) {
                                i.typeReason = String(self.createdReasonItem(self.reasonList)[0].code);
                            }

                        }
                        // set again id if remove tab1 with 2 first element
                        i.id = indexI + 1;
                        i.index = index;
                        // change text element to know value biding from array
                        i.changeElement();

                        if (ko.toJS(self.isPreAtr)) {
                            //                            self.isVisibleComlumn = false; 
                            i.changeElementByPreAtr();
                        }

                    });

                }
            });
            _.each(self.dataSource, (item, index) => {
                if (!_.isEmpty(item)) {
                    self.loadGrid(ko.toJS(self.nameGrids)[index], item, item[0].typeStamp);
                }
            })
            self.binding();
            self.disableControl();
        }

        disableControl() {
            const self = this;
            if (self.mode() == 2) {
                var loop = window.setInterval(function () {
                    if ($('.startTime input') && $('.endTime input') && $('.enableFlag input')) {
                        _.forEach($('.startTime input'), i => { $(i).prop('disabled', true) });
                        _.forEach($('.endTime input'), i => { $(i).prop('disabled', true) });
                        _.forEach($('.enableFlag input'), i => { $(i).prop('disabled', true) });
                        window.clearInterval(loop);
                    }
                }, 100);
            }
        }
        constructor() {
            super();

        }

        initDataSource() {
            const self = this;
            _.each(self.dataSource, (item, index) => {
                _.forEach(item, i => {
                    i.index = index;

                    // change text element to know value biding from array
                    i.changeElement();
                });
            });

        }

        loadGrid(id: string, items: any, type: number) {
            const self = this;
            
			
			if (!id || !items) return;
			
            const isChrome = /Chrome/.test(navigator.userAgent) && /Google Inc/.test(navigator.vendor);

			
			
			
            if ($('#' + id + '_container').length) {
                $('#' + id).ntsGrid("destroy");
            }
            let statesTable = [] as Array<any>;
            let numberDisable = 0;

            for (let i = 1; i < items.length + 1; i++) {
                if (!ko.toJS(items[i - 1].flagEnable)) {
                    numberDisable++;
                }

            }
            if (self.enableList) {
                _.each(self.enableList, (item, index) => {
                    if (index == items[0].index) {
                        item.subscribe((value) => {
                            _.forEach(self.dataSource[index], i => {
                                if (ko.toJS(i.flagEnable)) {
                                    i.flagObservable(value);
                                }
                            });
                        });
                    }

					_.forEach(self.dataSource[index], i => {
                        if (ko.toJS(i.flagEnable)) {
                            i.flagObservable.subscribe((value) => {
								if (value) {
									const list = _.filter(self.dataSource[index], itemDetail => {
										return !itemDetail.flagObservable() && itemDetail.flagEnable();
									});	
									if (index == items[0].index && !item() && !list.length) {
										item(true);
									}								
								} else {
									const list = _.filter(self.dataSource[index], itemDetail => {
										return itemDetail.flagObservable() && itemDetail.flagEnable();
									});
									if (index == items[0].index && item() && !list.length) {
										item(false);
									}
									
								}
								
							})
                        }
                    });

                });


            }
            //
            let headerFlagContent = '';
            let dataSource;
            _.each(self.isLinkList, (i, index) => {
                if (items[0].index == index) {
                    let paramString = 'enableList[' + String(index) + ']';
                    headerFlagContent = numberDisable != items.length ? '<div class="ntsCheckbox-002" style="display: block" align="center" data-bind="ntsCheckBox: { checked: ' + paramString + '}">' + self.$i18n('KAF002_72') + '</div>' : '<div style="display: block" align="center">' + self.$i18n('KAF002_72') + '</div>';

                    dataSource = items.length >= 10 && self.isLinkList[index] ? items.slice(0, 3) : items;
                }

            });

            let optionGrid = {
                width: (((!self.isVisibleComlumn && !ko.toJS(self.isPreAtr)) || ko.toJS(self.isPreAtr))) ? '420px' : '520px',
                height: isChrome ? (ko.toJS(self.isPreAtr) ? '310px' : '340px') : (ko.toJS(self.isPreAtr) ? '310px' : '340px'),
                dataSource: dataSource,
                primaryKey: 'id',
                virtualization: true,
                virtualizationMode: 'continuous',
                hidePrimaryKey: true,
                columns: [
                    { headerText: 'ID', key: 'id', dataType: 'number', width: '50px', ntsControl: 'Label' },
                    { headerText: '', key: 'text1', dataType: 'string', width: '120px' },
                    { headerText: self.$i18n('KAF002_22'), key: 'startTime', dataType: 'string', width: '140px' },
                    { headerText: self.$i18n('KAF002_23'), key: 'endTime', dataType: 'string', width: '140px' },
                    { headerText: headerFlagContent, key: 'flag', dataType: 'string', width: '100px' }

                ],
                features: [{
                    name: 'Resizing',
                    columnSettings: [{
                        columnKey: 'id', allowResizing: false, minimumWidth: 30
                    }, {
                        columnKey: 'startTime', allowResizing: false, minimumWidth: 30
                    }, {
                        columnKey: 'endTime', allowResizing: false, minimumWidth: 30
                    }, {
                        columnKey: 'flag', allowResizing: false, minimumWidth: 30
                    }
                    ]
                },
                {
                    name: 'Selection',
                    mode: 'row',
                    multipleSelection: true
                }
                ],
                ntsFeatures: [
                    {
                        name: 'CellState',
                        rowId: 'rowId',
                        columnKey: 'columnKey',
                        state: 'state',
                        states: statesTable
                    }
                ],
                ntsControls: []

            };

            // Define grid options for  外出／戻り mode
            let comboColumns = [
                { prop: 'name', length: 6 }];
            let comboItems = self.mode() == 0 ? self.createdReasonItem(self.reasonList)
                :
                [new ItemModel('0', '私用'),
                new ItemModel('1', '公用'),
                new ItemModel('2', '有償'),
                new ItemModel('3', '組合')];
            let option2 = {
              width: (((!self.isVisibleComlumn && !ko.toJS(self.isPreAtr)) || ko.toJS(self.isPreAtr))) ? '555px' : '655px',
              height: isChrome ? (ko.toJS(self.isPreAtr) ? '310px' : '340px') : (ko.toJS(self.isPreAtr) ? '310px' : '340px'),
              dataSource: dataSource,
              primaryKey: 'id',
              virtualization: true,
              virtualizationMode: 'continuous',
              hidePrimaryKey: true,
              columns: [
                  { headerText: 'ID', key: 'id', dataType: 'number', width: '50px', ntsControl: 'Label' },
                  { headerText: '', key: 'text1', dataType: 'string', width: '120px' },
                  { headerText: self.$i18n('KAF002_24'), key: 'typeReason', dataType: 'string', width: '137px', ntsControl: 'Combobox' },
                  { headerText: self.$i18n('KAF002_22'), key: 'startTime', dataType: 'string', width: '140px' },
                  { headerText: self.$i18n('KAF002_23'), key: 'endTime', dataType: 'string', width: '140px'},
                  { headerText: headerFlagContent, key: 'flag', dataType: 'string', width: '100px'}

              ],
              features: [{ name: 'Resizing',
                              columnSettings: [{
                                  columnKey: 'id', allowResizing: true, minimumWidth: 30
                              },  {
                                  columnKey: 'startTime', allowResizing: false, minimumWidth: 30
                              }, {
                                  columnKey: 'endTime', allowResizing: false, minimumWidth: 30
                              }, {
                                  columnKey: 'flag', allowResizing: false, minimumWidth: 30
                              }
                              ]
                          },
                          {
                              name: 'Selection',
                              mode: 'row',
                              multipleSelection: true
                          }
              ],
              ntsFeatures: [
                  { name: 'CellState',
                      rowId: 'rowId',
                      columnKey: 'columnKey',
                      state: 'state',
                      states: statesTable
                  }
                  ],
              ntsControls: [
                            { name: 'Combobox', width: '50px', height: '100px', options: comboItems, optionsValue: 'code', optionsText: 'name', columns: comboColumns, controlType: 'ComboBox', enable: self.mode() != 2 ? true : false, spaceSize: 'small' }
                              ]
              };
            if (!self.isVisibleComlumn || ko.toJS(self.isPreAtr)) {
                option2.columns.pop();
                optionGrid.columns.pop();
            }


            if (type == STAMPTYPE.GOOUT_RETURNING) {
                if ($('#' + id).length) {
                    $('#' + id).ntsGrid(option2);
                }
            } else if (type === STAMPTYPE.ATTENDENCE) {
                if ($('#' + id).length) {
                    $('#' + id).ntsGrid(self.getAttendanceGrid(isChrome, dataSource, headerFlagContent, statesTable));
                }
            } else if (type === STAMPTYPE.CHEERING) {
                if ($('#' + id).length) {
                    $('#' + id).ntsGrid(self.getAttendanceGrid(isChrome, dataSource, headerFlagContent, statesTable));
                    
                    const $expandRow = $('<tr id="trLinkCheer">');
                    const $firstCol = $('<td class="titleCorlor" style="height: 50px; background-color: #CFF1A5">');
                    const $secondCol = $('<td colspan="5">');
                    const $secondCol__div = $('<div id="moreRow' + String(items[0].index) + '" style="display: block" align="center">');
                    const $secondCol__div__link = $(`<a style="color: blue; text-decoration: underline">${self.$i18n('KAF002_85', ['0'])}</a>`)
                    $secondCol__div__link.click(() => self.doSomething(self.dataSource[items[0].index]));
                    $secondCol__div.append($secondCol__div__link);
                    $secondCol.append($secondCol__div);
                    $expandRow.append($firstCol);
                    $expandRow.append($secondCol);
                    $('#' + id).append($expandRow);
                }
            } else {
                if ($('#' + id).length) {
                    $('#' + id).ntsGrid(optionGrid);
                }
            }
            // if isCondition2 => error state of text1
            let nameAtr = 'td[aria-describedby ="' + id + '_text1"]';
            if ($(nameAtr)) {
                $(nameAtr).addClass('titleColor');
            }

            let buttonWorkplaceAtr = 'td[aria-describedby ="' + id + '_workplaceId"]';
            if ($(buttonWorkplaceAtr)) {
                $(buttonWorkplaceAtr).addClass('btn_workplace');
            }

            let buttonWorkLocationAtr = 'td[aria-describedby ="' + id + '_workLocaiton"]';
            if ($(buttonWorkLocationAtr)) {
                $(buttonWorkLocationAtr).addClass('btn_worklocation');
            }

            // add row to display expand row
            if (items.length >= 10 && self.isLinkList[items[0].index]) {
                if ($('#' + id).length) {
                    const $expandRow = $('<tr id="trLink2">');
                    const $firstCol = $('<td>')
                    const $secondCol = $('<td class="titleCorlor" style="height: 50px; background-color: #CFF1A5">')
                    const $thirdCol = $('<td colspan="4">');
                    const $thirdCol__div = $('<div id="moreRow' + String(items[0].index) + '" style="display: block" align="center">')
                    $thirdCol__div.append('<a style="color: blue; text-decoration: underline" data-bind="click: doSomething.bind($data, dataSource[' + items[0].index + ']), text: \'' + self.$i18n('KAF002_73') + '\'"></a>');
                    
                    $thirdCol.append($thirdCol__div);
                    $expandRow.append($firstCol);
                    $expandRow.append($secondCol);
                    $expandRow.append($thirdCol);
                    $('#' + id).append($expandRow);
                }

            } else {
                self.isLinkList[items[0].index] = false;
            }

            let moreRow = document.getElementById('moreRow' + String(items[0].index));
            if (moreRow && self.isLinkList[items[0].index]) {
                ko.applyBindings(self, moreRow);
            }


        }

        private getAttendanceGrid(isChrome: boolean, dataSource: any, headerFlagContent: any, statesTable: any) {
            const self = this;
            let options = {
                width: (((!self.isVisibleComlumn && !ko.toJS(self.isPreAtr)) || ko.toJS(self.isPreAtr))) ? '880px' : '980px',
                height: isChrome ? (ko.toJS(self.isPreAtr) ? '310px' : '340px') : (ko.toJS(self.isPreAtr) ? '310px' : '340px'),
                dataSource: dataSource,
                primaryKey: 'id',
                virtualization: true,
                virtualizationMode: 'continuous',
                hidePrimaryKey: true,
                columns: [
                    { headerText: 'ID', key: 'id', dataType: 'number', width: 0, hidden: true },
                    { headerText: '', key: 'text1', dataType: 'string', width: 120 },
                    { headerText: self.$i18n('KAF002_22'), key: 'startTime', dataType: 'string', width: 140 },
                    { headerText: self.$i18n('KAF002_23'), key: 'endTime', dataType: 'string', width: 140 },
                    { headerText: self.$i18n('KAF002_81'), key: 'workplaceId', dataType: 'string', width: 230, ntsControl: 'Button_WorkPlace' },
                    { headerText: self.$i18n('KAF002_82'), key: 'workLocaiton', dataType: 'string', width: 230, ntsControl: 'Button_WorkLocation' },
                    { headerText: headerFlagContent, key: 'flag', dataType: 'string', width: 100 }
                ],
                features: [
                    {
                        name: 'Resizing',
                        columnSettings: [
                            {
                                columnKey: 'id', allowResizing: true, minimumWidth: 30
                            },
                            {
                                columnKey: 'startTime', allowResizing: false, minimumWidth: 30
                            },
                            {
                                columnKey: 'endTime', allowResizing: false, minimumWidth: 30
                            },
                            {
                                columnKey: 'flag', allowResizing: false, minimumWidth: 30
                            }
                        ]
                    },
                    {
                        name: 'Selection',
                        mode: 'row',
                        multipleSelection: true
                    }
                ],
                ntsFeatures: [
                    {
                        name: 'CellState',
                        rowId: 'rowId',
                        columnKey: 'columnKey',
                        state: 'state',
                        states: statesTable
                    }
                ],
                ntsControls: [
                    {
                        name: 'Button_WorkPlace',
                        text: nts.uk.resource.getText('KAF002_83'),
                        click: function(data: any) {
                            nts.uk.ui.windows.setShared("inputCDL008", { executionId: data.id });
                            nts.uk.ui.windows.sub.modal("com", "/view/cdl/008/a/index.xhtml").onClosed(() => {
                            });
                        },
                        controlType: 'Button'
                    },
                    {
                        name: 'Button_WorkLocation',
                        text: nts.uk.resource.getText('KAF002_84'),
                        click: function(data: any) {
                            nts.uk.ui.windows.setShared("KDL010SelectWorkLocation", { executionId: data.id });
                            nts.uk.ui.windows.sub.modal("/view/kdl/010/a/index.xhtml").onClosed(() => {
                            });
                        },
                        controlType: 'Button'
                    }
                ]
            };
            if (!self.isVisibleComlumn || ko.toJS(self.isPreAtr)) {
                options.columns.pop();
            }
            return options;
        }

    }


    export class GridItem {
        id: number;
        flag: string;
        startTimeRequest: KnockoutObservable<number> = ko.observable(null);
        endTimeRequest: KnockoutObservable<number> = ko.observable(null);
        startTimeActual: number;
        endTimeActual: number
        typeReason?: string;
        startTime: string;
        endTime: string;
        text1: string;
        flagObservable: KnockoutObservable<boolean> = ko.observable(false);
        flagEnable: KnockoutObservable<boolean> = ko.observable(true);
        index: number;
        nameStart: string;
        nameEnd: string;
        workplace: string;
        workLocation: string;

        typeStamp: STAMPTYPE;
        constructor( dataObject: TimePlaceOutput, typeStamp: STAMPTYPE ) {
            const self = this;
            self.typeStamp = typeStamp;
            self.id = dataObject.frameNo;
            self.typeReason = dataObject.opGoOutReasonAtr ? String(dataObject.opGoOutReasonAtr) : (STAMPTYPE.GOOUT_RETURNING == typeStamp ? '0' : null);
            self.startTimeActual = dataObject.opStartTime;
            self.endTimeActual = dataObject.opEndTime;
            if (_.isNull(dataObject.opStartTime) && _.isNull(dataObject.opEndTime)) {
                self.flagEnable(false);
            }
            let parseTime = nts.uk.time.minutesBased.clock.dayattr;
            let start = _.isNull(self.startTimeActual) ? '--:--' : parseTime.create(self.startTimeActual).shortText;
            let end = _.isNull(self.endTimeActual) ? '--:--' : parseTime.create(self.endTimeActual).shortText;
            let idGetList = typeStamp == STAMPTYPE.EXTRAORDINARY ? self.id - 3 : self.id - 1;
            let param = 'dataSource[' + String(self.index) +']';
			const frameNo = dataObject.frameNo;
            if ( typeStamp == STAMPTYPE.ATTENDENCE ) {
				if (frameNo == 1) {
	                this.text1 = nts.uk.resource.getText( 'KAF002_103', [dataObject.frameNo]);	
					this.nameStart = nts.uk.resource.getText('KAF002_101', [dataObject.frameNo]);
					this.nameEnd = nts.uk.resource.getText('KAF002_102', [dataObject.frameNo]);
                    this.workplace = nts.uk.resource.getText('KAF002_81', [dataObject.frameNo]);
                    this.workLocation = nts.uk.resource.getText('KAF002_82', [dataObject.frameNo]);
				} else {
					this.text1 = nts.uk.resource.getText( 'KAF002_65', [dataObject.frameNo]);
					this.nameStart = nts.uk.resource.getText('KAF002_87', [dataObject.frameNo]);
					this.nameEnd = nts.uk.resource.getText('KAF002_88', [dataObject.frameNo]);
                    this.workplace = nts.uk.resource.getText('KAF002_81', [dataObject.frameNo]);
                    this.workLocation = nts.uk.resource.getText('KAF002_82', [dataObject.frameNo]);
				}
                param = param + 1;
            } else if ( typeStamp == STAMPTYPE.GOOUT_RETURNING ) {
                this.text1 = nts.uk.resource.getText( 'KAF002_67', [dataObject.frameNo] );
				this.nameStart = nts.uk.resource.getText('KAF002_91', [dataObject.frameNo]);
				this.nameEnd = nts.uk.resource.getText('KAF002_92', [dataObject.frameNo]);
                param = param + 3;
            } else if ( typeStamp == STAMPTYPE.BREAK ) {
                this.text1 = nts.uk.resource.getText( 'KAF002_75', [dataObject.frameNo] );
				this.nameStart = nts.uk.resource.getText('KAF002_93', [dataObject.frameNo]);
				this.nameEnd = nts.uk.resource.getText('KAF002_94', [dataObject.frameNo]);
                param = param + 4;
            } else if ( typeStamp == STAMPTYPE.PARENT ) {
                this.text1 = nts.uk.resource.getText( 'KAF002_68', [dataObject.frameNo] );
				this.nameStart = nts.uk.resource.getText('KAF002_95', [dataObject.frameNo]);
				this.nameEnd = nts.uk.resource.getText('KAF002_96', [dataObject.frameNo]);
                param = param + 5;
            } else if ( typeStamp == STAMPTYPE.NURSE ) {
                this.text1 = nts.uk.resource.getText( 'KAF002_69', [dataObject.frameNo] );
				this.nameStart = nts.uk.resource.getText('KAF002_97', [dataObject.frameNo]);
				this.nameEnd = nts.uk.resource.getText('KAF002_98', [dataObject.frameNo]);
                param = param + 6;
            } else if ( typeStamp == STAMPTYPE.EXTRAORDINARY ) {
                this.text1 = nts.uk.resource.getText( 'KAF002_66', [dataObject.frameNo - 2] );
				this.nameStart = nts.uk.resource.getText('KAF002_89', [dataObject.frameNo -2]);
				this.nameEnd = nts.uk.resource.getText('KAF002_90', [dataObject.frameNo -2]);
                param = param + 2;
            } else if (typeStamp === STAMPTYPE.CHEERING) {
                this.text1 = nts.uk.resource.getText( 'KAF002_86', [dataObject.frameNo] );
				this.nameStart = nts.uk.resource.getText('KAF002_99', [dataObject.frameNo]);
				this.nameEnd = nts.uk.resource.getText('KAF002_100', [dataObject.frameNo]);
            }
            this.startTime = '<div style="display: block; margin: 0px 5px 5px 5px">'
                + '<span style="display: block; text-align: center">' + start + '</span>'
                + '<div align="center">'
                + '<input style="width: 90px; text-align: center" data-name="Time Editor" data-bind="'
                + 'style:{\'background-color\': ' + param + '[' + idGetList + '].flagEnable() ? (' + param + '[' + idGetList + '].startTimeActual ? (' + param + '[' + idGetList + '].flagObservable() ? \'#b1b1b1\' : \'\') : \'#ffc0cb\') : \'\'},'
                + 'ntsTimeEditor: {value: ' + param + '[' + idGetList + '].startTimeRequest, enable: !' + param + '[' + idGetList + '].flagObservable() , constraint: \'TimeWithDayAttr\', inputFormat: \'time\', mode: \'time\', required: false, name: \''+ self.nameStart +'\'}" />'
                + '</div>'
                + '</div>';
            this.endTime = '<div style="display: block; margin: 0px 5px 5px 5px">'
                + '<span style="display: block; text-align: center">' + end + '</span>'
                + '<div align="center">'
                + '<input style="width: 90px; text-align: center" data-name="Time Editor" data-bind="'
                + 'style:{\'background-color\': ' + param + '[' + idGetList + '].flagEnable() ? (' + param + '[' + idGetList + '].endTimeActual ? (' + param + '[' + idGetList + '].flagObservable() ? \'#b1b1b1\' : \'\') : \'#ffc0cb\') : \'\'},'
                + 'ntsTimeEditor: {value: ' + param + '[' + idGetList + '].endTimeRequest, enable: !' + param + '[' + idGetList + '].flagObservable() , constraint: \'TimeWithDayAttr\', inputFormat: \'time\', mode: \'time\', required: false, name: \''+ self.nameEnd +'\'}" />'
                + '</div>'
                + '</div>';

            this.flag = '<div align="center" data-bind="css: !' + param + '[' + idGetList + '].flagEnable ? \'disableFlag\' : \'enableFlag\' , ntsCheckBox: {enable: ' + param + '[' + idGetList + '].flagEnable, checked: ' + param + '[' + idGetList + '].flagObservable}"></div>';
        }


        public changeElement() {
            let self = this;
            let parseTime = nts.uk.time.minutesBased.clock.dayattr;
            let start = _.isNull(self.startTimeActual) ? '--:--' : parseTime.create(self.startTimeActual).shortText;
            let end = _.isNull(self.endTimeActual) ? '--:--' : parseTime.create(self.endTimeActual).shortText;
            let param = 'dataSource[' + String(self.index) + ']';

            let idGetList = self.id - 1;
            this.startTime = '<div class="startTime" style="display: block; margin: 0px 5px 5px 5px">'
                + '<span style="display: block; text-align: center">' + start + '</span>'
                + '<div align="center">'
                + '<input style="width: 90px; text-align: center" data-name="Time Editor" data-bind="'
                + 'style:{\'background-color\': ' + param + '[' + idGetList + '].flagEnable() ? (' + param + '[' + idGetList + '].startTimeActual ? (' + param + '[' + idGetList + '].flagObservable() ? \'#b1b1b1\' : \'\') : \'#ffc0cb\') : \'\'},'
                + 'ntsTimeWithDayEditor: {value: ' + param + '[' + idGetList + '].startTimeRequest, enable: !' + param + '[' + idGetList + '].flagObservable() , constraint: \'TimeWithDayAttr\', inputFormat: \'time\', mode: \'time\', required: false, name: \''+ self.nameStart +'\'}" />'
                + '</div>'
                + '</div>';
            this.endTime = '<div class="endTime" style="display: block; margin: 0px 5px 5px 5px">'
                + '<span style="display: block; text-align: center">' + end + '</span>'
                + '<div align="center">'
                + '<input style="width: 90px; text-align: center" data-name="Time Editor" data-bind="'
                + 'style:{\'background-color\': ' + param + '[' + idGetList + '].flagEnable() ? (' + param + '[' + idGetList + '].endTimeActual ? (' + param + '[' + idGetList + '].flagObservable() ? \'#b1b1b1\' : \'\') : \'#ffc0cb\') : \'\'},'
                + 'ntsTimeWithDayEditor: {value: ' + param + '[' + idGetList + '].endTimeRequest, enable: !' + param + '[' + idGetList + '].flagObservable() , constraint: \'TimeWithDayAttr\', inputFormat: \'time\', mode: \'time\', required: false, name: \''+ self.nameEnd +'\'}" />'
                + '</div>'
                + '</div>';

            this.flag = '<div class="flag" align="center" data-bind="css: !' + param + '[' + idGetList + '].flagEnable ? \'disableFlag\' : \'enableFlag\' , ntsCheckBox: {enable: ' + param + '[' + idGetList + '].flagEnable, checked: ' + param + '[' + idGetList + '].flagObservable}"></div>';
        }
        public changeElementByPreAtr() {
            const self = this;
            let param = 'dataSource[' + String(self.index) + ']';
            let idGetList = self.id - 1;
            self.flagObservable(false);
            this.startTime = '<div class="startTime" style="display: block; margin: 0px 5px 5px 5px">'
                + '<div align="center" style="padding-top: 10px; padding-bottom: 5px">'
                + '<input style="width: 90px; text-align: center" data-name="Time Editor" data-bind="'
                + 'ntsTimeWithDayEditor: {value: ' + param + '[' + idGetList + '].startTimeRequest , constraint: \'TimeWithDayAttr\', inputFormat: \'time\', mode: \'time\', required: false, name: \''+ self.nameStart +'\'}" />'
                + '</div>'
                + '</div>';
            this.endTime = '<div class="endTime" style="display: block; margin: 0px 5px 5px 5px">'
                + '<div align="center" style="padding-top: 10px; padding-bottom: 5px">'
                + '<input style="width: 90px; text-align: center" data-name="Time Editor" data-bind="'
                + 'ntsTimeWithDayEditor: {value: ' + param + '[' + idGetList + '].endTimeRequest , constraint: \'TimeWithDayAttr\', inputFormat: \'time\', mode: \'time\', required: false, name: \''+ self.nameEnd +'\'}" />'
                + '</div>'
                + '</div>';
        }
        public convertTimeZoneStampClassification() {
            const self = this;
            if (self.typeStamp == STAMPTYPE.PARENT) {

                return 0;
            } else if (self.typeStamp == STAMPTYPE.NURSE) {

                return 1;
            } else if (self.typeStamp == STAMPTYPE.BREAK) {

                return 2;
            }
        }
        public convertTimeStampAppEnum() {
            const self = this;
            if (self.typeStamp == STAMPTYPE.ATTENDENCE) {

                return 0;
            } else if (self.typeStamp == STAMPTYPE.EXTRAORDINARY) {

                return 1;
            } else if (self.typeStamp == STAMPTYPE.GOOUT_RETURNING) {

                return 2;
            } else if (self.typeStamp == STAMPTYPE.CHEERING) {

                return 3;
            }
        }

    }
    export class GoOutTypeDispControl {
        display: number;
        goOutType: number;
    }

    export class TimePlaceOutput {

        opWorkLocationCD: string;

        opGoOutReasonAtr: number;

        frameNo: number;

        opEndTime: number;

        opStartTime: number;

        constructor(index: number) {
            this.opWorkLocationCD = null;
            this.opGoOutReasonAtr = null;
            this.frameNo = index;
            this.opStartTime = null;
            this.opEndTime = null;
        }

    }


    export class ItemModel {
        code: string;
        name: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }

    export class CellState {
        rowId: number;
        columnKey: string;
        state: Array<any>;
        constructor(rowId: string, columnKey: string, state: Array<any>) {
            this.rowId = rowId;
            this.columnKey = columnKey;
            this.state = state;
        }
    }
    export enum STAMPTYPE {
        //        出勤／退勤
        ATTENDENCE = 0,
        //        育児
        PARENT = 2,
        //        外出／戻り
        GOOUT_RETURNING = 1,
        //        応援
        CHEERING = 3,
        //        臨時
        EXTRAORDINARY = 4,
        //        休憩
        BREAK = 5,
        //        介護
        NURSE = 6,

    }
    export class TabM {
        title?: string;
        enable: boolean;
        visible: boolean
        constructor(title: string, enable: boolean, visible: boolean) {
            this.title = title;
            this.enable = enable;
            this.visible = visible;
        }
    }

    @handler({
        bindingName: 'fx-grid'
    })
    export class FxGridBindingHandler implements KnockoutBindingHandler {
        init = (element: HTMLElement, valueAccessor: () => any): void | { controlsDescendantBindings: boolean; } => {
            const options = valueAccessor();
            
            $(element).ntsGrid(options);

        }
    }

	class Comment{
        public content: string;
        public isBold: boolean;
        public color: string;
        constructor( content: string, isBold: boolean, color: string) {
            this.content = content;
            this.isBold = isBold;
            this.color = color;
        }
        toHtml() {
            const self = this;
            return '<div style= {}>' + self.content + '</div>'
        }
        
    }
}
