package Loghme.controllers.DTOs;

public class Token {
    private String jwt;

    public Token(String _jwt) {
        jwt = _jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
