package com.gateway.server.component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.GeneralSecurityException;

import javax.naming.AuthenticationException;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gateway.server.exception.ResponseCode;
import com.gateway.server.exception.UtilityException;
import com.gateway.server.security.EdCipher;
import com.gateway.server.security.EdCipherData;
import com.gateway.server.security.EncryptionRSA;
import com.gateway.server.utils.CommonUtility;
import com.gateway.server.utils.Constants;

public class ServletRequestWrapper extends HttpServletRequestWrapper {

	private final String payload;

	private static final Logger logger = LoggerFactory.getLogger(ServletRequestWrapper.class);

	private String encryptedKey = null;

	public ServletRequestWrapper(HttpServletRequest request) throws AuthenticationException, JSONException, IOException, GeneralSecurityException {
		super(request);
		StringBuilder stringBuilder = new StringBuilder();
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				StringWriter stringWriter = new StringWriter();
				IOUtils.copy(inputStream, stringWriter, "UTF-8");
				stringBuilder.append(stringWriter.toString());
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw new UtilityException(ResponseCode.ERROR_DECRYPTING_DATA);
		}

		String decrytedData = "";

		if (CommonUtility.isValidString(stringBuilder.toString())) {
			logger.info("Request from client before modification Request data  ::"+stringBuilder.toString());
			JSONObject jsonObject = new JSONObject(stringBuilder.toString());
			encryptedKey = jsonObject.getString(Constants.Headers.KEY);
			String encryptedData = jsonObject.getString(Constants.Headers.DATA);

			if(!CommonUtility.isValidString(encryptedKey)||!CommonUtility.isValidString(encryptedData)){
				throw new UtilityException(ResponseCode.INVALID_PARAMETER);
			}

			decrytedData = dataDecrypt(Base64.decodeBase64(encryptedData), encryptedKey);
		}

		logger.info("Request modified...:: " + decrytedData);
		payload = decrytedData;
	}

	public String encryptedKey() {
		return encryptedKey;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(payload.getBytes("UTF-8"));
		ServletInputStream inputStream = new ServletInputStream() {
			public int read()
					throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener arg0) {
			}
		};
		return inputStream;
	}

	public String getPayload() {
		return payload;
	}

	public String dataDecrypt(byte[] bs, String encryptedKey) throws UtilityException, IOException, GeneralSecurityException {
		logger.info("data decrypt method entered...");
		EncryptionRSA encryptionRSA = new EncryptionRSA();
		EdCipher edCipher = new EdCipher();

		String secretKeyDecrypted = encryptionRSA.decrypt(encryptedKey);
		logger.info("secret Key after Decryption :: "+ secretKeyDecrypted);
		String[] secretKey = secretKeyDecrypted.split(Constants.APP.APP_NAME);
		if (secretKey.length < 2) {
			logger.error("Secret key could not be split secret key :: "+secretKey);
			throw new UtilityException(ResponseCode.SYSTEM_ERROR);
		}
		EdCipherData edCipherData = new EdCipherData();
		edCipherData.setData(bs);
		edCipherData.setPassKey(secretKey[0]);
		edCipherData.setSalt(Base64.decodeBase64(secretKey[1]));
		edCipherData.setIv(Base64.decodeBase64(secretKey[2]));
		logger.info("data decrypt method exiting...");
		return edCipher.decrypt(edCipherData);
	}
}