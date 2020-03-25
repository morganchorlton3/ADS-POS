package sample;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.json.*;

public class Controller implements Initializable {

    private static HttpURLConnection con;

    @FXML
    TextField barcodeTXT;

    @FXML
    public Label priceLabel, countLabel;

    @FXML
    public Button cardBTN, cashBTN, backBTN ;

    public Cart cart =  new Cart(0,0.0);

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public void initialize(URL arg0, ResourceBundle arg1) {// Initializes
        // everything
        //barcodeTXT.setVisible(false);
        cashBTN.setFocusTraversable(false);
        cardBTN.setFocusTraversable(false);

        barcodeTXT.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode().toString().equalsIgnoreCase("TAB")) {
                    try{
                        JSONObject product = getProduct(barcodeTXT.getText());
                        if(!product.getString("name").equals("error")) {
                            Double itemPrice = product.getDouble("price");
                            cart.setTotal(cart.getTotal() + itemPrice);
                            cart.setCount(cart.getCount() + 1);
                            ArrayList<String> items = cart.getItems();
                            items.add(product.getString("name") + "       " + product.getDouble("price"));
                            cart.setItems(items);
                            String cartTotalString = "£" + df2.format(cart.getTotal());
                            priceLabel.setText(cartTotalString);
                            countLabel.setText(String.valueOf(cart.getCount()));

                        }else{
                            AlertHelper.itemNotFound();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    barcodeTXT.clear();
                }
            }
        });
    }

    @FXML
    private void cashHandle(ActionEvent event) throws IOException {
        cashPayment(cart);
    }

    @FXML
    private void cardHandle(ActionEvent event) throws IOException {
        cardPayment(cart);
    }

   /* public Double getPrice(String barcode) throws IOException {
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

            return Double.parseDouble(content.toString());

        }catch (IOException e){
            AlertHelper.itemNotFound();
            System.out.println("No Item Found");
            return 0.0;
        } finally {

            con.disconnect();
        }
    }*/

    public JSONObject getProduct(String barcode) throws IOException {
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
            return new JSONObject(content.toString());

        }catch (IOException e){
            AlertHelper.itemNotFound();
            System.out.println("No Item Found");
            e.printStackTrace();
        } finally {

            con.disconnect();
        }
        return null;
    }

    public void cashPayment(Cart cart){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cash Draw");
        alert.setHeaderText("Cash Draw Open");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeFive = new ButtonType("5");
        ButtonType buttonTypeTen = new ButtonType("10");
        ButtonType buttonTypeTwenty = new ButtonType("20");
        ButtonType buttonTypeFithty = new ButtonType("50");

        alert.getButtonTypes().setAll(buttonTypeFive, buttonTypeTen, buttonTypeTwenty, buttonTypeFithty);

        Optional<ButtonType> result = alert.showAndWait();
        double change = 0;
        if (result.get().equals(buttonTypeFive)){
            change = 5 - cart.getTotal();
        } else if (result.get().equals(buttonTypeTen)) {
            change = 10 - cart.getTotal();
        } else if (result.get().equals(buttonTypeTwenty)) {
            change = 20 -cart.getTotal();
        } else if (result.get().equals(buttonTypeFithty))  {
            change = 50 - cart.getTotal();
        }
        System.out.println("Total: " + cart.getTotal());
        System.out.println("Change: " + change);
        alert.close();
        if(change >= 0) {
            Alert finalAlert = new Alert(Alert.AlertType.INFORMATION);
            finalAlert.setTitle("Change");
            finalAlert.setHeaderText(null);
            finalAlert.setContentText("Change To Give: £" + df2.format(change));
            finalAlert.showAndWait();
            reset(cart);
        }else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error Dialog");
            errorAlert.setHeaderText("Not Enough paid");
            errorAlert.setContentText("Not enough Paid");

            errorAlert.showAndWait();
            cashPayment(cart);
        }

    }
    public void cardPayment(Cart cart){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Card Payment");
        alert.setHeaderText(null);
        alert.setContentText("Continue On Card Machine");
        alert.showAndWait();
        cart.setOrderCount(cart.getOrderCount() +1);
        reset(cart);
    }

    private void reset(Cart cart){
        cart.setTotal(0.0);
        cart.setCount(0);
        priceLabel.setText("£0.00");
        countLabel.setText("0");
    }
}