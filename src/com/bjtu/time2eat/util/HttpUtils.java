package com.bjtu.time2eat.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	private static DefaultHttpClient httpclient;
	private static final String host = "http://60.247.57.229:12088/";
	static {
		httpclient = new DefaultHttpClient();
		// HttpParams params = new BasicHttpParams();
		// PoolingClientConnectionManager cm = new
		// PoolingClientConnectionManager();
		// cm.setMaxTotal(4000);// 设置默认的最大连接数为4000
		// cm.setDefaultMaxPerRoute(400);// 设置每个路由最大连接数为400
		// params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
		// 30000);// 连接超时时间30秒
		// params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);//
		// 读取数据超时时间60秒
		// httpclient = new DefaultHttpClient(cm, params);
		// httpclient.setHttpRequestRetryHandler(new HttpRequestRetryHandler() {
		// public boolean retryRequest(IOException exception, int
		// executionCount, HttpContext context) {
		// if (executionCount > 3) {
		// return false;
		// }
		// if (exception instanceof org.apache.http.NoHttpResponseException) {
		// return true;
		// }
		// return false;
		// }
		// });
	}

	public static String fetchResponseByGet(String action,
			Map<String, Object> params, String charset)
			throws UnsupportedEncodingException {

		HttpResponse response = null;
		byte[] content = null;
		try {
			Set<String> keys = params.keySet();
			StringBuilder uri = new StringBuilder();
			for (String key : keys) {
				uri.append("&").append(key).append("=")
						.append((String) (params.get(key)));
			}

			HttpGet get = new HttpGet(host + action + "?" + uri.toString());
			response = httpclient.execute(get);
			int code = response.getStatusLine().getStatusCode();
			if (code == HttpStatus.SC_OK) {
				content = EntityUtils.toByteArray(response.getEntity());
			} else {
				throw new IOException();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				response.getEntity().getContent().close();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return content == null ? null : new String(content, "UTF-8");
	}

	/**
	 * 简单的http post请求
	 * 
	 * @param url
	 * @param params
	 *            post参数，要求参数值都是字符串类型
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String fetchResponseByPostWithUrlEncodedFormEntity(
			String action, Map<String, Object> params, String charset)
			throws ClientProtocolException, IOException {
		HttpResponse response = null;
		byte[] content = null;
		try {
			charset = StringUtils.isNotBlank(charset) ? charset : "UTF-8";
			HttpPost post = new HttpPost(host + action);
			List<NameValuePair> list = new LinkedList<NameValuePair>();

			if (params != null && !params.isEmpty()) {
				Set<String> keys = params.keySet();
				for (String key : keys) {
					list.add(new BasicNameValuePair(key, (String) params
							.get(key)));
				}
			}
			StringEntity entity = new UrlEncodedFormEntity(list, charset);
			post.setEntity(entity);
			response = httpclient.execute(post);
			int code = response.getStatusLine().getStatusCode();
			if (code == HttpStatus.SC_OK) {
				content = EntityUtils.toByteArray(response.getEntity());
			} else {
				throw new IOException();
			}
		} finally {
			response.getEntity().getContent().close();
		}

		return content == null ? null : new String(content, "UTF-8");
	}

}
