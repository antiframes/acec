package acec.antiframes.carteirinha;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

class RSSHelper {
    private static final String TAG = "RSSHELPER";
    private static final String NEWS_URL = "http://www.turismo.gov.br/ultimas-noticias.feed?type=rss";
    private static  final String MENU_URL = "http://acec-cairu.org.br/app/menu.xml";
    private static final String WEBSVC_URL = "http://acec-cairu.org.br/app/webservice.php";

    static List<MenuItem> getMenuItems(){
        List<MenuItem> menuItems = new ArrayList<>();
        Document xmlDoc = getXMLDocument(MENU_URL);
        if (xmlDoc!=null) {
            Log.d(TAG, xmlDoc.getDocumentElement().getNodeName());
            NodeList items = xmlDoc.getChildNodes().item(1).getChildNodes();

            Node curChild;
            Node curItemChild;
            NodeList itemChilds;
            MenuItem currentMenuItem;
            //ítens do menu
            for (int i=0;i<items.getLength();i++){
                curChild = items.item(i);
                if (curChild.getNodeName().equalsIgnoreCase("item")){
                    itemChilds = curChild.getChildNodes();
                    //informações do item
                    currentMenuItem = new MenuItem();
                    for (int j=0;j<itemChilds.getLength();j++) {
                        curItemChild= itemChilds.item(j);
                        if (curItemChild.getNodeName().equalsIgnoreCase("title"))
                            currentMenuItem.setTitle(curItemChild.getTextContent());
                        if (curItemChild.getNodeName().equalsIgnoreCase("link"))
                            currentMenuItem.setUrl(curItemChild.getTextContent());
                    }
                    if (currentMenuItem.getUrl()!=null)
                        menuItems.add(currentMenuItem);

                }
            }
            return menuItems;
        }
        return null;
    }

    static List<NewsItem> getNews(){
        List<NewsItem> newsItems = new ArrayList<>();
        Document xmlDoc = getXMLDocument(NEWS_URL);
        if (xmlDoc!=null) {
            NodeList items = xmlDoc.getChildNodes().item(1).getChildNodes().item(1).getChildNodes();

            Node curChild;
            Node curItemChild;
            NodeList itemChilds;
            NewsItem currentNewsItem;
            for (int i=0;i<items.getLength();i++) {
                curChild = items.item(i);
                if (curChild.getNodeName().equalsIgnoreCase("item")){
                    itemChilds = curChild.getChildNodes();
                    //informações do item
                    currentNewsItem = new NewsItem();
                    for (int j=0;j<itemChilds.getLength();j++) {
                        curItemChild= itemChilds.item(j);
                        if (curItemChild.getNodeName().equalsIgnoreCase("title"))
                            currentNewsItem.setTitle(curItemChild.getTextContent());
                        if (curItemChild.getNodeName().equalsIgnoreCase("link"))
                            currentNewsItem.setUrl(curItemChild.getTextContent());
                    }
                    if (currentNewsItem.getUrl()!=null)
                        newsItems.add(currentNewsItem);

                }
            }
            return newsItems;
        }
        return null;
    }


    private static Document getXMLDocument(String xmlUrl){
        try {
            URL url = new URL(xmlUrl);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            return builder.parse(inputStream);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static User getUserFromWebservice(String cpf, String pass, final CardActivity activity){
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(WEBSVC_URL);





            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("cpf", cpf));
            nameValuePairs.add(new BasicNameValuePair("senha", pass));
            nameValuePairs.add(new BasicNameValuePair("versao", "1.1"));
            nameValuePairs.add(new BasicNameValuePair("entidade", "1"));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpClient.execute(httpPost, new ResponseHandler<Object>() {

                @Override
                public Object handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                    Log.d(TAG, "handleResponse: "+httpResponse.toString());
                    HttpEntity entity=httpResponse.getEntity();
                    InputStream inputStream = entity.getContent();
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    try {
                        DocumentBuilder builder = builderFactory.newDocumentBuilder();
                        Document xmlDoc = builder.parse(inputStream);
                        parseXmlresponse(xmlDoc, new WeakReference<>(activity));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    return null;
                }
            });




        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void parseXmlresponse(Document document, final WeakReference<CardActivity> activity){
        if (document==null)
            return;
        NodeList items = document.getChildNodes().item(1).getChildNodes();

        Node curChild;
        Node curItemChild;
        NodeList itemChilds;
        NewsItem currentNewsItem;
        final User user=new User();


        for (int i=0;i<items.getLength();i++) {
            curChild = items.item(i);
            Log.d(TAG, "parseXmlresponse: "+curChild.getNodeName()+" "+curChild.getTextContent());

            if (curChild.getNodeName().equalsIgnoreCase("nome"))
                user.setName(curChild.getTextContent());

            if (curChild.getNodeName().equalsIgnoreCase("cnpj"))
                user.setCnpj(curChild.getTextContent());

            if (curChild.getNodeName().equalsIgnoreCase("empresa"))
                user.setCompany(curChild.getTextContent());

            if (curChild.getNodeName().equalsIgnoreCase("cpf"))
                user.setCpf(curChild.getTextContent());

            if (curChild.getNodeName().equalsIgnoreCase("validade"))
                user.setDueDate(curChild.getTextContent());

            if (curChild.getNodeName().equalsIgnoreCase("cargo"))
                user.setOccupation(curChild.getTextContent());

            if (curChild.getNodeName().equalsIgnoreCase("img"))
                user.setPicUrl(curChild.getTextContent());

        }


        activity.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.get().receiveUser(user);
            }
        });
    }
}
