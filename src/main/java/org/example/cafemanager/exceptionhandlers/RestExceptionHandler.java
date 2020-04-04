package org.example.cafemanager.exceptionhandlers;

import org.example.cafemanager.dto.ResponseModel;
import org.example.cafemanager.services.exceptions.ChooseAtLeastOneException;
import org.example.cafemanager.services.exceptions.InstanceNotFoundException;
import org.example.cafemanager.services.exceptions.MustBeUniqueException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@ControllerAdvice
public class RestExceptionHandler extends ExceptionHandlerExceptionResolver {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    ResponseModel handleIllegalArgumentException(IllegalArgumentException e) {
        return createResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(InstanceNotFoundException.class)
    @ResponseBody
    ResponseModel handleInstanceNotFoundException(InstanceNotFoundException e) {
        return createResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MustBeUniqueException.class)
    @ResponseBody
    ResponseModel handleInstanceMustBeUniqueException(MustBeUniqueException e) {
        return createResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ChooseAtLeastOneException.class)
    @ResponseBody
    ResponseModel handleInstanceChooseAtLeastOneException(ChooseAtLeastOneException e) {
        return createResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseModel handleInstanceException() {
        return createResponse("Something goes wrong! Try again later");
    }

    private ResponseModel createResponse(String mess) {
        ResponseModel result = new ResponseModel();
        result.setMessage(mess);
        return result;
    }
}
