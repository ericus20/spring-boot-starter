# Authentication and Authorization with JWT

Authentication and authorization is a core feature of the [JWT](https://jwt.io/) and it's been used
in the application as the authentication token.

JWT tokens generated within the application are automatically encrypted to prevent public viewing of
any data embedded in the payload.

JWT tokens can be traced within the application logs as such:

```bash
# Access and Refresh tokens generated in the raw form as JWT
Token successfully created as eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY1MTIxNTA0OCwiZXhwIjoxNjUxMzAxNDQ4fQ.EPDxQIlW_qv9j2ra9DInxfDlfXCKB4gOrjkmSQ_6Vefh2x_q1twdMPqnaewPdvRjMccC-Ky6DFJtJzp_iLhelw
Token successfully created as eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY1MTIxNTA0OSwiZXhwIjoxNjUxMjE2ODQ5fQ.iqKT4LAyvJuQrGGA2volDeAE0w_HkVCly8ZVrrgrtT710aNLX5Dy7kdS2wO9NA5t7uuOYABsqSpKDA-lfLtkxA

```

* This can be viewed in jwt.io to view the content of the payload. The username can be seen in the
  payload.

![img_1.png](images/auth-jwt.png)

But the response object returned will be encrypted.
Below is an example of a successful authentication response.

```bash
# Access and Refresh tokens returned in encrypted form
<= AuthRestApi.authenticateUser(..) : <200 OK OK,JwtResponseBuilder(accessToken=51gnEp5Wxy7k1CvZSVBmGq0/CKwMegZ9CyxIpv18JWBleUp/Q5QHQduAN5QhLKulOfy1zwg52BWYSYUtLE+DU9JgGYCxUdVKPMs8C3fxzmAbuaYuvHnpqYnWSzkdWJ/41BTbWzTvxmGGt8wc5y1YTiqnSTq5to5q8CkI872mm3YKQzMIFE7O3+nJZNUZxhIzpRe2NCPlJ/cC70GGPTfRL+slbYMc+ge7RYOk70/d11rGhQdoaOwsdnrwTzGpL6uEIMb3uu21, type=Bearer, publicId=6d84dac9-18d5-4a84-942d-a198970a0624, username=admin, email=admin@gmail.com, roles=[ROLE_ADMIN]),[Set-Cookie:"refreshToken=vHGfMEt7UlIABfQFvPHRkDmM/7Y+fJ3xAOXGBLGgBegNYfCpffSTSjyHWAsVoPbgRPvcVUKq/d1djPqi4UBdm4vSRg+4U3F7iAr9w38PWk5nl5UQHDPAP1PRcsHN8DsVcsog/T7/8sdbyxalKpBfs9B+pzjCWY2vf7zXT9MaYj4Xauuu8Es87RbOVIPwHWbXUwP62Xq5TEoWuO7LK/zsBDt7iAG9/GHCTlxCF9WH4yjYLvKq5TmZYcDoB5F6+5vlZkiYLykc; Path=/; Max-Age=604800; Expires=Fri, 06 May 2022 06:50:49 GMT; HttpOnly; SameSite=strict"]> - Finished, duration: 1315 ms
```

The accessToken, which is the JWT token is encrypted.

## Authentication Response

```json
{
  "accessToken": "51gnEp5Wxy7k1CvZSVBmGq0/CKwMegZ9CyxIpv18JWBleUp/Q5QHQduAN5QhLKulOfy1zwg52BWYSYUtLE+DU9JgGYCxUdVKPMs8C3fxzmAbuaYuvHnpqYnWSzkdWJ/41BTbWzTvxmGGt8wc5y1YTiqnSTq5to5q8CkI872mm3YKQzMIFE7O3+nJZNUZxhIzpRe2NCPlJ/cC70GGPTfRL+slbYMc+ge7RYOk70/d11rGhQdoaOwsdnrwTzGpL6uEIMb3uu21",
  "type": "Bearer",
  "publicId": "6d84dac9-18d5-4a84-942d-a198970a0624",
  "username": "admin",
  "email": "admin@gmail.com",
  "roles": [
    "ROLE_ADMIN"
  ]
}
```

The response explicitly states the roles of the authenticated user but JWT has it baked in already.
As a result of that, the token alone can be used to authorize the user appropriately.

```bash
# Pre-authorizing a user has admin role and not using a remember-me login can be done as such and should pass
@PreAuthorize("isFullyAuthenticated() && hasRole(T(com.developersboard.enums.RoleType).ROLE_ADMIN)")
```
