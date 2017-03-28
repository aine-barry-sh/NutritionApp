package ie.ul.csis.nutrition.httprequests.account;

import api.config.Config;
import api.config.ConfigReader;
import api.httprequests.PostRequest;

public class AccountToken extends PostRequest {

	public AccountToken() throws Exception
	{
		
		super(ConfigReader.getInstance().getElementAsString(Config.CHARSET_UTF8.getValue()),
				ConfigReader.getInstance().getElementAsString(Config.CONTENT_TYPE_TEXT_HTML.getValue()),
				ConfigReader.getInstance().getElementAsString(Config.ACCOUNT_LOGIN.getValue()),
				ConfigReader.getInstance().getElementAsInt(Config.ACCOUNT_LOGIN_TIMEOUT.getValue())
				);
		
	}
}
