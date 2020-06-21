/// <reference path="../../../../../lib/nittsu/viewcontext.d.ts" />

interface Params {
	model: KnockoutObservable<string>;
	tabs: KnockoutObservableArray<string>;
}

const template =`<div class="sidebar-navigator">
	<ul class="navigator" data-bind="foreach: params.tabs">
		<li data-bind="click: (tab) => $component.params.model(tab)">
			<a href="javascript: void(0)" data-bind="
					text: $component.$i18n($data),
					css: { 'active': $component.params.model() === $data
				}"></a>
		</li>
	</ul>
</div>`;

@component({
	name: 'ko-panel',
	template
})
class TabPanel extends ko.ViewModel {
	public params!: Params;	

	created(params: Params) {
		this.params = params;
	}
}