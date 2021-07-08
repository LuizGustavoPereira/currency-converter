# currency-converter

Version 1.0.0

## Introduction

The purpose of the Currency Converter Service (CCS) is to allow users to convert values between two different currencies,
to be able to do this all currency information from http://api.exchangeratesapi.io/latest?base=EUR

## Technologies

To develop this service I decided to use Spring-Boot with java for the backend development and H2 for database.
Java was the chosen one, because it is an easy language to work on restFull APIs, and we can use it with Spring Boot
that is a framework that makes configuration much easier. The DB was done using h2, because it almost doesn't need any configuration
to work and for small projects it's fits perfectly.

## How to use

First you should access the endpoint to grand access to the platform

```
curl --location --request POST 'https://dev-3u2hq9mr.us.auth0.com/oauth/token?=' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Cookie: did=s%3Av0%3A8deef9a0-ddff-11eb-9fcc-9732f4b7434c.FfEz1Q8e2AeDfxqFMTNrla8VnJrGMvTRT%2BAPiyW5YFo; did_compat=s%3Av0%3A8deef9a0-ddff-11eb-9fcc-9732f4b7434c.FfEz1Q8e2AeDfxqFMTNrla8VnJrGMvTRT%2BAPiyW5YFo' \
--data-urlencode 'grant_type=client_credentials' \
--data-urlencode 'client_id=JKKCmlxp76xSMjcixakiHDXzsjNN3yp3' \
--data-urlencode 'client_secret=FOPwQWd5RDXfsFcxL8M97aMuSA9aY9q6vHivOBe-9AWhjSU3cfwSeei_5c7fliVR' \
--data-urlencode 'audience=https://currency-converter-service.herokuapp.com/'
```

Calling this endpoint a response like bellow should be waited
```
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im9HWVZBZ09ldndyN2JyMXJMeldodiJ9.eyJpc3MiOiJodHRwczovL2Rldi0zdTJocTltci51cy5hdXRoMC5jb20vIiwic3ViIjoiSktLQ21seHA3NnhTTWpjaXhha2lIRFh6c2pOTjN5cDNAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vY3VycmVuY3ktY29udmVydGVyLXNlcnZpY2UuaGVyb2t1YXBwLmNvbS8iLCJpYXQiOjE2MjU3MDc0MzAsImV4cCI6MTYyNTc5MzgzMCwiYXpwIjoiSktLQ21seHA3NnhTTWpjaXhha2lIRFh6c2pOTjN5cDMiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.2GmIdJxjVR9c2ALVIEBqepFbQMZcVRGsdZmkbRjqU2oO03xMcU2FR7BKYtS6XdhEZNQMN8FWSd5amsXSTh92S7MGlS2zLjKFdChLbDWiYi7oTeImMyfgCWKDWu_n2xB98JN5g9epnCEoTX8X2G2764HdWDp7zTVenjfKCiwuYXbnkrqWe2X-bVCBzHY2FIPJJJ0vAcxvj45ZfFZCX7CJmqIjM-WyFGwMESNs73r9YevI4VBsjHlssYQfN6ZO-SJVxSfERHp0766ROp3d8mN0K5qTzT9EKr0-Fw8flgzL_41Xc56FLuBNXCo9PLK-0GJkWqzgi35gP08nrUGUn2sJqA",
    "expires_in": 86400,
    "token_type": "Bearer"
}
```

The access token should be used as authorization for all others endpoints. Now a user should 
be registered calling this endpoint, this endpoint expects a json with userName and email as body and Authorization Bearer
access token as header

```
curl --location --request POST 'https://currency-converter-service.herokuapp.com/api/user/register' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im9HWVZBZ09ldndyN2JyMXJMeldodiJ9.eyJpc3MiOiJodHRwczovL2Rldi0zdTJocTltci51cy5hdXRoMC5jb20vIiwic3ViIjoiSktLQ21seHA3NnhTTWpjaXhha2lIRFh6c2pOTjN5cDNAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vY3VycmVuY3ktY29udmVydGVyLXNlcnZpY2UuaGVyb2t1YXBwLmNvbS8iLCJpYXQiOjE2MjU1ODk4NDQsImV4cCI6MTYyNTY3NjI0NCwiYXpwIjoiSktLQ21seHA3NnhTTWpjaXhha2lIRFh6c2pOTjN5cDMiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.NTPd_E4dYbGLRvAAlj2krrTZpcsZqV8Of6fj1MO6XsFbtSVrr1EIkWUFO9g6patGCMgJ9MYrwax5R0zP6SS9_QxR7hz2I2zlGJ1L5JQH2V0I_vJZ2P7MKAa0NYODPhczXFoksfSbk2vrRxMe5q1cm7XykQC9FbD84eP0Pdq0DO5LLg05DuuOaicW5NjMnyQZ86ND9dZXM-XdyxOYE6nF2pb3oZdy-rAUdFhL8Ebh-Ymp2F22SnXlavqALaIyvVQVjG9552ztoXbZGmeAWRXdZ_1-G8z_iln-_uyjYdA-jw-N95fgSs_BOgMPDlxpitQENBdoo6O5t3p-9gye9l-sUA' \
--header 'Content-Type: application/json' \
--data-raw '{
    "userName": "joeDoe",
    "email": "joeDoes@gmail.com" 
}'
```

