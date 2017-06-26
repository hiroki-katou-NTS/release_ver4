/*!@license
* Infragistics.Web.ClientUI infragistics.chart_rangecategorychart.js 16.1.20161.2145
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
"RangeColumnSeries:y6", 
"RangeColumnSeriesView:y7", 
"Legend:ac1", 
"LegendView:ac2", 
"AbstractEnumerable:adb", 
"AbstractEnumerator:adc", 
"GenericEnumerable$1:add", 
"GenericEnumerator$1:ade"]);


$.ig.util.defType('RangeCategoryBucketCalculator', 'CategoryBucketCalculator', {
	_rangeView: null,
	rangeView: function (value) {
		if (arguments.length === 1) {
			this._rangeView = value;
			return value;
		} else {
			return this._rangeView;
		}
	}
	,
	init: function (view) {
		this.__lowValues = null;
		this.__highValues = null;
		$.ig.CategoryBucketCalculator.prototype.init.call(this, view);
		this.rangeView(view);
	},
	getBucket: function (bucket) {
		var allowHighLowCrossing = false;
		var lowColumn = this.__lowValues;
		var highColumn = this.__highValues;
		var lowCount = this.__lowValuesCount;
		var highCount = this.__highValuesCount;
		var count = Math.min(lowCount, highCount);
		var i0 = bucket * this._bucketSize;
		var i1 = Math.min(i0 + this._bucketSize - 1, count - 1);
		var min = NaN;
		var max = NaN;
		var first = true;
		var lowVal = 0;
		var highVal = 0;
		var low;
		var high;
		for (var i = i0; i <= i1; ++i) {
			lowVal = lowColumn[i];
			highVal = highColumn[i];
			if (lowVal < highVal) {
				low = lowVal;
				high = highVal;
			} else {
				high = lowVal;
				low = highVal;
			}
			if (!first) {
				if (!$.ig.util.isNaN(low)) {
					min = min < low ? min : low;
					max = max > low ? max : low;
				}
				if (!$.ig.util.isNaN(high)) {
					min = min < high ? min : high;
					max = max > high ? max : high;
				}
			} else {
				if (!$.ig.util.isNaN(low)) {
					if ($.ig.util.isNaN(min)) {
						min = low;
					} else {
						min = Math.min(min, low);
					}
					if (!$.ig.util.isNaN(max)) {
						max = Math.max(max, low);
					}
				}
				if (!$.ig.util.isNaN(high)) {
					if ($.ig.util.isNaN(max)) {
						max = high;
					} else {
						max = Math.max(max, high);
					}
					if (!$.ig.util.isNaN(min)) {
						min = Math.min(min, high);
					}
				}
				if (!$.ig.util.isNaN(min) && !$.ig.util.isNaN(max)) {
					first = false;
				}
			}
		}
		if (highVal < lowVal && allowHighLowCrossing) {
			var temp = min;
			min = max;
			max = temp;
		}
		if (!first) {
			var ret = new Array(3);
			ret[0] = (0.5 * (i0 + i1));
			ret[1] = min;
			ret[2] = max;
			return ret;
		}
		var np = new Array(3);
		np[0] = NaN;
		np[1] = NaN;
		np[2] = NaN;
		return np;
	}
	,
	__lowValuesCount: 0,
	__highValuesCount: 0,
	__lowValues: null,
	__highValues: null,
	cacheValues: function () {
		this.__lowValuesCount = this.rangeView().rangeModel().lowColumn().count();
		this.__highValuesCount = this.rangeView().rangeModel().highColumn().count();
		this.__lowValues = this.rangeView().rangeModel().lowColumn().asArray();
		this.__highValues = this.rangeView().rangeModel().highColumn().asArray();
	}
	,
	unCacheValues: function () {
		this.__lowValues = null;
		this.__highValues = null;
	}
	,
	$type: new $.ig.Type('RangeCategoryBucketCalculator', $.ig.CategoryBucketCalculator.prototype.$type)
}, true);

$.ig.util.defType('IHasHighLowValueCategory', 'Object', {
	$type: new $.ig.Type('IHasHighLowValueCategory', null)
}, true);

$.ig.util.defType('RangeCategoryFramePreparer', 'CategoryFramePreparerBase', {
	init: function (initNumber, host) {
		if (initNumber > 0) {
			switch (initNumber) {
				case 1:
					this.init1.apply(this, arguments);
					break;
			}
			return;
		}
		$.ig.RangeCategoryFramePreparer.prototype.init1.call(this, 1, host, $.ig.util.cast($.ig.ISupportsMarkers.prototype.$type, host), $.ig.util.cast($.ig.IProvidesViewport.prototype.$type, host), $.ig.util.cast($.ig.ISupportsErrorBars.prototype.$type, host), $.ig.util.cast($.ig.IBucketizer.prototype.$type, host));
	},
	init1: function (initNumber, host, markersHost, viewportHost, errorBarsHost, bucketizingHost) {
		$.ig.CategoryFramePreparerBase.prototype.init1.call(this, 1, host, markersHost, viewportHost, errorBarsHost, bucketizingHost);
		this.trendlineHost(new $.ig.DefaultCategoryTrendlineHost());
		if ($.ig.util.cast($.ig.IHasCategoryTrendline.prototype.$type, host) !== null) {
			this.trendlineHost($.ig.util.cast($.ig.IHasCategoryTrendline.prototype.$type, host));
		}
		this.valuesProvider(new $.ig.DefaultHighLowValueProvider());
		if ($.ig.util.cast($.ig.IHasHighLowValueCategory.prototype.$type, host) !== null) {
			this.valuesProvider($.ig.util.cast($.ig.IHasHighLowValueCategory.prototype.$type, host));
		}
	},
	_trendlineHost: null,
	trendlineHost: function (value) {
		if (arguments.length === 1) {
			this._trendlineHost = value;
			return value;
		} else {
			return this._trendlineHost;
		}
	}
	,
	_valuesProvider: null,
	valuesProvider: function (value) {
		if (arguments.length === 1) {
			this._valuesProvider = value;
			return value;
		} else {
			return this._valuesProvider;
		}
	}
	,
	prepareMarker: function (frame, bucket, collisionAvoider, itemIndex, markerCount, markerBucket) {
		var x = bucket[0];
		var yLow = bucket[1];
		var yHigh = bucket[2];
		if (!$.ig.util.isNaN(x) && !$.ig.util.isNaN(yLow) && !$.ig.util.isNaN(yHigh)) {
			frame._markers.add({ __x: x, __y: (yLow + yHigh) / 2, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			this.markersHost().updateMarkerTemplate(markerCount, itemIndex, markerBucket);
			return true;
		}
		return false;
	}
	,
	storeYValues: function (h, index, useTemp, isFragment) {
		var hl = h;
		var highValues = hl.highValues();
		var lowValues = hl.lowValues();
		var bucketHigh = this.convertToBucket(highValues.item(index));
		var bucketLow = this.convertToBucket(lowValues.item(index));
		var currentHigh = Math.max(bucketHigh, bucketLow);
		var currentLow = Math.min(bucketHigh, bucketLow);
		if (useTemp) {
			hl.tempY0(currentLow);
			hl.tempY1(currentHigh);
		} else {
			hl.bucketY1(currentHigh);
			hl.bucketY0(currentLow);
		}
	}
	,
	minMaxYValues: function (h, index, isFragment) {
		var hl = h;
		var highValues = hl.highValues();
		var lowValues = hl.lowValues();
		var high = this.convertToBucket(highValues.item(index));
		var low = this.convertToBucket(lowValues.item(index));
		if (!isNaN(high)) {
			hl.bucketY1(Math.max(hl.bucketY1(), high));
			hl.bucketY0(Math.min(hl.bucketY0(), high));
		}
		if (!isNaN(low)) {
			hl.bucketY1(Math.max(hl.bucketY1(), low));
			hl.bucketY0(Math.min(hl.bucketY0(), low));
		}
	}
	,
	getBucketSorting: function (xVal, h) {
		var hl = h;
		return [ this.convertToBucket(xVal), hl.bucketY0(), hl.bucketY1() ];
	}
	,
	scaleBucketValues: function (p, bucket, offset, isSortingScaler, xParams, yParams) {
		if (isSortingScaler) {
			bucket[0] = (bucket[0] + offset);
		} else {
			bucket[0] = (p.scaler().getScaledValue(bucket[0], xParams) + offset);
		}
		bucket[1] = p.yScaler().getScaledValue(bucket[1], yParams);
		bucket[2] = p.yScaler().getScaledValue(bucket[2], yParams);
	}
	,
	getValues: function (p) {
		var hl = new $.ig.HighLowValuesHolder();
		hl.highValues(this.valuesProvider().highColumn());
		hl.lowValues(this.valuesProvider().lowColumn());
		return hl;
	}
	,
	$type: new $.ig.Type('RangeCategoryFramePreparer', $.ig.CategoryFramePreparerBase.prototype.$type)
}, true);

$.ig.util.defType('HighLowValuesHolder', 'ValuesHolder', {
	init: function () {
		$.ig.ValuesHolder.prototype.init.call(this);
	},
	_highValues: null,
	highValues: function (value) {
		if (arguments.length === 1) {
			this._highValues = value;
			return value;
		} else {
			return this._highValues;
		}
	}
	,
	_lowValues: null,
	lowValues: function (value) {
		if (arguments.length === 1) {
			this._lowValues = value;
			return value;
		} else {
			return this._lowValues;
		}
	}
	,
	count: function () {
		if (this.highValues() == null || this.lowValues() == null) {
			return 0;
		}
		return Math.min(this.highValues().count(), this.lowValues().count());
	}
	,
	$type: new $.ig.Type('HighLowValuesHolder', $.ig.ValuesHolder.prototype.$type)
}, true);

$.ig.util.defType('DefaultHighLowValueProvider', 'Object', {
	init: function () {
		$.ig.Object.prototype.init.call(this);
	},
	highColumn: function () {
		return new $.ig.List$1(Number, 0);
	}
	,
	lowColumn: function () {
		return new $.ig.List$1(Number, 0);
	}
	,
	$type: new $.ig.Type('DefaultHighLowValueProvider', $.ig.Object.prototype.$type, [$.ig.IHasHighLowValueCategory.prototype.$type])
}, true);

$.ig.util.defType('RangeCategorySeries', 'CategorySeries', {
	createView: function () {
		return new $.ig.RangeCategorySeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.CategorySeries.prototype.onViewCreated.call(this, view);
		this.rangeView(view);
	}
	,
	_rangeView: null,
	rangeView: function (value) {
		if (arguments.length === 1) {
			this._rangeView = value;
			return value;
		} else {
			return this._rangeView;
		}
	}
	,
	init: function () {
		$.ig.CategorySeries.prototype.init.call(this);
		this.framePreparer(new $.ig.RangeCategoryFramePreparer(1, this, this.rangeView(), this, this, this.rangeView().bucketCalculator()));
	},
	_framePreparer: null,
	framePreparer: function (value) {
		if (arguments.length === 1) {
			this._framePreparer = value;
			return value;
		} else {
			return this._framePreparer;
		}
	}
	,
	lowMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RangeCategorySeries.prototype.lowMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RangeCategorySeries.prototype.lowMemberPathProperty);
		}
	}
	,
	lowColumn: function (value) {
		if (arguments.length === 1) {
			if (this._lowColumn != value) {
				var oldLowColumn = this._lowColumn;
				this._lowColumn = value;
				this.raisePropertyChanged($.ig.RangeCategorySeries.prototype.lowColumnPropertyName, oldLowColumn, this._lowColumn);
			}
			return value;
		} else {
			return this._lowColumn;
		}
	}
	,
	_lowColumn: null,
	highMemberPath: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RangeCategorySeries.prototype.highMemberPathProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RangeCategorySeries.prototype.highMemberPathProperty);
		}
	}
	,
	highColumn: function (value) {
		if (arguments.length === 1) {
			if (this._highColumn != value) {
				var oldHighColumn = this._highColumn;
				this._highColumn = value;
				this.raisePropertyChanged($.ig.RangeCategorySeries.prototype.highColumnPropertyName, oldHighColumn, this._highColumn);
			}
			return value;
		} else {
			return this._highColumn;
		}
	}
	,
	_highColumn: null,
	getDefaultTransitionInMode: function () {
		return $.ig.CategoryTransitionInMode.prototype.expand;
	}
	,
	renderFrame: function (frame, view) {
		$.ig.CategorySeries.prototype.renderFrame.call(this, frame, view);
		view.markerAppearanceHandled(true);
		$.ig.CategoryMarkerManager.prototype.rasterizeMarkers(this, frame._markers, view.markers(), this.useLightweightMarkers());
		this.colorizeMarkers(view, frame);
	}
	,
	rasterizePolygon: function (polyline0, polygon01, polyline1, count, buckets, useX0AsX1) {
		this.rangeView().rasterizePolygon(polyline0, polygon01, polyline1, count, buckets, useX0AsX1);
	}
	,
	scrollIntoView: function (item) {
		var unitRect = new $.ig.Rect(0, 0, 0, 1, 1);
		var windowRect = this.view() != null ? this.view().windowRect() : $.ig.Rect.prototype.empty();
		var viewportRect = this.view() != null ? this.view().viewport() : $.ig.Rect.prototype.empty();
		var index = !windowRect.isEmpty() && !viewportRect.isEmpty() && this.fastItemsSource() != null ? this.fastItemsSource().indexOf(item) : -1;
		var xAxis = this.getXAxis();
		var yAxis = this.getYAxis();
		var effectiveViewportRect = this.getEffectiveViewportForUnitViewport(this.view());
		var cx;
		if (xAxis != null) {
			var xParams = new $.ig.ScalerParams(0, unitRect, unitRect, xAxis.isInverted(), effectiveViewportRect);
			cx = xAxis.getScaledValue(index, xParams);
		} else {
			cx = NaN;
		}
		var offset = xAxis != null ? this.framePreparer().getOffset($.ig.util.cast($.ig.ICategoryScaler.prototype.$type, xAxis), unitRect, unitRect, effectiveViewportRect) : 0;
		cx += offset;
		if (index >= 0 && $.ig.Rect.prototype.l_op_Inequality(windowRect, null) && $.ig.Rect.prototype.l_op_Inequality(viewportRect, null)) {
			if (!$.ig.util.isNaN(cx)) {
				if (cx < windowRect.left() + 0.1 * windowRect.width()) {
					cx = cx + 0.4 * windowRect.width();
				}
				if (cx > windowRect.right() - 0.1 * windowRect.width()) {
					cx = cx - 0.4 * windowRect.width();
				}
				windowRect.x(cx - 0.5 * windowRect.width());
			}
			if (yAxis != null && this.highColumn() != null && index < this.highColumn().count()) {
				var yParams = new $.ig.ScalerParams(0, unitRect, unitRect, yAxis.isInverted(), effectiveViewportRect);
				var high = yAxis.getScaledValue(this.highColumn().item(index), yParams);
				var low = yAxis.getScaledValue(this.lowColumn().item(index), yParams);
				if (!$.ig.util.isNaN(high) && !$.ig.util.isNaN(low)) {
					var height = Math.abs(low - high);
					if (windowRect.height() < height) {
						windowRect.height(height);
						windowRect.y(Math.min(low, high));
					} else {
						if (low < windowRect.top() + 0.1 * windowRect.height()) {
							low = low + 0.4 * windowRect.height();
						}
						if (low > windowRect.bottom() - 0.1 * windowRect.height()) {
							low = low - 0.4 * windowRect.height();
						}
						windowRect.y(low - 0.5 * windowRect.height());
					}
				}
			}
			if (this.syncLink() != null) {
				this.syncLink().windowNotify(this.seriesViewer(), windowRect);
			}
		}
		return index >= 0;
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.CategorySeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.Series.prototype.fastItemsSourcePropertyName:
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, oldValue) != null) {
					(oldValue).deregisterColumn(this.lowColumn());
					(oldValue).deregisterColumn(this.highColumn());
					this.lowColumn(null);
					this.highColumn(null);
				}
				if ($.ig.util.cast($.ig.IFastItemsSource.prototype.$type, newValue) != null) {
					this.lowColumn(this.registerDoubleColumn(this.lowMemberPath()));
					this.highColumn(this.registerDoubleColumn(this.highMemberPath()));
				}
				if (!this.updateNumericAxisRange()) {
					this.categoryView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.RangeCategorySeries.prototype.lowMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.lowColumn());
					this.lowColumn(this.registerDoubleColumn(this.lowMemberPath()));
				}
				break;
			case $.ig.RangeCategorySeries.prototype.lowColumnPropertyName:
				if (!this.updateNumericAxisRange()) {
					this.categoryView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
			case $.ig.RangeCategorySeries.prototype.highMemberPathPropertyName:
				if (this.fastItemsSource() != null) {
					this.fastItemsSource().deregisterColumn(this.highColumn());
					this.highColumn(this.registerDoubleColumn(this.highMemberPath()));
				}
				break;
			case $.ig.RangeCategorySeries.prototype.highColumnPropertyName:
				if (!this.updateNumericAxisRange()) {
					this.categoryView().bucketCalculator().calculateBuckets(this.resolution());
					this.renderSeries(false);
				}
				break;
		}
	}
	,
	getRange: function (axis) {
		if (this.lowColumn() == null || this.lowColumn().count() == 0 || this.highColumn() == null || this.highColumn().count() == 0) {
			return null;
		}
		if (axis == this.getXAxis()) {
			var max = Math.min(this.lowColumn().count(), this.highColumn().count());
			return new $.ig.AxisRange(0, max - 1);
		}
		if (axis == this.getYAxis()) {
			var min = Math.min(this.lowColumn().minimum(), this.highColumn().minimum());
			var max1 = Math.max(this.lowColumn().maximum(), this.highColumn().maximum());
			return new $.ig.AxisRange(Math.min(min, max1), Math.max(min, max1));
		}
		return null;
	}
	,
	dataUpdatedOverride: function (action, position, count, propertyName) {
		if (this.getXAxis() != null && $.ig.util.cast($.ig.ISortingAxis.prototype.$type, this.getXAxis()) !== null) {
			(this.getXAxis()).notifyDataChanged();
		}
		var xAxis = this.getXAxis();
		switch (action) {
			case $.ig.FastItemsSourceEventAction.prototype.change:
				if (propertyName == this.lowMemberPath() || propertyName == this.highMemberPath()) {
					if (xAxis != null) {
						xAxis.updateRange();
					}
					if (!this.updateNumericAxisRange()) {
						this.renderSeries(true);
					}
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.insert:
				if (xAxis != null) {
					xAxis.updateRange();
				}
				this.categoryView().bucketCalculator().calculateBuckets(this.resolution());
				if (!this.updateNumericAxisRange()) {
					this.renderSeries(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.remove:
				if (xAxis != null) {
					xAxis.updateRange();
				}
				this.categoryView().bucketCalculator().calculateBuckets(this.resolution());
				if (!this.updateNumericAxisRange()) {
					this.renderSeries(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.replace:
				if (xAxis != null) {
					xAxis.updateRange();
				}
				if (this.lowMemberPath() != null && this.highMemberPath() != null && this.categoryView().bucketCalculator()._bucketSize > 0 && !this.updateNumericAxisRange()) {
					this.renderSeries(true);
				}
				break;
			case $.ig.FastItemsSourceEventAction.prototype.reset:
				if (xAxis != null) {
					xAxis.updateRange();
				}
				this.categoryView().bucketCalculator().calculateBuckets(this.resolution());
				if (!this.updateNumericAxisRange()) {
					this.renderSeries(true);
				}
				break;
		}
	}
	,
	validateSeries: function (viewportRect, windowRect, view) {
		var isValid = $.ig.CategorySeries.prototype.validateSeries.call(this, viewportRect, windowRect, view);
		if (this.lowColumn() == null || this.lowColumn().count() == 0 || this.highColumn() == null || this.highColumn().count() == 0) {
			isValid = false;
		}
		return isValid;
	}
	,
	prepareFrame: function (frame, view) {
		$.ig.CategorySeries.prototype.prepareFrame.call(this, frame, view);
		this.framePreparer().prepareFrame(frame, view);
	}
	,
	currentCategoryMode: function () {
		return this.preferredCategoryMode($.ig.util.cast($.ig.CategoryAxisBase.prototype.$type, this.getXAxis()));
	}
	,
	scaler: function () {
		return $.ig.util.cast($.ig.ICategoryScaler.prototype.$type, this.getXAxis());
	}
	,
	yScaler: function () {
		return $.ig.util.cast($.ig.IScaler.prototype.$type, this.getYAxis());
	}
	,
	bucketizer: function () {
		return this.categoryView().bucketCalculator();
	}
	,
	currentMode2Index: function () {
		return this.getMode2Index();
	}
	,
	provideCollisionDetector: function () {
		return new $.ig.CollisionAvoider();
	}
	,
	renderThumbnail: function (viewportRect, surface) {
		var dirty = this.thumbnailDirty();
		$.ig.CategorySeries.prototype.renderThumbnail.call(this, viewportRect, surface);
		if (!dirty) {
			this.view().prepSurface(surface);
			return;
		}
		this.view().prepSurface(surface);
		if (this.clearAndAbortIfInvalid1(this.thumbnailView())) {
			return;
		}
		var framePreparer = new $.ig.RangeCategoryFramePreparer(1, this, $.ig.util.cast($.ig.ISupportsMarkers.prototype.$type, this.thumbnailView()), this.seriesViewer().view().overviewPlusDetailViewportHost(), this, (this.thumbnailView()).bucketCalculator());
		if (!this.skipThumbnailPrepare()) {
			this._thumbnailFrame = new $.ig.CategoryFrame(3);
			this._thumbnailFrame.clearFrame();
			framePreparer.prepareFrame(this._thumbnailFrame, this.thumbnailView());
		}
		this.skipThumbnailPrepare(false);
		this.renderFrame(this._thumbnailFrame, this.thumbnailView());
		this.thumbnailDirty(false);
	}
	,
	_alternateFrame: null,
	alternateFrame: function (value) {
		if (arguments.length === 1) {
			this._alternateFrame = value;
			return value;
		} else {
			return this._alternateFrame;
		}
	}
	,
	renderAlternateView: function (viewportRect, windowRect, surface, viewIdentifier, effectiveScalingRatio) {
		$.ig.CategorySeries.prototype.renderAlternateView.call(this, viewportRect, windowRect, surface, viewIdentifier, effectiveScalingRatio);
		var view = this.alternateViews().item(viewIdentifier);
		var categorySeriesView = view;
		categorySeriesView.bucketCalculator().calculateBuckets(this.resolution());
		view.prepAltSurface(surface);
		if (this.clearAndAbortIfInvalid1(categorySeriesView)) {
			return;
		}
		var framePreparer = new $.ig.RangeCategoryFramePreparer(1, this, $.ig.util.cast($.ig.ISupportsMarkers.prototype.$type, view), view, this, (view).bucketCalculator());
		if (this.alternateFrame() == null) {
			this.alternateFrame(new $.ig.CategoryFrame(3));
		}
		this.alternateFrame().clearFrame();
		framePreparer.prepareFrame(this.alternateFrame(), categorySeriesView);
		this.renderFrame(this.alternateFrame(), categorySeriesView);
	}
	,
	$type: new $.ig.Type('RangeCategorySeries', $.ig.CategorySeries.prototype.$type, [$.ig.IIsCategoryBased.prototype.$type, $.ig.IHasHighLowValueCategory.prototype.$type])
}, true);

$.ig.util.defType('HorizontalRangeCategorySeries', 'RangeCategorySeries', {
	init: function () {
		$.ig.RangeCategorySeries.prototype.init.call(this);
	},
	xAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HorizontalRangeCategorySeries.prototype.xAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HorizontalRangeCategorySeries.prototype.xAxisProperty);
		}
	}
	,
	yAxis: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.HorizontalRangeCategorySeries.prototype.yAxisProperty, value);
			return value;
		} else {
			return this.getValue($.ig.HorizontalRangeCategorySeries.prototype.yAxisProperty);
		}
	}
	,
	getXAxis: function () {
		return this.xAxis();
	}
	,
	getYAxis: function () {
		return this.yAxis();
	}
	,
	getHighStrokeSegments: function (position) {
		var matching = this.getMatchingBuckets(this.getCategoryAxis(), this._currentFrame._buckets, this.getBucketSize(this.view()), this.getFirstBucket(this.view()), this.toWorldPosition(position), true);
		if (matching == null) {
			return null;
		}
		var prevBucketValue = matching[0];
		var nextBucketValue = matching[1];
		var ret = new Array(1);
		var seg = new Array(2);
		seg[0] = { __x: prevBucketValue[0], __y: prevBucketValue[2], $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		seg[1] = { __x: nextBucketValue[0], __y: nextBucketValue[2], $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		ret[0] = seg;
		return ret;
	}
	,
	getLowStrokeSegments: function (position) {
		var matching = this.getMatchingBuckets(this.getCategoryAxis(), this._currentFrame._buckets, this.getBucketSize(this.view()), this.getFirstBucket(this.view()), this.toWorldPosition(position), true);
		if (matching == null) {
			return null;
		}
		var prevBucketValue = matching[0];
		var nextBucketValue = matching[1];
		var ret = new Array(1);
		var seg = new Array(2);
		seg[0] = { __x: prevBucketValue[0], __y: prevBucketValue[1], $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		seg[1] = { __x: nextBucketValue[0], __y: nextBucketValue[1], $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName };
		ret[0] = seg;
		return ret;
	}
	,
	isRange: function () {
		return true;
	}
	,
	getResolvedHitTestMode: function () {
		if (this.hitTestMode() == $.ig.SeriesHitTestMode.prototype.auto) {
			return $.ig.SeriesHitTestMode.prototype.computational;
		} else {
			return $.ig.RangeCategorySeries.prototype.getResolvedHitTestMode.call(this);
		}
	}
	,
	getOffsetValue: function () {
		return this.framePreparer().getOffset(this.xAxis(), this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	getCategoryWidth: function () {
		return this.xAxis().getCategorySize(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	getNextOrExactIndex: function (world, skipUnknowns) {
		return this.getNextOrExactIndexHelper(world, skipUnknowns, this.xAxis(), this.getExactUnsortedItemIndex.runOn(this), new $.ig.RangeValueList(this.highColumn(), this.lowColumn()));
	}
	,
	getPreviousOrExactIndex: function (world, skipUnknowns) {
		return this.getPreviousOrExactIndexHelper(world, skipUnknowns, this.xAxis(), this.getExactUnsortedItemIndex.runOn(this), new $.ig.RangeValueList(this.highColumn(), this.lowColumn()));
	}
	,
	getDistanceToIndex: function (world, index, axis, p, offset) {
		if (axis == null) {
			return Number.POSITIVE_INFINITY;
		}
		var count = this.xAxis()._cachedItemsCount;
		return this.getDistanceToIndexHelper(world, index, this.xAxis(), p, offset, count, this.getExactUnsortedItemIndex.runOn(this));
	}
	,
	getSeriesValue: function (world, useInterpolation, skipUnknowns) {
		if (this.seriesViewer() == null) {
			return NaN;
		}
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var xParams = new $.ig.ScalerParams(0, this.seriesViewer().actualWindowRect(), this.view().viewport(), this.xAxis().isInverted(), effectiveViewportRect);
		var offset = this.framePreparer().getOffset(this.xAxis(), this.seriesViewer().actualWindowRect(), this.view().viewport(), effectiveViewportRect);
		return this.getSeriesValueHelper(new $.ig.RangeValueList(this.highColumn(), this.lowColumn()), world, this.xAxis(), xParams, offset, this.getExactUnsortedItemIndex.runOn(this), useInterpolation, skipUnknowns);
	}
	,
	getSeriesLowValue: function (world, useInterpolation, skipUnknowns) {
		if (this.seriesViewer() == null) {
			return NaN;
		}
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var xParams = new $.ig.ScalerParams(0, this.seriesViewer().actualWindowRect(), this.view().viewport(), this.xAxis().isInverted(), effectiveViewportRect);
		var offset = this.framePreparer().getOffset(this.xAxis(), this.seriesViewer().actualWindowRect(), this.view().viewport(), effectiveViewportRect);
		return this.getSeriesValueHelper(this.lowColumn(), world, this.xAxis(), xParams, offset, this.getExactUnsortedItemIndex.runOn(this), useInterpolation, skipUnknowns);
	}
	,
	getSeriesHighValue: function (world, useInterpolation, skipUnknowns) {
		if (this.seriesViewer() == null) {
			return NaN;
		}
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var xParams = new $.ig.ScalerParams(0, this.seriesViewer().actualWindowRect(), this.view().viewport(), this.xAxis().isInverted(), effectiveViewportRect);
		var offset = this.framePreparer().getOffset(this.xAxis(), this.seriesViewer().actualWindowRect(), this.view().viewport(), effectiveViewportRect);
		return this.getSeriesValueHelper(this.highColumn(), world, this.xAxis(), xParams, offset, this.getExactUnsortedItemIndex.runOn(this), useInterpolation, skipUnknowns);
	}
	,
	getSeriesHighValuePosition: function (world, useInterpolation, skipUnknowns) {
		var $self = this;
		return this.getSeriesValuePositionHelper(world, useInterpolation, skipUnknowns, this.framePreparer().getOffset(this.xAxis(), this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view())), this.yAxis(), this.xAxis(), this.getSeriesHighValue.runOn(this), function (w, skip) { return $self.getPreviousOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.highColumn()); }, function (w, skip) { return $self.getNextOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.highColumn()); });
	}
	,
	getSeriesLowValuePosition: function (world, useInterpolation, skipUnknowns) {
		var $self = this;
		return this.getSeriesValuePositionHelper(world, useInterpolation, skipUnknowns, this.framePreparer().getOffset(this.xAxis(), this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view())), this.yAxis(), this.xAxis(), this.getSeriesLowValue.runOn(this), function (w, skip) { return $self.getPreviousOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.lowColumn()); }, function (w, skip) { return $self.getNextOrExactIndexHelper(w, skip, $self.xAxis(), $self.getExactUnsortedItemIndex.runOn($self), $self.lowColumn()); });
	}
	,
	getSeriesValuePosition: function (world, useInterpolation, skipUnknowns) {
		return this.getSeriesValuePositionHelper(world, useInterpolation, skipUnknowns, this.framePreparer().getOffset(this.xAxis(), this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view())), this.yAxis(), this.xAxis(), null, null, null);
	}
	,
	updateNumericAxisRange: function () {
		return this.yAxis() != null && this.yAxis().updateRange();
	}
	,
	propertyUpdatedOverride: function (sender, propertyName, oldValue, newValue) {
		$.ig.RangeCategorySeries.prototype.propertyUpdatedOverride.call(this, sender, propertyName, oldValue, newValue);
		switch (propertyName) {
			case $.ig.HorizontalRangeCategorySeries.prototype.xAxisPropertyName:
				this.deregisterForAxis($.ig.util.cast($.ig.Axis.prototype.$type, oldValue));
				this.registerForAxis($.ig.util.cast($.ig.Axis.prototype.$type, newValue));
				this.categoryView().bucketCalculator().calculateBuckets(this.resolution());
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
			case $.ig.HorizontalRangeCategorySeries.prototype.yAxisPropertyName:
				this.deregisterForAxis($.ig.util.cast($.ig.Axis.prototype.$type, oldValue));
				this.registerForAxis($.ig.util.cast($.ig.Axis.prototype.$type, newValue));
				this.categoryView().bucketCalculator().calculateBuckets(this.resolution());
				this.updateNumericAxisRange();
				this.renderSeries(false);
				this.notifyThumbnailAppearanceChanged();
				break;
		}
	}
	,
	canUseAsYAxis: function (axis) {
		if ($.ig.util.cast($.ig.NumericYAxis.prototype.$type, axis) !== null) {
			return true;
		}
		return false;
	}
	,
	canUseAsXAxis: function (axis) {
		if ($.ig.util.cast($.ig.CategoryXAxis.prototype.$type, axis) !== null || $.ig.util.cast($.ig.CategoryDateTimeXAxis.prototype.$type, axis) !== null) {
			return true;
		}
		return false;
	}
	,
	$type: new $.ig.Type('HorizontalRangeCategorySeries', $.ig.RangeCategorySeries.prototype.$type)
}, true);

$.ig.util.defType('RangeAreaSeries', 'HorizontalRangeCategorySeries', {
	createView: function () {
		return new $.ig.RangeAreaSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.HorizontalRangeCategorySeries.prototype.onViewCreated.call(this, view);
		this.rangeAreaView(view);
	}
	,
	_rangeAreaView: null,
	rangeAreaView: function (value) {
		if (arguments.length === 1) {
			this._rangeAreaView = value;
			return value;
		} else {
			return this._rangeAreaView;
		}
	}
	,
	testRangeAreaOver: function (position, lowPosition, highPosition, isFinger) {
		if ($.ig.util.isNaN(lowPosition.__x) || $.ig.util.isNaN(lowPosition.__y)) {
			return false;
		}
		if ($.ig.util.isNaN(highPosition.__x) || $.ig.util.isNaN(highPosition.__y)) {
			return false;
		}
		if ((position.__y <= lowPosition.__y && position.__y >= highPosition.__y)) {
			return true;
		}
		return false;
	}
	,
	testHit: function (position, isFinger) {
		if (this.testHighLowStrokeOver(position, isFinger)) {
			return true;
		}
		if (this.testMarkersOver(position, isFinger)) {
			return true;
		}
		return false;
	}
	,
	init: function () {
		$.ig.HorizontalRangeCategorySeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.RangeAreaSeries.prototype.$type);
	},
	preferredCategoryMode: function (axis) {
		return $.ig.CategoryMode.prototype.mode0;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.HorizontalRangeCategorySeries.prototype.clearRendering.call(this, wipeClean, view);
		var rangeAreaView = view;
		rangeAreaView.clearRangeArea();
	}
	,
	renderFrame: function (frame, view) {
		$.ig.HorizontalRangeCategorySeries.prototype.renderFrame.call(this, frame, view);
		var count = frame._buckets.count();
		var buckets = new $.ig.List$1(Array, 2, count);
		for (var i = 0; i < count; i++) {
			var frameBucket = frame._buckets.__inner[i];
			var bucket = new Array(4);
			bucket[0] = frameBucket[0];
			bucket[1] = frameBucket[1];
			var frameBucket2 = frame._buckets.__inner[frame._buckets.count() - 1 - i];
			bucket[2] = frameBucket2[0];
			bucket[3] = frameBucket2[2];
			buckets.add(bucket);
		}
		var rangeAreaView = $.ig.util.cast($.ig.RangeAreaSeriesView.prototype.$type, view);
		var effectiveViewportRect = this.getEffectiveViewport1(rangeAreaView);
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.xAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		if (args != null) {
			areStylesOverriden = true;
		}
		if (areStylesOverriden) {
			var xParams = new $.ig.ScalerParams(0, view.windowRect(), view.viewport(), this.xAxis().isInverted(), effectiveViewportRect);
			this.performCategoryStyleOverride(buckets, -1, this.lowColumn().count(), this.xAxis(), xParams, view.isThumbnailView());
		}
		var line0 = rangeAreaView.polyline0();
		var line1 = rangeAreaView.polyline1();
		var fillArea = rangeAreaView.polygon01();
		this._renderManager.setCategoryShapeAppearance(line0, true, false, true, true);
		this._renderManager.setCategoryShapeAppearance(line1, true, false, true, true);
		this._renderManager.setCategoryShapeAppearance(fillArea, false, true, false, false);
		if (view.checkFrameDirty(frame)) {
			rangeAreaView.rasterizeRangeArea(frame._buckets.count(), buckets, false);
			view.updateFrameVersion(frame);
		}
		rangeAreaView.polygon01().__opacity = this._renderManager._actualRenderOpacity * this.actualAreaFillOpacity();
	}
	,
	$type: new $.ig.Type('RangeAreaSeries', $.ig.HorizontalRangeCategorySeries.prototype.$type)
}, true);

$.ig.util.defType('RangeValueList', 'Object', {
	__highColumn: null,
	__lowColumn: null,
	init: function (highColumn, lowColumn) {
		$.ig.Object.prototype.init.call(this);
		this.__highColumn = highColumn;
		this.__lowColumn = lowColumn;
	},
	indexOf: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	insert: function (index, item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	removeAt: function (index) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	item: function (index, value) {
		if (arguments.length === 2) {
			throw new $.ig.NotImplementedException(0);
			return value;
		} else {
			var high = NaN;
			var low = NaN;
			if (this.__highColumn != null && index >= 0 && index < this.__highColumn.count()) {
				high = this.__highColumn.item(index);
			}
			if (this.__lowColumn != null && index >= 0 && index < this.__lowColumn.count()) {
				low = this.__lowColumn.item(index);
			}
			var highIsNaN = $.ig.util.isNaN(high);
			var lowIsNaN = $.ig.util.isNaN(low);
			if (!highIsNaN && !lowIsNaN) {
				return (high + low) / 2;
			}
			if (!highIsNaN) {
				return high;
			}
			if (!lowIsNaN) {
				return low;
			}
			return NaN;
		}
	}
	,
	add: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	clear: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	contains: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	copyTo: function (array, arrayIndex) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	count: function () {
		var highCount = 0;
		var lowCount = 0;
		if (this.__highColumn != null) {
			highCount = this.__highColumn.count();
		}
		if (this.__lowColumn != null) {
			lowCount = this.__lowColumn.count();
		}
		var count = 0;
		count = Math.max(count, highCount);
		count = Math.max(count, lowCount);
		return count;
	}
	,
	isReadOnly: function () {
		return true;
	}
	,
	remove: function (item) {
		throw new $.ig.NotImplementedException(0);
	}
	,
	getEnumerator: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	getEnumerator: function () {
		throw new $.ig.NotImplementedException(0);
	}
	,
	$type: new $.ig.Type('RangeValueList', $.ig.Object.prototype.$type, [$.ig.IList$1.prototype.$type.specialize(Number)])
}, true);

$.ig.util.defType('RangeColumnSeries', 'HorizontalRangeCategorySeries', {
	createView: function () {
		return new $.ig.RangeColumnSeriesView(this);
	}
	,
	onViewCreated: function (view) {
		$.ig.HorizontalRangeCategorySeries.prototype.onViewCreated.call(this, view);
		this.rangeColumnView(view);
	}
	,
	_rangeColumnView: null,
	rangeColumnView: function (value) {
		if (arguments.length === 1) {
			this._rangeColumnView = value;
			return value;
		} else {
			return this._rangeColumnView;
		}
	}
	,
	init: function () {
		$.ig.HorizontalRangeCategorySeries.prototype.init.call(this);
		this.defaultStyleKey($.ig.RangeColumnSeries.prototype.$type);
	},
	radiusX: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RangeColumnSeries.prototype.radiusXProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RangeColumnSeries.prototype.radiusXProperty);
		}
	}
	,
	radiusY: function (value) {
		if (arguments.length === 1) {
			this.setValue($.ig.RangeColumnSeries.prototype.radiusYProperty, value);
			return value;
		} else {
			return this.getValue($.ig.RangeColumnSeries.prototype.radiusYProperty);
		}
	}
	,
	hasIndividualElements: function () {
		return true;
	}
	,
	preferredCategoryMode: function (axis) {
		return $.ig.CategoryMode.prototype.mode2;
	}
	,
	getSeriesValueBoundingBox: function (world) {
		var pos = this.fromWorldPosition(world);
		var windowRect = this.view().windowRect();
		var viewportRect = this.view().viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(this.view());
		var yParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.yAxis().isInverted(), effectiveViewportRect);
		var matching = this.getMatchingBuckets(this.getCategoryAxis(), this._currentFrame._buckets, this.getBucketSize(this.view()), this.getFirstBucket(this.view()), world, true);
		if (matching == null) {
			return $.ig.Rect.prototype.empty();
		}
		var prevBucketValue = matching[0];
		var nextBucketValue = matching[1];
		var prevDist = Math.abs(pos.__x - prevBucketValue[0]);
		var nextDist = Math.abs(pos.__x - nextBucketValue[0]);
		var groupWidth = this.xAxis().getGroupSize(windowRect, viewportRect, effectiveViewportRect);
		if (prevDist < nextDist) {
			var left = prevBucketValue[0] - 0.5 * groupWidth;
			var top = prevBucketValue[1];
			var bottom = prevBucketValue[2];
			return new $.ig.Rect(0, left, Math.min(top, bottom), groupWidth, Math.max(top, bottom) - Math.min(top, bottom));
		} else {
			var left1 = nextBucketValue[0] - 0.5 * groupWidth;
			var top1 = nextBucketValue[1];
			var bottom1 = nextBucketValue[2];
			return new $.ig.Rect(0, left1, Math.min(top1, bottom1), groupWidth, Math.max(top1, bottom1) - Math.min(top1, bottom1));
		}
	}
	,
	testHit: function (position, isFinger) {
		if (this.testShapesOver(position, isFinger)) {
			return true;
		}
		if (this.testMarkersOver(position, isFinger)) {
			return true;
		}
		return false;
	}
	,
	clearRendering: function (wipeClean, view) {
		$.ig.HorizontalRangeCategorySeries.prototype.clearRendering.call(this, wipeClean, view);
		var rangeColumnView = view;
		if (wipeClean && rangeColumnView.columns() != null) {
			rangeColumnView.columns().count(0);
		}
	}
	,
	getItemSpan: function () {
		return this.xAxis().getGroupSize(this.view().windowRect(), this.view().viewport(), this.getEffectiveViewport1(this.view()));
	}
	,
	renderFrame: function (frame, view) {
		$.ig.HorizontalRangeCategorySeries.prototype.renderFrame.call(this, frame, view);
		var rangeColumnView = $.ig.util.cast($.ig.RangeColumnSeriesView.prototype.$type, view);
		var buckets = frame._buckets;
		var windowRect = view.windowRect();
		var viewportRect = view.viewport();
		var effectiveViewportRect = this.getEffectiveViewport1(view);
		var groupWidth = this.xAxis().getGroupSize(windowRect, viewportRect, effectiveViewportRect);
		if ($.ig.util.isNaN(groupWidth) || Number.isInfinity(groupWidth)) {
			rangeColumnView.columns().count(0);
			return;
		}
		this._renderManager.initCategoryRenderSettings(this, this.shouldOverrideCategoryStyle(), this.xAxis(), this.getCategoryItems.runOn(this), this.getBucketSize(view), this.getFirstBucket(view));
		this._renderManager._initialRenderRadiusX = this.radiusX();
		this._renderManager._initialRenderRadiusY = this.radiusY();
		this._renderManager._actualRenderRadiusX = this.radiusX();
		this._renderManager._actualRenderRadiusY = this.radiusY();
		var areStylesOverriden = false;
		var args = this._renderManager.categoryOverrideArgs();
		if (args != null) {
			areStylesOverriden = true;
		}
		var isSorting = this.xAxis().isSorting();
		var valueCount = this.lowColumn().count();
		var xAxis = this.xAxis();
		var xParams = new $.ig.ScalerParams(0, windowRect, viewportRect, this.xAxis().isInverted(), effectiveViewportRect);
		var bucketCount = 0;
		for (var i = 0; i < buckets.count(); ++i) {
			var left = buckets.__inner[i][0] - 0.5 * groupWidth;
			var top = Math.min(buckets.__inner[i][1], buckets.__inner[i][2]);
			var bottom = Math.max(buckets.__inner[i][1], buckets.__inner[i][2]);
			var height = Math.abs(bottom - top);
			if (Number.isInfinity(height)) {
				continue;
			}
			var rectangle = rangeColumnView.columns().item(bucketCount);
			bucketCount++;
			rectangle.width(groupWidth);
			rectangle.height(height);
			if (areStylesOverriden) {
				this.performCategoryStyleOverride(buckets, i, valueCount, xAxis, xParams, view.isThumbnailView());
			}
			this._renderManager.setCategoryShapeAppearance(rectangle, false, false, false, false);
			rectangle.radiusX(this._renderManager._actualRenderRadiusX);
			rectangle.radiusY(this._renderManager._actualRenderRadiusY);
			rangeColumnView.positionRectangle(rectangle, left, top);
		}
		rangeColumnView.columns().count(bucketCount);
		view.updateFrameVersion(frame);
	}
	,
	$type: new $.ig.Type('RangeColumnSeries', $.ig.HorizontalRangeCategorySeries.prototype.$type)
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

$.ig.util.defType('RangeCategorySeriesView', 'CategorySeriesView', {
	_rangeModel: null,
	rangeModel: function (value) {
		if (arguments.length === 1) {
			this._rangeModel = value;
			return value;
		} else {
			return this._rangeModel;
		}
	}
	,
	init: function (model) {
		$.ig.CategorySeriesView.prototype.init.call(this, model);
		this.rangeModel(model);
	},
	rasterizePolygon: function (polyline0, polygon01, polyline1, count, buckets, useX0AsX1) {
		polyline0.points().clear();
		polygon01.points().clear();
		polyline1.points().clear();
		var indexes = $.ig.Flattener.prototype.chunkedFastFlatten1(count, buckets, true, useX0AsX1, this.model().resolution());
		var indexes2 = $.ig.Flattener.prototype.chunkedFastFlatten1(count, buckets, false, useX0AsX1, this.model().resolution());
		var indexCount = indexes.count();
		var index2Count = indexes2.count();
		var index;
		var bucket;
		var x0;
		var y0;
		for (var i = 0; i < indexCount; i++) {
			index = indexes.item(i);
			bucket = buckets.__inner[index];
			x0 = bucket[0];
			y0 = bucket[1];
			polyline0.points().add({ __x: x0, __y: y0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			polygon01.points().add({ __x: x0, __y: y0, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		var x1;
		var y1;
		for (var i1 = 0; i1 < index2Count; i1++) {
			index = indexes2.item(i1);
			bucket = buckets.__inner[index];
			if (useX0AsX1) {
				x1 = bucket[0];
				y1 = bucket[2];
			} else {
				x1 = bucket[2];
				y1 = bucket[3];
			}
			polyline1.points().add({ __x: x1, __y: y1, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
			polygon01.points().add({ __x: x1, __y: y1, $type: $.ig.Point.prototype.$type, getType: $.ig.Object.prototype.getType, getGetHashCode: $.ig.Object.prototype.getGetHashCode, typeName: $.ig.Object.prototype.typeName });
		}
		polyline0.isHitTestVisible(polyline0.points().count() > 0);
		polygon01.isHitTestVisible(polygon01.points().count() > 0);
		polyline1.isHitTestVisible(polyline1.points().count() > 0);
	}
	,
	createBucketCalculator: function () {
		return new $.ig.RangeCategoryBucketCalculator(this);
	}
	,
	getDefaultTooltipTemplate: function () {
		var tooltipTemplate = "<div class='ui-chart-default-tooltip-content'>";
		var axis = null;
		if (this.rangeModel().getXAxis().isCategory()) {
			axis = this.rangeModel().getXAxis();
		} else if (this.rangeModel().getYAxis().isCategory()) {
			axis = this.rangeModel().getYAxis();
		}
		var dateTimeAxis = $.ig.util.cast($.ig.CategoryDateTimeXAxis.prototype.$type, axis);
		if (dateTimeAxis != null) {
			tooltipTemplate += "<span>${item." + dateTimeAxis.dateTimeMemberPath() + "}</span><br/>";
		} else if (axis != null && axis.label() != null) {
			tooltipTemplate += "<span>${item." + axis.label() + "}</span><br/>";
		}
		tooltipTemplate += "<span";
		var nonTransparentOutline = this.model().actualOutline() != null && $.ig.Color.prototype.l_op_Inequality_Lifted($.ig.util.toNullable($.ig.Color.prototype.$type, this.model().actualOutline().color()), $.ig.util.toNullable($.ig.Color.prototype.$type, null)) && this.model().actualOutline().color().a() > 0;
		if (nonTransparentOutline) {
			tooltipTemplate += " style='color:" + this.model().actualOutline().__fill + "'";
		}
		tooltipTemplate += ">" + this.rangeModel().title() + ": </span><span class='ui-priority-primary'>" + "${item." + this.rangeModel().lowMemberPath() + "} - ${item." + this.rangeModel().highMemberPath() + "}</span></div>";
		return tooltipTemplate;
	}
	,
	$type: new $.ig.Type('RangeCategorySeriesView', $.ig.CategorySeriesView.prototype.$type)
}, true);

$.ig.util.defType('RangeAreaSeriesView', 'RangeCategorySeriesView', {
	_rangeAreaModel: null,
	rangeAreaModel: function (value) {
		if (arguments.length === 1) {
			this._rangeAreaModel = value;
			return value;
		} else {
			return this._rangeAreaModel;
		}
	}
	,
	init: function (model) {
		this._polyline0 = new $.ig.Polyline();
		this._polygon01 = new $.ig.Polygon();
		this._polyline1 = new $.ig.Polyline();
		this.__hitPolyline1 = new $.ig.Polyline();
		this.__hitPolyline0 = new $.ig.Polyline();
		this.__hitPolygon01 = new $.ig.Polygon();
		$.ig.RangeCategorySeriesView.prototype.init.call(this, model);
		this.rangeAreaModel(model);
	},
	onInit: function () {
		$.ig.RangeCategorySeriesView.prototype.onInit.call(this);
		if (!this.isThumbnailView()) {
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	_polyline0: null,
	_polygon01: null,
	_polyline1: null,
	polyline0: function () {
		return this._polyline0;
	}
	,
	polygon01: function () {
		return this._polygon01;
	}
	,
	polyline1: function () {
		return this._polyline1;
	}
	,
	clearRangeArea: function () {
		this._polygon01.points().clear();
		this._polyline0.points().clear();
		this._polyline1.points().clear();
	}
	,
	rasterizeRangeArea: function (count, buckets, useX0asX1) {
		this.rasterizePolygon(this._polyline0, this._polygon01, this._polyline1, count, buckets, useX0asX1);
		this.makeDirty();
	}
	,
	__hitPolyline1: null,
	__hitPolyline0: null,
	__hitPolygon01: null,
	setupHitAppearanceOverride: function () {
		$.ig.RangeCategorySeriesView.prototype.setupHitAppearanceOverride.call(this);
		this.__hitPolyline0.points(this._polyline0.points());
		this.__hitPolyline1.points(this._polyline1.points());
		this.__hitPolygon01.points(this._polygon01.points());
		var hitBrush = this.getHitBrush();
		this.__hitPolyline0.__stroke = hitBrush;
		this.__hitPolyline0.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolyline1.__stroke = hitBrush;
		this.__hitPolyline1.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		this.__hitPolygon01.__fill = hitBrush;
		this.__hitPolygon01.__opacity = 1;
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.RangeCategorySeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			if (isHitContext) {
				context.renderPolygon(this.__hitPolygon01);
				context.renderPolyline(this.__hitPolyline0);
				context.renderPolyline(this.__hitPolyline1);
			} else {
				context.renderPolygon(this._polygon01);
				context.renderPolyline(this._polyline0);
				context.renderPolyline(this._polyline1);
			}
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.RangeCategorySeriesView.prototype.exportViewShapes.call(this, svd);
		var lowerShape = new $.ig.PolyLineVisualData(1, "lowerShape", this._polyline0);
		lowerShape.tags().add("Lower");
		var upperShape = new $.ig.PolyLineVisualData(1, "upperShape", this._polyline1);
		upperShape.tags().add("Upper");
		upperShape.tags().add("Main");
		var fill = new $.ig.PolygonVisualData(1, "fillShape", this._polygon01);
		fill.tags().add("Fill");
		svd.shapes().add(lowerShape);
		svd.shapes().add(upperShape);
		svd.shapes().add(fill);
	}
	,
	$type: new $.ig.Type('RangeAreaSeriesView', $.ig.RangeCategorySeriesView.prototype.$type)
}, true);

$.ig.util.defType('RangeColumnSeriesView', 'RangeCategorySeriesView', {
	_rangeColumnModel: null,
	rangeColumnModel: function (value) {
		if (arguments.length === 1) {
			this._rangeColumnModel = value;
			return value;
		} else {
			return this._rangeColumnModel;
		}
	}
	,
	init: function (model) {
		var $self = this;
		this.__hitItem = new $.ig.Rectangle();
		$.ig.RangeCategorySeriesView.prototype.init.call(this, model);
		this.rangeColumnModel(model);
		this.columns((function () {
			var $ret = new $.ig.Pool$1($.ig.Rectangle.prototype.$type);
			$ret.create($self.columnCreate.runOn($self));
			$ret.activate($self.columnActivate.runOn($self));
			$ret.disactivate($self.columnDisactivate.runOn($self));
			$ret.destroy($self.columnDestroy.runOn($self));
			return $ret;
		}()));
	},
	_columns: null,
	columns: function (value) {
		if (arguments.length === 1) {
			this._columns = value;
			return value;
		} else {
			return this._columns;
		}
	}
	,
	onInit: function () {
		$.ig.RangeCategorySeriesView.prototype.onInit.call(this);
		this.visibleColumns(new $.ig.List$1($.ig.Rectangle.prototype.$type, 0));
		if (!this.isThumbnailView()) {
			this.model().resolution(4);
			this.model().legendItemBadgeTemplate((function () {
				var $ret = new $.ig.DataTemplate();
				$ret.render($.ig.LegendTemplates.prototype.rectBadgeTemplate);
				$ret.measure($.ig.LegendTemplates.prototype.legendItemBadgeMeasure);
				return $ret;
			}()));
		}
	}
	,
	_visibleColumns: null,
	visibleColumns: function (value) {
		if (arguments.length === 1) {
			this._visibleColumns = value;
			return value;
		} else {
			return this._visibleColumns;
		}
	}
	,
	columnCreate: function () {
		var column = new $.ig.Rectangle();
		this.visibleColumns().add(column);
		column.__visibility = $.ig.Visibility.prototype.collapsed;
		return column;
	}
	,
	columnActivate: function (column) {
		column.__visibility = $.ig.Visibility.prototype.visible;
	}
	,
	columnDisactivate: function (column) {
		column.__visibility = $.ig.Visibility.prototype.collapsed;
	}
	,
	columnDestroy: function (column) {
		this.visibleColumns().remove(column);
	}
	,
	positionRectangle: function (column, left, top) {
		if (!this.isDirty()) {
			this.makeDirty();
		}
		column.canvasTop(top);
		column.canvasLeft(left);
	}
	,
	getItem: function (index) {
		return this.visibleColumns().__inner[index];
	}
	,
	__hitItem: null,
	getHitItem: function (index) {
		var item = this.visibleColumns().__inner[index];
		this.__hitItem.__visibility = item.__visibility;
		this.__hitItem.canvasLeft(item.canvasLeft());
		this.__hitItem.canvasTop(item.canvasTop());
		this.__hitItem.width(item.width());
		this.__hitItem.height(item.height());
		var hitBrush = this.getHitBrush1(index);
		this.__hitItem.__fill = hitBrush;
		this.__hitItem.__stroke = hitBrush;
		this.__hitItem.strokeThickness(this.model().thickness() + $.ig.SeriesView.prototype.hIT_THICKNESS_AUGMENT);
		return this.__hitItem;
	}
	,
	renderOverride: function (context, isHitContext) {
		$.ig.RangeCategorySeriesView.prototype.renderOverride.call(this, context, isHitContext);
		if (context.shouldRender()) {
			for (var i = 0; i < this.visibleColumns().count(); i++) {
				var column = this.getCurrentItem(i, isHitContext);
				this.setupItemAppearance(column, i, isHitContext);
				context.renderRectangle(column);
			}
		}
	}
	,
	exportViewShapes: function (svd) {
		$.ig.RangeCategorySeriesView.prototype.exportViewShapes.call(this, svd);
		var i = 0;
		var toSort = new $.ig.List$1($.ig.Rectangle.prototype.$type, 0);
		var en = this.columns().active().getEnumerator();
		while (en.moveNext()) {
			var column = en.current();
			toSort.add(column);
		}
		toSort.sort2(function (c1, c2) {
			if (c1.canvasLeft() < c2.canvasLeft()) {
				return -1;
			} else if (c1.canvasLeft() > c2.canvasLeft()) {
				return 1;
			} else {
				return 0;
			}
		});
		var en1 = toSort.getEnumerator();
		while (en1.moveNext()) {
			var column1 = en1.current();
			var rvd = new $.ig.RectangleVisualData(1, "column" + i, column1);
			rvd.tags().add("Main");
			rvd.tags().add("Fill");
			svd.shapes().add(rvd);
		}
		i++;
	}
	,
	$type: new $.ig.Type('RangeColumnSeriesView', $.ig.RangeCategorySeriesView.prototype.$type)
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

$.ig.RangeCategorySeries.prototype.lowMemberPathPropertyName = "LowMemberPath";
$.ig.RangeCategorySeries.prototype.lowColumnPropertyName = "LowColumn";
$.ig.RangeCategorySeries.prototype.highMemberPathPropertyName = "HighMemberPath";
$.ig.RangeCategorySeries.prototype.highColumnPropertyName = "HighColumn";
$.ig.RangeCategorySeries.prototype.lowMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.RangeCategorySeries.prototype.lowMemberPathPropertyName, String, $.ig.RangeCategorySeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.RangeCategorySeries.prototype.lowMemberPathPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RangeCategorySeries.prototype.highMemberPathProperty = $.ig.DependencyProperty.prototype.register($.ig.RangeCategorySeries.prototype.highMemberPathPropertyName, String, $.ig.RangeCategorySeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.RangeCategorySeries.prototype.highMemberPathPropertyName, e.oldValue(), e.newValue());
}));

$.ig.HorizontalRangeCategorySeries.prototype.xAxisPropertyName = "XAxis";
$.ig.HorizontalRangeCategorySeries.prototype.yAxisPropertyName = "YAxis";
$.ig.HorizontalRangeCategorySeries.prototype.xAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.HorizontalRangeCategorySeries.prototype.xAxisPropertyName, $.ig.CategoryAxisBase.prototype.$type, $.ig.HorizontalRangeCategorySeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HorizontalRangeCategorySeries.prototype.xAxisPropertyName, e.oldValue(), e.newValue());
}));
$.ig.HorizontalRangeCategorySeries.prototype.yAxisProperty = $.ig.DependencyProperty.prototype.register($.ig.HorizontalRangeCategorySeries.prototype.yAxisPropertyName, $.ig.NumericYAxis.prototype.$type, $.ig.HorizontalRangeCategorySeries.prototype.$type, new $.ig.PropertyMetadata(2, null, function (sender, e) {
	(sender).raisePropertyChanged($.ig.HorizontalRangeCategorySeries.prototype.yAxisPropertyName, e.oldValue(), e.newValue());
}));

$.ig.RangeColumnSeries.prototype.radiusXPropertyName = "RadiusX";
$.ig.RangeColumnSeries.prototype.radiusYPropertyName = "RadiusY";
$.ig.RangeColumnSeries.prototype.radiusXProperty = $.ig.DependencyProperty.prototype.register($.ig.RangeColumnSeries.prototype.radiusXPropertyName, Number, $.ig.RangeColumnSeries.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (sender, e) {
	(sender).raisePropertyChanged($.ig.RangeColumnSeries.prototype.radiusXPropertyName, e.oldValue(), e.newValue());
}));
$.ig.RangeColumnSeries.prototype.radiusYProperty = $.ig.DependencyProperty.prototype.register($.ig.RangeColumnSeries.prototype.radiusYPropertyName, Number, $.ig.RangeColumnSeries.prototype.$type, new $.ig.PropertyMetadata(2, 2, function (sender, e) {
	(sender).raisePropertyChanged($.ig.RangeColumnSeries.prototype.radiusYPropertyName, e.oldValue(), e.newValue());
}));

$.ig.Legend.prototype.orientationPropertyName = "Orientation";

} (jQuery));


