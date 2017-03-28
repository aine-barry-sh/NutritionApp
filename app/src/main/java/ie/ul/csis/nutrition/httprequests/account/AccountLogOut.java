package ie.ul.csis.nutrition.httprequests.account;

import api.config.Config;
import api.config.ConfigReader;
import api.httprequests.PostRequest;


public class AccountLogOut extends PostRequest{

	
	public AccountLogOut(String token) throws Exception {
		super(ConfigReader.getInstance().getElementAsString(Config.CHARSET_UTF8.getValue()), 
				ConfigReader.getInstance().getElementAsString(Config.CONTENT_TYPE_TEXT_HTML.getValue()),
				ConfigReader.getInstance().getElementAsString(Config.ACCOUNT_LOGOUT.getValue()), 
				ConfigReader.getInstance().getElementAsInt(Config.ACCOUNT_LOGOUT_TIMEOUT.getValue())
				);
		
		this.token = token;
	}
	
}
