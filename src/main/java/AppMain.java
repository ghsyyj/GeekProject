import geek.week3.gateway.HttpClientUtil;
import geek.week3.gateway.Result;

public class AppMain {

    public static void main(String[] args) {

        Result message = HttpClientUtil.doGet("http://127.0.0.1:8802");

        System.out.println(message);
    }
}
