/// <reference path="../../../../../lib/nittsu/viewcontext.d.ts" />

const targetTemplate = `
	<div class="left-area d-inline">		
		<button data-bind="text: $i18n('KSU001_37')" />
		<div class="organization-name" data-bind="text: 'Organization Name'"></div>
	</div>
	<div class="right-area d-inline">
		<button class="d-inline" data-bind="text: $i18n('KSU001_67')" />
		<div class="d-inline" data-bind="component: { name: 'ksu-use-guide' }, popper: true"></div>
	</div>
`;

@component({
	name: 'ksu-target-organization',
	template: targetTemplate
})
class KSU001ATargetOrganizationComponent extends ko.ViewModel {
	created() {
	}

	mounted() {
		const vm = this;

		$(vm.$el).addClass('target-organization');
	}
}