Besides, that there are two other endpoints one for converting currencies and other one for get all the conversions done by one user.
First we gonna to talk about, is the one used to do conversions, for this one we have three parameters to be passed on the
URL: currency from, currency to and the amount. On the header we have to pass the token Authorization Bearer
access token, and the username
```
curl --location --request GET 'https://currency-converter-service.herokuapp.com/api/convertCurrency/AUD/USD/-1' \
--header 'userName: joeDoe' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im9HWVZBZ09ldndyN2JyMXJMeldodiJ9.eyJpc3MiOiJodHRwczovL2Rldi0zdTJocTltci51cy5hdXRoMC5jb20vIiwic3ViIjoiSktLQ21seHA3NnhTTWpjaXhha2lIRFh6c2pOTjN5cDNAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vY3VycmVuY3ktY29udmVydGVyLXNlcnZpY2UuaGVyb2t1YXBwLmNvbS8iLCJpYXQiOjE2MjU2MTk5OTgsImV4cCI6MTYyNTcwNjM5OCwiYXpwIjoiSktLQ21seHA3NnhTTWpjaXhha2lIRFh6c2pOTjN5cDMiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.tDAiSpi9wExYw-cSxhWiLQ_nu6tG4ESy1J77A1ZeiEndhF1Bn6XomIPGXa6FulTkO7jCce0o4Fjh5kvnS2kEn42v1xDOPoQruO2JpsRgynuLEgfEsRtm5q4tgb98WYkr_-oJpHLght-rD70v4JGxlvqF_RBxBzil7llOqfoFcO-Co8Xx4s1KcjY6S5yLAuxwRmAohbo68JRUZfb1_X0mqULDBgB8Q5EUg2_SPKp-yjgUERm8-twjvbqtOEZ8bI1uFgVhsCgFTpVv-093nsFlVMZq4K-SbqbRp8wM1vdd-swriyV6YQvHCue4MmjitPBLEFdradG_1DCM5ULqAUazKQ'
```

The other one is to get all conversions that one specific user did.
This endpoint requires a parameter on the URL, username, and like all the other the access token should be passes on the header

```
curl --location --request GET 'https://currency-converter-service.herokuapp.com/api/findTransactionsByUserName/joeDoe' \
--header 'Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im9HWVZBZ09ldndyN2JyMXJMeldodiJ9.eyJpc3MiOiJodHRwczovL2Rldi0zdTJocTltci51cy5hdXRoMC5jb20vIiwic3ViIjoiSktLQ21seHA3NnhTTWpjaXhha2lIRFh6c2pOTjN5cDNAY2xpZW50cyIsImF1ZCI6Imh0dHBzOi8vY3VycmVuY3ktY29udmVydGVyLXNlcnZpY2UuaGVyb2t1YXBwLmNvbS8iLCJpYXQiOjE2MjU2MTk5OTgsImV4cCI6MTYyNTcwNjM5OCwiYXpwIjoiSktLQ21seHA3NnhTTWpjaXhha2lIRFh6c2pOTjN5cDMiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.tDAiSpi9wExYw-cSxhWiLQ_nu6tG4ESy1J77A1ZeiEndhF1Bn6XomIPGXa6FulTkO7jCce0o4Fjh5kvnS2kEn42v1xDOPoQruO2JpsRgynuLEgfEsRtm5q4tgb98WYkr_-oJpHLght-rD70v4JGxlvqF_RBxBzil7llOqfoFcO-Co8Xx4s1KcjY6S5yLAuxwRmAohbo68JRUZfb1_X0mqULDBgB8Q5EUg2_SPKp-yjgUERm8-twjvbqtOEZ8bI1uFgVhsCgFTpVv-093nsFlVMZq4K-SbqbRp8wM1vdd-swriyV6YQvHCue4MmjitPBLEFdradG_1DCM5ULqAUazKQ'
```