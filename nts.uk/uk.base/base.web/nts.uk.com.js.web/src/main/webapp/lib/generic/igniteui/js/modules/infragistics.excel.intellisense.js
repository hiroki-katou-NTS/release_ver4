intellisense.annotate(jQuery.ig.documentsCore.DocumentEncryptedException, function () {
		/// <signature>
		///   <summary>Represents an error that occurs when no password is used to open an encrypted document.</summary>
		/// </signature>
		/// <signature>
		///   <summary>Represents an error that occurs when no password is used to open an encrypted document.</summary>
		///   <param name='message' type='String' >A message describing the error.</param>
		/// </signature>
		/// <signature>
		///   <summary>Represents an error that occurs when no password is used to open an encrypted document.</summary>
		///   <param name='message' type='String' >A message describing the error.</param>
		///   <param name='innerException' type='Error' >The exception that caused this exception.</param>
		/// </signature>
});

intellisense.addEventListener('statementcompletion', function (e) {
    e.items = e.items.filter(function (item) {

        var p = item.parentObject;

		if (p === $.ig) {
            return item.value && item.value._isNamespace;
        }

        if (p._isEnum && (typeof item.value !== 'number' || item.name === 'length')) {
            return false;
        }

		if (item.name == '$type') {
            return false;
        }

        if (p.$type && typeof (p.$type.typeName) === 'function') {

            var typeName = p.$type.typeName();
            if (typeName.indexOf('documentsCore.') === 0) {
				if (item.name.length !== 0 && item.name[0] === '$') {
                    return false;
                }

                if (item.name.indexOf('init') === 0) {
                    if (item.name.length === 4) {
                        return false;
                    }

                    var num = Number(item.name.substr(4));
                    if (isFinite(num)) {
                        return false;
                    }
                }

				if (item.name.indexOf('_op_') >= 0) {
                    return false;
                }

				// Hide members suffixed with ...Internal
				var internalIndex = item.name.indexOf('Internal');
                if (internalIndex >= 0 && (internalIndex + 8) === item.name.length) {
                    return false;
                }
            }
        }

        return true;
    });

	for (var i = 0; i < e.items.length; i++) {
        var item = e.items[i];
        if (item.value && item.value._isNamespace) {
            item.glyph = 'vs:GlyphGroupNamespace';
        }
        else if (item.value && item.value._isEnum) {
            item.glyph = 'vs:GlyphGroupEnum';
        }
        else if (item.parentObject._isEnum) {
            item.glyph = 'vs:GlyphGroupEnumMember';
        }
    }

});
intellisense.annotate(jQuery.ig.excel.SortDirection, function () {
		/// <signature>
		///   <summary>Represents the various sort directions which can be used with a SortCondition.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.SortDirection, {
		/// <field type='SortDirection' >Sort values in an ascending manner.</field>
	ascending: null,
		/// <field type='SortDirection' >Sort values in an descending manner.</field>
	descending: null
});

intellisense.annotate(jQuery.ig.excel.OpenPackagingNonConformanceReason, function () {
		/// <signature>
		///   <summary>Constants which identify the reason a IPackagePart was deemed
   ///     non-conformant during the conformance verification process.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.OpenPackagingNonConformanceReason, {
		/// <field type='OpenPackagingNonConformanceReason' >The IPackagePart conforms fully to the ECMA TC45 Open Packaging Conventions.</field>
	conformant: null,
		/// <field type='OpenPackagingNonConformanceReason' >The package implementer shall require a content type that does not include comments
		///     and the format designer shall specify such a content type. [M1.15]</field>
	contentTypeHasComments: null,
		/// <field type='OpenPackagingNonConformanceReason' >IPackage implementers shall only create and only recognize parts with a content type;
		///     format designers shall specify a content type for each part included in the format.
		///     Content types for package parts shall fit the definition and syntax for media types
		///     as specified in RFC 2616, §3.7. [M1.13]</field>
	contentTypeHasInvalidSyntax: null,
		/// <field type='OpenPackagingNonConformanceReason' >Content types shall not use linear white space either between the type and subtype or
		///     between an attribute and its value. Content types also shall not have leading or
		///     trailing white spaces. IPackage implementers shall create only such content types
		///     and shall require such content types when retrieving a part from a package;
		///     format designers shall specify only such content types for inclusion in the format. [M1.14]</field>
	contentTypeHasInvalidWhitespace: null,
		/// <field type='OpenPackagingNonConformanceReason' >IPackage implementers and format designers shall not create content types
		///     with parameters for the package specific parts defined in this Open Packaging
		///     specification and shall treat the presence of parameters in these content types
		///     as an error. [M1.22]</field>
	contentTypeHasParameters: null,
		/// <field type='OpenPackagingNonConformanceReason' >The package implementer shall require a content type and
		///     the format designer shall specify the content type. [M1.2], [M1.13]</field>
	contentTypeMissing: null,
		/// <field type='OpenPackagingNonConformanceReason' >The IPackage.GetPart method threw an exception.</field>
	couldNotGetPackagePart: null,
		/// <field type='OpenPackagingNonConformanceReason' >Part name equivalence is determined by comparing part names as
		///     case-insensitive ASCII strings. Packages shall not contain equivalent
		///     part names and package implementers shall neither create nor recognize
		///     packages with equivalent part names. [M1.12]</field>
	duplicateName: null,
		/// <field type='OpenPackagingNonConformanceReason' >If the package implementer specifies a growth hint, it is set when a
		///     part is created and the package implementer shall not change the growth
		///     hint after the part has been created. [M1.16]</field>
	growthHintChanged: null,
		/// <field type='OpenPackagingNonConformanceReason' >A package implementer shall neither create nor recognize
		///     a part with a part name derived from another part name by
		///     appending segments to it. [M1.11]</field>
	nameDerivesFromExistingPartName: null,
		/// <field type='OpenPackagingNonConformanceReason' >A part name shall start with a forward slash (“/”) character. [M1.4]</field>
	nameDoesNotStartWithForwardSlash: null,
		/// <field type='OpenPackagingNonConformanceReason' >A part name shall not have a forward slash as the last character. [M1.5]</field>
	nameEndsWithForwardSlash: null,
		/// <field type='OpenPackagingNonConformanceReason' >The package implementer shall require a part name. [M1.1]</field>
	nameMissing: null,
		/// <field type='OpenPackagingNonConformanceReason' >The package implementer shall require that every Relationship element
		///     has an Id attribute, the value of which is unique within the Relationships
		///     part, and that the Id type is xsd:ID, the value of which conforms to the naming
		///     restrictions for xsd:IDas described in the W3C Recommendation “XML Schema Part 2:
		///     Datatypes.” [M1.26]</field>
	relationshipIdInvalid: null,
		/// <field type='OpenPackagingNonConformanceReason' >The package implementer shall name relationship parts according to the special
		///     relationships part naming convention and require that parts with names that
		///     conform to this naming convention have the content type for a Relationships
		///     part. [M1.30]</field>
	relationshipNameInvalid: null,
		/// <field type='OpenPackagingNonConformanceReason' >The package implementer shall require the Target attribute to be a URI
		///     reference pointing to a target resource. The URI reference shall be a URI
		///     or a relative reference. [M1.28]</field>
	relationshipTargetInvalid: null,
		/// <field type='OpenPackagingNonConformanceReason' >When set to Internal, the Target attribute shall be a relative reference and
		///     that reference is interpreted relative to the “parent” part. For package
		///     relationships, the package implementer shallresolve relative references in
		///     the Target attribute against the pack URI that identifies the entire package
		///     resource. [M1.29]</field>
	relationshipTargetNotRelativeReference: null,
		/// <field type='OpenPackagingNonConformanceReason' >The Relationships part shall not have relationships to any other part. IPackage
		///     implementers shall enforce this requirement upon the attempt to create such a
		///     relationship and shall treat any such relationship as invalid. [M1.25]</field>
	relationshipTargetsOtherRelationship: null,
		/// <field type='OpenPackagingNonConformanceReason' >The package implementer shall require the Type attribute to be a URI
		///     that defines the role of the relationship and the format designer shall
		///     specify such a Type. [M1.27]</field>
	relationshipTypeInvalid: null,
		/// <field type='OpenPackagingNonConformanceReason' >A part name shall not have empty segments. [M1.3]</field>
	segmentEmpty: null,
		/// <field type='OpenPackagingNonConformanceReason' >A segment shall not end with a dot (“.”) character. [M1.9]</field>
	segmentEndsWithDotCharacter: null,
		/// <field type='OpenPackagingNonConformanceReason' >A segment shall not hold any characters other than pchar characters. [M1.6]</field>
	segmentHasNonPCharCharacters: null,
		/// <field type='OpenPackagingNonConformanceReason' >A segment shall not contain percent-encoded forward slash (“/”),
		///     or backward slash (“\”) characters. [M1.7]</field>
	segmentHasPercentEncodedSlashCharacters: null,
		/// <field type='OpenPackagingNonConformanceReason' >A segment shall not contain percent-encoded unreserved characters. [M1.8]</field>
	segmentHasPercentEncodedUnreservedCharacters: null,
		/// <field type='OpenPackagingNonConformanceReason' >A segment shall include at least one non-dot character. [M1.10]</field>
	segmentMissingNonDotCharacter: null,
		/// <field type='OpenPackagingNonConformanceReason' >XML content shall not contain elements or attributes drawn from “xml” or “xsi”
		///     namespaces unless they are explicitly defined in the XSD schema or by other means
		///     described in this Open Packaging specification. IPackage implementers shall enforce
		///     this requirement upon creation and retrieval of the XML content. [M1.21]</field>
	xmlContentDrawsOnUndefinedNamespace: null,
		/// <field type='OpenPackagingNonConformanceReason' >XML content shall be valid against the corresponding XSD schema defined
		///     in this Open Packaging specification. In particular, the XML content shall
		///     not contain elements or attributes drawn from namespaces that are not explicitly
		///     defined in the corresponding XSD unless the XSD allows elements or attributes drawn
		///     from any namespace to be present in particular locations in the XML markup. IPackage
		///     implementers  shall enforce this requirement upon creation and retrieval of the
		///     XML content. [M1.20]</field>
	xmlContentInvalidForSchema: null,
		/// <field type='OpenPackagingNonConformanceReason' >XML content shall be encoded using either UTF-8 or UTF-16.
		///     If any part includes an encoding declaration, as defined in
		///     §4.3.3 of the XML 1.0 specification, that declaration shall
		///     not name any encoding other than UTF-8 or UTF-16. IPackage
		///     implementers shall enforce this requirement upon creation
		///     and retrieval of the XML content. [M1.17]</field>
	xmlEncodingUnsupported: null
});

intellisense.annotate(jQuery.ig.excel.WorksheetProtectedSelectionMode, function () {
		/// <signature>
		///   <summary>Enumeration used to indicate which cells may be selected when the Worksheet is protected.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetProtectedSelectionMode, {
		/// <field type='WorksheetProtectedSelectionMode' >All cells including locked cells may be selected.</field>
	allCells: null,
		/// <field type='WorksheetProtectedSelectionMode' >The selection cannot be change.</field>
	noCells: null,
		/// <field type='WorksheetProtectedSelectionMode' >Only cells that are unlocked may be selected via the keyboard or mouse. Note a cell that is locked may be selected if a range selection is created between 2 unlocked cells.</field>
	unlockedCells: null
});

intellisense.annotate(jQuery.ig.excel.TopOrBottomFilterType, function () {
		/// <signature>
		///   <summary>Represents the various filter types available for the TopOrBottomFilter.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.TopOrBottomFilterType, {
		/// <field type='TopOrBottomFilterType' >Filter in the bottom N percent of values the sorted list of values.</field>
	bottomPercentage: null,
		/// <field type='TopOrBottomFilterType' >Filter in the bottom N values in the sorted list of values.</field>
	bottomValues: null,
		/// <field type='TopOrBottomFilterType' >Filter in the top N percent of values the sorted list of values.</field>
	topPercentage: null,
		/// <field type='TopOrBottomFilterType' >Filter in the top N values in the sorted list of values.</field>
	topValues: null
});

intellisense.annotate(jQuery.ig.excel.RelativeDateRangeOffset, function () {
		/// <signature>
		///   <summary>Represents the various relative date offsets which can be filtered by the RelativeDateRangeFilter.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.RelativeDateRangeOffset, {
		/// <field type='RelativeDateRangeOffset' >Filter in values in the current duration relative to the filter's creation date.</field>
	current: null,
		/// <field type='RelativeDateRangeOffset' >Filter in values in the next duration relative to the filter's creation date.</field>
	next: null,
		/// <field type='RelativeDateRangeOffset' >Filter in values in the previous duration relative to the filter's creation date.</field>
	previous: null
});

intellisense.annotate(jQuery.ig.excel.RelativeDateRangeDuration, function () {
		/// <signature>
		///   <summary>Represents the various durations which can be filtered by the RelativeDateRangeFilter.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.RelativeDateRangeDuration, {
		/// <field type='RelativeDateRangeDuration' >The duration of accepted values is one day.</field>
	day: null,
		/// <field type='RelativeDateRangeDuration' >The duration of accepted values is one month.</field>
	month: null,
		/// <field type='RelativeDateRangeDuration' >The duration of accepted values is one quarter.</field>
	quarter: null,
		/// <field type='RelativeDateRangeDuration' >The duration of accepted values is one week.</field>
	week: null,
		/// <field type='RelativeDateRangeDuration' >The duration of accepted values is one year.</field>
	year: null
});

intellisense.annotate(jQuery.ig.excel.FixedDateGroupType, function () {
		/// <signature>
		///   <summary>Represents the various types, or precisions, of a FixedDateGroup.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.FixedDateGroupType, {
		/// <field type='FixedDateGroupType' >The group represents the day in which the FixedDateGroup.Value exists.</field>
	day: null,
		/// <field type='FixedDateGroupType' >The group represents the hour in which the FixedDateGroup.Value exists.</field>
	hour: null,
		/// <field type='FixedDateGroupType' >The group represents the minute in which the FixedDateGroup.Value exists.</field>
	minute: null,
		/// <field type='FixedDateGroupType' >The group represents the month in which the FixedDateGroup.Value exists.</field>
	month: null,
		/// <field type='FixedDateGroupType' >The group represents the second in which the FixedDateGroup.Value exists.</field>
	second: null,
		/// <field type='FixedDateGroupType' >The group represents the year in which the FixedDateGroup.Value exists.</field>
	year: null
});

intellisense.annotate(jQuery.ig.excel.ExcelComparisonOperator, function () {
		/// <signature>
		///   <summary>Represents the various comparisons which can be used in the CustomFilterCondition.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ExcelComparisonOperator, {
		/// <field type='ExcelComparisonOperator' >Filter in string values which begin with the comparison value.</field>
	beginsWith: null,
		/// <field type='ExcelComparisonOperator' >Filter in string values which contain the comparison value.</field>
	contains: null,
		/// <field type='ExcelComparisonOperator' >Filter in string values which do not begin with the comparison value.</field>
	doesNotBeginWith: null,
		/// <field type='ExcelComparisonOperator' >Filter in string values which do not contain the comparison value.</field>
	doesNotContain: null,
		/// <field type='ExcelComparisonOperator' >Filter in string values which do not end with the comparison value.</field>
	doesNotEndWith: null,
		/// <field type='ExcelComparisonOperator' >Filter in string values which ends with the comparison value.</field>
	endsWith: null,
		/// <field type='ExcelComparisonOperator' >Filter in values which are equal to the comparison value.</field>
	equals: null,
		/// <field type='ExcelComparisonOperator' >Filter in values which are greater than the comparison value.</field>
	greaterThan: null,
		/// <field type='ExcelComparisonOperator' >Filter in values which are greater than or equal to the comparison value.</field>
	greaterThanOrEqual: null,
		/// <field type='ExcelComparisonOperator' >Filter in values which are less than the comparison value.</field>
	lessThan: null,
		/// <field type='ExcelComparisonOperator' >Filter in values which are less than or equal to the comparison value.</field>
	lessThanOrEqual: null,
		/// <field type='ExcelComparisonOperator' >Filter in values which are not equal to the comparison value.</field>
	notEqual: null
});

intellisense.annotate(jQuery.ig.excel.DatePeriodFilterType, function () {
		/// <signature>
		///   <summary>Represents the various date range types which can be filtered by the DatePeriodFilter.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.DatePeriodFilterType, {
		/// <field type='DatePeriodFilterType' >Filter in dates in a specific month of any year.</field>
	month: null,
		/// <field type='DatePeriodFilterType' >Filter in dates in a specific quarter of any year.</field>
	quarter: null
});

intellisense.annotate(jQuery.ig.excel.ConditionalOperator, function () {
		/// <signature>
		///   <summary>Represents the various logical operators used to combine the conditions of the CustomFilter.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ConditionalOperator, {
		/// <field type='ConditionalOperator' >Filter in values where only CustomFilter.Condition1 and CustomFilter.Condition2 pass.</field>
	and: null,
		/// <field type='ConditionalOperator' >Filter in values where either CustomFilter.Condition1 or CustomFilter.Condition2 (or both) pass.</field>
	or: null
});

intellisense.annotate(jQuery.ig.excel.CalendarType, function () {
		/// <signature>
		///   <summary>Represents the various calendar types available for the FixedValuesFilter</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.CalendarType, {
		/// <field type='CalendarType' >The Gregorian calendar should be used.</field>
	gregorian: null,
		/// <field type='CalendarType' >The Arabic version of the Gregorian calendar should be used.</field>
	gregorianArabic: null,
		/// <field type='CalendarType' >The Middle East French version of the Gregorian calendar should be used.</field>
	gregorianMeFrench: null,
		/// <field type='CalendarType' >The US English version of the Gregorian calendar should be used.</field>
	gregorianUs: null,
		/// <field type='CalendarType' >The transliterated English version of the Gregorian calendar should be used.</field>
	gregorianXlitEnglish: null,
		/// <field type='CalendarType' >The transliterated French version of the Gregorian calendar should be used.</field>
	gregorianXlitFrench: null,
		/// <field type='CalendarType' >The Hebrew lunar calendar, as described by the Gauss formula for Passover and The Complete Restatement of Oral Law, 
		///     should be used.</field>
	hebrew: null,
		/// <field type='CalendarType' >The Hijri lunar calendar, as described by the Kingdom of Saudi Arabia, Ministry of Islamic Affairs, Endowments, Da‘wah 
		///     and Guidance, should be used.</field>
	hijri: null,
		/// <field type='CalendarType' >The Japanese Emperor Era calendar, as described by Japanese Industrial Standard JIS X 0301, should be used.</field>
	japan: null,
		/// <field type='CalendarType' >The Korean Tangun Era calendar, as described by Korean Law Enactment No. 4, should be used.</field>
	korea: null,
		/// <field type='CalendarType' >Specifies that no calendar should be used.</field>
	none: null,
		/// <field type='CalendarType' >The Saka Era calendar, as described by the Calendar Reform Committee of India, as part of the Indian Ephemeris and Nautical 
		///     Almanac, should be used.</field>
	saka: null,
		/// <field type='CalendarType' >The Taiwanese calendar, as defined by the Chinese National Standard CNS 7648, should be used.</field>
	taiwan: null,
		/// <field type='CalendarType' >The Thai calendar, as defined by the Royal Decree of H.M. King Vajiravudh (Rama VI) in Royal Gazette B. E. 2456 (1913 A.D.) 
		///     and by the decree of Prime Minister Phibunsongkhram (1941 A.D.) to start the year on the Gregorian January 1 and to map year 
		///     zero to Gregorian year 543 B.C., should be used.</field>
	thai: null
});

intellisense.annotate(jQuery.ig.excel.AverageFilterType, function () {
		/// <signature>
		///   <summary>Represents the various types of the AverageFilter.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.AverageFilterType, {
		/// <field type='AverageFilterType' >Filter in values above the average of the entire range of data being filtered.</field>
	aboveAverage: null,
		/// <field type='AverageFilterType' >Filter in values below the average of the entire range of data being filtered.</field>
	belowAverage: null
});

intellisense.annotate(jQuery.ig.excel.WorksheetVisibility, function () {
		/// <signature>
		///   <summary>Represents the various visibilities of a worksheet.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetVisibility, {
		/// <field type='WorksheetVisibility' >The worksheet tab is not present in the tab bar. The worksheet can be made visible
		///     from the Unhide dialog in Microsoft Excel.</field>
	hidden: null,
		/// <field type='WorksheetVisibility' >The worksheet tab is not present in the tab bar. The worksheet can only be made visible
		///     again through a Visual Basic procedure in Microsoft Excel. The worksheet can not be made 
		///     visible through the user interface.</field>
	strongHidden: null,
		/// <field type='WorksheetVisibility' >The worksheet tab is present in the tab bar.</field>
	visible: null
});

intellisense.annotate(jQuery.ig.excel.WorksheetView, function () {
		/// <signature>
		///   <summary>Represents the various views for a worksheet.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetView, {
		/// <field type='WorksheetView' >The worksheet is displayed in the normal view.</field>
	normal: null,
		/// <field type='WorksheetView' >This view shows a preview of where pages will break when the worksheet is printed.</field>
	pageBreakPreview: null,
		/// <field type='WorksheetView' >The worksheet is displayed as it will appear when printed. This view displays where
		///     printed pages will begin and end as well as any headers or footers for the workbook.
		///     This value is only supported in Excel 2007 and defaults to Normal in earlier version.</field>
	pageLayout: null
});

intellisense.annotate(jQuery.ig.excel.WorksheetColumnWidthUnit, function () {
		/// <signature>
		///   <summary>Represents the various units in which a column width can be represented.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetColumnWidthUnit, {
		/// <field type='WorksheetColumnWidthUnit' >The column width is represented in units of the '0' digit character width, including column padding. The digit is 
		///     measured with the default font for the workbook. The padding is a few pixels on either side of the column plus an 
		///     additional pixel for the gridline.</field>
	character: null,
		/// <field type='WorksheetColumnWidthUnit' >The column width is represented in 256ths of the '0' digit character width, including column padding, which means this 
		///     value will be 256 times the width expressed in Character units. The digit is measured with the default font for the 
		///     workbook. The padding is a few pixels on either side of the column plus an additional pixel for the gridline. These 
		///     units are the units in which the WorksheetColumn.Width and Worksheet.DefaultColumnWidth 
		///     properties are expressed.</field>
	character256th: null,
		/// <field type='WorksheetColumnWidthUnit' >The column width is represented in units of the '0' digit character width, excluding padding. The digit is measured with 
		///     the default font for the workbook. These units are the units in which Microsoft Excel displays column widths to the user
		///     and accepts new column widths from the user in the 'Column Width' dialog.</field>
	characterPaddingExcluded: null,
		/// <field type='WorksheetColumnWidthUnit' >The column width is represented in pixels.</field>
	pixel: null,
		/// <field type='WorksheetColumnWidthUnit' >The column width is represented in points.</field>
	point: null,
		/// <field type='WorksheetColumnWidthUnit' >The column width is represented in twips (20ths of a point).</field>
	twip: null
});

intellisense.annotate(jQuery.ig.excel.WorksheetTableStyleArea, function () {
		/// <signature>
		///   <summary>Represents the various areas which can have a format applied at the table style level.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetTableStyleArea, {
		/// <field type='WorksheetTableStyleArea' >The format is applied to the even numbered column stripes in the table.</field>
	alternateColumnStripe: null,
		/// <field type='WorksheetTableStyleArea' >The format is applied to the even numbered row stripes in the table.</field>
	alternateRowStripe: null,
		/// <field type='WorksheetTableStyleArea' >The format is applied to the odd numbered column stripes in the table.</field>
	columnStripe: null,
		/// <field type='WorksheetTableStyleArea' >The format is applied to the first column in the table.</field>
	firstColumn: null,
		/// <field type='WorksheetTableStyleArea' >The format is applied to the first header cell in the table.</field>
	firstHeaderCell: null,
		/// <field type='WorksheetTableStyleArea' >The format is applied to the first total cell in the table.</field>
	firstTotalCell: null,
		/// <field type='WorksheetTableStyleArea' >The format is applied to the header row in the table.</field>
	headerRow: null,
		/// <field type='WorksheetTableStyleArea' >The format is applied to the last column in the table.</field>
	lastColumn: null,
		/// <field type='WorksheetTableStyleArea' >The format is applied to the last header cell in the table.</field>
	lastHeaderCell: null,
		/// <field type='WorksheetTableStyleArea' >The format is applied to the last total cell in the table.</field>
	lastTotalCell: null,
		/// <field type='WorksheetTableStyleArea' >The format is applied to the odd numbered row stripes in the table.</field>
	rowStripe: null,
		/// <field type='WorksheetTableStyleArea' >The format is applied to the totals row in the table.</field>
	totalRow: null,
		/// <field type='WorksheetTableStyleArea' >The format is applied to the entire table.</field>
	wholeTable: null
});

intellisense.annotate(jQuery.ig.excel.WorksheetTableColumnArea, function () {
		/// <signature>
		///   <summary>Represents the various areas which can have a format applied at the table column level.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetTableColumnArea, {
		/// <field type='WorksheetTableColumnArea' >The format is applied to the data area of the table column.</field>
	dataArea: null,
		/// <field type='WorksheetTableColumnArea' >The format is applied to the header cell of the table column.</field>
	headerCell: null,
		/// <field type='WorksheetTableColumnArea' >The format is applied to the total cell of the table column.</field>
	totalCell: null
});

intellisense.annotate(jQuery.ig.excel.WorksheetTableArea, function () {
		/// <signature>
		///   <summary>Represents the various areas which can have a format applied at the table level.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetTableArea, {
		/// <field type='WorksheetTableArea' >The format is applied to the data area of the table.</field>
	dataArea: null,
		/// <field type='WorksheetTableArea' >The format is applied to the header row of the table. All but the top border properties can be set on this 
		///     area format. Setting the top border properties will cause an exception.</field>
	headerRow: null,
		/// <field type='WorksheetTableArea' >The format is applied to the totals row of the table. All but the bottom border properties can be set on this 
		///     area format. Setting the bottom border properties will cause an exception.</field>
	totalsRow: null,
		/// <field type='WorksheetTableArea' >The format is applied to the entire table. Only the outer border properties can be set on this area format.
		///     Setting any other will cause an exception.</field>
	wholeTable: null
});

intellisense.annotate(jQuery.ig.excel.WorkbookThemeColorType, function () {
		/// <signature>
		///   <summary>Represents the various theme colors in a workbook.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorkbookThemeColorType, {
		/// <field type='WorkbookThemeColorType' >Represents the Accent 1 theme color.</field>
	accent1: null,
		/// <field type='WorkbookThemeColorType' >Represents the Accent 2 theme color.</field>
	accent2: null,
		/// <field type='WorkbookThemeColorType' >Represents the Accent 3 theme color.</field>
	accent3: null,
		/// <field type='WorkbookThemeColorType' >Represents the Accent 4 theme color.</field>
	accent4: null,
		/// <field type='WorkbookThemeColorType' >Represents the Accent 5 theme color.</field>
	accent5: null,
		/// <field type='WorkbookThemeColorType' >Represents the Accent 6 theme color.</field>
	accent6: null,
		/// <field type='WorkbookThemeColorType' >Represents the Text/Background - Dark 1 theme color.</field>
	dark1: null,
		/// <field type='WorkbookThemeColorType' >Represents the Text/Background - Dark 2 theme color.</field>
	dark2: null,
		/// <field type='WorkbookThemeColorType' >Represents the Followed Hyperlink theme color.</field>
	followedHyperlink: null,
		/// <field type='WorkbookThemeColorType' >Represents the Hyperlink theme color.</field>
	hyperlink: null,
		/// <field type='WorkbookThemeColorType' >Represents the Text/Background - Light 1 theme color.</field>
	light1: null,
		/// <field type='WorkbookThemeColorType' >Represents the Text/Background - Light 2 theme color.</field>
	light2: null
});

intellisense.annotate(jQuery.ig.excel.WorkbookFormat, function () {
		/// <signature>
		///   <summary>Represents the various file formats in which a workbook can be saved.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorkbookFormat, {
		/// <field type='WorkbookFormat' >The Excel 2007 XML file format.</field>
	excel2007: null,
		/// <field type='WorkbookFormat' >The Excel 2007 Macro-Enabled XML file format.</field>
	excel2007MacroEnabled: null,
		/// <field type='WorkbookFormat' >The Excel 2007 Macro-Enabled Template XML file format.</field>
	excel2007MacroEnabledTemplate: null,
		/// <field type='WorkbookFormat' >The Excel 2007 Template XML file format.</field>
	excel2007Template: null,
		/// <field type='WorkbookFormat' >The Excel 97-2003 BIFF8 file format.</field>
	excel97To2003: null,
		/// <field type='WorkbookFormat' >The Excel 97-2003 Template BIFF8 file format.</field>
	excel97To2003Template: null,
		/// <field type='WorkbookFormat' >The Strict Open XML file format (ISO/IEC 29500 Strict).</field>
	strictOpenXml: null
});

intellisense.annotate(jQuery.ig.excel.WorkbookEncryptionMode, function () {
		/// <signature>
		///   <summary>Represents the various encryption modes that can be used to encrypt documents saved in the 2007 file formats and later.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorkbookEncryptionMode, {
		/// <field type='WorkbookEncryptionMode' >Agile encryption used in Excel 2010 and later</field>
	agile: null,
		/// <field type='WorkbookEncryptionMode' >Standard encryption used in Excel 2007 and later</field>
	standard: null
});

intellisense.annotate(jQuery.ig.excel.VerticalTextAlignment, function () {
		/// <signature>
		///   <summary>Represents the various vertical text alignment types.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.VerticalTextAlignment, {
		/// <field type='VerticalTextAlignment' >Align the text to the bottom of the available area.</field>
	bottom: null,
		/// <field type='VerticalTextAlignment' >Align the center to the bottom of the available area.</field>
	center: null,
		/// <field type='VerticalTextAlignment' >Align the text to the top of the available area.</field>
	top: null
});

intellisense.annotate(jQuery.ig.excel.VerticalCellAlignment, function () {
		/// <signature>
		///   <summary>Enumeration for vertical alignment styles. Default value is used in property override situations.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.VerticalCellAlignment, {
		/// <field type='VerticalCellAlignment' >Bottom alignment.</field>
	bottom: null,
		/// <field type='VerticalCellAlignment' >Center alignment.</field>
	center: null,
		/// <field type='VerticalCellAlignment' >Use the current default.</field>
	default: null,
		/// <field type='VerticalCellAlignment' >Distributed alignment.</field>
	distributed: null,
		/// <field type='VerticalCellAlignment' >Justify alignment.</field>
	justify: null,
		/// <field type='VerticalCellAlignment' >Top alignment.</field>
	top: null
});

intellisense.annotate(jQuery.ig.excel.WorksheetCellFormatOptions, function () {
		/// <signature>
		///   <summary>Flagged enumeration which indicates which groups of formatting properties are used in a format.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetCellFormatOptions, {
		/// <field type='WorksheetCellFormatOptions' >All properties are used on the format.</field>
	all: null,
		/// <field type='WorksheetCellFormatOptions' >The IWorksheetCellFormat.Alignment, IWorksheetCellFormat.Indent, IWorksheetCellFormat.Rotation, 
		///     IWorksheetCellFormat.ShrinkToFit, IWorksheetCellFormat.VerticalAlignment, and 
		///     IWorksheetCellFormat.WrapText properties are used on the format.</field>
	applyAlignmentFormatting: null,
		/// <field type='WorksheetCellFormatOptions' >The IWorksheetCellFormat.BottomBorderColorInfo, IWorksheetCellFormat.BottomBorderStyle, 
		///     IWorksheetCellFormat.DiagonalBorderColorInfo, IWorksheetCellFormat.DiagonalBorders, 
		///     IWorksheetCellFormat.DiagonalBorderStyle, IWorksheetCellFormat.LeftBorderColorInfo, 
		///     IWorksheetCellFormat.LeftBorderStyle, IWorksheetCellFormat.RightBorderColorInfo, 
		///     IWorksheetCellFormat.RightBorderStyle, IWorksheetCellFormat.TopBorderColorInfo, and 
		///     IWorksheetCellFormat.TopBorderStyle properties are used on the format.</field>
	applyBorderFormatting: null,
		/// <field type='WorksheetCellFormatOptions' >The IWorksheetCellFormat.Fill property is used on the format.</field>
	applyFillFormatting: null,
		/// <field type='WorksheetCellFormatOptions' >The IWorksheetCellFormat.Font property is used on the format.</field>
	applyFontFormatting: null,
		/// <field type='WorksheetCellFormatOptions' >The IWorksheetCellFormat.FormatString property is used on the format.</field>
	applyNumberFormatting: null,
		/// <field type='WorksheetCellFormatOptions' >The IWorksheetCellFormat.Locked property is used on the format.</field>
	applyProtectionFormatting: null,
		/// <field type='WorksheetCellFormatOptions' >No format properties are used on the format.</field>
	none: null
});

intellisense.annotate(jQuery.ig.excel.TextFormatMode, function () {
		/// <signature>
		///   <summary>Represents the various way to combine a cell's value and format string to get its text.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.TextFormatMode, {
		/// <field type='TextFormatMode' >Format the cell text as it is displayed in the Microsoft Excel UI. This takes into account the cell width when 
		///     formatting the text.</field>
	asDisplayed: null,
		/// <field type='TextFormatMode' >Format the cell text as if it had unlimited space in the cell. However, this will not include padding characters 
		///     from the format string.</field>
	ignoreCellWidth: null
});

intellisense.annotate(jQuery.ig.excel.ShapePositioningMode, function () {
		/// <signature>
		///   <summary>Represents the ways shapes will be repositioned when rows and columns are resized.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ShapePositioningMode, {
		/// <field type='ShapePositioningMode' >Shapes will not move or size with the cells. The shape will remain in its absolute pixel position of
		///     the worksheet, regardless the rows and columns resized before or inside it.</field>
	dontMoveOrSizeWithCells: null,
		/// <field type='ShapePositioningMode' >Shapes will move and size with the cells. If columns before (or rows above) the shape are expanded,
		///     the shape will shift left. If columns within a shape are expanded, the shape will be widened.</field>
	moveAndSizeWithCells: null,
		/// <field type='ShapePositioningMode' >Shapes will move but not size with the cells. If columns before (or rows above) the shape are
		///     expanded, the shape will shift left. If columns within a shape are expanded, the shape will not
		///     be widened.</field>
	moveWithCells: null
});

intellisense.annotate(jQuery.ig.excel.ScrollBars, function () {
		/// <signature>
		///   <summary>Represents the various scroll bar configurations available for the workbook.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ScrollBars, {
		/// <field type='ScrollBars' >Both scroll bars are shown in Microsoft Excel.</field>
	both: null,
		/// <field type='ScrollBars' >Only the horizontal scroll bar is shown in Microsoft Excel.</field>
	horizontal: null,
		/// <field type='ScrollBars' >No scroll bars are shown in Microsoft Excel.</field>
	none: null,
		/// <field type='ScrollBars' >Only the vertical scroll bar is shown in Microsoft Excel.</field>
	vertical: null
});

intellisense.annotate(jQuery.ig.excel.ScalingType, function () {
		/// <signature>
		///   <summary>Represents the ways to scale a worksheet when it is printed.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ScalingType, {
		/// <field type='ScalingType' >The page maximums are used to determine how many pages the worksheet can be printed on.
		///     Less pages can be used if there is not enough printable content in the worksheet.</field>
	fitToPages: null,
		/// <field type='ScalingType' >The scaling factor is used to scale the worksheet when printing.</field>
	useScalingFactor: null
});

intellisense.annotate(jQuery.ig.excel.PrintNotes, function () {
		/// <signature>
		///   <summary>Represents the various ways to print cell notes.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.PrintNotes, {
		/// <field type='PrintNotes' >Cell notes are not printed.</field>
	dontPrint: null,
		/// <field type='PrintNotes' >Cell notes are printed as they are shown on the worksheet. With this option, cell
		///     notes will only appear in the printed worksheet if they are displayed on the worksheet
		///     in Microsoft Excel. If the notes just show indicators in Excel, the indicators and notes 
		///     will not be printed.</field>
	printAsDisplayed: null,
		/// <field type='PrintNotes' >Cell notes are printed on the last page, after the entire worksheet has printed.</field>
	printAtEndOfSheet: null
});

intellisense.annotate(jQuery.ig.excel.PrintErrors, function () {
		/// <signature>
		///   <summary>Represents the various ways to print cell errors in a worksheet.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.PrintErrors, {
		/// <field type='PrintErrors' >Errors are not prints, as though the cells containing them have no value.</field>
	dontPrint: null,
		/// <field type='PrintErrors' >Errors are printed as two dashes "--".</field>
	printAsDashes: null,
		/// <field type='PrintErrors' >Errors are printed just as they are displayed on the worksheet.</field>
	printAsDisplayed: null,
		/// <field type='PrintErrors' >Errors are prints as "#N/A".</field>
	printAsNA: null
});

intellisense.annotate(jQuery.ig.excel.PredefinedShapeType, function () {
		/// <signature>
		///   <summary>Represents the shape types that are predefined in Microsoft Excel.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.PredefinedShapeType, {
		/// <field type='PredefinedShapeType' >Represents the DiamondShape shape.</field>
	diamond: null,
		/// <field type='PredefinedShapeType' >Represents the EllipseShape shape.</field>
	ellipse: null,
		/// <field type='PredefinedShapeType' >Represents the HeartShape shape.</field>
	heart: null,
		/// <field type='PredefinedShapeType' >Represents the IrregularSeal1Shape shape.</field>
	irregularSeal1: null,
		/// <field type='PredefinedShapeType' >Represents the IrregularSeal2Shape shape.</field>
	irregularSeal2: null,
		/// <field type='PredefinedShapeType' >Represents the LightningBoltShape shape.</field>
	lightningBolt: null,
		/// <field type='PredefinedShapeType' >Represents the LineShape shape.</field>
	line: null,
		/// <field type='PredefinedShapeType' >Represents the PentagonShape shape.</field>
	pentagon: null,
		/// <field type='PredefinedShapeType' >Represents the RectangleShape shape.</field>
	rectangle: null,
		/// <field type='PredefinedShapeType' >Represents the RightTriangleShape shape.</field>
	rightTriangle: null,
		/// <field type='PredefinedShapeType' >Represents the StraightConnector1Shape shape.</field>
	straightConnector1: null
});

intellisense.annotate(jQuery.ig.excel.Precision, function () {
		/// <signature>
		///   <summary>Represents the types of precision which can be used when obtaining the value of a cell.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.Precision, {
		/// <field type='Precision' >The display value of the cell is used. If the cell's actual value is 10.005, but it is using currency formatting,
		///     it will display as $10.01.  When this cell is used in calculations, its displayed value of 10.01 will be used.</field>
	useDisplayValues: null,
		/// <field type='Precision' >The actual value of the cell is used. If the cell's actual value is 10.005, but it is using currency formatting,
		///     it will display as $10.01.  When this cell is used in calculations, its stored value of 10.005 will be used, 
		///     even though the display shows a slightly different value.</field>
	useRealCellValues: null
});

intellisense.annotate(jQuery.ig.excel.PositioningOptions, function () {
		/// <signature>
		///   <summary>Represents the options available for getting or setting the bounds of a shape, cell, or region.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.PositioningOptions, {
		/// <field type='PositioningOptions' >No special options should be used. Get and set actual bounds on the worksheet in its current state.</field>
	none: null,
		/// <field type='PositioningOptions' >Ignore the RowColumnBase.Hidden value on all rows and columns. 
		///     Get and set bounds as if all rows and columns were currently visible.</field>
	treatAllRowsAndColumnsAsVisible: null
});

intellisense.annotate(jQuery.ig.excel.PaperSize, function () {
		/// <signature>
		///   <summary>Represents the various paper sizes available for printing.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.PaperSize, {
		/// <field type='PaperSize' >A2 420mm x 594mm</field>
	a2: null,
		/// <field type='PaperSize' >A3 297mm x 420mm</field>
	a3: null,
		/// <field type='PaperSize' >A3 Extra 322mm x 445mm</field>
	a3Extra: null,
		/// <field type='PaperSize' >A3 Extra Transverse 322mm x 445mm</field>
	a3ExtraTransverse: null,
		/// <field type='PaperSize' >A3 Rotated 420mm x 297mm</field>
	a3Rotated: null,
		/// <field type='PaperSize' >A3 Transverse 297mm x 420mm</field>
	a3Transverse: null,
		/// <field type='PaperSize' >A4 210mm x 297mm</field>
	a4: null,
		/// <field type='PaperSize' >A4 Extra 235mm x 322mm</field>
	a4Extra: null,
		/// <field type='PaperSize' >A4 Plus 210mm x 330mm</field>
	a4Plus: null,
		/// <field type='PaperSize' >A4 Rotated 297mm x 210mm</field>
	a4Rotated: null,
		/// <field type='PaperSize' >A4 small 210mm x 297mm</field>
	a4Small: null,
		/// <field type='PaperSize' >A4 Transverse 210mm x 297mm</field>
	a4Transverse: null,
		/// <field type='PaperSize' >A5 148mm x 210mm</field>
	a5: null,
		/// <field type='PaperSize' >A5 Extra 174mm x 235mm</field>
	a5Extra: null,
		/// <field type='PaperSize' >A5 Rotated 210mm x 148mm</field>
	a5Rotated: null,
		/// <field type='PaperSize' >A5 Transverse 148mm x 210mm</field>
	a5Transverse: null,
		/// <field type='PaperSize' >A6 105mm x 148mm</field>
	a6: null,
		/// <field type='PaperSize' >A6 Rotated 148mm x 105mm</field>
	a6Rotated: null,
		/// <field type='PaperSize' >B4 (ISO) 250mm x 353mm</field>
	b4ISO_1: null,
		/// <field type='PaperSize' >B4 (ISO) 250mm x 353mm</field>
	b4ISO_2: null,
		/// <field type='PaperSize' >B4 (JIS) 257mm x 364mm</field>
	b4JIS: null,
		/// <field type='PaperSize' >B4 (JIS) Rotated 364mm x 257mm</field>
	b4JISRotated: null,
		/// <field type='PaperSize' >B5 (ISO) 176mm x 250mm</field>
	b5ISO: null,
		/// <field type='PaperSize' >B5 (ISO) Extra 201mm x 276mm</field>
	b5ISOExtra: null,
		/// <field type='PaperSize' >B5 (JIS) 182mm x 257mm</field>
	b5JIS: null,
		/// <field type='PaperSize' >B5 (JIS) Rotated 257mm x 182mm</field>
	b5JISRotated: null,
		/// <field type='PaperSize' >B5 (JIS) Transverse 182mm x 257mm</field>
	b5JISTransverse: null,
		/// <field type='PaperSize' >B6 (ISO) 125mm x 176mm</field>
	b6ISO: null,
		/// <field type='PaperSize' >B6 (JIS) 128mm x 182mm</field>
	b6JIS: null,
		/// <field type='PaperSize' >B6 (JIS) Rotated 182mm x 128mm</field>
	b6JISRotated: null,
		/// <field type='PaperSize' >C 17\" x 22\"</field>
	c: null,
		/// <field type='PaperSize' >D 22\" x 34\"</field>
	d: null,
		/// <field type='PaperSize' >Dbl. Japanese Postcard 200mm x 148mm</field>
	dblJapanesePostcard: null,
		/// <field type='PaperSize' >Dbl. Jap. Postcard Rot. 148mm x 200mm</field>
	dblJapanesePostcardRotated: null,
		/// <field type='PaperSize' >E 34\" x 44\"</field>
	e: null,
		/// <field type='PaperSize' >Envelope #10 4 1/8\" x 9 1/2\"</field>
	envelope10: null,
		/// <field type='PaperSize' >Envelope #11 4 1/2\" x 10 3/8\"</field>
	envelope11: null,
		/// <field type='PaperSize' >Envelope #12 4 3/4\" x 11\"</field>
	envelope12: null,
		/// <field type='PaperSize' >Envelope #14 5\" x 11 1/2\"</field>
	envelope14: null,
		/// <field type='PaperSize' >Envelope #9 3 7/8\" x 8 7/8\"</field>
	envelope9: null,
		/// <field type='PaperSize' >Envelope C3 324mm x 458mm</field>
	envelopeC3: null,
		/// <field type='PaperSize' >Envelope C4 229mm x 324mm</field>
	envelopeC4: null,
		/// <field type='PaperSize' >Envelope C5 162mm x 229mm</field>
	envelopeC5: null,
		/// <field type='PaperSize' >Envelope C6 114mm x 162mm</field>
	envelopeC6: null,
		/// <field type='PaperSize' >Envelope C6/C5 114mm x 229mm</field>
	envelopeC6C5: null,
		/// <field type='PaperSize' >Envelope DL 110mm x 220mm</field>
	envelopeDL: null,
		/// <field type='PaperSize' >Envelope Invite 220mm x 220mm</field>
	envelopeInvite: null,
		/// <field type='PaperSize' >Envelope Italy 110mm x 230mm</field>
	envelopeItaly: null,
		/// <field type='PaperSize' >Envelope Monarch 3 7/8\" x 7 1/2\"</field>
	envelopeMonarch: null,
		/// <field type='PaperSize' >Executive 7 1/4\" x 10 1/2\"</field>
	executive: null,
		/// <field type='PaperSize' >Folio 8 1/2\" x 13\"</field>
	folio: null,
		/// <field type='PaperSize' >German Legal Fanfold 8 1/2\" x 13\"</field>
	germanLegalFanfold: null,
		/// <field type='PaperSize' >German Std. Fanfold 8 1/2\" x 12\"</field>
	germanStandardFanfold: null,
		/// <field type='PaperSize' >Japanese Postcard 100mm x 148mm</field>
	japanesePostcard: null,
		/// <field type='PaperSize' >Japanese Postcard Rot. 148mm x 100mm</field>
	japanesePostcardRotated: null,
		/// <field type='PaperSize' >Ledger 17\" x 11\"</field>
	ledger: null,
		/// <field type='PaperSize' >Legal 8 1/2\" x 14\"</field>
	legal: null,
		/// <field type='PaperSize' >Legal Extra 9 1/2\" x 15\"</field>
	legalExtra: null,
		/// <field type='PaperSize' >Letter 8 1/2\" x 11\"</field>
	letter: null,
		/// <field type='PaperSize' >Letter Extra 9 1/2\" x 12\"</field>
	letterExtra: null,
		/// <field type='PaperSize' >Letter Extra Transv. 9 1/2\" x 12\"</field>
	letterExtraTransverse: null,
		/// <field type='PaperSize' >Letter Plus 8 1/2\" x 12 11/16\"</field>
	letterPlus: null,
		/// <field type='PaperSize' >Letter Rotated 11\" x 8 1/2\"</field>
	letterRotated: null,
		/// <field type='PaperSize' >Letter small 8 1/2\" x 11\"</field>
	letterSmall: null,
		/// <field type='PaperSize' >Letter Transverse 8 1/2\" x 11\"</field>
	letterTransverse: null,
		/// <field type='PaperSize' >Note 8 1/2\" x 11\"</field>
	note: null,
		/// <field type='PaperSize' >Quarto 215mm x 275mm</field>
	quarto: null,
		/// <field type='PaperSize' >10x11 10\" x 11\"</field>
	size10x11: null,
		/// <field type='PaperSize' >10x14 10\" x 14\"</field>
	size10x14: null,
		/// <field type='PaperSize' >11x17 11\" x 17\"</field>
	size11x17: null,
		/// <field type='PaperSize' >12x11 12\" x 11\"</field>
	size12x11: null,
		/// <field type='PaperSize' >15x11 15\" x 11\"</field>
	size15x11: null,
		/// <field type='PaperSize' >6 3/4 Envelope 3 5/8\" x 6 1/2\"</field>
	size634Envelope: null,
		/// <field type='PaperSize' >9x11 9\" x 11\"</field>
	size9x11: null,
		/// <field type='PaperSize' >Statement 5 1/2\" x 8 1/2\"</field>
	statement: null,
		/// <field type='PaperSize' >Super A/A4 227mm x 356mm</field>
	superAA4: null,
		/// <field type='PaperSize' >Super B/A3 305mm x 487mm</field>
	superBA3: null,
		/// <field type='PaperSize' >Tabloid 11\" x 17\"</field>
	tabloid: null,
		/// <field type='PaperSize' >Tabloid Extra 11 11/16\" x 18\"</field>
	tabloidExtra: null,
		/// <field type='PaperSize' >Undefined</field>
	undefined: null,
		/// <field type='PaperSize' >US Standard Fanfold 14 7/8\" x 11\"</field>
	uSStandardFanfold: null
});

intellisense.annotate(jQuery.ig.excel.PageNumbering, function () {
		/// <signature>
		///   <summary>Represents the way pages are numbered when printed.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.PageNumbering, {
		/// <field type='PageNumbering' >Pages are automatically numbered based on the style of the worksheet.</field>
	automatic: null,
		/// <field type='PageNumbering' >The starting page number specified is used for the first page, additional
		///     pages receive a page number that it one greater than the previous page.</field>
	useStartPageNumber: null
});

intellisense.annotate(jQuery.ig.excel.PageOrder, function () {
		/// <signature>
		///   <summary>Represents the ways to order the pages of multiple page worksheets.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.PageOrder, {
		/// <field type='PageOrder' >The first page to print is the top-left page. The next pages printed are below the first page.  
		///     When there are no more pages below, the page to the right of the top-left page is printed, then the pages 
		///     below it, and so on.</field>
	downThenOver: null,
		/// <field type='PageOrder' >The first page to print is the top-left page. The next pages printed are right of the first page.  
		///     When there are no more pages to the right, the page below the top-left page is printed, then the pages 
		///     to the right of it, and so on.</field>
	overThenDown: null
});

intellisense.annotate(jQuery.ig.excel.Orientation, function () {
		/// <signature>
		///   <summary>Represents the page orientations available when a worksheet is printed.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.Orientation, {
		/// <field type='Orientation' >The page is printed with the default setting.</field>
	default: null,
		/// <field type='Orientation' >The page is printed with the larger dimension horizontal.</field>
	landscape: null,
		/// <field type='Orientation' >The page is printed with the larger dimension vertical.</field>
	portrait: null
});

intellisense.annotate(jQuery.ig.excel.ObjectDisplayStyle, function () {
		/// <signature>
		///   <summary>Represents the various way objects and shapes are displayed in the workbook.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ObjectDisplayStyle, {
		/// <field type='ObjectDisplayStyle' >No objects or shapes are shown.</field>
	hideAll: null,
		/// <field type='ObjectDisplayStyle' >All object are be shown.</field>
	showAll: null,
		/// <field type='ObjectDisplayStyle' >Placeholders are shown in place of objects.</field>
	showPlaceholders: null
});

intellisense.annotate(jQuery.ig.excel.HorizontalTextAlignment, function () {
		/// <signature>
		///   <summary>Represents the various horizontal text alignment types.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.HorizontalTextAlignment, {
		/// <field type='HorizontalTextAlignment' >Align text in the center of the line.</field>
	center: null,
		/// <field type='HorizontalTextAlignment' >Distributes the words across the entire line.</field>
	distributed: null,
		/// <field type='HorizontalTextAlignment' >Align text so it is justified across the line.</field>
	justified: null,
		/// <field type='HorizontalTextAlignment' >Aligns the text with an adjusted kashida length for Arabic text.</field>
	justifiedLow: null,
		/// <field type='HorizontalTextAlignment' >Align the text to the left of the line.</field>
	left: null,
		/// <field type='HorizontalTextAlignment' >Align the text to the right of the line.</field>
	right: null,
		/// <field type='HorizontalTextAlignment' >Distributes Thai text by treating each character as a word.</field>
	thaiDistributed: null
});

intellisense.annotate(jQuery.ig.excel.HorizontalCellAlignment, function () {
		/// <signature>
		///   <summary>Enumeration for horizontal alignment styles. Default value is used in property override situations.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.HorizontalCellAlignment, {
		/// <field type='HorizontalCellAlignment' >Centered alignment.</field>
	center: null,
		/// <field type='HorizontalCellAlignment' >Centers the contents of the left-most cell in a center across selection group. All other cells in the center across selection group must be empty. The cells are not merged, and the data may appear to be in a cell other than the left-most cell.</field>
	centerAcrossSelection: null,
		/// <field type='HorizontalCellAlignment' >Use the current default.</field>
	default: null,
		/// <field type='HorizontalCellAlignment' >Distributed alignment.</field>
	distributed: null,
		/// <field type='HorizontalCellAlignment' >Repeat cell value to fill whole cell.</field>
	fill: null,
		/// <field type='HorizontalCellAlignment' >Alignment depends on underlying data type.</field>
	general: null,
		/// <field type='HorizontalCellAlignment' >Justify alignment.</field>
	justify: null,
		/// <field type='HorizontalCellAlignment' >Left alignment.</field>
	left: null,
		/// <field type='HorizontalCellAlignment' >Right alignment.</field>
	right: null
});

intellisense.annotate(jQuery.ig.excel.FontUnderlineStyle, function () {
		/// <signature>
		///   <summary>Enumeration for font underline styles. Default value is used in property override situations.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.FontUnderlineStyle, {
		/// <field type='FontUnderlineStyle' >Use the current default.</field>
	default: null,
		/// <field type='FontUnderlineStyle' >Double underline style.</field>
	double: null,
		/// <field type='FontUnderlineStyle' >Double accounting underline style.</field>
	doubleAccounting: null,
		/// <field type='FontUnderlineStyle' >No underline style.</field>
	none: null,
		/// <field type='FontUnderlineStyle' >Single underline style.</field>
	single: null,
		/// <field type='FontUnderlineStyle' >Single accounting underline style.</field>
	singleAccounting: null
});

intellisense.annotate(jQuery.ig.excel.FontSuperscriptSubscriptStyle, function () {
		/// <signature>
		///   <summary>Enumeration for font superscript or subscript style. Default value is used in property override situations.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.FontSuperscriptSubscriptStyle, {
		/// <field type='FontSuperscriptSubscriptStyle' >Use the current default.</field>
	default: null,
		/// <field type='FontSuperscriptSubscriptStyle' >No superscript or subscript style.</field>
	none: null,
		/// <field type='FontSuperscriptSubscriptStyle' >Subscript style.</field>
	subscript: null,
		/// <field type='FontSuperscriptSubscriptStyle' >Superscript style.</field>
	superscript: null
});

intellisense.annotate(jQuery.ig.excel.FillPatternStyle, function () {
		/// <signature>
		///   <summary>Enumeration for fill pattern styles. Default value is used in property override situations.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.FillPatternStyle, {
		/// <field type='FillPatternStyle' >Obsolete. Use None instead.</field>
	default: null,
		/// <field type='FillPatternStyle' >Diagonal crosshatch fill pattern.</field>
	diagonalCrosshatch: null,
		/// <field type='FillPatternStyle' >Diagonal stripe fill pattern.</field>
	diagonalStripe: null,
		/// <field type='FillPatternStyle' >"12.5% gray" fill pattern.</field>
	gray12percent: null,
		/// <field type='FillPatternStyle' >"25% gray" fill pattern.</field>
	gray25percent: null,
		/// <field type='FillPatternStyle' >"50% gray" fill pattern.</field>
	gray50percent: null,
		/// <field type='FillPatternStyle' >"6.25% gray" fill pattern.</field>
	gray6percent: null,
		/// <field type='FillPatternStyle' >"75% gray" fill pattern.</field>
	gray75percent: null,
		/// <field type='FillPatternStyle' >Horizontal stripe fill pattern.</field>
	horizontalStripe: null,
		/// <field type='FillPatternStyle' >No fill pattern.</field>
	none: null,
		/// <field type='FillPatternStyle' >Reverse diagonal stripe fill pattern.</field>
	reverseDiagonalStripe: null,
		/// <field type='FillPatternStyle' >Solid fill pattern with fill pattern foreground color.</field>
	solid: null,
		/// <field type='FillPatternStyle' >Thick diagonal crosshatch fill pattern.</field>
	thickDiagonalCrosshatch: null,
		/// <field type='FillPatternStyle' >Thin diagonal crosshatch fill pattern.</field>
	thinDiagonalCrosshatch: null,
		/// <field type='FillPatternStyle' >Thin diagonal stripe fill pattern.</field>
	thinDiagonalStripe: null,
		/// <field type='FillPatternStyle' >Thin horizontal crosshatch fill pattern.</field>
	thinHorizontalCrosshatch: null,
		/// <field type='FillPatternStyle' >Thin horizontal stripe fill pattern.</field>
	thinHorizontalStripe: null,
		/// <field type='FillPatternStyle' >Thin reverse diagonal stripe fill pattern.</field>
	thinReverseDiagonalStripe: null,
		/// <field type='FillPatternStyle' >Thin vertical stripe fill pattern.</field>
	thinVerticalStripe: null,
		/// <field type='FillPatternStyle' >Vertical stripe fill pattern.</field>
	verticalStripe: null
});

intellisense.annotate(jQuery.ig.excel.DiagonalBorders, function () {
		/// <signature>
		///   <summary>Represents the diagonal borders which can be displayed in cells.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.DiagonalBorders, {
		/// <field type='DiagonalBorders' >Both diagonal borders will be displayed in the cell.</field>
	all: null,
		/// <field type='DiagonalBorders' >The default value for the diagonal borders.</field>
	default: null,
		/// <field type='DiagonalBorders' >A diagonal border going from the top-left to bottom-right corner will be displayed in the cell.</field>
	diagonalDown: null,
		/// <field type='DiagonalBorders' >A diagonal border going from the bottom-left to top-right corner will be displayed in the cell.</field>
	diagonalUp: null,
		/// <field type='DiagonalBorders' >No diagonal borders will be displayed in the cell.</field>
	none: null
});

intellisense.annotate(jQuery.ig.excel.DateSystem, function () {
		/// <signature>
		///   <summary>Represents the various ways a date can be stored internally.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.DateSystem, {
		/// <field type='DateSystem' >Dates are stored as time elapsed since 1900.</field>
	from1900: null,
		/// <field type='DateSystem' >Dates are stored as time elapsed since 1904 (used mainly on Apple computers).</field>
	from1904: null
});

intellisense.annotate(jQuery.ig.excel.CellReferenceMode, function () {
		/// <signature>
		///   <summary>Represents the various ways cells can be referenced in a formula.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.CellReferenceMode, {
		/// <field type='CellReferenceMode' >Cells are referenced by first specifying characters representing the column and a one-based number 
		///     specifying the row (R54 or CA56). The dollar sign ($) can preface one or both identifiers to make them 
		///     absolute references ($A$7). Without the dollar sign, references still use absolute row and column addresses,
		///     although shifting a formula to a new cell will perform a similar shift on all relative references.</field>
	a1: null,
		/// <field type='CellReferenceMode' >Cells are referenced in the following format R&lt;RowIndex&gt;C&lt;ColumnIndex&gt; (R34C5 or R2C345). 
		///     These indices are one-based and represent absolute references. To create a relative reference in R1C1 mode, a relative
		///     index must be placed inside square brackets following the R and/or C ( R[-1]C[5] or R9C[-3] ).  An R by itself 
		///     also represents a relative reference and is equivalent to R[0]. Similarly, C is equivalent to C[0], which means a 
		///     formula of =RC always references the cell which contains the formula.</field>
	r1C1: null
});

intellisense.annotate(jQuery.ig.excel.CellBorderLineStyle, function () {
		/// <signature>
		///   <summary>Represents the different cell border line styles.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.CellBorderLineStyle, {
		/// <field type='CellBorderLineStyle' >Dash-dot border.</field>
	dashDot: null,
		/// <field type='CellBorderLineStyle' >Dash-dot-dot border.</field>
	dashDotDot: null,
		/// <field type='CellBorderLineStyle' >Dashed border.</field>
	dashed: null,
		/// <field type='CellBorderLineStyle' >Use the default border line style.</field>
	default: null,
		/// <field type='CellBorderLineStyle' >Dotted border.</field>
	dotted: null,
		/// <field type='CellBorderLineStyle' >Double-line border.</field>
	double: null,
		/// <field type='CellBorderLineStyle' >Dotted border with small dots.</field>
	hair: null,
		/// <field type='CellBorderLineStyle' >Medium border.</field>
	medium: null,
		/// <field type='CellBorderLineStyle' >Medium dash-dot border.</field>
	mediumDashDot: null,
		/// <field type='CellBorderLineStyle' >Medium dash-dot-dot border.</field>
	mediumDashDotDot: null,
		/// <field type='CellBorderLineStyle' >Dotted border with big dots.</field>
	mediumDashed: null,
		/// <field type='CellBorderLineStyle' >No border.</field>
	none: null,
		/// <field type='CellBorderLineStyle' >Slanted dash-dot border.</field>
	slantedDashDot: null,
		/// <field type='CellBorderLineStyle' >Thick border.</field>
	thick: null,
		/// <field type='CellBorderLineStyle' >Thin border.</field>
	thin: null
});

intellisense.annotate(jQuery.ig.excel.CalculationMode, function () {
		/// <signature>
		///   <summary>Represents the the ways formulas are recalculated when their referenced values change.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.CalculationMode, {
		/// <field type='CalculationMode' >Formulas and data tables are automatically recalculated when the values in referenced cells change.</field>
	automatic: null,
		/// <field type='CalculationMode' >Only formulas are automatically recalculated when the values in referenced cells change.
		///     Data tables must be recalculated manually.</field>
	automaticExceptForDataTables: null,
		/// <field type='CalculationMode' >Formulas must be recalculated manually, by pressing a button in the Microsoft Excel interface.</field>
	manual: null
});

intellisense.annotate(jQuery.ig.excel.TwoConstraintDataValidationOperator, function () {
		/// <signature>
		///   <summary>Represents the various operators which can be used when validating the cell value against two constraints.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.TwoConstraintDataValidationOperator, {
		/// <field type='TwoConstraintDataValidationOperator' >Only allows the cell value if it or its text length, depending on the validation criteria, is between the 
		///     constraints applied to the validation rule. The constraints are inclusive.</field>
	between: null,
		/// <field type='TwoConstraintDataValidationOperator' >Only allows the cell value if it or its text length, depending on the validation criteria, is not between the 
		///     constraints applied to the validation rule. The constraints are exclusive.</field>
	notBetween: null
});

intellisense.annotate(jQuery.ig.excel.OneConstraintDataValidationOperator, function () {
		/// <signature>
		///   <summary>Represents the various operators which can be used when validating the cell value against a constraint.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.OneConstraintDataValidationOperator, {
		/// <field type='OneConstraintDataValidationOperator' >Only allows the cell value if it or its text length, depending on the validation criteria, is equal to the 
		///     constraint applied to the validation rule.</field>
	equalTo: null,
		/// <field type='OneConstraintDataValidationOperator' >Only allows the cell value if it or its text length, depending on the validation criteria, is greater than the 
		///     constraint applied to the validation rule.</field>
	greaterThan: null,
		/// <field type='OneConstraintDataValidationOperator' >Only allows the cell value if it or its text length, depending on the validation criteria, is greater than or equal to the 
		///     constraint applied to the validation rule.</field>
	greaterThanOrEqualTo: null,
		/// <field type='OneConstraintDataValidationOperator' >Only allows the cell value if it or its text length, depending on the validation criteria, is less than the 
		///     constraint applied to the validation rule.</field>
	lessThan: null,
		/// <field type='OneConstraintDataValidationOperator' >Only allows the cell value if it or its text length, depending on the validation criteria, is less than or equal to the 
		///     constraint applied to the validation rule.</field>
	lessThanOrEqualTo: null,
		/// <field type='OneConstraintDataValidationOperator' >Only allows the cell value if it or its text length, depending on the validation criteria, is not equal to the 
		///     constraint applied to the validation rule.</field>
	notEqualTo: null
});

intellisense.annotate(jQuery.ig.excel.DataValidationErrorStyle, function () {
		/// <signature>
		///   <summary>Represents the various styles in which invalid values are handled by Microsoft Excel. When error messages are not 
   ///     shown for invalid values, the error style is ignored and all invalid values are allowed to be set on cells.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.DataValidationErrorStyle, {
		/// <field type='DataValidationErrorStyle' >Invalid values are allowed. The error dialog shown in Microsoft Excel displays an information icon and ok and cancel 
		///     buttons. The ok button commits the value to the cell. The cancel button will cancel the edit and revert the cell back 
		///     to the previous value it contained before the edit operation.</field>
	information: null,
		/// <field type='DataValidationErrorStyle' >Invalid values are not allowed. The error dialog shown in Microsoft Excel displays an error icon and retry and cancel 
		///     buttons. The retry button will put focus back in the cell and allow the user to try to enter a new value. The cancel 
		///     button will cancel the edit and revert the cell back to the previous value it contained before the edit operation.</field>
	stop: null,
		/// <field type='DataValidationErrorStyle' >Invalid values are allowed. The error dialog shown in Microsoft Excel displays a warning icon, asks the user if they 
		///     want to continue, and has yes, no and cancel buttons. The yes button commits the value to the cell. The no button will
		///     put focus back in the cell and allow the user to try to enter a new value. And the cancel button will cancel the edit 
		///     and revert the cell back to the previous value it contained before the edit operation.</field>
	warning: null
});

intellisense.annotate(jQuery.ig.excel.DataValidationCriteria, function () {
		/// <signature>
		///   <summary>Determines what types of cell values are allowed and how the cell value is validated against the constraint(s).</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.DataValidationCriteria, {
		/// <field type='DataValidationCriteria' >Dates with or without time portions are allowed as well as numbers equivalent to valid dates.
		///     When comparing against constraints, the cell value is used.</field>
	date: null,
		/// <field type='DataValidationCriteria' >Only numbers are allowed.
		///     When comparing against constraints, the cell value is used.</field>
	decimal: null,
		/// <field type='DataValidationCriteria' >Non-error values are allowed.
		///     When comparing against constraints, the length of the cell value's text equivalent is used.</field>
	textLength: null,
		/// <field type='DataValidationCriteria' >Times are allowed are well as numbers equivalent to valid times without a date portion.
		///     When comparing against constraints, the cell value is used.</field>
	time: null,
		/// <field type='DataValidationCriteria' >Only numbers are allowed and they cannot have a fractional part.
		///     When comparing against constraints, the cell value is used.</field>
	wholeNumber: null
});

intellisense.annotate(jQuery.ig.excel.ExcelCalcErrorCode, function () {
		/// <signature>
		///   <summary>Enumeration of error codes assigned to ExcelCalcErrorValue.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ExcelCalcErrorCode, {
		/// <field type='ExcelCalcErrorCode' >Occurs when a circularity formula is used when circularities are not allowed.</field>
	circularity: null,
		/// <field type='ExcelCalcErrorCode' >Occurs when a number is divided by zero (0)</field>
	div: null,
		/// <field type='ExcelCalcErrorCode' >Occurs when @NA is entered into a formula</field>
	nA: null,
		/// <field type='ExcelCalcErrorCode' >Occurs when text in a formula is not recognized.</field>
	name: null,
		/// <field type='ExcelCalcErrorCode' >Occurs when there is an intersection of two references that do not contain any common cells.</field>
	null: null,
		/// <field type='ExcelCalcErrorCode' >Occurs with invalid numeric values in a formula or function</field>
	num: null,
		/// <field type='ExcelCalcErrorCode' >Occurs when an invalid or disconnected reference is encountered while evaluating a formula</field>
	reference: null,
		/// <field type='ExcelCalcErrorCode' >Occurs when the wrong type of argument or operand is used in a formula</field>
	value: null
});

intellisense.annotate(jQuery.ig.excel.FirstWeekOfYear, function () {
		/// <signature>
		///   <summary>Indicates the first week of the year to use when calling date-related functions.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.FirstWeekOfYear, {
		/// <field type='FirstWeekOfYear' >The first week that has at least four days in the new year This member is
		///         equivalent to the Visual Basic constant vbFirstFourDays.</field>
	firstFourDays: null,
		/// <field type='FirstWeekOfYear' >The first full week of the year This member is equivalent to the Visual Basic
		///         constant vbFirstFullWeek.</field>
	firstFullWeek: null,
		/// <field type='FirstWeekOfYear' >The week in which January 1 occurs (default) This member is equivalent to
		///         the Visual Basic constant vbFirstJan1.</field>
	jan1: null,
		/// <field type='FirstWeekOfYear' >The day of the week specified in your system settings as the first day of
		///         the week This member is equivalent to the Visual Basic constant vbUseSystem.</field>
	system: null
});

intellisense.annotate(jQuery.ig.excel.DateInterval, function () {
		/// <signature>
		///   <summary>Indicates how to determine and format date intervals when calling date-related functions.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.DateInterval, {
		/// <field type='DateInterval' >Day of month (1 through 31)</field>
	day: null,
		/// <field type='DateInterval' >Day of year (1 through 366)</field>
	dayOfYear: null,
		/// <field type='DateInterval' >Hour (1 through 24)</field>
	hour: null,
		/// <field type='DateInterval' >Minute (1 through 60)</field>
	minute: null,
		/// <field type='DateInterval' >Month (1 through 12)</field>
	month: null,
		/// <field type='DateInterval' >Quarter of year (1 through 4)</field>
	quarter: null,
		/// <field type='DateInterval' >Second (1 through 60)</field>
	second: null,
		/// <field type='DateInterval' >Day of week (1 through 7)</field>
	weekday: null,
		/// <field type='DateInterval' >Week of year (1 through 53)</field>
	weekOfYear: null,
		/// <field type='DateInterval' >Year format</field>
	year: null
});

intellisense.annotate(jQuery.ig.excel.ExcelCalcValue, function () {
		/// <signature>
		///   <summary>Provides methods that manage a composite data type representing a value.</summary>
		///   <param name='value' type='Object' >Object to be represented by the ExcelCalcValue.</param>
		/// </signature>
		/// <signature>
		///   <summary>Provides methods that manage a composite data type representing a value.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ExcelCalcValue, {
	areValuesEqual: function () {
		/// <signature>
		///   <summary>Indicates if the value of the specified ExcelCalcValue is equivalent to the ExcelCalcValue.Value</summary>
		///   <param name='x' type='ExcelCalcValue' >First ExcelCalcValue to compare</param>
		///   <param name='y' type='ExcelCalcValue' >Second ExcelCalcValue to compare</param>
		///   <returns type='Boolean' >True if the values are the same; otherwise false is returned.</returns>
		/// </signature>
	},
	compareTo: function () {
		/// <signature>
		///   <summary>Compares x and y ExcelCalcValue instances and returns -1 if x is less than y, 
   ///     1 if x is greater than y and 0 if x and y are equal.</summary>
		///   <param name='x' type='ExcelCalcValue' >The first value to compare.</param>
		///   <param name='y' type='ExcelCalcValue' >The value to compare with the first value.</param>
		///   <returns type='Number' integer='true' >-1, 1 or 0 depending on whether the current instance is less than, greater than
		///       or equal to the passed in instance respectively.</returns>
		/// </signature>
	},
	dateTimeToExcelDate: function () {
		/// <signature>
		///   <summary>Converts a DateTime to Excel's numerical representation of a date.</summary>
		///   <param name='workbook' type='Workbook'  />
		///   <param name='dateValue' type='Date' >The DateTime value to convert to the Microsoft Excel date format.</param>
		///   <returns type='Number' />
		/// </signature>
		/// <signature>
		///   <summary>Converts a DateTime to Excel's numerical representation of a date.</summary>
		///   <param name='workbook' type='Workbook'  />
		///   <param name='dateValue' type='Date' >The DateTime value to convert to the Microsoft Excel date format.</param>
		///   <param name='shouldCorrect1900Dates' type='Boolean' >When using the 1900 date system in Excel, dates before 3/1/1900 must be corrected, because Excel 
		///       incorrectly assumes 1900 is a leap year. Pass False to disable this correction.</param>
		///   <returns type='Number' />
		/// </signature>
	},
	excelDateToDateTime: function () {
		/// <signature>
		///   <summary>Converts Excel's numerical representation of a date to a DateTime.</summary>
		///   <param name='workbook' type='Workbook'  />
		///   <param name='excelDate' type='Number' >The Microsoft Excel date format which should be converted to a DateTime.</param>
		///   <returns type='Date' />
		/// </signature>
		/// <signature>
		///   <summary>Converts Excel's numerical representation of a date to a DateTime.</summary>
		///   <param name='workbook' type='Workbook'  />
		///   <param name='excelDate' type='Number' >The Microsoft Excel date format which should be converted to a DateTime.</param>
		///   <param name='shouldCorrect1900Dates' type='Boolean' >When using the 1900 date system in Excel, dates before 3/1/1900 must be corrected, because Excel 
		///       incorrectly assumes 1900 is a leap year. Pass False to disable this correction.</param>
		///   <returns type='Date' />
		/// </signature>
	}
});
intellisense.annotate(jQuery.ig.excel.ExcelCalcValue.prototype, {
	compareTo: function () {
		/// <signature>
		///   <summary>Compares current instance with the passed in ExcelCalcValue instance. Returns 
   ///     -1, 1 or 0 depending on whether the current instance is less than, greater than
   ///     or equal to the passed in instance respectively.</summary>
		///   <param name='value' type='ExcelCalcValue' >The object that this instance should be compared against.</param>
		///   <returns type='Number' integer='true' >-1, 1 or 0 depending on whether the current instance is less than, greater than
		///       or equal to the passed in instance respectively.</returns>
		/// </signature>
	},
	getResolvedValue: function () {
		/// <signature>
		///   <summary>Gets the resolved value of the reference. This method will walk down the reference chain recursively to get the resolved value of the reference that is not just another reference.</summary>
		///   <returns type='Object' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the resolved value of the reference. This method will walk down the reference chain recursively to get the resolved value of the reference that is not just another reference.</summary>
		///   <param name='willUseValueInCalculations' type='Boolean' >True if the value returned will actually be used in calculations; False if the value is being requested to inspect some aspect of it, such as its type.</param>
		///   <returns type='Object' />
		/// </signature>
	},
	isArray: function () {
		/// <signature>
		///   <summary>Returns whether this class instance contains an array value.</summary>
		///   <returns type='Boolean' >True if this instance class contains an array, else false.</returns>
		/// </signature>
	},
	isArrayGroup: function () {
		/// <signature>
		///   <summary>Returns whether this class instance contains a group of array values.</summary>
		///   <returns type='Boolean' >True if this instance class contains an array group, else false.</returns>
		/// </signature>
	},
	isBoolean: function () {
		/// <signature>
		///   <summary>Returns whether this class instance contains a boolean value</summary>
		///   <returns type='Boolean' >True if this instance class contains a boolean, else false</returns>
		/// </signature>
	},
	isDateTime: function () {
		/// <signature>
		///   <summary>Returns whether this class instance contains a DateTime value</summary>
		///   <returns type='Boolean' >True if this instance class contains a DateTime, else false</returns>
		/// </signature>
	},
	isDBNull: function () {
		/// <signature>
		///   <summary>Returns whether this class instance contains a DBNull value</summary>
		///   <returns type='Boolean' >True if this instance class contains a null value, else false</returns>
		/// </signature>
	},
	isError: function () {
		/// <signature>
		///   <summary>Returns whether this class instance contains an error value</summary>
		///   <returns type='Boolean' >True if this class instance contains an error, else false</returns>
		/// </signature>
	},
	isNull: function () {
		/// <signature>
		///   <summary>Returns whether this class instance contains a null value</summary>
		///   <returns type='Boolean' >True if this instance class contains a null value, else false</returns>
		/// </signature>
	},
	isReference: function () {
		/// <signature>
		///   <summary>Returns whether this class instance contains a IExcelCalcReference value</summary>
		///   <returns type='Boolean' >True if this instance class contains a reference, else false</returns>
		/// </signature>
	},
	isSameValue: function () {
		/// <signature>
		///   <summary>Indicates if the specified ExcelCalcValue has the save ExcelCalcValue.Value as this instance.</summary>
		///   <param name='value' type='ExcelCalcValue' >ExcelCalcValue to compare against.</param>
		///   <returns type='Boolean' >True if the ExcelCalcValue.Value of both instances are equal; otherwise false.</returns>
		/// </signature>
	},
	isString: function () {
		/// <signature>
		///   <summary>Returns whether this class instance contains a string value</summary>
		///   <returns type='Boolean' >True if this instance class contains a string, else false</returns>
		/// </signature>
	},
	toArrayProxy: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to an ArrayProxy data type.</summary>
		///   <returns type='ArrayProxy' >An array equivalent to the value of this instance.</returns>
		/// </signature>
	},
	toArrayProxyGroup: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to an array group data type.</summary>
		///   <returns type='Array' elementType='ArrayProxy' >An array group equivalent to the value of this instance.</returns>
		/// </signature>
	},
	toBoolean: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to a boolean data type</summary>
		///   <returns type='Boolean' >A boolean floating-point equivalent to the value of this instance</returns>
		/// </signature>
		/// <signature>
		///   <summary>Convert this class instance's value to a boolean data type</summary>
		///   <param name='provider' type='IFormatProvider' >An IFormatProvider interface implementation that supplies culture-specific formatting information</param>
		///   <returns type='Boolean' >A boolean floating-point equivalent to the value of this instance</returns>
		/// </signature>
	},
	toByte: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to a byte data type</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Convert this class instance's value to a byte data type</summary>
		///   <param name='provider' type='IFormatProvider' >An IFormatProvider interface implementation that supplies culture-specific formatting information</param>
		///   <returns type='Number' integer='true' >A 8-bit unsigned integer equivalent to the value of this instance</returns>
		/// </signature>
	},
	toChar: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to a char data type</summary>
		///   <returns type='String' >A Unicode character equivalent to the value of this instance</returns>
		/// </signature>
		/// <signature>
		///   <summary>Convert this class instance's value to a char type</summary>
		///   <param name='provider' type='IFormatProvider' >An IFormatProvider interface implementation that supplies culture-specific formatting information</param>
		///   <returns type='String' >A Unicode character equivalent to the value of this instance</returns>
		/// </signature>
	},
	toDateTime: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to a DateTime data type</summary>
		///   <returns type='Date' />
		/// </signature>
		/// <signature>
		///   <summary>Convert this class instance's value to a DateTime data type</summary>
		///   <param name='provider' type='IFormatProvider' >An IFormatProvider interface implementation that supplies culture-specific formatting information</param>
		///   <returns type='Date' >A DateTime equivalent to the value of this instance</returns>
		/// </signature>
	},
	toDecimal: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to a decimal data type</summary>
		///   <returns type='Number' >A Decimal equivalent to the value of this instance</returns>
		/// </signature>
		/// <signature>
		///   <summary>Convert this class instance's value to a decimal data type</summary>
		///   <param name='provider' type='IFormatProvider' >An IFormatProvider interface implementation that supplies culture-specific formatting information</param>
		///   <returns type='Number' >A Decimal equivalent to the value of this instance</returns>
		/// </signature>
	},
	toDouble: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to a double data type</summary>
		///   <returns type='Number' />
		/// </signature>
		/// <signature>
		///   <summary>Convert this class instance's value to a double data type</summary>
		///   <param name='provider' type='IFormatProvider' >An IFormatProvider interface implementation that supplies culture-specific formatting information</param>
		///   <returns type='Number' >A double-precision floating-point equivalent to the value of this instance</returns>
		/// </signature>
	},
	toErrorValue: function () {
		/// <signature>
		///   <summary>Convert this instance's value to an ExcelCalcErrorValue</summary>
		///   <returns type='ExcelCalcErrorValue' >A ExcelCalcErrorValue containing the equivalent error code to the value of this instance</returns>
		/// </signature>
	},
	toInt: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to an int</summary>
		///   <returns type='Number' integer='true' >A signed 32-bit integer value containing the equivalent value of this instance</returns>
		/// </signature>
		/// <signature>
		///   <summary>Convert this class instance's value to an int</summary>
		///   <param name='provider' type='IFormatProvider' >An IFormatProvider interface implementation that supplies culture-specific formatting information</param>
		///   <returns type='Number' integer='true' >A signed 32-bit integer value containing the equivalent value of this instance</returns>
		/// </signature>
	},
	toInt16: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to a short data type</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Convert this class instance's value to a short data type</summary>
		///   <param name='provider' type='IFormatProvider' >An IFormatProvider interface implementation that supplies culture-specific formatting information</param>
		///   <returns type='Number' integer='true' >A 16-bit signed integer floating-point equivalent to the value of this instance</returns>
		/// </signature>
	},
	toInt32: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to a int data type</summary>
		///   <returns type='Number' integer='true' >A 32-bit signed integer floating-point equivalent to the value of this instance</returns>
		/// </signature>
		/// <signature>
		///   <summary>Convert this class instance's value to a int data type</summary>
		///   <param name='provider' type='IFormatProvider' >An IFormatProvider interface implementation that supplies culture-specific formatting information</param>
		///   <returns type='Number' integer='true' >A 32-bit signed integer floating-point equivalent to the value of this instance</returns>
		/// </signature>
	},
	toInt64: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to a long data type</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Convert this class instance's value to a long data type</summary>
		///   <param name='provider' type='IFormatProvider' >An IFormatProvider interface implementation that supplies culture-specific formatting information</param>
		///   <returns type='Number' integer='true' >A 64-bit signed integer equivalent to the value of this instance</returns>
		/// </signature>
	},
	toReference: function () {
		/// <signature>
		///   <summary>Convert this instance's value to a IExcelCalcReference.</summary>
		///   <returns type='IExcelCalcReference' >If this instance contains a object that implements the IExcelCalcReference interface, this method returns the object instance, else an error reference is returned.</returns>
		/// </signature>
	},
	toSingle: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to a float data type</summary>
		///   <returns type='Number' />
		/// </signature>
		/// <signature>
		///   <summary>Convert this class instance's value to a float data type</summary>
		///   <param name='provider' type='IFormatProvider' >An IFormatProvider interface implementation that supplies culture-specific formatting information</param>
		///   <returns type='Number' >A single-precision floating-point equivalent to the value of this instance</returns>
		/// </signature>
	},
	toString: function () {
		/// <signature>
		///   <summary>Returns a string representation of this instance's value.</summary>
		///   <returns type='String' >String representation of instance's value</returns>
		/// </signature>
	},
	toString1: function () {
		/// <signature>
		///   <summary>Convert this class instance's value to a string instance data type</summary>
		///   <param name='provider' type='IFormatProvider' >An IFormatProvider interface implementation that supplies culture-specific formatting information</param>
		///   <returns type='String' >A string instance equivalent to the value of this instance</returns>
		/// </signature>
	},
	value: function () {
		/// <signature>
		///   <summary>Return the underlying value</summary>
		///   <returns type='Object' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ExcelCalcFunction, function () {
		/// <signature>
		///   <summary>Base class for formula functions.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ExcelCalcFunction.prototype, {
	canParameterBeEnumerable: function () {
		/// <signature>
		///   <summary>Determines whether the parameter at the specified index will accept an enumerable reference.</summary>
		///   <param name='parameterIndex' type='Number' integer='true' >In 0-based index of the parameter.</param>
		///   <returns type='Boolean' >True if the parameter at the specified index can accept enumerable references; False otherwise or if the parameter is out of range for this function.</returns>
		/// </signature>
	},
	doesParameterAllowIntermediateResultArray: function () {
		/// <signature>
		///   <summary>Determines whether the function accepts an intermediate result array created by evaluating a nested function
   ///     on a per-element basis of an array or region parameter when a single value is expected.</summary>
		///   <param name='parameterIndex' type='Number' integer='true' >The 0-based index of parameter in which the intermediate array will be passed.</param>
		///   <param name='isCreatedFromRegionReference' type='Boolean' >True if the value which will generate the intermediate result array is a region reference; False if it is a constant array.</param>
		///   <returns type='Boolean' >True to allow intermediate result arrays for the specified parameter; False otherwise.</returns>
		/// </signature>
	},
	evaluate: function () {
		/// <signature>
		///   <summary>Evaluates the function against the arguments on the number stack.</summary>
		///   <param name='numberStack' type='ExcelCalcNumberStack' >Formula number stack containing function arguments.</param>
		///   <param name='argumentCount' type='Number' integer='true' >Denotes the number of function arguments pushed onto the number stack.</param>
		///   <returns type='ExcelCalcValue' >An ExcelCalcValue that represents the result of the function evaluation.</returns>
		/// </signature>
	},
	getArguments: function () {
		/// <signature>
		///   <summary>A helper method for extracting the ExcelCalcValue instances from the stack.</summary>
		///   <param name='numberStack' type='ExcelCalcNumberStack' >Number stack whose values should be popped.</param>
		///   <param name='argumentCount' type='Number' integer='true' >Number of items to pop/evaluate from the number stack</param>
		///   <param name='skipEmptyValues' type='Boolean' >True to ignore values whose IsNull returns true; otherwise false to include empty items in the list.</param>
		///   <returns type='Array' elementType='ExcelCalcValue' >An array of ExcelCalcValue instances removed from the number stack.</returns>
		/// </signature>
		/// <signature>
		///   <summary>A helper method for extracting the ExcelCalcValue instances from the stack.</summary>
		///   <param name='numberStack' type='ExcelCalcNumberStack' >Number stack whose values should be popped.</param>
		///   <param name='argumentCount' type='Number' integer='true' >Number of items to pop/evaluate from the number stack</param>
		///   <param name='skipEmptyValues' type='Boolean' >True to ignore values whose IsNull returns true; otherwise false to include empty items in the list.</param>
		///   <param name='skipHiddenCells' type='Boolean' >True to ignore values from hidden cells.</param>
		///   <returns type='Array' elementType='ExcelCalcValue' >An array of UltraCalcValue instances removed from the number stack.</returns>
		/// </signature>
	},
	maxArgs: function () {
		/// <signature>
		///   <summary>Maximum number of arguments required for the function</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	minArgs: function () {
		/// <signature>
		///   <summary>Minimum number of arguments required for the function</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	name: function () {
		/// <signature>
		///   <summary>Function name used to reference the function in a formula</summary>
		///   <returns type='String' />
		/// </signature>
	},
	performEvaluation: function () {
		/// <signature>
		///   <summary>Evaluates the function against the arguments on the number stack.</summary>
		///   <param name='numberStack' type='ExcelCalcNumberStack' >Formula number stack containing function arguments.</param>
		///   <param name='argumentCount' type='Number' integer='true' >Denotes the number of function arguments pushed onto the number stack.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ExcelCalcNumberStack, function () {
		/// <signature>
		///   <summary>Stack of ExcelCalcValue instances used to evaluate formulas.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ExcelCalcNumberStack.prototype, {
	clear: function () {
		/// <signature>
		///   <summary>Remove all values from number stack.</summary>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Return the number of values on number stack</summary>
		///   <returns type='Number' integer='true' >Number of stack values</returns>
		/// </signature>
	},
	owningCell: function () {
		/// <signature>
		///   <summary>Gets the WorksheetCell instance whose formula is currently being solved.</summary>
		///   <returns type='WorksheetCell' />
		/// </signature>
	},
	peek: function () {
		/// <signature>
		///   <summary>Return value off top of the number stack.</summary>
		///   <returns type='ExcelCalcValue' >ExcelCalcValue that is at the top of the number stack.</returns>
		/// </signature>
	},
	pop: function () {
		/// <signature>
		///   <summary>Pop value off top of the number stack.</summary>
		///   <returns type='ExcelCalcValue' >ExcelCalcValue that was at the top of the number stack.</returns>
		/// </signature>
	},
	push: function () {
		/// <signature>
		///   <summary>Push a value onto number stack.</summary>
		///   <param name='value' type='ExcelCalcValue' >Value to push onto the number stack</param>
		/// </signature>
	},
	reset: function () {
		/// <signature>
		///   <summary>Clear elements off top of number stack until it contains given number of elements</summary>
		///   <param name='elements' type='Number' integer='true' >Denotes the desired stack level</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ArrayProxy, function () {
		/// <signature>
		///   <summary>Represents a two-dimensional array of ExcelCalcValue instances used in calculations.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ArrayProxy.prototype, {
	getLength: function () {
		/// <signature>
		///   <summary>Gets the number of elements in the specified dimension of the array.</summary>
		///   <param name='dimension' type='Number' integer='true' >The zero-based index of the dimension.</param>
		///   <returns type='Number' integer='true' >The number of elements in the specified dimension.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ExcelCalcErrorValue, function () {
		/// <signature>
		///   <summary>Provides methods and properties used to define and manage a calculation error value.</summary>
		///   <param name='code' type='ExcelCalcErrorCode' >ExcelCalcErrorCode value to assign this instance</param>
		/// </signature>
		/// <signature>
		///   <summary>Provides methods and properties used to define and manage a calculation error value.</summary>
		///   <param name='code' type='ExcelCalcErrorCode' >ExcelCalcErrorCode value</param>
		///   <param name='message' type='String' >Localized Message indicating reason for error</param>
		/// </signature>
		/// <signature>
		///   <summary>Provides methods and properties used to define and manage a calculation error value.</summary>
		///   <param name='code' type='ExcelCalcErrorCode' >ExcelCalcErrorCode value</param>
		///   <param name='message' type='String' >Localized Message indicating reason for error</param>
		///   <param name='value' type='Object' >Value associated with error</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ExcelCalcErrorValue.prototype, {
	code: function () {
		/// <signature>
		///   <summary>Gets the error code for this class instance</summary>
		///   <returns type='ExcelCalcErrorCode' />
		/// </signature>
	},
	errorValue: function () {
		/// <signature>
		///   <summary>Gets the error object for this class instance</summary>
		///   <returns type='Object' />
		/// </signature>
	},
	message: function () {
		/// <signature>
		///   <summary>Gets error message for this class instance. Note that when setting this property the 
   ///     message is assumed to be localized.</summary>
		///   <returns type='String' />
		/// </signature>
	},
	toString: function () {
		/// <signature>
		///   <summary>Return a string message that denotes reason for error</summary>
		///   <returns type='String' >String containing error message</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.IExcelCalcFormula, function () {
	/// <summary>Interface implemented by the formula object.</summary>
});
intellisense.annotate(jQuery.ig.excel.IExcelCalcFormula.prototype, {
	addDynamicReferenceI: function () {
		/// <signature>
		///   <summary>Adds a reference created during the evaluation of the formula to the references of the formula.</summary>
		///   <param name='reference' type='IExcelCalcReference' >The reference to add to the formula's dynamic references collection.</param>
		///   <returns type='Boolean' />
		/// </signature>
	},
	baseReference: function () {
		/// <signature>
		///   <summary>Base reference associated with the formula.</summary>
		///   <returns type='IExcelCalcReference' />
		/// </signature>
	},
	dynamicReferences: function () {
		/// <signature>
		///   <summary>Gets the collection of reference created during the last evaluation of the formula.</summary>
		///   <returns type='IExcelCalcReferenceCollection' />
		/// </signature>
	},
	evaluate: function () {
		/// <signature>
		///   <summary>Evaluate the compiled expression against the given base reference</summary>
		///   <param name='reference' type='IExcelCalcReference' >Base reference used to resolve relative references into absolute references</param>
		///   <returns type='ExcelCalcValue' >Retuns an ExcelCalcValue containing result of formula evaluation</returns>
		/// </signature>
	},
	formulaString: function () {
		/// <signature>
		///   <summary>Formula string.</summary>
		///   <returns type='String' />
		/// </signature>
	},
	hasAlwaysDirty: function () {
		/// <signature>
		///   <summary>Returns whether the formula contains an always dirty function.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	staticReferences: function () {
		/// <signature>
		///   <summary>Retuns a collection of references contained in the formula token string</summary>
		///   <returns type='IExcelCalcReferenceCollection' >Collection of IExcelCalcReferences</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ArithmeticException, function () {
		/// <signature>
		///   <summary>Represents an arithmetic error.</summary>
		///   <param name='message' type='String' optional='true' >The message describing the error.</param>
		///   <param name='innerException' type='Error' optional='true' >The nested error, if any, which caused this error.</param>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.DivideByZeroException, function () {
		/// <signature>
		///   <summary>Represents an error in which a division by zero occurred during calculations.</summary>
		///   <param name='message' type='String' optional='true' >The message describing the error.</param>
		///   <param name='innerException' type='Error' optional='true' >The nested error, if any, which caused this error.</param>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.InvalidCastException, function () {
		/// <signature>
		///   <summary>Represents an error in which an attempt was made to treat a value as the wrong type.</summary>
		///   <param name='message' type='String' optional='true' >The message describing the error.</param>
		///   <param name='innerException' type='Error' optional='true' >The nested error, if any, which caused this error.</param>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.KeyNotFoundException, function () {
		/// <signature>
		///   <summary>Represents an error in which a keyed collection was accessed with a key which does not exist in the collection.</summary>
		///   <param name='message' type='String' optional='true' >The message describing the error.</param>
		///   <param name='innerException' type='Error' optional='true' >The nested error, if any, which caused this error.</param>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.IOException, function () {
		/// <signature>
		///   <summary>Represents an error in I/O processing.</summary>
		///   <param name='message' type='String' optional='true' >The message describing the error.</param>
		///   <param name='innerException' type='Error' optional='true' >The nested error, if any, which caused this error.</param>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.EndOfStreamException, function () {
		/// <signature>
		///   <summary>Represents an error in which an attempt was made to read past the end of a stream of data.</summary>
		///   <param name='message' type='String' optional='true' >The message describing the error.</param>
		///   <param name='innerException' type='Error' optional='true' >The nested error, if any, which caused this error.</param>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.Workbook, function () {
		/// <signature>
		///   <summary>Represents a Microsoft Excel workbook.</summary>
		/// </signature>
		/// <signature>
		///   <summary>Represents a Microsoft Excel workbook.</summary>
		///   <param name='format' type='WorkbookFormat' >The file format to use when imposing format restrictions and saving.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.Workbook, {
	getMaxColumnCount: function () {
		/// <signature>
		///   <summary>Returns the number of columns that are supported by the specified format.</summary>
		///   <param name='format' type='WorkbookFormat' >The format used by the workbook.</param>
		///   <returns type='Number' integer='true' >The maximum number of columns supported by the format.</returns>
		/// </signature>
	},
	getMaxRowCount: function () {
		/// <signature>
		///   <summary>Returns the number of rows that are supported by the specified format.</summary>
		///   <param name='format' type='WorkbookFormat' >The format used by the workbook.</param>
		///   <returns type='Number' integer='true' >The maximum number of rows supported by the format.</returns>
		/// </signature>
	},
	getWorkbookFormat: function () {
		/// <signature>
		///   <summary>Returns the WorkbookFormat based on the file extension of the specified file.</summary>
		///   <param name='fileName' type='String' >The filename of an excel file.</param>
		///   <returns type='WorkbookFormat' >The workbook format based on the file extension of the file, or null if the correct format cannot be determined.</returns>
		/// </signature>
	},
	isWorkbookEncrypted: function () {
		/// <signature>
		///   <summary>Determines whether the workbook in the specified stream is encrypted with an open password.</summary>
		///   <param name='stream' type='Uint8Array|String' >The stream to check for encryption.</param>
		///   <returns type='Boolean' >True if the stream contains a workbook which is encrypted; False otherwise</returns>
		/// </signature>
	},
	load: function () {
		/// <signature>
		///   <summary>Loads a workbook from a Uint8Array or base64 encoded string.</summary>
		///   <param name='data' type='Uint8Array|String' >The data from which to load the workbook.</param>
		///   <param name='successCallback' type='Function' >The function to call when the Workbook has been successfully loaded.</param>
		///   <param name='failCallback' type='Function' >The function to call when loading the Workbook has resulted in an error.</param>
		/// </signature>
		/// <signature>
		///   <summary>Loads a workbook from a Uint8Array or base64 encoded string.</summary>
		///   <param name='data' type='Uint8Array|String' >The data from which to load the workbook.</param>
		///   <param name='loadOptions' type='WorkbookLoadOptions' >The options to use to load the stream or null to use the default options.</param>
		///   <param name='successCallback' type='Function' >The function to call when the Workbook has been successfully loaded.</param>
		///   <param name='failCallback' type='Function' >The function to call when loading the Workbook has resulted in an error.</param>
		/// </signature>
	},
		/// <field type='Number' integer='true' >Maximum number of distinct cell formats in the workbook allowed by the Excel 2007 file format.</field>
	maxExcel2007CellFormatCount: null,
		/// <field type='Number' integer='true' >Maximum number of columns in the worksheet allowed by the Excel 2007 file format.</field>
	maxExcel2007ColumnCount: null,
		/// <field type='Number' integer='true' >Maximum number of rows in the worksheet allowed by the Excel 2007 file format.</field>
	maxExcel2007RowCount: null,
		/// <field type='Number' integer='true' >Maximum number of distinct cell formats in the workbook allowed by the Excel 97-2003 file format.</field>
	maxExcelCellFormatCount: null,
		/// <field type='Number' integer='true' >Maximum number of columns in the worksheet allowed by the Excel 97-2003 file format.</field>
	maxExcelColumnCount: null,
		/// <field type='Number' integer='true' >Maximum number of rows in the worksheet allowed by the Excel 97-2003 file format.</field>
	maxExcelRowCount: null,
		/// <field type='Number' integer='true' >Maximum number fonts in a workbook allowed by Excel.</field>
	maxExcelWorkbookFonts: null
});
intellisense.annotate(jQuery.ig.excel.Workbook.prototype, {
	calculationMode: function () {
		/// <signature>
		///   <summary>Gets the value which indicates how a formula will be recalculated when a referenced value changes.</summary>
		///   <returns type='CalculationMode' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates how a formula will be recalculated when a referenced value changes.</summary>
		///   <param name='value' type='CalculationMode'  />
		///   <returns type='CalculationMode' />
		/// </signature>
	},
	cellReferenceMode: function () {
		/// <signature>
		///   <summary>Gets the value which indicates the way cells in the workbook are referenced.</summary>
		///   <returns type='CellReferenceMode' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates the way cells in the workbook are referenced.</summary>
		///   <param name='value' type='CellReferenceMode'  />
		///   <returns type='CellReferenceMode' />
		/// </signature>
	},
	characterWidth256thsToPixels: function () {
		/// <signature>
		///   <summary>Converts units of 1/256s of the average character width to pixels.</summary>
		///   <param name='characterWidth256ths' type='Number' >The number of units of 1/256s of the average character width.</param>
		///   <returns type='Number' >The number of pixels equivalent to the characterWidth256ths value.</returns>
		/// </signature>
	},
	clearConnectionData: function () {
		/// <signature>
		///   <summary>Clears all external data connections from the Workbook.</summary>
		/// </signature>
	},
	clearPivotTableData: function () {
		/// <signature>
		///   <summary>Clears all pivot tables and associated slicers from the Workbook.</summary>
		/// </signature>
	},
	createNewWorkbookFont: function () {
		/// <signature>
		///   <summary>Factory method which creates new workbook font.</summary>
		///   <returns type='IWorkbookFont' >The created excel font object.</returns>
		/// </signature>
	},
	createNewWorksheetCellFormat: function () {
		/// <signature>
		///   <summary>Creates new worksheet cell format.</summary>
		///   <returns type='IWorksheetCellFormat' >The cell format which was created.</returns>
		/// </signature>
	},
	culture: function () {
		/// <signature>
		///   <summary>Gets the culture to use as the current culture for the workbook when doing any culture-aware conversions 
   ///     or comparisons.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the culture to use as the current culture for the workbook when doing any culture-aware conversions 
   ///     or comparisons.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	currentFormat: function () {
		/// <signature>
		///   <summary>Gets the current format of the workbook. This is the format which will be used when saving and imposing format restrictions.</summary>
		///   <returns type='WorkbookFormat' />
		/// </signature>
	},
	customTableStyles: function () {
		/// <signature>
		///   <summary>Gets the collection of custom table styles in the workbook.</summary>
		///   <returns type='CustomTableStyleCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the WorksheetTableStyle at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The index at which to get the WorksheetTableStyle.</param>
		///   <returns type='WorksheetTableStyle' >The WorksheetTableStyle instance at the specified index.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the WorksheetTableStyle with the specified name.</summary>
		///   <param name='name' type='String' >The name of the WorksheetTableStyle to find.</param>
		///   <returns type='WorksheetTableStyle' >The WorksheetTableStyle instance with the specified name or null if a table style with that name does not exist.</returns>
		/// </signature>
	},
	customViews: function () {
		/// <signature>
		///   <summary>Gets the collection of custom views for the workbook.</summary>
		///   <returns type='CustomViewCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the custom view at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the custom view to get.</param>
		///   <returns type='CustomView' />
		/// </signature>
	},
	dateSystem: function () {
		/// <signature>
		///   <summary>Gets the date system used internally by Microsoft Excel.</summary>
		///   <returns type='DateSystem' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the date system used internally by Microsoft Excel.</summary>
		///   <param name='value' type='DateSystem'  />
		///   <returns type='DateSystem' />
		/// </signature>
	},
	defaultTableStyle: function () {
		/// <signature>
		///   <summary>Gets the default style for tables in the workbook.</summary>
		///   <returns type='WorksheetTableStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the default style for tables in the workbook.</summary>
		///   <param name='value' type='WorksheetTableStyle'  />
		///   <returns type='WorksheetTableStyle' />
		/// </signature>
	},
	documentProperties: function () {
		/// <signature>
		///   <summary>Gets the properties associated with the workbook document.</summary>
		///   <returns type='DocumentProperties' />
		/// </signature>
	},
	getTable: function () {
		/// <signature>
		///   <summary>Gets the table with the specified name.</summary>
		///   <param name='name' type='String' >The name of the table to get.</param>
		///   <returns type='WorksheetTable' >A WorksheetTable instance if a table exists with the specified name; Otherwise null.</returns>
		/// </signature>
	},
	hasProtectionPassword: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the Workbook has been protected with a password.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	isProtected: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the Workbook has been protected.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	isSaving: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the Workbook is currently being saved.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	isValidFunctionName: function () {
		/// <signature>
		///   <summary>Gets a value indicating whether the specified function will be recognized and solved by Microsoft Excel when the workbook is saved out.</summary>
		///   <param name='functionName' type='String' >The case-insensitive name of the function.</param>
		///   <returns type='Boolean' >True if the function will be recognized in Microsoft Excel; False otherwise.</returns>
		/// </signature>
	},
	iterativeCalculationsEnabled: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether iterations are allowed while calculating formulas containing 
   ///     circular references.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether iterations are allowed while calculating formulas containing 
   ///     circular references.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	maxChangeInIteration: function () {
		/// <signature>
		///   <summary>Gets the maximum change of the values in a formula between iterations which will exit from iteration.</summary>
		///   <returns type='Number' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the maximum change of the values in a formula between iterations which will exit from iteration.</summary>
		///   <param name='value' type='Number'  />
		///   <returns type='Number' />
		/// </signature>
	},
	maxColumnCount: function () {
		/// <signature>
		///   <summary>Gets the maximum number of columns allowed in each worksheet based on the Workbook.CurrentFormat.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	maxRecursionIterations: function () {
		/// <signature>
		///   <summary>Gets the maximum number of times formulas should be iteratively calculated.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the maximum number of times formulas should be iteratively calculated.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	maxRowCount: function () {
		/// <signature>
		///   <summary>Gets the maximum number of rows allowed in each worksheet based on the Workbook.CurrentFormat.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	namedReferences: function () {
		/// <signature>
		///   <summary>Gets the collection of named references in the workbook.</summary>
		///   <returns type='NamedReferenceCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the named reference at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the named reference to get.</param>
		///   <returns type='NamedReference' />
		/// </signature>
	},
	palette: function () {
		/// <signature>
		///   <summary>Gets the color palette used when the saved file is opened in Microsoft Excel 2003 and earlier versions.</summary>
		///   <returns type='WorkbookColorPalette' />
		/// </signature>
		/// <signature>
		///   <summary>Gets a color in the palette.</summary>
		///   <param name='index' type='Number' integer='true' >The index of the color to get or set in the palette.</param>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets a color in the palette.</summary>
		///   <param name='index' type='Number' integer='true' >The index of the color to get or set in the palette.</param>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	pixelsToCharacterWidth256ths: function () {
		/// <signature>
		///   <summary>Converts pixels to units of 1/256s of the average character width.</summary>
		///   <param name='pixels' type='Number' >The number of pixels.</param>
		///   <returns type='Number' >The number of units of 1/256s of the average character width equivalent to the pixels value.</returns>
		/// </signature>
	},
	precision: function () {
		/// <signature>
		///   <summary>Gets the precision to use when obtaining a cell's value.</summary>
		///   <returns type='Precision' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the precision to use when obtaining a cell's value.</summary>
		///   <param name='value' type='Precision'  />
		///   <returns type='Precision' />
		/// </signature>
	},
	protect: function () {
		/// <signature>
		///   <summary>Protects the Workbook without a password.</summary>
		///   <param name='allowEditStructure' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorkbookProtection.AllowEditStructure.</param>
		///   <param name='allowEditWindows' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorkbookProtection.AllowEditWindows.</param>
		/// </signature>
	},
	protection: function () {
		/// <signature>
		///   <summary>Returns an object that provides information used when the Workbook has been protected.</summary>
		///   <returns type='WorkbookProtection' />
		/// </signature>
	},
	recalculate: function () {
		/// <signature>
		///   <summary>Recalculates all dirty formulas pending a calculation on the workbook.</summary>
		/// </signature>
		/// <signature>
		///   <summary>Recalculates all formulas on the workbook.</summary>
		///   <param name='includeNonDirtyFormulas' type='Boolean' >True to recalculate all formulas on the workbook regardless of whether they had a pending evaluation. False to only calculate dirty formulas.</param>
		/// </signature>
	},
	recalculateBeforeSave: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the workbook should recalculate all formulas before saving.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the workbook should recalculate all formulas before saving.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	registerUserDefinedFunction: function () {
		/// <signature>
		///   <summary>Registers a single ExcelCalcFunction instance.</summary>
		///   <param name='userDefinedFunction' type='ExcelCalcFunction' >User defined function instance to register</param>
		///   <returns type='Boolean' >Returns true if the type was registered successfully, else false if the registration failed</returns>
		/// </signature>
	},
	resumeCalculations: function () {
		/// <signature>
		///   <summary>Resumes the calculation of formulas.</summary>
		/// </signature>
	},
	save: function () {
		/// <signature>
		///   <summary>Writes the workbook to a Uint8Array.</summary>
		///   <param name='successCallback' type='Function' >The function to call when the Workbook has been successfully saved into a Uint8Array or base-64 encoded string.</param>
		///   <param name='failCallback' type='Function' >The function to call when saving the Workbook has caused an error.</param>
		/// </signature>
		/// <signature>
		///   <summary>Writes the workbook to a Uint8Array.</summary>
		///   <param name='saveOptions' type='WorkbookSaveOptions' >The options to use to save the stream or null to use the default options.</param>
		///   <param name='successCallback' type='Function' >The function to call when the Workbook has been successfully saved into a Uint8Array or base-64 encoded string.</param>
		///   <param name='failCallback' type='Function' >The function to call when saving the Workbook has caused an error.</param>
		/// </signature>
	},
	saveExternalLinkedValues: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether to save values linked from external workbooks.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether to save values linked from external workbooks.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	screenDpi: function () {
		/// <signature>
		///   <summary>Gets the Dpi to use when calculating row and column sizes for the workbook. If empty, the system Dpi will be used.</summary>
		///   <returns type='Object' > This will be an object with numeric values for properties 'width' and 'height'.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Sets the Dpi to use when calculating row and column sizes for the workbook. If empty, the system Dpi will be used.</summary>
		///   <param name='value' type='Object' > This can be an object with numeric values for properties 'width' and 'height', such as { width: 1, height: 2 }.</param>
		///   <returns type='Object' > This will be an object with numeric values for properties 'width' and 'height'.</returns>
		/// </signature>
	},
	setCurrentFormat: function () {
		/// <signature>
		///   <summary>Sets the current format of the workbook.</summary>
		///   <param name='format' type='WorkbookFormat' >The file format to use when imposing format restrictions and saving.</param>
		/// </signature>
	},
	shouldRemoveCarriageReturnsOnSave: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether carriage return characters should be removed from string values in cells
   ///     when the workbook is saved to an Excel file.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether carriage return characters should be removed from string values in cells
   ///     when the workbook is saved to an Excel file.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	standardTableStyles: function () {
		/// <signature>
		///   <summary>Gets the read-only collection of preset table styles in the workbook.</summary>
		///   <returns type='StandardTableStyleCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the WorksheetTableStyle at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The index at which to get the WorksheetTableStyle.</param>
		///   <returns type='WorksheetTableStyle' >The WorksheetTableStyle instance at the specified index.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the WorksheetTableStyle with the specified name.</summary>
		///   <param name='name' type='String' >The name of the WorksheetTableStyle to find.</param>
		///   <returns type='WorksheetTableStyle' >The WorksheetTableStyle instance with the specified name or null if a table style with that name does not exist.</returns>
		/// </signature>
	},
	styles: function () {
		/// <signature>
		///   <summary>Gets the collection of custom styles in the workbook.</summary>
		///   <returns type='WorkbookStyleCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the style at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the style to get.</param>
		///   <returns type='WorkbookStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the style with the specified name.</summary>
		///   <param name='name' type='String' >The name of the style to get.</param>
		///   <returns type='WorkbookStyle' />
		/// </signature>
	},
	suspendCalculations: function () {
		/// <signature>
		///   <summary>Temporarily suspends the calculation of formulas.</summary>
		/// </signature>
	},
	unprotect: function () {
		/// <signature>
		///   <summary>Removes the Workbook protection.</summary>
		/// </signature>
	},
	validateFormatStrings: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the format strings should be validated when they are set.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the format strings should be validated when they are set.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	windowOptions: function () {
		/// <signature>
		///   <summary>Gets the options which control various workbook level display properties.</summary>
		///   <returns type='WorkbookWindowOptions' />
		/// </signature>
	},
	worksheets: function () {
		/// <signature>
		///   <summary>Gets the collection of worksheets in the workbook.</summary>
		///   <returns type='WorksheetCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the worksheet at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the worksheet to get.</param>
		///   <returns type='Worksheet' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the worksheet with the specified name.</summary>
		///   <param name='name' type='String' >The name of the worksheet to get.</param>
		///   <returns type='Worksheet' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorkbookColorInfo, function () {
		/// <signature>
		///   <summary>An immutable object which represents a color in a Microsoft Excel workbook.</summary>
		///   <param name='color' type='String' >The color which should be displayed when the WorkbookColorInfo is used.</param>
		/// </signature>
		/// <signature>
		///   <summary>An immutable object which represents a color in a Microsoft Excel workbook.</summary>
		///   <param name='color' type='String' >The base color which should be displayed when the WorkbookColorInfo is used.</param>
		///   <param name='tint' type='Number' >The tint to apply to the base color, from -1.0 (100% darken) to 1.0 (100% lighten).</param>
		/// </signature>
		/// <signature>
		///   <summary>An immutable object which represents a color in a Microsoft Excel workbook.</summary>
		///   <param name='themeColorType' type='WorkbookThemeColorType' >The type of theme color which should be displayed when the WorkbookColorInfo is used.</param>
		/// </signature>
		/// <signature>
		///   <summary>An immutable object which represents a color in a Microsoft Excel workbook.</summary>
		///   <param name='themeColorType' type='WorkbookThemeColorType' >The type of theme color which should be the base color when the WorkbookColorInfo is used.</param>
		///   <param name='tint' type='Number' >The tint to apply to the base color, from -1.0 (100% darken) to 1.0 (100% lighten).</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorkbookColorInfo, {
	automatic: function () {
		/// <signature>
		///   <summary>Gets the automatic color, which is the window text system color.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	}
});
intellisense.annotate(jQuery.ig.excel.WorkbookColorInfo.prototype, {
	color: function () {
		/// <signature>
		///   <summary>Gets the base color associated of the WorkbookColorInfo.</summary>
		///   <returns type='String' />
		/// </signature>
	},
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the WorkbookColorInfo is equal to the specified object.</summary>
		///   <param name='obj' type='Object' >The object to test for equality.</param>
		///   <returns type='Boolean' >True if the object is equal to this instance; False otherwise.</returns>
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code for the WorkbookColorInfo.</summary>
		///   <returns type='Number' integer='true' >A number which can be used to hash this instance.</returns>
		/// </signature>
	},
	getResolvedColor: function () {
		/// <signature>
		///   <summary>Gets the actual color which will be seen in Microsoft Excel if the WorkbookColorInfo is used.</summary>
		///   <returns type='String' >A Color which combines the WorkbookColorInfo.Color and WorkbookColorInfo.Tint if it is set.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the actual color which will be seen in Microsoft Excel if the WorkbookColorInfo is used.</summary>
		///   <param name='workbook' type='Workbook' >The workbook in which the WorkbookColorInfo is used.</param>
		///   <returns type='String' >A Color which combines the WorkbookColorInfo.Color, WorkbookColorInfo.ThemeColorType, and/or WorkbookColorInfo.Tint, depending on what is set.</returns>
		/// </signature>
	},
	isAutomatic: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the WorkbookColorInfo is automatic, or the window text system color.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	themeColorType: function () {
		/// <signature>
		///   <summary>Gets the base theme color associated of the WorkbookColorInfo.</summary>
		///   <returns type='WorkbookThemeColorType' />
		/// </signature>
	},
	tint: function () {
		/// <signature>
		///   <summary>Gets the to apply to the base color, from -1.0 (100% darken) to 1.0 (100% lighten).</summary>
		///   <returns type='Number' />
		/// </signature>
	},
	toString: function () {
		/// <signature>
		///   <summary>Gets the string representation of the WorkbookColorInfo.</summary>
		///   <returns type='String' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorkbookOptionsBase, function () {
	/// <summary>Abstract base class for WorkbookLoadOptions or WorkbookSaveOptions.</summary>
});

intellisense.annotate(jQuery.ig.excel.WorkbookLoadOptions, function () {
		/// <signature>
		///   <summary>Contains the options related to loading a workbook from a file or stream.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorkbookLoadOptions.prototype, {
	autoResumeCalculations: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating whether the Workbook will automatically call its Workbook.ResumeCalculations methods before the Load method returns.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets a boolean indicating whether the Workbook will automatically call its Workbook.ResumeCalculations methods before the Load method returns.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	isDuplicateFormulaParsingOptimized: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating whether duplicate formulas parsed during the load will be optimized for faster loading of the workbook.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets a boolean indicating whether duplicate formulas parsed during the load will be optimized for faster loading of the workbook.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetProtectedRange, function () {
		/// <signature>
		///   <summary>Represents a range of cells in a worksheet that may be edited by the end user.</summary>
		///   <param name='title' type='String' >The name of the range</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetProtectedRange.prototype, {
	hasPassword: function () {
		/// <signature>
		///   <summary>Returns true if the range has a password associated with it.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	isProtected: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the range is currently locked.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	ranges: function () {
		/// <signature>
		///   <summary>Returns a collection of ranges that may be edited when the worksheet is protected.</summary>
		///   <returns type='ICollection`1' />
		/// </signature>
	},
	title: function () {
		/// <signature>
		///   <summary>Returns the name of the range.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the name of the range.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	worksheet: function () {
		/// <signature>
		///   <summary>Returns the worksheet with which the edit range is associated.</summary>
		///   <returns type='Worksheet' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ArgumentExceptionExtension, function () {
		/// <signature>
		///   <summary>Extend the class ArgumentException to add a constructor</summary>
		///   <param name='message' type='String' optional='true' >The error message that explains the reason for the exception.</param>
		///   <param name='innerException' type='Error' optional='true' >The exception that is the cause of the current exception. If the innerException parameter is not a null reference, the current exception is raised in a catch block that handles the inner exception.</param>
		/// </signature>
		/// <signature>
		///   <summary>Extend the class ArgumentException to add a constructor</summary>
		///   <param name='message' type='String' >The error message that explains the reason for the exception.</param>
		///   <param name='paramName' type='String' >Name of the parameter.</param>
		///   <param name='innerException' type='Error' optional='true' >The exception that is the cause of the current exception. If the innerException parameter is not a null reference, the current exception is raised in a catch block that handles the inner exception.</param>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.ArgumentOutOfRangeExceptionExtension, function () {
		/// <signature>
		///   <summary>Extend the class ArgumentOutOfRangeException to add a constructor</summary>
		///   <param name='paramName' type='String' optional='true' >Name of the param.</param>
		///   <param name='message' type='String' optional='true' >The error message that explains the reason for the exception.</param>
		/// </signature>
		/// <signature>
		///   <summary>Extend the class ArgumentOutOfRangeException to add a constructor</summary>
		///   <param name='paramName' type='String' >Name of the parameter.</param>
		///   <param name='value' type='Object' >The value of the parameter.</param>
		///   <param name='message' type='String' >The error message that explains the reason for the exception.</param>
		/// </signature>
		/// <signature>
		///   <summary>Extend the class ArgumentOutOfRangeException to add a constructor</summary>
		///   <param name='message' type='String' >The error message that explains the reason for the exception.</param>
		///   <param name='innerException' type='Error' >The exception that is the cause of the current exception. If the innerException parameter is not a null reference (Nothing in Visual Basic), the current exception is raised in a catch block that handles the inner exception.</param>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.InvalidEnumArgumentException, function () {
		/// <signature>
		///   <summary>Introduce InvalidEnumArgumentException class to Silverlight</summary>
		///   <param name='message' type='String' optional='true' >The error message that explains the reason for the exception.</param>
		///   <param name='innerException' type='Error' optional='true' >The exception that is the cause of the current exception. If the innerException parameter is not a null reference, the current exception is raised in a catch block that handles the inner exception.</param>
		/// </signature>
		/// <signature>
		///   <summary>Introduce InvalidEnumArgumentException class to Silverlight</summary>
		///   <param name='message' type='String' >The error message that explains the reason for the exception.</param>
		///   <param name='paramName' type='String' >The name of the parameter that caused the current exception.</param>
		/// </signature>
		/// <signature>
		///   <summary>Introduce InvalidEnumArgumentException class to Silverlight</summary>
		///   <param name='argumentName' type='String' >Name of the argument.</param>
		///   <param name='invalidValue' type='Number' integer='true' >The invalid value.</param>
		///   <param name='enumClass' type='Type' >The enum class.</param>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.IExcelCalcReference, function () {
	/// <summary>The Primary Reference Inteface.</summary>
});
intellisense.annotate(jQuery.ig.excel.IExcelCalcReference.prototype, {
	absoluteName: function () {
		/// <signature>
		///   <summary>The fully qualified unique name for the referenced element.  Read Only.</summary>
		///   <returns type='String' />
		/// </signature>
	},
	containsReference: function () {
		/// <signature>
		///   <summary>Returns true if this reference contains inReference</summary>
		///   <param name='inReference' type='IExcelCalcReference' >The contained candidate.</param>
		///   <returns type='Boolean' >true if inReference is contained by this reference.</returns>
		/// </signature>
	},
	context: function () {
		/// <signature>
		///   <summary>Returns a context for the Reference.</summary>
		///   <returns type='Object' />
		/// </signature>
	},
	createReference: function () {
		/// <signature>
		///   <summary>Create a new reference relative to this reference.</summary>
		///   <param name='referenceString' type='String' >The reference string.</param>
		///   <returns type='IExcelCalcReference' >The new reference.</returns>
		/// </signature>
	},
	elementName: function () {
		/// <signature>
		///   <summary>The unqualified name of this referenced element.  Read Only.</summary>
		///   <returns type='String' />
		/// </signature>
	},
	formula: function () {
		/// <signature>
		///   <summary>The IExcelCalcFormula, if any, associated with this Reference.  If this reference 
   ///     can not contain a formula then null is returned.</summary>
		///   <returns type='IExcelCalcFormula' />
		/// </signature>
	},
	isEnumerable: function () {
		/// <signature>
		///   <summary>Returns true if this reference is enumerable.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	isSubsetReference: function () {
		/// <signature>
		///   <summary>Returns true if inReference is a proper subset of this reference</summary>
		///   <param name='inReference' type='IExcelCalcReference' >The subset candidate.</param>
		///   <returns type='Boolean' >true if inReference is contained by this reference.</returns>
		/// </signature>
	},
	normalizedAbsoluteName: function () {
		/// <signature>
		///   <summary>Returns the normalized absolute name. Calculation engine makes use of normalized
   ///     absolute names of references to compare two references and search for references.
   ///     This property should return the absolute name with case insensitive parts of
   ///     the absolute names converted to lower case.</summary>
		///   <returns type='String' />
		/// </signature>
	},
	references: function () {
		/// <signature>
		///   <summary>Return a collection of references if this Reference is enumerable.  An exception is
   ///     thrown if this reference is not enumerable.</summary>
		///   <returns type='IExcelCalcReferenceCollection' >A Reference collection.</returns>
		/// </signature>
	},
	value: function () {
		/// <signature>
		///   <summary>Gets the ExcelCalcValue, if any, associated with this Reference.  If this reference 
   ///     does not contain a Value then a ExcelCalcErrorValue is returned.</summary>
		///   <returns type='ExcelCalcValue' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the ExcelCalcValue, if any, associated with this Reference.  If this reference 
   ///     does not contain a Value then a ExcelCalcErrorValue is returned.</summary>
		///   <param name='value' type='ExcelCalcValue'  />
		///   <returns type='ExcelCalcValue' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.IExcelCalcReferenceCollection, function () {
	/// <summary>Collection of IExcelCalcReference objects</summary>
});

intellisense.annotate(jQuery.ig.excel.CellFill, function () {
		/// <signature>
		///   <summary>Abstract base class for the fill of a cell.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.CellFill, {
	createLinearGradientFill: function () {
		/// <signature>
		///   <summary>Creates a linear gradient that can be applied to a cell's fill.</summary>
		///   <param name='angle' type='Number' >The angle, in degrees, of the direction of the linear gradient, going clockwise from the left-to-right direction.</param>
		///   <param name='color1' type='String' >The color at the start of the gradient.</param>
		///   <param name='color2' type='String' >The color at the end of the gradient.</param>
		///   <returns type='CellFillLinearGradient' />
		/// </signature>
		/// <signature>
		///   <summary>Creates a linear gradient that can be applied to a cell's fill.</summary>
		///   <param name='angle' type='Number' >The angle, in degrees, of the direction of the linear gradient, going clockwise from the left-to-right direction.</param>
		///   <param name='colorInfo1' type='WorkbookColorInfo' >A WorkbookColorInfo which describes the color at the start of the gradient.</param>
		///   <param name='colorInfo2' type='WorkbookColorInfo' >A WorkbookColorInfo which describes the color at the end of the gradient.</param>
		///   <returns type='CellFillLinearGradient' />
		/// </signature>
		/// <signature>
		///   <summary>Creates a linear gradient that can be applied to a cell's fill.</summary>
		///   <param name='angle' type='Number' >The angle, in degrees, of the direction of the linear gradient, going clockwise from the left-to-right direction.</param>
		///   <param name='stops' type='Array' elementType='CellFillGradientStop' parameterArray='true' >Two or more gradient stops which describe the color transitions and their positions within the gradient.</param>
		///   <returns type='CellFillLinearGradient' />
		/// </signature>
	},
	createPatternFill: function () {
		/// <signature>
		///   <summary>Creates a solid color or pattern fill that can be applied to a cell.</summary>
		///   <param name='backgroundColor' type='String' >The background color of the cell, which will only be seen if the patternStyle is not None.</param>
		///   <param name='patternColor' type='String' >The pattern color of the cell, which will only be seen if the patternStyle is not None or Solid.</param>
		///   <param name='patternStyle' type='FillPatternStyle' >The fill pattern for the cell.</param>
		///   <returns type='CellFillPattern' />
		/// </signature>
		/// <signature>
		///   <summary>Creates a solid color or pattern fill that can be applied to a cell.</summary>
		///   <param name='backgroundColorInfo' type='WorkbookColorInfo' >A WorkbookColorInfo which describes the background color of the cell, which will only be seen if the 
		///       patternStyle is not None.</param>
		///   <param name='patternColorInfo' type='WorkbookColorInfo' >A WorkbookColorInfo which describes the pattern color of the cell, which will only be seen if the 
		///       patternStyle is not None or Solid.</param>
		///   <param name='patternStyle' type='FillPatternStyle' >The fill pattern for the cell.</param>
		///   <returns type='CellFillPattern' />
		/// </signature>
	},
	createRectangularGradientFill: function () {
		/// <signature>
		///   <summary>Creates a rectangular gradient that can be applied to a cell's fill.</summary>
		///   <param name='color1' type='String' >The color at the inner rectangle (cell center) of the gradient.</param>
		///   <param name='color2' type='String' >The color at the outer rectangle (cell edges) of the gradient.</param>
		///   <returns type='CellFillRectangularGradient' />
		/// </signature>
		/// <signature>
		///   <summary>Creates a rectangular gradient that can be applied to a cell's fill.</summary>
		///   <param name='left' type='Number' >The left edge of the inner rectangle of the gradient, ranging from 0.0 (the left of the cell) to 1.0 (the right of the cell).</param>
		///   <param name='top' type='Number' >The top edge of the inner rectangle of the gradient, ranging from 0.0 (the top of the cell) to 1.0 (the bottom of the cell).</param>
		///   <param name='right' type='Number' >The right edge of the inner rectangle of the gradient, ranging from 0.0 (the left of the cell) to 1.0 (the right of the cell).</param>
		///   <param name='bottom' type='Number' >The bottom edge of the inner rectangle of the gradient, ranging from 0.0 (the top of the cell) to 1.0 (the bottom of the cell).</param>
		///   <param name='color1' type='String' >The color at the inner rectangle of the gradient.</param>
		///   <param name='color2' type='String' >The color at the outer rectangle (cell edges) of the gradient.</param>
		///   <returns type='CellFillRectangularGradient' />
		/// </signature>
		/// <signature>
		///   <summary>Creates a rectangular gradient that can be applied to a cell's fill.</summary>
		///   <param name='colorInfo1' type='WorkbookColorInfo' >A WorkbookColorInfo which describes the color at the inner rectangle (cell center) of the gradient.</param>
		///   <param name='colorInfo2' type='WorkbookColorInfo' >A WorkbookColorInfo which describes the color at the outer rectangle (cell edges) of the gradient.</param>
		///   <returns type='CellFillRectangularGradient' />
		/// </signature>
		/// <signature>
		///   <summary>Creates a rectangular gradient that can be applied to a cell's fill.</summary>
		///   <param name='left' type='Number' >The left edge of the inner rectangle of the gradient, ranging from 0.0 (the left of the cell) to 1.0 (the right of the cell).</param>
		///   <param name='top' type='Number' >The top edge of the inner rectangle of the gradient, ranging from 0.0 (the top of the cell) to 1.0 (the bottom of the cell).</param>
		///   <param name='right' type='Number' >The right edge of the inner rectangle of the gradient, ranging from 0.0 (the left of the cell) to 1.0 (the right of the cell).</param>
		///   <param name='bottom' type='Number' >The bottom edge of the inner rectangle of the gradient, ranging from 0.0 (the top of the cell) to 1.0 (the bottom of the cell).</param>
		///   <param name='colorInfo1' type='WorkbookColorInfo' >A WorkbookColorInfo which describes the color at the inner rectangle of the gradient.</param>
		///   <param name='colorInfo2' type='WorkbookColorInfo' >A WorkbookColorInfo which describes the color at the outer rectangle (cell edges) of the gradient.</param>
		///   <returns type='CellFillRectangularGradient' />
		/// </signature>
		/// <signature>
		///   <summary>Creates a rectangular gradient that can be applied to a cell's fill.</summary>
		///   <param name='left' type='Number' >The left edge of the inner rectangle of the gradient, ranging from 0.0 (the left of the cell) to 1.0 (the right of the cell).</param>
		///   <param name='top' type='Number' >The top edge of the inner rectangle of the gradient, ranging from 0.0 (the top of the cell) to 1.0 (the bottom of the cell).</param>
		///   <param name='right' type='Number' >The right edge of the inner rectangle of the gradient, ranging from 0.0 (the left of the cell) to 1.0 (the right of the cell).</param>
		///   <param name='bottom' type='Number' >The bottom edge of the inner rectangle of the gradient, ranging from 0.0 (the top of the cell) to 1.0 (the bottom of the cell).</param>
		///   <param name='stops' type='Array' elementType='CellFillGradientStop' parameterArray='true' >Two or more gradient stops which describe the color transitions and their positions within the gradient.</param>
		///   <returns type='CellFillRectangularGradient' />
		/// </signature>
	},
	createSolidFill: function () {
		/// <signature>
		///   <summary>Creates a solid color fill that can be applied to a cell.</summary>
		///   <param name='solidColor' type='String' >The solid color of the fill.</param>
		///   <returns type='CellFillPattern' />
		/// </signature>
		/// <signature>
		///   <summary>Creates a solid color fill that can be applied to a cell.</summary>
		///   <param name='solidColorInfo' type='WorkbookColorInfo' >A WorkbookColorInfo which describes the solid color of the fill.</param>
		///   <returns type='CellFillPattern' />
		/// </signature>
	},
	noColor: function () {
		/// <signature>
		///   <summary>Gets the default cell fill, which is no background color.</summary>
		///   <returns type='CellFill' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CellFillPattern, function () {
		/// <signature>
		///   <summary>An immutable object which represents a solid or pattern fill for a cell.</summary>
		///   <param name='backgroundColorInfo' type='WorkbookColorInfo' >A WorkbookColorInfo which describes the background color of the cell, which will only be seen if the 
		///       patternStyle is not None.</param>
		///   <param name='patternColorInfo' type='WorkbookColorInfo' >A WorkbookColorInfo which describes the pattern color of the cell, which will only be seen if the 
		///       patternStyle is not None or Solid.</param>
		///   <param name='patternStyle' type='FillPatternStyle' >The fill pattern for the cell.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.CellFillPattern.prototype, {
	backgroundColorInfo: function () {
		/// <signature>
		///   <summary>Gets the WorkbookColorInfo which describes the background color of the cell, which will only be seen if the 
   ///     CellFillPattern.PatternStyle is not None.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	},
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the CellFillPattern is equal to the specified object.</summary>
		///   <param name='obj' type='Object' >The object to test for equality.</param>
		///   <returns type='Boolean' >True if the object is equal to this instance; False otherwise.</returns>
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code for the CellFillPattern.</summary>
		///   <returns type='Number' integer='true' >A number which can be used to hash this instance.</returns>
		/// </signature>
	},
	patternColorInfo: function () {
		/// <signature>
		///   <summary>Gets the WorkbookColorInfo which describes the pattern color of the cell, which will only be seen if the 
   ///     CellFillPattern.PatternStyle is not None or Solid.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	},
	patternStyle: function () {
		/// <signature>
		///   <summary>Gets the fill pattern for the cell.</summary>
		///   <returns type='FillPatternStyle' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CellFillGradient, function () {
	/// <summary>Abstract base class for a gradient fill of a cell.</summary>
});
intellisense.annotate(jQuery.ig.excel.CellFillGradient.prototype, {
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the CellFillGradient is equal to the specified object.</summary>
		///   <param name='obj' type='Object' >The object to test for equality.</param>
		///   <returns type='Boolean' >True if the object is equal to this instance; False otherwise.</returns>
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code for the CellFillGradient.</summary>
		///   <returns type='Number' integer='true' >A number which can be used to hash this instance.</returns>
		/// </signature>
	},
	stops: function () {
		/// <signature>
		///   <summary>Gets the read-only collection of gradient stops which describe the color transitions and their positions within the gradient.</summary>
		///   <returns type='IList`1' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CellFillLinearGradient, function () {
		/// <signature>
		///   <summary>An immutable object which represents a linear gradient fill for a cell.</summary>
		///   <param name='angle' type='Number' >The angle, in degrees, of the direction of the linear gradient, going clockwise from the left-to-right direction.</param>
		///   <param name='stops' type='Array' elementType='CellFillGradientStop' parameterArray='true' >Two or more gradient stops which describe the color transitions and their positions within the gradient.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.CellFillLinearGradient.prototype, {
	angle: function () {
		/// <signature>
		///   <summary>Gets the angle, in degrees, of the direction of the linear gradient, going clockwise from the left-to-right direction.</summary>
		///   <returns type='Number' />
		/// </signature>
	},
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the CellFillLinearGradient is equal to the specified object.</summary>
		///   <param name='obj' type='Object' >The object to test for equality.</param>
		///   <returns type='Boolean' >True if the object is equal to this instance; False otherwise.</returns>
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code for the CellFillLinearGradient.</summary>
		///   <returns type='Number' integer='true' >A number which can be used to hash this instance.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CellFillRectangularGradient, function () {
		/// <signature>
		///   <summary>An immutable object which represents a rectangular gradient fill for a cell.</summary>
		///   <param name='left' type='Number' >The left edge of the inner rectangle of the gradient, ranging from 0.0 (the left of the cell) to 1.0 (the right of the cell).</param>
		///   <param name='top' type='Number' >The top edge of the inner rectangle of the gradient, ranging from 0.0 (the top of the cell) to 1.0 (the bottom of the cell).</param>
		///   <param name='right' type='Number' >The right edge of the inner rectangle of the gradient, ranging from 0.0 (the left of the cell) to 1.0 (the right of the cell).</param>
		///   <param name='bottom' type='Number' >The bottom edge of the inner rectangle of the gradient, ranging from 0.0 (the top of the cell) to 1.0 (the bottom of the cell).</param>
		///   <param name='stops' type='Array' elementType='CellFillGradientStop' parameterArray='true' >Two or more gradient stops which describe the color transitions and their positions within the gradient.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.CellFillRectangularGradient.prototype, {
	bottom: function () {
		/// <signature>
		///   <summary>Gets the bottom edge of the inner rectangle of the gradient, ranging from 0.0 (the top of the cell) to 1.0 (the bottom of the cell).</summary>
		///   <returns type='Number' />
		/// </signature>
	},
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the CellFillRectangularGradient is equal to the specified object.</summary>
		///   <param name='obj' type='Object' >The object to test for equality.</param>
		///   <returns type='Boolean' >True if the object is equal to this instance; False otherwise.</returns>
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code for the CellFillRectangularGradient.</summary>
		///   <returns type='Number' integer='true' >A number which can be used to hash this instance.</returns>
		/// </signature>
	},
	left: function () {
		/// <signature>
		///   <summary>Gets the left edge of the inner rectangle of the gradient, ranging from 0.0 (the left of the cell) to 1.0 (the right of the cell).</summary>
		///   <returns type='Number' />
		/// </signature>
	},
	right: function () {
		/// <signature>
		///   <summary>Gets the right edge of the inner rectangle of the gradient, ranging from 0.0 (the left of the cell) to 1.0 (the right of the cell).</summary>
		///   <returns type='Number' />
		/// </signature>
	},
	top: function () {
		/// <signature>
		///   <summary>Gets the top edge of the inner rectangle of the gradient, ranging from 0.0 (the top of the cell) to 1.0 (the bottom of the cell).</summary>
		///   <returns type='Number' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CellFillGradientStop, function () {
		/// <signature>
		///   <summary>Immutable class which describes a color transition in a cell fill gradient.</summary>
		///   <param name='color' type='String' >The color transition for the gradient stop.</param>
		///   <param name='offset' type='Number' >The position in the gradient of the color transition for the gradient stop, ranging from 0.0 to 1.0.</param>
		/// </signature>
		/// <signature>
		///   <summary>Immutable class which describes a color transition in a cell fill gradient.</summary>
		///   <param name='colorInfo' type='WorkbookColorInfo' >The WorkbookColorInfo describing the color transition for the gradient stop.</param>
		///   <param name='offset' type='Number' >The position in the gradient of the color transition for the gradient stop, ranging from 0.0 to 1.0.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.CellFillGradientStop.prototype, {
	colorInfo: function () {
		/// <signature>
		///   <summary>Gets the WorkbookColorInfo describing the color transition for the gradient stop.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	},
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the CellFillGradientStop is equal to the specified object.</summary>
		///   <param name='obj' type='Object' >The object to test for equality.</param>
		///   <returns type='Boolean' >True if the object is equal to this instance; False otherwise.</returns>
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code for the CellFillGradientStop.</summary>
		///   <returns type='Number' integer='true' >A number which can be used to hash this instance.</returns>
		/// </signature>
	},
	offset: function () {
		/// <signature>
		///   <summary>Gets the position in the gradient of the color transition for the gradient stop, ranging from 0.0 to 1.0.</summary>
		///   <returns type='Number' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CustomView, function () {
	/// <summary>Represents a custom view in Microsoft Excel.</summary>
});
intellisense.annotate(jQuery.ig.excel.CustomView.prototype, {
	apply: function () {
		/// <signature>
		///   <summary>Applies all options from the custom view to the associated workbook and its worksheets.</summary>
		/// </signature>
	},
	getDisplayOptions: function () {
		/// <signature>
		///   <summary>Gets the display options associated with the specified worksheet.</summary>
		///   <param name='worksheet' type='Worksheet' >The worksheet whose associated display options are to be retrieved.</param>
		///   <param name='createIfNull' type='Boolean' optional='true' >True to create the display options if it has not yet been allocated. The options will be initialized based on the current state of the Worksheet. Note the options should only have been null if there were no options for the Worksheet when the Workbook was loaded.</param>
		///   <returns type='CustomViewDisplayOptions' >Null if the worksheet does not belong to the workbook associated with this custom view; 
		///       otherwise, the display options associated with the worksheet.</returns>
		/// </signature>
	},
	getHiddenColumns: function () {
		/// <signature>
		///   <summary>Gets the hidden columns associated with the specified worksheet.</summary>
		///   <param name='worksheet' type='Worksheet' >The worksheet whose associated hidden columns are to be retrieved.</param>
		///   <param name='createIfNull' type='Boolean' optional='true' >True to create the hidden column if it has not yet been allocated of the Worksheet. The options will be initialized based on the current state. Note the options should only have been null if there were no options for the Worksheet when the Workbook was loaded. Also this will not allocate the collection unless the CustomView.SaveHiddenRowsAndColumns is true.</param>
		///   <returns type='HiddenColumnCollection' >Null if CustomView.SaveHiddenRowsAndColumns is False or if the worksheet does not belong to the workbook 
		///       associated with this custom view; otherwise, the collection of hidden columns associated with the worksheet.</returns>
		/// </signature>
	},
	getHiddenRows: function () {
		/// <signature>
		///   <summary>Gets the hidden rows associated with the specified worksheet.</summary>
		///   <param name='worksheet' type='Worksheet' >The worksheet whose associated hidden rows are to be retrieved.</param>
		///   <param name='createIfNull' type='Boolean' optional='true' >True to create the hidden column if it has not yet been allocated. The options will be initialized based on the current state of the Worksheet. Note the options should only have been null if there were no options for the Worksheet when the Workbook was loaded. Also this will not allocate the collection unless the CustomView.SaveHiddenRowsAndColumns is true.</param>
		///   <returns type='HiddenRowCollection' >Null if CustomView.SaveHiddenRowsAndColumns is False or if the worksheet does not belong to the workbook 
		///       associated with this custom view; otherwise, the collection of hidden rows associated with the worksheet.</returns>
		/// </signature>
	},
	getPrintOptions: function () {
		/// <signature>
		///   <summary>Gets the print options associated with the specified worksheet.</summary>
		///   <param name='worksheet' type='Worksheet' >The worksheet whose associated print options are to be retrieved.</param>
		///   <param name='createIfNull' type='Boolean' optional='true' >True to create the print options if it has not yet been allocated. The options will be initialized based on the current state of the Worksheet. Note the options should only have been null if there were no options for the Worksheet when the Workbook was loaded. Also this will not allocate the collection unless the CustomView.SavePrintOptions is true.</param>
		///   <returns type='PrintOptions' >Null if CustomView.SavePrintOptions is False or if the worksheet does not belong to the workbook associated
		///       with this custom view; otherwise, the print options associated with the worksheet.</returns>
		/// </signature>
	},
	name: function () {
		/// <signature>
		///   <summary>Gets the name of the custom view.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the name of the custom view.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	saveHiddenRowsAndColumns: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether hidden row and column settings are saved with the custom view.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	savePrintOptions: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether print options are saved with the custom view.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	windowOptions: function () {
		/// <signature>
		///   <summary>Gets the window options for the workbook associated with the custom view.</summary>
		///   <returns type='CustomViewWindowOptions' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.DisplayOptions, function () {
	/// <summary>Abstract base class which exposes the various display options available for a worksheet which can be 
 ///     saved with both a worksheet and a custom view.</summary>
});
intellisense.annotate(jQuery.ig.excel.DisplayOptions.prototype, {
	clearSelection: function () {
		/// <signature>
		///   <summary>Removes any saved selection information.</summary>
		/// </signature>
	},
	frozenPaneSettings: function () {
		/// <signature>
		///   <summary>Gets the settings which control the frozen panes in the worksheet.</summary>
		///   <returns type='FrozenPaneSettings' />
		/// </signature>
	},
	gridlineColor: function () {
		/// <signature>
		///   <summary>Gets the color of the gridlines on the worksheet.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the color of the gridlines on the worksheet.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	panesAreFrozen: function () {
		/// <signature>
		///   <summary>Gets the value which indicates if the panes in the worksheet are frozen.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates if the panes in the worksheet are frozen.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	reset: function () {
		/// <signature>
		///   <summary>Resets the display options to their default settings.</summary>
		/// </signature>
	},
	resetCore: function () {
		/// <signature>
		///   <summary>Resets the display options to their default settings.</summary>
		/// </signature>
	},
	showExpansionIndicatorBelowGroupedRows: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the expansion indicators should be shown below grouped, 
   ///     or indented rows.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the expansion indicators should be shown below grouped, 
   ///     or indented rows.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	showExpansionIndicatorToRightOfGroupedColumns: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the expansion indicators should be shown to the right of 
   ///     grouped, or indented rows.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the expansion indicators should be shown to the right of 
   ///     grouped, or indented rows.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	showFormulasInCells: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether formulas are shown in cells.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether formulas are shown in cells.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	showGridlines: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether gridlines are shown between cells.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether gridlines are shown between cells.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	showOutlineSymbols: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether outline symbols are shown for outlined columns and rows.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether outline symbols are shown for outlined columns and rows.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	showRowAndColumnHeaders: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether to display row and column headers.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether to display row and column headers.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	showRulerInPageLayoutView: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether to show rulers in the page layout view.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether to show rulers in the page layout view.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	showZeroValues: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether zero values are shown in cells.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether zero values are shown in cells.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	unfrozenPaneSettings: function () {
		/// <signature>
		///   <summary>Gets the settings which control the unfrozen panes in the worksheet.</summary>
		///   <returns type='UnfrozenPaneSettings' />
		/// </signature>
	},
	view: function () {
		/// <signature>
		///   <summary>Gets the current view of the worksheet.</summary>
		///   <returns type='WorksheetView' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the current view of the worksheet.</summary>
		///   <param name='value' type='WorksheetView'  />
		///   <returns type='WorksheetView' />
		/// </signature>
	},
	visibility: function () {
		/// <signature>
		///   <summary>Gets the visibility of the worksheet.</summary>
		///   <returns type='WorksheetVisibility' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the visibility of the worksheet.</summary>
		///   <param name='value' type='WorksheetVisibility'  />
		///   <returns type='WorksheetVisibility' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CustomViewDisplayOptions, function () {
	/// <summary>Class which exposes the worksheet display options which can only be controlled through the custom view.</summary>
});
intellisense.annotate(jQuery.ig.excel.CustomViewDisplayOptions.prototype, {
	magnificationInCurrentView: function () {
		/// <signature>
		///   <summary>Gets the magnification level of the worksheet in the current DisplayOptions.View.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the magnification level of the worksheet in the current DisplayOptions.View.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	resetCore: function () {
		/// <signature>
		///   <summary>Resets the display options to their default settings.</summary>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CustomViewCollection, function () {
	/// <summary>A collection of CustomView instances in a workbook.</summary>
});
intellisense.annotate(jQuery.ig.excel.CustomViewCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds a new custom view to the collection.</summary>
		///   <param name='name' type='String' >The name to give the newly created custom view.</param>
		///   <param name='savePrintOptions' type='Boolean' >True to save print options for each worksheet with the custom view; False otherwise.</param>
		///   <param name='saveHiddenRowsAndColumns' type='Boolean' >True to save information about hidden rows and columns for each worksheet with the custom view; False otherwise.</param>
		///   <returns type='CustomView' >The newly created CustomView instance.</returns>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears all custom views from the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether a custom view is in this collection.</summary>
		///   <param name='customView' type='CustomView' >The custom view to locate in the collection.</param>
		///   <returns type='Boolean' >True if the custom view is found; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of custom views in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified custom view from the collection.</summary>
		///   <param name='customView' type='CustomView' >The custom view to remove from the collection.</param>
		///   <returns type='Boolean' >True if the custom view was successfully removed; False if the custom view was not 
		///       in the collection.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the custom view at the specified index from the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the custom view in the collection.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WindowOptions, function () {
	/// <summary>Abstract base class which exposes the various workbook window options available which can be saved with 
 ///     both a workbook and a custom view.</summary>
});
intellisense.annotate(jQuery.ig.excel.WindowOptions.prototype, {
	objectDisplayStyle: function () {
		/// <signature>
		///   <summary>Gets the way the objects and shapes are displayed in the workbook.</summary>
		///   <returns type='ObjectDisplayStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the way the objects and shapes are displayed in the workbook.</summary>
		///   <param name='value' type='ObjectDisplayStyle'  />
		///   <returns type='ObjectDisplayStyle' />
		/// </signature>
	},
	reset: function () {
		/// <signature>
		///   <summary>Resets the window options to their default settings.</summary>
		/// </signature>
	},
	scrollBars: function () {
		/// <signature>
		///   <summary>Gets the scroll bars shown in the workbook window.</summary>
		///   <returns type='ScrollBars' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the scroll bars shown in the workbook window.</summary>
		///   <param name='value' type='ScrollBars'  />
		///   <returns type='ScrollBars' />
		/// </signature>
	},
	selectedWorksheet: function () {
		/// <signature>
		///   <summary>Gets the selected worksheet of the workbook.</summary>
		///   <returns type='Worksheet' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the selected worksheet of the workbook.</summary>
		///   <param name='value' type='Worksheet'  />
		///   <returns type='Worksheet' />
		/// </signature>
	},
	tabBarVisible: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the worksheet tab bar is visible.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the worksheet tab bar is visible.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	tabBarWidth: function () {
		/// <signature>
		///   <summary>Gets the width of the worksheet tab bar, expressed in 1/1000ths of the application width.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the width of the worksheet tab bar, expressed in 1/1000ths of the application width.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CustomViewWindowOptions, function () {
	/// <summary>Represents the workbook window options which are saved with custom views.</summary>
});
intellisense.annotate(jQuery.ig.excel.CustomViewWindowOptions.prototype, {
	boundsInPixels: function () {
		/// <signature>
		///   <summary>Gets the pixel bounds of the workbook's MDI child window when CustomView 
   ///     owning these window options is applied.</summary>
		///   <returns type='Object' > This will be an object with numeric values for properties 'x', 'y', 'width', 'height', 'left', 'top', 'right', and 'bottom'.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Sets the pixel bounds of the workbook's MDI child window when CustomView 
   ///     owning these window options is applied.</summary>
		///   <param name='value' type='Object' > This can be an object with numeric values for properties 'x', 'y', 'width', and 'height' or 'left', 'top', 'right', and 'bottom' such as { x: 1, y: 2, width: 100, height: 200 }.</param>
		///   <returns type='Object' > This will be an object with numeric values for properties 'x', 'y', 'width', 'height', 'left', 'top', 'right', and 'bottom'.</returns>
		/// </signature>
	},
	maximized: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the workbook's MDI child window will be maximized
   ///     when the CustomView owning these window options is applied.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the workbook's MDI child window will be maximized
   ///     when the CustomView owning these window options is applied.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	reset: function () {
		/// <signature>
		///   <summary>Resets the window options to their default settings.</summary>
		/// </signature>
	},
	showFormulaBar: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether Microsoft Excel will display the formula bar when
   ///     the CustomView owning these window options is applied.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether Microsoft Excel will display the formula bar when
   ///     the CustomView owning these window options is applied.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	showStatusBar: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether Microsoft Excel will display the status bar when
   ///     the CustomView owning these window options is applied.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether Microsoft Excel will display the status bar when
   ///     the CustomView owning these window options is applied.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.DataValidationRule, function () {
	/// <summary>Base class for all data validations rules which can be applied to a cell.</summary>
});
intellisense.annotate(jQuery.ig.excel.DataValidationRule.prototype, {
	clone: function () {
		/// <signature>
		///   <summary>Creates a copy of this rule which can be applied to other worksheets.</summary>
		///   <returns type='DataValidationRule' />
		/// </signature>
	},
	errorMessageDescription: function () {
		/// <signature>
		///   <summary>Gets the description which appears in the dialog box when an invalid value is applied to a cell
   ///     in Microsoft Excel.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the description which appears in the dialog box when an invalid value is applied to a cell
   ///     in Microsoft Excel.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	errorMessageTitle: function () {
		/// <signature>
		///   <summary>Gets the title which appears in the dialog box when an invalid value is applied to a cell
   ///     in Microsoft Excel.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the title which appears in the dialog box when an invalid value is applied to a cell
   ///     in Microsoft Excel.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	errorStyle: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the value is allowed when it is invalid and which options are given to 
   ///     the user in the error dialog shown by Microsoft Excel.</summary>
		///   <returns type='DataValidationErrorStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the value is allowed when it is invalid and which options are given to 
   ///     the user in the error dialog shown by Microsoft Excel.</summary>
		///   <param name='value' type='DataValidationErrorStyle'  />
		///   <returns type='DataValidationErrorStyle' />
		/// </signature>
	},
	inputMessageDescription: function () {
		/// <signature>
		///   <summary>Gets the description in the tooltip which appears when the user selects the cell in Microsoft Excel.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the description in the tooltip which appears when the user selects the cell in Microsoft Excel.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	inputMessageTitle: function () {
		/// <signature>
		///   <summary>Gets the title in the tooltip which appears when the user selects the cell in Microsoft Excel.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the title in the tooltip which appears when the user selects the cell in Microsoft Excel.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	showErrorMessageForInvalidValue: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the error dialog should appear in Microsoft Excel when invalid data 
   ///     is entered in the cell.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the error dialog should appear in Microsoft Excel when invalid data 
   ///     is entered in the cell.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	showInputMessage: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether to show the user an input prompt tooltip when the user selects 
   ///     the cell in Microsoft Excel.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether to show the user an input prompt tooltip when the user selects 
   ///     the cell in Microsoft Excel.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.AnyValueDataValidationRule, function () {
		/// <signature>
		///   <summary>Represents a data validation rule which allows any value to be set on the target cells.
   ///     This would be used to provide an input message to the user when the cell was selected.</summary>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.LimitedValueDataValidationRule, function () {
	/// <summary>Base class for all data validations rules which prevent certain values form being applied to a cell.</summary>
});
intellisense.annotate(jQuery.ig.excel.LimitedValueDataValidationRule.prototype, {
	allowNull: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether a null, or blank, value is allowed to be set on a cell.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether a null, or blank, value is allowed to be set on a cell.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CustomDataValidationRule, function () {
		/// <signature>
		///   <summary>Represents a data validation rule which allows any formula to be used to validate the value applied to a cell.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.CustomDataValidationRule.prototype, {
	getFormula: function () {
		/// <signature>
		///   <summary>Gets the formula used to validate the value applied to a cell.</summary>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <returns type='String' >A formula used to validate the value applied to a cell.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the formula used to validate the value applied to a cell.</summary>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <param name='format' type='WorkbookFormat' >The workbook format with which to parse address.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode with which to parse address.</param>
		///   <param name='culture' type='String' >The culture to use when generating the formula string.</param>
		///   <returns type='String' >A formula used to validate the value applied to a cell.</returns>
		/// </signature>
	},
	setFormula: function () {
		/// <signature>
		///   <summary>Sets the formula used to validate the value applied to a cell.</summary>
		///   <param name='formula' type='String' >The validation formula to use for the rule.</param>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		/// </signature>
		/// <signature>
		///   <summary>Sets the formula used to validate the value applied to a cell.</summary>
		///   <param name='formula' type='String' >The validation formula to use for the rule.</param>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <param name='format' type='WorkbookFormat' >The workbook format with which to parse address.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode with which to parse address.</param>
		///   <param name='culture' type='String' >The culture to use when parsing the formula string.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetCell, function () {
	/// <summary>Represents a cell in a Microsoft Excel worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetCell, {
	getCellAddressString: function () {
		/// <signature>
		///   <summary>Gets the string representation of the address of the cell.</summary>
		///   <param name='worksheetRow' type='WorksheetRow' >The WorksheetRow of the cell.</param>
		///   <param name='columnIndex' type='Number' integer='true' >The index of the column of the cell.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to generate cell references.</param>
		///   <param name='includeWorksheetName' type='Boolean' >The value indicating whether to include the worksheet name in the cell address.</param>
		///   <returns type='String' >The string representation of the address of the cell.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the string representation of the address of the cell.</summary>
		///   <param name='worksheetRow' type='WorksheetRow' >The WorksheetRow of the cell.</param>
		///   <param name='columnIndex' type='Number' integer='true' >The index of the column of the cell.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to generate cell references.</param>
		///   <param name='includeWorksheetName' type='Boolean' >The value indicating whether to include the worksheet name in the cell address.</param>
		///   <param name='useRelativeColumn' type='Boolean' >The value indicating whether to use a relative column address.</param>
		///   <param name='useRelativeRow' type='Boolean' >The value indicating whether to use a relative row address.</param>
		///   <returns type='String' >The string representation of the address of the cell.</returns>
		/// </signature>
	},
	isCellTypeSupported: function () {
		/// <signature>
		///   <summary>Returns True if a particular type of value can be exported to excel.</summary>
		///   <param name='cellType' type='Type' >The type to test.</param>
		///   <returns type='Boolean' >True if the type is supported as a cell value, False otherwise.</returns>
		/// </signature>
	}
});
intellisense.annotate(jQuery.ig.excel.WorksheetCell.prototype, {
	applyFormula: function () {
		/// <signature>
		///   <summary>Applies a formula to the cell.</summary>
		///   <param name='value' type='String' >The formula to parse and apply to the cell.</param>
		/// </signature>
	},
	associatedDataTable: function () {
		/// <signature>
		///   <summary>Gets the data table to which the cell belongs.</summary>
		///   <returns type='WorksheetDataTable' />
		/// </signature>
	},
	associatedMergedCellsRegion: function () {
		/// <signature>
		///   <summary>Gets the merged cells region which contains the cell, or null if the cell is not merged.</summary>
		///   <returns type='WorksheetMergedCellsRegion' />
		/// </signature>
	},
	associatedTable: function () {
		/// <signature>
		///   <summary>Gets the WorksheetTable to which this cell belongs.</summary>
		///   <returns type='WorksheetTable' />
		/// </signature>
	},
	cellFormat: function () {
		/// <signature>
		///   <summary>Gets the cell formatting for this cell.</summary>
		///   <returns type='IWorksheetCellFormat' />
		/// </signature>
	},
	clearComment: function () {
		/// <signature>
		///   <summary>Removes the comment associated with the cell.</summary>
		/// </signature>
	},
	columnIndex: function () {
		/// <signature>
		///   <summary>Gets the column index of the cell.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	comment: function () {
		/// <signature>
		///   <summary>Gets the comment applied to the cell.</summary>
		///   <returns type='WorksheetCellComment' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the comment applied to the cell.</summary>
		///   <param name='value' type='WorksheetCellComment'  />
		///   <returns type='WorksheetCellComment' />
		/// </signature>
	},
	dataValidationRule: function () {
		/// <signature>
		///   <summary>Gets the data validation rule for the WorksheetCell.</summary>
		///   <returns type='DataValidationRule' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the data validation rule for the WorksheetCell.</summary>
		///   <param name='value' type='DataValidationRule'  />
		///   <returns type='DataValidationRule' />
		/// </signature>
	},
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the specified object is another WorksheetCell instance which refers 
   ///     to the same location on the same worksheet as this cell.</summary>
		///   <param name='obj' type='Object' >The instance to check for equality.</param>
		///   <returns type='Boolean' >True if the cells refer to the same location on the same worksheet; False otherwise.</returns>
		/// </signature>
	},
	formula: function () {
		/// <signature>
		///   <summary>Gets the formula which has been applied to the cell.</summary>
		///   <returns type='Formula' />
		/// </signature>
	},
	getBoundsInTwips: function () {
		/// <signature>
		///   <summary>Gets the bounds of the cell in twips (1/20th of a point).</summary>
		///   <returns type='Object' >The bounds of the cell on its worksheet. This will be an object with numeric values for properties 'x', 'y', 'width', 'height', 'left', 'top', 'right', and 'bottom'.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the bounds of the cell in twips (1/20th of a point).</summary>
		///   <param name='options' type='PositioningOptions' >The options to use when getting the bounds of the cell.</param>
		///   <returns type='Object' >The bounds of the cell on its worksheet. This will be an object with numeric values for properties 'x', 'y', 'width', 'height', 'left', 'top', 'right', and 'bottom'.</returns>
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code of the WorksheetCell.</summary>
		///   <returns type='Number' integer='true' >The hash code of the WorksheetCell.</returns>
		/// </signature>
	},
	getHyperlink: function () {
		/// <signature>
		///   <summary>Gets the effective hyperlink on the cell.</summary>
		///   <returns type='WorksheetHyperlink' />
		/// </signature>
	},
	getResolvedCellFormat: function () {
		/// <signature>
		///   <summary>Gets the resolved cell formatting for this cell.</summary>
		///   <returns type='IWorksheetCellFormat' >A format object describing the actual formatting that will be used when displayed this cell in Microsoft Excel.</returns>
		/// </signature>
	},
	getText: function () {
		/// <signature>
		///   <summary>Gets the text displayed in the cell.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the text of the cell.</summary>
		///   <param name='textFormatMode' type='TextFormatMode' >The format mode to use when getting the cell text.</param>
		///   <returns type='String' />
		/// </signature>
	},
	hasCellFormat: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the cell's format has been initialized yet.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	hasComment: function () {
		/// <signature>
		///   <summary>Get the value indicating whether the cell has an associated comment.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	rowIndex: function () {
		/// <signature>
		///   <summary>Gets the row index of the cell.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	toString: function () {
		/// <signature>
		///   <summary>Gets the string representation of the address of the cell.</summary>
		///   <returns type='String' >The string representation of the address of the cell.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the string representation of the address of the cell.</summary>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to generate cell references.</param>
		///   <param name='includeWorksheetName' type='Boolean' >The value indicating whether to include the worksheet name in the cell address.</param>
		///   <returns type='String' >The string representation of the address of the cell.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the string representation of the address of the cell.</summary>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to generate cell references.</param>
		///   <param name='includeWorksheetName' type='Boolean' >The value indicating whether to include the worksheet name in the cell address.</param>
		///   <param name='useRelativeColumn' type='Boolean' >The value indicating whether to use a relative column address.</param>
		///   <param name='useRelativeRow' type='Boolean' >The value indicating whether to use a relative row address.</param>
		///   <returns type='String' >The string representation of the address of the cell.</returns>
		/// </signature>
	},
	validateValue: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the value of the cell is valid considering the associated WorksheetCell.DataValidationRule</summary>
		///   <returns type='Boolean' >Returns false if the cell has an associated WorksheetCell.DataValidationRule and the current WorksheetCell.Value does not pass the validation defined by that rule; otherwise true is returned.</returns>
		/// </signature>
	},
	value: function () {
		/// <signature>
		///   <summary>Gets the value of the cell.</summary>
		///   <returns type='Object' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value of the cell.</summary>
		///   <param name='value' type='Object'  />
		///   <returns type='Object' />
		/// </signature>
	},
	worksheet: function () {
		/// <signature>
		///   <summary>Gets the worksheet to which the cell belongs.</summary>
		///   <returns type='Worksheet' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetRegion, function () {
		/// <signature>
		///   <summary>Represents a rectangular region of cells on a worksheet.</summary>
		///   <param name='worksheet' type='Worksheet' >The worksheet on which the region resides.</param>
		///   <param name='firstRow' type='Number' integer='true' >The index of the first row of the region.</param>
		///   <param name='firstColumn' type='Number' integer='true' >The index of the first column of the region.</param>
		///   <param name='lastRow' type='Number' integer='true' >The index of the last row of the region.</param>
		///   <param name='lastColumn' type='Number' integer='true' >The index of the last row column of the region.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetRegion.prototype, {
	applyArrayFormula: function () {
		/// <signature>
		///   <summary>Applies a array formula to the region of cells.</summary>
		///   <param name='value' type='String' >The array formula to parse and apply to the region.</param>
		/// </signature>
	},
	applyFormula: function () {
		/// <signature>
		///   <summary>Applies a formula to the region of cells.</summary>
		///   <param name='value' type='String' >The formula to parse and apply to the region.</param>
		/// </signature>
	},
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the specified value equals this WorksheetRegion.</summary>
		///   <param name='obj' type='Object' >The value to test for equality.</param>
		///   <returns type='Boolean' />
		/// </signature>
	},
	firstColumn: function () {
		/// <signature>
		///   <summary>Gets the index of the first column in the region.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	firstRow: function () {
		/// <signature>
		///   <summary>Gets the index of the first row in the region.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	formatAsTable: function () {
		/// <signature>
		///   <summary>Formats the region as a table and adds an associated WorksheetTable to the Worksheet.Tables 
   ///     collection.</summary>
		///   <param name='tableHasHeaders' type='Boolean' >A value which indicates whether the top row of the region contains the headers for the table.</param>
		///   <returns type='WorksheetTable' >The WorksheetTable created the represent the formatted table for the region.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Formats the region as a table and adds an associated WorksheetTable to the Worksheet.Tables 
   ///     collection.</summary>
		///   <param name='tableHasHeaders' type='Boolean' >A value which indicates whether the top row of the region contains the headers for the table.</param>
		///   <param name='tableStyle' type='WorksheetTableStyle' >The WorksheetTableStyle to apply to the table or null to use the Workbook.DefaultTableStyle.</param>
		///   <returns type='WorksheetTable' >The WorksheetTable created the represent the formatted table for the region.</returns>
		/// </signature>
	},
	getBoundsInTwips: function () {
		/// <signature>
		///   <summary>Gets the bounds of the region in twips (1/20th of a point).</summary>
		///   <returns type='Object' >The bounds of the region on its worksheet. This will be an object with numeric values for properties 'x', 'y', 'width', 'height', 'left', 'top', 'right', and 'bottom'.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the bounds of the region in twips (1/20th of a point).</summary>
		///   <param name='options' type='PositioningOptions' >The options to use when getting the bounds of the region.</param>
		///   <returns type='Object' >The bounds of the region on its worksheet. This will be an object with numeric values for properties 'x', 'y', 'width', 'height', 'left', 'top', 'right', and 'bottom'.</returns>
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gtes the hash code for the WorksheetRegion.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	lastColumn: function () {
		/// <signature>
		///   <summary>Gets the index of the last column in the region.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	lastRow: function () {
		/// <signature>
		///   <summary>Gets the index of the last row in the region.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	toString: function () {
		/// <signature>
		///   <summary>Gets the string representation of the range of cells in the region.</summary>
		///   <returns type='String' >The string representation of the range of cells in the region.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the string representation of the range of cells in the region.</summary>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to generate cell references.</param>
		///   <param name='includeWorksheetName' type='Boolean' >The value indicating whether to include the worksheet name in the range address.</param>
		///   <returns type='String' >The string representation of the range of cells in the region.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the string representation of the range of cells in the region.</summary>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to generate cell references.</param>
		///   <param name='includeWorksheetName' type='Boolean' >The value indicating whether to include the worksheet name in the range address.</param>
		///   <param name='useRelativeColumn' type='Boolean' >The value indicating whether to use a relative column address for the cells in the range.</param>
		///   <param name='useRelativeRow' type='Boolean' >The value indicating whether to use a relative row address for the cells in the range.</param>
		///   <returns type='String' >The string representation of the range of cells in the region.</returns>
		/// </signature>
	},
	worksheet: function () {
		/// <signature>
		///   <summary>Gets the worksheet on which the region resides.</summary>
		///   <returns type='Worksheet' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetReferenceCollection, function () {
		/// <signature>
		///   <summary>A collection of cells or regions which are all on the same WorksheetReferenceCollection.Worksheet.</summary>
		///   <param name='worksheet' type='Worksheet' >The worksheet to which the references in the collection will belong.</param>
		/// </signature>
		/// <signature>
		///   <summary>A collection of cells or regions which are all on the same WorksheetReferenceCollection.Worksheet.</summary>
		///   <param name='worksheet' type='Worksheet' >The worksheet to which the references in the collection will belong.</param>
		///   <param name='references' type='String' >The space delimited list of references to add to the collection.</param>
		/// </signature>
		/// <signature>
		///   <summary>A collection of cells or regions which are all on the same WorksheetReferenceCollection.Worksheet.</summary>
		///   <param name='cell' type='WorksheetCell' >The cell with which the collection should be initialized.</param>
		/// </signature>
		/// <signature>
		///   <summary>A collection of cells or regions which are all on the same WorksheetReferenceCollection.Worksheet.</summary>
		///   <param name='region' type='WorksheetRegion' >The cell with which the collection should be initialized.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetReferenceCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds a list of references to the collection of references.</summary>
		///   <param name='references' type='String' >The space delimited list of references to add to the collection.</param>
		/// </signature>
		/// <signature>
		///   <summary>Adds a list of references to the collection of references.</summary>
		///   <param name='references' type='String' >The space delimited list of references to add to the collection.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode with which to parse the references.</param>
		/// </signature>
		/// <signature>
		///   <summary>Adds a cell to the collection of references.</summary>
		///   <param name='cell' type='WorksheetCell' >The cell to add to the collection.</param>
		/// </signature>
		/// <signature>
		///   <summary>Adds a region to the collection of references.</summary>
		///   <param name='region' type='WorksheetRegion' >The region to add to the collection.</param>
		/// </signature>
	},
	cellsCount: function () {
		/// <signature>
		///   <summary>Gets the number of cells contains in all references in this collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears all references from the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the collection contains the specified cell.</summary>
		///   <param name='cell' type='WorksheetCell' >The cell to search for in the collection.</param>
		///   <returns type='Boolean' >True if the cell is contained in the collection or a region which contains the cell is contained in the collection; False otherwise.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Determines whether the collection contains the specified region.</summary>
		///   <param name='region' type='WorksheetRegion' >The region to search for in the collection.</param>
		///   <returns type='Boolean' >True if the region is contained in the collection or a region which fully contains the specified region is contained in the collection; 
		///       False otherwise.</returns>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes a list of references from the collection of references.</summary>
		///   <param name='references' type='String' >The space delimited list of references to remove from the collection.</param>
		///   <returns type='Boolean' >True if any cells in the references were found and removed. False otherwise.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Removes a list of references from the collection of references.</summary>
		///   <param name='references' type='String' >The space delimited list of references to remove from the collection.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode with which to parse the references.</param>
		///   <returns type='Boolean' >True if any cells in the references were found and removed. False otherwise.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Removes a cell from the collection of references.</summary>
		///   <param name='cell' type='WorksheetCell' >The cell to remove from the collection.</param>
		///   <returns type='Boolean' >True if the cell was found and removed. False otherwise.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Removes a region from the collection of references.</summary>
		///   <param name='region' type='WorksheetRegion' >The region to remove from the collection.</param>
		///   <returns type='Boolean' >True if any cells in the region were found and removed. False otherwise.</returns>
		/// </signature>
	},
	toString: function () {
		/// <signature>
		///   <summary>Returns the string value represent the cell and region addresses in the collection.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Returns the string value represent the cell and region addresses in the collection.</summary>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode with which to get the region strings.</param>
		///   <returns type='String' />
		/// </signature>
	},
	worksheet: function () {
		/// <signature>
		///   <summary>Gets the worksheet for which this collection contains references.</summary>
		///   <returns type='Worksheet' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.DataValidationRuleCollection, function () {
	/// <summary>A collection of DataValidationRule instances in a worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.DataValidationRuleCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Applies a data validation rule to the a set of references.</summary>
		///   <param name='rule' type='DataValidationRule' >The data validation rule to apply to the set of references.</param>
		///   <param name='references' type='WorksheetReferenceCollection' >The reference to which the data validation rule should be applied.</param>
		/// </signature>
		/// <signature>
		///   <summary>Applies a data validation rule to the a set of references.</summary>
		///   <param name='rule' type='DataValidationRule' >The data validation rule to apply to the set of references.</param>
		///   <param name='references' type='WorksheetReferenceCollection' >The reference to which the data validation rule should be applied.</param>
		///   <param name='overwriteExistingRules' type='Boolean' >Indicates whether cells in the references collection which already have data validation rules should have the rules overwritten.</param>
		/// </signature>
		/// <signature>
		///   <summary>Applies an AnyValueDataValidationRule to a WorksheetCell.</summary>
		///   <param name='rule' type='AnyValueDataValidationRule' >The data validation rule to apply to the cell.</param>
		///   <param name='cell' type='WorksheetCell' >The cell to which the data validation rule should be applied.</param>
		/// </signature>
		/// <signature>
		///   <summary>Applies an AnyValueDataValidationRule to the cells in a WorksheetRegion.</summary>
		///   <param name='rule' type='AnyValueDataValidationRule' >The data validation rule to apply to the region.</param>
		///   <param name='region' type='WorksheetRegion' >The region to which the data validation rule should be applied.</param>
		/// </signature>
		/// <signature>
		///   <summary>Applies an CustomDataValidationRule to a WorksheetCell.</summary>
		///   <param name='rule' type='CustomDataValidationRule' >The data validation rule to apply to the cell.</param>
		///   <param name='cell' type='WorksheetCell' >The cell to which the data validation rule should be applied.</param>
		/// </signature>
		/// <signature>
		///   <summary>Applies an CustomDataValidationRule to the cells in a WorksheetRegion.</summary>
		///   <param name='rule' type='CustomDataValidationRule' >The data validation rule to apply to the region.</param>
		///   <param name='region' type='WorksheetRegion' >The region to which the data validation rule should be applied.</param>
		/// </signature>
		/// <signature>
		///   <summary>Applies an ListDataValidationRule to a WorksheetCell.</summary>
		///   <param name='rule' type='ListDataValidationRule' >The data validation rule to apply to the cell.</param>
		///   <param name='cell' type='WorksheetCell' >The cell to which the data validation rule should be applied.</param>
		/// </signature>
		/// <signature>
		///   <summary>Applies an ListDataValidationRule to the cells in a WorksheetRegion.</summary>
		///   <param name='rule' type='ListDataValidationRule' >The data validation rule to apply to the region.</param>
		///   <param name='region' type='WorksheetRegion' >The region to which the data validation rule should be applied.</param>
		/// </signature>
		/// <signature>
		///   <summary>Applies an OneConstraintDataValidationRule to a WorksheetCell.</summary>
		///   <param name='rule' type='OneConstraintDataValidationRule' >The data validation rule to apply to the cell.</param>
		///   <param name='cell' type='WorksheetCell' >The cell to which the data validation rule should be applied.</param>
		/// </signature>
		/// <signature>
		///   <summary>Applies an OneConstraintDataValidationRule to the cells in a WorksheetRegion.</summary>
		///   <param name='rule' type='OneConstraintDataValidationRule' >The data validation rule to apply to the region.</param>
		///   <param name='region' type='WorksheetRegion' >The region to which the data validation rule should be applied.</param>
		/// </signature>
		/// <signature>
		///   <summary>Applies an TwoConstraintDataValidationRule to a WorksheetCell.</summary>
		///   <param name='rule' type='TwoConstraintDataValidationRule' >The data validation rule to apply to the cell.</param>
		///   <param name='cell' type='WorksheetCell' >The cell to which the data validation rule should be applied.</param>
		/// </signature>
		/// <signature>
		///   <summary>Applies an TwoConstraintDataValidationRule to the cells in a WorksheetRegion.</summary>
		///   <param name='rule' type='TwoConstraintDataValidationRule' >The data validation rule to apply to the region.</param>
		///   <param name='region' type='WorksheetRegion' >The region to which the data validation rule should be applied.</param>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Removes all data validation rules from the DataValidationRuleCollection.Worksheet.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the specified data validation rule exists on the DataValidationRuleCollection.Worksheet.</summary>
		///   <param name='rule' type='DataValidationRule' >The rule to test.</param>
		///   <returns type='Boolean' >True if the rule is applied to one or more cells on the Worksheet; False otherwise.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Determines whether the specified cell has a data validation rule applied to it.</summary>
		///   <param name='cell' type='WorksheetCell' >The cell to test.</param>
		///   <returns type='Boolean' >True if the cell has a data validation rule applied; False otherwise.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Determines whether the specified references collection has a data validation rule applied to any of it's cells.</summary>
		///   <param name='references' type='WorksheetReferenceCollection' >The references collection to test.</param>
		///   <returns type='Boolean' >True if the any cell from the references collection has a data validation rule applied; False otherwise.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Determines whether the specified region has a data validation rule applied to any of it's cells.</summary>
		///   <param name='region' type='WorksheetRegion' >The region to test.</param>
		///   <returns type='Boolean' >True if the any cell from the region has a data validation rule applied; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of data validation rules applied on the DataValidationRuleCollection.Worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	findRule: function () {
		/// <signature>
		///   <summary>Find the data validation rule applied to the specified cell.</summary>
		///   <param name='cell' type='WorksheetCell' >The cell to test.</param>
		///   <returns type='DataValidationRule' >A DataValidationRule-derived instance or null if the cell has no rule applied.</returns>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified rule from the DataValidationRuleCollection.Worksheet.</summary>
		///   <param name='rule' type='DataValidationRule' >The rule which should be removed.</param>
		///   <returns type='Boolean' >True if the rule was contained on the Worksheet before removal; False otherwise.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Removes the data validation rule form the cell if one is applied.</summary>
		///   <param name='cell' type='WorksheetCell' >The cell which should have its data validation rule cleared.</param>
		///   <returns type='Boolean' >True if the data validation rule was found and removed from the cell; False if the cell had no data validation rule applied.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Removes the data validation rule form all cells in the specified collection.</summary>
		///   <param name='references' type='WorksheetReferenceCollection' >The collection of references which should have its data validation rules cleared.</param>
		///   <returns type='Boolean' >True if the data validation rule was found and removed from any cells in the collection; False if the cells had no data validation rule applied.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Removes the data validation rule form all cells in the specified region.</summary>
		///   <param name='region' type='WorksheetRegion' >The region which should have its data validation rules cleared.</param>
		///   <returns type='Boolean' >True if the data validation rule was found and removed from any cells in the region; False if the region had no data validation rule applied.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ListDataValidationRule, function () {
		/// <signature>
		///   <summary>Represents a data validation rule which allows a value from a list of accepted values to be applied to a cell.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ListDataValidationRule.prototype, {
	getValuesFormula: function () {
		/// <signature>
		///   <summary>Gets the formula which specifies the accepted values.</summary>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <returns type='String' >A formula used to validate the value applied to a cell.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the formula which specifies the accepted values.</summary>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <param name='format' type='WorkbookFormat' >The workbook format with which to parse address.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode with which to parse address.</param>
		///   <param name='culture' type='String' >The culture to use when generating the formula string.</param>
		///   <returns type='String' >A formula used to validate the value applied to a cell.</returns>
		/// </signature>
	},
	setValues: function () {
		/// <signature>
		///   <summary>Sets the list of accepted values the cell can accept.</summary>
		///   <param name='values' type='Array' elementType='Object' parameterArray='true' >The list of accepted values.</param>
		/// </signature>
	},
	setValuesFormula: function () {
		/// <signature>
		///   <summary>Sets the formula which specifies the accepted values.</summary>
		///   <param name='valuesFormula' type='String' >The formula which provides the accepted values for the rule.</param>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		/// </signature>
		/// <signature>
		///   <summary>Sets the formula which specifies the accepted values.</summary>
		///   <param name='valuesFormula' type='String' >The formula which provides the accepted values for the rule.</param>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <param name='format' type='WorkbookFormat' >The workbook format with which to parse address.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode with which to parse address.</param>
		///   <param name='culture' type='String' >The culture to use when parsing the formula string.</param>
		/// </signature>
	},
	showDropdown: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether a drop down should be displayed in Microsoft Excel with the list of accepted values.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether a drop down should be displayed in Microsoft Excel with the list of accepted values.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ValueConstraintDataValidationRule, function () {
	/// <summary>Base class for all data validations rules which compare the cell value against one or more constraint when determining
 ///     the validity of the cell value.</summary>
});
intellisense.annotate(jQuery.ig.excel.ValueConstraintDataValidationRule.prototype, {
	validationCriteria: function () {
		/// <signature>
		///   <summary>Gets the criteria to use when validating the cell value against the constraint(s).</summary>
		///   <returns type='DataValidationCriteria' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the criteria to use when validating the cell value against the constraint(s).</summary>
		///   <param name='value' type='DataValidationCriteria'  />
		///   <returns type='DataValidationCriteria' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.OneConstraintDataValidationRule, function () {
		/// <signature>
		///   <summary>Represents a data validation rule which can validate the cell value against a single constraint value or formula.</summary>
		/// </signature>
		/// <signature>
		///   <summary>Represents a data validation rule which can validate the cell value against a single constraint value or formula.</summary>
		///   <param name='validationOperator' type='OneConstraintDataValidationOperator' >The operator to use when comparing the cell value to the constraint value.</param>
		///   <param name='validationCriteria' type='DataValidationCriteria' >The criteria to use when validating the cell value.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.OneConstraintDataValidationRule.prototype, {
	getConstraintFormula: function () {
		/// <signature>
		///   <summary>Gets the constraint formula used to validate the cell value.</summary>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <returns type='String' >A formula used to validate the value applied to a cell.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the constraint formula used to validate the cell value.</summary>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <param name='format' type='WorkbookFormat' >The workbook format with which to parse address.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode with which to parse address.</param>
		///   <param name='culture' type='String' >The culture to use when generating the formula string.</param>
		///   <returns type='String' >A formula used to validate the value applied to a cell.</returns>
		/// </signature>
	},
	setConstraint: function () {
		/// <signature>
		///   <summary>Sets the constraint value used to validate the cell value.</summary>
		///   <param name='value' type='Number' >The constraint value used to compare against the cell value.</param>
		/// </signature>
		/// <signature>
		///   <summary>Sets the constraint value used to validate the cell value.</summary>
		///   <param name='value' type='Date' >The constraint value used to compare against the cell value.</param>
		/// </signature>
	},
	setConstraintFormula: function () {
		/// <signature>
		///   <summary>Sets the constraint formula used to validate the cell value.</summary>
		///   <param name='constraintFormula' type='String' >The validation formula to use for the rule.</param>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		/// </signature>
		/// <signature>
		///   <summary>Sets the constraint formula used to validate the cell value.</summary>
		///   <param name='constraintFormula' type='String' >The validation formula to use for the rule.</param>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <param name='format' type='WorkbookFormat' >The workbook format with which to parse address.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode with which to parse address.</param>
		///   <param name='culture' type='String' >The culture to use when parsing the formula string.</param>
		/// </signature>
	},
	validationOperator: function () {
		/// <signature>
		///   <summary>Gets the validation operator to use when comparing the cell value against the constraint value or formula.</summary>
		///   <returns type='OneConstraintDataValidationOperator' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the validation operator to use when comparing the cell value against the constraint value or formula.</summary>
		///   <param name='value' type='OneConstraintDataValidationOperator'  />
		///   <returns type='OneConstraintDataValidationOperator' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.TwoConstraintDataValidationRule, function () {
		/// <signature>
		///   <summary>Represents a data validation rule which can validate the cell value against two constraint values or formulas.</summary>
		/// </signature>
		/// <signature>
		///   <summary>Represents a data validation rule which can validate the cell value against two constraint values or formulas.</summary>
		///   <param name='validationOperator' type='TwoConstraintDataValidationOperator' >The operator to use when comparing the cell value to the constraint values.</param>
		///   <param name='validationCriteria' type='DataValidationCriteria' >The criteria to use when validating the cell value.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.TwoConstraintDataValidationRule.prototype, {
	getLowerConstraintFormula: function () {
		/// <signature>
		///   <summary>Gets the lower constraint formula used to validate the cell value.</summary>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <returns type='String' >A formula used to validate the value applied to a cell.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the lower constraint formula used to validate the cell value.</summary>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <param name='format' type='WorkbookFormat' >The workbook format with which to parse address.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode with which to parse address.</param>
		///   <param name='culture' type='String' >The culture to use when generating the formula string.</param>
		///   <returns type='String' >A formula used to validate the value applied to a cell.</returns>
		/// </signature>
	},
	getUpperConstraintFormula: function () {
		/// <signature>
		///   <summary>Gets the upper constraint formula used to validate the cell value.</summary>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <returns type='String' >A formula used to validate the value applied to a cell.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the upper constraint formula used to validate the cell value.</summary>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <param name='format' type='WorkbookFormat' >The workbook format with which to parse address.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode with which to parse address.</param>
		///   <param name='culture' type='String' >The culture to use when generating the formula string.</param>
		///   <returns type='String' >A formula used to validate the value applied to a cell.</returns>
		/// </signature>
	},
	setLowerConstraint: function () {
		/// <signature>
		///   <summary>Sets the lower constraint value used to validate the cell value.</summary>
		///   <param name='value' type='Number' >The lower constraint value used to compare against the cell value.</param>
		/// </signature>
		/// <signature>
		///   <summary>Sets the lower constraint value used to validate the cell value.</summary>
		///   <param name='value' type='Date' >The lower constraint value used to compare against the cell value.</param>
		/// </signature>
	},
	setLowerConstraintFormula: function () {
		/// <signature>
		///   <summary>Gets the lower constraint formula used to validate the cell value.</summary>
		///   <param name='lowerConstraintFormula' type='String' >The lower constraint formula to use for the rule.</param>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		/// </signature>
		/// <signature>
		///   <summary>Gets the lower constraint formula used to validate the cell value.</summary>
		///   <param name='lowerConstraintFormula' type='String' >The lower constraint formula to use for the rule.</param>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <param name='format' type='WorkbookFormat' >The workbook format with which to parse address.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode with which to parse address.</param>
		///   <param name='culture' type='String' >The culture to use when parsing the formula string.</param>
		/// </signature>
	},
	setUpperConstraint: function () {
		/// <signature>
		///   <summary>Sets the upper constraint value used to validate the cell value.</summary>
		///   <param name='value' type='Number' >The lower constraint value used to compare against the cell value.</param>
		/// </signature>
		/// <signature>
		///   <summary>Sets the upper constraint value used to validate the cell value.</summary>
		///   <param name='value' type='Date' >The lower constraint value used to compare against the cell value.</param>
		/// </signature>
	},
	setUpperConstraintFormula: function () {
		/// <signature>
		///   <summary>Gets the upper constraint formula used to validate the cell value.</summary>
		///   <param name='upperConstraintFormula' type='String' >The upper constraint formula to use for the rule.</param>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		/// </signature>
		/// <signature>
		///   <summary>Gets the upper constraint formula used to validate the cell value.</summary>
		///   <param name='upperConstraintFormula' type='String' >The upper constraint formula to use for the rule.</param>
		///   <param name='address' type='String' >The address of the cell or region that serves as the basis for relative references, or null to use the top-left cell of 
		///       the worksheet.</param>
		///   <param name='format' type='WorkbookFormat' >The workbook format with which to parse address.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode with which to parse address.</param>
		///   <param name='culture' type='String' >The culture to use when parsing the formula string.</param>
		/// </signature>
	},
	validationOperator: function () {
		/// <signature>
		///   <summary>Gets the validation operator to use when comparing the cell value against the constraint values or formulas.</summary>
		///   <returns type='TwoConstraintDataValidationOperator' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the validation operator to use when comparing the cell value against the constraint values or formulas.</summary>
		///   <param name='value' type='TwoConstraintDataValidationOperator'  />
		///   <returns type='TwoConstraintDataValidationOperator' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.DisplayValueCollection, function () {
	/// <summary>A collection of display text values.</summary>
});
intellisense.annotate(jQuery.ig.excel.DisplayValueCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds a display text value to the collection.</summary>
		///   <param name='item' type='String' >The display text value to add to the collection.</param>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the specified value is in the collection.</summary>
		///   <param name='item' type='String' >The display text value to find in the collection.</param>
		///   <returns type='Boolean' />
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of display text values in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	indexOf: function () {
		/// <signature>
		///   <summary>Gets the index of the specified display text value in the collection.</summary>
		///   <param name='item' type='String' >The display text value to find in the collection.</param>
		///   <returns type='Number' integer='true' >The 0-based index of the specified display text value in the collection or -1 if the item is not in the collection.</returns>
		/// </signature>
	},
	insert: function () {
		/// <signature>
		///   <summary>Inserts a display text value into the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index where the value should be inserted.</param>
		///   <param name='item' type='String' >The display text value to insert into the collection.</param>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes a display text value from the collection.</summary>
		///   <param name='item' type='String' >The display text value to remove from the collection.</param>
		///   <returns type='Boolean' >True if the value was found and removed; False otherwise.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the display text value at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index of the value to remove.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.DocumentProperties, function () {
	/// <summary>Class which exposes the document level properties for a Microsoft Excel file.</summary>
});
intellisense.annotate(jQuery.ig.excel.DocumentProperties.prototype, {
	author: function () {
		/// <signature>
		///   <summary>Gets the author of the document.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the author of the document.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	category: function () {
		/// <signature>
		///   <summary>Gets the category of the document.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the category of the document.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	comments: function () {
		/// <signature>
		///   <summary>Gets the comments associated with the document.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the comments associated with the document.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	company: function () {
		/// <signature>
		///   <summary>Gets the company to which the document belongs.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the company to which the document belongs.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	keywords: function () {
		/// <signature>
		///   <summary>Gets the keywords which describe the document.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the keywords which describe the document.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	manager: function () {
		/// <signature>
		///   <summary>Gets the manager associated with the document.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the manager associated with the document.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	status: function () {
		/// <signature>
		///   <summary>Gets the current status of the document.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the current status of the document.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	subject: function () {
		/// <signature>
		///   <summary>Gets the subject of the contents of the document.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the subject of the contents of the document.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	title: function () {
		/// <signature>
		///   <summary>Gets the title of the document.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the title of the document.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ErrorValue, function () {
	/// <summary>Represents an error value in Microsoft Excel.</summary>
});
intellisense.annotate(jQuery.ig.excel.ErrorValue, {
	argumentOrFunctionNotAvailable: function () {
		/// <signature>
		///   <summary>Gets the ErrorValue representing the #N/A error.</summary>
		///   <returns type='ErrorValue' />
		/// </signature>
	},
	circularity: function () {
		/// <signature>
		///   <summary>Gets the ErrorValue representing a circularity error.</summary>
		///   <returns type='ErrorValue' />
		/// </signature>
	},
	divisionByZero: function () {
		/// <signature>
		///   <summary>Gets the ErrorValue representing the #DIV/0! error.</summary>
		///   <returns type='ErrorValue' />
		/// </signature>
	},
	emptyCellRangeIntersection: function () {
		/// <signature>
		///   <summary>Gets the ErrorValue representing the #NULL! error.</summary>
		///   <returns type='ErrorValue' />
		/// </signature>
	},
	invalidCellReference: function () {
		/// <signature>
		///   <summary>Gets the ErrorValue representing the #REF! error.</summary>
		///   <returns type='ErrorValue' />
		/// </signature>
	},
	valueRangeOverflow: function () {
		/// <signature>
		///   <summary>Gets the ErrorValue representing the #NUM! error.</summary>
		///   <returns type='ErrorValue' />
		/// </signature>
	},
	wrongFunctionName: function () {
		/// <signature>
		///   <summary>Gets the ErrorValue representing the #NAME? error.</summary>
		///   <returns type='ErrorValue' />
		/// </signature>
	},
	wrongOperandType: function () {
		/// <signature>
		///   <summary>Gets the ErrorValue representing the #VALUE! error.</summary>
		///   <returns type='ErrorValue' />
		/// </signature>
	}
});
intellisense.annotate(jQuery.ig.excel.ErrorValue.prototype, {
	toString: function () {
		/// <signature>
		///   <summary>Gets the string representation of the error value.</summary>
		///   <returns type='String' >The string representation of the error value.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.Filter, function () {
	/// <summary>Abstract base class for all filters which filters cells in a worksheet.</summary>
});

intellisense.annotate(jQuery.ig.excel.DynamicValuesFilter, function () {
	/// <summary>Abstract base class for all filter types which filter data based on a dynamic condition, such as the data present in the filtered
 ///     data range, the date when the filter was applied, or the date when the filter is evaluated.</summary>
});

intellisense.annotate(jQuery.ig.excel.AverageFilter, function () {
	/// <summary>Represents a filter which can filter data based on whether the data is below or above the average of the entire data range.</summary>
});
intellisense.annotate(jQuery.ig.excel.AverageFilter.prototype, {
	average: function () {
		/// <signature>
		///   <summary>Gets the average that was computed the last time the filter was applied or 0 if any errors or all non-numeric values 
   ///     were found when applying the filter.</summary>
		///   <returns type='Number' />
		/// </signature>
	},
	type: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether to filter in values below or above the average of the data range.</summary>
		///   <returns type='AverageFilterType' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether to filter in values below or above the average of the data range.</summary>
		///   <param name='value' type='AverageFilterType'  />
		///   <returns type='AverageFilterType' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CustomFilter, function () {
	/// <summary>Represents a filter which can filter data based on one or two custom conditions.</summary>
});
intellisense.annotate(jQuery.ig.excel.CustomFilter.prototype, {
	condition1: function () {
		/// <signature>
		///   <summary>Gets the first condition by which to filter the cells in the data range.</summary>
		///   <returns type='CustomFilterCondition' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the first condition by which to filter the cells in the data range.</summary>
		///   <param name='value' type='CustomFilterCondition'  />
		///   <returns type='CustomFilterCondition' />
		/// </signature>
	},
	condition2: function () {
		/// <signature>
		///   <summary>Gets the second condition by which to filter the cells in the data range.</summary>
		///   <returns type='CustomFilterCondition' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the second condition by which to filter the cells in the data range.</summary>
		///   <param name='value' type='CustomFilterCondition'  />
		///   <returns type='CustomFilterCondition' />
		/// </signature>
	},
	conditionalOperator: function () {
		/// <signature>
		///   <summary>Gets the operator which defines how to logically combine CustomFilter.Condition1 and CustomFilter.Condition2</summary>
		///   <returns type='ConditionalOperator' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the operator which defines how to logically combine CustomFilter.Condition1 and CustomFilter.Condition2</summary>
		///   <param name='value' type='ConditionalOperator'  />
		///   <returns type='ConditionalOperator' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CustomFilterCondition, function () {
		/// <signature>
		///   <summary>A filter condition used in a CustomFilter.</summary>
		///   <param name='comparisonOperator' type='ExcelComparisonOperator' >The operator which describes how the cell values should be compared against value.</param>
		///   <param name='value' type='Object' >The number, string, or date against which the cell values should be compared. The string can contains wild cards for any 
		///       character (?) or for zero or more characters (*).</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.CustomFilterCondition.prototype, {
	comparisonOperator: function () {
		/// <signature>
		///   <summary>Gets the operator which describes how the cell values should be compared against CustomFilterCondition.Value.</summary>
		///   <returns type='ExcelComparisonOperator' />
		/// </signature>
	},
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the CustomFilterCondition is equal to the specified object.</summary>
		///   <param name='obj' type='Object' >The object to test for equality.</param>
		///   <returns type='Boolean' >True if the object is equal to this instance; False otherwise.</returns>
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code for the CustomFilterCondition.</summary>
		///   <returns type='Number' integer='true' >A number which can be used to hash this instance.</returns>
		/// </signature>
	},
	value: function () {
		/// <signature>
		///   <summary>Gets the value against which the cell values should be compared.</summary>
		///   <returns type='Object' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.DatePeriodFilter, function () {
	/// <summary>Represents a filter which can filter dates in a specific period.</summary>
});
intellisense.annotate(jQuery.ig.excel.DatePeriodFilter.prototype, {
	type: function () {
		/// <signature>
		///   <summary>Gets the type of date period to filter in.</summary>
		///   <returns type='DatePeriodFilterType' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the type of date period to filter in.</summary>
		///   <param name='value' type='DatePeriodFilterType'  />
		///   <returns type='DatePeriodFilterType' />
		/// </signature>
	},
	value: function () {
		/// <signature>
		///   <summary>Gets the 1-based value of the month or quarter to filter in.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the 1-based value of the month or quarter to filter in.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.DateRangeFilter, function () {
	/// <summary>Abstract base class for filters which filter dates based on whether they are within a specified range of dates or not.</summary>
});
intellisense.annotate(jQuery.ig.excel.DateRangeFilter.prototype, {
	end: function () {
		/// <signature>
		///   <summary>Gets the exclusive end date of the filtered in date range.</summary>
		///   <returns type='Date' />
		/// </signature>
	},
	start: function () {
		/// <signature>
		///   <summary>Gets the inclusive start date of the filtered in date range.</summary>
		///   <returns type='Date' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FillFilter, function () {
	/// <summary>Represents a filter which will filter cells based on their background fills.</summary>
});
intellisense.annotate(jQuery.ig.excel.FillFilter.prototype, {
	fill: function () {
		/// <signature>
		///   <summary>Gets the CellFill by which the cells should be filtered.</summary>
		///   <returns type='CellFill' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the CellFill by which the cells should be filtered.</summary>
		///   <param name='value' type='CellFill'  />
		///   <returns type='CellFill' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FixedDateGroup, function () {
		/// <signature>
		///   <summary>Represents a fixed range of dates.</summary>
		///   <param name='type' type='FixedDateGroupType' >The type, or precision, of the group.</param>
		///   <param name='value' type='Date' >The reference date which determines range of accepted dates.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.FixedDateGroup.prototype, {
	end: function () {
		/// <signature>
		///   <summary>Gets the exclusive end date of the accepted date range with a CalendarType of None.</summary>
		///   <returns type='Date' />
		/// </signature>
	},
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the FixedDateGroup is equal to the specified object.</summary>
		///   <param name='obj' type='Object' >The object to test for equality.</param>
		///   <returns type='Boolean' >True if the object is equal to this instance; False otherwise.</returns>
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code for the FixedDateGroup.</summary>
		///   <returns type='Number' integer='true' >A number which can be used to hash this instance.</returns>
		/// </signature>
	},
	getRange: function () {
		/// <signature>
		///   <summary>Gets the accepted date range based on the specified calendar type.</summary>
		///   <param name='calendarType' type='CalendarType' >The calendar type in which to get the accepted date range.</param>
		///   <returns type='DateRange' >A DateRange where the start is the inclusive start date of the accepted date range and the end is 
		///       the exclusive end date.</returns>
		/// </signature>
	},
	start: function () {
		/// <signature>
		///   <summary>Gets the inclusive start date of the accepted date range with a CalendarType of None.</summary>
		///   <returns type='Date' />
		/// </signature>
	},
	type: function () {
		/// <signature>
		///   <summary>Gets the type, or precision, of the group.</summary>
		///   <returns type='FixedDateGroupType' />
		/// </signature>
	},
	value: function () {
		/// <signature>
		///   <summary>Gets the reference date which determines range of accepted dates.</summary>
		///   <returns type='Date' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.DateRange, function () {
		/// <signature>
		///   <summary>Represents a range of dates.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.DateRange.prototype, {
		/// <field type='Date' >The end of the date range (the later date).</field>
	end: null,
		/// <field type='Date' >The start of the date range (the earlier date).</field>
	start: null
});

intellisense.annotate(jQuery.ig.excel.FixedDateGroupCollection, function () {
	/// <summary>A collection of fixed date groups.</summary>
});
intellisense.annotate(jQuery.ig.excel.FixedDateGroupCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds a fixed date group to the collection.</summary>
		///   <param name='item' type='FixedDateGroup' >The fixed date group to add to the collection.</param>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the specified fixed date group is in the collection.</summary>
		///   <param name='item' type='FixedDateGroup' >The fixed date group to find in the collection.</param>
		///   <returns type='Boolean' >True if the item is in the collection; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of fixed date groups in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	indexOf: function () {
		/// <signature>
		///   <summary>Gets the index of the specified fixed date group in the collection.</summary>
		///   <param name='item' type='FixedDateGroup' >The fixed date group to find in the collection.</param>
		///   <returns type='Number' integer='true' >The 0-based index of the specified fixed date group in the collection or -1 if the item is not in the collection.</returns>
		/// </signature>
	},
	insert: function () {
		/// <signature>
		///   <summary>Inserts a fixed date group into the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index where the value should be inserted.</param>
		///   <param name='item' type='FixedDateGroup' >The fixed date group to insert into the collection.</param>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the fixed date group from the collection.</summary>
		///   <param name='item' type='FixedDateGroup' >The fixed date group to remove from the collection.</param>
		///   <returns type='Boolean' >True if the value was found and removed; False otherwise.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the fixed date group at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index of the value to remove.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FixedValuesFilter, function () {
	/// <summary>Represents a filter which can filter cells based on specific, fixed values, which are allowed to display.</summary>
});
intellisense.annotate(jQuery.ig.excel.FixedValuesFilter.prototype, {
	calendarType: function () {
		/// <signature>
		///   <summary>Gets the calendar type used to interpret values in the FixedValuesFilter.DateGroups collection.</summary>
		///   <returns type='CalendarType' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the calendar type used to interpret values in the FixedValuesFilter.DateGroups collection.</summary>
		///   <param name='value' type='CalendarType'  />
		///   <returns type='CalendarType' />
		/// </signature>
	},
	dateGroups: function () {
		/// <signature>
		///   <summary>Gets the collection of fixed date groups which should be filtered in.</summary>
		///   <returns type='FixedDateGroupCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the fixed date group at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index of the value to get or set.</param>
		///   <returns type='FixedDateGroup' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the fixed date group at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index of the value to get or set.</param>
		///   <param name='value' type='FixedDateGroup'  />
		///   <returns type='FixedDateGroup' />
		/// </signature>
	},
	displayValues: function () {
		/// <signature>
		///   <summary>Gets the collection of cell text values which should be filtered in.</summary>
		///   <returns type='DisplayValueCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the display text value at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index of the value to get or set.</param>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the display text value at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index of the value to get or set.</param>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	includeBlanks: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether blank cells should be filtered in.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether blank cells should be filtered in.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FontColorFilter, function () {
	/// <summary>Represents a filter which will filter cells based on their font colors.</summary>
});
intellisense.annotate(jQuery.ig.excel.FontColorFilter.prototype, {
	fontColorInfo: function () {
		/// <signature>
		///   <summary>Gets the WorkbookColorInfo which describes the font color by which the cells should be filtered.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the WorkbookColorInfo which describes the font color by which the cells should be filtered.</summary>
		///   <param name='value' type='WorkbookColorInfo'  />
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.RelativeDateRangeFilter, function () {
	/// <summary>Represents a filter which can filter date cells based on dates relative to the when the filter was applied.</summary>
});
intellisense.annotate(jQuery.ig.excel.RelativeDateRangeFilter.prototype, {
	duration: function () {
		/// <signature>
		///   <summary>Gets the duration of the full range of accepted dates.</summary>
		///   <returns type='RelativeDateRangeDuration' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the duration of the full range of accepted dates.</summary>
		///   <param name='value' type='RelativeDateRangeDuration'  />
		///   <returns type='RelativeDateRangeDuration' />
		/// </signature>
	},
	offset: function () {
		/// <signature>
		///   <summary>Gets the offset of relative filter (previous, current, or next).</summary>
		///   <returns type='RelativeDateRangeOffset' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the offset of relative filter (previous, current, or next).</summary>
		///   <param name='value' type='RelativeDateRangeOffset'  />
		///   <returns type='RelativeDateRangeOffset' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.TopOrBottomFilter, function () {
	/// <summary>Represents a filter which can filter in cells in the upper or lower portion of the sorted values.</summary>
});
intellisense.annotate(jQuery.ig.excel.TopOrBottomFilter.prototype, {
	type: function () {
		/// <signature>
		///   <summary>Gets the type of the filter.</summary>
		///   <returns type='TopOrBottomFilterType' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the type of the filter.</summary>
		///   <param name='value' type='TopOrBottomFilterType'  />
		///   <returns type='TopOrBottomFilterType' />
		/// </signature>
	},
	value: function () {
		/// <signature>
		///   <summary>Gets the number or percentage of value of values which should be filtered in.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the number or percentage of value of values which should be filtered in.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.YearToDateFilter, function () {
	/// <summary>Represents a filter which can filter in date cells if the dates occur between the start of the current year 
 ///     and the time when the filter is evaluated.</summary>
});

intellisense.annotate(jQuery.ig.excel.IWorkbookFont, function () {
	/// <summary>Represents a font for the cell in the related context.</summary>
});
intellisense.annotate(jQuery.ig.excel.IWorkbookFont.prototype, {
	bold: function () {
		/// <signature>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	colorInfo: function () {
		/// <signature>
		///   <summary>Gets the foreground color of the font.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the foreground color of the font.</summary>
		///   <param name='value' type='WorkbookColorInfo'  />
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	},
	height: function () {
		/// <signature>
		///   <summary>Gets the font height in twips (1/20th of a point).</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the font height in twips (1/20th of a point).</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	italic: function () {
		/// <signature>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	name: function () {
		/// <signature>
		///   <summary>Gets the font family name.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the font family name.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	setFontFormatting: function () {
		/// <signature>
		///   <summary>Sets all font properties to specific font formatting.</summary>
		///   <param name='source' type='IWorkbookFont' >Source font format.</param>
		/// </signature>
	},
	strikeout: function () {
		/// <signature>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	superscriptSubscriptStyle: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the font is superscript or subscript.</summary>
		///   <returns type='FontSuperscriptSubscriptStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the font is superscript or subscript.</summary>
		///   <param name='value' type='FontSuperscriptSubscriptStyle'  />
		///   <returns type='FontSuperscriptSubscriptStyle' />
		/// </signature>
	},
	underlineStyle: function () {
		/// <signature>
		///   <summary>Gets the underline style of the font.</summary>
		///   <returns type='FontUnderlineStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the underline style of the font.</summary>
		///   <param name='value' type='FontUnderlineStyle'  />
		///   <returns type='FontUnderlineStyle' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FormattedFontBase, function () {
	/// <summary>Abstract base class which controls the formatting of a range of characters in a FormattedString or FormattedText.</summary>
});
intellisense.annotate(jQuery.ig.excel.FormattedFontBase.prototype, {
	bold: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the font is bold.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the font is bold.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	colorInfo: function () {
		/// <signature>
		///   <summary>Gets the fore color of the font.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the fore color of the font.</summary>
		///   <param name='value' type='WorkbookColorInfo'  />
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	},
	height: function () {
		/// <signature>
		///   <summary>Gets the height of the font.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the height of the font.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	italic: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the font is italic.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the font is italic.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	length: function () {
		/// <signature>
		///   <summary>Gets the number of characters covered by this font. Zero indicates the font controls from 
   ///     the FormattedFontBase.StartIndex to the end of the string.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	name: function () {
		/// <signature>
		///   <summary>Gets the name of the font.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the name of the font.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	setFontFormatting: function () {
		/// <signature>
		///   <summary>Sets all properties of this font to the properties of the specified font.</summary>
		///   <param name='source' type='IWorkbookFont' >The font whose properties should be copied over to this font.</param>
		/// </signature>
	},
	startIndex: function () {
		/// <signature>
		///   <summary>Gets the index of the first character covered by this font.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	strikeout: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the font is struck out.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the font is struck out.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	superscriptSubscriptStyle: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the font is superscript or subscript.</summary>
		///   <returns type='FontSuperscriptSubscriptStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the font is superscript or subscript.</summary>
		///   <param name='value' type='FontSuperscriptSubscriptStyle'  />
		///   <returns type='FontSuperscriptSubscriptStyle' />
		/// </signature>
	},
	underlineStyle: function () {
		/// <signature>
		///   <summary>Gets the underline style of the font.</summary>
		///   <returns type='FontUnderlineStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the underline style of the font.</summary>
		///   <param name='value' type='FontUnderlineStyle'  />
		///   <returns type='FontUnderlineStyle' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FormattedString, function () {
		/// <signature>
		///   <summary>Represents a string with mixed formatting in a cell or cell comment.</summary>
		///   <param name='unformattedString' type='String' >The string that will be displayed in the cell with the formatting.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.FormattedString.prototype, {
	clone: function () {
		/// <signature>
		///   <summary>Creates a new FormattedString that is a copy of this one.</summary>
		///   <returns type='FormattedString' >A new FormattedString that is a copy of this one.</returns>
		/// </signature>
	},
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the specified Object is equal to this FormattedString.</summary>
		///   <param name='obj' type='Object' >The value to test for equality to this FormattedString.</param>
		///   <returns type='Boolean' >True if the obj is a FormattedString instance and it contains the same unformatted 
		///       string and formatting as this FormattedString; False otherwise.</returns>
		/// </signature>
	},
	getFont: function () {
		/// <signature>
		///   <summary>Gets the font which controls the formatting properties in the string from the specified start index to 
   ///     the end of the string.</summary>
		///   <param name='startIndex' type='Number' integer='true' >The index of the first character the returned font controls.</param>
		///   <returns type='FormattedStringFont' >A FormattedStringFont instance which controls the formatting of the end portion of the string.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the font which controls the formatting properties in the string from the specified start index for
   ///     the specified number of characters.</summary>
		///   <param name='startIndex' type='Number' integer='true' >The index of the first character the returned font controls.</param>
		///   <param name='length' type='Number' integer='true' >The number of characters after the start index controlled by the returned font.</param>
		///   <returns type='FormattedStringFont' >A FormattedStringFont instance which controls the formatting of a portion of the string.</returns>
		/// </signature>
	},
	getFormattingRuns: function () {
		/// <signature>
		///   <summary>Gets the collection of formatting runs representing contiguous blocks of similar formatting starting at the beginning of the string.</summary>
		///   <returns type='IEnumerable`1' />
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Calculates the has code for this FormattedString.</summary>
		///   <returns type='Number' integer='true' >A number which can be used in hashing functions.</returns>
		/// </signature>
	},
	toString: function () {
		/// <signature>
		///   <summary>Returns the String that represents this FormattedString.
   ///     This is just the unformatted string.</summary>
		///   <returns type='String' >The String that represents this FormattedString.</returns>
		/// </signature>
	},
	unformattedString: function () {
		/// <signature>
		///   <summary>Gets the unformatted string.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the unformatted string.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FormattedStringFont, function () {
	/// <summary>Controls the formatting of a range of characters in a FormattedStringFont.FormattedString.</summary>
});
intellisense.annotate(jQuery.ig.excel.FormattedStringFont.prototype, {
	formattedString: function () {
		/// <signature>
		///   <summary>Gets the formatted string which is controlled by this font.</summary>
		///   <returns type='FormattedString' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FormattedText, function () {
		/// <signature>
		///   <summary>Represents text with multiple paragraphs and mixed formatting in a shape.</summary>
		///   <param name='unformattedString' type='String' >The string that will be displayed in the shape. Each line of the string will be placed in a separate paragraph of the 
		///       FormattedText.Paragraphs collection.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.FormattedText.prototype, {
	clone: function () {
		/// <signature>
		///   <summary>Creates a new FormattedText that is a copy of this one.</summary>
		///   <returns type='FormattedText' >A new FormattedText that is a copy of this one.</returns>
		/// </signature>
	},
	getFont: function () {
		/// <signature>
		///   <summary>Gets the font which controls the formatting properties in the string from the specified start index to 
   ///     the end of the string.</summary>
		///   <param name='startIndex' type='Number' integer='true' >The index of the first character the returned font controls.</param>
		///   <returns type='FormattedTextFont' >A FormattedTextFont instance which controls the formatting of the end portion of the string.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the font which controls the formatting properties in the string from the specified start index for
   ///     the specified number of characters.</summary>
		///   <param name='startIndex' type='Number' integer='true' >The index of the first character the returned font controls.</param>
		///   <param name='length' type='Number' integer='true' >The number of characters after the start index controlled by the returned font.</param>
		///   <returns type='FormattedTextFont' >A FormattedTextFont instance which controls the formatting of a portion of the string.</returns>
		/// </signature>
	},
	getFormattingRuns: function () {
		/// <signature>
		///   <summary>Gets the collection of formatting runs representing contiguous blocks of similar formatting starting at the beginning of the string.</summary>
		///   <returns type='IEnumerable`1' />
		/// </signature>
	},
	paragraphs: function () {
		/// <signature>
		///   <summary>Gets the paragraphs in the formatted text.</summary>
		///   <returns type='FormattedTextParagraphCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the paragraph at the specified zero-based index.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the paragraph to get.</param>
		///   <returns type='FormattedTextParagraph' >The FormattedTextParagraph representing the paragraph at the specified index.</returns>
		/// </signature>
	},
	toString: function () {
		/// <signature>
		///   <summary>Returns the string that represents the FormattedText, which is the unformatted string.</summary>
		///   <returns type='String' >The string that represents the FormattedText.</returns>
		/// </signature>
	},
	verticalAlignment: function () {
		/// <signature>
		///   <summary>Gets the vertical alignment of the formatted text in the owning shape.</summary>
		///   <returns type='VerticalTextAlignment' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the vertical alignment of the formatted text in the owning shape.</summary>
		///   <param name='value' type='VerticalTextAlignment'  />
		///   <returns type='VerticalTextAlignment' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FormattedTextFont, function () {
	/// <summary>Controls the formatting of a range of characters in FormattedText.</summary>
});
intellisense.annotate(jQuery.ig.excel.FormattedTextFont.prototype, {
	formattedText: function () {
		/// <signature>
		///   <summary>Gets the FormattedText which is controlled by this font.</summary>
		///   <returns type='FormattedText' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FormattedTextParagraph, function () {
	/// <summary>Represents a paragraph in FormattedText.</summary>
});
intellisense.annotate(jQuery.ig.excel.FormattedTextParagraph.prototype, {
	alignment: function () {
		/// <signature>
		///   <summary>Gets the alignment of the paragraph.</summary>
		///   <returns type='HorizontalTextAlignment' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the alignment of the paragraph.</summary>
		///   <param name='value' type='HorizontalTextAlignment'  />
		///   <returns type='HorizontalTextAlignment' />
		/// </signature>
	},
	formattedText: function () {
		/// <signature>
		///   <summary>Gets the owning FormattedText to which the paragraph belongs or null if the 
   ///     paragraph has been removed from its owning formatted text.</summary>
		///   <returns type='FormattedText' />
		/// </signature>
	},
	startIndex: function () {
		/// <signature>
		///   <summary>Gets the zero-based index of the paragraph's first character in the overall formatted text.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	unformattedString: function () {
		/// <signature>
		///   <summary>Gets the raw string of the paragraph.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the raw string of the paragraph.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FormattedTextParagraphCollection, function () {
	/// <summary>A collection of FormattedTextParagraph instances in FormattedText.</summary>
});
intellisense.annotate(jQuery.ig.excel.FormattedTextParagraphCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds a paragraph with the specified text and returns the FormattedTextParagraph 
   ///     representing the new paragraph.</summary>
		///   <param name='paragraphText' type='String' >The text in the paragraph.</param>
		///   <returns type='FormattedTextParagraph' >The FormattedTextParagraph representing the new paragraph.</returns>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Removes all paragraphs from the formatted text.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the specified paragraph exists in the collection.</summary>
		///   <param name='paragraph' type='FormattedTextParagraph' >The paragraph to find in the collection.</param>
		///   <returns type='Boolean' >True if the paragraph exists in the collection; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of paragraphs in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	getEnumerator: function () {
		/// <signature>
		///   <summary>Gets the enumerator to iterate over all paragraphs.</summary>
		///   <returns type='IEnumerator`1' />
		/// </signature>
	},
	indexOf: function () {
		/// <signature>
		///   <summary>Gets the zero-based index of the specified paragraph in the collection.</summary>
		///   <param name='paragraph' type='FormattedTextParagraph' >The paragraph to find in the collection.</param>
		///   <returns type='Number' integer='true' >The zero-based index of the paragraph in the collection or -1 if the paragraph doesn't exist in the collection.</returns>
		/// </signature>
	},
	insert: function () {
		/// <signature>
		///   <summary>Inserts a paragraph with the specified text and returns the FormattedTextParagraph 
   ///     representing the new paragraph.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index in the collection where the paragraph should be inserted.</param>
		///   <param name='paragraphText' type='String' >The text in the paragraph.</param>
		///   <returns type='FormattedTextParagraph' >The FormattedTextParagraph representing the new paragraph.</returns>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified paragraph from the collection.</summary>
		///   <param name='paragraph' type='FormattedTextParagraph' >The paragraph to remove from the collection.</param>
		///   <returns type='Boolean' >True if the paragraph was in the collection and was removed; False otherwise.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the paragraph at the specified zero-based index in the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the paragraph to remove.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.Formula, function () {
	/// <summary>Represents a formula for a cell or group of cells.</summary>
});
intellisense.annotate(jQuery.ig.excel.Formula, {
	equalsStatic: function () {
		/// <signature>
		///   <summary>Determines whether two Formula instances are equal using the specified cell reference mode.</summary>
		///   <param name='formulaA' type='Formula' >The first Formula to compare.</param>
		///   <param name='formulaB' type='Formula' >The second Formula to compare.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode to use when comparing the two formulas.</param>
		///   <returns type='Boolean' >True if the formulas are both null or both equivalent; False otherwise.</returns>
		/// </signature>
	},
	parse: function () {
		/// <signature>
		///   <summary>Parses the specified formula value and returns the formula which was created from it.</summary>
		///   <param name='value' type='String' >The string which defines the formula to parse.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to interpret cell references in the formula.</param>
		///   <returns type='Formula' >A Formula instance which represents the formula specified.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Parses the specified formula value and returns the formula which was created from it.</summary>
		///   <param name='value' type='String' >The string which defines the formula to parse.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to interpret cell references in the formula.</param>
		///   <param name='culture' type='String' >The culture used to parse the formula.</param>
		///   <returns type='Formula' >A Formula instance which represents the formula specified.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Parses the specified formula value and returns the formula which was created from it.</summary>
		///   <param name='value' type='String' >The string which defines the formula to parse.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to interpret cell references in the formula.</param>
		///   <param name='fileFormat' type='WorkbookFormat' >The file format to use when parsing the formula. This will be used to determine certain limits which are format dependant.</param>
		///   <returns type='Formula' >A Formula instance which represents the formula specified.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Parses the specified formula value and returns the formula which was created from it.</summary>
		///   <param name='value' type='String' >The string which defines the formula to parse.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to interpret cell references in the formula.</param>
		///   <param name='fileFormat' type='WorkbookFormat' >The file format to use when parsing the formula. This will be used to determine certain limits which are format dependant.</param>
		///   <param name='culture' type='String' >The culture used to parse the formula.</param>
		///   <returns type='Formula' >A Formula instance which represents the formula specified.</returns>
		/// </signature>
	}
});
intellisense.annotate(jQuery.ig.excel.Formula.prototype, {
	applyTo: function () {
		/// <signature>
		///   <summary>Applies the formula to the specified cell.</summary>
		///   <param name='cell' type='WorksheetCell' >The cell to apply the formula to.</param>
		/// </signature>
		/// <signature>
		///   <summary>Applies the formula to the specified region of cells.</summary>
		///   <param name='region' type='WorksheetRegion' >The region of cells to apply the formula to.</param>
		/// </signature>
		/// <signature>
		///   <summary>Applies the formula to all specified regions of cells.</summary>
		///   <param name='regions' type='Array' elementType='WorksheetRegion' >The regions of cells to apply the formula to.</param>
		/// </signature>
	},
	toString: function () {
		/// <signature>
		///   <summary>Converts the formula to a string representation, similar to the string with which it was created. 
   ///     This uses the CellReferenceMode with which the formula was created to create cell
   ///     reference strings.</summary>
		///   <returns type='String' >The string representing the formula.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Converts the formula to a string representation, similar to the string with which it was created.</summary>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode used to create cell reference strings.</param>
		///   <returns type='String' >The string representing the formula.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Converts the formula to a string representation, similar to the string with which it was created.</summary>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode used to create cell reference strings.</param>
		///   <param name='culture' type='String' >The culture used to generate the formula string.</param>
		///   <returns type='String' >The string representing the formula.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ArrayFormula, function () {
	/// <summary>Represents an array formula for a group of cells.</summary>
});
intellisense.annotate(jQuery.ig.excel.ArrayFormula, {
	parse: function () {
		/// <signature>
		///   <summary>Parses the specified formula value and returns the array formula which was created from it.</summary>
		///   <param name='value' type='String' >The string which defines the array formula to parse.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to interpret cell references in the array formula.</param>
		///   <returns type='ArrayFormula' >An ArrayFormula instance which represents the array formula value specified.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Parses the specified formula value and returns the array formula which was created from it.</summary>
		///   <param name='value' type='String' >The string which defines the array formula to parse.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to interpret cell references in the array formula.</param>
		///   <param name='culture' type='String' >The culture used to parse the formula.</param>
		///   <returns type='ArrayFormula' >An ArrayFormula instance which represents the array formula value specified.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Parses the specified formula value and returns the array formula which was created from it.</summary>
		///   <param name='value' type='String' >The string which defines the array formula to parse.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to interpret cell references in the array formula.</param>
		///   <param name='fileFormat' type='WorkbookFormat' >The file format to use when parsing the array formula. This will be used to determine certain limits which are format dependant.</param>
		///   <returns type='ArrayFormula' >An ArrayFormula instance which represents the array formula value specified.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Parses the specified formula value and returns the array formula which was created from it.</summary>
		///   <param name='value' type='String' >The string which defines the array formula to parse.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to interpret cell references in the array formula.</param>
		///   <param name='fileFormat' type='WorkbookFormat' >The file format to use when parsing the array formula. This will be used to determine certain limits which are format dependant.</param>
		///   <param name='culture' type='String' >The culture used to parse the formula.</param>
		///   <returns type='ArrayFormula' >An ArrayFormula instance which represents the array formula value specified.</returns>
		/// </signature>
	}
});
intellisense.annotate(jQuery.ig.excel.ArrayFormula.prototype, {
	cellRange: function () {
		/// <signature>
		///   <summary>Gets the cells to which the array formula is applied.</summary>
		///   <returns type='WorksheetRegion' />
		/// </signature>
	},
	clearCellRange: function () {
		/// <signature>
		///   <summary>Removes this array formula as the formula for the cells to which it was applied.</summary>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FormulaParseException, function () {
		/// <signature>
		///   <summary>The exception thrown when a formula parse error occurs.</summary>
		/// </signature>
		/// <signature>
		///   <summary>The exception thrown when a formula parse error occurs.</summary>
		///   <param name='message' type='String' >The message that describes the error.</param>
		/// </signature>
		/// <signature>
		///   <summary>The exception thrown when a formula parse error occurs.</summary>
		///   <param name='message' type='String' >The message that describes the error.</param>
		///   <param name='innerException' type='Error' >The exception that is the cause of this exception.</param>
		/// </signature>
		/// <signature>
		///   <summary>The exception thrown when a formula parse error occurs.</summary>
		///   <param name='charIndexOfError' type='Number' integer='true' >The character index in the formulaValue parameter where the parse error occurred.</param>
		///   <param name='formulaValue' type='String' >The formula which had the error being parsed.</param>
		///   <param name='message' type='String' >The message that describes the error.</param>
		///   <param name='portionWithError' type='String' >The portion of the formula which had the error being parsed.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.FormulaParseException.prototype, {
	charIndexOfError: function () {
		/// <signature>
		///   <summary>Gets the character index in the FormulaParseException.FormulaValue at which the parse error occurred.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	formulaValue: function () {
		/// <signature>
		///   <summary>Gets the formula string which had the error being parsed.</summary>
		///   <returns type='String' />
		/// </signature>
	},
	message: function () {
		/// <signature>
		///   <summary>Gets the error message and the portion of the formula with the error.</summary>
		///   <returns type='String' />
		/// </signature>
	},
	portionWithError: function () {
		/// <signature>
		///   <summary>Gets the portion of the formula which contains the error.</summary>
		///   <returns type='String' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.PaneSettingsBase, function () {
	/// <summary>Abstract base class for classes which control pane settings.</summary>
});
intellisense.annotate(jQuery.ig.excel.PaneSettingsBase.prototype, {
	firstColumnInRightPane: function () {
		/// <signature>
		///   <summary>Gets the first visible column in the right pane(s) of the worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the first visible column in the right pane(s) of the worksheet.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	firstRowInBottomPane: function () {
		/// <signature>
		///   <summary>Gets the first visible row in the bottom pane(s) of the worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the first visible row in the bottom pane(s) of the worksheet.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	reset: function () {
		/// <signature>
		///   <summary>Resets the pane settings to their defaults.</summary>
		/// </signature>
	},
	resetCore: function () {
		/// <signature>
		///   <summary>Resets the pane settings to their defaults.</summary>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FrozenPaneSettings, function () {
	/// <summary>Class which controls the way frozen panes are arranged and used for a worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.FrozenPaneSettings.prototype, {
	frozenColumns: function () {
		/// <signature>
		///   <summary>Gets the number of columns frozen at the left of the worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the number of columns frozen at the left of the worksheet.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	frozenRows: function () {
		/// <signature>
		///   <summary>Gets the number of rows frozen at the top of the worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the number of rows frozen at the top of the worksheet.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	resetCore: function () {
		/// <signature>
		///   <summary>Resets the frozen pane settings to their defaults.</summary>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.RowColumnBase, function () {
	/// <summary>Abstract base class for worksheet row and worksheet column.</summary>
});
intellisense.annotate(jQuery.ig.excel.RowColumnBase.prototype, {
	cellFormat: function () {
		/// <signature>
		///   <summary>Gets the default cell format for cells in this row or column.</summary>
		///   <returns type='IWorksheetCellFormat' />
		/// </signature>
	},
	getResolvedCellFormat: function () {
		/// <signature>
		///   <summary>Gets the resolved cell formatting for the cells in the row or column.</summary>
		///   <returns type='IWorksheetCellFormat' >A format object describing the actual formatting that will be used when displayed the row or column in Microsoft Excel.</returns>
		/// </signature>
	},
	hidden: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the row or column is hidden.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the row or column is hidden.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	index: function () {
		/// <signature>
		///   <summary>Gets the 0-based index of the row or column in the worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	outlineLevel: function () {
		/// <signature>
		///   <summary>Gets the outline level for the row or column.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the outline level for the row or column.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	worksheet: function () {
		/// <signature>
		///   <summary>Gets the worksheet to which the row or column belongs.</summary>
		///   <returns type='Worksheet' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetColumn, function () {
	/// <summary>Represents a column in a Microsoft Excel worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetColumn.prototype, {
	getWidth: function () {
		/// <signature>
		///   <summary>Gets the column width in the specified units, or NaN if the column has the default width.</summary>
		///   <param name='units' type='WorksheetColumnWidthUnit' >The units in which the width should be returned.</param>
		///   <returns type='Number' >The column width in the specified units, or NaN if the column has the default width.</returns>
		/// </signature>
	},
	index: function () {
		/// <signature>
		///   <summary>Gets the 0-based index of the column in the worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	setWidth: function () {
		/// <signature>
		///   <summary>Sets the column width in the specified units.</summary>
		///   <param name='value' type='Number' >The width to set on the column, expressed in the specified units.</param>
		///   <param name='units' type='WorksheetColumnWidthUnit' >The units in which the value is expressed.</param>
		/// </signature>
	},
	width: function () {
		/// <signature>
		///   <summary>Gets the column width including padding, in 256ths of the '0' digit character width in the workbook's default font.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the column width including padding, in 256ths of the '0' digit character width in the workbook's default font.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.HiddenColumnCollection, function () {
	/// <summary>A collection of hidden WorksheetColumn instances.</summary>
});
intellisense.annotate(jQuery.ig.excel.HiddenColumnCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds a column to the collection.</summary>
		///   <param name='column' type='WorksheetColumn' >The column to be added to the hidden columns collection.</param>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears all columns from the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the specified column exists in the collection.</summary>
		///   <param name='column' type='WorksheetColumn' >The column to search for in the collection.</param>
		///   <returns type='Boolean' >True if the column exists in the collection; False otherwise or if the specified column is null.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of columns in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified column from the collection if it exists.</summary>
		///   <param name='column' type='WorksheetColumn' >The column to removed from the collection.</param>
		///   <returns type='Boolean' >True if the column existed in the collection and was removed; False otherwise or if the 
		///       specified column is null.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the column at the specified index in the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the column in the collection.</param>
		/// </signature>
	},
	worksheet: function () {
		/// <signature>
		///   <summary>Gets the worksheet associated with this collection.</summary>
		///   <returns type='Worksheet' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetRow, function () {
	/// <summary>Represents a row in a Microsoft Excel worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetRow.prototype, {
	applyCellFormula: function () {
		/// <signature>
		///   <summary>Applies a formula to the cell at the specified column index.</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <param name='formula' type='String' >The formula to parse and apply to the cell.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' optional='true' >The mode used to interpret cell references in the formula.</param>
		/// </signature>
	},
	cells: function () {
		/// <signature>
		///   <summary>Gets the collection of cells in the row.</summary>
		///   <returns type='WorksheetCellCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the cell at the specified column index in the owning row.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based column index of the cell to get.</param>
		///   <returns type='WorksheetCell' />
		/// </signature>
	},
	getCellAssociatedDataTable: function () {
		/// <signature>
		///   <summary>Gets the data table to which the cell at the specified index belongs.</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <returns type='WorksheetDataTable' >The data table to which the cell belongs or null if the cell does not belong to a data table.</returns>
		/// </signature>
	},
	getCellAssociatedMergedCellsRegion: function () {
		/// <signature>
		///   <summary>Gets the merged cells region which contains the cell at the specified index, or null if the cell is not merged.</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <returns type='WorksheetMergedCellsRegion' >The merged cells region which contains the cell at the specified index, or null if the cell is not merged.</returns>
		/// </signature>
	},
	getCellAssociatedTable: function () {
		/// <signature>
		///   <summary>Gets the WorksheetTable to which the cell at the specified index belongs.</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <returns type='WorksheetTable' />
		/// </signature>
	},
	getCellBoundsInTwips: function () {
		/// <signature>
		///   <summary>Gets the bounds of the cell at the specified column index in twips (1/20th of a point).</summary>
		///   <param name='columnIndex' type='Number' integer='true'  />
		///   <returns type='Object' >The bounds of the cell at the specified column index on its worksheet. This will be an object with numeric values for properties 'x', 'y', 'width', 'height', 'left', 'top', 'right', and 'bottom'.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the bounds of the cell at the specified column index in twips (1/20th of a point).</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <param name='options' type='PositioningOptions' >The options to use when getting the bounds of the cell.</param>
		///   <returns type='Object' >The bounds of the cell at the specified column index on its worksheet. This will be an object with numeric values for properties 'x', 'y', 'width', 'height', 'left', 'top', 'right', and 'bottom'.</returns>
		/// </signature>
	},
	getCellComment: function () {
		/// <signature>
		///   <summary>Gets or sets the comment applied to the cell at the specified column index.</summary>
		///   <param name='columnIndex' type='Number' integer='true'  />
		///   <returns type='WorksheetCellComment' >The comment applied to the cell.</returns>
		/// </signature>
	},
	getCellFormat: function () {
		/// <signature>
		///   <summary>Gets the cell formatting for the cell at the specified column index.</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <returns type='IWorksheetCellFormat' >The cell formatting for the cell at the specified column index.</returns>
		/// </signature>
	},
	getCellFormula: function () {
		/// <signature>
		///   <summary>Gets the formula which has been applied to the cell at the specified column index.</summary>
		///   <param name='columnIndex' type='Number' integer='true'  />
		///   <returns type='Formula' >The formula which has been applied to the cell or null if no formula has been applied.</returns>
		/// </signature>
	},
	getCellHyperlink: function () {
		/// <signature>
		///   <summary>Gets the effective hyperlink on the cell at the specified index.</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <returns type='WorksheetHyperlink' />
		/// </signature>
	},
	getCellText: function () {
		/// <signature>
		///   <summary>Gets the display text in the cell at the specified index.</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the text in the cell at the specified index.</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <param name='textFormatMode' type='TextFormatMode' >The format mode to use when getting the cell text.</param>
		///   <returns type='String' />
		/// </signature>
	},
	getCellValue: function () {
		/// <signature>
		///   <summary>Gets the value of the cell at the specified column index.</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <returns type='Object' />
		/// </signature>
	},
	getResolvedCellFormat: function () {
		/// <signature>
		///   <summary>Gets the resolved cell formatting for the cell at the specified column index.</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <returns type='IWorksheetCellFormat' >A format object describing the actual formatting that will be used when displayed the cell in Microsoft Excel.</returns>
		/// </signature>
	},
	height: function () {
		/// <signature>
		///   <summary>Gets the height of the row in twips (1/20th of a point).</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the height of the row in twips (1/20th of a point).</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	index: function () {
		/// <signature>
		///   <summary>Gets the 0-based index of the row in the worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	setCellComment: function () {
		/// <signature>
		///   <summary>Sets the comment applied to the cell at the specified column index.</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <param name='comment' type='WorksheetCellComment' >The comment to apply to the cell.</param>
		/// </signature>
	},
	setCellValue: function () {
		/// <signature>
		///   <summary>Sets the value of a cell at the specified column index.</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <param name='value' type='Object' >The value to assign to the cell.</param>
		/// </signature>
	},
	validateCellValue: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the value of the cell is valid considering its associated WorksheetCell.DataValidationRule</summary>
		///   <param name='columnIndex' type='Number' integer='true' >The 0-based index of the cell within the WorksheetRow.</param>
		///   <returns type='Boolean' >Returns false if the cell has an associated WorksheetCell.DataValidationRule and the current WorksheetCell.Value does not pass the validation defined by that rule; otherwise true is returned.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.HiddenRowCollection, function () {
	/// <summary>A collection of hidden WorksheetRow instances.</summary>
});
intellisense.annotate(jQuery.ig.excel.HiddenRowCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds a row to the collection.</summary>
		///   <param name='row' type='WorksheetRow' >The row to be added to the hidden rows collection.</param>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears all rows from the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the specified row exists in the collection.</summary>
		///   <param name='row' type='WorksheetRow' >The row to search for in the collection.</param>
		///   <returns type='Boolean' >True if the row exists in the collection; False otherwise or if the specified row is null.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of rows in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified row from the collection if it exists.</summary>
		///   <param name='row' type='WorksheetRow' >The row to removed from the collection.</param>
		///   <returns type='Boolean' >True if the row existed in the collection and was removed; False otherwise or if the 
		///       specified row is null.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the row at the specified index in the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the row in the collection.</param>
		/// </signature>
	},
	worksheet: function () {
		/// <signature>
		///   <summary>Gets the worksheet associated with this collection.</summary>
		///   <returns type='Worksheet' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.IWorksheetCellFormat, function () {
	/// <summary>Represents the format for the cell.</summary>
});
intellisense.annotate(jQuery.ig.excel.IWorksheetCellFormat.prototype, {
	alignment: function () {
		/// <signature>
		///   <summary>Gets the horizontal alignment of the content in a cell.</summary>
		///   <returns type='HorizontalCellAlignment' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the horizontal alignment of the content in a cell.</summary>
		///   <param name='value' type='HorizontalCellAlignment'  />
		///   <returns type='HorizontalCellAlignment' />
		/// </signature>
	},
	bottomBorderColorInfo: function () {
		/// <signature>
		///   <summary>Gets the bottom border color.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the bottom border color.</summary>
		///   <param name='value' type='WorkbookColorInfo'  />
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	},
	bottomBorderStyle: function () {
		/// <signature>
		///   <summary>Gets the bottom border style.</summary>
		///   <returns type='CellBorderLineStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the bottom border style.</summary>
		///   <param name='value' type='CellBorderLineStyle'  />
		///   <returns type='CellBorderLineStyle' />
		/// </signature>
	},
	diagonalBorderColorInfo: function () {
		/// <signature>
		///   <summary>Gets the diagonal border color.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the diagonal border color.</summary>
		///   <param name='value' type='WorkbookColorInfo'  />
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	},
	diagonalBorders: function () {
		/// <signature>
		///   <summary>Gets the diagonal borders.</summary>
		///   <returns type='DiagonalBorders' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the diagonal borders.</summary>
		///   <param name='value' type='DiagonalBorders'  />
		///   <returns type='DiagonalBorders' />
		/// </signature>
	},
	diagonalBorderStyle: function () {
		/// <signature>
		///   <summary>Gets the diagonal border style.</summary>
		///   <returns type='CellBorderLineStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the diagonal border style.</summary>
		///   <param name='value' type='CellBorderLineStyle'  />
		///   <returns type='CellBorderLineStyle' />
		/// </signature>
	},
	fill: function () {
		/// <signature>
		///   <summary>Gets the fill of the cell.</summary>
		///   <returns type='CellFill' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the fill of the cell.</summary>
		///   <param name='value' type='CellFill'  />
		///   <returns type='CellFill' />
		/// </signature>
	},
	font: function () {
		/// <signature>
		///   <summary>Gets or sets the default font formatting.</summary>
		///   <returns type='IWorkbookFont' />
		/// </signature>
	},
	formatOptions: function () {
		/// <signature>
		///   <summary>Gets the groups of properties provided by the format.</summary>
		///   <returns type='WorksheetCellFormatOptions' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the groups of properties provided by the format.</summary>
		///   <param name='value' type='WorksheetCellFormatOptions'  />
		///   <returns type='WorksheetCellFormatOptions' />
		/// </signature>
	},
	formatString: function () {
		/// <signature>
		///   <summary>Gets the number format string.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the number format string.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	indent: function () {
		/// <signature>
		///   <summary>Gets the indent in units of average character widths.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the indent in units of average character widths.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	leftBorderColorInfo: function () {
		/// <signature>
		///   <summary>Gets the left border color.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the left border color.</summary>
		///   <param name='value' type='WorkbookColorInfo'  />
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	},
	leftBorderStyle: function () {
		/// <signature>
		///   <summary>Gets the left border style.</summary>
		///   <returns type='CellBorderLineStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the left border style.</summary>
		///   <param name='value' type='CellBorderLineStyle'  />
		///   <returns type='CellBorderLineStyle' />
		/// </signature>
	},
	locked: function () {
		/// <signature>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	rightBorderColorInfo: function () {
		/// <signature>
		///   <summary>Gets the right border color.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the right border color.</summary>
		///   <param name='value' type='WorkbookColorInfo'  />
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	},
	rightBorderStyle: function () {
		/// <signature>
		///   <summary>Gets the right border style.</summary>
		///   <returns type='CellBorderLineStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the right border style.</summary>
		///   <param name='value' type='CellBorderLineStyle'  />
		///   <returns type='CellBorderLineStyle' />
		/// </signature>
	},
	rotation: function () {
		/// <signature>
		///   <summary>Gets the rotation of the cell content in degrees.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the rotation of the cell content in degrees.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	setFormatting: function () {
		/// <signature>
		///   <summary>Copies all cell format properties to from the specified cell format.</summary>
		///   <param name='source' type='IWorksheetCellFormat' >The cell format from which to copy the properties.</param>
		/// </signature>
	},
	shrinkToFit: function () {
		/// <signature>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	style: function () {
		/// <signature>
		///   <summary>Gets the parent WorkbookStyle of the format.</summary>
		///   <returns type='WorkbookStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the parent WorkbookStyle of the format.</summary>
		///   <param name='value' type='WorkbookStyle'  />
		///   <returns type='WorkbookStyle' />
		/// </signature>
	},
	topBorderColorInfo: function () {
		/// <signature>
		///   <summary>Gets the top border color.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the top border color.</summary>
		///   <param name='value' type='WorkbookColorInfo'  />
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	},
	topBorderStyle: function () {
		/// <signature>
		///   <summary>Gets the top border style.</summary>
		///   <returns type='CellBorderLineStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the top border style.</summary>
		///   <param name='value' type='CellBorderLineStyle'  />
		///   <returns type='CellBorderLineStyle' />
		/// </signature>
	},
	verticalAlignment: function () {
		/// <signature>
		///   <summary>Gets the vertical alignment of the content in a cell.</summary>
		///   <returns type='VerticalCellAlignment' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the vertical alignment of the content in a cell.</summary>
		///   <param name='value' type='VerticalCellAlignment'  />
		///   <returns type='VerticalCellAlignment' />
		/// </signature>
	},
	wrapText: function () {
		/// <signature>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.NamedReferenceBase, function () {
	/// <summary>Abstract base class for a named reference defined in the workbook.</summary>
});
intellisense.annotate(jQuery.ig.excel.NamedReferenceBase.prototype, {
	comment: function () {
		/// <signature>
		///   <summary>Gets the comment associated with the named reference or table.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the comment associated with the named reference or table.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	name: function () {
		/// <signature>
		///   <summary>Gets the name of the reference.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the name of the reference.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	scope: function () {
		/// <signature>
		///   <summary>Gets the scope of the named reference.</summary>
		///   <returns type='Object' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.NamedReference, function () {
	/// <summary>Represents a named reference defined in the workbook.</summary>
});
intellisense.annotate(jQuery.ig.excel.NamedReference.prototype, {
	formula: function () {
		/// <signature>
		///   <summary>Gets the formula which defines the named reference.</summary>
		///   <returns type='String' />
		/// </signature>
	},
	isSimpleReferenceFormula: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the NamedReference.Formula is a simple formula referring to a single cell, a single region, 
   ///     or multiple regions in the same workbook as the named reference.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	referencedCell: function () {
		/// <signature>
		///   <summary>Gets the WorksheetCell referenced by the NamedReference.Formula.</summary>
		///   <returns type='WorksheetCell' />
		/// </signature>
	},
	referencedRegion: function () {
		/// <signature>
		///   <summary>Gets the WorksheetRegion referenced by the NamedReference.Formula.</summary>
		///   <returns type='WorksheetRegion' />
		/// </signature>
	},
	referencedRegions: function () {
		/// <signature>
		///   <summary>Gets the array of WorksheetRegion instances referenced by the NamedReference.Formula.</summary>
		///   <returns type='Array' elementType='WorksheetRegion' />
		/// </signature>
	},
	setFormula: function () {
		/// <signature>
		///   <summary>Sets the formula for a named reference.</summary>
		///   <param name='formula' type='String' >The string containing the formula value.</param>
		/// </signature>
		/// <signature>
		///   <summary>Sets the formula for a named reference.</summary>
		///   <param name='formula' type='String' >The string containing the formula value.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to interpret cell references in the formula.</param>
		/// </signature>
		/// <signature>
		///   <summary>Sets the formula for a named reference.</summary>
		///   <param name='formula' type='String' >The string containing the formula value.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to interpret cell references in the formula.</param>
		///   <param name='culture' type='String' >The culture used to parse the formula.</param>
		/// </signature>
	},
	toString: function () {
		/// <signature>
		///   <summary>Gets the string representation of the named reference.</summary>
		///   <returns type='String' >The string representation of the named reference.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.NamedReferenceCollection, function () {
	/// <summary>A collection of NamedReference instances in a workbook.</summary>
});
intellisense.annotate(jQuery.ig.excel.NamedReferenceCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds a named reference with a scope of the collection's associated NamedReferenceCollection.Workbook to the collection.</summary>
		///   <param name='name' type='String' >The name to give the named reference.</param>
		///   <param name='formula' type='String' >The formula to give the named reference.</param>
		///   <returns type='NamedReference' >The named reference which was added to the collection.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Adds a named reference with a scope of the collection's associated NamedReferenceCollection.Workbook to the collection.</summary>
		///   <param name='name' type='String' >The name to give the named reference.</param>
		///   <param name='formula' type='String' >The formula to give the named reference.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to interpret cell references in the formula.</param>
		///   <returns type='NamedReference' >The named reference which was added to the collection.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Adds a named reference with a scope of a worksheet to the collection.</summary>
		///   <param name='name' type='String' >The name to give the named reference.</param>
		///   <param name='formula' type='String' >The formula to give the named reference.</param>
		///   <param name='worksheet' type='Worksheet' >The scope of the named reference.</param>
		///   <returns type='NamedReference' >The named reference which was added to the collection.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Adds a named reference with a scope of a worksheet to the collection.</summary>
		///   <param name='name' type='String' >The name to give the named reference.</param>
		///   <param name='formula' type='String' >The formula to give the named reference.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The mode used to interpret cell references in the formula.</param>
		///   <param name='worksheet' type='Worksheet' >The scope of the named reference.</param>
		///   <returns type='NamedReference' >The named reference which was added to the collection.</returns>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears all named references from the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether a named reference is in the collection.</summary>
		///   <param name='namedReference' type='NamedReference' >The named reference to locate in the collection.</param>
		///   <returns type='Boolean' >True if the named reference is found; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of named references in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	find: function () {
		/// <signature>
		///   <summary>Finds a named reference in the collection with a scope of the collection's associated Workbook.</summary>
		///   <param name='name' type='String' >The name of the named reference to find.</param>
		///   <returns type='NamedReference' >The named reference with the specified name or null if the named reference was not found.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Finds a named reference in the collection with a scope of the specified worksheet.</summary>
		///   <param name='name' type='String' >The name of the named reference to find.</param>
		///   <param name='worksheetScope' type='Worksheet' >The worksheet that the named reference found must have a scope of.</param>
		///   <returns type='NamedReference' >The named reference with the specified name or null if the named reference was not found.</returns>
		/// </signature>
	},
	findAll: function () {
		/// <signature>
		///   <summary>Finds all named references in the collection with the specified name.</summary>
		///   <param name='name' type='String' >The name of the named references to find.</param>
		///   <returns type='Array' elementType='NamedReference' >An array of NamedReference instances with the specified name.</returns>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified named reference from the collection.</summary>
		///   <param name='namedReference' type='NamedReference' >The named reference to remove fro the collection.</param>
		///   <returns type='Boolean' >True if the named reference existed in the collection and was removed; False otherwise.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the named reference at the specified index in the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the named reference in the collection.</param>
		/// </signature>
	},
	workbook: function () {
		/// <signature>
		///   <summary>Gets the workbook associated with this collection.</summary>
		///   <returns type='Workbook' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.PageBreak, function () {
	/// <summary>Base class for horizontal and vertical page breaks in a Worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.PageBreak.prototype, {
	equals: function () {
		/// <signature>
		///   <summary>Determines whether this PageBreak is equal to the specified object.</summary>
		///   <param name='obj' type='Object' >The object to test.</param>
		///   <returns type='Boolean' >True if the object is the same type as this PageBreak and has the same data; False otherwise.</returns>
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code for the PageBreak.</summary>
		///   <returns type='Number' integer='true' >A hash code for the instance.</returns>
		/// </signature>
	},
	printArea: function () {
		/// <signature>
		///   <summary>Gets the print area in which the page break occurs.</summary>
		///   <returns type='WorksheetRegion' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.HorizontalPageBreak, function () {
		/// <signature>
		///   <summary>Represents a horizontal page break in a Worksheet.</summary>
		///   <param name='firstRowOnPage' type='Number' integer='true' >The 0-based index of the first row on the page after this break.</param>
		/// </signature>
		/// <signature>
		///   <summary>Represents a horizontal page break in a Worksheet.</summary>
		///   <param name='firstRowOnPage' type='Number' integer='true' >The 0-based index of the first row on the page after this break.</param>
		///   <param name='printArea' type='WorksheetRegion' >The print area in which the page break should occur or null to break in the entire sheet.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.HorizontalPageBreak.prototype, {
	firstRowOnPage: function () {
		/// <signature>
		///   <summary>Gets the 0-based index of the first row on the page after this break.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.PageBreakCollection$1, function () {
	/// <summary>Base class for the collections of horizontal and vertical page breaks on a PageBreakCollection`1.Worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.PageBreakCollection$1.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds a page break to the PageBreakCollection`1.Worksheet.</summary>
		///   <param name='pageBreak' type='T' >The page break to add to the Worksheet.</param>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears the collection of page breaks.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the specified page break exists on the PageBreakCollection`1.Worksheet.</summary>
		///   <param name='pageBreak' type='T' >The page break to test.</param>
		///   <returns type='Boolean' >True if the page break is on the Worksheet; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of page breaks in this collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	indexOf: function () {
		/// <signature>
		///   <summary>Gets the 0-based index of the specified page break.</summary>
		///   <param name='pageBreak' type='T' >The page break for which the index should be obtained.</param>
		///   <returns type='Number' integer='true' >The 0-based index of the page break or -1 if the page break is no tin the collection.</returns>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified page break from the PageBreakCollection`1.Worksheet.</summary>
		///   <param name='pageBreak' type='T' >The page break which should be removed.</param>
		///   <returns type='Boolean' >True if the page break was contained on the Worksheet before removal; False otherwise.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the page break at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index of the page break to remove.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.HorizontalPageBreakCollection, function () {
	/// <summary>A collection of horizontal page breaks on a Worksheet.</summary>
});

intellisense.annotate(jQuery.ig.excel.VerticalPageBreak, function () {
		/// <signature>
		///   <summary>Represents a vertical page break in a Worksheet.</summary>
		///   <param name='firstColumOnPage' type='Number' integer='true' >The 0-based index of the first column on the page after this break.</param>
		/// </signature>
		/// <signature>
		///   <summary>Represents a vertical page break in a Worksheet.</summary>
		///   <param name='firstColumOnPage' type='Number' integer='true' >The 0-based index of the first column on the page after this break.</param>
		///   <param name='printArea' type='WorksheetRegion' >The print area in which the page break should occur or null to break in the entire sheet.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.VerticalPageBreak.prototype, {
	firstColumnOnPage: function () {
		/// <signature>
		///   <summary>Gets the 0-based index of the first column on the page after this break.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.VerticalPageBreakCollection, function () {
	/// <summary>A collection of horizontal page breaks on a Worksheet.</summary>
});

intellisense.annotate(jQuery.ig.excel.WorksheetShape, function () {
	/// <summary>Abstract base class for all shapes (atomic or compound) in a worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetShape, {
	createPredefinedShape: function () {
		/// <signature>
		///   <summary>Creates a shape which is predefined in Microsoft Excel.</summary>
		///   <param name='shapeType' type='PredefinedShapeType' >The type of shape to create.</param>
		///   <returns type='WorksheetShape' >A WorksheetShape-derived instance representing the predefined shape.</returns>
		/// </signature>
	}
});
intellisense.annotate(jQuery.ig.excel.WorksheetShape.prototype, {
	bottomRightCornerCell: function () {
		/// <signature>
		///   <summary>Gets the cell where the bottom-right corner of the shape resides.</summary>
		///   <returns type='WorksheetCell' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the cell where the bottom-right corner of the shape resides.</summary>
		///   <param name='value' type='WorksheetCell'  />
		///   <returns type='WorksheetCell' />
		/// </signature>
	},
	bottomRightCornerPosition: function () {
		/// <signature>
		///   <summary>Gets the position in the WorksheetShape.BottomRightCornerCell of the shape's bottom-right corner,
   ///     expressed in percentages.</summary>
		///   <returns type='Object' > This will be an object with numeric values for properties 'x' and 'y'.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Sets the position in the WorksheetShape.BottomRightCornerCell of the shape's bottom-right corner,
   ///     expressed in percentages.</summary>
		///   <param name='value' type='Object' > This can be an object with numeric values for properties 'x' and 'y', such as { x: 1, y: 2 }.</param>
		///   <returns type='Object' > This will be an object with numeric values for properties 'x' and 'y'.</returns>
		/// </signature>
	},
	clearUnknownData: function () {
		/// <signature>
		///   <summary>Clears the cached unknown shape data which was read in from a parsed excel file.</summary>
		/// </signature>
	},
	fill: function () {
		/// <signature>
		///   <summary>Gets the fill to use in the background of the shape.</summary>
		///   <returns type='ShapeFill' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the fill to use in the background of the shape.</summary>
		///   <param name='value' type='ShapeFill'  />
		///   <returns type='ShapeFill' />
		/// </signature>
	},
	flippedHorizontally: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the shape is flipped horizontally along the vertical center line.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the shape is flipped horizontally along the vertical center line.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	flippedVertically: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the shape is flipped vertically along the horizontal center line.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the shape is flipped vertically along the horizontal center line.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	getBoundsInTwips: function () {
		/// <signature>
		///   <summary>Gets the bounds of the shape in twips (1/20th of a point).</summary>
		///   <returns type='Object' >The bounds of the shape on its worksheet. This will be an object with numeric values for properties 'x', 'y', 'width', 'height', 'left', 'top', 'right', and 'bottom'.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the bounds of the shape in twips (1/20th of a point).</summary>
		///   <param name='options' type='PositioningOptions' >The options to use when getting the bounds of the shape.</param>
		///   <returns type='Object' >The bounds of the shape on its worksheet. This will be an object with numeric values for properties 'x', 'y', 'width', 'height', 'left', 'top', 'right', and 'bottom'.</returns>
		/// </signature>
	},
	outline: function () {
		/// <signature>
		///   <summary>Gets the outline to use for the shape.</summary>
		///   <returns type='ShapeOutline' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the outline to use for the shape.</summary>
		///   <param name='value' type='ShapeOutline'  />
		///   <returns type='ShapeOutline' />
		/// </signature>
	},
	positioningMode: function () {
		/// <signature>
		///   <summary>Gets the way shapes will be repositioned in excel when cells before or within the shape are resized.</summary>
		///   <returns type='ShapePositioningMode' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the way shapes will be repositioned in excel when cells before or within the shape are resized.</summary>
		///   <param name='value' type='ShapePositioningMode'  />
		///   <returns type='ShapePositioningMode' />
		/// </signature>
	},
	setBoundsInTwips: function () {
		/// <signature>
		///   <summary>Sets the bounds of the shape in twips (1/20th of a point).</summary>
		///   <param name='worksheet' type='Worksheet' >The worksheet on which the shape should be placed.</param>
		///   <param name='bounds' type='Object' >The new bounds where the shape should be placed. This can be an object with numeric values for properties 'x', 'y', 'width', and 'height' or 'left', 'top', 'right', and 'bottom' such as { x: 1, y: 2, width: 100, height: 200 }.</param>
		/// </signature>
		/// <signature>
		///   <summary>Sets the bounds of the shape in twips (1/20th of a point).</summary>
		///   <param name='worksheet' type='Worksheet' >The worksheet on which the shape should be placed.</param>
		///   <param name='bounds' type='Object' >The new bounds where the shape should be placed. This can be an object with numeric values for properties 'x', 'y', 'width', and 'height' or 'left', 'top', 'right', and 'bottom' such as { x: 1, y: 2, width: 100, height: 200 }.</param>
		///   <param name='options' type='PositioningOptions' >The options to use when setting the bounds of the shape.</param>
		/// </signature>
	},
	topLeftCornerCell: function () {
		/// <signature>
		///   <summary>Gets the cell where the top-left corner of the shape resides.</summary>
		///   <returns type='WorksheetCell' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the cell where the top-left corner of the shape resides.</summary>
		///   <param name='value' type='WorksheetCell'  />
		///   <returns type='WorksheetCell' />
		/// </signature>
	},
	topLeftCornerPosition: function () {
		/// <signature>
		///   <summary>Gets the position in the WorksheetShape.TopLeftCornerCell of the shape's top-left corner,
   ///     expressed in percentages.</summary>
		///   <returns type='Object' > This will be an object with numeric values for properties 'x' and 'y'.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Sets the position in the WorksheetShape.TopLeftCornerCell of the shape's top-left corner,
   ///     expressed in percentages.</summary>
		///   <param name='value' type='Object' > This can be an object with numeric values for properties 'x' and 'y', such as { x: 1, y: 2 }.</param>
		///   <returns type='Object' > This will be an object with numeric values for properties 'x' and 'y'.</returns>
		/// </signature>
	},
	visible: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the shape is visible on the worksheet.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the shape is visible on the worksheet.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	worksheet: function () {
		/// <signature>
		///   <summary>Gets the worksheet on which the shape resides.</summary>
		///   <returns type='Worksheet' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetShapeWithText, function () {
	/// <summary>Abstract base class for all shapes that can display text.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetShapeWithText.prototype, {
	text: function () {
		/// <signature>
		///   <summary>Gets the formatted text of the shape.</summary>
		///   <returns type='FormattedText' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the formatted text of the shape.</summary>
		///   <param name='value' type='FormattedText'  />
		///   <returns type='FormattedText' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.DiamondShape, function () {
		/// <signature>
		///   <summary>Represents the diamond shape.</summary>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.EllipseShape, function () {
		/// <signature>
		///   <summary>Represents the ellipse shape.</summary>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.HeartShape, function () {
		/// <signature>
		///   <summary>Represents the heart shape.</summary>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.IrregularSeal1Shape, function () {
		/// <signature>
		///   <summary>Represents the irregular seal 1 shape.</summary>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.IrregularSeal2Shape, function () {
		/// <signature>
		///   <summary>Represents the irregular seal 2 shape.</summary>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.LightningBoltShape, function () {
		/// <signature>
		///   <summary>Represents the lightning bolt shape.</summary>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.LineShape, function () {
		/// <signature>
		///   <summary>Represents the line shape.</summary>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.PentagonShape, function () {
		/// <signature>
		///   <summary>Represents the pentagon shape.</summary>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.RectangleShape, function () {
		/// <signature>
		///   <summary>Represents the rectangle shape.</summary>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.RightTriangleShape, function () {
		/// <signature>
		///   <summary>Represents the right triangle shape.</summary>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.StraightConnector1Shape, function () {
		/// <signature>
		///   <summary>Represents the straight connector 1 shape.</summary>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.PrintAreasCollection, function () {
	/// <summary>Gets the collection of print areas in a Worksheet or a worksheet's print settings in a CustomView.</summary>
});
intellisense.annotate(jQuery.ig.excel.PrintAreasCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds a print area to the collection.</summary>
		///   <param name='printArea' type='WorksheetRegion' >The print area to add to the collection.</param>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears all print areas from the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the specified print area is in the collection.</summary>
		///   <param name='printArea' type='WorksheetRegion' >The print area to search for in the collection.</param>
		///   <returns type='Boolean' >True if the print area is in the collection; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of print areas in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified print area from the collection.</summary>
		///   <param name='printArea' type='WorksheetRegion' >The print area to remove from the collection.</param>
		///   <returns type='Boolean' >True if the print area was found and removed; False otherwise.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the print area at the specified index from the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The index of the print area to remove from the collection.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.PrintOptions, function () {
	/// <summary>Class which exposes the various print options available for a worksheet which can be saved with both a 
 ///     worksheet and a custom view.</summary>
});
intellisense.annotate(jQuery.ig.excel.PrintOptions.prototype, {
	alignHeadersAndFootersWithMargins: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether to align header and footer margins with page margins.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether to align header and footer margins with page margins.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	bottomMargin: function () {
		/// <signature>
		///   <summary>Gets the margin at the bottom of each printed page of the worksheet, specified in inches.</summary>
		///   <returns type='Number' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the margin at the bottom of each printed page of the worksheet, specified in inches.</summary>
		///   <param name='value' type='Number'  />
		///   <returns type='Number' />
		/// </signature>
	},
	centerHorizontally: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the printed pages should be centered horizontally.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the printed pages should be centered horizontally.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	centerVertically: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the printed pages should be centered vertically.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the printed pages should be centered vertically.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	clearPageBreaks: function () {
		/// <signature>
		///   <summary>Clears all page breaks from the PrintOptions.</summary>
		/// </signature>
	},
	columnsToRepeatAtLeft: function () {
		/// <signature>
		///   <summary>Gets the range of columns which should be printed on every page.</summary>
		///   <returns type='RepeatTitleRange' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the range of columns which should be printed on every page.</summary>
		///   <param name='value' type='RepeatTitleRange'  />
		///   <returns type='RepeatTitleRange' />
		/// </signature>
	},
	draftQuality: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the printed pages should be printed using draft quality.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the printed pages should be printed using draft quality.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	footer: function () {
		/// <signature>
		///   <summary>Gets the footer for each page of the printed worksheet.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the footer for each page of the printed worksheet.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	footerMargin: function () {
		/// <signature>
		///   <summary>Gets the footer margin for each printed page of the worksheet, specified in inches.</summary>
		///   <returns type='Number' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the footer margin for each printed page of the worksheet, specified in inches.</summary>
		///   <param name='value' type='Number'  />
		///   <returns type='Number' />
		/// </signature>
	},
	header: function () {
		/// <signature>
		///   <summary>Gets the header for each page of the printed worksheet.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the header for each page of the printed worksheet.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	headerMargin: function () {
		/// <signature>
		///   <summary>Gets the header margin for each printed page of the worksheet, specified in inches.</summary>
		///   <returns type='Number' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the header margin for each printed page of the worksheet, specified in inches.</summary>
		///   <param name='value' type='Number'  />
		///   <returns type='Number' />
		/// </signature>
	},
	horizontalPageBreaks: function () {
		/// <signature>
		///   <summary>Gets the collection of horizontal page breaks in the Worksheet.</summary>
		///   <returns type='HorizontalPageBreakCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the page break at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The index of the page break to get.</param>
		///   <returns type='HorizontalPageBreak' >A PageBreak-derived instance.</returns>
		/// </signature>
	},
	insertPageBreak: function () {
		/// <signature>
		///   <summary>Inserts a horizontal and/or vertical page break before the specified cell.</summary>
		///   <param name='cell' type='WorksheetCell' >The cell at which to insert the page break(s).</param>
		/// </signature>
		/// <signature>
		///   <summary>Inserts a vertical page break to the left of the specified column.</summary>
		///   <param name='column' type='WorksheetColumn' >The column at which to insert the page break.</param>
		/// </signature>
		/// <signature>
		///   <summary>Inserts a horizontal page break above the specified row.</summary>
		///   <param name='row' type='WorksheetRow' >The row at which to insert the page break.</param>
		/// </signature>
	},
	leftMargin: function () {
		/// <signature>
		///   <summary>Gets the margin at the left of each printed page of the worksheet, specified in inches.</summary>
		///   <returns type='Number' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the margin at the left of each printed page of the worksheet, specified in inches.</summary>
		///   <param name='value' type='Number'  />
		///   <returns type='Number' />
		/// </signature>
	},
	maxPagesHorizontally: function () {
		/// <signature>
		///   <summary>Gets the maximum number of pages allowed in the horizontal direction to print the worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the maximum number of pages allowed in the horizontal direction to print the worksheet.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	maxPagesVertically: function () {
		/// <signature>
		///   <summary>Gets the maximum number of pages allowed in the vertical direction to print the worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the maximum number of pages allowed in the vertical direction to print the worksheet.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	numberOfCopies: function () {
		/// <signature>
		///   <summary>Gets the number of copies to print.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the number of copies to print.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	orientation: function () {
		/// <signature>
		///   <summary>Gets the orientation for each page of the printed worksheet.</summary>
		///   <returns type='Orientation' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the orientation for each page of the printed worksheet.</summary>
		///   <param name='value' type='Orientation'  />
		///   <returns type='Orientation' />
		/// </signature>
	},
	orientationResolved: function () {
		/// <signature>
		///   <summary>Returns the resolved orientation for each page of the printed worksheet.</summary>
		///   <returns type='Orientation' />
		/// </signature>
	},
	pageNumbering: function () {
		/// <signature>
		///   <summary>Gets the method with which pages are numbered.</summary>
		///   <returns type='PageNumbering' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the method with which pages are numbered.</summary>
		///   <param name='value' type='PageNumbering'  />
		///   <returns type='PageNumbering' />
		/// </signature>
	},
	pageOrder: function () {
		/// <signature>
		///   <summary>Gets the order in which to print pages for multiple page worksheets.</summary>
		///   <returns type='PageOrder' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the order in which to print pages for multiple page worksheets.</summary>
		///   <param name='value' type='PageOrder'  />
		///   <returns type='PageOrder' />
		/// </signature>
	},
	paperSize: function () {
		/// <signature>
		///   <summary>Gets the paper size for each printed page of the worksheet.</summary>
		///   <returns type='PaperSize' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the paper size for each printed page of the worksheet.</summary>
		///   <param name='value' type='PaperSize'  />
		///   <returns type='PaperSize' />
		/// </signature>
	},
	printAreas: function () {
		/// <signature>
		///   <summary>Gets the collection of print areas in the Worksheet.</summary>
		///   <returns type='PrintAreasCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the print area at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The index of the print area to get.</param>
		///   <returns type='WorksheetRegion' >A WorksheetRegion instance representing a print area in the Worksheet.</returns>
		/// </signature>
	},
	printErrors: function () {
		/// <signature>
		///   <summary>Gets the way error values of cells are printed.</summary>
		///   <returns type='PrintErrors' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the way error values of cells are printed.</summary>
		///   <param name='value' type='PrintErrors'  />
		///   <returns type='PrintErrors' />
		/// </signature>
	},
	printGridlines: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether to print the worksheet gridlines.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether to print the worksheet gridlines.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	printInBlackAndWhite: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the worksheet should be printed in black and white.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the worksheet should be printed in black and white.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	printNotes: function () {
		/// <signature>
		///   <summary>Gets the way cell comments are printed.</summary>
		///   <returns type='PrintNotes' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the way cell comments are printed.</summary>
		///   <param name='value' type='PrintNotes'  />
		///   <returns type='PrintNotes' />
		/// </signature>
	},
	printRowAndColumnHeaders: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether to print row and column headers.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether to print row and column headers.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	reset: function () {
		/// <signature>
		///   <summary>Resets the print options to their default settings.</summary>
		/// </signature>
	},
	resolution: function () {
		/// <signature>
		///   <summary>Gets the horizontal print resolution in DPI.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the horizontal print resolution in DPI.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	rightMargin: function () {
		/// <signature>
		///   <summary>Gets the margin at the right of each printed page of the worksheet, specified in inches.</summary>
		///   <returns type='Number' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the margin at the right of each printed page of the worksheet, specified in inches.</summary>
		///   <param name='value' type='Number'  />
		///   <returns type='Number' />
		/// </signature>
	},
	rowsToRepeatAtTop: function () {
		/// <signature>
		///   <summary>Gets the range of rows which should be printed on every page.</summary>
		///   <returns type='RepeatTitleRange' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the range of rows which should be printed on every page.</summary>
		///   <param name='value' type='RepeatTitleRange'  />
		///   <returns type='RepeatTitleRange' />
		/// </signature>
	},
	scaleHeadersAndFootersWithDocument: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether to scale the headers and footers with the document scaling.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether to scale the headers and footers with the document scaling.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	scalingFactor: function () {
		/// <signature>
		///   <summary>Gets the scaling factor to use when printing the worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the scaling factor to use when printing the worksheet.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	scalingType: function () {
		/// <signature>
		///   <summary>Gets the method for scaling the worksheet when it is printed.</summary>
		///   <returns type='ScalingType' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the method for scaling the worksheet when it is printed.</summary>
		///   <param name='value' type='ScalingType'  />
		///   <returns type='ScalingType' />
		/// </signature>
	},
	startPageNumber: function () {
		/// <signature>
		///   <summary>Gets the page number for the first printed page of the worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the page number for the first printed page of the worksheet.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	topMargin: function () {
		/// <signature>
		///   <summary>Gets the margin at the top of each printed page of the worksheet, specified in inches.</summary>
		///   <returns type='Number' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the margin at the top of each printed page of the worksheet, specified in inches.</summary>
		///   <param name='value' type='Number'  />
		///   <returns type='Number' />
		/// </signature>
	},
	verticalPageBreaks: function () {
		/// <signature>
		///   <summary>Gets the collection of vertical page breaks in the Worksheet.</summary>
		///   <returns type='VerticalPageBreakCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the page break at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The index of the page break to get.</param>
		///   <returns type='VerticalPageBreak' >A PageBreak-derived instance.</returns>
		/// </signature>
	},
	verticalResolution: function () {
		/// <signature>
		///   <summary>Gets the vertical print resolution in DPI.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the vertical print resolution in DPI.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorkbookProtection, function () {
	/// <summary>Provides information about the types of changes that are disabled when the associated Workbook is protected.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorkbookProtection.prototype, {
	allowEditStructure: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the sheets displayed may be modified when the Workbook is protected.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	allowEditWindows: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the Workbook windows may be modified when the Workbook is protected.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetProtectedRangeCollection, function () {
	/// <summary>A collection of WorksheetProtectedRange instances in a Worksheet</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetProtectedRangeCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds the specified range to the collection.</summary>
		///   <param name='item' type='WorksheetProtectedRange' >The range to be added</param>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears all ranges from the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether a range is in the collection.</summary>
		///   <param name='range' type='WorksheetProtectedRange' >The range to locate in the collection.</param>
		///   <returns type='Boolean' >True if the range is found; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of ranges in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	getEnumerator: function () {
		/// <signature>
		///   <summary>Gets an enumerator which can iterator all ranges in the collection.</summary>
		///   <returns type='IEnumerator`1' />
		/// </signature>
	},
	insert: function () {
		/// <signature>
		///   <summary>Inserts the specified range in the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index in the collection where the range should be inserted.</param>
		///   <param name='range' type='WorksheetProtectedRange' >The range to insert in the collection.</param>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified range from the collection.</summary>
		///   <param name='range' type='WorksheetProtectedRange' >The range to remove from the collection.</param>
		///   <returns type='Boolean' >True if the range was successfully removed from the collection; 
		///       False if the range did not exist in the collection.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the range at the specified index from the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The index of the range to remove from the collection.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetProtection, function () {
	/// <summary>Provides information about the types of changes that are disabled when the associated Worksheet is protected.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetProtection.prototype, {
	allowDeletingColumns: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the deletion of columns is allowed in a protected worksheet.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	allowDeletingRows: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the deletion of rows is allowed in a protected worksheet.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	allowedEditRanges: function () {
		/// <signature>
		///   <summary>Returns a collection of WorksheetProtectedRange instances that define which locked ranges of cells may be edited in a protected worksheet.</summary>
		///   <returns type='WorksheetProtectedRangeCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the range at the specified index in the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the range to get.</param>
		///   <returns type='WorksheetProtectedRange' />
		/// </signature>
	},
	allowEditObjects: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if objects such as shapes, comments, charts and controls may be created or changed when the worksheet is protected.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	allowEditScenarios: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if existing scenarios are protected when the worksheet is protected.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	allowFiltering: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the user may use an auto filter created before the worksheet was protected.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	allowFormattingCells: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the user may change the formatting of cells in a protected worksheet.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	allowFormattingColumns: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the user may change column information such as its width or visibility in a protected worksheet.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	allowFormattingRows: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the user may change row information such as its height or visibility in a protected worksheet.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	allowInsertingColumns: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the user may insert columns in a protected worksheet.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	allowInsertingHyperlinks: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the user may insert hyperlinks in unlocked cells in a protected worksheet.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	allowInsertingRows: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the user may insert rows in a protected worksheet.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	allowSorting: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the sorting is allowed in a protected worksheet.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	allowUsingPivotTables: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the user can change pivot tables in a protected worksheet.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	selectionMode: function () {
		/// <signature>
		///   <summary>Returns an enumeration used to indicate how cell selection should be affected when the associated Worksheet is protected.</summary>
		///   <returns type='WorksheetProtectedSelectionMode' />
		/// </signature>
		/// <signature>
		///   <summary>Sets an enumeration used to indicate how cell selection should be affected when the associated Worksheet is protected.</summary>
		///   <param name='value' type='WorksheetProtectedSelectionMode'  />
		///   <returns type='WorksheetProtectedSelectionMode' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.RepeatTitleRange, function () {
		/// <signature>
		///   <summary>Represents a range of contiguous rows or columns which should be repeated at the top or left or printed pages of the Worksheet.</summary>
		///   <param name='startIndex' type='Number' integer='true' >The index of the first row or column in the range.</param>
		///   <param name='endIndex' type='Number' integer='true' >The index of the last row or column in the range.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.RepeatTitleRange.prototype, {
	endIndex: function () {
		/// <signature>
		///   <summary>Gets the index of the last row or column in the range.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the specified value equals this RepeatTitleRange.</summary>
		///   <param name='obj' type='Object' >The value to test for equality.</param>
		///   <returns type='Boolean' />
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gtes the hash code for the RepeatTitleRange.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	startIndex: function () {
		/// <signature>
		///   <summary>Gets the index of the first row or column in the range.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	toString: function () {
		/// <signature>
		///   <summary>Gets the string representation of the range.</summary>
		///   <returns type='String' >The string representation of the range.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetItemCollection$1, function () {
	/// <summary>Abstract base class for collections of the main worksheet elements (rows, columns, and cells).</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetItemCollection$1.prototype, {
	maxCount: function () {
		/// <signature>
		///   <summary>Gets the maximum number of items allowed in this collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.RowColumnCollectionBase$1, function () {
	/// <summary>Base class for row collection and column collection.</summary>
});

intellisense.annotate(jQuery.ig.excel.ISortable, function () {
	/// <summary>A type representing a range of cells which can be sorted.</summary>
});
intellisense.annotate(jQuery.ig.excel.ISortable.prototype, {
	index: function () {
		/// <signature>
		///   <summary>Gets the index of the sort-able item in its owner.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetTableColumn, function () {
	/// <summary>Represents a column in a WorksheetTable.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetTableColumn.prototype, {
	applyAverageFilter: function () {
		/// <signature>
		///   <summary>Applies an AverageFilter to the column.</summary>
		///   <param name='type' type='AverageFilterType' >The value indicating whether to filter in values below or above the average of the data range.</param>
		///   <returns type='AverageFilter' />
		/// </signature>
	},
	applyCustomFilter: function () {
		/// <signature>
		///   <summary>Applies a CustomFilter to the column.</summary>
		///   <param name='condition' type='CustomFilterCondition' >The condition which must pass for the data to be filtered in.</param>
		///   <returns type='CustomFilter' />
		/// </signature>
		/// <signature>
		///   <summary>Applies a CustomFilter to the column.</summary>
		///   <param name='condition1' type='CustomFilterCondition' >The first condition used to filter the data.</param>
		///   <param name='condition2' type='CustomFilterCondition' >The second condition used to filter the data.</param>
		///   <param name='conditionalOperator' type='ConditionalOperator' >The operator which defines how to logically combine condition1 and condition2.</param>
		///   <returns type='CustomFilter' />
		/// </signature>
	},
	applyDatePeriodFilter: function () {
		/// <signature>
		///   <summary>Applies an DatePeriodFilter to the column.</summary>
		///   <param name='type' type='DatePeriodFilterType' >The type of date period to filter in.</param>
		///   <param name='value' type='Number' integer='true' >The 1-based value of the month or quarter to filter in.</param>
		///   <returns type='DatePeriodFilter' />
		/// </signature>
	},
	applyFillFilter: function () {
		/// <signature>
		///   <summary>Applies a FillFilter to the column.</summary>
		///   <param name='fill' type='CellFill' >A CellFill by which the cells should be filtered.</param>
		///   <returns type='FillFilter' />
		/// </signature>
	},
	applyFixedValuesFilter: function () {
		/// <signature>
		///   <summary>Applies a FixedValuesFilter to the column.</summary>
		///   <param name='includeBlanks' type='Boolean' >The value which indicates whether blank cells should be filtered in.</param>
		///   <param name='calendarType' type='CalendarType' >The calendar type used to interpret values in the dateGroups collection.</param>
		///   <param name='dateGroups' type='Array' elementType='FixedDateGroup' parameterArray='true' >The collection of fixed date groups which should be filtered in.</param>
		///   <returns type='FixedValuesFilter' />
		/// </signature>
	},
	applyFontColorFilter: function () {
		/// <signature>
		///   <summary>Applies a FontColorFilter to the column.</summary>
		///   <param name='fontColor' type='String' >The font color by which the cells should be filtered.</param>
		///   <returns type='FontColorFilter' />
		/// </signature>
		/// <signature>
		///   <summary>Applies a FontColorFilter to the column.</summary>
		///   <param name='fontColorInfo' type='WorkbookColorInfo' >A WorkbookColorInfo which describes the font color by which the cells should be filtered.</param>
		///   <returns type='FontColorFilter' />
		/// </signature>
	},
	applyRelativeDateRangeFilter: function () {
		/// <signature>
		///   <summary>Applies a RelativeDateRangeFilter to the column.</summary>
		///   <param name='offset' type='RelativeDateRangeOffset' >The offset of relative filter. This combined with the duration determines the full range of accepted dates.</param>
		///   <param name='duration' type='RelativeDateRangeDuration' >The duration of the full range of accepted dates.</param>
		///   <returns type='RelativeDateRangeFilter' />
		/// </signature>
	},
	applyTopOrBottomFilter: function () {
		/// <signature>
		///   <summary>Applies a TopOrBottomFilter to the column which will filter in the top 10 values in the list of sorted values.</summary>
		///   <returns type='TopOrBottomFilter' />
		/// </signature>
		/// <signature>
		///   <summary>Applies a TopOrBottomFilter to the column.</summary>
		///   <param name='type' type='TopOrBottomFilterType' >The type of the filter.</param>
		///   <param name='value' type='Number' integer='true' >The number or percentage of value of values which should be filtered in.</param>
		///   <returns type='TopOrBottomFilter' />
		/// </signature>
	},
	applyYearToDateFilter: function () {
		/// <signature>
		///   <summary>Applies a YearToDateFilter to the column.</summary>
		///   <returns type='YearToDateFilter' />
		/// </signature>
	},
	areaFormats: function () {
		/// <signature>
		///   <summary>Gets the collection of formats used for each area of the column.</summary>
		///   <returns type='WorksheetTableAreaFormatsCollection`1' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the format for the specified area.</summary>
		///   <param name='area' type='WorksheetTableColumnArea' >The area for which to get the format.</param>
		///   <returns type='IWorksheetCellFormat' />
		/// </signature>
	},
	clearFilter: function () {
		/// <signature>
		///   <summary>Removes the filter from the column if one is applied.</summary>
		/// </signature>
	},
	columnFormula: function () {
		/// <signature>
		///   <summary>Gets the formula associated with the data area of the column.</summary>
		///   <returns type='Formula' />
		/// </signature>
	},
	dataAreaRegion: function () {
		/// <signature>
		///   <summary>Gets the WorksheetRegion which represents the region of cells in the data area of the column.</summary>
		///   <returns type='WorksheetRegion' />
		/// </signature>
	},
	filter: function () {
		/// <signature>
		///   <summary>Gets the filter applied to the column.</summary>
		///   <returns type='Filter' />
		/// </signature>
	},
	headerCell: function () {
		/// <signature>
		///   <summary>Gets the WorksheetCell which represents the header cell for the column.</summary>
		///   <returns type='WorksheetCell' />
		/// </signature>
	},
	index: function () {
		/// <signature>
		///   <summary>Gets the 0-based index of the column in the owning WorksheetTable.Columns collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	name: function () {
		/// <signature>
		///   <summary>Gets the name of the column.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the name of the column.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	setColumnFormula: function () {
		/// <signature>
		///   <summary>Sets the formula to use in the data cells in the column.</summary>
		///   <param name='formula' type='Formula' >The formula for the data cells of the column or null to remove the current column formula.</param>
		///   <param name='overwriteExistingValues' type='Boolean' >True to overwrite the existing cells values and apply the formula to all data cells in the column. 
		///       False to only apply the formula to the cells with no value set.</param>
		/// </signature>
	},
	sortCondition: function () {
		/// <signature>
		///   <summary>Gets the sort condition used to sort the column in the table.</summary>
		///   <returns type='SortCondition' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the sort condition used to sort the column in the table.</summary>
		///   <param name='value' type='SortCondition'  />
		///   <returns type='SortCondition' />
		/// </signature>
	},
	table: function () {
		/// <signature>
		///   <summary>Gets the WorksheetTable to which the column belongs.</summary>
		///   <returns type='WorksheetTable' />
		/// </signature>
	},
	totalCell: function () {
		/// <signature>
		///   <summary>Gets the WorksheetCell which represents the total cell for the column.</summary>
		///   <returns type='WorksheetCell' />
		/// </signature>
	},
	totalFormula: function () {
		/// <signature>
		///   <summary>Gets the formula to use in the total cell of the column.</summary>
		///   <returns type='Formula' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the formula to use in the total cell of the column.</summary>
		///   <param name='value' type='Formula'  />
		///   <returns type='Formula' />
		/// </signature>
	},
	totalLabel: function () {
		/// <signature>
		///   <summary>Gets the text label to use in the total cell of the column.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the text label to use in the total cell of the column.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	wholeColumnRegion: function () {
		/// <signature>
		///   <summary>Gets the WorksheetRegion which represents the region of cells in the whole column, including the header and total cells, 
   ///     if visible.</summary>
		///   <returns type='WorksheetRegion' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetShapeGroup, function () {
		/// <signature>
		///   <summary>Represents a group of shapes in a worksheet.  This group is also a shape which can be 
   ///     positioned and contained within another group or on a worksheet.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetShapeGroup.prototype, {
	shapes: function () {
		/// <signature>
		///   <summary>Gets the collection of shapes contained in the group.</summary>
		///   <returns type='WorksheetShapeCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the shape at the specified index in the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the shape to get.</param>
		///   <returns type='WorksheetShape' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ShapeFill, function () {
		/// <signature>
		///   <summary>Abstract base class for the fill of a shape.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ShapeFill, {
	fromColor: function () {
		/// <signature>
		///   <summary>Creates an instance to describe a solid fill outline.</summary>
		///   <param name='solidColor' type='String' >The color of the fill to create.</param>
		///   <returns type='ShapeFill' >A ShapeFillSolid instance with the specified color.</returns>
		/// </signature>
	},
	fromColorInfo: function () {
		/// <signature>
		///   <summary>Creates an instance to describe a solid fill outline.</summary>
		///   <param name='solidColorInfo' type='WorkbookColorInfo' >The WorkbookColorInfo describing the color of the fill to create.</param>
		///   <returns type='ShapeFill' >A ShapeFillSolid instance with the specified color.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ShapeOutline, function () {
		/// <signature>
		///   <summary>Abstract base class for the outline of a shape.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ShapeOutline, {
	fromColor: function () {
		/// <signature>
		///   <summary>Creates an instance to describe a solid color outline.</summary>
		///   <param name='solidColor' type='String' >The color of the outline to create.</param>
		///   <returns type='ShapeOutline' >A ShapeOutlineSolid instance with the specified color.</returns>
		/// </signature>
	},
	fromColorInfo: function () {
		/// <signature>
		///   <summary>Creates an instance to describe a solid color outline.</summary>
		///   <param name='solidColorInfo' type='WorkbookColorInfo' >The WorkbookColorInfo describing the color of the outline to create.</param>
		///   <returns type='ShapeOutline' >A ShapeOutlineSolid instance with the specified color.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.Worksheet, function () {
	/// <summary>Represents one worksheet in a Microsoft Excel workbook.</summary>
});
intellisense.annotate(jQuery.ig.excel.Worksheet.prototype, {
	clearConditionalFormattingData: function () {
		/// <signature>
		///   <summary>Clears all conditional formatting rules from the Worksheet so they do not get saved.</summary>
		/// </signature>
	},
	columns: function () {
		/// <signature>
		///   <summary>Gets the collection of columns in the worksheet.</summary>
		///   <returns type='WorksheetColumnCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the column at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the column to get.</param>
		///   <returns type='WorksheetColumn' />
		/// </signature>
	},
	dataTables: function () {
		/// <signature>
		///   <summary>Gets the collection of data tables in the worksheet.</summary>
		///   <returns type='WorksheetDataTableCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the data table at the specified index in the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the data table to get.</param>
		///   <returns type='WorksheetDataTable' />
		/// </signature>
	},
	dataValidationRules: function () {
		/// <signature>
		///   <summary>Gets the collection of data validation rules applied to cells in the Worksheet.</summary>
		///   <returns type='DataValidationRuleCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the references which have the data validation rule applied to it.</summary>
		///   <param name='rule' type='DataValidationRule' >A data validation rule.</param>
		///   <returns type='WorksheetReferenceCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the references which have the data validation rule applied to it.</summary>
		///   <param name='rule' type='DataValidationRule' >A data validation rule.</param>
		///   <param name='value' type='WorksheetReferenceCollection'  />
		///   <returns type='WorksheetReferenceCollection' />
		/// </signature>
	},
	defaultColumnWidth: function () {
		/// <signature>
		///   <summary>Gets the default column width including padding, in 256ths of the '0' digit character width in the workbook's default font.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the default column width including padding, in 256ths of the '0' digit character width in the workbook's default font.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	defaultRowHeight: function () {
		/// <signature>
		///   <summary>Gets the default row height in twips (1/20th of a point).</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the default row height in twips (1/20th of a point).</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	deleteCells: function () {
		/// <signature>
		///   <summary>Deletes a region of cells.</summary>
		///   <param name='region' type='WorksheetRegion' >The region to delete</param>
		///   <param name='shiftCellsLeft' type='Boolean' >If true will shift cells left from the right of the region to occupy the deleted region, otherwise will shift cells up from below the region.</param>
		/// </signature>
		/// <signature>
		///   <summary>Deletes a region of cells.</summary>
		///   <param name='address' type='String' >The address of the region to delete</param>
		///   <param name='shiftCellsLeft' type='Boolean' >If true will shift cells left from the right of the region to occupy the deleted region, otherwise will shift cells up from below the region.</param>
		/// </signature>
	},
	displayOptions: function () {
		/// <signature>
		///   <summary>Gets the object which controls the display of the worksheet.</summary>
		///   <returns type='WorksheetDisplayOptions' />
		/// </signature>
	},
	getCell: function () {
		/// <signature>
		///   <summary>Gets the cell at the specified address or name.</summary>
		///   <param name='address' type='String' >The address or name of the cell.</param>
		///   <returns type='WorksheetCell' >A cell represented by the specified address or name.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the cell at the specified address or name.</summary>
		///   <param name='address' type='String' >The address or name of the cell.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode to use to parse the cell address.</param>
		///   <returns type='WorksheetCell' >A cell represented by the specified address or name.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the cell at the specified address or name.</summary>
		///   <param name='address' type='String' >The address or name of the cell.</param>
		///   <param name='originCell' type='WorksheetCell' >The origin for resolving relative R1C1 references.</param>
		///   <returns type='WorksheetCell' >A cell represented by the specified address or name.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the cell at the specified address or name.</summary>
		///   <param name='address' type='String' >The address or name of the cell.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode to use to parse the cell address.</param>
		///   <param name='originCell' type='WorksheetCell' >The origin for resolving relative R1C1 references.</param>
		///   <returns type='WorksheetCell' >A cell represented by the specified address or name.</returns>
		/// </signature>
	},
	getDefaultColumnWidth: function () {
		/// <signature>
		///   <summary>Gets the default column width in the specified units.</summary>
		///   <param name='units' type='WorksheetColumnWidthUnit' >The units in which the width should be returned.</param>
		///   <returns type='Number' >The default column width in the specified units.</returns>
		/// </signature>
	},
	getRegion: function () {
		/// <signature>
		///   <summary>Gets the region at the specified address or name.</summary>
		///   <param name='address' type='String' >The address or name of the region.</param>
		///   <returns type='WorksheetRegion' >A region represented by the specified address or name.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the region at the specified address or name.</summary>
		///   <param name='address' type='String' >The address or name of the region.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode to use to parse the region address.</param>
		///   <returns type='WorksheetRegion' >A region represented by the specified address or name.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the region at the specified address or name.</summary>
		///   <param name='address' type='String' >The address or name of the region.</param>
		///   <param name='originCell' type='WorksheetCell' >The origin for resolving relative R1C1 references.</param>
		///   <returns type='WorksheetRegion' >A region represented by the specified address or name.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the region at the specified address or name.</summary>
		///   <param name='address' type='String' >The address or name of the region.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode to use to parse the region address.</param>
		///   <param name='originCell' type='WorksheetCell' >The origin for resolving relative R1C1 references.</param>
		///   <returns type='WorksheetRegion' >A region represented by the specified address or name.</returns>
		/// </signature>
	},
	getRegions: function () {
		/// <signature>
		///   <summary>Gets the regions at the specified address or name.</summary>
		///   <param name='address' type='String' >The address or name of the region.</param>
		///   <returns type='Array' elementType='WorksheetRegion' >An array of regions represented by the specified address or name.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the regions at the specified address or name.</summary>
		///   <param name='address' type='String' >The address or name of the region.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode to use to parse the region address.</param>
		///   <returns type='Array' elementType='WorksheetRegion' >An array of regions represented by the specified address or name.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the regions at the specified address or name.</summary>
		///   <param name='address' type='String' >The address or name of the region.</param>
		///   <param name='originCell' type='WorksheetCell' >The origin for resolving relative R1C1 references.</param>
		///   <returns type='Array' elementType='WorksheetRegion' >An array of regions represented by the specified address or name.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the regions at the specified address or name.</summary>
		///   <param name='address' type='String' >The address or name of the region.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode to use to parse the region address.</param>
		///   <param name='originCell' type='WorksheetCell' >The origin for resolving relative R1C1 references.</param>
		///   <returns type='Array' elementType='WorksheetRegion' >An array of regions represented by the specified address or name.</returns>
		/// </signature>
	},
	hasProtectionPassword: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the Worksheet has been protected with a password.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	hideColumns: function () {
		/// <signature>
		///   <summary>Hides the columns in the specified range.</summary>
		///   <param name='startIndex' type='Number' optional='true' >The index of the first column to hide, or null to start hiding at the first column.</param>
		///   <param name='count' type='Number' optional='true' >The number of columns to hide. If null, a single column will be hidden when startIndex is specified and 
		///       otherwise, all columns will be hidden.</param>
		/// </signature>
	},
	hideRows: function () {
		/// <signature>
		///   <summary>Hides the rows in the specified range.</summary>
		///   <param name='startIndex' type='Number' optional='true' >The index of the first row to hide, or null to start hiding at the first row.</param>
		///   <param name='count' type='Number' optional='true' >The number of rows to hide. If null, a single row will be hidden when startIndex is specified and 
		///       otherwise, all rows will be hidden.</param>
		/// </signature>
	},
	hyperlinks: function () {
		/// <signature>
		///   <summary>Gets the collection of hyperlinks applied to cells and regions in the Worksheet.</summary>
		///   <returns type='WorksheetHyperlinkCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the hyperlink at the specified index in the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the hyperlink to get.</param>
		///   <returns type='WorksheetHyperlink' />
		/// </signature>
	},
	index: function () {
		/// <signature>
		///   <summary>Gets the zero-based index of this worksheet in its parent Workbook.Worksheets collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	insertCells: function () {
		/// <signature>
		///   <summary>Inserts a region of cells.</summary>
		///   <param name='region' type='WorksheetRegion' >The region to insert</param>
		///   <param name='shiftCellsRight' type='Boolean' >If true will shift existing cells right to make room for the newly inserted cells, otherwise will shift existing cells down below the region.</param>
		/// </signature>
		/// <signature>
		///   <summary>Inserts a region of cells.</summary>
		///   <param name='address' type='String' >The region to insert</param>
		///   <param name='shiftCellsRight' type='Boolean' >If true will shift existing cells right to make room for the newly inserted cells, otherwise will shift existing cells down below the region.</param>
		/// </signature>
	},
	isProtected: function () {
		/// <signature>
		///   <summary>Returns a boolean indicating if the Worksheet has been protected.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	mergedCellsRegions: function () {
		/// <signature>
		///   <summary>Gets the collection of merged cell ranges in this worksheet.</summary>
		///   <returns type='WorksheetMergedCellsRegionCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the merged cell region at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the merged cell region to get.</param>
		///   <returns type='WorksheetMergedCellsRegion' />
		/// </signature>
	},
	moveToIndex: function () {
		/// <signature>
		///   <summary>Moves the worksheet to a new position in the owning workbook's collections of worksheets.</summary>
		///   <param name='index' type='Number' integer='true' >The new 0-based index to where the worksheet should be moved.</param>
		/// </signature>
	},
	name: function () {
		/// <signature>
		///   <summary>Gets the worksheet name.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the worksheet name.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	printOptions: function () {
		/// <signature>
		///   <summary>Gets the object which controls how the worksheet prints.</summary>
		///   <returns type='PrintOptions' />
		/// </signature>
	},
	protect: function () {
		/// <signature>
		///   <summary>Protects the worksheet without a password.</summary>
		///   <param name='allowDeletingColumns' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorksheetProtection.AllowDeletingColumns.</param>
		///   <param name='allowDeletingRows' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorksheetProtection.AllowDeletingRows.</param>
		///   <param name='allowEditObjects' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorksheetProtection.AllowEditObjects.</param>
		///   <param name='allowEditScenarios' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorksheetProtection.AllowEditScenarios.</param>
		///   <param name='allowFiltering' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorksheetProtection.AllowFiltering.</param>
		///   <param name='allowFormattingCells' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorksheetProtection.AllowFormattingCells.</param>
		///   <param name='allowFormattingColumns' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorksheetProtection.AllowFormattingColumns.</param>
		///   <param name='allowFormattingRows' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorksheetProtection.AllowFormattingRows.</param>
		///   <param name='allowInsertingColumns' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorksheetProtection.AllowInsertingColumns.</param>
		///   <param name='allowInsertingHyperlinks' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorksheetProtection.AllowInsertingHyperlinks.</param>
		///   <param name='allowInsertingRows' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorksheetProtection.AllowInsertingRows.</param>
		///   <param name='allowSorting' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorksheetProtection.AllowSorting.</param>
		///   <param name='allowUsingPivotTables' type='Boolean' optional='true' >Optional boolean indicating the new value for the WorksheetProtection.AllowUsingPivotTables.</param>
		/// </signature>
	},
	protection: function () {
		/// <signature>
		///   <summary>Returns an object that provides information used when the Worksheet has been protected.</summary>
		///   <returns type='WorksheetProtection' />
		/// </signature>
	},
	rows: function () {
		/// <signature>
		///   <summary>Gets the collection of rows in the worksheet.</summary>
		///   <returns type='WorksheetRowCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the row at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the row to get.</param>
		///   <returns type='WorksheetRow' />
		/// </signature>
	},
	selected: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether this worksheet is selected.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	setDefaultColumnWidth: function () {
		/// <signature>
		///   <summary>Sets the default column width in the specified units.</summary>
		///   <param name='value' type='Number' >The default column width to set on the worksheet, expressed in the specified units.</param>
		///   <param name='units' type='WorksheetColumnWidthUnit' >The units in which the value is expressed.</param>
		/// </signature>
	},
	shapes: function () {
		/// <signature>
		///   <summary>Gets the collection of shapes on the worksheet.</summary>
		///   <returns type='WorksheetShapeCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the shape at the specified index in the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the shape to get.</param>
		///   <returns type='WorksheetShape' />
		/// </signature>
	},
	tables: function () {
		/// <signature>
		///   <summary>Gets the collection of WorksheetTable instances, or regions formatted as tables, in the worksheet.</summary>
		///   <returns type='WorksheetTableCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the WorksheetTable at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the WorksheetTable to get.</param>
		///   <returns type='WorksheetTable' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the WorksheetTable with the specified name.</summary>
		///   <param name='name' type='String' >The name of the WorksheetTable to get.</param>
		///   <returns type='WorksheetTable' />
		/// </signature>
	},
	unhideColumns: function () {
		/// <signature>
		///   <summary>Unhides the columns in the specified range.</summary>
		///   <param name='startIndex' type='Number' optional='true' >The index of the first column to unhide, or null to start unhiding at the first column.</param>
		///   <param name='count' type='Number' optional='true' >The number of columns to unhide. If null, a single column will be unhidden when startIndex is specified and 
		///       otherwise, all columns will be unhidden.</param>
		/// </signature>
	},
	unhideRows: function () {
		/// <signature>
		///   <summary>Unhides the rows in the specified range.</summary>
		///   <param name='startIndex' type='Number' optional='true' >The index of the first row to unhide, or null to start unhiding at the first row.</param>
		///   <param name='count' type='Number' optional='true' >The number of rows to unhide. If null, a single row will be unhidden when startIndex is specified and 
		///       otherwise, all rows will be unhidden.</param>
		/// </signature>
	},
	unprotect: function () {
		/// <signature>
		///   <summary>Removes the Worksheet protection.</summary>
		/// </signature>
	},
	workbook: function () {
		/// <signature>
		///   <summary>Gets the Worksheet.Workbook that owns the worksheet.</summary>
		///   <returns type='Workbook' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ShapeFillSolid, function () {
		/// <signature>
		///   <summary>Represents a shape fill with a solid color.</summary>
		/// </signature>
		/// <signature>
		///   <summary>Represents a shape fill with a solid color.</summary>
		///   <param name='color' type='String' >The color of the fill.</param>
		/// </signature>
		/// <signature>
		///   <summary>Represents a shape fill with a solid color.</summary>
		///   <param name='colorInfo' type='WorkbookColorInfo' >The WorkbookColorInfo describing the color of the fill.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ShapeFillSolid.prototype, {
	colorInfo: function () {
		/// <signature>
		///   <summary>Gets or sets the WorkbookColorInfo describing the color of the fill.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.ShapeOutlineSolid, function () {
		/// <signature>
		///   <summary>Represents a shape outline with a solid color.</summary>
		/// </signature>
		/// <signature>
		///   <summary>Represents a shape outline with a solid color.</summary>
		///   <param name='color' type='String' >The color of the outline.</param>
		/// </signature>
		/// <signature>
		///   <summary>Represents a shape outline with a solid color.</summary>
		///   <param name='colorInfo' type='WorkbookColorInfo' >The WorkbookColorInfo describing the color of the outline.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.ShapeOutlineSolid.prototype, {
	colorInfo: function () {
		/// <signature>
		///   <summary>Gets or sets the WorkbookColorInfo describing the color of the outline.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.SortCondition, function () {
	/// <summary>Abstract base class for the sort conditions which describe how to sort data in a region.</summary>
});
intellisense.annotate(jQuery.ig.excel.SortCondition.prototype, {
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the SortCondition is equal to the specified object.</summary>
		///   <param name='obj' type='Object' >The object to test for equality.</param>
		///   <returns type='Boolean' >True if the object is equal to this instance; False otherwise.</returns>
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code for the SortCondition.</summary>
		///   <returns type='Number' integer='true' >A number which can be used to hash this instance.</returns>
		/// </signature>
	},
	sortDirection: function () {
		/// <signature>
		///   <summary>Gets the value which indicates the sort direction represented by the sort condition.</summary>
		///   <returns type='SortDirection' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CustomListSortCondition, function () {
		/// <signature>
		///   <summary>Represents a sort condition which will sort cells based on a custom, ordered list of values.</summary>
		///   <param name='sortDirection' type='SortDirection' >The direction with which to sort the data.</param>
		///   <param name='list' type='Array' elementType='String' parameterArray='true' >The list of values in the order in which they should be sorted.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.CustomListSortCondition.prototype, {
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the CustomListSortCondition is equal to the specified object.</summary>
		///   <param name='obj' type='Object' >The object to test for equality.</param>
		///   <returns type='Boolean' >True if the object is equal to this instance; False otherwise.</returns>
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code for the CustomListSortCondition.</summary>
		///   <returns type='Number' integer='true' >A number which can be used to hash this instance.</returns>
		/// </signature>
	},
	list: function () {
		/// <signature>
		///   <summary>Gets the ordered list of values by which to sort.</summary>
		///   <returns type='IEnumerable`1' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FillSortCondition, function () {
		/// <signature>
		///   <summary>Represents a sort condition which will sort cells based on their background fill.</summary>
		///   <param name='fill' type='CellFill' >The CellFill by which the cells should be sorted.</param>
		/// </signature>
		/// <signature>
		///   <summary>Represents a sort condition which will sort cells based on their background fill.</summary>
		///   <param name='fill' type='CellFill' >The CellFill by which the cells should be sorted.</param>
		///   <param name='sortDirection' type='SortDirection' >The direction by which to sort the cells.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.FillSortCondition.prototype, {
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the FillSortCondition is equal to the specified object.</summary>
		///   <param name='obj' type='Object' >The object to test for equality.</param>
		///   <returns type='Boolean' >True if the object is equal to this instance; False otherwise.</returns>
		/// </signature>
	},
	fill: function () {
		/// <signature>
		///   <summary>Gets the CellFill by which the cells should be sorted.</summary>
		///   <returns type='CellFill' />
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code for the FillSortCondition.</summary>
		///   <returns type='Number' integer='true' >A number which can be used to hash this instance.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.FontColorSortCondition, function () {
		/// <signature>
		///   <summary>Represents a sort condition which will sort cells based on their fonts colors.</summary>
		///   <param name='fontColorInfo' type='WorkbookColorInfo' >The color by which the cells should be sorted.</param>
		/// </signature>
		/// <signature>
		///   <summary>Represents a sort condition which will sort cells based on their fonts colors.</summary>
		///   <param name='fontColorInfo' type='WorkbookColorInfo' >A WorkbookColorInfo which describes the color by which the cells should be sorted.</param>
		///   <param name='sortDirection' type='SortDirection' >The direction by which to sort the cells.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.FontColorSortCondition.prototype, {
	equals: function () {
		/// <signature>
		///   <summary>Determines whether the FontColorSortCondition is equal to the specified object.</summary>
		///   <param name='obj' type='Object' >The object to test for equality.</param>
		///   <returns type='Boolean' >True if the object is equal to this instance; False otherwise.</returns>
		/// </signature>
	},
	fontColorInfo: function () {
		/// <signature>
		///   <summary>Gets the WorkbookColorInfo which describes the color by which the cells should be sorted.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	},
	getHashCode: function () {
		/// <signature>
		///   <summary>Gets the hash code for the FontColorSortCondition.</summary>
		///   <returns type='Number' integer='true' >A number which can be used to hash this instance.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.OrderedSortCondition, function () {
		/// <signature>
		///   <summary>Represents an ordered sort condition, which can sort data in either an ascending or descending manner.</summary>
		/// </signature>
		/// <signature>
		///   <summary>Represents an ordered sort condition, which can sort data in either an ascending or descending manner.</summary>
		///   <param name='sortDirection' type='SortDirection' >The direction with which to sort the data.</param>
		/// </signature>
});

intellisense.annotate(jQuery.ig.excel.SortConditionCollection$1, function () {
	/// <summary>An ordered collection of sort conditions which are applied to a sort-able regions in a worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.SortConditionCollection$1.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds a sort condition to the collection.</summary>
		///   <param name='sortableItem' type='T' >The sort-able item over which the sort condition will be applied.</param>
		///   <param name='sortCondition' type='SortCondition' >The sort condition to apply to the sort-able item.</param>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the specified sort-able item is in the collection.</summary>
		///   <param name='sortableItem' type='T' >The sort-able item to find in the collection.</param>
		///   <returns type='Boolean' >True if the sort-able item is in the collection; False otherwise.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Determines whether the specified sort condition is in the collection.</summary>
		///   <param name='sortCondition' type='SortCondition' >The sort condition to find in the collection.</param>
		///   <returns type='Boolean' >True if the sort condition is in the collection; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of sort conditions in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	indexOf: function () {
		/// <signature>
		///   <summary>Gets the index of the specified sort-able item in the collection.</summary>
		///   <param name='sortableItem' type='T' >The sort-able item to find in the collection.</param>
		///   <returns type='Number' integer='true' >The 0-based index of the specified sort-able item in the collection or -1 if the item is not in the collection.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the index of the specified sort condition in the collection.</summary>
		///   <param name='sortCondition' type='SortCondition' >The sort condition to find in the collection.</param>
		///   <returns type='Number' integer='true' >The 0-based index of the specified sort condition in the collection or -1 if the item is not in the collection.</returns>
		/// </signature>
	},
	insert: function () {
		/// <signature>
		///   <summary>Inserts a sort condition into the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index where the sort condition should be inserted.</param>
		///   <param name='sortableItem' type='T' >The sort-able item over which the sort condition will be applied.</param>
		///   <param name='sortCondition' type='SortCondition' >The sort condition to apply to the sort-able item.</param>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes a sort-able item from the collection.</summary>
		///   <param name='sortableItem' type='T' >The sort-able item to remove from the collection.</param>
		///   <returns type='Boolean' >True if the sort-able item was found and removed; False otherwise.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Removes a sort condition from the collection.</summary>
		///   <param name='sortCondition' type='SortCondition' >The sort condition to remove from the collection.</param>
		///   <returns type='Boolean' >True if the sort condition was found and removed; False otherwise.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the sort condition at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index of the sort condition to remove.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.SortSettings$1, function () {
	/// <summary>Represents the settings which apply to sorting a region of values.</summary>
});
intellisense.annotate(jQuery.ig.excel.SortSettings$1.prototype, {
	caseSensitive: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether strings should be compared case-sensitively when they are sorted.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether strings should be compared case-sensitively when they are sorted.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	sortConditions: function () {
		/// <signature>
		///   <summary>Gets the collection of sort conditions to use when sorting the region of data.</summary>
		///   <returns type='SortConditionCollection`1' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the sort condition for the specified sort-able item.</summary>
		///   <param name='sortableItem' type='T' >The sort-able item for which to get or set the sort condition.</param>
		///   <returns type='SortCondition' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the sort condition for the specified sort-able item.</summary>
		///   <param name='sortableItem' type='T' >The sort-able item for which to get or set the sort condition.</param>
		///   <param name='value' type='SortCondition'  />
		///   <returns type='SortCondition' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the pair of item and sort condition at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The index at which to get the pair of item and sort condition.</param>
		///   <returns type='KeyValuePair`2' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the pair of item and sort condition at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The index at which to get the pair of item and sort condition.</param>
		///   <param name='value' type='KeyValuePair`2'  />
		///   <returns type='KeyValuePair`2' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetTableStyle, function () {
		/// <signature>
		///   <summary>Represents a style which can be applied to a WorksheetTable.</summary>
		///   <param name='name' type='String' >The name of the new style.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetTableStyle.prototype, {
	alternateColumnStripeWidth: function () {
		/// <signature>
		///   <summary>Gets the number of columns which will span each alternate column stripe.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the number of columns which will span each alternate column stripe.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	alternateRowStripeHeight: function () {
		/// <signature>
		///   <summary>Gets the number of rows which will span each alternate row stripe.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the number of rows which will span each alternate row stripe.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	areaFormats: function () {
		/// <signature>
		///   <summary>Gets the collection of formats used for each area of a WorksheetTable to which the style is applied.</summary>
		///   <returns type='WorksheetTableAreaFormatsCollection`1' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the format for the specified area.</summary>
		///   <param name='area' type='WorksheetTableStyleArea' >The area for which to get the format.</param>
		///   <returns type='IWorksheetCellFormat' />
		/// </signature>
	},
	clone: function () {
		/// <signature>
		///   <summary>Duplicates the style and returns a deep copy.</summary>
		///   <param name='name' type='String' >The name to give to the cloned style.</param>
		///   <returns type='WorksheetTableStyle' >The clone of the style.</returns>
		/// </signature>
	},
	columnStripeWidth: function () {
		/// <signature>
		///   <summary>Gets the number of columns which will span each column stripe.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the number of columns which will span each column stripe.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	isCustom: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the style is a custom style.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	name: function () {
		/// <signature>
		///   <summary>Gets the name of the style.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the name of the style.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	rowStripeHeight: function () {
		/// <signature>
		///   <summary>Gets the number of rows which will span each row stripe.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the number of rows which will span each row stripe.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.CustomTableStyleCollection, function () {
	/// <summary>A collection of custom WorksheetTableStyle instances which can be applied to a WorksheetTable in the 
 ///     CustomTableStyleCollection.Workbook.</summary>
});
intellisense.annotate(jQuery.ig.excel.CustomTableStyleCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds a custom WorksheetTableStyle to the collection.</summary>
		///   <param name='style' type='WorksheetTableStyle' >The custom table style to add to the collection.</param>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the specified WorksheetTableStyle is contained in the collection.</summary>
		///   <param name='style' type='WorksheetTableStyle' >The table style to find in the collection.</param>
		///   <returns type='Boolean' >True if the style is in the collection; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of custom WorksheetTableStyle instances in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	indexOf: function () {
		/// <signature>
		///   <summary>Gets the index of the specified style in the collection.</summary>
		///   <param name='style' type='WorksheetTableStyle' >The style to find in the collection.</param>
		///   <returns type='Number' integer='true' >The 0-based index of the specified style in the collection or -1 if the style is not in the collection.</returns>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified WorksheetTableStyle from the collection.</summary>
		///   <param name='style' type='WorksheetTableStyle' >The table style to remove from the collection.</param>
		///   <returns type='Boolean' >True if the style was in the collection and therefore removed; False otherwise.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the WorksheetTableStyle at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The index of the table style to remove.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.StandardTableStyleCollection, function () {
	/// <summary>A collection of standard WorksheetTableStyle instances which can be applied to a WorksheetTable in a 
 ///     Workbook.</summary>
});
intellisense.annotate(jQuery.ig.excel.StandardTableStyleCollection.prototype, {
	count: function () {
		/// <signature>
		///   <summary>Gets the number of custom WorksheetTableStyle instances in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetTable, function () {
	/// <summary>Represents a region of cells formatted as a table.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetTable.prototype, {
	areaFormats: function () {
		/// <signature>
		///   <summary>Gets the collection of formats used for each area of the WorksheetTable.</summary>
		///   <returns type='WorksheetTableAreaFormatsCollection`1' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the format for the specified area.</summary>
		///   <param name='area' type='WorksheetTableArea' >The area for which to get the format.</param>
		///   <returns type='IWorksheetCellFormat' />
		/// </signature>
	},
	clearFilters: function () {
		/// <signature>
		///   <summary>Clears all filters from the columns in the table.</summary>
		/// </signature>
	},
	clearSortConditions: function () {
		/// <signature>
		///   <summary>Clears all sort conditions from the columns in the table.</summary>
		/// </signature>
	},
	columns: function () {
		/// <signature>
		///   <summary>Gets the collection of columns in the table.</summary>
		///   <returns type='WorksheetTableColumnCollection' />
		/// </signature>
		/// <signature>
		///   <summary>Gets the WorksheetTableColumn at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index of the column to get.</param>
		///   <returns type='WorksheetTableColumn' >The WorksheetTableColumn at the specified index.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Gets the WorksheetTableColumn with the specified name or null if it doesn't exist.</summary>
		///   <param name='name' type='String' >The name of the column to get.</param>
		///   <returns type='WorksheetTableColumn' >The WorksheetTableColumn with the specified name or null a column with the specified name doesn't exist.</returns>
		/// </signature>
	},
	dataAreaRegion: function () {
		/// <signature>
		///   <summary>Gets the WorksheetRegion which represents the region of cells in the data area of the table.</summary>
		///   <returns type='WorksheetRegion' />
		/// </signature>
	},
	deleteColumns: function () {
		/// <signature>
		///   <summary>Deletes one or more columns from the table</summary>
		///   <param name='tableColumnIndex' type='Number' integer='true' >The deletion start relative to the first column in the table.</param>
		///   <param name='count' type='Number' integer='true' optional='true' >The number of columns to delete.</param>
		/// </signature>
	},
	deleteDataRows: function () {
		/// <signature>
		///   <summary>Deletes one or more data rows from the table</summary>
		///   <param name='dataRowIndex' type='Number' integer='true' >The deletion start relative to the first row in the WorksheetTable.DataAreaRegion.</param>
		///   <param name='count' type='Number' integer='true' optional='true' >The number of rows to delete.</param>
		/// </signature>
	},
	displayBandedColumns: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the alternate column format should be applied to the appropriate columns of the 
   ///     WorksheetTable.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the alternate column format should be applied to the appropriate columns of the 
   ///     WorksheetTable.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	displayBandedRows: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the alternate row format should be applied to the appropriate rows of the 
   ///     WorksheetTable.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the alternate row format should be applied to the appropriate rows of the 
   ///     WorksheetTable.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	displayFirstColumnFormatting: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the first column format should be applied to the appropriate column of the 
   ///     WorksheetTable.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the first column format should be applied to the appropriate column of the 
   ///     WorksheetTable.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	displayLastColumnFormatting: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the last column format should be applied to the appropriate column of the 
   ///     WorksheetTable.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the last column format should be applied to the appropriate column of the 
   ///     WorksheetTable.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	headerRowRegion: function () {
		/// <signature>
		///   <summary>Gets the WorksheetRegion which represents the region of cells in the header row of the table.</summary>
		///   <returns type='WorksheetRegion' />
		/// </signature>
	},
	insertColumns: function () {
		/// <signature>
		///   <summary>Inserts one or more columns into the table</summary>
		///   <param name='tableColumnIndex' type='Number' integer='true' >The insertion point relative to the first column in the table.</param>
		///   <param name='count' type='Number' integer='true' optional='true' >The number of columns to insert</param>
		/// </signature>
	},
	insertDataRows: function () {
		/// <signature>
		///   <summary>Inserts one or more data rows into the table</summary>
		///   <param name='dataRowIndex' type='Number' integer='true' >The insertion point relative to the first row in the WorksheetTable.DataAreaRegion.</param>
		///   <param name='count' type='Number' integer='true' optional='true' >The number of columns to insert</param>
		/// </signature>
	},
	isFilterUIVisible: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether to allow filtering and show filter buttons in the table headers.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether to allow filtering and show filter buttons in the table headers.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	isHeaderRowVisible: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the row containing column headers should be displayed.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the row containing column headers should be displayed.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	isTotalsRowVisible: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the row containing column totals should be displayed.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether the row containing column totals should be displayed.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	reapplyFilters: function () {
		/// <signature>
		///   <summary>Re-filters all data cells in the table based on the filters from the columns in the table.</summary>
		/// </signature>
	},
	reapplySortConditions: function () {
		/// <signature>
		///   <summary>Re-sorts all data cells in the table based on the sort conditions from the columns in the table.</summary>
		/// </signature>
	},
	resize: function () {
		/// <signature>
		///   <summary>Resizes the table to a new range of data on the worksheet.</summary>
		///   <param name='headerAndDataRegionAddress' type='String' >The address of the new region of headers (if currently visible) and data for the table, which must contain at least one data row, 
		///       and overlap and have the same top as the current table region.</param>
		/// </signature>
		/// <signature>
		///   <summary>Resizes the table to a new range of data on the worksheet.</summary>
		///   <param name='headerAndDataRegionAddress' type='String' >The address of the new region of headers (if currently visible) and data for the table, which must contain at least one data row,
		///       and overlap and have the same top as the current table region.</param>
		///   <param name='cellReferenceMode' type='CellReferenceMode' >The cell reference mode to use to parse the region address.</param>
		/// </signature>
		/// <signature>
		///   <summary>Resizes the table to a new range of data on the worksheet.</summary>
		///   <param name='headerAndDataRegion' type='WorksheetRegion' >The new region of headers (if currently visible) and data for the table, which must contain at least one data row, and overlap 
		///       and have the same top as the current table region.</param>
		/// </signature>
	},
	sortSettings: function () {
		/// <signature>
		///   <summary>Gets the settings which determine how the data within the table should be sorted.</summary>
		///   <returns type='SortSettings`1' />
		/// </signature>
	},
	style: function () {
		/// <signature>
		///   <summary>Gets the style to use on the WorksheetTable.</summary>
		///   <returns type='WorksheetTableStyle' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the style to use on the WorksheetTable.</summary>
		///   <param name='value' type='WorksheetTableStyle'  />
		///   <returns type='WorksheetTableStyle' />
		/// </signature>
	},
	toString: function () {
		/// <signature>
		///   <summary>Gets the string representation of the table.</summary>
		///   <returns type='String' >The string representation of the table.</returns>
		/// </signature>
	},
	totalsRowRegion: function () {
		/// <signature>
		///   <summary>Gets the WorksheetRegion which represents the region of cells in the totals row of the table.</summary>
		///   <returns type='WorksheetRegion' />
		/// </signature>
	},
	wholeTableRegion: function () {
		/// <signature>
		///   <summary>Gets the WorksheetRegion which represents the region of cells in the whole table, including the header and totals rows, 
   ///     if visible.</summary>
		///   <returns type='WorksheetRegion' />
		/// </signature>
	},
	worksheet: function () {
		/// <signature>
		///   <summary>Gets the WorksheetTable.Worksheet to which the table belongs.</summary>
		///   <returns type='Worksheet' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetTableAreaFormatsCollection$1, function () {
	/// <summary>A collection of formats for areas of a WorksheetTable.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetTableAreaFormatsCollection$1.prototype, {
	count: function () {
		/// <signature>
		///   <summary>Gets the number of area formats in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	hasFormat: function () {
		/// <signature>
		///   <summary>Determines whether the area has a non-default format applied.</summary>
		///   <param name='area' type='TArea' >The area of which to test the format.</param>
		///   <returns type='Boolean' >True if the area has a non-default format applied; False otherwise.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetTableCollection, function () {
	/// <summary>The collection of WorksheetTable instances on a Worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetTableCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Formats a region as a table and adds an associated WorksheetTable to the collection.</summary>
		///   <param name='region' type='String' >The region to format as a table.</param>
		///   <param name='tableHasHeaders' type='Boolean' >A value which indicates whether the top row of the region contains the headers for the table.</param>
		///   <param name='tableStyle' type='WorksheetTableStyle' optional='true' >The WorksheetTableStyle to apply to the table or null to use the Workbook.DefaultTableStyle.</param>
		///   <returns type='WorksheetTable' >The WorksheetTable created the represent the formatted table for the region.</returns>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears the collection and removes all tables from the worksheet.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the specified WorksheetTable is in the collection.</summary>
		///   <param name='table' type='WorksheetTable' >The WorksheetTable to find in the collection.</param>
		///   <returns type='Boolean' >True if the WorksheetTable is in the collection; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of tables in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	exists: function () {
		/// <signature>
		///   <summary>Determines whether a WorksheetTable with the specified name is in the collection.</summary>
		///   <param name='name' type='String' >The name of the WorksheetTable to find.</param>
		///   <returns type='Boolean' >True if a WorksheetTable with the specified name is in the collection; False otherwise.</returns>
		/// </signature>
	},
	indexOf: function () {
		/// <signature>
		///   <summary>Gets the index of the specified WorksheetTable in the collection.</summary>
		///   <param name='table' type='WorksheetTable' >The WorksheetTable to find in the collection.</param>
		///   <returns type='Number' integer='true' >The 0-based index of the specified WorksheetTable in the collection or -1 if the item is not in the collection.</returns>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the WorksheetTable from the collection.</summary>
		///   <param name='table' type='WorksheetTable' >The WorksheetTable to remove from the collection.</param>
		///   <param name='convertToRange' type='Boolean' optional='true' >True to apply the table style formatting to the cells</param>
		///   <returns type='Boolean' >True if the WorksheetTable was found and removed; False otherwise.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the WorksheetTable at the specified index.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index of the WorksheetTable to remove.</param>
		///   <param name='convertToRange' type='Boolean' optional='true' >True to apply the table style formatting to the cells</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetTableColumnCollection, function () {
	/// <summary>A read-only collection of all WorksheetTableColumn instances which exist in a WorksheetTable.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetTableColumnCollection.prototype, {
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the specified WorksheetTableColumn is in the collection.</summary>
		///   <param name='column' type='WorksheetTableColumn' >The column to find in the collection.</param>
		///   <returns type='Boolean' >True if the specified column is in the collection; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of WorksheetTableColumn instances in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	indexOf: function () {
		/// <signature>
		///   <summary>Gets the index of the specified WorksheetTableColumn in the collection.</summary>
		///   <param name='column' type='WorksheetTableColumn' >The WorksheetTableColumn to find in the collection.</param>
		///   <returns type='Number' integer='true' >The 0-based index of the specified WorksheetTableColumn in the collection or -1 if the item is not in the collection.</returns>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.UnfrozenPaneSettings, function () {
	/// <summary>Class which controls the way unfrozen panes are arranged and used for a worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.UnfrozenPaneSettings.prototype, {
	firstColumnInLeftPane: function () {
		/// <signature>
		///   <summary>Gets the first visible column in the left pane(s) of the worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the first visible column in the left pane(s) of the worksheet.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	firstRowInTopPane: function () {
		/// <signature>
		///   <summary>Gets the first visible row in the top pane(s) of the worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the first visible row in the top pane(s) of the worksheet.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	leftPaneWidth: function () {
		/// <signature>
		///   <summary>Gets the width of the left pane in twips (1/20th of a point).</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the width of the left pane in twips (1/20th of a point).</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	resetCore: function () {
		/// <signature>
		///   <summary>Resets the unfrozen pane settings to their defaults.</summary>
		/// </signature>
	},
	topPaneHeight: function () {
		/// <signature>
		///   <summary>Gets the height of the top pane in twips (1/20th of a point).</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the height of the top pane in twips (1/20th of a point).</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.UnknownShape, function () {
	/// <summary>Represents an unsupported shape which has been loaded from a workbook file.</summary>
});
intellisense.annotate(jQuery.ig.excel.UnknownShape.prototype, {
	clearUnknownData: function () {
		/// <signature>
		///   <summary>Throws an exception because all data in an unknown shape is unknown, and clearing that data would leave 
   ///     no data with the shape.</summary>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorkbookColorPalette, function () {
	/// <summary>Represents the color palette used when the saved file is opened in Microsoft Excel 2003 and earlier versions.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorkbookColorPalette.prototype, {
	contains: function () {
		/// <signature>
		///   <summary>Determines whether the specified color is in the color palette.</summary>
		///   <param name='color' type='String' >The color to find in the palette.</param>
		///   <returns type='Boolean' >Determines whether an equivalent color is in the palette.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of colors in the palette, which is always 56.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	getIndexOfNearestColor: function () {
		/// <signature>
		///   <summary>Gets the index of the closest color in the color palette, which will be seen when the file is opened in Microsoft Excel 2003 
   ///     and older versions.</summary>
		///   <param name='color' type='String' >The color to match in the palette.</param>
		///   <returns type='Number' integer='true' >A 0-based index into the collection of the closest color available in the palette.</returns>
		/// </signature>
	},
	isCustom: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether the palette has been cusotmized.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	reset: function () {
		/// <signature>
		///   <summary>Resets the palette back to the default colors for Microsoft Excel.</summary>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorkbookSaveOptions, function () {
		/// <signature>
		///   <summary>Contains the options related to saving a workbook to a file or stream.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorkbookSaveOptions.prototype, {
	type: function () {
		/// <signature>
		///   <summary>Gets the format in which to save the workbook: "base64", "string", "uint8array", "arraybuffer", "blob"</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the format in which to save the workbook: "base64", "string", "uint8array", "arraybuffer", "blob"</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorkbookStyle, function () {
	/// <summary>Represents a complex format which can be applied to a cell's format.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorkbookStyle.prototype, {
	isBuiltIn: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the style is a built in style in Microsoft Excel.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	name: function () {
		/// <signature>
		///   <summary>Gets the name of the workbook style.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the name of the workbook style.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	reset: function () {
		/// <signature>
		///   <summary>Resets the style to its original state.</summary>
		/// </signature>
	},
	styleFormat: function () {
		/// <signature>
		///   <summary>Gets the cell format which applies to the workbook style.</summary>
		///   <returns type='IWorksheetCellFormat' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorkbookStyleCollection, function () {
	/// <summary>A collection of workbook styles, or complex formats which can be easily applied to cells in Microsoft Excel.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorkbookStyleCollection.prototype, {
	addUserDefinedStyle: function () {
		/// <signature>
		///   <summary>Adds new user defined style to the workbook.</summary>
		///   <param name='name' type='String' >The name which will identify the style in Microsoft Excel.</param>
		///   <returns type='WorkbookStyle' >The added user defined style as a WorkbookStyle instance.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Adds new user defined style to the workbook.</summary>
		///   <param name='styleFormat' type='IWorksheetCellFormat' >A cell format of the style.</param>
		///   <param name='name' type='String' >The name which will identify the style in Microsoft Excel.</param>
		///   <returns type='WorkbookStyle' >The added user defined style as a WorkbookStyle instance.</returns>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears all styles, other than the Normal style, from the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether a style is in the collection.</summary>
		///   <param name='style' type='WorkbookStyle' >The style to locate in the collection.</param>
		///   <returns type='Boolean' >True if the style is found; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of styles in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	normalStyle: function () {
		/// <signature>
		///   <summary>Gets the default style for the workbook.</summary>
		///   <returns type='WorkbookStyle' />
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified style from the collection.</summary>
		///   <param name='style' type='WorkbookStyle' >The style to remove from the collection.</param>
		///   <returns type='Boolean' >True if the style was successfully removed; False if the style was not 
		///       in the collection.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the style at the specified index from the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the style in the collection.</param>
		/// </signature>
	},
	reset: function () {
		/// <signature>
		///   <summary>Resets the collection to its original state by clearing all styles and adding in preset built in styles.</summary>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorkbookWindowOptions, function () {
	/// <summary>Represents the window options which are saved with the workbook.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorkbookWindowOptions.prototype, {
	boundsInTwips: function () {
		/// <signature>
		///   <summary>Gets the bounds of the workbook's MDI child window in twips (1/20th of a point).</summary>
		///   <returns type='Object' > This will be an object with numeric values for properties 'x', 'y', 'width', 'height', 'left', 'top', 'right', and 'bottom'.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Sets the bounds of the workbook's MDI child window in twips (1/20th of a point).</summary>
		///   <param name='value' type='Object' > This can be an object with numeric values for properties 'x', 'y', 'width', and 'height' or 'left', 'top', 'right', and 'bottom' such as { x: 1, y: 2, width: 100, height: 200 }.</param>
		///   <returns type='Object' > This will be an object with numeric values for properties 'x', 'y', 'width', 'height', 'left', 'top', 'right', and 'bottom'.</returns>
		/// </signature>
	},
	firstVisibleTabIndex: function () {
		/// <signature>
		///   <summary>Gets the index of the first visible tab in the worksheet tab bar.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the index of the first visible tab in the worksheet tab bar.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	minimized: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the workbook's MDI child window is minimized in Microsoft Excel.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the workbook's MDI child window is minimized in Microsoft Excel.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	reset: function () {
		/// <signature>
		///   <summary>Resets the window options to their default settings.</summary>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetCellComment, function () {
		/// <signature>
		///   <summary>Represents a comment for a cell.</summary>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetCellComment.prototype, {
	author: function () {
		/// <signature>
		///   <summary>Gets the author of the comment.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the author of the comment.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	cell: function () {
		/// <signature>
		///   <summary>Gets the cell to which the comment is applied.</summary>
		///   <returns type='WorksheetCell' />
		/// </signature>
	},
	text: function () {
		/// <signature>
		///   <summary>Gets the formatted text of the comment.</summary>
		///   <returns type='FormattedString' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the formatted text of the comment.</summary>
		///   <param name='value' type='FormattedString'  />
		///   <returns type='FormattedString' />
		/// </signature>
	},
	worksheet: function () {
		/// <signature>
		///   <summary>Gets the worksheet on which the shape resides.</summary>
		///   <returns type='Worksheet' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetCellCollection, function () {
	/// <summary>A collection of cells in a row.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetCellCollection.prototype, {
	maxCount: function () {
		/// <signature>
		///   <summary>Gets the maximum number of items allowed in this collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetChart, function () {
	/// <summary>Represents a chart shape. This class does not allow for the editing of chart data. 
 ///     It is used for round-tripping chart data between loading and saving a workbook.</summary>
});

intellisense.annotate(jQuery.ig.excel.WorksheetColumnCollection, function () {
	/// <summary>A collection of columns in a worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetColumnCollection.prototype, {
	insert: function () {
		/// <signature>
		///   <summary>Inserts one or more columns into the collection</summary>
		///   <param name='index' type='Number' integer='true' >the insertion point</param>
		///   <param name='count' type='Number' integer='true' optional='true' >The number of columns to insert</param>
		/// </signature>
	},
	maxCount: function () {
		/// <signature>
		///   <summary>Gets the maximum number of items allowed in this collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes one or more columns from the collection</summary>
		///   <param name='index' type='Number' integer='true' >the index of the first column to remove point</param>
		///   <param name='count' type='Number' integer='true' optional='true' >The number of columns to remove</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetDataTable, function () {
	/// <summary>Represents a data table for a range of cells.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetDataTable.prototype, {
	cellsInTable: function () {
		/// <signature>
		///   <summary>Gets the region of cells in the data table.</summary>
		///   <returns type='WorksheetRegion' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the region of cells in the data table.</summary>
		///   <param name='value' type='WorksheetRegion'  />
		///   <returns type='WorksheetRegion' />
		/// </signature>
	},
	columnInputCell: function () {
		/// <signature>
		///   <summary>Gets the cell used as the column-input cell in the data table.</summary>
		///   <returns type='WorksheetCell' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the cell used as the column-input cell in the data table.</summary>
		///   <param name='value' type='WorksheetCell'  />
		///   <returns type='WorksheetCell' />
		/// </signature>
	},
	rowInputCell: function () {
		/// <signature>
		///   <summary>Gets the cell used as the row-input cell in the data table.</summary>
		///   <returns type='WorksheetCell' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the cell used as the row-input cell in the data table.</summary>
		///   <param name='value' type='WorksheetCell'  />
		///   <returns type='WorksheetCell' />
		/// </signature>
	},
	worksheet: function () {
		/// <signature>
		///   <summary>Gets the worksheet on which this data table resides.</summary>
		///   <returns type='Worksheet' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetDataTableCollection, function () {
	/// <summary>A collection of WorksheetDataTable instances on a worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetDataTableCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Creates a new data table and adds it to the collection.</summary>
		///   <param name='cellsInTable' type='WorksheetRegion' >The region of cells in the data table.</param>
		///   <param name='columnInputCell' type='WorksheetCell' >The cell used as the column-input cell in the data table.</param>
		///   <param name='rowInputCell' type='WorksheetCell' >The cell used as the row-input cell in the data table.</param>
		///   <returns type='WorksheetDataTable' >The newly created data table.</returns>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears all data tables from the collection.</summary>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of data tables in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified data table from the collection.</summary>
		///   <param name='dataTable' type='WorksheetDataTable' >The data table to remove from the collection.</param>
		///   <returns type='Boolean' >True if the dataTable was successfully removed; False if the dataTable was not in the collection.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the data table at the specified index from the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the data table to remove from the collection.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetDisplayOptions, function () {
	/// <summary>Class which exposes the display options which can only be controlled through the worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetDisplayOptions.prototype, {
	magnificationInNormalView: function () {
		/// <signature>
		///   <summary>Gets the magnification level of the worksheet when it is displayed in normal view.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the magnification level of the worksheet when it is displayed in normal view.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	magnificationInPageBreakView: function () {
		/// <signature>
		///   <summary>Gets the magnification level of the worksheet when it is displayed in the page break preview.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the magnification level of the worksheet when it is displayed in the page break preview.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	magnificationInPageLayoutView: function () {
		/// <signature>
		///   <summary>Gets the magnification level of the worksheet when it is displayed in page layout view.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the magnification level of the worksheet when it is displayed in page layout view.</summary>
		///   <param name='value' type='Number' integer='true'  />
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	orderColumnsRightToLeft: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the columns are ordered right to left.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value indicating whether the columns are ordered right to left.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	resetCore: function () {
		/// <signature>
		///   <summary>Resets the display options to their default settings.</summary>
		/// </signature>
	},
	showWhitespaceInPageLayoutView: function () {
		/// <signature>
		///   <summary>Gets the value which indicates whether to show whitespace between worksheet pages in page layout view.</summary>
		///   <returns type='Boolean' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value which indicates whether to show whitespace between worksheet pages in page layout view.</summary>
		///   <param name='value' type='Boolean'  />
		///   <returns type='Boolean' />
		/// </signature>
	},
	tabColorInfo: function () {
		/// <signature>
		///   <summary>Gets the WorkbookColorInfo to use for the associated worksheet's tab in the tab bar of Microsoft Excel.</summary>
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the WorkbookColorInfo to use for the associated worksheet's tab in the tab bar of Microsoft Excel.</summary>
		///   <param name='value' type='WorkbookColorInfo'  />
		///   <returns type='WorkbookColorInfo' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetHyperlink, function () {
		/// <signature>
		///   <summary>Represents a hyperlink for a cell or region.</summary>
		///   <param name='sourceAddress' type='String' >The address of the cell or region on which the hyperlink should reside.</param>
		///   <param name='target' type='Object' >The target of the hyperlink, which can be a string, WorksheetCell, WorksheetRegion, or 
		///       NamedReference instance. For bookmarks in an external document, separate the sub-address with a #, 
		///       such as "File.xlsx#Sheet1!C3". For email addresses, use a target string with the format "mailto:joe@company.com" or 
		///       "mailto:joe@company.com?subject=Email%20Subject".</param>
		///   <param name='displayText' type='String' optional='true' >The text to display in the cell when the hyperlink is added to a worksheet, or null to use the target text.</param>
		///   <param name='toolTip' type='String' optional='true' >The text to use in a tool tip when hovering over the source cell or region, or null to use the default tool tip.</param>
		/// </signature>
		/// <signature>
		///   <summary>Represents a hyperlink for a cell or region.</summary>
		///   <param name='sourceCell' type='WorksheetCell' >The cell on which the hyperlink should reside.</param>
		///   <param name='target' type='Object' >The target of the hyperlink, which can be a string, WorksheetCell, WorksheetRegion, or 
		///       NamedReference instance. For bookmarks in an external document, separate the sub-address with a #, 
		///       such as "File.xlsx#Sheet1!C3". For email addresses, use a target string with the format "mailto:joe@company.com" or 
		///       "mailto:joe@company.com?subject=Email%20Subject".</param>
		///   <param name='displayText' type='String' optional='true' >The text to display in the cell when the hyperlink is added to a worksheet, or null to use the target text.</param>
		///   <param name='toolTip' type='String' optional='true' >The text to use in a tool tip when hovering over the source cell or region, or null to use the default tool tip.</param>
		/// </signature>
		/// <signature>
		///   <summary>Represents a hyperlink for a cell or region.</summary>
		///   <param name='sourceRegion' type='WorksheetRegion' >The region on which the hyperlink should reside.</param>
		///   <param name='target' type='Object' >The target of the hyperlink, which can be a string, WorksheetCell, WorksheetRegion, or 
		///       NamedReference instance. For bookmarks in an external document, separate the sub-address with a #, 
		///       such as "File.xlsx#Sheet1!C3". For email addresses, use a target string with the format "mailto:joe@company.com" or 
		///       "mailto:joe@company.com?subject=Email%20Subject".</param>
		///   <param name='displayText' type='String' optional='true' >The text to display in the cell when the hyperlink is added to a worksheet, or null to use the target text.</param>
		///   <param name='toolTip' type='String' optional='true' >The text to use in a tool tip when hovering over the source cell or region, or null to use the default tool tip.</param>
		/// </signature>
});
intellisense.annotate(jQuery.ig.excel.WorksheetHyperlink.prototype, {
	displayText: function () {
		/// <signature>
		///   <summary>Gets the text to display in the cell when the hyperlink is added to a worksheet, or null to use the target text.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the text to display in the cell when the hyperlink is added to a worksheet, or null to use the target text.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	isSealed: function () {
		/// <signature>
		///   <summary>Gets the value indicating whether the hyperlink is sealed and cannot be modified.</summary>
		///   <returns type='Boolean' />
		/// </signature>
	},
	sourceAddress: function () {
		/// <signature>
		///   <summary>Gets the address of the cell or region on which the hyperlink resides.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the address of the cell or region on which the hyperlink resides.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	sourceCell: function () {
		/// <signature>
		///   <summary>Gets the cell on which the hyperlink resides.</summary>
		///   <returns type='WorksheetCell' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the cell on which the hyperlink resides.</summary>
		///   <param name='value' type='WorksheetCell'  />
		///   <returns type='WorksheetCell' />
		/// </signature>
	},
	sourceRegion: function () {
		/// <signature>
		///   <summary>Gets the region on which the hyperlink resides.</summary>
		///   <returns type='WorksheetRegion' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the region on which the hyperlink resides.</summary>
		///   <param name='value' type='WorksheetRegion'  />
		///   <returns type='WorksheetRegion' />
		/// </signature>
	},
	target: function () {
		/// <signature>
		///   <summary>Gets the target of the hyperlink, which can be a string, WorksheetCell, WorksheetRegion, or 
   ///     NamedReference instance. For bookmarks in an external document, separate the sub-address with a #, 
   ///     such as "File.xlsx#Sheet1!C3". For email addresses, use a target string with the format "mailto:joe@company.com" or 
   ///     "mailto:joe@company.com?subject=Email%20Subject".</summary>
		///   <returns type='Object' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the target of the hyperlink, which can be a string, WorksheetCell, WorksheetRegion, or 
   ///     NamedReference instance. For bookmarks in an external document, separate the sub-address with a #, 
   ///     such as "File.xlsx#Sheet1!C3". For email addresses, use a target string with the format "mailto:joe@company.com" or 
   ///     "mailto:joe@company.com?subject=Email%20Subject".</summary>
		///   <param name='value' type='Object'  />
		///   <returns type='Object' />
		/// </signature>
	},
	targetAddress: function () {
		/// <signature>
		///   <summary>Gets the target address of the hyperlink. For bookmarks in an external document, separate the sub-address with a #, 
   ///     such as "File.xlsx#Sheet1!C3". For email addresses, use a target string with the format "mailto:joe@company.com" or 
   ///     "mailto:joe@company.com?subject=Email%20Subject".</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the target address of the hyperlink. For bookmarks in an external document, separate the sub-address with a #, 
   ///     such as "File.xlsx#Sheet1!C3". For email addresses, use a target string with the format "mailto:joe@company.com" or 
   ///     "mailto:joe@company.com?subject=Email%20Subject".</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	targetCell: function () {
		/// <signature>
		///   <summary>Gets the target cell of the hyperlink.</summary>
		///   <returns type='WorksheetCell' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the target cell of the hyperlink.</summary>
		///   <param name='value' type='WorksheetCell'  />
		///   <returns type='WorksheetCell' />
		/// </signature>
	},
	targetNamedReference: function () {
		/// <signature>
		///   <summary>Gets the target named reference of the hyperlink.</summary>
		///   <returns type='NamedReference' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the target named reference of the hyperlink.</summary>
		///   <param name='value' type='NamedReference'  />
		///   <returns type='NamedReference' />
		/// </signature>
	},
	targetRegion: function () {
		/// <signature>
		///   <summary>Gets the target region of the hyperlink.</summary>
		///   <returns type='WorksheetRegion' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the target region of the hyperlink.</summary>
		///   <param name='value' type='WorksheetRegion'  />
		///   <returns type='WorksheetRegion' />
		/// </signature>
	},
	toolTip: function () {
		/// <signature>
		///   <summary>Gets the text to use in a tool tip when hovering over the source cell or region, or null to use the default tool tip.</summary>
		///   <returns type='String' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the text to use in a tool tip when hovering over the source cell or region, or null to use the default tool tip.</summary>
		///   <param name='value' type='String'  />
		///   <returns type='String' />
		/// </signature>
	},
	toString: function () {
		/// <signature>
		///   <summary>Gets the string representation of the hyperlink.</summary>
		///   <returns type='String' >The string representation of the hyperlink.</returns>
		/// </signature>
	},
	worksheet: function () {
		/// <signature>
		///   <summary>Gets the worksheet to which the hyperlink belongs.</summary>
		///   <returns type='Worksheet' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetHyperlinkCollection, function () {
	/// <summary>A collection of WorksheetHyperlink instances in a Worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetHyperlinkCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds the specified hyperlink to the collection.</summary>
		///   <param name='hyperlink' type='WorksheetHyperlink' >The hyperlink to add to the collection.</param>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears all hyperlinks from the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether a hyperlink is in the collection.</summary>
		///   <param name='hyperlink' type='WorksheetHyperlink' >The hyperlink to locate in the collection.</param>
		///   <returns type='Boolean' >True if the hyperlink is found; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of hyperlinks on the Worksheet.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	getEnumerator: function () {
		/// <signature>
		///   <summary>Gets an enumerator which can iterator all hyperlinks in the collection.</summary>
		///   <returns type='IEnumerator`1' />
		/// </signature>
	},
	insert: function () {
		/// <signature>
		///   <summary>Inserts the specified hyperlink in the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The 0-based index in the collection where the hyperlink should be inserted.</param>
		///   <param name='hyperlink' type='WorksheetHyperlink' >The hyperlink to insert in the collection.</param>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified hyperlink from the collection.</summary>
		///   <param name='hyperlink' type='WorksheetHyperlink' >The hyperlink to remove from the collection.</param>
		///   <returns type='Boolean' >True if the hyperlink was successfully removed from the collection; 
		///       False if the hyperlink did not exist in the collection.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the hyperlink at the specified index from the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The index of the hyperlink to remove from the collection.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetImage, function () {
	/// <summary>Represents any image on the worksheet except the background image.</summary>
});

intellisense.annotate(jQuery.ig.excel.WorksheetMergedCellsRegion, function () {
	/// <summary>Represents a merged region of cells, or cells which share a value and format and appear as one cell when 
 ///     viewed in Microsoft Excel.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetMergedCellsRegion.prototype, {
	cellFormat: function () {
		/// <signature>
		///   <summary>Gets the cell formatting for the merged cell region.</summary>
		///   <returns type='IWorksheetCellFormat' />
		/// </signature>
	},
	comment: function () {
		/// <signature>
		///   <summary>Gets the comment for the merged cells region.</summary>
		///   <returns type='WorksheetCellComment' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the comment for the merged cells region.</summary>
		///   <param name='value' type='WorksheetCellComment'  />
		///   <returns type='WorksheetCellComment' />
		/// </signature>
	},
	formula: function () {
		/// <signature>
		///   <summary>Gets the formula which has been applied to the merged region.</summary>
		///   <returns type='Formula' />
		/// </signature>
	},
	getResolvedCellFormat: function () {
		/// <signature>
		///   <summary>Gets the resolved cell formatting for this merged cell region.</summary>
		///   <returns type='IWorksheetCellFormat' >A format object describing the actual formatting that will be used when displayed this cell in Microsoft Excel.</returns>
		/// </signature>
	},
	value: function () {
		/// <signature>
		///   <summary>Gets the value of the merged cell region.</summary>
		///   <returns type='Object' />
		/// </signature>
		/// <signature>
		///   <summary>Sets the value of the merged cell region.</summary>
		///   <param name='value' type='Object'  />
		///   <returns type='Object' />
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetMergedCellsRegionCollection, function () {
	/// <summary>A collection of WorksheetMergedCellsRegion instances in a Worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetMergedCellsRegionCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Creates new merged cell region and adds it to the collection.</summary>
		///   <param name='firstRow' type='Number' integer='true' >The index of the first row of the merged cell region.</param>
		///   <param name='firstColumn' type='Number' integer='true' >The index of the first column of the merged cell region.</param>
		///   <param name='lastRow' type='Number' integer='true' >The index of the last row of the merged cell region.</param>
		///   <param name='lastColumn' type='Number' integer='true' >The index of the last row column of the merged cell region.</param>
		///   <returns type='WorksheetMergedCellsRegion' >The newly created WorksheetMergedCellsRegion.</returns>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears all merged cell regions from the collection.</summary>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of merged cell regions in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	isOverlappingWithMergedRegion: function () {
		/// <signature>
		///   <summary>Checks if any part of specified region is already a part of a merged cell region.</summary>
		///   <param name='firstRow' type='Number' integer='true' >The index of the first row of the merged cell region.</param>
		///   <param name='firstColumn' type='Number' integer='true' >The index of the first column of the merged cell region.</param>
		///   <param name='lastRow' type='Number' integer='true' >The index of the last row of the merged cell region.</param>
		///   <param name='lastColumn' type='Number' integer='true' >The index of the last row column of the merged cell region.</param>
		///   <returns type='Boolean' >True if any part of specified region is a part of merged cell region; False otherwise.</returns>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified merged cell region from the collection.</summary>
		///   <param name='region' type='WorksheetMergedCellsRegion' >The merged cell region to remove from the collection.</param>
		///   <returns type='Boolean' >True if the merged cell region was successfully removed; False if the merged cell region was not 
		///       in the collection.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the merged cell region at the specified index from the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The zero-based index of the merged cell region in the collection.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetRowCollection, function () {
	/// <summary>A collection of rows in a worksheet.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetRowCollection.prototype, {
	insert: function () {
		/// <signature>
		///   <summary>Inserts one or more rows into the collection</summary>
		///   <param name='index' type='Number' integer='true' >the insertion point</param>
		///   <param name='count' type='Number' integer='true' optional='true' >The number of rows to insert</param>
		/// </signature>
	},
	maxCount: function () {
		/// <signature>
		///   <summary>Gets the maximum number of items allowed in this collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes one or more rows from the collection</summary>
		///   <param name='index' type='Number' integer='true' >the index of the first row to remove point</param>
		///   <param name='count' type='Number' integer='true' optional='true' >The number of rows to remove</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetCollection, function () {
	/// <summary>A collection of worksheets in a workbook.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Creates a new Worksheet and adds it to the collection.</summary>
		///   <param name='name' type='String' >The name to give the new Worksheet.</param>
		///   <returns type='Worksheet' >The Worksheet created with the specified name.</returns>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears all worksheets from the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether a worksheet is in the collection.</summary>
		///   <param name='worksheet' type='Worksheet' >The worksheet to locate in the collection.</param>
		///   <returns type='Boolean' >True if the worksheet is found; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of worksheets in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	exists: function () {
		/// <signature>
		///   <summary>Determines whether a worksheet with the specified name exists in the collection.</summary>
		///   <param name='name' type='String' >The name of the worksheet to search for. The name is compared case-insensitively.</param>
		///   <returns type='Boolean' >True if a worksheet with the specified name is found; False otherwise.</returns>
		/// </signature>
	},
	indexOf: function () {
		/// <signature>
		///   <summary>Gets the index of the specified worksheet in the collection.</summary>
		///   <param name='worksheet' type='Worksheet' >The worksheet of which to get the index.</param>
		///   <returns type='Number' integer='true' >The index of the specified worksheet in the collection.</returns>
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified worksheet from the collection.</summary>
		///   <param name='worksheet' type='Worksheet' >The worksheet to remove from the collection.</param>
		///   <returns type='Boolean' >True if the worksheet was successfully removed from the collection; 
		///       False if the worksheet did not exist in the collection.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the worksheet at the specified index from the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The index of the worksheet to remove from the collection.</param>
		/// </signature>
	}
});

intellisense.annotate(jQuery.ig.excel.WorksheetShapeCollection, function () {
	/// <summary>A collection of WorksheetShape instances in a WorksheetShapeCollection.Worksheet 
 ///     or WorksheetShapeGroup.</summary>
});
intellisense.annotate(jQuery.ig.excel.WorksheetShapeCollection.prototype, {
	add: function () {
		/// <signature>
		///   <summary>Adds a predefined shape to the collection with the specified bounds.</summary>
		///   <param name='shapeType' type='PredefinedShapeType' >The type of shape to add to the collection.</param>
		///   <param name='boundsInTwips' type='Object' >The bounds of the shape on the WorksheetShapeCollection.Worksheet in twips (1/20th of a point). This can be an object with numeric values for properties 'x', 'y', 'width', and 'height' or 'left', 'top', 'right', and 'bottom' such as { x: 1, y: 2, width: 100, height: 200 }.</param>
		///   <returns type='WorksheetShape' >A WorksheetShape-derived instance representing the predefined shape.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Adds a predefined shape to the collection with the specified bounds.</summary>
		///   <param name='shapeType' type='PredefinedShapeType' >The type of shape to add to the collection.</param>
		///   <param name='worksheet' type='Worksheet' >The WorksheetShapeCollection.Worksheet to which the boundsInTwips relate.</param>
		///   <param name='boundsInTwips' type='Object' >The bounds of the shape on the worksheet in twips (1/20th of a point). This can be an object with numeric values for properties 'x', 'y', 'width', and 'height' or 'left', 'top', 'right', and 'bottom' such as { x: 1, y: 2, width: 100, height: 200 }.</param>
		///   <returns type='WorksheetShape' >A WorksheetShape-derived instance representing the predefined shape.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Adds a predefined shape to the collection with the specified anchors.</summary>
		///   <param name='shapeType' type='PredefinedShapeType' >The type of shape to add to the collection.</param>
		///   <param name='topLeftCornerCell' type='WorksheetCell' >The cell where the top-left corner of the shape resides.</param>
		///   <param name='topLeftCornerPosition' type='Object' >The position in the topLeftCornerCell of the shape's top-left corner,
		///       expressed in percentages. This can be an object with numeric values for properties 'x' and 'y', such as { x: 1, y: 2 }.</param>
		///   <param name='bottomRightCornerCell' type='WorksheetCell' >The cell where the bottom-right corner of the shape resides.</param>
		///   <param name='bottomRightCornerPosition' type='Object' >the position in the bottomRightCornerCell of the shape's bottom-right 
		///       corner, expressed in percentages. This can be an object with numeric values for properties 'x' and 'y', such as { x: 1, y: 2 }.</param>
		///   <returns type='WorksheetShape' >A WorksheetShape-derived instance representing the predefined shape.</returns>
		/// </signature>
		/// <signature>
		///   <summary>Adds a shape to the collection.</summary>
		///   <param name='shape' type='WorksheetShape' >The shape to add to the collection.</param>
		/// </signature>
	},
	clear: function () {
		/// <signature>
		///   <summary>Clears all shapes from the collection.</summary>
		/// </signature>
	},
	contains: function () {
		/// <signature>
		///   <summary>Determines whether a shape is in the collection.</summary>
		///   <param name='shape' type='WorksheetShape' >The shape to locate in the collection.</param>
		///   <returns type='Boolean' >True if the shape is found; False otherwise.</returns>
		/// </signature>
	},
	count: function () {
		/// <signature>
		///   <summary>Gets the number of shapes in the collection.</summary>
		///   <returns type='Number' integer='true' />
		/// </signature>
	},
	remove: function () {
		/// <signature>
		///   <summary>Removes the specified shape from the collection.</summary>
		///   <param name='shape' type='WorksheetShape' >The shape to remove from the collection.</param>
		///   <returns type='Boolean' >True if the shape was successfully removed from the collection; 
		///       False if the shape did not exist in the collection.</returns>
		/// </signature>
	},
	removeAt: function () {
		/// <signature>
		///   <summary>Removes the shape at the specified index from the collection.</summary>
		///   <param name='index' type='Number' integer='true' >The index of the shape to remove from the collection.</param>
		/// </signature>
	}
});

intellisense.addEventListener('statementcompletion', function (e) {
    e.items = e.items.filter(function (item) {

        var p = item.parentObject;

		if (p === $.ig) {
            return item.value && item.value._isNamespace;
        }

        if (p._isEnum && (typeof item.value !== 'number' || item.name === 'length')) {
            return false;
        }

		if (item.name == '$type') {
            return false;
        }

        if (p.$type && typeof (p.$type.typeName) === 'function') {

            var typeName = p.$type.typeName();
            if (typeName.indexOf('excel.') === 0) {
				if (item.name.length !== 0 && item.name[0] === '$') {
                    return false;
                }

                if (item.name.indexOf('init') === 0) {
                    if (item.name.length === 4) {
                        return false;
                    }

                    var num = Number(item.name.substr(4));
                    if (isFinite(num)) {
                        return false;
                    }
                }

				if (item.name.indexOf('_op_') >= 0) {
                    return false;
                }

				// Hide members suffixed with ...Internal
				var internalIndex = item.name.indexOf('Internal');
                if (internalIndex >= 0 && (internalIndex + 8) === item.name.length) {
                    return false;
                }
            }
        }

        return true;
    });

	for (var i = 0; i < e.items.length; i++) {
        var item = e.items[i];
        if (item.value && item.value._isNamespace) {
            item.glyph = 'vs:GlyphGroupNamespace';
        }
        else if (item.value && item.value._isEnum) {
            item.glyph = 'vs:GlyphGroupEnum';
        }
        else if (item.parentObject._isEnum) {
            item.glyph = 'vs:GlyphGroupEnumMember';
        }
    }

});
