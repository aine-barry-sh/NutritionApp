package ie.ul.csis.nutrition.httprequests;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

import api.dto.Dto;

public class GetRequest extends Request{

	public GetRequest(String charset, String contentType, String urlAddress, int timeout) throws Exception {
		super(charset, contentType, urlAddress, timeout);

	}

	public void request(Dto dto)
	{
		try
		{
			if(dto != null)
			{
				this.address += dto.toString();
			}
			
			URL url = new URL(this.address);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(this.timeout);
			connection.setRequestMethod("GET");
			
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			
			connection.setRequestProperty("Content-Type", this.contentType);
			connection.setRequestProperty("charset", this.charset);
			
			if(this.token != null)
			{
				  connection.setRequestProperty("authorization","bearer " + this.token);
			}
			

			 StringBuilder stringBuilder = new StringBuilder();
	         BufferedReader bufferedReader;
	         
	         this.responseCode = connection.getResponseCode();
	         
				if (this.responseCode == this.expectedResponceCode)
				{
					bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	
				}
				else
				{
					bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				}
		        String line;
	         while ((line = bufferedReader.readLine()) != null) {
	        	 stringBuilder.append(line+"\n");
	         }
	         bufferedReader.close();
	        
	         this.responseMessage = stringBuilder.toString();
		}
		catch(SocketTimeoutException | ConnectException e )
		{
			this.responseCode = 408;
			this.responseMessage = "{\"message\": \"Connection Timed Out\"}";
		} catch (MalformedURLException e) {
			this.responseMessage = "{\"message\": \"MalformedURLException URL\"}";
		} catch (ProtocolException e) {
			this.responseMessage = "{\"message\": \"ProtocolException \"}";
		} catch (IOException e) {
			this.responseMessage = "{\"message\": \"IOException\"}";
			e.printStackTrace();
		}
	}

}
