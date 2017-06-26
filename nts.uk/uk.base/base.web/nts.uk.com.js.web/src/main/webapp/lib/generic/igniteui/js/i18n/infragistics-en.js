/*!@license
* Infragistics.Web.ClientUI data source localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {

    $.ig = $.ig || {};

    if (!$.ig.DataSourceLocale) {
	    $.ig.DataSourceLocale = {};

	    $.extend($.ig.DataSourceLocale, {

		    locale: {
			    invalidDataSource: "The supplied data source is invalid. It happens to be a scalar.",
			    unknownDataSource: "Cannot determine the data source type. Please specify if it is JSON or XML data.",
			    errorParsingArrays: "There was an error parsing the array data and applying the defined data schema: ",
			    errorParsingJson: "There was an error parsing the JSON data and applying the defined data schema: ",
			    errorParsingXml: "There was an error parsing the XML data and applying the defined data schema: ",
			    errorParsingHtmlTable: "There was an error extracting the data from the HTML Table and applying the schema : ",
			    errorExpectedTbodyParameter: "Expected a tbody or a table as a parameter.",
			    errorTableWithIdNotFound: "The HTML Table with the following ID was not found: ",
			    errorParsingHtmlTableNoSchema: "There was an error parsing the Table DOM: ",
			    errorParsingJsonNoSchema: "There was an error parsing/evaluating the JSON string: ",
			    errorParsingXmlNoSchema: "There was an error parsing the XML string: ",
			    errorXmlSourceWithoutSchema: "The supplied data source is an xml document, but there is no defined data schema ($.IgDataSchema) ",
			    errorUnrecognizedFilterCondition: " The filter condition that was passed was not recognized: ",
			    errorRemoteRequest: "The remote request to fetch data has failed: ",
			    errorSchemaMismatch: "The input data doesn't match the schema, the following field couldn't be mapped: ",
			    errorSchemaFieldCountMismatch: "The input data doesn't match the schema in terms of number of fields. ",
			    errorUnrecognizedResponseType: "The response type was either not set correctly, or it was not possible to detect it automatically. Please set settings.responseDataType and/or settings.responseContentType.",
			    hierarchicalTablesNotSupported: "Tables are not supported for HierarchicalSchema",
			    cannotBuildTemplate: "The jQuery template could not be built. There are no records present in the data source, and no columns defined.",
			    unrecognizedCondition: "Unrecognized filtering condition in the following expression: ",
			    fieldMismatch: "The following expression contains an invalid field or filtering condition: ",
			    noSortingFields: "There are no fields specified. You need to specify at least one field to sort by, when calling sort().",
			    filteringNoSchema: "There is no schema / fields specified. You need to specify a schema with field definitions and types to be able to filter the data source.",
			    noSaveChanges: "Saving changes was not successful. Server did not return Success object or returned Success:false.",
			    errorUnexpectedCustomFilterFunction: "An unexpected value was provided for a custom filtering function. A function or string is expected."
		    }
	    });

    }
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI common DV widget localization resources 16.2.20162.2040
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

    if (!$.ig.Chart) {
	    $.ig.Chart = {};

	    $.extend($.ig.Chart, {

		    locale: {
			    seriesName: "must specify series name option when setting options.",
			    axisName: "must specify axis name option when setting options.",
			    invalidLabelBinding: "There is no such value for the labels to bind.",
			    invalidSeriesAxisCombination: "Invalid combination of series and axis types: ",
			    close: "Close",
			    overview: "Overview",
			    zoomOut: "Zoom Out",
			    zoomIn: "Zoom In",
			    resetZoom: "Reset Zoom",
			    seriesUnsupportedOption: "the current series type does not support the option: ",
			    seriesTypeNotLoaded: "the JavaScript file containing the requested series type has not been loaded or the series type is invalid: ",
			    axisTypeNotLoaded: "the JavaScript file containing the requested axis type has not been loaded or the axis type is invalid: ",
			    axisUnsupportedOption: "the current axis type does not support the option: "
		    }
	    });

    }
}));// REMOVE_FROM_COMBINED_FILES

/*!@license
* Infragistics.Web.ClientUI shared localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
$.ig = $.ig || {};

if (!$.ig.SharedLocale) {
	$.ig.SharedLocale = {};

	$.extend($.ig.SharedLocale, {

		locale: {
			
		}
	});

}
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI templating localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.Templating) {
	    $.ig.Templating = {};

	    $.extend($.ig.Templating, {
		    locale: {
			    undefinedArgument: 'An error has occurred while trying to retrieve data source property: '
		    }
	    });
    }
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Barcode localization resources 16.2.20162.2040
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

    if (!$.ig.Barcode) {
	    $.ig.Barcode = {
		    locale: {
			    aILength: "The AI should have at least 2 digits.",
			    badFormedUCCValue: "The Data property value of the UCC barcode is not well formed. It should look like (AI)GTIN.",
			    code39_NonNumericError: "The character '{0}' is invalid for CODE39 Data property value. The valid ones are: {1}",
			    countryError: "Error converting Country property value code. It should be a numeric value.",
			    emptyValueMsg: "The Data property value is empty.",
			    encodingError: "Error in the convertion. Refer to the documentation for the valid property values.",
			    errorMessageText: "Invalid value! Reference the documentation for the valid barcode Data property value structure.",
			    gS1ExMaxAlphanumNumber: "The GS1 DataBar Expanded family can encode up to 41 alphanumeric characters.",
			    gS1ExMaxNumericNumber: "The GS1 DataBar Expanded family can encode up to 74 numeric characters.",
			    gS1Length: "The GS1 DataBar Data property value is used for GTIN - 8, 12, 13, 14 and its length should be 7, 11, 12 or 13. The last digit is reserved for a check sum.",
			    gS1LimitedFirstChar: "GS1 DataBar Limited barcode should have 0 or 1 in the first digit. When encoding GTIN-14 data with an Indicator value greater than 1, Omnidirectional, Stacked, Stacked Omnidirectional or Truncated barcode type must be used.",
			    i25Length: "The Interleaved2of5 barcode should have even number of digits. You can put 0 in the front of it if they are odd number.",
			    intelligentMailLength: "The length of the Intelligent Mail barcode Data property value should be 20, 25, 29 or 31 characters - 20 digits track code (2 for barcode identifier, 3 for service type identifier, 6 or 9 for mailer identifier and 9 or 6 for serial number) and 0, 5, 9 or 11 zip code symbols.",
			    intelligentMailSecondDigit: "The second digit should be in the range of 0-4.",
			    invalidAI: "Invalid Application Identifier element strings. Please, ensure that the AI string in the Data property value is well formed.",
			    invalidCharacter: "The character '{0}' is invalid for the current barcode type. The valid ones are: {1}",
			    invalidDimension: "The barcode dimension cannot be determined because of an incorrect combination of Stretch, BarsFillMode and XDimension property values.",
			    invalidHeight: "This number of barcode grid rows ({0}) cannot fit in such a height ({1} pixel(s)).",
			    invalidLength: "The barcode Data property value should have {0} digit(s).",
			    invalidPostalCode: "Invalid PostalCode value - Mode 2 encodes up to 9 digits postal code (U.S. zip code) while Mode 3 encodes up to 6 characters alpha-numeric code.",
			    invalidPropertyValue: "The {0} property value should be in the range of {1}-{2}.",
			    invalidVersion: "The SizeVersion propery value number does not generate enough cells to encode the data with the current encoding mode and error correction level.",
			    invalidWidth: "This number of barcode grid columns ({0}) cannot fit in such a width ({1} pixel(s)). Check the XDimension or/and the WidthToHeightRatio property values.",
			    invalidXDimensionValue: "The XDimension property value should be in the range of {0} to {1} for the current barcode type.",
			    maxLength: "The length {0} of the text exceeds the maximum encodable for the current type of barcode. It could encode max {1} characters.",
			    notSupportedEncoding: "The encoding corresponding under the {0} {1} is not supported.",
			    pDF417InvalidRowsColumnsCombination: "The codewords (data & error correction) are more than can be encoded in symbol with a matrix {0}x{1}.",
			    primaryMessageError: "Cannot extract the primary message from the Data property value. Refer to the documentation for its structure.",
			    serviceClassError: "Error converting service class. It should be a numeric value.",
			    smallSize: "Cannot fit the grid in Size({0}, {1}) with the defined Stretch settings.",
			    unencodableCharacter: "The character '{0}' cannot be encoded.",
			    uPCEFirstDigit: "The first UPCE digit shall always be zero by specification.",
			    warningString: "Barcode warning: ",
			    wrongCompactionMode: "The Data property value cannot be compacted with {0} mode.",
                notLoadedEncoding: "The {0} encoding is not loaded."
		    }
	    };
    }
}));// REMOVE_FROM_COMBINED_FILES
/*!@license
* Infragistics.Web.ClientUI Combo localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.Combo) {
	    $.ig.Combo = {
		    locale: {
		        noMatchFoundText: 'No matches found',
			    dropDownButtonTitle: 'Show drop-down',
			    clearButtonTitle: 'Clear value',
			    placeHolder: 'select...',
			    notSuported: 'Operation is not supported.',
			    errorNoSupportedTextsType: "A different filtering text is required. Provide a value that is either a string or an array of strings.",
			    errorUnrecognizedHighlightMatchesMode: "A different highlight matches mode is required. Choose a value between 'multi', 'contains', 'startsWith', 'full' and 'null'.",
		    	errorIncorrectGroupingKey: "Grouping key is not correct."
		    }
	    };
    }
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Dialog localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.Dialog) {
	    $.ig.Dialog = {
		    locale: {
			    closeButtonTitle: "Close",
			    minimizeButtonTitle: "Minimize",
			    maximizeButtonTitle: "Maximize",
			    pinButtonTitle: "Pin",
			    unpinButtonTitle: "Unpin",
			    restoreButtonTitle: "Restore",
				setOptionError: 'Runtime changes are not allowed for the following option: '
		    }
	    };
    }
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Doughnut Chart localization resources 16.2.20162.2040
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

    if (!$.ig.igDoughnutChart) {
        $.ig.igDoughnutChart = {};

        $.extend($.ig.igDoughnutChart, {
            locale: {
                invalidBaseElement: " is not supported as base element. Use DIV instead."
            }
        });
    }
}));// REMOVE_FROM_COMBINED_FILES
/*!@license
* Infragistics.Web.ClientUI Editors localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
	$.ig = $.ig || {};

	if (!$.ig.Editor) {
		$.ig.Editor = {
			locale: {
				spinUpperTitle: 'Increment',
				spinLowerTitle: 'Decrement',
				buttonTitle: 'Show list',
				clearTitle: 'Clear value',
				ariaTextEditorFieldLabel: 'Text Editor',
				ariaNumericEditorFieldLabel: 'Numeric Editor',
				ariaCurrencyEditorFieldLabel: 'Currency Editor',
				ariaPercentEditorFieldLabel: 'Percent Editor',
				ariaMaskEditorFieldLabel: 'Mask Editor',
				ariaDateEditorFieldLabel: 'Date Editor',
				ariaDatePickerFieldLabel: 'Date Picker',
				ariaSpinUpButton: 'Spin up',
				ariaSpinDownButton: 'Spin down',
				ariaDropDownButton: 'Drop down',
				ariaClearButton: 'Clear',
				ariaCalendarButton: 'Calendar',
				datePickerButtonTitle: 'Show calendar',
				updateModeUnsupportedValue: 'updateMode requires a different configuration. Choose a value between "onChange" and "immediate".',
				updateModeNotSupported: 'updateMode property supports only "onchange" mode for igMaskEditor, igDateEditor and igDatePicker extensions',
				renderErrMsg: "A base editor cannot be instantiated directly. Try with a text, numeric, date, or other editor.",
				multilineErrMsg: 'textArea requires a different configuration. The textMode should be set to "multiline".',
				targetNotSupported: "This target element is not supported.",
				placeHolderNotSupported: "The placeholder attribute is not supported by your browser.",
				allowedValuesMsg: "Pick a value from the drop-down list",
				maxLengthErrMsg: "Entry is too long and was trimmed to {0} symbols",
				maxLengthWarningMsg: "Entry reached the maximum length of {0} for this field",
				minLengthErrMsg: "At least {0} characters should be entered",
				maxValErrMsg: "Entry reached the maximum value of {0} for this field",
				minValErrMsg: "Entry reached the minimum value of {0} for this field",
				maxValExceedRevertErrMsg: "Entry exceeded the maximum value of {0} and was reverted to previous one",
				minValExceedRevertErrMsg: "Entry was less than the minimum value of {0} and was reverted to previous one",
				maxValExceedSetErrMsg: "Entry exceeded the maximum value of {0} and was set to the maximum value",
				minValExceedSetErrMsg: "Entry exceeded the minimum value of {0} and was set to the minimum value",
				maxValExceededWrappedAroundErrMsg: "Entry exceeded the maximum value of {0} and was set to the minimum allowed one",
				minValExceededWrappedAroundErrMsg: "Entry was less than the minimum value of {0} and was set to the maximum allowed one",
				btnValueNotSupported: 'A different button value is required. Choose a value between "dropdown", "clear" and "spin".',
				scientificFormatErrMsg: 'A different scientificFormat is required. Choose a value between "E", "e", "E+" and "e+".',
				spinDeltaIsOfTypeNumber: "A different type of spinDelta is required. A positive number should be entered.",
				spinDeltaCouldntBeNegative: "The spinDelta option cannot be negative. A positive number should be entered.",
				spinDeltaContainsExceedsMaxDecimals: "Maximum allowed fractions for spinDelta are set to {0}. Either change MaxDecimals or try to shrink your value.",
				spinDeltaIncorrectFloatingPoint: 'A floating point spinDelta requires a different configuration. Set the dataMode of the editor to either "double" or "float" or set spinDelta to integer.',
				numericEditorNoSuchMethod: "The numeric editor does not support this method.",
				numericEditorNoSuchOption: "The numeric editor does not support this option.",
				displayFactorIsOfTypeNumber: "displayFactor requires a different value. Its value should be set to 1 or 100 as a number.",
				displayFactorAllowedValue: "displayFactor requires a different value. Its value should be set to 1 or 100 as a number.",
				instantiateCheckBoxErrMsg: "igCheckboxEditor requires a different element. Use INPUT, SPAN or DIV element.",
				cannotParseNonBoolValue: "igCheckboxEditor requires a different value. A boolean value should be provided.",
				cannotSetNonBoolValue: "igCheckboxEditor requires a different value. A boolean value should be provided.",
				maskEditorNoSuchMethod: "The mask editor does not support this method.",
				datePickerEditorNoSuchMethod: "The date editor does not support this method.",
				datePickerNoSuchMethodDropDownContainer: "The date editor does not support this method. Use 'getCalendar' one instead.",
				buttonTypeIsDropDownOnly: "Datepicker allows only dropdown and clear values for the buttonType option.",
				dateEditorMinValue: "MinValue option can not be set runtime.",
				dateEditorMaxValue: "MaxValue option can not be set runtime.",
				setOptionError: 'Runtime changes are not allowed for the following option: ',
				invalidDate: "Invalid date",
				maskMessage: 'All required positions should be filled',
				maskRevertMessage: 'All required positions should be filled, that is why the value was reverted to the last valid one.',
				dateMessage: 'A valid date should be entered',
				centuryThresholdValidValues: "centuryThreshold property shoud be between 0 and 99. The value has been reverted to defaults.",
				noListItemsNoButton: "No spin or dropdown button is rendered because there are no listitems."
			}
		};
	}
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Grid localization resources 16.2.20162.2040
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

if (!$.ig.Grid) {
	$.ig.Grid = {};

	$.extend($.ig.Grid, {

		locale: {
			noSuchWidget: "{featureName} was not recognized. Verify that such a feature exists and the spelling is correct.",
			autoGenerateColumnsNoRecords: "autoGenerateColumns is enabled, but there are no records in the data source. Load a data source with records to be able to determine the columns.",
			optionChangeNotSupported: "{optionName} cannot be edited after initialization. Its value should be set during initialization.",
			optionChangeNotScrollingGrid: "{optionName} cannot be edited after initialization because your grid initially does not scroll and full re-rendering will be required. This option should be set during initialization.",
			widthChangeFromPixelsToPercentagesNotSupported: "Cannot change dynamically option width of the grid from pixels to percentages.",
			widthChangeFromPercentagesToPixelsNotSupported: "Cannot change dynamically option width of the grid from percentages to pixels.",
			noPrimaryKeyDefined: "There is no primary key defined for the grid. Define a primary key in order to use features such as Grid Editing.",
			indexOutOfRange: "The specified row index is out of range. A row index between 0 and {max} should be provided.",
			noSuchColumnDefined: "The specified column key is not valid. A column key that matches the key of one of the defined grid columns should be provided.",
			columnIndexOutOfRange: "The specified column index is out of range. A column index between 0 and {max} should be provided.",
			recordNotFound: "A record with id {id} could not be found in the data view. Verify the id used for the search and adjust it if necessary.",
			columnNotFound: "A column with key {key} could not be found. Verify the key used for the search and adjust it if necessary.",
			colPrefix: "Column ",
			columnVirtualizationRequiresWidth: "Virtualization and columnVirtualization require the width of the grid or its columns to be set. Provide a value for the grid width, defaultColumnWidth or the width of each column.",
			virtualizationRequiresHeight: "Virtualization requires the height of the grid to be set. A value for the grid height should be provided.",
			colVirtualizationDenied: "columnVirtualization requires a different virtualizationMode setting. The virtualizationMode should be set to 'fixed'.",
			noColumnsButAutoGenerateTrue: "autoGenerateColumns is disabled and no columns are defined for the grid. Either enable autoGenerateColumns or manually specify the columns.",
			noPrimaryKey: "igHierarchicalGrid requires a primary key to be defined. A primary key should be provided.",
			//templatingEnabledButNoTemplate: "Enabling the jQueryTemplating works only when a template is defined. The template should be set through the rowTemplate option.",
			expandTooltip: "Expand Row",
			collapseTooltip: "Collapse Row",
			featureChooserTooltip: "Feature chooser",
			movingNotAllowedOrIncompatible: "The specified column could not be moved. Verify that such a column exists and its end position would not break the column layout.",
			allColumnsHiddenOnInitialization: "All columns cannot be hidden during initialization. At least one column should be configured as visible.",
			virtualizationNotSupportedWithAutoSizeCols: "Virtualization requires a different column width configuration than '*'. The column width should be set as a number in pixels.",
			columnVirtualizationNotSupportedWithPercentageWidth: "Column virtualization requires a different grid width configuration. The column width should be set as a number in pixels.",
			mixedWidthsNotSupported: "All columns are required to have their width set the same way. Set all column widths either as percentages or as number in pixels.",
			multiRowLayoutColumnError: "The column with key: {key1} could not be added to the multi-row-layout because its place in the layout has already been taken by the column with key: {key2} .",
			multiRowLayoutNotComplete: "The multi-row-layout is not complete. The column definition creates a layout that has empty spaces and cannot be rendered correctly.",
			multiRowLayoutMixedWidths: "Mixed widths (percentage and pixels) are not supported in the Multi-Row Layout. Please define all column widths in either pixels or percentage. ",
			scrollableGridAreaNotVisible: "Fixed header and footer areas are larger than the available grid height. The scrollable area is not visible.Please set a larger grid height."
		}
	});

	$.ig.GridFiltering = $.ig.GridFiltering || {};

	$.extend($.ig.GridFiltering, {
		locale: {
			startsWithNullText: "Starts with...",
			endsWithNullText: "Ends with...",
			containsNullText: "Contains...",
			doesNotContainNullText: "Does not contain...",
			equalsNullText: "Equals...",
			doesNotEqualNullText: "Does not equal...",
			greaterThanNullText: "Greater than...",
			lessThanNullText: "Less than...",
			greaterThanOrEqualToNullText: "Greater than or equal to...",
			lessThanOrEqualToNullText: "Less than or equal to...",
			onNullText: "On...",
			notOnNullText: "Not on...",
			afterNullText: "After",
			beforeNullText: "Before",
			emptyNullText: "Empty",
			notEmptyNullText: "Not empty",
			nullNullText: "Null",
			notNullNullText: "Not null",
			startsWithLabel: "Starts with",
			endsWithLabel: "Ends with",
			containsLabel: "Contains",
			doesNotContainLabel: "Does not contain",
			equalsLabel: "Equals",
			doesNotEqualLabel: "Does not equal",
			greaterThanLabel: "Greater than",
			lessThanLabel: "Less than",
			greaterThanOrEqualToLabel: "Greater than or equal to",
			lessThanOrEqualToLabel: "Less than or equal to",
			trueLabel: "True",
			falseLabel: "False",
			afterLabel: "After",
			beforeLabel: "Before",
			todayLabel: "Today",
			yesterdayLabel: "Yesterday",
			thisMonthLabel: "This month",
			lastMonthLabel: "Last month",
			nextMonthLabel: "Next month",
			thisYearLabel: "This year",
			lastYearLabel: "Last year",
			nextYearLabel: "Next year",
			clearLabel: "Clear Filter",
			noFilterLabel: "No",
			onLabel: "On",
			notOnLabel: "Not on",
			advancedButtonLabel: "Advanced",
			filterDialogCaptionLabel: "ADVANCED FILTER",
			filterDialogConditionLabel1: "Show records matching ",
			filterDialogConditionLabel2: " of the following criteria",
			filterDialogConditionDropDownLabel: "Filtering condition",
			filterDialogOkLabel: "Search",
			filterDialogCancelLabel: "Cancel",
			filterDialogAnyLabel: "ANY",
			filterDialogAllLabel: "ALL",
			filterDialogAddLabel: "Add",
			filterDialogErrorLabel: "You reached the maximum number of filters supported.",
			filterDialogCloseLabel: "Close filtering dialog",
			filterSummaryTitleLabel: "Search results",
			filterSummaryTemplate: "${matches} matching records",
			filterDialogClearAllLabel: "Clear ALL",
			tooltipTemplate: "${condition} filter applied",
			// M.H. 13 Oct. 2011 Fix for bug #91007
			featureChooserText: "Hide Filter",
			featureChooserTextHide: "Show Filter",
			// M.H. 17 Oct. 2011 Fix for bug #91007
			featureChooserTextAdvancedFilter: "Advanced Filter",
			virtualizationSimpleFilteringNotAllowed: "Column virtualization requires a different type of filtering. Set filtering mode to 'advanced' or disable advancedModeEditorsVisible",
			multiRowLayoutSimpleFilteringNotAllowed: "Multi-row-layout requires a different type of filtering. Set filtering mode to 'advanced'",
			featureChooserNotReferenced: "A reference to Feature Chooser is missing. Include infragistics.ui.grid.featurechooser.js in your project, use a loader or one of the combined script files.",
			conditionListLengthCannotBeZero: "The conditionList array in columnSettings is empty. A suitable array for the conditionList should be provided.",
			conditionNotValidForColumnType: "The condition '{0}' is not valid for the current configuration. It should be replaced with a condition suitable for {1} column type.",
			defaultConditionContainsInvalidCondition: "defaultExpression for the '{0}' column contains a condition that is not allowed. It should be replaced it with a condition suitable for {0} column type."
		}
	});

	$.ig.GridGroupBy = $.ig.GridGroupBy || {};

	$.extend($.ig.GridGroupBy, {
		locale: {
			emptyGroupByAreaContent: "Drag a column here or {0} to Group By",
			emptyGroupByAreaContentSelectColumns: "select columns",
			emptyGroupByAreaContentSelectColumnsCaption: "select columns",
			expandTooltip: "Expand Grouped Row",
			collapseTooltip: "Collapse Grouped Row",
			removeButtonTooltip: "Remove Grouped Column",
			modalDialogCaptionButtonDesc: "Sort ascending",
			modalDialogCaptionButtonAsc: "Sort descending",
			modalDialogCaptionButtonUngroup: "Ungroup",
			modalDialogGroupByButtonText: "Group By",
			modalDialogCaptionText: 'Add to Group By',
			modalDialogDropDownLabel: 'Showing:',
			modalDialogClearAllButtonLabel: 'Clear ALL',
			modalDialogRootLevelHierarchicalGrid: 'Root',
			modalDialogDropDownButtonCaption: "Show/Hide",
			modalDialogButtonApplyText: 'Apply',
			modalDialogButtonCancelText: 'Cancel',
			fixedVirualizationNotSupported: "Group By requires another virtualization setting. The virtualizationMode should be set to 'continuous'.",
			summaryRowTitle: 'Grouping summary row'
		}
	});

	$.ig.GridHiding = $.ig.GridHiding || {};

	$.extend($.ig.GridHiding, {
		locale: {
			columnChooserDisplayText: "Column Chooser",
			hiddenColumnIndicatorTooltipText: "Hidden Column(s)",
			columnHideText: "Hide",
			columnChooserCaptionLabel: "Column Chooser",
			//columnChooserCheckboxesHeader: "View",
			//columnChooserColumnsHeader: "Column",
			columnChooserCloseButtonTooltip: "Close",
			hideColumnIconTooltip: "Hide",
			featureChooserNotReferenced: "A reference to Feature Chooser is missing. Include infragistics.ui.grid.featurechooser.js in your project or use one of the combined script files.",
			columnChooserShowText: "Show",
			columnChooserHideText: "Hide",
			columnChooserResetButtonLabel: "Reset",
			columnChooserButtonApplyText: 'Apply',
			columnChooserButtonCancelText: 'Cancel'
		}
	});

	$.ig.GridResizing = $.ig.GridResizing || {};

	$.extend($.ig.GridResizing, {
		locale: {
			noSuchVisibleColumn: "There is no visible column for the specified key. The showColumn() method should be used on the column before trying to resize it.",
			resizingAndFixedVirtualizationNotSupported: "Resizing columns requires a different virtualization setting. Use rowVirtualization and set the virtualizationMode to 'continuous'."
		}
	});

	$.ig.GridPaging = $.ig.GridPaging || {};

	$.extend($.ig.GridPaging, {

		locale: {
			pageSizeDropDownLabel: "Show ",
			pageSizeDropDownTrailingLabel: "records",
			//pageSizeDropDownTemplate: "Show ${dropdown} records",
			nextPageLabelText: "Next",
			prevPageLabelText: "Prev",
			firstPageLabelText: "",
			lastPageLabelText: "",
			currentPageDropDownLeadingLabel: "Pg",
			currentPageDropDownTrailingLabel: "of ${count}",
			//currentPageDropDownTemplate: "Pg ${dropdown} of ${count}",
			currentPageDropDownTooltip: "Choose page index",
			pageSizeDropDownTooltip: "Choose number of records per page",
			pagerRecordsLabelTooltip: "Current records range",
			prevPageTooltip: "Previous page",
			nextPageTooltip: "Next page",
			firstPageTooltip: "First page",
			lastPageTooltip: "Last page",
			pageTooltipFormat: "Page ${index}",
			pagerRecordsLabelTemplate: "${startRecord} - ${endRecord} of ${recordCount} records",
			invalidPageIndex: "The specified page index is not valid. Provide a page index that is greater than or equal to 0 and less than the total number of pages."
		}
	});

	$.ig.GridSelection = $.ig.GridSelection || {};

	$.extend($.ig.GridSelection, {
		locale: {
			persistenceImpossible: "Persisting selection requires a different configuration. The primary key option of the grid should be configured."
		}
	});

	$.ig.GridRowSelectors = $.ig.GridRowSelectors || {};

	$.extend($.ig.GridRowSelectors, {
		locale: {
			selectionNotLoaded: "igGridSelection has not been initialized. Selection should be enabled for the grid.",
			columnVirtualizationEnabled: "Row Selectors require a different virtualization setting. Use rowVirtualization or set the virtualizationMode to 'continuous'.",
			selectedRecordsText: "You have selected ${checked} records.",
			deselectedRecordsText: "You have deselected ${unchecked} records.",
			selectAllText: "Select all ${totalRecordsCount} records",
			deselectAllText: "Deselect all ${totalRecordsCount} records",
			requireSelectionWithCheckboxes: "Selection is required when there are checkboxes enabled"
		}
	});

	$.ig.GridSorting = $.ig.GridSorting || {};

	$.extend($.ig.GridSorting, {
		locale: {
			sortedColumnTooltipFormat: 'Sorted ${direction}',
			unsortedColumnTooltip: 'Sort column',
			ascending: 'ascending',
			descending: 'descending',
			modalDialogSortByButtonText: 'Sort by',
			modalDialogResetButton: "Reset",
			modalDialogCaptionButtonDesc: "Click to sort descending",
			modalDialogCaptionButtonAsc: "Click to sort ascending",
			modalDialogCaptionButtonUnsort: "Click to remove sorting",
			featureChooserText: "Sort on Multiple",
			modalDialogCaptionText: "Sort on Multiple",
			modalDialogButtonApplyText: 'Apply',
			modalDialogButtonCancelText: 'Cancel',
			sortingHiddenColumnNotSupport: 'The specified column could not be sorted because it is hidden. Use the showColumn() method on it before trying to sort it.',
			featureChooserSortAsc: 'Sort ascending',
			featureChooserSortDesc: 'Sort descending'
			//modalDialogButtonSlideCaption: "Click to show/hide sorted columns"
		}
	});

	$.ig.GridSummaries = $.ig.GridSummaries || {};

	$.extend($.ig.GridSummaries, {
		locale: {
			featureChooserText: "Hide Summaries",
			featureChooserTextHide: "Show Summaries",
			dialogButtonOKText: 'OK',
			dialogButtonCancelText: 'Cancel',
			emptyCellText: '',
			summariesHeaderButtonTooltip: 'Show/Hide summaries',
			// M.H. 13 Oct. 2011 Fix for bug 91008
			defaultSummaryRowDisplayLabelCount: 'Count',
			defaultSummaryRowDisplayLabelMin: 'Min',
			defaultSummaryRowDisplayLabelMax: 'Max',
			defaultSummaryRowDisplayLabelSum: 'Sum',
			defaultSummaryRowDisplayLabelAvg: 'Avg',
			defaultSummaryRowDisplayLabelCustom: 'Custom',
			calculateSummaryColumnKeyNotSpecified: "Column key is missing. A column key should be specified to calculate summaries.",
			featureChooserNotReferenced: "A reference to Feature Chooser is missing. Include infragistics.ui.grid.featurechooser.js in your project or use one of the combined script files."
		}
	});

	$.ig.GridUpdating = $.ig.GridUpdating || {};

	$.extend($.ig.GridUpdating, {
		locale: {
			doneLabel: 'Done',
			doneTooltip: 'Stop editing and update',
			cancelLabel: 'Cancel',
			cancelTooltip: 'Stop editing without updating',
			addRowLabel: 'Add new row',
			addRowTooltip: 'Start adding a new row',
			deleteRowLabel: 'Delete row',
			deleteRowTooltip: 'Delete row',
			igTextEditorException: 'It is currently not possible to update string columns in the grid. ui.igTextEditor should be loaded first.',
			igNumericEditorException: 'It is currently not possible to update numeric columns in the grid. ui.igNumericEditor should be loaded first.',
			igCheckboxEditorException: 'It is currently not possible to update checkbox columns in the grid. ui.igCheckboxEditor should be loaded first.',
			igCurrencyEditorException: 'It is currently not possible to update numeric columns with currency format in the grid. ui.igCurrencyEditor should be loaded first.',
			igPercentEditorException: 'It is currently not possible to update numeric columns with percent format in the grid. ui.igPercentEditor should be loaded first.',
			igDateEditorException: 'It is currently not possible to update date columns in the grid. ui.igDateEditor should be loaded first.',
			igDatePickerException: 'It is currently not possible to update date columns in the grid. ui.igDatePicker should be loaded first.',
			igComboException: 'It is currently not possible to use a combo in the grid. ui.igCombo should be loaded first.',
			igRatingException: 'It is currently not possible to use igRating as an editor in the grid. ui.igRating should be loaded first.',
			igValidatorException: 'It is currently not possible to support validation with the options defined in igGridUpdating. ui.igValidator should be loaded first.',
			editorTypeCannotBeDetermined: 'Updating did not have enough information to properly determine the type of editor to use for column: ',
			noPrimaryKeyException: 'In order to support update operations after a row was deleted, application should define primaryKey in options of igGrid.',
			hiddenColumnValidationException: 'Cannot edit row which has a hidden column with enabled validation.',
			dataDirtyException: 'Grid has pending transactions which may affect rendering of data. To prevent exception, application may enable autoCommit option of igGrid, or it should process dataDirty event of igGridUpdating and return false. While processing that event, application also may do commit() data in igGrid.',
			recordOrPropertyNotFoundException: 'The specified record or property was not found. Verify the criteria for your search and adjust them if necessary.',
			rowEditDialogCaptionLabel: 'Edit row data',
			excelNavigationNotSupportedWithCurrentEditMode: "Excel Navigation requires a different configuration. editMode should be set to 'cell' or 'row'",
			columnNotFound: "The specified column key was not found in the visible columns' collection or the specified index was out of range.",
			rowOrColumnSpecifiedOutOfView: "Editing the specified row or column is currently not possible. It should be in view on the current page and virtualization frame.",
			editingInProgress: "A row or cell is currently being edited. Another updating procedure cannot start before the current editing is finished.",
			undefinedCellValue: 'Undefined cannot be set as a cell value.',
			addChildTooltip: 'Add a child row',
			multiRowGridNotSupportedWithCurrentEditMode: "When the grid has multi-row-layout enabled only dialog edit mode is supported."
		}
	});

	$.ig.ColumnMoving = $.ig.ColumnMoving || {};
	
	$.extend($.ig.ColumnMoving, {
		locale: {
			movingDialogButtonApplyText: 'Apply',
			movingDialogButtonCancelText: 'Cancel',
			movingDialogCaptionButtonDesc: 'Move down',
			movingDialogCaptionButtonAsc: 'Move up',
			movingDialogCaptionText: 'Move Columns',
			movingDialogDisplayText: 'Move Columns',
			movingDialogDropTooltipText: "Move here",
			movingDialogCloseButtonTitle: 'Close moving dialog',
			dropDownMoveLeftText: 'Move left',
			dropDownMoveRightText: 'Move right',
			dropDownMoveFirstText: 'Move first',
			dropDownMoveLastText: 'Move last',
			featureChooserNotReferenced: 'A reference to Feature Chooser is missing. Include infragistics.ui.grid.featurechooser.js in your project or use one of the combined script files.',
			movingToolTipMove: 'Move',
			featureChooserSubmenuText: 'Move To'
		}
	});

	$.ig.ColumnFixing = $.ig.ColumnFixing || {};
	
	$.extend($.ig.ColumnFixing, {
		locale: {
			headerFixButtonText: 'Fix this column',
			headerUnfixButtonText: 'Unfix this column',
			featureChooserTextFixedColumn: 'Fix column',
			featureChooserTextUnfixedColumn: 'Unfix column',
			groupByNotSupported: 'Column Fixing requires a different configuration. The Group By functionality should be disabled.',
			virtualizationNotSupported: 'Column Fixing requires a different virtualization setting. rowVirtualization should be used instead.',
			columnVirtualizationNotSupported: 'Column Fixing requires a different virtualization setting. columnVirtualization should be disabled.',
			columnMovingNotSupported: 'Column Fixing requires a different configuration. Column Moving should be disabled.',
			hidingNotSupported: 'Column Fixing requires a different configuration. The Hiding functionality should be disabled.',
			hierarchicalGridNotSupported: 'igHierarchicalGrid does not support Column Fixing. Column Fixing should be disabled.',
			responsiveNotSupported: 'Column Fixing requires a different configuration. The Responsive functionality should be disabled.',
			noGridWidthNotSupported: 'Column Fixing requires a different configuration. The grid width should be set either as percentages or as number in pixels.',
			gridHeightInPercentageNotSupported: 'Column Fixing requires a different configuration. The grid height should be set in pixels.',
			defaultColumnWidthInPercentageNotSupported: "Column Fixing requires a different configuration. The default column width should be set as a number in pixels.",
			columnsWidthShouldBeSetInPixels: 'Column Fixing requires a different column width setting. The width of column with key {key} should be set in pixels.',
			unboundColumnsNotSupported: 'Column Fixing requires a different configuration. Unbound Columns should be disabled.',
			excelNavigationNotSupportedWithCurrentEditMode: "Excel Navigation requires a different configuration. editMode should be set to 'cell' or 'row'.",
			initialFixingNotApplied: 'Initial fixing could not be applied for column with key: {0}. Reason: {1}', // {0} is placeholder for columnKey, {1} error message
			setOptionGridWidthException: 'Incorrect value for option grid width. When there are fixed columns width of the visible area of unfixed column(s) should be greater that or equal to value of minimalVisibleAreaWidth.',
			internalErrors: {
				none: 'Your grid configuration is successful!',
				notValidIdentifier: 'The specified column key is not valid. Provide a column key that matches the key of one of the defined grid columns.',
				fixingRefused: 'Fixing this column is not currently supported. Unfix another visible column or use the showColumn() method on any hidden unfixed column first.',
				fixingRefusedMinVisibleAreaWidth: 'This column cannot be fixed. Its width exceeds the available space for fixing a column in the grid.',
				alreadyHidden: 'Fixing/Unfixing this column is currently not possible. The showColumn() method should be used on the column first.',
				alreadyUnfixed: 'This column is already unfixed.',
				alreadyFixed: 'This column is already fixed.',
				unfixingRefused: 'Unfixing this column is currently not possible. The showColumn() method should be used on any hidden fixed column first.',
				targetNotFound: 'Target column with key {key} could not be found. Verify the key used for the search and adjust it if necessary.'
			}
		}
	});

	$.ig.GridAppendRowsOnDemand = $.ig.GridAppendRowsOnDemand || {};

	$.extend($.ig.GridAppendRowsOnDemand, {
		locale: {
			loadMoreDataButtonText: 'Load more data',
			appendRowsOnDemandRequiresHeight: 'Append Rows On Demand requires a different configuration. The grid height should be set.',
			groupByNotSupported: 'Append Rows On Demand requires a different configuration. Group By should be disabled.',
			pagingNotSupported: 'Append Rows On Demand requires a different configuration. Paging should be disabled.',
			cellMergingNotSupported: 'Append Rows On Demand requires a different configuration. Cell Merging should be disabled.',
			virtualizationNotSupported: 'Append Rows On Demand requires a different configuration. Virtualization should be disabled.'
		}
	});

	$.ig.igGridResponsive = $.ig.igGridResponsive || {};

	$.extend($.ig.igGridResponsive, {
		locale: {
			fixedVirualizationNotSupported: 'The Responsive functionality requires a different virtualization setting. virtualizationMode should be set to "continuous".'
		}
	});

	$.ig.igGridMultiColumnHeaders = $.ig.igGridMultiColumnHeaders || {};

	$.extend($.ig.igGridMultiColumnHeaders, {
		locale: {
		    multiColumnHeadersNotSupportedWithColumnVirtualization: 'Multi-column headers require a different configuration. columnVirtualization should be disabled.',
		    atLeastOneColumnShouldBeShownWhenCollapseOrExpand: 'You should have at least one column shown when you expand or collapse a multi-column header.',
		    collapsedColumnIconTooltip: "Expand",
            expandedColumnIconTooltip: "Collapse"
		}
	});

}
}));// REMOVE_FROM_COMBINED_FILES
/*!@license
* Infragistics.Web.ClientUI HTML Editor localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
$.ig = $.ig || {};

if (!$.ig.HtmlEditor) {
	$.ig.HtmlEditor = {};

	$.extend($.ig.HtmlEditor, {

		locale: {
			boldButtonTitle: 'Bold',
			italicButtonTitle: 'Italic',
			underlineButtonTitle: 'Underline',
			strikethroughButtonTitle: 'Strikethrough',
			increaseFontSizeButtonTitle: 'Increase Font Size',
			decreaseFontSizeButtonTitle: 'Decrease Font Size',
			alignTextLeftButtonTitle: 'Align Text Left',
			alignTextRightButtonTitle: 'Align Text Right',
			alignTextCenterButtonTitle: 'Center',
			justifyButtonTitle: 'Justify',
			bulletsButtonTitle: 'Bullets',
			numberingButtonTitle: 'Numbering',
			decreaseIndentButtonTitle: 'Decrease Indent',
			increaseIndentButtonTitle: 'Increase Indent',
			insertPictureButtonTitle: 'Insert Picture',
			fontColorButtonTitle: 'Font Color',
			textHighlightButtonTitle: 'Text Highlight Color',
			insertLinkButtonTitle: 'Insert Hyperlink',
			insertTableButtonTitle: 'Table',
			addRowButtonTitle: 'Add Row',
			removeRowButtonTitle: 'Remove Row',
			addColumnButtonTitle: 'Add Column',
			removeColumnButtonTitle: 'Remove Column',
			inserHRButtonTitle: 'Insert Horizontal Rule',
			viewSourceButtonTitle: 'View Source',
			cutButtonTitle: 'Cut',
			copyButtonTitle: 'Copy',
			pasteButtonTitle: 'Paste',
			undoButtonTitle: 'Undo',
			redoButtonTitle: 'Redo',
			imageUrlDialogText: 'Image URL:',
			imageAlternativeTextDialogText: 'Alternative Text:',
			imageWidthDialogText: 'Image Width:',
			imageHeihgtDialogText: 'Image Height:',
			linkNavigateToUrlDialogText: 'Navigate to URL:',
			linkDisplayTextDialogText: 'Display Text:',
			linkOpenInDialogText: 'Open In:',
			linkTargetNewWindowDialogText: 'New Window',
			linkTargetSameWindowDialogText: 'Same Window',
			linkTargetParentWindowDialogText: 'Parent Window',
			linkTargetTopmostWindowDialogText: 'Topmost Window',
			applyButtonTitle: 'Apply',
			cancelButtonTitle: 'Cancel',
            defaultToolbars: {
                textToolbar: "text manipulation toolbar",
                formattingToolbar: "text formatting toolbar",
                insertObjectToolbar: "objects insertion toolbar",
                copyPasteToolbar: "copy/paste toolbar"
            },
			fontNames: {
				win: [
						{ text: "Times New Roman", value: "Times New Roman" },
						{ text: "Arial", value: "Arial" },
						{ text: "Arial Black", value: "Arial Black" },
						{ text: "Helvetica", value: "Helvetica" },
						{ text: "Comic Sans MS", value: "Comic Sans MS" },
						{ text: "Courier New", value: "Courier New" },
						{ text: "Georgia", value: "Georgia" },
						{ text: "Impact", value: "Impact" },
						{ text: "Lucida Console", value: "Lucida Console" },
						{ text: "Lucida Sans Unicode", value: "Lucida Sans Unicode" },
						{ text: "Palatino Linotype", value: "Palatino Linotype" },
						{ text: "Tahoma", value: "Tahoma" },
						{ text: "Trebuchet MS", value: "Trebuchet MS" },
						{ text: "Verdana", value: "Verdana" },
						{ text: "Symbol", value: "Symbol" },
						{ text: "Webdings", value: "Webdings" },
						{ text: "Wingdings", value: "Wingdings" },
						{ text: "MS Sans Serif", value: "MS Sans Serif" },
						{ text: "MS Serif", value: "MS Serif" }
					],
				mac: [
						{ text: "Times New Roman", value: "Times New Roman" },
						{ text: "Arial", value: "Arial" },
						{ text: "Arial Black", value: "Arial Black" },
						{ text: "Helvetica", value: "Helvetica" },
						{ text: "Comic Sans MS", value: "Comic Sans MS" },
						{ text: "Courier New", value: "Courier New" },
						{ text: "Georgia", value: "Georgia" },
						{ text: "Impact", value: "Impact" },
						{ text: "Monaco", value: "Monaco" },
						{ text: "Lucida Grande", value: "Lucida Grande" },
						{ text: "Book Antiqua", value: "Book Antiqua" },
						{ text: "Geneva", value: "Geneva" },
						{ text: "Trebuchet MS", value: "Trebuchet" },
						{ text: "Verdana", value: "Verdana" },
						{ text: "Symbol", value: "Symbol" },
						{ text: "Webdings", value: "Webdings" },
						{ text: "Zapf Dingbats", value: "Zapf Dingbats" },
						{ text: "New York", value: "New York" }
					]
			},
			fontSizes: [
				{ text: "1", value: "7.5 pt"},
				{ text: "2", value: "10 pt"},
				{ text: "3", value: "12 pt"},
				{ text: "4", value: "13.5 pt"},
				{ text: "5", value: "18 pt"},
				{ text: "6", value: "24 pt"},
				{ text: "7", value: "36 pt"}
			],
			formatsList: [
					{ text: "h1", value: "Heading 1" },
					{ text: "h2", value: "Heading 2" },
					{ text: "h3", value: "Heading 3" },
					{ text: "h4", value: "Heading 4" },
					{ text: "h5", value: "Heading 5" },
					{ text: "h6", value: "Heading 6" },
                    { text: "p", value: "Normal" }
				]
		}

	});
}
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Notifier localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function($) {
$.ig = $.ig || {};

if (!$.ig.Notifier) {
	$.ig.Notifier = {};

	$.extend($.ig.Notifier, {
		locale: {
			successMsg: "Success",
			errorMsg: "Error",
			warningMsg: "Warning"
		}
	});

}
})(jQuery);

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
/*!@license
* Infragistics.Web.ClientUI Pivot Data Selector localization resources 16.2.20162.2040
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

    if (!$.ig.PivotDataSelector) {
        $.ig.PivotDataSelector = {};

        $.extend($.ig.PivotDataSelector, {
            locale: {
                invalidBaseElement: " is not supported as base element. Use DIV instead.",
                catalog: "Catalog",
                cube: "Cube",
                measureGroup: "Measure Group",
                measureGroupAll: "(All)",
                rows: "Rows",
                columns: "Columns",
                measures: "Measures",
                filters: "Filters",
                deferUpdate: "Defer Update",
                updateLayout: "Update Layout",
                selectAll: "Select All"
            }
        });
    }
}));// REMOVE_FROM_COMBINED_FILES
/*!@license
* Infragistics.Web.ClientUI Pivot Grid localization resources 16.2.20162.2040
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

    if (!$.ig.PivotGrid) {
        $.ig.PivotGrid = {};

        $.extend($.ig.PivotGrid, {
            locale: {
                filtersHeader: "Drop Filter Fields Here",
                measuresHeader: "Drop Data Items Here",
                rowsHeader: "Drop Row Fields Here",
                columnsHeader: "Drop Column Fields Here",
                disabledFiltersHeader: "Filter Fields",
                disabledMeasuresHeader: "Data Items",
                disabledRowsHeader: "Row Fields",
                disabledColumnsHeader: "Column Fields",
                noSuchAxis: "No such axis"
            }
        });
    }
}));// REMOVE_FROM_COMBINED_FILES
/*!@license
* Infragistics.Web.ClientUI Popover localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function($) {
$.ig = $.ig || {};

if (!$.ig.Popover) {
	$.ig.Popover = {};

	$.extend( $.ig.Popover, {
		locale: {
			popoverOptionChangeNotSupported: "Changing the following option after igPopover has been initialized is not supported:",
			popoverShowMethodWithoutTarget: "The target parameter of the show function is mandatory when the selectors option is used"
		}
	});

}
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Rating localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.Rating) {
	    $.ig.Rating = {};

	    $.extend($.ig.Rating, {
		    locale: {
			    setOptionError: 'Runtime changes are not allowed for the following option: '
		    }
	    });
    }
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Scroll localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.Scroll) {
	    $.ig.Scroll = {};

	    $.extend($.ig.Scroll, {
		    locale: {
		        errorNoElementLink: 'Element that is being linked does not exists.',
		        errorNoScrollbarLink: 'Scrollbar element that is being linked does not exists.'
		    }
	    });

    }
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Splitter localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.Splitter) {
	    $.ig.Splitter = {};

	    $.extend($.ig.Splitter, {
		    locale: {
		        errorPanels: 'The number of panels have to be no more than two.',
		        errorSettingOption: 'Error setting option.'
		    }
	    });

    }
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Tile Manager localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
$.ig = $.ig || {};

if (!$.ig.TileManager) {
	$.ig.TileManager = {};

	$.extend($.ig.TileManager, {
		locale: {
			renderDataError: "Data was not successfully retrieved or parsed.",
		    setOptionItemsLengthError: "The length of the items configurations does not match the number of the tiles."
		}
	});
}
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Toolbar localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
$.ig = $.ig || {};

if (!$.ig.Toolbar) {
    $.ig.Toolbar = {};

    $.extend($.ig.Toolbar, {

		locale: {
			collapseButtonTitle: 'Collapse',
			expandButtonTitle: 'Expand'
		}

	});
}
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Tree localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.Tree) {
	    $.ig.Tree = {};

	    $.extend($.ig.Tree, {
		    locale: {
			    invalidArgumentType: 'Invalid argument type provided.',
			    errorOnRequest: 'An error has occurred while retrieving data: ',
			    noDataSourceUrl: 'The igTree control requires a dataSourceUrl provided in order to initiate a request for data to that URL.',
			    incorrectPath: 'A node was not found at the provided path: ',
			    incorrectNodeObject: 'The provided argument is not a jQuery node element.',
			    setOptionError: 'Runtime changes are not allowed for the following option: ',
			    moveTo: '<strong>Move to</strong> {0}',
			    moveBetween: '<strong>Move between</strong> {0} and {1}',
			    moveAfter: '<strong>Move after</strong> {0}',
			    moveBefore: '<strong>Move before</strong> {0}',
			    copyTo: '<strong>Copy to</strong> {0}',
			    copyBetween: '<strong>Copy between</strong> {0} and {1}',
			    copyAfter: '<strong>Copy after</strong> {0}',
			    copyBefore: '<strong>Copy before</strong> {0}',
			    and: 'and'
		    }
	    });

    }
})(jQuery);

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
/*!@license
* Infragistics.Web.ClientUI Upload localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.Upload) {
	    $.ig.Upload = {};

	    $.extend($.ig.Upload, {

		    locale: {
			    labelUploadButton: "Upload File",
			    labelAddButton: "Add",
			    labelClearAllButton: "Clear Uploaded",
			    // M.H. 13 May 2011 - fix bug 75042
			    labelSummaryTemplate: "{0} of {1} uploaded",
			    labelSummaryProgressBarTemplate: "{0}/{1}",
			    labelShowDetails: "Show Details",
			    labelHideDetails: "Hide Details",
			    labelSummaryProgressButtonCancel: "Cancel",
			    // M.H. 1 June 2011 Fix bug #77532
			    labelSummaryProgressButtonContinue: "Upload",
			    labelSummaryProgressButtonDone: "Done",
			    labelProgressBarFileNameContinue: "...",

			    //error messages
			    errorMessageFileSizeExceeded: "Max file size exceeded.",
			    errorMessageGetFileStatus: "Could not get your current file status! Probably connection dropped.",
			    errorMessageCancelUpload: "Could not send to server command to cancel upload! Probably connection dropped.",
			    errorMessageNoSuchFile: "The file you requested could not be found. Probably this file is too big.",
			    errorMessageOther: "There is internal error uploading file. Error code: {0}.",
			    errorMessageValidatingFileExtension: "File extension validation failed.",
			    errorMessageAJAXRequestFileSize: "AJAX error while trying to get file size.",
			    errorMessageMaxUploadedFiles: "Maximum count of uploading files exceeded.",
			    errorMessageMaxSimultaneousFiles: "Value of maxSimultaneousFilesUploads is incorrect. It should be more than 0 or null.",
			    errorMessageTryToRemoveNonExistingFile: "You are trying to remove non-existing file with id {0}.",
			    errorMessageTryToStartNonExistingFile: "You are trying to start non-existing file with id {0}.",
				errorMessageDropMultipleFilesWhenSingleModel: "It is not allowed to drop more than 1 file when mode is single",

			    // M.H. 12 May 2011 - fix bug 74763: add title to all buttons
			    // title attributes            
			    titleUploadFileButtonInit: "Upload File",
			    titleAddFileButton: "Add",
			    titleCancelUploadButton: "Cancel",
			    // M.H. 1 June 2011 Fix bug #77532
			    titleSummaryProgressButtonContinue: "Upload",
			    titleClearUploaded: "Clear Uploaded",
			    titleShowDetailsButton: "Show Details",
			    titleHideDetailsButton: "Hide Details",
			    titleSummaryProgressButtonCancel: "Cancel",
			    titleSummaryProgressButtonDone: "Done",
			    // M.H. 1 June 2011 Fix bug #77532
			    titleSingleUploadButtonContinue: "Upload",
			    titleClearAllButton: "Clear Uploaded"
		    }
	    });

    }
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Validator localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.Validator) {
	    $.ig.Validator = {
		    locale: {
		        defaultMessage: 'This field needs attention',
		        selectMessage: 'A value should be selected',
		        rangeSelectMessage: 'At least {0} but no more than {1} items should be selected',
		        minSelectMessage: 'At least {0} item(s) should be selected',
		        maxSelectMessage: 'No more than {0} item(s) should be selected',
		        rangeLengthMessage: 'Entry should be between {0} and {1} characters long',
		        minLengthMessage: 'Entry should be at least {0} character(s) long',
		        maxLengthMessage: 'Entry should be no more than {0} character(s) long',
		        requiredMessage: 'This field is required',
		        patternMessage: 'Entry does not match the required pattern',
		        maskMessage: 'All required positions should be filled',
		        dateFieldsMessage: 'Date field values should be entered',
		        invalidDayMessage: 'A valid day of the month should be entered',
		        dateMessage: 'A valid date should be entered',
		        numberMessage: 'A valid number should be entered',
		        rangeValueMessage: 'A value between {0} and {1} should be entered',
		        minValueMessage: 'A value of at least {0} should be entered',
		        maxValueMessage: 'A value no more than {0} should be entered',
		        emailMessage: 'A valid email address should be entered',
		        creditCardMessage: 'A valid payment card number should be entered',
		        equalToMessage: 'The two values do not match',
		        optionalString: '(optional)'
		    }
	    };
    }
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Video Player localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.VideoPlayer) {
	    $.ig.VideoPlayer = {};

	    $.extend($.ig.VideoPlayer, {

		    locale: {
			    liveStream: "Live video",
			    live: "Live",
			    paused: "Paused",
			    playing: "Playing",
			    play: 'Play',
			    volume: "Volume",
			    unsupportedVideoSource: "The current video sources does not contain a format that is supported by your browser.",
			    missingVideoSource: "No compatible video source.",
			    progressLabelLongFormat: "$currentTime$ / $duration$",
			    progressLabelShortFormat: "$currentTime$",
			    enterFullscreen: "Go Fullscreen",
			    exitFullscreen: "Exit Fullscreen",
			    skipTo: "SKIP TO",
			    unsupportedBrowser: "The current browser does not support HTML5 video. <br/>Try upgrading to any of the following versions:",
			    currentBrowser: "Current browser: {0}",
			    ie9: "Microsoft Internet Explorer V 9+",
			    chrome8: "Google Chrome V 8+",
			    firefox36: "Mozilla Firefox V 3.6+",
			    safari5: "Apple Safari V 5+",
			    opera11: "Opera V 11+",
			    ieDownload: "http://www.microsoft.com/windows/internet-explorer/default.aspx",
			    operaDownload: "http://www.opera.com/download/",
			    chromeDownload: "http://www.google.com/chrome",
			    firefoxDownload: "http://www.mozilla.com/",
			    safariDownload: "http://www.apple.com/safari/download/",
			    buffering: 'Buffering...',
			    adMessage: 'Ad: Video will resume in $duration$ seconds.',
			    adMessageLong: 'Ad: Video will resume in $duration$.',
			    adMessageNoDuration: 'Ad: Video will resume after the commercial.',
			    adNewWindowTip: 'Ad: Click to open ad content in a new window.',
			    nonDivException: 'The Infragistics HTML5 Video Player can be instantiated only on a DIV tag.',
			    relatedVideos: 'RELATED VIDEOS',
			    replayButton: 'Replay',
			    replayTooltip: 'Click to replay last video.'
		    }
	    });

    }
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI Zoombar localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
	$.ig = $.ig || {};

	if (!$.ig.Zoombar) {
		$.ig.Zoombar = {};

		$.extend($.ig.Zoombar, {

			locale: {
				zoombarTargetNotSpecified: "igZoombar requires a valid target to attach to!",
				zoombarTypeNotSupported: "The type of widget the Zoombar is trying to attach to is not supported!",
				zoombarProviderNotRecognized: "igZoombar could not initialize a provider from the class specified or the value passed is not a class.",
				optionChangeNotSupported: "Changing the following option after the igZoombar has been created is not supported:"
			}
		});

	}
})(jQuery);

/*!@license
* Infragistics.Web.ClientUI common utilities localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.util) {
	    $.ig.util = {};

	    $.extend($.ig.util, {

		    locale: {
			    unsupportedBrowser: "The current browser does not support HTML5 canvas element. <br/>Try upgrading to any of the following versions:",
			    currentBrowser: "Current browser: {0}",
			    ie9: "Microsoft Internet Explorer V 9+",
			    chrome8: "Google Chrome V 8+",
			    firefox36: "Mozilla Firefox V 3.6+",
			    safari5: "Apple Safari V 5+",
			    opera11: "Opera V 11+",
			    ieDownload: "http://www.microsoft.com/windows/internet-explorer/default.aspx",
			    operaDownload: "http://www.opera.com/download/",
			    chromeDownload: "http://www.google.com/chrome",
			    firefoxDownload: "http://www.mozilla.com/",
			    safariDownload: "http://www.apple.com/safari/download/"
		    }
	    });

    }
})(jQuery);


