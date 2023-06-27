package com.pragma.powerup.infrastructure.cognito;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CognitoUserService {

    @Value("${aws.cognito.user-pool-id}")
    private String userPoolId;

    @Value("${aws.cognito.app-client-id}")
    private String appClientId;

    private final AWSCognitoIdentityProvider cognitoIdentityProvider;

    public void registerUser(String username, String password) {
        AdminCreateUserRequest createUserRequest = new AdminCreateUserRequest()
                .withUserPoolId(userPoolId)
                .withUsername(username)
                .withTemporaryPassword(password)
                .withMessageAction("SUPPRESS");

            AdminCreateUserResult createUserResult = cognitoIdentityProvider.adminCreateUser(createUserRequest);
    }

    public void loginUser(String username, String password) {
        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withUserPoolId(userPoolId)
                .withClientId(appClientId)
                .withAuthFlow("ADMIN_NO_SRP_AUTH")
                .withAuthParameters(Map.of(
                        "USERNAME", username,
                        "PASSWORD", password
                ));

        AdminInitiateAuthResult authResult = cognitoIdentityProvider.adminInitiateAuth(authRequest);
    }
}
