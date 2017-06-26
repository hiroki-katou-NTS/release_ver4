/*!@license
* Infragistics.Web.ClientUI infragistics.chart_piechart.js 16.1.20161.2145
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
* Depends:
*     jquery-1.4.4.js
*     jquery.ui.core.js
*     jquery.ui.widget.js
*     infragistics.util.js
*/

(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["AbstractEnumerable:a", 
"Object:b", 
"Type:c", 
"Boolean:d", 
"ValueType:e", 
"Void:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"Func$1:ba", 
"MulticastDelegate:bb", 
"IntPtr:bc", 
"AbstractEnumerator:bd", 
"Array:bo", 
"GenericEnumerable$1:cj", 
"GenericEnumerator$1:ck"]);


} (jQuery));



(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["Object:b", 
"Type:c", 
"Boolean:d", 
"ValueType:e", 
"Void:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"MulticastDelegate:bg", 
"IntPtr:bh", 
"Array:c4", 
"Func$1:ib", 
"AbstractEnumerable:kp", 
"AbstractEnumerator:kq", 
"GenericEnumerable$1:kr", 
"GenericEnumerator$1:ks"]);


} (jQuery));



(function ($) {
$.ig = $.ig || {};
var $$t = {}
$.ig.$currDefinitions = $$t;
$.ig.util.bulkDefine(["IProvidesViewport:a", 
"Void:b", 
"ValueType:c", 
"Object:d", 
"Type:e", 
"Boolean:f", 
"IConvertible:g", 
"IFormatProvider:h", 
"Number:i", 
"String:j", 
"IComparable:k", 
"Number:l", 
"IComparable$1:m", 
"IEquatable$1:n", 
"Number:o", 
"Number:p", 
"Number:q", 
"NumberStyles:r", 
"Enum:s", 
"Array:t", 
"IList:u", 
"ICollection:v", 
"IEnumerable:w", 
"IEnumerator:x", 
"NotSupportedException:y", 
"Error:z", 
"Number:aa", 
"String:ab", 
"StringComparison:ac", 
"RegExp:ad", 
"CultureInfo:ae", 
"DateTimeFormatInfo:af", 
"Calendar:ag", 
"Date:ah", 
"Number:ai", 
"DayOfWeek:aj", 
"DateTimeKind:ak", 
"CalendarWeekRule:al", 
"NumberFormatInfo:am", 
"CompareInfo:an", 
"CompareOptions:ao", 
"IEnumerable$1:ap", 
"IEnumerator$1:aq", 
"IDisposable:ar", 
"StringSplitOptions:as", 
"Number:at", 
"Number:au", 
"Number:av", 
"Number:aw", 
"Number:ax", 
"Number:ay", 
"Assembly:az", 
"Stream:a0", 
"SeekOrigin:a1", 
"RuntimeTypeHandle:a2", 
"MethodInfo:a3", 
"MethodBase:a4", 
"MemberInfo:a5", 
"ParameterInfo:a6", 
"TypeCode:a7", 
"ConstructorInfo:a8", 
"PropertyInfo:a9", 
"Rect:ba", 
"Size:bb", 
"Point:bc", 
"Math:bd", 
"Series:be", 
"Control:bf", 
"FrameworkElement:bg", 
"UIElement:bh", 
"DependencyObject:bi", 
"Dictionary:bj", 
"DependencyProperty:bk", 
"PropertyMetadata:bl", 
"PropertyChangedCallback:bm", 
"MulticastDelegate:bn", 
"IntPtr:bo", 
"DependencyPropertyChangedEventArgs:bp", 
"DependencyPropertiesCollection:bq", 
"UnsetValue:br", 
"Script:bs", 
"Binding:bt", 
"PropertyPath:bu", 
"Transform:bv", 
"Visibility:bw", 
"Style:bx", 
"Thickness:by", 
"HorizontalAlignment:bz", 
"VerticalAlignment:b0", 
"INotifyPropertyChanged:b1", 
"PropertyChangedEventHandler:b2", 
"PropertyChangedEventArgs:b3", 
"SeriesView:b4", 
"ISchedulableRender:b5", 
"SeriesViewer:b6", 
"SeriesViewerView:b7", 
"CanvasRenderScheduler:b8", 
"List$1:b9", 
"IList$1:ca", 
"ICollection$1:cb", 
"IArray:cc", 
"IArrayList:cd", 
"Array:ce", 
"CompareCallback:cf", 
"Func$3:cg", 
"Action$1:ch", 
"Comparer$1:ci", 
"IComparer:cj", 
"IComparer$1:ck", 
"DefaultComparer$1:cl", 
"Comparison$1:cm", 
"ReadOnlyCollection$1:cn", 
"Predicate$1:co", 
"NotImplementedException:cp", 
"Callback:cq", 
"window:cr", 
"RenderingContext:cs", 
"IRenderer:ct", 
"Rectangle:cu", 
"Shape:cv", 
"Brush:cw", 
"Color:cx", 
"ArgumentException:cy", 
"DoubleCollection:cz", 
"Path:c0", 
"Geometry:c1", 
"GeometryType:c2", 
"TextBlock:c3", 
"Polygon:c4", 
"PointCollection:c5", 
"Polyline:c6", 
"DataTemplateRenderInfo:c7", 
"DataTemplatePassInfo:c8", 
"ContentControl:c9", 
"DataTemplate:da", 
"DataTemplateRenderHandler:db", 
"DataTemplateMeasureHandler:dc", 
"DataTemplateMeasureInfo:dd", 
"DataTemplatePassHandler:de", 
"Line:df", 
"FontInfo:dg", 
"XamOverviewPlusDetailPane:dh", 
"XamOverviewPlusDetailPaneView:di", 
"XamOverviewPlusDetailPaneViewManager:dj", 
"JQueryObject:dk", 
"Element:dl", 
"ElementAttributeCollection:dm", 
"ElementCollection:dn", 
"WebStyle:dp", 
"ElementNodeType:dq", 
"Document:dr", 
"EventListener:ds", 
"IElementEventHandler:dt", 
"ElementEventHandler:du", 
"ElementAttribute:dv", 
"JQueryPosition:dw", 
"JQueryCallback:dx", 
"JQueryEvent:dy", 
"JQueryUICallback:dz", 
"EventProxy:d0", 
"ModifierKeys:d1", 
"Func$2:d2", 
"MouseWheelHandler:d3", 
"Delegate:d4", 
"Interlocked:d5", 
"GestureHandler:d6", 
"ZoomGestureHandler:d7", 
"FlingGestureHandler:d8", 
"ContactHandler:d9", 
"TouchHandler:ea", 
"MouseOverHandler:eb", 
"MouseHandler:ec", 
"KeyHandler:ed", 
"Key:ee", 
"JQuery:ef", 
"JQueryDeferred:eg", 
"JQueryPromise:eh", 
"Action:ei", 
"CanvasViewRenderer:ej", 
"CanvasContext2D:ek", 
"CanvasContext:el", 
"TextMetrics:em", 
"ImageData:en", 
"CanvasElement:eo", 
"Gradient:ep", 
"LinearGradientBrush:eq", 
"GradientStop:er", 
"GeometryGroup:es", 
"GeometryCollection:et", 
"FillRule:eu", 
"PathGeometry:ev", 
"PathFigureCollection:ew", 
"LineGeometry:ex", 
"RectangleGeometry:ey", 
"EllipseGeometry:ez", 
"ArcSegment:e0", 
"PathSegment:e1", 
"PathSegmentType:e2", 
"SweepDirection:e3", 
"PathFigure:e4", 
"PathSegmentCollection:e5", 
"LineSegment:e6", 
"PolyLineSegment:e7", 
"BezierSegment:e8", 
"PolyBezierSegment:e9", 
"GeometryUtil:fa", 
"Tuple$2:fb", 
"TransformGroup:fc", 
"TransformCollection:fd", 
"TranslateTransform:fe", 
"RotateTransform:ff", 
"ScaleTransform:fg", 
"DivElement:fh", 
"BaseDOMEventProxy:fi", 
"DOMEventProxy:fj", 
"MSGesture:fk", 
"MouseEventArgs:fl", 
"EventArgs:fm", 
"DoubleAnimator:fn", 
"EasingFunctionHandler:fo", 
"ImageElement:fp", 
"RectUtil:fq", 
"MathUtil:fr", 
"RuntimeHelpers:fs", 
"RuntimeFieldHandle:ft", 
"PropertyChangedEventArgs$1:fu", 
"InteractionState:fv", 
"OverviewPlusDetailPaneMode:fw", 
"IOverviewPlusDetailControl:fx", 
"EventHandler$1:fy", 
"ArgumentNullException:fz", 
"OverviewPlusDetailViewportHost:f0", 
"XamDataChart:f1", 
"GridMode:f2", 
"BrushCollection:f3", 
"ObservableCollection$1:f4", 
"INotifyCollectionChanged:f5", 
"NotifyCollectionChangedEventHandler:f6", 
"NotifyCollectionChangedEventArgs:f7", 
"NotifyCollectionChangedAction:f8", 
"InterpolationMode:f9", 
"Random:ga", 
"ColorUtil:gb", 
"AxisCollection:gc", 
"XamDataChartView:gd", 
"SeriesViewerViewManager:ge", 
"AxisTitlePosition:gf", 
"PointerTooltipStyle:gg", 
"Dictionary$2:gh", 
"IDictionary$2:gi", 
"IDictionary:gj", 
"KeyValuePair$2:gk", 
"Enumerable:gl", 
"Thread:gm", 
"ThreadStart:gn", 
"IOrderedEnumerable$1:go", 
"SortedList$1:gp", 
"IEqualityComparer$1:gq", 
"EqualityComparer$1:gr", 
"IEqualityComparer:gs", 
"DefaultEqualityComparer$1:gt", 
"InvalidOperationException:gu", 
"CanvasGestureDOMEventProxy:gv", 
"TouchPointInfo:gw", 
"DOMExecutionContext:gx", 
"IExecutionContext:gy", 
"ExecutionContextExecuteCallback:gz", 
"TouchGestureRecognizer:g0", 
"TouchGestureState:g1", 
"TouchVelocityTracker:g2", 
"TouchHistoryItem:g3", 
"TouchVelocityReading:g4", 
"TouchGestureEventHandler:g5", 
"TouchGestureEventArgs:g6", 
"CancelableTouchGestureEventHandler:g7", 
"CssHelper:g8", 
"CssGradientUtil:g9", 
"FontUtil:ha", 
"TileZoomTile:hb", 
"TileZoomTileInfo:hc", 
"TileZoomTileCache:hd", 
"TileZoomManager:he", 
"RectChangedEventHandler:hf", 
"RectChangedEventArgs:hg", 
"Debug:hh", 
"TileZoomInfo:hi", 
"LinkedList$1:hj", 
"LinkedListNode$1:hk", 
"RenderSurface:hl", 
"FragmentBase:hm", 
"HorizontalAnchoredCategorySeries:hn", 
"AnchoredCategorySeries:ho", 
"CategorySeries:hp", 
"MarkerSeries:hq", 
"MarkerSeriesView:hr", 
"Marker:hs", 
"DataContext:ht", 
"MarkerTemplates:hu", 
"HashPool$2:hv", 
"IHashPool$2:hw", 
"IPool$1:hx", 
"Func$1:hy", 
"Pool$1:hz", 
"IIndexedPool$1:h0", 
"MarkerType:h1", 
"SeriesVisualData:h2", 
"PrimitiveVisualDataList:h3", 
"IVisualData:h4", 
"PrimitiveVisualData:h5", 
"PrimitiveAppearanceData:h6", 
"BrushAppearanceData:h7", 
"StringBuilder:h8", 
"Environment:h9", 
"AppearanceHelper:ia", 
"LinearGradientBrushAppearanceData:ib", 
"GradientStopAppearanceData:ic", 
"SolidBrushAppearanceData:id", 
"GeometryData:ie", 
"GetPointsSettings:ig", 
"EllipseGeometryData:ih", 
"RectangleGeometryData:ii", 
"LineGeometryData:ij", 
"PathGeometryData:ik", 
"PathFigureData:il", 
"SegmentData:im", 
"LineSegmentData:io", 
"PolylineSegmentData:ip", 
"ArcSegmentData:iq", 
"PolyBezierSegmentData:ir", 
"BezierSegmentData:is", 
"LabelAppearanceData:it", 
"ShapeTags:iu", 
"PointerTooltipVisualDataList:iv", 
"MarkerVisualDataList:iw", 
"MarkerVisualData:ix", 
"PointerTooltipVisualData:iy", 
"RectangleVisualData:iz", 
"PolygonVisualData:i0", 
"PolyLineVisualData:i1", 
"IFastItemsSource:i2", 
"IFastItemColumn$1:i3", 
"IFastItemColumnPropertyName:i4", 
"FastItemsSourceEventArgs:i5", 
"FastItemsSourceEventAction:i6", 
"IHasCategoryModePreference:i7", 
"IHasCategoryAxis:i8", 
"CategoryAxisBase:i9", 
"Axis:ja", 
"AxisView:jb", 
"StackedSeriesBase:jc", 
"IIsCategoryBased:jd", 
"CategoryMode:je", 
"ICategoryScaler:jf", 
"IScaler:jg", 
"ScalerParams:jh", 
"IBucketizer:ji", 
"IDetectsCollisions:jj", 
"StackedSeriesView:jk", 
"CategorySeriesView:jl", 
"ISupportsMarkers:jm", 
"CategoryBucketCalculator:jn", 
"ISortingAxis:jo", 
"CategoryFrame:jp", 
"Frame:jq", 
"BrushUtil:jr", 
"Canvas:js", 
"Panel:jt", 
"UIElementCollection:ju", 
"StackedBucketCalculator:jv", 
"StackedSeriesManager:jw", 
"StackedSeriesCollection:jx", 
"StackedFragmentSeries:jy", 
"PenLineCap:jz", 
"PropertyUpdatedEventHandler:j0", 
"PropertyUpdatedEventArgs:j1", 
"StackedAreaSeries:j2", 
"HorizontalStackedSeriesBase:j3", 
"NumericYAxis:j4", 
"StraightNumericAxisBase:j5", 
"NumericAxisBase:j6", 
"NumericAxisBaseView:j7", 
"NumericAxisRenderer:j8", 
"AxisRendererBase:j9", 
"ShouldRenderHandler:ka", 
"ScaleValueHandler:kb", 
"AxisRenderingParametersBase:kc", 
"RangeInfo:kd", 
"TickmarkValues:ke", 
"TickmarkValuesInitializationParameters:kf", 
"Func$4:kg", 
"GetGroupCenterHandler:kh", 
"GetUnscaledGroupCenterHandler:ki", 
"PathRenderingInfo:kj", 
"RenderStripHandler:kk", 
"RenderLineHandler:kl", 
"ShouldRenderLinesHandler:km", 
"ShouldRenderContentHandler:kn", 
"RenderAxisLineHandler:ko", 
"DetermineCrossingValueHandler:kp", 
"ShouldRenderLabelHandler:kq", 
"GetLabelLocationHandler:kr", 
"LabelPosition:ks", 
"TransformToLabelValueHandler:kt", 
"AxisLabelManager:ku", 
"AxisLabelPanelBase:kv", 
"AxisLabelPanelBaseView:kw", 
"AxisLabelSettings:kx", 
"AxisLabelsLocation:ky", 
"TitleSettings:kz", 
"GetLabelForItemHandler:k0", 
"CreateRenderingParamsHandler:k1", 
"SnapMajorValueHandler:k2", 
"AdjustMajorValueHandler:k3", 
"CategoryAxisRenderingParameters:k4", 
"LogarithmicTickmarkValues:k5", 
"LogarithmicNumericSnapper:k6", 
"Snapper:k7", 
"LinearTickmarkValues:k8", 
"LinearNumericSnapper:k9", 
"AxisRangeChangedEventArgs:la", 
"AxisRange:lb", 
"AutoRangeCalculator:lc", 
"NumericRadiusAxis:ld", 
"NumericRadiusAxisView:le", 
"NumericAngleAxis:lf", 
"IAngleScaler:lg", 
"NumericAngleAxisView:lh", 
"PolarAxisRenderingManager:li", 
"ViewportUtils:lj", 
"PolarAxisRenderingParameters:lk", 
"NumericAxisRenderingParameters:ll", 
"IPolarRadialRenderingParameters:lm", 
"RadialAxisRenderingParameters:ln", 
"AxisOrientation:lo", 
"AngleAxisLabelPanel:lp", 
"AngleAxisLabelPanelView:lq", 
"Extensions:lr", 
"CategoryAngleAxis:ls", 
"CategoryAngleAxisView:lt", 
"CategoryAxisBaseView:lu", 
"CategoryAxisRenderer:lv", 
"LinearCategorySnapper:lw", 
"CategoryTickmarkValues:lx", 
"RadialAxisLabelPanel:ly", 
"HorizontalAxisLabelPanelBase:lz", 
"HorizontalAxisLabelPanelBaseView:l0", 
"RadialAxisLabelPanelView:l1", 
"NumericScaler:l2", 
"StraightNumericAxisBaseView:l3", 
"NumericScaleMode:l4", 
"LogarithmicScaler:l5", 
"NumericXAxis:l6", 
"NumericXAxisView:l7", 
"HorizontalSmartAxisLabelPanel:l8", 
"AxisExtentType:l9", 
"SmartAxisLabelDisplayType:ma", 
"HorizontalSmartAxisLabelPanelView:mb", 
"FontMappingInfo:mc", 
"CategoryDateTimeXAxis:md", 
"CategoryDateTimeXAxisView:me", 
"TimeAxisDisplayType:mf", 
"FastItemDateTimeColumn:mg", 
"IFastItemColumnInternal:mh", 
"FastItemColumn:mi", 
"FastReflectionHelper:mj", 
"HorizontalAxisLabelPanel:mk", 
"CoercionInfo:ml", 
"SortedListView$1:mm", 
"ArrayUtil:mn", 
"HorizontalLogarithmicScaler:mo", 
"HorizontalLinearScaler:mp", 
"LinearScaler:mq", 
"NumericYAxisView:mr", 
"VerticalAxisLabelPanel:ms", 
"VerticalAxisLabelPanelView:mt", 
"VerticalLogarithmicScaler:mu", 
"VerticalLinearScaler:mv", 
"CategoryFramePreparerBase:mw", 
"FramePreparer:mx", 
"ISupportsErrorBars:my", 
"DefaultSupportsMarkers:mz", 
"DefaultProvidesViewport:m0", 
"DefaultSupportsErrorBars:m1", 
"PreparationParams:m2", 
"CategoryYAxis:m3", 
"CategoryYAxisView:m4", 
"SyncSettings:m5", 
"ValuesHolder:m6", 
"LineSeries:m7", 
"LineSeriesView:m8", 
"AnchoredCategorySeriesView:m9", 
"CategoryTrendLineManagerBase:na", 
"TrendLineManagerBase$1:nb", 
"TrendLineType:nc", 
"Clipper:nd", 
"EdgeClipper:ne", 
"LeftClipper:nf", 
"BottomClipper:ng", 
"RightClipper:nh", 
"TopClipper:ni", 
"TrendResolutionParams:nj", 
"Flattener:nk", 
"Stack$1:nl", 
"ReverseArrayEnumerator$1:nm", 
"SpiralTodo:nn", 
"FlattenerSettings:no", 
"IPreparesCategoryTrendline:np", 
"SortingTrendLineManager:nq", 
"TrendFitCalculator:nr", 
"LeastSquaresFit:ns", 
"Numeric:nt", 
"TrendAverageCalculator:nu", 
"CategoryTrendLineManager:nv", 
"AnchoredCategoryBucketCalculator:nw", 
"UnknownValuePlotting:nx", 
"CategoryLineRasterizer:ny", 
"Action$5:nz", 
"PathVisualData:n0", 
"CategorySeriesRenderManager:n1", 
"AssigningCategoryStyleEventArgs:n2", 
"AssigningCategoryStyleEventArgsBase:n3", 
"GetCategoryItemsHandler:n4", 
"HighlightingInfo:n5", 
"HighlightingState:n6", 
"AssigningCategoryMarkerStyleEventArgs:n7", 
"HighlightingManager:n8", 
"SplineSeriesBase:n9", 
"SplineSeriesBaseView:oa", 
"SplineType:ob", 
"CollisionAvoider:oc", 
"SafeSortedReadOnlyDoubleCollection:od", 
"SafeReadOnlyDoubleCollection:oe", 
"SafeEnumerable:of", 
"AreaSeries:og", 
"AreaSeriesView:oh", 
"LegendTemplates:oi", 
"PieChartBase:oj", 
"PieChartBaseView:ok", 
"PieChartViewManager:ol", 
"PieChartVisualData:om", 
"PieSliceVisualDataList:on", 
"PieSliceVisualData:oo", 
"PieSliceDataContext:op", 
"Slice:oq", 
"SliceView:or", 
"PieLabel:os", 
"LabelsPosition:ot", 
"MouseButtonEventArgs:ou", 
"FastItemsSource:ov", 
"ColumnReference:ow", 
"FastItemObjectColumn:ox", 
"FastItemIntColumn:oy", 
"LeaderLineType:oz", 
"OthersCategoryType:o0", 
"IndexCollection:o1", 
"LegendBase:o2", 
"LegendBaseView:o3", 
"LegendBaseViewManager:o4", 
"GradientData:o5", 
"GradientStopData:o6", 
"DataChartLegendMouseButtonEventArgs:o7", 
"DataChartMouseButtonEventArgs:o8", 
"ChartLegendMouseEventArgs:o9", 
"ChartMouseEventArgs:pa", 
"DataChartLegendMouseButtonEventHandler:pb", 
"DataChartLegendMouseEventHandler:pc", 
"LegendVisualData:pd", 
"LegendVisualDataList:pe", 
"LegendItemVisualData:pf", 
"FunnelSliceDataContext:pg", 
"PieChartFormatLabelHandler:ph", 
"LabelClickEventHandler:pi", 
"LabelClickEventArgs:pj", 
"SliceClickEventHandler:pk", 
"SliceClickEventArgs:pl", 
"ItemLegend:pm", 
"ItemLegendView:pn", 
"LegendItemInfo:po", 
"BubbleSeries:pp", 
"ScatterBase:pq", 
"ScatterBaseView:pr", 
"MarkerManagerBase:ps", 
"OwnedPoint:pt", 
"MarkerManagerBucket:pu", 
"ScatterTrendLineManager:pv", 
"NumericMarkerManager:pw", 
"CollisionAvoidanceType:px", 
"SmartPlacer:py", 
"ISmartPlaceable:pz", 
"SmartPosition:p0", 
"SmartPlaceableWrapper$1:p1", 
"ScatterAxisInfoCache:p2", 
"ScatterErrorBarSettings:p3", 
"ErrorBarSettingsBase:p4", 
"EnableErrorBars:p5", 
"ErrorBarCalculatorReference:p6", 
"IErrorBarCalculator:p7", 
"ErrorBarCalculatorType:p8", 
"ScatterFrame:p9", 
"ScatterFrameBase$1:qa", 
"DictInterpolator$3:qb", 
"Action$6:qc", 
"SeriesHitTestMode:qd", 
"SyncLink:qe", 
"IFastItemsSourceProvider:qf", 
"ChartCollection:qg", 
"FastItemsSourceReference:qh", 
"SyncManager:qi", 
"SyncLinkManager:qj", 
"ErrorBarsHelper:qk", 
"BubbleSeriesView:ql", 
"BubbleMarkerManager:qm", 
"SizeScale:qn", 
"BrushScale:qo", 
"ScaleLegend:qp", 
"ScaleLegendView:qq", 
"CustomPaletteBrushScale:qr", 
"BrushSelectionMode:qs", 
"ValueBrushScale:qt", 
"RingSeriesBase:qu", 
"XamDoughnutChart:qv", 
"RingCollection:qw", 
"Ring:qx", 
"RingControl:qy", 
"RingControlView:qz", 
"Arc:q0", 
"ArcView:q1", 
"ArcItem:q2", 
"SliceItem:q3", 
"RingSeriesBaseView:q4", 
"Nullable$1:q5", 
"RingSeriesCollection:q6", 
"SliceCollection:q7", 
"XamDoughnutChartView:q8", 
"Action$2:q9", 
"DoughnutChartVisualData:ra", 
"RingSeriesVisualDataList:rb", 
"RingSeriesVisualData:rc", 
"RingVisualDataList:rd", 
"RingVisualData:re", 
"ArcVisualDataList:rf", 
"ArcVisualData:rg", 
"SliceVisualDataList:rh", 
"SliceVisualData:ri", 
"DoughnutChartLabelVisualData:rj", 
"HoleDimensionsChangedEventHandler:rk", 
"HoleDimensionsChangedEventArgs:rl", 
"XamFunnelChart:rm", 
"IItemProvider:rn", 
"MessageHandler:ro", 
"MessageHandlerEventHandler:rp", 
"Message:rq", 
"ServiceProvider:rr", 
"MessageChannel:rs", 
"MessageEventHandler:rt", 
"Queue$1:ru", 
"XamFunnelConnector:rv", 
"XamFunnelController:rw", 
"SliceInfoList:rx", 
"SliceInfo:ry", 
"SliceAppearance:rz", 
"PointList:r0", 
"FunnelSliceVisualData:r1", 
"PointData:r2", 
"SliceInfoUnaryComparison:r3", 
"Bezier:r4", 
"BezierPoint:r5", 
"BezierOp:r6", 
"BezierPointComparison:r7", 
"DoubleColumn:r8", 
"ObjectColumn:r9", 
"XamFunnelView:sa", 
"IOuterLabelWidthDecider:sb", 
"IFunnelLabelSizeDecider:sc", 
"MouseLeaveMessage:sd", 
"InteractionMessage:se", 
"MouseMoveMessage:sf", 
"MouseButtonMessage:sg", 
"MouseButtonAction:sh", 
"MouseButtonType:si", 
"SetAreaSizeMessage:sj", 
"RenderingMessage:sk", 
"RenderSliceMessage:sl", 
"RenderOuterLabelMessage:sm", 
"TooltipValueChangedMessage:sn", 
"TooltipUpdateMessage:so", 
"FunnelDataContext:sp", 
"PropertyChangedMessage:sq", 
"ConfigurationMessage:sr", 
"ClearMessage:ss", 
"ClearTooltipMessage:st", 
"ContainerSizeChangedMessage:su", 
"ViewportChangedMessage:sv", 
"ViewPropertyChangedMessage:sw", 
"OuterLabelAlignment:sx", 
"FunnelSliceDisplay:sy", 
"SliceSelectionManager:sz", 
"DataUpdatedMessage:s0", 
"ItemsSourceAction:s1", 
"FunnelFrame:s2", 
"UserSelectedItemsChangedMessage:s3", 
"LabelSizeChangedMessage:s4", 
"FrameRenderCompleteMessage:s5", 
"IntColumn:s6", 
"IntColumnComparison:s7", 
"Convert:s8", 
"SelectedItemsChangedMessage:s9", 
"ModelUpdateMessage:ta", 
"SliceClickedMessage:tb", 
"FunnelSliceClickedEventHandler:tc", 
"FunnelSliceClickedEventArgs:td", 
"FunnelChartVisualData:te", 
"FunnelSliceVisualDataList:tf", 
"RingSeries:tg", 
"WaterfallSeries:th", 
"WaterfallSeriesView:ti", 
"CategoryTransitionInMode:tj", 
"FinancialSeries:tk", 
"FinancialSeriesView:tl", 
"FinancialBucketCalculator:tm", 
"CategoryTransitionSourceFramePreparer:tn", 
"TransitionInSpeedType:to", 
"FinancialCalculationDataSource:tp", 
"CalculatedColumn:tq", 
"FinancialEventArgs:tr", 
"FinancialCalculationSupportingCalculations:ts", 
"ColumnSupportingCalculation:tt", 
"SupportingCalculation$1:tu", 
"SupportingCalculationStrategy:tv", 
"DataSourceSupportingCalculation:tw", 
"ProvideColumnValuesStrategy:tx", 
"AssigningCategoryStyleEventHandler:ty", 
"FinancialValueList:tz", 
"CategoryXAxis:t0", 
"CategoryXAxisView:t1", 
"FinancialEventHandler:t2", 
"StepLineSeries:t3", 
"StepLineSeriesView:t4", 
"StepAreaSeries:t5", 
"StepAreaSeriesView:t6", 
"RangeAreaSeries:t7", 
"HorizontalRangeCategorySeries:t8", 
"RangeCategorySeries:t9", 
"IHasHighLowValueCategory:ua", 
"RangeCategorySeriesView:ub", 
"RangeCategoryBucketCalculator:uc", 
"RangeCategoryFramePreparer:ud", 
"IHasCategoryTrendline:ue", 
"IHasTrendline:uf", 
"DefaultCategoryTrendlineHost:ug", 
"DefaultCategoryTrendlinePreparer:uh", 
"DefaultHighLowValueProvider:ui", 
"HighLowValuesHolder:uj", 
"CategoryMarkerManager:uk", 
"RangeValueList:ul", 
"RangeAreaSeriesView:um", 
"LineFragment:un", 
"LineFragmentView:uo", 
"LineFragmentBucketCalculator:up", 
"IStacked100Series:uq", 
"AreaFragment:ur", 
"AreaFragmentView:us", 
"AreaFragmentBucketCalculator:ut", 
"StackedSplineAreaSeries:uu", 
"SplineAreaFragment:uv", 
"SplineFragmentBase:uw", 
"SplineAreaFragmentView:ux", 
"StackedColumnSeries:uy", 
"StackedColumnSeriesView:uz", 
"StackedColumnBucketCalculator:u0", 
"ColumnFragment:u1", 
"ColumnFragmentView:u2", 
"StackedBarSeries:u3", 
"VerticalStackedSeriesBase:u4", 
"IBarSeries:u5", 
"StackedBarSeriesView:u6", 
"StackedBarBucketCalculator:u7", 
"BarFragment:u8", 
"StackedLineSeries:u9", 
"StackedSplineSeries:va", 
"SplineFragment:vb", 
"SplineFragmentView:vc", 
"SplineFragmentBucketCalculator:vd", 
"StackedSeriesFramePreparer:ve", 
"CategoryFramePreparer:vf", 
"IHasSingleValueCategory:vg", 
"DefaultSingleValueProvider:vh", 
"SingleValuesHolder:vi", 
"StackedSeriesCreatedEventHandler:vj", 
"StackedSeriesCreatedEventArgs:vk", 
"StackedSeriesVisualData:vl", 
"SeriesVisualDataList:vm", 
"AxisComponentsForView:vn", 
"AxisComponentsFromView:vo", 
"AxisFormatLabelHandler:vp", 
"VisualExportHelper:vq", 
"ContentInfo:vr", 
"LabelFontHeuristics:vs", 
"AxisRangeChangedEventHandler:vt", 
"ChartContentManager:vu", 
"ChartContentType:vv", 
"RenderRequestedEventArgs:vw", 
"ChartTitleVisualData:vx", 
"VisualDataSerializer:vy", 
"AxisVisualData:vz", 
"AxisLabelVisualDataList:v0", 
"AxisLabelVisualData:v1", 
"AssigningCategoryMarkerStyleEventHandler:v2", 
"SeriesComponentsForView:v3", 
"CategorySeriesMarkerCollisionAvoidance:v4", 
"NonCollisionAvoider:v5", 
"DataChartAxisRangeChangedEventHandler:v6", 
"ChartAxisRangeChangedEventArgs:v7", 
"ChartVisualData:v8", 
"AxisVisualDataList:v9", 
"RadialBase:wa", 
"RadialBaseView:wb", 
"RadialBucketCalculator:wc", 
"SeriesRenderer$2:wd", 
"SeriesRenderingArguments:we", 
"RadialFrame:wf", 
"RadialAxes:wg", 
"PolarBase:wh", 
"PolarBaseView:wi", 
"PolarTrendLineManager:wj", 
"PolarLinePlanner:wk", 
"AngleRadiusPair:wl", 
"PolarAxisInfoCache:wm", 
"PolarFrame:wn", 
"PolarAxes:wo", 
"SeriesCollection:wp", 
"SeriesViewerComponentsFromView:wq", 
"SeriesViewerSurfaceViewer:wr", 
"LabelPanelArranger:ws", 
"LabelPanelsArrangeState:wt", 
"ChartHitTestMode:wu", 
"WindowResponse:wv", 
"ViewerSurfaceUsage:ww", 
"SeriesViewerComponentsForView:wx", 
"DataChartCursorEventHandler:wy", 
"ChartCursorEventArgs:wz", 
"DataChartMouseButtonEventHandler:w0", 
"DataChartMouseEventHandler:w1", 
"AnnotationLayer:w2", 
"AnnotationLayerView:w3", 
"RefreshCompletedEventHandler:w4", 
"SeriesComponentsFromView:w5", 
"EasingFunctions:w6", 
"PolygonUtil:w7", 
"TrendCalculators:w8", 
"BarFramePreparer:yi", 
"BarTrendFitCalculator:yj", 
"BarTrendLineManager:yk", 
"VerticalAnchoredCategorySeries:yl", 
"BarSeries:ym", 
"BarSeriesView:yn", 
"BarBucketCalculator:yo", 
"XamPieChart:yt", 
"XamPieChartView:yu", 
"Legend:ac1", 
"LegendView:ac2", 
"HierarchicalRingSeries:ac8", 
"IgQueue$1:ac9", 
"Node:ada", 
"AbstractEnumerable:adb", 
"AbstractEnumerator:adc", 
"GenericEnumerable$1:add", 
"GenericEnumerator$1:ade"]);


$.ig.util.defType('ArcView', 'PieChartBaseView', {
	_arcModel: null,
	arcModel: function (value) {
		if (arguments.length === 1) {
			this._arcModel = value;
			return value;
		} else {
			return this._arcModel;
		}
	}
	,
	init: function (model) {
		$.ig.PieChartBaseView.prototype.init.call(this, model);
		this.arcModel(model);
	},
	onRingChanged: function (oldRing, _ring) {
		this.toolTipContext().series(this.arcModel().ring().ringSeries());
	}
	,
	getActiveSlice: function () {
		return this.activeSlice();
	}
	,
	updateFont: function () {
		this.fontBrush(new $.ig.Brush());
		var container = this.viewManager().getContainer();
		this.fontBrush().__fill = container.css("color");
		this.font($.ig.FontUtil.prototype.getFont(container));
	}
	,
	$type: new $.ig.Type('ArcView', $.ig.PieChartBaseView.prototype.$type)
}, true);

$.ig.util.defType('RingControlView', 'Object', {
	_model: null,
	model: function (value) {
		if (arguments.length === 1) {
			this._model = value;
			return value;
		} else {
			return this._model;
		}
	}
	,
	init: function (model) {
		this.__allArcs = new $.ig.List$1($.ig.Arc.prototype.$type, 0);
		this.__visibleArcs = new $.ig.List$1($.ig.Arc.prototype.$type, 0);
		this.__arcDivs = new $.ig.Dictionary$2($.ig.Arc.prototype.$type, $.ig.JQueryObject.prototype.$type, 0);
		this.__owningSeries = null;
		$.ig.Object.prototype.init.call(this);
		this.model(model);
	},
	__allArcs: null,
	__visibleArcs: null,
	arcCreate: function () {
		var arc = new $.ig.Arc();
		arc.canvasTop(0);
		arc.canvasLeft(0);
		arc.ring(this.model().ring());
		this.__allArcs.add(arc);
		this.setupArc(arc);
		arc.labelExtent(this.model().series().labelExtent());
		arc.formatLabel(this.model().series().formatLabel());
		arc.labelMemberPath(this.model().series().labelMemberPath());
		arc.labelsPosition(this.model().series().labelsPosition());
		arc.labelTemplate(this.model().series().labelTemplate());
		arc.valueMemberPath(this.model().series().valueMemberPath());
		if (this.model().series().toolTip() != null) {
			arc.toolTip(this.model().series().toolTip());
		}
		arc.leaderLineVisibility(this.model().series().leaderLineVisibility());
		arc.leaderLineStyle(this.model().series().leaderLineStyle());
		arc.leaderLineType(this.model().series().leaderLineType());
		arc.leaderLineMargin(this.model().series().leaderLineMargin());
		arc.othersCategoryThreshold(this.model().series().othersCategoryThreshold());
		arc.othersCategoryType(this.model().series().othersCategoryType());
		arc.othersCategoryText(this.model().series().othersCategoryText());
		arc.othersCategoryStyle(this.model().series().othersCategoryStyle());
		arc.legend(this.model().series().legend());
		arc.legendItemTemplate(this.model().series().legendItemTemplate());
		arc.legendItemBadgeTemplate(this.model().series().legendItemBadgeTemplate());
		arc.outlines(this.model().series().outlines());
		arc.startAngle(this.model().series().startAngle());
		arc.brushes(this.model().series().brushes());
		arc.selectedStyle(this.model().series().selectedStyleResolved());
		arc.isSurfaceInteractionDisabled(true);
		arc.radiusFactor(this.model().series().radiusFactor());
		arc.allowSliceExplosion(this.model().series().chart().allowSliceExplosion());
		arc.allowSliceSelection(this.model().series().chart().allowSliceSelection());
		arc.formatLegendLabel(this.model().series().formatLegendLabel());
		arc.legendLabelMemberPath(this.model().series().legendLabelMemberPath());
		return arc;
	}
	,
	arcActivate: function (arc) {
		var div = this.__arcDivs.item(arc);
		if (div != null) {
			div.show();
		}
		this.__visibleArcs.add(arc);
	}
	,
	arcDisactivate: function (arc) {
		var div = this.__arcDivs.item(arc);
		if (div != null) {
			div.hide();
		}
		this.__visibleArcs.remove(arc);
	}
	,
	arcDestroy: function (arc) {
		this.destroyArc(arc);
		this.__allArcs.remove(arc);
	}
	,
	arcs: function () {
		return this.__allArcs;
	}
	,
	onInit: function () {
	}
	,
	sizeUpdated: function () {
		this.model().onSizeUpdated();
	}
	,
	positionArc: function (arc, x, y) {
		x = x + this.model().canvasLeft();
		y = y + this.model().canvasTop();
		if (this.__owningSeries != null) {
			x = x + this.__owningSeries.canvasLeft();
			y = y + this.__owningSeries.canvasTop();
		}
		if (arc.canvasLeft() != x || arc.canvasTop() != y) {
			arc.canvasLeft(x);
			arc.canvasTop(y);
			this.positionDiv(arc, this.__arcDivs.item(arc));
		}
	}
	,
	_container: null,
	container: function (value) {
		if (arguments.length === 1) {
			this._container = value;
			return value;
		} else {
			return this._container;
		}
	}
	,
	provideContainer: function (container) {
		if (container == null) {
			this.model()._arcs.count(0);
			var en = this.__allArcs.getEnumerator();
			while (en.moveNext()) {
				var arc = en.current();
				arc.destroy();
			}
			this.__allArcs.clear();
			this.__arcDivs.clear();
			this.__visibleArcs.clear();
			return;
		}
		this.container($(container));
		this.container().css("position", "relative");
		var en1 = this.__allArcs.getEnumerator();
		while (en1.moveNext()) {
			var arc1 = en1.current();
			var div = this.__arcDivs.item(arc1);
			this.container().append(div);
			arc1.arcView().updateFont();
		}
	}
	,
	__arcDivs: null,
	setupArc: function (arc) {
		var arcDiv = $("<div style=\"position : absolute;\" />");
		this.__arcDivs.add(arc, arcDiv);
		this.adjustDiv(arc, arcDiv);
		this.positionDiv(arc, arcDiv);
		if (this.container() != null) {
			this.container().append(arcDiv);
		}
		arc.provideContainer(arcDiv);
		arcDiv.css("position", "absolute");
		arcDiv.removeClass("ui-corner-all ui-widget-content");
	}
	,
	adjustDiv: function (arc, arcDiv) {
		arcDiv.css("width", arc.width().toString());
		arcDiv.css("height", arc.height().toString());
	}
	,
	positionDiv: function (arc, arcDiv) {
		arcDiv.css("top", arc.canvasTop().toString() + "px");
		arcDiv.css("left", arc.canvasLeft().toString() + "px");
	}
	,
	destroyArc: function (arc) {
		var div = this.__arcDivs.item(arc);
		div.remove();
		this.__arcDivs.remove(arc);
		arc.provideContainer(null);
	}
	,
	updateArcSize: function (arc) {
		this.adjustDiv(arc, arc.getContainer());
		arc.onContainerResized(arc.width(), arc.height());
	}
	,
	isReady: function () {
		return true;
	}
	,
	onContainerResized: function () {
	}
	,
	__owningSeries: null,
	onSeriesProvided: function (ringSeriesBase) {
		this.__owningSeries = ringSeriesBase;
		if (ringSeriesBase != null) {
			ringSeriesBase.propertyUpdated = $.ig.Delegate.prototype.combine(ringSeriesBase.propertyUpdated, this.ringSeriesBase_PropertyUpdated.runOn(this));
		}
	}
	,
	ringSeriesBase_PropertyUpdated: function (sender, e) {
		switch (e.propertyName()) {
			case "LabelExtent":
				for (var i = 0; i < this.__allArcs.count(); i++) {
					this.__allArcs.__inner[i].labelExtent(e.newValue());
				}
				break;
			case "LabelMemberPath":
				for (var i1 = 0; i1 < this.__allArcs.count(); i1++) {
					this.__allArcs.__inner[i1].labelMemberPath(e.newValue());
				}
				break;
			case "LabelsPosition":
				for (var i2 = 0; i2 < this.__allArcs.count(); i2++) {
					this.__allArcs.__inner[i2].labelsPosition($.ig.util.getEnumValue(e.newValue()));
				}
				break;
			case "LabelTemplate":
				for (var i3 = 0; i3 < this.__allArcs.count(); i3++) {
					this.__allArcs.__inner[i3].labelTemplate(e.newValue());
				}
				break;
			case "ValueMemberPath":
				for (var i4 = 0; i4 < this.__allArcs.count(); i4++) {
					this.__allArcs.__inner[i4].valueMemberPath(e.newValue());
				}
				break;
			case "ToolTip":
				for (var i5 = 0; i5 < this.__allArcs.count(); i5++) {
					this.__allArcs.__inner[i5].toolTip(e.newValue());
				}
				break;
			case "LeaderLineVisibility":
				for (var i6 = 0; i6 < this.__allArcs.count(); i6++) {
					this.__allArcs.__inner[i6].leaderLineVisibility($.ig.util.getEnumValue(e.newValue()));
				}
				break;
			case "LeaderLineStyle":
				for (var i7 = 0; i7 < this.__allArcs.count(); i7++) {
					this.__allArcs.__inner[i7].leaderLineStyle(e.newValue());
				}
				break;
			case "LeaderLineType":
				for (var i8 = 0; i8 < this.__allArcs.count(); i8++) {
					this.__allArcs.__inner[i8].leaderLineType($.ig.util.getEnumValue(e.newValue()));
				}
				break;
			case "LeaderLineMargin":
				for (var i9 = 0; i9 < this.__allArcs.count(); i9++) {
					this.__allArcs.__inner[i9].leaderLineMargin(e.newValue());
				}
				break;
			case "OthersCategoryThreshold":
				for (var i10 = 0; i10 < this.__allArcs.count(); i10++) {
					this.__allArcs.__inner[i10].othersCategoryThreshold(e.newValue());
				}
				break;
			case "OthersCategoryType":
				for (var i11 = 0; i11 < this.__allArcs.count(); i11++) {
					this.__allArcs.__inner[i11].othersCategoryType($.ig.util.getEnumValue(e.newValue()));
				}
				break;
			case "OthersCategoryText":
				for (var i12 = 0; i12 < this.__allArcs.count(); i12++) {
					this.__allArcs.__inner[i12].othersCategoryText(e.newValue());
				}
				break;
			case "OthersCategoryStyle":
				for (var i13 = 0; i13 < this.__allArcs.count(); i13++) {
					this.__allArcs.__inner[i13].othersCategoryStyle(e.newValue());
				}
				break;
			case "Legend":
				for (var i14 = 0; i14 < this.__allArcs.count(); i14++) {
					this.__allArcs.__inner[i14].legend(e.newValue());
				}
				break;
			case "LegendItemTemplate":
				for (var i15 = 0; i15 < this.__allArcs.count(); i15++) {
					this.__allArcs.__inner[i15].legendItemTemplate(e.newValue());
				}
				break;
			case "LegendItemBadgeTemplate":
				for (var i16 = 0; i16 < this.__allArcs.count(); i16++) {
					this.__allArcs.__inner[i16].legendItemBadgeTemplate(e.newValue());
				}
				break;
			case "Outlines":
				for (var i17 = 0; i17 < this.__allArcs.count(); i17++) {
					this.__allArcs.__inner[i17].outlines(e.newValue());
				}
				break;
			case "Brushes":
				for (var i18 = 0; i18 < this.__allArcs.count(); i18++) {
					this.__allArcs.__inner[i18].brushes(e.newValue());
				}
				break;
			case "SelectedStyle":
				for (var i19 = 0; i19 < this.__allArcs.count(); i19++) {
					this.__allArcs.__inner[i19].selectedStyle(e.newValue());
				}
				break;
			case "SelectedStyleResolved":
				for (var i20 = 0; i20 < this.__allArcs.count(); i20++) {
					this.__allArcs.__inner[i20].selectedStyle(e.newValue());
				}
				break;
			case "RadiusFactor":
				for (var i21 = 0; i21 < this.__allArcs.count(); i21++) {
					this.__allArcs.__inner[i21].radiusFactor(e.newValue());
				}
				break;
		}
	}
	,
	onArcsResized: function () {
		var en = this.__allArcs.getEnumerator();
		while (en.moveNext()) {
			var arc = en.current();
			arc.flush();
		}
	}
	,
	getContainer: function () {
		return this.container();
	}
	,
	positionRingControl: function (x, y) {
		this.model().canvasLeft(x);
		this.model().canvasTop(y);
		this.repositionArcs();
	}
	,
	repositionArcs: function () {
		for (var i = 0; i < this.__allArcs.count(); i++) {
			var arc = this.__allArcs.__inner[i];
			this.positionArc(arc, 0, 0);
		}
	}
	,
	$type: new $.ig.Type('RingControlView', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RingSeriesBaseView', 'Object', {
	_model: null,
	model: function (value) {
		if (arguments.length === 1) {
			this._model = value;
			return value;
		} else {
			return this._model;
		}
	}
	,
	init: function (model) {
		$.ig.Object.prototype.init.call(this);
		this.attachedRings(new $.ig.List$1($.ig.RingControl.prototype.$type, 0));
		this.model(model);
	},
	onInit: function () {
		this.model().legendItemTemplate((function () {
			var $ret = new $.ig.DataTemplate();
			$ret.render($.ig.LegendTemplates.prototype.doughnutLegendItemRender);
			$ret.measure($.ig.LegendTemplates.prototype.defaultLegendItemMeasure);
			return $ret;
		}()));
		this.model().legendItemBadgeTemplate((function () {
			var $ret = new $.ig.DataTemplate();
			$ret.render($.ig.LegendTemplates.prototype.doughnutBadgeTemplate);
			$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
			return $ret;
		}()));
	}
	,
	_attachedRings: null,
	attachedRings: function (value) {
		if (arguments.length === 1) {
			this._attachedRings = value;
			return value;
		} else {
			return this._attachedRings;
		}
	}
	,
	addRing: function (ringControl) {
		if (!this.attachedRings().contains(ringControl)) {
			this.attachedRings().add(ringControl);
		}
		if (this.container() != null && (ringControl.getContainer() != this.container())) {
			ringControl.provideContainer(this.container());
		}
	}
	,
	_container: null,
	container: function (value) {
		if (arguments.length === 1) {
			this._container = value;
			return value;
		} else {
			return this._container;
		}
	}
	,
	getDefaultStyle: function (container, brushes, outlines, fontBrush, font) {
		var $ret = $.ig.BrushUtil.prototype.getBrushCollection("chart", container, brushes, outlines, null);
		brushes = $ret.p2;
		outlines = $ret.p3;
		fontBrush = new $.ig.Brush();
		fontBrush.__fill = this.container().css("color");
		font = $.ig.FontUtil.prototype.getFont(container);
		return {
			p1: brushes,
			p2: outlines,
			p3: fontBrush,
			p4: font
		};
	}
	,
	__brushes: null,
	__outlines: null,
	__fontBrush: null,
	__font: null,
	provideContainer: function (container) {
		if (container == null) {
			var en = this.attachedRings().getEnumerator();
			while (en.moveNext()) {
				var ring = en.current();
				ring.provideContainer(null);
			}
			return;
		}
		this.container($(container));
		var brushes;
		var outlines;
		var fontBrush;
		var font;
		var $ret = this.getDefaultStyle(this.container(), brushes, outlines, fontBrush, font);
		brushes = $ret.p1;
		outlines = $ret.p2;
		fontBrush = $ret.p3;
		font = $ret.p4;
		this.__brushes = brushes;
		this.__outlines = outlines;
		this.__fontBrush = fontBrush;
		this.__font = font;
		if (this.model().brushes() == null) {
			this.model().brushes(this.__brushes);
		}
		if (this.model().outlines() == null) {
			this.model().outlines(this.__outlines);
		}
		var en1 = this.attachedRings().getEnumerator();
		while (en1.moveNext()) {
			var ring1 = en1.current();
			ring1.provideContainer(this.container());
		}
	}
	,
	isReady: function () {
		return true;
	}
	,
	clearView: function () {
		this.provideContainer(null);
	}
	,
	onSizeChanged: function () {
		for (var i = 0; i < this.attachedRings().count(); i++) {
			var currRing = this.attachedRings().__inner[i];
			for (var j = 0; j < currRing._arcs.active().count(); j++) {
				var arc = currRing._arcs.active().__inner[j];
				arc.flush();
			}
		}
	}
	,
	positionSeries: function (x, y) {
		this.model().canvasLeft(x);
		this.model().canvasTop(y);
		this.model().location({ __x: x, __y: y, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	$type: new $.ig.Type('RingSeriesBaseView', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('XamDoughnutChartView', 'Object', {
	_model: null,
	model: function (value) {
		if (arguments.length === 1) {
			this._model = value;
			return value;
		} else {
			return this._model;
		}
	}
	,
	_centerCoordinates: null,
	centerCoordinates: function (value) {
		if (arguments.length === 1) {
			this._centerCoordinates = value;
			return value;
		} else {
			return this._centerCoordinates;
		}
	}
	,
	_holeRadius: 0,
	holeRadius: function (value) {
		if (arguments.length === 1) {
			this._holeRadius = value;
			return value;
		} else {
			return this._holeRadius;
		}
	}
	,
	init: function (model) {
		$.ig.Object.prototype.init.call(this);
		this.viewport($.ig.Rect.prototype.empty());
		this.centerCoordinates({ __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		this.holeRadius(0);
		this.attachedSeries(new $.ig.List$1($.ig.RingSeriesBase.prototype.$type, 0));
		this.model(model);
	},
	onInit: function () {
	}
	,
	updateView: function () {
	}
	,
	_attachedSeries: null,
	attachedSeries: function (value) {
		if (arguments.length === 1) {
			this._attachedSeries = value;
			return value;
		} else {
			return this._attachedSeries;
		}
	}
	,
	addSeries: function (series) {
		if (!this.attachedSeries().contains(series)) {
			series.provideContainer(this.container());
			this.attachedSeries().add(series);
		}
	}
	,
	removeSeries: function (series) {
		if (this.attachedSeries().contains(series)) {
			series.provideContainer(null);
			this.attachedSeries().remove(series);
		}
	}
	,
	clearSeries: function () {
	}
	,
	_viewport: null,
	viewport: function (value) {
		if (arguments.length === 1) {
			this._viewport = value;
			return value;
		} else {
			return this._viewport;
		}
	}
	,
	sizeUpdated: function () {
		if (this.container() == null) {
			return;
		}
		var width = this.container().width();
		var height = this.container().height();
		this.viewport(new $.ig.Rect(0, 0, 0, width, height));
		if (this.eventProxy() != null) {
			this.eventProxy().viewport(this.viewport());
		}
		this.model().onSizeUpdated();
	}
	,
	_canvas: null,
	canvas: function (value) {
		if (arguments.length === 1) {
			this._canvas = value;
			return value;
		} else {
			return this._canvas;
		}
	}
	,
	_container: null,
	container: function (value) {
		if (arguments.length === 1) {
			this._container = value;
			return value;
		} else {
			return this._container;
		}
	}
	,
	_eventProxy: null,
	eventProxy: function (value) {
		if (arguments.length === 1) {
			this._eventProxy = value;
			return value;
		} else {
			return this._eventProxy;
		}
	}
	,
	provideContainer: function (container) {
		if (container == null) {
			if (this.eventProxy() != null) {
				this.eventProxy().destroy();
			}
			return;
		}
		var cont = $(container);
		this.container(cont);
		var width = this.container().width();
		var height = this.container().height();
		this.viewport(new $.ig.Rect(0, 0, 0, width, height));
		this.eventProxy(new $.ig.DOMEventProxy(this.container()));
		this.eventProxy().viewport(this.viewport());
		var $t = this.eventProxy();
		$t.onMouseOver = $.ig.Delegate.prototype.combine($t.onMouseOver, this.eventProxy_OnMouseOver.runOn(this));
		var $t1 = this.eventProxy();
		$t1.onMouseLeave = $.ig.Delegate.prototype.combine($t1.onMouseLeave, this.eventProxy_OnMouseLeave.runOn(this));
		var $t2 = this.eventProxy();
		$t2.onMouseDown = $.ig.Delegate.prototype.combine($t2.onMouseDown, this.eventProxy_OnMouseDown.runOn(this));
		var $t3 = this.eventProxy();
		$t3.onMouseUp = $.ig.Delegate.prototype.combine($t3.onMouseUp, this.eventProxy_OnMouseUp.runOn(this));
		this.sizeUpdated();
	}
	,
	passArcEvent: function (point, action, eventType) {
		var nullValue = $.ig.util.toNullable($.ig.Boolean.prototype.$type, null);
		for (var i = this.attachedSeries().count() - 1; i >= 0; i--) {
			var currSeries = this.attachedSeries().__inner[i];
			if ($.ig.util.nullableEquals(currSeries.isSurfaceInteractionDisabled(), nullValue)) {
				if ($.ig.util.nullableEquals(this.model().isSurfaceInteractionDisabled(), nullValue)) {
				} else {
					if (this.model().isSurfaceInteractionDisabled().value()) {
						continue;
					}
				}
			} else {
				if (currSeries.isSurfaceInteractionDisabled().value()) {
					continue;
				}
			}
			var attachedRings = currSeries.view().attachedRings();
			for (var j = 0; j < attachedRings.count(); j++) {
				var currRing = attachedRings.__inner[j];
				var activeArcs = currRing._arcs.active();
				for (var k = 0; k < activeArcs.count(); k++) {
					var currentArc = activeArcs.__inner[k];
					var translatedPoint = { __x: point.__x - currentArc.canvasLeft(), __y: point.__y - currentArc.canvasTop(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
					action(currentArc.view(), translatedPoint);
					if (eventType == "mouseUp") {
						var activeSlice = currentArc.arcView().getActiveSlice();
						if (activeSlice != null) {
							return;
						}
					}
				}
			}
		}
	}
	,
	eventProxy_OnMouseUp: function (point) {
		this.passArcEvent(point, function (a, p) { a.canvasMouseUp(p); }, "mouseUp");
	}
	,
	eventProxy_OnMouseDown: function (point) {
		this.passArcEvent(point, function (a, p) { a.canvasMouseDown(p); }, "mouseDown");
	}
	,
	eventProxy_OnMouseLeave: function (point) {
		this.passArcEvent(point, function (a, p) { a.canvasMouseLeave(p); }, "mouseLeave");
	}
	,
	eventProxy_OnMouseOver: function (point, onMouseMove, isFinger) {
		this.passArcEvent(point, function (a, p) { a.canvasMouseMove(p, onMouseMove, isFinger); }, "mouseOver");
	}
	,
	getContainerID: function () {
		return this.container().attr("id");
	}
	,
	positonCenterPresenter: function (x, y) {
		if (!this.viewport().isEmpty() && this.viewport().width() != 0 && this.viewport().height() != 0) {
			var oldX = this.centerCoordinates().__x;
			var oldY = this.centerCoordinates().__y;
			this.centerCoordinates().__x = this.viewport().width() / 2;
			this.centerCoordinates().__y = this.viewport().height() / 2;
			if (this.centerCoordinates().__x != oldX || this.centerCoordinates().__y != oldY) {
				this.model().onHoleDimensionsChanged();
			}
		}
	}
	,
	setCenterPresenterSize: function (width, height) {
		var oldRadius = this.holeRadius();
		this.holeRadius(width / 2);
		if (oldRadius != this.holeRadius()) {
			this.model().onHoleDimensionsChanged();
		}
	}
	,
	ensureCenterPresenter: function () {
	}
	,
	exportViewShapes: function (doughnutChart) {
		doughnutChart.width(this.model().width());
		doughnutChart.height(this.model().height());
		doughnutChart.holeRadius(this.model().getHoleRadius());
		for (var i = 0; i < this.model().series().count(); i++) {
			doughnutChart.series().add(new $.ig.RingSeriesVisualData());
			doughnutChart.series().__inner[i].rings(new $.ig.RingVisualDataList());
			doughnutChart.series().__inner[i].name(this.model().series().__inner[i].name());
			doughnutChart.series().__inner[i].labelExtent(this.model().series().__inner[i].labelExtent());
			doughnutChart.series().__inner[i].leaderLineMargin(this.model().series().__inner[i].leaderLineMargin());
			doughnutChart.series().__inner[i].leaderLineType($.ig.LeaderLineType.prototype.$getName(this.model().series().__inner[i].leaderLineType()));
			doughnutChart.series().__inner[i].labelsPosition($.ig.LabelsPosition.prototype.$getName(this.model().series().__inner[i].labelsPosition()));
			doughnutChart.series().__inner[i].leaderLineVisibility(this.model().series().__inner[i].leaderLineVisibility());
			for (var j = 0; j < this.model().series().__inner[i].analyzeRings().count(); j++) {
				doughnutChart.series().__inner[i].rings().add(new $.ig.RingVisualData());
				doughnutChart.series().__inner[i].rings().__inner[j].arcs(new $.ig.ArcVisualDataList());
				for (var k = 0; k < this.model().series().__inner[i].analyzeRings().__inner[j].arcItems().count(); k++) {
					doughnutChart.series().__inner[i].rings().__inner[j].arcs().add(new $.ig.ArcVisualData());
					doughnutChart.series().__inner[i].rings().__inner[j].arcs().__inner[k].slices(new $.ig.SliceVisualDataList());
					for (var m = 0; m < this.model().series().__inner[i].analyzeRings().__inner[j].arcItems().__inner[k].sliceItems().count(); m++) {
						if (null == this.model().series().__inner[i].analyzeRings().__inner[j].arcItems().__inner[k].sliceItems().__inner[m].slice()) {
							continue;
						}
						doughnutChart.series().__inner[i].rings().__inner[j].arcs().__inner[k].slices().add(new $.ig.SliceVisualData());
						var svd = new $.ig.SliceVisualData();
						var currentSlice = this.model().series().__inner[i].analyzeRings().__inner[j].arcItems().__inner[k].sliceItems().__inner[m].slice();
						svd.visibility(currentSlice.__visibility);
						svd.endAngle(currentSlice.endAngle());
						svd.startAngle(currentSlice.startAngle());
						svd.radius(currentSlice.radius());
						svd.origin(currentSlice.origin());
						svd.explodedOrigin(currentSlice.explodedOrigin());
						svd.index(currentSlice.index());
						svd.isExploded(currentSlice.isExploded());
						svd.isSelected(currentSlice.isSelected());
						svd.outline(currentSlice.borderBrush());
						svd.backgroundPath(new $.ig.PathVisualData(1, "slicePath", currentSlice.view().getSlicePath()));
						doughnutChart.series().__inner[i].rings().__inner[j].arcs().__inner[k].slices().__inner[m] = svd;
						var currentLabel = currentSlice.label();
						if (null != currentLabel) {
							doughnutChart.series().__inner[i].rings().__inner[j].arcs().__inner[k].slices().__inner[m].labelVisualData((function () {
								var $ret = new $.ig.DoughnutChartLabelVisualData();
								$ret.labelSize($.ig.RectUtil.prototype.size(currentLabel.bounds()));
								$ret.labelValue((currentLabel.label()).text());
								return $ret;
							}()));
							if (!$.ig.util.isNaN(currentLabel.canvasLeft()) && !$.ig.util.isNaN(currentLabel.canvasTop())) {
								doughnutChart.series().__inner[i].rings().__inner[j].arcs().__inner[k].slices().__inner[m].labelVisualData().labelPosition({ __x: currentLabel.canvasLeft(), __y: currentLabel.canvasTop(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
							}
						}
					}
				}
			}
		}
	}
	,
	$type: new $.ig.Type('XamDoughnutChartView', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('XamPieChart', 'PieChartBase', {
	createView: function () {
		return new $.ig.XamPieChartView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.PieChartBase.prototype.onViewCreated.call(this, view);
		this.pieChartView(view);
	}
	,
	_pieChartView: null,
	pieChartView: function (value) {
		if (arguments.length === 1) {
			this._pieChartView = value;
			return value;
		} else {
			return this._pieChartView;
		}
	}
	,
	init: function () {
		$.ig.PieChartBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.XamPieChart.prototype.$type);
	},
	$type: new $.ig.Type('XamPieChart', $.ig.PieChartBase.prototype.$type)
}, true);

$.ig.util.defType('Legend', 'LegendBase', {
	createView: function () {
		return new $.ig.LegendView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.LegendBase.prototype.onViewCreated.call(this, view);
		this.legendView(view);
	}
	,
	_legendView: null,
	legendView: function (value) {
		if (arguments.length === 1) {
			this._legendView = value;
			return value;
		} else {
			return this._legendView;
		}
	}
	,
	init: function () {
		var $self = this;
		$.ig.LegendBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.Legend.prototype.$type);
		var $t = this.children();
		$t.collectionChanged = $.ig.Delegate.prototype.combine($t.collectionChanged, function (o, e) {
			if (e.oldItems() != null) {
				var en = e.oldItems().getEnumerator();
				while (en.moveNext()) {
					var item = en.current();
					$self.legendView().removeItemVisual(item);
				}
			}
			if (e.newItems() != null) {
				var en1 = e.newItems().getEnumerator();
				while (en1.moveNext()) {
					var item1 = en1.current();
					$self.legendView().addItemVisual(item1);
				}
			}
		});
	},
	getSeriesIndex: function (series) {
		if ($.ig.util.cast($.ig.FragmentBase.prototype.$type, series) !== null || $.ig.util.cast($.ig.SplineFragmentBase.prototype.$type, series) !== null) {
			var parentSeries = $.ig.util.cast($.ig.FragmentBase.prototype.$type, series) !== null ? (series).parentSeries() : (series).parentSeries();
			if (parentSeries.reverseLegendOrder()) {
				return -1 + parentSeries.index() + parentSeries.actualSeries().count() - parentSeries.stackedSeriesManager().seriesVisual().indexOf($.ig.util.cast($.ig.AnchoredCategorySeries.prototype.$type, series));
			}
		}
		return series.index();
	}
	,
	addChildInOrder: function (legendItem, series) {
		if ($.ig.util.cast($.ig.StackedSeriesBase.prototype.$type, series) !== null) {
			return;
		}
		if (!series.isUsableInLegend()) {
			return;
		}
		var index = 0;
		var en = this.children().getEnumerator();
		while (en.moveNext()) {
			var item = en.current();
			var itemChart;
			var itemSeries;
			var itemItem;
			var $ret = this.view().fetchLegendEnvironment(item, itemChart, itemSeries, itemItem);
			itemChart = $ret.p1;
			itemSeries = $ret.p2;
			itemItem = $ret.p3;
			if (series.seriesViewer() != null && itemChart != null && (this.getSortOrder(series.seriesViewer()) < this.getSortOrder(itemChart) || (this.getSortOrder(series.seriesViewer()) == -1 && this.getSortOrder(itemChart) == -1 && series.seriesViewer().getHashCode() < itemChart.getHashCode()))) {
				break;
			}
			if (series.seriesViewer() != null && itemChart != null && series.seriesViewer() == itemChart && itemSeries != null) {
				var indexOfSeries = this.getSeriesIndex(series);
				var indexOfItemSeries = this.getSeriesIndex(itemSeries);
				var sortOrderSeries = this.getSortOrder(series);
				var sortOrderItemSeries = this.getSortOrder(itemSeries);
				if ($.ig.util.cast($.ig.BarSeries.prototype.$type, itemSeries) !== null) {
					if (sortOrderItemSeries == -1 && sortOrderSeries == -1) {
						index = 0;
						break;
					}
					if (sortOrderSeries < sortOrderItemSeries || sortOrderItemSeries == -1) {
						break;
					}
				}
				if (indexOfSeries <= indexOfItemSeries) {
					break;
				}
			}
			index++;
		}
		this.children().insert(index, legendItem);
		var info = new $.ig.LegendItemInfo();
		info.legendItem(legendItem);
		info.series(series);
		var contentControl = $.ig.util.cast($.ig.ContentControl.prototype.$type, legendItem);
		if (contentControl != null && contentControl.content() != null) {
			var context = $.ig.util.cast($.ig.DataContext.prototype.$type, contentControl.content());
			if (context != null) {
				info.dataContext(context);
				info.text(context.itemLabel());
			}
		}
	}
	,
	getSortOrder: function (target) {
		return -1;
	}
	,
	$type: new $.ig.Type('Legend', $.ig.LegendBase.prototype.$type)
}, true);

$.ig.util.defType('LegendView', 'LegendBaseView', {
	init: function (model) {
		$.ig.LegendBaseView.prototype.init.call(this, model);
		this.legendModel(model);
	},
	_legendModel: null,
	legendModel: function (value) {
		if (arguments.length === 1) {
			this._legendModel = value;
			return value;
		} else {
			return this._legendModel;
		}
	}
	,
	onInit: function () {
		$.ig.LegendBaseView.prototype.onInit.call(this);
	}
	,
	$type: new $.ig.Type('LegendView', $.ig.LegendBaseView.prototype.$type)
}, true);

$.ig.util.defType('XamPieChartView', 'PieChartBaseView', {
	_pieChartModel: null,
	pieChartModel: function (value) {
		if (arguments.length === 1) {
			this._pieChartModel = value;
			return value;
		} else {
			return this._pieChartModel;
		}
	}
	,
	init: function (model) {
		$.ig.PieChartBaseView.prototype.init.call(this, model);
		this.pieChartModel(model);
	},
	$type: new $.ig.Type('XamPieChartView', $.ig.PieChartBaseView.prototype.$type)
}, true);

$.ig.util.defType('Arc', 'PieChartBase', {
	createView: function () {
		return new $.ig.ArcView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.PieChartBase.prototype.onViewCreated.call(this, view);
		this.arcView(view);
	}
	,
	_arcView: null,
	arcView: function (value) {
		if (arguments.length === 1) {
			this._arcView = value;
			return value;
		} else {
			return this._arcView;
		}
	}
	,
	init: function () {
		this.__ring = null;
		$.ig.PieChartBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.Arc.prototype.$type);
	},
	__ring: null,
	ring: function (value) {
		if (arguments.length === 1) {
			var oldRing = this.__ring;
			this.__ring = value;
			this.arcView().onRingChanged(oldRing, this.__ring);
			return value;
		} else {
			return this.__ring;
		}
	}
	,
	_arcItem: null,
	arcItem: function (value) {
		if (arguments.length === 1) {
			this._arcItem = value;
			return value;
		} else {
			return this._arcItem;
		}
	}
	,
	endAngle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.Arc.prototype.endAngleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.Arc.prototype.endAngleProperty);
		}
	}
	,
	_actualEndAngle: 0,
	actualEndAngle: function (value) {
		if (arguments.length === 1) {
			this._actualEndAngle = value;
			return value;
		} else {
			return this._actualEndAngle;
		}
	}
	,
	arcBreadth: function () {
		if (this.ring() != null) {
			return this.ring().ringBreadth();
		}
		return 0;
	}
	,
	onSliceClick: function (sender, e) {
		$.ig.PieChartBase.prototype.onSliceClick.call(this, sender, e);
		if (this.ring() != null) {
			this.ring().ringSeries().chart().onSliceClick(sender, e);
		}
	}
	,
	explodeSlice: function (slice, explode) {
		if (this.ring() != null && this.ring().owner() != null) {
			$.ig.PieChartBase.prototype.explodeSlice.call(this, slice, explode);
			var item = this.getSliceItem(slice);
			if (item != null) {
				this.ring().owner().explodeSlice(item, explode);
			}
		}
	}
	,
	selectSlice: function (slice, shouldSelect) {
		if (this.ring() != null && this.ring().owner() != null) {
			$.ig.PieChartBase.prototype.selectSlice.call(this, slice, shouldSelect);
			var item = this.getSliceItem(slice);
			if (item != null) {
				this.ring().owner().selectSlice(item, shouldSelect);
			}
		}
	}
	,
	getSliceItem: function (slice) {
		var en = this.arcItem().sliceItems().getEnumerator();
		while (en.moveNext()) {
			var item = en.current();
			if (item.slice() == slice) {
				return item;
			}
		}
		return null;
	}
	,
	selectedIndicesChangedOverride: function (args) {
		if (this.allowSliceSelection()) {
			this.prepareSlices();
			this.renderSlices();
			this.renderLegendItems();
		}
	}
	,
	prepareSlices: function () {
		if (this.itemsSource() == null || this.fastItemsSource() == null) {
			this._slices.count(0);
			return;
		}
		var totalSliceCount = this.valueIndices().count();
		var hasOtherSlice = this.othersValueIndices().count() > 0;
		var startAngle = $.ig.PieChartBase.prototype.roundAngle(this.actualStartAngle());
		var endAngle = $.ig.PieChartBase.prototype.roundAngle(this.actualStartAngle());
		this.totalSliceCount(totalSliceCount);
		if (hasOtherSlice) {
			totalSliceCount++;
		}
		for (var i = 0; i < totalSliceCount; i++) {
			var isOtherSlice = false;
			var value;
			if (i == totalSliceCount - 1 && hasOtherSlice) {
				value = this.othersTotal();
				isOtherSlice = true;
			} else {
				value = this.valueColumn().item(this.valueIndices().__inner[i]);
			}
			if (this.sweepDirection() == $.ig.SweepDirection.prototype.clockwise) {
				endAngle += $.ig.PieChartBase.prototype.roundAngle(Math.abs(value) * this.endAngle() / this.total());
			} else {
				endAngle -= $.ig.PieChartBase.prototype.roundAngle(Math.abs(value) * this.endAngle() / this.total());
			}
			var slice = this._slices.item(i);
			if (this.chartInnerExtent() >= 100) {
				slice.__visibility = $.ig.Visibility.prototype.collapsed;
			} else {
				slice.__visibility = $.ig.Visibility.prototype.visible;
			}
			slice.suspendCreation(true);
			slice.startAngle(startAngle);
			slice.endAngle(endAngle);
			slice.innerExtentStart(slice.innerExtentEnd(this.chartInnerExtent()));
			slice.isOthersSlice(isOtherSlice);
			slice.explodedRadius(this.actualExplodedRadius());
			slice.index(i);
			slice.dataContext(isOtherSlice ? this.others() : this.fastItemsSource().item(this.valueIndices().__inner[i]));
			slice.isExploded(this.explodedSlices().contains(i));
			slice.isSelected(this.selectedSlices().contains(i));
			startAngle = endAngle;
			slice.suspendCreation(false);
		}
		this._slices.count(totalSliceCount);
		if (this.arcItem() != null) {
			this.arcItem().updateSliceItems(this);
		}
	}
	,
	renderLegendItems: function () {
		var $self = this;
		var itemLegend = $.ig.util.cast($.ig.ItemLegend.prototype.$type, this.legend());
		if (itemLegend == null) {
			return;
		}
		if (this.labelColumn() == null || this.labelColumn().count() == 0 || ((this.ring() != null && this.ring().ringSeries() != null) && (this.ring().owner() != null && !this.ring().owner().series().contains(this.ring().ringSeries())))) {
			itemLegend.clearLegendItems(this.ring().ringSeries());
			return;
		}
		this.legendItems(new $.ig.List$1($.ig.UIElement.prototype.$type, 0));
		var en = this._slices.active().getEnumerator();
		while (en.moveNext()) {
			var slice = en.current();
			var item = new $.ig.ContentControl();
			var itemLabel = this.getLabel(slice);
			var itemBrush = slice.background();
			item.content((function () {
				var $ret = new $.ig.PieSliceDataContext();
				$ret.series($self.ring().ringSeries());
				$ret.slice(slice);
				$ret.item(slice.dataContext());
				$ret.itemBrush(itemBrush);
				$ret.itemLabel(itemLabel != null ? itemLabel.toString() : null);
				$ret.percentValue($self.getPercentValue(slice));
				$ret.isOthersSlice(slice.isOthersSlice());
				return $ret;
			}()));
			var dc = item.content();
			if (this.formatLegendLabel() != null) {
				var legendLabel = this.formatLegendLabel()(dc);
				if (legendLabel != null) {
					dc.legendLabel(legendLabel.toString());
				}
			}
			if (this.legendItemTemplate() != null) {
				item.contentTemplate(this.legendItemTemplate());
			}
			this.legendItems().add(item);
		}
		itemLegend.createLegendItemsInsert(this.legendItems(), this.ring().ringSeries());
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.PieChartBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.Arc.prototype.endAnglePropertyName:
				this.actualEndAngle(newValue);
				this.prepareSlices();
				this.prepareLabels();
				this.renderSlices();
				this.renderLabels();
				break;
			case $.ig.PieChartBase.prototype.radiusFactorPropertyName:
				if (this.ring() != null && this.ring().owner() != null) {
					this.ring().owner().prepareCenterPresenter();
				}
				break;
			case $.ig.PieChartBase.prototype.legendPropertyName:
				var oldLegend = $.ig.util.cast($.ig.ItemLegend.prototype.$type, oldValue);
				if (oldLegend != null && this.ring() != null && this.ring().ringSeries() != null) {
					oldLegend.clearLegendItems(this.ring().ringSeries());
				}
				break;
		}
	}
	,
	getContainer: function () {
		return this.view().getContainer();
	}
	,
	$type: new $.ig.Type('Arc', $.ig.PieChartBase.prototype.$type)
}, true);

$.ig.util.defType('ArcItem', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
		this.startAngle(0);
		this.endAngle(360);
		this.sliceItems(new $.ig.List$1($.ig.SliceItem.prototype.$type, 0));
	},
	_ring: null,
	ring: function (value) {
		if (arguments.length === 1) {
			this._ring = value;
			return value;
		} else {
			return this._ring;
		}
	}
	,
	_parent: null,
	parent: function (value) {
		if (arguments.length === 1) {
			this._parent = value;
			return value;
		} else {
			return this._parent;
		}
	}
	,
	_valueMemberPath: null,
	valueMemberPath: function (value) {
		if (arguments.length === 1) {
			this._valueMemberPath = value;
			return value;
		} else {
			return this._valueMemberPath;
		}
	}
	,
	_index: 0,
	index: function (value) {
		if (arguments.length === 1) {
			this._index = value;
			return value;
		} else {
			return this._index;
		}
	}
	,
	_levelDepth: 0,
	levelDepth: function (value) {
		if (arguments.length === 1) {
			this._levelDepth = value;
			return value;
		} else {
			return this._levelDepth;
		}
	}
	,
	_startAngle: 0,
	startAngle: function (value) {
		if (arguments.length === 1) {
			this._startAngle = value;
			return value;
		} else {
			return this._startAngle;
		}
	}
	,
	_endAngle: 0,
	endAngle: function (value) {
		if (arguments.length === 1) {
			this._endAngle = value;
			return value;
		} else {
			return this._endAngle;
		}
	}
	,
	_othersCategoryThreshold: 0,
	othersCategoryThreshold: function (value) {
		if (arguments.length === 1) {
			this._othersCategoryThreshold = value;
			return value;
		} else {
			return this._othersCategoryThreshold;
		}
	}
	,
	_othersCategoryType: 0,
	othersCategoryType: function (value) {
		if (arguments.length === 1) {
			this._othersCategoryType = value;
			return value;
		} else {
			return this._othersCategoryType;
		}
	}
	,
	_brushes: null,
	brushes: function (value) {
		if (arguments.length === 1) {
			this._brushes = value;
			return value;
		} else {
			return this._brushes;
		}
	}
	,
	_sliceItems: null,
	sliceItems: function (value) {
		if (arguments.length === 1) {
			this._sliceItems = value;
			return value;
		} else {
			return this._sliceItems;
		}
	}
	,
	_parentSlice: null,
	parentSlice: function (value) {
		if (arguments.length === 1) {
			this._parentSlice = value;
			return value;
		} else {
			return this._parentSlice;
		}
	}
	,
	_itemSource: null,
	itemSource: function (value) {
		if (arguments.length === 1) {
			this._itemSource = value;
			return value;
		} else {
			return this._itemSource;
		}
	}
	,
	prepareData: function (startAngleParam, endAngleParam) {
		var total = 0;
		var othersTotal = 0;
		var valueIndices = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		var othersValueIndices = new $.ig.List$1($.ig.Number.prototype.$type, 0);
		var others = new $.ig.List$1($.ig.Object.prototype.$type, 0);
		this.sliceItems().clear();
		if (this.itemSource() == null) {
			return;
		}
		var fs = new $.ig.FastItemsSource();
		fs.itemsSource(this.itemSource());
		var valueColumn = this.registerDoubleColumn(fs, this.valueMemberPath());
		if (valueColumn == null || valueColumn.count() == 0) {
			fs.deregisterColumn(valueColumn);
			fs.detach();
			return;
		}
		var en = valueColumn.getEnumerator();
		while (en.moveNext()) {
			var value = en.current();
			if ($.ig.util.isNaN(value) || Number.isInfinity(value) || value <= 0) {
				continue;
			}
			total = total + value;
		}
		for (var i = 0; i < valueColumn.count(); i++) {
			var value1 = valueColumn.item(i);
			if ($.ig.util.isNaN(value1) || Number.isInfinity(value1) || value1 <= 0) {
				continue;
			}
			var calculatedValue = this.othersCategoryType() == $.ig.OthersCategoryType.prototype.percent ? value1 / total : value1;
			var calculatedThreshold = this.othersCategoryType() == $.ig.OthersCategoryType.prototype.percent ? this.othersCategoryThreshold() / 100 : this.othersCategoryThreshold();
			if (calculatedValue <= calculatedThreshold) {
				othersTotal = othersTotal + value1;
				othersValueIndices.add(i);
				others.add1(fs.item(i));
			} else {
				valueIndices.add(i);
			}
		}
		var totalSliceCount = valueIndices.count();
		var hasOtherSlice = othersValueIndices.count() > 0;
		var startAngle = $.ig.PieChartBase.prototype.roundAngle(startAngleParam);
		var endAngle = $.ig.PieChartBase.prototype.roundAngle(startAngleParam);
		if (hasOtherSlice) {
			totalSliceCount++;
		}
		for (var i1 = 0; i1 < totalSliceCount; i1++) {
			var isOtherSlice = false;
			var value2;
			if (i1 == totalSliceCount - 1 && hasOtherSlice) {
				value2 = othersTotal;
				isOtherSlice = true;
			} else {
				value2 = valueColumn.item(valueIndices.__inner[i1]);
			}
			var sweepDirection = $.ig.SweepDirection.prototype.clockwise;
			if (sweepDirection == $.ig.SweepDirection.prototype.clockwise) {
				endAngle += $.ig.PieChartBase.prototype.roundAngle(Math.abs(value2) * endAngleParam / total);
			} else {
				endAngle -= $.ig.PieChartBase.prototype.roundAngle(Math.abs(value2) * endAngleParam / total);
			}
			this.sliceItems().add((function () {
				var $ret = new $.ig.SliceItem();
				$ret.startAngle(startAngle);
				$ret.endAngle(endAngle - startAngle);
				$ret.data(isOtherSlice ? others : fs.item(valueIndices.__inner[i1]));
				return $ret;
			}()));
			startAngle = endAngle;
		}
		fs.deregisterColumn(valueColumn);
		fs.detach();
	}
	,
	registerDoubleColumn: function (fs, memberPath) {
		var coercionMethod = null;
		return fs.registerColumn(memberPath, coercionMethod, false);
	}
	,
	prepareSliceItems: function (seriesStartAngle) {
		var startAngle = this.parentSlice() == null ? seriesStartAngle : this.parentSlice().startAngle();
		var endAngle = this.parentSlice() == null ? 360 : this.parentSlice().endAngle();
		this.prepareData(startAngle, endAngle);
	}
	,
	updateSliceItems: function (arc) {
		var $self = this;
		this.sliceItems().clear();
		for (var index = 0; index < arc._slices.count(); index++) {
			var slice = arc._slices.item(index);
			this.sliceItems().add((function () {
				var $ret = new $.ig.SliceItem();
				$ret.startAngle(slice.startAngle());
				$ret.endAngle(slice.endAngle() - slice.startAngle());
				$ret.index(index);
				$ret.arcItem($self);
				$ret.data(slice.dataContext());
				$ret.isOther(slice.isOthersSlice());
				$ret.slice(slice);
				return $ret;
			}()));
		}
	}
	,
	$type: new $.ig.Type('ArcItem', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('DoughnutChartVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_series: null,
	series: function (value) {
		if (arguments.length === 1) {
			this._series = value;
			return value;
		} else {
			return this._series;
		}
	}
	,
	_height: 0,
	height: function (value) {
		if (arguments.length === 1) {
			this._height = value;
			return value;
		} else {
			return this._height;
		}
	}
	,
	_width: 0,
	width: function (value) {
		if (arguments.length === 1) {
			this._width = value;
			return value;
		} else {
			return this._width;
		}
	}
	,
	_holeRadius: 0,
	holeRadius: function (value) {
		if (arguments.length === 1) {
			this._holeRadius = value;
			return value;
		} else {
			return this._holeRadius;
		}
	}
	,
	scaleByViewport: function () {
		if (this.series() != null) {
			var en = this.series().getEnumerator();
			while (en.moveNext()) {
				var series = en.current();
				var en1 = series.rings().getEnumerator();
				while (en1.moveNext()) {
					var ring = en1.current();
					var en2 = ring.arcs().getEnumerator();
					while (en2.moveNext()) {
						var arc = en2.current();
						var en3 = arc.slices().getEnumerator();
						while (en3.moveNext()) {
							var slice = en3.current();
							slice.scaleByViewport(this.viewport());
						}
					}
				}
			}
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		var first = true;
		sb.appendLine1("{");
		if (!$.ig.util.isNaN(this.width())) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("width: ");
			sb.appendLine1(this.width().toString());
		}
		if (!$.ig.util.isNaN(this.height())) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("height: ");
			sb.appendLine1(this.height().toString());
		}
		if (!$.ig.util.isNaN(this.holeRadius())) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("holeRadius: ");
			sb.appendLine1(this.holeRadius().toString());
		}
		if (this.series() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.appendLine1("series: [");
			for (var i = 0; i < this.series().count(); i++) {
				if (i != 0) {
					sb.append5(", ");
				}
				sb.append5(this.series().__inner[i].serialize());
			}
			sb.appendLine1("]");
		}
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	_viewport: null,
	viewport: function (value) {
		if (arguments.length === 1) {
			this._viewport = value;
			return value;
		} else {
			return this._viewport;
		}
	}
	,
	$type: new $.ig.Type('DoughnutChartVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RingSeriesVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_name: null,
	name: function (value) {
		if (arguments.length === 1) {
			this._name = value;
			return value;
		} else {
			return this._name;
		}
	}
	,
	_labelExtent: 0,
	labelExtent: function (value) {
		if (arguments.length === 1) {
			this._labelExtent = value;
			return value;
		} else {
			return this._labelExtent;
		}
	}
	,
	_labelsPosition: null,
	labelsPosition: function (value) {
		if (arguments.length === 1) {
			this._labelsPosition = value;
			return value;
		} else {
			return this._labelsPosition;
		}
	}
	,
	_leaderLineType: null,
	leaderLineType: function (value) {
		if (arguments.length === 1) {
			this._leaderLineType = value;
			return value;
		} else {
			return this._leaderLineType;
		}
	}
	,
	_leaderLineVisibility: 0,
	leaderLineVisibility: function (value) {
		if (arguments.length === 1) {
			this._leaderLineVisibility = value;
			return value;
		} else {
			return this._leaderLineVisibility;
		}
	}
	,
	_leaderLineMargin: 0,
	leaderLineMargin: function (value) {
		if (arguments.length === 1) {
			this._leaderLineMargin = value;
			return value;
		} else {
			return this._leaderLineMargin;
		}
	}
	,
	_rings: null,
	rings: function (value) {
		if (arguments.length === 1) {
			this._rings = value;
			return value;
		} else {
			return this._rings;
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		var first = true;
		sb.appendLine1("{");
		if (this.name() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("name: ");
			sb.appendLine1("'" + this.name() + "'");
		}
		if (!$.ig.util.isNaN(this.labelExtent())) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("labelExtent: ");
			sb.appendLine1(this.labelExtent().toString());
		}
		if (this.labelsPosition() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("labelsPosition: ");
			sb.appendLine1("'" + this.labelsPosition() + "'");
		}
		if (this.leaderLineType() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("leaderLineType: ");
			sb.appendLine1("'" + this.leaderLineType() + "'");
		}
		sb.append5(", leaderLineVisibility: ");
		sb.appendLine1(this.leaderLineVisibility() == $.ig.Visibility.prototype.visible ? "1" : "0");
		if (!$.ig.util.isNaN(this.leaderLineMargin())) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("leaderLineMargin: ");
			sb.appendLine1(this.leaderLineMargin().toString());
		}
		sb.appendLine1(", rings: [");
		for (var i = 0; i < this.rings().count(); i++) {
			if (i != 0) {
				sb.append5(", ");
			}
			sb.append5(this.rings().__inner[i].serialize());
		}
		sb.appendLine1("]}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('RingSeriesVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RingSeriesVisualDataList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.RingSeriesVisualData.prototype.$type, 0);
	},
	$type: new $.ig.Type('RingSeriesVisualDataList', $.ig.List$1.prototype.$type.specialize($.ig.RingSeriesVisualData.prototype.$type))
}, true);

$.ig.util.defType('RingVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_arcs: null,
	arcs: function (value) {
		if (arguments.length === 1) {
			this._arcs = value;
			return value;
		} else {
			return this._arcs;
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		var first = true;
		sb.appendLine1("{");
		if (this.arcs() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.appendLine1("arcs: [");
			for (var i = 0; i < this.arcs().count(); i++) {
				if (i != 0) {
					sb.append5(", ");
				}
				sb.append5(this.arcs().__inner[i].serialize());
			}
			sb.appendLine1("]");
		}
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('RingVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RingVisualDataList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.RingVisualData.prototype.$type, 0);
	},
	$type: new $.ig.Type('RingVisualDataList', $.ig.List$1.prototype.$type.specialize($.ig.RingVisualData.prototype.$type))
}, true);

$.ig.util.defType('ArcVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_slices: null,
	slices: function (value) {
		if (arguments.length === 1) {
			this._slices = value;
			return value;
		} else {
			return this._slices;
		}
	}
	,
	_leaderLine: null,
	leaderLine: function (value) {
		if (arguments.length === 1) {
			this._leaderLine = value;
			return value;
		} else {
			return this._leaderLine;
		}
	}
	,
	scaleByViewport: function (viewport) {
		var en = this.slices().getEnumerator();
		while (en.moveNext()) {
			var slice = en.current();
			slice.scaleByViewport(viewport);
		}
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		var first = true;
		sb.appendLine1("{");
		if (this.leaderLine() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("leaderLine: ");
			sb.appendLine1(this.leaderLine().serialize());
		}
		if (this.slices() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.appendLine1("slices: [");
			for (var i = 0; i < this.slices().count(); i++) {
				if (i != 0) {
					sb.append5(", ");
				}
				sb.append5(this.slices().__inner[i].serialize());
			}
		}
		sb.appendLine1("]}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('ArcVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('ArcVisualDataList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.ArcVisualData.prototype.$type, 0);
	},
	$type: new $.ig.Type('ArcVisualDataList', $.ig.List$1.prototype.$type.specialize($.ig.ArcVisualData.prototype.$type))
}, true);

$.ig.util.defType('SliceVisualData', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_pieSlice: null,
	pieSlice: function (value) {
		if (arguments.length === 1) {
			this._pieSlice = value;
			return value;
		} else {
			return this._pieSlice;
		}
	}
	,
	_outline: null,
	outline: function (value) {
		if (arguments.length === 1) {
			this._outline = value;
			return value;
		} else {
			return this._outline;
		}
	}
	,
	_labelVisualData: null,
	labelVisualData: function (value) {
		if (arguments.length === 1) {
			this._labelVisualData = value;
			return value;
		} else {
			return this._labelVisualData;
		}
	}
	,
	_backgroundPath: null,
	backgroundPath: function (value) {
		if (arguments.length === 1) {
			this._backgroundPath = value;
			return value;
		} else {
			return this._backgroundPath;
		}
	}
	,
	_origin: null,
	origin: function (value) {
		if (arguments.length === 1) {
			this._origin = value;
			return value;
		} else {
			return this._origin;
		}
	}
	,
	_explodedOrigin: null,
	explodedOrigin: function (value) {
		if (arguments.length === 1) {
			this._explodedOrigin = value;
			return value;
		} else {
			return this._explodedOrigin;
		}
	}
	,
	_isExploded: false,
	isExploded: function (value) {
		if (arguments.length === 1) {
			this._isExploded = value;
			return value;
		} else {
			return this._isExploded;
		}
	}
	,
	_isSelected: false,
	isSelected: function (value) {
		if (arguments.length === 1) {
			this._isSelected = value;
			return value;
		} else {
			return this._isSelected;
		}
	}
	,
	_index: 0,
	index: function (value) {
		if (arguments.length === 1) {
			this._index = value;
			return value;
		} else {
			return this._index;
		}
	}
	,
	_radius: 0,
	radius: function (value) {
		if (arguments.length === 1) {
			this._radius = value;
			return value;
		} else {
			return this._radius;
		}
	}
	,
	_startAngle: 0,
	startAngle: function (value) {
		if (arguments.length === 1) {
			this._startAngle = value;
			return value;
		} else {
			return this._startAngle;
		}
	}
	,
	_endAngle: 0,
	endAngle: function (value) {
		if (arguments.length === 1) {
			this._endAngle = value;
			return value;
		} else {
			return this._endAngle;
		}
	}
	,
	_visibility: 0,
	visibility: function (value) {
		if (arguments.length === 1) {
			this._visibility = value;
			return value;
		} else {
			return this._visibility;
		}
	}
	,
	scaleByViewport: function (viewport) {
		this.origin({ __x: (this.origin().__x - viewport.left()) / viewport.width(), __y: (this.origin().__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		this.explodedOrigin({ __x: (this.explodedOrigin().__x - viewport.left()) / viewport.width(), __y: (this.explodedOrigin().__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
	}
	,
	serialize: function () {
		var sb = new $.ig.StringBuilder(0);
		var first = true;
		sb.appendLine1("{");
		if (this.pieSlice() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("pieSlice: ");
			sb.appendLine1(this.pieSlice().serialize());
		}
		if (this.labelVisualData() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("labelVisualData: ");
			sb.appendLine1(this.labelVisualData().serialize());
		}
		if (this.backgroundPath() != null) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("backgroundPath: ");
			sb.appendLine1(this.backgroundPath().serialize());
		}
		if (first) {
			first = false;
		} else {
			sb.append5(", ");
		}
		sb.append5("isExploded: ");
		sb.appendLine1(this.isExploded().toString());
		if (first) {
			first = false;
		} else {
			sb.append5(", ");
		}
		sb.append5("isSelected: ");
		sb.appendLine1(this.isSelected().toString());
		if (!$.ig.util.isNaN(this.explodedOrigin().__x) && !$.ig.util.isNaN(this.explodedOrigin().__y)) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("explodedOrigin: ");
			sb.appendLine1("{ x: " + this.explodedOrigin().__x.toString() + ", y: " + this.explodedOrigin().__y.toString() + "}");
		}
		if (!$.ig.util.isNaN(this.origin().__x) && !$.ig.util.isNaN(this.origin().__y)) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("origin: ");
			sb.appendLine1("{ x: " + this.origin().__x.toString() + ", y: " + this.origin().__y.toString() + "}");
		}
		if (!$.ig.util.isNaN(this.index())) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("index: ");
			sb.appendLine1(this.index().toString());
		}
		if (!$.ig.util.isNaN(this.radius())) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("radius: ");
			sb.appendLine1(this.radius().toString());
		}
		sb.append5(", visibility: ");
		sb.appendLine1(this.visibility() == $.ig.Visibility.prototype.visible ? "1" : "0");
		if (!$.ig.util.isNaN(this.startAngle())) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("startAngle: ");
			sb.appendLine1(this.startAngle().toString());
		}
		if (!$.ig.util.isNaN(this.endAngle())) {
			if (first) {
				first = false;
			} else {
				sb.append5(", ");
			}
			sb.append5("endAngle: ");
			sb.appendLine1(this.endAngle().toString());
		}
		sb.appendLine1("}");
		return sb.toString();
	}
	,
	$type: new $.ig.Type('SliceVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('SliceVisualDataList', 'List$1', {
	init: function () {
		$.ig.List$1.prototype.init.call(this, $.ig.SliceVisualData.prototype.$type, 0);
	},
	$type: new $.ig.Type('SliceVisualDataList', $.ig.List$1.prototype.$type.specialize($.ig.SliceVisualData.prototype.$type))
}, true);

$.ig.util.defType('DoughnutChartLabelVisualData', 'Object', {
	init: function () {
		this._labelSize = new $.ig.Size();
		$.ig.Object.prototype.init.call(this);
	},
	_labelValue: null,
	labelValue: function (value) {
		if (arguments.length === 1) {
			this._labelValue = value;
			return value;
		} else {
			return this._labelValue;
		}
	}
	,
	_labelSize: null,
	labelSize: function (value) {
		if (arguments.length === 1) {
			this._labelSize = value;
			return value;
		} else {
			return this._labelSize;
		}
	}
	,
	_labelPosition: null,
	labelPosition: function (value) {
		if (arguments.length === 1) {
			this._labelPosition = value;
			return value;
		} else {
			return this._labelPosition;
		}
	}
	,
	scaleByViewport: function (viewport) {
		this.labelPosition({ __x: (this.labelPosition().__x - viewport.left()) / viewport.width(), __y: (this.labelPosition().__y - viewport.top()) / viewport.height(), $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		this.labelSize(new $.ig.Size(1, (this.labelSize().width()) / viewport.width(), (this.labelSize().height()) / viewport.height()));
	}
	,
	serialize: function () {
		return "{ labelValue: '" + this.labelValue() + "', labelSize: { width: " + this.labelSize().width() + ", height: " + this.labelSize().height() + "}, labelPosition: { x: " + this.labelPosition().__x + ", y: " + this.labelPosition().__y + "}}";
	}
	,
	$type: new $.ig.Type('DoughnutChartLabelVisualData', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RingSeriesBase', 'Control', {
	__propertyUpdatedOverride: null,
	init: function () {
		var $self = this;
		$.ig.Control.prototype.init.call(this);
		this.location({ __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		this.__propertyUpdatedOverride = function (o, e) {
			$self.propertyUpdatedOverride(o, e.propertyName(), e.oldValue(), e.newValue());
		};
		this.propertyUpdated = $.ig.Delegate.prototype.combine(this.propertyUpdated, this.__propertyUpdatedOverride);
		this.view(this.createView());
		this.onViewCreated(this.view());
		this.view().onInit();
		var nullValue = $.ig.util.toNullable($.ig.Boolean.prototype.$type, null);
		this.isSurfaceInteractionDisabled(nullValue);
		this._fastItemsSource_Event = function (o, e) {
			$self.dataUpdatedOverride(e.action(), e.position(), e.count(), e.propertyName());
		};
	},
	_chart: null,
	chart: function (value) {
		if (arguments.length === 1) {
			this._chart = value;
			return value;
		} else {
			return this._chart;
		}
	}
	,
	itemsSource: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.itemsSourceProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.itemsSourceProperty);
		}
	}
	,
	valueMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.valueMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.valueMemberPathProperty);
		}
	}
	,
	labelMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.labelMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.labelMemberPathProperty);
		}
	}
	,
	legendLabelMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.legendLabelMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.legendLabelMemberPathProperty);
		}
	}
	,
	propertyChanged: null,
	propertyUpdated: null,
	raisePropertyChanged: function (propertyName, oldValue, newValue) {
		if (this.propertyChanged != null) {
			this.propertyChanged(this, new $.ig.PropertyChangedEventArgs(propertyName));
		}
		if (this.propertyUpdated != null) {
			this.propertyUpdated(this, new $.ig.PropertyUpdatedEventArgs(propertyName, oldValue, newValue));
		}
	}
	,
	labelsPosition: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.labelsPositionProperty, $.ig.LabelsPosition.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.RingSeriesBase.prototype.labelsPositionProperty));
		}
	}
	,
	leaderLineVisibility: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.leaderLineVisibilityProperty, $.ig.Visibility.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.RingSeriesBase.prototype.leaderLineVisibilityProperty));
		}
	}
	,
	leaderLineStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.leaderLineStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.leaderLineStyleProperty);
		}
	}
	,
	leaderLineType: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.leaderLineTypeProperty, $.ig.LeaderLineType.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.RingSeriesBase.prototype.leaderLineTypeProperty));
		}
	}
	,
	leaderLineMargin: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.leaderLineMarginProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.leaderLineMarginProperty);
		}
	}
	,
	toolTip: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.toolTipProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.toolTipProperty);
		}
	}
	,
	othersCategoryThreshold: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.othersCategoryThresholdProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.othersCategoryThresholdProperty);
		}
	}
	,
	othersCategoryType: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.othersCategoryTypeProperty, $.ig.OthersCategoryType.prototype.getBox(value));
			return value;
		} else {
			return $.ig.util.getEnumValue(this.getValue($.ig.RingSeriesBase.prototype.othersCategoryTypeProperty));
		}
	}
	,
	othersCategoryText: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.othersCategoryTextProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.othersCategoryTextProperty);
		}
	}
	,
	legend: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.legendProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.legendProperty);
		}
	}
	,
	formatLabel: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.formatLabelProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.formatLabelProperty);
		}
	}
	,
	formatLegendLabel: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.formatLegendLabelProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.formatLegendLabelProperty);
		}
	}
	,
	labelExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.labelExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.labelExtentProperty);
		}
	}
	,
	startAngle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.startAngleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.startAngleProperty);
		}
	}
	,
	othersCategoryStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.othersCategoryStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.othersCategoryStyleProperty);
		}
	}
	,
	selectedStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.selectedStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.selectedStyleProperty);
		}
	}
	,
	selectedStyleResolved: function (value) {
		if (arguments.length === 1) {
			var oldValue = this.selectedStyleResolved();
			if (this.selectedStyle() != null && value != this.selectedStyle()) {
				value = this.selectedStyle();
			}
			this.raisePropertyChanged($.ig.RingSeriesBase.prototype.selectedStyleResolvedPropertyName, oldValue, value);
			return value;
		} else {
			if (this.selectedStyle() != null) {
				return this.selectedStyle();
			}
			if (this.chart() != null && this.chart().selectedStyle() != null) {
				return this.chart().selectedStyle();
			}
			return null;
		}
	}
	,
	toolTipStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.toolTipStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.toolTipStyleProperty);
		}
	}
	,
	brushes: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.brushesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.brushesProperty);
		}
	}
	,
	outlines: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.outlinesProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.outlinesProperty);
		}
	}
	,
	legendItemTemplate: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.legendItemTemplateProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.legendItemTemplateProperty);
		}
	}
	,
	legendItemBadgeTemplate: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.legendItemBadgeTemplateProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.legendItemBadgeTemplateProperty);
		}
	}
	,
	labelTemplate: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.labelTemplateProperty, value);
			return value;
		} else {
			return $.ig.util.cast($.ig.DataTemplate.prototype.$type, this.getValue($.ig.RingSeriesBase.prototype.labelTemplateProperty));
		}
	}
	,
	isSurfaceInteractionDisabled: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.isSurfaceInteractionDisabledProperty, $.ig.util.unwrapNullable(value));
			return value;
		} else {
			return $.ig.util.wrapNullable($.ig.Boolean.prototype.$type, this.getValue($.ig.RingSeriesBase.prototype.isSurfaceInteractionDisabledProperty));
		}
	}
	,
	radiusFactor: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.radiusFactorProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.radiusFactorProperty);
		}
	}
	,
	__fastItemsSourceAttached: false,
	_fastItemsSource_Event: null,
	fastItemsSource: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RingSeriesBase.prototype.fastItemsSourceProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RingSeriesBase.prototype.fastItemsSourceProperty);
		}
	}
	,
	_location: null,
	location: function (value) {
		if (arguments.length === 1) {
			this._location = value;
			return value;
		} else {
			return this._location;
		}
	}
	,
	_view: null,
	view: function (value) {
		if (arguments.length === 1) {
			this._view = value;
			return value;
		} else {
			return this._view;
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		if (propertyName == $.ig.RingSeriesBase.prototype.itemsSourcePropertyName) {
			this.fastItemsSource((function () {
				var $ret = new $.ig.FastItemsSource();
				$ret.itemsSource(newValue);
				return $ret;
			}()));
		}
		if (propertyName == $.ig.RingSeriesBase.prototype.fastItemsSourcePropertyName) {
			if ($.ig.util.cast($.ig.FastItemsSource.prototype.$type, oldValue) != null) {
				var $t = ($.ig.util.cast($.ig.FastItemsSource.prototype.$type, oldValue));
				$t.event = $.ig.Delegate.prototype.remove($t.event, this._fastItemsSource_Event);
				this.__fastItemsSourceAttached = false;
			}
			if ($.ig.util.cast($.ig.FastItemsSource.prototype.$type, newValue) != null) {
				var $t1 = ($.ig.util.cast($.ig.FastItemsSource.prototype.$type, newValue));
				$t1.event = $.ig.Delegate.prototype.combine($t1.event, this._fastItemsSource_Event);
				this.__fastItemsSourceAttached = true;
			}
			this.prepareData();
		}
		switch (propertyName) {
			case $.ig.RingSeriesBase.prototype.selectedStylePropertyName:
				this.selectedStyleResolved(newValue);
				break;
		}
	}
	,
	_rootCanvas: null,
	rootCanvas: function (value) {
		if (arguments.length === 1) {
			this._rootCanvas = value;
			return value;
		} else {
			return this._rootCanvas;
		}
	}
	,
	dataUpdatedOverride: function (action, position, count, propertyName) {
		this.prepareData();
	}
	,
	createView: function () {
		return new $.ig.RingSeriesBaseView(this);
	}
	,
	onViewCreated: function (view) {
	}
	,
	analyzeRings: function () {
	}
	,
	prepareData: function () {
	}
	,
	prepareBrushes: function () {
	}
	,
	provideContainer: function (container) {
		this.view().provideContainer(container);
	}
	,
	clearView: function () {
		if (this.view() != null) {
			this.view().clearView();
		}
	}
	,
	renderSeries: function () {
	}
	,
	renderLegendItems: function () {
	}
	,
	notifyInsertItem: function (index, newItem) {
		this.handleCollectionChanged(new $.ig.NotifyCollectionChangedEventArgs(1, $.ig.NotifyCollectionChangedAction.prototype.add, newItem, index));
	}
	,
	notifySetItem: function (index, oldItem, newItem) {
		this.handleCollectionChanged(new $.ig.NotifyCollectionChangedEventArgs(2, $.ig.NotifyCollectionChangedAction.prototype.replace, newItem, oldItem, index));
	}
	,
	notifyClearItems: function () {
		this.handleCollectionChanged(new $.ig.NotifyCollectionChangedEventArgs(0, $.ig.NotifyCollectionChangedAction.prototype.reset));
	}
	,
	notifyRemoveItem: function (index, oldItem) {
		this.handleCollectionChanged(new $.ig.NotifyCollectionChangedEventArgs(1, $.ig.NotifyCollectionChangedAction.prototype.remove, oldItem, index));
	}
	,
	handleCollectionChanged: function (e) {
		if (this.fastItemsSource() != null) {
			this.fastItemsSource().handleCollectionChanged(e);
		}
	}
	,
	$type: new $.ig.Type('RingSeriesBase', $.ig.Control.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('HierarchicalRingSeries', 'RingSeriesBase', {
	init: function () {
		$.ig.RingSeriesBase.prototype.init.call(this);
		this.defaultStyleKey($.ig.HierarchicalRingSeries.prototype.$type);
	},
	_rings: null,
	rings: function (value) {
		if (arguments.length === 1) {
			this._rings = value;
			return value;
		} else {
			return this._rings;
		}
	}
	,
	childrenMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HierarchicalRingSeries.prototype.childrenMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HierarchicalRingSeries.prototype.childrenMemberPathProperty);
		}
	}
	,
	analyzeRings: function () {
		if (this.itemsSource() == null) {
			return new $.ig.RingCollection();
		}
		this.rings(this.traverseTree(this.itemsSource()));
		return this.rings();
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.RingSeriesBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		if (propertyName == $.ig.RingSeriesBase.prototype.startAnglePropertyName) {
			var oldAngle = oldValue;
			var newAngle = newValue;
			var delta = newAngle - oldAngle;
			if (this.rings() != null && this.rings().count() > 0) {
				for (var index = 0; index < this.rings().count(); index++) {
					var ring = this.rings().__inner[index];
					var en = ring.arcItems().getEnumerator();
					while (en.moveNext()) {
						var arc = en.current();
						arc.startAngle(arc.startAngle() + delta);
					}
				}
			}
		}
		if (propertyName == $.ig.RingSeriesBase.prototype.brushesPropertyName) {
			this.prepareBrushes();
		}
		if (this.rings() != null && this.view().isReady()) {
			var resized = false;
			var en1 = this.rings().getEnumerator();
			while (en1.moveNext()) {
				var ring1 = en1.current();
				ring1.prepareArcs();
				if (ring1.renderArcs()) {
					resized = true;
				}
			}
			if (resized) {
				var en2 = this.rings().getEnumerator();
				while (en2.moveNext()) {
					var ring2 = en2.current();
					ring2.ringSeries().view().onSizeChanged();
				}
			}
		}
	}
	,
	prepareData: function () {
		if (this.chart() != null) {
			this.chart().prepareRingCollection();
			this.chart().renderChart();
		}
	}
	,
	prepareBrushes: function () {
		if (this.rings() != null) {
			var en = this.rings().getEnumerator();
			while (en.moveNext()) {
				var ring = en.current();
				var en1 = ring.arcItems().getEnumerator();
				while (en1.moveNext()) {
					var arcItem = en1.current();
					this.setBrush(arcItem);
				}
			}
		}
	}
	,
	renderSeries: function () {
		if (this.rings() != null && this.rings().count() > 0) {
			var outerRing = this.rings().__inner[this.rings().count() - 1];
			this.width(outerRing.controlSize().width());
			this.height(outerRing.controlSize().height());
			this.view().positionSeries(outerRing.center().__x, outerRing.center().__y);
		}
	}
	,
	renderLegendItems: function () {
		for (var i = 0; i < this.rings().count(); i++) {
			var en = this.rings().__inner[i].ringControl()._arcs.active().getEnumerator();
			while (en.moveNext()) {
				var arc = en.current();
				arc.renderLegendItems();
			}
		}
	}
	,
	reflectItemSource: function (obj_) {
		var memberPath_ = this.childrenMemberPath();
		if (obj_[memberPath_] !== undefined) {
			return obj_[memberPath_];
		}
		return null;
	}
	,
	traverseTree: function (root) {
		var $self = this;
		var parents = new $.ig.IgQueue$1($.ig.ArcItem.prototype.$type);
		var nodes = new $.ig.IgQueue$1($.ig.ArcItem.prototype.$type);
		var rootInfo = (function () {
			var $ret = new $.ig.ArcItem();
			$ret.levelDepth(0);
			$ret.itemSource(root);
			$ret.valueMemberPath($self.valueMemberPath());
			$ret.othersCategoryType($self.othersCategoryType());
			$ret.othersCategoryThreshold($self.othersCategoryThreshold());
			return $ret;
		}());
		rootInfo.prepareSliceItems(this.startAngle());
		var parentInfo = (function () {
			var $ret = new $.ig.ArcItem();
			$ret.levelDepth(-1);
			$ret.itemSource(null);
			return $ret;
		}());
		nodes.enqueue(rootInfo);
		parents.enqueue(parentInfo);
		var rings = new $.ig.RingCollection();
		var currentRing = null;
		var prevLevelDepth = -1;
		while (nodes.count() > 0) {
			var current;
			var $ret = nodes.dequeue(current);
			current = $ret.p0;
			var parent;
			var $ret1 = parents.dequeue(parent);
			parent = $ret1.p0;
			if (current == null) {
				continue;
			}
			var index = 0;
			var en = current.sliceItems().getEnumerator();
			while (en.moveNext()) {
				var child = en.current();
				var childItemSource = this.reflectItemSource(child.data());
				if ((childItemSource != null && this.isEmpty(childItemSource) == false) || child.isOther()) {
					var childInfo = (function () {
						var $ret = new $.ig.ArcItem();
						$ret.levelDepth(current.levelDepth() + 1);
						$ret.itemSource(child.isOther() ? (function () {
							var $ret = new $.ig.List$1($.ig.Number.prototype.$type, 0);
							$ret.add(0);
							return $ret;
						}()) : childItemSource);
						$ret.index(index);
						$ret.parent(current);
						$ret.valueMemberPath($self.valueMemberPath());
						$ret.parentSlice(child);
						return $ret;
					}());
					childInfo.prepareSliceItems(this.startAngle());
					nodes.enqueue(childInfo);
					parents.enqueue(current);
				}
				index++;
			}
			var newRing = this.doSomething(current, parent, prevLevelDepth, currentRing);
			if (newRing != currentRing) {
				rings.add(newRing);
				currentRing = newRing;
			}
			prevLevelDepth = current.levelDepth();
		}
		return rings;
	}
	,
	doSomething: function (current, parent, prevLevelDepth, currentRing) {
		var $self = this;
		current.startAngle(current.parentSlice() == null ? this.startAngle() : current.parentSlice().startAngle());
		current.endAngle(current.parentSlice() == null ? 360 : current.parentSlice().endAngle());
		this.setBrush(current);
		if (current.levelDepth() != prevLevelDepth) {
			var newRing = (function () {
				var $ret = new $.ig.Ring();
				$ret.ringSeries($self);
				return $ret;
			}());
			newRing.arcItems().add(current);
			current.ring(newRing);
			return newRing;
		}
		current.ring(currentRing);
		currentRing.arcItems().add(current);
		return currentRing;
	}
	,
	isEmpty: function (en) {
		var en1 = en.getEnumerator();
		while (en1.moveNext()) {
			var c = en1.current();
			return false;
		}
		return true;
	}
	,
	setBrush: function (arcItem) {
		if (arcItem.parent() == null) {
			arcItem.brushes(this.brushes());
		} else if (arcItem.parent().levelDepth() == 0) {
			arcItem.brushes(new $.ig.BrushCollection());
			if (arcItem.parent().brushes() != null) {
				arcItem.brushes().add(arcItem.parent().brushes().item(arcItem.index() % arcItem.parent().brushes().count()));
			}
		} else {
			arcItem.brushes(arcItem.parent().brushes());
		}
	}
	,
	$type: new $.ig.Type('HierarchicalRingSeries', $.ig.RingSeriesBase.prototype.$type)
}, true);

