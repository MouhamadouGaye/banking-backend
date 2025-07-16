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
--------------------------------------------------------------------------------
---------------------- Migration Script (PostgreSQL) ---------------------------
--------------------------------------------------------------------------------
-- STEP 1: Drop the existing foreign key constraint
ALTER TABLE admin_actions DROP CONSTRAINT IF EXISTS fk2rta03r4anjotqvu1kcsiqi3x;
--------------------------------------------------------------------------------
-- STEP 2: Convert 'admin_id' in 'admin_actions' table from VARCHAR to UUID
ALTER TABLE admin_actions
ALTER COLUMN admin_id
SET DATA TYPE uuid USING admin_id::uuid;
--------------------------------------------------------------------------------
-- STEP 3: Convert 'id' in 'users' table from VARCHAR to UUID
ALTER TABLE users
ALTER COLUMN id
SET DATA TYPE uuid USING id::uuid;
--------------------------------------------------------------------------------
-- STEP 4: Recreate the foreign key constraint with a proper name
ALTER TABLE admin_actions
ADD CONSTRAINT fk_admin_actions_admin_id FOREIGN KEY (admin_id) REFERENCES users(id);
--------------------------------------------------------------------------------
SELECT con.conname AS constraint_name,
    conrelid::regclass AS table_name,
    a.attname AS column_name,
    confrelid::regclass AS referenced_table,
    af.attname AS referenced_column
FROM pg_constraint con
    JOIN pg_class rel ON rel.oid = con.conrelid
    JOIN pg_attribute a ON a.attnum = ANY (con.conkey)
    AND a.attrelid = rel.oid
    JOIN pg_class frel ON frel.oid = con.confrelid
    JOIN pg_attribute af ON af.attnum = ANY (con.confkey)
    AND af.attrelid = frel.oid
WHERE con.contype = 'f';
----------------------------------------------------------------------------------------
-- Show all foreign keys where users is the referenced table
SELECT conname AS constraint_name,
    conrelid::regclass AS table_with_fk
FROM pg_constraint
WHERE confrelid = 'users'::regclass
    AND contype = 'f';
----------------------------------------------------------------------------------------Check for invalid UUIDs in users.id
SELECT id
FROM users
WHERE id !~* '^[0-9a-fA-F-]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$';
SELECT conrelid::regclass AS referencing_table,
    a.attname AS referencing_column
FROM pg_constraint c
    JOIN pg_attribute a ON a.attnum = ANY(c.conkey)
    AND a.attrelid = c.conrelid
WHERE confrelid = 'users'::regclass
    AND contype = 'f';
