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

        //Does not work 403 forbidden
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
     * Is supposed to get information from a scraping job but only returns one item.
     * Possible solution: find another web scraping api
     */
    private void getScraperJob() {
//    HttpResponse<JsonNode> response;
    HttpResponse<String> response;
    String[] responseString;

    try{


        File file = new File("files/Coop information.json");
        /*response = Unirest.get("https://api.webscraper.io/api/v1/scraping-job/3798112/json?api_token=PraKnld9vsPzFXUZaaXp5nKLhLKZcb8zEY1q1HqnPMsrxKQxwvVFqXVunxM1")
                            .queryString("format","json")
                            .asJson();*/
        response = Unirest.get("https://api.webscraper.io/api/v1/scraping-job/3798112/json?api_token=PraKnld9vsPzFXUZaaXp5nKLhLKZcb8zEY1q1HqnPMsrxKQxwvVFqXVunxM1")
                            .queryString("format", "string")
                            .asString();
        if (response == null){
            System.out.println("Response was null :(");
        } else {
            file.createNewFile();
            System.out.println("Response valid. Writing to file");
            FileWriter fileWriter = new FileWriter(file);
            responseString = response.getBody().split(",");
//            System.out.println(response.getStatus() + " " + response.getStatusText() + "\n" + response.getBody());
            fileWriter.write("[" + "\n");
            for (String s : responseString) {
                System.out.println(s);
                System.out.println("charAt(0): " + s.charAt(0) + " || charAt(length-1): " + s.charAt(s.length() - 1));
                if (s.charAt(0) == '{') {
//                    System.out.println("Open");
                    fileWriter.write("{" + "\n");
                    fileWriter.write(s.substring(1) + "," + "\n");
                }
                else if (s.charAt(s.length() - 1) == '}'){
//                    System.out.println("Close");
                    fileWriter.write(s.substring(0, s.length() - 1) + "\n");
                    fileWriter.write("}," + "\n");
                } else {
                    fileWriter.write(s + "," + "\n");
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
