package com.sg.pcrf.statistic.client;

import java.io.IOException;

import javax.net.ssl.SSLContext;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	private static final long MAX_CONTENT_LENGTH_TO_CONSUME = 5000000;

	public static void releaseConnection(HttpEntity respEntity) {
		if (respEntity != null) {
			try {
				if (respEntity.getContentLength() > MAX_CONTENT_LENGTH_TO_CONSUME) {
					respEntity.getContent().close();
				} else {
					EntityUtils.consume(respEntity);
				}
			} catch (IOException ignore) {
			} catch (UnsupportedOperationException ignore) {
			} catch (IllegalStateException ignore) {
			} catch (Exception e) {
			}
		}
	}

	public static HttpClient createConnPoolClientReuse(int commTimeout, int connPerRoute, int connTotal,
			boolean https) {
		SSLConnectionSocketFactory sslsf = null;
		try {
			if (https) {
				SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy())
						.build();
				String[] tls = new String[1]
				tls[0]="TLSv1"
				sslsf = new SSLConnectionSocketFactory(sslcontext, tls, null,
						SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			}
		} catch (Exception e) {
		}

		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(commTimeout)
				.setConnectTimeout(commTimeout).setConnectionRequestTimeout(commTimeout)
				.setStaleConnectionCheckEnabled(false).build();

		ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy() {
			public long getKeepAliveDuration(HttpResponse response, HttpContext arg1) {
				HeaderElementIterator it = new BasicHeaderElementIterator(
						response.headerIterator(HTTP.CONN_KEEP_ALIVE));

				while (it.hasNext()) {
					HeaderElement he = it.nextElement();
					String param = he.getName();
					String value = he.getValue();
					if (value != null && param.equalsIgnoreCase("timeout")) {
						try {
							return Long.parseLong(value) * 1000;
						} catch (NumberFormatException ignore) {
						}
					}
				}

				return 5000;
			}

		};

		HttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig)
				.setMaxConnTotal(connTotal).setKeepAliveStrategy(keepAliveStrategy).setMaxConnPerRoute(connPerRoute)
				.setSSLSocketFactory(sslsf).build();

		return httpclient;
	}
}
