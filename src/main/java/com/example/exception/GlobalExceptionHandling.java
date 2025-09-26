package com.example.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.dto.Result;

@ControllerAdvice
public class GlobalExceptionHandling {
	private HashMap<String, List<String>> addMap(HashMap<String, List<String>> map,String key,String message){
		if(!map.containsKey(key)) {
			map.put(key, Arrays.asList(message));
		}else {
		   List<String> current=map.get(key);
		   current = new ArrayList<>(current);
		   current.add(message);
		   map.put(key, current);
		}
		return map;
	}
	
		
@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
	HashMap<String, List<String>> map=new HashMap<>();
		List<ObjectError> errors=ex.getBindingResult().getAllErrors();
		for(ObjectError error:errors) {
		  map=addMap(map, ((FieldError)error).getField(), error.getDefaultMessage());
		}
		Result<HashMap<String, List<String>>> result=Result.notValid(map);
		return ResponseEntity.status(result.getStatus()).body(result);
	}
   

    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity<Object> handleDatabaseException(DataAccessException ex) {
    	Result<String> result=Result.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    	return ResponseEntity.badRequest().body(result);
    }
    
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
    	Result<String> result=Result.error(HttpStatus.NOT_FOUND, ex.getMessage());
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }
    
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex){
    	Result<String> result=Result.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    	return ResponseEntity.internalServerError().body(result);
    }
    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<Object> handleAuthException(Exception ex){
    	Result<String> result=Result.error(HttpStatus.UNAUTHORIZED, ex.getMessage());
    	return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(result);
    }
}
