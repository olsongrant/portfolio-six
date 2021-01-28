# FormulaFund Portfolio Manager
----

In order for this project to compile properly in the IDE,
you are likely to need to install [Project Lombok](https://projectlombok.org/) in your IDE.



Secrets that you'll need to provide to the application:
* spring.mail.password=REPLACE_ME
* spring.security.oauth2.client.registration.facebook.client-id=3853934561682476 # (some random digits replaced)
* spring.security.oauth2.client.registration.facebook.client-secret=0198403dff0a7686840383c548e6150b # (random digits replaced)

The `jasypt` encryption library was used to encrypt the secrets.

		<dependency>
			<groupId>com.github.ulisesbocchio</groupId>
			<artifactId>jasypt-spring-boot-starter</artifactId>
			<version>3.0.3</version>
		</dependency>
---
A web page describing the way jaysypt was used: <https://www.codejava.net/frameworks/spring-boot/spring-boot-password-encryption>


