CREATE TABLE bank_accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_number VARCHAR(34) NOT NULL UNIQUE,
    user_id UUID NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance NUMERIC(19, 4) NOT NULL DEFAULT 0,
    currency CHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    overdraft_limit NUMERIC(19, 4),
    minimum_balance NUMERIC(19, 4),
    interest_rate NUMERIC(5, 4),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true,
    version BIGINT NOT NULL DEFAULT 0,
    features JSONB,
    daily_transfer_total NUMERIC(19, 4) DEFAULT 0,
    daily_transfer_limit NUMERIC(19, 4),
    CONSTRAINT fk_bank_account_user FOREIGN KEY (user_id) REFERENCES users(id)
);
-- Indexes for bank accounts
CREATE INDEX idx_bank_accounts_user ON bank_accounts(user_id);
CREATE INDEX idx_bank_accounts_status ON bank_accounts(status);
-- -----------------------------------------------------------------------------------
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    dob DATE NOT NULL,
    kyc_status VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_enabled BOOLEAN NOT NULL DEFAULT true,
    device_token VARCHAR(255),
    -- Address fields (embedded)
    address_street VARCHAR(100),
    address_city VARCHAR(50),
    address_state VARCHAR(50),
    address_postal_code VARCHAR(20),
    address_country VARCHAR(50)
);
-- Indexes for users
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);
-- ---------------------------------------------------------------------------------------notification_preferences
CREATE TABLE notification_preferences (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    transaction_emails BOOLEAN NOT NULL DEFAULT true,
    marketing_emails BOOLEAN NOT NULL DEFAULT false,
    security_alerts BOOLEAN NOT NULL DEFAULT true,
    push_notifications BOOLEAN NOT NULL DEFAULT true,
    sms_notifications BOOLEAN NOT NULL DEFAULT false,
    email_notifications_enabled BOOLEAN,
    sms_notifications_enabled BOOLEAN,
    push_notifications_enabled BOOLEAN,
    transaction_emails_enabled BOOLEAN,
    marketing_emails_enabled BOOLEAN,
    security_alerts_enabled BOOLEAN,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_notification_prefs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_notification_prefs_user UNIQUE (user_id)
);
-- Create indexes
CREATE INDEX idx_notification_prefs_user ON notification_preferences(user_id);
## Migration Script (Flyway/V5 Format)
-- V2__create_notification_preferences.sql
CREATE TABLE notification_preferences (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    transaction_emails BOOLEAN NOT NULL DEFAULT true,
    marketing_emails BOOLEAN NOT NULL DEFAULT false,
    security_alerts BOOLEAN NOT NULL DEFAULT true,
    push_notifications BOOLEAN NOT NULL DEFAULT true,
    sms_notifications BOOLEAN NOT NULL DEFAULT false,
    email_notifications_enabled BOOLEAN,
    sms_notifications_enabled BOOLEAN,
    push_notifications_enabled BOOLEAN,
    transaction_emails_enabled BOOLEAN,
    marketing_emails_enabled BOOLEAN,
    security_alerts_enabled BOOLEAN,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_notification_prefs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_notification_prefs_user UNIQUE (user_id)
);
CREATE INDEX idx_notification_prefs_user ON notification_preferences(user_id);
-- ---------------------------------------------------------------------------------------
CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(100)
);
-- ---------------------------------------------------------------------------------------
CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);
-- ---------------------------------------------------------------------------------------user_settings
CREATE TABLE user_settings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    language VARCHAR(5) NOT NULL DEFAULT 'en',
    theme VARCHAR(10) NOT NULL DEFAULT 'LIGHT',
    currency CHAR(3) NOT NULL DEFAULT 'USD',
    transaction_notifications_enabled BOOLEAN NOT NULL DEFAULT true,
    security_alerts_enabled BOOLEAN NOT NULL DEFAULT true,
    marketing_notifications_enabled BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    -- Notification Preferences (embedded)
    notification_transaction_emails BOOLEAN NOT NULL DEFAULT true,
    notification_marketing_emails BOOLEAN NOT NULL DEFAULT false,
    notification_security_alerts BOOLEAN NOT NULL DEFAULT true,
    notification_push_notifications BOOLEAN NOT NULL DEFAULT true,
    notification_sms_notifications BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT fk_user_settings_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_user_settings_user UNIQUE (user_id)
);
-- Indexes
CREATE INDEX idx_user_settings_user ON user_settings(user_id);
## Migration Script (Flyway/V5 Format)
-- V3__create_user_settings.sql
CREATE TABLE user_settings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    language VARCHAR(5) NOT NULL DEFAULT 'en',
    theme VARCHAR(10) NOT NULL DEFAULT 'LIGHT',
    currency CHAR(3) NOT NULL DEFAULT 'USD',
    transaction_notifications_enabled BOOLEAN NOT NULL DEFAULT true,
    security_alerts_enabled BOOLEAN NOT NULL DEFAULT true,
    marketing_notifications_enabled BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    -- Embedded notification preferences
    notification_transaction_emails BOOLEAN NOT NULL DEFAULT true,
    notification_marketing_emails BOOLEAN NOT NULL DEFAULT false,
    notification_security_alerts BOOLEAN NOT NULL DEFAULT true,
    notification_push_notifications BOOLEAN NOT NULL DEFAULT true,
    notification_sms_notifications BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT fk_user_settings_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_user_settings_user UNIQUE (user_id)
);
CREATE INDEX idx_user_settings_user ON user_settings(user_id);
-- ----------------------------------------------------------------------------------------account_beneficiaries
-- Account-specific beneficiaries (lightweight)
CREATE TABLE account_beneficiaries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL,
    beneficiary_name VARCHAR(100) NOT NULL,
    account_number VARCHAR(34) NOT NULL,
    routing_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_beneficiary FOREIGN KEY (account_id) REFERENCES bank_accounts(id) ON DELETE CASCADE
);
## Migration Script
-- V5__create_beneficiary_tables.sql
CREATE TABLE account_beneficiaries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL,
    beneficiary_name VARCHAR(100) NOT NULL,
    account_number VARCHAR(34) NOT NULL,
    routing_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_beneficiary FOREIGN KEY (account_id) REFERENCES bank_accounts(id) ON DELETE CASCADE
);
-- ------------------------------------------------------------------------------------------beneficiaries
-- User's global beneficiary list (detailed)
CREATE TABLE beneficiaries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    account_number VARCHAR(34) NOT NULL,
    routing_number VARCHAR(11),
    currency CHAR(3) NOT NULL,
    type VARCHAR(20) NOT NULL,
    bank_name VARCHAR(50) NOT NULL,
    bank_address VARCHAR(100),
    email VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verified BOOLEAN NOT NULL DEFAULT false,
    limits JSONB,
    CONSTRAINT fk_beneficiary_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
## Migration Script
CREATE TABLE beneficiaries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    account_number VARCHAR(34) NOT NULL,
    routing_number VARCHAR(11),
    currency CHAR(3) NOT NULL,
    type VARCHAR(20) NOT NULL,
    bank_name VARCHAR(50) NOT NULL,
    bank_address VARCHAR(100),
    email VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    verified BOOLEAN NOT NULL DEFAULT false,
    limits JSONB,
    CONSTRAINT fk_beneficiary_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE INDEX idx_account_beneficiaries_account ON account_beneficiaries(account_id);
CREATE INDEX idx_beneficiaries_user ON beneficiaries(user_id);
CREATE INDEX idx_beneficiaries_account ON beneficiaries(account_number);
-- ------------------------------------------------------------------------------------------------- this one is for changing the data type of a column
ALTER TABLE report_requests
ALTER COLUMN account_id
SET DATA TYPE uuid;
--------------------------------------- If getting this error: [ERROR: column "account_id" cannot be cast automatically to type uuid
ALTER TABLE report_requests
ALTER COLUMN account_id
SET DATA TYPE uuid USING account_id::uuid;