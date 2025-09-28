package com.example.utility;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class CookieHelper {
	public static void setCookie(String name, String value, Duration duration, HttpServletResponse response) {
		ResponseCookie accessCookie = ResponseCookie.from(name, value).httpOnly(true).secure(true).path("/")
				.sameSite("Strict").maxAge(duration).build();
		response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
	}

	public static String getValue(HttpServletRequest request, String name) {
		List<Cookie> cookies = Optional.ofNullable(request.getCookies()).map(Arrays::asList)
				.orElse(Collections.emptyList());
		String token = cookies.stream().filter(x -> x.getName().equals(name)).map(Cookie::getValue).findFirst()
				.orElse(null);
		return token;
	}
}
