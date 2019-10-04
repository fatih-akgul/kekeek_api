# KEKEEK API
This project implements a REST API to be used as back-end for our travel guide websites. 

Models
--
- Page: Pages on the site
- Content: A content element. This can fill a section on the page or an article. 
- ContentPage: Pages of a content element. Articles etc can have multiple pages.

Environment variables required:
--
- DB_USERNAME
- DB_PASSWORD
- DB_URL (i.e. jdbc:postgresql://localhost:5432/kekeek)
- API_PASSWORD

Apply Flyway migrations:
--
./gradlew flywayMigrate -Dflyway.configFiles=flyway.properties
