import static spark.Spark.port;

public class Server {

    public static void main(String[] args) {
        System.out.println("Start server");

        port(7000);

        API.initAPI();

        System.out.println("Post API request");
    }


}
