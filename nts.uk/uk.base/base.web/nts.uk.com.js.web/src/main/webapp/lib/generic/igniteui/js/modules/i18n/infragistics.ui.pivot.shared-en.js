/*!@license
* Infragistics.Web.ClientUI Pivot Shared localization resources 16.2.20162.2040
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

    if (!$.ig.PivotShared) {
        $.ig.PivotShared = {};

        $.extend($.ig.PivotShared, {
            locale: {
                invalidDataSource: "The passed data source either null or not supported.",
                measureList: "Measures",
                ok: "OK",
                cancel: "Cancel",
                addToMeasures: "Add to Measures",
                addToFilters: "Add to Filters",
                addToColumns: "Add to Columns",
                addToRows: "Add to Rows"
            }
        });
    }
}));// REMOVE_FROM_COMBINED_FILES