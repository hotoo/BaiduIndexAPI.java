
class Test{
    public static void main(String[] args) {

        String url = "http://www.liuzm.com";
        String keyword = "��־�Ͳ���";
        createhttpClient p = new createhttpClient();
        String response = p.createhttpClient(url, keyword); // ��һ�ַ���
        // p.getPageContent(url, "post", 100500);//�ڶ��ַ���
    }

    public String getPageContent(String strUrl, String strPostRequest,
            int maxLength) {
        // ��ȡ�����ҳ
        StringBuffer buffer = new StringBuffer();
        System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
        System.setProperty("sun.net.client.defaultReadTimeout", "5000");
        try {
            URL newUrl = new URL(strUrl);
            HttpURLConnection hConnect = (HttpURLConnection) newUrl
                    .openConnection();
            // POST��ʽ�Ķ�������
            if (strPostRequest.length() > 0) {
                hConnect.setDoOutput(true);
                OutputStreamWriter out = new OutputStreamWriter(hConnect
                        .getOutputStream());
                out.write(strPostRequest);
                out.flush();
                out.close();
            }
            // ��ȡ����

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    hConnect.getInputStream()));
            int ch;
            for (int length = 0; (ch = rd.read()) > -1
                    && (maxLength <= 0 || length < maxLength); length++)
                buffer.append((char) ch);
            String s = buffer.toString();
            s.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
            System.out.println(s);

            rd.close();
            hConnect.disconnect();
            return buffer.toString().trim();
        } catch (Exception e) {
            // return "����:��ȡ��ҳʧ�ܣ�";
            //
            return null;


        }
    }

}
