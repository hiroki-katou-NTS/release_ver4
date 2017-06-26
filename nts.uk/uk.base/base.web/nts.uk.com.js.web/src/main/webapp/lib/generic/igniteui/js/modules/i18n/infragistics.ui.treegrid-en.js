/*!@license
* Infragistics.Web.ClientUI Tree Grid localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

/*global define, jQuery */
(function (factory) {
	if (typeof define === "function" && define.amd) {
		define( ["jquery"], factory );
	} else {
		factory(jQuery);
	}
}
(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.TreeGrid) {
        $.ig.TreeGrid = {};

        $.extend($.ig.TreeGrid, {
            locale: {
            	fixedVirtualizationNotSupported: "Row Virtualization requires a different virtualizationMode setting. The virtualizationMode should be set to 'continuous'."
            }
        });

		$.ig.TreeGridPaging = $.ig.TreeGridPaging || {};

		$.extend($.ig.TreeGridPaging, {
			locale: {
				contextRowLoadingText: "Loading...",
				contextRowRootText: "Root",
				columnFixingWithContextRowNotSupported: "Column Fixing requires a different contextRowMode setting. contextRowMode should be set to 'none' in order to enable column fixing."
			}
		});

		$.ig.TreeGridFiltering = $.ig.TreeGridFiltering || {};

		$.extend($.ig.TreeGridFiltering, {
			locale: {
				filterSummaryInPagerTemplate: "${currentPageMatches} of ${totalMatches} matching records"
			}
		});

		$.ig.TreeGridRowSelectors = $.ig.TreeGridRowSelectors || {};

		$.extend($.ig.TreeGridRowSelectors, {
			locale: {
				multipleSelectionWithTriStateCheckboxesNotSupported: "Multiple selection requires a different checkBoxMode setting. checkBoxMode should be set to biState in order to enable multiple selection."
			}
		});
    }
}));// REMOVE_FROM_COMBINED_FILES