package com.sg.pcrf.statistic.client

import com.sg.pcrf.statistic.util.Constants
import com.sg.pcrf.statistic.util.Utils
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity

public class Client {
	public static Properties properties = null;
	public static HttpClient httpClient = null;
	public static RequestConfig requestConfig = null;
	static {
		properties = Utils.loadProperties(Constants.pccCfg);
		httpClient = HttpUtil.createConnPoolClientReuse(
				Integer.valueOf(properties.getProperty("COMMUNICATION_TIMEOUT")),
				Integer.valueOf(properties.getProperty("HTTP_MAX_THREADS")), 1000, false);
		requestConfig = RequestConfig.custom()
				.setSocketTimeout(Integer.valueOf(properties.getProperty("COMMUNICATION_TIMEOUT")))
				.setConnectTimeout(Integer.valueOf(properties.getProperty("COMMUNICATION_TIMEOUT")))
				.setConnectionRequestTimeout(Integer.valueOf(properties.getProperty("COMMUNICATION_TIMEOUT")))
				.setRedirectsEnabled(false).build();
	}

	public static boolean sentMsg(String message) {

		boolean status = true;
		HttpPost post = new HttpPost(properties.getProperty("URL"));
		post.setConfig(requestConfig);

		HttpEntity respEntity = null;
		HttpEntity entity = new StringEntity(message, ContentType.create("text/xml", "UTF-8"));
		post.setEntity(entity);
		// post.addHeader(SpmlCommUtil.HEADER_ATTRIBUTE_SOAP_ACTION,
		// SpmlCommUtil.EMPTY_SOAP_ACTION);

		HttpResponse resp = null;
		try {
			resp = httpClient.execute(post);
			int result = resp.getStatusLine().getStatusCode();
			respEntity = resp.getEntity();
			if (!responseCodeOK(result)) {
				status = false;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			status = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			status = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			status = false;
		} finally {
			HttpUtil.releaseConnection(respEntity);
		}
		// String resultString = EntityUtils.toString(respEntity);

		return status;
	}

	public static boolean responseCodeOK(int responseCode) {
		/* 2XX: generally "OK" */
		/* 3XX: relocation/redirect */
		/* 4XX: client error */
		/* 5XX: server error */
		if (responseCode >= 200 && responseCode < 300) {
			return true;
		}
		return false;
	}
}
