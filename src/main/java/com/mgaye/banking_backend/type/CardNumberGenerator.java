// package com.mgaye.banking_backend.type;

// import com.mgaye.banking_backend.dto.request.CardIssuanceRequest;

// public interface CardNumberGenerator {
//     String generate(CardIssuanceRequest.CardType cardType);
// }

package com.mgaye.banking_backend.type;

import com.mgaye.banking_backend.dto.request.CardIssuanceRequest;

public interface CardNumberGenerator {
    String generate(CardIssuanceRequest.CardType cardType, CardIssuanceRequest.CardDesign design);
}