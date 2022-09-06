package zzgeek.utlis;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import zzgeek.commons.meta.ApiException;
import zzgeek.enums.ExceptionCodeAndMsg;

import java.util.Date;

/**
 * @author ZZGeek
 * @date 2021年10月28日 13:34
 * @description JwtUtil
 */
public class JwtUtil {

      public static String encodeJwt(String account,String roleCode,String secretKey,Long effectiveTime){
          Algorithm algorithm = Algorithm.HMAC256(secretKey);
          return JWT.create()
                  .withExpiresAt(new Date(System.currentTimeMillis() + effectiveTime))
                  .withAudience(account).withClaim("role",roleCode)
                  .sign(algorithm);
      }

      public static String decodeAccount(String token) {
          return JWT.decode(token).getAudience().get(0);
      }

      public static String decodeRole(String token){
          return JWT.decode(token).getClaim("role").asString();
      }

      public static void verify(String token,String secretKey){
          JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
          try{
              jwtVerifier.verify(token);
          }catch (JWTVerificationException e){
              throw new ApiException(ExceptionCodeAndMsg.INVALID_ADMINS_TOKEN,"JwtUtil");
          }
      }
}
