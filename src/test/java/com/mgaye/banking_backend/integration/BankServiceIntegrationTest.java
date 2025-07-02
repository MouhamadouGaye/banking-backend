// test/BankingServiceIntegrationTest.java
package com.mgaye.banking_backend.integration;

import net.bytebuddy.utility.dispatcher.JavaDispatcher.Container;

@SpringBootTest
@Testcontainers
class BankingServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private TransactionService txService;

    @Test
    @Transactional
    void whenValidTransfer_thenAccountsUpdated() {
        // Test implementation
    }
}