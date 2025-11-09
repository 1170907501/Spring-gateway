/**
 * 客户端信息表
 */
CREATE TABLE oauth2_registered_client (
      id varchar(100) NOT NULL,
      client_id varchar(100) NOT NULL,
      client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
      client_secret varchar(200) DEFAULT NULL,
      client_secret_expires_at timestamp DEFAULT NULL,
      client_name varchar(200) NOT NULL,
      client_authentication_methods varchar(1000) NOT NULL,
      authorization_grant_types varchar(1000) NOT NULL,
      redirect_uris varchar(1000) DEFAULT NULL,
      post_logout_redirect_uris varchar(1000) DEFAULT NULL,
      scopes varchar(1000) NOT NULL,
      client_settings varchar(2000) NOT NULL,
      token_settings varchar(2000) NOT NULL,
      PRIMARY KEY (id)
);

/**
 * 授权确认表
 */
CREATE TABLE oauth2_authorization_consent (
      registered_client_id varchar(100) NOT NULL,
      principal_name varchar(200) NOT NULL,
      authorities varchar(1000) NOT NULL,
      PRIMARY KEY (registered_client_id, principal_name)
);


/**
 * 授权信息表
 */
CREATE TABLE oauth2_authorization (
      id varchar(100) NOT NULL,
      registered_client_id varchar(100) NOT NULL,
      principal_name varchar(200) NOT NULL,
      authorization_grant_type varchar(100) NOT NULL,
      authorized_scopes varchar(1000) DEFAULT NULL,
      attributes blob DEFAULT NULL,
      state varchar(500) DEFAULT NULL,
      authorization_code_value blob DEFAULT NULL,
      authorization_code_issued_at timestamp DEFAULT NULL,
      authorization_code_expires_at timestamp DEFAULT NULL,
      authorization_code_metadata blob DEFAULT NULL,
      access_token_value blob DEFAULT NULL,
      access_token_issued_at timestamp DEFAULT NULL,
      access_token_expires_at timestamp DEFAULT NULL,
      access_token_metadata blob DEFAULT NULL,
      access_token_type varchar(100) DEFAULT NULL,
      access_token_scopes varchar(1000) DEFAULT NULL,
      oidc_id_token_value blob DEFAULT NULL,
      oidc_id_token_issued_at timestamp DEFAULT NULL,
      oidc_id_token_expires_at timestamp DEFAULT NULL,
      oidc_id_token_metadata blob DEFAULT NULL,
      refresh_token_value blob DEFAULT NULL,
      refresh_token_issued_at timestamp DEFAULT NULL,
      refresh_token_expires_at timestamp DEFAULT NULL,
      refresh_token_metadata blob DEFAULT NULL,
      user_code_value blob DEFAULT NULL,
      user_code_issued_at timestamp DEFAULT NULL,
      user_code_expires_at timestamp DEFAULT NULL,
      user_code_metadata blob DEFAULT NULL,
      device_code_value blob DEFAULT NULL,
      device_code_issued_at timestamp DEFAULT NULL,
      device_code_expires_at timestamp DEFAULT NULL,
      device_code_metadata blob DEFAULT NULL,
      PRIMARY KEY (id)
);


INSERT INTO `oauth-server`.`oauth2_registered_client` (`id`, `client_id`, `client_id_issued_at`, `client_secret`, `client_secret_expires_at`, `client_name`, `client_authentication_methods`, `authorization_grant_types`, `redirect_uris`, `post_logout_redirect_uris`, `scopes`, `client_settings`, `token_settings`) VALUES ('3eacac0e-0de9-4727-9a64-6bdd4be2ee1f', 'oidc-client', '2023-07-12 07:33:42', '$2a$10$.J0Rfg7y2Mu8AN8Dk2vL.eBFa9NGbOYCPOAFEw.QhgGLVXjO7eFDC', NULL, '3eacac0e-0de9-4727-9a64-6bdd4be2ee1f', 'client_secret_basic', 'refresh_token,authorization_code', 'http://www.baidu.com', 'http://127.0.0.1:8080/', 'openid,profile', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}');


CREATE TABLE `sys_user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
    `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '密码',
    `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '姓名',
    `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
    `status` tinyint DEFAULT NULL COMMENT '状态（1：正常 0：停用）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';

INSERT INTO `oauth-server`.`sys_user` (`id`, `username`, `password`, `name`, `description`, `status`) VALUES (1, 'fox', '$2a$10$8fyY0WbNAr980e6nLcPL5ugmpkLLH3serye5SJ3UcDForTW5b0Sx.', '测试用户', 'Spring Security 测试用户', 1);
