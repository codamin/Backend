package Loghme.Utilities;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JwtUtils {
    private static String SECRET_KEY = "loghme";
    private static long expirePreiod = 24*60*60*1000;

    public static String createJWT(String userMail) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long curTime = System.currentTimeMillis();
        Date now = new Date(curTime);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        
        JwtBuilder builder = Jwts.builder();
        builder.setIssuedAt(now);
        builder.setExpiration(new Date(now.getTime()+expirePreiod));
        builder.setIssuer(userMail);
        builder.signWith(signatureAlgorithm, signingKey);
        return builder.compact();
    }

    public static String verifyJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return claims.getIssuer();
    }
}