package nts.uk.shr.infra.web.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import org.apache.commons.lang3.StringUtils;

import nts.uk.shr.infra.web.util.FilterConst;

public class NtsHttpServletRequest implements HttpServletRequest {

	private HttpServletRequest originalRequest; 
	
	private Map<String, String[]> parameters;
	
	private NtsHttpServletRequest(HttpServletRequest request){
		this.originalRequest = request;
		this.parameters = new HashMap<>(request.getParameterMap());
	}
	
	public static NtsHttpServletRequest wrap(HttpServletRequest request){
		return new NtsHttpServletRequest(request);
	}
	
	@Override
	public Object getAttribute(String name) {
		return originalRequest.getAttribute(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return originalRequest.getAttributeNames();
	}

	@Override
	public String getCharacterEncoding() {
		return originalRequest.getCharacterEncoding();
	}

	@Override
	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		originalRequest.setCharacterEncoding(env);
	}

	@Override
	public int getContentLength() {
		return originalRequest.getContentLength();
	}

	@Override
	public long getContentLengthLong() {
		return originalRequest.getContentLengthLong();
	}

	@Override
	public String getContentType() {
		return originalRequest.getContentType();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return originalRequest.getInputStream();
	}

	@Override
	public String getParameter(String name) {
		String[] strings = parameters.get(name);
        if (strings != null)
        {
            return strings[0];
        }
        return originalRequest.getParameter(name);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(parameters.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		return parameters.get(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		//TODO: to check
		return Collections.unmodifiableMap(parameters);
	}
	
	public void removeParam(String key){
		parameters.remove(key);
	}
	
	public boolean containsParam(String key){
		return parameters.containsKey(key);
	}

	@Override
	public String getProtocol() {
		return originalRequest.getProtocol();
	}

	@Override
	public String getScheme() {
		return originalRequest.getScheme();
	}

	@Override
	public String getServerName() {
		return originalRequest.getServerName();
	}

	@Override
	public int getServerPort() {
		return originalRequest.getServerPort();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return originalRequest.getReader();
	}

	@Override
	public String getRemoteAddr() {
		return originalRequest.getRemoteAddr();
	}

	@Override
	public String getRemoteHost() {
		return originalRequest.getRemoteHost();
	}

	@Override
	public void setAttribute(String name, Object o) {
		originalRequest.setAttribute(name, o);
	}

	@Override
	public void removeAttribute(String name) {
		originalRequest.removeAttribute(name);
	}

	@Override
	public Locale getLocale() {
		return originalRequest.getLocale();
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return originalRequest.getLocales();
	}

	@Override
	public boolean isSecure() {
		return originalRequest.isSecure();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return originalRequest.getRequestDispatcher(path);
	}

	@Override
	@SuppressWarnings("deprecation")
	public String getRealPath(String path) {
		return originalRequest.getRealPath(path);
	}

	@Override
	public int getRemotePort() {
		return originalRequest.getRemotePort();
	}

	@Override
	public String getLocalName() {
		return originalRequest.getLocalName();
	}

	@Override
	public String getLocalAddr() {
		return originalRequest.getLocalAddr();
	}

	@Override
	public int getLocalPort() {
		return originalRequest.getLocalPort();
	}

	@Override
	public ServletContext getServletContext() {
		return originalRequest.getServletContext();
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return originalRequest.getAsyncContext();
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
			throws IllegalStateException {
		return originalRequest.startAsync(servletRequest, servletResponse);
	}

	@Override
	public boolean isAsyncStarted() {
		return originalRequest.isAsyncStarted();
	}

	@Override
	public boolean isAsyncSupported() {
		return originalRequest.isAsyncSupported();
	}

	@Override
	public AsyncContext getAsyncContext() {
		return originalRequest.getAsyncContext();
	}

	@Override
	public DispatcherType getDispatcherType() {
		return originalRequest.getDispatcherType();
	}

	@Override
	public String getAuthType() {
		return originalRequest.getAuthType();
	}

	@Override
	public Cookie[] getCookies() {
		return originalRequest.getCookies();
	}

	@Override
	public long getDateHeader(String name) {
		return originalRequest.getDateHeader(name);
	}

	@Override
	public String getHeader(String name) {
		return originalRequest.getHeader(name);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		return originalRequest.getHeaders(name);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return originalRequest.getHeaderNames();
	}

	@Override
	public int getIntHeader(String name) {
		return originalRequest.getIntHeader(name);
	}

	@Override
	public String getMethod() {
		return originalRequest.getMethod();
	}

	@Override
	public String getPathInfo() {
		return originalRequest.getPathInfo();
	}

	@Override
	public String getPathTranslated() {
		return originalRequest.getPathTranslated();
	}

	@Override
	public String getContextPath() {
		return originalRequest.getContextPath();
	}

	@Override
	public String getQueryString() {
		//TODO: to check
		Object[] toBuilds = parameters.entrySet().stream()
						.map(p -> StringUtils.join(p.getKey(), "=", p.getValue()[0]))
						.toArray();
		if(toBuilds.length > 0){
		return StringUtils.join(toBuilds, "&");
		}
		return "";
//		return originalRequest.getQueryString();
	}

	@Override
	public String getRemoteUser() {
		return originalRequest.getRemoteUser();
	}

	@Override
	public boolean isUserInRole(String role) {
		return originalRequest.isUserInRole(role);
	}

	@Override
	public Principal getUserPrincipal() {
		return originalRequest.getUserPrincipal();
	}

	@Override
	public String getRequestedSessionId() {
		return originalRequest.getRequestedSessionId();
	}

	@Override
	public String getRequestURI() {
		return originalRequest.getRequestURI();
	}

	@Override
	public StringBuffer getRequestURL() {
		return originalRequest.getRequestURL();
	}

	@Override
	public String getServletPath() {
		return originalRequest.getServletPath();
	}

	@Override
	public HttpSession getSession(boolean create) {
		return originalRequest.getSession(create);
	}

	@Override
	public HttpSession getSession() {
		return originalRequest.getSession();
	}

	@Override
	public String changeSessionId() {
		return originalRequest.changeSessionId();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return originalRequest.isRequestedSessionIdValid();
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return originalRequest.isRequestedSessionIdFromCookie();
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return originalRequest.isRequestedSessionIdFromURL();
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isRequestedSessionIdFromUrl() {
		return originalRequest.isRequestedSessionIdFromUrl();
	}

	@Override
	public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
		return originalRequest.authenticate(response);
	}

	@Override
	public void login(String username, String password) throws ServletException {
		originalRequest.login(username, password);
	}

	@Override
	public void logout() throws ServletException {
		originalRequest.logout();
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return originalRequest.getParts();
	}

	@Override
	public Part getPart(String name) throws IOException, ServletException {
		return originalRequest.getPart(name);
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
		return originalRequest.upgrade(handlerClass);
	}

}
