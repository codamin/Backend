package Loghme.Utilities;


import Loghme.database.dataMappers.user.UserMapper;
import Loghme.entities.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.mysql.cj.jdbc.SuspendableXAConnection;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;

public class JwtUtils {
    private static String SECRET_KEY = "loghme";
    private static long expirePreiod = 24*60*60*1000;

    private static Date expirationDate() {
        long curTime = System.currentTimeMillis();
        return new Date(curTime + expirePreiod);
    }

    public static String createJWT(String userMail) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        
        JwtBuilder builder = Jwts.builder();
        builder.setIssuer(userMail);
        builder.setIssuedAt(new Date(System.currentTimeMillis()));
        builder.setExpiration(expirationDate());
        builder.signWith(signatureAlgorithm, signingKey);
        
        return builder.compact();
    }

    public static String verifyJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        if (claims.getExpiration().getTime() < System.currentTimeMillis())
            return null;
        return claims.getIssuer();
    }

    public static String verifyGoogleTokenId(String tokenIdString) throws GeneralSecurityException, IOException, SQLException {
        String CLIENT_ID = "805487349717-belcub0d2g4mrq6mq9dn8sjddf0fhqh6.apps.googleusercontent.com";
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(tokenIdString);
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            User found_User = UserMapper.getInstance().find(email);
            if(found_User!= null)
                return email;
            else
                return null;
        } else {
            System.out.println("Invalid ID token.");
            return null;
        }
    }
}