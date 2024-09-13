package com.momand.docauthor.document.dto;

import jakarta.validation.constraints.NotNull;


public class DocumentDTOBasicNoRef {
    //private Long id;
    @NotNull(message = "Title is required for a Document.")
    private String title;
    @NotNull(message = "Body is required for a Document.")
    private String body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }
}
