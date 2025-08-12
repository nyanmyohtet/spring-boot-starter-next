package com.nyanmyohtet.springbootstarter.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.Jwts.SIG.RS256;

@Component
public class JwtUtil {
    private final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.clientId}")
    private String clientId;

    @Value("${jwt.userId}")
    private String userId;

    @Value("${jwt.aud}")
    private String audience;

    @Value("${jwt.validity}")
    private long validity;

    @Value("${jwt.scope}")
    private String scope;

    @Value("${jwt.privateKey}")
    private Resource privateKeyResource;

    private RSAPrivateKey privateKey;

    @PostConstruct
    public void init() throws Exception {
        try (InputStream is = privateKeyResource.getInputStream()) {
            String key = new String(is.readAllBytes())
                    .replaceAll("-----\\w+ PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Decoders.BASE64.decode(key);
//            byte[] decoded = Base64.getMimeDecoder().decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
            LOGGER.info("this.privateKey: {}", this.privateKey.getAlgorithm());
        }
    }

    public String generateJwtAssertion() {
        LOGGER.info("scope: {}", scope);
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(validity); // 1 hour validity

        return Jwts.builder()
//                .header().add("typ", "JWT").and() // Optional, JJWT includes it by default
                .issuer(clientId)
                .subject(userId)
                .audience().add(audience).and()
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claim("scope", scope)
                .signWith(privateKey, RS256)
                .compact();
    }

//    public String generateJwtAssertion() {
//        Instant now = Instant.now();
//        Instant exp = now.plusSeconds(3600); // 1 hour validity
//
//        return Jwts.builder()
//                .setHeaderParam("typ", "JWT") // Optional, JJWT includes it by default
//                .setIssuer(issuer)
//                .setSubject(subject)
//                .setAudience(audience)
//                .setIssuedAt(Date.from(now))
//                .setExpiration(Date.from(exp))
//                .claim("scope", scope)
//                .signWith(privateKey, SignatureAlgorithm.RS256)
//                .compact();
//    }
}

