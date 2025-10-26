package org.example.java.xmlbatch;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateAdapterDTO extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String s){
        return s==null ? null : LocalDate.parse(s);
    }

    @Override
    public String marshal(LocalDate localDate){
        return localDate==null ? null : localDate.toString();
    }
}