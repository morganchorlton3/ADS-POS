package sample;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import javax.net.ssl.HttpsURLConnection;

public class Controller implements Initializable {

    private static HttpURLConnection con;

    @FXML
    TextField barcodeTXT;


    public void initialize(URL arg0, ResourceBundle arg1) {// Initializes
        Cart cart = new Cart(0,0);
        // everything
        barcodeTXT.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode().toString().equalsIgnoreCase("TAB")) {
                    //System.out.println(barcodeTXT.getText());
                    try{
                        //System.out.println(getPrice(barcodeTXT.getText()));
                        cart.setCount(cart.getCount()+1);
                        cart.setTotal(cart.getTotal() + getPrice(barcodeTXT.getText()));
                        //getPrice(barcodeTXT.getText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    barcodeTXT.clear();
                }
            }
        });
    }
    public int getPrice(String barcode) throws IOException {
        var url = "http://ads.test/api/getPrice";
        var urlParameters = "barcode="+ barcode;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        try {

            var myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (var wr = new DataOutputStream(con.getOutputStream())) {

                wr.write(postData);
            }

            StringBuilder content;

            try (var br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            return Integer.parseInt(content.toString());

        } finally {

            con.disconnect();
        }
    }
}