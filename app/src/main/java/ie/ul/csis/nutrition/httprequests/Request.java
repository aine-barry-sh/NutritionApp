package ie.ul.csis.nutrition.httprequests;


public class Request {
	
	protected int responseCode;
	protected String responseMessage;
	protected String token;
	protected String charset;
	protected String contentType;
	protected String address;
	protected int timeout;
	protected int expectedResponceCode;
	
	public Request(String charset, String contentType, String urlAddress, int timeout) throws Exception
	{
		this.charset = charset;
		this.contentType = contentType;
		this.address = urlAddress;
		this.timeout = timeout;
		this.token = null;
		this.responseCode = 0;
		this.responseMessage = null;
		this.expectedResponceCode = 200;
	}
	
	public void setToken(String token)
	{
		this.token = token;
	}
	
	public int getResponseCode()
	{
		return responseCode;
	}
	
	public String getResponseMessage()
	{
		return responseMessage;
	}
	
	public void setExpectedResponceCode(int expectedResponceCode) {
		this.expectedResponceCode = expectedResponceCode;
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getToken() {
		return token;
	}

	public int getExpectedResponceCode() {
		return expectedResponceCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

}
