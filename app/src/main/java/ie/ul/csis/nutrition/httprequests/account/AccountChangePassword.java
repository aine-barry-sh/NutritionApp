package ie.ul.csis.nutrition.httprequests.account;

import api.config.Config;
import api.config.ConfigReader;
import api.httprequests.PostRequest;

public class AccountChangePassword extends PostRequest {

	public AccountChangePassword() throws Exception
	{
		super(ConfigReader.getInstance().getElementAsString(Config.CHARSET_UTF8.getValue()), 
				ConfigReader.getInstance().getElementAsString(Config.CONTENT_TYPE_APPLICATION_JSON.getValue()),
				ConfigReader.getInstance().getElementAsString(Config.ACCOUNT_CHANGE_PASSWORD.getValue()), 
				ConfigReader.getInstance().getElementAsInt(Config.ACCOUNT_CHANGE_PASSWORD_TIMEOUT.getValue())
				);
	}
	
}
