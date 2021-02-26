import static spark.Spark.port;

public class Server {

    public static void main(String[] args) {
        System.out.println("Start serve");

        port(7000);


        API.initAPI();

        System.out.println("Server living the life of a server, hehe");
    }


}