----------------------------------------------------------------------------------------
------------------------------Full SQL Migration Script--------------------------------
-- Step 1: Drop foreign key constraints
ALTER TABLE tax_documents DROP CONSTRAINT IF EXISTS tax_documents_user_id_fkey;
ALTER TABLE ticket_messages DROP CONSTRAINT IF EXISTS ticket_messages_author_id_fkey;
ALTER TABLE support_tickets DROP CONSTRAINT IF EXISTS support_tickets_assigned_to_fkey;
ALTER TABLE support_tickets DROP CONSTRAINT IF EXISTS support_tickets_user_id_fkey;
ALTER TABLE bank_accounts DROP CONSTRAINT IF EXISTS bank_accounts_user_id_fkey;
ALTER TABLE beneficiaries DROP CONSTRAINT IF EXISTS beneficiaries_user_id_fkey;
ALTER TABLE cards DROP CONSTRAINT IF EXISTS cards_user_id_fkey;
ALTER TABLE fraud_reports DROP CONSTRAINT IF EXISTS fraud_reports_reported_by_fkey;
ALTER TABLE loan_applications DROP CONSTRAINT IF EXISTS loan_applications_user_id_fkey;
ALTER TABLE loans DROP CONSTRAINT IF EXISTS loans_user_id_fkey;
ALTER TABLE notification_preferences DROP CONSTRAINT IF EXISTS notification_preferences_user_id_fkey;
ALTER TABLE refresh_tokens DROP CONSTRAINT IF EXISTS refresh_tokens_user_id_fkey;
ALTER TABLE report_requests DROP CONSTRAINT IF EXISTS report_requests_user_id_fkey;
ALTER TABLE security_settings DROP CONSTRAINT IF EXISTS security_settings_user_id_fkey;
ALTER TABLE transaction DROP CONSTRAINT IF EXISTS transaction_user_id_fkey;
ALTER TABLE user_roles DROP CONSTRAINT IF EXISTS user_roles_user_id_fkey;
ALTER TABLE user_settings DROP CONSTRAINT IF EXISTS user_settings_user_id_fkey;
-------------------------------------------------------------------------------------------------
-- Step 2: Alter all referencing columns to UUID
ALTER TABLE tax_documents
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
ALTER TABLE ticket_messages
ALTER COLUMN author_id TYPE UUID USING author_id::uuid;
ALTER TABLE support_tickets
ALTER COLUMN assigned_to TYPE UUID USING assigned_to::uuid;
ALTER TABLE support_tickets
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
ALTER TABLE bank_accounts
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
ALTER TABLE beneficiaries
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
ALTER TABLE cards
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
ALTER TABLE fraud_reports
ALTER COLUMN reported_by TYPE UUID USING reported_by::uuid;
ALTER TABLE loan_applications
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
ALTER TABLE loans
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
ALTER TABLE notification_preferences
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
ALTER TABLE refresh_tokens
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
ALTER TABLE report_requests
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
ALTER TABLE security_settings
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
ALTER TABLE transaction
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
ALTER TABLE user_roles
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
ALTER TABLE user_settings
ALTER COLUMN user_id TYPE UUID USING user_id::uuid;
--------------------------------------------------------------------------------------------
-- Step 3: Alter users.id to UUID
ALTER TABLE users
ALTER COLUMN id TYPE UUID USING id::uuid;
--------------------------------------------------------------------------------------------
-- Step 4: Re-add foreign key constraints
ALTER TABLE tax_documents
ADD CONSTRAINT tax_documents_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE ticket_messages
ADD CONSTRAINT ticket_messages_author_id_fkey FOREIGN KEY (author_id) REFERENCES users(id);
ALTER TABLE support_tickets
ADD CONSTRAINT support_tickets_assigned_to_fkey FOREIGN KEY (assigned_to) REFERENCES users(id);
ALTER TABLE support_tickets
ADD CONSTRAINT support_tickets_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE bank_accounts
ADD CONSTRAINT bank_accounts_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE beneficiaries
ADD CONSTRAINT beneficiaries_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE cards
ADD CONSTRAINT cards_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE fraud_reports
ADD CONSTRAINT fraud_reports_reported_by_fkey FOREIGN KEY (reported_by) REFERENCES users(id);
ALTER TABLE loan_applications
ADD CONSTRAINT loan_applications_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE loans
ADD CONSTRAINT loans_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE notification_preferences
ADD CONSTRAINT notification_preferences_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE refresh_tokens
ADD CONSTRAINT refresh_tokens_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE report_requests
ADD CONSTRAINT report_requests_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE security_settings
ADD CONSTRAINT security_settings_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE transaction
ADD CONSTRAINT transaction_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE user_roles
ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE user_settings
ADD CONSTRAINT user_settings_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);
--------------------------------------------------------------------------------------------
DO $$
DECLARE r RECORD;
BEGIN -- Disable foreign key checks temporarily
EXECUTE 'SET session_replication_role = replica';
-- Drop all tables in public schema
FOR r IN (
    SELECT tablename
    FROM pg_tables
    WHERE schemaname = 'public'
) LOOP EXECUTE 'DROP TABLE IF EXISTS public.' || quote_ident(r.tablename) || ' CASCADE';
END LOOP;
-- Drop all sequences in public schema
FOR r IN (
    SELECT sequencename
    FROM pg_sequences
    WHERE schemaname = 'public'
) LOOP EXECUTE 'DROP SEQUENCE IF EXISTS public.' || quote_ident(r.sequencename) || ' CASCADE';
END LOOP;
-- Restore default constraint enforcement
EXECUTE 'SET session_replication_role = DEFAULT';
END $$;
-------------------------------------------------------------------------------------------- 1
ALTER TABLE users
ALTER COLUMN id
SET DEFAULT gen_random_uuid();
-------------------------------------------------------------------------------------------- 2
ALTER TABLE admin_actions
ALTER COLUMN id
SET DEFAULT gen_random_uuid();
--------------------------------------------------------------------------------------------
-- For users table
ALTER TABLE users
ALTER COLUMN id DROP DEFAULT;
-------------------------------------------------------------------------------------------- to undo the 1
-- For admin_actions table
ALTER TABLE admin_actions
ALTER COLUMN id DROP DEFAULT;
-------------------------------------------------------------------------------------------- to undo the 2