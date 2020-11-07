# ArchUnit-Examples-Application-Analysis

This is a sample repository for application analysis using ArchUnit, which I introduced in [my presentation in JJUG CCC 2020 fall](https://confengine.com/jjug-ccc-2020-fall/proposal/14765/archunit).

## Sample Gradle tasks implemented in this repository

### scanDependenciesFromControllerToExternalApi

A gradle task that generates a table of which external API endpoints depend on the API endpoints of the Controller class in `/build/scan/Controller-to-ExternalApiUrl.csv`

Gradle tasks can be executed with the following commands
```
> ./gradlew scanDependenciesFromControllerToExternalApi
```

The generated CSV file contains the following data
```
END_POINT(Controller),REPOSITORY_METHOD,END_POINT(ExternalApiUrl)
/books/{author}(GET),findBooksByAuthor,https://api.book/search?author={author}
/books/{author}(GET),findRecommendedBook,https://api.book/recommend/search?={bookId}
```

#### TODO (Features not yet implemented as sample code)

- Tracking Dependencies with Consideration of Lambda Functions
