package ie.ul.csis.nutrition.httprequests.account;

import api.config.Config;
import api.config.ConfigReader;
import api.httprequests.PostRequest;

public class AccountRegister extends PostRequest {

	public AccountRegister() throws Exception
	{
		super(ConfigReader.getInstance().getElementAsString(Config.CHARSET_UTF8.getValue()),
				ConfigReader.getInstance().getElementAsString(Config.CONTENT_TYPE_APPLICATION_JSON.getValue()),
				ConfigReader.getInstance().getElementAsString(Config.ACCOUNT_REGISTER.getValue()),
				ConfigReader.getInstance().getElementAsInt(Config.ACCOUNT_REGISTER_TIMEOUT.getValue())
				);
		this.expectedResponceCode = 201;
	}
}
