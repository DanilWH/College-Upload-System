package com.example.CollegeUploadSystem.dto.output;

public class AuthOutput {
    private Long id;
    private String login;
    private String accessJws;
    private String refreshJws;
    private Long accessJwsExpirationMs;
    private Long refreshJwsExpirationMs;

    public Long getId() {
        return id;
    }

    public AuthOutput setId(Long id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public AuthOutput setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getAccessJws() {
        return accessJws;
    }

    public AuthOutput setAccessJws(String accessJws) {
        this.accessJws = accessJws;
        return this;
    }

    public String getRefreshJws() {
        return refreshJws;
    }

    public AuthOutput setRefreshJws(String refreshJws) {
        this.refreshJws = refreshJws;
        return this;
    }

    public Long getAccessJwsExpirationMs() {
        return accessJwsExpirationMs;
    }

    public AuthOutput setAccessJwsExpirationMs(Long accessJwsExpirationMs) {
        this.accessJwsExpirationMs = accessJwsExpirationMs;
        return this;
    }

    public Long getRefreshJwsExpirationMs() {
        return refreshJwsExpirationMs;
    }

    public AuthOutput setRefreshJwsExpirationMs(Long refreshJwsExpirationMs) {
        this.refreshJwsExpirationMs = refreshJwsExpirationMs;
        return this;
    }
}
