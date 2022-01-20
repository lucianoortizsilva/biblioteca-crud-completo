package com.lucianoortizsilva.crud.security.authentication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class CachedBodyServletInputStream extends ServletInputStream {

	private InputStream cachedBodyInputStream;

	public CachedBodyServletInputStream(byte[] cachedBody) {
		this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
	}

	@Override
	public boolean isFinished() {
		boolean isFinished = false;
		try {
			isFinished = cachedBodyInputStream.available() == 0;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return isFinished;
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public int read() throws IOException {
		return cachedBodyInputStream.read();
	}

	@Override
	public void setReadListener(ReadListener listener) {
		log.info("ReadListener");
	}

}