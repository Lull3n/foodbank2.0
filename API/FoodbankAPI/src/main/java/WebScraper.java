import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.HttpResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WebScraper {


    /**
     * Is supposed to start a scraping job on webscraper.io but request returns 403 forbidden.
     * Possible that you need a subscription to make requests from outside their website
     */
    private void postScraperRequest(){
        HttpResponse<JsonNode> postResponse;
        String postStatus;
        try {
            postResponse = Unirest.post("https://api.webscraper.io/api/v1/scraping-job?api_token=PraKnld9vsPzFXUZaaXp5nKLhLKZcb8zEY1q1HqnPMsrxKQxwvVFqXVunxM1")
                            .queryString("JSON:", "{\n" +
                            "\"sitemap_id\": 453311,\n" +
                            "\"driver\": \"fast\",\n" +
                            "\"page_load_delay\": 500,\n" +
                            "\"request_interval\": 2000,\n" +
                            "\"proxy\": 1,\n" +
                            "\"start_urls\": [\n" +
                            "\"https://www.coop.se/handla/\",\n" +
                            "],\n" +
                            "}")
                            .asJson();

            postStatus = postResponse.getStatusText();
            System.out.println("http-code: " + postResponse.getStatus() + " " + postStatus);
        } catch (UnirestException e){
            e.printStackTrace();
        }
    }

    /**
     * gets data from a specific scraper job and writes data to json file
     * scraping data is returned in String format
     */
    private void getScraperJob() {
    HttpResponse<String> response;
    String[] responseString;

    try{


        File file = new File("files/Coop information.json");
        response = Unirest.get("https://api.webscraper.io/api/v1/scraping-job/3798112/json?api_token=PraKnld9vsPzFXUZaaXp5nKLhLKZcb8zEY1q1HqnPMsrxKQxwvVFqXVunxM1")
                            .queryString("format", "string")
                            .asString();
        if (response == null){
            System.out.println("Response was null");
        } else {
            file.createNewFile();
            System.out.println("Response valid. Writing to file");
            FileWriter fileWriter = new FileWriter(file);
            responseString = response.getBody().split("}");
            fileWriter.write("[" + "\n");
            for (String s : responseString) {
                String[] objectString = s.split(",");
                for (int i = 0; i < objectString.length; i++){
                    if (objectString[i].charAt(0) == '{') {
                        fileWriter.write("{" + "\n");
                        fileWriter.write(objectString[i].substring(1) + "," + "\n");
                    } else if (objectString.length - 1 == i){
                        fileWriter.write(objectString[i] + "\n");
                        fileWriter.write("},");
                    } else {
                        System.out.println(objectString[i].substring(0, 3));
                        fileWriter.write(objectString[i] + "," + "\n");
                    }
                }
            }
            fileWriter.write("]");
            fileWriter.flush();
            fileWriter.close();
        }
    } catch (UnirestException | IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebScraper parseHubRunner = new WebScraper();
        parseHubRunner.getScraperJob();
    }
}
