package Loghme.Utilities;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Collections;
import java.util.Date;

import static com.google.api.client.googleapis.util.Utils.getDefaultJsonFactory;


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

    public static String verifyTokenId(String tokenIdString) throws GeneralSecurityException, IOException {
        System.out.println("start verify token id");
        String CLIENT_ID = "805487349717-mup8qor9qlha42ooq5v45g0nols9g1s4.apps.googleusercontent.com";
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), getDefaultJsonFactory())
            //Specify the CLIENT_ID of the app that accesses the backend:
        .setAudience(Collections.singletonList(CLIENT_ID))
            // Or, if multiple clients access the backend:
            //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
         .build();
        System.out.println("step one");
        // (Receive idTokenString by HTTPS POST)

        GoogleIdToken idToken = verifier.verify(tokenIdString);
        System.out.println("google id token made");
        if (idToken != null) {
            System.out.println("if");
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            // Use or store profile information
            // ...
            System.out.println(email+" "+name+familyName);
            return email;
        } else {
            System.out.println("Invalid ID token.");
            return null;
        }
    }
}