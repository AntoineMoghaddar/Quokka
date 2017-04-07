package Design;


//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonParser;
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.JsonNode;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.exceptions.UnirestException;
//import com.mashape.unirest.request.HttpRequest;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.security.*;
import java.text.*;
import java.util.*;

/**
 * @author Moose
 * @organization CreativeICT
 * @project CVVSHOP - Openshop.io-connect
 * @description The json requester for CCVShop
 */

public class Requester {

    /**
     * @definition The public key given by the customer
     * @use This key will be used to get an interaction between APP and API
     * @testData PUBLIC_KEY = "19omenl18yzwuygb"
     */
    private static String PUBLIC_KEY;

    /**
     * @definition The Secret key will serve as a password between the client and the API
     * @use The key will be used to validate and receive access to the API
     * @testData SECRET_KEY = "jjdak38fxcvffgu6f9ijmtbli073pe4y"
     */
    private static String SECRET_KEY;

    /**
     * @definition The URI on which the so called action will be taken place
     * @-> the validation of what the customer wants
     * @use Will serve as an extension to the request URL
     */
    private static String URI;

    /**
     * @definition Timestamp is needed in order to get the correct hash (SHA512)
     * @-> Timestamp is second specific
     * @method Timestamp will be determined according to the UTC Timeframe
     * @use Timestamp will be set up according to a solid interface needed for the hashcode
     * @-> The timestamp will only be used in a part of the HASH code
     */
    private static String TIMESTAMP;

    /**
     * @definition The entire call URL String
     * @use Trigger-calling the API
     * @included This caller won't do much without headers
     * @-> [x-date] [x-public] [x-hash]
     */
    private static String URLString;

    /**
     * @definition The data that is submitted with the request.
     * @frequency Only used in post, patch requests.
     * @use Sends extra data to the resource
     */
    private static String DATA;

    /**
     * @definition Method used for performing the correct task
     * @description HTTP method in uppercase (ie: GET, POST, PATCH, DELETE)
     * @use saves the action method required by the client's use
     */
    private RequestMethod method;

    /**
     * @definition main (constructor) constructor for this class
     * @description requires empty constructor
     * @consider Singleton implementation
     * @requires manually added data
     */
    public Requester() {
    }

    /**
     * @param public_key;    public API key given by the client
     * @param secret_key;    secret API key given by the client
     * @param valid_API_URL; API URL needed for making a call
     * @param URI;           URI method needed for performing correct action
     * @param data;          not used in this implementation, only needed in 'POST'
     * @definition main used constructor for this class
     * @frequency Only uses 'GET' Method
     * @consider Singleton implementation
     */
    public Requester(String public_key, String secret_key, String valid_API_URL, String URI, String data) {
        PUBLIC_KEY = public_key;
        SECRET_KEY = secret_key;
        TIMESTAMP = setTIMESTAMP();
        URLString = valid_API_URL;
        Requester.URI = URI;
        method = RequestMethod.GET;
        DATA = (data != null) ? data : "";
    }

