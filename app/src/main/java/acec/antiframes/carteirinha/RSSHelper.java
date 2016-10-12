package acec.antiframes.carteirinha;

import android.util.Log;
import android.view.Menu;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class RSSHelper {
    private static final String TAG = "RSSHELPER";
    private static final String NEWS_URL = "http://www.turismo.gov.br/ultimas-noticias.feed?type=rss";
    private static  final String MENU_URL = "http://acec-cairu.org.br/app/menu.xml";

    public static List<MenuItem> getMenuItems(){
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

    public static List<MenuItem> getNews(){
        List<MenuItem> newsItems = new ArrayList<>();
        Document xmlDoc = getXMLDocument(NEWS_URL);
        if (xmlDoc!=null) {
            NodeList items = xmlDoc.getChildNodes().item(1).getChildNodes().item(1).getChildNodes();

            Node curChild;
            Node curItemChild;
            NodeList itemChilds;
            MenuItem currentNewsItem;
            for (int i=0;i<items.getLength();i++) {
                curChild = items.item(i);
                if (curChild.getNodeName().equalsIgnoreCase("item")){
                    itemChilds = curChild.getChildNodes();
                    //informações do item
                    currentNewsItem = new MenuItem();
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
}
