/**
 *	
 *	@author		: CHOUABBIA Amine
 *
 *	@Name		: SecurityConfigProperties
 *	@CreatedOn	: 10-29-2025
 *
 *	@Type		: Class
 *	@Layer		: Configuration
 *	@Package	: System / Security
 *
 **/

package dz.mdn.raas.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "raas.security")
public class SecurityConfigProperties {

    private Jwt jwt = new Jwt();
    private Cors cors = new Cors();
    private Authentication auth = new Authentication();
    private Authorization authz = new Authorization();

    @Data
    public static class Jwt {
        private String secret = "raasDefaultSecretKeyThatShouldBeChangedInProduction";
        private Long expiration = 86400000L; // 24 hours
        private Long refreshExpiration = 604800000L; // 7 days
        private String issuer = "raas-api";
        private String audience = "raas-client";
    }

    @Data
    public static class Cors {
        private List<String> allowedOrigins = List.of("http://localhost:3000", "http://localhost:8080");
        private Boolean allowCredentials = true;
        private Long maxAge = 3600L;
    }

    @Data
    public static class Authentication {
        private Integer maxFailedAttempts = 5;
        private Long lockDuration = 1800000L; // 30 minutes
        private Boolean enableBruteForceProtection = true;
        private Long passwordExpirationDays = 90L;
    }

    @Data
    public static class Authorization {
        private Boolean enableObjectLevelSecurity = true;
        private Boolean enableAuditLogging = true;
        private String defaultRole = "USER";
    }
}
