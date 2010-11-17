//package com.baidu.index.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.Character;
import java.lang.StringBuffer;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.*;

public class Robot{

    private static String URL = "http://index.baidu.com/main/word.php?word=";
    private String keys; // split by comma, eg. "百度" and "百度,指数"
    private String width;
    private String height;
    private String dateStart;
    private String dateEnd;

    private String trendTable;
    private String trendFlash;

    public static void main (String [] args){
        if(args.length <= 0){
            System.out.println("Usage:");
            System.out.println("  Robot key[,word]");
            return;
        }

        String keys = args[0];
        Robot rb = new Robot(keys);
        rb.parse(keys);

        System.out.println(rb.getTrendTable());
        System.out.println();
        System.out.println(rb.getTrendFlash());

    }

    private void parse(String keys){
        try{
            String url = URL + URLEncoder.encode(keys, "gbk");

            String html = httpGET(url);

            trendTable = getTrendTable(html);
            trendFlash = getTrendFlash(keys, html);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    private String getDate(String rel){
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        if(rel == "month"){
            cal.add(Calendar.MONTH, -1);
        }else if(rel == "year"){
            cal.add(Calendar.YEAR, -1);
        }
        return fmt.format(cal.getTime());
    }

    public Robot(String keys){
        this.keys      = keys;
        this.width     = "501";
        this.height    = "390";
        this.dateStart = getDate("month");
        this.dateEnd   = getDate("");
    }
    public Robot(String keys, int width, int height){
        this.keys      = keys;
        this.width     = String.valueOf(width);
        this.height    = String.valueOf(height);
        this.dateStart = getDate("month");
        this.dateEnd   = getDate("");
    }
    public Robot(String keys, String dateStart, String dateEnd){
        this.keys      = keys;
        this.width     = "501";
        this.height    = "390";
        this.dateStart = dateStart;
        this.dateEnd   = dateEnd;
    }
    public Robot(String keys, String dateStart, String dateEnd, int width, int height){
        this.keys      = keys;
        this.width     = String.valueOf(width);
        this.height    = String.valueOf(height);
        this.dateStart = dateStart;
        this.dateEnd   = dateEnd;
    }

    public String getTrendTable(){
        return trendTable;
    }
    private String getTrendTable(String html){
        //Pattern pattern = Pattern.compile("(<table id=\"trend_table_1\" .+?</table>)"); // before 2010/11/17 12:52:04
        Pattern pattern = Pattern.compile("<div class=\"boxContent\"><div class=\"topleft\"></div>\\s*<div class=\"topright\"></div>\\s*(<ul>.+?</ul>)");
        Matcher matcher = pattern.matcher(html);
        //return matcher.find() ? matcher.group(1) : "";  // before 2010/11/17 12:52:04
        return matcher.find() ? "<div id='trend-table'>"+matcher.group(1)+"</div>" : "";
    }

    private String charCode(char c){
        return String.valueOf((int) c);
    }
    private String urlEncode(String aURLFragment){
        String result = null;
        try {
            result = URLEncoder.encode(aURLFragment, "UTF-8");
        }
        catch (UnsupportedEncodingException ex){
            throw new RuntimeException("UTF-8 not supported", ex);
        }
        return result;
    }
    private String urlDecode(String aURLFragment){
        String result = null;
        try {
            result = URLDecoder.decode(aURLFragment, "UTF-8");
        }
        catch (UnsupportedEncodingException ex){
            throw new RuntimeException("UTF-8 not supported", ex);
        }
        return result;
    }
    // @see http://www.blogjava.net/emu/articles/4773.html
    private static String escape (String src){
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length()*6);

        for (i=0; i<src.length(); i++){
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
                tmp.append(j);
            else
                if (j<256) {
                    tmp.append( "%" );
                    if (j<16)
                        tmp.append( "0" );
                    tmp.append( Integer.toString(j,16) );
                } else {
                    tmp.append( "%u" );
                    tmp.append( Integer.toString(j,16) );
                }
        }
        return tmp.toString();
    }

    private String  unescape (String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int  lastPos=0,pos=0;
        char ch;
        while (lastPos<src.length()) {
            pos = src.indexOf("%",lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos+1)=='u') {
                    ch = (char)Integer.parseInt(src.substring(pos+2,pos+6),16);
                    tmp.append(ch);
                    lastPos = pos+6;
                } else {
                    ch = (char)Integer.parseInt(src.substring(pos+1,pos+3),16);
                    tmp.append(ch);
                    lastPos = pos+3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos=src.length();
                } else {
                    tmp.append(src.substring(lastPos,pos));
                    lastPos=pos;
                }
            }
        }
        return tmp.toString();
    }

    private String fromCharCode(int... codePoints) {
        return new String(codePoints, 0, codePoints.length);
    }
    private String asd(String keys, String a, String b){
        String c = keys;
        c = c.replaceAll("(^[,]+|[,]+$)", "");
        String [] d = c.split("[,]|[，]");
        a = escape(a);
        String e = "";
        for (int i = 0; i<b.length(); i++) {
            e += charCode(b.charAt(i));
        }
        String f = a.substring(a.length() - 13, a.length());
        String g = f;
        //F = F ^ 99999999;
        String C = a.substring(0, a.length() - 13);
        a = C;
        e = e + g;
        String h = "";
        for (int i = 0; i < a.length(); i += 2) {
            String j = a.substring(i, i+2);
            int J = Integer.parseInt(j, 16);
            int k = (i / 2) % e.length();
            String l = e.substring(k, k+1);
            int L = Integer.valueOf(l);
            J = J ^ L;
            h += fromCharCode(J);
            //J += d[k];
        }
        return unescape(h);
    }
    private String getTrendFlashKey(String json){
        Pattern pattern = Pattern.compile("\\bkey:\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if(! matcher.find()){
            return "";
        }
        return matcher.group(1);
    }
    public String getTrendFlash(){
        return trendFlash;
    }
    private String getTrendFlash(String keys, String html){
        //Pattern pattern = Pattern.compile("eval\\(asd\\('([^']+)','([^']+)'\\)\\);");  // before 2010/11/17 12:52:04
        Pattern pattern = Pattern.compile("eval\\(Dec\\('([^']+)','([^']+)'\\)\\);");
        Matcher matcher = pattern.matcher(html);
        if(! matcher.find()){
            return "";
        }
        String a = matcher.group(1);
        String b = matcher.group(2);
        String res = asd(keys, a, b);
        // random key form baidu index.
        String key = getTrendFlashKey(res);
        keys = urlEncode(keys);

        String flash = "<object height='"+height+"' width='"+width+"' align='middle' id='trendFlash' "+
                "codebase='http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0' "+
                "classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000'>"+
            "<param value='opaque' name='wmode'>"+
            "<param value='http://index.baidu.com/fla/TrendAnalyser.swf' name='movie'>"+
            "<param value='keys="+keys+"&amp;areas=0&amp;periods="+dateStart+"%7C"+dateEnd+"&amp;periodNames=null&amp;shortKeys="+keys+"&amp;dv=1&amp;gateway=http%3A%2F%2Findex.baidu.com%2Fgateway.php&amp;snapshot=http%3A%2F%2Findex.baidu.com%2Fmain%2Fshow.php&amp;key="+key+"' name='flashvars'>"+
            "<embed height='"+height+"' width='"+width+"' "+
                "align='middle' pluginspage='http://www.macromedia.com/go/getflashplayer' "+
                "type='application/x-shockwave-flash' name='trendFlash' "+
                "src='http://index.baidu.com/fla/TrendAnalyser.swf' "+
                "flashvars='keys="+keys+"&areas=0&periods="+dateStart+"%7C"+dateEnd+"&periodNames=null&shortKeys="+keys+
                    "&dv=1&gateway=http%3A%2F%2Findex.baidu.com%2Fgateway.php&snapshot=http%3A%2F%2Findex.baidu.com%2Fmain%2Fshow.php&key="+key+"' "+
                "wmode='opaque' bgcolor='#FFFFFF' errormessage='Please download the newest flash player.' ver='9.0.0'>"+
            "</object>";
        return flash;
    }

    private String httpGET(String uri){
        String s = "";
        try{
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();

            BufferedReader is = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer buffer = new StringBuffer();
            String str;
            while((str = is.readLine()) != null){
                buffer.append(str);
                //buffer.append("\n");
            }
            s = buffer.toString();
            is.close();

            //str = buffer.toString().replaceAll("<script(.|\n)+?</script>", "").replaceAll("<(.|\n)+?>", "").replaceAll("&nbsp;", " ");
            //str = buffer.toString();
            //String[] s = str.split("\n");
            //buffer = new StringBuffer();
            //for(int i=0;i<s.length;i++){
                //if(s[i].trim().equals("") ){
                    //continue;
                //}else{
                    //buffer.append(s[i]);
                    //buffer.append("\n");
                //}
            //}


        }catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
