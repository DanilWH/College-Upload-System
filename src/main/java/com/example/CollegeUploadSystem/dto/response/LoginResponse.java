package com.example.CollegeUploadSystem.dto.response;

public class LoginResponse {
    private Long id;
    private String login;
    private String accessJws;
    private String refreshJws;
    private Long accessJwsExpirationMs;
    private Long refreshJwsExpirationMs;

    public Long getId() {
        return id;
    }

    public LoginResponse setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public LoginResponse setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getAccessJws() {
        return accessJws;
    }

    public LoginResponse setAccessJws(String accessJws) {
        this.accessJws = accessJws;
        return this;
    }

    public String getRefreshJws() {
        return refreshJws;
    }

    public LoginResponse setRefreshJws(String refreshJws) {
        this.refreshJws = refreshJws;
        return this;
    }

    public Long getAccessJwsExpirationMs() {
        return accessJwsExpirationMs;
    }

    public LoginResponse setAccessJwsExpirationMs(Long accessJwsExpirationMs) {
        this.accessJwsExpirationMs = accessJwsExpirationMs;
        return this;
    }

    public Long getRefreshJwsExpirationMs() {
        return refreshJwsExpirationMs;
    }

    public LoginResponse setRefreshJwsExpirationMs(Long refreshJwsExpirationMs) {
        this.refreshJwsExpirationMs = refreshJwsExpirationMs;
        return this;
    }
}
