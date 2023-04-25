package com.example.UserService.projections;

import com.example.CommonService.model.CardDetails;
import com.example.CommonService.model.User;
import com.example.CommonService.queries.GetUserPaymentDetailsQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

/**
 * @author Heshan Karunaratne
 */
@Component
@Slf4j
public class UserProjections {
    @QueryHandler
    public User getUserPaymentDetails(GetUserPaymentDetailsQuery query) {
        // Get details from the Database
        log.info("User fetched");
        CardDetails cardDetails = CardDetails.builder()
                .name("Heshan Karunaratne")
                .validUntilYear(2023)
                .validUntilMonth(07)
                .cardNumber("12345677")
                .cvv(123)
                .build();

        return User.builder()
                .cardDetails(cardDetails)
                .userId(query.getUserId())
                .lastName("Karunaratne")
                .firstName("Heshan")
                .build();
    }
}
