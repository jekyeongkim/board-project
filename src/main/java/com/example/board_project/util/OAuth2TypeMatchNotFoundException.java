package com.example.board_project.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "OAuth2 타입이 일치하지 않습니다.")
public class OAuth2TypeMatchNotFoundException extends RuntimeException {
}
