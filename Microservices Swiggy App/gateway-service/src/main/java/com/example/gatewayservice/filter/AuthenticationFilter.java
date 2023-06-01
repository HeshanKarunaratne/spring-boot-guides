package com.example.gatewayservice.filter;

import com.example.gatewayservice.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Heshan Karunaratne
 */
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private RestTemplate template;

    @Autowired
    private JWTUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            if (routeValidator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                //REST call to AUTH service
                try {
//                    template.getForObject("http://APP-SERVICE/auth/validate?token=" + authHeader, String.class);
                    jwtUtil.validateToken(authHeader);

                } catch (RestClientException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Unauthorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}

