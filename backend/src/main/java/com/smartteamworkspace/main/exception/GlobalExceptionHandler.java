package com.smartteamworkspace.main.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.smartteamworkspace.main.response.ResponseStructure;

@RestControllerAdvice
public class GlobalExceptionHandler {
 
	private ResponseEntity<ResponseStructure<String>> buildResponse(String message,String data,HttpStatus status){
		ResponseStructure<String> response= new ResponseStructure<>();
		response.setData(data);
		response.setStatusCode(status.value());
		response.setMessage(message);
		
		return new ResponseEntity<ResponseStructure<String>>(response,status);
	}
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> resourceNotFoundExceptionHandler(ResourceNotFoundException e){
		return buildResponse(e.getMessage(), "ResourceNotFoundException Occurred", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ResponseStructure<String>> invalidRequestExceptionHandler(InvalidRequestException e){
		 return buildResponse(e.getMessage(), "InvalidRequestException Occurred", HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ResponseStructure<String>> accessDeniedExceptionHandler(AccessDeniedException e){
		return buildResponse(e.getMessage(), "AccessDeniedException Occurred", HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ResponseStructure<String>> conflictExceptionHandler(ConflictException e){
		return buildResponse(e.getMessage(), "ConflictException Occurred", HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(FileStorageException.class)
	public ResponseEntity<ResponseStructure<String>> fileStorageExceptionHandler(FileStorageException e){
		return buildResponse(e.getMessage(), "FileStorageException Occurred", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ResponseStructure<String>> unauthorizedExceptionHandler(UnauthorizedException e){
		return buildResponse(e.getMessage(), "UnauthorizedException Occurred", HttpStatus.UNAUTHORIZED);
	}
}   
