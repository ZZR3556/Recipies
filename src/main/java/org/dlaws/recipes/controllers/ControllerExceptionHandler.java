package org.dlaws.recipes.controllers;

import lombok.extern.slf4j.Slf4j;

import org.dlaws.recipes.exceptions.BadRequestException;
import org.dlaws.recipes.exceptions.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import static org.dlaws.recipes.Statics.*;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler
{
    @ResponseStatus( HttpStatus.NOT_FOUND )
    @ExceptionHandler( NotFoundException.class )
    public ModelAndView handleNotFound(Exception e )
    {
        log.error("Handling not found exception. ( " + e.getMessage() + " )");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("error");
        modelAndView.addObject( MODEL_ATTRIBUTE_TITLE_MESSAGE,"404 Not Found" );
        modelAndView.addObject( MODEL_ATTRIBUTE_EXCEPTION_MESSAGE, e.getMessage() );

        return modelAndView;
    }

    @ResponseStatus( HttpStatus.BAD_REQUEST )
    @ExceptionHandler( BadRequestException.class )
    public ModelAndView handleBadRequestException( Exception e )
    {
        log.error("Handling not found exception. ( " + e.getMessage() + " )");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("error");
        modelAndView.addObject( MODEL_ATTRIBUTE_TITLE_MESSAGE,"400 Bad Request" );
        modelAndView.addObject( MODEL_ATTRIBUTE_EXCEPTION_MESSAGE, e.getMessage() );

        return modelAndView;
    }

}
