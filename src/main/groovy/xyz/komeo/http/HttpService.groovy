package xyz.komeo.http

class HttpService {

    static final FIXIE_URL_ENV = 'FIXIE_URL'

    private String fixieUser
    private String fixiePassword
    private String fixieHost
    private int fixiePort

    String getWebsiteContent(String targetUrl) {
        BufferedReader br
        try {
            URL url = new URL(targetUrl)
            URLConnection connection = isProxyEmabled()
                    ? url.openConnection(createProxy())
                    : url.openConnection()

            br = new BufferedReader(new InputStreamReader(connection.getInputStream()))

            StringBuffer sb = new StringBuffer()
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine)
            }

            return sb.toString()
        } finally {
            if (br != null) {
                br.close()
            }
        }
    }

    private Proxy createProxy() {
        new Proxy(Proxy.Type.HTTP, new InetSocketAddress(fixieHost, fixiePort));
    }

    private void setProxyAuthentication() {
        Authenticator authenticator = new Authenticator() {
            PasswordAuthentication getPasswordAuthentication() {
                return (new PasswordAuthentication(fixieUser, fixiePassword.toCharArray()))
            }
        }
        Authenticator.setDefault(authenticator);
    }

    private void initializeProxy() {
        String fixieUrl = System.getenv(FIXIE_URL_ENV)

        String[] fixieValues = fixieUrl.split("[/(:\\/@)/]+");
        fixieUser = fixieValues[1];
        fixiePassword = fixieValues[2];
        fixieHost = fixieValues[3];
        fixiePort = Integer.parseInt(fixieValues[4]);
    }

    static private boolean isProxyEmabled() {
        System.getenv(FIXIE_URL_ENV)
    }

    void init() {
        if (isProxyEmabled()) {
            initializeProxy()
            setProxyAuthentication()
        }
    }
}
