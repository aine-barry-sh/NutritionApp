package ie.ul.csis.nutrition.httprequests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

import api.dto.Dto;

public abstract class PostRequest extends Request {

	public PostRequest(String charset, String contentType, String urlAddress, int timeout) throws Exception
	{
		super(charset, contentType, urlAddress, timeout);
	}
	
	public void request(Dto dto)
	{
		try
		{
			URL url = new URL(this.address);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(this.timeout);
			connection.setRequestMethod("POST");
			
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			
			connection.setRequestProperty("Content-Type", this.contentType);
			connection.setRequestProperty("charset", this.charset);
			
			if(this.token != null)
			{
				  connection.setRequestProperty("authorization","bearer " + this.token);
			}
			
			
			OutputStream outputStream = connection.getOutputStream();
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, this.charset);
			
			if(dto != null)
				sendDto(outputStreamWriter, dto);
			
			outputStreamWriter.flush();
			outputStreamWriter.close();
			
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
	
	protected void sendDto(OutputStreamWriter outputStreamWriter, Dto dto) throws IOException
	{
		outputStreamWriter.write(dto.toString());
		outputStreamWriter.flush();
	}
	
	
}
