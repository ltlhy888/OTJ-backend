INSERT INTO content_key VALUES
("ACCOUNT_ACTIVATION", "2023-07-20 20:48:07","2023-07-20 20:48:07","TEMPLATE", "Send user email to self activate account"),
("RESET_PASSWORD", "2023-07-20 20:48:07","2023-07-20 20:48:07","TEMPLATE", "Send user email to reset password");

INSERT INTO content VALUES
("a3d9e692-dfd8-4bfd-9fa1-b30435c903f8", "2023-07-20 20:48:07","2023-07-20 20:48:07","ACCOUNT_ACTIVATION", 0, "Hi {{username}}: {{activateToken}}"),
("a3d9e692-dfd8-4bfd-9fa1-b30439c903f8", "2023-07-20 20:48:07","2023-07-20 20:48:07","RESET_PASSWORD", 0, "Reset token: {{resetToken}}");

