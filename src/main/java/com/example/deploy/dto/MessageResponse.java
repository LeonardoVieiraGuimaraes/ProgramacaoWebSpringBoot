package com.example.deploy.dto;

/**
 * 📨 DTO para mensagens de resposta da API
 * 
 * @author Leonardo Vieira Guimarães
 * @since 2025-09-23
 */
public class MessageResponse {

    private String message;
    private boolean success;
    private Object data;

    // 🔧 Construtores
    public MessageResponse() {}

    public MessageResponse(String message) {
        this.message = message;
        this.success = true;
    }

    public MessageResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public MessageResponse(String message, boolean success, Object data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    // 🔧 Factory methods para facilitar criação
    public static MessageResponse success(String message) {
        return new MessageResponse(message, true);
    }

    public static MessageResponse success(String message, Object data) {
        return new MessageResponse(message, true, data);
    }

    public static MessageResponse error(String message) {
        return new MessageResponse(message, false);
    }

    public static MessageResponse error(String message, Object data) {
        return new MessageResponse(message, false, data);
    }

    // 🔧 Getters e Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "message='" + message + '\'' +
                ", success=" + success +
                ", data=" + data +
                '}';
    }
}