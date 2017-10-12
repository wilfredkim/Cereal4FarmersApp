package com.example.gilbert.myproject;

/**
 * Created by Gilbert on 7/19/2016.
 */
public class Utility {
    private static String SERVER_IP ="http://192.168.43.189";
    private static String SERVER_PORT ="9000";

    public static  String url_news=SERVER_IP+":"+SERVER_PORT+"/backnews";
    public  static  String url_regDetails = SERVER_IP+":"+SERVER_PORT+"/regLogin";
    public static  String url_seed=SERVER_IP+":"+SERVER_PORT+"/getSeed";
    public static  String url_fert=SERVER_IP+":"+SERVER_PORT+"/getFert";
    public static  String url_status=SERVER_IP+":"+SERVER_PORT+"/status";
    public static  String url_inq =SERVER_IP+":"+SERVER_PORT+"/inqreceive";
    public static  String url_pay =SERVER_IP+":"+SERVER_PORT+"/pay";
    public static  String url_payseed =SERVER_IP+":"+SERVER_PORT+"/payseed";
    public static  String url_post_regDetails =SERVER_IP+":"+SERVER_PORT+"/viewFert";
    public static  String  url_seedpost = SERVER_IP+":"+SERVER_PORT+"/viewSeed";
    public  static  String url_existseed = SERVER_IP+":"+SERVER_PORT+"/seedexist";
    public  static  String url_existfert = SERVER_IP+":"+SERVER_PORT+"/fertexist";
    public  static  String url_land = SERVER_IP+":"+SERVER_PORT+"/compareland";
    public  static  String url_getland = SERVER_IP+":"+SERVER_PORT+"/getcompareland";

}
