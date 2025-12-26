package com.sfl.core.web.rest.errors;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends  GlobalException{
   public UnauthorizedException(String message){
       super(message,401,HttpStatus.UNAUTHORIZED);
   }
}
