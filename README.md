# Search Api - Youtube
1. Go to project directory 
2. Run mvn clean package
3. Run docker build --tag=search-api:latest .
4. docker run -p 8200:8200 search-api:latest

Now your application is running on localhost:8200
1. For checking paginated api response - make a get request to http://localhost:8200/fetch-video-response?page=1&size=20, here page is the nth page you want and size is the size of each page.
2. For checking the response of search api on title and desciprition - make a get request to http://localhost:8200/search-video?title=rock&description=slow

In the backend their is a continuos api call happening to youtube search api on every nth seconds, which is configuration with the given search query.

If you want to run the application without docker
1. Go to project directory 
2. Run mvn clean package
3. java -jar target/search-api-0.0.1-SNAPSHOT.jar

PS- If the API keys are exhausted or invalid please add your own youtube api keys.