    /**
     * @return specific timestamp minus one hour
     * @--> dateFormat.format(date) + "T" + (timeFormat.format(cal.getTime())) + "Z"
     * @except This API requires a timestamp which is minus one hour since it's located in another timezone
     * @definition defines the current time and rewrites that timestamp matching the required format
     * @description returns a milisecond specific timestamp formatted according the restrictions of CCVShop
     * @TODO Reformatting code / optimalizing code
     * @consider using Time() implementation
     */
    private String setTIMESTAMP() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);

        //Debug
        Logger.console(dateFormat.format(date) + "T" + timeFormat.format(cal.getTime()) + "Z"); //2016/11/16 12:08:43
        return dateFormat.format(date) + "T" + (timeFormat.format(cal.getTime())) + "Z";
    }

    /**
     * @return Unhashed combination without XORed variant
     * @definition The hash combination required for making a call on the CCVShop API
     * @description Creates a valid hash combination(unhashed)and returns this
     * @use After creating this combination the return result will be XOR-hashed with the provided secret key
     * @--> This resulting has will be send as a request to the API
     */
    public String getPassword() {
        return PUBLIC_KEY + "|" + method.toString() + "|" + URI + "|" + DATA + "|" + TIMESTAMP;
    }

    /**
     * @param passwordToHash; password combination created in the getPassword() method
     * @return generatedPassword; the generated password required by the API
     * @throws UnsupportedEncodingException
     * @definition SHA512 encoder
     * @--> XORes and hashes the provided password combination created in getPassword();
     * @description XORes and Hashes the full API password provided by another method with SHA512
     * @--> and the provided secret key by the client
     * @use Won't be used since this method is not capable of XORing the hash correctly
     * @see SHA512_encoder for an improved version of this method
     */
    @Deprecated
    @Ignore
    //Not needed since this one does not XOR's the hash with other String values.
    public String SHA512_encoder(String passwordToHash) throws UnsupportedEncodingException {
        Logger.confirm(passwordToHash);

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Logger.debug(generatedPassword);
        return generatedPassword;
    }


    /**
     * @param value;     password combination unXORed with the hascode (created by getPassword())
     * @param secretkey; the secret key provided by the client
     * @return returns a perfectly fine API hash which is ready to be send as a header request
     * @definition The used encoder for creating the required hash by the CCVShop API
     * @description XORes and Hashes the full API password provided by another method with SHA512
     * @--> and the provided secret key by the client
     * @use XORes the provided password combination (created in getPassword()) with the secret key
     * @--> and hashes it all into one SHA512 encoded String
     */
    String SHA512_encoder(String value, String secretkey) {
        try {
            // Get an hmac_sha1 secretkey from the raw secretkey bytes
            byte[] keyBytes = secretkey.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA512");

            // Get an hmac_sha512 Mac instance and initialize with the signing secretkey
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);

            //  Covert array of Hex bytes to a String
            Logger.debug(new String(hexBytes, "UTF-8"));
            return new String(hexBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


//    /**
//     * Sends a HTTP request to the API
//     * Receives JSON formatted file
//     */
//    void request() {
//
//        try {
//            HttpRequest request = Unirest.get(URLString)
//                    .header("x-public", getPublicKey())
//                    .header("x-date", getTIMESTAMP())
//                    .header("x-hash", SHA512_encoder(getPassword(), getSecretKey()));
//
//            HttpResponse<JsonNode> jsonNodeHttpResponse = null;
//            try {
//                jsonNodeHttpResponse = request.asJson();
//            } catch (UnirestException e) {
//                e.printStackTrace();
//            }
//            processRequest(jsonNodeHttpResponse);
//            Unirest.shutdown();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * @throws Exception
     */
    public void requester() throws Exception {


        URL obj = new URL(URLString);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod(RequestMethod.GET.toString());
        con.setRequestProperty("x-public", getPublicKey());
        con.setRequestProperty("x-date", getTIMESTAMP());
        con.setRequestProperty("x-hash", SHA512_encoder(getPassword(), getSecretKey()));

        int responseCode = con.getResponseCode();
        Logger.debug("\nSending 'GET' request to URL : " + URLString
                + "\nResponse code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
        );
        String inputLine;
        StringBuffer resp = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            resp.append(inputLine);
        }
        in.close();

        Logger.notice(resp.toString());
        (new JSONProcessing()).process_json(resp.toString());

    }
//
//    private void processRequest(HttpResponse<JsonNode> jsonNodeHttpResponse) {
//        assert jsonNodeHttpResponse != null;
//        String responseJSONString = jsonNodeHttpResponse.getBody().toString();
//        Logger.notice(new GsonBuilder()
//                .setPrettyPrinting()
//                .create()
//                .toJson(new JsonParser()
//                        .parse(responseJSONString)));
//        (new JSONProcessing()).process_json(responseJSONString);
//    }


    /**
     * @param publicKey
     */
    public static void setPublicKey(String publicKey) {
        PUBLIC_KEY = publicKey;
    }

    /**
     * @param secretKey
     */
    public static void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    /**
     * @param URI
     */
    public static void setURI(String URI) {
        Requester.URI = URI;
    }

    /**
     * @param DATA
     */
    public static void setDATA(String DATA) {
        Requester.DATA = DATA;
    }

    /**
     * @param method
     */
    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    /**
     * @return
     */
    public static String getPublicKey() {
        return PUBLIC_KEY;
    }

    /**
     * @return
     */
    public static String getSecretKey() {
        return SECRET_KEY;
    }

    /**
     * @return
     */
    public static String getURI() {
        return URI;
    }

    /**
     * @return
     */
    public static String getTIMESTAMP() {
        return TIMESTAMP;
    }

    /**
     * @return
     */
    public static String getURLString() {
        return URLString;
    }

    /**
     * @return
     */
    public static String getDATA() {
        return DATA;
    }

    /**
     * @return
     */
    public RequestMethod getMethod() {
        return method;
    }

}
