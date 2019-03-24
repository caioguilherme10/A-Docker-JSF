/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.a.docker.jsf;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author 
 */
@FacesConverter(value = "converter.Date", forClass = LocalDate.class)
public class DateConverter implements Converter {

    private final DateTimeFormatter formmater = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) return null;

        return LocalDate.parse(value, formmater);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return new String();

        return formmater.format((LocalDate) value);
    }

}
