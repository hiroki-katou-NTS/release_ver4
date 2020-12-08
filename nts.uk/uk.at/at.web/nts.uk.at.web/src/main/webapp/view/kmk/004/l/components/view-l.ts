/// <reference path="../../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.view.kmk004.l {
	import IParam = nts.uk.at.view.kmk004.p.IParam;
	
	const template = `
	<div class="sidebar-content-header">
		<div class="title" data-bind="i18n: 'Com_Company'"></div>
		<a class="goback" data-bind="ntsLinkButton: { jump: '/view/kmk/004/a/index.xhtml' },i18n: 'KMK004_224'"></a>
		<button class="proceed" data-bind="i18n: 'KMK004_225'"></button>
		<button data-bind="visible: false, i18n: 'KMK004_226'"></button>
		<button class="danger" data-bind="i18n: 'KMK004_227'"></button>
	</div>
	<div class="view-l">
				<div class="header-l">
					<div id="title", data-bind="i18n: 'KMK004_307'"></div>
					<hr></hr>
					<div class="header_title">
						<div data-bind="ntsFormLabel: {inline: true}, i18n: 'KMK004_229'"></div>
						<button data-bind="i18n: 'KMK004_231', click: openViewP"></button>
					</div>
					<div class="header_content">
						<div data-bind="component: {
							name: 'view-l-basic-setting',
							params: params
						}"></div>
					</div>
					<div data-bind="ntsFormLabel: {inline: true}, i18n: 'KMK004_232'"></div>
				</div>
				<div class="content">
					<button id = "btn_year" data-bind="click: openQDialog, i18n: 'KMK004_233'"></button>
					<div class="div_row"> 
								
								<div class= "box-year" data-bind="component: {
									name: 'box-year',
									params:{ 
										selectedYear: selectedYear,
										change: changeYear
									}
								}"></div>
								
								<div class= "time-work" data-bind="component: {
									name: 'time-work',
									params:{
										selectedYear: selectedYear,
										change: changeYear,
										checkEmployee: checkEmployee
									}
								}"></div>
					</div>
			</div>
	</div>
	`;

	@component({
		name: 'view-l',
		template
    })
    
	export class ViewLComponent extends ko.ViewModel {
		
		public selectedYear: KnockoutObservable<number | null> = ko.observable(null);
		public changeYear: KnockoutObservable<boolean> = ko.observable(true);
		public checkEmployee: KnockoutObservable<boolean> = ko.observable(false);
		public existYear: KnockoutObservable<boolean> = ko.observable(true);
		isLoadData: KnockoutObservable<boolean> = ko.observable(false);
		
		constructor(private params: IParam){
			super();
		}
		
		created() {
			let vm = this;
			vm.params = {isLoadData: vm.isLoadData, sidebarType : "Com_Company", wkpId: ko.observable(''), empCode :ko.observable(''), empId: ko.observable(''), titleName:'', deforLaborTimeComDto: null, settingDto: null}
			vm.selectedYear
			.subscribe(() => {
				if(vm.selectedYear != null) {
					vm.existYear(true);
				}
			});
		
		}

		mounted() {
			
		}
		
		openViewP() {
			let vm = this;
			vm.$window.modal('at', '/view/kmk/004/p/index.xhtml', vm.params).then(() => {
				vm.isLoadData(true);
			});
		}
		
		openQDialog() {
			
		}
    }
}
