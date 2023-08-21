package com.css.autocsfinal.jwt;

import com.css.autocsfinal.exception.TokenException;
import com.css.autocsfinal.member.dto.TokenDTO;
import com.css.autocsfinal.member.entity.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;


@Component
@Slf4j
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";

    private static final String BEARER_TYPE = "Bearer";  // Bearer 토큰 사용시 앞에 붙이는 prefix문자열

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분(ms단위)

    private final Key key;   // java.security.Key로 imort

    private final UserDetailsService userDetailsService;
    public TokenProvider(@Value("${jwt.secret}") String secretKey, UserDetailsService userDetailsService){

        byte[] keyBytest = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytest);
        this.userDetailsService = userDetailsService;
    }


    /* 1. 토큰(xxxxx.yyyyy.zzzzz) 생성 메소드 */
    public TokenDTO generateTokenDTO(Member member){
//        log.info("[TokenProvider] generateTokenDTO Start =============================== ");
//        List<String> roles = new ArrayList<>(); // 멤버의 권한을 추출
//        for(MemberRole memberRole : member.getMemberRole()){
//            roles.add(memberRole.getAuthority().getAuthorityName());
//        }
//
//        log.info("[TokenProvider] authorites   {}", roles);

        /* 1. 회원 아이디를 "sub"이라는 클레임으로 토큰으로 추가 */
        Claims claims = Jwts.claims().setSubject(member.getId());

//        /* 2. 회원의 권한들을 "auth"라는 클레임으로 토큰에 추가 */
//        claims.put(AUTHORITIES_KEY, roles);

        long now = System.currentTimeMillis();   // 현재시간을 밀리세컨드단위로 가져옴

        Date accessTokenExpriesIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME); // java.util.Date로 import
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(accessTokenExpriesIn) // 토큰의 만료기간을 DATE형으로 토큰에 추가(exp라는 클레임으로 long형으로 토큰에 추가)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        log.info("[TokenProvider] generateTokenDTO End =============================== ");

        return new TokenDTO(BEARER_TYPE, member.getId(), accessToken, accessTokenExpriesIn.getTime());
    }

    /* 2. 토큰의 등록된 클레임의 subject에서 해당 회원의 아이디를 추출 */
    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()          // payload의 Clamis 추출
                .getSubject();      // Claim중에 등록 클레임에 해당하는 sub값 추출(회원 아이디)
    }
    /* 3. AccessToken으로 인증 객체 추출 */
    public Authentication getAuthentication(String token){

        log.info("[TokenProvider] getAuthentication Start =============================== ");
        /* 토큰에서 claim들을 추출(토큰 복호화) */
        Claims claims = parseClaims(token);

        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        /* 클레임에서 권한 정보 가져오기 */
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")) // ex: "ROLE_ADMIN"이랑 "ROLE_MEMBER"같은 문자열이 들어있는 문자열 배열
//                        .map(role -> new SimpleGrantedAuthority(role))   // 문자열 배열에 들어있는 권한 문자열 마다 SimpleGrantedAuthority 객체로 만듦
//                        .collect(Collectors.toList());
//        log.info("[TokenProvider] authorities {} ", authorities);   //   [TokenProvider] authorities [ROLE_ADMIN, ROLE_USER]

        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token));
        log.info("[TokenProvider] ===================== {}",  userDetails.getAuthorities());

        log.info("[TokenProvider] getAuthentication End =============================== ");
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    /* 4. 토큰 유효성 검사 */
    public boolean validateToken(String token){

        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("[TokenProvider] 잘못된 JWT 서명입니다.");
            throw new TokenException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e){
            log.info("[TokenProvider] 만료된 JWT 토큰입니다.");
            throw new TokenException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e){
            log.info("[TokenProvider] 지원되지 않는 JWT 토큰입니다.");
            throw new TokenException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e){
            log.info("[TokenProvider] JWT 토큰이 잘못되었습니다.");
            throw new TokenException("JWT 토큰이 잘못되었습니다.");
        }
    }

    /* 5. AccessToken에서 클레임 추출하는 메소드 */
    private Claims parseClaims(String token){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims(); // 토큰이 만료되어 예외가 발생하더라도 클레임 값들을 뽑을 수 있다.
        }
    }
}