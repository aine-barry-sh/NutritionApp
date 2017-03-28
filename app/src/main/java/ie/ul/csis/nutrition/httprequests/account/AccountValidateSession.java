package ie.ul.csis.nutrition.httprequests.account;

import api.config.Config;
import api.config.ConfigReader;
import api.httprequests.GetRequest;

public class AccountValidateSession extends GetRequest {

	public AccountValidateSession(String token) throws  Exception
	{
		super(ConfigReader.getInstance().getElementAsString(Config.CHARSET_UTF8.getValue()),
				ConfigReader.getInstance().getElementAsString(Config.CONTENT_TYPE_TEXT_HTML.getValue()),
				ConfigReader.getInstance().getElementAsString(Config.ACCOUNT_VALIDATE_SESSION.getValue()),
				ConfigReader.getInstance().getElementAsInt(Config.ACCOUNT_VALIDATE_SESSION_TIMEOUT.getValue())
				);
		
		this.token = token;
	}
}