$.ig.util.defType('IgQueue$1', 'Object', {
	$t: null,
	init: function ($t) {
		this.$t = $t;
		this.$type = this.$type.specialize(this.$t);
		this._count = 0;
		this._front = null;
		this._end = null;
		this._temp = null;
		$.ig.Object.prototype.init.call(this);
	},
	_count: 0,
	_front: null,
	_end: null,
	_temp: null,
	empty: function () {
		return (this._count == 0);
	}
	,
	count: function () {
		return this._count;
	}
	,
	enqueue: function (obj) {
		if (this._count == 0) {
			this._front = this._end = new $.ig.Node($.ig.util.getBoxIfEnum(this.$t, obj), this._front);
		} else {
			this._end._next = new $.ig.Node($.ig.util.getBoxIfEnum(this.$t, obj), this._end._next);
			this._end = this._end._next;
		}
		this._count++;
	}
	,
	dequeue: function (value) {
		this._temp = this._front;
		if (this._count == 0) {
			throw new $.ig.Error(1, "tried to serve from an empty Queue");
		}
		this._front = this._front._next;
		this._count--;
		value = $.ig.util.cast(this.$t, this._temp._value);
		return {
			p0: value
		};
	}
	,
	$type: new $.ig.Type('IgQueue$1', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('Node', 'Object', {
	_next: null,
	_value: null,
	init: function (value, next) {
		$.ig.Object.prototype.init.call(this);
		this._next = next;
		this._value = value;
	},
	$type: new $.ig.Type('Node', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('HoleDimensionsChangedEventArgs', 'EventArgs', {
	init: function (center, radius) {
		$.ig.EventArgs.prototype.init.call(this);
		this.__center = center;
		this.__radius = radius;
	},
	__center: null,
	center: function () {
		return this.__center;
	}
	,
	__radius: 0,
	radius: function () {
		return this.__radius;
	}
	,
	$type: new $.ig.Type('HoleDimensionsChangedEventArgs', $.ig.EventArgs.prototype.$type)
}, true);

$.ig.util.defType('Ring', 'Object', {
	init: function () {
		var $self = this;
		this._controlSize = new $.ig.Size();
		$.ig.Object.prototype.init.call(this);
		this.center({ __x: 0, __y: 0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		this.controlSize(new $.ig.Size(1, 0, 0));
		this.ringControl((function () {
			var $ret = new $.ig.RingControl();
			$ret.ring($self);
			return $ret;
		}()));
		this.arcItems(new $.ig.List$1($.ig.ArcItem.prototype.$type, 0));
		this.clearContent(true);
	},
	_index: 0,
	index: function (value) {
		if (arguments.length === 1) {
			this._index = value;
			return value;
		} else {
			return this._index;
		}
	}
	,
	_innerExtend: 0,
	innerExtend: function (value) {
		if (arguments.length === 1) {
			this._innerExtend = value;
			return value;
		} else {
			return this._innerExtend;
		}
	}
	,
	_controlSize: null,
	controlSize: function (value) {
		if (arguments.length === 1) {
			this._controlSize = value;
			return value;
		} else {
			return this._controlSize;
		}
	}
	,
	_center: null,
	center: function (value) {
		if (arguments.length === 1) {
			this._center = value;
			return value;
		} else {
			return this._center;
		}
	}
	,
	_ringControl: null,
	ringControl: function (value) {
		if (arguments.length === 1) {
			this._ringControl = value;
			return value;
		} else {
			return this._ringControl;
		}
	}
	,
	__ringSeries: null,
	ringSeries: function (value) {
		if (arguments.length === 1) {
			this.__ringSeries = value;
			this.__ringSeries.view().addRing(this.ringControl());
			if (this.ringControl() != null) {
				this.ringControl().view().onSeriesProvided(value);
			}
			return value;
		} else {
			return this.__ringSeries;
		}
	}
	,
	_ringBreadth: 0,
	ringBreadth: function (value) {
		if (arguments.length === 1) {
			this._ringBreadth = value;
			return value;
		} else {
			return this._ringBreadth;
		}
	}
	,
	_arcItems: null,
	arcItems: function (value) {
		if (arguments.length === 1) {
			this._arcItems = value;
			return value;
		} else {
			return this._arcItems;
		}
	}
	,
	_owner: null,
	owner: function (value) {
		if (arguments.length === 1) {
			this._owner = value;
			return value;
		} else {
			return this._owner;
		}
	}
	,
	_clearContent: false,
	clearContent: function (value) {
		if (arguments.length === 1) {
			this._clearContent = value;
			return value;
		} else {
			return this._clearContent;
		}
	}
	,
	renderArcs: function () {
		if (this.ringControl() != null && this.ringControl().view().isReady()) {
			var x, y;
			x = this.center().__x - this.ringSeries().location().__x;
			y = this.center().__y - this.ringSeries().location().__y;
			this.ringControl().view().positionRingControl(x, y);
			return this.ringControl().renderControl();
		}
		return false;
	}
	,
	prepareArcs: function () {
		if (this.ringControl() != null) {
			this.ringControl().width(this.controlSize().width());
			this.ringControl().height(this.controlSize().height());
			this.ringSeries().view().addRing(this.ringControl());
			this.ringControl().itemsSource(this.arcItems());
		}
	}
	,
	notifyInsertItem: function (source_, index, newItem) {
		this.ringControl().notifyInsertItem(source_, index, newItem);
	}
	,
	notifyClearItems: function (source_) {
		this.ringControl().notifyClearItems(source_);
	}
	,
	notifyRemoveItem: function (source_, index, oldItem) {
		this.ringControl().notifyRemoveItem(source_, index, oldItem);
	}
	,
	notifySetItem: function (source_, index, oldItem, newItem) {
		this.ringControl().notifySetItem(source_, index, oldItem, newItem);
	}
	,
	$type: new $.ig.Type('Ring', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('RingCollection', 'ObservableCollection$1', {
	init: function () {
		$.ig.ObservableCollection$1.prototype.init.call(this, $.ig.Ring.prototype.$type, 0);
	},
	clearItems: function () {
		var en = this.getEnumerator();
		while (en.moveNext()) {
			var ring = en.current();
			if (ring.clearContent()) {
				var index = 0;
				var count = ring.ringControl()._arcs.count();
				while (index < count) {
					ring.ringControl()._arcs.destroy()(ring.ringControl()._arcs.item(index));
					index++;
				}
				ring.ringControl()._arcs.count(0);
				ring.ringControl(null);
			}
		}
		$.ig.ObservableCollection$1.prototype.clearItems.call(this);
	}
	,
	removeItem: function (index) {
		var ring = this.__inner[index];
		if (ring.clearContent()) {
			ring.ringControl()._arcs.destroy()(ring.ringControl()._arcs.item(index));
			ring.ringControl()._arcs.count(0);
			ring.ringControl(null);
		}
		$.ig.ObservableCollection$1.prototype.removeItem.call(this, index);
	}
	,
	$type: new $.ig.Type('RingCollection', $.ig.ObservableCollection$1.prototype.$type.specialize($.ig.Ring.prototype.$type))
}, true);

$.ig.util.defType('RingControl', 'Control', {
	init: function () {
		var $self = this;
		$.ig.Control.prototype.init.call(this);
		this.defaultStyleKey($.ig.RingControl.prototype.$type);
		this.view(this.createView());
		this.onViewCreated(this.view());
		this.view().onInit();
		this._arcs = (function () {
			var $ret = new $.ig.Pool$1($.ig.Arc.prototype.$type);
			$ret.create($self.view().arcCreate.runOn($self.view()));
			$ret.activate($self.view().arcActivate.runOn($self.view()));
			$ret.disactivate($self.view().arcDisactivate.runOn($self.view()));
			$ret.destroy($self.view().arcDestroy.runOn($self.view()));
			return $ret;
		}());
	},
	series: function () {
		return this.ring().ringSeries();
	}
	,
	_view: null,
	view: function (value) {
		if (arguments.length === 1) {
			this._view = value;
			return value;
		} else {
			return this._view;
		}
	}
	,
	__ring: null,
	ring: function (value) {
		if (arguments.length === 1) {
			this.__ring = value;
			if (this.__ring == null) {
				this.view().onSeriesProvided(null);
			} else {
				this.view().onSeriesProvided(this.__ring.ringSeries());
			}
			return value;
		} else {
			return this.__ring;
		}
	}
	,
	_arcs: null,
	_itemsSource: null,
	itemsSource: function (value) {
		if (arguments.length === 1) {
			this._itemsSource = value;
			return value;
		} else {
			return this._itemsSource;
		}
	}
	,
	_rootCanvas: null,
	rootCanvas: function (value) {
		if (arguments.length === 1) {
			this._rootCanvas = value;
			return value;
		} else {
			return this._rootCanvas;
		}
	}
	,
	createView: function () {
		return new $.ig.RingControlView(this);
	}
	,
	onViewCreated: function (view) {
	}
	,
	propertyChanged: null,
	propertyUpdated: null,
	raisePropertyChanged: function (propertyName, oldValue, newValue) {
		if (this.propertyChanged != null) {
			this.propertyChanged(this, new $.ig.PropertyChangedEventArgs(propertyName));
		}
		if (this.propertyUpdated != null) {
			this.propertyUpdated(this, new $.ig.PropertyUpdatedEventArgs(propertyName, oldValue, newValue));
		}
	}
	,
	onSizeUpdated: function () {
		this.renderControl();
	}
	,
	renderControl: function () {
		if (this.itemsSource() == null) {
			return false;
		}
		var index = 0;
		var areResized = false;
		var en = this.itemsSource().getEnumerator();
		while (en.moveNext()) {
			var arcItem = en.current();
			var arc = this._arcs.item(index);
			arc.arcItem(arcItem);
			if (arc.innerExtent() != this.ring().innerExtend()) {
				arc.innerExtent(this.ring().innerExtend());
			}
			if ((arc.width() != this.ring().controlSize().width() || arc.height() != this.ring().controlSize().height()) && arc.view().ready()) {
				arc.width(this.ring().controlSize().width());
				arc.height(this.ring().controlSize().height());
				this.view().updateArcSize(arc);
				areResized = true;
			}
			if (arc.startAngle() != arcItem.startAngle()) {
				arc.startAngle(arcItem.startAngle());
			}
			if (arc.endAngle() != arcItem.endAngle()) {
				arc.endAngle(arcItem.endAngle());
			}
			if (arc.itemsSource() != arcItem.itemSource()) {
				arc.itemsSource(arcItem.itemSource());
			}
			if (arc.brushes() != arcItem.brushes()) {
				arc.brushes(arcItem.brushes());
			}
			this.view().positionArc(arc, 0, 0);
			index++;
		}
		this._arcs.count(index);
		return areResized;
	}
	,
	provideContainer: function (container) {
		this.view().provideContainer(container);
	}
	,
	onContainerResized: function () {
		this.view().onContainerResized();
	}
	,
	getContainer: function () {
		return this.view().getContainer();
	}
	,
	notifyInsertItem: function (source_, index, newItem) {
		for (var ii = 0; ii < this._arcs.count(); ii++) {
			this._arcs.item(ii).notifyInsertItem(source_, index, newItem);
		}
	}
	,
	notifyClearItems: function (source_) {
		for (var ii = 0; ii < this._arcs.count(); ii++) {
			this._arcs.item(ii).notifyClearItems(source_);
		}
	}
	,
	notifyRemoveItem: function (source_, index, oldItem) {
		for (var ii = 0; ii < this._arcs.count(); ii++) {
			this._arcs.item(ii).notifyRemoveItem(source_, index, oldItem);
		}
	}
	,
	notifySetItem: function (source_, index, oldItem, newItem) {
		for (var ii = 0; ii < this._arcs.count(); ii++) {
			this._arcs.item(ii).notifySetItem(source_, index, oldItem, newItem);
		}
	}
	,
	$type: new $.ig.Type('RingControl', $.ig.Control.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.util.defType('RingSeries', 'RingSeriesBase', {
	init: function () {
		var $self = this;
		$.ig.RingSeriesBase.prototype.init.call(this);
		this.ring((function () {
			var $ret = new $.ig.Ring();
			$ret.ringSeries($self);
			$ret.clearContent(false);
			return $ret;
		}()));
		var arcItem = (function () {
			var $ret = new $.ig.ArcItem();
			$ret.startAngle($self.startAngle());
			$ret.ring($self.ring());
			$ret.valueMemberPath($self.valueMemberPath());
			$ret.othersCategoryType($self.othersCategoryType());
			$ret.othersCategoryThreshold($self.othersCategoryThreshold());
			return $ret;
		}());
		this.ring().arcItems().add(arcItem);
		this.defaultStyleKey($.ig.RingSeries.prototype.$type);
	},
	_ring: null,
	ring: function (value) {
		if (arguments.length === 1) {
			this._ring = value;
			return value;
		} else {
			return this._ring;
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.RingSeriesBase.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		if (propertyName == $.ig.RingSeriesBase.prototype.formatLabelPropertyName) {
			for (var i = 0; i < this.ring().ringControl()._arcs.count(); i++) {
				this.ring().ringControl()._arcs.item(i).formatLabel(newValue);
			}
		}
		if (propertyName == $.ig.RingSeriesBase.prototype.formatLegendLabelPropertyName) {
			for (var i1 = 0; i1 < this.ring().ringControl()._arcs.count(); i1++) {
				this.ring().ringControl()._arcs.item(i1).formatLegendLabel(newValue);
			}
		}
		if (propertyName == $.ig.RingSeriesBase.prototype.legendLabelMemberPathPropertyName) {
			for (var i2 = 0; i2 < this.ring().ringControl()._arcs.count(); i2++) {
				this.ring().ringControl()._arcs.item(i2).legendLabelMemberPath(this.legendLabelMemberPath());
			}
		}
		if (propertyName == $.ig.RingSeriesBase.prototype.brushesPropertyName) {
			this.prepareBrushes();
		}
		if (propertyName == $.ig.RingSeriesBase.prototype.startAnglePropertyName) {
			if (this.ring().arcItems() != null && this.ring().arcItems().count() > 0) {
				this.ring().arcItems().__inner[0].startAngle(this.startAngle());
			}
		}
		if (propertyName == $.ig.RingSeriesBase.prototype.valueMemberPathPropertyName) {
			if (this.ring().arcItems() != null && this.ring().arcItems().count() > 0) {
				this.ring().arcItems().__inner[0].valueMemberPath(this.valueMemberPath());
				this.prepareData();
			}
		}
		if (this.ring() != null && this.view().isReady()) {
			this.ring().prepareArcs();
			if (this.ring().renderArcs()) {
				this.ring().ringSeries().view().onSizeChanged();
			}
		}
	}
	,
	analyzeRings: function () {
		var coll = new $.ig.RingCollection();
		if (this.ring().arcItems().__inner[0].sliceItems().count() > 0) {
			coll.add(this.ring());
		}
		return coll;
	}
	,
	prepareData: function () {
		if (this.ring() != null) {
			this.ring().arcItems().__inner[0].itemSource(this.itemsSource());
			this.ring().arcItems().__inner[0].prepareSliceItems(this.startAngle());
			if (this.chart() != null) {
				this.chart().prepareRingCollection();
				this.chart().renderChart();
			}
		}
	}
	,
	prepareBrushes: function () {
		if (this.ring() != null) {
			this.ring().arcItems().__inner[0].brushes(this.brushes());
		}
	}
	,
	renderSeries: function () {
		if (this.ring() != null) {
			this.width(this.ring().controlSize().width());
			this.height(this.ring().controlSize().height());
			this.view().positionSeries(this.ring().center().__x, this.ring().center().__y);
		}
	}
	,
	renderLegendItems: function () {
		if (this.ring() != null) {
			var en = this.ring().ringControl()._arcs.active().getEnumerator();
			while (en.moveNext()) {
				var arc = en.current();
				arc.renderLegendItems();
			}
		}
	}
	,
	notifyInsertItem: function (index, newItem) {
		$.ig.RingSeriesBase.prototype.notifyInsertItem.call(this, index, newItem);
		if (this.ring() != null) {
			this.ring().notifyInsertItem(this.itemsSource(), index, newItem);
		}
	}
	,
	notifyClearItems: function () {
		$.ig.RingSeriesBase.prototype.notifyClearItems.call(this);
		if (this.ring() != null) {
			this.ring().notifyClearItems(this.itemsSource());
		}
	}
	,
	notifyRemoveItem: function (index, oldItem) {
		$.ig.RingSeriesBase.prototype.notifyRemoveItem.call(this, index, oldItem);
		if (this.ring() != null) {
			this.ring().notifyRemoveItem(this.itemsSource(), index, oldItem);
		}
	}
	,
	notifySetItem: function (index, oldItem, newItem) {
		$.ig.RingSeriesBase.prototype.notifySetItem.call(this, index, oldItem, newItem);
		if (this.ring() != null) {
			this.ring().notifySetItem(this.itemsSource(), index, oldItem, newItem);
		}
	}
	,
	$type: new $.ig.Type('RingSeries', $.ig.RingSeriesBase.prototype.$type)
}, true);

$.ig.util.defType('RingSeriesCollection', 'ObservableCollection$1', {
	init: function () {
		$.ig.ObservableCollection$1.prototype.init.call(this, $.ig.RingSeriesBase.prototype.$type, 0);
	},
	$type: new $.ig.Type('RingSeriesCollection', $.ig.ObservableCollection$1.prototype.$type.specialize($.ig.RingSeriesBase.prototype.$type))
}, true);

$.ig.util.defType('SliceItem', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	_startAngle: 0,
	startAngle: function (value) {
		if (arguments.length === 1) {
			this._startAngle = value;
			return value;
		} else {
			return this._startAngle;
		}
	}
	,
	_endAngle: 0,
	endAngle: function (value) {
		if (arguments.length === 1) {
			this._endAngle = value;
			return value;
		} else {
			return this._endAngle;
		}
	}
	,
	_arcItem: null,
	arcItem: function (value) {
		if (arguments.length === 1) {
			this._arcItem = value;
			return value;
		} else {
			return this._arcItem;
		}
	}
	,
	_index: 0,
	index: function (value) {
		if (arguments.length === 1) {
			this._index = value;
			return value;
		} else {
			return this._index;
		}
	}
	,
	_slice: null,
	slice: function (value) {
		if (arguments.length === 1) {
			this._slice = value;
			return value;
		} else {
			return this._slice;
		}
	}
	,
	_data: null,
	data: function (value) {
		if (arguments.length === 1) {
			this._data = value;
			return value;
		} else {
			return this._data;
		}
	}
	,
	_isOther: false,
	isOther: function (value) {
		if (arguments.length === 1) {
			this._isOther = value;
			return value;
		} else {
			return this._isOther;
		}
	}
	,
	$type: new $.ig.Type('SliceItem', $.ig.Object.prototype.$type)
}, true);

$.ig.util.defType('SliceCollection', 'ObservableCollection$1', {
	init: function () {
		$.ig.ObservableCollection$1.prototype.init.call(this, $.ig.SliceItem.prototype.$type, 0);
	},
	_isAttached: false,
	isAttached: function (value) {
		if (arguments.length === 1) {
			this._isAttached = value;
			return value;
		} else {
			return this._isAttached;
		}
	}
	,
	_useForSelection: false,
	useForSelection: function (value) {
		if (arguments.length === 1) {
			this._useForSelection = value;
			return value;
		} else {
			return this._useForSelection;
		}
	}
	,
	insertItem: function (index, item) {
		if (this.contains(item)) {
			return;
		}
		var sliceItemIndex = this.findSliceItemIndexBySlice(item.slice());
		if (sliceItemIndex != -1) {
			$.ig.ObservableCollection$1.prototype.setItem.call(this, sliceItemIndex, item);
		} else {
			$.ig.ObservableCollection$1.prototype.insertItem.call(this, index, item);
			if (this.isAttached()) {
				if (this.useForSelection()) {
					item.slice().isSelected(true);
				} else {
					item.slice().isExploded(true);
				}
			}
		}
	}
	,
	setItem: function (index, item) {
		if (this.isAttached()) {
			if (this.useForSelection()) {
				this.__inner[index].slice().isSelected(false);
			} else {
				this.__inner[index].slice().isExploded(false);
			}
		}
		$.ig.ObservableCollection$1.prototype.setItem.call(this, index, item);
	}
	,
	removeItem: function (index) {
		var sliceItem = this.__inner[index];
		$.ig.ObservableCollection$1.prototype.removeItem.call(this, index);
		if (this.isAttached()) {
			if (this.useForSelection()) {
				sliceItem.slice().isSelected(false);
			} else {
				sliceItem.slice().isExploded(false);
			}
		}
	}
	,
	remove: function (item) {
		var sliceItemIndex = this.findSliceItemIndexBySlice(item.slice());
		if (sliceItemIndex != -1) {
			this.removeItem(sliceItemIndex);
			return true;
		} else {
			return false;
		}
	}
	,
	clearItems: function () {
		while (this.count() > 0) {
			this.removeItem(0);
		}
	}
	,
	findSliceItemIndexBySlice: function (slice) {
		for (var i = 0; i < this.count(); i++) {
			if (this.__inner[i].slice() == slice) {
				return i;
			}
		}
		return -1;
	}
	,
	$type: new $.ig.Type('SliceCollection', $.ig.ObservableCollection$1.prototype.$type.specialize($.ig.SliceItem.prototype.$type))
}, true);

$.ig.util.defType('XamDoughnutChart', 'Control', {
	__series: null,
	__rings: null,
	__selectedSlices: null,
	__explodedSlices: null,
	__propertyUpdatedOverride: null,
	init: function () {
		var $self = this;
		this.__series = new $.ig.RingSeriesCollection();
		this.__rings = new $.ig.RingCollection();
		this.__selectedSlices = (function () {
			var $ret = new $.ig.SliceCollection();
			$ret.isAttached(true);
			$ret.useForSelection(true);
			return $ret;
		}());
		this.__explodedSlices = (function () {
			var $ret = new $.ig.SliceCollection();
			$ret.isAttached(true);
			$ret.useForSelection(false);
			return $ret;
		}());
		$.ig.Control.prototype.init.call(this);
		var view = this.createView();
		this.onViewCreated(view);
		view.onInit();
		this.defaultStyleKey($.ig.XamDoughnutChart.prototype.$type);
		this.__propertyUpdatedOverride = function (o, e) {
			$self.propertyUpdatedOverride(o, e.propertyName(), e.oldValue(), e.newValue());
		};
		this.propertyUpdated = $.ig.Delegate.prototype.combine(this.propertyUpdated, this.__propertyUpdatedOverride);
		var $t = this.series();
		$t.collectionChanged = $.ig.Delegate.prototype.combine($t.collectionChanged, this.series_CollectionChanged.runOn(this));
		var nullValue = $.ig.util.toNullable($.ig.Boolean.prototype.$type, null);
		this.isSurfaceInteractionDisabled(nullValue);
	},
	rings: function () {
		return this.__rings;
	}
	,
	allowSliceSelection: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamDoughnutChart.prototype.allowSliceSelectionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamDoughnutChart.prototype.allowSliceSelectionProperty);
		}
	}
	,
	isSurfaceInteractionDisabled: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamDoughnutChart.prototype.isSurfaceInteractionDisabledProperty, $.ig.util.unwrapNullable(value));
			return value;
		} else {
			return $.ig.util.wrapNullable($.ig.Boolean.prototype.$type, this.getValue($.ig.XamDoughnutChart.prototype.isSurfaceInteractionDisabledProperty));
		}
	}
	,
	allowSliceExplosion: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamDoughnutChart.prototype.allowSliceExplosionProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamDoughnutChart.prototype.allowSliceExplosionProperty);
		}
	}
	,
	series: function () {
		return this.__series;
	}
	,
	series_CollectionChanged: function (sender, e) {
		var removed = new $.ig.List$1($.ig.RingSeriesBase.prototype.$type, 0);
		if (e.oldItems() != null) {
			var en = e.oldItems().getEnumerator();
			while (en.moveNext()) {
				var item = en.current();
				if (e.newItems() == null || !e.newItems().contains(item)) {
					(item).clearView();
					this.view().removeSeries(item);
				}
			}
		}
		this.prepareRingCollection();
		this.renderChart();
	}
	,
	innerExtent: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamDoughnutChart.prototype.innerExtentProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamDoughnutChart.prototype.innerExtentProperty);
		}
	}
	,
	selectedSlices: function (value) {
		if (arguments.length === 1) {
			this.__selectedSlices.clear();
			this.__selectedSlices.isAttached(false);
			this.__selectedSlices = value;
			this.__selectedSlices.isAttached(true);
			this.__selectedSlices.useForSelection(true);
			for (var i = 0; i < this.__selectedSlices.count(); i++) {
				this.__selectedSlices.__inner[i].slice().isSelected(true);
			}
			return value;
		} else {
			return this.__selectedSlices;
		}
	}
	,
	explodedSlices: function (value) {
		if (arguments.length === 1) {
			this.__explodedSlices.clear();
			this.__explodedSlices.isAttached(false);
			this.__explodedSlices = value;
			this.__explodedSlices.isAttached(true);
			this.__explodedSlices.useForSelection(false);
			for (var i = 0; i < this.__explodedSlices.count(); i++) {
				this.__explodedSlices.__inner[i].slice().isExploded(true);
			}
			return value;
		} else {
			return this.__explodedSlices;
		}
	}
	,
	selectedStyle: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamDoughnutChart.prototype.selectedStyleProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamDoughnutChart.prototype.selectedStyleProperty);
		}
	}
	,
	centerData: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamDoughnutChart.prototype.centerDataProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamDoughnutChart.prototype.centerDataProperty);
		}
	}
	,
	centerDataTemplate: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.XamDoughnutChart.prototype.centerDataTemplateProperty, value);
			return value;
		} else {
			return this.getValue($.ig.XamDoughnutChart.prototype.centerDataTemplateProperty);
		}
	}
	,
	_view: null,
	view: function (value) {
		if (arguments.length === 1) {
			this._view = value;
			return value;
		} else {
			return this._view;
		}
	}
	,
	destroy: function () {
		this.view().provideContainer(null);
		var en = this.series().getEnumerator();
		while (en.moveNext()) {
			var series = en.current();
			series.clearView();
		}
	}
	,
	selectSlice: function (slice, shouldSelect) {
		if (shouldSelect) {
			this.selectedSlices().add(slice);
		} else {
			this.selectedSlices().remove(slice);
		}
	}
	,
	explodeSlice: function (slice, explode) {
		if (explode) {
			this.explodedSlices().add(slice);
		} else {
			this.explodedSlices().remove(slice);
		}
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		switch (propertyName) {
			case $.ig.XamDoughnutChart.prototype.selectedStylePropertyName:
				var en = this.series().getEnumerator();
				while (en.moveNext()) {
					var series = en.current();
					series.selectedStyleResolved(newValue);
				}
				break;
			case $.ig.XamDoughnutChart.prototype.allowSliceExplosionPropertyName:
				for (var i = 0; i < this.__rings.count(); i++) {
					var en1 = this.__rings.__inner[i].ringControl()._arcs.active().getEnumerator();
					while (en1.moveNext()) {
						var arc = en1.current();
						arc.allowSliceExplosion(this.allowSliceExplosion());
					}
				}
				break;
			case $.ig.XamDoughnutChart.prototype.allowSliceSelectionPropertyName:
				for (var i1 = 0; i1 < this.__rings.count(); i1++) {
					var en2 = this.__rings.__inner[i1].ringControl()._arcs.active().getEnumerator();
					while (en2.moveNext()) {
						var arc1 = en2.current();
						arc1.allowSliceSelection(this.allowSliceSelection());
					}
				}
				break;
		}
	}
	,
	createView: function () {
		return new $.ig.XamDoughnutChartView(this);
	}
	,
	onViewCreated: function (view) {
		this.view(view);
	}
	,
	renderChart: function () {
		this.prepareRings();
		this.renderSeries();
		this.renderRings();
		this.prepareCenterPresenter();
		this.view().updateView();
	}
	,
	renderSeries: function () {
		var en = this.series().getEnumerator();
		while (en.moveNext()) {
			var series = en.current();
			series.renderSeries();
		}
	}
	,
	prepareCenterPresenter: function () {
		var viewport = this.view().viewport();
		if (viewport.isEmpty() || viewport.width() == 0 || viewport.height() == 0) {
			return;
		}
		var centerPoint = { __x: viewport.width() / 2, __y: viewport.height() / 2, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var breadth = Math.min(viewport.width() / 2, viewport.height() / 2);
		var innerSeriesRadiusFactor = 0.9;
		if (this.series().count() > 0) {
			innerSeriesRadiusFactor = this.series().__inner[0].radiusFactor();
			if ($.ig.util.isNaN(innerSeriesRadiusFactor) || Number.isInfinity(innerSeriesRadiusFactor) || innerSeriesRadiusFactor < 0) {
				innerSeriesRadiusFactor = 0;
			} else if (innerSeriesRadiusFactor > 1) {
				innerSeriesRadiusFactor = 1;
			}
		}
		var initialOffset = this.innerExtent() * breadth / 100 * innerSeriesRadiusFactor;
		this.view().setCenterPresenterSize(initialOffset * 2, initialOffset * 2);
		this.view().positonCenterPresenter(centerPoint.__x - initialOffset, centerPoint.__y - initialOffset);
	}
	,
	renderRings: function () {
		var sizeChanged = false;
		var en = this.__rings.getEnumerator();
		while (en.moveNext()) {
			var ring = en.current();
			if (ring.renderArcs()) {
				sizeChanged = true;
			}
		}
		if (sizeChanged) {
			var en1 = this.__rings.getEnumerator();
			while (en1.moveNext()) {
				var ring1 = en1.current();
				ring1.ringSeries().view().onSizeChanged();
			}
		}
	}
	,
	prepareRings: function () {
		var viewport = this.view().viewport();
		if (viewport.isEmpty() || viewport.width() == 0 || viewport.height() == 0) {
			return;
		}
		var centerPoint = { __x: viewport.width() / 2, __y: viewport.height() / 2, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		var widthBreadth = Math.min(viewport.width() / 2, viewport.height() / 2);
		var heightBreadth = Math.max(viewport.width() / 2, viewport.height() / 2);
		if (viewport.width() > viewport.height()) {
			widthBreadth = widthBreadth + heightBreadth;
			heightBreadth = widthBreadth - heightBreadth;
			widthBreadth = widthBreadth - heightBreadth;
		}
		var seriesWidthBreadth = ((100 - this.innerExtent()) / 100 * widthBreadth) / this.__rings.count();
		var seriesHeightBreadth = ((100 - this.innerExtent()) / 100 * heightBreadth) / this.__rings.count();
		var initialWidthOffset = this.innerExtent() * widthBreadth / 100;
		var initialHeightOffset = this.innerExtent() * heightBreadth / 100;
		var index = 1;
		var en = this.__rings.getEnumerator();
		while (en.moveNext()) {
			var ring = en.current();
			var halfWidth = (initialWidthOffset + (seriesWidthBreadth * index));
			var halfHeight = (initialHeightOffset + (seriesHeightBreadth * index));
			ring.ringBreadth(seriesWidthBreadth);
			ring.controlSize(new $.ig.Size(1, halfWidth * 2, halfHeight * 2));
			ring.center({ __x: centerPoint.__x - halfWidth, __y: centerPoint.__y - halfHeight, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			ring.innerExtend(100 - (seriesWidthBreadth / halfWidth * 100));
			ring.prepareArcs();
			index++;
		}
	}
	,
	prepareRingCollection: function () {
		this.explodedSlices().clear();
		this.selectedSlices().clear();
		this.__rings.clear();
		this.view().ensureCenterPresenter();
		this.view().clearSeries();
		var index = 0;
		for (var i = 0; i < this.series().count(); i++) {
			var series = this.series().__inner[i];
			series.chart(this);
			this.view().addSeries(series);
			var seriesRings = series.analyzeRings();
			var en = seriesRings.getEnumerator();
			while (en.moveNext()) {
				var ring = en.current();
				ring.owner(this);
				ring.index(index);
				this.__rings.add(ring);
				index++;
			}
		}
	}
	,
	onSizeUpdated: function () {
		this.renderChart();
	}
	,
	sliceClick: null,
	onSliceClick: function (sender, e) {
		if (this.isSurfaceInteractionDisabled().hasValue() && this.isSurfaceInteractionDisabled().value()) {
			var arc = sender;
			var ringSeries = arc.ring().ringSeries();
			if (ringSeries.isSurfaceInteractionDisabled().hasValue()) {
				if (ringSeries.isSurfaceInteractionDisabled().value()) {
					return;
				}
			} else {
				return;
			}
		}
		if (this.sliceClick != null) {
			this.sliceClick(sender, e);
		}
	}
	,
	propertyChanged: null,
	propertyUpdated: null,
	raisePropertyChanged: function (name, oldValue, newValue) {
		if (this.propertyChanged != null) {
			this.propertyChanged(this, new $.ig.PropertyChangedEventArgs(name));
		}
		if (this.propertyUpdated != null) {
			this.propertyUpdated(this, new $.ig.PropertyUpdatedEventArgs(name, oldValue, newValue));
		}
	}
	,
	provideContainer: function (container) {
		this.view().provideContainer(container);
	}
	,
	notifyResized: function () {
		this.view().sizeUpdated();
	}
	,
	getContainerID: function () {
		return this.view().getContainerID();
	}
	,
	getCenterCoordinates: function () {
		return this.view().centerCoordinates();
	}
	,
	getHoleRadius: function () {
		return this.view().holeRadius();
	}
	,
	flush: function () {
		var arcs;
		var rings;
		for (var i = 0; i < this.series().count(); i++) {
			rings = this.series().__inner[i].analyzeRings();
			for (var j = 0; j < rings.count(); j++) {
				arcs = rings.__inner[j].ringControl().view().arcs();
				for (var k = 0; k < arcs.count(); k++) {
					arcs.__inner[k].flush();
				}
			}
		}
	}
	,
	holeDimensionsChanged: null,
	onHoleDimensionsChanged: function () {
		if (this.holeDimensionsChanged != null) {
			this.holeDimensionsChanged(this, new $.ig.HoleDimensionsChangedEventArgs(this.getCenterCoordinates(), this.getHoleRadius()));
		}
	}
	,
	exportVisualData: function () {
		var visualData = new $.ig.DoughnutChartVisualData();
		visualData.series(new $.ig.RingSeriesVisualDataList());
		visualData.viewport(this.view().viewport());
		this.view().exportViewShapes(visualData);
		return visualData;
	}
	,
	getSeriesWithItemsSource: function (source) {
		return ($.ig.Enumerable.prototype.where$1($.ig.RingSeriesBase.prototype.$type, (this.series()), function (series) { return $.ig.PieChartBase.prototype.sourcesEqual1(source, series.itemsSource()); }));
	}
	,
	notifyInsertItem: function (source_, index, newItem) {
		var en = this.getSeriesWithItemsSource(source_).getEnumerator();
		while (en.moveNext()) {
			var series = en.current();
			series.notifyInsertItem(index, newItem);
		}
	}
	,
	notifySetItem: function (source_, index, oldItem, newItem) {
		var en = this.getSeriesWithItemsSource(source_).getEnumerator();
		while (en.moveNext()) {
			var series = en.current();
			series.notifySetItem(index, oldItem, newItem);
		}
	}
	,
	notifyClearItems: function (source_) {
		var en = this.getSeriesWithItemsSource(source_).getEnumerator();
		while (en.moveNext()) {
			var series = en.current();
			series.notifyClearItems();
		}
	}
	,
	notifyRemoveItem: function (source_, index, oldItem) {
		var en = this.getSeriesWithItemsSource(source_).getEnumerator();
		while (en.moveNext()) {
			var series = en.current();
			series.notifyRemoveItem(index, oldItem);
		}
	}
	,
	$type: new $.ig.Type('XamDoughnutChart', $.ig.Control.prototype.$type, [$.ig.INotifyPropertyChanged.prototype.$type])
}, true);

$.ig.Legend.prototype.orientationPropertyName = "Orientation";

$.ig.Arc.prototype.endAnglePropertyName = "EndAngle";
$.ig.Arc.prototype.endAngleProperty = $.ig.DependencyProperty.prototype.register($.ig.Arc.prototype.endAnglePropertyName, Number, $.ig.Arc.prototype.$type, new $.ig.PropertyMetadata(2, 360, function (o, e) {
	($.ig.util.cast($.ig.Arc.prototype.$type, o)).raisePropertyChanged($.ig.Arc.prototype.endAnglePropertyName, e.oldValue(), e.newValue());
}));

$.ig.RingSeriesBase.prototype.itemsSourcePropertyName = "ItemsSource";
$.ig.RingSeriesBase.prototype.valueMemberPathPropertyName = "ValueMemberPath";
$.ig.RingSeriesBase.prototype.labelMemberPathPropertyName = "LabelMemberPath";
$.ig.RingSeriesBase.prototype.legendLabelMemberPathPropertyName = "LegendLabelMemberPath";
$.ig.RingSeriesBase.prototype.labelsPositionPropertyName = "LabelsPosition";
$.ig.RingSeriesBase.prototype.leaderLineVisibilityPropertyName = "LeaderLineVisibility";
$.ig.RingSeriesBase.prototype.leaderLineStylePropertyName = "LeaderLineStyle";
$.ig.RingSeriesBase.prototype.leaderLineTypePropertyName = "LeaderLineType";
$.ig.RingSeriesBase.prototype.leaderLineMarginPropertyName = "LeaderLineMargin";
$.ig.RingSeriesBase.prototype.toolTipPropertyName = "ToolTip";
$.ig.RingSeriesBase.prototype.othersCategoryThresholdPropertyName = "OthersCategoryThreshold";
$.ig.RingSeriesBase.prototype.othersCategoryTypePropertyName = "OthersCategoryType";
$.ig.RingSeriesBase.prototype.othersCategoryTextPropertyName = "OthersCategoryText";
$.ig.RingSeriesBase.prototype.legendPropertyName = "Legend";
$.ig.RingSeriesBase.prototype.formatLabelPropertyName = "FormatLabel";
$.ig.RingSeriesBase.prototype.formatLegendLabelPropertyName = "FormatLegendLabel";
$.ig.RingSeriesBase.prototype.labelExtentPropertyName = "LabelExtent";
$.ig.RingSeriesBase.prototype.startAnglePropertyName = "StartAngle";
$.ig.RingSeriesBase.prototype.othersCategoryStylePropertyName = "OthersCategoryStyle";
$.ig.RingSeriesBase.prototype.selectedStylePropertyName = "SelectedStyle";
$.ig.RingSeriesBase.prototype.selectedStyleResolvedPropertyName = "SelectedStyleResolved";
$.ig.RingSeriesBase.prototype.toolTipStylePropertyName = "ToolTipStyle";
$.ig.RingSeriesBase.prototype.brushesPropertyName = "Brushes";
$.ig.RingSeriesBase.prototype.outlinesPropertyName = "Outlines";
$.ig.RingSeriesBase.prototype.legendItemTemplatePropertyName = "LegendItemTemplate";
$.ig.RingSeriesBase.prototype.legendItemBadgeTemplatePropertyName = "LegendItemBadgeTemplate";
$.ig.RingSeriesBase.prototype._labelTemplatePropertyName = "LabelTemplate";
$.ig.RingSeriesBase.prototype.isSurfaceInteractionDisabledPropertyName = "IsSurfaceInteractionDisabled";
$.ig.RingSeriesBase.prototype.radiusFactorPropertyName = "RadiusFactor";
$.ig.RingSeriesBase.prototype.fastItemsSourcePropertyName = "FastItemsSource";
$.ig.RingSeriesBase.prototype._rootCanvasName = "RootCanvas";
$.ig.RingSeriesBase.prototype.itemsSourceProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.itemsSourcePropertyName, $.ig.IEnumerable.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.itemsSourcePropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.valueMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.valueMemberPathPropertyName, String, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.valueMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.labelMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.labelMemberPathPropertyName, String, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(1, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.labelMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.legendLabelMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.legendLabelMemberPathPropertyName, String, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(1, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.legendLabelMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.labelsPositionProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.labelsPositionPropertyName, $.ig.LabelsPosition.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.LabelsPosition.prototype.getBox($.ig.LabelsPosition.prototype.center), function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.labelsPositionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.leaderLineVisibilityProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.leaderLineVisibilityPropertyName, $.ig.Visibility.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.Visibility.prototype.getBox($.ig.Visibility.prototype.visible), function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.leaderLineVisibilityPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.leaderLineStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.leaderLineStylePropertyName, $.ig.Style.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.leaderLineStylePropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.leaderLineTypeProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.leaderLineTypePropertyName, $.ig.LeaderLineType.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.LeaderLineType.prototype.getBox($.ig.LeaderLineType.prototype.straight), function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.leaderLineTypePropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.leaderLineMarginProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.leaderLineMarginPropertyName, Number, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, 6, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.leaderLineMarginPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.toolTipProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.toolTipPropertyName, $.ig.Object.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, sender)).raisePropertyChanged($.ig.RingSeriesBase.prototype.toolTipPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.othersCategoryThresholdProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.othersCategoryThresholdPropertyName, Number, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, 3, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.othersCategoryThresholdPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.othersCategoryTypeProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.othersCategoryTypePropertyName, $.ig.OthersCategoryType.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, $.ig.OthersCategoryType.prototype.getBox($.ig.OthersCategoryType.prototype.percent), function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.othersCategoryTypePropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.othersCategoryTextProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.othersCategoryTextPropertyName, String, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, "Others", function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.othersCategoryTextPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.legendProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.legendPropertyName, $.ig.LegendBase.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.legendPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.formatLabelProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.formatLabelPropertyName, $.ig.Func$2.prototype.$type.specialize($.ig.Object.prototype.$type, String), $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, sender)).raisePropertyChanged($.ig.RingSeriesBase.prototype.formatLabelPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.formatLegendLabelProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.formatLegendLabelPropertyName, $.ig.Func$2.prototype.$type.specialize($.ig.Object.prototype.$type, String), $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, sender)).raisePropertyChanged($.ig.RingSeriesBase.prototype.formatLegendLabelPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.labelExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.labelExtentPropertyName, Number, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, 10, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.labelExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.startAngleProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.startAnglePropertyName, Number, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, 0, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.startAnglePropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.othersCategoryStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.othersCategoryStylePropertyName, $.ig.Style.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.othersCategoryStylePropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.selectedStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.selectedStylePropertyName, $.ig.Style.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.selectedStylePropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.toolTipStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.toolTipStylePropertyName, $.ig.Style.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.toolTipStylePropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.brushesProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.brushesPropertyName, $.ig.BrushCollection.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, sender)).raisePropertyChanged($.ig.RingSeriesBase.prototype.brushesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.outlinesProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.outlinesPropertyName, $.ig.BrushCollection.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, sender)).raisePropertyChanged($.ig.RingSeriesBase.prototype.outlinesPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.legendItemTemplateProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.legendItemTemplatePropertyName, $.ig.DataTemplate.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, sender)).raisePropertyChanged($.ig.RingSeriesBase.prototype.legendItemTemplatePropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.legendItemBadgeTemplateProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.legendItemBadgeTemplatePropertyName, $.ig.DataTemplate.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, sender)).raisePropertyChanged($.ig.RingSeriesBase.prototype.legendItemBadgeTemplatePropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.labelTemplateProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype._labelTemplatePropertyName, $.ig.DataTemplate.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, sender)).raisePropertyChanged($.ig.RingSeriesBase.prototype._labelTemplatePropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.isSurfaceInteractionDisabledProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.isSurfaceInteractionDisabledPropertyName, $.ig.Nullable$1.prototype.$type.specialize($.ig.Boolean.prototype.$type), $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.RingSeriesBase.prototype.isSurfaceInteractionDisabledPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.radiusFactorProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.radiusFactorPropertyName, Number, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(2, 0.9, function (o, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, o)).raisePropertyChanged($.ig.RingSeriesBase.prototype.radiusFactorPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RingSeriesBase.prototype.fastItemsSourceProperty = $.ig.DependencyProperty.prototype.register($.ig.RingSeriesBase.prototype.fastItemsSourcePropertyName, $.ig.FastItemsSource.prototype.$type, $.ig.RingSeriesBase.prototype.$type, new $.ig.PropertyMetadata(1, function (sender, e) {
	($.ig.util.cast($.ig.RingSeriesBase.prototype.$type, sender)).raisePropertyChanged($.ig.RingSeriesBase.prototype.fastItemsSourcePropertyName, e.oldValue(), e.newValue());
}));

$.ig.HierarchicalRingSeries.prototype.childrenMemberPathPropertyName = "ChildrenMemberPath";
$.ig.HierarchicalRingSeries.prototype.childrenMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.HierarchicalRingSeries.prototype.childrenMemberPathPropertyName, String, $.ig.HierarchicalRingSeries.prototype.$type, new $.ig.PropertyMetadata(1, function (o, e) {
	($.ig.util.cast($.ig.HierarchicalRingSeries.prototype.$type, o)).raisePropertyChanged($.ig.HierarchicalRingSeries.prototype.childrenMemberPathPropertyName, e.oldValue(), e.newValue());
}));

$.ig.RingControl.prototype._rootCanvasName = "RootCanvas";

$.ig.XamDoughnutChart.prototype.allowSliceSelectionPropertyName = "AllowSliceSelection";
$.ig.XamDoughnutChart.prototype.isSurfaceInteractionDisabledPropertyName = "IsSurfaceInteractionDisabled";
$.ig.XamDoughnutChart.prototype.allowSliceExplosionPropertyName = "AllowSliceExplosion";
$.ig.XamDoughnutChart.prototype._contentPresenterName = "ContentPresenter";
$.ig.XamDoughnutChart.prototype.innerExtentPropertyName = "InnerExtent";
$.ig.XamDoughnutChart.prototype.selectedStylePropertyName = "SelectedStyle";
$.ig.XamDoughnutChart.prototype.centerDataPropertyName = "CenterData";
$.ig.XamDoughnutChart.prototype.centerDataTemplatePropertyName = "CenterDataTemplate";
$.ig.XamDoughnutChart.prototype.allowSliceSelectionProperty = $.ig.DependencyProperty.prototype.register($.ig.XamDoughnutChart.prototype.allowSliceSelectionPropertyName, $.ig.Boolean.prototype.$type, $.ig.XamDoughnutChart.prototype.$type, new $.ig.PropertyMetadata(2, true, function (o, e) {
	($.ig.util.cast($.ig.XamDoughnutChart.prototype.$type, o)).raisePropertyChanged($.ig.XamDoughnutChart.prototype.allowSliceSelectionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamDoughnutChart.prototype.isSurfaceInteractionDisabledProperty = $.ig.DependencyProperty.prototype.register($.ig.XamDoughnutChart.prototype.isSurfaceInteractionDisabledPropertyName, $.ig.Nullable$1.prototype.$type.specialize($.ig.Boolean.prototype.$type), $.ig.XamDoughnutChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	($.ig.util.cast($.ig.XamDoughnutChart.prototype.$type, o)).raisePropertyChanged($.ig.XamDoughnutChart.prototype.isSurfaceInteractionDisabledPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamDoughnutChart.prototype.allowSliceExplosionProperty = $.ig.DependencyProperty.prototype.register($.ig.XamDoughnutChart.prototype.allowSliceExplosionPropertyName, $.ig.Boolean.prototype.$type, $.ig.XamDoughnutChart.prototype.$type, new $.ig.PropertyMetadata(2, true, function (o, e) {
	($.ig.util.cast($.ig.XamDoughnutChart.prototype.$type, o)).raisePropertyChanged($.ig.XamDoughnutChart.prototype.allowSliceExplosionPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamDoughnutChart.prototype.innerExtentProperty = $.ig.DependencyProperty.prototype.register($.ig.XamDoughnutChart.prototype.innerExtentPropertyName, Number, $.ig.XamDoughnutChart.prototype.$type, new $.ig.PropertyMetadata(2, 40, function (o, e) {
	var newValue = e.newValue();
	if (newValue < 0 || newValue > 100) {
		if (e.oldValue() != null) {
			(o).innerExtent(e.oldValue());
		}
		return;
	}
	(o).renderChart();
	(o).raisePropertyChanged($.ig.XamDoughnutChart.prototype.innerExtentPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamDoughnutChart.prototype.selectedStyleProperty = $.ig.DependencyProperty.prototype.register($.ig.XamDoughnutChart.prototype.selectedStylePropertyName, $.ig.Style.prototype.$type, $.ig.XamDoughnutChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	($.ig.util.cast($.ig.XamDoughnutChart.prototype.$type, o)).raisePropertyChanged($.ig.XamDoughnutChart.prototype.selectedStylePropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamDoughnutChart.prototype.centerDataProperty = $.ig.DependencyProperty.prototype.register($.ig.XamDoughnutChart.prototype.centerDataPropertyName, $.ig.Object.prototype.$type, $.ig.XamDoughnutChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	($.ig.util.cast($.ig.XamDoughnutChart.prototype.$type, o)).raisePropertyChanged($.ig.XamDoughnutChart.prototype.centerDataPropertyName, e.oldValue(), e.newValue());
}));
$.ig.XamDoughnutChart.prototype.centerDataTemplateProperty = $.ig.DependencyProperty.prototype.register($.ig.XamDoughnutChart.prototype.centerDataTemplatePropertyName, $.ig.DataTemplate.prototype.$type, $.ig.XamDoughnutChart.prototype.$type, new $.ig.PropertyMetadata(2, null, function (o, e) {
	($.ig.util.cast($.ig.XamDoughnutChart.prototype.$type, o)).raisePropertyChanged($.ig.XamDoughnutChart.prototype.centerDataTemplatePropertyName, e.oldValue(), e.newValue());
}));

} (jQuery));


