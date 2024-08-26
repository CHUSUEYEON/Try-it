package Try_it.security;

import Try_it.config.jwt.JwtProperties;
import Try_it.user.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
public class TokenProvider {
    @Autowired
    private JwtProperties jwtProperties;

    public String create(UserEntity user){
        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
            .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey())
            .setSubject(String.valueOf(user.getUserPk()))
            .setIssuer(jwtProperties.getIssuer())
            .setExpiration(expiredDate)
            .setIssuedAt(new Date())
            .compact();
    }

    //입력된 token 에서 payload 에 있는 userPk 뽑기
    public String validateAndGetUserPk(String token){
        Claims claims = Jwts.parser()
            .setSigningKey(jwtProperties.getSecretKey())
            .parseClaimsJws(token)// 토큰이 위조되지 않았다면 payload 를 return
            .getBody();

        return claims.getSubject();
    }
}
