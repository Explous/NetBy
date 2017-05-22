package com.byteli.netby;

/**
 * Created by byte on 5/30/16.
 */
public class KeyNetBy {
    //client and server
    public static final char HEAD_APP = '0';
    public static final char APP_MSG = '0';
    public static final char APP_INVITE = '1';
    public static final char APP_CHECK = '2';
    public static final char APP_START = '3';

    public static final char HEAD_GUY = '1';
    public static final char GUY_NEW = '0';
    public static final char GUY_DELETE = '1';
    public static final char TRUE = '0';
    public static final char FALSE = '1';

    //handler
    public static final char UPDATE_GUYS = 'a';
    public static final char UPDATE_GUYID = 'b';
    public static final char UPDATE_GUYCOUNT = 'c';
    public static final char CLEAR_STATUSPAGER = 'd';

    public static final int ACTION_DELETE = 0;
    public static final char MARK = '/';//the space mark global used in this app

    public static final int BUFFER_SIZE = 1024;//for the file transimission buffer

}
