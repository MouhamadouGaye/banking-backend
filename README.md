# banking-backend
sequenceDiagram
    Frontend->>Backend: Login (email/password)
    Backend->>Frontend: JWT (minimal claims)
    Frontend->>Backend: GET /users/me (with JWT)
    Backend->>Frontend: Full user details (only after auth)