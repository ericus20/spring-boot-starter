# API Authentication Controller

The API Authentication Controller is used to authenticate users, refresh expired tokens and logout.
The endpoints are not secured and are open to all users.

The base URL for the API Authentication Controller is:

```
/api/v1/auth
```

![img.png](images/auth-rest-api.png)

# Login Endpoint

Attempts to authenticate with the provided credentials. If successful, a JWT token is returned with
some user details.

A refresh token is generated and returned as a cookie.

The URL for the login endpoint is:

```
POST: /api/v1/auth/login
```

![img_1.png](images/auth-rest-login-request.png)

```json
{
  "username": "admin",
  "password": "password"
}
```

Takes the username and password of a user as json then generates a JWT response object.
A new refreshToken is generated and returned as a cookie.

When a valid refreshToken is specified, the provided refreshToken will be used instead of generating
a new one.

![img_2.png](images/auth-rest-login-response.png)

```
accessToken: This token should be passed along with requests to access protected endpoints.
publicId: This should be used as the userId for all requests to uniquely identify a user.
```

* Request to retrieve a user object does not include the database id. PublicId must be used instead.

![img_3.png](images/sample-secured-url-access.png)

# Refresh Endpoint

Refreshes the current access token by creating a new access token when given a valid refreshToken.

The URL for the refresh endpoint is:

```
GET: /api/v1/auth/refresh
```

![img_4.png](images/auth-rest-refresh-token.png)

# Logout Endpoint

Logs out the current user by invalidating the current refresh token. This is done by returning an
empty refreshToken via cookie.
Logout the user from the system and clear all cookies.

The URL for the logout endpoint is:

```
DELETE: /api/v1/auth/logout
```

![img.png](images/auth-rest-logout.png)
