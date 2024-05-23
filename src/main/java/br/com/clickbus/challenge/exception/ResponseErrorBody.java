package br.com.clickbus.challenge.exception;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseErrorBody {
    private Date timestamp = new Date();
    private String status = "error";
    private int statusCode = 400;
    private String error;
}
