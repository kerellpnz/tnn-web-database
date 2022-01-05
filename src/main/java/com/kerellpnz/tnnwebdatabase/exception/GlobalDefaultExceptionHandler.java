package com.kerellpnz.tnnwebdatabase.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handlerNoHandlerFoundException() {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("errorTitle", "Страницы не существует!");
        mv.addObject("errorDescription", "Запрашиваемая Вами страница отсутствует на сервере. " +
                "Не вводите адрес в ручную, пользуйтесь кнопками... Для кого я их делал по Вашему?");
        mv.addObject("title", "Ошибка");
        return mv;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handlerEntityNotFoundException() {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("errorTitle", "Объект не найден!");
        mv.addObject("errorDescription", "Запрашиваемый объект отсутствует в базе данных!");
        mv.addObject("title", "Ошибка");
        return mv;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handlerException(Exception e) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("errorTitle", "Хоба! Ошибка работы приложения!");

        /*FOR DEBUGGING*/
        StringWriter sw = new StringWriter(); //sw in mv.addObject
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        mv.addObject("errorDescription", "Не знаю, что Вы наделали, но такого явно быть не должно. " +
                "Скопируйте сообщение об ошибке, наведите курсор мыши на свою фамилию и нажмите \"Сообщить об ошибке\"," +
                "опишите ваши действия и вставьте скопированное сообщение.");
        mv.addObject("errorMessage", e.getMessage());
        mv.addObject("title", "Ошибка");
        return mv;
    }
}
